package me.hjeong.mojji.setting.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ProfileForm {

    @Email
    @NotBlank
    private String email;

    @Length(max = 8)
    @NotBlank
    private String nickname;

    private String profileImage;

}
