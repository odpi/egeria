/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WorkingSetProperties describes a collection that is organizing a set of elements that are being worked on by
 * a specific person or process.  The disposition attribute describes the status of the elements in the collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WorkingSetProperties extends CollectionProperties
{
    private String disposition = null;

    /**
     * Default constructor
     */
    public WorkingSetProperties()
    {
        super();
        super.typeName = OpenMetadataType.WORKING_SET_COLLECTION.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public WorkingSetProperties(WorkingSetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.disposition = template.getDisposition();
        }
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public WorkingSetProperties(CollectionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.WORKING_SET_COLLECTION.typeName;
    }


    /**
     * Return the status of the elements in the collection.
     *
     * @return string description
     */
    public String getDisposition()
    {
        return disposition;
    }


    /**
     * Set up the status of the elements in the collection.
     *
     * @param disposition string description
     */
    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WorkingSetProperties{" +
                "disposition='" + disposition + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        WorkingSetProperties that = (WorkingSetProperties) objectToCompare;
        return Objects.equals(disposition, that.disposition);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), disposition);
    }
}
