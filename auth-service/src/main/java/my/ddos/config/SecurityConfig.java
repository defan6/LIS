package my.ddos.config;


import lombok.RequiredArgsConstructor;
import my.ddos.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserService userService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/v1/auth", "/api/v1/auth/**", "/css", "/js").permitAll()
                                .anyRequest().authenticated()
                                )
                .formLogin(form -> form
                        .loginPage("/api/v1/auth/login")
                        .loginProcessingUrl("/api/v1/auth/login")
                        .defaultSuccessUrl("/api/v1/auth/dashboard", true)
                        .failureUrl("/api/v1/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/login?logout=true")
                        .permitAll()
                )
                .userDetailsService(userService)
                .build();
    }
}
