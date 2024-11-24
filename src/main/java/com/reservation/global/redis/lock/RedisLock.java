package com.reservation.global.redis.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.LockAcquisitionException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.net.Proxy;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLock {
    private final RedissonClient redissonClient;
    private final HospitalReservationServiceWithLock hospitalReservationServiceWithLock;

    public String lockReservation(Long hospId, LocalDate reservationAt, LocalTime reservationTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = reservationAt.format(formatter);
        DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = reservationTime.format(formatterT);

        String key = hospId + ":" + formattedDate + ":" + formattedTime;
        RLock lock = redissonClient.getLock(key);
        try {
            boolean available = lock.tryLock(10, 20, TimeUnit.SECONDS);
            if(!available) {
                throw new RuntimeException();
            }
            return hospitalReservationServiceWithLock.getLock(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
