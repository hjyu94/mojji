package me.hjeong.mojji.module.main;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.infra.config.AppProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class BaseController {

    private final AppProperties appProperties;

    @ModelAttribute
    public void hostAddress(Model model) {
        model.addAttribute("host", appProperties.getHost());
    }

}
