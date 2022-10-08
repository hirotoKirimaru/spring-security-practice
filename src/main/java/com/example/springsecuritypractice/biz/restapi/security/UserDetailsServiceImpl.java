package com.example.springsecuritypractice.biz.restapi.security;

import com.example.springsecuritypractice.biz.restapi.UsersController;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new AuthUser(User.builder()
        .username("kirimaru")
        .password("{bcrypt}$2a$12$aOJOHaGigNu5gue.PYVhNOfwE/lwhJ0xBAOMdAQHlgY9sezVwgoFe")
        .roles("ADMIN")
        .build(),
        UsersController.User.builder()
            .id("1")
            .email("admin@example.com")
            .name("きり丸")
            .build()
        );
  }
}
