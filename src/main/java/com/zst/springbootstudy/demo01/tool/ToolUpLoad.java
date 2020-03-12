package com.zst.springbootstudy.demo01.tool;


import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.service.impl.OrganizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class ToolUpLoad {

    @Autowired
    OrganizationServiceImpl organizationService;
    @Autowired
    JWTUtil jwtUtil;

    public  Map<String, Object> fileUpload(MultipartFile file,String prex,String targetPath) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String tempPath = null;
        resultMap.put("code","100");
        try {
            tempPath = ResourceUtils.getURL("classpath:static/").getPath()+targetPath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (null == file) {
            resultMap.put("result", false);
            resultMap.put("msg", "获取上传文件失败,请检查file上传组件的名称是否正确");
        } else if (file.isEmpty()) {
            resultMap.put("result", false);
            resultMap.put("msg", "没有选择文件");
        } else {
            File fileDir = new File(tempPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String filename = file.getOriginalFilename();
            String filePath ="";
            try {
                filePath = prex+ filename;
                filename = tempPath  + prex+ filename;


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                filename =  URLDecoder.decode(filename,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            File dest = new File(filename);
            System.out.println(filename);
            //保存文件
            try {
                file.transferTo(dest);
                resultMap.put("result", true);
                resultMap.put("msg", "上传成功");
                resultMap.put("filePath",filePath);
                resultMap.put("code","200");
            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("result", false);
                resultMap.put("msg", "文件上传发生异常");
            }
        }
        return resultMap;
    }
}
