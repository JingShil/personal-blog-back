package com.ccsu.blog.personal_blog.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FileData {
    private List<String> errFiles;
    private Map<String,String> succMap;
}
