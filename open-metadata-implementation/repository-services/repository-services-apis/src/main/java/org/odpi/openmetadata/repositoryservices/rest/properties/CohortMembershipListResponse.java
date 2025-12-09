/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortMembershipListResponse support an OMRS REST API response that returns a list of cohort member registration objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortMembershipListResponse extends OMRSAPIPagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<MemberRegistration> cohortMembers = null;


    /**
     * Default constructor
     */
    public CohortMembershipListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CohortMembershipListResponse(CohortMembershipListResponse template)
    {
        super(template);

        if (template != null)
        {
            cohortMembers = template.getCohortMembers();
        }
    }


    /**
     * Return the list of cohortMembers.
     *
     * @return entity list
     */
    public List<MemberRegistration> getCohortMembers()
    {
        if (cohortMembers == null)
        {
            return null;
        }
        else if (cohortMembers.isEmpty())
        {
            return null;
        }
        else
        {
            return cohortMembers;
        }
    }


    /**
     * Set up the list of cohortMembers.
     *
     * @param cohortMembers entity list
     */
    public void setCohortMembers(List<MemberRegistration> cohortMembers)
    {
        this.cohortMembers = cohortMembers;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CohortMembershipListResponse{" +
                "cohortMembers=" + cohortMembers +
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
        if (!(objectToCompare instanceof CohortMembershipListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CohortMembershipListResponse
                that = (CohortMembershipListResponse) objectToCompare;
        return Objects.equals(getCohortMembers(), that.getCohortMembers());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getCohortMembers());
    }
}
