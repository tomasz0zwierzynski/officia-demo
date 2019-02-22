package com.company.application.service;

import com.company.application.domain.Available;
import com.company.application.domain.Tutor;
import com.company.application.domain.User;
import com.company.application.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AvailableUserService {

    private final Logger log = LoggerFactory.getLogger(AvailableUserService.class);

    @Autowired
    private AvailableService availableService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserService userService;

    public List<Available> getAvailablesForLoggedInTutor() {
        Optional<User> user = userService.getUserWithAuthorities();

        if (user.isPresent()) {
               log.debug("Logged user found!");

               User currentUser = user.get();
               if (currentUser.getAuthorities().stream().anyMatch(a -> a.getName().equals(AuthoritiesConstants.TUTOR))) {
                   log.debug("has authority tutor");
               }

        } else {
            log.debug("User is not logged in!");
        }


        List<Tutor> tutors = tutorService.findByUserIsCurrentUser();
        if (tutors.size() != 1) {
            log.debug("Too many or lack of tutor for logged user");
            return new ArrayList<>();
        }

        return availableService.findByTutorId(tutors.get(0).getId());
    }

}
