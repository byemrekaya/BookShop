package com.prisma.bookshop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String firstName;

    private LocalDate memberSince;

    private LocalDate memberTill;

    private String gender;

    @OneToMany(mappedBy = "borrower")
    private List<BorrowedEntity> books;
}
