package com.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cao_y on 2018/1/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/QA")
public class QAController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String QAIndex() {
        System.out.println("11");
        return "QAIndex.jsp";
    }
}
