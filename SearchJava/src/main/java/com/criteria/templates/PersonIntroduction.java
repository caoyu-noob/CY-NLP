package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

// provide template for question which is asking question for a person introduction
public class PersonIntroduction implements Templates {

    private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
        {
            //Only a person name
            add(Arrays.asList(new SegItem("*", THULACCate.PERSON.getValue())));
        }
    };

    // provide the matching mode foreach template
    // 1 = regex matching mode, 2 = total text matching mode, 3 = only word property matching mode
    private final static List<Integer> matchMode = new ArrayList<Integer>() {
        {
            add(1);
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
}
