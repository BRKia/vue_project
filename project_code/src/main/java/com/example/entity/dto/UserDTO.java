package com.example.entity.dto;


import com.example.entity.Menu;
import lombok.Data;

import java.util.List;

/*
 * 接收前段登录请求的参数
 */
@Data
public class UserDTO {
    private String Username;
    private String password;
    private String nickname;
    private String token;
    private String avatar;
    private List<Menu> menus;
    private String role;
    private Integer id;
}
