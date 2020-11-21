package com.soundcloud.dao;

import com.soundcloud.builder.BuildException;

import java.sql.ResultSet;
import java.util.Optional;

/**
 * Interface expand CRUD interface
 *
 * @param <ENTITY> entity
 * @param <KEY>    key
 */
public interface EntityDao<ENTITY, KEY> extends CRUDDao<KEY, ENTITY> {
    /**
     * method clone entity
     *
     * @param entity entity
     * @return cloning entity
     */
    ENTITY clone(ENTITY entity);

    /**
     * method build entity by result set data
     *
     * @param data result set data
     * @return if we build successfully return optional entity, else optional.empty()
     * @throws BuildException if we have exceptions while building
     * @throws DaoException   if we have exceptions in sql request
     */
    Optional<ENTITY> build(ResultSet data) throws BuildException, DaoException;
}
