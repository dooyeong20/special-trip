package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Day;
import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Plan;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.form.PlanForm;
import com.project.mega.triplus.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {
    @PersistenceContext
    private EntityManager em;

    private final PlanRepository planRepository;

    public Long savePlan(User user, Plan plan, PlanForm planForm) {
        Day day;
        Place place;

        plan.setUser(user);
        plan.setName(planForm.getPlan());
        plan.setUpdate(LocalDateTime.now());
        plan.getDays().clear();


        for(List<Map<String, String>> d : planForm.getDayList()){
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

        em.persist(plan);

        return plan.getId();
    }


    public Plan getPlanById(Long id) {
        return em.find(Plan.class, id);
    }
}