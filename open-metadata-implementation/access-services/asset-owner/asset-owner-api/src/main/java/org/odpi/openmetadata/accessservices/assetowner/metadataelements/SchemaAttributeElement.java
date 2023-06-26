/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * SchemaAttributeElement contains the properties and header for a schema attribute retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributeElement implements MetadataElement
{
    private ElementHeader             elementHeader = null;
    private SchemaAttributeProperties schemaAttributeProperties = null;


    /**
     * Default constructor
     */
    public SchemaAttributeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaAttributeElement(SchemaAttributeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            schemaAttributeProperties = template.getSchemaAttributeProperties();
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
     * Return the properties for a schema attribute.
     *
     * @return properties bean
     */
    public SchemaAttributeProperties getSchemaAttributeProperties()
    {
        return schemaAttributeProperties;
    }


    /**
     * Set up the properties for a schema attribute.
     *
     * @param schemaAttributeProperties properties bean
     */
    public void setSchemaAttributeProperties(SchemaAttributeProperties schemaAttributeProperties)
    {
        this.schemaAttributeProperties = schemaAttributeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaAttributeElement{" +
                "elementHeader=" + elementHeader +
                ", schemaAttributeProperties=" + schemaAttributeProperties +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaAttributeElement that = (SchemaAttributeElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(schemaAttributeProperties, that.schemaAttributeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, schemaAttributeProperties);
    }
}
