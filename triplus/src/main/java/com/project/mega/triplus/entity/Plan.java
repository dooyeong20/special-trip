package com.project.mega.triplus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private User user;


    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Day> days = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        user.getMyPlans().add(this);
    }

    private int dayCounts;

    public int getDayCounts(){
        return days.size();
    }

    private String mainImg;

    public String getMainImg(){
        return days.get(0).getPlaces().get(0).getThumbnailUrl();
    }

    private String mainAreaCode;

    public String getMainAreaCode(){
        return days.get(0).getPlaces().get(0).getAreaCode();
    }
}
