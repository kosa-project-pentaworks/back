package com.reservation.HospitalReservation.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationSaveRequest {
    private Long hospId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationAt;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime reservationTime;

    public ReservationSaveRequest() {
    }

    public ReservationSaveRequest(Long hospId, LocalDate reservationAt, LocalTime reservationTime) {
        this.hospId = hospId;
        this.reservationAt = reservationAt;
        this.reservationTime = reservationTime;
    }
}
