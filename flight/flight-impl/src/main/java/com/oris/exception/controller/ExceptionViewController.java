package com.oris.exception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exception")
public class ExceptionViewController {

    @GetMapping("/401")
    public String handle401() {
        return "exception/401";
    }

    @GetMapping("/403")
    public String handle403() {
        return "exception/403";
    }
}
