package com.example.springsecuritypractice.e2e;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApi_get {

  @Autowired
  TestRestTemplate restTemplate;

  List<String> COOKIES = List.of();

  @Test
  void test_01() {
    login("email", "password");

    HttpHeaders headers = getHttpHeaders();
    HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response =
        restTemplate.exchange("/users", HttpMethod.GET, httpEntity, String.class);

    // TODO: JSESSIONIDの発行が行われていない…？
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new RuntimeException("変なこと起きてる！");
    }
  }

  public void login(String email, String password) {
    HttpHeaders headers = getHttpHeaders();

    HttpEntity<String> httpEntity = new HttpEntity<>(
        """
            {
               "email": %1$s,
               "password": %2$s
            }
            """.formatted(email, password), headers);
    ResponseEntity<String> response =
        restTemplate.exchange("/login", HttpMethod.POST, httpEntity, String.class);

    COOKIES = response.getHeaders().get(HttpHeaders.SET_COOKIE);

    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new RuntimeException("変なこと起きてる！");
    }

  }

  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

    for (String cookie : COOKIES) {

      if (cookie.startsWith("XSRF-TOKEN")) {
        String format = cookie.split("XSRF-TOKEN=")[1].split(";")[0];
        headers.add("X-XSRF-TOKEN", format);
      }

      headers.add(HttpHeaders.COOKIE, cookie);
    }

    return headers;
  }

}
