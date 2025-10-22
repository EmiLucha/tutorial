package com.ccsw.tutorial.loan.service;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.exception.GameAlreadyInLoanException;
import com.ccsw.tutorial.exception.TooManyLoansException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import com.ccsw.tutorial.loan.repository.LoanRepository;
import com.ccsw.tutorial.loan.repository.LoanSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(Long idGame, Long idClient, Date date, LoanSearchDto dto) {

        LoanSpecification titleSpec = new LoanSpecification(new SearchCriteria("game.id", ":", idGame));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", idClient));
        LoanSpecification dateSpec = new LoanSpecification(new SearchCriteria("dateBetween", "dateBetween", date));

        Specification<Loan> spec = titleSpec.and(clientSpec).and(dateSpec);

        Pageable pageable = dto.getPageable().getPageable();

        return this.loanRepository.findAll(spec, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto data) {

        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        // Comprobar que el mismo juego no puede estar prestado ninguna fecha entre checkDate y returnDate
        if (loanRepository.existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(data.getGame(), data.getReturnDate(), data.getCheckOutDate())) {
            throw new GameAlreadyInLoanException("El juego ya está reservado");
        }

        // Comprobar que el mismo cliente no puede tener más de dos juegos ninguna fecha entre checkDate y returnDate
        if (loanRepository.countByClientAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(data.getClient(), data.getReturnDate(), data.getCheckOutDate()) >= 2) {
            throw new TooManyLoansException("El cliente ya tiene dos préstamos activos en ese periodo");
        }

        BeanUtils.copyProperties(data, loan, "id");

        this.loanRepository.save(loan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

}
