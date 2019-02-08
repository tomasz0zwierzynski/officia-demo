package com.company.application.service;

import com.company.application.domain.Available;
import com.company.application.repository.AvailableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Available.
 */
@Service
@Transactional
public class AvailableService {

    private final Logger log = LoggerFactory.getLogger(AvailableService.class);

    private final AvailableRepository availableRepository;

    public AvailableService(AvailableRepository availableRepository) {
        this.availableRepository = availableRepository;
    }

    /**
     * Save a available.
     *
     * @param available the entity to save
     * @return the persisted entity
     */
    public Available save(Available available) {
        log.debug("Request to save Available : {}", available);
        return availableRepository.save(available);
    }

    /**
     * Get all the availables.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Available> findAll() {
        log.debug("Request to get all Availables");
        return availableRepository.findAll();
    }


    /**
     * Get one available by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Available> findOne(Long id) {
        log.debug("Request to get Available : {}", id);
        return availableRepository.findById(id);
    }

    /**
     * Delete the available by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Available : {}", id);        availableRepository.deleteById(id);
    }
}
