package com.zealren.sequence.controller;

import com.zealren.sequence.formatter.SequenceFormatter;
import com.zealren.sequence.generator.SequenceGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/sequence")
public class MainController {

    @Resource
    private SequenceGenerator redisSequenceGenerator;
    @Resource
    private SequenceFormatter commonSequenceFormatter;


    @GetMapping("/index")
    public ModelAndView index(Model model) throws Exception {
        ModelAndView view = new ModelAndView("index");
        List<String> nos = redisSequenceGenerator.generate("ORDER", "1", "ShipmentNo", 1L,
                commonSequenceFormatter, null);
        view.addObject("sequenceNo", nos.get(0));
        return view;
    }
}
