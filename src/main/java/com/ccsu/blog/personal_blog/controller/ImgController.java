package com.ccsu.blog.personal_blog.controller;

import com.ccsu.blog.personal_blog.entity.*;
import com.ccsu.blog.personal_blog.mapper.ImgMapper;
import com.ccsu.blog.personal_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/img")
public class ImgController {

    @Autowired
    private ImgMapper imgMapper;

    @Value("${file.path}")
    private String filePath;

    @Value("${img.downloadPath}")
    private String downloadPath;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/upload")
    public FileFormData imgUpload(@RequestBody MultipartFile file){

        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = UUID.randomUUID() + "." + fileExtension;

//            String uploadDir = filePath;
//            File uploadPath = new File(uploadDir);
//            if (!uploadPath.exists()) {
//                uploadPath.mkdirs();
//            }
            String finalFilePath = filePath + fileName;
            File dest = new File(finalFilePath);
            file.transferTo(dest);
            Img img = new Img();
            img.setFilePath(fileName);
            imgMapper.insert(img);
            FileFormData fileFormData = new FileFormData();
            FileData fileData = new FileData();
            Map<String,String> map=new HashMap<>();
            map.put(fileName,downloadPath + fileName);
            fileData.setSuccMap(map);
            fileFormData.setData(fileData);
            fileFormData.setMsg("");
            fileFormData.setCode(1);
            return fileFormData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/upload/article")
    public Result<String> imgArticleUpload(@RequestHeader("ArticleId") String articleId,@RequestBody MultipartFile file){
        if(articleId==null){
            return Result.error("错误");
        }

        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

            String fileName = articleId + "." + fileExtension;

            String finalFilePath = filePath + fileName;
            File dest = new File(finalFilePath);
            file.transferTo(dest);
            Article article = articleService.getById(articleId);
            article.setImg(fileName);
            articleService.saveOrUpdate(article);
            return Result.success(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) {
        String uploadDir = filePath;
        String finalFilePath = uploadDir + fileName;
        // 使用绝对路径创建文件对象
        File file = new File(finalFilePath);

        try {
            // 从文件对象创建资源
            Resource resource = new org.springframework.core.io.UrlResource(file.toURI());

            // 返回文件资源
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            // 处理文件下载失败的逻辑
            return ResponseEntity.notFound().build();
        }
    }
}
