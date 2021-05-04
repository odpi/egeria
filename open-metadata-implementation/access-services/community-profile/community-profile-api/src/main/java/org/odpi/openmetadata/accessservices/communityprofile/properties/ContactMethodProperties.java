/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContactMethod describes a single mechanism that can be used to contact an individual.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContactMethod extends CommonHeader
{
    private static final long    serialVersionUID = 1L;

    private ContactMethodType    type = null;
    private String               service = null;
    private String               value = null;


    /**
     * Default constructor
     */
    public ContactMethod()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContactMethod(ContactMethod  template)
    {
        super(template);

        if (template != null)
        {
            type = template.getType();
            service = template.getService();
            value = template.getValue();
        }
    }


    /**
     * Return the type of the contact method.
     *
     * @return contact method type enum
     */
    public ContactMethodType getType()
    {
        return type;
    }


    /**
     * Set up the type of the contact method.
     *
     * @param type contact method type enum
     */
    public void setType(ContactMethodType type)
    {
        this.type = type;
    }


    /**
     * Return the URL of the service used to contact the individual.
     *
     * @return service URL
     */
    public String getService()
    {
        return service;
    }


    /**
     * Set up theURL of the service used to contact the individual.
     *
     * @param service service URL
     */
    public void setService(String service)
    {
        this.service = service;
    }


    /**
     * Return the account name or similar value used to direct the message to the individual.
     *
     * @return value string
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Set up the account name or similar value used to direct the message to the individual.
     *
     * @param value value string
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContactMethod{" +
                "type=" + type +
                ", service='" + service + '\'' +
                ", value='" + value + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ContactMethod that = (ContactMethod) objectToCompare;
        return getType() == that.getType() &&
                Objects.equals(getService(), that.getService()) &&
                Objects.equals(getValue(), that.getValue());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getType(), getService(), getValue());
    }
}
