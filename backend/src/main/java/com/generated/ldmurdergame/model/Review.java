package com.generated.ldmurdergame.model;

import java.time.LocalDateTime;

public record Review(
  Integer id,
  Integer groupSessionId,
  Integer scriptId,
  String scriptName,
  Integer dmId,
  String dmName,
  String playerName,
  Integer scriptRating,
  String scriptComment,
  Integer dmRating,
  String dmComment,
  LocalDateTime createdAt
) {}
