/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Database is a java bean used to create Databases associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Database extends DataStore {

    /**
     * The description of the database type
     * -- GETTER --
     * Return a description of the database type.
     * @return string type name
     * -- SETTER --
     * Set up a description of the database type.
     * @param databaseType string type name
     */
    private String databaseType;

    /**
     * The version of the database
     * -- GETTER --
     * Return the version of the database - often this is related to the version of its schemas.
     * @return version name
     * -- SETTER --
     * Set up the version of the database - often this is related to the version of its schemas.
     * @param databaseVersion version name
     */
    private String databaseVersion;

    /**
     * The name of this database instance
     * -- GETTER --
     * Return the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     * @return instance name
     * -- SETTER --
     * Set up the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     * @param databaseInstance instance name
     */
    private String databaseInstance;

    /**
     * The  source (typically connection name) of the database information
     * -- GETTER --
     * Return the source (typically connection name) of the database information.
     * @return source name
     * -- SETTER --
     * Set up the source (typically connection name) of the database information.
     * @param databaseImportedFrom source name
     */
    private String databaseImportedFrom;

    /**
     * The database schema
     * -- GETTER --
     * Get database schema
     * @return the database schema for the database
     * -- SETTER --
     * Set up the database schema for the database
     * @param databaseSchema DatabaseSchema object
     */
    @JsonProperty("schema")
    private DatabaseSchema databaseSchema;

    /**
     * The relational tables inside the database
     * -- GETTER --
     * Gets the relational tables inside the database.
     * @return the relational tables inside the database
     * -- SETTER --
     * Sets up relational tables inside the database.
     * @param tables relational tables inside the database
     */
    List<RelationalTable> tables;

    /**
     * The Endpoint protocol
     * -- GETTER --
     * Get an Endpoint protocol
     * @return network address
     * -- SETTER --
     * Sets the protocol. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     * @param protocol protocol
     */
    // Needed to create Endpoint, which in turn is internally generated along with Connection, not provided by user
    private String protocol;

    /**
     * The Endpoint network address
     * -- GETTER --
     * Get an Endpoint network address
     * @return network address
     * -- SETTER --
     * Sets the network address. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     * @param networkAddress network address
     */
    private String networkAddress;

    /**
     * Determines if the database is incomplete
     * -- GETTER --
     * Return if the database is incomplete
     *
     * @return if the database is incomplete
     * -- SETTER --
     * Sets up if the database is incomplete
     * @param incomplete if the database is incomplete
     */
    @JsonProperty("incomplete")
    private boolean incomplete;
}