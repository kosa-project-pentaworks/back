package com.reservation.hospitals.service.response;

import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HospitalsPageResponse {
    private int number; // 현재 페이지
    private int totalPage; // 전체 페이지
    private Boolean previous;
    private Boolean next;
    private List<HospitalSearchDto> hospitals = new ArrayList<>();

    @Builder
    public HospitalsPageResponse(int number, int totalPage, Boolean previous, Boolean next, List<HospitalSearchDto> hospitals) {
        this.number = nowPage(number);
        this.totalPage = totalPage;
        this.previous = previous;
        this.next = next;
        this.hospitals = hospitals;
    }
    public int nowPage(int number){
        return number + 1;
    }
}
