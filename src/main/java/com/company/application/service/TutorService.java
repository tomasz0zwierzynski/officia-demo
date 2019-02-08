package com.company.application.service;

import com.company.application.domain.Tutor;
import com.company.application.repository.TutorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Tutor.
 */
@Service
@Transactional
public class TutorService {

    private final Logger log = LoggerFactory.getLogger(TutorService.class);

    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    /**
     * Save a tutor.
     *
     * @param tutor the entity to save
     * @return the persisted entity
     */
    public Tutor save(Tutor tutor) {
        log.debug("Request to save Tutor : {}", tutor);
        return tutorRepository.save(tutor);
    }

    /**
     * Get all the tutors.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Tutor> findAll() {
        log.debug("Request to get all Tutors");
        return tutorRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Tutor with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Tutor> findAllWithEagerRelationships(Pageable pageable) {
        return tutorRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one tutor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Tutor> findOne(Long id) {
        log.debug("Request to get Tutor : {}", id);
        return tutorRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the tutor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tutor : {}", id);        tutorRepository.deleteById(id);
    }
}
