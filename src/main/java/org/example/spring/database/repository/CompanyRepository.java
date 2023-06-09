package org.example.spring.database.repository;

import org.example.spring.database.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Company c
            SET c.name = :name
            WHERE c.id =:id
            """)
    void updateNameById(String name, Integer id);

    void deleteByNameStartingWith(String startSymbols);

}
