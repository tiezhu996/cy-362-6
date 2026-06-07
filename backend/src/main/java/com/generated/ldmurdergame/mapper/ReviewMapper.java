package com.generated.ldmurdergame.mapper;

import com.generated.ldmurdergame.model.Review;
import com.generated.ldmurdergame.model.ReviewRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewMapper {
  int insert(ReviewRequest request);

  List<Review> findByScriptId(@Param("scriptId") Integer scriptId);

  List<Review> findByDmId(@Param("dmId") Integer dmId);

  List<Review> findByGroupSessionId(@Param("groupSessionId") Integer groupSessionId);

  Optional<Review> findByGroupSessionIdAndPlayerName(
    @Param("groupSessionId") Integer groupSessionId,
    @Param("playerName") String playerName
  );

  Double getAvgScriptRatingByScriptId(@Param("scriptId") Integer scriptId);

  Double getAvgDmRatingByDmId(@Param("dmId") Integer dmId);

  Integer countReviewsByScriptId(@Param("scriptId") Integer scriptId);

  Integer countReviewsByDmId(@Param("dmId") Integer dmId);
}
