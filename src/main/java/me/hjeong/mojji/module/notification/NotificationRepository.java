package me.hjeong.mojji.module.notification;

import me.hjeong.mojji.module.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByAccount(Account account);

    long countByAccountAndChecked(Account account, boolean isChecked);

    List<Notification> findTop3ByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean isChecked);

    List<Notification> findByAccountAndChecked(Account account, boolean isChecked);

    Page<Notification> findByAccountAndChecked(Account account, boolean isChecked, Pageable pageable);

}
