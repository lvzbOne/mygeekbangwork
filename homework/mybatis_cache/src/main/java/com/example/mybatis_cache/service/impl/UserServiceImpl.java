//package com.example.mybatis_cache.service.impl;
//
//import com.example.mybatis_cache.bean.User;
//import com.example.mybatis_cache.mapper.UserMapper;
//import com.example.mybatis_cache.service.UserService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserMapper userMapper; //DAO  // Repository
//
//    // 开启spring cache
//    @Cacheable(key = "#id", value = "userCache")
//    @Override
//    public User find(int id) {
//        System.out.println(" ==> find " + id);
//        return userMapper.find(id);
//    }
//
//    // 开启spring cache
//    @Cacheable //(key="methodName",value="userCache")
//    @Override
//    public List<User> list() {
//        return userMapper.list();
//    }
//
//}
