package kata.supermarket;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Basket {
    private final List<Item> items = new ArrayList<>();
    private final Map<Product, BigDecimal> unitItems = new HashMap<>();
    private final Map<WeighedProduct, BigDecimal> weighedItems = new HashMap<>();
    private final DiscountService discountService;

    public Basket(DiscountService discountService) {
        this.discountService = discountService;
    }

    public void add(final Item item) {
        this.items.add(item);
        /*
         * I don't like this but I am trying not to break the interface of existing classes
         */
        if (item instanceof ItemByUnit) {
            Product product = ((ItemByUnit) item).getProduct();
            BigDecimal quantity = unitItems.getOrDefault(product, BigDecimal.ZERO);
            this.unitItems.put(product, quantity.add(BigDecimal.ONE));
        } else if (item instanceof ItemByWeight) {
            ItemByWeight itemByWeight = (ItemByWeight) item;
            WeighedProduct weighedProduct = itemByWeight.getProduct();
            BigDecimal quantity = weighedItems.getOrDefault(weighedProduct, BigDecimal.ZERO);
            this.weighedItems.put(weighedProduct, quantity.add(itemByWeight.getWeightInKilos()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    List<Item> items() {
        return Collections.unmodifiableList(items);
    }

    Map<Product, BigDecimal> unitItems() {
        return Collections.unmodifiableMap(unitItems);
    }

    Map<WeighedProduct, BigDecimal> weighedItems() {
        return Collections.unmodifiableMap(weighedItems);
    }

    public BigDecimal total() {
        return new TotalCalculator().calculate();
    }

    private class TotalCalculator {
        private final Map<Product, BigDecimal> unitItems;
        private final Map<WeighedProduct, BigDecimal> weighedItems;
        private final List<Item> items;

        TotalCalculator() {
            this.unitItems = unitItems();
            this.weighedItems = weighedItems();
            this.items = items();
        }

        private BigDecimal subtotal() {
            return items.stream().map(Item::price)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        /**
         * TODO: This could be a good place to apply the results of
         *  the discount calculations.
         *  It is not likely to be the best place to do those calculations.
         *  Think about how Basket could interact with something
         *  which provides that functionality.
         */
        private BigDecimal discounts() {
            return discountService.apply(Basket.this, new ByDiscountComparator(), new ByDiscountComparator());
        }

        private BigDecimal calculate() {
            return subtotal().subtract(discounts());
        }
    }
}
