package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Enumerated(EnumType.STRING)
    private PlanStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getMyPlans().add(this);
    }

    @OneToMany(mappedBy = "plan", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Day> days = new ArrayList<>();

}