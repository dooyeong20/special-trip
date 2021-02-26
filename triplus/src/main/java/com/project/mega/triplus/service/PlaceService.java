package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Review;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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

    public void saveReview(User user, Place place, String content){
        Review review = new Review();

        review.setTitle(user.getNickName());
        review.setPlace(place);
        review.setUser(user);
        review.setContent(content);
        review.setRegdate(LocalDateTime.now().toString().substring(0, 10));

        em.persist(place);
//        placeRepository.save(place);      // 이거 안되고 em.persist 만 됨
    }
}
