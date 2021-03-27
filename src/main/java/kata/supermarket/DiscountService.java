package kata.supermarket;

import java.math.BigDecimal;
import java.util.Map;

public interface DiscountService {
    BigDecimal apply(Map<Item, Integer> basket);
}
