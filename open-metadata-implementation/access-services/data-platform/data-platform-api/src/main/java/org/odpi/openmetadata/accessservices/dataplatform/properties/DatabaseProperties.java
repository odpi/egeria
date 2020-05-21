/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseProperties is a class for representing a generic database.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseProperties extends DataStoreProperties
{
    private static final long    serialVersionUID = 1L;

    private String databaseType         = null;
    private String databaseVersion      = null;
    private String databaseInstance     = null;
    private String databaseImportedFrom = null;

    /**
     * Default constructor
     */
    public DatabaseProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DatabaseProperties(DatabaseProperties template)
    {
        super(template);

        if (template != null)
        {

        }
    }


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
        return "DatabaseProperties{" +
                "databaseType='" + databaseType + '\'' +
                ", databaseVersion='" + databaseVersion + '\'' +
                ", databaseInstance='" + databaseInstance + '\'' +
                ", databaseImportedFrom='" + databaseImportedFrom + '\'' +
                ", createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encoding='" + getEncodingType() + '\'' +
                ", language='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", latestChange='" + getLatestChange() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DatabaseProperties that = (DatabaseProperties) objectToCompare;
        return Objects.equals(databaseType, that.databaseType) &&
                Objects.equals(databaseVersion, that.databaseVersion) &&
                Objects.equals(databaseInstance, that.databaseInstance) &&
                Objects.equals(databaseImportedFrom, that.databaseImportedFrom);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), databaseType, databaseVersion, databaseInstance, databaseImportedFrom);
    }
}
