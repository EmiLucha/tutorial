package com.ccsw.tutorialloan.repository;

import com.ccsw.tutorialloan.model.loan.Loan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    boolean existsByGameIdAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(Long gameId, Date endDate, Date startDate);

    long countByClientIdAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(Long clientId, Date returnDate, Date checkOutDate);
}
