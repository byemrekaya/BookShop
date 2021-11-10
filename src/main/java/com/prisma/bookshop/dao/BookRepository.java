package com.prisma.bookshop.dao;

import com.prisma.bookshop.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity,Long> {

    List<BookEntity> findByTitle(String title);

}
