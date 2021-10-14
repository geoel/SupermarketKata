package kata.supermarket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountServieForItemsInBasketTest {
    @Mock
    DiscountStorage discountStorage;

    @Mock
    Basket basket;

    Comparator<Discount<Product>> pComparator = new ByDiscountComparator<>();
    Comparator<Discount<WeighedProduct>> wComparator = new ByDiscountComparator<>();

    @Test
    public void serviceCalcuatesDiscountForProductsWithMultipleDiscountsDependingOnOrder() {
        Product milk = new Product(new BigDecimal("2.0"));
        Product hummus = new Product(new BigDecimal("1.5"));
        Product bread = new Product(new BigDecimal("0.5"));
        Product nuts = new Product(new BigDecimal("1.5"));
        WeighedProduct potatoes = new WeighedProduct(new BigDecimal("2.0"));
        WeighedProduct bananas = new WeighedProduct(new BigDecimal("1.5"));
        WeighedProduct candies = new WeighedProduct(new BigDecimal("11.5"));

        Map<Product, BigDecimal> hummus3For1 = new HashMap<>();
        hummus3For1.put(hummus, new BigDecimal("3"));
        Map<Product, BigDecimal> freeBreadWithHummus = new HashMap<>();
        freeBreadWithHummus.put(hummus, BigDecimal.ONE);
        freeBreadWithHummus.put(bread, BigDecimal.ONE);
        Map<Product, BigDecimal> milkTwoForOne = new HashMap<>();
        milkTwoForOne.put(milk, new BigDecimal("2"));

        Map<WeighedProduct, BigDecimal> potatoTwoAndAHalfTwentyPercentDiscount = new HashMap<>();
        potatoTwoAndAHalfTwentyPercentDiscount.put(potatoes, new BigDecimal("2.5"));
        Map<WeighedProduct, BigDecimal> bananasThreeKilos2GBPDiscount = new HashMap<>();
        bananasThreeKilos2GBPDiscount.put(bananas, new BigDecimal("3"));
        Map<WeighedProduct, BigDecimal> candiesTwoKilosSave5 = new HashMap<>();
        candiesTwoKilosSave5.put(candies, new BigDecimal("2"));

        when(discountStorage.getDiscountsByProduct(hummus)).thenReturn(Arrays.asList(
                new Discount<>(new BigDecimal(".5"), freeBreadWithHummus),
                new Discount<>(new BigDecimal("1.5"), hummus3For1)
        ));
        when(discountStorage.getDiscountsByProduct(milk)).thenReturn(Collections.singletonList(
                new Discount<>(new BigDecimal("2.0"), milkTwoForOne)));
        when(discountStorage.getDiscountsByProduct(bread)).thenReturn(Collections.singletonList(
                new Discount<>(new BigDecimal(".5"), freeBreadWithHummus)
        ));
        when(discountStorage.getDiscountsByProduct(nuts)).thenReturn(Collections.emptyList());

        when(discountStorage.getDiscountsByProduct(potatoes)).thenReturn(Arrays.asList(
                new Discount<>(new BigDecimal("1.0"), potatoTwoAndAHalfTwentyPercentDiscount)
        ));
        when(discountStorage.getDiscountsByProduct(bananas)).thenReturn(Collections.singletonList(
                new Discount<>(new BigDecimal("2"), bananasThreeKilos2GBPDiscount)
        ));
        when(discountStorage.getDiscountsByProduct(candies)).thenReturn(Collections.singletonList(
                new Discount<>(new BigDecimal("5"), candiesTwoKilosSave5)
        ));

        Map<Product, Integer> unitItems = new HashMap<>();
        unitItems.put(milk, 1);
        unitItems.put(hummus, 3); // 1.5
        unitItems.put(bread, 2);
        unitItems.put(nuts, 2);
        when(basket.unitItems()).thenReturn(unitItems);

        Map<WeighedProduct, BigDecimal> weighedItems = new HashMap<>();
        weighedItems.put(potatoes, new BigDecimal("2.5")); // 1
        weighedItems.put(bananas, new BigDecimal("4")); // 2
        weighedItems.put(candies, new BigDecimal("1.5"));
        when(basket.weighedItems()).thenReturn(weighedItems);

        BigDecimal bigDecimal = new DiscountServiceForItemsInBasket(discountStorage).apply(basket, pComparator, wComparator);

        assertEquals(new BigDecimal("4.5"), bigDecimal);


    }
}
