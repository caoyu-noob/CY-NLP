package com.criteria;

import com.constants.SearchConstant.Property;
import com.constants.SearchConstant.TargetModel;

/**
 * Created by cao_y on 2018/1/24.
 */
public class SearchParameter {

    private TargetModel targetModel = null;
    private String subject = null;
    private Property property = null;

    public TargetModel getTargetModel() {
        return this.targetModel;
    }

    public void setTargetModel(TargetModel model) {
        this.targetModel = model;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
