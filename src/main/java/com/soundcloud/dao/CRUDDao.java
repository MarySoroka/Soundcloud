package com.soundcloud.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations
 *
 * @param <KEY>    key for CRUD methods
 * @param <ENTITY> entity for CRUD methods
 */
public interface CRUDDao<KEY, ENTITY> {
    /**
     * method delete Entity by key
     *
     * @param key key
     * @return if entity has been deleted successfully return true, else false
     * @throws DaoException if we have exceptions in sql request
     */
    boolean delete(KEY key) throws DaoException;

    /**
     * method update Entity by key
     *
     * @param key    key
     * @param entity entity
     * @return if entity has been updated successfully return true, else false
     * @throws DaoException if we have exceptions in sql request
     */
    boolean update(KEY key, ENTITY entity) throws DaoException;

    /**
     * method save entity
     *
     * @param entity entity
     * @return key, that has been generated by database
     * @throws DaoException if we have exceptions in sql request
     */
    KEY save(ENTITY entity) throws DaoException;

    /**
     * method find entity by id
     *
     * @param id entity id
     * @return if entity presents, then return optional entity, else optional.empty()
     * @throws DaoException if we have exceptions in sql request
     */
    Optional<ENTITY> getById(KEY id) throws DaoException;

    /**
     * method return list of all entities
     *
     * @return list of all entities
     * @throws DaoException if we have exceptions in sql request
     */
    List<ENTITY> getAll() throws DaoException;

}
