package com.company.application.repository;

import com.company.application.domain.Available;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Available entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableRepository extends JpaRepository<Available, Long> {

   @Query("select a from Available a where a.tutor.id = :tutorId")
   List<Available> findByTutorId(@Param("tutorId") Long tutorId);

}
