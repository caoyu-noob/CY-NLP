package com.criteria.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/8.
 */
public class TemplateMatcher {

    /**
     * determine whether a segment result matches a template (a question class)
     * @param segItems segmentation result
     * @param templates the template
     * @return
     */
    public static boolean Match(List<SegItem> segItems, Templates.templates templates) {
        List<List<SegItem>> templatesList = templates.getTemplate();
        List<Integer> matchMode = templates.getMatchMode();
        List<Integer> errorLimits = templates.getErrorLimits();
        for (int i = 0; i < templatesList.size(); i++) {
            if (matchWordAndProperty(segItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i)) >= 0)
                return true;
        }
        return false;
    }

    /**
     * return the specific start position of matched result in the segments， -1 means it cannot be matched
     * @param segItems
     * @param templates
     * @return
     */
    public static int MatchAndGetPos(List<SegItem> segItems, Templates.templates templates) {
        List<List<SegItem>> templatesList = templates.getTemplate();
        List<Integer> matchMode = templates.getMatchMode();
        List<Integer> errorLimits = templates.getErrorLimits();
        for (int i = 0; i < templatesList.size(); i++) {
            int matchedPos = matchWordAndProperty(segItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i));
            if (matchedPos >= 0)
                return matchedPos;
        }
        return -1;
    }

    /**
     * find the start and end position of the a given slot in a segmented items, if no matched slot found, return -1
     * In order to find the matched result, it will try to match the template in each position
     * @param segItems
     * @param templates
     * @return
     */
    public static List<Integer> MatchEvent(List<SegItem> segItems, Templates.templates templates) {
        List<Integer> res = new ArrayList<>();
        int index = 0;
        List<List<SegItem>> templatesList = templates.getTemplate();
        List<Integer> matchMode = templates.getMatchMode();
        List<Integer> errorLimits = templates.getErrorLimits();
        while(index < segItems.size()) {
            for (int i = 0; i < templatesList.size(); i++) {
                if (segItems.size() - index < templatesList.get(i).size())
                    continue;
                List<SegItem> currentSegItems = segItems.subList(index, index + templatesList.get(i).size());
                if (matchWordAndProperty(currentSegItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i)) >= 0) {
                    res.add(index);
                    res.add(index + templatesList.get(i).size());
                    index += templatesList.get(i).size() - 1;
                    break;
                }
            }
            index++;
        }
        return res;
    }

    /**
     *
     * @param target
     * @param template
     * @param mode
     * @param errorLimit
     * @return
     */
    private static int matchWordAndProperty(List<SegItem> target, List<SegItem> template, int mode, int errorLimit) {
        boolean isRegex = (mode & 2) != 0;
        boolean isProperty = (mode & 1) != 0;
        if (template.size() > target.size()) {
            return -1;
        }
        int targetIndex = 0;
        while (targetIndex < target.size() && target.size() - targetIndex >= template.size()) {
            int templateIndex = 0;
            int currentTargetIndex = targetIndex;
            while (currentTargetIndex < target.size() && target.size() - currentTargetIndex >= (template.size() - targetIndex)
                    && templateIndex < template.size()) {
                boolean regexMatched = true;
                boolean propertyMatched =  true;
                if (isRegex) {
                    regexMatched = Pattern.matches(template.get(templateIndex).word, target.get(currentTargetIndex).word);
                }
                if (isProperty) {
                    if (template.get(templateIndex).pos == "*")
                        propertyMatched = true;
                    else
                        propertyMatched = target.get(currentTargetIndex).pos.equals(template.get(templateIndex).pos);
                }
                if (regexMatched && propertyMatched) {
                    currentTargetIndex++;
                    templateIndex++;
                } else {
                    currentTargetIndex++;
                    if (errorLimit >= 0 && (currentTargetIndex - templateIndex) > errorLimit)
                        break;
                }
            }
            if (templateIndex >= template.size()) {
                return targetIndex;
            }
            targetIndex++;
        }
        return -1;
    }
}
