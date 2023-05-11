/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActivityDescriptionProperties is used to classify a glossary that describes an activity.  This request body includes
 * an enum that describes the type of activity.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDescriptionProperties extends ClassificationProperties
{
    private GlossaryTermActivityType activityType = null;


    /**
     * Default constructor
     */
    public ActivityDescriptionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public ActivityDescriptionProperties(ActivityDescriptionProperties template)
    {
        super(template);

        if (template != null)
        {
            activityType = template.getActivityType();
        }
    }


    /**
     * Return the activity type for the classification.
     *
     * @return enum
     */
    public GlossaryTermActivityType getActivityType()
    {
        return activityType;
    }


    /**
     * Set up the activity type for the classification.
     *
     * @param activityType enum
     */
    public void setActivityType(GlossaryTermActivityType activityType)
    {
        this.activityType = activityType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ActivityDescriptionProperties{" +
                       "activityType=" + activityType +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ActivityDescriptionProperties that = (ActivityDescriptionProperties) objectToCompare;
        return activityType == that.activityType;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), activityType);
    }
}
