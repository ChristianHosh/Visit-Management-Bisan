package com.example.vm.dto.mapper;

import com.example.vm.dto.response.UserResponse;
import com.example.vm.model.User;

import java.util.List;

public class UserMapper {

    public static UserResponse toListResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();

        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setAccessLevel(user.getAccessLevel());

        response.setEnabled(user.getEnabled());
        response.setCreatedTime(user.getCreatedTime());
        response.setLastModifiedTime(user.getLastModifiedTime());

        return response;
    }

    public static List<UserResponse> listToResponseList(List<User> userList) {
        if (userList == null) return null;

        return userList
                .stream()
                .map(UserMapper::toListResponse)
                .toList();
    }



}

