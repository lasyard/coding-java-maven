package io.github.lasyard.quiz;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DateTimeFormatTest {
    public static @NonNull Stream<Arguments> getCaseForDateParse() {
        return Stream.of(
            arguments("yyyyMMdd", "19700101", new Date(0)),
            arguments("y-M-d", "1970-1-1", new Date(0)),
            arguments("y-M-d", "1970-01-01", new Date(0)),
            arguments("yyyy-M-d", "1970-01-01", new Date(0)),
            arguments("yyyy-M-d", "1970-1-1", new Date(0)),
            arguments("yyyy-MM-dd", "1970-01-01", new Date(0)),
            arguments("y/M/d", "1970/1/1", new Date(0)),
            arguments("y.M.d", "1970.1.1", new Date(0))
        );
    }

    public static @NonNull Stream<Arguments> getCaseForDateParseFailInDateTimeFormatter() {
        return Stream.of(
            arguments("yyyy-MM-dd", "1970-1-1", new Date(0)),
            arguments("y-M-d", "70-1-1", new Date(0)),
            arguments("yy-M-d", "70-1-1", new Date(0)),
            arguments("yyyy-M-d", "70-1-1", new Date(-(1900L * 365L + 462L) * 24L * 60L * 60L * 1000L))
        );
    }

    public static @NonNull Stream<Arguments> getCaseForDateParseFailInSimpleDateTimeFormat() {
        return Stream.of(
            arguments("yy-M-d", "70-1-1", new Date((100L * 365L + 25L) * 24L * 60L * 60L * 1000L))
        );
    }

    public static @NonNull Stream<Arguments> getCaseForDateParseInvalid() {
        return Stream.of(
            arguments("yyyy-MM-dd", "1970-02-30", new Date(60L * 24L * 60L * 60L * 1000L))
        );
    }

    public static @NonNull Stream<Arguments> getCaseForDateParseOnlyInDateTimeFormatterSmart() {
        return Stream.of(
            arguments("yyyy-MM-dd", "1970-02-30", new Date(58L * 24L * 60L * 60L * 1000L))
        );
    }

    @ParameterizedTest
    @MethodSource({
        "getCaseForDateParse",
        "getCaseForDateParseFailInDateTimeFormatter",
        "getCaseForDateParseInvalid",
    })
    public void testSimpleDateFormatLenient(String format, String str, Date result) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(true);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(str);
        assertThat(date).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource({
        "getCaseForDateParse",
        "getCaseForDateParseFailInDateTimeFormatter",
    })
    public void testSimpleDateFormatNotLenient(String format, String str, Date result) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(str);
        assertThat(date).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource({
        "getCaseForDateParse",
        "getCaseForDateParseFailInSimpleDateTimeFormat",
        "getCaseForDateParseInvalid",
    })
    public void testDateTimeFormatterLenient(String format, String str, Date result) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.LENIENT);
        TemporalAccessor ta = dtf.parse(str);
        LocalDate t = LocalDate.from(ta);
        Date date = new Date(t.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        assertThat(date).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource({
        "getCaseForDateParse",
        "getCaseForDateParseFailInSimpleDateTimeFormat",
        "getCaseForDateParseOnlyInDateTimeFormatterSmart",
    })
    public void testDateTimeFormatterSmart(String format, String str, Date result) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.SMART);
        TemporalAccessor ta = dtf.parse(str);
        LocalDate t = LocalDate.from(ta);
        Date date = new Date(t.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        assertThat(date).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource({
        "getCaseForDateParse",
        "getCaseForDateParseFailInSimpleDateTimeFormat",
    })
    public void testDateTimeFormatterStrict(@NonNull String format, String str, Date result) {
        // In java 8, `u` is year and `y` is year of era.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format.replace('y', 'u'))
            .withResolverStyle(ResolverStyle.STRICT);
        TemporalAccessor ta = dtf.parse(str);
        LocalDate t = LocalDate.from(ta);
        Date date = new Date(t.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        assertThat(date).isEqualTo(result);
    }
}
