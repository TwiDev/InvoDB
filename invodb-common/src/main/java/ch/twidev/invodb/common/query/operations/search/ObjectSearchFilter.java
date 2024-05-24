package ch.twidev.invodb.common.query.operations.search;

public class ObjectSearchFilter extends SearchFilter{

    private final String value;
    private final Object object;

    public ObjectSearchFilter(String value, Object object, SearchFilterType searchFilterType, SearchCondition searchCondition) {
        super(searchFilterType, searchCondition);
        this.value = value;
        this.object = object;
    }

    public String getValue() {
        return value;
    }

    public Object getObject() {
        return object;
    }
}
