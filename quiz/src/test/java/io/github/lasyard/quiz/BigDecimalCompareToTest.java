package io.github.lasyard.quiz;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalCompareToTest {
    @Test
    public void testBigDecimalCompareTo() {
        BigDecimal a = BigDecimal.valueOf(Double.parseDouble("2"));
        BigDecimal b = BigDecimal.valueOf(Integer.parseInt("2"));
        assertThat(a.compareTo(b)).isEqualTo(0);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void testBigDecimalCompareTo1() {
        BigDecimal a = BigDecimal.valueOf(0.1);
        @SuppressWarnings("UnpredictableBigDecimalConstructorCall")
        BigDecimal b = new BigDecimal(0.1);
        assertThat(a.compareTo(b)).isNotEqualTo(0);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void testBigDecimalCompareTo2() {
        BigDecimal a = BigDecimal.valueOf(0.1);
        BigDecimal b = BigDecimal.valueOf(Float.parseFloat("0.1"));
        assertThat(a.compareTo(b)).isNotEqualTo(0);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void testBigDecimalCompareTo3() {
        String s1 = "0.99999999999999995"; // 16 of '9's
        String s2 = "1.00000000000000004"; // 16 of '0's
        BigDecimal a1 = BigDecimal.valueOf(Double.parseDouble(s1));
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal a2 = BigDecimal.valueOf(Double.parseDouble(s2));
        BigDecimal b2 = new BigDecimal(s2);
        assertThat(a1).isEqualTo(a2);
        assertThat(b1).isLessThan(b2);
        assertThat(b1.compareTo(b2)).isLessThan(0);
    }
}
