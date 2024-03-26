package com.ccsu.blog.personal_blog.entity;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Date;
import java.time.LocalDateTime;
@Data
public class UserInfo {

    private String name;
    private Date birthday;
    private String avatar;
    private Integer sex;
    private String location;
    private String college;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
