/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGMetadataStoreProperties
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGMetadataStoreProperties extends OMAGServerProperties
{
    private OMAGConnectorProperties repositoryConnector         = null;
    private String                  localMetadataCollectionId   = null;
    private String                  localMetadataCollectionName = null;


    public OMAGMetadataStoreProperties()
    {
    }


    /**
     * Return details about the connector to the server's metadata repository.
     *
     * @return connector properties
     */
    public OMAGConnectorProperties getRepositoryConnector()
    {
        return repositoryConnector;
    }


    /**
     * Set up details about the connector to the server's metadata repository.
     *
     * @param repositoryConnector connector properties
     */
    public void setRepositoryConnector(OMAGConnectorProperties repositoryConnector)
    {
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Return the identifier of the metadata collection stored inside the local repository.
     *
     * @return string
     */
    public String getLocalMetadataCollectionId()
    {
        return localMetadataCollectionId;
    }


    /**
     * Set up the identifier of the metadata collection stored inside the local repository.
     *
     * @param localMetadataCollectionId string
     */
    public void setLocalMetadataCollectionId(String localMetadataCollectionId)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Return the optional name of the metadata collection stored inside the local repository.
     *
     * @return string
     */
    public String getLocalMetadataCollectionName()
    {
        return localMetadataCollectionName;
    }


    /**
     * Set up the optional name of the metadata collection stored inside the local repository.
     *
     * @param localMetadataCollectionName string
     */
    public void setLocalMetadataCollectionName(String localMetadataCollectionName)
    {
        this.localMetadataCollectionName = localMetadataCollectionName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGMetadataStoreProperties{" +
                "repositoryConnector=" + repositoryConnector +
                ", localMetadataCollectionId='" + localMetadataCollectionId + '\'' +
                ", localMetadataCollectionName='" + localMetadataCollectionName + '\'' +
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
        OMAGMetadataStoreProperties that = (OMAGMetadataStoreProperties) objectToCompare;
        return Objects.equals(repositoryConnector, that.repositoryConnector) &&
                Objects.equals(localMetadataCollectionId, that.localMetadataCollectionId)  &&
                Objects.equals(localMetadataCollectionName, that.localMetadataCollectionName);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(repositoryConnector, localMetadataCollectionId, localMetadataCollectionName);
    }
}
