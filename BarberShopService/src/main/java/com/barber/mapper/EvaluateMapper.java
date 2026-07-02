package com.barber.mapper;

import com.barber.entity.Evaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.barber.mapping.EvaluateDTO;
import com.barber.mapping.EvaluateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评价 Mapper 接口
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
public interface EvaluateMapper extends BaseMapper<Evaluate> {

    /**
     * 查询评价列表
     * @param evaluateDTO
     * @return
     */
    List<EvaluateVO> queryEvaluateList(@Param("evaluateDTO") EvaluateDTO evaluateDTO);
}
