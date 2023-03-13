/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseColumnProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseForeignKeyProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabasePrimaryKeyProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DatabaseColumnElement contains the properties and header for a database column entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseColumnElement  implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader                elementHeader = null;
    private DatabaseColumnProperties     databaseColumnProperties = null;

    /*
     * Filled out when this column is a primary key
     */
    private DatabasePrimaryKeyProperties primaryKeyProperties = null;

    /*
     * Filled out when this column is a foreign key - ie points to the primary key in another table
     */
    private DatabaseForeignKeyProperties foreignKeyProperties = null;
    private String                       referencedColumnGUID = null;
    private String                       referencedColumnQualifiedName = null;


    /**
     * Default constructor
     */
    public DatabaseColumnElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DatabaseColumnElement(DatabaseColumnElement template)
    {
        if (template != null)
        {
            elementHeader                 = template.getElementHeader();
            databaseColumnProperties      = template.getDatabaseColumnProperties();
            primaryKeyProperties          = template.getPrimaryKeyProperties();
            foreignKeyProperties          = template.getForeignKeyProperties();
            referencedColumnGUID          = template.getReferencedColumnGUID();
            referencedColumnQualifiedName = template.getReferencedColumnQualifiedName();
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
     * Return the principle properties for the database column.
     *
     * @return properties bean
     */
    public DatabaseColumnProperties getDatabaseColumnProperties()
    {
        return databaseColumnProperties;
    }


    /**
     * Set up the principle properties for the database column.
     *
     * @param databaseColumnProperties properties bean
     */
    public void setDatabaseColumnProperties(DatabaseColumnProperties databaseColumnProperties)
    {
        this.databaseColumnProperties = databaseColumnProperties;
    }


    /**
     * When this column is a primary key, return the properties of the primary key value; otherwise null
     *
     * @return primary key properties
     */
    public DatabasePrimaryKeyProperties getPrimaryKeyProperties()
    {
        return primaryKeyProperties;
    }


    /**
     * Set up the properties that indicate this column is a primary key
     *
     * @param primaryKeyProperties primary key properties
     */
    public void setPrimaryKeyProperties(DatabasePrimaryKeyProperties primaryKeyProperties)
    {
        this.primaryKeyProperties = primaryKeyProperties;
    }


    /**
     * When this column contains the primary key of another table (ie it is a foreign key) return the properties
     * associated with the relationship.
     *
     * @return foreign key properties
     */
    public DatabaseForeignKeyProperties getForeignKeyProperties()
    {
        return foreignKeyProperties;
    }


    /**
     * Set up the properties that indicate that this column is a foreign key.
     * This is null if the column is not a foreign key.
     *
     * @param foreignKeyProperties foreign key properties
     */
    public void setForeignKeyProperties(DatabaseForeignKeyProperties foreignKeyProperties)
    {
        this.foreignKeyProperties = foreignKeyProperties;
    }


    /**
     * Return the unique identifier of the column in another table that this column references through a foreign key relationship.
     * This is null if the column is not a foreign key.
     *
     * @return string unique identifier
     */
    public String getReferencedColumnGUID()
    {
        return referencedColumnGUID;
    }


    /**
     * Set up the unique identifier of the column in another table that this column references through a foreign key relationship.
     * This is null if the column is not a foreign key.
     *
     * @param referencedColumnGUID string unique identifier
     */
    public void setReferencedColumnGUID(String referencedColumnGUID)
    {
        this.referencedColumnGUID = referencedColumnGUID;
    }


    /**
     * Return the unique name of the column in another table that this column references through a foreign key relationship.
     * This is null if the column is not a foreign key.
     *
     * @return string unique identifier
     */
    public String getReferencedColumnQualifiedName()
    {
        return referencedColumnQualifiedName;
    }

    /**
     * Set up the unique name of the column in another table that this column references through a foreign key relationship.
     * This is null if the column is not a foreign key.
     *
     * @param referencedColumnQualifiedName string unique identifier
     */
    public void setReferencedColumnQualifiedName(String referencedColumnQualifiedName)
    {
        this.referencedColumnQualifiedName = referencedColumnQualifiedName;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DatabaseColumnElement{" +
                "elementHeader=" + elementHeader +
                ", databaseColumnProperties=" + databaseColumnProperties +
                ", primaryKeyProperties=" + primaryKeyProperties +
                ", foreignKeyProperties=" + foreignKeyProperties +
                ", referencedColumnGUID='" + referencedColumnGUID + '\'' +
                ", referencedColumnQualifiedName='" + referencedColumnQualifiedName + '\'' +
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
        DatabaseColumnElement that = (DatabaseColumnElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(databaseColumnProperties, that.databaseColumnProperties) &&
                       Objects.equals(primaryKeyProperties, that.primaryKeyProperties) &&
                       Objects.equals(foreignKeyProperties, that.foreignKeyProperties) &&
                       Objects.equals(referencedColumnGUID, that.referencedColumnGUID) &&
                       Objects.equals(referencedColumnQualifiedName, that.referencedColumnQualifiedName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, databaseColumnProperties, primaryKeyProperties, foreignKeyProperties,
                            referencedColumnGUID, referencedColumnQualifiedName);
    }
}
