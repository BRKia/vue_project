package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Files;
import com.example.entity.UserFile;
import com.example.mapper.FileMapper;
import com.example.mapper.UserFileMapper;
import com.example.mapper.UserMapper;
import com.example.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author brk
 * @since 2022-08-06
 */

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath; //把拿到的存储路径赋值给fileUploadPath

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IFileService fileService;

    @Resource
    private UserMapper userMapper;

//    @PostMapping
//    public Result save(@RequestBody Files file) {
//        return Result.success(fileService.saveOrUpdate(file));
//    }

    /**
     * 文件上传接口
     * @param file 前端传递的文件
     * @return url
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam String nickname) throws IOException {
        String originalFilename = file.getOriginalFilename(); //文件原始名
        String type = FileUtil.extName(originalFilename); //文件类型
        long size = Math.round((long)(file.getSize() / 1024));//文件大小
        //定义一个文件唯一的标识码
//        String uuid = IdUtil.fastSimpleUUID();
//        String fileUUID = originalFilename;
        File uploadFile = new File(fileUploadPath + originalFilename);
        File parentFile = uploadFile.getParentFile(); //获取父目录
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        String md5;
        String url;
            /*
            以下实现：
            判断文件存在 -> 若存在 -> 拿到md5 -> 根据md5从数据库中查询 -> 如果有相同记录 -> 拿到url
                                                        如果没有相同记录 -> 把获取到的文件存储在磁盘中 -> 定义新的url
                          若文件不存在 -> 存储到磁盘 -> 拿到md5 -> 定义新的url
             */
        //获取文件的md5
        md5 = SecureUtil.md5(originalFilename);
        //查询数据库中是否有相同的md5
        //根据数据库中是否有相同的文件，再考虑是否要拷入磁盘
        Files db_md5 = getFileMd5(md5);
        if (db_md5 != null) { //数据库中有相同文件
            return "文件已存在";
        }else {
            //把获取到的文件存储到对应路径
            file.transferTo(uploadFile);
            url = "http://124.70.63.245:9090/file/" +originalFilename;
        }
        //接下来把新文件上传到数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setSize(size);
        saveFile.setType(type);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setUploadUser(nickname);

        // 查询是否为学生上传的头像
        String role = userMapper.selectRole(nickname);
        if (role.equals("STUDENT_CHINESE") || role.equals("STUDENT_CLINICAL")) {
            saveFile.setEnable(false); // 若为学生上传，则头像不在文件浏览中显示
        }
        try {
            fileMapper.insert(saveFile);
        }catch (Exception e){
            return url;
        }
        return url;
    }

    /**
     * 通过文件md5查询数据库文件
     * @param md5
     * @return Files对象
     */
    private Files getFileMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        return fileMapper.selectOne(queryWrapper);
    }

    /**
     * 分页查询接口
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam String role) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        IPage<Files> page = new Page<>(pageNum, pageSize);
        //查询未删除的记录
        queryWrapper.eq("is_delete", false);
        if(role.equals("STUDENT_CHINESE") || role.equals("STUDENT_CLINICAL")){
            //学生角度，若老师关闭了下载渠道，则不显示该文件
            queryWrapper.eq("enable", true);
        }
        //进行判断，若数据库中字段为空，则不用加%，直接查询
        if (!"".equals(name)) {
            name = "%" + name + "%";
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("id");
        return Result.success(fileMapper.selectPage(page, queryWrapper));
    }

    //备份页面
    @GetMapping("/recycle")
    public Result findRecycle(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        IPage<Files> page = new Page<>(pageNum, pageSize);
        //查询删除的记录
        queryWrapper.eq("is_delete", true);
        queryWrapper.orderByAsc("id");
        return Result.success(fileMapper.selectPage(page, queryWrapper));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // select * from maSys_file where id in ids
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper); //拿到符合条件的files
        if(files.size() > 0) {
            for (Files file : files) {
                file.setIsDelete(true);
                fileMapper.updateById(file);
            }
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files file = fileMapper.selectById(id);
        file.setIsDelete(true);
        fileMapper.updateById(file);
        return Result.success();
    }

    // 永久删除文件
    @PostMapping("/delFor/{id}")
    public Result delFor(@PathVariable Integer id) {
        // 拿到旧文件名
        String old_name = fileService.getFileName(id);
        // 缓存文件夹中删除文件
        String path = fileUploadPath +old_name;
        File file = new File(path);
        file.delete();
        fileService.removeById(id); //数据库中删除文件

        fileService.updateFileAvatar(old_name, null);
        return Result.success();
    }

    // 一键清空
    @PostMapping("/delFor/batch")
    public Result delBatchEver(@RequestBody List<Integer> ids){
        for (Integer id : ids) {
            String old_name = fileService.getFileName(id); // 根据id在数据库中查文件名
            String path = fileUploadPath + old_name;
            File file = new File(path);
            file.delete();
            fileService.updateFileAvatar(old_name, null);
        }
        fileService.removeByIds(ids); //删除数据库中文件
        // 若为头像，则删除user的头像
        return Result.success();
    }

    @PostMapping("/{id}")
    public Result recoverFile(@PathVariable Integer id) {
        Files file = fileMapper.selectById(id);
        file.setIsDelete(false);
        fileMapper.updateById(file);
        return Result.success();
    }

    @PostMapping("/recoverFile/batch")
    public Result recoverBatch(@RequestBody List<Integer> ids){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // select * from maSys_file where id in ids
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper); //拿到符合条件的files
        for (Files file : files) {
            file.setIsDelete(false);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    @PostMapping("/update")
    public Result save(@RequestBody Files file) {
        // 拿到旧文件的名字
        String old_name = fileService.getFileName(file.getId());
        String old_path = fileUploadPath + old_name;
        File old_file = new File(old_path);
        fileMapper.updateById(file); // 更新数据库中的文件

        // 更新sys_user表中的avatar字段
        String new_name = "http://124.70.63.245:9090/file/" + file.getName();
        fileService.updateFileAvatar(old_name, new_name);

        // 更新数据库中文件的url, 由于下载时会调用到文件的url
        fileService.updateUrl(old_name, new_name);

        // 更新文件夹中文件
        String new_path = fileUploadPath + fileService.getFileName(file.getId());
        File new_file = new File(new_path);
        old_file.renameTo(new_file);
        return Result.success();
    }


    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException{
//        String baseurl = "124.70.63.245:9090";
        File uploadFile = new File(fileUploadPath + fileUUID);
        ServletOutputStream outputStream = response.getOutputStream();
        //设置输出流格式
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }
}

