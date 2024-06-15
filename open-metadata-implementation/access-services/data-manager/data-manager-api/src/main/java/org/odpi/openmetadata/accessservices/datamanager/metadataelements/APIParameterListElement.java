/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIParameterListProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIParameterListType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * APIParameterListElement contains the properties and header for an APIParameterList entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIParameterListElement implements MetadataElement
{
    private ElementHeader              elementHeader     = null;
    private APIParameterListProperties properties        = null;
    private APIParameterListType       parameterListType = null;
    private int                        parameterCount    = 0;

    /**
     * Default constructor
     */
    public APIParameterListElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public APIParameterListElement(APIParameterListElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            parameterListType = template.getParameterListType();
            parameterCount = template.getParameterCount();
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
     * Return the properties for the parameter list.
     *
     * @return schema properties (using appropriate subclass)
     */
    public APIParameterListProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the parameter list.
     *
     * @param properties schema properties
     */
    public void setProperties(APIParameterListProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the type of parameter list for the API operation.
     *
     * @return enum value
     */
    public APIParameterListType getParameterListType()
    {
        return parameterListType;
    }


    /**
     * Set up the type of parameter list for the API Operation.
     *
     * @param parameterListType enum value
     */
    public void setParameterListType(APIParameterListType parameterListType)
    {
        this.parameterListType = parameterListType;
    }


    /**
     * Return the count of parameters in this parameter list.
     *
     * @return String data type name
     */
    public int getParameterCount() { return parameterCount; }


    /**
     * Set up the count of parameters in this parameter list
     *
     * @param parameterCount data type name
     */
    public void setParameterCount(int parameterCount)
    {
        this.parameterCount = parameterCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "APIParameterListElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", parameterListType=" + parameterListType +
                       ", parameterCount=" + parameterCount +
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
        APIParameterListElement that = (APIParameterListElement) objectToCompare;
        return parameterCount == that.parameterCount &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(parameterListType, that.parameterListType) &&
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
        return Objects.hash(super.hashCode(), elementHeader, properties, parameterListType, parameterCount);
    }
}
