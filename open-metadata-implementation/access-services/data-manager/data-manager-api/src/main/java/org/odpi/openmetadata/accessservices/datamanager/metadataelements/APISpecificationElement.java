/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.APISpecificationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APISpecificationElement contains the properties and header for an API Specification (SchemaType) retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APISpecificationElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private APISpecificationProperties properties     = null;
    private ElementHeader              elementHeader  = null;
    private int                        operationCount = 0;

    /**
     * Default constructor
     */
    public APISpecificationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APISpecificationElement(APISpecificationElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();

            operationCount = template.getOperationCount();
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
     * Return the properties for the API specification.
     *
     * @return schema properties (using appropriate subclass)
     */
    public APISpecificationProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the API specification.
     *
     * @param properties schema properties
     */
    public void setProperties(APISpecificationProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return the count of operations in this API.
     *
     * @return String data type name
     */
    public int getOperationCount() { return operationCount; }


    /**
     * Set up the count of operations in this API
     *
     * @param operationCount data type name
     */
    public void setOperationCount(int operationCount)
    {
        this.operationCount = operationCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APISpecificationElement{" +
                       "properties=" + properties +
                       ", elementHeader=" + elementHeader +
                       ", operationCount=" + operationCount +
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
        APISpecificationElement that = (APISpecificationElement) objectToCompare;
        return operationCount == that.operationCount &&
                       Objects.equals(properties, that.properties) &&
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
        return Objects.hash(super.hashCode(), elementHeader, properties, operationCount);
    }
}
