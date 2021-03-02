package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Place> places = new ArrayList<>();

    private String placeImg;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public void setPlan(Plan plan) {
        this.plan = plan;
        plan.getDays().add(this);
        plan.setDayCounts(plan.getDayCounts()+1);
    }

    public void addPlace(Place place){
        places.add(place);
        placeImg = place.getThumbnailUrl();
    }
}