package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Colum;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.ColumServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zst
 * @since 2020-02-16
 */
@RestController
@RequestMapping("/colum")
public class ColumController {

    @Autowired
    ColumServiceImpl columService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //add.......................................................................................................
    //delete.......................................................................................................
    //update.......................................................................................................
    //query.......................................................................................................
    @RequestMapping("/queryColums")
    public IPage<Colum> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize){
        IPage<Colum> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
        IPage<Colum> columIPage = columService.page(page,queryWrapper);
        return columIPage;
    }


}
