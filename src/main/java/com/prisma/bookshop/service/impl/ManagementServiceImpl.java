package com.prisma.bookshop.service.impl;

import com.prisma.bookshop.dao.BookRepository;
import com.prisma.bookshop.dao.BorrowedRepository;
import com.prisma.bookshop.dao.UserRepository;
import com.prisma.bookshop.model.BookEntity;
import com.prisma.bookshop.model.BorrowedEntity;
import com.prisma.bookshop.model.UserEntity;
import com.prisma.bookshop.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ManagementServiceImpl implements ManagementService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BorrowedRepository borrowedRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void postConstructor() {
        resetAllData();
    }

    @Override
    public void resetAllData() {
        borrowedRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
        resetUserEntities();
        resetBookEntities();
        resetBorrowedEntities();
    }

    private void resetUserEntities() {
        List<UserEntity> userEntities = readFile(new File("data/user.csv")).skip(1)
                .map(raw -> {
                    try {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setName(raw[0]);
                        userEntity.setFirstName(raw[1]);
//                      userEntity.setFullName(userEntity.getName() + "," +userEntity.getFirstName());
                        findAsDate(raw, 2).ifPresent(userEntity::setMemberSince);
                        findAsDate(raw, 3).ifPresent(userEntity::setMemberTill);
                        userEntity.setGender(raw[4]);
                        return userEntity;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        userRepository.saveAll(userEntities);
    }

    private void resetBookEntities() {
        List<BookEntity> bookEntities = readFile(new File("data/books.csv")).skip(1)
                .map(raw -> {
                    try {
                        BookEntity bookEntity = new BookEntity();
                        bookEntity.setTitle(raw[0]);
                        bookEntity.setAuthor(raw[1]);
                        bookEntity.setGenre(raw[2]);
                        bookEntity.setPublisher(raw[3]);
                        return bookEntity;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        bookRepository.saveAll(bookEntities);
    }

    private void resetBorrowedEntities() {
        List<BorrowedEntity> borrowedEntities = readFile(new File("data/borrowed.csv")).skip(1)
                .map(raw -> {
                    try {
                        BorrowedEntity borrowedEntity = new BorrowedEntity();
                        userRepository.findByFullName(raw[0].replaceAll("\"", "")).stream().findFirst().ifPresent(borrowedEntity::setBorrower);
                        bookRepository.findByTitle(raw[1]).stream().findFirst().ifPresent(borrowedEntity::setBook);
                        findAsDate(raw, 2).ifPresent(borrowedEntity::setBorrowedTo);
                        findAsDate(raw, 3).ifPresent(borrowedEntity::setBorrowedFrom);
                        return borrowedEntity;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        borrowedRepository.saveAll(borrowedEntities);
    }

    private Optional<LocalDate> findAsDate(String[] data, int index) {
        if (data == null || data.length < index) {
            return Optional.empty();
        }
        return Optional.ofNullable(data[index])
                .filter(date -> !date.isEmpty())
                .map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

    }

    private Stream<String[]> readFile(File file) {
        Stream.Builder<String[]> builder = Stream.builder();

        Scanner sc;
        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String cleanCsvRaw = cleanCsvRaw(sc.nextLine());
                String[] cellItem = cleanCsvRaw.split(",");
                for (int i = 0; i < cellItem.length; i++) {
                    cellItem[i] = cellItem[i].replaceAll("\\|", ",");
                }
                builder.accept(cellItem);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

    public String cleanCsvRaw(String item) {
        char[] chars = item.toCharArray();
        boolean firsQuote = false;
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\"') {
                if (firsQuote) {
                    firsQuote = false;
                    inQuote = false;
                } else {
                    firsQuote = true;
                    inQuote = true;
                }
            }
            if (inQuote && c == ',') {
                sb.append('|');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
