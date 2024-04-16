package com.ccsu.blog.personal_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccsu.blog.personal_blog.dto.CommentDto;
import com.ccsu.blog.personal_blog.dto.CommentReply;
import com.ccsu.blog.personal_blog.entity.Comment;
import com.ccsu.blog.personal_blog.entity.Result;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentService extends IService<Comment> {
    public IPage<CommentReply> getComments(String articleId,
                                           Integer pageNumber,
                                           Integer pageSize);
}
