/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Provides the parameters passed on each API operation to control the metadata queries
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BasicOptions
{
    private List<String> governanceZoneFilter   = null;
    private boolean      forLineage             = false;
    private boolean      forDuplicateProcessing = false;
    private Date         effectiveTime          = null;

    /**
     * Default constructor
     */
    public BasicOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BasicOptions(BasicOptions template)
    {
        if (template != null)
        {
            forLineage             = template.getForLineage();
            forDuplicateProcessing = template.getForDuplicateProcessing();
            effectiveTime          = template.getEffectiveTime();
            governanceZoneFilter   = template.getGovernanceZoneFilter();
        }
    }


    /**
     * Return the list of governance zones to restrict the assets (if any) returned to members of these
     * zones.  If the value is null (the default) then all matching assets are returned, subject to
     * the usual security restrictions.
     *
     * @return list of governance zone names
     */
    public List<String> getGovernanceZoneFilter()
    {
        return governanceZoneFilter;
    }


    /**
     * Set up the list of governance zones to restrict the assets (if any) returned to members of these
     * zones.  If the value is null (the default) then all matching assets are returned, subject to
     * the usual security restrictions.
     *
     * @param governanceZoneFilter list of governance zone names
     */
    public void setGovernanceZoneFilter(List<String> governanceZoneFilter)
    {
        this.governanceZoneFilter = governanceZoneFilter;
    }


    /**
     * Return whether this request is to update lineage memento elements.
     *
     * @return flag
     */
    public boolean getForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether this request is to update lineage memento elements.
     *
     * @param forLineage flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return whether this request is updating an element as part of a deduplication exercise.
     *
     * @return flag
     */
    public boolean getForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether this request is updating an element as part of a deduplication exercise.
     *
     * @param forDuplicateProcessing flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Return the effective time that this update is to occur in.
     *
     * @return date/time
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up the effective time that this update is to occur in.
     *
     * @param effectiveTime date/time
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "BasicOptions{" +
                "governanceZoneFilter=" + governanceZoneFilter +
                ", forLineage=" + forLineage +
                ", forDuplicateProcessing=" + forDuplicateProcessing +
                ", effectiveTime=" + effectiveTime +  '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        BasicOptions that = (BasicOptions) objectToCompare;
        return forLineage == that.forLineage &&
                forDuplicateProcessing == that.forDuplicateProcessing &&
                Objects.equals(governanceZoneFilter, that.governanceZoneFilter) &&
                Objects.equals(effectiveTime, that.effectiveTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(forLineage, forDuplicateProcessing, governanceZoneFilter, effectiveTime);
    }
}
