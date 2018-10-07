package io.blueharvest.bank.service;

import java.util.List;
import java.util.Optional;

/**
 * Basic CRUD operations a service should support
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
public interface CrudService<T> {
    /**
     * Get a specific item with its ID
     * @param id The ID of the item
     * @return The item if found
     */
    Optional<T> get(Long id);
    List<T> getAll();
    boolean create(T item);
    boolean update(T item);
    boolean delete(Long id);
}
