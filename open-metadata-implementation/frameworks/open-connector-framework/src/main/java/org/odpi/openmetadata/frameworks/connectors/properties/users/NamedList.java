/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NamedList described a list of users, or other named list.  A named list can represent a security group,
 * defining who has access to a specific resource, or a security role, defining who is performing a specific role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NamedList
{
    private String              displayName       = null;
    private String              description       = null;
    private String              distinguishedName = null;
    private List<String>        userMembers       = null;
    private List<String>        listMembers       = null;
    private Map<String, Object> otherProperties   = null;



    /**
     * Default constructor
     */
    public NamedList()
    {
        super();
    }


    /**
     * Copy constructor
     */
    public NamedList(NamedList template)
    {
        if (template != null)
        {
            this.displayName       = template.getDisplayName();
            this.description       = template.getDescription();
            this.distinguishedName = template.getDistinguishedName();
            this.userMembers       = template.getUserMembers();
            this.listMembers       = template.getListMembers();
            this.otherProperties   = template.getOtherProperties();
        }
    }


    /**
     * Return the display name for this list.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this list.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of this list.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this list.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the distinguished name of the list.
     *
     * @return string name
     */
    public String getDistinguishedName()
    {
        return distinguishedName;
    }


    /**
     * Set up the distinguished name of the list.
     *
     * @param distinguishedName string name
     */
    public void setDistinguishedName(String distinguishedName)
    {
        this.distinguishedName = distinguishedName;
    }


    /**
     * Return the list of members - typically this is the distinguished names of the members.
     *
     * @return list of names
     */
    public List<String> getUserMembers()
    {
        return userMembers;
    }


    /**
     * Set up the list of user account members - typically this is the distinguished names of the members.
     *
     * @param userMembers list of names
     */
    public void setUserMembers(List<String> userMembers)
    {
        this.userMembers = userMembers;
    }


    /**
     * Return the list of nested lists - typically this is the distinguished names of the members.
     *
     * @return list of names
     */
    public List<String> getListMembers()
    {
        return listMembers;
    }


    /**
     * Set up list of nested lists - typically this is the distinguished names of the members.
     *
     * @param listMembers list of names
     */
    public void setListMembers(List<String> listMembers)
    {
        this.listMembers = listMembers;
    }


    /**
     * Return any other properties that should be shared with security providers.
     *
     * @return property map
     */
    public Map<String, Object> getOtherProperties()
    {
        return otherProperties;
    }


    /**
     * Set up any other properties that should be shared with security providers.
     *
     * @param otherProperties property map
     */
    public void setOtherProperties(Map<String, Object> otherProperties)
    {
        this.otherProperties = otherProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NamedList{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", distinguishedName='" + distinguishedName + '\'' +
                ", userMembers=" + userMembers +
                ", listMembers=" + listMembers +
                ", otherProperties=" + otherProperties +
                '}';
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        NamedList that = (NamedList) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(distinguishedName, that.distinguishedName) &&
                Objects.equals(userMembers, that.userMembers) &&
                Objects.equals(listMembers, that.listMembers) &&
                Objects.equals(otherProperties, that.otherProperties) ;
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(displayName, description, distinguishedName, userMembers, listMembers, otherProperties);
    }
}
