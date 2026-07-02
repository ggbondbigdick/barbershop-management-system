package com.barber.controller;


import com.barber.mapping.ReserveDTO;
import com.barber.service.ReserveService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 预约 前端控制器
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Resource
    private ReserveService reserveService;


    @PostMapping("/page")
    public ResultVO getReservePaginationList(@RequestBody ReserveDTO reserveDTO) {return reserveService.getReservePaginationList(reserveDTO);}


    @GetMapping("/info")
    public ResultVO getReserveInfo(@RequestParam("id") String id) {return reserveService.getReserveInfo(id);}


    @PostMapping("/add")
    public ResultVO addReserve(@RequestBody ReserveDTO reserveDTO) {return reserveService.addOrModifyReserve(reserveDTO);}


    @PostMapping("/modify")
    public ResultVO modifyReserve(@RequestBody ReserveDTO reserveDTO) {return reserveService.addOrModifyReserve(reserveDTO);}


    @PostMapping("/check")
    public ResultVO checkReserve(@RequestBody ReserveDTO reserveDTO) {return reserveService.checkReserve(reserveDTO);}


    @GetMapping("/pay")
    public ResultVO payReserve(@RequestParam("id") String id) {return reserveService.payReserve(id);}


    @GetMapping("/cancel")
    public ResultVO cancelReserve(@RequestParam("id") String id) {return reserveService.cancelReserve(id);}


    @GetMapping("/del")
    public ResultVO deleteReserve(@RequestParam("id") String id) {return reserveService.deleteReserve(id);}

}
