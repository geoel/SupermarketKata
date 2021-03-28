package kata.supermarket;

import java.util.Comparator;

/*
 * It will work without T and would require only one comparator passed to the service but it would be a type abuse and
 * ultimately, since there is a separation between Product and Weighed product maybe the comparators we decide to
 * use can be different too
 */
public class ByDiscountComparator<T> implements Comparator<Discount<T>> {
    @Override
    public int compare(Discount<T> o1, Discount<T> o2) {
        return o2.getDiscountAmount().compareTo(o1.getDiscountAmount());
    }
}
