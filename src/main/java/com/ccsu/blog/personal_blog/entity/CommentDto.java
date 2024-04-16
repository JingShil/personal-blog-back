package com.ccsu.blog.personal_blog.entity;

import lombok.Data;

import java.util.List;

@Data
public class CommentDto {
    private Comment comment;
    private List<Reply> replyList;
}
