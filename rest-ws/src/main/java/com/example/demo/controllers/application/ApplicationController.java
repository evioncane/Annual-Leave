package com.example.demo.controllers.application;

import com.example.demo.exceptions.application.ApplicationException;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import com.example.demo.payloads.application.ApplicationEvaluationRequest;
import com.example.demo.service.application.ApplicationService;
import com.example.demo.service.dto.Application;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.example.demo.util.Constants.APPLICATION_CREATED_MESSAGE;
import static com.example.demo.util.Constants.APPLICATION_DELETED_FAILED_MESSAGE;
import static com.example.demo.util.Constants.APPLICATION_DELETED_MESSAGE;
import static com.example.demo.util.Constants.APPLICATION_EVALUATED_MESSAGE;
import static com.example.demo.util.Constants.APPLICATION_UPDATED_MESSAGE;
import static com.example.demo.util.Constants.SOMETHING_WENT_WRONG;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> getApplications(@RequestParam(required = false) String username,
                                             @RequestParam(required = false) ApplicationType type,
                                             @RequestParam(required = false) Status status,
                                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        List<Application> applicationList = this.applicationService.getAll(username, type, status, fromDate, toDate);
        return ResponseEntity.ok(applicationList);
    }

    @PostMapping("/evaluate")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> evaluateApplication(@Valid @RequestBody ApplicationEvaluationRequest request) {
        try {
            this.applicationService.evaluate(request.getId(), request.getStatus(), request.getMessage());
            return ResponseEntity.ok(APPLICATION_EVALUATED_MESSAGE);
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/personal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPersonalApplications(@RequestParam(required = false) ApplicationType type,
                                                     @RequestParam(required = false) Status status,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        List<Application> applicationList = this.applicationService.getAll(type, status, fromDate, toDate);
        return ResponseEntity.ok(applicationList);
    }

    @PutMapping("/personal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createApplication(@RequestParam ApplicationType type, @RequestParam int days) {
        try {
            this.applicationService.createUserApplication(type, days);
            return ResponseEntity.ok(APPLICATION_CREATED_MESSAGE);
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/personal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateApplication(@RequestParam long id,
                                               @RequestParam(required = false) ApplicationType type,
                                               @RequestParam(required = false) Integer days) {
        try {
            this.applicationService.updateUserApplication(id, type, days);
            return ResponseEntity.ok(APPLICATION_UPDATED_MESSAGE);
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteApplication(@NotNull @PathVariable Long id){
        try {
            this.applicationService.deleteApplication(id);
            return ResponseEntity.ok(APPLICATION_DELETED_MESSAGE);
        } catch (ApplicationException e) {
            return ResponseEntity.ok(APPLICATION_DELETED_FAILED_MESSAGE);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SOMETHING_WENT_WRONG);
        }
    }
}
