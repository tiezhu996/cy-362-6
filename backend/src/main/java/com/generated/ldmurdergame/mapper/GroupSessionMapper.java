package com.generated.ldmurdergame.mapper;

import com.generated.ldmurdergame.model.GroupSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GroupSessionMapper {
  List<GroupSession> findAllWithDetails(@Param("playerName") String playerName);

  Optional<GroupSession> findByIdWithDetails(@Param("id") Integer id);
}
