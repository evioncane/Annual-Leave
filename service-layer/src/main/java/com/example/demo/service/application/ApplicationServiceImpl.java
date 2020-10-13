package com.example.demo.service.application;

import com.example.demo.exceptions.application.ApplicationException;
import com.example.demo.model.ApplicationEntity;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.services.user.UserDetailsImpl;
import com.example.demo.service.dto.Application;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.APPLICATION_DELETED_FAILED_MESSAGE;
import static com.example.demo.util.Constants.EMPTY_MESSAGE_ON_REJECTION;
import static com.example.demo.util.Constants.REGISTRATION_DAYS_EVALUATION_MESSAGE;
import static java.lang.String.format;

/**
 * Contains all logic behind application API's
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final int MIN_NUMBER_OF_DAYS = 90;

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lists all applications which match the parameters below. If the parameters are null they are ignored.
     *
     * @param username unique username
     * @param type type of application (VACATION, COMPENSATION)
     * @param status status of application (APPROVED, REJECTED, PENDING)
     * @param fromDate date to start the search
     * @param toDate date to finish the search
     * @return filtered list
     */
    @Override
    public List<Application> getAll(String username, ApplicationType type, Status status, Date fromDate, Date toDate) {
        return this.applicationRepository.filterAll(type, status, username, fromDate, toDate, false)
                .stream()
                .map(Application::new)
                .collect(Collectors.toList());
    }

    /**
     * Lists all applications which match the parameters below. If the parameters are null they are ignored.
     *
     * @param type type of application (VACATION, COMPENSATION)
     * @param status status of application (APPROVED, REJECTED, PENDING)
     * @param fromDate date to start the search
     * @param toDate date to finish the search
     * @return filtered list
     */
    @Override
    public List<Application> getAll(ApplicationType type, Status status, Date fromDate, Date toDate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.applicationRepository.filterAll(type, status, userDetails.getUsername(), fromDate, toDate, false)
                .stream()
                .map(Application::new)
                .collect(Collectors.toList());
    }

    /**
     * Used by the supervisor to evaluate users applications
     *
     * @param id unique identification of application
     * @param status status of application (APPROVED, REJECTED, PENDING)
     * @param message message supervisor left for user
     */
    @Override
    public void evaluate(long id, Status status, String message) {
        boolean messageCheck = message == null || message.isEmpty();
        if (status.equals(Status.REJECTED) && messageCheck) {
            throw new ApplicationException(EMPTY_MESSAGE_ON_REJECTION);
        }
        ApplicationEntity entity = this.applicationRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new ApplicationException(id));
        entity.setStatus(status);
        if (!messageCheck) {
            entity.setMessage(message);
        }
        this.applicationRepository.save(entity);
        // TODO: send email
    }

    /**
     * Creates an application for the logged user. Parameters are required.
     *
     * @param type type of application (VACATION, COMPENSATION)
     * @param days number of days user is applying for
     */
    @Override
    @Transactional
    public void createUserApplication(ApplicationType type, int days) {
        UserEntity userEntity = getUserEntity();
        if (!canUserApply(userEntity.getStartDate())) {
            throw new ApplicationException(REGISTRATION_DAYS_EVALUATION_MESSAGE);
        }
        ApplicationEntity applicationEntity = new ApplicationEntity(type, days, Status.PENDING, new Date(), userEntity);
        this.applicationRepository.save(applicationEntity);
        // TODO: send email
    }

    /**
     * Checks if enough days have passed for user to apply
     * @param registrationDate date the current user is registerd
     * @return true if enough days have passed, else false
     */
    private boolean canUserApply(Date registrationDate) {
        int diffInDays = (int)( ((new Date()).getTime() - registrationDate.getTime())
                / (1000 * 60 * 60 * 24) );
        return diffInDays >= MIN_NUMBER_OF_DAYS;
    }

    /**
     * Updates an application for the logged user
     *
     * @param id unique identification of application
     * @param type type of application (VACATION, COMPENSATION)
     * @param days number of days user is applying for
     */
    @Override
    public void updateUserApplication(long id, ApplicationType type, Integer days) {
        UserEntity userEntity = getUserEntity();
        ApplicationEntity applicationEntity = this.applicationRepository.findByIdAndDeletedAndUser(id, false, userEntity)
                .orElseThrow(() -> new ApplicationException(id));
        this.applicationRepository.save(applicationEntity);
        // TODO: send email
    }

    /**
     * Deletes an application of the logged user
     *
     * @param id unique identification of application
     */
    @Override
    public void deleteApplication(long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long deletionSize = this.applicationRepository.deleteById(id, false, userDetails.getUsername());
        if (deletionSize <= 0) {
            throw new ApplicationException(APPLICATION_DELETED_FAILED_MESSAGE);
        }
        // TODO: send email
    }

    private UserEntity getUserEntity() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException(format("User %s not found!", userDetails.getId())));
    }
}
