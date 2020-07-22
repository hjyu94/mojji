package me.hjeong.mojji.module.account;

import me.hjeong.mojji.module.account.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired public MockMvc mockMvc;
    @Autowired public AccountRepository accountRepository;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @Description("회원 가입 - 성공")
    @ParameterizedTest
    @CsvSource({
            "hjeong, hjeong@email.com, 12345678, 12345678",
            "hjeong2, hjeong2@email.com, 12345678, 12345678"
    })
    public void register_success(String nickname, String email, String password, String confirmPassword) throws Exception {
        assertNull(accountRepository.findByEmail(email));

        mockMvc.perform(post("/new-account")
                .with(csrf())
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password)
                .param("confirmPassword", confirmPassword)
        )
                .andExpect(status().is3xxRedirection())
        ;

        assertNotNull(accountRepository.findByEmail(email));
    }


    @Description("회원 가입 - 실패")
    @ParameterizedTest
    @CsvSource({
            ", hj@email.com, 12345678, 12345678",
            "hjeong, this is not email, 12345678, 12345678",
            "hjeong, hj@email.com, 123, 1",
            "hjeong, hj@email.com, 1234, 1234",
    })
    public void register_fail(String nickname, String email, String password, String confirmPassword) throws Exception {
        mockMvc.perform(post("/new-account")
                .with(csrf())
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password)
                .param("confirmPassword", confirmPassword)
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("registerForm"))
        ;
    }
}