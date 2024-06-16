package ru.tgbot.tgbot.bot.service;

import ru.tgbot.tgbot.model.UserAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .requestMatchers(HttpMethod.GET, "/jokes").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/top").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jokes/calls/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/jokes").permitAll()
                                // Разрешить только модераторам и администраторам доступ к POST, PUT, DELETE запросам на /jokes
                                .requestMatchers(HttpMethod.POST, "/jokes").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority(), UserAuthority.ADMIN.getAuthority())
                                .requestMatchers(HttpMethod.PUT, "/jokes/{id}").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority(), UserAuthority.ADMIN.getAuthority())
                                .requestMatchers(HttpMethod.DELETE, "/jokes/{id}").hasAnyAuthority(UserAuthority.MODERATOR.getAuthority(), UserAuthority.ADMIN.getAuthority())
                                .requestMatchers("/registration", "/login").permitAll()
                                // Все остальные запросы доступны только администратору
                                .anyRequest().hasAuthority(UserAuthority.ADMIN.getAuthority()))
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}