/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortListResponse support an OMRS REST API response that returns a list of cohort description objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortListResponse extends OMRSAPIPagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<CohortDescription> cohorts = null;


    /**
     * Default constructor
     */
    public CohortListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CohortListResponse(CohortListResponse template)
    {
        super(template);

        if (template != null)
        {
            cohorts = template.getCohorts();
        }
    }


    /**
     * Return the list of cohorts.
     *
     * @return entity list
     */
    public List<CohortDescription> getCohorts()
    {
        if (cohorts == null)
        {
            return null;
        }
        else if (cohorts.isEmpty())
        {
            return null;
        }
        else
        {
            return cohorts;
        }
    }


    /**
     * Set up the list of cohorts.
     *
     * @param cohorts entity list
     */
    public void setCohorts(List<CohortDescription> cohorts)
    {
        this.cohorts = cohorts;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CohortListResponse{" +
                "cohorts=" + cohorts +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof CohortListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CohortListResponse
                that = (CohortListResponse) objectToCompare;
        return Objects.equals(getCohorts(), that.getCohorts());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getCohorts());
    }
}
