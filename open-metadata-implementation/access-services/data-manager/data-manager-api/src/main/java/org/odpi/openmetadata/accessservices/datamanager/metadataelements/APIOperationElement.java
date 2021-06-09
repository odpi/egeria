/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIOperationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIOperationElement contains the properties and header for an APIOperation entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIOperationElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private APIOperationProperties properties    = null;
    private ElementHeader          elementHeader = null;
    private int                    payloadCount  = 0;

    /**
     * Default constructor
     */
    public APIOperationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIOperationElement(APIOperationElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();

            payloadCount = template.getPayloadCount();
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
     * Return the properties for the API operation.
     *
     * @return schema properties (using appropriate subclass)
     */
    public APIOperationProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the API operation.
     *
     * @param properties schema properties
     */
    public void setProperties(APIOperationProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return the count of the payloads (header, request, response) defined for this operation.
     *
     * @return int
     */
    public int getPayloadCount() { return payloadCount; }


    /**
     * Set up the count of payloads (header, request, response) defined for this operation
     *
     * @param payloadCount int
     */
    public void setPayloadCount(int payloadCount)
    {
        this.payloadCount = payloadCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APIOperationElement{" +
                       "properties=" + properties +
                       ", elementHeader=" + elementHeader +
                       ", payloadCount=" + payloadCount +
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
        APIOperationElement that = (APIOperationElement) objectToCompare;
        return payloadCount == that.payloadCount &&
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
        return Objects.hash(super.hashCode(), elementHeader, properties, payloadCount);
    }
}
