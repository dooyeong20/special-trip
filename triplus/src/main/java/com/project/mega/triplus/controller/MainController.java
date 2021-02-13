package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.service.PlaceService;
import com.project.mega.triplus.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.mega.triplus.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {


    private final PlanService planService;

    private final PlanRepository planRepository;

    private final PlaceService placeService;

    private final ApiService apiService;


    @PostConstruct
    public void init(){
        // 맨 처음 place 들(관광지, 숙소, 축제 등)을 우리 데이터베이스로 load 해옴
        if(!apiService.loadPlaces()){
            log.error(" !!! data load error !!! ");
        }

        // 추천일정 top3 더미데이터 생성하기
        Plan plan1=new Plan();
        plan1.setName("제주도 여행");
        plan1.setStatus(PlanStatus.COMPLETE);
        plan1.setLiked(10);
        plan1.setUpdate(LocalDateTime.now());

        Plan plan2=new Plan();
        plan2.setName("부산 여행");
        plan2.setStatus(PlanStatus.COMPLETE);
        plan2.setLiked(6);
        plan2.setUpdate(LocalDateTime.now());

        planRepository.save(plan1);
        planRepository.save(plan2);
    }

    @GetMapping("/")
    public String index(Model model){
        List<Place> placeList = placeService.getPlace();
        List<Plan> planList = planRepository.findAllByOrderByLikedDesc();

        model.addAttribute("placeList", placeList);
        model.addAttribute("planList", planList);

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

//    // mypage controller
//    @GetMapping("/mypage_main")
//    public String mypage(){
//        return "view/mypage/mypage_main";
//    }
//
//
//    @GetMapping("/mypage_info")
//    public String mypage_info(){return "view/mypage/mypage_info"; }
//
//    @GetMapping("/mypage_like")
//    public String mypage_like(){return "view/mypage/mypage_like"; }
//
//    @GetMapping("/mypage_review")
//    public String mypage_review(){return "view/mypage/mypage_review"; }
//
//    @GetMapping("/mypage_plan")
//    public String mypage_plan(){return "view/mypage/mypage_plan";}

    @GetMapping("/admin")
    public String admin(){
        return "view/admin/admin";
    }

    @GetMapping("/mypage")
    public String mypage(){
        return "view/mypage";
    }

}
