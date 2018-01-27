package com.criteria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/1/24.
 * design a criteria to match different question type
 */
public class QuestionClassification {

    public enum QuestionType {
        WHERE,
        WHEN,
        WHO,
        UNKNOW;

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

    public QuestionType classifyQuestion(List<SegItem> segItems) {
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
}
