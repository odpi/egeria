/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewMetadataElementRequestBody provides a structure for passing the properties for a new metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewMetadataElementRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String            typeName      = null;
    private ElementStatus     initialStatus = null;
    private Date              effectiveFrom = null;
    private Date              effectiveTo   = null;
    private ElementProperties properties    = null;
    private String            templateGUID  = null;


    /**
     * Default constructor
     */
    public NewMetadataElementRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewMetadataElementRequestBody(NewMetadataElementRequestBody template)
    {
        if (template != null)
        {
            typeName = template.getTypeName();
            initialStatus = template.getInitialStatus();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            properties = template.getProperties();
            templateGUID = template.getTemplateGUID();
        }
    }


    /**
     * Return the open metadata type name for the new metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name for the new metadata element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the initial status of the metadata element (typically ACTIVE).
     *
     * @return element status enum value
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status of the metadata element (typically ACTIVE).
     *
     * @param initialStatus element status enum value
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Return the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set up the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the properties for the new metadata element.
     *
     * @return list of properties
     */
    public ElementProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the new metadata element.
     *
     * @param properties list of properties
     */
    public void setProperties(ElementProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Set up the unique identifier of the element to use as a template (optional).
     *
     * @param templateGUID String guid
     */
    public void setTemplateGUID(String templateGUID)
    {
        this.templateGUID = templateGUID;
    }


    /**
     * Returns the unique identifier of the element to use as a template (optional).
     *
     * @return string guid
     */
    public String getTemplateGUID()
    {
        return templateGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewMetadataElementRequestBody{" +
                       "typeName='" + typeName + '\'' +
                       ", initialStatus=" + initialStatus +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", properties=" + properties +
                       ", templateGUID='" + templateGUID + '\'' +
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
        NewMetadataElementRequestBody that = (NewMetadataElementRequestBody) objectToCompare;
        return Objects.equals(typeName, that.typeName) &&
                       initialStatus == that.initialStatus &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(templateGUID, that.templateGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeName, initialStatus, effectiveFrom, effectiveTo, properties, templateGUID);
    }
}
