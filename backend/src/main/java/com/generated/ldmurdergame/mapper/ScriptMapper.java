package com.generated.ldmurdergame.mapper;

import com.generated.ldmurdergame.model.Script;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ScriptMapper {
  List<Script> findAllWithRatings();

  Optional<Script> findByIdWithRatings(@Param("id") Integer id);
}
