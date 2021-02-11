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

    // Place 더미데이터
    @PostConstruct
    @Transactional
    protected void setTempPlace(){
        Place place1=new Place();
        place1.setName("남산타워");
        place1.setContent("대한민국 서울특별시 용산구 용산동2가 남산 정상 부근에 위치한 전파 송출 및 관광용 타워이다.");
        place1.setImageUrls(List.of("southTower.jpg"));
        place1.setLiked(8);

        Place place2=new Place();
        place2.setName("경복궁");
        place2.setContent("경복궁은 대한민국 서울 세종로에 있는 대조선국 왕조의 법궁이다.");
        place2.setImageUrls(List.of("Gyeongbokgung1.jpg"));
        place2.setLiked(10);

        placeRepository.save(place1);
        placeRepository.save(place2);
    }
}
