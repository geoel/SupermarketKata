package kata.supermarket;

import java.util.Comparator;

public class ByDiscountComparator implements Comparator<Discount> {
    @Override
    public int compare(Discount o1, Discount o2) {
        return o2.getDiscountAmount().compareTo(o1.getDiscountAmount());
    }
}
