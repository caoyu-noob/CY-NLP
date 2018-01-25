import constants.ModelConstant;
import constants.SearchConstant;
import dao.ModelDao;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.AnswerService;
import service.FileReader;
import service.SearchService;

import java.io.IOException;
import java.util.*;

import io.github.yizhiru.thulac4j.SegPos;
import io.github.yizhiru.thulac4j.model.SegItem;


public class JenaTest {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        //Please run this function when you run it in the first time in you local environment
//        saveModels();
        //Please run this function when you run it in the first time in you local environment

        AnswerService answerService = new AnswerService();
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
