package com.example.service;

import com.example.entity.Files;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-08-06
 */
public interface IFileService extends IService<Files>{

    String getFileName(Integer id);

    void updateFileAvatar(String old_name, String new_name);

    void updateUrl(String old_url, String new_url);
}
