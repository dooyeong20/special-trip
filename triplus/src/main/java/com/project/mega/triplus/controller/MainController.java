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

    // mypage controller

    @GetMapping("/widgets")
    public String w(){
        return "view/admin/widgets";
    }


    @GetMapping("/mypage_main")
    public String mypage(){
        return "view/mypage/mypage_main";
    }


    @GetMapping("/mypage_info")
    public String mypage_info(){return "view/mypage/mypage_info"; }

    @GetMapping("/mypage_like")
    public String mypage_like(){return "view/mypage/mypage_like"; }

    @GetMapping("/mypage_review")
    public String mypage_review(){return "view/mypage/mypage_review"; }

    @GetMapping("/admin")
    public String admin(){
        return "view/admin/admin";
    }

}
