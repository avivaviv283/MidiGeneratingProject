package com.example.midigeneratorproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // configure SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()

                .requestMatchers("/registration/**").permitAll()
                .requestMatchers("/index/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/getUsers/**").hasAnyRole("ADMIN")
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/user_home_page/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .requestMatchers("/**").permitAll()
                .and()
                // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
                .formLogin(
                        new Customizer<FormLoginConfigurer<HttpSecurity>>() {
                            @Override
                            public void customize(FormLoginConfigurer<HttpSecurity> form) {
                                form
                                        .loginPage("/login")
                                        .loginProcessingUrl("/login")
                                        .defaultSuccessUrl("/index/")
                                        .permitAll();
                            }
                        }
                ).logout(
                        new Customizer<LogoutConfigurer<HttpSecurity>>() {
                            @Override
                            public void customize(LogoutConfigurer<HttpSecurity> logout) {
                                logout
                                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                        .logoutSuccessUrl("/login")
                                        .permitAll();
                            }
                        }

                );
        return http.build();
    }

    // Configuring web security to ignore requests to the CSS and CSS images directories
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/css_images/**");
    }

}