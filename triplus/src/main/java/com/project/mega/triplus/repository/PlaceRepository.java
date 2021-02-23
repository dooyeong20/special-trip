package com.project.mega.triplus.repository;

import com.project.mega.triplus.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    // 임시
    Optional<Place> findById(Long id);

    // Liked 내림차순 처음 6개
    List<Place> findFirst6ByOrderByLikedDesc();

    List<Place> findFirstByOrderByLikedDesc();

}
