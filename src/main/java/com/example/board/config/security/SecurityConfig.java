package com.example.board.config.security;

import com.example.board.config.security.login.AuthFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final AuthFailureHandler authFailureHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");

        filter.setForceEncoding(true);

        httpSecurity.addFilterBefore(filter, CsrfFilter.class);

        httpSecurity.authorizeRequests()
                .antMatchers("/common/css/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/board/**").permitAll()
                .antMatchers("/post/**").permitAll()
                .antMatchers("/board/post").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", false)
                .failureHandler(authFailureHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.authenticationProvider(userAuthenticationProvider);
    }
}
