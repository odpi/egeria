/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecretsCollection stores a collection or properties that are used to connect to a particular digital resource.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecretsCollection
{
    private long                     refreshTimeInterval = 60L;
    private Map<String, String>      secrets             = null;
    private TokenAPI                 tokenAPI            = null;
    private Map<String, UserAccount> users               = null;


    /**
     * Default constructor
     */
    public SecretsCollection()
    {
    }


    /**
     * Return the length of time that secrets can be cached.
     *
     * @return long
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the length of time that secrets can be cached.
     *
     * @param getRefreshTimeInterval long
     */
    public void setRefreshTimeInterval(long getRefreshTimeInterval)
    {
        this.refreshTimeInterval = getRefreshTimeInterval;
    }


    /**
     * Return the users stored in the secrets collection.
     *
     * @return map of userIds to user details
     */
    public Map<String, UserAccount> getUsers()
    {
        return users;
    }


    /**
     * Set up the users stored in the secrets collection.
     *
     * @param users  map of userIds to user details
     */
    public void setUsers(Map<String, UserAccount> users)
    {
        this.users = users;
    }


    /**
     * Return the list of secrets.
     *
     * @return map of secret values
     */
    public Map<String, String> getSecrets()
    {
        return secrets;
    }


    /**
     * Set up the in memory version of the secrets collection.
     *
     * @param secrets a map of secrets values
     */
    public void setSecrets(Map<String, String> secrets)
    {
        this.secrets = secrets;
    }


    /**
     * Return details of a token API associated with the secrets collection.
     *
     * @return token API definition
     */
    public TokenAPI getTokenAPI()
    {
        return tokenAPI;
    }


    /**
     * Set up the token API for this secrets collection.
     *
     * @param tokenAPI token API definition
     */
    public void setTokenAPI(TokenAPI tokenAPI)
    {
        this.tokenAPI = tokenAPI;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecretsCollection{" +
                "refreshTimeInterval=" + refreshTimeInterval +
                ", users=" + users +
                ", secrets=" + secrets +
                ", tokenAPI=" + tokenAPI +
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
        SecretsCollection that = (SecretsCollection) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                Objects.equals(users, that.users) &&
                Objects.equals(secrets, that.secrets) &&
                Objects.equals(tokenAPI, that.tokenAPI);
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(refreshTimeInterval, users, secrets, tokenAPI);
    }
}
