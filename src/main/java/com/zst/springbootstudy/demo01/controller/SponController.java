package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.SponServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
@RequestMapping("/spon")
public class SponController {

    @Autowired
    SponServiceImpl sponService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    //add.......................................................................................................

    /**
     * 添加机构
     * @param spon
     * @return
     */
    @RequestMapping("/AddSpon")
    public Map addSpon( @RequestBody Spon spon){
        Map map = new HashMap();
        map.put("code","100");
        QueryWrapper<Spon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Spon :: getSponName,spon.getSponName())
                .eq(Spon::getSite,spon.getSite()).eq(Spon::getOrgId,stringRedisTemplate.opsForValue().get("orgId"));
        int size = sponService.list(queryWrapper).size();
        if(size > 0){
            map.put("msg","已存在");
        }else{
            spon.setOrgId(Integer.valueOf(stringRedisTemplate.opsForValue().get("orgId")));
            sponService.save(spon);
            map.put("msg","添加成功");
            map.put("code","200");
        }
        return map;
    }

    //delete.......................................................................................................

    /**
     * 删除赞助商
     * @param spon
     */
    @RequestMapping("/deleteOrgById")
    public void deleteOrgById(@RequestBody Spon spon){
        sponService.removeById(spon.getSponId());
    }

    //update.......................................................................................................

    /**
     * 更新赞助商
     * @param spon
     * @return
     */
    @RequestMapping("/updateSpon")
    public Map<String,Object> updateOrg(@RequestBody Spon spon){
        Map map = new HashMap();
        map.put("code","100");
        QueryWrapper<Spon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Spon :: getSponName,spon.getSponName())
               .or().eq(Spon::getSite,spon.getSite()).eq(Spon::getOrgId,stringRedisTemplate.opsForValue().get("orgId")).ne(Spon::getSponId,spon.getSponId());
        int size = sponService.list(queryWrapper).size();
        if(size > 0){
            map.put("msg","已存在");
        }else{
            sponService.updateById(spon);
            map.put("spon",sponService.getById(spon.getSponId()));
            map.put("msg","成功");
            map.put("code","200");
        }

        return map;
    }

    //query.......................................................................................................
    /**
     * 分页请求
     * @param pageNo 页号
     * @param pageSize 页面大小
     * @return
     */
    @RequestMapping("/page")
    public IPage<Spon> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize,@Param("condition")Spon spon){
        IPage<Spon> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
        IPage<Spon> sponIPage = sponService.page(page,queryWrapper);
        return sponIPage;
    }


}
