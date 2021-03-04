package com.project.mega.triplus.service;

import com.project.mega.triplus.entity.Review;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
    @PersistenceContext
    private EntityManager em;

    public Review getReviewById(Long id){
        return em.find(Review.class, id);
    }
    public void deleteReviewById(long id) {
        em.remove(getReviewById(id));
    }
}
