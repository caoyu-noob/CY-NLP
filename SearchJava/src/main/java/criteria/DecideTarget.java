package criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        segItems.stream().forEach(item -> {
            if (wordPropertyMap.containsKey(item.pos)) {
                wordPropertyMap.get(item.pos).add(item.word);
            } else {
                wordPropertyMap.put(item.pos, Arrays.asList(item.word));
            }
        });
        if (wordPropertyMap.containsKey("ns")) {
            searchParameter.setTargetModel(TargetModel.EVENT);
            searchParameter.setSubject(wordPropertyMap.get("ns").get(0));
        }else if (wordPropertyMap.containsKey("np")) {
            searchParameter.setTargetModel(TargetModel.FIGURE);
            searchParameter.setSubject(wordPropertyMap.get("np").get(0));
        }
        if (questionType.equals(QuestionType.WHERE)) {
            searchParameter.setProperty("where");
        }
        if(questionType.equals(QuestionType.WHEN)) {
            searchParameter.setProperty("when");
        }
        if (questionType.equals(QuestionType.WHO)) {
            searchParameter.setProperty("participatefigure");
        }
        return searchParameter;
    }
}
