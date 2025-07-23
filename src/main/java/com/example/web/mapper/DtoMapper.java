package com.example.web.mapper;

import com.example.user.model.User;
import com.example.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public UserEditRequest mapUserToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}