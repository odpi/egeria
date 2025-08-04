/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewElementOptions provides a structure for the common properties when creating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewElementOptions extends NewLinkedElementOptions
{
    private ElementStatus initialStatus = ElementStatus.ACTIVE;


    /**
     * Default constructor
     */
    public NewElementOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementOptions(NewElementOptions template)
    {
        super(template);

        if (template != null)
        {
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param initialStatus object to copy
     */
    public NewElementOptions(ElementStatus initialStatus)
    {
        super();

        if (initialStatus != null)
        {
            this.initialStatus = initialStatus;
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementOptions(AnchorOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return the initial status of the solution element.
     *
     * @return instance status
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status of the solution element.
     *
     * @param initialStatus instance status
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewElementOptions{" +
                "initialStatus=" + initialStatus +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof NewElementOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return initialStatus == that.initialStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), initialStatus);
    }
}
