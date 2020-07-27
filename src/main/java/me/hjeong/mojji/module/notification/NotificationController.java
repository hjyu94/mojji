package me.hjeong.mojji.module.notification;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.module.account.CurrentAccount;
import me.hjeong.mojji.module.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repository;
    private final NotificationService service;

    @GetMapping("/notifications")
    public String getNotifications(@CurrentAccount Account account, Model model) {
        // 새 알림 갯수와, 미리보기 용도 3개 표시
        long newNotiCount = repository.countByAccountAndChecked(account, false);
        model.addAttribute("newNotiCount", newNotiCount);

        // 읽은 알림 미리보기 용도 3개 표시
        long oldNotiCount = repository.countByAccountAndChecked(account, true);
        model.addAttribute("oldNotiCount", oldNotiCount);

        if(newNotiCount > 0) {
            List<Notification> newNotifications = repository.findTop3ByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
            model.addAttribute("newNotifications", newNotifications);
            service.markAsRead(newNotifications, true);
            newNotiCount = newNotiCount - newNotifications.size();
            model.addAttribute("newNotiCount", newNotiCount);
        }

        if(oldNotiCount > 0) {
            List<Notification> oldNotifications = repository.findTop3ByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
            model.addAttribute("oldNotifications", oldNotifications);
        }

        return "notification/list";
    }

    // 5개씩 끊어서 페이징 적용
    @GetMapping("/notification/old")
    public String getOldNotifications(@CurrentAccount Account account, Model model
            , @PageableDefault(size = 5, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Notification> oldNotiPage = repository.findByAccountAndChecked(account, true, pageable);
        model.addAttribute("oldNotiPage", oldNotiPage);

        long oldNotiCount = repository.countByAccountAndChecked(account, true);
        model.addAttribute("oldNotiCount", oldNotiCount);

        return "notification/old";
    }

    // 전체 새 알림 리스트 표시하기
    @GetMapping("/notification/new")
    public String getNewNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> newNotifications = repository.findByAccountAndChecked(account, false);
        model.addAttribute("newNotifications", newNotifications);
        model.addAttribute("newNotiCount", newNotifications.size()); // for interceptor
        service.markAsRead(newNotifications, true);
        return "notification/new";
    }

    @DeleteMapping("/notifications")
    public ResponseEntity deleteNotifications(@CurrentAccount Account account, @RequestParam("notificationId[]") List<String> notificationIdList) {
        int result = 0;
        if(null != account) {
            service.deleteNotifications(notificationIdList);
            result = 1;
        }
        return ResponseEntity.ok(result);
    }
}