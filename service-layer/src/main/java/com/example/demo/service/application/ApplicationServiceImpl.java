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
import static java.lang.String.format;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Application> getAll(String username, ApplicationType type, Status status, Date fromDate, Date toDate) {
        return this.applicationRepository.filterAll(type, status, username, fromDate, toDate, false)
                .stream()
                .map(Application::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> getAll(ApplicationType type, Status status, Date fromDate, Date toDate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.applicationRepository.filterAll(type, status, userDetails.getUsername(), fromDate, toDate, false)
                .stream()
                .map(Application::new)
                .collect(Collectors.toList());
    }

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

    @Override
    @Transactional
    public void createUserApplication(ApplicationType type, int days) {
        UserEntity userEntity = getUserEntity();
        ApplicationEntity applicationEntity = new ApplicationEntity(type, days, Status.PENDING, new Date(), userEntity);
        this.applicationRepository.save(applicationEntity);
        // TODO: send email
    }

    @Override
    public void updateUserApplication(long id, ApplicationType type, Integer days) {
        UserEntity userEntity = getUserEntity();
        ApplicationEntity applicationEntity = this.applicationRepository.findByIdAndDeletedAndUser(id, false, userEntity)
                .orElseThrow(() -> new ApplicationException(id));
        this.applicationRepository.save(applicationEntity);
        // TODO: send email
    }

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
