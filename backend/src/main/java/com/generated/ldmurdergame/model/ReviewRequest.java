package com.generated.ldmurdergame.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
  @NotNull(message = "组局ID不能为空")
  Integer groupSessionId,

  @NotNull(message = "剧本ID不能为空")
  Integer scriptId,

  @NotNull(message = "DM ID不能为空")
  Integer dmId,

  @NotBlank(message = "玩家名称不能为空")
  String playerName,

  @NotNull(message = "剧本评分不能为空")
  @Min(value = 1, message = "剧本评分最小为1")
  @Max(value = 5, message = "剧本评分最大为5")
  Integer scriptRating,

  String scriptComment,

  @NotNull(message = "DM评分不能为空")
  @Min(value = 1, message = "DM评分最小为1")
  @Max(value = 5, message = "DM评分最大为5")
  Integer dmRating,

  String dmComment
) {}
