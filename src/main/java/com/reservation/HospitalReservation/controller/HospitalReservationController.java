package com.reservation.HospitalReservation.controller;

import com.reservation.HospitalReservation.controller.request.ReservationSaveRequest;
import com.reservation.HospitalReservation.controller.request.ReservationUpdateRequest;
import com.reservation.HospitalReservation.repository.custom.dto.FindHospitalReservationDto;
import com.reservation.HospitalReservation.service.HospitalReservationResponse.HospitalReservationPageResponse;
import com.reservation.HospitalReservation.service.HospitalReservationResponse.ReservationIsvalidAndLockResponse;
import com.reservation.HospitalReservation.service.HospitalReservationService;
import com.reservation.global.redis.client.ApplicationLockClient;
import com.reservation.global.response.CustomApiResponse;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HospitalReservationController {
    private final HospitalReservationService hospitalReservationService;
    private final ApplicationLockClient applicationLockClient;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public HospitalReservationController(HospitalReservationService hospitalReservationService, ApplicationLockClient applicationLockClient, JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.hospitalReservationService = hospitalReservationService;
        this.applicationLockClient = applicationLockClient;
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    // 예약 가능한 날짜 확인
    @GetMapping("/api/v1/hospitalreservation")
    public CustomApiResponse<List<FindHospitalReservationDto>> findAllss(
        @RequestParam("hospId") Long hospId,
        @RequestParam("reservationAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationAt
    ){
        List<FindHospitalReservationDto> hospitalReservationList = hospitalReservationService.findAllReservationByHospIdAndReservationDate(hospId, reservationAt);
        return CustomApiResponse.ok(hospitalReservationList);
    }

    // 예약 날짜 선택하고 결제 시작
    @PostMapping("/api/v1/hospitalreservation")
    public CustomApiResponse<ReservationIsvalidAndLockResponse> saveReservation(
        @RequestBody ReservationSaveRequest request
        ){
        ReservationIsvalidAndLockResponse checkReservation = hospitalReservationService.isValidReservaion(request);
        return CustomApiResponse.ok(checkReservation);
    }

    // 나의 예약목록 조회
    @GetMapping("/api/v1/hospitalreservation/list")
    public CustomApiResponse<HospitalReservationPageResponse> hospitalReservationList(
        @RequestParam(value = "type", defaultValue = "") String type,
        @RequestParam(value = "providerId", defaultValue = "") String providerId,
        @RequestParam(value = "page",defaultValue = "1") int page,
        @RequestParam(value ="size" ,defaultValue = "10") int size
    ){
        HospitalReservationPageResponse myReservationList = hospitalReservationService.findAllHospitalReservationByUserId(providerId,type, page, size);
        return CustomApiResponse.ok(myReservationList);
    }

    @PostMapping("/api/v1/hospitalreservation/update")
    public CustomApiResponse<Boolean> hospitalReservationUpdate(
        @RequestBody ReservationUpdateRequest reservationUpdateRequest
        ){
        hospitalReservationService.updateHospitalReservation(reservationUpdateRequest);
        return CustomApiResponse.ok(true);
    }

//    @GetMapping("/api/v1/data")
//    public List<FindHospitalReservationDto> fourthJob2(@RequestParam("value") String value) throws Exception {
//        System.out.println("여기 안들어옴?");
//        JobParameters jobParameters = new JobParametersBuilder()
//            .addString("date", value)
//            .toJobParameters();
//
//        jobLauncher.run(jobRegistry.getJob("hospitalReservationJob"), jobParameters);
//        return hospitalReservationService.findAllHospitalReservationByUserId(1L);
//    }

}
