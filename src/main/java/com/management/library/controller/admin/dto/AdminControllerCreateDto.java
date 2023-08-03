package com.management.library.controller.admin.dto;

import com.management.library.service.admin.dto.AdminServiceCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdminControllerCreateDto {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Request {

    private String email;
    private String name;
    private String password;

    public Request(String email, String name, String password) {
      this.email = email;
      this.name = name;
      this.password = password;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Response {

    private Long id;
    private String name;
    private String email;

    @Builder
    private Response(Long id, String name, String email) {
      this.id = id;
      this.name = name;
      this.email = email;
    }

    public static Response of(AdminServiceCreateDto.Response response) {
      return Response.builder()
          .id(response.getId())
          .name(response.getName())
          .email(response.getEmail())
          .build();
    }
  }
}
