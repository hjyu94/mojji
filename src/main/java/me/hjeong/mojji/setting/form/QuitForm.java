package me.hjeong.mojji.setting.form;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

@Data
public class QuitForm {

    @AssertTrue
    private boolean notiConfirm;

    @NotBlank
    private String password;

}
