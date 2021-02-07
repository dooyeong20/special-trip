package com.project.mega.triplus.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Activity extends Place {
    private LocalDate start;

    private LocalDate end;
}
