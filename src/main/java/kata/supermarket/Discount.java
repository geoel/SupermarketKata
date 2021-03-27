package kata.supermarket;

import java.math.BigDecimal;
import java.util.Map;

public class Discount {

    private final DiscountType discountType;
    private final BigDecimal discountAmount;
    private final Map<Product, Integer> requiredInBasket;

    enum DiscountType {
        SINGLE, COMBINED;
    }

    public Discount(DiscountType discountType, BigDecimal discountAmount, Map<Product, Integer> requiredInBasket) {
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.requiredInBasket = requiredInBasket;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Map<Product, Integer> getRequiredInBasket() {
        return requiredInBasket;
    }
}
