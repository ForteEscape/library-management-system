package com.management.library.controller.book.dto;

import com.management.library.domain.type.BookStatus;
import com.management.library.service.book.dto.BookServiceUpdateDto;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BookControllerUpdateDto {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BookUpdateRequest {

    @ApiModelProperty(example = "book1")
    @NotEmpty(message = "해당 부분은 비어있으면 안됩니다.")
    private String title;
    @ApiModelProperty(example = "author1")
    @NotEmpty(message = "해당 부분은 비어있으면 안됩니다.")
    private String author;
    @ApiModelProperty(example = "publisher1")
    @NotEmpty(message = "해당 부분은 비어있으면 안됩니다.")
    private String publisher;
    @ApiModelProperty(example = "2015")
    @Positive(message = "출판 연도는 0보다 작을 수 없습니다.")
    private int publishedYear;
    @ApiModelProperty(example = "location1")
    @NotEmpty(message = "해당 부분은 비어있으면 안됩니다.")
    private String location;
    @Max(value = 999, message = "분류 번호는 999를 넘길 수 없습니다.")
    @Min(value = 1, message = "분류 코드는 최소 1부터 시작합니다.")
    @ApiModelProperty(example = "130")
    private int typeCode;

    @Builder
    private BookUpdateRequest(String title, String author, String publisher, int publishedYear,
        String location,
        int typeCode) {
      this.title = title;
      this.author = author;
      this.publisher = publisher;
      this.publishedYear = publishedYear;
      this.location = location;
      this.typeCode = typeCode;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BookUpdateResponse {

    @ApiModelProperty(example = "1")
    private Long id;
    @ApiModelProperty(example = "book1")
    private String title;
    @ApiModelProperty(example = "author1")
    private String author;
    @ApiModelProperty(example = "publisher1")
    private String publisher;
    @ApiModelProperty(example = "2015")
    private int publishedYear;
    @ApiModelProperty(example = "location1")
    private String location;
    @ApiModelProperty(example = "130")
    private int typeCode;
    @ApiModelProperty(example = "AVAILABLE")
    private BookStatus status;

    @Builder
    private BookUpdateResponse(Long id, String title, String author, String publisher,
        int publishedYear,
        BookStatus status, String location, int typeCode) {
      this.id = id;
      this.title = title;
      this.author = author;
      this.publisher = publisher;
      this.publishedYear = publishedYear;
      this.status = status;
      this.location = location;
      this.typeCode = typeCode;
    }

    public static BookUpdateResponse of(BookServiceUpdateDto.Response response) {
      return BookUpdateResponse.builder()
          .id(response.getId())
          .author(response.getAuthor())
          .title(response.getTitle())
          .publisher(response.getPublisher())
          .publishedYear(response.getPublishedYear())
          .location(response.getLocation())
          .status(response.getStatus())
          .typeCode(response.getTypeCode())
          .build();
    }
  }
}
