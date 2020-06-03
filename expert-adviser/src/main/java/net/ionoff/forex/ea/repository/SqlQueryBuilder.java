package net.ionoff.forex.ea.repository;

public final class SqlQueryBuilder {
    public static final String joinWithSpace(String... strings) {
        return String.join(" ", strings);
    }
}
