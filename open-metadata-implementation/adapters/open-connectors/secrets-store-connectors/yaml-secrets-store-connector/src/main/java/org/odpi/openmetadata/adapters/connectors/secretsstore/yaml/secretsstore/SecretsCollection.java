/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.secretsstore;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private Map<String, String> secrets = null;
    private TokenAPI            tokenAPI = null;

    /**
     * Default constructor
     */
    public SecretsCollection()
    {
    }


    /**
     * Return the list of secrets.
     *
     * @return
     */
    public Map<String, String> getSecrets()
    {
        return secrets;
    }

    public void setSecrets(Map<String, String> secrets)
    {
        this.secrets = secrets;
    }


    public TokenAPI getTokenAPI()
    {
        return tokenAPI;
    }

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
                "secrets=" + secrets +
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
        return Objects.equals(secrets, that.secrets) && Objects.equals(tokenAPI, that.tokenAPI);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(secrets, tokenAPI);
    }
}
