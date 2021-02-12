package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.lang.annotation.Inherited;
import java.time.LocalDate;

@Entity
@Getter @Setter
@DiscriminatorValue("ACT")
public class Activity extends Place {
    private String start;

    private String end;
}
