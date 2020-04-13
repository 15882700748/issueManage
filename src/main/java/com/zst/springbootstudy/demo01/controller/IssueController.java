package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Article;
import com.zst.springbootstudy.demo01.entity.Colum;
import com.zst.springbootstudy.demo01.entity.Issue;
import com.zst.springbootstudy.demo01.entity.IssueArticle;
import com.zst.springbootstudy.demo01.mapper.IssueArticleMapper;
import com.zst.springbootstudy.demo01.service.impl.ArticleServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.IssueServiceImpl;
import com.zst.springbootstudy.demo01.tool.RegHtml;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    IssueServiceImpl issueService;

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RegHtml regHtml;

   @Autowired
   IssueArticleMapper mapper;

    //add.......................................................................................................

    /**
     * 添加会议
     * @param issue
     * @return
     */
    @RequestMapping("/addIssue")
    public Map<String,Object> addIssue(@RequestBody Issue issue){
        Map map = new HashMap();
        map.put("code","100");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        //重复排除
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orgId",orgId);
        queryWrapper.eq("title",issue.getTitle());
        int size = issueService.list(queryWrapper).size();
        if(size > 0){
            map.put("msg","标题已存在");
        }else{
            map.put("code","200");
            map.put("msg","添加成功");
            issue.setOrgId(Integer.valueOf(orgId));
            String fileName = issue.getLogo();
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
                    tempPath = path + "issue/";
                    tempFilePath =tempPath +fileName;
                    isUpload = false;
                }else {
                    tempPath = path + "temp/";
                    tempFilePath =tempPath +prex+fileName;
                    isUpload = true;
                }
                targetPath = path + "issue/";
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
                    issue.setLogo(prex+fileName);
                }else{
                    System.out.println("文件不存在or不是文件");
                }
                //temp文件删除
                if(isUpload){
                    tempPath =  URLDecoder.decode(tempPath,"utf-8");
                    File[] files = new File(tempPath).listFiles();
                    for(File file : files){
                        file.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringRedisTemplate.opsForValue().set("sponTempPrex","");
            issueService.save(issue);
        }
        return map;
    }
    //delete.......................................................................................................

    /**
     * 删除会议并删除图片
     * @param issue
     */
    @RequestMapping("/deleteIssue")
    public void deleteIssue(@RequestBody Issue issue){
        issue = issueService.getById(issue);
        List<String> srcList = regHtml.getSrc(issue.getContent());
        try {
            String path = ResourceUtils.getURL("classpath:static/").getPath()+"issue/";
            for(String src: srcList){
                String temp = src.substring(28);
                System.out.println(temp);
                File file = new  File(URLDecoder.decode(path + temp));
                if(file.exists()){
                    file.delete();
                }
            }
            File  file = new File(URLDecoder.decode(ResourceUtils.getURL("classpath:static/").getPath()+"issue/"+issue.getLogo()));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //删除会议文章
        String id = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Article> queryWrapper = new QueryWrapper();
        queryWrapper.eq("orgId",id);
        queryWrapper.eq("issueId",issue.getIssueId());
        articleService.remove(queryWrapper);
        issueService.removeById(issue.getIssueId());
    }
    //update.......................................................................................................

    /**
     * 会议更新
     * @param issue
     * @return
     */
    @RequestMapping("/updateIssue")
    public Map<String,Object> updateIssue(@RequestBody Issue issue){
        Map map = new HashMap();
        map.put("code","100");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        //重复排除
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orgId",orgId);
        queryWrapper.eq("title",issue.getTitle());
        int size = issueService.list(queryWrapper).size();
        if(size >1){
            map.put("msg","标题已存在");
        }else{
            map.put("code","200");
            map.put("msg","添加成功");
            issue.setOrgId(Integer.valueOf(orgId));
            issueService.updateById(issue);
        }
        return map;
    }

    //query.......................................................................................................

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryPage")
    public IPage<Issue> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize){
        IPage<Issue> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
        IPage<Issue> issueIPage = issueService.page(page,queryWrapper);
        return issueIPage;
    }

    @RequestMapping("/getAll")
    public List<IssueArticle> getAll(){
        return mapper.selectIssueArticles();
    }
}
