package com.company.application.repository;

import com.company.application.domain.Available;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Available entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableRepository extends JpaRepository<Available, Long> {

}
