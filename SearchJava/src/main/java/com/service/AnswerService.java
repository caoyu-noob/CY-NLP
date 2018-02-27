package com.service;

import java.io.File;
import java.io.IOException;

import com.entity.GetAnswerEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import com.constants.ModelConstant;
import com.criteria.DecideTarget;
import com.criteria.QuestionClassification;
import com.criteria.SearchParameter;
import javafx.util.Pair;

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

    private final String fileSeparator = System.getProperty("file.separator");

    private static SegPos segPos;

    public AnswerService(int applicationMode) throws IOException {
        this.searchService = new SearchService(applicationMode);
        String cModelPath = StringUtils.EMPTY;
        String cDatPath = StringUtils.EMPTY;
        if (applicationMode == ModelConstant.CONSOLE_MODE) {
            cModelPath = "." + fileSeparator + "models" + fileSeparator + "model_c_model.bin";
            cDatPath = "." + fileSeparator + "models" + fileSeparator +"model_c_dat.bin";
        } else if (applicationMode == ModelConstant.WEB_MODE) {
            String rootDir = fileAndDatasetNameService.getCurrentRootDir();
            cModelPath = rootDir + fileSeparator + "models" + fileSeparator + "model_c_model.bin";
            cDatPath = rootDir + fileSeparator + "models" + fileSeparator +"model_c_dat.bin";
        }
        segPos = new SegPos(cModelPath, cDatPath);
    }

    public String AnswerQuestion(String question) {
        Pair<String, Boolean> processResult = preProcessQuestion(question);
        String postQuestion = processResult.getKey();
        boolean isMoreThanOne = processResult.getValue();
        if (postQuestion.isEmpty())
            return "请输入有效的问题";
        List<SegItem> segResult = segPos.segment(postQuestion);
        return GetAnswer(segResult, isMoreThanOne);
    }

    private String GetAnswer(List<SegItem> segItems, boolean isMoreThanOne) {
        String answerPrefix = StringUtils.EMPTY;
        String answer = StringUtils.EMPTY;
        if (isMoreThanOne) {
            answerPrefix = "识别到了多个问题，现在只回答第一个/n";
        }
        if (segItems.isEmpty()) {
            answer = "好像不能理解这个问题。。。";
        }
        GetAnswerEntity getAnswerEntity = questionClassification.classifyQuestion(segItems);
        System.out.println(getAnswerEntity.getQuestionType());
        System.out.println(getAnswerEntity.getSubjects() != null ? getAnswerEntity.getSubjects().get(0) : "");
//        SearchParameter searchParameter = decideTarget.getTarget(questionType, segItems);
//        List<String> result = searchService.findGivenPropertyContainGivenName(searchParameter.getTargetModel(), searchParameter.getSubject(),
//                searchParameter.getProperty());
//        if (CollectionUtils.isEmpty(result)) {
//            answer = "知道你在问什么，但找不到相关的数据啊。。。";
//        } else {
//            for (String item : result) {
//                System.out.println(item);
//                answer = answer + item + ", ";
//            }
//            answer = answer.substring(0, answer.length() - 2);
//        }
        return answerPrefix + answer;
    }

    /**
     * remove unnecessary punctuations and remain only the first question
     * @param question
     * @return left: (first) question after processing, right: if more than one question exists
     */
    private Pair<String, Boolean> preProcessQuestion(String question) {
        question = question.replaceAll("[a-zA-Z·`~@#$%^&*+=￥…|<>{}/\\\\\\[\\]]", "");
        String[] subQuestions = question.split("[。？！.?!]");
        String res = "";
        boolean existAnother = false;
        for (int i = 0; i < subQuestions.length; i++) {
            if (subQuestions[i].length()>1) {
                if (res.isEmpty())
                    res = subQuestions[i];
                else
                    existAnother = true;
            }
        }
        return new Pair<>(res, existAnother);
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
