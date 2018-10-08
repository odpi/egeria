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
 * MapSchemaElement describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class MapSchemaElement extends SchemaElement
{
    protected SchemaElement mapFromElement = null;
    protected SchemaElement mapToElement   = null;


    /**
     * Default constructor
     */
    public MapSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchema template object to copy.
     */
    public MapSchemaElement(MapSchemaElement templateSchema)
    {
        super(templateSchema);

        if (templateSchema != null)
        {
            mapFromElement = templateSchema.getMapFromElement();
            mapToElement   = templateSchema.getMapToElement();
        }
    }


    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public SchemaElement getMapFromElement()
    {
        if (mapFromElement == null)
        {
            return null;
        }
        else
        {
            return mapFromElement.cloneSchemaElement();
        }
    }


    /**
     * Set up the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @param mapFromElement SchemaElement
     */
    public void setMapFromElement(SchemaElement mapFromElement)
    {
        this.mapFromElement = mapFromElement;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public SchemaElement getMapToElement()
    {
        if (mapToElement == null)
        {
            return null;
        }
        else
        {
            return mapToElement.cloneSchemaElement();
        }
    }


    /**
     * Set up the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @param mapToElement SchemaElement
     */
    public void setMapToElement(SchemaElement mapToElement)
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
        return new MapSchemaElement(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MapSchemaElement{" +
                "mapFromElement=" + mapFromElement +
                ", mapToElement=" + mapToElement +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof MapSchemaElement))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MapSchemaElement that = (MapSchemaElement) objectToCompare;
        return Objects.equals(getMapFromElement(), that.getMapFromElement()) &&
                Objects.equals(getMapToElement(), that.getMapToElement());
    }
}