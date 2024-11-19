package com.reservation.global.redis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ApplicationLockClient {
    private final RedisOperations<String, String> redis;
    private final Duration expiredTime = Duration.ofMinutes(3L);

    public static String createKey(String key){
        return "hospId:"+key;
    }

    public void save(String key){
        redis.opsForValue().set(key, "pending", expiredTime);
    }

    public void remove(String key){
        redis.delete(key);
    }

    public String findByKey(String key){
        return redis.opsForValue().get(key);
    }

    public Boolean isvalidRedisKey(Long hospId, LocalDate reservationAt, LocalTime reservationTime){
        String redisKey = createKey(hospId, reservationAt, reservationTime);
        return findByKey(createKey(redisKey)) == null;
    }

    public String createKey(Long hospId, LocalDate reservationAt, LocalTime reservationTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = reservationAt.format(formatter);
        String formattedTime = reservationTime.format(formatterT);
        return hospId + ":" + formattedDate + ":" + formattedTime;
    }



}
