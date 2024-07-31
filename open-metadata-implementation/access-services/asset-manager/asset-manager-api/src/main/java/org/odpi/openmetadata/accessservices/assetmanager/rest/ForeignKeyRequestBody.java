/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ForeignKeyRequestBody describes the request body used when creating a foreign key relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForeignKeyRequestBody extends EffectiveTimeQueryRequestBody
{
    private ForeignKeyProperties properties = null;


    /**
     * Default constructor
     */
    public ForeignKeyRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ForeignKeyRequestBody(ForeignKeyRequestBody template)
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
    public ForeignKeyProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param properties properties object
     */
    public void setProperties(ForeignKeyProperties properties)
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
        return "ForeignKeyRequestBody{" +
                       "assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
                       ", effectiveTime=" + getEffectiveTime() +
                       ", properties=" + properties +
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
        ForeignKeyRequestBody that = (ForeignKeyRequestBody) objectToCompare;
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
