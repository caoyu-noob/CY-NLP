package com.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.service.AnswerService;
import com.vo.Answer;

/**
 * Created by cao_y on 2018/1/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/QA")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QAController {

    private final AnswerService answerService;

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

    @RequestMapping(value = "/ask", method = RequestMethod.POST)
    @ResponseBody
    public Answer QAAnswerQuestion(@RequestParam String question) {
        String answer = answerService.AnswerQuestion(question);
        return Answer.of(answer);
    }
}
