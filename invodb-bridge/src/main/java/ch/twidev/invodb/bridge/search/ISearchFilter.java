package ch.twidev.invodb.bridge.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;

public interface ISearchFilter {

    SearchCondition getSearchCondition();

    SearchFilterType getSearchFilterType();

    String toQuery(SearchDictionary searchDictionary);

}
