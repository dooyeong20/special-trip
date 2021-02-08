package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int star;

    private String name;

    private String content;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public void setPlace(Place place) {
        this.place = place;
        place.getReviews().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }
}