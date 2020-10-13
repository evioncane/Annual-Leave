package com.example.demo.service.application;

import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import com.example.demo.service.dto.Application;

import java.util.Date;
import java.util.List;

public interface ApplicationService {
    List<Application> getAll(String username, ApplicationType type, Status status,
                             Date fromDate, Date toDate);
    List<Application> getAll(ApplicationType type, Status status, Date fromDate, Date toDate);
    void evaluate(long id, Status status, String message);
    void createUserApplication(ApplicationType type, int days);
    void updateUserApplication(long id, ApplicationType type, Integer days);
    void deleteApplication(long id);
}
