package kata.supermarket;

import java.math.BigDecimal;
import java.util.Comparator;

public interface DiscountService {
    BigDecimal apply(ItemsContainer container,
                     Comparator<Discount<Product>> pDiscountComparator,
                     Comparator<Discount<WeighedProduct>> wDiscountComparator);
}
