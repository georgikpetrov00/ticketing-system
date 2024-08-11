package com.georgi.crm.core.security;

import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
  
  private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
  private final UserDetailsService userDetailsService;
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(Customizer.withDefaults())
      
      .formLogin(form -> form.loginPage("/api/auth/login").permitAll())
      
      .authorizeHttpRequests(
        authn -> authn
          .requestMatchers("/admin").hasAnyRole("ADMIN")
          .requestMatchers("/processor").hasAnyRole("ADMIN", "PROCESSOR")
          .requestMatchers("/customer").hasAnyRole("ADMIN", "PROCESSOR", "CUSTOMER")
          .requestMatchers("/login", "authenticate", "/error").permitAll()
          .requestMatchers("/api/auth/**").permitAll()
          .anyRequest().authenticated()
      )
      
      .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
      
      .cors()
      .and()
      .csrf().disable()
      
      .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
      
//      .formLogin(Customizer.withDefaults())
//      .rememberMe(Customizer.withDefaults())
      ;
    
    return http.build();
  }
  
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(userDetailsService);
  }
  
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      request.setAttribute("errorMessage", "You do not have permission to access this resource.");
      request.setAttribute("exception", accessDeniedException);
      request.getRequestDispatcher("/error").forward(request, response);
    };
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PASSWORD_ENCODER;
  }
  
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  
}
