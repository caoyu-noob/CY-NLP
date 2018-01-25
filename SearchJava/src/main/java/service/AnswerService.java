package service;

import java.io.IOException;
import org.apache.commons.collections.CollectionUtils;
import java.util.List;

import criteria.DecideTarget;
import criteria.QuestionClassification;
import criteria.QuestionClassification.QuestionType;
import criteria.SearchParameter;

import io.github.yizhiru.thulac4j.SegPos;
import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/1/24.
 */
public class AnswerService {

    private final QuestionClassification questionClassification = new QuestionClassification();

    private final DecideTarget decideTarget = new DecideTarget();

    private final SearchService searchService = new SearchService();

    private static SegPos segPos;

    public AnswerService() throws IOException {
        segPos = new SegPos("./models/model_c_model.bin", "./models/model_c_dat.bin");
    }

    public void AnswerQuestion(String question) {
        List<SegItem> segResult = segPos.segment(question);
        GetAnswer(segResult);
    }

    private void GetAnswer(List<SegItem> segItems) {
        QuestionType questionType = questionClassification.classifyQuestion(segItems);
        SearchParameter searchParameter = decideTarget.getTarget(questionType, segItems);
        List<String> result = searchService.findGivenPropertyContainGivenName(searchParameter.getTargetModel(), searchParameter.getSubject(),
                searchParameter.getProperty());
        if (CollectionUtils.isEmpty(result)) {
            System.out.println("无法回答");
        } else {
            for (String item : result) {
                System.out.println(item);
            }
        }
    }
}
