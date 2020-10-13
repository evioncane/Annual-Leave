package com.example.demo.service.application;

import com.example.demo.exceptions.application.ApplicationException;
import com.example.demo.model.ApplicationEntity;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.service.dto.Application;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.EMPTY_MESSAGE_ON_REJECTION;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> getAll(String username, ApplicationType type, Status status, Date fromDate, Date toDate) {
        return this.applicationRepository.filterAll(type, status, username, fromDate, toDate)
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
        ApplicationEntity entity = this.applicationRepository.findById(id).orElseThrow(() -> new ApplicationException(id));
        entity.setStatus(status);
        if (!messageCheck) {
            entity.setMessage(message);
        }
        this.applicationRepository.save(entity);

    }
}
