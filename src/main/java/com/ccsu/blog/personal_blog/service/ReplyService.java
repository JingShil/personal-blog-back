package com.ccsu.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccsu.blog.personal_blog.dto.ReplyDto;
import com.ccsu.blog.personal_blog.entity.Reply;
import com.ccsu.blog.personal_blog.entity.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReplyService extends IService<Reply> {
    public List<ReplyDto> getReplyList(String commentId);
}
