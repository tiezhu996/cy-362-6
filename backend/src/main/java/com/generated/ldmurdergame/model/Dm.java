package com.generated.ldmurdergame.model;

import java.time.LocalDateTime;

public record Dm(
  Integer id,
  String name,
  String phone,
  String avatarUrl,
  LocalDateTime createdAt,
  Double avgDmRating,
  Integer reviewCount
) {}
