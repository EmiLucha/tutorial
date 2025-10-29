package com.ccsw.tutorialloan.repository;

import com.ccsw.tutorialloan.common.criteria.SearchCriteria;
import com.ccsw.tutorialloan.model.loan.Loan;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if ("dateBetween".equalsIgnoreCase(criteria.getOperation()) && criteria.getValue() != null) {
            return builder.and(builder.lessThanOrEqualTo(root.get("checkOutDate"), (Date) criteria.getValue()), builder.greaterThanOrEqualTo(root.get("returnDate"), (Date) criteria.getValue()));
        }
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {

            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
        return null;
    }

}