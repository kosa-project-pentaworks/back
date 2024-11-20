package com.reservation.user.repository.request;

import com.reservation.user.domain.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateSocialUser {

    private final String providerId;
    private final String phone;
    private final Address address;

    public UpdateSocialUser(String providerId, String phone, Address address) {
        this.providerId = providerId;
        this.phone = phone;
        this.address = address;
    }
}
