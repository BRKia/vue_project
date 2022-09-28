package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
//@Transactional(rollbackFor = Exception.class)
public class UserService extends ServiceImpl<UserMapper, User> {
//    @Resource
    public boolean saveUser(User user){
//        boolean save = save(user);//mybatis-plus提供的save方法
//        if(user.getId() == null){
//            save(user);
//        } else{
//            return updateById(user);
//        }
//        return false;
        return saveOrUpdate(user);
    }

//    @Resource
//    private UserMapper userMapper;
//
//    public int save(User user){
//
//        if(user.getId() == null){
//            return userMapper.insert(user); //表中无此id，故需要进行添加
//        }else{
//            return userMapper.update(user); //更新表数据
//        }
//
//    }

}
