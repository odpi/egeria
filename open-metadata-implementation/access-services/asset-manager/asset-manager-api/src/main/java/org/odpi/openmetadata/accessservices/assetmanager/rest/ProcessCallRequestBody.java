/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessCallProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ProcessCallRequestBody describes the request body used when linking elements within processes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessCallRequestBody extends AssetManagerIdentifiersRequestBody
{
    private static final long    serialVersionUID = 1L;

    private ProcessCallProperties properties = null;


    /**
     * Default constructor
     */
    public ProcessCallRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ProcessCallRequestBody(ProcessCallRequestBody template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }


    /**
     * Return the properties for the relationship.
     *
     * @return properties object
     */
    public ProcessCallProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param properties properties object
     */
    public void setProperties(ProcessCallProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProcessCallRequestBody{" +
                       "properties=" + properties +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        ProcessCallRequestBody that = (ProcessCallRequestBody) objectToCompare;
        return Objects.equals(getProperties(), that.getProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
