package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.*;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {
    private final PlanRepository planRepository;

    public void savePlan(Plan plan) {
        planRepository.save(plan);
    }
}