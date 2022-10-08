package com.example.springsecuritypractice.biz.restapi.security;

import com.example.springsecuritypractice.biz.restapi.UsersController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  ObjectMapper om = new ObjectMapper();

  private static final String USERNAME_PARAMETER = "email";
  private static final String PASSWORD_PARAMETER = "password";

  protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

  public JsonAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(new AntPathRequestMatcher("/login", "POST"));
    this.setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    Map<String, Object> requestObject;
    try {
      requestObject = om.readValue(request.getInputStream(), Map.class);
    } catch (IOException e) {
      requestObject = new HashMap<>();
    }

    String username = Optional
        .ofNullable(requestObject.get(USERNAME_PARAMETER))
        .map(Object::toString)
        .map(String::trim)
        .orElse("");

    String password = Optional
        .ofNullable(requestObject.get(PASSWORD_PARAMETER))
        .map(Object::toString)
        .orElse("");

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

    return this.getAuthenticationManager().authenticate((authRequest));
  }
}
