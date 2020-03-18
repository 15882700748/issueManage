package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Article;
import com.zst.springbootstudy.demo01.entity.Spon;
import com.zst.springbootstudy.demo01.service.impl.ArticleServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //add.......................................................................................................

    /**
     * 增加文章、标题、内容、发行日期、举办日期
     * 增加时判断是否有重复
     * @param article
     */
    @RequestMapping("/addArticle")
    public Map<String,Object> addArticle(@RequestBody Article article){
        Map<String,Object> map = new HashMap<>();
        map.put("code","100");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Article> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Article::getTitle,article.getTitle()).or().eq(Article::getContent,article.getContent())
                .eq(Article::getOrgId,orgId);
        int size = articleService.list(queryWrapper).size();
        if(size > 0 ){
            map.put("msg","已存在，请重新输入");
        }else{
            article.setRealseTime(LocalDateTime.now());
            article.setOrgId(Integer.valueOf(orgId));
            articleService.save(article);
            map.put("code","200");
            map.put("msg","添加成功");
        }
        return map;
    }

    //delete.......................................................................................................
    @RequestMapping("/deleteArticle")
    public void deleteArticle(@RequestBody Article article){
        articleService.removeById(article.getArticleId());
    }

    //update.......................................................................................................

    /**
     * 更新文章：文章标题、内容、举办时间
     * 检查重复：修改标题内容时检查是否村子
     * @param article
     * @return
     */
    @RequestMapping("/updateArticle")
    public Map<String,Object> updateArticle(@RequestBody Article article){
        Map<String,Object> map = new HashMap<>();
        map.put("code","200");
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper<Article> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Article::getTitle,article.getTitle()).or().eq(Article::getContent,article.getContent())
                .eq(Article::getOrgId,orgId);
        int size = articleService.list(queryWrapper).size();
        if(size > 1 ){
            map.put("msg","已存在，请重新输入");
        }else{
            article.setRealseTime(LocalDateTime.now());
            articleService.updateById(article);
            map.put("code","200");
            map.put("msg","修改成功");
        }
        return map;
    }

    //query.......................................................................................................
    @RequestMapping("/page")
    public IPage<Article> page(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize){
        IPage<Article> page = new Page<>(pageNo, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        queryWrapper.eq("orgId",orgId);
//        if(StringUtils.isNotEmpty(sponName)){
//            queryWrapper.eq("sponName",sponName);
//        }
//        if(StringUtils.isNotEmpty(site)){
//            queryWrapper.eq("site",site);
//        }
        IPage<Article> articleIPage = articleService.page(page,queryWrapper);
        return articleIPage;
    }

    @RequestMapping("/getOneArticle")
    public Article getOneArticle(@RequestBody Article article){
        return articleService.getById(article.getArticleId());
    }
}
