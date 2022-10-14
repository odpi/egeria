/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseTableProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DatabaseTableElement contains the properties and header for a database table entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseTableElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader           elementHeader = null;
    private DatabaseTableProperties databaseTableProperties = null;
    private int                     databaseColumnCount = 0;


    /**
     * Default constructor
     */
    public DatabaseTableElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DatabaseTableElement(DatabaseTableElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            databaseTableProperties = template.getDatabaseTableProperties();
            databaseColumnCount = template.getDatabaseColumnCount();
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
     * Return the database table properties.
     *
     * @return properties bean
     */
    public DatabaseTableProperties getDatabaseTableProperties()
    {
        return databaseTableProperties;
    }


    /**
     * Set up the database table properties.
     *
     * @param databaseTableProperties properties bean
     */
    public void setDatabaseTableProperties(DatabaseTableProperties databaseTableProperties)
    {
        this.databaseTableProperties = databaseTableProperties;
    }


    /**
     * Return the count of columns for this database table.
     *
     * @return int
     */
    public int getDatabaseColumnCount()
    {
        return databaseColumnCount;
    }


    /**
     * Set up the count of the columns for this database.
     *
     * @param databaseColumnCount int
     */
    public void setDatabaseColumnCount(int databaseColumnCount)
    {
        this.databaseColumnCount = databaseColumnCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DatabaseTableElement{" +
                "elementHeader=" + elementHeader +
                       ", databaseTableProperties=" + databaseTableProperties +
                       ", databaseColumnCount=" + databaseColumnCount +
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
        DatabaseTableElement that = (DatabaseTableElement) objectToCompare;
        return databaseColumnCount == that.databaseColumnCount &&
                       Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(databaseTableProperties, that.databaseTableProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, databaseTableProperties, databaseColumnCount);
    }
}
