package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

// provide template for question which is asking question for a person introduction
public class PersonIntroduction implements Templates {

    public static class templates implements Templates.templates{
        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //Only a person name
                add(Arrays.asList(new SegItem(".*", THULACCate.PERSON.getValue())));
                // 人名/动词（是、为）/代词
                add(Arrays.asList(
                        new SegItem(".*", THULACCate.PERSON.getValue()),
                        new SegItem("[是为]", THULACCate.VERB.getValue()),
                        new SegItem(".*", THULACCate.PRONOUN.getValue())
                ));
                // 人名/代词
            }
        };

        // provide the matching mode foreach template
        // 3 = regex and word property matching mode, 2 = only regex matching mode, 1 = only word property matching mode
        private final static List<Integer> matchMode = new ArrayList<Integer>() {
            {
                add(1);
                add(3);
            }
        };

        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
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
}
