/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a member of the team responsible for a Bitol Open Data Contract Standard (ODCS) data
 * contract or Open Data Product Standard (ODPS) data product.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolTeamMember
{
    private String                             id = null;
    private String                             username = null;
    private String                             name = null;
    private String                             description = null;
    private String                             role = null;
    private String                             dateIn = null;
    private String                             dateOut = null;
    private String                             replacedByUsername = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public BitolTeamMember()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the username or email address of the team member.
     *
     * @return string user identifier
     */
    public String getUsername()
    {
        return username;
    }


    /**
     * Set up the username or email address of the team member.
     *
     * @param username string user identifier
     */
    public void setUsername(String username)
    {
        this.username = username;
    }


    /**
     * Return the name of the team member.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the team member.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the team member.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the team member.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the job role of the team member, for example owner or data steward.
     *
     * @return string role name
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Set up the job role of the team member, for example owner or data steward.
     *
     * @param role string role name
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * Return the date (ISO 8601) when the team member joined the team.
     *
     * @return string date
     */
    public String getDateIn()
    {
        return dateIn;
    }


    /**
     * Set up the date (ISO 8601) when the team member joined the team.
     *
     * @param dateIn string date
     */
    public void setDateIn(String dateIn)
    {
        this.dateIn = dateIn;
    }


    /**
     * Return the date (ISO 8601) when the team member left the team.
     *
     * @return string date
     */
    public String getDateOut()
    {
        return dateOut;
    }


    /**
     * Set up the date (ISO 8601) when the team member left the team.
     *
     * @param dateOut string date
     */
    public void setDateOut(String dateOut)
    {
        this.dateOut = dateOut;
    }


    /**
     * Return the username of the person who replaced this team member.
     *
     * @return string user identifier
     */
    public String getReplacedByUsername()
    {
        return replacedByUsername;
    }


    /**
     * Set up the username of the person who replaced this team member.
     *
     * @param replacedByUsername string user identifier
     */
    public void setReplacedByUsername(String replacedByUsername)
    {
        this.replacedByUsername = replacedByUsername;
    }


    /**
     * Return the list of tags attached to this element.
     *
     * @return list of tag strings
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the list of tags attached to this element.
     *
     * @param tags list of tag strings
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolTeamMember{" +
                       "id='" + id + '\'' +
                       ", username='" + username + '\'' +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", role='" + role + '\'' +
                       ", dateIn='" + dateIn + '\'' +
                       ", dateOut='" + dateOut + '\'' +
                       ", replacedByUsername='" + replacedByUsername + '\'' +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        BitolTeamMember that = (BitolTeamMember) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(username, that.username) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(role, that.role) &&
                       Objects.equals(dateIn, that.dateIn) &&
                       Objects.equals(dateOut, that.dateOut) &&
                       Objects.equals(replacedByUsername, that.replacedByUsername) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, name, description, role, dateIn, dateOut, replacedByUsername, tags, customProperties, authoritativeDefinitions);
    }
}
