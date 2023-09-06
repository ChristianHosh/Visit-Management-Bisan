package com.example.vm.dto.mapper;

import com.example.vm.dto.request.PasswordResetRequest;
import com.example.vm.dto.response.PasswordResetResponse;
import com.example.vm.model.PasswordReset;
import com.example.vm.model.User;
import com.example.vm.model.enums.PasswordStatus;

import java.util.List;

public class PasswordResetMapper {

    public static PasswordReset toEntity(PasswordResetRequest passwordResetRequest, User user) {
        return PasswordReset
                .builder()
                .password(passwordResetRequest.getPassword().trim())
                .user(user)
                .status(PasswordStatus.PENDING)
                .build();
    }

    public static PasswordResetResponse toResponse(PasswordReset passwordReset) {
        PasswordResetResponse response = new PasswordResetResponse();
        response.setId(passwordReset.getId());
        response.setStatus(passwordReset.getStatus().name());
        response.setUser(UserMapper.toListResponse(passwordReset.getUser()));

        response.setEnabled(passwordReset.getEnabled());
        response.setCreatedTime(passwordReset.getCreatedTime());
        response.setLastModifiedTime(passwordReset.getLastModifiedTime());

        return response;
    }

    public static List<PasswordResetResponse> listToResponseList(List<PasswordReset> passwordResetList) {
        return passwordResetList.stream().map(PasswordResetMapper::toResponse).toList();
    }
}
