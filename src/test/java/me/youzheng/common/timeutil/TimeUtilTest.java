package me.youzheng.common.timeutil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static me.youzheng.common.timeutil.TimeUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilTest {

    LocalDateTime source = LocalDateTime.of(2022, 9, 1, 12, 13);


    @Test
    public void 시간확인테스트() {

        // 같은 시간 시작
        assertTrue(match(source, isSameHour(source.withMinute(0).withSecond(0))));
        // 같은 시간 마지막
        assertTrue(match(source, isSameHour(source.withMinute(59).withSecond(59))));

        // 한시간전 마지막
        assertFalse(match(source, isSameHour(source.minusHours(1).withMinute(59).withSecond(59))));
        // 한시간후 시작
        assertFalse(match(source, isSameHour(source.plusHours(1).withMinute(0).withSecond(0))));
        // 년도만 다름
        assertFalse(match(source, isSameHour(source.plusYears(1))));
    }

    @Test
    public void 년도확인테스트() {
        // 1월 1일
        assertTrue(TimeUtil.match(source, isSameYear(LocalDateTime.of(2022, 1, 1, 0, 0, 0))));
        // 12월 31일
        assertTrue(TimeUtil.match(source, isSameYear(LocalDateTime.of(2022, 12, 31, 23, 59, 59))));

        // 작년 마지막
        assertFalse(TimeUtil.match(source, isSameYear(LocalDateTime.of(2021, 12, 31, 23, 59, 59))));
        // 내년 시작
        assertFalse(TimeUtil.match(source, isSameYear(LocalDateTime.of(2023, 1, 1, 0, 0, 0))));
    }

    @Test
    public void 월확인테스트() {
        // 이번달 시작
        assertTrue(TimeUtil.match(source, isSameMonth(LocalDateTime.of(2022, 9, 1, 0, 0, 0))));
        // 이번달 마지막
        assertTrue(TimeUtil.match(source, isSameMonth(LocalDateTime.of(2022, 9, 30, 23, 59, 59))));

        // 다음달 시작
        assertFalse(TimeUtil.match(source, isSameMonth(LocalDateTime.of(2022, 10, 1, 0, 0, 0))));
        // 이전달 마지막
        assertFalse(TimeUtil.match(source, isSameMonth(LocalDateTime.of(2022, 8, 31, 23, 59, 59))));
    }

    @Test
    public void 날짜확인테스트() {
        // 당일 처음
        assertTrue(TimeUtil.match(source, isSameDays(LocalDateTime.of(2022, 9, 1, 0, 0))),
                "당일 시작 00:00:00");
        // 당일 마지막
        assertTrue(TimeUtil.match(source, isSameDays(LocalDateTime.of(2022, 9, 1, 23, 59, 59))),
                "당일 마지막 23:59:59");
        // 다음날 시작
        assertFalse(TimeUtil.match(source, isSameDays(LocalDateTime.of(2022, 9, 2, 0, 0))),
                "다음달 시작, + 1day 00:00:00");
        // 전날 마지막
        assertFalse(TimeUtil.match(source, isSameDays(LocalDateTime.of(2022, 8, 30, 23, 59, 59))),
                "전달 마지막, - 1day 23:50:50");
        // 년도가 다름
        assertFalse(TimeUtil.match(source, isSameDays(source.withYear(source.getYear() + 1))));
    }

    @Test
    public void 요일확인테스트() {
        final LocalDateTime startOfWeek = LocalDateTime.of(2022, 8, 28, 0, 0, 0);
        final LocalDateTime endOfWeek = LocalDateTime.of(2022, 9, 3, 0, 0, 0);

        final LocalDateTime startOfNextWeek = startOfWeek.plusWeeks(1);
        final LocalDateTime endOfLastWeek = startOfWeek.minusWeeks(1);

        // 같은주 시작
        assertTrue(TimeUtil.match(source, isSameWeek(startOfWeek)));
        // 같은주 마지막
        assertTrue(TimeUtil.match(source, isSameWeek(endOfWeek)));

        // 다음주 시작
        assertFalse(TimeUtil.match(source, isSameWeek(startOfNextWeek)));
        // 이전주 마지막
        assertFalse(TimeUtil.match(source, isSameWeek(endOfLastWeek)));

        // 다음해 같은날
        assertFalse(TimeUtil.match(source, isSameWeek(source.plusYears(1))));
    }

    @Test
    public void 분확인테스트() {
        LocalDateTime startOfMinute = source.withSecond(0);
        LocalDateTime endOfMinute = source.withSecond(59);

        LocalDateTime plusOneMinute = source.withMinute(source.getMinute() + 1).withSecond(0);
        LocalDateTime minusOneMinute = source.withMinute(source.getMinute() - 1).withSecond(59);

        assertTrue(TimeUtil.match(source, isSameMin(startOfMinute)));
        assertTrue(TimeUtil.match(source, isSameMin(endOfMinute)));

        assertFalse(TimeUtil.match(source, isSameMin(plusOneMinute)));
        assertFalse(TimeUtil.match(source, isSameMin(minusOneMinute)));
    }

    @ParameterizedTest
    @MethodSource("isBetweenTestArguments")
    public void isBetweenTest(LocalDateTime start, LocalDateTime end, List<LocalDateTime> isTrues, List<LocalDateTime> isFalses) {

        // true test
        for (LocalDateTime localDateTime : isTrues) {
            assertTrue(TimeUtil.match(localDateTime, isBetween(start, end)));
        }
        // false test
        for (LocalDateTime localDateTime : isFalses) {
            assertFalse(TimeUtil.match(localDateTime, isBetween(start, end)));
        }
    }

    private static Stream<Arguments> isBetweenTestArguments() { // argument source method
        return Stream.of(
                Arguments.of( // 년도 범위
                        LocalDateTime.of(2022, 1, 1, 0, 0, 0), // from
                        LocalDateTime.of(2022, 12, 31, 23, 58, 59), // end
                        Arrays.asList(LocalDateTime.of(2022, 12, 31, 23, 58, 58), // test1
                                LocalDateTime.of(2022, 1, 1, 0, 0, 1)    // test2)
                        ),
                        Arrays.asList(
                                LocalDateTime.of(2023,  1, 1, 0, 0, 0), // from
                                LocalDateTime.of(2021, 12, 31, 23, 58, 59) // end
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2022, 9, 1, 0, 0, 0), // from
                        LocalDateTime.of(2022, 9, 30, 23, 58, 59), // end
                        Arrays.asList(
                                LocalDateTime.of(2022, 9, 30, 23, 58, 58), // test1
                                LocalDateTime.of(2022, 9, 1, 0, 0, 1)    // test2)
                        ),
                        Arrays.asList(
                                LocalDateTime.of(2022, 8, 31, 23, 58, 58), // test1
                                LocalDateTime.of(2022, 10, 1, 0, 0, 1)    // test2)
                        )),
                Arguments.of(
                        LocalDateTime.of(2022,9,1,0,0,0),
                        LocalDateTime.of(2022,9,1,23,59,59),
                        Arrays.asList(LocalDateTime.of(2022,9,1,23,59,58),
                                LocalDateTime.of(2022,9,1,0,0,1)),
                        Arrays.asList(LocalDateTime.of(2022,8,31,23,59,58),
                                LocalDateTime.of(2022,10,1,0,0,0))
                        ),
                Arguments.of(
                        LocalDateTime.of(2022,9,1,23,0,0),
                        LocalDateTime.of(2022,9,1,23,59,59),
                        Arrays.asList(LocalDateTime.of(2022,9,1,23,59,58),
                                LocalDateTime.of(2022,9,1,23,0,1)),
                        Arrays.asList(LocalDateTime.of(2022,9,1,22,59,59),
                                LocalDateTime.of(2022,9,2,0,0,0))
                )
        );
    }

    @Test
    public void parseTo_문자열변환_테스트() {
        // yyyy-MM-dd
        final String pattern = "yyyy-MM-dd";
        final String expected = "2022-09-01";
        final String result = parseTo(source, pattern);
        assertEquals(expected, result);

        // yyyy-MM-dd hh시 mm분
        final String pattern2 = "yyyy-MM-dd hh시 mm분";
        final String expected2 = "2022-09-01 12시 13분";
        String result2 = parseTo(source, pattern2);
        assertEquals(expected2, result2);
    }

}