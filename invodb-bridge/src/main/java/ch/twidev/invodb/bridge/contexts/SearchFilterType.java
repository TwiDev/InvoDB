package ch.twidev.invodb.bridge.contexts;

public enum SearchFilterType {

    ALL(2),
    AND(3),
    OR(4),
    EQUAL(5),
    NOT_EQUAL(6);

    private final int queryCode;

    SearchFilterType(int queryCode) {
        this.queryCode = queryCode;
    }

    public int getQueryCode() {
        return queryCode;
    }
}
