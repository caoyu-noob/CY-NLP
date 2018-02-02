package com;

import com.constants.ModelConstant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.service.AnswerService;
import com.service.FileReader;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


public class JenaTest {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        //Please run this function when you run it in the first time in you local environment
//        saveModels();
        //Please run this function when you run it in the first time in you local environment
        List<String> s = new LinkedList<>();
        s.addAll(null);
        AnswerService answerService = new AnswerService(ModelConstant.CONSOLE_MODE);
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("-----------");
            System.out.println("请输入问题：");
            String question = sc.next();
            answerService.AnswerQuestion(question);
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
