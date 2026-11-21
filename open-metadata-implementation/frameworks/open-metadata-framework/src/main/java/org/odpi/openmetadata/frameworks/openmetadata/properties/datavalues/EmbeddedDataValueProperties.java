/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EmbeddedDataValueProperties describes the link between a data value and another data value that is embedded in it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmbeddedDataValueProperties extends LabeledRelationshipProperties
{
    private Integer position = null;


    /**
     * Default constructor
     */
    public EmbeddedDataValueProperties()
    {
        super();
        super.typeName = OpenMetadataType.EMBEDDED_DATA_VALUE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public EmbeddedDataValueProperties(EmbeddedDataValueProperties template)
    {
        super(template);

        if (template != null)
        {
            position = template.getPosition();
        }
    }


    /**
     * Return the position of the embedded value in its parent.  Zero means no position set; the first position is 1.
     *
     * @return integer
     */
    public Integer getPosition()
    {
        return position;
    }


    /**
     * Set up the position of the embedded value in its parent.  Zero means no position set; the first position is 1.
     *
     * @param position integer
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedDataValueProperties{" +
                "position=" + position +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        EmbeddedDataValueProperties that = (EmbeddedDataValueProperties) objectToCompare;
        return Objects.equals(position, that.position);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position);
    }
}
