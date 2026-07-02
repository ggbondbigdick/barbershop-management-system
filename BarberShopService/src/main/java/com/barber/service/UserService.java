package com.barber.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.tool.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.barber.entity.Attendance;
import com.barber.entity.Evaluate;
import com.barber.entity.Reserve;
import com.barber.entity.User;
import com.barber.mapper.AttendanceMapper;
import com.barber.mapper.EvaluateMapper;
import com.barber.mapper.ReserveMapper;
import com.barber.mapper.UserMapper;
import com.barber.mapping.UserDTO;
import com.barber.tool.data.ResultVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Value("${rsa.privateKey}")
    private String privateKey;

    @Resource
    private PreviewTool previewTool;

    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private ReserveMapper reserveMapper;

    @Resource
    private EvaluateMapper evaluateMapper;


    /**
     * 用户登录
     * user login
     *
     * @param user
     * @return
     */
    public ResultVO userLogin(User user) {

        User newUser = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, user.getAccount().toUpperCase()));

        if (!Optional.ofNullable(newUser).isPresent()) {
            return new ResultVO<>(false, "账号或密码错误");
        }

        if (Objects.equals(PassTool.decrypt(newUser.getPassword(), PassTool.getPrivateKey(privateKey)),
                PassTool.decrypt(user.getPassword(), PassTool.getPrivateKey(privateKey)))) {
            // 执行登录
            StpUtil.login(newUser.getId(), "PC");
            // 获取 token
            String token = StpUtil.getTokenInfo().getTokenValue();
            // 获取 token 有效期
            long expireTime = StpUtil.getTokenTimeout();
            // 计算 token 到期时间
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(expireTime);
            long timestampSeconds = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
            // 获取登入设备类型
            String device = StpUtil.getLoginDevice();

            JSONObject json = new JSONObject(new LinkedHashMap<>());
            json.put("token", token);
            json.put("device", device);
            json.put("expireTime", timestampSeconds);

            return new ResultVO(true, json, "登陆成功");
        }

        return new ResultVO(false, "账号或密码错误");
    }


    /**
     * 获取用户分页列表
     * Get user paging list
     *
     * @param param
     * @return
     */
    public ResultVO getUserPage(UserDTO param) {

        // 构造查询条件
        LambdaQueryWrapper<User> query = Wrappers.<User>lambdaQuery().ne(User::getRole, "1")
                .orderByDesc(User::getCreateTime);

        if (StrUtil.isNotBlank(param.getQueryParam())) {
            query.and(q -> q.like(User::getAccount, param.getQueryParam())
                    .or().like(User::getName, param.getQueryParam()));
        }

        if (StrUtil.isNotBlank(param.getRole())) {
            if (StrUtil.equals("2", param.getRole())) {
                query.and(q -> q.eq(User::getRole, "2").or().isNull(User::getRole).or().eq(User::getRole, ""));
            } else {
                query.eq(User::getRole, param.getRole());
            }
        }

        PageHelper.startPage(param.getPage(), param.getSize());

        List<User> userList = list(query);

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        List<JSONObject> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
            list = pageInfo.getList().stream().map(user -> {
                JSONObject json = new JSONObject(new LinkedHashMap<>());
                json.put("id", user.getId());
                json.put("avatarUrl", StrUtil.isBlank(user.getAvatar()) ? "" : previewTool.getFilePreviewUrl(user.getAvatar()));
                json.put("account", user.getAccount());
                json.put("name", user.getName());
                json.put("age", user.getAge());
                json.put("sex", user.getSex());
                json.put("contactTel", user.getContactTel());
                json.put("balance", user.getBalance());
                json.put("role", StrUtil.blankToDefault(user.getRole(), "2"));
                json.put("goodType", user.getGoodType());
                json.put("goodTypeLabel", convertGoodTypeLabel(user.getGoodType()));
                json.put("registerTime", StrUtil.sub(user.getCreateTime(), 0, 10));
                return json;
            }).collect(Collectors.toList());
        }

        ResultVO r = new ResultVO<>();
        r.setSuccess(Boolean.TRUE);
        r.setRows(list);
        r.setTotal(pageInfo.getTotal());

        return r;
    }


    /**
     * 获取用户信息
     * Get user information
     *
     * @return
     */
    public ResultVO getUserInfo(String id) {

        String userId = "";

        if (StrUtil.isBlank(id)) {
            userId = LoginUtil.getCurrentUserId();
            if (StrUtil.isBlank(userId)) return new ResultVO(false, "用户未登录");
        } else {
            userId = id;
        }

        User user = getById(userId);

        JSONObject json = getJsonObject(user);

        return new ResultVO<>(true, json);
    }

    private static @NotNull JSONObject getJsonObject(User user) {

        JSONObject json = new JSONObject(new LinkedHashMap<>());
        json.put("id", user.getId());
        json.put("avatar", user.getAvatar());
        json.put("account", user.getAccount());
        json.put("name", user.getName());
        json.put("age", user.getAge());
        json.put("sex", user.getSex());
        json.put("contactTel", user.getContactTel());
        json.put("balance", user.getBalance());
        json.put("role", StrUtil.blankToDefault(user.getRole(), "2"));
        json.put("goodType", user.getGoodType());

        return json;
    }


    /**
     * 获取可预约理发师
     * @param projectType
     * @param reserveDate
     * @return
     */
    public ResultVO getAvailableStaff(String projectType, String reserveDate) {

        Optional.ofNullable(projectType).orElseThrow(() -> new RuntimeException("项目类型不能为空"));
        Optional.ofNullable(reserveDate).orElseThrow(() -> new RuntimeException("预约日期不能为空"));

        List<User> staffList = list(Wrappers.<User>lambdaQuery()
                .eq(User::getRole, "3")
                .and(q -> q.eq(User::getGoodType, projectType).or().isNull(User::getGoodType).or().eq(User::getGoodType, "")));

        List<JSONObject> result = staffList.stream().map(staff -> {
            JSONObject json = getJsonObject(staff);
            json.put("avatarUrl", StrUtil.isBlank(staff.getAvatar()) ? "" : previewTool.getFilePreviewUrl(staff.getAvatar()));
            json.put("goodTypeLabel", convertGoodTypeLabel(staff.getGoodType()));

            String status = "AVAILABLE";
            String statusLabel = "可预约";

            String dayStart = StrUtil.format("{} 00:00:00", reserveDate);
            String dayEnd = StrUtil.format("{} 23:59:59", reserveDate);

            long leaveCount = attendanceMapper.selectCount(Wrappers.<Attendance>lambdaQuery()
                    .eq(Attendance::getStaff, staff.getId())
                    .eq(Attendance::getType, "2")
                    .between(Attendance::getCreateTime, dayStart, dayEnd));

            if (leaveCount > 0) {
                status = "LEAVE";
                statusLabel = "当天请假";
            } else if (StrUtil.equals(reserveDate, DateTool.getCurrentDate())) {
                long attendanceCount = attendanceMapper.selectCount(Wrappers.<Attendance>lambdaQuery()
                        .eq(Attendance::getStaff, staff.getId())
                        .eq(Attendance::getType, "1")
                        .between(Attendance::getCreateTime, dayStart, dayEnd));

                if (attendanceCount == 0) {
                    status = "OFF_DUTY";
                    statusLabel = "今日不在岗";
                }
            }

            long activeReserveCount = reserveMapper.selectCount(Wrappers.<Reserve>lambdaQuery()
                    .eq(Reserve::getReserveStaff, staff.getId())
                    .eq(Reserve::getReserveTime, reserveDate)
                    .in(Reserve::getState, "1", "2", "3", "4"));

            if (!StrUtil.equalsAny(status, "LEAVE", "OFF_DUTY") && activeReserveCount > 0) {
                status = "BOOKED";
                statusLabel = "该日已约满";
            }

            json.put("status", status);
            json.put("statusLabel", statusLabel);
            json.put("available", StrUtil.equals(status, "AVAILABLE"));
            return json;
        }).collect(Collectors.toList());

        return new ResultVO<>(true, result);
    }

    private String convertGoodTypeLabel(String goodType) {
        if (StrUtil.isBlank(goodType)) {
            return "全能理发师";
        }

        switch (goodType) {
            case "1":
                return "擅长洗发";
            case "2":
                return "擅长理发";
            case "3":
                return "擅长烫发";
            case "4":
                return "擅长染发";
            case "5":
                return "擅长头发保养";
            default:
                return "全能理发师";
        }
    }


    /**
     * 获取统计数据
     * Get statistical data
     *
     * @return
     */
    public ResultVO getStatisticsData() {

        // get user balance
        double userBalance = 0;

        // get number of orders placed in the past seven days
        int orderCount = 0;

        // get total user number
        long userCount = count(new LambdaQueryWrapper<User>().ne(User::getRole, "1"));

        JSONObject resultJson = new JSONObject();
        resultJson.put("userBalance", userBalance);
        resultJson.put("orderCount", orderCount);
        resultJson.put("userCount", userCount);

        return new ResultVO<>(true, resultJson);
    }



    /**
     * 新增（修改）用户
     * add or update user
     *
     * @param param
     * @return
     */
    public ResultVO addOrUpdateUser(UserDTO param, HttpServletRequest request) {

        Long count = baseMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getAccount, param.getAccount().toUpperCase())
                .ne(StrUtil.isNotBlank(param.getId()), User::getId, param.getId()));
        if (count > 0) {
            return new ResultVO(false, "账号已被注册，请更换账号注册");
        }

        String time = DateTool.getCurrentZoneTime(HeaderTool.getHeaderTimezoneString(request));
        User user = BeanUtil.copyProperties(param, User.class);
        user.setAccount(param.getAccount().toUpperCase());
        user.setUpdateTime(time);

        if (StrUtil.isBlank(param.getId())) {
            String id = IdUtil.simpleUUID();
            user.setId(id);
            user.setCreateTime(time);
            user.setRole(StrUtil.blankToDefault(param.getRole(), "2"));
            user.insert();
        } else {
            user.setId(param.getId());
            user.updateById();
        }

        return new ResultVO(true);
    }


    /**
     * 修改用户密码
     * Modify user password
     *
     * @param param
     * @return
     */
    public ResultVO modifyUserPassword(UserDTO param) {

        // get current user ID
        String userId = LoginUtil.getCurrentUserId();
        Optional.ofNullable(userId).orElseThrow(() -> new RuntimeException("用户未登录"));
        User user = getById(userId);

        if (!Objects.equals(PassTool.decrypt(param.getOldPassword(), PassTool.getPrivateKey(privateKey)),
                PassTool.decrypt(user.getPassword(), PassTool.getPrivateKey(privateKey)))) {
            return new ResultVO(false, "原密码输入错误");
        }

        // modify password
        user.setPassword(param.getPassword());
        user.updateById();

        return new ResultVO(true);
    }


    /**
     * 重置密码
     * Reset user password
     *
     * @param userDTO
     * @return
     */
    public ResultVO resetUserPassword(UserDTO userDTO) {

        // check captcha
        // boolean validated = captchaTool.validateCaptcha(StrUtil.format("{}{}", "RST_", ""), "");
        // if (!validated) {
        //     return new ResultVO(false, "Verification code error");
        // }

        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, userDTO.getAccount().toUpperCase()));
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("User not registered or registered email error"));

        if (Objects.equals(user.getRole(), "1")) { throw new RuntimeException("User does not exist"); }

        // reset password
        user.setPassword(userDTO.getPassword());
        user.updateById();

        return new ResultVO(true);
    }


    /**
     * 余额充值
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO rechargeBalance(UserDTO param) {

        String userId = LoginUtil.getCurrentUserId();
        if (StrUtil.isBlank(userId)) {
            throw new RuntimeException("用户未登录");
        }
        if (param.getBalance() <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }

        User user = getById(userId);
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setBalance(user.getBalance() + param.getBalance());
        user.setUpdateTime(DateTool.getCurrentTime());
        if (!user.updateById()) {
            throw new RuntimeException("充值失败，请稍后重试");
        }

        JSONObject result = new JSONObject();
        result.put("balance", user.getBalance());

        return new ResultVO<>(true, result);
    }


    /**
     * 发送验证码
     * Send verification code
     *
     * @param email
     * @param type
     */
    public void sendEmailVerifyCode(String email, String type) {
        if (StrUtil.isNotBlank(email)) {
            // if (Objects.equals(type, "1")) {
            //     // 注册
            //     String code = captchaTool.generateCaptcha();
            //     // emailTool.sendSimpleMail(email, "Order Management System Registration Verification Code", "You Registration Verification code: " + code);
            //     captchaTool.saveCaptcha(StrUtil.format("{}{}", "REG_", email), code, 2);
            // }
            //
            // if (Objects.equals(type, "2")) {
            //     // 重置密码
            //     String code = captchaTool.generateCaptcha();
            //     // emailTool.sendSimpleMail(email, "Order Management System Reset Password Verification Code", "You Reset Password Verification code: " + code);
            //     captchaTool.saveCaptcha(StrUtil.format("{}{}", "RST_", email), code, 2);
            // }
        }
    }


    /**
     * 冻结 (解冻)用户
     * freeze or thaw user
     *
     * @param id ID
     * @return {@link ResultVO}
     */
    public ResultVO freezeOrThawUser(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new RuntimeException("User ID cannot be empty"));

        User user = getById(id);
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("User information exception"));

        // user.setStatus(Objects.equals(user.getStatus(), "1") ? "2" : "1");
        user.updateById();

        return new ResultVO<>(true);
    }


    /**
     * 删除用户
     * delete user
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO deleteUser(String userId) {

        Optional.ofNullable(userId).orElseThrow(() -> new IllegalArgumentException("User ID cannot be empty"));

        checkAdminPermission();

        User user = getById(userId);
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("User information exception"));

        List<String> reserveIds = reserveMapper.selectList(Wrappers.<Reserve>lambdaQuery()
                        .eq(Reserve::getCreateUser, userId)
                        .or()
                        .eq(Reserve::getReserveStaff, userId))
                .stream()
                .map(Reserve::getId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(reserveIds)) {
            evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery().in(Evaluate::getReserveObject, reserveIds));
        }

        evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery().eq(Evaluate::getCreateUser, userId));
        reserveMapper.delete(Wrappers.<Reserve>lambdaQuery()
                .eq(Reserve::getCreateUser, userId)
                .or()
                .eq(Reserve::getReserveStaff, userId));
        attendanceMapper.delete(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getStaff, userId));

        return new ResultVO(removeById(userId));
    }


    /**
     * 清理历史遗留的无效关联数据
     * clean historical orphan data
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultVO cleanUnusedData() {

        checkAdminPermission();

        int evaluateWithoutUserCount = evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery()
                .notExists("SELECT 1 FROM sys_user u WHERE u.id = evaluate.create_user"));
        int evaluateWithoutReserveCount = evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery()
                .notExists("SELECT 1 FROM reserve r WHERE r.id = evaluate.reserve_object"));

        int reserveWithoutUserCount = reserveMapper.delete(Wrappers.<Reserve>lambdaQuery()
                .notExists("SELECT 1 FROM sys_user u WHERE u.id = reserve.create_user"));
        int reserveWithoutProjectCount = reserveMapper.delete(Wrappers.<Reserve>lambdaQuery()
                .notExists("SELECT 1 FROM haircutting_project p WHERE p.id = reserve.reserve_object"));
        int reserveWithoutStaffCount = reserveMapper.delete(Wrappers.<Reserve>lambdaQuery()
                .isNotNull(Reserve::getReserveStaff)
                .ne(Reserve::getReserveStaff, "")
                .notExists("SELECT 1 FROM sys_user u WHERE u.id = reserve.reserve_staff"));

        int evaluateWithoutReserveAfterReserveCleanCount = evaluateMapper.delete(Wrappers.<Evaluate>lambdaQuery()
                .notExists("SELECT 1 FROM reserve r WHERE r.id = evaluate.reserve_object"));
        int attendanceWithoutStaffCount = attendanceMapper.delete(Wrappers.<Attendance>lambdaQuery()
                .notExists("SELECT 1 FROM sys_user u WHERE u.id = attendance.staff"));

        JSONObject result = new JSONObject(new LinkedHashMap<>());
        result.put("evaluateWithoutUserCount", evaluateWithoutUserCount);
        result.put("evaluateWithoutReserveCount", evaluateWithoutReserveCount + evaluateWithoutReserveAfterReserveCleanCount);
        result.put("reserveWithoutUserCount", reserveWithoutUserCount);
        result.put("reserveWithoutProjectCount", reserveWithoutProjectCount);
        result.put("reserveWithoutStaffCount", reserveWithoutStaffCount);
        result.put("attendanceWithoutStaffCount", attendanceWithoutStaffCount);

        return new ResultVO<>(true, result);
    }


    private void checkAdminPermission() {

        User currentUser = getById(LoginUtil.getCurrentUserId());
        if (!Objects.equals("1", Optional.ofNullable(currentUser).map(User::getRole).orElse(""))) {
            throw new RuntimeException("No permission");
        }

    }
}
