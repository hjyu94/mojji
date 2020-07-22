package me.hjeong.mojji.module.setting.validator;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.UserAccount;
import me.hjeong.mojji.module.setting.form.PasswordForm;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PasswordFormValidator implements Validator {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordForm form = (PasswordForm) o;

        // 기존 패스워드와 일치해야 한다
        SecurityContext context = SecurityContextHolder.getContext();
        UserAccount userAccount = (UserAccount) context.getAuthentication().getPrincipal();
        if(!form.getCurrentPassword().isBlank() && !passwordEncoder.matches(form.getCurrentPassword(), userAccount.getAccount().getPassword())) {
            errors.rejectValue("currentPassword", "currentPassword", "패스워드가 올바르지 않습니다");
        }

        // 두 비밀번호가 일치하지 않으면 에러
        if(!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "newPassword", "패스워드가 일치하지 않습니다");
            errors.rejectValue("newPasswordConfirm", "newPasswordConfirm", "패스워드가 일치하지 않습니다");
        }
    }
}
