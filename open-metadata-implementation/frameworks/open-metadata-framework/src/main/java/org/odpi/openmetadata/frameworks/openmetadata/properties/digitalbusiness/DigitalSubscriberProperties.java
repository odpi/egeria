/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalProductDependencyProperties describes a dependency relationship between two digital products.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitalSubscriberProperties extends RelationshipBeanProperties
{
    private String subscriberId = null;


    /**
     * Default constructor
     */
    public DigitalSubscriberProperties()
    {
        super();
        super.typeName = OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DigitalSubscriberProperties(DigitalSubscriberProperties template)
    {
        super(template);

        if (template != null)
        {
            subscriberId = template.getSubscriberId();
        }
    }


    /**
     * Set up the subscriberId for the subscription.
     *
     * @param subscriberId String name
     */
    public void setSubscriberId(String subscriberId)
    {
        this.subscriberId = subscriberId;
    }


    /**
     * Returns the subscriberId for the subscription.
     *
     * @return String name
     */
    public String getSubscriberId()
    {
        return subscriberId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalSubscriberProperties{" +
                "subscriberId='" + subscriberId + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof DigitalSubscriberProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(subscriberId, that.subscriberId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), subscriberId);
    }
}
