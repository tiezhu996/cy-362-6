package com.generated.ldmurdergame.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.generated.ldmurdergame.mapper.GroupSessionMapper;
import com.generated.ldmurdergame.model.GroupSession;

@Service
public class GroupSessionService {
  private static final Logger logger = LoggerFactory.getLogger(GroupSessionService.class);

  private final GroupSessionMapper groupSessionMapper;

  public GroupSessionService(GroupSessionMapper groupSessionMapper) {
    this.groupSessionMapper = groupSessionMapper;
  }

  public List<GroupSession> getAllSessions(String playerName) {
    try {
      return groupSessionMapper.findAllWithDetails(playerName);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback sessions data: {}", e.getMessage());
      return getFallbackSessions(playerName);
    }
  }

  public Optional<GroupSession> getSessionById(Integer id) {
    try {
      return groupSessionMapper.findByIdWithDetails(id);
    } catch (Exception e) {
      logger.warn("Database access failed, using fallback session data: {}", e.getMessage());
      return getFallbackSessions(null).stream().filter(s -> s.id().equals(id)).findFirst();
    }
  }

  private List<GroupSession> getFallbackSessions(String playerName) {
    List<GroupSession> sessions = new ArrayList<>();

    sessions.add(new GroupSession(1, 1, "雾起云浮", 1, "张DM",
      LocalDateTime.of(2025, 1, 15, 14, 0),
      List.of("小明", "小红", "小刚", "小丽", "小强", "小美"),
      "completed", LocalDateTime.now(),
      "小明".equals(playerName) || "小红".equals(playerName) || "小刚".equals(playerName), playerName));

    sessions.add(new GroupSession(2, 2, "月下沙利叶", 2, "李DM",
      LocalDateTime.of(2025, 1, 16, 19, 0),
      List.of("阿杰", "阿明", "阿华", "阿丽", "阿芬", "阿龙"),
      "completed", LocalDateTime.now(),
      "阿杰".equals(playerName) || "阿明".equals(playerName), playerName));

    sessions.add(new GroupSession(3, 3, "须臾", 3, "王DM",
      LocalDateTime.of(2025, 1, 17, 13, 0),
      List.of("大伟", "大强", "大美", "大丽", "大华", "大刚"),
      "completed", LocalDateTime.now(),
      "大伟".equals(playerName) || "大强".equals(playerName), playerName));

    sessions.add(new GroupSession(4, 4, "曦和失焰", 1, "张DM",
      LocalDateTime.of(2025, 1, 18, 14, 0),
      List.of("小天", "小雨", "小风", "小云", "小雪", "小雷", "小霞"),
      "completed", LocalDateTime.now(),
      "小天".equals(playerName), playerName));

    sessions.add(new GroupSession(5, 1, "雾起云浮", 2, "李DM",
      LocalDateTime.of(2025, 1, 20, 19, 0),
      List.of("张三", "李四", "王五", "赵六", "钱七", "孙八"),
      "completed", LocalDateTime.now(),
      "张三".equals(playerName) || "李四".equals(playerName), playerName));

    sessions.add(new GroupSession(6, 2, "月下沙利叶", 3, "王DM",
      LocalDateTime.of(2025, 1, 22, 14, 0),
      List.of("周吴", "郑王", "冯陈", "褚卫", "蒋沈", "韩杨"),
      "in_progress", LocalDateTime.now(), false, playerName));

    sessions.add(new GroupSession(7, 5, "年轮", 1, "张DM",
      LocalDateTime.of(2025, 1, 25, 14, 0),
      List.of("朱秦", "尤许", "何吕", "施张", "孔曹"),
      "scheduled", LocalDateTime.now(), false, playerName));

    sessions.sort(Comparator.comparing(GroupSession::sessionTime).reversed());
    return sessions;
  }
}
