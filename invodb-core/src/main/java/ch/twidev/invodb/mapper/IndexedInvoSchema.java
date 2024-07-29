package ch.twidev.invodb.mapper;

import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.mapper.annotations.PrimaryField;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public abstract class IndexedInvoSchema extends InvoSchema {

    private final LinkedHashMap<String, Field> primaryKeys = new LinkedHashMap<>();

    public IndexedInvoSchema() {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if(declaredField.isAnnotationPresent(PrimaryField.class)) {
                ch.twidev.invodb.mapper.annotations.Field field = declaredField.getAnnotation(ch.twidev.invodb.mapper.annotations.Field.class);
                primaryKeys.put(field.name().isEmpty() ? declaredField.getName() : field.name(), declaredField);
            }
        }
    }

    public String[] getPrimaryKeys() {
        return primaryKeys.keySet().toArray(new String[0]);
    }

    public Object[] getPrimaryValues() {
        return this.primaryKeys.values()
                .stream()
                .map(field -> this.getFields().get(field.getName()).getFormattedValue()).toArray(Object[]::new);
    }

    public void save() {
        this.save(this.getPrimaryFilters());
    }

    public CompletableFuture<OperationResult> saveAsync() {
        return this.saveAsync(this.getPrimaryFilters());
    }

    public void delete() {
        this.delete(this.getPrimaryFilters());
    }

    public void deleteAsync() {
        this.delete(this.getPrimaryFilters());
    }

    public SearchFilter getPrimaryFilters(){
        return this.getPrimaryFilters(this.getPrimaryValues());
    }

    public SearchFilter getPrimaryFilters(Object[] primaryValues){
        final String[] primaryKeys = this.getPrimaryKeys();

        SearchFilter searchFilter = SearchFilter.eq(primaryKeys[0], primaryValues[0]);

        for (int i = 1; i < primaryKeys.length; i++) {
            searchFilter = SearchFilter.and(searchFilter, SearchFilter.eq(primaryKeys[i], primaryValues[i]));
        }

        return searchFilter;
    }
}
