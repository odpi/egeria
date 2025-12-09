/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers;

import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
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
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String cohortName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return extractor.connectToCohort(cohortName);
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return extractor.disconnectFromCohort(cohortName);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return extractor.unregisterFromCohort(cohortName);
    }


    /*
     * =============================================================
     * Load archives
     */

    /**
     * Add a new open metadata archive to running repository.
     *
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveFile(String fileName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        extractor.addOpenMetadataArchiveFile(fileName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param connection connection for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchive(Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        extractor.addOpenMetadataArchive(connection);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param openMetadataArchive openMetadataArchive for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveContent(OpenMetadataArchive openMetadataArchive) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
        extractor.addOpenMetadataArchiveContent(openMetadataArchive);
    }
}
