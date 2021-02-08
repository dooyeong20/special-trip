package com.project.mega.triplus.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("RES")
public class Restaurant extends Place {
    private int price;
}
