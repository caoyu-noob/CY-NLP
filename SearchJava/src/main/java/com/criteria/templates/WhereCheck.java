package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/26.
 */
//a match template to determine if current question is a valid Where question
//it needs to be used combined with another template Where to finally determine if it's a valid question
//some questions are invalid such as 刘备(人名)发生在哪？ 赤壁之战(事件名)是哪的人？
public class WhereCheck {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //contains 人名
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
