package com.example.demo.service.dto;

import com.example.demo.model.ApplicationEntity;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Application {

    private long id;

    private ApplicationType type;

    private int days;

    private Status status;

    private String message;

    private Date date;

    private String username;

    public Application(ApplicationEntity entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.days = entity.getDays();
        this.status = entity.getStatus();
        this.message = entity.getMessage();
        this.date = entity.getDate();
        this.username = entity.getUser().getUsername();
    }
}
