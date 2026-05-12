/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccountProfileProperties extends ClassificationBeanProperties
{
    private long                userAccountCount       = 0;
    private long                employeeAccountCount   = 0;
    private long                contractorAccountCount = 0;
    private long                externalAccountCount   = 0;
    private long                digitalAccountCount    = 0;
    private long                activeAccountCount     = 0;
    private long                expiredAccountCount    = 0;
    private long                lockedAccountCount     = 0;
    private long                disabledAccountCount   = 0;
    private Map<String,Long>    userAccountTypes       = null;
    private Map<String,Long>    userAccountStatuses    = null;
    private Map<String, String> additionalProperties   = null;


    /**
     * Default constructor
     */
    public UserAccountProfileProperties()
    {
        super.typeName = OpenMetadataType.USER_ACCOUNT_PROFILE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UserAccountProfileProperties(UserAccountProfileProperties template)
    {
        super(template);

        if (template != null)
        {
            userAccountCount       = template.getUserAccountCount();
            employeeAccountCount   = template.getEmployeeAccountCount();
            contractorAccountCount = template.getContractorAccountCount();
            externalAccountCount   = template.getExternalAccountCount();
            digitalAccountCount    = template.getDigitalAccountCount();
            activeAccountCount     = template.getActiveAccountCount();
            expiredAccountCount    = template.getExpiredAccountCount();
            lockedAccountCount     = template.getLockedAccountCount();
            disabledAccountCount   = template.getDisabledAccountCount();
            userAccountTypes       = template.getUserAccountTypes();
            userAccountStatuses    = template.getUserAccountStatuses();
            additionalProperties   = template.getAdditionalProperties();
        }
    }


    /**
     * Retrieves the total count of user accounts.
     *
     * @return long
     */
    public long getUserAccountCount()
    {
        return userAccountCount;
    }


    /**
     * Sets the total count of user accounts.
     *
     * @param userAccountCount long
     */
    public void setUserAccountCount(long userAccountCount)
    {
        this.userAccountCount = userAccountCount;
    }


    /**
     * Retrieves the total count of employee accounts.
     *
     * @return long
     */
    public long getEmployeeAccountCount()
    {
        return employeeAccountCount;
    }


    /**
     * Sets the total count of employee accounts.
     *
     * @param employeeAccountCount long
     */
    public void setEmployeeAccountCount(long employeeAccountCount)
    {
        this.employeeAccountCount = employeeAccountCount;
    }


    /**
     * Retrieves the total count of contractor accounts.
     *
     * @return long
     */
    public long getContractorAccountCount()
    {
        return contractorAccountCount;
    }


    /**
     * Sets the total count of contractor accounts.
     *
     * @param contractorAccountCount long
     */
    public void setContractorAccountCount(long contractorAccountCount)
    {
        this.contractorAccountCount = contractorAccountCount;
    }


    /**
     * Retrieves the total count of external accounts.
     *
     * @return long
     */
    public long getExternalAccountCount()
    {
        return externalAccountCount;
    }


    /**
     * Sets the total count of external accounts.
     *
     * @param externalAccountCount long
     */
    public void setExternalAccountCount(long externalAccountCount)
    {
        this.externalAccountCount = externalAccountCount;
    }


    /**
     * Retrieves the total count of digital accounts.
     *
     * @return long
     */
    public long getDigitalAccountCount()
    {
        return digitalAccountCount;
    }


    /**
     * Sets the total count of digital accounts.
     *
     * @param digitalAccountCount long
     */
    public void setDigitalAccountCount(long digitalAccountCount)
    {
        this.digitalAccountCount = digitalAccountCount;
    }


    /**
     * Retrieves the total count of active accounts.
     *
     * @return long
     */
    public long getActiveAccountCount()
    {
        return activeAccountCount;
    }


    /**
     * Sets the total count of active accounts.
     *
     * @param activeAccountCount long
     */
    public void setActiveAccountCount(long activeAccountCount)
    {
        this.activeAccountCount = activeAccountCount;
    }


    /**
     * Retrieves the total count of expired accounts.
     *
     * @return long
     */
    public long getExpiredAccountCount()
    {
        return expiredAccountCount;
    }


    /**
     * Sets the total count of expired accounts.
     *
     * @param expiredAccountCount long
     */
    public void setExpiredAccountCount(long expiredAccountCount)
    {
        this.expiredAccountCount = expiredAccountCount;
    }


    /**
     * Retrieves the total count of locked accounts.
     *
     * @return long
     */
    public long getLockedAccountCount()
    {
        return lockedAccountCount;
    }


    /**
     * Sets the total count of locked accounts.
     *
     * @param lockedAccountCount long
     */
    public void setLockedAccountCount(long lockedAccountCount)
    {
        this.lockedAccountCount = lockedAccountCount;
    }


    /**
     * Retrieves the total count of disabled accounts.
     *
     * @return long
     */
    public long getDisabledAccountCount()
    {
        return disabledAccountCount;
    }


    /**
     * Returns the map of user account types.
     *
     * @return map of user account types
     */
    public Map<String, Long> getUserAccountTypes()
    {
        return userAccountTypes;
    }


    /**
     * Sets up the map of user account types.
     *
     * @param userAccountTypes map of user account types
     */
    public void setUserAccountTypes(Map<String, Long> userAccountTypes)
    {
        this.userAccountTypes = userAccountTypes;
    }


    /**
     * Returns the map of user account statuses.
     *
     * @return map of user account statuses
     */
    public Map<String, Long> getUserAccountStatuses()
    {
        return userAccountStatuses;
    }


    /**
     * Sets up the map of user account statuses.
     *
     * @param userAccountStatuses map of user account statuses
     */
    public void setUserAccountStatuses(Map<String, Long> userAccountStatuses)
    {
        this.userAccountStatuses = userAccountStatuses;
    }


    /**
     * Sets the total count of disabled accounts.
     *
     * @param disabledAccountCount long
     */
    public void setDisabledAccountCount(long disabledAccountCount)
    {
        this.disabledAccountCount = disabledAccountCount;
    }


    /**
     * Retrieves additional properties associated with the user account.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Sets additional properties associated with the user account.
     *
     * @param additionalProperties map of name-value pairs
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "UserAccountProfile{" +
                "userAccountCount=" + userAccountCount +
                ", employeeAccountCount=" + employeeAccountCount +
                ", contractorAccountCount=" + contractorAccountCount +
                ", externalAccountCount=" + externalAccountCount +
                ", digitalAccountCount=" + digitalAccountCount +
                ", activeAccountCount=" + activeAccountCount +
                ", expiredAccountCount=" + expiredAccountCount +
                ", lockedAccountCount=" + lockedAccountCount +
                ", disabledAccountCount=" + disabledAccountCount +
                ", userAccountTypes=" + userAccountTypes +
                ", userAccountStatuses=" + userAccountStatuses +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Compares the properties of this UserAccountProfile with another object.
     *
     * @param objectToCompare object to compare against
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        UserAccountProfileProperties that = (UserAccountProfileProperties) objectToCompare;
        return userAccountCount == that.userAccountCount &&
                employeeAccountCount == that.employeeAccountCount &&
                contractorAccountCount == that.contractorAccountCount &&
                externalAccountCount == that.externalAccountCount &&
                digitalAccountCount == that.digitalAccountCount &&
                activeAccountCount == that.activeAccountCount &&
                expiredAccountCount == that.expiredAccountCount &&
                lockedAccountCount == that.lockedAccountCount &&
                disabledAccountCount == that.disabledAccountCount &&
                Objects.equals(userAccountTypes, that.userAccountTypes) &&
                Objects.equals(userAccountStatuses, that.userAccountStatuses) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Generates a hash code for this UserAccountProfile based on its properties.
     *
     * @return hash code value
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userAccountCount, employeeAccountCount, contractorAccountCount,
                            digitalAccountCount, activeAccountCount, expiredAccountCount, lockedAccountCount,
                            disabledAccountCount, userAccountTypes, userAccountStatuses, additionalProperties);
    }
}
