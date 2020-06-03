package net.ionoff.forex.ea.model;

public enum Period {
    M1(1), M5(2), M15(3), M30(4), H1(5), H4(6), D1(7), W1(8), MN1(9);

    private final int value;

    Period(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Period parse(int period) {
        for (Period p : Period.values()) {
            if (p.getValue() == period) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid period number value: " + period);
    }

    public static String getName(int period) {
        return parse(period).toString();
    }
}
