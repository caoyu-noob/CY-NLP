package com.constants;

import lombok.Getter;

public enum THULACCate {
    NOUN("n"),//名词
    PERSON("np"),//人名
    PLACE("ns"),//地名
    ORG("ni"),//机构名
    OTHERSPEC("nz"),//其它专名
    NUMBER("m"),//数词
    QUANTITY("q"),//量词
    NUMBERQUANTITY("mq"),//数量词
    TIME("t"),//时间词
    DIRECT("f"),//方位词
    SPACE("s"),//处所词
    VERB("v"),//动词
    ADJECTIVE("a"),//形容词
    ADVERB("d"),//副词
    PREFIX("h"),//前接成分
    POSTFIX("k"),//后接成分
    CUSTOM("i"),//习语
    ABBREVIATION("j"),//简称
    PRONOUN("r"),//代词
    CONJUCTION("c"),//连词
    PREPOSTION("p"),//介词
    AUX("u"),//助词
    MODAL("y"),//语气助词
    INTERJ("e"),//叹词
    SOUND("o"),//拟声词
    MORPHEME("g"),//语素
    COMMA("w"),//标点
    OTHER("x"),
    ANY("*");//其它

    @Getter
    private String value;

    private THULACCate(String value) {
        this.value = value;
    }
}
