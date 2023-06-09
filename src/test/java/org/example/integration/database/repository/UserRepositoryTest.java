package org.example.integration.database.repository;



import lombok.RequiredArgsConstructor;
import org.example.annotation.IT;
import org.example.spring.database.entity.Role;
import org.example.spring.database.entity.User;
import org.example.spring.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private final List<Long> sortedByBirthUserIds = new ArrayList<>(List.of(4L, 5L, 1L, 2L));
    private final List<Long> sortedByBirthAndFioUserIds = new ArrayList<>(List.of(5L, 4L, 1L, 2L));
    private final List<Long> sortedUsersByBirthDateIds = new ArrayList<>(List.of(4L, 2L, 3L));
    private static final Integer FIND_FIRST_4_SIZE = 4;
    private static final Integer ADMIN_RESULT_SIZE = 2;
    private static final Integer USER_RESULT_SIZE = 3;
    private static final LocalDate DATE_FROM = LocalDate.of(1980, 1, 1);
    private static final LocalDate DATE_TO = LocalDate.of(1990, 12, 31);

    @Test
    void findFirst4SortByBirthDate() {
        var users = userRepository.findFirst4By(Sort.by("birthDate"));
        assertThat(users).hasSize(FIND_FIRST_4_SIZE);
        assertEquals(users.stream().map(User::getId).collect(Collectors.toList()), sortedByBirthUserIds);
    }

    @Test
    void findFirst4SortByBirthDateAndFio() {
        var users = userRepository.findFirst4By(Sort.by("birthDate")
                .and(Sort.by("firstname")
                        .and(Sort.by("lastname"))));
        assertThat(users).hasSize(4);
        assertEquals(users.stream().map(User::getId).collect(Collectors.toList()), sortedByBirthAndFioUserIds);
    }

    @Test
    void findAllAdminBetween() {
        var admins = userRepository.findAllByRoleAndBirthDateBetween(Role.ADMIN, DATE_FROM, DATE_TO);
        assertThat(admins).hasSize(ADMIN_RESULT_SIZE);
        var birthDates = admins.stream().map(User::getBirthDate).collect(Collectors.toList());
        birthDates.forEach(birthDate -> {
            assertTrue(birthDate.isAfter(DATE_FROM));
            assertTrue(birthDate.isBefore(DATE_TO));
        });
    }

    @Test
    void findAllByRoleTest() {
        var users = userRepository.findAll(Role.ADMIN, Pageable.unpaged());
        assertThat(users).hasSize(ADMIN_RESULT_SIZE);
        users = userRepository.findAll(Role.USER, Pageable.unpaged());
        assertThat(users).hasSize(USER_RESULT_SIZE);
    }

    @Test
    void findAllBySortTest() {
        var pageable = PageRequest.of(0, 4, Sort.by("birthDate"));
        var users = userRepository.findAll(Role.USER, pageable);
        assertThat(users).hasSize(USER_RESULT_SIZE);
        assertEquals(users.stream().map(User::getId).collect(Collectors.toList()), sortedUsersByBirthDateIds);
    }

    @Test
    void findAllByPageTest() {
        // users.size in db = 3
        var pageable = PageRequest.of(0, 3);
        var users = userRepository.findAll(Role.USER, pageable);
        assertThat(users).hasSize(3);
        pageable = PageRequest.of(2,1);
        users = userRepository.findAll(Role.USER, pageable);
        assertThat(users).hasSize(1);
        pageable = PageRequest.of(3,4);
        users = userRepository.findAll(Role.USER, pageable);
        assertThat(users).hasSize(0);
    }
}
