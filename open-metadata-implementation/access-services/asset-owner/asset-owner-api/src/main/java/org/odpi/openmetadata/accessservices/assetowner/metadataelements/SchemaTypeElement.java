/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaTypeProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeElement contains the properties and header for a reference data asset retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private SchemaTypeProperties schemaTypeProperties = null;
    private ElementHeader        elementHeader = null;


    /**
     * Default constructor
     */
    public SchemaTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaTypeElement(SchemaTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            schemaTypeProperties = template.getSchemaTypeProperties();
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
     * Return the properties for the schema.
     *
     * @return schema properties (using appropriate subclass)
     */
    public SchemaTypeProperties getSchemaTypeProperties()
    {
        return schemaTypeProperties;
    }


    /**
     * Set up the properties for the schema.
     *
     * @param schemaTypeProperties schema properties
     */
    public void setSchemaTypeProperties(SchemaTypeProperties schemaTypeProperties)
    {
        this.schemaTypeProperties = schemaTypeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaTypeElement{" +
                "elementHeader=" + elementHeader +
                ", properties='" + getSchemaTypeProperties() + '\'' +
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
        SchemaTypeElement that = (SchemaTypeElement) objectToCompare;
        return Objects.equals(schemaTypeProperties, that.schemaTypeProperties) &&
                Objects.equals(elementHeader, that.elementHeader);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, schemaTypeProperties);
    }
}
