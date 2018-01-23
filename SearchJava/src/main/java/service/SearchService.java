package service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import constants.ModelConstant;
import constants.SearchConstant;
import dao.ModelDao;

/**
 * Created by cao_y on 2018/1/22.
 */
public class SearchService {

    private final Logger logger = LogManager.getLogger();

    private final ModelDao modelDao;

    public SearchService() {
        this.modelDao = new ModelDao(generateSatasetNamesMap());
    }

    public Map<Object, Object> findEnityByGivenName(SearchConstant.TargetModel targetModel, String id) {
        StringBuffer queryString = new StringBuffer();
        queryString.append(targetModel.getPrefix()).append("select ?s ?p ?o where { ").append(targetModel.getName())
                .append(":").append(id).append("_sanguozhi ?p ?o}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        Map<Object, Object> result = new HashMap<>();
        while(resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            Object key = null;
            Object value = null;
            if (p.isLiteral()) {
                key = p.toString();
            } else {
                key = p.asResource();
            }
            if (o.isLiteral()) {
                value = o.toString();
            } else {
                value = o.asResource();
            }
            result.put(key, value);
        }
        return result;
    }

    private Map<String, String> generateSatasetNamesMap() {
        Map<String, String> datasetNamesMap = new HashMap<>();
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), ModelConstant.DatasetNames.SANGUO_EVENT.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), ModelConstant.DatasetNames.SANGUO_FIGURE.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), ModelConstant.DatasetNames.SANGUO_LOCATION.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_TIME.get(), ModelConstant.DatasetNames.SANGUO_TIME.get());
        return datasetNamesMap;
    }

}
