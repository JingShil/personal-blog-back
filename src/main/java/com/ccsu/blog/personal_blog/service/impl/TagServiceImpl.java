package com.ccsu.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccsu.blog.personal_blog.entity.Tag;
import com.ccsu.blog.personal_blog.mapper.TagMapper;
import com.ccsu.blog.personal_blog.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
