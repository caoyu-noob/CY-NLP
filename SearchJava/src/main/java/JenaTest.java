import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JenaTest {

    private static String EVENT_FILE_NAME = "sanguoevent_chs.owl";
    private static String FIGURE_FILE_NAME = "sanguofigure_chs.owl";
    private static String LOCATION_FILE_NAME = "sanguolocation_chs.owl";
    private static String TIME_FILE_NAME = "sanguotime_chs.owl";

    private static String SANGUO_EVENT = "event";
    private static String SANGUO_FIGURE = "figure";
    private static String SANGUO_LOCATION = "location";
    private static String SANGUO_TIME = "time";

    public static void main(String[] args) throws IOException{
        Map<String, String> fileNames = new HashMap<>();
        fileNames.put(SANGUO_EVENT, EVENT_FILE_NAME);
        fileNames.put(SANGUO_FIGURE, FIGURE_FILE_NAME);
        fileNames.put(SANGUO_LOCATION, LOCATION_FILE_NAME);
        fileNames.put(SANGUO_TIME, TIME_FILE_NAME);
        FileReader fileReader = new FileReader(fileNames);
        Map<String, OntModel> models = fileReader.readOwlFile();
        OntModel model = models.get(SANGUO_EVENT);
        for (Iterator<OntClass> i = model.listClasses(); i.hasNext();) {
            OntClass c = i.next();
            System.out.println(c);
        }
        for (Iterator<Individual> i = model.listIndividuals(); i.hasNext();) {
            Individual in = i.next();
            System.out.println(in);
        }
    }

}
