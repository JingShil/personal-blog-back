package com.ccsu.blog.personal_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccsu.blog.personal_blog.entity.Article;
import com.ccsu.blog.personal_blog.entity.ArticleListElement;

import java.util.List;

public interface ArticleService extends IService<Article> {
    public IPage<Article> getArticleList(Integer pageNumber, Integer pageSize, String userId, String tagId, String title, Integer published, Integer order);
    public IPage<Article> getArticles(ArticleListElement articleListElement);
}
