package com.ccsu.blog.personal_blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccsu.blog.personal_blog.entity.*;
import com.ccsu.blog.personal_blog.service.ArticleService;
import com.ccsu.blog.personal_blog.service.ArticleTagService;
import com.ccsu.blog.personal_blog.service.TagService;
import com.ccsu.blog.personal_blog.service.UserService;
import com.ccsu.blog.personal_blog.utils.TokenUtil;
import com.ccsu.blog.personal_blog.utils.canstant.Order;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/article")

public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    private TokenUtil tokenUtil = new TokenUtil();


    @PostMapping("/save")
    public Result<String> save(@RequestHeader("UserId") String userId,@RequestBody ArticleTags articleTags){

        List<Tag> tagList = articleTags.getTagList();
        Article article = articleTags.getArticle();
        article.setUserId(userId);
        if(article==null){
            return Result.error("保存错误");
        }
        if(article.getId() != null) {
            LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleTag::getArticleId, article.getId());
            articleTagService.remove(queryWrapper);
        }else {
            String id = UUID.randomUUID().toString();
            article.setId(id);
        }

        List<ArticleTag> articleTagList = new ArrayList<>();

        if(tagList !=null)
        for (Tag tag : tagList){
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tag.getId());
            articleTagList.add(articleTag);
        }
        articleService.saveOrUpdate(article);
        articleTagService.saveBatch(articleTagList);

        return Result.success("保存成功");
    }

    @PostMapping("/list/private")
    public Result<IPage<Article>> listPrivate(@RequestHeader("UserId") String userId, @RequestBody ArticleListElement articleListElement){
//        String id = tokenUtil.extractSubjectFromToken(token);
        articleListElement.setUserId(userId);
        IPage<Article> articlePageList = articleService.getArticles(articleListElement);
        if(articlePageList == null){
            return Result.error("错误");
        }

        return Result.success(articlePageList);
    }

    @PostMapping("/list")
    public Result<IPage<Article>> list(@RequestBody ArticleListElement articleListElement){

        IPage<Article> articlePageList = articleService.getArticles(articleListElement);
        if(articlePageList == null){
            return Result.error("错误");
        }

        return Result.success(articlePageList);
    }


//    @GetMapping("/list")
//    public Result<IPage<Article>> list(
//            @RequestParam(value = "pageNumber") Integer pageNumber,
//            @RequestParam(value = "pageSize") Integer pageSize,
//            @RequestParam(value = "userId", required = false) String userId,
//            @RequestParam(value = "tagId", required = false) String tagId,
//            @RequestParam(value = "title", required = false) String title,
//            @RequestParam(value = "published", required = false) Integer published,
//            @RequestParam(value = "order", required = false) Integer order){
//        IPage<Article> articlePageList = articleService.getArticleList(pageNumber, pageSize, userId, tagId, title, published, order);
//        if(articlePageList == null){
//            return Result.error("错误");
//        }
////        List<Article> articleList = articlePageList.getRecords();
////        Long total = articlePageList.getTotal();
//
//
//        return Result.success(articlePageList);
//
//    }

    @GetMapping("/get-one")
    public Result<ArticleTags> getOne(@RequestParam(value = "articleId") String articleId){
        if(articleId==null){
            return Result.error("错误");
        }
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleId);
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        List<String> tagIdList=new ArrayList<>();
        for(ArticleTag articleTag : articleTagList){
            tagIdList.add(articleTag.getTagId());
        }
        List<Tag> tagList = new ArrayList<>();
//        System.out.println(tagIdList.isEmpty());
        if(!tagIdList.isEmpty()){
           tagList = tagService.listByIds(tagIdList);
        }
        Article article = articleService.getById(articleId);
        ArticleTags articleTags = new ArticleTags();
        articleTags.setArticle(article);
        articleTags.setTagList(tagList);

        return Result.success(articleTags);
    }


    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(value = "articleId") String articleId){
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleId);
        articleTagService.remove(queryWrapper);
        articleService.removeById(articleId);

        return Result.success("删除成功");
    }

    @GetMapping("/public")
    public Result<String> publicArticle(@RequestParam(value = "articleId") Integer articleId){
        Article article = articleService.getById(articleId);
        if(article.getPublished()==1){
            return Result.error("该文章已发布");
        }else{
            article.setPublished(1);
            articleService.save(article);
            return Result.success("文章已发布");
        }

    }

    @GetMapping("/private")
    public Result<String> privateArticle(@RequestParam(value = "articleId") Integer articleId){
        Article article = articleService.getById(articleId);
        if(article.getPublished()==0){
            return Result.error("该文章没有发布");
        }else{
            article.setPublished(0);
            articleService.save(article);
            return Result.success("文章已取消发布");
        }

    }


}
