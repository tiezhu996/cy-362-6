package com.generated.ldmurdergame.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldmurdergame.model.GroupSession;
import com.generated.ldmurdergame.service.GroupSessionService;

@RestController
@RequestMapping({"/group-sessions", "/api/group-sessions"})
public class GroupSessionController {
  private final GroupSessionService groupSessionService;

  public GroupSessionController(GroupSessionService groupSessionService) {
    this.groupSessionService = groupSessionService;
  }

  @GetMapping
  public List<GroupSession> getAllSessions(@RequestParam(defaultValue = "") String playerName) {
    return groupSessionService.getAllSessions(playerName);
  }

  @GetMapping("/{id}")
  public GroupSession getSessionById(@PathVariable Integer id) {
    return groupSessionService.getSessionById(id).orElse(null);
  }
}
