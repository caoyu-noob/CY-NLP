package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/27.
 */
public class When implements Templates {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 什么时候/年代
                add(Arrays.asList(
                        new SegItem("(什么|何|啥|哪些|哪个|哪)", THULACCate.PRONOUN.getValue()),
                        new SegItem("(时候|时间|年代|年份|日期)", THULACCate.NOUN.getValue())
                ));
                //contains 何时
                add(Arrays.asList(
                        new SegItem("(何时)", THULACCate.PRONOUN.getValue())
                ));
                //contains 哪年/何年
                add(Arrays.asList(
                        new SegItem("(何|哪)", THULACCate.PRONOUN.getValue()),
                        new SegItem("[年]", THULACCate.QUANTITY.getValue())
                ));
                //contains XX时间是什么/是哪一年
                add(Arrays.asList(
                        new SegItem("(时候|时间|年代|年份|日期|年月)", THULACCate.NOUN.getValue())
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
            }
        };

        //the error limits for each template
        // matching errors more than the limitations will be regarded as a failure matching, -1 means no limitation
        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
                add(1);
                add(0);
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
