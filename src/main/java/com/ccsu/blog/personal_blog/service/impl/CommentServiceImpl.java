package com.ccsu.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccsu.blog.personal_blog.dto.CommentDto;
import com.ccsu.blog.personal_blog.dto.CommentReply;
import com.ccsu.blog.personal_blog.dto.ReplyDto;
import com.ccsu.blog.personal_blog.entity.Comment;
import com.ccsu.blog.personal_blog.entity.User;
import com.ccsu.blog.personal_blog.mapper.CommentMapper;
import com.ccsu.blog.personal_blog.service.CommentService;
import com.ccsu.blog.personal_blog.service.ReplyService;
import com.ccsu.blog.personal_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private ReplyService replyService;

    @Value("${img.downloadPath}")
    private String downloadPath;

    @Override
    public IPage<CommentReply> getComments(String articleId, Integer pageNumber, Integer pageSize) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId,articleId);
        commentLambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        Page<Comment> commentPage = new Page<>(pageNumber,pageSize);
        IPage<Comment> commentIPage = this.page(commentPage,commentLambdaQueryWrapper);

        List<Comment> commentList = commentIPage.getRecords();

        // 获取所有评论的用户信息
        List<String> userIds = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());
        if(userIds==null||userIds.size()==0){
            return null;
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId,userIds);
        List<User> userList = userService.list(userLambdaQueryWrapper);
        List<CommentReply> commentReplyList = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();
        for(User user : userList){
            userMap.put(user.getId(),user);
        }
// 将用户信息和回复集成到CommentDto中

        for (Comment comment : commentList) {
            User user = (User)userMap.get(comment.getUserId());
            CommentDto commentDto = new CommentDto();
            commentDto.setComment(comment);
            commentDto.setUserName(user.getName());
            commentDto.setUserAvatar(user.getAvatar());
//            commentDtoList.add(commentDto);
            //获取回复
            List<ReplyDto> replyList = replyService.getReplyList(comment.getId());
            CommentReply commentReply = new CommentReply();
            commentReply.setCommentDto(commentDto);
            commentReply.setReplyDtoList(replyList);
            commentReplyList.add(commentReply);
        }
        IPage<CommentReply> commentReplyIPage= new Page<>();
        commentReplyIPage.setPages(commentIPage.getPages());
        commentReplyIPage.setCurrent(commentIPage.getCurrent());
        commentReplyIPage.setRecords(commentReplyList);
        commentReplyIPage.setSize(commentIPage.getSize());
        commentReplyIPage.setTotal(commentIPage.getTotal());



        return commentReplyIPage;
    }
}
