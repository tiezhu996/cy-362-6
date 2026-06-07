package com.generated.ldmurdergame.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.generated.ldmurdergame.mapper.ScriptMapper;
import com.generated.ldmurdergame.model.Script;

@Service
public class ScriptService {
  private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);

  private final ScriptMapper scriptMapper;

  public ScriptService(ScriptMapper scriptMapper) {
    this.scriptMapper = scriptMapper;
  }

  public List<Script> getAllScripts() {
    try {
      return scriptMapper.findAllWithRatings();
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback scripts data: {}", e.getMessage());
      return getFallbackScripts();
    }
  }

  public Optional<Script> getScriptById(Integer id) {
    try {
      return scriptMapper.findByIdWithRatings(id);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback script data: {}", e.getMessage());
      return getFallbackScripts().stream().filter(s -> s.id().equals(id)).findFirst();
    }
  }

  private List<Script> getFallbackScripts() {
    List<Script> scripts = new ArrayList<>();
    scripts.add(new Script(1, "雾起云浮", "推理", "困难", 300, 6,
      "民国背景硬核推理本，多重反转挑战脑力。", null, 1,
      LocalDateTime.now(), 4.7, 5));
    scripts.add(new Script(2, "月下沙利叶", "恐怖", "中等", 240, 6,
      "沉浸式恐怖体验，DM演绎至关重要。", null, 2,
      LocalDateTime.now(), 4.5, 2));
    scripts.add(new Script(3, "须臾", "推理", "困难", 360, 6,
      "变格推理神作，逻辑严密环环相扣。", null, 3,
      LocalDateTime.now(), 5.0, 2));
    scripts.add(new Script(4, "曦和失焰", "推理", "中等", 300, 7,
      "机制推理本，互动性强。", null, 1,
      LocalDateTime.now(), 4.0, 1));
    scripts.add(new Script(5, "年轮", "情感", "简单", 240, 5,
      "经典情感本，故事感人至深。", null, 2,
      LocalDateTime.now(), null, 0));
    scripts.sort(Comparator.comparing(Script::createdAt).reversed());
    return scripts;
  }
}
