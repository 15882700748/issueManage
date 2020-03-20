package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Colum;
import com.zst.springbootstudy.demo01.entity.Issue;
import com.zst.springbootstudy.demo01.entity.IssueArticle;
import com.zst.springbootstudy.demo01.mapper.IssueArticleMapper;
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
import java.io.FileNotFoundException;
import java.net.URLDecoder;
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
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    IssueServiceImpl issueService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RegHtml regHtml;

   @Autowired
   IssueArticleMapper mapper;

    //add.......................................................................................................
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        issueService.removeById(issue.getIssueId());
    }
    //update.......................................................................................................
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
