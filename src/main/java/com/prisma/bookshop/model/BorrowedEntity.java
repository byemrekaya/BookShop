package com.prisma.bookshop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowed")
@Getter
@Setter
public class BorrowedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "borrower")
    private UserEntity borrower;

    @ManyToOne
    @JoinColumn(name = "book")
    private BookEntity book;

    private LocalDate borrowedFrom;

    private LocalDate borrowedTo;

}
