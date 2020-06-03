package net.ionoff.forex.ea.model;

public enum Volume {
    V300(300);

    private final int value;

    Volume(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Volume parse(int period) {
        for (Volume p : Volume.values()) {
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
