package org.fbi.fskfq.repository.dao.common;

import org.apache.ibatis.annotations.Select;

public interface CommonMapper {
    @Select("SELECT count(*) FROM  ptoper")
    int selectCount();
}
