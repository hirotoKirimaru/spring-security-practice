package com.example.springsecuritypractice.biz.restapi.security;

import com.example.springsecuritypractice.biz.restapi.UsersController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      JsonAuthenticationFilter jsonAuthenticationFilter) throws Exception {
    http.csrf(csrf -> csrf
            .ignoringAntMatchers("/login")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .cors().and()
        .addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            // ログアウト時のリダイレクトをしないようにする
            .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
            .invalidateHttpSession(true))
        .authorizeHttpRequests(auth -> auth
            .mvcMatchers("/actuator/health").permitAll()
            .mvcMatchers(HttpMethod.POST.name(), "/admin").hasAnyRole("ROLE_ADMIN")
            .mvcMatchers("/").permitAll()
            .anyRequest().authenticated()
        ).exceptionHandling(handler -> handler
            // 未認証時のレスポンス
            // .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            // 認証失敗時のレスポンス
            .accessDeniedHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
        );

    return http.build();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("*"));
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public JsonAuthenticationFilter jsonAuthenticationFilter(AuthenticationManager authenticationManager){
    JsonAuthenticationFilter filter = new JsonAuthenticationFilter(authenticationManager);
    // 認証成功・失敗のリダイレクト解除
    filter.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK));
    filter.setAuthenticationFailureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_OK));

    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
