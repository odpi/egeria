/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.devops.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfigurationItemProperties provides the base class for infrastructure items.  This extends referenceable with the ability to
 * set effectivity dates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ServerAssetUseProperties.class, name = "ServerAssetUseProperties"),
                @JsonSubTypes.Type(value = SupportedCapabilityProperties.class, name = "SupportedCapabilityProperties"),
        })
public class ConfigurationItemRelationshipProperties
{
    private Date effectiveFrom = null;
    private Date effectiveTo   = null;


    /**
     * Default constructor
     */
    public ConfigurationItemRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ConfigurationItemRelationshipProperties(ConfigurationItemRelationshipProperties template)
    {
        if (template != null)
        {
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
        }
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfigurationItemRelationshipProperties{" +
                       "effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
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
        ConfigurationItemRelationshipProperties that = (ConfigurationItemRelationshipProperties) objectToCompare;
        return Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(effectiveFrom, effectiveTo);
    }
}