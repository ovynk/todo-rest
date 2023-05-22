package com.oleksiy.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/users/create").permitAll()
                .anyRequest()
                .authenticated()
                    .and()
                .formLogin()
                .loginPage("/form-login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/form-login?error=true")
                .permitAll()
                    .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/form-login?logout=true")
                .permitAll()
                .deleteCookies("JSESSIONID");

        return http.build();
    }
}
