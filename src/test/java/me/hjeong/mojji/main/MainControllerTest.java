package me.hjeong.mojji.main;

import me.hjeong.mojji.account.AccountRepository;
import me.hjeong.mojji.account.WithAccount;
import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.factory.NotificationFactory;
import me.hjeong.mojji.notification.NotificationRepository;
import me.hjeong.mojji.notification.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired NotificationFactory notificationFactory;

    @Test
    @WithAccount("hjeong")
    @DisplayName("로그인 상태이고 리다이렉트되는 뷰가 아닐 때 알림 갯수 표시")
    void testNotificationCount() throws Exception {
        Account hjeong = accountRepository.findByNickname("hjeong");
        notificationFactory.createNotification(hjeong, NotificationType.POST_CREATED);

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(model().attribute("newNotiCount", (long) 1));
    }

}