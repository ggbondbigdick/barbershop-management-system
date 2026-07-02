package com.barber.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.entity.Evaluate;
import com.barber.entity.Reserve;
import com.barber.entity.User;
import com.barber.mapper.EvaluateMapper;
import com.barber.mapper.ReserveMapper;
import com.barber.mapping.ReserveDTO;
import com.barber.mapping.ReserveVO;
import com.barber.tool.DateTool;
import com.barber.tool.LoginUtil;
import com.barber.tool.PageTool;
import com.barber.tool.PreviewTool;
import com.barber.tool.data.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 预约 服务实现类
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Service
public class ReserveService extends ServiceImpl<ReserveMapper, Reserve> {

    @Resource
    private UserService userService;

    @Resource
    private PreviewTool previewTool;

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 查询预约分页列表
     * @param reserveDTO
     * @return
     */
    public ResultVO getReservePaginationList(ReserveDTO reserveDTO) {

        // 判断用户角色
        String currentUserRole = LoginUtil.getCurrentUserRole();
        if (StrUtil.equals("3", currentUserRole)) {
            reserveDTO.setReserveStaff(LoginUtil.getCurrentUserId());
        } else if (!StrUtil.equals("1", currentUserRole)) {
            reserveDTO.setCreateUser(LoginUtil.getCurrentUserId());
        }

        PageHelper.startPage(reserveDTO.getPage(), reserveDTO.getSize());

        // 查询预约列表
        List<ReserveVO> reserveList = baseMapper.queryReserveList(reserveDTO);

        List<ReserveVO> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reserveList)) {
            resultList = reserveList.stream().map(reserveVO -> {
                reserveVO.setRelatedThumbnail(StrUtil.isBlank(reserveVO.getRelatedThumbnail()) ? "" : previewTool.getFilePreviewUrl(reserveVO.getRelatedThumbnail()));
                reserveVO.setReserveStaffAvatar(StrUtil.isBlank(reserveVO.getReserveStaffAvatar()) ? "" : previewTool.getFilePreviewUrl(reserveVO.getReserveStaffAvatar()));
                return reserveVO;
            }).collect(Collectors.toList());
        }

        PageInfo<ReserveVO> pageInfo = new PageInfo<>(resultList);

        return PageTool.convertResult(pageInfo);
    }


    /**
     * 查询预约详情
     * @param id
     * @return
     */
    public ResultVO getReserveInfo(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        ReserveVO reserveVO = baseMapper.queryReserveInfo(id);

        reserveVO.setRelatedThumbnail(StrUtil.isBlank(reserveVO.getRelatedThumbnail()) ? "" : previewTool.getFilePreviewUrl(reserveVO.getRelatedThumbnail()));
        reserveVO.setReserveStaffAvatar(StrUtil.isBlank(reserveVO.getReserveStaffAvatar()) ? "" : previewTool.getFilePreviewUrl(reserveVO.getReserveStaffAvatar()));

        return PageTool.convertResult(reserveVO);
    }


    /**
     * 新增（修改）预约
     * @param reserveDTO
     * @return
     */
    public ResultVO addOrModifyReserve(ReserveDTO reserveDTO) {

        String userId = LoginUtil.getCurrentUserId();
        String time = DateTool.getCurrentTime();

        // 查询用户当日是否预约了相同项目
        Reserve reserveData = getOne(Wrappers.<Reserve>lambdaQuery().eq(Reserve::getReserveObject, reserveDTO.getReserveObject())
                .eq(Reserve::getReserveTime, reserveDTO.getReserveTime())
                .eq(Reserve::getCreateUser, userId));

        Optional.ofNullable(reserveData).ifPresent(reserve -> {
            throw new RuntimeException("您在" + reserveDTO.getReserveTime() + "已经预约了项目，请勿重复预约");
        });

        if (StrUtil.isNotBlank(reserveDTO.getReserveStaff())) {
            long staffBookedCount = count(Wrappers.<Reserve>lambdaQuery()
                    .eq(Reserve::getReserveStaff, reserveDTO.getReserveStaff())
                    .eq(Reserve::getReserveTime, reserveDTO.getReserveTime())
                    .in(Reserve::getState, "1", "2", "3", "4")
                    .ne(StrUtil.isNotBlank(reserveDTO.getId()), Reserve::getId, reserveDTO.getId()));
            if (staffBookedCount > 0) {
                throw new RuntimeException("该理发师当前时段已被预约，请重新选择");
            }
        }

        Reserve reserve = BeanUtil.copyProperties(reserveDTO, Reserve.class);
        reserve.setUpdateTime(time);

        if (StrUtil.isBlank(reserve.getId())) {
            reserve.setId(IdUtil.simpleUUID());
            reserve.setCreateTime(time);
            reserve.setCreateUser(userId);
            reserve.setState("1");
            reserve.setEvaluate("2");
            save(reserve);
        } else {
            updateById(reserve);
        }

        return new ResultVO<>(true);
    }


    /**
     * 预约审核
     * @param
     * @return
     */
    public ResultVO checkReserve(ReserveDTO reserveDTO) {

        ReserveVO reserveVO = baseMapper.queryReserveInfo(reserveDTO.getId());

        Optional.ofNullable(reserveVO).orElseThrow(() -> new RuntimeException("预约不存在"));

        Reserve reserve = BeanUtil.copyProperties(reserveVO, Reserve.class);
        reserve.setState(StrUtil.equals("1", reserveDTO.getCheckState()) ?  "3" : "6");
        updateById(reserve);

       return new ResultVO<>(true);
    }


    /**
     * 预约支付
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO payReserve(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        ReserveVO reserveVO = baseMapper.queryReserveInfo(id);

        Optional.ofNullable(reserveVO).orElseThrow(() -> new RuntimeException("预约不存在"));
        if (!StrUtil.equals("3", reserveVO.getState())) {
            throw new RuntimeException("当前预约状态不允许支付");
        }

        String currentUserId = LoginUtil.getCurrentUserId();
        if (!StrUtil.equals(reserveVO.getCreateUser(), currentUserId)) {
            throw new RuntimeException("只能支付自己的预约订单");
        }

        // 扣除用户余额
        User user = userService.getById(currentUserId);
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("用户不存在"));
        if ((user.getBalance() - reserveVO.getRelatedPrice()) < 0) {
            throw new RuntimeException("余额不足，请充值");
        }

        user.setBalance(user.getBalance() - (reserveVO.getRelatedPrice()));
        if (!user.updateById()) {
            throw new RuntimeException("支付失败，请稍后重试");
        }

        Reserve reserve = BeanUtil.copyProperties(reserveVO, Reserve.class);
        reserve.setState("4");
        reserve.setPayTime(DateTool.getCurrentTime());
        if (!updateById(reserve)) {
            throw new RuntimeException("支付失败，请稍后重试");
        }

        return new ResultVO<>(true);
    }


    /**
     * 取消预约
     * @param id
     * @return
     */
    public ResultVO cancelReserve(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        Reserve reserve = getById(id);
        Optional.ofNullable(reserve).orElseThrow(() -> new RuntimeException("预约不存在"));

        String currentUserId = LoginUtil.getCurrentUserId();
        String currentUserRole = LoginUtil.getCurrentUserRole();

        if (StrUtil.equals("2", currentUserRole) && !StrUtil.equals(reserve.getCreateUser(), currentUserId)) {
            throw new RuntimeException("您无权取消该预约");
        }

        if (!(StrUtil.equalsAny(reserve.getState(), "1", "3"))) {
            throw new RuntimeException("当前预约状态不允许取消");
        }

        reserve.setState("7");
        reserve.setUpdateTime(DateTool.getCurrentTime());
        updateById(reserve);

        return new ResultVO<>(true);
    }


    /**
     * 删除预约
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO deleteReserve(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        Reserve reserve = getById(id);
        Optional.ofNullable(reserve).orElseThrow(() -> new RuntimeException("预约不存在"));

        String currentUserRole = LoginUtil.getCurrentUserRole();
        if (!StrUtil.equals("1", currentUserRole)) {
            throw new RuntimeException("只有管理员可以删除预约记录");
        }

        evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery().eq(Evaluate::getReserveObject, id));

        return new ResultVO(removeById(id));
    }

}
