/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers;

import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

/**
 * MetadataAccessServerConnector Connectors to access a metadata access point or metadata access store.
 */
public class MetadataAccessServerConnector extends OMAGServerConnectorBase
{
    public MetadataAccessServerConnector()
    {
        super("Metadata Access Server Connector");
    }



    /**
     * Retrieve a list of the access services registered on the platform
     *
     *
     * @return List of access services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAccessServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return extractor.getAccessServices();
    }


    /*
     * ========================================================================================
     * Metadata Access Server specific services
     */

    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param cohortName name of cohort
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     *
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                             RepositoryErrorException,
                                                             org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        return extractor.connectToCohort(cohortName);
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        return extractor.disconnectFromCohort(cohortName);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String cohortName) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
    {
        return extractor.unregisterFromCohort(cohortName);
    }
}
