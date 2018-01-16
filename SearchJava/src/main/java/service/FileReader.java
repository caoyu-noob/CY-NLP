package service;

import dao.ModelDao;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {

    //Map<modelName, fileName>
    private Map<String, String> owlFileNamesMap;

    //Map<modelName, datasetName>
    private Map<String, String> owlDatasetNamesMap;

    private final ModelDao modelDao = new ModelDao();

    public FileReader(Map<String, String> owlFileNames, Map<String, String> owlDatasetNamesMap) {
        this.owlFileNamesMap = owlFileNames;
        this.owlDatasetNamesMap = owlDatasetNamesMap;
    }

    public Map<String, OntModel> readOwlFile() {
        Map<String, OntModel> modelMap = new HashMap<>();
        for(Map.Entry<String, String> entry : owlFileNamesMap.entrySet()) {
            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(this.getClass().getClassLoader().getResource(entry.getValue()).toString(), "RDF/XML");
            modelMap.put(entry.getKey(), model);
        }
      return modelMap;
    }

    public boolean saveOwlModel() {
        Map<String, OntModel> modelMap = readOwlFile();
        boolean success = true;
        for(Map.Entry<String, String> entry : owlDatasetNamesMap.entrySet()) {
            String modelName = entry.getKey();
            if (!modelDao.saveModel(modelMap.get(modelName), entry.getValue(), modelName, false)) {
                success = false;
                break;
            }
        }
        return success;
    }


}
