/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarymanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ClassificationRequestBody describes the request body used when classifying/reclassifying elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationRequestBody
{
    private ClassificationProperties properties    = null;
    private Date                     effectiveTime = null;



    /**
     * Default constructor
     */
    public ClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ClassificationRequestBody(ClassificationRequestBody template)
    {
        if (template != null)
        {
            properties = template.getProperties();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return the properties for the classification.
     *
     * @return properties object
     */
    public ClassificationProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the classification.
     *
     * @param properties properties object
     */
    public void setProperties(ClassificationProperties properties)
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ClassificationRequestBody{" +
                       ", properties=" + properties +
                       ", effectiveTime=" + effectiveTime +
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
        if (! (objectToCompare instanceof ClassificationRequestBody that))
        {
            return false;
        }
        return Objects.equals(properties, that.properties) &&
                       Objects.equals(effectiveTime, that.effectiveTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(properties, effectiveTime);
    }
}
