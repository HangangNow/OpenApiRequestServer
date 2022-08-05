package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.Parking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingRepository {

    private final EntityManager em;


    public void save(Parking parking){
        em.persist(parking);
    }


    public Parking findOne(Long id){
        return em.find(Parking.class, id);
    }

    public Parking findByName(String name){
        return em.createQuery("select p from Parking p where p.name =:name", Parking.class)
                .setParameter("name", name)
                .getResultList()
                .get(0);
    }


}
