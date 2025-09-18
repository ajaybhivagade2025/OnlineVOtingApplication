/*

package com.onlineVotingApplication2.config;

import com.onlineVotingApplication2.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    @Bean
//    @Order(1)
//    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .securityMatcher("/admin/**")
//            .authorizeHttpRequests(authorize -> authorize
//                .anyRequest().hasRole("ADMIN")
//            )
//            .formLogin(form -> form
//                .loginPage("/admin/login")
//                .loginProcessingUrl("/admin/login")
//                .defaultSuccessUrl("/admin/dashboard", true)
//                .failureUrl("/admin/login?error=true")
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/admin/logout")
//                .logoutSuccessUrl("/")
//            );
//        return http.build();
//    }
*/
/**//*

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/voter/**", "/css/**", "/js/**").permitAll()
//                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()); // Disabling CSRF for simplicity, can be configured later
        return http.build();
    }
}
*/
