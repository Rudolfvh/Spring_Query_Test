package org.example.integration.database.repository;


import lombok.RequiredArgsConstructor;
import org.example.annotation.IT;
import org.example.spring.database.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CompanyRepositoryTest {

    private final CompanyRepository companyRepository;
    private static final String DEFAULT_COMPANY_NAME = "Google";
    private static final String NEW_COMPANY_NAME = "Amine-Company";
    private static final Integer DEFAULT_COMPANY_SIZE = 4;
    private static final Integer SIZE_AFTER_DELETE = 2;

    @Test
    @Rollback
    void updateCompanyById() {
        companyRepository.findById(1).ifPresent((company) -> {
            assertEquals(company.getName(), DEFAULT_COMPANY_NAME);
        });
        companyRepository.updateNameById(NEW_COMPANY_NAME, 1);
        companyRepository.findById(1).ifPresent((company) -> {
            assertEquals(company.getName(), NEW_COMPANY_NAME);
        });
    }

    @Test
    @Rollback
    void deleteCompanyByStartingWith() {
        assertThat(companyRepository.findAll()).hasSize(DEFAULT_COMPANY_SIZE);
        companyRepository.deleteByNameStartingWith("A");
        assertThat(companyRepository.findAll()).hasSize(SIZE_AFTER_DELETE);
    }

}
