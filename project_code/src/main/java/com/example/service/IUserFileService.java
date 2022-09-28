package com.example.service;

import com.example.common.Result;
import com.example.entity.UserFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.FileDTO;
import com.example.entity.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-08-11
 */
public interface IUserFileService extends IService<UserFile> {
        void setUserFile(Integer userId, Integer fileId);

        List<FileDTO> checkDownload(Integer fileId);
        }
