package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/28.
 */
public class WhatCheck {
    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 人名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PERSON.getValue()),
                        new SegItem("(参加|参与|加入)", THULACCate.VERB.getValue())
                ));
                //contains 人名+合适的介词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PERSON.getValue()),
                        new SegItem("(在)", THULACCate.PREPOSTION.getValue())
                ));
                //contains 合适的动词+人名
                add(Arrays.asList(
                        new SegItem("(有|包括|包含)", THULACCate.VERB.getValue()),
                        new SegItem(".*", THULACCate.PERSON.getValue())
                ));
                //contains 地名+合适的动词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PLACE.getValue()),
                        new SegItem("(发生|进行|爆发|有)", THULACCate.VERB.getValue())
                ));
                //contains 地名+合适的介词
                add(Arrays.asList(
                        new SegItem("(于|在)", THULACCate.PREPOSTION.getValue()),
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
            }
        };

        //the error limits for each template
        // matching errors more than the limitations will be regarded as a failure matching, -1 means no limitation
        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
                add(-1);
                add(0);
                add(1);
                add(1);
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
    public static SegItem checkTargetType(List<SegItem> segItems) {
        Set<String> targetTypes = new HashSet<String>() {
            {
                add(THULACCate.PLACE.getValue());
                add(THULACCate.PERSON.getValue());
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
