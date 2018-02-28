package com.criteria.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.THULACCate;

import io.github.yizhiru.thulac4j.model.SegItem;

/**
 * Created by cao_y on 2018/2/12.
 */
public class Event implements Templates {

    public static class templates implements Templates.templates {

        private final static List<List<SegItem>> template = new ArrayList<List<SegItem>>(){
            {
                //XX（地名）之战 e.g. 赤壁之战
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.PLACE.getValue()),
                    new SegItem("之", THULACCate.AUX.getValue()),
                    new SegItem("[战乱役围]", THULACCate.ANY.getValue())
                ));
                //XX(方位)之战
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.DIRECT.getValue()),
                    new SegItem("之", THULACCate.AUX.getValue()),
                    new SegItem("[战乱役围]", THULACCate.ANY.getValue())
                ));
                //XX(名词)之战
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.NOUN.getValue()),
                    new SegItem("之", THULACCate.AUX.getValue()),
                    new SegItem("[战乱役围]", THULACCate.ANY.getValue())
                ));
                //XX（人名）入XX（地名） e.g. 刘备入益州
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.PERSON.getValue()),
                    new SegItem("(进|入|出)", THULACCate.VERB.getValue()),
                    new SegItem(".*", THULACCate.PLACE.getValue())
                ));
                //习语
                add(Arrays.asList(
                    new SegItem("(单刀赴会|挟天子以令诸侯|三顾茅庐)", THULACCate.CUSTOM.getValue())
                ));
                //XX(人名）之X e.g. 董卓之乱
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.PERSON.getValue()),
                    new SegItem("之", THULACCate.AUX.getValue()),
                    new SegItem("[乱叛]", THULACCate.VERB.getValue())
                ));
                //XX(人名)X(动词)XX(人名) 曹操征张鲁
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.PERSON.getValue()),
                    new SegItem("(征|废|诛|诛杀|杀)", THULACCate.VERB.getValue()),
                    new SegItem(".*", THULACCate.PERSON.getValue())
                ));
                //XX（人名）XX（行为） 袁术称帝
                add(Arrays.asList(
                    new SegItem(".*", THULACCate.PERSON.getValue()),
                    new SegItem("(称帝|谋反)", THULACCate.ANY.getValue())
                ));
                //XX事件
                add(Arrays.asList(
                    new SegItem("((?!(什么|哪些|哪个)).)*[^何啥]", THULACCate.ANY.getValue()),
                    new SegItem("事件", THULACCate.ANY.getValue())
                ));
                add(Arrays.asList(
                    new SegItem("赤壁", THULACCate.ANY.getValue()),
                    new SegItem("之", THULACCate.ANY.getValue()),
                    new SegItem("战", THULACCate.ANY.getValue())
                ));
                add(Arrays.asList(
                    new SegItem("赤", THULACCate.ANY.getValue()),
                    new SegItem("壁", THULACCate.ANY.getValue()),
                    new SegItem("之", THULACCate.ANY.getValue()),
                    new SegItem("战", THULACCate.ANY.getValue())
                ));
                add(Arrays.asList(
                    new SegItem("官", THULACCate.ANY.getValue()),
                    new SegItem("渡", THULACCate.ANY.getValue()),
                    new SegItem("之", THULACCate.ANY.getValue()),
                    new SegItem("战", THULACCate.ANY.getValue())
                ));
            }
        };

        // provide the matching mode foreach template
        // 3 = regex and word property matching mode, 2 = only regex matching mode, 1 = only word property matching mode
        private final static List<Integer> matchMode = new ArrayList<Integer>() {
            {
                add(3);
                add(3);
                add(3);
                add(3);
                add(3);
                add(3);
                add(3);
                add(3);
                add(2);
                add(2);
                add(2);
                add(2);
            }
        };

        //the error limits for each template
        // matching errors more than the limitations will be regarded as a failure matching, -1 means no limitation
        private final static List<Integer> errorLimits = new ArrayList<Integer>() {
            {
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
            }
        };

        @Override
        public List<List<SegItem>> getTemplate() {
            return template;
        }

        @Override
        public List<Integer> getMatchMode() {
            return matchMode;
        }

        @Override
        public List<Integer> getErrorLimits() {
            return errorLimits;
        }
    }
}
