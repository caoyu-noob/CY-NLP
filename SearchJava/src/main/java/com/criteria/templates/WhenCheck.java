package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.constants.THULACCate;
import javafx.util.Pair;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/27.
 */
public class WhenCheck {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 人名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PERSON.getValue()),
                        new SegItem("(出生|诞生|死亡|逝世|病逝|离世|去世|是)", THULACCate.VERB.getValue())
                ));
                //只有事件名
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue())
                ));
                //contains 事件名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue()),
                        new SegItem("(发生|进行|爆发|是)", THULACCate.VERB.getValue())
                ));
                //contains 合适的动词+事件名
                add(Arrays.asList(
                        new SegItem("(发生|进行|爆发)", THULACCate.VERB.getValue()),
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue())
                ));
                //contains 事件名+合适的介词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue()),
                        new SegItem("(在)", THULACCate.PREPOSTION.getValue())
                ));
            }
        };

        // provide the matching mode foreach template
        // 3 = regex and word property matching mode, 2 = only regex matching mode, 1 = only word property matching mode
        private final static List<Integer> matchMode = new ArrayList<Integer>() {
            {
                add(3);
                add(1);
                add(3);
                add(3);
                add(3);
            }
        };

        //the error limits for each template
        // matching errors more than the limitations will be regarded as a failure matching, -1 means no limitation
        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
                add(-1);
                add(-1);
                add(-1);
                add(-1);
                add(0);
            }
        };

        @Override
        public List<List<SegItem>> getTemplate() {
            return template;
        }

        @Override
        public List<Integer> getMatchMode() {
            return matchMode;
        }

        @Override
        public List<Integer> getErrorLimits() {
            return errorLimits;
        }
    }

    /**
     * check if segment results contain the target types for when question, if so return SegItem, else return null
     * @param segItems
     * @return
     */
    public static Pair<SegItem, List<String>> checkTargetType(List<SegItem> segItems) {
        Set<String> targetTypes = new HashSet<String>() {
            {
                add(THULACCate.PERSON.getValue());
                add(THULACCate.SANGUO_EVENT.getValue());
            }
        };
        SegItem targetItem = null;
        for (SegItem segItem : segItems) {
            if (targetTypes.contains(segItem.pos)) {
                targetItem = segItem;
                break;
            }
        }
        List<String> predicate = new ArrayList<>();
        if (Objects.nonNull(targetItem)){
            if (targetItem.pos.equals(THULACCate.PERSON.getValue())) {
                Set<String> words = segItems.stream().map(segItem -> segItem.word).collect(Collectors.toSet());
                if (words.contains("出生") || words.contains("诞生")) {
                    predicate.add("birthday");
                } else if (words.contains("死亡") || words.contains("去世") || words.contains("病逝") ||
                        words.contains("离世") || words.contains("逝世")) {
                    predicate.add("deadTime");
                } else {
                    predicate.add("birthday");
                    predicate.add("deadTime");
                }
            } else {
                predicate.add("when");
            }
        }
        if (Objects.nonNull(targetItem)) {
            return new Pair<>(targetItem, predicate);
        }
        return null;
    }
}
