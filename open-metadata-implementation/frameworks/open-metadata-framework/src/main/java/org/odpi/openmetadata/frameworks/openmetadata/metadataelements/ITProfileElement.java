/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ITProfileElement describes a system, any contact methods and linked userIds.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ITProfileElement implements MetadataElement
{
    private ElementHeader                elementHeader        = null;
    private ITProfileProperties          profileProperties    = null;
    private List<ContactMethodElement>   contactMethods       = null;
    private List<ProfileIdentityElement> userIdentities       = null;
    private List<ElementStub>            linkedInfrastructure = null;


    /**
     * Default Constructor
     */
    public ITProfileElement()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ITProfileElement(ITProfileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            profileProperties = template.getProfileProperties();
            contactMethods = template.getContactMethods();
            userIdentities = template.getUserIdentities();
            linkedInfrastructure = template.getLinkedInfrastructure();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the profile.
     *
     * @return  properties
     */
    public ITProfileProperties getProfileProperties()
    {
        return profileProperties;
    }


    /**
     * Set up the profile properties.
     *
     * @param profileProperties  properties
     */
    public void setProfileProperties(ITProfileProperties profileProperties)
    {
        this.profileProperties = profileProperties;
    }


    /**
     * Return the contact methods for this profile.
     *
     * @return list of contact methods
     */
    public List<ContactMethodElement> getContactMethods()
    {
        return contactMethods;
    }


    /**
     * Set up the contact methods for this profile.
     *
     * @param contactMethods list of contact methods
     */
    public void setContactMethods(List<ContactMethodElement> contactMethods)
    {
        this.contactMethods = contactMethods;
    }


    /**
     * Return the list of user identities for this profile.
     *
     * @return list of userIds
     */
    public List<ProfileIdentityElement> getUserIdentities()
    {
        return userIdentities;
    }


    /**
     * Set up the list of user identities for this profile.
     *
     * @param userIdentities list of userIds
     */
    public void setUserIdentities(List<ProfileIdentityElement> userIdentities)
    {
        this.userIdentities = userIdentities;
    }


    /**
     * Return the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @return list of element stubs
     */
    public List<ElementStub> getLinkedInfrastructure()
    {
        return linkedInfrastructure;
    }


    /**
     * Set up the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @param linkedInfrastructure list of element stubs
     */
    public void setLinkedInfrastructure(List<ElementStub> linkedInfrastructure)
    {
        this.linkedInfrastructure = linkedInfrastructure;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ITProfileElement{" +
                       "elementHeader=" + elementHeader +
                       ", profileProperties=" + profileProperties +
                       ", contactMethods=" + contactMethods +
                       ", userIdentities=" + userIdentities +
                       ", linkedInfrastructure=" + linkedInfrastructure +
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
        ITProfileElement that = (ITProfileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(profileProperties, that.profileProperties) &&
                       Objects.equals(contactMethods, that.contactMethods) &&
                       Objects.equals(userIdentities, that.userIdentities) &&
                       Objects.equals(linkedInfrastructure, that.linkedInfrastructure);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, profileProperties, contactMethods, userIdentities, linkedInfrastructure);
    }
}
