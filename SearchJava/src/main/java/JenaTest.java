import dao.ModelDao;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class JenaTest {

    private static String EVENT_FILE_NAME = "sanguoevent_chs.owl";
    private static String FIGURE_FILE_NAME = "sanguofigure_chs.owl";
    private static String LOCATION_FILE_NAME = "sanguolocation_chs.owl";
    private static String TIME_FILE_NAME = "sanguotime_chs.owl";

    private static String SANGUO_EVENT = "event";
    private static String SANGUO_FIGURE = "figure";
    private static String SANGUO_LOCATION = "location";
    private static String SANGUO_TIME = "time";

    private static String DATABASE = "datasets";

    private static String NS = "http://www.sim.whu.edu.cn/historyevent.owl";

    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        Map<String, String> fileNames = new HashMap<>();
        fileNames.put(SANGUO_EVENT, EVENT_FILE_NAME);
        fileNames.put(SANGUO_FIGURE, FIGURE_FILE_NAME);
        fileNames.put(SANGUO_LOCATION, LOCATION_FILE_NAME);
        fileNames.put(SANGUO_TIME, TIME_FILE_NAME);
        FileReader fileReader = new FileReader(fileNames);
        Map<String, OntModel> models = fileReader.readOwlFile();
        OntModel model = models.get(SANGUO_EVENT);
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

        ModelDao modelDao = new ModelDao();
        modelDao.saveModel(model, false, "datasets/event", "SanGuoEvent");
    }

}
