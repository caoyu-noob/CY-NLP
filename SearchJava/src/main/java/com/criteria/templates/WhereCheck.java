package com.criteria.templates;

import java.util.*;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/26.
 */
//a match template to determine if current question is a valid Where question
//it needs to be used combined with another template Where to finally determine if it's a valid question
//some questions are invalid such as 刘备(人名)发生在哪？ 赤壁之战(事件名)是哪的人？
//question matched this template will be regarded as a invalid question
public class WhereCheck {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 人名+不合适的动词
                add(Arrays.asList(
                        new SegItem("*", THULACCate.PERSON.getValue()),
                        new SegItem("[^[出生|诞生|是]]", THULACCate.VERB.getValue())
                ));
                //contains 事件名+不合适的动词
                add(Arrays.asList(
                        new SegItem("*", THULACCate.SANGUO_EVENT.getValue()),
                        new SegItem("[^[发生|进行|爆发|是]]", THULACCate.VERB.getValue())
                ));
                //contains 地名+不合适的动词
                add(Arrays.asList(
                        new SegItem("*", THULACCate.PLACE.getValue()),
                        new SegItem("[^[是]]", THULACCate.VERB.getValue())
                ));
            }
        };

        // provide the matching mode foreach template
        // 3 = regex and word property matching mode, 2 = only regex matching mode, 1 = only word property matching mode
        private final static List<Integer> matchMode = new ArrayList<Integer>() {
            {
                add(3);
                add(3);
            }
        };

        //the error limits for each template
        // matching errors more than the limitations will be regarded as a failure matching, -1 means no limitation
        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
                add(0);
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
     * check if segment results contain the target types for where question, if so return SegItem, else return null
     * @param segItems
     * @return
     */
    public static SegItem checkTargetType(List<SegItem> segItems) {
        Set<String> targetTypes = new HashSet<String>() {
            {
                add(THULACCate.PERSON.getValue());
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
