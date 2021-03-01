package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import com.project.mega.triplus.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class PlanService {
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
        Day plan_day1=new Day();
        plan_day1.setName("First Day");
        plan_day1.addPlace(placeRepository.findById(1L).get());
        plan_day1.addPlace(placeRepository.findById(3L).get());

        // total_plan을 위한 plan 더미 데이터
        Plan plan=new Plan();
        plan.setName("First plan");
        plan.setLiked(10);
        plan.setUser(user);
        plan_day1.setPlan(plan);
        plan.setStatus(PlanStatus.COMPLETE);
        plan.setUpdate(LocalDateTime.now());
        planRepository.save(plan);

        Plan plan2=new Plan();
        plan2.setName("Second plan");
        plan2.setLiked(8);
        plan2.setUser(user);
        plan2.setStatus(PlanStatus.COMPLETE);
        plan2.setUpdate(LocalDateTime.now());
        planRepository.save(plan2);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<Plan> getAllByOrderByLikedDesc(){
        return planRepository.findAllByOrderByLikedDesc();
    }
}
