/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseSchemaProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DatabaseSchemaElement contains the properties and header for a deployed database schema asset entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseSchemaElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader            elementHeader = null;
    private DatabaseSchemaProperties databaseSchemaProperties = null;

    /**
     * Default constructor
     */
    public DatabaseSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DatabaseSchemaElement(DatabaseSchemaElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            databaseSchemaProperties = template.getDatabaseSchemaProperties();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties for the database schema.
     *
     * @return schema properties
     */
    public DatabaseSchemaProperties getDatabaseSchemaProperties()
    {
        return databaseSchemaProperties;
    }


    /**
     * Set up the database schema properties.
     *
     * @param databaseSchemaProperties schema properties
     */
    public void setDatabaseSchemaProperties(DatabaseSchemaProperties databaseSchemaProperties)
    {
        this.databaseSchemaProperties = databaseSchemaProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DatabaseSchemaElement{" +
                "elementHeader=" + elementHeader +
                ", databaseSchemaProperties=" + databaseSchemaProperties +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        DatabaseSchemaElement that = (DatabaseSchemaElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(databaseSchemaProperties, that.databaseSchemaProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, databaseSchemaProperties);
    }
}
