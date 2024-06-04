package ch.twidev.invodb.bridge.search;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;

import java.util.List;

public interface ISearchFilter {

    SearchCondition getSearchCondition();

    SearchFilterType getSearchFilterType();

    String toQuery(SearchDictionary searchDictionary, PlaceholderContext placeHolder);

    List<Object> getContexts();

}
