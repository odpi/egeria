/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataAssetRequestBody describes the request body used to create/update asset properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataAssetRequestBody extends UpdateRequestBody
{
    private DataAssetProperties elementProperties = null;


    /**
     * Default constructor
     */
    public DataAssetRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataAssetRequestBody(DataAssetRequestBody template)
    {
        super(template);

        if (template != null)
        {
            elementProperties = template.getElementProperties();
        }
    }


    /**
     * Return the properties for the element.
     *
     * @return properties object
     */
    public DataAssetProperties getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param elementProperties properties object
     */
    public void setElementProperties(DataAssetProperties elementProperties)
    {
        this.elementProperties = elementProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataAssetRequestBody{" +
                       "elementProperties=" + elementProperties +
                       ", metadataCorrelationProperties=" + getMetadataCorrelationProperties() +
                       ", effectiveTime=" + getEffectiveTime() +
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
        DataAssetRequestBody that = (DataAssetRequestBody) objectToCompare;
        return Objects.equals(getMetadataCorrelationProperties(), that.getMetadataCorrelationProperties()) &&
                       Objects.equals(getElementProperties(), that.getElementProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementProperties);
    }
}
