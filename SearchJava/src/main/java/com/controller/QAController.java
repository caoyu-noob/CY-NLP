package com.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.service.AnswerService;
import com.service.FileReader;
import com.service.SearchService;
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
    private final SearchService searchService;
    private final FileReader fileReader;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView QAIndex() {
        ModelAndView modelAndView = new ModelAndView("QAIndex.jsp");
        String validity = searchService.checkDataAvailable() ? "true" : "false";
        modelAndView.addObject("dataAvailable", validity);
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

    @RequestMapping(value = "/resetData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> resetData() {
        Map<String, String> res = new HashMap<>();
        List<String> checkList = fileReader.checkOwlFileValidity();
        if (CollectionUtils.isEmpty(checkList)) {
            try {
                fileReader.saveOwlModel();
            } catch (Exception e) {
                e.printStackTrace();
                res.put("isSuccess", "false");
                String message = "存储中出现错误: \n";
                res.put("message", message + e.toString());
                return res;
            }
            res.put("isSuccess", "true");
            res.put("message", "重置数据成功！");
        } else {
            res.put("isSuccess", "false");
            String message = "无法重置数据库，以下文件读取出错: \n";
            for (String fileName : checkList) {
                message = message + fileName +"\n";
            }
            res.put("message", message);
        }
        return res;
    }
}
