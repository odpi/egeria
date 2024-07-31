/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TermAssignmentStatusesResponse is a response object for passing back a list of enum values or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermAssignmentStatusesResponse extends FFDCResponseBase
{
    private List<GlossaryTermAssignmentStatus> statuses = null;


    /**
     * Default constructor
     */
    public TermAssignmentStatusesResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TermAssignmentStatusesResponse(TermAssignmentStatusesResponse template)
    {
        super(template);

        if (template != null)
        {
            statuses = template.getStatuses();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public List<GlossaryTermAssignmentStatus> getStatuses()
    {
        return statuses;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param statuses result object
     */
    public void setStatuses(List<GlossaryTermAssignmentStatus> statuses)
    {
        this.statuses = statuses;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TermAssignmentStatusesResponse{" +
                "statuses=" + statuses +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        TermAssignmentStatusesResponse that = (TermAssignmentStatusesResponse) objectToCompare;
        return Objects.equals(statuses, that.statuses);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), statuses);
    }
}
