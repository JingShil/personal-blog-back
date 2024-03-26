package com.ccsu.blog.personal_blog.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageArticleTag {
    private List<ArticleTags> articleTagsList;
    private Long total;
}
