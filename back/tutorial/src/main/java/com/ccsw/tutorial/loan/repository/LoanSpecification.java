package com.ccsw.tutorial.loan.repository;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;
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
            Path<String> path = getPath(root);
            return builder.equal(path, criteria.getValue());
        }
        return null;
    }

    private Path<String> getPath(Root<Loan> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}