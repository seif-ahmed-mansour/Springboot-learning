package com.demo.crud.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    // Create -> ADD
    T create(T entity);

    // Update
    T update(Long id, T entity);

    //GET
    Optional<T> getById(Long id);

    //Get ALl

    List<T> getAll();

    //Delete

    Boolean delete(Long id);

    // check if exists

    Boolean exists(Long id);
}
