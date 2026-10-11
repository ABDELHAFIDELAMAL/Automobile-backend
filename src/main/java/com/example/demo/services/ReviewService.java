package com.example.demo.services;

import com.example.demo.entities.Review;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview( Review review) {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if (reviewRepository.findByUsername(currentUsername).isPresent()) {
            throw new IllegalStateException("You have already submitted a review on the site. Only one review per user is allowed.");
        }
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        return Optional.ofNullable(reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Review Not found with id :" + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating() {
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getNumberOfStars)
                .average()
                .orElse(0.0);
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not fount with ID : " + id);
        }
        reviewRepository.deleteById(id);
    }
}