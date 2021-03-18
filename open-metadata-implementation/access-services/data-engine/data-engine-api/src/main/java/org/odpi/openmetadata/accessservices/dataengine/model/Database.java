/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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

    /**
     * Return a description of the database type.
     *
     * @return string type name
     */
    public String getDatabaseType()
    {
        return databaseType;
    }


    /**
     * Set up a description of the database type.
     *
     * @param databaseType string type name
     */
    public void setDatabaseType(String databaseType)
    {
        this.databaseType = databaseType;
    }


    /**
     * Return the version of the database - often this is related to the version of its schemas.
     *
     * @return version name
     */
    public String getDatabaseVersion()
    {
        return databaseVersion;
    }


    /**
     * Set up the version of the database - often this is related to the version of its schemas.
     *
     * @param databaseVersion version name
     */
    public void setDatabaseVersion(String databaseVersion)
    {
        this.databaseVersion = databaseVersion;
    }


    /**
     * Return the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     *
     * @return instance name
     */
    public String getDatabaseInstance()
    {
        return databaseInstance;
    }


    /**
     * Set up the name of this database instance - useful if the same schemas are deployed to multiple database instances.
     *
     * @param databaseInstance instance name
     */
    public void setDatabaseInstance(String databaseInstance)
    {
        this.databaseInstance = databaseInstance;
    }


    /**
     * Return the source (typically connection name) of the database information.
     *
     * @return source name
     */
    public String getDatabaseImportedFrom()
    {
        return databaseImportedFrom;
    }


    /**
     *  Set up the the source (typically connection name) of the database information.
     *
     * @param databaseImportedFrom source name
     */
    public void setDatabaseImportedFrom(String databaseImportedFrom)
    {
        this.databaseImportedFrom = databaseImportedFrom;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Database{" +
                "databaseType='" + databaseType + '\'' +
                ", databaseVersion='" + databaseVersion + '\'' +
                ", databaseInstance='" + databaseInstance + '\'' +
                ", databaseImportedFrom='" + databaseImportedFrom + '\'' +
                ", pathName='" + getPathName() + '\'' +
                ", createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encodingType='" + getEncodingType() + '\'' +
                ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", encodingProperties=" + getEncodingProperties() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerType=" + getOwnerType() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }

}
