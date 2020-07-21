package me.hjeong.mojji.chat;

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
public class ChatMessageInterceptor implements HandlerInterceptor {

    private final ChatRepository chatRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (modelAndView != null && !InterceptorUtil.isRedirectView(modelAndView)
                && !modelAndView.getModelMap().containsAttribute("newMsgCount")
                && authentication != null && authentication.getPrincipal() instanceof UserAccount)
        {
            Account account = ((UserAccount)authentication.getPrincipal()).getAccount();
            long count = chatRepository.countTotalUnreadMessageByAccount(account);
            modelAndView.addObject("newMsgCount", count);
        }
    }

}
