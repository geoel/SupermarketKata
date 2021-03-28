package kata.supermarket;

import java.util.List;

public interface DiscountStorage {
    <T> List<Discount<T>> getDiscountsByProduct(T product);
}
