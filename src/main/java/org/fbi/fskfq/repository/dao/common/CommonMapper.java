package org.fbi.fskfq.repository.dao.common;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface CommonMapper {
    @Select("SELECT count(*) FROM  ptoper")
    int selectCount();

    @Select("select sum(t.chargemoney) from fs_kfq_payment_item t " +
            " where t.main_pkid = #{pkid}")
    BigDecimal qryBillMoney(@Param("pkid") String pkid);
}
