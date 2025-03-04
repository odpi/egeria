/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindAssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRequest provides the base class for many query requests.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class FindRequest
{
    private Date                effectiveTime        = null;
    private Date                asOfTime             = null;

    /**
     * Default constructor
     */
    public FindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public FindRequest(FindRequest template)
    {
        if (template != null)
        {
            effectiveTime        = template.getEffectiveTime();
            asOfTime             = template.getAsOfTime();
        }
    }


    /**
     * Return the date/time to use for the query.
     *
     * @return date object
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up the date/time to use for the query.
     *
     * @param effectiveTime date object
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * Return the time used for a historical query - null mean current repository contents.
     *
     * @return date/time object
     */
    public Date getAsOfTime()
    {
        return asOfTime;
    }


    /**
     * Set up the time used for a historical query - null mean current repository contents.
     *
     * @param asOfTime date/time object
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindRequest{" +
                "effectiveTime=" + effectiveTime +
                ", asOfTime=" + asOfTime +
                '}';
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindRequest that = (FindRequest) objectToCompare;
        return Objects.equals(effectiveTime, that.effectiveTime) &&
                Objects.equals(asOfTime, that.asOfTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(effectiveTime, asOfTime);
    }
}