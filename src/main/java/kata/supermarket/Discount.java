package kata.supermarket;

import java.math.BigDecimal;
import java.util.Map;

public class Discount<T> {
    private final BigDecimal discountAmount;
    private final Map<T, BigDecimal> requiredInBasket;

    public Discount(BigDecimal discountAmount, Map<T, BigDecimal> requiredInBasket) {
        this.discountAmount = discountAmount;
        this.requiredInBasket = requiredInBasket;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Map<T, BigDecimal> getRequiredInBasket() {
        return requiredInBasket;
    }
}
