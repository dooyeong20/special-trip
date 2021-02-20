package com.project.mega.triplus.controller;


import com.project.mega.triplus.entity.Day;
import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Plan;
import com.project.mega.triplus.entity.PlanStatus;
import com.project.mega.triplus.entity.XMLResponseItem;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.service.ApiService;
import com.project.mega.triplus.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final PlaceRepository placeRepository;

    private final PlanRepository planRepository;

    private final PlaceService placeService;

    private final ApiService apiService;


    @Transactional

//    @PostConstruct

    public void init(){
        // 맨 처음 place 들(관광지, 숙소, 축제 등)을 우리 데이터베이스로 load 해옴
        if(!apiService.loadPlaces()){
            log.error(" !!! data load error !!! ");
        }

        // 추천일정 top3 더미데이터 생성하기
        Plan plan1=new Plan();
        Day plan1_day1=new Day();
        Day plan1_day2=new Day();

        // Place 테이블에서 실제 장소 데이터 빼오기.
        Optional<Place> place1=placeRepository.findById(250L);
        Optional<Place> place2=placeRepository.findById(350L);
        Optional<Place> place3=placeRepository.findById(450L);
        Optional<Place> place4=placeRepository.findById(550L);

        if(place1.isPresent() && place2.isPresent() && place3.isPresent() && place4.isPresent()){

            // day1에 장소들 임의로 추가.
            plan1_day1.addPlace(place1.get());
            plan1_day1.addPlace(place2.get());
            plan1_day2.addPlace(place3.get());
            plan1_day2.addPlace(place4.get());

            // plan1_day1, plan1_day2 은 plan1 소속이다.
            plan1_day1.setPlan(plan1);
            plan1_day2.setPlan(plan1);

            plan1_day1.setName("FirstDay");
            plan1_day2.setName("SecondDay");
        }

        // TODO plan1_day1, plan1_day2 는 index 로 어떻게 넘겨야하는가? (day1, day2 에 속한 place 리스트도)

        plan1.setName("나의 첫번째 여행");
        plan1.setStatus(PlanStatus.COMPLETE);
        plan1.setLiked(10);
        plan1.setUpdate(LocalDateTime.now());
        plan1.setDays(List.of(plan1_day1,plan1_day2));
        planRepository.save(plan1);

        // TODO Place 의 imageUrl 은 어디서 어떻게 가져와야하는가?

//        List<List<String>> imageUrlsList = new ArrayList<>();
//        imageUrlsList.add(plan1_day1.getPlaces().get(0).getImageUrls());
//        imageUrlsList.add(plan1_day1.getPlaces().get(1).getImageUrls());
//        imageUrlsList.add(plan1_day2.getPlaces().get(0).getImageUrls());
//        imageUrlsList.add(plan1_day2.getPlaces().get(1).getImageUrls());
//
//        System.out.println(imageUrlsList);
    }


    @GetMapping("/")
    public String index(Model model){
        List<Place> placeList = placeService.getPlace();
        List<Plan> planList = planRepository.findAllByOrderByLikedDesc();


        model.addAttribute("placeList", placeList);
        model.addAttribute("planList", planList);

        return "index";
    }

    @GetMapping("/search")
    public String search(){
        return "view/search";
    }


    @GetMapping("/detail")
    public String detail(@RequestParam(value = "content_id") String contentId, Model model){
        XMLResponseItem item = apiService.getItemByContentId(contentId);
        model.addAttribute("item", item);
        return "view/detail";
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

    @GetMapping("/total_plan")
    public String totalPlan(){
        return "view/total_plan";
    }

    @GetMapping("/total_place")
    public String totalPlace(){
        return "view/total_place";
    }

    @GetMapping("/access_denied")
    public String accessDenied(){
        return "view/access_denied";
    }
}
