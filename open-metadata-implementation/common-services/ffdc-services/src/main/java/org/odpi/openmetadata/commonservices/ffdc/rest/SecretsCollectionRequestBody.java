/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecretsCollection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecretsCollectionRequestBody passes information to set up a client side secret with the security connector to support a coll to a third party, typically from an integration connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecretsCollectionRequestBody
{
    private OpenMetadataSecretsCollection secretsCollection = null;


    /**
     * Default constructor
     */
    public SecretsCollectionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template source
     */
    public SecretsCollectionRequestBody(SecretsCollectionRequestBody template)
    {
        if (template != null)
        {
            secretsCollection = template.getSecretsCollection();
        }
    }


    /**
     * Return the user account.
     *
     * @return user account
     */
    public OpenMetadataSecretsCollection getSecretsCollection()
    {
        return secretsCollection;
    }


    /**
     * Set up the user account.
     *
     * @param secretsCollection user account
     */
    public void setSecretsCollection(OpenMetadataSecretsCollection secretsCollection)
    {
        this.secretsCollection = secretsCollection;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "SecretsCollectionRequestBody{" +
                "secretsCollection=" + secretsCollection +
                '}';
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
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
        SecretsCollectionRequestBody that = (SecretsCollectionRequestBody) objectToCompare;
        return Objects.equals(getSecretsCollection(), that.getSecretsCollection());
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSecretsCollection());
    }
}
