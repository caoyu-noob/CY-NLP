package com;

import com.constants.ModelConstant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.constants.SearchConstant;
import com.dao.ModelDao;
import com.service.AnswerService;
import com.service.FileAndDatasetNameService;
import com.service.FileReader;
import com.service.SearchService;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JenaTest {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        //Please run this function when you run it in the first time in you local environment
//        saveModels();
        //Please run this function when you run it in the first time in you local environment
        String a = "dsaodao, 三国江苏淮阴人,ds dssdfsd";
        Pattern regex = Pattern.compile("\\s三国.+人");
        Matcher result = regex.matcher(a);
        while (result.find()) {
            System.out.println(result.group().substring(3, result.group().length() - 2));
        }
        AnswerService answerService = new AnswerService(ModelConstant.CONSOLE_MODE);
        while(true) {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\n");
            System.out.println("-----------");
            System.out.println("请输入问题：");
            String question = sc.next();
            System.out.println(answerService.AnswerQuestion(question));
            System.out.println("-----------");
        }

    }

    private static void saveModels() {
        Map<String, String> fileNames = new HashMap<>();
        fileNames.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), ModelConstant.FileNames.EVENT.get());
        fileNames.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), ModelConstant.FileNames.FIGURE.get());
        fileNames.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), ModelConstant.FileNames.LOCATION.get());
        fileNames.put(ModelConstant.ModelNames.SANGUO_TIME.get(), ModelConstant.FileNames.TIME.get());
        Map<String, String> datasetNames = new HashMap<>();
        datasetNames.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), ModelConstant.DatasetNames.SANGUO_EVENT.get());
        datasetNames.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), ModelConstant.DatasetNames.SANGUO_FIGURE.get());
        datasetNames.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), ModelConstant.DatasetNames.SANGUO_LOCATION.get());
        datasetNames.put(ModelConstant.ModelNames.SANGUO_TIME.get(), ModelConstant.DatasetNames.SANGUO_TIME.get());
        FileReader fileReader = new FileReader(fileNames, datasetNames);
        boolean isSuccess = fileReader.saveOwlModel();
        if (isSuccess) {
            logger.info("Save all models successfully");
        }
    }

}
