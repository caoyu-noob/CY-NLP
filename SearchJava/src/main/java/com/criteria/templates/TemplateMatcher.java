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
            if (matchWordAndProperty(segItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i)))
                return true;
        }
        return false;
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
                if (matchWordAndProperty(currentSegItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i))) {
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
    private static boolean matchWordAndProperty(List<SegItem> target, List<SegItem> template, int mode, int errorLimit) {
        boolean isRegex = (mode & 2) != 0;
        boolean isProperty = (mode & 1) != 0;
        if (template.size() > target.size() || target.size() - template.size() > errorLimit) {
            return false;
        }
        int targetIndex = 0;
        int templateIndex = 0;
        while (targetIndex < target.size() && templateIndex < template.size()) {
            boolean regexMatched = true;
            boolean propertyMatched =  true;
            if (isRegex) {
                regexMatched = Pattern.matches(template.get(templateIndex).word, target.get(targetIndex).word);
            }
            if (isProperty) {
                if (template.get(templateIndex).pos == "*")
                    propertyMatched = true;
                else
                    propertyMatched = target.get(targetIndex).pos.equals(template.get(templateIndex).pos);
            }
            if (regexMatched && propertyMatched) {
                targetIndex++;
                templateIndex++;
            } else {
                targetIndex++;
                if (errorLimit >= 0 && (targetIndex - templateIndex) > errorLimit)
                    return false;
            }
        }
        if (templateIndex == template.size())
            return true;
        else
            return false;
    }
}
