package org.example.spring.database.repository;


import org.example.spring.database.entity.Role;
import org.example.spring.database.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByRoleAndBirthDateBetween(Role role, LocalDate birthDate, LocalDate birthDate2);

    List<User> findFirst4By(Sort sort);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.role = :role
            """)
    List<User> findAll(Role role, Pageable pageable);

}
