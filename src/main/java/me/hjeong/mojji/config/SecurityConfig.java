package me.hjeong.mojji.config;

import lombok.RequiredArgsConstructor;
import me.hjeong.mojji.account.AccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/confirmed-account-by-email", "/account/password", "/test").permitAll()
                .antMatchers(HttpMethod.GET, "/account/profile", "/post/*").permitAll()
                .antMatchers("/new-account", "/account/password").anonymous()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login").permitAll()
                .and()

                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()

                .rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository())
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
                .mvcMatchers("/node_modules/**", "/upload/**")
                // 프론트엔드 라이브러리 파일 요청에도 필터를 적용하지 말자
                // upload img
        ;
    }
}
