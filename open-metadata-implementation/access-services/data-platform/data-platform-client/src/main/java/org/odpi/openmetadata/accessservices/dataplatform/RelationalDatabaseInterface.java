/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform;

import org.odpi.openmetadata.accessservices.dataplatform.properties.AdditionalProperties;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseServer;
import org.odpi.openmetadata.accessservices.dataplatform.properties.VendorProperties;

import java.util.List;

/**
 * RelationalDatabaseInterface defines the client side interface for the Data Platform OMAS that is
 * relevant for relational database assets.   It provides the ability for a relational database server platform to
 * define and maintain the metadata about itself and the relational database assets it hosts.  This includes
 * information about databases, database schemas (and the tables, views and columns within them) along with any
 * database functions (aka stored procedures) within them.
 */
public interface RelationalDatabaseInterface
{
    /*
     * Manage information about the database server itself
     */

    String addDatabaseServer(String userId,
                             String serverName,
                             String hostName,
                             VendorProperties vendorSpecificProperties,
                             AdditionalProperties databaseServerProperties);

    void  moveDatabaseServer(String userId,
                             String databaseServerGUID,
                             String newHostName);

    void  renameDatabaseServer(String userId,
                               String databaseServerGUID,
                               String newSeverName);

    DatabaseServer getDatabaseServerByGUID(String userId,
                                           String databaseServerGUID);

    DatabaseServer    getDatabaseServerByName(String userId,
                                              String databaseServerName);

    List<DatabaseServer>   findDatabaseServersByHost(String userId,
                                                     String hostName);

    void  updateDatabaseServerProperties(String userId,
                                         String databaseServerGUID,
                                         AdditionalProperties updatedProperties);

    void  updateDatabaseServerVendorProperties(String userId,
                                               String databaseServerGUID,
                                               AdditionalProperties updatedProperties);

    void  removeDatabaseServer(String userId,
                               String databaseServerGUID);



    String addDeployedDatabase(String userId,
                               List<String> connectionsGUID,
                               String databaseName,
                               List<String> databaseSchemas,
                               VendorProperties vendorSpecificProperties,
                               AdditionalProperties databaseServerProperties);

    String removeDeployedDatabase(String userId,
                                  String databaseServerGUID,
                                  String databaseGUID);


    String addDatabaseSchema(String userId,
                             String schemaName,
                             List<String> databaseTables,
                             List<String> databaseViews,
                             String deployedDatabaseGUID);

    String addTable(String userId,
                    String tableName);


    String addTable(String userId,
                    String tableName,
                    List<String> columnGUIDs);

    String addDatabaseFunction(String userId,
                               String databaseGUID,
                               String functionName);


}
