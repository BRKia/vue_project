package com.example.service.impl;

import com.example.common.Result;
import com.example.entity.Files;
import com.example.entity.UserFile;
import com.example.entity.dto.FileDTO;
import com.example.entity.dto.UserDTO;
import com.example.mapper.FileMapper;
import com.example.mapper.UserFileMapper;
import com.example.service.IFileService;
import com.example.service.IUserFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-08-11
 */
@Service
        public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements IUserFileService {

        @Resource
        private UserFileMapper userFileMapper;

        @Resource
        private IFileService fileService;


        @Transactional
        @Override
        public void setUserFile(Integer userId, Integer fileId) {
                userFileMapper.delete(userId, fileId);
                Files file = fileService.getById(fileId);
                if (file != null) {
                        UserFile userFile = new UserFile();
                        userFile.setFileId(file.getId());
                        userFile.setUserId(userId);
                        userFileMapper.insert(userFile);
                }
        }


        @Override
        public List<FileDTO> checkDownload(Integer fileId) {
             return userFileMapper.selectByFileIdResult(fileId);
        }


}
