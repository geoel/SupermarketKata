package kata.supermarket;

import java.math.BigDecimal;
import java.util.*;

public class DiscountServiceForItemsInBasket implements DiscountService {
    private final DiscountStorage discountStorage;

    public DiscountServiceForItemsInBasket(DiscountStorage discountStorage) {
        this.discountStorage = discountStorage;
    }

    @Override
    public BigDecimal apply(ItemsContainer container,
                            Comparator<Discount<Product>> pDiscountComparator,
                            Comparator<Discount<WeighedProduct>> wDiscountComparator) {
        Map<Product, BigDecimal> unitItems = new HashMap<>();
        container.unitItems().entrySet().forEach(x -> unitItems.put(x.getKey(), new BigDecimal(x.getValue())));
        Map<WeighedProduct, BigDecimal> weighedItems = new HashMap<>(container.weighedItems());
        return applyDiscountForProduct(unitItems, pDiscountComparator).add(
                applyDiscountForProduct(weighedItems, wDiscountComparator));
    }

    private <T> boolean isDiscountApplicable(Discount<T> discount, Map<T, BigDecimal> items) {
        Map<T, BigDecimal> intemsNeeded = discount.getRequiredInBasket();
        return intemsNeeded.entrySet().stream().allMatch(x -> items.get(x.getKey()).compareTo(x.getValue()) >= 0);
    }

    private <T> BigDecimal applyDiscount(Discount<T> discount, Map<T, BigDecimal> items) {
        BigDecimal amount = BigDecimal.ZERO;

        while (isDiscountApplicable(discount, items)) {
            discount.getRequiredInBasket().entrySet().forEach(x ->
                    items.put(x.getKey(), items.get(x.getKey()).subtract(x.getValue())));
            amount = amount.add(discount.getDiscountAmount());
        }

        return amount;
    }

    private <T> BigDecimal applyDiscountForProduct(Map<T, BigDecimal> items, Comparator<Discount<T>> discountComparator) {
        BigDecimal amount = BigDecimal.ZERO;
        List<Discount<T>> discounts;
        List<Discount<T>> applicableDiscounts = new LinkedList<>();

        for (T product : items.keySet()) {
            discounts = discountStorage.getDiscountsByProduct(product);

            for (Discount<T> discount : discounts) {
                if (isDiscountApplicable(discount, items)) {
                    applicableDiscounts.add(discount);
                }
            }
        }

        Collections.sort(applicableDiscounts, discountComparator);

        for (Discount discount : applicableDiscounts) {
            amount = amount.add(applyDiscount(discount, items));
        }

        return amount;
    }
}
