package com.generated.ldmurdergame.model;

import java.time.LocalDateTime;

public record Script(
  Integer id,
  String name,
  String type,
  String difficulty,
  Integer duration,
  Integer playerCount,
  String description,
  String coverUrl,
  Integer dmId,
  LocalDateTime createdAt,
  Double avgScriptRating,
  Integer reviewCount
) {}
