package com.barber.controller;


import com.barber.mapping.EvaluateDTO;
import com.barber.service.EvaluateService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 评价 前端控制器
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    @PostMapping("/page")
    public ResultVO getEvaluatePaginationList(@RequestBody EvaluateDTO evaluateDTO) {
        return evaluateService.queryEvaluatePaginationList(evaluateDTO);
    }

    @GetMapping("/info")
    public ResultVO getEvaluateInfo(@RequestParam("id") String id) {
        return evaluateService.queryEvaluateInfo(id);
    }

    @PostMapping("/add")
    public ResultVO addEvaluate(@RequestBody EvaluateDTO evaluateDTO) {
        return evaluateService.addOrModifyEvaluate(evaluateDTO);
    }

    @PostMapping("/modify")
    public ResultVO modifyEvaluate(@RequestBody EvaluateDTO evaluateDTO) {
        return evaluateService.addOrModifyEvaluate(evaluateDTO);
    }

    @GetMapping("/del")
    public ResultVO deleteEvaluate(@RequestParam("id") String id) {
        return evaluateService.deleteEvaluate(id);
    }

}
