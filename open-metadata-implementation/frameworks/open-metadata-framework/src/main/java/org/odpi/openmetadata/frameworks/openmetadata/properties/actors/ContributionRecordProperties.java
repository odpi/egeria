/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ContributionRecordProperties holds the information about an individual's contribution to the open metadata
 * ecosystem.  The base types provide support for karma points.  This can be extended using the
 * open metadata type system and these new properties can be maintained using the extended properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContributionRecordProperties extends ReferenceableProperties
{
    private long    karmaPoints       = 0;


    /**
     * Default Constructor
     */
    public ContributionRecordProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONTRIBUTION_RECORD.typeName);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ContributionRecordProperties(ContributionRecordProperties template)
    {
        super (template);

        if (template != null)
        {
            this.karmaPoints = template.getKarmaPoints();
        }
    }


    /**
     * Return the karma points awarded to this person.
     *
     * @return count
     */
    public long getKarmaPoints()
    {
        return karmaPoints;
    }


    /**
     * Set up the karma points for this person.
     *
     * @param karmaPoints count
     */
    public void setKarmaPoints(long karmaPoints)
    {
        this.karmaPoints = karmaPoints;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContributionRecordProperties{" +
                "karmaPoints=" + karmaPoints +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ContributionRecordProperties that = (ContributionRecordProperties) objectToCompare;
        return karmaPoints == that.karmaPoints;
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), karmaPoints);
    }
}
