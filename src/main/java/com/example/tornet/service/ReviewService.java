package com.example.tornet.service;

import com.example.tornet.model.Review;
import com.example.tornet.reposotory.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {
private final ReviewRepository reviewRepository;

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream().findFirst().orElse(null);
    }
    public void updateReview(Review review) {
        reviewRepository.save(review);
    }
}
