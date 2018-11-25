/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.services;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;


/**
 * OMRSRepositoryServicesInstance caches references to OMRS objects for a specific server
 */
public class OMRSRepositoryServicesInstance
{
    private OMRSMetadataCollection localMetadataCollection;
    private String                 localServerURL;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param localRepositoryConnector link to the local repository responsible for servicing the REST calls.
     *                                 If localRepositoryConnector is null when a REST calls is received, the request
     *                                 is rejected.
     * @param localServerURL URL of the local server
     */
    public OMRSRepositoryServicesInstance(LocalOMRSRepositoryConnector localRepositoryConnector,
                                          String                       localServerURL)
    {
        try
        {
            this.localMetadataCollection = localRepositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            this.localMetadataCollection = null;
        }

        this.localServerURL = localServerURL;
    }


    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     */
    public OMRSMetadataCollection getLocalMetadataCollection()
    {
        return localMetadataCollection;
    }


    /**
     * Return the URL root for this server.
     *
     * @return URL
     */
    public String getLocalServerURL()
    {
        return localServerURL;
    }
}
