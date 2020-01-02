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
public class ContributionRecord extends CommonHeader
{
    private static final long    serialVersionUID = 1L;

    private List<Classification> classifications      = null;
    private String               qualifiedName        = null;
    private int                  karmaPoints          = 0;
    private int                  karmaPointPlateau    = 0;
    private Map<String, Object>  extendedProperties   = null;
    private Map<String, String>  additionalProperties = null;


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
            this.classifications = template.getClassifications();
            this.qualifiedName = template.getQualifiedName();
            this.karmaPoints = template.getKarmaPoints();
            this.karmaPointPlateau = template.getKarmaPointPlateau();
            this.extendedProperties = template.getExtendedProperties();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the list of active classifications for this element.
     *
     * @return list of classification objects
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return classifications;
        }
    }


    /**
     * Set up the list of active classifications for this element.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Return the qualified for this record - it is that same as the qualified name for the personal profile.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the qualified for this record - it is that same as the qualified name for the personal profile.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
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
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfile{" +
                "classifications=" + classifications +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", karmaPoints=" + karmaPoints +
                ", karmaPointPlateau=" + karmaPointPlateau +
                ", extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", originId='" + getOriginId() + '\'' +
                ", originName='" + getOriginName() + '\'' +
                ", originType='" + getOriginType() + '\'' +
                ", originLicense='" + getOriginLicense() + '\'' +
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
               getKarmaPointPlateau() == that.getKarmaPointPlateau() &&
                Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getClassifications(), getKarmaPoints(), getKarmaPointPlateau(),
                            getExtendedProperties(), getAdditionalProperties());
    }
}
