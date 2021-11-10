package com.prisma.bookshop.dao;

import com.prisma.bookshop.model.BorrowedEntity;
import com.prisma.bookshop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedRepository extends JpaRepository<BorrowedEntity,Long> {
    @Query("select distinct b.borrower from BorrowedEntity b")
    List<UserEntity> findUserAtLeastHaveOneBook();
}
