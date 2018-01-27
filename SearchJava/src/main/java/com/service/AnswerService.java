package com.service;

import java.io.IOException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import com.constants.ModelConstant;
import com.criteria.DecideTarget;
import com.criteria.QuestionClassification;
import com.criteria.QuestionClassification.QuestionType;
import com.criteria.SearchParameter;

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

    public AnswerService(int applicationMode) throws IOException {
        String cModelPath = StringUtils.EMPTY;
        String cDatPath = StringUtils.EMPTY;
        if (applicationMode == ModelConstant.CONSOLE_MODE) {
            cModelPath = "./models/model_c_model.bin";
            cDatPath = "./models/model_c_dat.bin";
        } else if (applicationMode == ModelConstant.WEB_MODE) {
//            System.out.println(this.getClass().getResource("/").getPath());
//            String absPath = this.getClass().getResource("/").getPath();
            cModelPath = "C:/Develop/CY-NLP/SearchJava/models/model_c_model.bin";
            cDatPath = "C:/Develop/CY-NLP/SearchJava/models/model_c_dat.bin";
        }
        segPos = new SegPos(cModelPath, cDatPath);
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
