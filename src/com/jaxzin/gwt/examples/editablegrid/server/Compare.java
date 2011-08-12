package com.jaxzin.gwt.examples.editablegrid.server;

/**
 * <pre>
 * Date: Jan 12, 2007
 * Time: 10:22:05 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class Compare {

    private Compare() {}

    private static boolean valid(Object left, Object right) {
        return left != null && right != null;
    }

    public static boolean eq(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) == 0;
    }

    public static boolean lt(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) < 0;
    }

    public static boolean gt(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) > 0;
    }

    public static boolean le(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) <= 0;
    }

    public static boolean ge(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) >= 0;
    }

    public static boolean ne(Comparable left, Comparable right) {
        return valid(left, right) && left.compareTo(right) != 0;
    }

}
