/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
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
public class CohortMembershipResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private MemberRegistration cohortMember = null;


    /**
     * Default constructor
     */
    public CohortMembershipResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CohortMembershipResponse(CohortMembershipResponse template)
    {
        super(template);

        if (template != null)
        {
            cohortMember = template.getCohortMember();
        }
    }


    /**
     * Return the list of cohortMember.
     *
     * @return entity list
     */
    public MemberRegistration getCohortMember()
    {
        return cohortMember;
    }


    /**
     * Set up the list of cohortMember.
     *
     * @param cohortMember entity list
     */
    public void setCohortMember(MemberRegistration cohortMember)
    {
        this.cohortMember = cohortMember;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CohortMembershipResponse{" +
                "cohortMember=" + cohortMember +
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
        if (!(objectToCompare instanceof CohortMembershipResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CohortMembershipResponse
                that = (CohortMembershipResponse) objectToCompare;
        return Objects.equals(getCohortMember(), that.getCohortMember());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getCohortMember());
    }
}
