package com.example.springsecuritypractice.biz.restapi;

import com.example.springsecuritypractice.biz.restapi.security.AuthUser;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

  @RequestMapping(
      method = RequestMethod.GET,
      value = "/users",
      produces = {"application/json"})
  ResponseEntity<User> find(
      @AuthenticationPrincipal AuthUser user
  ) {
    System.out.println(user);
    return ResponseEntity.ok(User.builder()
        .id("1")
        .email("1@example.com")
        .name("サンプル")
        .build()
    );
  }

  @Value
  @Builder
  public static class User {

    private String id;
    private String email;
    private String name;
  }
}
