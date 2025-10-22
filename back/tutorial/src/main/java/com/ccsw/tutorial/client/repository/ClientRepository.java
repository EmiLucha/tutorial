package com.ccsw.tutorial.client.repository;

import com.ccsw.tutorial.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */
public interface ClientRepository extends CrudRepository<Client, Long>, JpaRepository<Client, Long> {

    boolean existsByName(String name);

}