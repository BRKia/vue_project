package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.UserDTO;
import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-06-27
 */
@Service
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    Page<User> findPage01(Page<User> objectPage, String username, String nickname, String email, String address);

    Page<User> findPage02(Page<User> objectPage, String username, String nickname, String email, String address);

}
