package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String homepage;

    private String tel;

    private String content;

    private String thumbnailUrl;

    @ElementCollection
    private List<String> imageUrls;

    private int liked;

    @OneToMany(mappedBy = "place", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Review> reviews;

    private String contentId;

    private String contentType;

    private String mapX;

    private String mapY;

    private String cat1;

    private String cat2;

    private String cat3;
}
