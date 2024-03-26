package com.ccsu.blog.personal_blog.entity;

import lombok.Data;

import java.util.List;

@Data
public class ArticleTags {
    private Article article;
    private List<Tag> tagList;
}
