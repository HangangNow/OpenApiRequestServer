package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.*;
import com.hangangnow.openapiserver.domain.dto.ParkPhotoRequestDto;
import com.hangangnow.openapiserver.domain.dto.ParkRequestDto;
import com.hangangnow.openapiserver.domain.dto.ParkingRequestDto;
import com.hangangnow.openapiserver.repository.ParkRepository;
import com.hangangnow.openapiserver.repository.ParkingRepository;
import com.hangangnow.openapiserver.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParkService {

    private final ParkRepository parkRepository;
    private final PhotoRepository photoRepository;
    private final ParkingRepository parkingRepository;

    @Transactional
    public void createPark(List<ParkRequestDto> parkRequestDtos){
        for (ParkRequestDto parkRequestDto : parkRequestDtos) {
            Local local = new Local(parkRequestDto.getParkName(), parkRequestDto.getX_pos(), parkRequestDto.getY_pos());
            Address address = new Address(parkRequestDto.getSi(), parkRequestDto.getGu(), parkRequestDto.getGil(), parkRequestDto.getDetail());

            Park park = new Park(parkRequestDto.getParkId(), parkRequestDto.getParkName(), local, address, parkRequestDto.getContent(), parkRequestDto.getLastModifiedTime());
            parkRepository.save(park);
        }
    }


    @Transactional
    public void updateParkInfo(List<ParkRequestDto> parkRequestDtos){
        for (ParkRequestDto parkRequestDto : parkRequestDtos) {
            Park findPark = parkRepository.findById(parkRequestDto.getParkId());
            Local local = new Local(parkRequestDto.getParkName(), parkRequestDto.getX_pos(), parkRequestDto.getY_pos());
            Address address = new Address(parkRequestDto.getSi(), parkRequestDto.getGu(), parkRequestDto.getGil(), parkRequestDto.getDetail());
            findPark.updatePark(parkRequestDto.getParkName(), local, address, parkRequestDto.getContent(), parkRequestDto.getLastModifiedTime());
        }
    }


    @Transactional
    public void createParkPhoto(List<ParkPhotoRequestDto> parkPhotoRequestDtos){
        for (ParkPhotoRequestDto parkPhotoRequestDto : parkPhotoRequestDtos) {
            Park findPark = parkRepository.findById(parkPhotoRequestDto.getParkId());

            HashMap<String, String> hashMap = parkPhotoRequestDto.getHashMap();
            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                ParkPhoto parkPhoto = new ParkPhoto(stringStringEntry.getKey(), stringStringEntry.getValue(), LocalDateTime.now());
                findPark.addPhoto(parkPhoto);
            }
        }
    }



    @Transactional
    public void createParkingInfo(List<ParkingRequestDto> parkingRequestDtos){
        for (ParkingRequestDto parkingRequestDto : parkingRequestDtos) {
            Park findPark = parkRepository.findById(parkingRequestDto.getParkId());

            Address address = new Address(parkingRequestDto.getSi(), parkingRequestDto.getGu(), parkingRequestDto.getGil(), parkingRequestDto.getDetail());
            Local local = new Local(parkingRequestDto.getParkingName(), null, null);

            Parking parking = new Parking(parkingRequestDto.getParkingName(), address, local, parkingRequestDto.getTotalCount(), parkingRequestDto.getAvailableCount(), LocalDateTime.now());
            findPark.addParking(parking);
        }
    }


    @Transactional
    public void updateParkingInfo(List<ParkingRequestDto> parkingRequestDtos){
        for (ParkingRequestDto parkingRequestDto : parkingRequestDtos) {
            Parking findParking = parkingRepository.findByName(parkingRequestDto.getParkingName());
            Address address = new Address(parkingRequestDto.getSi(), parkingRequestDto.getGu(), parkingRequestDto.getGil(), parkingRequestDto.getDetail());
            Local local = new Local(parkingRequestDto.getParkingName(), null, null);
            findParking.updateParking(parkingRequestDto.getParkingName(), address, local, parkingRequestDto.getTotalCount(), parkingRequestDto.getAvailableCount(), LocalDateTime.now());
        }
    }
}
