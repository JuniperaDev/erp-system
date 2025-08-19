package io.github.erp.internal.acl;

import java.util.Optional;

public interface ExternalSystemAdapter<T> {
    
    Optional<T> fetchData(String identifier);
    
    boolean validateData(T data);
    
    void handleError(Exception error, String context);
    
    default boolean isAvailable() {
        return true;
    }
    
    default String getSystemName() {
        return this.getClass().getSimpleName();
    }
}
