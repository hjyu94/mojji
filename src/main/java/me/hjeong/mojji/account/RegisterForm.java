package me.hjeong.mojji.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterForm {

    @NotBlank
    String nickname;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Length(min = 8, max = 20)
    String password;

    @NotBlank
    String confirmPassword;

}
