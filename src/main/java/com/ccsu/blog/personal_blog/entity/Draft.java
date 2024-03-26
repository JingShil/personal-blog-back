package com.ccsu.blog.personal_blog.entity;


import lombok.Data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
public class Draft {
    private String id;
    private String articleId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onupdate(){
        updateTime=LocalDateTime.now();
    }
}
