/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupsResponse provides a response object for returning information about a
 * list of integration groups that are configured in an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationGroupsResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<IntegrationGroupConfig> groups;


    /**
     * Default constructor
     */
    public IntegrationGroupsResponse()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public IntegrationGroupsResponse(IntegrationGroupsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.groups = template.getGroups();
        }
    }


    /**
     * Return the list of groups
     *
     * @return group descriptions
     */
    public List<IntegrationGroupConfig> getGroups()
    {
        if (groups == null)
        {
            return null;
        }
        else if (groups.isEmpty())
        {
            return null;
        }
        else
        {
            return groups;
        }
    }


    /**
     * Set up the list of groups
     *
     * @param groups groups
     */
    public void setGroups(List<IntegrationGroupConfig> groups)
    {
        this.groups = groups;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupsResponse{" +
                "groups=" + groups +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        IntegrationGroupsResponse that = (IntegrationGroupsResponse) objectToCompare;
        return Objects.equals(getGroups(), that.getGroups());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getGroups());
    }
}
