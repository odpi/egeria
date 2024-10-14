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
 * UserAccount described details of a user account for the open metadata ecosystem.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccount
{
    private String                            userName          = null;
    private String                            distinguishedName = null;
    private List<String>                      securityRoles     = null;
    private List<String>                      securityGroups    = null;
    private Map<String, List<ZoneAccessType>> zoneAccess        = null;
    private Map<String, Object>               otherProperties   = null;
    private UserAccountStatus                 userAccountStatus = UserAccountStatus.DISABLED;
    private Map<String, String>               secrets           = null;


    /**
     * Default constructor
     */
    public UserAccount()
    {
        super();
    }



    /**
     * Copy constructor
     */
    public UserAccount(UserAccount template)
    {
        if (template != null)
        {
            this.userName = template.getUserName();
            this.distinguishedName = template.getDistinguishedName();
            this.securityRoles = template.getSecurityRoles();
            this.securityGroups = template. getSecurityGroups();
            this.zoneAccess = template.getZoneAccess();
            this.otherProperties = template.getOtherProperties();
            this.userAccountStatus = template.getUserAccountStatus();
            this.secrets = template.getSecrets();
        }
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
     * Return details of the actions that are permitted on particular Governance Zones.
     *
     * @return map of accesses
     */
    public Map<String, List<ZoneAccessType>> getZoneAccess()
    {
        return zoneAccess;
    }


    /**
     * Set up details of the actions that are permitted on particular Governance Zones.
     *
     * @param zoneAccess map of accesses
     */
    public void setZoneAccess(Map<String, List<ZoneAccessType>> zoneAccess)
    {
        this.zoneAccess = zoneAccess;
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
     * Return the status of the account.
     *
     * @return status enum
     */
    public UserAccountStatus getUserAccountStatus()
    {
        return userAccountStatus;
    }


    /**
     * Set up the status of the account.
     *
     * @param userAccountStatus status enum
     */
    public void setUserAccountStatus(UserAccountStatus userAccountStatus)
    {
        this.userAccountStatus = userAccountStatus;
    }


    /**
     * Return the secrets associated with the user account.
     *
     * @return a map of secret names to secret values
     */
    public Map<String, String> getSecrets()
    {
        return secrets;
    }


    /**
     * Set up the secrets associated with the user account.
     *
     * @param secrets a map of secret names to secret values
     */
    public void setSecrets(Map<String, String> secrets)
    {
        this.secrets = secrets;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "UserAccount{" +
                "userName='" + userName + '\'' +
                ", distinguishedName='" + distinguishedName + '\'' +
                ", securityRoles=" + securityRoles +
                ", securityGroups=" + securityGroups +
                ", otherProperties=" + otherProperties +
                ", userAccountStatus=" + userAccountStatus +
                ", secrets=" + secrets +
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
        UserAccount that = (UserAccount) objectToCompare;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(distinguishedName, that.distinguishedName) &&
                Objects.equals(securityRoles, that.securityRoles) &&
                Objects.equals(securityGroups, that.securityGroups) &&
                Objects.equals(otherProperties, that.otherProperties) &&
                userAccountStatus == that.userAccountStatus &&
                Objects.equals(secrets, that.secrets);
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userName, distinguishedName, securityRoles, securityGroups, otherProperties, userAccountStatus, secrets);
    }
}
