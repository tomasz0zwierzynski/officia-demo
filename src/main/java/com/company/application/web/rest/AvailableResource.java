package com.company.application.web.rest;
import com.company.application.domain.Available;
import com.company.application.service.AvailableService;
import com.company.application.web.rest.errors.BadRequestAlertException;
import com.company.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Available.
 */
@RestController
@RequestMapping("/api")
public class AvailableResource {

    private final Logger log = LoggerFactory.getLogger(AvailableResource.class);

    private static final String ENTITY_NAME = "available";

    private final AvailableService availableService;

    public AvailableResource(AvailableService availableService) {
        this.availableService = availableService;
    }

    /**
     * POST  /availables : Create a new available.
     *
     * @param available the available to create
     * @return the ResponseEntity with status 201 (Created) and with body the new available, or with status 400 (Bad Request) if the available has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/availables")
    public ResponseEntity<Available> createAvailable(@Valid @RequestBody Available available) throws URISyntaxException {
        log.debug("REST request to save Available : {}", available);
        if (available.getId() != null) {
            throw new BadRequestAlertException("A new available cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Available result = availableService.save(available);
        return ResponseEntity.created(new URI("/api/availables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /availables : Updates an existing available.
     *
     * @param available the available to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated available,
     * or with status 400 (Bad Request) if the available is not valid,
     * or with status 500 (Internal Server Error) if the available couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/availables")
    public ResponseEntity<Available> updateAvailable(@Valid @RequestBody Available available) throws URISyntaxException {
        log.debug("REST request to update Available : {}", available);
        if (available.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Available result = availableService.save(available);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, available.getId().toString()))
            .body(result);
    }

    /**
     * GET  /availables : get all the availables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of availables in body
     */
    @GetMapping("/availables")
    public List<Available> getAllAvailables() {
        log.debug("REST request to get all Availables");
        return availableService.findAll();
    }

    /**
     * GET  /availables/:id : get the "id" available.
     *
     * @param id the id of the available to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the available, or with status 404 (Not Found)
     */
    @GetMapping("/availables/{id}")
    public ResponseEntity<Available> getAvailable(@PathVariable Long id) {
        log.debug("REST request to get Available : {}", id);
        Optional<Available> available = availableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(available);
    }

    /**
     * DELETE  /availables/:id : delete the "id" available.
     *
     * @param id the id of the available to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/availables/{id}")
    public ResponseEntity<Void> deleteAvailable(@PathVariable Long id) {
        log.debug("REST request to delete Available : {}", id);
        availableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
