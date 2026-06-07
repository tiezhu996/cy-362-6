package com.generated.ldmurdergame.model;

import java.time.LocalDateTime;
import java.util.List;

public record GroupSession(
  Integer id,
  Integer scriptId,
  String scriptName,
  Integer dmId,
  String dmName,
  LocalDateTime sessionTime,
  List<String> playerNames,
  String status,
  LocalDateTime createdAt,
  Boolean hasReviewed,
  String currentPlayer
) {}
