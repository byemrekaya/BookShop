package com.prisma.bookshop;

import com.prisma.bookshop.dao.BorrowedRepository;
import com.prisma.bookshop.dto.UserDto;
import com.prisma.bookshop.model.BorrowedEntity;
import com.prisma.bookshop.model.UserEntity;
import com.prisma.bookshop.service.ManagementService;
import com.prisma.bookshop.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class UserServiceIntegrationTests {

    @Autowired
    private ManagementService managementService;
    @Autowired
    private BorrowedRepository borrowedRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        managementService.resetAllData();
    }

    @Test
    void returns_all_users_who_have_actually_borrowed_at_least_one_book() {
        List<UserDto> userAtLeastHaveOneBook = userService.findUserAtLeastHaveOneBook();
        Assertions.assertNotNull(userAtLeastHaveOneBook);
        Assertions.assertFalse(userAtLeastHaveOneBook.isEmpty());
    }
    @Test
    void returns_all_non_terminated_users_who_have_not_currently_borrowed_anything() {
        List<UserDto> userAtLeastHaveOneBook = userService.findUserAtLeastHaveOneBook();
        Assertions.assertNotNull(userAtLeastHaveOneBook);
        Assertions.assertFalse(userAtLeastHaveOneBook.isEmpty());

        UserDto selectedUser = userAtLeastHaveOneBook.iterator().next();
        BorrowedEntity query = new BorrowedEntity();
        query.setBorrower(UserEntity.fromId(selectedUser.getId()));

        List<BorrowedEntity> userOfBorrowed = borrowedRepository.findAll(Example.of(query));
        borrowedRepository.deleteAll(userOfBorrowed);

        List<UserDto> userDontHaveBook = userService.findUserDontHaveBook();
        Assertions.assertNotNull(userDontHaveBook);
        Assertions.assertFalse(userDontHaveBook.isEmpty());

        UserDto selectedUserFromUserDontHaveBookList = userDontHaveBook.stream()
                .filter(userDto -> Objects.equals(userDto.getId(), selectedUser.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(selectedUserFromUserDontHaveBookList);
        Assertions.assertEquals(selectedUser,selectedUserFromUserDontHaveBookList);
    }
}
