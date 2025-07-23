package com.example.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Builder
@Data
public class UserEditRequest {

    @Size(max = 20, message = "First name can't have more than 20 symbols")
    private String firstName;

    @Size(max = 20, message = "Last name can't have more than 20 symbols")
    private String lastName;

    @URL(message = "Requires correct web link format")
    private String profilePictureUrl;
}
