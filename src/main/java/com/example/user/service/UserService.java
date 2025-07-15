package com.example.user.service;

import com.example.user.model.User;
import com.example.user.model.UserRole;
import com.example.web.dto.RegisterRequest;
import com.example.web.dto.UserEditRequest;

import java.util.List;

public interface UserService {

    void register(RegisterRequest registerRequest);

    void editUserDetails(Long userId, UserEditRequest userEditRequest);

    User getById(Long userId);

    List<User> getAllUsers();

    void switchStatus(Long userId);

    void switchRole(Long userId, UserRole newRole);
}