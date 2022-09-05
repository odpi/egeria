/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

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

    private long    karmaPoints       = 0;
    private long    karmaPointPlateau = 0;
    private boolean isPublic          = false;



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
            this.isPublic = template.getIsPublic();
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
     * Return the current karma point plateau level for this person.
     *
     * @return count
     */
    public long getKarmaPointPlateau()
    {
        return karmaPointPlateau;
    }


    /**
     * Set up the karma point plateau level for this person.
     *
     * @param karmaPointPlateau count
     */
    public void setKarmaPointPlateau(long karmaPointPlateau)
    {
        this.karmaPointPlateau = karmaPointPlateau;
    }


    /**
     * Return whether this record can be shared with colleagues.
     *
     * @return flag
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether this record can be shared with colleagues.
     *
     * @param isPublic flag
     */
    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
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
                       ", isPublic=" + isPublic +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ContributionRecord that = (ContributionRecord) objectToCompare;
        return karmaPoints == that.karmaPoints &&
                       karmaPointPlateau == that.karmaPointPlateau &&
                       isPublic == that.isPublic;
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), karmaPoints, karmaPointPlateau, isPublic);
    }
}
