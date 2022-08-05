package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.Park;
import com.hangangnow.openapiserver.domain.ParkPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {
    private final EntityManager em;

    public List<ParkPhoto> findByParkId(Park park){
        return em.createQuery("select p from ParkPhoto p join fetch p.park" +
                        " where p.park =:park", ParkPhoto.class)
                .setParameter("park", park)
                .getResultList();
    }

}
