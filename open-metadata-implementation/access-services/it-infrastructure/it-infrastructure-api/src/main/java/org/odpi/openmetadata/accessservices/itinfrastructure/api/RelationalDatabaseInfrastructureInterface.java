/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.accessservices.itinfrastructure.properties.DatabaseManagerProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.DatabaseServerProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * RelationalDatabaseInfrastructureInterface defines the client side interface for the Data Platform OMAS that is
 * relevant for relational database infrastructure.   It provides the ability to
 * define and maintain the metadata about a database server.
 *
 *
 */
public interface RelationalDatabaseInfrastructureInterface
{
    /*
     * Relational databases are managed by a relational database management system (DBMS).  This is
     * hosted in a "Database Server".  The DBMS is represented by the Database Platform.
     */


    String addDatabaseServer(String userId,
                             String integrationDaemonGUID,
                             DatabaseServerProperties databaseServerProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;

    String addDatabaseManager(String userId,
                              String integrationDaemonGUID,
                              DatabaseManagerProperties databaseManagerProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

    void linkDatabaseManagerToServer(String userId,
                                     String integrationDaemonGUID,
                                     String databasePlatformGUID,
                                     String databaseServerGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    String  addDatabaseEndpoint(String             userId,
                                String             integrationDaemonGUID,
                                String             databaseServerGUID,
                                EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    void linkDatabaseEndpointToServer(String userId,
                                      String integrationDaemonGUID,
                                      String databaseEndpointGUID,
                                      String databaseServerGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;



    DatabaseServerProperties getDatabaseServerByGUID(String userId,
                                                     String databaseServerGUID);

    DatabaseServerProperties getDatabaseServerByName(String userId,
                                                     String databaseServerName);

    List<DatabaseServerProperties>   findDatabaseServersByHost(String userId,
                                                               String hostName);

    void  updateDatabaseServerProperties(String userId,
                                         String databaseServerGUID,
                                         Map<String, String> updatedProperties);

    void  updateDatabaseServerVendorProperties(String userId,
                                               String databaseServerGUID,
                                               Map<String, String> updatedProperties);

    void  removeDatabaseServer(String userId,
                               String databaseServerGUID);

}
