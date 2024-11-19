package com.reservation.HospitalReservation.service.HospitalReservationResponse;

import com.reservation.HospitalReservation.repository.custom.dto.FindHospitalReservationDto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class HospitalReservationPageResponse {
    private int number; // 현재 페이지
    private int totalPage; // 전체 페이지
    private Boolean previous;
    private Boolean next;
    private List<FindHospitalReservationDto> hospitalReservationList = new ArrayList<>();

    @Builder
    public HospitalReservationPageResponse(int number, int totalPage, Boolean previous, Boolean next, List<FindHospitalReservationDto> hospitalReservationList) {
        this.number = nowPage(number);
        this.totalPage = totalPage;
        this.previous = previous;
        this.next = next;
        this.hospitalReservationList = hospitalReservationList;
    }

    public static HospitalReservationPageResponse response(int number, int totalPage, Boolean previous, Boolean next, List<FindHospitalReservationDto> hospitalReservationList ){
        return HospitalReservationPageResponse.builder()
            .number(number)
            .totalPage(totalPage)
            .previous(previous)
            .next(next)
            .hospitalReservationList(hospitalReservationList)
            .build();
    }

    public int nowPage(int number){
        return number + 1;
    }
}
