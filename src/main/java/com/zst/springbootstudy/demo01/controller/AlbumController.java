package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Album;
import com.zst.springbootstudy.demo01.entity.Img;
import com.zst.springbootstudy.demo01.service.impl.AlbumServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.ImgServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
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
 * @since 2020-03-13
 */
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    AlbumServiceImpl albumService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ImgServiceImpl imgService;


    //add.......................................................................................................

    /**
     * 添加相册
     * @param album
     * @return
     */
    @RequestMapping("/addAlbum")
    public Map<String,Object> addAlbum(@RequestBody Album album){
        Map<String, Object> map = new HashMap<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        map.put("code","100");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("albumName",album.getAlbumName());
        queryWrapper.eq("orgId",orgId);
        int size = albumService.list(queryWrapper).size();
        if(size >0 ){
            map.put("msg","相册已存在");
        }else{
            map.put("msg","添加成功");
            map.put("code","200");
            album.setCreateTime(LocalDateTime.now());
            album.setOrgId(Integer.valueOf(orgId));
            albumService.save(album);
        }
        return map;
    }

    //delete.......................................................................................................

    /**
     * 删除相册
     * @param album
     */
    @RequestMapping("/deleteAlbum")
    public void deleteAlbum(@RequestBody Album album){
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("albumId",album.getAlbumId());
        queryWrapper.eq("orgId",orgId);
        //deleteImgFile
        List<Img> imgs = imgService.list(queryWrapper);
        String path = "";
        try {
            path = ResourceUtils.getURL("classpath:static/").getPath()+"album/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Img item : imgs){
            try {
                String filePath =  path+ item.getImgUrl();
                filePath = URLDecoder.decode(filePath);
                File file = new File(filePath);
                if(file.exists()&&file.isFile()){
                    file.delete();
                }else{
                    System.out.println("文件不存在");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //deleteImg
        imgService.remove(queryWrapper);
        //deleteAlbum
        albumService.removeById(album.getAlbumId());

    }

    //update.......................................................................................................

    /**
     * 更新相册
     * @param album
     * @return
     */
    @RequestMapping("/updateAlbum")
    public Map<String,Object> updateAlbum(@RequestBody Album album){
        Map<String, Object> map = new HashMap<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        map.put("code","100");
        QueryWrapper<Album> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Album::getAlbumName,album.getAlbumName()).eq(Album::getOrgId,orgId);
        int size = albumService.list(queryWrapper).size();
        if(size >1 ){
            map.put("msg","相册已存在");
        }else{
            map.put("msg","修改成功成功");
            map.put("code","200");
            albumService.updateById(album);
        }
        return map;
    }

    //query.......................................................................................................

    @RequestMapping("/pageQuery")
    public IPage<Album> page(@Param("pageNo")Integer  pageNo, @Param("pageSize") Integer pageSize){
        IPage<Album> page = new Page<>(pageNo,pageSize);
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orgId",orgId);
        IPage<Album> albumIPage= albumService.page(page,queryWrapper);
        return albumIPage;
    }
    @RequestMapping("/getFisrtPicture")
    public Map<String, Object> getFirstPicture(@RequestBody Album album){
        Map<String, Object> map = new HashMap<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("albumId",album.getAlbumId());
        int size = imgService.list(queryWrapper).size();
        Img img;
        if(size > 0){
            img = (Img) imgService.list(queryWrapper).get(0);
        }else{
            img = new Img();
            img.setImgUrl("16christine-zhu-1460573-unsplash.jpg");
        }
        map.put("img",img);
        return map;
    }
}
