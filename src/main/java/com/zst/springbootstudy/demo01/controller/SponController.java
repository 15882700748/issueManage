package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.SponServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;

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
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Spon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Spon :: getSponName,spon.getSponName())
                .eq(Spon::getOrgId,orgId);
        int size = sponService.list(queryWrapper).size();
        if(size > 0){
            map.put("msg","已存在");
        }else{
            spon.setOrgId(Integer.valueOf(orgId));
            //图标控制
            String fileName = spon.getLogoUrl();
            try {
                String path = ResourceUtils.getURL("classpath:static/").getPath();
                String prex = stringRedisTemplate.opsForValue().get("sponTempPrex");
                String tempPath;
                String targetPath;
                String tempFilePath;
                String targetFilePath;
                boolean isUpload ;
                // no upload
                if(StringUtils.isEmpty(prex)){
                    prex = randomUUID().toString();
                    tempPath = path + "sponIcon/";
                    tempFilePath =tempPath +fileName;
                    isUpload = false;
                }else {
                    tempPath = path + "temp/";
                    tempFilePath =tempPath +prex+fileName;
                    isUpload = true;
                }
                targetPath = path + "sponIcon/";
                targetFilePath = targetPath +prex+ fileName;
                //decode
                tempFilePath = URLDecoder.decode(tempFilePath,"utf-8");
                targetFilePath = URLDecoder.decode(targetFilePath,"utf-8");
                File tempFile = new File(tempFilePath);
                File targetFile = new File(targetFilePath);
                System.out.println(tempFilePath);
                System.out.println(targetFilePath);
                if(tempFile.exists()&&tempFile.isFile()){
                    //tempFile move to targetFile
                    FileChannel input = new FileInputStream(tempFile).getChannel();
                    FileChannel  output = new FileOutputStream(targetFile).getChannel();
                    output.transferFrom(input, 0, input.size());
                    spon.setLogoUrl(prex+fileName);
                    System.out.println(spon.getLogoUrl());
                }else{
                    System.out.println("文件不存在or不是文件");
                }
                if(isUpload){
                    //temp文件删除
                    tempPath =  URLDecoder.decode(tempPath,"utf-8");
                    File[] files = new File(tempPath).listFiles();
                    for(File file : files){
                        file.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            sponService.save(spon);
            stringRedisTemplate.opsForValue().set("sponTempPrex","");
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
    @RequestMapping("/deleteSponById")
    public void deleteOrgById(@RequestBody Spon spon){
        spon = sponService.getById(spon.getSponId());
        try {
            File  file = new File(URLDecoder.decode(ResourceUtils.getURL("classpath:static/").getPath()+"sponIcon/"+spon.getLogoUrl()));
            file.delete();
            sponService.removeById(spon.getSponId());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public IPage<Spon> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize,@Param("sponNom")String sponName,@Param("site")String site){
        IPage<Spon> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
        if(StringUtils.isNotEmpty(sponName)){
            queryWrapper.eq("sponName",sponName);
        }
        if(StringUtils.isNotEmpty(site)){
            queryWrapper.eq("site",site);
        }
        IPage<Spon> sponIPage = sponService.page(page,queryWrapper);
        return sponIPage;
    }


}
