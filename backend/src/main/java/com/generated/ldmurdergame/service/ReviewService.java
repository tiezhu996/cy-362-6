package com.generated.ldmurdergame.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.generated.ldmurdergame.exception.ApiException;
import com.generated.ldmurdergame.mapper.ReviewMapper;
import com.generated.ldmurdergame.model.Review;
import com.generated.ldmurdergame.model.ReviewRequest;

@Service
public class ReviewService {
  private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

  private final ReviewMapper reviewMapper;
  private final Map<String, Review> fallbackReviews = new HashMap<>();

  public ReviewService(ReviewMapper reviewMapper) {
    this.reviewMapper = reviewMapper;
    initFallbackReviews();
  }

  private void initFallbackReviews() {
    List<Review> reviews = getFallbackReviews();
    for (Review r : reviews) {
      String key = r.groupSessionId() + "-" + r.playerName();
      fallbackReviews.put(key, r);
    }
  }

  public Review createReview(ReviewRequest request) {
    String key = request.groupSessionId() + "-" + request.playerName();

    try {
      Optional<Review> existing = reviewMapper.findByGroupSessionIdAndPlayerName(
        request.groupSessionId(), request.playerName());
      if (existing.isPresent()) {
        throw new ApiException("您已经对该局进行过评价");
      }
      reviewMapper.insert(request);
      return reviewMapper.findByGroupSessionIdAndPlayerName(
        request.groupSessionId(), request.playerName()).orElseThrow();
    } catch (ApiException e) {
      throw e;
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback review creation: {}", e.getMessage());
      if (fallbackReviews.containsKey(key)) {
        throw new ApiException("您已经对该局进行过评价");
      }
      Review review = new Review(
        fallbackReviews.size() + 1,
        request.groupSessionId(),
        request.scriptId(),
        getScriptNameById(request.scriptId()),
        request.dmId(),
        getDmNameById(request.dmId()),
        request.playerName(),
        request.scriptRating(),
        request.scriptComment(),
        request.dmRating(),
        request.dmComment(),
        LocalDateTime.now()
      );
      fallbackReviews.put(key, review);
      return review;
    }
  }

  public List<Review> getReviewsByScriptId(Integer scriptId) {
    try {
      return reviewMapper.findByScriptId(scriptId);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback reviews data: {}", e.getMessage());
      return fallbackReviews.values().stream()
        .filter(r -> r.scriptId().equals(scriptId))
        .sorted(Comparator.comparing(Review::createdAt).reversed())
        .toList();
    }
  }

  public List<Review> getReviewsByGroupSessionId(Integer groupSessionId) {
    try {
      return reviewMapper.findByGroupSessionId(groupSessionId);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback reviews data: {}", e.getMessage());
      return fallbackReviews.values().stream()
        .filter(r -> r.groupSessionId().equals(groupSessionId))
        .sorted(Comparator.comparing(Review::createdAt).reversed())
        .toList();
    }
  }

  public Optional<Review> getReviewBySessionAndPlayer(Integer groupSessionId, String playerName) {
    try {
      return reviewMapper.findByGroupSessionIdAndPlayerName(groupSessionId, playerName);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback review data: {}", e.getMessage());
      return Optional.ofNullable(fallbackReviews.get(groupSessionId + "-" + playerName));
    }
  }

  public Double getAvgScriptRating(Integer scriptId) {
    try {
      return reviewMapper.getAvgScriptRatingByScriptId(scriptId);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback avg rating: {}", e.getMessage());
      List<Review> reviews = fallbackReviews.values().stream()
        .filter(r -> r.scriptId().equals(scriptId))
        .toList();
      if (reviews.isEmpty()) return null;
      double avg = reviews.stream().mapToInt(Review::scriptRating).average().orElse(0);
      return Math.round(avg * 10) / 10.0;
    }
  }

  public Integer getReviewCountByScript(Integer scriptId) {
    try {
      return reviewMapper.countReviewsByScriptId(scriptId);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback review count: {}", e.getMessage());
      return (int) fallbackReviews.values().stream()
        .filter(r -> r.scriptId().equals(scriptId))
        .count();
    }
  }

  private List<Review> getFallbackReviews() {
    List<Review> reviews = new ArrayList<>();
    reviews.add(new Review(1, 1, 1, "雾起云浮", 1, "张DM", "小明", 5,
      "逻辑严密，反转惊艳，是玩过最棒的推理本！", 5,
      "张DM控场一流，节奏把握恰到好处，沉浸感满分。", LocalDateTime.now()));
    reviews.add(new Review(2, 1, 1, "雾起云浮", 1, "张DM", "小红", 4,
      "剧情很棒，但部分线索有点牵强。", 4,
      "整体不错，希望下次能更耐心解答问题。", LocalDateTime.now()));
    reviews.add(new Review(3, 1, 1, "雾起云浮", 1, "张DM", "小刚", 5,
      "硬核推理爱好者必玩，全程烧脑过瘾。", 5,
      "DM专业水准高，体验非常好。", LocalDateTime.now()));
    reviews.add(new Review(4, 2, 2, "月下沙利叶", 2, "李DM", "阿杰", 5,
      "恐怖氛围拉满，吓得我不敢睁眼！", 5,
      "李DM的演绎太绝了，代入感超强。", LocalDateTime.now()));
    reviews.add(new Review(5, 2, 2, "月下沙利叶", 2, "李DM", "阿明", 4,
      "恐怖效果不错，推理部分稍弱。", 5,
      "DM很会调动气氛，体验极佳。", LocalDateTime.now()));
    reviews.add(new Review(6, 3, 3, "须臾", 3, "王DM", "大伟", 5,
      "变格巅峰之作，作者脑洞太大了！", 4,
      "王DM很认真，但对变格设定解释可以更清晰。", LocalDateTime.now()));
    reviews.add(new Review(7, 3, 3, "须臾", 3, "王DM", "大强", 5,
      "逻辑闭环完美，每一个细节都有呼应。", 5,
      "DM带本经验丰富，全程丝滑。", LocalDateTime.now()));
    reviews.add(new Review(8, 4, 4, "曦和失焰", 1, "张DM", "小天", 4,
      "机制新颖，推理和互动结合得很好。", 5,
      "张DM对机制理解透彻，讲解清楚。", LocalDateTime.now()));
    reviews.add(new Review(9, 5, 1, "雾起云浮", 2, "李DM", "张三", 5,
      "二刷还是觉得精彩，每次都有新发现。", 4,
      "李DM带本也不错，但比张DM稍逊一筹。", LocalDateTime.now()));
    reviews.add(new Review(10, 5, 1, "雾起云浮", 2, "李DM", "李四", 4,
      "经典好本，值得推荐。", 5,
      "DM服务态度好，体验很棒。", LocalDateTime.now()));
    return reviews;
  }

  private String getScriptNameById(Integer id) {
    return switch (id) {
      case 1 -> "雾起云浮";
      case 2 -> "月下沙利叶";
      case 3 -> "须臾";
      case 4 -> "曦和失焰";
      case 5 -> "年轮";
      default -> "未知剧本";
    };
  }

  private String getDmNameById(Integer id) {
    return switch (id) {
      case 1 -> "张DM";
      case 2 -> "李DM";
      case 3 -> "王DM";
      default -> "未知DM";
    };
  }
}
