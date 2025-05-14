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
    private UserDetailsService userDetailsService; // zo ga je in je JPA

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    } 

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/login**").permitAll()
                        		.requestMatchers("/registration**").permitAll()
                        		.requestMatchers("/events/**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/i18n/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/404**").permitAll()
                                .requestMatchers("/500**").permitAll()
                                
                                .requestMatchers("/favourites/**").hasAnyRole("USER")
                                .requestMatchers("/events/favourites/**").hasAnyRole("USER")
                                
                                .requestMatchers("/speakers/**").hasAnyRole("ADMIN")
                                .requestMatchers("/rooms/**").hasAnyRole("ADMIN")
                				.requestMatchers("/events/new/**").hasAnyRole("ADMIN"))
                .formLogin(form ->
                        form.defaultSuccessUrl("/events", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password")
                )
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"));

        return http.build();
    }

}