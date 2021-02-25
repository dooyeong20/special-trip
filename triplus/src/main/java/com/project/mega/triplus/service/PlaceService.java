package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Review;
import com.project.mega.triplus.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    @PersistenceContext
    private EntityManager em;

    public List<Place> getPlaceList() {
        // 좋아요(liked) 많은순(내림차순)으로 findAll.
        return placeRepository.findFirst6ByOrderByLikedDesc();
    }

    public Place getPlaceByContentId(String contentId) {
        return placeRepository.findByContentId(contentId);
    }

    public void saveReview(Place place){
        em.persist(place);
//        placeRepository.save(place);      // 이거 안됨
    }
}
