package com.criteria.templates;

import com.constants.THULACCate;
import io.github.yizhiru.thulac4j.model.SegItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//a match template using pronoun and noun to determine whether the question is querying where
//it needs to be used combined with another template WhereCheck to finally determine if it's a valid question
public class Where implements Templates {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 什么地方/位置
                add(Arrays.asList(
                    new SegItem("[什么|何|啥|哪些]", THULACCate.PRONOUN.getValue()),
                    new SegItem("[地方|地点|位置|区域]", THULACCate.NOUN.getValue())
                ));
                //contains 哪里/何处/何地
                add(Arrays.asList(
                    new SegItem("[何处|何地|哪里|哪儿|哪]", THULACCate.PRONOUN.getValue())
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
}
