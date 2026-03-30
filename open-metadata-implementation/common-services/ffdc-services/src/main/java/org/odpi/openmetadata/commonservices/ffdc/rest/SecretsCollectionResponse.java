/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SecretsCollectionResponse is the response structure used passes information to retrieve a client side secret with the security connector to support a coll to a third party, typically from an integration connector.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretsCollectionResponse extends FFDCResponseBase
{
    private OpenMetadataUserAccount userAccount = null;

    /**
     * Default constructor
     */
    public SecretsCollectionResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecretsCollectionResponse(SecretsCollectionResponse template)
    {
        super(template);

        if (template != null)
        {
            this.userAccount = template.getUserAccount();
        }
    }


    /**
     * Return the user account object.
     *
     * @return user account
     */
    public OpenMetadataUserAccount getUserAccount()
    {
        return userAccount;
    }


    /**
     * Set up the user account object.
     *
     * @param userAccount - user account
     */
    public void setUserAccount(OpenMetadataUserAccount userAccount)
    {
        this.userAccount = userAccount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UserAccountResponse{" +
                "userAccount=" + userAccount +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof SecretsCollectionResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getUserAccount(), that.getUserAccount());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (userAccount == null)
        {
            return super.hashCode();
        }
        else
        {
            return userAccount.hashCode();
        }
    }
}
