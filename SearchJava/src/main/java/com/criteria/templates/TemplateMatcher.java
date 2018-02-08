package com.criteria.templates;

import java.util.List;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/8.
 */
public class TemplateMatcher {

    public boolean Match(List<SegItem> segItems, Templates templates) {
        List<List<SegItem>> templatesList = templates.getTemplate();
        List<Integer> matchMode = templates.getMatchMode();
        for (int i = 0; i < templatesList.size(); i++) {
            switch matchMode.get(i):
            case 1:
                if (segItems)
        }
        return true;
    }

    private boolean matchWordProperty(List<SegItem> target, List<SegItem> template) {
        if (template.size() > target.size()) {
            return false;
        }
        int targetIndex = 0;
        int templateIndex = 0;
        while (targetIndex <= templateIndex && targetIndex < target.size() && templateIndex < template.size()) {
            
        }
    }
}
