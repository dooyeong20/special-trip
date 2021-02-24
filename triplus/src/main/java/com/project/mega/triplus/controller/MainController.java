package com.project.mega.triplus.controller;

import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.oauth2.LoginUser;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.service.ApiService;
import com.project.mega.triplus.service.CurrentUser;
import com.project.mega.triplus.service.PlaceService;
import com.project.mega.triplus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final PlaceRepository placeRepository;

    private final PlanRepository planRepository;

    private final PlaceService placeService;

    private final ApiService apiService;

    private final UserService userService;

    private final HttpSession  httpSession;


    @Transactional
    @PostConstruct
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

        plan1.setName("나의 첫번째 여행");
        plan1.setStatus(PlanStatus.COMPLETE);
        plan1.setLiked(10);
        plan1.setUpdate(LocalDateTime.now());
        plan1.setDays(List.of(plan1_day1,plan1_day2));
        planRepository.save(plan1);

//        List<List<String>> imageUrlsList = new ArrayList<>();
//        imageUrlsList.add(plan1_day1.getPlaces().get(0).getImageUrls());
//        imageUrlsList.add(plan1_day1.getPlaces().get(1).getImageUrls());
//        imageUrlsList.add(plan1_day2.getPlaces().get(0).getImageUrls());
//        imageUrlsList.add(plan1_day2.getPlaces().get(1).getImageUrls());
//
//        System.out.println(imageUrlsList);
    }

    @RequestMapping("/")
    public String index(Model model){
        List<Place> placeList = placeService.getPlace();
        List<Plan> planList = planRepository.findAllByOrderByLikedDesc();

        model.addAttribute("placeList", placeList);
        model.addAttribute("planList", planList);

        return "index";
    }

    @GetMapping("/loginSuccess")
    public String loginComplete(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("status", "login");

        return index(model);
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model){
        int rand, cnt = 4;

        List<XMLResponseItem> itemList = apiService.getKeywordResultList(keyword);
        List<XMLResponseItem> attractionList = new ArrayList<>();
        List<XMLResponseItem> foodList = new ArrayList<>();
        List<XMLResponseItem> shopList = new ArrayList<>();
        List<XMLResponseItem> festivalList = new ArrayList<>();

        for(XMLResponseItem item : itemList){
            switch (item.getContentTypeId()){
                case "12":
                    attractionList.add(item);
                    break;
                case "39":
                    foodList.add(item);
                    break;
                case "38":
                    shopList.add(item);
                    break;
                case "15":
                    festivalList.add(item);
                default:
                    break;
            }
        }

        model.addAttribute("keyword", keyword);
        // null check
        // model.addAttribute("itemList", itemList.subList(rand, rand + cnt));
        rand = Math.max((int)(Math.random() * (attractionList.size() - cnt)), 0);
        model.addAttribute("attractionList", attractionList.subList(rand, Math.min(rand + cnt, attractionList.size())));


        rand = Math.max((int)(Math.random() * (foodList.size() - cnt)), 0);
        model.addAttribute("foodList", foodList.subList(rand, Math.min(rand + cnt, foodList.size())));

        rand = Math.max((int)(Math.random() * (shopList.size() - cnt)), 0);
        model.addAttribute("shopList", shopList.subList(rand, Math.min(rand + cnt, shopList.size())));

        rand = Math.max((int)(Math.random() * (festivalList.size() - cnt)), 0);
        model.addAttribute("festivalList", festivalList.subList(rand, Math.min(rand + cnt, festivalList.size())));


        return "view/search";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(value = "content_id") String contentId, Model model){
        String radius = "50000";
        int rand, cnt = 10;

        XMLResponseItem item = apiService.getItemByContentId(contentId);
        List<XMLResponseItem> recommendPlaces = apiService.getItemByMapXAndMapY(item.getMapX(), item.getMapY(), radius, "12");
        rand = Math.max((int) (Math.random() * (recommendPlaces.size() - cnt)), 0);

        model.addAttribute("item", item);
        model.addAttribute("recommendPlaces", recommendPlaces.subList(rand, rand + Math.min(recommendPlaces.size(), cnt)));

        return "view/detail";
    }


    @GetMapping("/plan")
    public String plan(){ return "view/plan"; }

    @GetMapping("/widgets")
    public String w(){
        return "view/admin/widgets";
    }

    @GetMapping("/admin")
    public String admin(){
        return "view/admin/admin";
    }

    @GetMapping("/mypage")
    public String mypage(@CurrentUser User user, Model model){
        if(user == null){
            user = (User)httpSession.getAttribute("user");
        }
        String nickName = user.getNickName();
        model.addAttribute("nickName", nickName);

        return "view/mypage";
    }

    @GetMapping("/total_plan")
    public String totalPlan(){
        return "view/total_plan";
    }

    @GetMapping("/total_place")
    public String totalPlace(Model model){
        /*
        < areaCode >

        1 서울
        2 인천
        3 대전
        4 대구
        5 광주
        6 부산
        7 울산
        8 세종특별자치시
        31 경기도
        32 강원도
        33 충청북도
        34 충청남도
        35 경상북도
        36 경상남도
        37 전라북도
        38 전라남도
        39 제주도
         */
        Set<String> citySet = new HashSet<>(Arrays.asList("1", "2", "31", "32", "6", "7", "4", "5", "3", "38", "39"));

        model.addAttribute("placeList", placeRepository.findAllByContentType("12")
                .stream().filter(city -> citySet.contains(city.getAreaCode())).collect(Collectors.toList()));

        return "view/total_place";
    }

    @GetMapping("/access_denied")
    public String accessDenied(){
        return "view/access_denied";
    }

}
