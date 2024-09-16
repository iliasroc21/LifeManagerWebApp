package org.lifemanager.backend.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String searchKey, Class<?> entity){
        super(" The " + entity.getSimpleName().toLowerCase() + " with key: " + searchKey +" already exists in our records ");
    }

}
