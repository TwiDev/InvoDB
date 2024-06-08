package ch.twidev.invodb.bridge.search;

import java.util.List;

public interface ICompositeSearchFilter {

    List<ISearchFilter> getSearchFilter();

}
