package criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import constants.SearchConstant;
import constants.SearchConstant.TargetModel;
import criteria.QuestionClassification.QuestionType;
import javafx.util.Pair;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/1/24.
 */
public class DecideTarget {

    public SearchParameter getTarget(QuestionType questionType,
            List<SegItem> segItems) {
        SearchParameter searchParameter= new SearchParameter();
        if (questionType.equals(QuestionType.UNKNOW)) {
            return searchParameter;
        }
        Map<String, List<String>> wordPropertyMap = new HashMap<>();
        for(SegItem item : segItems) {
            if (wordPropertyMap.containsKey(item.pos)) {
                wordPropertyMap.get(item.pos).add(item.word);
            } else {
                wordPropertyMap.put(item.pos, new ArrayList<>());
                wordPropertyMap.get(item.pos).add(item.word);
            }
        }
        if (wordPropertyMap.containsKey("ns")) {
            searchParameter.setTargetModel(TargetModel.EVENT);
            searchParameter.setSubject(wordPropertyMap.get("ns").get(0));
        } else if (wordPropertyMap.containsKey("a")) {
            searchParameter.setTargetModel(TargetModel.EVENT);
            searchParameter.setSubject(wordPropertyMap.get("a").get(0));
        } else if (wordPropertyMap.containsKey("v")) {
            searchParameter.setTargetModel(TargetModel.EVENT);
            searchParameter.setSubject(wordPropertyMap.get("v").get(0));
        }
        else if (wordPropertyMap.containsKey("np")) {
            searchParameter.setTargetModel(TargetModel.FIGURE);
            searchParameter.setSubject(wordPropertyMap.get("np").get(0));
        }
        if (questionType.equals(QuestionType.WHERE)) {
            searchParameter.setProperty(SearchConstant.Property.WHERE);
        }
        if(questionType.equals(QuestionType.WHEN)) {
            searchParameter.setProperty(SearchConstant.Property.WHEN);
        }
        if (questionType.equals(QuestionType.WHO)) {
            searchParameter.setProperty(SearchConstant.Property.PARTICIPANT);
        }
        return searchParameter;
    }
}
