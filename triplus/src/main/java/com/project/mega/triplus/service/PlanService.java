package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.form.PlanForm;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class PlanService {
    @PersistenceContext
    private EntityManager em;

    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public void createDummyData(){
        // total_plan을 위한 user 더미 데이터
        User user=new User();
        user.setNickName("user01");
        user.setEmail("a@a.a");
        user.setPassword(passwordEncoder.encode("1234"));
        userRepository.save(user);

        // total_plan을 위한 day 더미 데이터
        Day plan1_day1=new Day();
        plan1_day1.setName("First Day");
        plan1_day1.addPlace(placeRepository.findById(75L).get());
        plan1_day1.addPlace(placeRepository.findById(89L).get());

        Day plan1_day2=new Day();
        plan1_day2.setName("Second Day");
        plan1_day2.addPlace(placeRepository.findById(15L).get());
        plan1_day2.addPlace(placeRepository.findById(120L).get());

        Day plan2_day1=new Day();
        plan2_day1.setName("First Day");
        plan2_day1.addPlace(placeRepository.findById(21L).get());
        plan2_day1.addPlace(placeRepository.findById(70L).get());

        // total_plan을 위한 plan 더미 데이터
        Plan plan=new Plan();
        plan.setName("First plan");
        plan.setLiked(11);
        plan.setUser(user);
        plan1_day1.setPlan(plan);
        plan1_day2.setPlan(plan);
        plan.setStatus(PlanStatus.COMPLETE);
        plan.setDayCounts(plan.getDayCounts());
        plan.setMainAreaCode(plan.getMainAreaCode());
        plan.setMainImg(plan.getMainImg());
        planRepository.save(plan);

        Plan plan2=new Plan();
        plan2.setName("Second plan");
        plan2.setLiked(61);
        plan2.setUser(user);
        plan2_day1.setPlan(plan2);
        plan2.setStatus(PlanStatus.COMPLETE);
        plan2.setDayCounts(plan2.getDayCounts());
        plan2.setMainAreaCode(plan2.getMainAreaCode());
        plan2.setMainImg(plan2.getMainImg());
        planRepository.save(plan2);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<Plan> getAllByOrderByLikedDesc(){
        return planRepository.findAllByOrderByLikedDesc();
    }

    public Long savePlan(User user, Plan plan, PlanForm planForm) {
        Day day;
        Place place;

        plan.setUser(user);
        plan.setName(planForm.getPlan());
        plan.setUpdate(LocalDateTime.now());

        List<Day> tmpList = new ArrayList<>();


        for(List<Map<String, String>> d : planForm.getDayList()){
            day = new Day();

            for(Map<String, String> p : d){
                String content_id = p.get("content_id");
                Place singleResult = em.createQuery("select p from Place p where p.contentId = :contentId", Place.class).setParameter(
                        "contentId", content_id
                ).getSingleResult();

                day.addPlace(singleResult);
            }

            day.setPlan(plan);
            tmpList.add(day);
        }
        plan.getDays().clear();
        plan.getDays().addAll(tmpList);

        em.persist(plan);

        return plan.getId();
    }


    public Plan getPlanById(Long id) {
        return em.find(Plan.class, id);
    }
}
