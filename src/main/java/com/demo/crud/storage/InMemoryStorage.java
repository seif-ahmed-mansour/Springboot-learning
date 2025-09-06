package com.demo.crud.storage;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryStorage<T extends BaseEntity> implements Storage<T> {
    private final Map<Long, T> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // implementing CREATE
    @Override
    public T create(T entity) {
        long newId = idCounter.getAndIncrement();
        entity.setId(newId);
        storage.put(newId, entity);
        return entity;
    }

    // implementing PUT - FIXED
    @Override
    public T update(Long id, T entity) {
        // ✅ FIXED: Use ! (NOT) to check if it DOES NOT exist
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("Entity with id " + id + " not found");
        }
        entity.setId(id);
        entity.setUpdatedAt(LocalDateTime.now());
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public Boolean exists(Long id) {
        return storage.containsKey(id);
    }
}