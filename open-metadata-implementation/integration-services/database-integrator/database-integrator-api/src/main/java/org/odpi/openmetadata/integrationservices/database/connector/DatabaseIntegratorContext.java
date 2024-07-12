/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.database.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ValidValueManagement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import java.util.List;
import java.util.Map;


/**
 * DatabaseIntegratorContext is the context for managing resources from a relational database server.
 */
public class DatabaseIntegratorContext extends IntegrationContext
{
    private final ConnectionManagerClient connectionManagerClient;
    private final DatabaseManagerClient   databaseManagerClient;
    private final DataManagerEventClient eventClient;
    private final ValidValueManagement   validValueManagement;



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param databaseManagerClient client to managing database metadata
     * @param connectionManagerClient client for managing connections
     * @param validValueManagement client for managing valid value sets and definitions
     * @param eventClient client to register for events
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the database manager
     * @param externalSourceName unique name of the software server capability for the database manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public DatabaseIntegratorContext(String                       connectorId,
                                     String                       connectorName,
                                     String                       connectorUserId,
                                     String                       serverName,
                                     OpenIntegrationClient        openIntegrationClient,
                                     GovernanceConfiguration      governanceConfiguration,
                                     OpenMetadataClient           openMetadataStoreClient,
                                     DatabaseManagerClient        databaseManagerClient,
                                     ConnectionManagerClient      connectionManagerClient,
                                     ValidValueManagement         validValueManagement,
                                     DataManagerEventClient       eventClient,
                                     boolean                      generateIntegrationReport,
                                     PermittedSynchronization     permittedSynchronization,
                                     String                       integrationConnectorGUID,
                                     String                       externalSourceGUID,
                                     String                       externalSourceName,
                                     AuditLog                     auditLog,
                                     int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.databaseManagerClient   = databaseManagerClient;
        this.eventClient             = eventClient;
        this.connectionManagerClient = connectionManagerClient;
        this.validValueManagement    = validValueManagement;
    }

    /* ========================================================
     * Returning the database manager name from the configuration
     */


    /**
     * Return the qualified name of the database manager that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getDatabaseManagerName()
    {
        return externalSourceName;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database.
     *
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabase(DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String databaseGUID = databaseManagerClient.createDatabase(userId, externalSourceGUID, externalSourceName, databaseProperties);

        if ((databaseGUID != null) &&(integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementCreation(databaseGUID);
        }

        return databaseGUID;
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseFromTemplate(String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String databaseGUID = databaseManagerClient.createDatabaseFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties);

        if ((databaseGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementCreation(databaseGUID);
        }

        return databaseGUID;
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public void updateDatabase(String             databaseGUID,
                               DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        databaseManagerClient.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, false, databaseProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
        }
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabase(String             databaseGUID,
                               boolean            isMergeUpdate,
                               DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        databaseManagerClient.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, isMergeUpdate, databaseProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
        }
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param databaseGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabase(String databaseGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        databaseManagerClient.publishDatabase(userId, databaseGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
        }
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param databaseGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabase(String databaseGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        databaseManagerClient.withdrawDatabase(userId, databaseGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
        }
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    @SuppressWarnings(value="unused")
    public void removeDatabase(String databaseGUID,
                               String qualifiedName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        databaseManagerClient.removeDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementDelete(databaseGUID);
        }
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabase(String databaseGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        databaseManagerClient.removeDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseGUID, databaseGUID);
            integrationReportWriter.reportElementDelete(databaseGUID);
        }
    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseElement> findDatabases(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return databaseManagerClient.findDatabases(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseElement>   getDatabasesByName(String name,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return databaseManagerClient.getDatabasesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseElement>   getMyDatabases(int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return databaseManagerClient.getDatabasesForDatabaseManager(userId, externalSourceGUID, externalSourceName, startFrom, pageSize);
    }


    /**
     * Retrieve the database metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseElement getDatabaseByGUID(String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return databaseManagerClient.getDatabaseByGUID(userId, guid);
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchema(String                   databaseGUID,
                                       DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String databaseSchemaGUID = databaseManagerClient.createDatabaseSchema(userId,
                                                                               externalSourceGUID,
                                                                               externalSourceName,
                                                                               databaseGUID,
                                                                               databaseSchemaProperties);

        if ((databaseSchemaGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
            integrationReportWriter.reportElementCreation(databaseSchemaGUID);
        }

        return databaseSchemaGUID;
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchemaFromTemplate(String             templateGUID,
                                                   String             databaseGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String databaseSchemaGUID = databaseManagerClient.createDatabaseSchemaFromTemplate(userId,
                                                                                           externalSourceGUID,
                                                                                           externalSourceName,
                                                                                           templateGUID,
                                                                                           databaseGUID,
                                                                                           templateProperties);

        if ((databaseSchemaGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseGUID);
            integrationReportWriter.reportElementCreation(databaseSchemaGUID);
        }

        return databaseSchemaGUID;
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public void updateDatabaseSchema(String                   databaseSchemaGUID,
                                     DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        databaseManagerClient.updateDatabaseSchema(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   databaseSchemaGUID,
                                                   false,
                                                   databaseSchemaProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseSchemaGUID);
        }
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseSchema(String                   databaseSchemaGUID,
                                     boolean                  isMergeUpdate,
                                     DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        databaseManagerClient.updateDatabaseSchema(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   databaseSchemaGUID,
                                                   isMergeUpdate,
                                                   databaseSchemaProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseSchemaGUID);
        }
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabaseSchema(String databaseSchemaGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        databaseManagerClient.publishDatabaseSchema(userId, databaseSchemaGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseSchemaGUID);
        }
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabaseSchema(String databaseSchemaGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        databaseManagerClient.withdrawDatabaseSchema(userId, databaseSchemaGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementUpdate(databaseSchemaGUID);
        }
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    @SuppressWarnings(value="unused")
    public void removeDatabaseSchema(String databaseSchemaGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        databaseManagerClient.removeDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementDelete(databaseSchemaGUID);
        }
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseSchema(String databaseSchemaGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        databaseManagerClient.removeDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseSchemaGUID, databaseSchemaGUID);
            integrationReportWriter.reportElementDelete(databaseSchemaGUID);
        }
    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseSchemaElement>   findDatabaseSchemas(String searchString,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return databaseManagerClient.findDatabaseSchemas(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a database.
     *
     * @param databaseGUID unique identifier of the database to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested database
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseSchemaElement>   getSchemasForDatabase(String databaseGUID,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return databaseManagerClient.getSchemasForDatabase(userId, databaseGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database schema metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseSchemaElement>   getDatabaseSchemasByName(String name,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return databaseManagerClient.getDatabaseSchemasByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database schema metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseSchemaElement getDatabaseSchemaByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return databaseManagerClient.getDatabaseSchemaByGUID(userId, guid);
    }


    /* ==========================================================================
     * A database or database schema may contain multiple database tables and database views.
     */



    /**
     * Create a database top-level schema type used to attach tables and views to the database/database schema.
     *
     * @param qualifiedName qualified name ofr the schema type - suggest "SchemaOf:" + asset's qualified name
     * @return unique identifier of the database schema type
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createDatabaseSchemaType(String qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        String databaseSchemaTypeGUID = databaseManagerClient.createDatabaseSchemaType(userId,
                                                                                       externalSourceGUID,
                                                                                       externalSourceName,
                                                                                       qualifiedName);

        if ((databaseSchemaTypeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(databaseSchemaTypeGUID);
        }

        return databaseSchemaTypeGUID;
    }


    /**
     * Link the schema type and asset.  This is called from outside the AssetHandler.  The databaseAssetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param databaseAssetGUID unique identifier of the asset to connect the schema to
     * @param schemaTypeGUID identifier for schema Type object
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  void attachSchemaTypeToDatabaseAsset(String  databaseAssetGUID,
                                                 String  schemaTypeGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        databaseManagerClient.attachSchemaTypeToDatabaseAsset(userId, externalSourceGUID, externalSourceName, databaseAssetGUID, schemaTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseAssetGUID, databaseAssetGUID);
            integrationReportWriter.setAnchor(schemaTypeGUID, databaseAssetGUID);
            integrationReportWriter.reportElementUpdate(databaseAssetGUID);
        }
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTable(String                  databaseAssetGUID,
                                      DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        String databaseTableGUID = databaseManagerClient.createDatabaseTable(userId,
                                                                             externalSourceGUID,
                                                                             externalSourceName,
                                                                             databaseAssetGUID,
                                                                             databaseTableProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(databaseAssetGUID, databaseAssetGUID);
            integrationReportWriter.setAnchor(databaseTableGUID, databaseAssetGUID);
            integrationReportWriter.reportElementCreation(databaseTableGUID);
        }

        return databaseTableGUID;
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTableFromTemplate(String             templateGUID,
                                                  String             databaseAssetGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        String databaseTableGUID = databaseManagerClient.createDatabaseTableFromTemplate(userId,
                                                                                         externalSourceGUID,
                                                                                         externalSourceName,
                                                                                         templateGUID,
                                                                                         databaseAssetGUID,
                                                                                         templateProperties);

        if ((databaseTableGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseAssetGUID, databaseAssetGUID);
            integrationReportWriter.setAnchor(databaseTableGUID, databaseAssetGUID);
            integrationReportWriter.reportElementCreation(databaseTableGUID);
        }

        return databaseTableGUID;
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param databaseSchemaTypeGUID unique identifier of the database or database schema where the database table is located
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTableForSchemaType(String                  databaseSchemaTypeGUID,
                                                   DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String databaseTableGUID = databaseManagerClient.createDatabaseTableForSchemaType(userId,
                                                                                          externalSourceGUID,
                                                                                          externalSourceName,
                                                                                          databaseSchemaTypeGUID,
                                                                                          databaseTableProperties);

        if ((databaseTableGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseTableGUID, databaseSchemaTypeGUID);
            integrationReportWriter.reportElementCreation(databaseTableGUID);
        }

        return databaseTableGUID;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the database table to update
     * @param databaseTableProperties new properties for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public void updateDatabaseTable(String                  databaseTableGUID,
                                    DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        databaseManagerClient.updateDatabaseTable(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  databaseTableGUID,
                                                  false,
                                                  databaseTableProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseTableGUID);
        }
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the database table to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseTableProperties new properties for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseTable(String                  databaseTableGUID,
                                    boolean                 isMergeUpdate,
                                    DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        databaseManagerClient.updateDatabaseTable(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  databaseTableGUID,
                                                  isMergeUpdate,
                                                  databaseTableProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseTableGUID);
        }
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    @Deprecated
    public void removeDatabaseTable(String databaseTableGUID,
                                    String qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        databaseManagerClient.removeDatabaseTable(userId, externalSourceGUID, externalSourceName, databaseTableGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseTableGUID);
        }
    }



    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseTable(String databaseTableGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        databaseManagerClient.removeDatabaseTable(userId, externalSourceGUID, externalSourceName, databaseTableGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseTableGUID);
        }
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseTableElement>   findDatabaseTables(String searchString,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return databaseManagerClient.findDatabaseTables(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database tables associated with a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public List<DatabaseTableElement>    getTablesForDatabaseSchema(String databaseSchemaGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return databaseManagerClient.getTablesForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database tables associated with a database or database schema.
     *
     * @param databaseSchemaGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseTableElement>    getTablesForDatabaseAsset(String databaseSchemaGUID,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return databaseManagerClient.getTablesForDatabaseAsset(userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database table metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseTableElement>   getDatabaseTablesByName(String name,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return databaseManagerClient.getDatabaseTablesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database table metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseTableElement getDatabaseTableByGUID(String guid) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return databaseManagerClient.getDatabaseTableByGUID(userId, guid);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseView(String                 databaseAssetGUID,
                                     DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String databaseViewGUID = databaseManagerClient.createDatabaseView(userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           databaseAssetGUID,
                                                                           databaseViewProperties);

        if ((databaseViewGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseViewGUID, databaseAssetGUID);
            integrationReportWriter.reportElementCreation(databaseViewGUID);
        }

        return databaseViewGUID;
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseViewFromTemplate(String             templateGUID,
                                                 String             databaseAssetGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        String databaseViewGUID = databaseManagerClient.createDatabaseViewFromTemplate(userId,
                                                                                       externalSourceGUID,
                                                                                       externalSourceName,
                                                                                       templateGUID,
                                                                                       databaseAssetGUID,
                                                                                       templateProperties);

        if ((databaseViewGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseViewGUID, databaseAssetGUID);
            integrationReportWriter.reportElementCreation(databaseViewGUID);
        }

        return databaseViewGUID;
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param databaseSchemaTypeGUID unique identifier of the schema type where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseViewForSchemaType(String                 databaseSchemaTypeGUID,
                                                  DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        String databaseViewGUID = databaseManagerClient.createDatabaseViewForSchemaType(userId,
                                                                                        externalSourceGUID,
                                                                                        externalSourceName,
                                                                                        databaseSchemaTypeGUID,
                                                                                        databaseViewProperties);

        if ((databaseViewGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(databaseViewGUID, databaseSchemaTypeGUID);
            integrationReportWriter.reportElementCreation(databaseViewGUID);
        }

        return databaseViewGUID;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the database view to update
     * @param databaseViewProperties properties for the new database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public void updateDatabaseView(String                 databaseViewGUID,
                                   DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        databaseManagerClient.updateDatabaseView(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 databaseViewGUID,
                                                 false,
                                                 databaseViewProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseViewGUID);
        }
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the database view to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseViewProperties properties for the new database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseView(String                 databaseViewGUID,
                                   boolean                isMergeUpdate,
                                   DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        databaseManagerClient.updateDatabaseView(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 databaseViewGUID,
                                                 isMergeUpdate,
                                                 databaseViewProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseViewGUID);
        }
    }



    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    @Deprecated
    public void removeDatabaseView(String databaseViewGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        databaseManagerClient.removeDatabaseView(userId, externalSourceGUID, externalSourceName, databaseViewGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseViewGUID);
        }
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseView(String databaseViewGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        databaseManagerClient.removeDatabaseView(userId, externalSourceGUID, externalSourceName, databaseViewGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseViewGUID);
        }
    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseViewElement>   findDatabaseViews(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return databaseManagerClient.findDatabaseViews(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database views associated with a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public List<DatabaseViewElement>    getViewsForDatabaseSchema(String databaseSchemaGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return databaseManagerClient.getViewsForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize);
    }



    /**
     * Retrieve the list of database views associated with a database schema.
     *
     * @param databaseAssetGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseViewElement>    getViewsForDatabaseAsset(String databaseAssetGUID,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return databaseManagerClient.getViewsForDatabaseAsset(userId, databaseAssetGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database view metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseViewElement>   getDatabaseViewsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return databaseManagerClient.getDatabaseViewsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database view metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseViewElement getDatabaseViewByGUID(String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return databaseManagerClient.getDatabaseViewByGUID(userId, guid);
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumn(String                   databaseTableGUID,
                                       DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String databaseColumnGUID = databaseManagerClient.createDatabaseColumn(userId,
                                                                               externalSourceGUID,
                                                                               externalSourceName,
                                                                               databaseTableGUID,
                                                                               databaseColumnProperties);

        if ((databaseColumnGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(databaseColumnGUID, databaseTableGUID);
            integrationReportWriter.reportElementCreation(databaseColumnGUID);
        }

        return databaseColumnGUID;
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumnFromTemplate(String             templateGUID,
                                                   String             databaseTableGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String databaseColumnGUID = databaseManagerClient.createDatabaseColumnFromTemplate(userId,
                                                                                           externalSourceGUID,
                                                                                           externalSourceName,
                                                                                           templateGUID,
                                                                                           databaseTableGUID,
                                                                                           templateProperties);

        if ((databaseColumnGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(databaseColumnGUID, databaseTableGUID);
            integrationReportWriter.reportElementCreation(databaseColumnGUID);
        }

        return databaseColumnGUID;
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    public void updateDatabaseColumn(String                   databaseColumnGUID,
                                     DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        databaseManagerClient.updateDatabaseColumn(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   databaseColumnGUID,
                                                   false,
                                                   databaseColumnProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseColumnGUID);
        }
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseColumn(String                   databaseColumnGUID,
                                     boolean                  isMergeUpdate,
                                     DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        databaseManagerClient.updateDatabaseColumn(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   databaseColumnGUID,
                                                   isMergeUpdate,
                                                   databaseColumnProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseColumnGUID);
        }
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    @SuppressWarnings(value = "unused")
    public void removeDatabaseColumn(String databaseColumnGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        databaseManagerClient.removeDatabaseColumn(userId, externalSourceGUID, externalSourceName, databaseColumnGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseColumnGUID);
        }
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseColumn(String databaseColumnGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        databaseManagerClient.removeDatabaseColumn(userId, externalSourceGUID, externalSourceName, databaseColumnGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(databaseColumnGUID);
        }
    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseColumnElement>   findDatabaseColumns(String searchString,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
       return databaseManagerClient.findDatabaseColumns(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of column for a database table (or view)
     *
     * @param databaseTableGUID unique identifier of the database table of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseColumnElement>    getColumnsForDatabaseTable(String databaseTableGUID,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return databaseManagerClient.getColumnsForDatabaseTable(userId, databaseTableGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database column metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseColumnElement>   getDatabaseColumnsByName(String name,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return databaseManagerClient.getDatabaseColumnsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database column metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseColumnElement getDatabaseColumnByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return databaseManagerClient.getDatabaseColumnByGUID(userId, guid);
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column, and it can be used to uniquely identify the column.
     *
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setPrimaryKeyOnColumn(String                       databaseColumnGUID,
                                      DatabasePrimaryKeyProperties databasePrimaryKeyProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        databaseManagerClient.setPrimaryKeyOnColumn(userId, externalSourceGUID, externalSourceName, databaseColumnGUID, databasePrimaryKeyProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseColumnGUID);
        }
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param databaseColumnGUID unique identifier if the primary key column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePrimaryKeyFromColumn(String databaseColumnGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        databaseManagerClient.removePrimaryKeyFromColumn(userId, externalSourceGUID, externalSourceName, databaseColumnGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(databaseColumnGUID);
        }
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addForeignKeyRelationship(String                       primaryKeyColumnGUID,
                                          String                       foreignKeyColumnGUID,
                                          DatabaseForeignKeyProperties databaseForeignKeyProperties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        databaseManagerClient.addForeignKeyRelationship(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        primaryKeyColumnGUID,
                                                        foreignKeyColumnGUID,
                                                        databaseForeignKeyProperties);
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(primaryKeyColumnGUID);
            integrationReportWriter.reportElementUpdate(foreignKeyColumnGUID);
        }
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForeignKeyRelationship(String  primaryKeyColumnGUID,
                                             String  foreignKeyColumnGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        databaseManagerClient.removeForeignKeyRelationship(userId, externalSourceGUID, externalSourceName, primaryKeyColumnGUID, foreignKeyColumnGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(primaryKeyColumnGUID);
            integrationReportWriter.reportElementUpdate(foreignKeyColumnGUID);
        }
    }



    /* =====================================================================================================================
     * A Connection is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a connection.
     *
     * @param connectionProperties properties about the connection to store
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnection(userId, null, null, connectionProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnectionFromTemplate(userId,
                                                                                     null,
                                                                                     null,
                                                                                     templateGUID,
                                                                                     templateProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Update the metadata element representing a connection.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String               connectionGUID,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        connectionManagerClient.updateConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, isMergeUpdate, connectionProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String  connectionGUID,
                                   String  connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        connectionManagerClient.setupConnectorType(userId, null, null, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.clearConnectorType(userId, externalSourceGUID, externalSourceName, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String  connectionGUID,
                              String  endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.setupEndpoint(userId, null, null, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        connectionManagerClient.clearEndpoint(userId, externalSourceGUID, externalSourceName, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String              connectionGUID,
                                        int                 position,
                                        String              displayName,
                                        Map<String, Object> arguments,
                                        String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        connectionManagerClient.setupEmbeddedConnection(userId, null, null, connectionGUID, position, displayName, arguments, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(embeddedConnectionGUID, connectionGUID);
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String connectionGUID,
                                        String embeddedConnectionGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        connectionManagerClient.clearEmbeddedConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(embeddedConnectionGUID, connectionGUID);
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String  assetGUID,
                                     String  assetSummary,
                                     String  connectionGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.setupAssetConnection(userId, null, null, assetGUID, assetSummary, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(connectionGUID, assetGUID);
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String assetGUID,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        connectionManagerClient.clearAssetConnection(userId, externalSourceGUID, externalSourceName, assetGUID, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(connectionGUID, assetGUID);
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove the metadata element representing a connection.
     *
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String connectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        connectionManagerClient.removeConnection(userId, externalSourceGUID, externalSourceName, connectionGUID);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return connectionManagerClient.findConnections(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name used to retrieve the connection
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return connectionManagerClient.getConnectionsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String connectionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return connectionManagerClient.getConnectionByGUID(userId, connectionGUID);
    }


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpoint(userId, null, null, endpointProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpointFromTemplate(userId,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 networkAddress,
                                                                                 templateGUID,
                                                                                 templateProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        connectionManagerClient.updateEndpoint(userId, externalSourceGUID, externalSourceName, isMergeUpdate, endpointGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endpointGUID);
        }
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(endpointGUID);
        }
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return connectionManagerClient.findEndpoints(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return connectionManagerClient.getEndpointsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return connectionManagerClient.getEndpointByGUID(userId, endpointGUID);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return connectionManagerClient.findConnectorTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return connectionManagerClient.getConnectorTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String connectorTypeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return connectionManagerClient.getConnectorTypeByGUID(userId, connectorTypeGUID);
    }


    /* =====================================================================================================================
     * A ValidValue is the top level object for working with valid values
     */

    /**
     * Create a new metadata element to represent a valid value.
     *
     * @param validValueProperties properties about the valid value to store
     *
     * @return unique identifier of the new valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValue(ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String validValueGUID = validValueManagement.createValidValue(userId, externalSourceGUID, externalSourceName, validValueProperties);

        if ((validValueGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(validValueGUID);
        }

        return validValueGUID;
    }


    /**
     * Update the metadata element representing a valid value.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateValidValue(String               validValueGUID,
                                 boolean              isMergeUpdate,
                                 ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        validValueManagement.updateValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID, isMergeUpdate, validValueProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueGUID);
        }
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param properties describes the properties of the membership
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValueMember(String                         validValueSetGUID,
                                      ValidValueMembershipProperties properties,
                                      String                         validValueMemberGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        validValueManagement.setupValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, properties, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(validValueMemberGUID, validValueSetGUID);
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValueMember(String validValueSetGUID,
                                      String validValueMemberGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        validValueManagement.clearValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(validValueMemberGUID, validValueSetGUID);
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the permissions that the role has in the validValue
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValues(String                         elementGUID,
                                 ValidValueAssignmentProperties properties,
                                 String                         validValueGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        validValueManagement.setupValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValues(String elementGUID,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.clearValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the quality of the assignment
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupReferenceValueTag(String                             elementGUID,
                                       ReferenceValueAssignmentProperties properties,
                                       String                             validValueGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        validValueManagement.setupReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearReferenceValueTag(String elementGUID,
                                       String validValueGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        validValueManagement.clearReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the metadata element representing a valid value.
     *
     * @param validValueGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeValidValue(String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.removeValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(validValueGUID);
        }
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> findValidValues(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return validValueManagement.findValidValues(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getValidValuesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return validValueManagement.getValidValuesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid values.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getAllValidValues(int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return validValueManagement.getAllValidValues(userId, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getValidValueSetMembers(String  validValueSetGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return validValueManagement.getValidValueSetMembers(userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param validValueGUID          unique identifier of valid value to query
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getSetsForValidValue(String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getSetsForValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid value set linked to an element as its set of valid values.
     *
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ValidValueElement getValidValuesForConsumer(String elementGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return validValueManagement.getValidValuesForConsumer(userId, elementGUID);
    }


    /**
     * Return information about the consumers linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElement> getConsumersOfValidValue(String validValueGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getConsumersOfValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param elementGUID unique identifier for the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid values
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ValidValueElement> getReferenceValues(String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return validValueManagement.getReferenceValues(userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Return information about the person roles linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElement> getAssigneesOfReferenceValue(String validValueGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return validValueManagement.getAssigneesOfReferenceValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param validValueGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueElement getValidValueByGUID(String validValueGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return validValueManagement.getValidValueByGUID(userId, validValueGUID);
    }
}
