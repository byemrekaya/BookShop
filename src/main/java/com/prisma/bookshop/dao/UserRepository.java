package com.prisma.bookshop.dao;

import com.prisma.bookshop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("select u from UserEntity u where concat(u.name,',',u.firstName) = :name")
    List<UserEntity> findByFullName(String name);

    @Query(nativeQuery = true, value = "select distinct u.* from user u left join borrowed b on u.id = b.borrower where b.id is null")
    List<UserEntity> findUserDontHaveBook();

    @Query("select distinct  u from UserEntity u,BorrowedEntity b WHERE u.id = b.borrower.id and b.borrowedFrom =:date")
    List<UserEntity> findUserBorrowedAtDate(LocalDate date);
}
