package com.ccsw.tutorialcategory.repository;

import com.ccsw.tutorialcategory.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

}