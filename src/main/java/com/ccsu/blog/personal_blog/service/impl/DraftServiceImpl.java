package com.ccsu.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccsu.blog.personal_blog.entity.Draft;
import com.ccsu.blog.personal_blog.mapper.DraftMapper;
import com.ccsu.blog.personal_blog.service.DraftService;
import org.springframework.stereotype.Service;

@Service
public class DraftServiceImpl extends ServiceImpl<DraftMapper, Draft> implements DraftService {
}
