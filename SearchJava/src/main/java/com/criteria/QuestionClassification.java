package com.criteria;

import java.util.*;
import java.util.stream.Collectors;

import com.constants.THULACCate;
import com.criteria.templates.*;
import com.entity.GetAnswerEntity;
import com.entity.QuestionType;

import io.github.yizhiru.thulac4j.model.SegItem;
import org.apache.jena.query.QuerySolution;
import org.springframework.util.CollectionUtils;

/**
 * Created by cao_y on 2018/1/24.
 * design a criteria to match different question type
 */
public class QuestionClassification {

    private static final PersonIntroduction.templates personIntroduction = new PersonIntroduction.templates();
    private static final Event.templates event = new Event.templates();
    private static final EventIntroduction.templates eventIntroduction = new EventIntroduction.templates();
    private static final Where.templates where = new Where.templates();
    private static final WhereCheck.templates whereCheck = new WhereCheck.templates();
    private static final When.templates when = new When.templates();
    private static final WhenCheck.templates whenCheck = new WhenCheck.templates();


    //Determine the question type at the first stage
    public GetAnswerEntity classifyQuestion(List<SegItem> segItems) {
        replaceEventSegItem(segItems);
        //determine if question is for where
        int whenPos = getPosForWhenQuestion(segItems);
        if (whenPos >= 0) {
            SegItem targetItem = checkValidityOfWhenQuestion(segItems);
            if (targetItem != null) {
                return new GetAnswerEntity(QuestionType.WHEN, targetItem.pos, Arrays.asList(targetItem.word), null);
            }
            return GetAnswerEntity.getUnkownEntity();
        }
        //determine if question is for where
        int wherePos = getPosForWhereQuestion(segItems);
        if (wherePos >= 0) {
            SegItem targetItem = chechValidityOfWhereQuestion(segItems);
            if (targetItem != null) {
                return new GetAnswerEntity(QuestionType.WHERE, targetItem.pos, Arrays.asList(targetItem.word), null);
            }
            return GetAnswerEntity.getUnkownEntity();
        }
        //determine if question is for person introduction
        if (isForPersonIntroduction(segItems)) {
            List<String> personNames = findWordsForGivenProperty(segItems, THULACCate.PERSON);
            return new GetAnswerEntity(QuestionType.PERSON_INTRODUCTION, THULACCate.PERSON.getValue(), personNames, null);
        }
        //determine if question is for event introduction
        if (isForEventIntroduction(segItems)) {
            List<String> eventNames = findWordsForGivenProperty(segItems, THULACCate.SANGUO_EVENT);
            return new GetAnswerEntity(QuestionType.EVENT_INTRODUCTION, THULACCate.PERSON.getValue(), eventNames, null);
        }
        Set<SegItem> segItemsSet = new HashSet<>();
        segItemsSet.addAll(segItems);
        Map<Integer, SegItem> posSegItemMap = toPosSegItemMap(segItems);
        Map<SegItem, Integer> segItemPosMap = toSegItemPosMap(segItems);
//        return roughClassify(segItemPosMap, posSegItemMap);
        return GetAnswerEntity.getUnkownEntity();
    }

//    private QuestionType roughClassify(Map<SegItem, Integer> segItemPosMap, Map<Integer, SegItem> posSegItemMap) {
//        if (segItemPosMap.containsKey(RoughKeyWord.WHEREKEY2.getSegItem()) ||
//                segItemPosMap.containsKey(RoughKeyWord.WHEREKEY1.getSegItem())) {
//            return QuestionType.WHERE;
//        }
//        if (segItemPosMap.containsKey(RoughKeyWord.WHOKEY1.getSegItem())) {
//            return QuestionType.WHO;
//        }
//        for (UncertainPron uPron : UncertainPron.values()) {
//            if (segItemPosMap.containsKey(uPron.getSegItem())) {
//                if (segItemPosMap.containsKey(CertainNoun.WHEN1.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHEN2.getSegItem())) {
//                    return QuestionType.WHEN;
//                }
//                if (segItemPosMap.containsKey(CertainNoun.WHERE1.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHERE2.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHERE3.getSegItem())) {
//                    return QuestionType.WHERE;
//                }
//                if (segItemPosMap.containsKey(CertainNoun.WHO1.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHO2.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHO3.getSegItem()) ||
//                        segItemPosMap.containsKey(CertainNoun.WHO4.getSegItem())) {
//                    return QuestionType.WHO;
//                }
//            }
//        }
//        return QuestionType.UNKNOW;
//    }

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
    private boolean isForPersonIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, personIntroduction, true);
    }

    //determine if the current question is for the introduction of a Sanguo event
    private boolean isForEventIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, eventIntroduction, true);
    }

    private int getPosForWhereQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, where, false);
    }

    private int getPosForWhenQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, when, false);
    }

    private SegItem chechValidityOfWhereQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whereCheck, false)) {
            return WhereCheck.checkTargetType(segItems);
        }
        return null;
    }

    private SegItem checkValidityOfWhenQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whenCheck, false)) {
            return WhenCheck.checkTargetType(segItems);
        }
        return null;
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

    private List<String> findWordsForGivenProperty(List<SegItem> segItems, THULACCate property) {
        return segItems.stream()
                .filter(segItem -> segItem.pos.equals(property.getValue()))
                .map(segItem -> segItem.word)
                .collect(Collectors.toList());
    }

    private List<String> findWordsForGivenProperty(List<SegItem> segItems, THULACCate property, int start, int end) {
        List<String> res = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (segItems.get(i).pos.equals(property.getValue())) {
                res.add(segItems.get(i).word);
            }
        }
        return res;
    }
}
