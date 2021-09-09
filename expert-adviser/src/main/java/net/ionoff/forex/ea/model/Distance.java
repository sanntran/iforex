package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Distance {

    private double distance;

    public static Distance of(double from, double to) {
        return new Distance(from - to);
    }

    public double asPip() {
        return distance * 100000;
    }
}
