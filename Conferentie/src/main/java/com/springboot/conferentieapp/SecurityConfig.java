package com.springboot.conferentieapp; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService; 

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    } 

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login**","/fragments/**", "/i18n/**", "/registration**", 
                		"/events**", "/css/**", "/images/**").permitAll()
                .requestMatchers("/speakers**", "/speakers/**", "/rooms/**", "/events/new**", 
                		"/events/**").hasRole("ADMIN")
                .requestMatchers("/events/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/events", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            );

        return http.build();
    }


}