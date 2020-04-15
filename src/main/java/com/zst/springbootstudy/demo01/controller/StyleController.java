package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zst.springbootstudy.demo01.entity.Article;
import com.zst.springbootstudy.demo01.entity.Colum;
import com.zst.springbootstudy.demo01.entity.Issue;
import com.zst.springbootstudy.demo01.service.impl.ArticleServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.ColumServiceImpl;
import com.zst.springbootstudy.demo01.service.impl.IssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/style")
public class StyleController {


    @Autowired
    IssueServiceImpl issueService;
    @Autowired
    ColumServiceImpl columService;
    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/queryHomePageInfo")
    public Map<String, Object> queryHomePageInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","100");
        //获取会议
        QueryWrapper queryIssueWrapper = new QueryWrapper();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        if(StringUtils.isEmpty(orgId)){

        }else{
            queryIssueWrapper.eq("orgId",orgId);
            Issue  issue = (Issue) issueService.list(queryIssueWrapper).get(0);
            map.put("issue",issue);
            //获取栏目
            QueryWrapper queryColumnWrapper = new QueryWrapper();
            queryColumnWrapper.eq("orgId",orgId);
            queryColumnWrapper.eq("issueId",issue.getIssueId());
            List<Colum> columList = columService.list(queryColumnWrapper);
            map.put("columns",columList);
            //获取栏目文章
            QueryWrapper queryArticleWrapper = new QueryWrapper();
            queryArticleWrapper.eq("orgId",orgId);
            queryArticleWrapper.eq("issueId",issue.getIssueId());
            List<Article> articleList = articleService.list(queryArticleWrapper);
            map.put("articles",articleList);
        }
      return map;
    }
}
