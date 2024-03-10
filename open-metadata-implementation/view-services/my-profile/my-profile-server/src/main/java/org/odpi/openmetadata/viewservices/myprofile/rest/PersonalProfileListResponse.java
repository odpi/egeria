/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.rest.CommunityProfileOMASAPIResponse;
import org.odpi.openmetadata.viewservices.myprofile.metadataelements.PersonalProfileUniverse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PersonalProfileListResponse is the response structure used on the OMAS REST API calls that return
 * a list of personal profile objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileListResponse extends CommunityProfileOMASAPIResponse
{
    private List<PersonalProfileUniverse> personalProfiles = null;


    /**
     * Default constructor
     */
    public PersonalProfileListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileListResponse(PersonalProfileListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.personalProfiles = template.getPersonalProfiles();
        }
    }


    /**
     * Return the personalProfile result.
     *
     * @return unique identifier
     */
    public List<PersonalProfileUniverse> getPersonalProfiles()
    {
        if (personalProfiles == null)
        {
            return null;
        }
        else if (personalProfiles.isEmpty())
        {
            return null;
        }
        else
        {
            return personalProfiles;
        }
    }


    /**
     * Set up the personalProfile result.
     *
     * @param personalProfiles - unique identifier
     */
    public void setPersonalProfiles(List<PersonalProfileUniverse> personalProfiles)
    {
        this.personalProfiles = personalProfiles;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonalProfileListResponse{" +
                "personalProfiles=" + personalProfiles +
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
        if (!(objectToCompare instanceof PersonalProfileListResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getPersonalProfiles(), that.getPersonalProfiles());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), personalProfiles);
    }
}
