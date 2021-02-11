package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ACC")
@Getter @Setter
public class Accomm extends Place{
    private int price;
}
