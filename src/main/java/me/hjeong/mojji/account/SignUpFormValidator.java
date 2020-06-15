package me.hjeong.mojji.account;


import me.hjeong.mojji.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFormValidator implements Validator {

    @Autowired AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(SignUpForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) o;

        // email
        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email"
                    , new Object[]{signUpForm.getEmail()}
                    , "이미 사용중인 이메일입니다.");
        }

        // nickname
        if(accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname"
                    , new Object[]{signUpForm.getEmail()}
                    , "이미 사용중인 닉네임입니다.");
        }

        // password
        if(!signUpForm.getPassword().equals(signUpForm.getPassword_again())) {
            errors.rejectValue("password", "invalid.password"
                    , new Object[]{signUpForm.getPassword()}
                    , " 비밀번호가 같지 않습니다.");
        }
    }
}
