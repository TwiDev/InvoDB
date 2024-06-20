package ch.twidev.invodb.common.query.operations;

public enum QueryOperation {

    FIND(true, false),
    INSERT(false, false),
    UPDATE,
    DELETE,
    COUNT(false, false);

    private final boolean isCacheable;
    private final boolean invalidateCache;

    QueryOperation(boolean isCacheable, boolean invalidateCache) {
        this.isCacheable = isCacheable;
        this.invalidateCache = invalidateCache;
    }

    QueryOperation() {
        this.isCacheable = false;
        this.invalidateCache = true;
    }

    public boolean isCacheable() {
        return isCacheable;
    }

    public boolean isInvalidateCache() {
        return invalidateCache;
    }
}
