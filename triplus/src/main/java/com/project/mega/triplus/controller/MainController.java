package com.project.mega.triplus.controller;

import com.google.gson.JsonObject;
import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.form.JoinForm;
import com.project.mega.triplus.form.PlanForm;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.repository.UserRepository;
import com.project.mega.triplus.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    private final UserRepository userRepository;

    private final PlanService planService;

    private final ReviewService reviewService;


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
        int rand, cnt = 8;

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

        int rand, cnt = 5;

        XMLResponseItem item = apiService.getItemByContentId(contentId);
        List<XMLResponseItem> recommendPlaces_attraction = apiService.getItemByMapXAndMapY(item.getMapX(), item.getMapY(), radius, "12");
        List<XMLResponseItem> recommendPlaces_food = apiService.getItemByMapXAndMapY(item.getMapX(), item.getMapY(), radius, "39");
        place = placeService.getPlaceByContentId(contentId);

        model.addAttribute("item", item);
        model.addAttribute("reviews", place.getReviews());
        model.addAttribute("content_id", contentId);
        model.addAttribute("user", user);

        rand = Math.max((int) (Math.random() * (recommendPlaces_attraction.size() - cnt)), 0);
        model.addAttribute("recommendPlaces_attraction", recommendPlaces_attraction.subList(rand, rand + Math.min(recommendPlaces_attraction.size(), cnt)));

        rand = Math.max((int) (Math.random() * (recommendPlaces_food.size() - cnt)), 0);
        model.addAttribute("recommendPlaces_food", recommendPlaces_food.subList(rand, rand + Math.min(recommendPlaces_food.size(), cnt)));

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
    public String plan(Model model, @RequestParam(value = "plan_id", required = false)String planId){
        int rand, cnt = 10;
        Plan plan;

        if(planId != null){
            try{
                plan = planService.getPlanById(Long.parseLong(planId));
                model.addAttribute("plan", plan);
            } catch (Exception e){
                log.error("no plan");
            }
        }
        rand = (int)(Math.random() * 70);
        Random random = new Random();

        List<Place> placeList = placeRepository.findAllByContentType("12");
        placeList.addAll(placeRepository.findAllByContentType("15"));
        placeList.addAll(placeRepository.findAllByContentType("38"));
        placeList.addAll(placeRepository.findAllByContentType("39"));

        List<Place> pickedList = new ArrayList<>();

        for(int i=0; i<400; ++i){
            pickedList.add(placeList.get(random.nextInt(placeList.size())));
        }

        model.addAttribute("placeList", pickedList);

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

    @GetMapping("/detail/remove")
    @ResponseBody
    public String removeReview(@CurrentUser User user,
                               @RequestParam(value = "id") String reviewId){

        reviewService.deleteReviewById((Long.parseLong(reviewId)));

//        Review reviewById = reviewService.getReviewById(Long.parseLong(reviewId));
//        System.out.println(reviewById);


        return "done";
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

        // 내 일정
        List<Plan> planList = userService.getPlanList(user);
        model.addAttribute("planList", planList);

        // 내 리뷰
        List<Review> reviewList = userService.getReviewList(user);
        model.addAttribute("reviewList", reviewList);

        // 내 찜
        List<Place> likeList = userService.getLikeList(user);

        List<Place> attractionList = new ArrayList<>();
        List<Place> foodList = new ArrayList<>();
        List<Place> shopList = new ArrayList<>();
        List<Place> festivalList = new ArrayList<>();

        for(Place like : likeList){
            switch (like.getContentType()){
                case "12":
                    attractionList.add(like);
                    break;
                case "39":
                    foodList.add(like);
                    break;
                case "38":
                    shopList.add(like);
                    break;
                case "15":
                    festivalList.add(like);
                default:
                    break;
            }
        }

        //model.addAttribute("likeList", likeList);

        model.addAttribute("attractionList", attractionList);
        model.addAttribute("foodList", foodList);
        model.addAttribute("shopList", shopList);
        model.addAttribute("festivalList", festivalList);


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
    public String totalPlace(Model model, @PageableDefault Pageable pageable,
                             @RequestParam(value = "cat", required = false) String cat){
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

        Page<Place> allPlaceList = placeService.getPlaceList(pageable, "12");
        Page<Place> seoulPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "1");
        Page<Place> incheonPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "2");
        Page<Place> ulsanPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "7");
        Page<Place> gyeonggiPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "31");
        Page<Place> busanPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "6");
        Page<Place> daeguPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "4");
        Page<Place> gwangjuPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "5");
        Page<Place> daejeonPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "3");
        Page<Place> jejuPlaceList = placeService.getPlaceListEachAreaCode(pageable, "12", "39");

        model.addAttribute("allPlaceList", allPlaceList);
        model.addAttribute("seoulPlaceList",seoulPlaceList);
        model.addAttribute("incheonPlaceList",incheonPlaceList);
        model.addAttribute("ulsanPlaceList",ulsanPlaceList);
        model.addAttribute("gyeonggiPlaceList",gyeonggiPlaceList);
        model.addAttribute("busanPlaceList",busanPlaceList);
        model.addAttribute("daeguPlaceList",daeguPlaceList);
        model.addAttribute("gwangjuPlaceList",gwangjuPlaceList);
        model.addAttribute("daejeonPlaceList",daejeonPlaceList);
        model.addAttribute("jejuPlaceList",jejuPlaceList);
        model.addAttribute("cat", cat);

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
            @RequestBody PlanForm planForm){

        Plan plan = new Plan();
        Long planId = planForm.getPlanId();

        if(planId > 0){
            plan = planService.getPlanById(planId);
        }

        return Long.toString(planService.savePlan(user, plan, planForm));
    }


    @GetMapping("/mypage/myplan")
    @ResponseBody
    public Plan myPlan(@CurrentUser User user,
                         @RequestParam(value = "id") Long id ){
        return planService.getPlanById(id);
    }

}
