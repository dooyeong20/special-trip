package com.project.mega.triplus.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Restaurant extends Place {
    // 메뉴가 있어야 할 듯 ...
    private int price;

    private LocalDate start;

    private LocalDate end;
}
