package com.school.erp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/notifications/stream").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/api/notifications/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/api/students/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/api/teachers/**").hasRole("TEACHER")
                .requestMatchers("/api/class-sections/**").hasRole("TEACHER")
                .requestMatchers("/attendance/session/**").hasRole("TEACHER")
                .requestMatchers("/attendance/mark").hasRole("TEACHER")
                .requestMatchers("/attendance/my").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/homework/class/**").hasRole("TEACHER")
                .requestMatchers("/homework/*/submissions").hasRole("TEACHER")
                .requestMatchers("/homework/my").hasRole("STUDENT")
                .requestMatchers("/homework/submit").hasRole("STUDENT")
                .requestMatchers("/homework/my-submissions").hasRole("STUDENT")
                .requestMatchers("/homework/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/exams").hasRole("TEACHER")
                .requestMatchers("/exams/class/**").hasRole("TEACHER")
                .requestMatchers("/exams/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/marks").hasRole("TEACHER")
                .requestMatchers("/marks/exam/**").hasRole("TEACHER")
                .requestMatchers("/marks/my").hasRole("STUDENT")
                .requestMatchers("/timetable").hasRole("TEACHER")
                .requestMatchers("/timetable/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/announcements").hasRole("TEACHER")
                .requestMatchers("/announcements/my").hasRole("STUDENT")
                .requestMatchers("/complaints").hasRole("TEACHER")
                .requestMatchers("/complaints/*/status").hasRole("TEACHER")
                .requestMatchers("/complaints/my").hasRole("STUDENT")
                .requestMatchers("/complaints/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/study-material").hasRole("TEACHER")
                .requestMatchers("/study-material/my").hasRole("STUDENT")
                .requestMatchers("/study-material/**").hasAnyRole("STUDENT", "TEACHER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
