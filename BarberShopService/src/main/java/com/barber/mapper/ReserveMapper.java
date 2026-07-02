package com.barber.mapper;

import com.barber.entity.Reserve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.barber.mapping.ReserveDTO;
import com.barber.mapping.ReserveVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 预约 Mapper 接口
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
public interface ReserveMapper extends BaseMapper<Reserve> {

    /**
     * 查询预约列表
     * @param reserveDTO
     * @return
     */
    List<ReserveVO> queryReserveList(@Param("reserveDTO") ReserveDTO reserveDTO);

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    ReserveVO queryReserveInfo(@Param("id") String id);
}
