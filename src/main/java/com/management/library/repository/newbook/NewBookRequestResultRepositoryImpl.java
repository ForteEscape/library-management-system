package com.management.library.repository.newbook;

import static com.management.library.domain.QAdministrator.*;
import static com.management.library.domain.QNewBookRequest.*;
import static com.management.library.domain.QNewBookRequestResult.*;

import com.management.library.domain.newbook.NewBookRequestResult;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class NewBookRequestResultRepositoryImpl implements NewBookRequestResultRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  public NewBookRequestResultRepositoryImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }


  @Override
  public Optional<NewBookRequestResult> findByRequestId(Long requestId) {
    NewBookRequestResult result = queryFactory.selectFrom(newBookRequestResult)
        .join(newBookRequestResult.newBookRequest, newBookRequest)
        .where(newBookRequest.id.eq(requestId))
        .fetchOne();

    return Optional.ofNullable(result);
  }

  @Override
  public Page<NewBookRequestResult> findByAdminId(String adminEmail, Pageable pageable) {
    List<NewBookRequestResult> result = queryFactory.selectFrom(newBookRequestResult)
        .join(newBookRequestResult.administrator, administrator).fetchJoin()
        .where(administrator.email.eq(adminEmail))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = queryFactory.select(newBookRequestResult.count())
        .from(newBookRequestResult)
        .join(newBookRequestResult.administrator, administrator)
        .where(administrator.email.eq(adminEmail));

    return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
  }
}
