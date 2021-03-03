package com.project.mega.triplus.controller;

import com.google.gson.JsonObject;
import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.form.JoinForm;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PlaceRepository placeRepository;

    private final PlaceService placeService;

    private final ApiService apiService;

    private final UserService userService;

    private final HttpSession  httpSession;

    private final PlanService planService;


    @Transactional
    @PostConstruct
    public void init(){
        // 맨 처음 place 들(관광지, 숙소, 축제 등)을 우리 데이터베이스로 load 해옴
        if(!apiService.loadPlaces()){
            log.error(" !!! data load error !!! ");
        }

       planService.createDummyData();
    }

    @RequestMapping("/")
    public String index(Model model){
        List<Place> placeList = placeService.getPlace();
        List<Plan> planList = planService.getAllByOrderByLikedDesc();

        model.addAttribute("placeList", placeList);
        model.addAttribute("planList", planList);

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
    public String detail(
            @CurrentUser User user,
            @RequestParam(value = "content_id") String contentId, Model model){
        String radius = "50000";
        Place place;

        int rand, cnt = 10;

        XMLResponseItem item = apiService.getItemByContentId(contentId);
        List<XMLResponseItem> recommendPlaces = apiService.getItemByMapXAndMapY(item.getMapX(), item.getMapY(), radius, "12");
        place = placeService.getPlaceByContentId(contentId);

        // ===============================
//        Review sampleReview = new Review();
//        sampleReview.setTitle("샘플 리뷰 제목 1");
//        sampleReview.setContent("샘플 컨텐츠 1");
//        place.getReviews().add(sampleReview);
//        sampleReview.setTitle("샘플 리뷰 제목 2");
//        sampleReview.setContent("샘플 컨텐츠 2");
//        place.getReviews().add(sampleReview);
        // ===============================

        rand = Math.max((int) (Math.random() * (recommendPlaces.size() - cnt)), 0);

        model.addAttribute("item", item);
        model.addAttribute("reviews", place.getReviews());
        model.addAttribute("content_id", contentId);
        model.addAttribute("recommendPlaces", recommendPlaces.subList(rand, rand + Math.min(recommendPlaces.size(), cnt)));

        return "view/detail";
    }

    @PostMapping("/register_review")
    public String review(
            @CurrentUser User user, Model model,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "content_id") String contentId){

        Place place = placeService.getPlaceByContentId(contentId);
        placeService.saveReview(user, place, content);

        return detail(user,contentId,model);
    }


    @GetMapping("/plan")
    public String plan(Model model){
        int rand, cnt = 10;

        List<Place> placeList = placeRepository.findAllByContentType("12");

        rand = Math.max((int) (Math.random() * (placeList.size() - cnt)), 0);

        model.addAttribute("placeList", placeList.subList(rand, rand + Math.min(placeList.size(), cnt)));

        return "view/plan";
    }

    @GetMapping("/widgets")
    public String w(){
        return "view/admin/widgets";
    }

    @GetMapping("/admin")
    public String admin(){

        return "view/admin/admin";
    }



    @GetMapping("/detail/like")
    @ResponseBody  // 리턴값 (String)은 view 이름이 아니라 responseBody 부분이다!
    public String addLike(@CurrentUser User user,
                @RequestParam(value = "content_id") String contentId){

        JsonObject object = new JsonObject();

        // contentId : 찜 당할 장소의 content_id
        // user : 현재 로그인한 유저

        // Place : liked 1 증가
        // User : likes 리스트에 해당 place 를 추가
        try {
            placeService.addLikes(user, contentId);
            object.addProperty("result", true);
            object.addProperty("message", "찜 목록에 등록되었습니다.");
        } catch (IllegalStateException e){
            object.addProperty("result", false);
            object.addProperty("message", e.getMessage());
        }
        logger.info("찜 결과 : " + object.toString());

        return object.toString();
    }

    @GetMapping("/mypage")
    public String mypage(@CurrentUser User user, Model model){
        if(user == null ){
            user = (User)httpSession.getAttribute("user");
        }
        model.addAttribute("user", user);

        return "view/mypage";
    }


    @PostMapping("/mypage/like")
        public String likeList(@CurrentUser User user, Model model){
        List<Place> likeList = userService.getLikeList(user);

        model.addAttribute("likeList", likeList);

        return "view/mypage";
    }


    @GetMapping("/total_plan")
    public String totalPlan(Model model){
        List<Plan> allPlans = planService.getAllPlans();

        Set<String> citySet = new HashSet<>(Arrays.asList("1", "2", "31", "32", "6", "7", "4", "5", "3", "38", "39"));


        model.addAttribute("planList", allPlans
        .stream().filter(city -> citySet.contains(city.getMainAreaCode())).collect(Collectors.toList()));

        return "view/total_plan";
    }

    @GetMapping("/total_place")
    public String totalPlace(Model model, final Pageable pageable, String requestCode){
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
        //Set<String> citySet = new HashSet<>(Arrays.asList("1", "2", "31", "32", "6", "7", "4", "5", "3", "38", "39"));

        Set<String> citySet = new HashSet<>(Arrays.asList("1", "2", "31", "32", "6", "7", "4", "5", "3", "38", "39"));

        model.addAttribute("placeList", placeRepository.findAllByContentType("12")
                .stream().filter(city -> citySet.contains(city.getAreaCode())).collect(Collectors.toList()));

        return "view/total_place";
    }

    @GetMapping("/access_denied")
    public String accessDenied()
    {
        return "view/access_denied";
    }


    @PostMapping("/mypage/delete")
    public String userDelete(@CurrentUser User user,
                             @RequestParam(value = "item_id", required = false)String[] itemIds,
                             Model model){

        if(itemIds != null && itemIds.length != 0){
            List<Long> idList = List.of(Arrays.stream(itemIds).map(Long::parseLong).toArray(Long[]::new));
            userService.deleteUser(user, idList);
        }

        return "index";
    }

    // 하림님 회원가입 문제 !!  ////////////////////////////////////
    @GetMapping("/harim")
    public String harim(){
        JoinForm joinForm = new JoinForm();
        joinForm.setEmail("harim@email.com");
        joinForm.setPassword("harim");
        joinForm.setNickname("harim");
        joinForm.setAgreeTermsOfService("true");
        userService.login(userService.processNewUser(joinForm));

        return "index";
    }
    ///////////////////////////////////////////////////////////

    @PostMapping("/plan/save")
    @ResponseBody
    public String savePlan(@CurrentUser User user,
            @RequestBody List<List<Map<String, String>>> planList){
        Plan plan = new Plan();
        Day day;
        Place place;

        plan.setUser(user);

        for(List<Map<String, String>> d : planList){
            day = new Day();

            for(Map<String, String> p : d){
                place = new Place();
                place.setName(p.get("title"));
                place.setAddr(p.get("addr"));
                place.setThumbnailUrl(p.get("imgUrl"));
                place.setContentId(p.get("content_id"));
                day.addPlace(place);
            }

            day.setPlan(plan);
        }

        planService.savePlan(plan);

        return "done";
    }

}
