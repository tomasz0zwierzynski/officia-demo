package com.company.application.web.rest;
import com.company.application.domain.Tutor;
import com.company.application.service.TutorService;
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
 * REST controller for managing Tutor.
 */
@RestController
@RequestMapping("/api")
public class TutorResource {

    private final Logger log = LoggerFactory.getLogger(TutorResource.class);

    private static final String ENTITY_NAME = "tutor";

    private final TutorService tutorService;

    public TutorResource(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    /**
     * POST  /tutors : Create a new tutor.
     *
     * @param tutor the tutor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tutor, or with status 400 (Bad Request) if the tutor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tutors")
    public ResponseEntity<Tutor> createTutor(@Valid @RequestBody Tutor tutor) throws URISyntaxException {
        log.debug("REST request to save Tutor : {}", tutor);
        if (tutor.getId() != null) {
            throw new BadRequestAlertException("A new tutor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tutor result = tutorService.save(tutor);
        return ResponseEntity.created(new URI("/api/tutors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tutors : Updates an existing tutor.
     *
     * @param tutor the tutor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tutor,
     * or with status 400 (Bad Request) if the tutor is not valid,
     * or with status 500 (Internal Server Error) if the tutor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tutors")
    public ResponseEntity<Tutor> updateTutor(@Valid @RequestBody Tutor tutor) throws URISyntaxException {
        log.debug("REST request to update Tutor : {}", tutor);
        if (tutor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tutor result = tutorService.save(tutor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tutor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tutors : get all the tutors.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of tutors in body
     */
    @GetMapping("/tutors")
    public List<Tutor> getAllTutors(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Tutors");
        return tutorService.findAll();
    }

    /**
     * GET  /tutors/:id : get the "id" tutor.
     *
     * @param id the id of the tutor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tutor, or with status 404 (Not Found)
     */
    @GetMapping("/tutors/{id}")
    public ResponseEntity<Tutor> getTutor(@PathVariable Long id) {
        log.debug("REST request to get Tutor : {}", id);
        Optional<Tutor> tutor = tutorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tutor);
    }

    /**
     * DELETE  /tutors/:id : delete the "id" tutor.
     *
     * @param id the id of the tutor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tutors/{id}")
    public ResponseEntity<Void> deleteTutor(@PathVariable Long id) {
        log.debug("REST request to delete Tutor : {}", id);
        tutorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
