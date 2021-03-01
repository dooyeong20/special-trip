package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.Review;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    public List<Place> getPlace() {
        // 좋아요(liked) 많은순(내림차순)으로 findAll.
        return placeRepository.findFirst6ByOrderByLikedDesc();
    }


    public List<Place> getPlaceList() {
        // 좋아요(liked) 많은순(내림차순)으로 findAll.
        return placeRepository.findFirst6ByOrderByLikedDesc();
    }

    public Place getPlaceByContentId(String contentId) {

        return placeRepository.findByContentId(contentId);
    }


    public void addLikes(User user, String contentId) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요한 기능입니다.");
        }

        user = userRepository.findByEmail(user.getEmail());
        Place place = placeRepository.findByContentId(contentId);

        if (place == null) {
            throw new IllegalStateException("등록되지 않은 여행지입니다.");
        }

        List<Place> list = user.getPlaceLikes();

        if (list.contains(place)) {
            throw new IllegalStateException("이미 찜한 여행지입니다.");
        }

        place.setLiked(place.getLiked() + 1);
        user.getPlaceLikes().add(place);

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
