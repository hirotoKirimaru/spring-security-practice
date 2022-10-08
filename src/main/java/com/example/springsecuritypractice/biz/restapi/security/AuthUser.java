package com.example.springsecuritypractice.biz.restapi.security;

import com.example.springsecuritypractice.biz.restapi.UsersController;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthUser extends User {
  private final UsersController.User user;

  public AuthUser(UserDetails userDetails, UsersController.User user) {
    super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    this.user = user;
  }
}
