package service;

import java.util.List;

import criteria.DecideTarget;
import criteria.QuestionClassification;
import criteria.QuestionClassification.QuestionType;
import criteria.SearchParameter;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/1/24.
 */
public class AnswerService {

    private final QuestionClassification questionClassification = new QuestionClassification();

    private final DecideTarget decideTarget = new DecideTarget();

    private final SearchService searchService = new SearchService();

    public void answer(List<SegItem> segItems) {
        QuestionType questionType = questionClassification.classifyQuestion(segItems);
        SearchParameter searchParameter = decideTarget.getTarget(questionType, segItems);
        searchService.findEnityByGivenName(searchParameter.getTargetModel(), searchParameter.getSubject());
    }
}
