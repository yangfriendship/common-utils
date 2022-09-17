package me.youzheng.common.timeutil;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static me.youzheng.common.timeutil.TimeUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilTest {

    LocalDateTime source = LocalDateTime.of(2022, 9, 1, 12, 13);


    @Test
    public void 시간확인테스트() {

        assertTrue(match(source, isSameHour(
                LocalDateTime.of(2022, 10, 2, 12, 13))));
        assertTrue(match(source, isSameHour(
                LocalDateTime.of(2023, 11, 2, 12, 13))));
        assertTrue(match(source, isSameHour(
                LocalDateTime.of(2023, 11, 2, 12, 14))));

        assertTrue(match(source, isSameHour(
                LocalDateTime.of(2023, 11, 2, 12, 14))));
    }

    @Test
    public void 연도확인테스트() {
        assertFalse(TimeUtil.match(source, isSameYear(source.plusYears(1))));
        assertFalse(TimeUtil.match(source, isSameYear(source.minusYears(1))));
        assertFalse(TimeUtil.match(source,
                isSameYear(LocalDateTime.of(2021, 12, 31, 23, 59, 59, 0))));
        assertFalse(TimeUtil.match(source,
                isSameYear(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0))));

        assertTrue(TimeUtil.match(source, isSameYear(source.minusDays(1))));
        assertTrue(TimeUtil.match(source,
                isSameYear(LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0))));
        assertTrue(TimeUtil.match(source,
                isSameYear(LocalDateTime.of(2022, 12, 31, 23, 59, 59, 0))));
    }

    @Test
    public void 월확인테스트() {
        assertFalse(TimeUtil.match(source, isSameYear(source.plusYears(1))));
        assertFalse(TimeUtil.match(source, isSameYear(source.minusYears(1))));
    }


}