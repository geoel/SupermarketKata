package kata.supermarket;

import java.math.BigDecimal;
import java.util.Map;

public interface ItemsContainer {
    Map<Product, Integer> unitItems();
    Map<WeighedProduct, BigDecimal> weighedItems();
}
