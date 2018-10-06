package io.blueharvest.bank.service;

import java.util.List;

/**
 * Basic CRUD operations a service should support
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
public interface CrudService<T> {
    T get(Long id);
    List<T> getAll();
    boolean create(T item);
    boolean update(T item);
    boolean delete(Long id);
}
