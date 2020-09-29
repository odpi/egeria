/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseManagerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileManagerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileSystemProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.SoftwareServerCapabilitiesProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * MetadataSourceInterface is the interface used to define information about the third party technologies that
 * an integration daemon is extracting metadata from.
 *
 * The these technologies are represented by a software server capability in open metadata.
 */
public interface MetadataSourceInterface
{
    /**
     * Create information about a File System that is being used to store data files.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param fileSystemProperties description of the file system
     *
     * @return unique identifier of the file system's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createFileSystem(String               userId,
                             String               externalSourceGUID,
                             String               externalSourceName,
                             FileSystemProperties fileSystemProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Create information about an application that manages a collection of data files.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param fileManagerProperties description of the
     *
     * @return unique identifier of the file manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createFileManager(String                userId,
                              String                externalSourceGUID,
                              String                externalSourceName,
                              FileManagerProperties fileManagerProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically this is Egeria's data manager proxy.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param databaseManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the database management's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String createDatabaseManager(String                    userId,
                                 String                    externalSourceGUID,
                                 String                    externalSourceName,
                                 DatabaseManagerProperties databaseManagerProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a database manager, filesystem or file manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String getMetadataSourceGUID(String  userId,
                                 String  qualifiedName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;
}
