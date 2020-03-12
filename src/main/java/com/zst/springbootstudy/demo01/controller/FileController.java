package com.zst.springbootstudy.demo01.controller;

import com.github.pagehelper.util.StringUtil;
import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.OrganizationServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.SponServiceImpl;
import com.zst.springbootstudy.demo01.tool.ToolUpLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.Map;

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

    @RequestMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file")MultipartFile file){
        String id=stringRedisTemplate.opsForValue().get("orgId");
        Map map =toolUpLoad.fileUpload(file,"orgId"+id,"defaultImg/");
        if("200".equalsIgnoreCase(String.valueOf(map.get("code")))){
            Organization organization = new Organization();
            organization.setOrgId(Integer.valueOf(id));
            organization.setLogoUrl("orgId"+id + file.getOriginalFilename());
            organizationService.updateById(organization);
        }
        return map;
    }

    @RequestMapping("/updateSponIcon/{id}")
    public Map<String, Object> uploadSponIcon(@RequestParam("file")MultipartFile file, @PathVariable("id")String sponId){
        String id=stringRedisTemplate.opsForValue().get("orgId");
        Map map =toolUpLoad.fileUpload(file,"sponid"+sponId,"sponIcon/");
        if("200".equalsIgnoreCase(String.valueOf(map.get("code")))){
            Spon spon = new Spon();
            spon.setSponId(Integer.valueOf(sponId));
            spon.setLogoUrl("sponid"+sponId + file.getOriginalFilename());
            sponService.updateById(spon);
        }
        return map;
    }
}
