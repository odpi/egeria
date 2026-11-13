/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateOptions provides a structure for the additional options when updating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateOptions extends MetadataSourceOptions
{
    private boolean mergeUpdate = true;


    /**
     * Default constructor
     */
    public UpdateOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateOptions(UpdateOptions template)
    {
        super(template);

        if (template != null)
        {
            mergeUpdate = template.getMergeUpdate();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return flag to indicate whether to merge the supplied properties with the existing properties (true), or
     * completely replace the existing properties with the supplied properties (false).  With a merge update, each
     * supplied property replaces the existing value of that property - a map or array property is replaced in full,
     * not merged with its existing entries - and properties that are not supplied keep their current values.
     * The default is true.
     *
     * @return boolean
     */
    public boolean getMergeUpdate()
    {
        return mergeUpdate;
    }


    /**
     * Set up flag to indicate whether to merge the supplied properties with the existing properties (true), or
     * completely replace the existing properties with the supplied properties (false).  With a merge update, each
     * supplied property replaces the existing value of that property - a map or array property is replaced in full,
     * not merged with its existing entries - and properties that are not supplied keep their current values.
     * The default is true.
     *
     * @param mergeUpdate boolean
     */
    public void setMergeUpdate(boolean mergeUpdate)
    {
        this.mergeUpdate = mergeUpdate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateOptions{" +
                ", mergeUpdate=" + mergeUpdate +
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
        if (! (objectToCompare instanceof UpdateOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return mergeUpdate == that.mergeUpdate;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mergeUpdate);
    }
}
