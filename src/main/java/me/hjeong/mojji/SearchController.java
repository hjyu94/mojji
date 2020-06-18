package me.hjeong.mojji;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String Search(@RequestParam String keywords) {
        System.out.println(keywords);
        return "search";
    }
}
