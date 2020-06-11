package net.ionoff.forex.ea.constant;

public enum Operation {
    OP_BUY(0), OP_SELL(1);

    private final int value;

    Operation(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Operation parse(int value) {
        for (Operation p : Operation.values()) {
            if (p.getValue() == value) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid number value: " + value);
    }

    public static String getName(int value) {
        return parse(value).toString();
    }
}
