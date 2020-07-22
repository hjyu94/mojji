package me.hjeong.mojji.module.setting.validator;

import me.hjeong.mojji.module.account.UserAccount;
import me.hjeong.mojji.module.setting.form.ProfileForm;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileForm form = (ProfileForm) o;

        SecurityContext context = SecurityContextHolder.getContext();
        UserAccount userAccount = (UserAccount) context.getAuthentication().getPrincipal();

        // 닉네임을 변경하려는데 중복확인을 하지 않은 경우
        if(!form.getNickname().equals(userAccount.getAccount().getNickname()) && !form.isNicknameConfirm()) {
            errors.rejectValue("nickname", "invalid.nickname", "중복확인이 필요합니다.");
        }
    }
}
