package me.hjeong.mojji.notification;

import me.hjeong.mojji.domain.Account;
import me.hjeong.mojji.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByAccount(Account account);

    long countByAccountAndChecked(Account account, boolean isChecked);

}
