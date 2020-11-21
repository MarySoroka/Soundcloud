package com.soundcloud.builder;

import java.sql.ResultSet;

/**
 * Interface for builder entities
 *
 * @param <T> building entity type
 */
public interface EntityBuilder<T> {
    /**
     * method build entity
     * @param data result set data for building
     * @return entity type
     * @throws BuildException if we couldn't find any field by field type or blob file
     */
    T build(ResultSet data) throws BuildException;
}
