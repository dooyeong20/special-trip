package com.project.mega.triplus.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.lang.annotation.Inherited;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ACT")
public class Activity extends Place {
    private String start;

    private String end;
}
