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
import service.FileReader;
import service.SearchService;

import java.io.IOException;
import java.util.*;

import io.github.yizhiru.thulac4j.SegPos;
import io.github.yizhiru.thulac4j.model.SegItem;


public class JenaTest {

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
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
        Map<String, OntModel> models = fileReader.readOwlFile();
        SearchService searchService = new SearchService();
        SegPos segPos = new SegPos("./models/model_c_model.bin", "./models/model_c_dat.bin");
        String question = "官渡之战在什么地方发生的？";
        List<SegItem> segResult = segPos.segment(question);
        Map<Object, Object> res = searchService.findEnityContainsGivenName(SearchConstant.TargetModel.EVENT, "官渡");
        System.out.println("literal");
//        ModelDao modelDao = new ModelDao(datasetNames);
//        String query = EVENT_PREFIX + "select ?s ?p ?o where { event:赤壁之战_sanguozhi ?p ?o}";
//        ResultSet resultSet = modelDao.queryModel(ModelConstant.ModelNames.SANGUO_EVENT.get(), query);
//        while (resultSet.hasNext()) {
//            QuerySolution qs = resultSet.next();
//            RDFNode p = qs.get("p");
//            RDFNode o = qs.get("o");
//            if (o.isResource()) {
//                System.out.println("resource");
//            }
//            if (o.isLiteral()) {
//                System.out.println("literal");
//            }
//            System.out.println(p.toString() + ": " + o.toString());
//        }
//        System.out.println("11");
//        for (Iterator<OntClass> i = model.listClasses(); i.hasNext();) {
//            OntClass c = i.next();
//            System.out.println(c);
//        }
//        for (Iterator<Individual> i = model.listIndividuals(); i.hasNext();) {
//            Individual in = i.next();
//            System.out.println(in);
//        }
//        OntClass sanguoEvent = model.getOntClass(NS + "#" + "SanGuoEvent");
//        if (sanguoEvent.hasSubClass()) {
//            for (Iterator<OntClass> subClasses = sanguoEvent.listSubClasses(); subClasses.hasNext();) {
//                OntClass subClass = subClasses.next();
//                System.out.println(subClass);
//            }
//        }
    }

    private void saveModels() {
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
