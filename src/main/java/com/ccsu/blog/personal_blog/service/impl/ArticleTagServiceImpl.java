package com.ccsu.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccsu.blog.personal_blog.entity.ArticleTag;
import com.ccsu.blog.personal_blog.mapper.ArticleMapper;
import com.ccsu.blog.personal_blog.mapper.ArticleTagMapper;
import com.ccsu.blog.personal_blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
