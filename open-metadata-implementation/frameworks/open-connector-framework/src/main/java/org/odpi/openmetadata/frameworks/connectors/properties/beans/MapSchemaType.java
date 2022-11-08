/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MapSchemaType describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class MapSchemaType extends SchemaType
{
    private static final long     serialVersionUID = 1L;

    protected SchemaType mapFromElement = null;
    protected SchemaType mapToElement   = null;


    /**
     * Default constructor
     */
    public MapSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public MapSchemaType(MapSchemaType template)
    {
        super(template);

        if (template != null)
        {
            mapFromElement = template.getMapFromElement();
            mapToElement   = template.getMapToElement();
        }
    }


    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public SchemaType getMapFromElement()
    {
        if (mapFromElement == null)
        {
            return null;
        }
        else
        {
            return mapFromElement.cloneSchemaType();
        }
    }


    /**
     * Set up the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @param mapFromElement SchemaElement
     */
    public void setMapFromElement(SchemaType mapFromElement)
    {
        this.mapFromElement = mapFromElement;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public SchemaType getMapToElement()
    {
        if (mapToElement == null)
        {
            return null;
        }
        else
        {
            return mapToElement.cloneSchemaType();
        }
    }


    /**
     * Set up the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @param mapToElement SchemaType
     */
    public void setMapToElement(SchemaType mapToElement)
    {
        this.mapToElement = mapToElement;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new MapSchemaType(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return SchemaType
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new MapSchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MapSchemaType{" +
                       "mapFromElement=" + mapFromElement +
                       ", mapToElement=" + mapToElement +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", calculatedValue=" + isCalculatedValue() +
                       ", expression='" + getExpression() + '\'' +
                       ", formula='" + getFormula() + '\'' +
                       ", queries=" + getQueries() +
                       ", versionNumber='" + getVersionNumber() + '\'' +
                       ", author='" + getAuthor() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", encodingStandard='" + getEncodingStandard() + '\'' +
                       ", namespace='" + getNamespace() + '\'' +
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
        MapSchemaType that = (MapSchemaType) objectToCompare;
        return Objects.equals(getMapFromElement(), that.getMapFromElement()) &&
                       Objects.equals(getMapToElement(), that.getMapToElement());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMapFromElement(), getMapToElement());
    }
}