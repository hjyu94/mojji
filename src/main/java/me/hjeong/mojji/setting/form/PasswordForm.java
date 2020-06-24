package me.hjeong.mojji.setting.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordForm {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;

}
