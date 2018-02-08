package com.criteria.templates;

import java.util.List;
import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/8.
 */
public interface Templates {

    List<List<SegItem>> getTemplate();

    List<Integer> getMatchMode();
}
