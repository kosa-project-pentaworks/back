package com.reservation.user.service.command;

import com.reservation.user.domain.Address;
import lombok.Builder;

@Builder
public record SocialUserModificationCommand(String providerId, String phone, Address address) {
}
