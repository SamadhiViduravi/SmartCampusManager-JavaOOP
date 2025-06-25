package com.campus.utils;

import java.util.List;

/**
 * Generic interface for CRUD operations
 */
public interface Manageable<T> {
    void create(T item);
    T read(String id);
    void update(T item);
    void delete(String id);
    List<T> getAll();
}