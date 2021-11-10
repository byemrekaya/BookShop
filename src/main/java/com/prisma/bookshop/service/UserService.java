package com.prisma.bookshop.service;

import com.prisma.bookshop.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findUserAtLeastHaveOneBook();

    List<UserDto> findUserDontHaveBook();

    List<UserDto> findUserBorrowedAtDate(String date);
}
