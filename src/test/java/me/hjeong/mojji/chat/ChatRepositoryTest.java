package me.hjeong.mojji.chat;

import me.hjeong.mojji.infra.MyDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MyDataJpaTest
class ChatRepositoryTest {

    @Autowired ChatRepository repository;

    @Test
    public void crud() {
        //
    }

}