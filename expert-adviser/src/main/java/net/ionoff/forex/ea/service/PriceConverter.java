package net.ionoff.forex.ea.service;

public class PriceConverter {

    public static int ONE_LOT = 100000;

    public static double getPip(double price) {
        return price * ONE_LOT;
    }
}
