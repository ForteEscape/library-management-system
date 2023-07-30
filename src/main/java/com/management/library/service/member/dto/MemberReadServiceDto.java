package com.management.library.service.member.dto;

import com.management.library.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberReadServiceDto {

  private String name;
  private String memberCode;
  private String birthdayCode;

  @Builder
  private MemberReadServiceDto(String name, String memberCode, String birthdayCode) {
    this.name = name;
    this.memberCode = memberCode;
    this.birthdayCode = birthdayCode;
  }

  public static MemberReadServiceDto of(Member member){
    return MemberReadServiceDto.builder()
        .name(member.getName())
        .memberCode(member.getMemberCode())
        .birthdayCode(member.getBirthdayCode())
        .build();
  }
}
