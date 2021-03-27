package kata.supermarket;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByDiscountComparatorTest {

    ByDiscountComparator byDiscountComparator = new ByDiscountComparator();

    @ParameterizedTest
    @MethodSource("valuesToCompare")
    public void testComparator(Discount d1, Discount d2, Integer expected) {
        assertEquals(expected, byDiscountComparator.compare(d1, d2));
    }

    private static Stream<Arguments> valuesToCompare() {
        return Stream.of(
                Arguments.of(
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.69"), Collections.emptyMap()),
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.699"), Collections.emptyMap()),
                        1),
                Arguments.of(
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.699"), Collections.emptyMap()),
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.69"), Collections.emptyMap()),
                        -1),
                Arguments.of(
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.69"), Collections.emptyMap()),
                        new Discount(Discount.DiscountType.SINGLE, new BigDecimal("100.69"), Collections.emptyMap()),
                        0)

        );
    }
}
