package criteria;

import constants.SearchConstant.TargetModel;

/**
 * Created by cao_y on 2018/1/24.
 */
public class SearchParameter {

    private TargetModel targetModel = null;
    private String subject = null;
    private String property = null;

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

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
