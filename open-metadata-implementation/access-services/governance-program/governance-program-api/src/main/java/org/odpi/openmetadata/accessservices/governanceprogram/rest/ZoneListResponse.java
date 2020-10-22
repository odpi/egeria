/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ZoneListResponse is the response structure used on the OMAS REST API calls that return a list of governance zones.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ZoneListResponse extends GovernanceProgramOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceZoneElement> governanceZones = null;


    /**
     * Default constructor
     */
    public ZoneListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ZoneListResponse(ZoneListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.governanceZones = template.getGovernanceZones();
        }
    }


    /**
     * Return the governanceZone result.
     *
     * @return list of governance zone objects
     */
    public List<GovernanceZoneElement> getGovernanceZones()
    {
        if (governanceZones == null)
        {
            return null;
        }
        else if (governanceZones.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(governanceZones);
    }


    /**
     * Set up the governanceZones result.
     *
     * @param governanceZones list of results
     */
    public void setGovernanceZone(List<GovernanceZoneElement> governanceZones)
    {
        this.governanceZones = governanceZones;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ZoneListResponse{" +
                "governanceZones=" + governanceZones +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ZoneListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ZoneListResponse that = (ZoneListResponse) objectToCompare;
        return Objects.equals(governanceZones, that.governanceZones);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceZones);
    }
}
