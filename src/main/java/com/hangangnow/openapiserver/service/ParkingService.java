package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.Address;
import com.hangangnow.openapiserver.domain.Local;
import com.hangangnow.openapiserver.domain.Park;
import com.hangangnow.openapiserver.domain.Parking;
import com.hangangnow.openapiserver.domain.dto.ParkingRequestDto;
import com.hangangnow.openapiserver.repository.ParkRepository;
import com.hangangnow.openapiserver.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class ParkingService {
    private static String URL = "https://www.ihangangpark.kr/parking/region/";
    private static String region = "region";

    private final ParkRepository parkRepository;
    private final ParkingRepository parkingRepository;

    @Scheduled(cron = "0 0/3 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void requestParking() throws IOException {
        log.info("한강공원 주차장 크롤러 동작.");

        // 광나루, 잠실, 뚝섬, 잠원, 반포, 이촌, 망원, 여의도, 난지, 강서, 양화
        String[] contentIds = {"250252", "970460", "1030763", "970342", "970138", "970636",
                "1059638", "1059479", "127859", "2733968", "1059877"};

        String[] parkNames = {"광나루한강공원", "잠실한강공원", "뚝섬한강공원", "잠원한강공원", "반포한강공원", "이촌한강공원",
                "망원한강공원", "여의도한강공원", "난지한강공원", "강서한강공원", "양화한강공원"};

        List<ParkingRequestDto> parkingRequestDtos = new ArrayList<>();

        for(int idx=1; idx<12; idx++)
        {
            Document doc = Jsoup.connect(URL+region+idx).get();
            Elements eles = doc.select(".region-tab-cont.active .table.board.mt10 tbody tr td");

            for(int i=0; i<eles.size(); i+=6)
            {
                String name = Jsoup.parse(eles.get(i).toString()).text();
                String address = Jsoup.parse(eles.get(i+1).toString()).text();
                String[] addresses = address.split("\\s");
                String si = addresses[0];
                String gu = addresses[1];
                String detail = "";

                for(int j=2; j< addresses.length; j++){
                    detail += addresses[j] + " ";
                }

                Integer avail = Integer.parseInt(Jsoup.parse(eles.get(i+3).toString()).text());
                Integer total = Integer.parseInt(Jsoup.parse(eles.get(i+4).toString()).text());

                ParkingRequestDto parkingRequestDto = new ParkingRequestDto(contentIds[idx-1], parkNames[idx-1], si, gu, detail, name, avail, total);
                parkingRequestDtos.add(parkingRequestDto);
            }
        }
        updateParkingInfo(parkingRequestDtos);
    }



    public void createParkingInfo(List<ParkingRequestDto> parkingRequestDtos){
        for (ParkingRequestDto parkingRequestDto : parkingRequestDtos) {
            Park findPark = parkRepository.findByContentId(parkingRequestDto.getContentId());

            Address address = new Address(parkingRequestDto.getSi(), parkingRequestDto.getGu(), parkingRequestDto.getDetail());
            Local local = new Local(parkingRequestDto.getParkingName(), null, null);

            Parking parking = new Parking(parkingRequestDto.getParkingName(), address, local, parkingRequestDto.getTotalCount(), parkingRequestDto.getAvailableCount(), LocalDateTime.now());
            findPark.addParking(parking);
        }
    }



    public void updateParkingInfo(List<ParkingRequestDto> parkingRequestDtos){
        for (ParkingRequestDto parkingRequestDto : parkingRequestDtos) {
            Parking findParking = parkingRepository.findByName(parkingRequestDto.getParkingName());
            Address address = new Address(parkingRequestDto.getSi(), parkingRequestDto.getGu(), parkingRequestDto.getDetail());
            Local local = new Local(parkingRequestDto.getParkingName(), null, null);
            findParking.updateParking(parkingRequestDto.getParkingName(), address, local, parkingRequestDto.getTotalCount(), parkingRequestDto.getAvailableCount(), LocalDateTime.now());
        }
    }
}
