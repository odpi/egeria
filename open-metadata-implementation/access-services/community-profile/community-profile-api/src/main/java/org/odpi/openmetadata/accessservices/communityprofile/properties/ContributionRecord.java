/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ContributionRecord holds the information about an individual's contribution to the open metadata
 * ecosystem.  The base types provide support for karma points.  This can be extended using the
 * open metadata type system and these new properties can be maintained using the extended properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContributionRecord extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private int karmaPoints       = 0;
    private int karmaPointPlateau = 0;



    /**
     * Default Constructor
     */
    public ContributionRecord()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ContributionRecord(ContributionRecord template)
    {
        super (template);

        if (template != null)
        {
            this.karmaPoints = template.getKarmaPoints();
            this.karmaPointPlateau = template.getKarmaPointPlateau();
        }
    }


    /**
     * Return the karma points awarded to this person.
     *
     * @return count
     */
    public int getKarmaPoints()
    {
        return karmaPoints;
    }


    /**
     * Set up the karma points for this person.
     *
     * @param karmaPoints count
     */
    public void setKarmaPoints(int karmaPoints)
    {
        this.karmaPoints = karmaPoints;
    }


    /**
     * Return the current karma point plateau level for this person.
     *
     * @return count
     */
    public int getKarmaPointPlateau()
    {
        return karmaPointPlateau;
    }


    /**
     * Set up the karma point plateau level for this person.
     *
     * @param karmaPointPlateau count
     */
    public void setKarmaPointPlateau(int karmaPointPlateau)
    {
        this.karmaPointPlateau = karmaPointPlateau;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContributionRecord{" +
                       "karmaPoints=" + karmaPoints +
                       ", karmaPointPlateau=" + karmaPointPlateau +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ContributionRecord that = (ContributionRecord) objectToCompare;
        return getKarmaPoints() == that.getKarmaPoints() &&
               getKarmaPointPlateau() == that.getKarmaPointPlateau();
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getKarmaPoints(), getKarmaPointPlateau());
    }
}
