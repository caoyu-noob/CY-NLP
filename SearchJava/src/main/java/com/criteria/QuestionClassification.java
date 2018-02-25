package com.criteria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.constants.THULACCate;
import com.criteria.templates.Event;
import com.criteria.templates.EventIntroduction;
import com.criteria.templates.PersonIntroduction;
import com.criteria.templates.TemplateMatcher;

import io.github.yizhiru.thulac4j.model.SegItem;
import org.apache.jena.query.QuerySolution;
import org.springframework.util.CollectionUtils;

/**
 * Created by cao_y on 2018/1/24.
 * design a criteria to match different question type
 */
public class QuestionClassification {

    private final PersonIntroduction.templates personIntroduction = new PersonIntroduction.templates();
    private final Event.templates event = new Event.templates();
    private final EventIntroduction.templates eventIntroduction = new EventIntroduction.templates();

    public enum QuestionType {
        // the subject of the question is location, e.g. XX战役在哪发生的, XX是哪里人
        WHERE,
        // the subject of the question is time, e.g. XX之战什么时候发生的，XX什么时候出生的
        WHEN,
        // the subject of the question is person, e.g. XX之战有谁参与
        WHO,
        // the subject of the question is event, e.g. XX参加了什么事件
        WHAT,
        // the subject is person introduction, e.g. XX是谁（该问题回答方式会将实体的所有相关属性一一列出）
        PERSON_INRODUCTION,
        // the subject is event introduction, e.g. 介绍下XX事件 （该问题回答方式会将实体的所有相关属性一一列出）
        EVENT_INTRODUCTION,
        // the subject is some other property, e.g. XX事件的影响， XX的官职
        OTHER,
        // cannot understand the question at the first stage
        UNKNOW;

    }

    public enum KeyWordForWhere {

    }

    public enum RoughKeyWord {
        WHEREKEY1("哪","r"),
        WHEREKEY2("哪里","r"),
        WHOKEY1("谁","r");

        private SegItem segItem;

        private RoughKeyWord(String word, String type) {
            this.segItem = new SegItem(word, type);
        }

        private SegItem getSegItem() {
            return this.segItem;
        }
    }

    public enum UncertainPron {
        U1("什么", "r"),
        U2("哪些","r"),
        U3("啥","r");

        private SegItem segItem;

        private UncertainPron(String word, String type) {
            this.segItem = new SegItem(word, type);
        }

        private SegItem getSegItem() {
            return this.segItem;
        }
    }

    public enum CertainNoun {
        WHEN1("时候", "n"),
        WHEN2("时间", "n"),
        WHO1("人","n"),
        WHO2("人物","n"),
        WHO3("将领","n"),
        WHO4("参与者","n"),
        WHERE1("地方","n"),
        WHERE2("地点","n"),
        WHERE3("位置","n");

        private SegItem segItem;

        private CertainNoun(String word, String type) {
            this.segItem = new SegItem(word, type);
        }

        private SegItem getSegItem() {
            return this.segItem;
        }
    }

    //Determine the question type at the first stage
    public QuestionType classifyQuestion(List<SegItem> segItems) {
        if (isForPersonIntroduction(segItems)) {
            return QuestionType.PERSON_INRODUCTION;
        }
        replaceEventSegItem(segItems);
        if (isForEventIntroduction(segItems)) {
            return QuestionType.EVENT_INTRODUCTION;
        }
        Set<SegItem> segItemsSet = new HashSet<>();
        segItemsSet.addAll(segItems);
        Map<Integer, SegItem> posSegItemMap = toPosSegItemMap(segItems);
        Map<SegItem, Integer> segItemPosMap = toSegItemPosMap(segItems);
        return roughClassify(segItemPosMap, posSegItemMap);
    }

    private QuestionType roughClassify(Map<SegItem, Integer> segItemPosMap, Map<Integer, SegItem> posSegItemMap) {
        if (segItemPosMap.containsKey(RoughKeyWord.WHEREKEY2.getSegItem()) ||
                segItemPosMap.containsKey(RoughKeyWord.WHEREKEY1.getSegItem())) {
            return QuestionType.WHERE;
        }
        if (segItemPosMap.containsKey(RoughKeyWord.WHOKEY1.getSegItem())) {
            return QuestionType.WHO;
        }
        for (UncertainPron uPron : UncertainPron.values()) {
            if (segItemPosMap.containsKey(uPron.getSegItem())) {
                if (segItemPosMap.containsKey(CertainNoun.WHEN1.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHEN2.getSegItem())) {
                    return QuestionType.WHEN;
                }
                if (segItemPosMap.containsKey(CertainNoun.WHERE1.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHERE2.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHERE3.getSegItem())) {
                    return QuestionType.WHERE;
                }
                if (segItemPosMap.containsKey(CertainNoun.WHO1.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHO2.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHO3.getSegItem()) ||
                        segItemPosMap.containsKey(CertainNoun.WHO4.getSegItem())) {
                    return QuestionType.WHO;
                }
            }
        }
        return QuestionType.UNKNOW;
    }

    private Map<Integer, SegItem> toPosSegItemMap(List<SegItem> segItems) {
        Map<Integer, SegItem> result = new HashMap<>();
        for (int i = 0; i < segItems.size(); i++) {
            result.put(i, segItems.get(i));
        }
        return result;
    }

    private Map<SegItem, Integer> toSegItemPosMap(List<SegItem> segItems) {
        Map<SegItem, Integer> result = new HashMap<>();
        for (int i = 0; i < segItems.size(); i++) {
            result.put(segItems.get(i), i);
        }
        return result;
    }

    //determine if the current question is for the introduction of a person
    public boolean isForPersonIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, personIntroduction);
    }

    //determine if the current question is for the introduction of a Sanguo event
    public boolean isForEventIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, eventIntroduction);
    }

    //replace the original segItems which is a event word with a new single segItem
    //no replacement will happen if there is no event word in segItems
    //e.g. 赤壁(地名)/之(介词)/战(动词) will become 赤壁之战(三国事件)
    private void replaceEventSegItem(List<SegItem> segItems) {
        List<Integer> eventStartAndEndIndex = TemplateMatcher.MatchEvent(segItems, event);
        if (!CollectionUtils.isEmpty(eventStartAndEndIndex)) {
            int removeCnt = 0;
            for (int i = 0; i < eventStartAndEndIndex.size(); i+=2) {
                int start = eventStartAndEndIndex.get(i) - removeCnt;
                int end = eventStartAndEndIndex.get(i+1) - removeCnt;
                String eventName = "";
                for (int j = start; j < end; j++) {
                    eventName += segItems.get(start).word;
                    segItems.remove(start);
                    removeCnt++;
                }
                segItems.add(start, new SegItem(eventName, THULACCate.SANGUO_EVENT.getValue()));
                removeCnt--;
            }
        }
    }
}
