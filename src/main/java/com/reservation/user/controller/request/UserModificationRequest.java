package com.reservation.user.controller.request;

import com.reservation.user.domain.Address;
import lombok.Getter;

@Getter
public class UserModificationRequest {

    private final String phone;
    private final Address address;

    public UserModificationRequest(String phone, Address address) {
        this.phone = phone;
        this.address = address;
    }
}
