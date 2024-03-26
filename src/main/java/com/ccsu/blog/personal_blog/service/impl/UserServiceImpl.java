package com.ccsu.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccsu.blog.personal_blog.entity.User;
import com.ccsu.blog.personal_blog.mapper.UserMapper;
import com.ccsu.blog.personal_blog.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
