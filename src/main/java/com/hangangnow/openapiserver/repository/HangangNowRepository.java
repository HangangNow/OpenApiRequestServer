package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.hangangnow.Dust;
import com.hangangnow.openapiserver.domain.hangangnow.HangangNowData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HangangNowRepository {
    private final EntityManager em;

    public void save(HangangNowData hangangNowData){
        em.persist(hangangNowData);
    }

    public Optional<HangangNowData> findHangangNowData(){
        return Optional.ofNullable(em.find(HangangNowData.class, 1L));
    }

}
