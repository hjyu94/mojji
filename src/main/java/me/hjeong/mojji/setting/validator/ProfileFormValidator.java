package me.hjeong.mojji.setting.validator;

import me.hjeong.mojji.setting.form.ProfileForm;
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
        if(form.getNickname().length() < 1 || 8 < form.getNickname().length()) {
            errors.rejectValue("nickname", "invalid.nickname", "length problem");
        }
    }
}
