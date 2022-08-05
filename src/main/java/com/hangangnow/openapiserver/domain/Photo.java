package com.hangangnow.openapiserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "photo_category")
@NoArgsConstructor
@Getter
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    protected Long id;



    @Column(nullable = false)
    protected String url;

    protected LocalDateTime lastModifiedTime;


    public void setPhoto(String url, LocalDateTime lastModifiedTime) {
        this.url = url;
        this.lastModifiedTime = lastModifiedTime;
    }
}
