package com.sumain.matching.engine.model;


import com.sumain.common.utils.DateUtils;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
@Builder
public class TimeDuration {
    private LocalTime startTime;
    private LocalTime endTime;
    private Long duration;

    public Long getDuration() {
        return Duration.between(this.startTime, this.endTime).toMillis();
    }

    public boolean isWithin(String timeStr) {
        LocalTime localTime = DateUtils.toLocalTime(timeStr);
        return isWithin(localTime);
    }

    public boolean isWithin(LocalTime time) {
        return (time.equals(startTime) || time.isAfter(startTime)) && time.isBefore(endTime);
    }


}
