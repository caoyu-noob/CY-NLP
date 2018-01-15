package dao;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Objects;

public class ModelDao {

    private final Logger logger = LogManager.getLogger();

    public boolean saveModel(Model model, boolean isOverride, String datasetDir, String modelName) {
        Dataset dataset = TDBFactory.createDataset(datasetDir);
        Model dataModel = null;
        dataset.begin(ReadWrite.WRITE);
        boolean success = true;
        try {
            if (dataset.containsNamedModel(modelName) && !isOverride) {
                logger.info("Model has existed and will not be overridden.");
            } else {
                if (dataset.containsNamedModel(modelName) && isOverride) {
                        logger.info("Override an existed model = {}", modelName);
                } else {
                    logger.info("Create a new model = {}", modelName);
                }
                dataset.removeNamedModel(modelName);
                dataModel = dataset.getNamedModel(modelName);
                dataModel.add(model);
                logger.info("Save model successfully.");
            }
            dataset.commit();
        } catch (Exception e) {
            logger.error(e.toString());
            success = false;
        } finally {
            dataset.end();
        }
        return success;
    }
}
