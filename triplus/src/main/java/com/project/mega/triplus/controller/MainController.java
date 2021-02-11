package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.XMLResponse;
import com.project.mega.triplus.entity.XMLResponseItem;
import com.project.mega.triplus.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final ApiService apiService;

    @PostConstruct
    public void init(){
        // 맨 처음 place 들(관광지, 숙소, 축제 등)을 우리 데이터베이스로 load 해옴
        if(!apiService.loadPlaces()){
            log.error(" !!! data load error !!! ");
        }

    }

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
