package com.project.mega.triplus.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ACC")
public class Accomm extends Place{
    private int price;
}
