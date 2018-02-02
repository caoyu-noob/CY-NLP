package com.service;

import java.io.File;
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

    private final SearchService searchService;

    private final FileAndDatasetNameService fileAndDatasetNameService = new FileAndDatasetNameService();

    private static SegPos segPos;

    public AnswerService(int applicationMode) throws IOException {
        this.searchService = new SearchService(applicationMode);
        String cModelPath = StringUtils.EMPTY;
        String cDatPath = StringUtils.EMPTY;
        if (applicationMode == ModelConstant.CONSOLE_MODE) {
            cModelPath = ".\\models\\model_c_model.bin";
            cDatPath = ".\\models\\model_c_dat.bin";
        } else if (applicationMode == ModelConstant.WEB_MODE) {
            String rootDir = fileAndDatasetNameService.getCurrentRootDir();
            cModelPath = rootDir + "\\models\\model_c_model.bin";
            cDatPath = rootDir + "\\models\\model_c_dat.bin";
        }
        segPos = new SegPos(cModelPath, cDatPath);
    }

    public String AnswerQuestion(String question) {
        List<SegItem> segResult = segPos.segment(question);
        return GetAnswer(segResult);
    }

    private String GetAnswer(List<SegItem> segItems) {
        QuestionType questionType = questionClassification.classifyQuestion(segItems);
        SearchParameter searchParameter = decideTarget.getTarget(questionType, segItems);
        List<String> result = searchService.findGivenPropertyContainGivenName(searchParameter.getTargetModel(), searchParameter.getSubject(),
                searchParameter.getProperty());
        String answer = StringUtils.EMPTY;
        if (CollectionUtils.isEmpty(result)) {
            System.out.println("无法回答");
            answer = "无法回答";
        } else {
            for (String item : result) {
                System.out.println(item);
                answer = answer + item + ", ";
            }
            answer = answer.substring(0, answer.length() - 2);
        }
        return answer;
    }

//    private String getCurrentRootDir() {
//        File file = new File(this.getClass().getResource("/").getFile());
//        int i = 4;
//        while (i-- > 0) {
//            file = file.getParentFile();
//        }
//        return file.getAbsolutePath();
//    }
}
