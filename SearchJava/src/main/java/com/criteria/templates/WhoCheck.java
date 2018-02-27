package com.criteria.templates;

import com.constants.THULACCate;
import io.github.yizhiru.thulac4j.model.SegItem;

import java.util.*;

public class WhoCheck {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 事件名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue()),
                        new SegItem("(有|包括|包含|是)", THULACCate.VERB.getValue())
                ));
                //contains 合适的动词+事件名
                add(Arrays.asList(
                        new SegItem("(参加|加入|参与|是)", THULACCate.VERB.getValue()),
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue())
                ));
                //contains 合适的介词+事件名
                add(Arrays.asList(
                        new SegItem("(在)", THULACCate.PREPOSTION.getValue()),
                        new SegItem(".*", THULACCate.SANGUO_EVENT.getValue())
                ));
                //contains 地名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PLACE.getValue()),
                        new SegItem("(有|产生|出生|出)", THULACCate.VERB.getValue())
                ));
                //contains 合适的介词+地名
                add(Arrays.asList(
                        new SegItem("(在|于)", THULACCate.PREPOSTION.getValue()),
                        new SegItem(".*", THULACCate.PLACE.getValue())
                ));
                add(Arrays.asList(
                        new SegItem("(出生|诞生|是)", THULACCate.VERB.getValue()),
                        new SegItem(".*", THULACCate.PLACE.getValue())
                ));
            }
        };

        // provide the matching mode foreach template
        // 3 = regex and word property matching mode, 2 = only regex matching mode, 1 = only word property matching mode
        private final static List<Integer> matchMode = new ArrayList<Integer>() {
            {
                add(3);
                add(3);
                add(3);
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
                add(2);
                add(2);
                add(2);
                add(0);
                add(2);
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
     * check if segment results contain the target types for who question, if so return SegItem, else return null
     * @param segItems
     * @return
     */
    public static SegItem checkTargetType(List<SegItem> segItems) {
        Set<String> targetTypes = new HashSet<String>() {
            {
                add(THULACCate.PLACE.getValue());
                add(THULACCate.SANGUO_EVENT.getValue());
            }
        };
        for (SegItem segItem : segItems) {
            if (targetTypes.contains(segItem.pos)) {
                return segItem;
            }
        }
        return null;
    }
}
