package com.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by cao_y on 2018/1/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/QA")
public class QAController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView QAIndex() {
        ModelAndView modelAndView = new ModelAndView("QAIndex.jsp");
        modelAndView.addObject("dataAvailable", "false");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String QAIndexRedirect() {
        return "redirect:/QA";
    }
}
