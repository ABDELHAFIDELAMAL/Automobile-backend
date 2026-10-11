package com.example.demo.controllers;

import com.example.demo.entities.Review;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    @Autowired
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.createReview(review);
        ApiResponse response = new ApiResponse("Review created successfully", createdReview, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getAllReviews() {
        List<Review> data = reviewService.getAllReviews();
        ApiResponse response = new ApiResponse("Reviews fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReviewById(@PathVariable Long id) {
        Review data = reviewService.getReviewById(id).orElse(null);
        ApiResponse response = new ApiResponse("Review fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse> getAverageRating() {
        Double data = reviewService.getAverageRating();
        ApiResponse response = new ApiResponse("Average rating fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        ApiResponse response = new ApiResponse("Review deleted successfully", null, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
