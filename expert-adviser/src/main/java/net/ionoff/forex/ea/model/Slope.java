package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Slope {
    private double slope;
    private Period period;

    @Getter
    @AllArgsConstructor
    public enum Period {
        SHORT(10000, 100000, 0.00236, 0.005),
        MEDIUM(100000, 10000000, 0.00236, 0.005),
        LONG(100000, 100000000, 0.00236, 0.005);
        private final long readableSlope;
        private final long readableSlopeSlope;
        private final double readableSlopeFast;
        private final double readableSlopeVeryFast;
    }

    public static Slope of(double slope, String period) {
        return new Slope(slope, Period.valueOf(period));
    }

    public double asReadableSlope() {
        return slope * period.readableSlope;
    }

    public double asReadableSlopeSlope() {
        return slope * period.readableSlopeSlope;
    }
}
