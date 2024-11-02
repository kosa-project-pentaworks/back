package com.reservation.global.audit;

import java.util.Optional;

public interface RequestedByProvider {

    Optional<String> getRequestedBy();
}
