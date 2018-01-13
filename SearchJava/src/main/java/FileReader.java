import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {

    private Map<String, String> owlNamesMap;

    public FileReader(Map<String, String> owlNames) {
        this.owlNamesMap = owlNames;
    }

    Map<String, OntModel> readOwlFile() {
        Map<String, OntModel> modelMap = new HashMap<>();
        for (Map.Entry<String, String> entry : owlNamesMap.entrySet()) {
            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(this.getClass().getClassLoader().getResource(entry.getValue()).toString(), "RDF/XML");
            modelMap.put(entry.getKey(), model);
        }
      return modelMap;
    }
}
