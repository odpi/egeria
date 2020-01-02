/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActorHeader covers the properties that are common between a Team and a Person.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PersonalProfile.class, name = "PersonalProfile"),
        @JsonSubTypes.Type(value = TeamSummary.class, name = "TeamSummary")
})
public abstract class ActorHeader extends ReferenceableHeader
{
    private static final long    serialVersionUID = 1L;

    private List<ContactMethod>    contactDetails = null;

    /**
     * Default constructor
     */
    public ActorHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorHeader(ActorHeader template)
    {
        super(template);

        if (template != null)
        {
            contactDetails = template.getContactDetails();
        }
    }


    /**
     * Return the contact details for this person or team.
     *
     * @return list of contact methods
     */
    public List<ContactMethod> getContactDetails()
    {
        if (contactDetails == null)
        {
            return null;
        }
        else if (contactDetails.isEmpty())
        {
            return null;
        }
        else
        {
            List<ContactMethod>  returnList = new ArrayList<>();

            for (ContactMethod   contactMethod : contactDetails)
            {
                returnList.add(new ContactMethod(contactMethod));
            }

            return returnList;
        }
    }


    /**
     * Set up the contact details for the person or team.
     *
     * @param contactDetails list of contact methods
     */
    public void setContactDetails(List<ContactMethod> contactDetails)
    {
        this.contactDetails = contactDetails;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActorHeader{" +
                "contactDetails=" + contactDetails +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        ActorHeader that = (ActorHeader) objectToCompare;
        return Objects.equals(getContactDetails(), that.getContactDetails());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getContactDetails());
    }
}
