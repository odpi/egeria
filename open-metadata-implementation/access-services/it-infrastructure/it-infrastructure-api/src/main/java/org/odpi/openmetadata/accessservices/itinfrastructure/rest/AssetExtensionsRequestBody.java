/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetExtensionsRequestBody provides the request body payload for relationships between assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetExtensionsRequestBody extends EffectiveDatesRequestBody
{
    private static final long    serialVersionUID = 1L;

    private Date                effectiveTime = null;
    private Map<String, Object> properties = null;

    /**
     * Default constructor
     */
    public AssetExtensionsRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetExtensionsRequestBody(AssetExtensionsRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return the properties for this user identity.
     *
     * @return properties bean
     */
    public Map<String, Object> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for this user identity.
     *
     * @param properties properties bean
     */
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }


    /**
     * Return the date/time to use for the query.
     *
     * @return date object
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up  the date/time to use for the query.
     *
     * @param effectiveTime date object
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AssetExtensionsRequestBody{" +
                       "effectiveTime=" + effectiveTime +
                       ", properties=" + properties +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        AssetExtensionsRequestBody that = (AssetExtensionsRequestBody) objectToCompare;
        return Objects.equals(effectiveTime, that.effectiveTime) && Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, effectiveTime);
    }
}
