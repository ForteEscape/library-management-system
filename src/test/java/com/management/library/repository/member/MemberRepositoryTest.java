package com.management.library.repository.member;

import static com.management.library.domain.type.MemberRentalStatus.*;
import static org.assertj.core.api.Assertions.*;

import com.management.library.domain.member.Address;
import com.management.library.domain.member.Member;
import com.management.library.domain.type.Authority;
import com.management.library.domain.type.MemberRentalStatus;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @DisplayName("회원을 회원 도서 번호로 조회할 수 있다.")
  @Test
  public void findByMemberCode() {
    // given
    Member member1 = createMember("kim", RENTAL_UNAVAILABLE, "123456");
    Member member2 = createMember("park", RENTAL_AVAILABLE, "123457");
    Member member3 = createMember("lee", RENTAL_UNAVAILABLE, "123458");

    memberRepository.saveAll(List.of(member1, member2, member3));

    // when
    Member member = memberRepository.findByMemberCode("123456")
        .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

    // then
    assertThat(member)
        .extracting("name", "memberRentalStatus", "memberCode")
        .containsExactlyInAnyOrder(
            "kim", RENTAL_UNAVAILABLE, "123456"
        );
  }

  @DisplayName("회원을 회원 도서 번호로 조회할 수 있다. 존재하지 않는 경우 예외를 발생시킨다.")
  @Test
  public void findByMemberCodeWithNoMember() {
    // when
    // then
    assertThatThrownBy(() -> memberRepository.findByMemberCode("123456")
        .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("사용자가 존재하지 않습니다.");
  }

  @DisplayName("회원을 도서 대출 가능 상태로 조회할 수 있다. 페이징이 가능하다.")
  @Test
  public void findAllByMemberRentalStatus() {
    // given
    Member member1 = createMember("kim", RENTAL_UNAVAILABLE, "123456");
    Member member2 = createMember("park", RENTAL_AVAILABLE, "123457");
    Member member3 = createMember("lee", RENTAL_UNAVAILABLE, "123458");
    Member member4 = createMember("kim", RENTAL_UNAVAILABLE, "123459");
    Member member5 = createMember("im", RENTAL_AVAILABLE, "123460");
    Member member6 = createMember("han", RENTAL_UNAVAILABLE, "123461");
    Member member7 = createMember("hong", RENTAL_UNAVAILABLE, "123462");
    Member member8 = createMember("kang", RENTAL_UNAVAILABLE, "123463");

    memberRepository.saveAll(
        List.of(member1, member2, member3, member4, member5, member6, member7, member8)
    );

    PageRequest pageRequest1 = PageRequest.of(0, 5);
    PageRequest pageRequest2 = PageRequest.of(1, 5);

    // when
    Page<Member> members1 = memberRepository.findAllByMemberRentalStatus(RENTAL_UNAVAILABLE,
        pageRequest1);
    List<Member> result1 = members1.getContent();

    Page<Member> members2 = memberRepository.findAllByMemberRentalStatus(
        RENTAL_UNAVAILABLE, pageRequest2);
    List<Member> result2 = members2.getContent();

    // then
    assertThat(result1).hasSize(5)
        .extracting("name", "memberRentalStatus", "memberCode")
        .containsExactlyInAnyOrder(
            tuple("kim", RENTAL_UNAVAILABLE, "123456"),
            tuple("lee", RENTAL_UNAVAILABLE, "123458"),
            tuple("kim", RENTAL_UNAVAILABLE, "123459"),
            tuple("han", RENTAL_UNAVAILABLE, "123461"),
            tuple("hong", RENTAL_UNAVAILABLE, "123462")
        );

    assertThat(result2).hasSize(1)
        .extracting("name", "memberRentalStatus", "memberCode")
        .containsExactlyInAnyOrder(
            tuple("kang", RENTAL_UNAVAILABLE, "123463")
        );
  }

  @DisplayName("특정 회원의 이름과 주소로 회원을 조회할 수 있다.")
  @Test
  public void findByMemberNameAndAddress() {
    // given
    Member member1 = createMember("kim", RENTAL_UNAVAILABLE, "123456");
    Member member2 = createMember("park", RENTAL_AVAILABLE, "123457");
    Member member3 = createMember("lee", RENTAL_UNAVAILABLE, "123458");
    Member member4 = createMember("kim", RENTAL_UNAVAILABLE, "123459");
    Member member5 = createMember("im", RENTAL_AVAILABLE, "123460");
    Member member6 = createMember("han", RENTAL_UNAVAILABLE, "123461");
    Member member7 = createMember("hong", RENTAL_UNAVAILABLE, "123462");
    Member member8 = createMember("kang", RENTAL_UNAVAILABLE, "123463");

    memberRepository.saveAll(
        List.of(member1, member2, member3, member4, member5, member6, member7, member8)
    );

    // when
    Member member = memberRepository.findByMemberNameAndAddress("park", "경상남도", "김해시", "삼계로")
        .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));

    // then
    assertThat(member)
        .extracting("name", "memberCode")
        .contains("park", "123457");

    assertThat(member)
        .extracting(Member::getAddress)
        .extracting(Address::getLegion, Address::getCity, Address::getStreet)
        .contains("경상남도", "김해시", "삼계로");
  }

  @DisplayName("존재하지 않는 회원을 조회시 예외를 발생시킨다.")
  @Test
  public void findByMemberNameAndAddressWithNoResult() {
    // given
    Member member1 = createMember("kim", RENTAL_UNAVAILABLE, "123456");
    Member member2 = createMember("park", RENTAL_AVAILABLE, "123457");
    Member member3 = createMember("lee", RENTAL_UNAVAILABLE, "123458");
    Member member4 = createMember("kim", RENTAL_UNAVAILABLE, "123459");
    Member member5 = createMember("im", RENTAL_AVAILABLE, "123460");
    Member member6 = createMember("han", RENTAL_UNAVAILABLE, "123461");
    Member member7 = createMember("hong", RENTAL_UNAVAILABLE, "123462");
    Member member8 = createMember("kang", RENTAL_UNAVAILABLE, "123463");

    memberRepository.saveAll(
        List.of(member1, member2, member3, member4, member5, member6, member7, member8)
    );

    // when
    // then
    assertThatThrownBy(() -> memberRepository.findByMemberNameAndAddress("um", "경상남도", "김해시", "삼계로")
        .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다.")))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("해당 회원이 존재하지 않습니다.");
  }

  @DisplayName("가장 최근에 저장된 회원의 데이터를 가져올 수 있다.")
  @Test
  public void findTopByOrderByMemberId() {
    // given
    Member member1 = createMember("park", RENTAL_AVAILABLE, "123457");
    Member member2 = createMember("lee", RENTAL_UNAVAILABLE, "123458");
    Member member3 = createMember("kim", RENTAL_UNAVAILABLE, "123459");
    Member member4 = createMember("im", RENTAL_AVAILABLE, "123460");
    Member member5 = createMember("han", RENTAL_UNAVAILABLE, "123461");

    memberRepository.saveAll(
        List.of(member1, member2, member3, member4, member5)
    );

    // when
    String memberCode = memberRepository.findTopByOrderByIdDesc()
        .orElseThrow(() -> new NoSuchElementException("저장된 회원이 없습니다."));

    // then
    assertThat(memberCode).isEqualTo("123461");
  }

  @DisplayName("가장 최근에 저장된 회원의 데이터를 가져올 수 있다. 존재하지 않을 시 예외를 발생시킨다.")
  @Test
  public void findTopByOrderByMemberIdWithEmptyMember() {
    // when
    // then
    assertThatThrownBy(() -> memberRepository.findTopByOrderByIdDesc()
        .orElseThrow(() -> new NoSuchElementException("저장된 회원이 없습니다.")))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("저장된 회원이 없습니다.");
  }

  private static Member createMember(String name, MemberRentalStatus memberRentalStatus,
      String memberCode) {
    Address address = Address.builder()
        .legion("경상남도")
        .city("김해시")
        .street("삼계로")
        .build();

    return Member.builder()
        .name(name)
        .birthdayCode("980101")
        .memberRentalStatus(memberRentalStatus)
        .memberCode(memberCode)
        .address(address)
        .password("1234")
        .authority(Authority.ROLE_MEMBER)
        .build();
  }
}