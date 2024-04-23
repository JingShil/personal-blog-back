package com.ccsu.blog.personal_blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ccsu.blog.personal_blog.entity.Result;
import com.ccsu.blog.personal_blog.entity.User;
import com.ccsu.blog.personal_blog.entity.UserInfo;
import com.ccsu.blog.personal_blog.service.UserService;
import com.ccsu.blog.personal_blog.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @Value("${user.adminId}")
    private String adminId;


    @Value("${file.path}")
    private String filePath;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${spring.redis.token.expiration-time}")
    private Long expirationTime;

    @Value("${img.downloadPath}")
    private String imgDownloadPath;


    private TokenUtil tokenUtil = new TokenUtil();

//    private static String filePath = "E:/ImgData/Blog/";

    @GetMapping("/code")
    public Result<String> code(@RequestBody User user){
        return Result.success("1111");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());


        User dataUser = userService.getOne(queryWrapper);
        if(dataUser == null){
            return Result.error("账号未注册");
        }
        if(!dataUser.getPassword().equals(user.getPassword()) ){
            return Result.error("密码错误");
        }
//        String token = tokenUtil.generateToken(dataUser.getId());
        String token = setTokenAndToRedis(dataUser.getId());
//        UserInfo userInfo = new UserInfo();
//        userInfo.setName(dataUser.getName());
//        userInfo.setLocation(dataUser.getLocation());
//        userInfo.setCollege(dataUser.getCollege());
//        userInfo.setBirthday(dataUser.getBirthday());
//        userInfo.setAvatar(dataUser.getAvatar());
//        userInfo.setSex(dataUser.getSex());
//        userInfo.setCreateTime(dataUser.getCreateTime());
//        userInfo.setUpdateTime(dataUser.getUpdateTime());

        return Result.successByToken(dataUser,token);
    }

    @PostMapping("/token")
    public Result<String> token(HttpServletRequest request){
        String token = request.getHeader("Token");

        boolean b = tokenUtil.validateToken(token);
        return Result.success("你好");
    }

    public String setTokenAndToRedis(String id){
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        redisTemplate.opsForValue().set("blog:token:" + id,token,18000L, TimeUnit.SECONDS);
        return token;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User dataUser = userService.getOne(queryWrapper);
        if(dataUser!=null){
            return Result.error("该账号已注册");
        }
        userService.save(user);
        return Result.success("注册成功");
    }



    @PostMapping("/save")
    public Result<String> save(@RequestHeader("UserId") String userId,@RequestBody User user){
//        String id=tokenUtil.extractSubjectFromToken(userId);
        user.setId(userId);
        userService.saveOrUpdate(user);
        return Result.success("保存成功");
    }

    @PutMapping("/upload/avatar")
    public Result<String> saveAvatar(@RequestHeader("UserId") String userId,@RequestParam("file") MultipartFile file){
//        String id = tokenUtil.extractSubjectFromToken(token);
        User user = userService.getById(userId);
        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = userId + "." + fileExtension;
            // 指定要保存文件的路径

            // 确保上传目录存在，如果不存在则创建
            File uploadPath = new File(filePath);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // 将文件保存到指定路径
            String filePath1 = filePath + fileName;
            File dest = new File(filePath1);
            file.transferTo(dest);

            // 更新用户的头像信息为文件路径或文件名
            user.setAvatar(fileName);
            userService.saveOrUpdate(user);

            return Result.success("保存成功");
        } catch (IOException e) {
            return Result.error("保存失败");
        }
    }

    @GetMapping("/download/avatar")
    public ResponseEntity<Resource> downloadAvatar(@RequestParam String fileName) {
        // 使用绝对路径创建文件对象
        String filePath1 = filePath + fileName;
        File file = new File(filePath1);

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

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam String userId){
        User user = userService.getById(userId);
//        UserInfo userInfo = userToUserInfo(user);
        return Result.success(user);
    }

    @GetMapping("/article/info")
    public Result<UserInfo> getArticleUserInfo(@RequestParam String articleId){
        User user = userService.getById(articleId);
        UserInfo userInfo = userToUserInfo(user);
        return Result.success(userInfo);
    }

    @GetMapping("/admin/info")
    public Result<UserInfo> getAdminInfo(){
        User user = userService.getById(adminId);
        UserInfo userInfo = userToUserInfo(user);
        return Result.success(userInfo);
    }

    private UserInfo userToUserInfo(User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(user.getAvatar());
        userInfo.setBirthday(user.getBirthday());
        userInfo.setCollege(user.getCollege());
        userInfo.setName(user.getName());
        userInfo.setSex(user.getSex());
        userInfo.setUpdateTime(user.getUpdateTime());
        userInfo.setCreateTime(user.getCreateTime());
        userInfo.setLocation(user.getLocation());
        return userInfo;
    }

}
