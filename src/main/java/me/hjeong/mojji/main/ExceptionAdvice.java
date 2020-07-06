package me.hjeong.mojji.main;

import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.account.CurrentAccount;
import me.hjeong.mojji.domain.Account;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(@CurrentAccount Account account, HttpServletRequest req, NoSuchElementException e, Model model) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), req.getRequestURI());
        } else {
            log.info("requested '{}'", req.getRequestURI());
        }
        log.error("bad request", e);
        model.addAttribute("title", "ERROR");
        model.addAttribute("message", e.getMessage());
        return "account/message";
    }
}
