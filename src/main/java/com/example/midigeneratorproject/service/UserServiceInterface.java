package com.example.midigeneratorproject.service;

import com.example.midigeneratorproject.dto.UserDto;
import com.example.midigeneratorproject.entity.User;

import java.util.List;

public interface UserServiceInterface {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    List<String> getRoleList();
}

