package com.supaki.mktplace.utillity;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class Utillity {

    public static LocalDateTime getMonthBeforeTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.minusMonths(1);

    }

    public static LocalDateTime getDayBeforeTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.minusDays(1);
    }
}
