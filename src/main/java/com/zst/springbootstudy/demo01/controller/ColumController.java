package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Article;
import com.zst.springbootstudy.demo01.entity.Colum;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.ColumServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 添加栏目
     * @param colum
     * @return
     */
    @RequestMapping("/addColum")
    public Map<String,Object> addColum(@RequestBody Colum colum){
        Map<String,Object> map = new HashMap<>();
        map.put("code","100");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Colum> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Colum::getName,colum.getName()).eq(Colum::getIssueId,colum.getIssueId());
        int size = columService.list(queryWrapper).size();
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("orgId",orgId);
        queryWrapper1.eq("issueId",colum.getIssueId());
        int columnNumber = columService.list(queryWrapper1).size();
        if(size > 0 ){
            map.put("msg","已存在，请重新输入");
        }else{
            if(columnNumber >=8){
                map.put("msg","以最大数目，不可增加");
            }else{
                colum.setCreatTime(LocalDateTime.now());
                colum.setOrgId(Integer.valueOf(orgId));
                columService.save(colum);
                map.put("code","200");
                map.put("msg","添加成功");
            }

        }
        return map;
    }
    //delete.......................................................................................................

    /**
     * 删除栏目
     * @param colum
     */
    @RequestMapping("/deleteColum")
    public void deleteArticle(@RequestBody Colum colum){
        columService.removeById(colum.getColumId());
    }
    //update.......................................................................................................

    /**
     * 修改栏目
     * @param colum
     * @return
     */
    @RequestMapping("/updateColum")
    public Map<String,Object> updateColum(@RequestBody Colum colum){
        Map<String,Object> map = new HashMap<>();
        map.put("code","100");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Colum> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Colum::getName,colum.getName()).eq(Colum::getIssueId,colum.getIssueId());
        int size = columService.list(queryWrapper).size();
        if(size > 1 ){
            map.put("msg","已存在，请重新输入");
        }else{
            columService.updateById(colum);
            map.put("code","200");
            map.put("msg","修改成功");
        }
        return map;
    }
    //query.......................................................................................................

    /**
     * 查询栏目
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryColums")
    public IPage<Colum> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize,@Param("issueId")String issueId){
        IPage<Colum> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
        queryWrapper.eq("issueId",issueId);
        IPage<Colum> columIPage = columService.page(page,queryWrapper);
        return columIPage;
    }
    //前端

    @RequestMapping("/getColumns")
    public Map<String, Object> getColumns(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","100");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        String issueId = stringRedisTemplate.opsForValue().get("issueId");
        queryWrapper.eq("orgId",orgId);
        if(StringUtils.isNotEmpty(issueId)){
            queryWrapper.eq("issueId",issueId);
            List<Colum> list = columService.list(queryWrapper);
            map.put("code","200");
            map.put("msg","查询成功");
            map.put("columns",list);
        }else{
            map.put("msg","未设置展示会议");
        }

        return map;
    }


}
