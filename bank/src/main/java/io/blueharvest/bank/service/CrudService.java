package io.blueharvest.bank.service;

import java.util.List;

/**
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
