package com.generated.ldmurdergame.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldmurdergame.model.Script;
import com.generated.ldmurdergame.service.ScriptService;

@RestController
@RequestMapping({"/scripts", "/api/scripts"})
public class ScriptController {
  private final ScriptService scriptService;

  public ScriptController(ScriptService scriptService) {
    this.scriptService = scriptService;
  }

  @GetMapping
  public List<Script> getAllScripts() {
    return scriptService.getAllScripts();
  }

  @GetMapping("/{id}")
  public Script getScriptById(@PathVariable Integer id) {
    return scriptService.getScriptById(id).orElse(null);
  }
}
