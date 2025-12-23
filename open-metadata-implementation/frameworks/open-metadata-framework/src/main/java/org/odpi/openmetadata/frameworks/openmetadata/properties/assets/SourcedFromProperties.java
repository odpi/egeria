/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SourcedFromProperties links an element created by a template to the element that was copied.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SourcedFromProperties extends RelationshipBeanProperties
{
    private long sourceVersionNumber = 0L;


    /**
     * Default constructor
     */
    public SourcedFromProperties()
    {
        super();
        super.typeName = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public SourcedFromProperties(SourcedFromProperties template)
    {
        super(template);

        if (template != null)
        {
            sourceVersionNumber = template.getSourceVersionNumber();
        }
    }


    /**
     * Return the version number that the source element was at when the copy was taken.
     *
     * @return string
     */
    public long getSourceVersionNumber() { return sourceVersionNumber; }


    /**
     * Set up the version number that the source element was at when the copy was taken.
     *
     * @param sourceVersionNumber long
     */
    public void setSourceVersionNumber(long sourceVersionNumber)
    {
        this.sourceVersionNumber = sourceVersionNumber;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SampleDataProperties{" +
                "sourceVersionNumber='" + sourceVersionNumber + '\'' +
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
        SourcedFromProperties that = (SourcedFromProperties) objectToCompare;
        return (sourceVersionNumber == that.sourceVersionNumber);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), sourceVersionNumber);
    }
}