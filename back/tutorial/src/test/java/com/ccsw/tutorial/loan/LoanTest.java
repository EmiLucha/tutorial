package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.exception.GameAlreadyInLoanException;
import com.ccsw.tutorial.exception.TooManyLoansException;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import com.ccsw.tutorial.loan.repository.LoanRepository;
import com.ccsw.tutorial.loan.service.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private LoanDto loanDto;
    private Loan loan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loanDto = new LoanDto();

        Category category = new Category();
        category.setId(1L);
        category.setName("test");

        Author author = new Author();
        author.setId(1L);
        author.setName("test");
        author.setNationality("test");

        Game game = new Game();
        game.setId(1L);
        game.setTitle("test");
        game.setCategory(category);
        game.setAuthor(author);

        loanDto.setGame(game);

        Client client = new Client();
        client.setId(1L);
        client.setName("test");

        loanDto.setClient(client);
        loanDto.setCheckOutDate(java.sql.Date.valueOf("2025-10-01"));
        loanDto.setReturnDate(java.sql.Date.valueOf("2025-10-10"));

        loan = new Loan();
        loan.setId(1L);
    }

    @Test
    void findPageShouldReturnPageOfLoans() {

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(new com.ccsw.tutorial.common.pagination.PageableRequest(0, 10));

        Pageable pageable = dto.getPageable().getPageable();

        Page<Loan> expectedPage = new PageImpl<>(List.of(new Loan(), new Loan()));
        when(loanRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Loan> result = loanService.findPage(1L, 2L, new Date(), dto);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(loanRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getShouldReturnNullWhenNotExists() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        Loan result = loanService.get(1L);

        assertNull(result);
        verify(loanRepository).findById(1L);
    }

    @Test
    void saveShouldCreateNewLoanWhenIdIsNull() {
        when(loanRepository.existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(false);
        when(loanRepository.countByClientAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(0L);

        loanService.save(null, loanDto);

        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void saveShouldUpdateExistingLoanWhenIdIsNotNull() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(false);
        when(loanRepository.countByClientAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(0L);

        loanService.save(1L, loanDto);

        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void saveShouldThrowGameAlreadyInLoanExceptionWhenGameAlreadyLoaned() {
        when(loanRepository.existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(true);

        assertThrows(GameAlreadyInLoanException.class, () -> loanService.save(null, loanDto));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void saveShouldThrowTooManyLoansExceptionWhenClientHasTwoLoans() {
        when(loanRepository.existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(false);
        when(loanRepository.countByClientAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(any(), any(), any())).thenReturn(2L);

        assertThrows(TooManyLoansException.class, () -> loanService.save(null, loanDto));

        verify(loanRepository, never()).save(any());
    }

}