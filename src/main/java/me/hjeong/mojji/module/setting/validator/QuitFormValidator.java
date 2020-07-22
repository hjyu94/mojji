package me.hjeong.mojji.module.setting.validator;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.UserAccount;
import me.hjeong.mojji.module.setting.form.QuitForm;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class QuitFormValidator implements Validator {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(QuitForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuitForm form = (QuitForm) o;

        // 기존 패스워드와 일치해야 한다
        SecurityContext context = SecurityContextHolder.getContext();
        UserAccount userAccount = (UserAccount) context.getAuthentication().getPrincipal();
        if(!form.getPassword().isBlank() && !passwordEncoder.matches(form.getPassword(), userAccount.getAccount().getPassword())) {
            errors.rejectValue("password", "password", "패스워드가 올바르지 않습니다");
        }
    }
}
