/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataUserDetails holds details of a particular user that can be shared between security services.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataUserDetails
{
    /*
     * Public properties of the user.
     */
    private String              userId            = null;
    private String              userName          = null;
    private String              distinguishedName = null;
    private List<String>        securityRoles     = null;
    private List<String>        securityGroups    = null;
    private Map<String, Object> otherProperties   = null;


    /**
     * Default constructor
     */
    public OpenMetadataUserDetails()
    {
    }


    /**
     * Return the userId that identifies the account.
     *
     * @return string identifier
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the userId that identifies the account.
     *
     * @param userId string identifier
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return the name of the user.
     *
     * @return string name
     */
    public String getUserName()
    {
        return userName;
    }


    /**
     * Set up the name of the user.
     *
     * @param userName string name
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }


    /**
     * Return the distinguished name of the user.
     *
     * @return string name
     */
    public String getDistinguishedName()
    {
        return distinguishedName;
    }


    /**
     * Set up the distinguished name of the user.
     *
     * @param distinguishedName string name
     */
    public void setDistinguishedName(String distinguishedName)
    {
        this.distinguishedName = distinguishedName;
    }


    /**
     * Return the list of security roles assigned to the user.
     *
     * @return list of names
     */
    public List<String> getSecurityRoles()
    {
        return securityRoles;
    }


    /**
     * Set up the list of security roles assigned to the user.
     *
     * @param securityRoles list of names
     */
    public void setSecurityRoles(List<String> securityRoles)
    {
        this.securityRoles = securityRoles;
    }


    /**
     * Return the list of security roles assigned to the user.
     *
     * @return list of names
     */
    public List<String> getSecurityGroups()
    {
        return securityGroups;
    }


    /**
     * Set upi the list of security groups that the user belongs to.
     *
     * @param securityGroups list of names
     */
    public void setSecurityGroups(List<String> securityGroups)
    {
        this.securityGroups = securityGroups;
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
        return "OpenMetadataUserDetails{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", distinguishedName='" + distinguishedName + '\'' +
                ", securityRoles=" + securityRoles +
                ", securityGroups=" + securityGroups +
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
        OpenMetadataUserDetails that = (OpenMetadataUserDetails) objectToCompare;
        return Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) &&
                Objects.equals(distinguishedName, that.distinguishedName) &&
                Objects.equals(securityRoles, that.securityRoles) &&
                Objects.equals(securityGroups, that.securityGroups) &&
                Objects.equals(otherProperties, that.otherProperties);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, userName, distinguishedName, securityRoles, securityGroups, otherProperties);
    }
}
