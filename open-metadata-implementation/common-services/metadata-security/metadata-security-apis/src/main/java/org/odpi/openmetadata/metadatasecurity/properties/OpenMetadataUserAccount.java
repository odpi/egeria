/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataUserAccount extends UserAccount with associated userId property.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMetadataUserAccount extends UserAccount
{
    private String       userId      = null;
    private List<String> secretNames = null;

    /**
     * Default constructor
     */
    public OpenMetadataUserAccount()
    {
        super();
    }


    /**
     * Copy constructor
     *
     * @param userId      associated user account identifier
     * @param userAccount super class properties
     */
    public OpenMetadataUserAccount(String      userId,
                                   UserAccount userAccount)
    {
        super(userAccount);

        this.userId = userId;

        if (super.getSecrets() != null)
        {
            secretNames = new ArrayList<>(super.getSecrets().keySet());
        }
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
     * Return the list of secret names associated with this user account.
     *
     * @return null or list of secret names
     */
    public List<String> getSecretNames()
    {
        return secretNames;
    }


    /**
     * Set up the list of secret names associated with this user account.
     *
     * @param secretNames null or list of secret names
     */
    public void setSecretNames(List<String> secretNames)
    {
        this.secretNames = secretNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataUserAccount{" +
                "userId='" + userId + '\'' +
                ", secretNames=" + secretNames +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        OpenMetadataUserAccount that = (OpenMetadataUserAccount) objectToCompare;
        return Objects.equals(userId, that.userId) && Objects.equals(secretNames, that.secretNames);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userId, secretNames);
    }
}
