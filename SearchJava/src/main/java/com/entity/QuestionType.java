package com.entity;

/**
 * Created by cao_y on 2018/2/26.
 */
public enum QuestionType {
    // the subject of the question is location, e.g. XX战役在哪发生的, XX是哪里人
    WHERE,
    // the subject of the question is time, e.g. XX之战什么时候发生的，XX什么时候出生的
    WHEN,
    // the subject of the question is person, e.g. XX之战有谁参与
    WHO,
    // the subject of the question is event, e.g. XX参加了什么事件
    WHAT,
    // the subject is person introduction, e.g. XX是谁（该问题回答方式会将实体的所有相关属性一一列出）
    PERSON_INTRODUCTION,
    // the subject is event introduction, e.g. 介绍下XX事件 （该问题回答方式会将实体的所有相关属性一一列出）
    EVENT_INTRODUCTION,
    // the subject is some other property, e.g. XX事件的影响， XX的官职
    OTHER,
    // cannot understand the question at the first stage
    UNKNOW;

}
