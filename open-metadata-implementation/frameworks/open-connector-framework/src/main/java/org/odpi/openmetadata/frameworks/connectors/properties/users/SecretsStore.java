/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.users;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A SecretsStore holds a set of named collections each holding the secrets needed to either describe a user
 * (used to authenticate that user and define the resources they have access to) or create the necessary
 * authentication information to connect to a remote system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecretsStore
{
    private Map<String, SecretsCollection> secretsCollections = null;


    /**
     * Default constructor
     */
    public SecretsStore()
    {
    }


    /**
     * Return the map of secrets collections.
     *
     * @return map
     */
    public Map<String, SecretsCollection> getSecretsCollections()
    {
        return secretsCollections;
    }


    /**
     * Set up a new secrets collection map.
     *
     * @param secretsCollections map
     */
    public void setSecretsCollections(Map<String, SecretsCollection> secretsCollections)
    {
        this.secretsCollections = secretsCollections;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecretsStore{" +
                "secretsCollections=" + secretsCollections +
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
        SecretsStore that = (SecretsStore) objectToCompare;
        return Objects.equals(secretsCollections, that.secretsCollections);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(secretsCollections);
    }
}
