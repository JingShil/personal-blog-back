package com.ccsu.blog.personal_blog.entity;

import lombok.Data;

import java.util.List;

@Data
public class ArticleListElement {
    private Integer pageNumber;
    private Integer pageSize;
    private String userId;
    private String tagId;
    private String title;
    private Integer published;
    private Integer order;
}
