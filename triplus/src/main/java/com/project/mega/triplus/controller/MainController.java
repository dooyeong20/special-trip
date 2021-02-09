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
    public String blog(){
        return "view/blog";
    }


    @GetMapping("/tour")
    public String tour(){
        return "view/tour";
    }


    @GetMapping("/plan")
    public String plan(){

        return "view/plan";
    }



    @GetMapping("/mypage_main")
    public String mypage(){
        return "view/mypage/mypage_main";
    }

    @GetMapping("/pre")
    public String pre(){
        return "view/pre";
    }

    @GetMapping("/template")
    public String template(){
        return "view/template";
    }


}
