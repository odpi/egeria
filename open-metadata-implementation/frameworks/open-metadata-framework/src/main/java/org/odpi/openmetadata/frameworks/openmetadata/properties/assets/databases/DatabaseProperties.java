/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
    private String databaseInstance = null;
    private String importedFrom     = null;

    /**
     * Default constructor
     */
    public DatabaseProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATABASE.typeName);
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
            databaseInstance = template.getDatabaseInstance();
            importedFrom     = template.getImportedFrom();
        }
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
    public String getImportedFrom()
    {
        return importedFrom;
    }


    /**
     *  Set up the source (typically connection name) of the database information.
     *
     * @param importedFrom source name
     */
    public void setImportedFrom(String importedFrom)
    {
        this.importedFrom = importedFrom;
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
                "databaseInstance='" + databaseInstance + '\'' +
                ", databaseImportedFrom='" + importedFrom + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DatabaseProperties that = (DatabaseProperties) objectToCompare;
        return Objects.equals(databaseInstance, that.databaseInstance) && Objects.equals(importedFrom, that.importedFrom);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), databaseInstance, importedFrom);
    }
}
