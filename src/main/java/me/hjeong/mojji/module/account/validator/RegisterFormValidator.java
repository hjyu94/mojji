package me.hjeong.mojji.module.account.validator;


import me.hjeong.mojji.module.account.form.RegisterForm;
import me.hjeong.mojji.module.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterFormValidator implements Validator {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(RegisterForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterForm RegisterForm = (RegisterForm) o;

        // email
        if(accountRepository.existsByEmail(RegisterForm.getEmail())) {
            errors.rejectValue("email", "invalid.email"
                    , new Object[]{RegisterForm.getEmail()}
                    , "이미 사용중인 이메일입니다.");
        }

        // nickname
        if(accountRepository.existsByNickname(RegisterForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname"
                    , new Object[]{RegisterForm.getEmail()}
                    , "이미 사용중인 닉네임입니다.");
        }

        // password
        if(!RegisterForm.getPassword().equals(RegisterForm.getConfirmPassword())) {
            errors.rejectValue("password", "invalid.password"
                    , new Object[]{RegisterForm.getPassword()}
                    , "패스워드가 같지 않습니다.");
        }
    }
}
