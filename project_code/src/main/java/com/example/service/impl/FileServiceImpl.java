package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Files;
import com.example.mapper.FileMapper;
import com.example.service.IFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-08-06
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {


    @Resource
    private FileMapper fileMapper;


    // 查询文件名，用于删除文件时添加为路径参数
    @Override
    public String getFileName(Integer id) {
        return fileMapper.selectByFileId(id);
    }

    @Override
    public void updateFileAvatar(String old_name, String new_name) {
        old_name = "%" + old_name + "%";
        fileMapper.updateFileAvatar(old_name, new_name);
    }

    @Override
    public void updateUrl(String old_url, String new_url) {
        old_url = "%" + old_url + "%";
        fileMapper.updateUrl(old_url, new_url);
    }
}
