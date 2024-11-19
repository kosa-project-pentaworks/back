package com.reservation.global.redis.lock;

import com.reservation.global.redis.client.ApplicationLockClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalReservationServiceWithLock {
    private final ApplicationLockClient applicationLockClient;
    // 락을 걸고 체크한다. 어떤식으로 내가 할 수 있는 체크를 다하고 레디스에 저장한다.

    public String getLock(String key){
        String redisKey = ApplicationLockClient.createKey(key);
        // 일단 레디스에 있는지 체크 이전 결제 데이터가 있는지
        String checkValue = applicationLockClient.findByKey(redisKey);
        if(checkValue != null){
            System.out.println("널 체크");
        }
        applicationLockClient.save(redisKey);
        String redisValue = applicationLockClient.findByKey(redisKey);
        if(!redisValue.equals("pending")) throw new RuntimeException();// 에러코드
        return redisKey;
    }
}
