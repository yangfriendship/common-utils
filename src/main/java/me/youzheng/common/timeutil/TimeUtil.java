package me.youzheng.common.timeutil;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

@UtilityClass
public class TimeUtil {

    private static final Map<String, DateTimeFormatter> dateTimeFormatterCache = new ConcurrentHashMap<>();
    private static Locale DEFAULT_LOCALE = Locale.getDefault();
    private static WeekFields WEEK_FIELDS = WeekFields.of(DEFAULT_LOCALE);

    public static void setLocale(Locale locale) {
        DEFAULT_LOCALE = locale;
        WEEK_FIELDS= WeekFields.of(locale);
    }

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

    public static TimePredicate isSameMonth(LocalDateTime target) {
        return IsSameMonth.create(target);
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

    public static TimePredicate[] isBetween(LocalDateTime start, LocalDateTime end) {
        LocalDateTime from;
        LocalDateTime to;
        if (start.isBefore(end)) {
            from = start;
            to = end;
        } else {
            from = end;
            to = start;
        }
        return new TimePredicate[]{IsAfter.create(from), IsBefore.create(to)};
    }

    public static String parseTo(LocalDateTime target, String pattern) {
        return target.format(getDateTimeFormatter(pattern));
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        DateTimeFormatter formatter = dateTimeFormatterCache.get(pattern);
        if (formatter == null) {
            synchronized (dateTimeFormatterCache) {
                if (!dateTimeFormatterCache.containsKey(pattern)) {
                    formatter = DateTimeFormatter.ofPattern(pattern);
                    dateTimeFormatterCache.put(pattern, formatter);
                } else {
                    formatter = dateTimeFormatterCache.get(pattern);
                }
            }
        }
        return formatter;
    }

    @FunctionalInterface
    interface BinaryTimePredicate extends BiPredicate<LocalDateTime, LocalDateTime> {
        BinaryTimePredicate IS_SAME_YEAR = ((source, target) -> source.getYear() == target.getYear());
        BinaryTimePredicate IS_SAME_MONTH = ((source, target) -> {
            if (!IS_SAME_YEAR.test(source,target)) {
                return false;
            }
            return source.getMonth() == target.getMonth();
        });
        BinaryTimePredicate IS_SAME_WEEK = ((source, target) -> {
            if (!IS_SAME_YEAR.test(source,target)) {
                return false;
            }
            return source.get(WEEK_FIELDS.weekOfWeekBasedYear()) == target.get(WEEK_FIELDS.weekOfWeekBasedYear());
        });
        BinaryTimePredicate IS_SAME_DAYS = ((source, target) -> {
            if (!IS_SAME_YEAR.test(source,target)) {
                return false;
            }
            return source.getDayOfYear() == target.getDayOfYear();
        });
        BinaryTimePredicate IS_SAME_HOUR = ((source, target) -> {
            if (!IS_SAME_DAYS.test(source,target)) {
                return false;
            }
            return source.getHour() == target.getHour();
        });
        BinaryTimePredicate IS_SAME_MIN = ((source, target) -> {
            if (!IS_SAME_HOUR.test(source,target)) {
                return false;
            }
            return source.getMinute() == target.getMinute();
        });
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