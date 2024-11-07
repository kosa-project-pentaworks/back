package com.reservation.hospitals.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hospitaldata", url = "http://apis.data.go.kr/B551182/spclMdlrtHospInfoService1")
public interface HospitalDataClient {
    final String ServiceKey = "qSSreNvhgqXTKW%2FALIPK%2FDyuZSuKCqXI1AjmzCL7wNDMC3YMXaxGQcpxLaO%2BuC4Td2IL2ywb2hxhl%2BdRREyCDQ%3D%3D";
    @GetMapping("/getRcperHospList1?ServiceKey=" + ServiceKey)
    String getCount();
    @GetMapping("/getRcperHospList1?ServiceKey=" + ServiceKey)
    String getAllData(
        @RequestParam(value = "numOfRows") int count
    );
}
