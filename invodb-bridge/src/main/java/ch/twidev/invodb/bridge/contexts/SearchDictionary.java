package ch.twidev.invodb.bridge.contexts;

import java.util.HashMap;
import java.util.List;

public class SearchDictionary extends HashMap<SearchFilterType, SearchDictionary.SearchParameter> {

    @Override
    public SearchParameter get(Object key) {
        if(!this.containsKey(key)) {
            throw new UnsupportedOperationException("Invalid search query parameter, not in search dictionary");
        }

        return super.get(key);
    }

    public SearchFieldParameter getField(SearchFilterType key) {
        if(this.get(key) instanceof SearchFieldParameter searchFieldParameter) {
            return searchFieldParameter;
        }else{
            throw new UnsupportedOperationException("Invalid search query type in dictionary");
        }
    }

    public SearchCompositeParameter getComposite(SearchFilterType key) {
        if(this.get(key) instanceof SearchCompositeParameter searchFieldParameter) {
            return searchFieldParameter;
        }else{
            throw new UnsupportedOperationException("Invalid search query type in dictionary");
        }
    }

    public interface SearchParameter {};

    public record SearchCompositeParameter(String value) implements SearchParameter {}

    public static abstract class SearchFieldParameter implements SearchParameter {

        public abstract String parse(String key, List<Object> context);

    }
}
