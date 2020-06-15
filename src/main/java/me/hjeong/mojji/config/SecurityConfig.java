package me.hjeong.mojji.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up").permitAll()
                .anyRequest().permitAll()
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                // 기본적인 static resources 경로에 대해서는 스프링 시큐리티 필터를 사용하지 않겠다.
                /*
                    CSS(new String[]{"/css/**"}),
                    JAVA_SCRIPT(new String[]{"/js/**"}),
                    IMAGES(new String[]{"/images/**"}),
                    WEB_JARS(new String[]{"/webjars/**"}),
                    FAVICON(new String[]{"/**\/favicon.ico"});
                */
                .mvcMatchers("/node_modules/**", "/assets/**")
                // 프론트엔드 라이브러리 파일 요청에도 필터를 적용하지 말자
        ;
    }
}
