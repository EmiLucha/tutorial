package com.ccsw.tutorialloan.service;

import com.ccsw.tutorialloan.common.criteria.SearchCriteria;
import com.ccsw.tutorialloan.exception.GameAlreadyInLoanException;
import com.ccsw.tutorialloan.exception.TooManyLoansException;
import com.ccsw.tutorialloan.model.loan.Loan;
import com.ccsw.tutorialloan.model.loan.LoanDto;
import com.ccsw.tutorialloan.model.loan.LoanSearchDto;
import com.ccsw.tutorialloan.repository.LoanRepository;
import com.ccsw.tutorialloan.repository.LoanSpecification;
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

        LoanSpecification titleSpec = new LoanSpecification(new SearchCriteria("gameId", ":", idGame));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("clientId", ":", idClient));
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
        if (loanRepository.existsByGameIdAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(data.getGame().getId(), data.getReturnDate(), data.getCheckOutDate())) {
            throw new GameAlreadyInLoanException("El juego ya está reservado");
        }

        // Comprobar que el mismo cliente no puede tener más de dos juegos ninguna fecha entre checkDate y returnDate
        if (loanRepository.countByClientIdAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(data.getClient().getId(), data.getReturnDate(), data.getCheckOutDate()) >= 2) {
            throw new TooManyLoansException("El cliente ya tiene dos préstamos activos en ese periodo");
        }

        BeanUtils.copyProperties(data, loan, "id", "client", "game");
        loan.setClient(data.getClient().getId());
        loan.setGame(data.getGame().getId());

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
