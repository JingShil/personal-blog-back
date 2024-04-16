package com.ccsu.blog.personal_blog.dto;

import com.ccsu.blog.personal_blog.entity.Comment;
import lombok.Data;

@Data
public class CommentDto {
    private String userName;
    private String userAvatar;
    private Comment comment;
}
