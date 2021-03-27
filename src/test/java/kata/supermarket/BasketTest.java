package kata.supermarket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketTest {

    private final static Product PACK_OF_DIGESTIVES = new Product(new BigDecimal("1.55"));
    private final static Product PINT_OF_MILK = new Product(new BigDecimal("0.49"));
    private final static WeighedProduct KILO_OF_PICK_AND_MIX = aKiloOfPickAndMix();
    private final static WeighedProduct KILO_OF_AMERICAN_SWEETS = aKiloOfAmericanSweets();

    @Mock
    DiscountService discountService;

    @DisplayName("basket provides its total value when containing...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void basketProvidesTotalValue(String description, String expectedTotal, Collection<Item> items) {
        ArgumentCaptor<Basket> argumentCaptor = ArgumentCaptor.forClass(Basket.class);
        BigDecimal discount = new BigDecimal("3.0");
        when(discountService.apply(argumentCaptor.capture())).thenReturn(discount);
        final Basket basket = new Basket(discountService);
        items.forEach(basket::add);
        BigDecimal total = basket.total();

        Basket arg = argumentCaptor.getValue();
        assertSame(basket, arg);
        assertEquals(new BigDecimal(expectedTotal).subtract(discount), total);
    }


    @MethodSource
    @ParameterizedTest(name = "{0}")
    void basketConvertsItems(String description, Collection<Item> items,
                             Map<Product, Integer> unitItems, Map<WeighedProduct, BigDecimal> weighedItems) {
        final Basket basket = new Basket(discountService);
        items.forEach(basket::add);

        assertEquals(unitItems, basket.unitItems());
        assertEquals(weighedItems, basket.weighedItems());
    }

    static Stream<Arguments> basketProvidesTotalValue() {
        return Stream.of(
                noItems(),
                aSingleItemPricedPerUnit(),
                multipleItemsPricedPerUnit(),
                aSingleItemPricedByWeight(),
                multipleItemsPricedByWeight(),
                multipleMixedItems()
        );
    }

    static Stream<Arguments> basketConvertsItems() {
        return Stream.of(
                Arguments.of("Just weight", Collections.singleton(twoFiftyGramsOfAmericanSweets()),
                        Collections.emptyMap(),
                        Collections.singletonMap(KILO_OF_AMERICAN_SWEETS, new BigDecimal("0.25"))),
                Arguments.of("Just unit", Collections.singleton(aPintOfMilk()),
                        Collections.singletonMap(PINT_OF_MILK, 1),
                        Collections.emptyMap()),
                Arguments.of("Mixed", mixedItems(),
                        unitItems(),
                        weighedItems()),
                Arguments.of("Mixed with duplicates", mixedDuplicates(),
                        unitItemsWithDuplicate(),
                        weighedItemsWithDuplicate())
        );
    }

    private static Arguments aSingleItemPricedByWeight() {
        return Arguments.of("a single weighed item", "1.25", Collections.singleton(twoFiftyGramsOfAmericanSweets()));
    }

    private static Arguments multipleItemsPricedByWeight() {
        return Arguments.of("multiple weighed items", "1.85",
                Arrays.asList(twoFiftyGramsOfAmericanSweets(), twoHundredGramsOfPickAndMix())
        );
    }

    private static Arguments multipleItemsPricedPerUnit() {
        return Arguments.of("multiple items priced per unit", "2.04",
                Arrays.asList(aPackOfDigestives(), aPintOfMilk()));
    }

    private static Arguments aSingleItemPricedPerUnit() {
        return Arguments.of("a single item priced per unit", "0.49", Collections.singleton(aPintOfMilk()));
    }

    private static Arguments noItems() {
        return Arguments.of("no items", "0.00", Collections.emptyList());
    }

    private static Item aPintOfMilk() {
        return PINT_OF_MILK.oneOf();
    }

    private static Item aPackOfDigestives() {
        return PACK_OF_DIGESTIVES.oneOf();
    }

    private static WeighedProduct aKiloOfAmericanSweets() {
        return new WeighedProduct(new BigDecimal("4.99"));
    }

    private static Item twoFiftyGramsOfAmericanSweets() {
        return KILO_OF_AMERICAN_SWEETS.weighing(new BigDecimal(".25"));
    }

    private static WeighedProduct aKiloOfPickAndMix() {
        return new WeighedProduct(new BigDecimal("2.99"));
    }

    private static Item twoHundredGramsOfPickAndMix() {
        return KILO_OF_PICK_AND_MIX.weighing(new BigDecimal(".2"));
    }

    private static List<Item> mixedItems() {
        return Arrays.asList(aPackOfDigestives(), aPintOfMilk(), twoHundredGramsOfPickAndMix());
    }

    private static List<Item> mixedDuplicates() {
        return Arrays.asList(aPackOfDigestives(), aPintOfMilk(), twoHundredGramsOfPickAndMix(),
                aPintOfMilk(), KILO_OF_PICK_AND_MIX.weighing(new BigDecimal(".3")), twoFiftyGramsOfAmericanSweets());
    }

    private static Arguments multipleMixedItems() {
        return Arguments.of("mixed items", "2.64", mixedItems());
    }

    private static Map<Product, Integer> unitItems() {
        Map<Product, Integer> unitItems = new HashMap<>();
        unitItems.put(PACK_OF_DIGESTIVES, 1);
        unitItems.put(PINT_OF_MILK, 1);

        return unitItems;
    }

    private static Map<Product, Integer> unitItemsWithDuplicate() {
        Map<Product, Integer> unitItems = new HashMap<>();
        unitItems.put(PACK_OF_DIGESTIVES, 1);
        unitItems.put(PINT_OF_MILK, 2);

        return unitItems;
    }

    private static Map<WeighedProduct, BigDecimal> weighedItems() {
        Map<WeighedProduct, BigDecimal> weighedItems = new HashMap<>();
        weighedItems.put(KILO_OF_PICK_AND_MIX, new BigDecimal(".2"));
        return weighedItems;
    }

    private static Map<WeighedProduct, BigDecimal> weighedItemsWithDuplicate() {
        Map<WeighedProduct, BigDecimal> weighedItems = new HashMap<>();
        weighedItems.put(KILO_OF_PICK_AND_MIX, new BigDecimal(".5"));
        weighedItems.put(KILO_OF_AMERICAN_SWEETS, new BigDecimal(".25"));
        return weighedItems;
    }
}