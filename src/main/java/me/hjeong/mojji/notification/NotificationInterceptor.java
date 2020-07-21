package me.hjeong.mojji.notification;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.UserAccount;
import me.hjeong.mojji.config.InterceptorUtil;
import me.hjeong.mojji.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {

    private final NotificationRepository notificationRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (modelAndView != null && !InterceptorUtil.isRedirectView(modelAndView)
                && !modelAndView.getModelMap().containsAttribute("newNotiCount")
                && authentication != null && authentication.getPrincipal() instanceof UserAccount)
        {
            Account account = ((UserAccount)authentication.getPrincipal()).getAccount();
            long count = notificationRepository.countByAccountAndChecked(account, false);
            modelAndView.addObject("newNotiCount", count);
        }
    }

}
