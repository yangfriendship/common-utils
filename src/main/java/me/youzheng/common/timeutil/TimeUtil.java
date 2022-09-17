package me.youzheng.common.timeutil;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.function.BiPredicate;

@UtilityClass
public class TimeUtil {

    public static boolean match(LocalDateTime source, TimePredicate timePredicate) {
        return timePredicate.getPredicate().test(source, timePredicate.getTarget());
    }

    public static boolean match(LocalDateTime source, TimePredicate... timePredicates) {
        for (TimePredicate predicate : timePredicates) {
            if (!match(source, predicate)) {
                return false;
            }
        }
        return true;
    }

    public static boolean notMatch(LocalDateTime source, TimePredicate... timePredicates) {
        return !match(source, timePredicates);
    }

    public static TimePredicate isSameDays(LocalDateTime target) {
        return IsSameDay.create(target);
    }

    public static TimePredicate isSameYear(LocalDateTime target) {
        return IsSameYear.create(target);
    }

    public static TimePredicate isSameWeek(LocalDateTime target) {
        return IsSameWeek.create(target);
    }

    public static TimePredicate isSameHour(LocalDateTime target) {
        return IsSameHour.create(target);
    }

    public static TimePredicate isSameMin(LocalDateTime target) {
        return IsSameMin.create(target);
    }

    public static TimePredicate isAfterWeekOf(long weeks, LocalDateTime target) {
        return IsSameWeek.create(target.plusWeeks(weeks));
    }

    public static TimePredicate isAfterDaysOf(long days, LocalDateTime target) {
        return IsSameWeek.create(target.plusDays(days));
    }

    public static TimePredicate[] isBetween(LocalDateTime start, LocalDateTime end) {
        return new TimePredicate[]{IsAfter.create(start), IsBefore.create(end)};
    }

    public static TimePredicate[] isBetween(LocalDateTime start, LocalDateTime end, DateTimeType timeType) {
        return new TimePredicate[]{IsAfter.create(timeType.down.apply(start)), IsBefore.create(timeType.down.apply(end))};
    }



    @FunctionalInterface
    interface BinaryTimePredicate extends BiPredicate<LocalDateTime, LocalDateTime> {
        BinaryTimePredicate IS_SAME_YEAR = ((source, target) -> source.getYear() == target.getYear());
        BinaryTimePredicate IS_SAME_MONTH = ((source, target) -> source.getMonth() == target.getMonth());
        BinaryTimePredicate IS_SAME_WEEK = ((source, target) -> source.getDayOfWeek() == target.getDayOfWeek());
        BinaryTimePredicate IS_SAME_DAYS = ((source, target) -> source.getDayOfMonth() == target.getDayOfMonth());
        BinaryTimePredicate IS_SAME_HOUR = ((source, target) -> source.getHour() == target.getHour());
        BinaryTimePredicate IS_SAME_MIN = ((source, target) -> source.getMinute() == target.getMinute());
        BinaryTimePredicate IS_BEFORE = LocalDateTime::isBefore;
        BinaryTimePredicate IS_AFTER = LocalDateTime::isAfter;
    }

    protected interface TimePredicate {

        BinaryTimePredicate getPredicate();

        LocalDateTime getTarget();

    }

    interface IsBefore extends TimePredicate {

        static IsBefore create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_BEFORE;
        }

        LocalDateTime getTarget();
    }

    interface IsAfter extends TimePredicate {

        static IsAfter create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_AFTER;
        }

        LocalDateTime getTarget();
    }

    interface IsSameDay extends TimePredicate {

        static IsSameDay create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_DAYS;
        }

        LocalDateTime getTarget();
    }

    interface IsSameYear extends TimePredicate {

        static IsSameYear create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_YEAR;
        }

        LocalDateTime getTarget();
    }

    interface IsSameMonth extends TimePredicate {

        static IsSameMonth create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_MONTH;
        }

        LocalDateTime getTarget();
    }

    interface IsSameWeek extends TimePredicate {

        static IsSameWeek create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_WEEK;
        }

        LocalDateTime getTarget();
    }

    interface IsSameHour extends TimePredicate {

        static IsSameHour create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_HOUR;
        }

        LocalDateTime getTarget();
    }

    interface IsSameMin extends TimePredicate {

        static IsSameMin create(LocalDateTime target) {
            return () -> target;
        }

        default BinaryTimePredicate getPredicate() {
            return BinaryTimePredicate.IS_SAME_MIN;
        }

        LocalDateTime getTarget();
    }

}