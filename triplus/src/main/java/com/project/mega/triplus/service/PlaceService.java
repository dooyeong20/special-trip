package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<Place> getPlace() {
        // 좋아요(liked) 많은순(내림차순)으로 findAll.
        List<Place> RecommendPlace = placeRepository.findFirst6ByOrderByLikedDesc();

        return RecommendPlace;
    }

}
