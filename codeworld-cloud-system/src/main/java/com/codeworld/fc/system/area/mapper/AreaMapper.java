package com.codeworld.fc.system.area.mapper;

import com.codeworld.fc.common.core.BaseMapper;
import com.codeworld.fc.system.area.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AreaMapper extends BaseMapper<Area> {

    List<Area> getAllArea();
}
