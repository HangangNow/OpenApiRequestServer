package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.Park;
import com.hangangnow.openapiserver.domain.ParkPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {
    private final EntityManager em;

    public Optional<ParkPhoto> findByImageNumber(String imageNumber){
        List<ParkPhoto> parkPhotos = em.createQuery("select p from ParkPhoto p" +
                        " where p.imageNumber =:imageNumber", ParkPhoto.class)
                .setParameter("imageNumber", imageNumber)
                .getResultList();

        return parkPhotos.stream().findAny();

    }


    public void deleteAll(){
        em.createQuery("delete from ParkPhoto")
                .executeUpdate();
    }

}
