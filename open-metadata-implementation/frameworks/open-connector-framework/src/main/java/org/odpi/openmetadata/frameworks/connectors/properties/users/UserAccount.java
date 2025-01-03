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
 * UserAccount described details of a user account for the open metadata ecosystem.  This could be
 * for a person or system.  The fields are informed from the LDAP inetOrgPerson attributes (see RFC 2798).
 * However, many organizations develop their own definitions of a user and so this object includes
 * otherProperties to allow for extensions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccount
{
    private String                             userName          = null;
    private UserAccountType                    userAccountType   = null;
    private String                             employeeNumber    = null;
    private String                             employeeType      = null;
    private String                             displayName       = null;
    private String                             givenName         = null;
    private String                             surname           = null;
    private String                             email             = null;
    private String                             manager           = null;
    private String                             distinguishedName = null;
    private List<String>                       securityRoles     = null;
    private List<String>                       securityGroups    = null;
    private Map<String, List<AccessOperation>> zoneAccess        = null;
    private Map<String, Object>                otherProperties   = null;
    private UserAccountStatus                  userAccountStatus = UserAccountStatus.DISABLED;
    private Map<String, String>                secrets           = null;


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
            this.userName        = template.getUserName();
            this.userAccountType = template.getAccountType();
            this.employeeNumber  = template.getEmployeeNumber();
            this.employeeType = template.getEmployeeType();
            this.displayName = template.getDisplayName();
            this.givenName = template.getGivenName();
            this.surname = template.getSurname();
            this.email = template.getEmail();
            this.manager = template.getManager();
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
     * Return whether this account for a person or automation.
     *
     * @return account type enum
     */
    public UserAccountType getAccountType()
    {
        return userAccountType;
    }


    /**
     * Set up whether this account for a person or automation.
     *
     * @param userAccountType enum
     */
    public void setAccountType(UserAccountType userAccountType)
    {
        this.userAccountType = userAccountType;
    }


    /**
     * Return the employee number (maybe called personnel number, or similar name).
     *
     * @return string
     */
    public String getEmployeeNumber()
    {
        return employeeNumber;
    }


    /**
     * Set up the employee number (maybe called personnel number, or similar name).
     *
     * @param employeeNumber string
     */
    public void setEmployeeNumber(String employeeNumber)
    {
        this.employeeNumber = employeeNumber;
    }


    /**
     * Return the type of employee (for example, are they full time, contractor etc).  Each organization has their own definitions for these values.
     *
     * @return string
     */
    public String getEmployeeType()
    {
        return employeeType;
    }


    /**
     * Set up the type of employee contract (for example, are they full time, contractor etc).  Each organization has their own definitions for these values.
     *
     * @param employeeType string
     */
    public void setEmployeeType(String employeeType)
    {
        this.employeeType = employeeType;
    }


    /**
     * Return the display name for this user (for example, the preferred name of a person).
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this user (for example, the preferred name of a person).
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return a person's given name.
     *
     * @return string
     */
    public String getGivenName()
    {
        return givenName;
    }


    /**
     * Set up a person's given name.
     *
     * @param givenName string
     */
    public void setGivenName(String givenName)
    {
        this.givenName = givenName;
    }


    /**
     * Return a person's surname.
     *
     * @return string
     */
    public String getSurname()
    {
        return surname;
    }


    /**
     * Set up a person's surname.
     *
     * @param surname string
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }


    /**
     * Return a person's contact email.
     *
     * @return string
     */
    public String getEmail()
    {
        return email;
    }


    /**
     * Set up a person's contact email.
     *
     * @param email string
     */
    public void setEmail(String email)
    {
        this.email = email;
    }


    /**
     * Return a person's manager.  Typically expressed as a distinguished name.
     * This is typically the person who authorizes access for this person.
     *
     * @return string
     */
    public String getManager()
    {
        return manager;
    }


    /**
     * Set up a person's manager.  Typically expressed as a distinguished name.
     * This is typically the person who authorizes access for this person.
     *
     * @param manager string
     */
    public void setManager(String manager)
    {
        this.manager = manager;
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
    public Map<String, List<AccessOperation>> getZoneAccess()
    {
        return zoneAccess;
    }


    /**
     * Set up details of the actions that are permitted on particular Governance Zones.
     *
     * @param zoneAccess map of accesses
     */
    public void setZoneAccess(Map<String, List<AccessOperation>> zoneAccess)
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
                ", userAccountType='" + userAccountType + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", employeeType='" + employeeType + '\'' +
                ", displayName='" + displayName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", manager='" + manager + '\'' +
                ", distinguishedName='" + distinguishedName + '\'' +
                ", securityRoles=" + securityRoles +
                ", securityGroups=" + securityGroups +
                ", zoneAccess=" + zoneAccess +
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
                userAccountType == that.userAccountType &&
                Objects.equals(employeeNumber, that.employeeNumber) &&
                Objects.equals(employeeType, that.employeeType) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(givenName, that.givenName) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(manager, that.manager) &&
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
        return Objects.hash(userName, userAccountType, employeeNumber, employeeType, displayName, givenName, surname, email, manager, distinguishedName, securityRoles, securityGroups, otherProperties, userAccountStatus, secrets);
    }
}
