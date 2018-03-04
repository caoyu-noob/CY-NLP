package com.service;

import java.io.IOException;

import com.constants.SearchConstant;
import com.constants.THULACCate;
import com.entity.GetAnswerEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;

import com.constants.ModelConstant;
import com.criteria.DecideTarget;
import com.criteria.QuestionClassification;
import javafx.util.Pair;

import io.github.yizhiru.thulac4j.SegPos;
import io.github.yizhiru.thulac4j.model.SegItem;
import org.apache.jena.rdf.model.Resource;
import org.omg.CORBA.NO_IMPLEMENT;

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

    private static final String INVALID_QUESTION = "请输入有效的问题";
    private static final String MULTI_QUESTION = "识别到了多个问题，现在只回答第一个/n";
    private static final String UNKNOWN_QUESTION = "好像不能理解这个问题。。。";
    private static final String NODATA_QUESTION = "知道你在问什么，但找不到相关的数据啊。。。";

    private static final Map<String, List<String>> typeAndPredicateMapForWhatQuestion = new HashMap<String, List<String>>(){
        {
            put(THULACCate.PERSON.getValue(), Arrays.asList("participatefigure", "figure"));
            put(THULACCate.PLACE.getValue(), Arrays.asList("where", "location"));
        }
    };

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
            return INVALID_QUESTION;
        List<SegItem> segResult = segPos.segment(postQuestion);
        return GetAnswer(segResult, isMoreThanOne);
    }

    private String GetAnswer(List<SegItem> segItems, boolean isMoreThanOne) {
        String answerPrefix = StringUtils.EMPTY;
        String answer = StringUtils.EMPTY;
        if (isMoreThanOne) {
            answerPrefix = MULTI_QUESTION;
        }
        if (segItems.isEmpty()) {
            answer = "好像不能理解这个问题。。。";
        }
        GetAnswerEntity getAnswerEntity = questionClassification.classifyQuestion(segItems);
        System.out.println(getAnswerEntity.getQuestionType());
        System.out.println(getAnswerEntity.getSubjects() != null ? getAnswerEntity.getSubjects().get(0) : "");
        return answerPrefix + getAnswerStringFromGetAnswerEntity(getAnswerEntity);
    }

    /**
     * remove unnecessary punctuations and remain only the first question
     * @param question
     * @return left: (first) question after processing, right: if more than one question exists
     */
    private Pair<String, Boolean> preProcessQuestion(String question) {
        question = question.replaceAll("\\s+", "");
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

    private String getAnswerStringFromGetAnswerEntity(GetAnswerEntity getAnswerEntity) {
        String answerString = StringUtils.EMPTY;
        switch(getAnswerEntity.getQuestionType()){
            case WHEN:
                answerString = getWhenAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects(),
                        getAnswerEntity.getObjects());
                break;
            case WHERE:
                answerString = getWhereAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects());
                break;
            case WHO:
                answerString = getWhoAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects());
                break;
            case WHAT:
                answerString = getWhatAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects());
                break;
            case PERSON_INTRODUCTION:
                answerString = getIntroductionAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects());
                break;
            case EVENT_INTRODUCTION:
                answerString = getIntroductionAnswerString(getAnswerEntity.getType(), getAnswerEntity.getSubjects());
                break;
            default:
                answerString = UNKNOWN_QUESTION;
        }
        return answerString;
    }

    private String getWhenAnswerString(String type, List<String> subjects, List<String> objects) {
        String answerString = NODATA_QUESTION;
        Map<String, String> propertyAndValueMap = searchService.findPropertyByGivenEntityId(
                SearchConstant.typeStringAndModelMap.get(type), subjects.get(0), objects);
        if (MapUtils.isNotEmpty(propertyAndValueMap)) {
            String resultString = "";
            if (objects.size() == 1) {
                if (Objects.nonNull(propertyAndValueMap.get(objects.get(0))) &&
                        !propertyAndValueMap.get(objects.get(0)).equals("")) {
                    resultString = propertyAndValueMap.get(objects.get(0));
                }
            } else if (objects.size() == 2) {
                String date1 = "?";
                String date2 = "?";
                if (Objects.nonNull(propertyAndValueMap.get(objects.get(0))) &&
                        !propertyAndValueMap.get(objects.get(0)).equals("")) {
                    date1 = propertyAndValueMap.get(objects.get(0));
                }
                if (Objects.nonNull(propertyAndValueMap.get(objects.get(1))) &&
                        !propertyAndValueMap.get(objects.get(1)).equals("")) {
                    date2 = propertyAndValueMap.get(objects.get(1));
                }
                if (!date1.equals("?") || !date2.equals("?")) {
                    resultString = date1 + " - " + date2;
                }
            }
            if (!resultString.isEmpty()) {
                answerString = resultString;
            }
        }
        return answerString;
    }

    private String getWhatAnswerString(String type, List<String> subjects) {
        String answerString = NODATA_QUESTION;
        List<String> labels = searchService.findEntitiesByPredicateAndObject(
                SearchConstant.TargetModel.EVENT,
                typeAndPredicateMapForWhatQuestion.get(type).get(0),
                subjects.get(0),
                typeAndPredicateMapForWhatQuestion.get(type).get(1));
        if (CollectionUtils.isNotEmpty(labels)) {
            StringBuilder resultString = new StringBuilder();
            labels.stream().forEach(label -> {
                resultString.append(label).append(", ");
            });
            return resultString.substring(0, resultString.length() - 2).toString();
        }
        return answerString;
    }

    private String getWhoAnswerString(String type, List<String> subjects) {
        return "";
    }

    private String getWhereAnswerString(String type, List<String> subjects) {
        return "";
    }

    private String getIntroductionAnswerString(String type, List<String> subjects) {
        StringBuilder answerString = new StringBuilder(NODATA_QUESTION);
        Map<String, String> propertyAndValueMap = searchService.getPropertyMapByEntityId(
                SearchConstant.typeStringAndModelMap.get(type), subjects.get(0));
        if (MapUtils.isNotEmpty(propertyAndValueMap)) {
            answerString = new StringBuilder(subjects.get(0));
            answerString.append("\n");
            for (Entry<String, String> entry : propertyAndValueMap.entrySet()) {
                answerString.append(entry.getKey()).append(" ： ").append(entry.getValue()).append("\n");
            }
            answerString.substring(0, answerString.length() - 2);
        }
        return answerString.toString();
    }
}
