/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsCollection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataSecretsCollection extends SecretsCollection with name property.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMetadataSecretsCollection extends SecretsCollection
{
    private String collectionName = null;

    /**
     * Default constructor
     */
    public OpenMetadataSecretsCollection()
    {
        super();
    }


    /**
     * Copy constructor
     *
     * @param collectionName      name of the collection
     * @param secretsCollection super class properties
     */
    public OpenMetadataSecretsCollection(String            collectionName,
                                         SecretsCollection secretsCollection)
    {
        super(secretsCollection);

        this.collectionName = collectionName;
    }


    /**
     * Return the name that identifies the collection.
     *
     * @return string identifier
     */
    public String getCollectionName()
    {
        return collectionName;
    }


    /**
     * Set up the name that identifies the collection.
     *
     * @param collectionName string identifier
     */
    public void setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataSecretsCollection{" +
                "collectionName='" + collectionName + '\'' +
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
        OpenMetadataSecretsCollection that = (OpenMetadataSecretsCollection) objectToCompare;
        return Objects.equals(collectionName, that.collectionName);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), collectionName);
    }
}
