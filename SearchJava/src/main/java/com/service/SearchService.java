package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.constants.ModelConstant;
import com.constants.SearchConstant;
import com.constants.SearchConstant.Property;
import com.constants.SearchConstant.TargetModel;
import com.dao.ModelDao;

/**
 * Created by cao_y on 2018/1/22.
 */
public class SearchService {

    private final Logger logger = LogManager.getLogger();

    private final ModelDao modelDao;

    private final FileAndDatasetNameService fileAndDatasetNameService = new FileAndDatasetNameService();

    private static Map<String, String> eventPropertyAndLabelMap = new HashMap<>();
    private static Map<String, String> figurePropertyAndLabelMap = new HashMap<>();
    private static Map<String, String> locationPropertyAndLabelMap = new HashMap<>();

    public SearchService(int applicationMode) {
        this.modelDao = new ModelDao(fileAndDatasetNameService.getDatasetNamesMap(applicationMode));
    }

    public Map<Object, Object> findEntityByGivenName(TargetModel targetModel, String id) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(targetModel.getPrefix()).append("select ?p ?o where { ").append(targetModel.getName())
                .append(":").append(id).append("_sanguozhi ?p ?o }");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        Map<Object, Object> result = new HashMap<>();
        while(resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            Object key = null;
            Object value = null;
            if (p.isLiteral()) {
                key = p.asLiteral().getString();
            } else {
                key = p.asResource();
            }
            if (o.isLiteral()) {
                value = o.asLiteral().getString();
            } else {
                value = o.asResource();
            }
            if (result.containsKey(key)) {
                Object prevObject = result.get(key);
                if (prevObject instanceof List) {
                    List.class.cast(prevObject).add(value);
                } else {
                    List<Object> prevList = new LinkedList<>();
                    prevList.add(result.get(key));
                    prevList.add(value);
                    result.put(key, prevList);
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    public Map<String, Object> findPropertyByGivenEntityId(TargetModel targetModel, String id, List<String> properties) {
        if (targetModel == null || id == null || CollectionUtils.isEmpty(properties)) {
            return null;
        }
        StringBuilder queryString = new StringBuilder();
        String valuesString = generateValuesForQuery("?p", properties, targetModel.getName());
        queryString.append(SearchConstant.RDFS).append(SearchConstant.OWL).append(targetModel.getPrefix())
                .append("select ?p ?o where { ")
                .append(valuesString)
                .append(targetModel.getName()).append(":").append(id).append("_sanguozhi ?p ?o}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        Map<String, Object> resultMap = new HashMap<>();
        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            String completeProperty = qs.get("p").asResource().toString();
            String currentProperty = "";
            for (String property : properties) {
                if (completeProperty.contains(property)) {
                    currentProperty = property;
                }
            }
            RDFNode o = qs.get("o");
            String currentValue = "";
            if (o.isLiteral()) {
                currentValue = o.asLiteral().getString();
            } else {
                List<String> labels = getLabelsForResources(Arrays.asList(qs.get("o")));
                if (CollectionUtils.isNotEmpty(labels)) {
                    currentValue = labels.get(0);
                }
            }
            if (!currentProperty.isEmpty()) {
                if (!resultMap.containsKey(currentProperty)) {
                    resultMap.put(currentProperty, currentValue);
                } else {
                    if (resultMap.get(currentProperty) instanceof String) {
                        resultMap.put(currentProperty, Arrays.asList(resultMap.get(currentProperty).toString(), currentValue));
                    } else {
                        List.class.cast(resultMap.get(currentProperty)).add(currentValue);
                    }
                }

            }
        }
        return resultMap;
    }

    public List<String> findGivenPropertyContainGivenName(TargetModel targetModel, String id, Property property) {
        if (targetModel == null || id == null || property == null) {
            return null;
        }
        StringBuilder queryString = new StringBuilder();
        queryString.append(targetModel.getPrefix()).append("select ?s ?p ?o where { ")
                .append("?s ").append(targetModel.getName()).append(":").append(property.getProperty()).append(" ?o ")
                .append("FILTER regex(str(?s), \"").append(id).append("\")}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        List<String> result = new ArrayList<>();
        while(resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            RDFNode o = qs.get("o");
            if (o.isLiteral()) {
                result.add(o.asLiteral().getLexicalForm());
            } else {
                result.add(findLabelByGivenId(Property.getTargetModelByProperty(property), o.asResource().getURI()));
            }
        }
        return result;
    }

    public List<String> findEntitiesByPredicateAndObject(TargetModel targetModel, String predicate, String object,
            String objectPrefix) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(targetModel.EVENT.getPrefix()).append(targetModel.FIGURE.getPrefix())
                .append(targetModel.LOCATION.getPrefix()).append(targetModel.TIME.getPrefix()).
                append("select ?s where {?s ").append(targetModel.getName())
                .append(":").append(predicate).append(" ").append(objectPrefix).append(":").append(object)
                .append("_sanguozhi}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        List<Object> resources = new LinkedList<>();
        while (resultSet.hasNext()) {
            RDFNode s = resultSet.next().get("s");
            if (s.isResource()) {
                resources.add(s.asResource());
            }
        }
        return getLabelsForResources(resources);
    }

    public List<String> findLabelsByRegexValue(TargetModel targetModel, String predicate, String regex) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(targetModel.getPrefix()).append(SearchConstant.RDFS)
                .append("select ?s where {?s ").append(targetModel.getName()).append(":").append(predicate)
                .append(" ?o FILTER regex(str(?o), \"").append(regex).append("\")}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        List<Object> resources = new LinkedList<>();
        while (resultSet.hasNext()) {
            RDFNode s = resultSet.next().get("s");
            if (s.isResource()) {
                resources.add(s.asResource());
            }
        }
        return getLabelsForResources(resources);
    }

    public boolean checkDataAvailable() {
        for(ModelConstant.ModelNames name : ModelConstant.ModelNames.values()) {
            if (!modelDao.testModel(name.get())) {
                return false;
            }
        }
        return true;
    }

    private String findLabelByGivenId(TargetModel targetModel, String id) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(SearchConstant.RDFS).append("select ?s ?p ?o where { <").append(id).append("> rdfs:label ?o}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        String result = StringUtils.EMPTY;
        while(resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            RDFNode o = qs.get("o");
            result = o.asLiteral().getLexicalForm();
        }
        return result;
    }

    public Map<String, String> getAllPropertiesForModel(TargetModel targetModel) {
        StringBuilder queryString = new StringBuilder();
        queryString.append(SearchConstant.RDFS).append(SearchConstant.OWL).append(targetModel.getPrefix())
                .append("select distinct ?p where {?s ?p ?o}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        List<String> propertyIds = new LinkedList<>();
        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            String p = qs.get("p").toString();
            if (p.contains(targetModel.getOwlIndex())) {
                propertyIds.add(p);
            }
        }
        String valuesString = generateValuesForQuery("?s", propertyIds, null);
        queryString.setLength(0);
        queryString.append(SearchConstant.RDFS).append(SearchConstant.OWL)
                .append("select ?s ?o where {").append(valuesString).append("?s rdfs:label ?o}");
        resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        Map<String, String> propertyAndLabelMap = new HashMap<>();
        while (resultSet.hasNext()) {
            QuerySolution qs = resultSet.next();
            String o = qs.get("o").asLiteral().getString();
            String s = qs.get("s").toString();
            propertyAndLabelMap.put(s, o);
        }
        return propertyAndLabelMap;
    }

    public Map<String, String> getPropertyMapByEntityId(TargetModel targetModel, String id) {
        Map<Object, Object> propertyAndValueMap = findEntityByGivenName(targetModel, id);
        Map<String, String> propertyAndLabelMap = getPropertyAndLabelMap(targetModel);
        Map<String, String> propertyMap = new HashMap<>();
        propertyAndValueMap.forEach((property, value) -> {
            if (propertyAndLabelMap.containsKey(property.toString())) {
                if (value instanceof String) {
                    propertyMap.put(propertyAndLabelMap.get(property.toString()), value.toString());
                } else if (value instanceof List) {
                    List<Object> valueList = List.class.cast(value);
                    List<String> labels;
                    if (valueList.get(0) instanceof String) {
                        labels = valueList.stream().map(Object::toString).collect(Collectors.toList());
                    } else {
                        labels = getLabelsForResources(List.class.cast(value));
                    }
                    StringBuilder valueString = new StringBuilder();
                    for (String label : labels) {
                        valueString.append(label).append(", ");
                    }
                    valueString.substring(0, valueString.length() - 2);
                    propertyMap.put(propertyAndLabelMap.get(property.toString()), valueString.toString());
                } else if (value instanceof Resource) {
                    List<String> labels = getLabelsForResources(Arrays.asList(value));
                    propertyMap.put(propertyAndLabelMap.get(property.toString()), labels.get(0));
                }
            }
        });
        return propertyMap;
    }

    private Map<String, String> getPropertyAndLabelMap(TargetModel targetModel) {
        Map<String, String> resultMap = new HashMap<>();
        if (targetModel.equals(TargetModel.EVENT)) {
            if (MapUtils.isEmpty(eventPropertyAndLabelMap)) {
                eventPropertyAndLabelMap = getAllPropertiesForModel(targetModel);
            }
            resultMap = eventPropertyAndLabelMap;
        } else if (targetModel.equals(TargetModel.FIGURE)) {
            if (MapUtils.isEmpty(figurePropertyAndLabelMap)) {
                figurePropertyAndLabelMap = getAllPropertiesForModel(targetModel);
            }
            resultMap = figurePropertyAndLabelMap;
        } else if (targetModel.equals(TargetModel.LOCATION)) {
            if (MapUtils.isEmpty(locationPropertyAndLabelMap)) {
                locationPropertyAndLabelMap = getAllPropertiesForModel(targetModel);
            }
            resultMap = locationPropertyAndLabelMap;
        }
        return resultMap;
    }

    private List<String> getLabelsForResources(List<Object> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return null;
        }
        TargetModel targetModel = null;
        String resource = resources.get(0).toString();
        List<String> ids = resources.stream().map(r -> Resource.class.cast(r).toString())
                .collect(Collectors.toList());
        if (resource.contains(TargetModel.EVENT.getOwlIndex())) {
            targetModel = TargetModel.EVENT;
        } else if (resource.contains(TargetModel.FIGURE.getOwlIndex())) {
            targetModel = TargetModel.FIGURE;
        } else if (resource.contains(TargetModel.TIME.getOwlIndex())) {
            targetModel = TargetModel.TIME;
        } else if (resource.contains(TargetModel.LOCATION.getOwlIndex())) {
            targetModel = TargetModel.LOCATION;
        }
        return getLabelsForEntities(targetModel, ids);
    }

    private List<String> getLabelsForEntities(TargetModel targetModel, List<String> entityIds) {
        StringBuilder queryString = new StringBuilder();
        String valuesString = generateValuesForQuery("?s", entityIds, null);
        queryString.append(SearchConstant.RDFS).append(targetModel.getPrefix())
                .append(" select ?o where {").append(valuesString).append(" ?s rdfs:label ?o}");
        ResultSet resultSet = modelDao.queryModel(targetModel.getModelName().get(), queryString.toString());
        List<String> result = new LinkedList<>();
        while (resultSet.hasNext()) {
            result.add(resultSet.next().get("o").asLiteral().getString());
        }
        return result;
    }

    private String generateValuesForQuery(String name, List<String> valuesList, String prefix) {
        StringBuilder values = new StringBuilder("VALUES ");
        values.append(name).append(" {");
        for (String value : valuesList) {
            if (Objects.isNull(prefix)) {
                values.append("<").append(value).append("> ");
            } else {
                values.append(prefix).append(":").append(value).append(" ");
            }
        }
        return values.append("} ").toString();
    }

//    private String getCurrentRootDir() {
//        File file = new File(this.getClass().getResource("/").getFile());
//        int i = 4;
//        while (i-- > 0) {
//            file = file.getParentFile();
//        }
//        return file.getAbsolutePath();
//    }

}
