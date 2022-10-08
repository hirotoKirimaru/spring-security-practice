package com.example.springsecuritypractice.biz.restapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@WithMockUser(value = "kirimaru")
class UsersControllerTest {
  @Autowired
  private MockMvc mockMvc;
  private final String rootUrl = "/users";

  @Test
  void success() throws Exception {
    // Language=json
    String expected = """
      {
        "id": "1",
        "email": "1@example.com",
        "name": "サンプル"
      }
      """;

    this.mockMvc.perform(get(rootUrl))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }
}