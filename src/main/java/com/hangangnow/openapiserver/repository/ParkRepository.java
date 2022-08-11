package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkRepository {

    private final EntityManager em;

    public void save(Park park){
        em.persist(park);
    }

    public Park findByContentId(String contentId){
        return em.createQuery("select p from Park p where p.contentId =:contentId", Park.class)
                .setParameter("contentId", contentId)
                .getSingleResult();
    }

    public List<Park> findAll(){
        return em.createQuery("select p from Park p", Park.class).getResultList();
    }
}
