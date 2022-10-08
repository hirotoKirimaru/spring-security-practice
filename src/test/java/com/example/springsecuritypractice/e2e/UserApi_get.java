package com.example.springsecuritypractice.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApi_get {
  @Autowired
  TestRestTemplate restTemplate;

  @Test
  void test_01() {
    login("email", "password");

//    HttpHeaders headers =
//    HttpEntity<String> httpEntity = new HttpEntity<>("")
//    restTemplate.exchange("/users", HttpMethod.GET, httpEntity, String.class)
//
  }

  public void login(String email, String password) {

  }

}
