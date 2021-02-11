package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Accomm;
import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlaceRepository placeRepository;

}
