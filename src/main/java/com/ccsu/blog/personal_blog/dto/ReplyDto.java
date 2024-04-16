package com.ccsu.blog.personal_blog.dto;

import com.ccsu.blog.personal_blog.entity.Reply;
import lombok.Data;

@Data
public class ReplyDto {
    private String userName;
    private String userAvatar;
    private Reply reply;
}
