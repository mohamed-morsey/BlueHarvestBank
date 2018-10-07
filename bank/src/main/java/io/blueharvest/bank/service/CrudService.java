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

    /**
     * Get all items
     * @return A list of all items if any exists, otherwise and empty list
     */
    List<T> getAll();

    /**
     * Creates a new item and adds it to the system
     * @param item The item to be created
     * @return True if addition was successful, false otherwise
     */
    boolean create(T item);

    /**
     * Updates an existing item
     * @param item The item to be updated
     * @return True if update was successful, false otherwise
     */
    boolean update(T item);

    /**
     * Deletes an existing item
     * @param id The ID of the item
     * @return True if deletion was successful, false otherwise
     */
    boolean delete(Long id);
}
