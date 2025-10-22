package com.ccsw.tutorial.loan.repository;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    boolean existsByGameAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(Game game, Date endDate, Date startDate);

    long countByClientAndCheckOutDateLessThanEqualAndReturnDateGreaterThanEqual(Client client, Date returnDate, Date checkOutDate);
}
