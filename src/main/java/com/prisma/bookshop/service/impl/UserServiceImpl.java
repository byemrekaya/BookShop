package com.prisma.bookshop.service.impl;

import com.prisma.bookshop.dao.BorrowedRepository;
import com.prisma.bookshop.dao.UserRepository;
import com.prisma.bookshop.dto.UserDto;
import com.prisma.bookshop.model.UserEntity;
import com.prisma.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BorrowedRepository borrowedRepository;

    @Override
    public List<UserDto> findUserAtLeastHaveOneBook() {
        return borrowedRepository.findUserAtLeastHaveOneBook().stream()
                .map(this::maptoUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findUserBorrowedAtDate(String date) {
        return Optional.ofNullable(date)
                .filter(dateVal -> !dateVal.isEmpty())
                .map(dateVal -> LocalDate.parse(dateVal, DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                .map(userRepository::findUserBorrowedAtDate)
                .orElseGet(ArrayList::new)
                .stream()
                .map(this::maptoUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findUserDontHaveBook() {
        return userRepository.findUserDontHaveBook().stream()
                .map(this::maptoUserDto)
                .collect(Collectors.toList());
    }

    private UserDto maptoUserDto(UserEntity userEntity) {
        UserDto user = new UserDto();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setFirstName(userEntity.getFirstName());
        user.setGender(userEntity.getGender());
        user.setMemberSince(userEntity.getMemberSince());
        user.setMemberTill(userEntity.getMemberTill());
        return user;
    }
}
