package com.barber.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.barber.entity.HaircuttingProject;
import com.barber.mapper.HaircuttingProjectMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.mapping.HaircuttingProjectDTO;
import com.barber.mapping.HaircuttingProjectVO;
import com.barber.tool.DateTool;
import com.barber.tool.FileTool;
import com.barber.tool.PageTool;
import com.barber.tool.PreviewTool;
import com.barber.tool.data.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 理发项目 服务实现类
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Service
public class HaircuttingProjectService extends ServiceImpl<HaircuttingProjectMapper, HaircuttingProject> {

    @Resource
    private PreviewTool previewTool;


    /**
     * 查询理发项目分页列表
     * @param haircuttingProjectDTO
     * @return
     */
    public ResultVO queryHaircuttingProjectPaginationList(HaircuttingProjectDTO haircuttingProjectDTO) {

        // 查询条件
        LambdaQueryWrapper<HaircuttingProject> query = Wrappers.<HaircuttingProject>lambdaQuery()
                .like(StrUtil.isNotBlank(haircuttingProjectDTO.getName()), HaircuttingProject::getName, haircuttingProjectDTO.getName())
                .eq(StrUtil.isNotBlank(haircuttingProjectDTO.getType()), HaircuttingProject::getType, haircuttingProjectDTO.getType())
                .orderByDesc(HaircuttingProject::getCreateTime);

        PageHelper.startPage(haircuttingProjectDTO.getPage(), haircuttingProjectDTO.getSize());

        // 查询列表
        List<HaircuttingProject> haircuttingProjectList = list(query);

        // 转换为VO 对象
        List<HaircuttingProjectVO> haircuttingProjectVOS = BeanUtil.copyToList(haircuttingProjectList, HaircuttingProjectVO.class);

        if (CollectionUtil.isNotEmpty(haircuttingProjectVOS)) {
            haircuttingProjectVOS = haircuttingProjectVOS.stream().map(haircuttingProjectVO -> {
                if (StrUtil.isNotBlank(haircuttingProjectVO.getThumbnail())) {
                    haircuttingProjectVO.setThumbnailUrl(previewTool.getFilePreviewUrl(haircuttingProjectVO.getThumbnail()));
                }
                return haircuttingProjectVO;
            }).collect(Collectors.toList());
        }

        PageInfo<HaircuttingProjectVO> pageInfo = new PageInfo<>(haircuttingProjectVOS);

        return PageTool.convertResult(pageInfo);
    }


    /**
     * 查询理发项目列表
     * @return
     */
    public ResultVO getHaircuttingProjectList() {

        LambdaQueryWrapper<HaircuttingProject> query = Wrappers.<HaircuttingProject>lambdaQuery()
                .orderByDesc(HaircuttingProject::getCreateTime);

        List<HaircuttingProject> haircuttingProjectList = list(query);

        // 转换为VO 对象
        List<HaircuttingProjectVO> haircuttingProjectVOS = BeanUtil.copyToList(haircuttingProjectList, HaircuttingProjectVO.class);

        if (CollectionUtil.isNotEmpty(haircuttingProjectVOS)) {
            haircuttingProjectVOS = haircuttingProjectVOS.stream().map(haircuttingProjectVO -> {
                if (StrUtil.isNotBlank(haircuttingProjectVO.getThumbnail())) {
                    haircuttingProjectVO.setThumbnailUrl(previewTool.getFilePreviewUrl(haircuttingProjectVO.getThumbnail()));
                }
                return haircuttingProjectVO;
            }).collect(Collectors.toList());
        }

        return PageTool.convertResult(haircuttingProjectVOS);
    }


    /**
     * 查询理发项目详情
     * @param id
     * @return
     */
    public ResultVO getHaircuttingProjectInfo(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new RuntimeException("ID 参数错误"));

        HaircuttingProject haircuttingProject = getById(id);

        HaircuttingProjectVO haircuttingProjectVO = BeanUtil.copyProperties(haircuttingProject, HaircuttingProjectVO.class);

        if (StrUtil.isNotBlank(haircuttingProjectVO.getThumbnail())) {
            haircuttingProjectVO.setThumbnailUrl(previewTool.getFilePreviewUrl(haircuttingProjectVO.getThumbnail()));
        }

        return PageTool.convertResult(haircuttingProjectVO);
    }


    /**
     * 添加或修改理发项目
     * @param haircuttingProjectDTO
     * @return
     */
    public ResultVO addOrModifyHaircuttingProject(HaircuttingProjectDTO haircuttingProjectDTO) {

        String time = DateTool.getCurrentTime();

        HaircuttingProject haircuttingProject = BeanUtil.copyProperties(haircuttingProjectDTO, HaircuttingProject.class);
        haircuttingProject.setUpdateTime(time);

        if (StrUtil.isBlank(haircuttingProject.getId())) {
            haircuttingProject.setId(IdUtil.simpleUUID());
            haircuttingProject.setCreateTime(time);
            save(haircuttingProject);
        } else {
            updateById(haircuttingProject);
        }

        return new ResultVO(true);
    }


    /**
     * 删除理发项目
     * @param id
     * @return
     */
    public ResultVO deleteHaircuttingProject(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new RuntimeException("ID 参数错误"));

        HaircuttingProject project = getById(id);

        if (FileTool.exists(project.getThumbnail())) {
            FileTool.delete(project.getThumbnail());
        }

        project.deleteById();

        return new ResultVO(true);
    }

}
