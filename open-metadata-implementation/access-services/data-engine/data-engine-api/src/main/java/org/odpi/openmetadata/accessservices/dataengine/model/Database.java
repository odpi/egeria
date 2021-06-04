/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Database extends DataStore {

    private String databaseType;
    private String databaseVersion;
    private String databaseInstance;
    private String databaseImportedFrom;

    @JsonProperty("schema")
    private DatabaseSchema databaseSchema;

    // Needed to create Endpoint, which in turn is internally generated along with Connection, not provided by user
    private String protocol;
    private String networkAddress;


    /**
     * @return the database schema for the database
     */
    public DatabaseSchema getDatabaseSchema() {
        return databaseSchema;
    }

    /**
     * Set up the database schema for the database
     *
     * @param databaseSchema DatabaseSchema object
     */
    public void setDatabaseSchema(DatabaseSchema databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    /**
     * Return a description of the database type.
     *
     * @return string type name
     */
    public String getDatabaseType() {
        return databaseType;
    }


    /**
     * Set up a description of the database type.
     *
     * @param databaseType string type name
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * Return the version of the database - often this is related to the version of its schemas.
     *
     * @return version name
     */
    public String getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Set up the version of the database - often this is related to the version of its schemas.
     *
     * @param databaseVersion version name
     */
    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    /**
     * Return the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     *
     * @return instance name
     */
    public String getDatabaseInstance() {
        return databaseInstance;
    }

    /**
     * Set up the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     *
     * @param databaseInstance instance name
     */
    public void setDatabaseInstance(String databaseInstance) {
        this.databaseInstance = databaseInstance;
    }

    /**
     * Return the source (typically connection name) of the database information.
     *
     * @return source name
     */
    public String getDatabaseImportedFrom() {
        return databaseImportedFrom;
    }

    /**
     * Set up the the source (typically connection name) of the database information.
     *
     * @param databaseImportedFrom source name
     */
    public void setDatabaseImportedFrom(String databaseImportedFrom) {
        this.databaseImportedFrom = databaseImportedFrom;
    }

    /**
     * Get an Endpoint network address
     *
     * @return network address
     *
     */
    public String getNetworkAddress() {
        return networkAddress;
    }

    /**
     * Sets the network address. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     *
     * @param networkAddress network address
     */
    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    /**
     * Get an Endpoint protocol
     *
     * @return network address
     *
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the protocol. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     *
     * @param protocol protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String
    toString() {
        return "Database{" +
                "databaseType='" + databaseType + '\'' +
                ", databaseVersion='" + databaseVersion + '\'' +
                ", databaseInstance='" + databaseInstance + '\'' +
                ", databaseImportedFrom='" + databaseImportedFrom + '\'' +
                ", databaseSchema=" + databaseSchema +
                ", networkAddress=" + networkAddress +
                ", protocol=" + protocol +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Database database = (Database) o;
        return Objects.equals(databaseType, database.databaseType) &&
                Objects.equals(databaseVersion, database.databaseVersion) &&
                Objects.equals(databaseInstance, database.databaseInstance) &&
                Objects.equals(databaseImportedFrom, database.databaseImportedFrom) &&
                Objects.equals(databaseSchema, database.databaseSchema) &&
                Objects.equals(networkAddress, database.networkAddress) &&
                Objects.equals(protocol, database.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), databaseType, databaseVersion, databaseInstance, databaseImportedFrom,
                databaseSchema, networkAddress, protocol);
    }
}