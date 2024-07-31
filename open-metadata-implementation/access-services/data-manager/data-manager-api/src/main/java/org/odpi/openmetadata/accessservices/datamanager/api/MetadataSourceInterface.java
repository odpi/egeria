/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;

/**
 * MetadataSourceInterface is the interface used to define information about the third party technologies that
 * an integration daemon is extracting metadata from.
 *
 * These technologies are represented by a software server capability in open metadata.
 */
public interface MetadataSourceInterface
{
    /**
     * Create information about the component that manages APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param apiManagerProperties description of the API manager (specify qualified name at a minimum)
     *
     * @return unique identifier of the API manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String createAPIManager(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            APIManagerProperties apiManagerProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;




    /**
     * Create information about the database manager (DBMS) that is supplying database schema metadata.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param databaseManagerProperties description of the database manager (DBMS) (specify qualified name at a minimum)
     *
     * @return unique identifier of the database manager's software server capability
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
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically this is Egeria's data manager proxy.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param eventBrokerProperties description of the event broker (specify qualified name at a minimum)
     *
     * @return unique identifier of the event broker's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String createEventBroker(String                userId,
                             String                externalSourceGUID,
                             String                externalSourceName,
                             EventBrokerProperties eventBrokerProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Create information about a File System that is being used to store data files.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param fileSystemProperties description of the file system (specify qualified name at a minimum)
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
     * @param fileManagerProperties description of the file manager (specify qualified name at a minimum)
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
     * Create information about an application.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param applicationProperties description of the application (specify qualified name at a minimum)
     *
     * @return unique identifier of the application's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createApplication(String                userId,
                              String                externalSourceGUID,
                              String                externalSourceName,
                              ApplicationProperties applicationProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Create information about a data processing engine - set up typeName in the properties to create sub types such as
     * ReportingEngine, WorkflowEngine, AnalyticsEngine, DataMovementEngine or DataVirtualizationEngine.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param engineProperties description of the engine (specify qualified name at a minimum)
     *
     * @return unique identifier of the engine's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createDataProcessingEngine(String                         userId,
                                       String                         externalSourceGUID,
                                       String                         externalSourceName,
                                       DataProcessingEngineProperties engineProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a database manager, event broker, API Manager, filesystem or file manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the data manager
     *
     * @return unique identifier of the data manager's software server capability
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
