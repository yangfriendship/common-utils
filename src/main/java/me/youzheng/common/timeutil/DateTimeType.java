package me.youzheng.common.timeutil;

import java.time.LocalDateTime;
import java.util.function.Function;

public enum DateTimeType {
    YEAR((time) ->
            LocalDateTime.of(time.getYear(), 1, 1, 0, 0, 0, 0),
            ((time) ->
                    LocalDateTime.of(time.getYear() + 1, 1, 1, 0, 0).minusSeconds(1))),
    MONTH(time ->
            LocalDateTime.of(time.getYear(), time.getMonth(), 1, 0, 0, 0, 0),
            ((time) ->
                    LocalDateTime.of(time.getYear(), time.getMonth().getValue() + 1, 1, 0, 0).minusSeconds(1))),
    DAYS(time ->
            LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 0, 0, 0, 0),
            ((time) ->
                    LocalDateTime.of(time.getYear(), time.getMonth().getValue(), time.getDayOfMonth() + 1, 0, 0).minusSeconds(1))),
    HOUR(time ->
            LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), 0, 0, 0),
            ((time) ->
                    LocalDateTime.of(time.getYear(), time.getMonth().getValue(), time.getDayOfMonth(), time.getHour(), 0).minusSeconds(1))),
    MIN(time ->
            LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute(), 0, 0),
            ((time) ->
                    LocalDateTime.of(time.getYear(), time.getMonth().getValue(), time.getDayOfMonth() + 1, 0, 0).minusSeconds(1))),
    SECONDS(time ->
            LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond(), 0),
            ((time) ->
                    LocalDateTime.of(time.getYear(), time.getMonth().getValue(), time.getDayOfMonth() + 1, 0, 0).minusSeconds(1)));

    final Function<LocalDateTime, LocalDateTime> down;
    final Function<LocalDateTime, LocalDateTime> up;

    DateTimeType(final Function<LocalDateTime, LocalDateTime> down, final Function<LocalDateTime, LocalDateTime> up) {
        this.down = down;
        this.up = up;
    }
}