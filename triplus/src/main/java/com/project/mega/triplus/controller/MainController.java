package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.service.PlaceService;
import com.project.mega.triplus.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private PlanService planService;
    @Autowired
    private PlaceService placeService;

    @GetMapping("/")
    public String index(Model model){

        List<Place> placeList = placeService.getPlace();
        model.addAttribute("placeList",placeList);
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

    @GetMapping("/widgets")
    public String w(){
        return "view/admin/widgets";
    }

    // mypage controller
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

    @GetMapping("/mypage_plan")
    public String mypage_plan(){return "view/mypage/mypage_plan";}

    @GetMapping("/admin")
    public String admin(){
        return "view/admin/admin";
    }

}
