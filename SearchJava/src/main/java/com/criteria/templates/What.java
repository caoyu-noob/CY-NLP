package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/28.
 */
public class What implements Templates {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 什么时候/年代
                add(Arrays.asList(
                        new SegItem("(什么|何|啥|哪些|哪个)", THULACCate.PRONOUN.getValue()),
                        new SegItem("(事件|战役|战斗|事情|战争)", THULACCate.NOUN.getValue())
                ));
                //contains 什么
                add(Arrays.asList(
                        new SegItem("(什么)", THULACCate.PRONOUN.getValue())
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
}
