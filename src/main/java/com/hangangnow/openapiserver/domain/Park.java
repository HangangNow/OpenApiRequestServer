package com.hangangnow.openapiserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "park_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String contentId;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Local local;

    @Embedded
    private Address address;

    private String callNumber;

    private String facilities;

    private String funItems;

    private String summary;

    private String content;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkPhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parking> parkings = new ArrayList<>();

    private LocalDateTime lastModifiedTime;

    public Park(String contentId, String name, Local local, Address address, String content, LocalDateTime lastModifiedTime) {
        this.contentId = contentId;
        this.name = name;
        this.local = local;
        this.address = address;
        this.content = content;
        this.lastModifiedTime = lastModifiedTime;
    }


    public void addPhoto(ParkPhoto parkPhoto){
        photos.add(parkPhoto);
        parkPhoto.setPark(this);
    }


    public void addParking(Parking parking){
        parkings.add(parking);
        parking.setPark(this);
    }


    public void updatePark(String name, Local local, Address address, String content, LocalDateTime lastModifiedTime){
        this.name = name;
        this.local = local;
        this.address = address;
        this.content = content;
        this.lastModifiedTime = lastModifiedTime;
    }


    public void updatePhoto(ParkPhoto parkPhoto){

    }

}
