package org.lifemanager.backend.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String searchKey, Class<?> entity){
        super(" The " + entity.getSimpleName().toLowerCase() + " with key: " + searchKey +" does not exist in our records ");
    }
}
