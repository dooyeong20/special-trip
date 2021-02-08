package com.project.mega.triplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/blog")
    public String about(){

        return "view/blog";
    }


    @GetMapping("/tour")
    public String tour(){

        return "view/tour";
    }
}
