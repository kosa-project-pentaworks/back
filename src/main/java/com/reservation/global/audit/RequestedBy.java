package com.reservation.global.audit;

import com.reservation.global.audit.authentication.Authentication;
import lombok.Getter;

@Getter
public class RequestedBy implements Authentication {

    private final String requestedBy;

    public RequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
