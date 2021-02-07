package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int liked;

    private LocalDateTime update;

    @OneToMany
    private List<Place> places;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
