package com.barber.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.entity.Evaluate;
import com.barber.entity.Reserve;
import com.barber.mapper.EvaluateMapper;
import com.barber.mapping.EvaluateDTO;
import com.barber.mapping.EvaluateVO;
import com.barber.tool.DateTool;
import com.barber.tool.LoginUtil;
import com.barber.tool.PageTool;
import com.barber.tool.data.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 评价 服务实现类
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Service
public class EvaluateService extends ServiceImpl<EvaluateMapper, Evaluate> {

    @Resource
    private ReserveService reserveService;


    /**
     * 分页查询评价
     * @param evaluateDTO
     * @return
     */
    public ResultVO queryEvaluatePaginationList(EvaluateDTO evaluateDTO) {

        PageHelper.startPage(evaluateDTO.getPage(), evaluateDTO.getSize());

        // 查询评价列表
        List<EvaluateVO> evaluateList = baseMapper.queryEvaluateList(evaluateDTO);

        PageInfo<EvaluateVO> pageInfo = new PageInfo<>(evaluateList);

        return PageTool.convertResult(pageInfo);
    }


    /**
     * 查询评价信息
     * @param id
     * @return
     */
    public ResultVO queryEvaluateInfo(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        Evaluate evaluate = getById(id);

        return PageTool.convertResult(evaluate);
    }


    /**
     * 新增（修改）评价
     * @param evaluateDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO addOrModifyEvaluate(EvaluateDTO evaluateDTO) {

        String userId = LoginUtil.getCurrentUserId();
        String time = DateTool.getCurrentTime();

        Evaluate evaluate = BeanUtil.copyProperties(evaluateDTO, Evaluate.class);
        evaluate.setUpdateTime(time);

        if (StrUtil.isBlank(evaluate.getId())) {
            evaluate.setId(IdUtil.simpleUUID());
            evaluate.setCreateTime(time);
            evaluate.setCreateUser(userId);
            save(evaluate);

            // 修改预约状态为已评价
            reserveService.update(Wrappers.<Reserve>lambdaUpdate().eq(Reserve::getId, evaluate.getReserveObject()).set(Reserve::getEvaluate, "1"));
        } else {
            updateById(evaluate);
        }

        return new ResultVO<>(true);
    }


    /**
     * 删除评价
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO deleteEvaluate(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        Evaluate evaluate = getById(id);
        Optional.ofNullable(evaluate).orElseThrow(() -> new RuntimeException("评价不存在"));

        removeById(id);

        long reserveEvaluateCount = count(Wrappers.<Evaluate>lambdaQuery()
                .eq(Evaluate::getReserveObject, evaluate.getReserveObject()));
        if (reserveEvaluateCount == 0) {
            reserveService.update(Wrappers.<Reserve>lambdaUpdate()
                    .eq(Reserve::getId, evaluate.getReserveObject())
                    .set(Reserve::getEvaluate, "2"));
        }

        return new ResultVO<>(true);
    }

}
