/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContactMethodProperties describes a single mechanism that can be used to contact an individual.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContactMethodProperties implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String               name = null;
    private String               contactType = null;

    private ContactMethodType    contactMethodType    = null;
    private String               contactMethodService = null;
    private String               contactMethodValue   = null;

    private Date                 effectiveFrom = null;
    private Date                 effectiveTo   = null;

    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;


    /**
     * Default constructor
     */
    public ContactMethodProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContactMethodProperties(ContactMethodProperties template)
    {
        if (template != null)
        {
            contactMethodType = template.getContactMethodType();
            contactMethodService = template.getContactMethodService();
            contactMethodValue = template.getContactMethodValue();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            typeName = template.getTypeName();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the name to give this contact method (imagine a list of contact methods).
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name to give this contact method (imagine a list of contact methods).
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
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
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the name of the open metadata type for this metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContactMethodProperties{" +
                       "type=" + contactMethodType +
                       ", service='" + contactMethodService + '\'' +
                       ", value='" + contactMethodValue + '\'' +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", typeName='" + typeName + '\'' +
                       ", extendedProperties=" + extendedProperties +
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
        ContactMethodProperties that = (ContactMethodProperties) objectToCompare;
        return contactMethodType == that.contactMethodType &&
                       Objects.equals(contactMethodService, that.contactMethodService) &&
                       Objects.equals(contactMethodValue, that.contactMethodValue) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(typeName, that.typeName) &&
                       Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(contactMethodType, contactMethodService, contactMethodValue, effectiveFrom, effectiveTo, typeName, extendedProperties);
    }
}
