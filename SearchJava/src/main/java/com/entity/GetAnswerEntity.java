package com.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.constants.THULACCate;

/**
 * Created by cao_y on 2018/2/26.
 */
//the entity contains necessary data so that we can generate SPARQL to find answer
@Getter
@Setter
@AllArgsConstructor
public class GetAnswerEntity {
    private QuestionType questionType;
    private String type;
    private List<String> subjects = null;
    private List<String> objects = null;

    public static GetAnswerEntity getUnkownEntity() {
        return new GetAnswerEntity(QuestionType.UNKNOW, THULACCate.ANY.getValue(), null, null);
    }
}
