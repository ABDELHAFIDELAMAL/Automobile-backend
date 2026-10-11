package com.example.demo.services;

import com.example.demo.entities.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    Review createReview(Review review);
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    Double getAverageRating();
    void deleteReview(Long id);
}
