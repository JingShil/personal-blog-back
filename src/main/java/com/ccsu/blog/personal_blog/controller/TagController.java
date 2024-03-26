package com.ccsu.blog.personal_blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ccsu.blog.personal_blog.entity.Result;
import com.ccsu.blog.personal_blog.entity.Tag;
import com.ccsu.blog.personal_blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")

public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public Result<List<Tag>> getTagList(@RequestParam(value = "name", required = false) String name){

        if(name != null){
            LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Tag::getName,name);
            return Result.success(tagService.list(queryWrapper));
        }
        return Result.success(tagService.list());
    }

    @PostMapping("save")
    public Result<String> save(@RequestBody Tag tag){
        if(tag != null){
            tagService.saveOrUpdate(tag);
            return Result.success("保存成功");
        }else{
            return Result.error("错误");
        }
    }

    @DeleteMapping("delete")
    public Result<String> delete(@RequestParam String tagId){
        if(tagId!=null){
            tagService.removeById(tagId);
            return Result.success("删除成功");
        }else{
            return Result.error("错误");
        }
    }
}
