package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Img;
import com.zst.springbootstudy.demo01.service.ImgService;
import com.zst.springbootstudy.demo01.service.impl.ImgServiceImpl;
import com.zst.springbootstudy.demo01.tool.ToolUpLoad;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zst
 * @since 2020-03-13
 */
@RestController
@RequestMapping("/img")
public class ImgController {

    @Autowired
    ImgServiceImpl imgService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ToolUpLoad toolUpLoad;

    //add.......................................................................................................

    /**
     * 添加图片信息
     * @param img
     * @return
     */
    @RequestMapping("/addImg/{albumId}")
    public Map<String,Object> addImg(@RequestBody Img img, @PathVariable("albumId")String albumId){
        Map<String, Object> map = new HashMap<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        map.put("code","200");
        map.put("msg","添加成功");
        img.setOrgId(Integer.valueOf(orgId));
        img.setAlbumId(Integer.valueOf(albumId));
        img.setUploadTime(LocalDateTime.now());
        imgService.save(img);
        return map;
    }

    //delete.......................................................................................................
    @RequestMapping("/deleteImg")
    public void deleteImg(@RequestBody Img img){
        Img tempImg = imgService.getById(img.getImgId());
        String fileName = tempImg.getImgUrl();
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:static/").getPath()+"album/";
            File targetFile = new File(path+fileName);
            targetFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgService.removeById(img.getImgId());
    }

    //update.......................................................................................................
    @RequestMapping("/updateImg")
    public Map<String,Object> updateImg(@RequestBody Img img){
        Map<String, Object> map = new HashMap<>();
        map.put("code","100");
        img.setUploadTime(LocalDateTime.now());
        imgService.updateById(img);
        return map;
    }

    //query.......................................................................................................
    @RequestMapping("/pageQuery")
    public IPage<Img> page(@Param("pageNo")Integer  pageNo, @Param("pageSize") Integer pageSize,@Param("albumId")String albumId){
        IPage<Img> page = new Page<>(pageNo,pageSize);
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orgId",orgId);
        queryWrapper.eq("albumId",albumId);
        IPage<Img> albumIPage= imgService.page(page,queryWrapper);
        return albumIPage;
    }


}
