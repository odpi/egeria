/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContactDetailsProperties describes a single mechanism that can be used to contact an individual.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContactDetailsProperties extends ReferenceableProperties
{
    private String            contactType          = null;
    private ContactMethodType contactMethodType    = null;
    private String            contactMethodService = null;
    private String            contactMethodValue   = null;

    /**
     * Default constructor
     */
    public ContactDetailsProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTACT_DETAILS.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContactDetailsProperties(ContactDetailsProperties template)
    {
        super(template);

        if (template != null)
        {
            contactType = template.getContactType();
            contactMethodType = template.getContactMethodType();
            contactMethodService = template.getContactMethodService();
            contactMethodValue = template.getContactMethodValue();
        }
    }


    /**
     * Return the type of contact - is it related to work or personal etc.
     *
     * @return string type name - often controlled by a valid value set
     */
    public String getContactType()
    {
        return contactType;
    }


    /**
     * Set up the type of contact - is it related to work or personal etc.
     *
     * @param contactType string type name - often controlled by a valid value set
     */
    public void setContactType(String contactType)
    {
        this.contactType = contactType;
    }


    /**
     * Return the type of the contact method.
     *
     * @return contact method type enum
     */
    public ContactMethodType getContactMethodType()
    {
        return contactMethodType;
    }


    /**
     * Set up the type of the contact method.
     *
     * @param contactMethodType contact method type enum
     */
    public void setContactMethodType(ContactMethodType contactMethodType)
    {
        this.contactMethodType = contactMethodType;
    }


    /**
     * Return the URL of the service used to contact the individual.
     *
     * @return service URL
     */
    public String getContactMethodService()
    {
        return contactMethodService;
    }


    /**
     * Set up theURL of the service used to contact the individual.
     *
     * @param contactMethodService service URL
     */
    public void setContactMethodService(String contactMethodService)
    {
        this.contactMethodService = contactMethodService;
    }


    /**
     * Return the account name or similar value used to direct the message to the individual.
     *
     * @return value string
     */
    public String getContactMethodValue()
    {
        return contactMethodValue;
    }


    /**
     * Set up the account name or similar value used to direct the message to the individual.
     *
     * @param contactMethodValue value string
     */
    public void setContactMethodValue(String contactMethodValue)
    {
        this.contactMethodValue = contactMethodValue;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContactDetailsProperties{" +
                "contactType='" + contactType + '\'' +
                ", contactMethodType=" + contactMethodType +
                ", contactMethodService='" + contactMethodService + '\'' +
                ", contactMethodValue='" + contactMethodValue + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ContactDetailsProperties that = (ContactDetailsProperties) objectToCompare;
        return Objects.equals(contactType, that.contactType) &&
                contactMethodType == that.contactMethodType &&
                Objects.equals(contactMethodService, that.contactMethodService) &&
                Objects.equals(contactMethodValue, that.contactMethodValue);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contactType, contactMethodType, contactMethodService, contactMethodValue);
    }
}
