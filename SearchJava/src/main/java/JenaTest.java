import dao.ModelDao;
import org.apache.jena.ontology.OntModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.FileReader;

import java.io.IOException;
import java.util.*;


public class JenaTest {

    private static String EVENT_FILE_NAME = "sanguoevent_chs.owl";
    private static String FIGURE_FILE_NAME = "sanguofigure_chs.owl";
    private static String LOCATION_FILE_NAME = "sanguolocation_chs.owl";
    private static String TIME_FILE_NAME = "sanguotime_chs.owl";

    private static String SANGUO_EVENT = "SanGuoEvent";
    private static String SANGUO_FIGURE = "SanGuoFigure";
    private static String SANGUO_LOCATION = "SanGuoLocation";
    private static String SANGUO_TIME = "SanGuoTime";

    private static String DATASET = "datasets";

    private static String EVENT_DATASET_NAME = DATASET + "/" + SANGUO_EVENT;
    private static String FIGURE_DATASET_NAME = DATASET + "/" + SANGUO_FIGURE;
    private static String LOCATION_DATASET_NAME = DATASET + "/" + SANGUO_LOCATION;
    private static String TIME_DATASET_NAME = DATASET + "/" + SANGUO_TIME;

    private static String NS = "http://www.sim.whu.edu.cn/historyevent.owl";

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        Map<String, String> fileNames = new HashMap<>();
        fileNames.put(SANGUO_EVENT, EVENT_FILE_NAME);
        fileNames.put(SANGUO_FIGURE, FIGURE_FILE_NAME);
        fileNames.put(SANGUO_LOCATION, LOCATION_FILE_NAME);
        fileNames.put(SANGUO_TIME, TIME_FILE_NAME);
        Map<String, String> datasetNames = new HashMap<>();
        datasetNames.put(SANGUO_EVENT, EVENT_DATASET_NAME);
        datasetNames.put(SANGUO_FIGURE, FIGURE_DATASET_NAME);
        datasetNames.put(SANGUO_LOCATION, LOCATION_DATASET_NAME);
        datasetNames.put(SANGUO_TIME, TIME_DATASET_NAME);
        FileReader fileReader = new FileReader(fileNames, datasetNames);
        Map<String, OntModel> models = fileReader.readOwlFile();
        boolean isSuccess = fileReader.saveOwlModel();
        if (isSuccess) {
            logger.info("Save all models successfully");
        }
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

}
