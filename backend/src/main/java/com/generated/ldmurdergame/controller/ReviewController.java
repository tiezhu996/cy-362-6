package com.generated.ldmurdergame.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldmurdergame.model.Review;
import com.generated.ldmurdergame.model.ReviewRequest;
import com.generated.ldmurdergame.service.ReviewService;

@RestController
@RequestMapping({"/reviews", "/api/reviews"})
public class ReviewController {
  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping
  public Review createReview(@Valid @RequestBody ReviewRequest request) {
    return reviewService.createReview(request);
  }

  @GetMapping("/script/{scriptId}")
  public Map<String, Object> getReviewsByScriptId(@PathVariable Integer scriptId) {
    Map<String, Object> result = new HashMap<>();
    List<Review> reviews = reviewService.getReviewsByScriptId(scriptId);
    Double avgRating = reviewService.getAvgScriptRating(scriptId);
    Integer reviewCount = reviewService.getReviewCountByScript(scriptId);
    result.put("reviews", reviews);
    result.put("avgScriptRating", avgRating);
    result.put("reviewCount", reviewCount);
    return result;
  }

  @GetMapping("/group-session/{groupId}")
  public List<Review> getReviewsByGroupSessionId(@PathVariable Integer groupId) {
    return reviewService.getReviewsByGroupSessionId(groupId);
  }

  @GetMapping("/check")
  public Map<String, Boolean> checkReviewExists(
      @RequestParam Integer groupSessionId,
      @RequestParam String playerName) {
    Map<String, Boolean> result = new HashMap<>();
    boolean exists = reviewService.getReviewBySessionAndPlayer(groupSessionId, playerName).isPresent();
    result.put("hasReviewed", exists);
    return result;
  }
}
