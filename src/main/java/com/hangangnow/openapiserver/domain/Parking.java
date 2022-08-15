package com.hangangnow.openapiserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private Local local;

    private int total_count;

    private int available_count;

    private LocalDateTime lastModifiedTime;


    public void setPark(Park park){
        this.park = park;
    }

    public Parking( String name, Address address, Local local, int total_count, int available_count, LocalDateTime lastModifiedTime) {
        this.name = name;
        this.address = address;
        this.local = local;
        this.total_count = total_count;
        this.available_count = available_count;
        this.lastModifiedTime = lastModifiedTime;
    }


    public void updateParking(String name, Address address, int total_count, int available_count, LocalDateTime lastModifiedTime){
        this.name = name;
        this.address = address;
        this.total_count = total_count;
        this.available_count = available_count;
        this.lastModifiedTime = lastModifiedTime;
    }

}
