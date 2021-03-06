package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Review;
import com.project.mega.triplus.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager em;

    public void getReviewById(Long id){
    }

    @PreRemove
    public void deleteReviewById(Long id) {
        reviewRepository.deleteById(id);
    }
}
