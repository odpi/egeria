/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

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
            databaseType = template.getDatabaseType();
            databaseVersion = template.getDatabaseVersion();
            databaseInstance = template.getDatabaseInstance();
            databaseImportedFrom = template.getDatabaseImportedFrom();
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
     *  Set up the source (typically connection name) of the database information.
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
                       "name='" + getName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", pathName='" + getPathName() + '\'' +
                       ", createTime=" + getCreateTime() +
                       ", modifiedTime=" + getModifiedTime() +
                       ", encodingType='" + getEncodingType() + '\'' +
                       ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                       ", encodingDescription='" + getEncodingDescription() + '\'' +
                       ", encodingProperties=" + getEncodingProperties() +
                       ", databaseType='" + databaseType + '\'' +
                       ", databaseVersion='" + databaseVersion + '\'' +
                       ", databaseInstance='" + databaseInstance + '\'' +
                       ", databaseImportedFrom='" + databaseImportedFrom + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
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
        if (! (objectToCompare instanceof DatabaseProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        DatabaseProperties that = (DatabaseProperties) objectToCompare;

        if (databaseType != null ? ! databaseType.equals(that.databaseType) : that.databaseType != null)
        {
            return false;
        }
        if (databaseVersion != null ? ! databaseVersion.equals(that.databaseVersion) : that.databaseVersion != null)
        {
            return false;
        }
        if (databaseInstance != null ? ! databaseInstance.equals(that.databaseInstance) : that.databaseInstance != null)
        {
            return false;
        }
        return databaseImportedFrom != null ? databaseImportedFrom.equals(that.databaseImportedFrom) : that.databaseImportedFrom == null;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), databaseType, databaseVersion, databaseInstance, databaseImportedFrom);
    }
}
