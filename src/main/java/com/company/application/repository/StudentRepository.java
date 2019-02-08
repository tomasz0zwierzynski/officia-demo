package com.company.application.repository;

import com.company.application.domain.Student;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select student from Student student where student.user.login = ?#{principal.username}")
    List<Student> findByUserIsCurrentUser();

}
