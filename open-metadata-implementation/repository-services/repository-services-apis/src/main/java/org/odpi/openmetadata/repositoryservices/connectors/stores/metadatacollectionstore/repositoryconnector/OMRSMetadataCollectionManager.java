/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

/**
 * OMRSRepositoryConnectors are used by OMRS to retrieve metadata from metadata repositories.  Each implementation
 * of the OMRSRepositoryConnector is for a different type of repository.  This interface defines the extension that
 * an OMRSRepositoryConnector must implement over the base connector definition.  It describes the concept of a
 * metadata collection.  This is a collection of metadata that includes the type definitions (TypeDefs) and
 * metadata instances (Entities and Relationships) stored in the repository.
 */
public interface OMRSMetadataCollectionManager
{
    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building TypeDefs and metadata instances.
     */
    void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper);


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator);


    /**
     * Return the name of the repository where the metadata collection resides.
     *
     * @return String name
     */
    String  getRepositoryName();


    /**
     * Set up the name of the repository where the metadata collection resides.
     *
     * @param repositoryName String name
     */
    void  setRepositoryName(String repositoryName);


    /**
     * Return the name of the server where the metadata collection resides.
     *
     * @return String name
     */
    String getServerName();


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    void  setServerName(String serverName);


    /**
     * Return the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @return String name
     */
    String getServerType();


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    void setServerType(String serverType);


    /**
     * Return the name of the organization that runs/owns the server used to access the repository.
     *
     * @return String name
     */
    String getOrganizationName();


    /**
     * Set up the name of the organization that runs/owns the server used to access the repository.
     *
     * @param organizationName String organization name
     */
    void setOrganizationName(String organizationName);


    /**
     * Return the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @return user id
     */
    String getServerUserId();


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param serverUserId string user id
     */
    void setServerUserId(String serverUserId);


    /**
     * Return the maximum page size
     *
     * @return maximum number of elements that can be retrieved on a request.
     */
    int getMaxPageSize();


    /**
     * Set up the maximum PageSize
     *
     * @param maxPageSize maximum number of elements that can be retrieved on a request.
     */
    void setMaxPageSize(int maxPageSize);


    /**
     * Return the unique id for this metadata collection.
     *
     * @return String unique id
     */
    String getMetadataCollectionId();


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique id
     */
    void setMetadataCollectionId(String metadataCollectionId);


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataInstanceStore metadata TypeDefs and instances retrieved from the metadata repository.
     * @throws RepositoryErrorException no metadata collection
     */
    OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException;
}