package me.hjeong.mojji.module.setting.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ProfileForm {

    @NotBlank
    @Length(min = 2, max = 8)
    private String nickname;

    @AssertTrue
    private boolean nicknameConfirm;

    private String profileImage;

    private boolean notiByWeb;

    private boolean notiByEmail;

}
