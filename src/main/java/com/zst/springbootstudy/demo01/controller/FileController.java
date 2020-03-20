package com.zst.springbootstudy.demo01.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.zst.springbootstudy.demo01.entity.Img;
import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.ImgServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.OrganizationServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.SponServiceImpl;
import com.zst.springbootstudy.demo01.tool.FileTool;
import com.zst.springbootstudy.demo01.tool.ToolUpLoad;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.*;

@RestController
public class FileController {

    @Autowired
    ToolUpLoad toolUpLoad;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    OrganizationServiceImpl organizationService;
    @Autowired
    SponServiceImpl sponService;
    @Autowired
    ImgServiceImpl imgService;

    @Autowired
    FileTool fileTool;

    //addOrgHeader

    /**
     * 添加or修改机构的头像图标
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file")MultipartFile file){
        String id=stringRedisTemplate.opsForValue().get("orgId");
        String prex = randomUUID().toString();
        Map map =toolUpLoad.fileUpload(file, prex,"defaultImg/");
        if("200".equalsIgnoreCase(String.valueOf(map.get("code")))){
            Organization organization = new Organization();
            organization.setOrgId(Integer.valueOf(id));
            organization.setLogoUrl(prex + file.getOriginalFilename());
            organizationService.updateById(organization);
        }
        return map;
    }

    @RequestMapping("/uploadSponIcon")
    public Map<String,Object> addSponIcon(@RequestParam("file")MultipartFile file){
        String prex = randomUUID().toString();
        stringRedisTemplate.opsForValue().set("sponTempPrex",prex);
        Map map =toolUpLoad.fileUpload(file,prex,"temp/");
        return map;
    }

    @RequestMapping("/cancleUploadSponIcon")
    public void cancleUploadSponIcon(){
        try {
            String path = ResourceUtils.getURL("classpath:static/").getPath()+"temp/";
            File[] files = new File(URLDecoder.decode(path)).listFiles();
            for (File file:files){
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改赞助商图标
     * @param file
     * @param sponId
     * @return
     */
    @RequestMapping("/updateSponIcon/{id}")
    public Map<String, Object> uploadSponIcon(@RequestParam("file")MultipartFile file, @PathVariable("id")String sponId){
        String id=stringRedisTemplate.opsForValue().get("orgId");
        String prex = randomUUID().toString();
        Map map =toolUpLoad.fileUpload(file,prex,"sponIcon/");
        if("200".equalsIgnoreCase(String.valueOf(map.get("code")))){
            Spon tempSpon = sponService.getById(sponId);
            fileTool.deleteFile(tempSpon.getLogoUrl(),"sponIcon/");
            Spon spon = new Spon();
            spon.setSponId(Integer.valueOf(sponId));
            spon.setLogoUrl(prex+ file.getOriginalFilename());
            sponService.updateById(spon);
        }
        return map;
    }
    //img..............................................................................................................

    @RequestMapping("/uploadImg{status}/{id}")
    public Map<String,Object> uploadImg(@RequestParam("file")MultipartFile file,@PathVariable("id") String id,@PathVariable("status") String status){
        Map map ;
        String prex = randomUUID().toString();
        //有id则更新
        if("update".equalsIgnoreCase(status)){
            Img img = imgService.getById(id);
            //deleteImg
            String fileName = img.getImgUrl();
            try {
                String path = ResourceUtils.getURL("classpath:static/").getPath()+"album/";
                File targetFile = new File(URLDecoder.decode(path+fileName));
                targetFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{//无id则上传

        }
        map =toolUpLoad.fileUpload(file,prex,"album/");
        return map;
    }

    @RequestMapping("/uploadArticleImg")
    public Map<String, Object> uploadArticleImg(@RequestParam("fileName")MultipartFile file){
        return toolUpLoad.fileUpload(file,randomUUID().toString()+file.getOriginalFilename(),"article/");
    }


    @RequestMapping("/sponIconFileDelete")
    public void fileDelete(@Param("isUpload")boolean isUpload){
        if(isUpload){
            try {
                stringRedisTemplate.opsForValue().set("sponTempPrex","");
                String path = ResourceUtils.getURL("classpath:static/").getPath();
                String tempPath = path + "temp/";
                tempPath =  URLDecoder.decode(tempPath,"utf-8");
                File[] files = new File(tempPath).listFiles();
                for(File file : files){
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{

        }
    }
}
