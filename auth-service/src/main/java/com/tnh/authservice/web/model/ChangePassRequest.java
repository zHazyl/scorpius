package com.tnh.authservice.web.model;

import com.tnh.authservice.constants.ApplicationConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePassRequest {

    @NotBlank
    @Size(min = ApplicationConstants.USER_PASSWORD_MIN_LENGTH, max = ApplicationConstants.USER_PASSWORD_MAX_LENGTH)
    private String currentPassword;

    @NotBlank
    @Size(min = ApplicationConstants.USER_PASSWORD_MIN_LENGTH, max = ApplicationConstants.USER_PASSWORD_MAX_LENGTH)
    private String newPassword;

}
