package com.barber.controller;


import com.barber.mapping.HaircuttingProjectDTO;
import com.barber.service.HaircuttingProjectService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 理发项目 前端控制器
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/haircuttingProject")
public class HaircuttingProjectController {

    @Resource
    private HaircuttingProjectService haircuttingProjectService;


    @PostMapping("/list")
    public ResultVO queryHaircuttingProjectPaginationList(@RequestBody HaircuttingProjectDTO haircuttingProjectDTO) {
        return haircuttingProjectService.queryHaircuttingProjectPaginationList(haircuttingProjectDTO);
    }

    @GetMapping("/home")
    public ResultVO getHaircuttingProjectList() {
        return haircuttingProjectService.getHaircuttingProjectList();
    }


    @GetMapping("/info")
    public ResultVO getHaircuttingProjectInfo(@RequestParam("id") String id) {
        return haircuttingProjectService.getHaircuttingProjectInfo(id);
    }


    @PostMapping("/add")
    public ResultVO addHaircuttingProject(@RequestBody HaircuttingProjectDTO haircuttingProjectDTO) {
        return haircuttingProjectService.addOrModifyHaircuttingProject(haircuttingProjectDTO);
    }


    @PostMapping("/modify")
    public ResultVO modifyHaircuttingProject(@RequestBody HaircuttingProjectDTO haircuttingProjectDTO) {
        return haircuttingProjectService.addOrModifyHaircuttingProject(haircuttingProjectDTO);
    }


    @GetMapping("/del")
    public ResultVO deleteHaircuttingProject(@RequestParam("id") String id) {
        return haircuttingProjectService.deleteHaircuttingProject(id);
    }

}
