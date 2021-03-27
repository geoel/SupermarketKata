package kata.supermarket;

import java.util.List;

public interface DiscountStorage {
    List<Discount> getDiscountsByProduct(Product product);
}
