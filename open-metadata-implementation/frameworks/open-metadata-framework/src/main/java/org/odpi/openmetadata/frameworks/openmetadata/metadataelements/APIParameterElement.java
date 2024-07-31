/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIParameterElement contains the properties and header for a APIParameter entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIParameterElement implements MetadataElement
{
    private ElementHeader          elementHeader = null;
    private APIParameterProperties properties    = null;
    private SchemaTypeElement      type          = null;

    /**
     * Default constructor
     */
    public APIParameterElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIParameterElement(APIParameterElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            type = template.getType();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIParameterElement(SchemaAttributeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();

            if (template.getSchemaAttributeProperties() != null)
            {
                properties = new APIParameterProperties(template.getSchemaAttributeProperties());
            }

            type = template.getSchemaTypeElement();
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
     * Return the properties of the API parameter.
     *
     * @return properties bean
     */
    public APIParameterProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the API parameter.
     *
     * @param properties properties bean
     */
    public void setProperties(APIParameterProperties properties)
    {
        this.properties = properties;
    }


    public SchemaTypeElement getType()
    {
        return type;
    }


    public void setType(SchemaTypeElement type)
    {
        this.type = type;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APIParameterElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", type=" + type +
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
        APIParameterElement that = (APIParameterElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(type, that.type);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, type);
    }
}
