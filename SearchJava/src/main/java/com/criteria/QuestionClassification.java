package com.criteria;

import java.util.*;
import java.util.stream.Collectors;

import com.constants.THULACCate;
import com.criteria.templates.*;
import com.entity.GetAnswerEntity;
import com.entity.QuestionType;
import javafx.util.Pair;

import io.github.yizhiru.thulac4j.model.SegItem;
import org.apache.jena.query.QuerySolution;
import org.springframework.util.CollectionUtils;

/**
 * Created by cao_y on 2018/1/24.
 * design a criteria to match different question type
 */
public class QuestionClassification {

    private static final PersonIntroduction.templates personIntroduction = new PersonIntroduction.templates();
    private static final Event.templates event = new Event.templates();
    private static final EventIntroduction.templates eventIntroduction = new EventIntroduction.templates();
    private static final Where.templates where = new Where.templates();
    private static final WhereCheck.templates whereCheck = new WhereCheck.templates();
    private static final When.templates when = new When.templates();
    private static final WhenCheck.templates whenCheck = new WhenCheck.templates();
    private static final Who.templates who = new Who.templates();
    private static final WhoCheck.templates whoCheck = new WhoCheck.templates();
    private static final What.templates what = new What.templates();
    private static final WhatCheck.templates whatCheck = new WhatCheck.templates();

    //Determine the question type at the first stage
    public GetAnswerEntity classifyQuestion(List<SegItem> segItems) {
        replaceEventSegItem(segItems);
        //determine if question is for where
        int whenPos = getPosForWhenQuestion(segItems);
        if (whenPos >= 0) {
            Pair<SegItem, List<String>> targetPair = checkValidityOfWhenQuestion(segItems);
            if (targetPair != null) {
                return new GetAnswerEntity(QuestionType.WHEN, targetPair.getKey().pos,
                        Arrays.asList(targetPair.getKey().word), targetPair.getValue());
            }
        }
        //determine if question is for where
        int wherePos = getPosForWhereQuestion(segItems);
        if (wherePos >= 0) {
            SegItem targetItem = chechValidityOfWhereQuestion(segItems);
            if (targetItem != null) {
                return new GetAnswerEntity(QuestionType.WHERE, targetItem.pos, Arrays.asList(targetItem.word), null);
            }
        }
        //determine if question is for who
        int whoPos = getPosForWhoQuestion(segItems);
        if (whoPos >= 0) {
            SegItem targetItem = checkValidityOfWhoQuestion(segItems);
            if (targetItem != null) {
                return new GetAnswerEntity(QuestionType.WHO, targetItem.pos, Arrays.asList(targetItem.word), null);
            }
        }
        //determine if question is for what
        int whatPos = getPosForWhatQuestion(segItems);
        if (whatPos >= 0) {
            SegItem targetItem = checkValidityOfWhatQuestion(segItems);
            if (targetItem != null) {
                return new GetAnswerEntity(QuestionType.WHAT, targetItem.pos, Arrays.asList(targetItem.word), null);
            }
        }
        //determine if question is for person introduction
        if (isForPersonIntroduction(segItems)) {
            List<String> personNames = findWordsForGivenProperty(segItems, THULACCate.PERSON);
            return new GetAnswerEntity(QuestionType.PERSON_INTRODUCTION, THULACCate.PERSON.getValue(), personNames, null);
        }
        //determine if question is for event introduction
        if (isForEventIntroduction(segItems)) {
            List<String> eventNames = findWordsForGivenProperty(segItems, THULACCate.SANGUO_EVENT);
            return new GetAnswerEntity(QuestionType.EVENT_INTRODUCTION, THULACCate.SANGUO_EVENT.getValue(), eventNames, null);
        }
        Set<SegItem> segItemsSet = new HashSet<>();
        segItemsSet.addAll(segItems);
        return GetAnswerEntity.getUnkownEntity();
    }

    //determine if the current question is for the introduction of a person
    private boolean isForPersonIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, personIntroduction, true);
    }

    //determine if the current question is for the introduction of a Sanguo event
    private boolean isForEventIntroduction(List<SegItem> segItems) {
        return TemplateMatcher.Match(segItems, eventIntroduction, true);
    }

    private int getPosForWhereQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, where, false);
    }

    private int getPosForWhenQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, when, false);
    }

    private int getPosForWhoQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, who, false);
    }

    private int getPosForWhatQuestion(List<SegItem> segItems) {
        return TemplateMatcher.MatchAndGetPos(segItems, what, false);
    }

    private SegItem chechValidityOfWhereQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whereCheck, false)) {
            return WhereCheck.checkTargetType(segItems);
        }
        return null;
    }

    private Pair<SegItem, List<String>> checkValidityOfWhenQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whenCheck, false)) {
            return WhenCheck.checkTargetType(segItems);
        }
        return null;
    }

    private SegItem checkValidityOfWhoQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whoCheck, false)) {
            return WhoCheck.checkTargetType(segItems);
        }
        return null;
    }

    private SegItem checkValidityOfWhatQuestion(List<SegItem> segItems) {
        if (TemplateMatcher.Match(segItems, whatCheck, false)) {
            return WhatCheck.checkTargetType(segItems);
        }
        return null;
    }

    //replace the original segItems which is a event word with a new single segItem
    //no replacement will happen if there is no event word in segItems
    //e.g. 赤壁(地名)/之(介词)/战(动词) will become 赤壁之战(三国事件)
    private void replaceEventSegItem(List<SegItem> segItems) {
        List<Integer> eventStartAndEndIndex = TemplateMatcher.MatchEvent(segItems, event);
        if (!CollectionUtils.isEmpty(eventStartAndEndIndex)) {
            int removeCnt = 0;
            for (int i = 0; i < eventStartAndEndIndex.size(); i+=2) {
                int start = eventStartAndEndIndex.get(i) - removeCnt;
                int end = eventStartAndEndIndex.get(i+1) - removeCnt;
                String eventName = "";
                for (int j = start; j < end; j++) {
                    eventName += segItems.get(start).word;
                    segItems.remove(start);
                    removeCnt++;
                }
                segItems.add(start, new SegItem(eventName, THULACCate.SANGUO_EVENT.getValue()));
                removeCnt--;
            }
        }
    }

    private List<String> findWordsForGivenProperty(List<SegItem> segItems, THULACCate property) {
        return segItems.stream()
                .filter(segItem -> segItem.pos.equals(property.getValue()))
                .map(segItem -> segItem.word)
                .collect(Collectors.toList());
    }

    private List<String> findWordsForGivenProperty(List<SegItem> segItems, THULACCate property, int start, int end) {
        List<String> res = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (segItems.get(i).pos.equals(property.getValue())) {
                res.add(segItems.get(i).word);
            }
        }
        return res;
    }
}
