/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between a data structure and a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberDataFieldProperties extends RelationshipProperties
{
    private int dataFieldPosition = 0;

    /**
     * Default constructor
     */
    public MemberDataFieldProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public MemberDataFieldProperties(MemberDataFieldProperties template)
    {
        super(template);

        if (template != null)
        {
            dataFieldPosition = template.getDataFieldPosition();
        }
    }


    /**
     * Return the position of this data field in the data structure.
     *
     * @return int position in schema - 0 means first
     */
    public int getDataFieldPosition() { return dataFieldPosition; }


    /**
     * Set up the position of this data field in the data structure.
     *
     * @param dataFieldPosition int position in schema - 0 means first
     */
    public void setDataFieldPosition(int dataFieldPosition)
    {
        this.dataFieldPosition = dataFieldPosition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MemberDataFieldProperties{" +
                "dataFieldPosition=" + dataFieldPosition +
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
        MemberDataFieldProperties that = (MemberDataFieldProperties) objectToCompare;
        return dataFieldPosition == that.dataFieldPosition;
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataFieldPosition);
    }
}
