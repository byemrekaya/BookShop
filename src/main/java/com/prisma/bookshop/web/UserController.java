package com.prisma.bookshop.web;

import com.prisma.bookshop.dto.UserDto;
import com.prisma.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/v")
    public ResponseEntity<List<UserDto>> findAtLeastHaveOneBook() {
        return ResponseEntity.ok(userService.findUserAtLeastHaveOneBook());
    }

    @GetMapping(value = "/dontHaveOneBook")
    public ResponseEntity<List<UserDto>> findUserDontHaveBook() {
        return ResponseEntity.ok(userService.findUserDontHaveBook());
    }

    @GetMapping(value = "/borrowedAtDate")
    public ResponseEntity<List<UserDto>> findUserBorrowedAtDate(@RequestParam String date) {
        return ResponseEntity.ok(userService.findUserBorrowedAtDate(date));
    }
}
