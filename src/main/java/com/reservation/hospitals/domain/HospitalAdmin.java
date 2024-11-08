package com.reservation.hospitals.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class HospitalAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "hosp_id")
    private HospitalEntity hospitalEntity;
}
