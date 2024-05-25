package ch.twidev.invodb.common.query.operations.search;

public class FieldSearchFilter extends SearchFilter{

    private final String value;
    private final Object object;

    public FieldSearchFilter(String value, Object object, SearchFilterType searchFilterType, SearchCondition searchCondition) {
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

    @Override
    public String toQuery(SearchDictionary searchDictionary) {
        return searchDictionary.getField(this.getSearchFilterType())
                .parse(value, object);
    }
}
