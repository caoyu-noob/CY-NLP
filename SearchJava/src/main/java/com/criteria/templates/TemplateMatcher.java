package com.criteria.templates;

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
            if (matchWordProperty(segItems, templatesList.get(i), matchMode.get(i), errorLimits.get(i)))
                return true;
        }
        return false;
    }

    /**
     *
     * @param target
     * @param template
     * @param mode
     * @param errorLimit
     * @return
     */
    private static boolean matchWordProperty(List<SegItem> target, List<SegItem> template, int mode, int errorLimit) {
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
