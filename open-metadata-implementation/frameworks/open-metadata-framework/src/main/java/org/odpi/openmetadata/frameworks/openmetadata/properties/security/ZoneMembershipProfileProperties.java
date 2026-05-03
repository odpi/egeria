/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * List of zones that the element belongs to
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ZoneMembershipProfileProperties extends ClassificationBeanProperties
{
    private long              totalMembership = 0L;
    private Map<String, Long> typeMembership  = null;
    private long              anchoredTotalMembership = 0L;
    private Map<String, Long> anchoredTypeMembership  = null;
    private long              allTotalMembership = 0L;
    private Map<String, Long> allTypeMembership  = null;
    private Date              analysisTime    = null;


    /**
     * Default constructor
     */
    public ZoneMembershipProfileProperties()
    {
        super();
        super.typeName = OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ZoneMembershipProfileProperties(ZoneMembershipProfileProperties template)
    {
        super(template);

        if (template != null)
        {
            this.totalMembership = template.getTotalMembership();
            this.typeMembership = template.getTypeMembership();
            this.anchoredTotalMembership = template.getAnchoredTotalMembership();
            this.anchoredTypeMembership = template.getAnchoredTypeMembership();
            this.allTotalMembership = template.getAllTotalMembership();
            this.allTypeMembership = template.getAllTypeMembership();
            this.analysisTime = template.getAnalysisTime();
        }
    }


    /**
     * Return the total number of elements of all open metadata types in the zone.
     *
     * @return long count
     */
    public long getTotalMembership()
    {
        return totalMembership;
    }


    /**
     * Set up the total number of elements of all open metadata types in the zone.
     *
     * @param totalMembership long count
     */
    public void setTotalMembership(long totalMembership)
    {
        this.totalMembership = totalMembership;
    }


    /**
     * Return the list of open metadata type Names mapped to the number of instances of that type in the zone.
     *
     * @return string name
     */
    public Map<String, Long> getTypeMembership()
    {
        return typeMembership;
    }


    /**
     * Set up the list of zone names
     *
     * @param typeMembership string name
     */
    public void setTypeMembership(Map<String, Long> typeMembership)
    {
        this.typeMembership = typeMembership;
    }


    /**
     * Return the total anchored elements of all open metadata types in the zone.
     *
     * @return long count
     */
    public long getAnchoredTotalMembership()
    {
        return anchoredTotalMembership;
    }


    /**
     * Set up the total anchored elements of all open metadata types in the zone.
     *
     * @param anchoredTotalMembership long count
     */
    public void setAnchoredTotalMembership(long anchoredTotalMembership)
    {
        this.anchoredTotalMembership = anchoredTotalMembership;
    }


    /**
     * Return the list of open metadata type Names mapped to the count of anchored instances of that type in the zone.
     *
     * @return map
     */
    public Map<String, Long> getAnchoredTypeMembership()
    {
        return anchoredTypeMembership;
    }


    /**
     * Set up the list of open metadata type Names mapped to the count of anchored instances of that type in the zone.
     *
     * @param anchoredTypeMembership map
     */
    public void setAnchoredTypeMembership(Map<String, Long> anchoredTypeMembership)
    {
        this.anchoredTypeMembership = anchoredTypeMembership;
    }


    /**
     * Return the total elements of all open metadata types in the zone.
     *
     * @return long count
     */
    public long getAllTotalMembership()
    {
        return allTotalMembership;
    }


    /**
     * Set up the total elements of all open metadata types in the zone.
     *
     * @param allTotalMembership long count
     */
    public void setAllTotalMembership(long allTotalMembership)
    {
        this.allTotalMembership = allTotalMembership;
    }


    /**
     * Return the list of open metadata type names mapped to the count of all instances of that type in the zone.
     *
     * @return map
     */
    public Map<String, Long> getAllTypeMembership()
    {
        return allTypeMembership;
    }


    /**
     * Set up the list of open metadata type names mapped to the count of all instances of that type in the zone.
     *
     * @param allTypeMembership map
     */
    public void setAllTypeMembership(Map<String, Long> allTypeMembership)
    {
        this.allTypeMembership = allTypeMembership;
    }


    /**
     * Return the time the analysis was performed
     *
     * @return date/time
     */
    public Date getAnalysisTime()
    {
        return analysisTime;
    }


    /**
     * Set the time the analysis was performed
     *
     * @param analysisTime date/time
     */
    public void setAnalysisTime(Date analysisTime)
    {
        this.analysisTime = analysisTime;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ZoneMembershipProfileProperties{" +
                "totalMembership=" + totalMembership +
                ", typeMembership=" + typeMembership +
                ", anchoredTotalMembership=" + anchoredTotalMembership +
                ", anchoredTypeMembership=" + anchoredTypeMembership +
                ", allTotalMembership=" + allTotalMembership +
                ", allTypeMembership=" + allTypeMembership +
                ", analysisTime=" + analysisTime +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof ZoneMembershipProfileProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(typeMembership, that.typeMembership) &&
                totalMembership == that.totalMembership &&
                Objects.equals(anchoredTotalMembership, that.anchoredTotalMembership) &&
                Objects.equals(anchoredTypeMembership, that.anchoredTypeMembership) &&
                Objects.equals(allTotalMembership, that.allTotalMembership) &&
                Objects.equals(allTypeMembership, that.allTypeMembership) &&
                Objects.equals(analysisTime, that.analysisTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), totalMembership, typeMembership, anchoredTotalMembership,
                            anchoredTypeMembership, allTotalMembership, allTypeMembership, analysisTime);
    }
}
