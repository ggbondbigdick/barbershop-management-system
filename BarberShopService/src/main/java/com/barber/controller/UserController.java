package com.barber.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.barber.entity.User;
import com.barber.mapping.UserDTO;
import com.barber.service.UserService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("register")
    public ResultVO register(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        userDTO.setRole("2");
        return userService.addOrUpdateUser(userDTO, request);
    }


    @PostMapping("login")
    public ResultVO userLogin(@RequestBody User user) { return userService.userLogin(user); }


    @PostMapping("page")
    public ResultVO userPage(@RequestBody UserDTO param) { return userService.getUserPage(param); }


    @GetMapping("info")
    public ResultVO userInfo(@RequestParam("id") String id) { return userService.getUserInfo(id); }


    @GetMapping("statistics")
    public ResultVO getStatisticsData() { return userService.getStatisticsData(); }


    @GetMapping("availableStaff")
    public ResultVO getAvailableStaff(@RequestParam("projectType") String projectType, @RequestParam("reserveDate") String reserveDate) {
        return userService.getAvailableStaff(projectType, reserveDate);
    }


    @PostMapping("modify")
    public ResultVO modifyUser(@RequestBody UserDTO param, HttpServletRequest request) {
        param.setRole("3");
        return userService.addOrUpdateUser(param, request);
    }


    @PostMapping("modifyPassword")
    public ResultVO modifyPassword(@RequestBody UserDTO userDTO) { return userService.modifyUserPassword(userDTO); }


    @DeleteMapping("del/{id}")
    public ResultVO userDel(@PathVariable("id") String id) { return userService.deleteUser(id); }


    @DeleteMapping("cleanUnusedData")
    public ResultVO cleanUnusedData() { return userService.cleanUnusedData(); }


    @PostMapping("resetPassword")
    public ResultVO userResetPassword(@RequestBody UserDTO userDTO) { return userService.resetUserPassword(userDTO); }


    @GetMapping("recharge")
    public ResultVO userRecharge(@RequestParam("balance") double balance) {
        UserDTO userDTO = new UserDTO();
        userDTO.setBalance(balance);
        return userService.rechargeBalance(userDTO);
    }


    @GetMapping("verifyCode")
    public void sendVerifyCode(@RequestParam("email") String email, @RequestParam("type") String type) {
        userService.sendEmailVerifyCode(email, type);
    }


    @GetMapping("freezeOrThaw")
    public ResultVO userFreeze(@RequestParam("id") String id) { return userService.freezeOrThawUser(id); }


    @GetMapping("logout")
    public ResultVO userLogout() {
        StpUtil.logout();
        return new ResultVO<>(Boolean.TRUE);
    }

}
