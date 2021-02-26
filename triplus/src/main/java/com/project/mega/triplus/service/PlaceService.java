package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Place;
import com.project.mega.triplus.entity.User;
import com.project.mega.triplus.repository.PlaceRepository;
import com.project.mega.triplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Place> getPlace() {
        // 좋아요(liked) 많은순(내림차순)으로 findAll.
        List<Place> RecommendPlace = placeRepository.findFirst6ByOrderByLikedDesc();

        return RecommendPlace;
    }

    public void addLikes(User user, Long id) {
        if(user == null){
            throw new IllegalStateException("로그인이 필요한 기능입니다.");
        }

        user = userRepository.findByEmail(user.getEmail());

        Optional<Place> placeOptional = placeRepository.findById(id);

        if(placeOptional.isEmpty()){
            throw new IllegalStateException("등록되지 않은 여행지입니다.");
        }

        Place place = placeOptional.get();

        List<Place> list = user.getPlaceLikes();

        if(list.contains(place)){
            throw new IllegalStateException("이미 찜한 여행지입니다.");
        }


        place.setLiked( place.getLiked() + 1 );

        user.getPlaceLikes().add(place);
    }
}
