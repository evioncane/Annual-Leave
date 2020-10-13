package com.example.demo.payloads.application;

import com.example.demo.model.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ApplicationEvaluationRequest {

    @NotNull
    private Long id;

    private Status status;

    private String message;
}
