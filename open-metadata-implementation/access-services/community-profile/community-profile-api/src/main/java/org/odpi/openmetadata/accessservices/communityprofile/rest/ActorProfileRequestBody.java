/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActorProfileRequestBody provides the request body payload for working on ActorProfile entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActorProfileRequestBody extends ExternalSourceRequestBody
{
    private static final long    serialVersionUID = 1L;

    private ActorProfileProperties properties         = null;
    private ContributionRecord     contributionRecord = null;

    /**
     * Default constructor
     */
    public ActorProfileRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorProfileRequestBody(ActorProfileRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
            this.contributionRecord = template.getContributionRecord();
        }
    }


    /**
     * Return the properties for this profile.
     *
     * @return properties bean
     */
    public ActorProfileProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for this profile.
     *
     * @param properties properties bean
     */
    public void setProperties(ActorProfileProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the optional contribution record for the profile.  If this is not null, the contribution set up.
     *
     * @return record
     */
    public ContributionRecord getContributionRecord()
    {
        return contributionRecord;
    }


    /**
     * Set up the optional contribution record for the profile.  If this is not null, the contribution set up.
     *
     * @param contributionRecord record
     */
    public void setContributionRecord(ContributionRecord contributionRecord)
    {
        this.contributionRecord = contributionRecord;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActorProfileRequestBody{" +
                       "properties=" + properties +
                       ", contributionRecord=" + contributionRecord +
                       ", externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ActorProfileRequestBody that = (ActorProfileRequestBody) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                       Objects.equals(contributionRecord, that.contributionRecord);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, contributionRecord);
    }
}
