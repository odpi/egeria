/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.catalog;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorConnector;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PostgresServerIntegrationConnector retrieves details of the databases hosted on a PostgreSQL Database Server
 * and creates associated data assets/server capabilities/connections for them.
 */
public class PostgresServerIntegrationConnector extends InfrastructureIntegratorConnector implements CatalogTargetIntegrator
{
    final PropertyHelper propertyHelper = new PropertyHelper();

    List<String> defaultExcludeDatabases = null;
    List<String> defaultIncludeDatabases = null;
    String       defaultFriendshipGUID   = null;
    Map<String, String> defaultTemplates = new HashMap<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        defaultExcludeDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                      connectionDetails.getConfigurationProperties(),
                                                                      Collections.singletonList("postgres"));

        defaultIncludeDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                      connectionDetails.getConfigurationProperties());

        defaultFriendshipGUID = this.getFriendshipGUID(connectionDetails.getConfigurationProperties());

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                PostgresAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                       defaultFriendshipGUID));
        }

        for (PostgreSQLTemplateType templateType : PostgreSQLTemplateType.values())
        {
            defaultTemplates.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof JDBCResourceConnector jdbcResourceConnector)
                {
                    try
                    {
                        if (! jdbcResourceConnector.isActive())
                        {
                            jdbcResourceConnector.start();
                        }

                        catalogDatabases(null,
                                         null,
                                         defaultTemplates,
                                         connectionDetails.getConfigurationProperties(),
                                         jdbcResourceConnector);
                    }
                    catch (ConnectorCheckedException exception)
                    {
                        throw exception;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          exception.getClass().getName(),
                                                                                                          methodName,
                                                                                                          exception.getMessage()),
                                              exception);
                    }
                }
            }
        }

        this.refreshCatalogTargets(this);
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "integrateCatalogTarget";

        if (PostgresDeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName().equals(requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName()))
        {
            String databaseServerGUID = requestedCatalogTarget.getCatalogTargetElement().getGUID();
            String databaseManagerGUID = this.getDatabaseManagerGUID(databaseServerGUID, requestedCatalogTarget.getCatalogTargetElement().getUniqueName());
            try
            {
                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(databaseServerGUID, auditLog);

                JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;

                assetConnector.start();

                catalogDatabases(databaseServerGUID,
                                 databaseManagerGUID,
                                 requestedCatalogTarget.getTemplateProperties(),
                                 requestedCatalogTarget.getConfigurationProperties(),
                                 assetConnector);

                assetConnector.disconnect();
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                  exception.getClass().getName(),
                                                                                                  methodName,
                                                                                                  exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName(),
                                        PostgresDeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                                        connectorName,
                                        methodName);
        }
    }


    /**
     * Retrieve or recreate the database manager for this PostgreSQL Server.
     *
     * @param databaseServerGUID unique identifier of the database server
     * @param databaseServerQualifiedName unique name of the database server
     * @return unique identifier of the database manager
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    private String getDatabaseManagerGUID(String databaseServerGUID,
                                          String databaseServerQualifiedName) throws ConnectorCheckedException
    {
        final String methodName = "getDatabaseManagerGUID";

        String databaseManagerGUID = null;

        try
        {
            OpenMetadataAccess openMetadataAccess = getContext().getIntegrationGovernanceContext().getOpenMetadataAccess();

            int                          startFrom = 0;
            RelatedMetadataElementList relatedCapabilities = openMetadataAccess.getRelatedMetadataElements(databaseServerGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                           startFrom,
                                                                                                           getContext().getMaxPageSize());
            while ((relatedCapabilities != null) && (relatedCapabilities.getElementList() != null))
            {
                for (RelatedMetadataElement relatedCapability : relatedCapabilities.getElementList())
                {
                    if (OpenMetadataType.DATABASE_MANAGER.typeName.equals(relatedCapability.getElement().getType().getTypeName()))
                    {
                        databaseManagerGUID = relatedCapability.getElement().getElementGUID();
                        break;
                    }
                }

                startFrom           = startFrom + getContext().getMaxPageSize();
                relatedCapabilities = openMetadataAccess.getRelatedMetadataElements(databaseServerGUID,
                                                                                    1,
                                                                                    OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                    startFrom,
                                                                                    getContext().getMaxPageSize());
            }

            if (databaseManagerGUID == null)
            {
                databaseManagerGUID = openMetadataAccess.createMetadataElementInStore(OpenMetadataType.DATABASE_MANAGER.typeName,
                                                                                      ElementStatus.ACTIVE,
                                                                                      null,
                                                                                      databaseServerGUID,
                                                                                      false,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      propertyHelper.addStringProperty(null,
                                                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                       databaseServerQualifiedName + ":DBMS"),
                                                                                      databaseServerGUID,
                                                                                      OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                      propertyHelper.addEnumProperty(null,
                                                                                                                     OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                                                     OperationalStatus.getOpenTypeName(),
                                                                                                                     OperationalStatus.ENABLED.getName()),
                                                                                      true);
            }
        }
        catch (ConnectorCheckedException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);
        }

        return databaseManagerGUID;
    }


    /**
     * Catalog a database retrieved from the requested database server.
     *
     * @param databaseServerGUID unique identifier of the entity representing the PostgreSQL Server
     * @param databaseManagerGUID name of the associated database manager.
     * @param configuredTemplates list of templates
     * @param configurationProperties configuration properties
     * @param assetConnector connector to the database server
     * @throws ConnectorCheckedException unrecoverable error
     */
    private void catalogDatabases(String                databaseServerGUID,
                                  String                databaseManagerGUID,
                                  Map<String, String>   configuredTemplates,
                                  Map<String, Object>   configurationProperties,
                                  JDBCResourceConnector assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogDatabases";

        List<String> excludedDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                             configurationProperties,
                                                                             defaultExcludeDatabases);

        List<String> includedDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                             configurationProperties,
                                                                             defaultIncludeDatabases);

        Map<String, String>   templates = defaultTemplates;

        if (configuredTemplates != null)
        {
            templates = configuredTemplates;
        }

        try
        {
            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            final String sqlCommand1 = "SELECT datname, datistemplate, datallowconn from pg_database;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand1);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                /*
                 * This first test removes databases that are templates or do not allow connections.
                 */
                if ((!resultSet.getBoolean("datistemplate")) &&
                    (resultSet.getBoolean("datallowconn")))
                {
                    String databaseName = resultSet.getString("datname");

                    if (getContext().elementShouldBeCatalogued(databaseName, excludedDatabases, includedDatabases))
                    {
                        catalogDatabase(databaseName,
                                        databaseServerGUID,
                                        databaseManagerGUID,
                                        templates,
                                        configurationProperties);
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
            jdbcConnection.commit();
        }
        catch (ConnectorCheckedException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                  exception);
        }
    }


    /**
     * Catalog a database retrieved from the requested database server.
     *
     * @param databaseName name of the retrieved database
     * @param databaseServerGUID unique identifier of the entity representing the PostgreSQL Server
     * @param databaseManagerGUID name of the associated database manager
     * @param templates list of templates
     * @param configurationProperties configuration properties
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private   void catalogDatabase(String              databaseName,
                                   String              databaseServerGUID,
                                   String              databaseManagerGUID,
                                   Map<String, String> templates,
                                   Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException,
                                                                                       ConnectorCheckedException
    {
        final String methodName = "catalogDatabase";

        String friendshipConnectorGUID = getFriendshipGUID(configurationProperties);

        OpenMetadataAccess openMetadataAccess = getContext().getIntegrationGovernanceContext().getOpenMetadataAccess();
        ElementProperties   serverAssetUseProperties = propertyHelper.addEnumProperty(null,
                                                                                      OpenMetadataProperty.USE_TYPE.name,
                                                                                      ServerAssetUseType.getOpenTypeName(),
                                                                                      ServerAssetUseType.OWNS.getName());

        String databaseTemplateGUID = templates.get(PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID());

        if (databaseTemplateGUID == null)
        {
            databaseTemplateGUID = PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID();
        }

        Map<String, String> placeholderProperties = super.getSuppliedPlaceholderProperties(configurationProperties);

        if (placeholderProperties == null)
        {
            placeholderProperties = new HashMap<>();
        }

        placeholderProperties.put(PostgresConfigurationProperty.DATABASE_NAME.getName(), databaseName);

        OpenMetadataElement templateElement = openMetadataAccess.getMetadataElementByGUID(databaseTemplateGUID);

        String qualifiedName = propertyHelper.getResolvedStringPropertyFromTemplate(connectorName,
                                                                                    templateElement,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    placeholderProperties);

        OpenMetadataElement databaseElement = openMetadataAccess.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        if (databaseElement != null)
        {
            auditLog.logMessage(methodName, PostgresAuditCode.SKIPPING_DATABASE.getMessageDefinition(connectorName,
                                                                                                     databaseElement.getElementGUID(),
                                                                                                     qualifiedName));
        }
        else
        {
            String databaseGUID = openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                                                                    databaseServerGUID,
                                                                                    false,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    databaseTemplateGUID,
                                                                                    null,
                                                                                    placeholderProperties,
                                                                                    databaseManagerGUID,
                                                                                    OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                                    serverAssetUseProperties,
                                                                                    true);

            auditLog.logMessage(methodName, PostgresAuditCode.CATALOGED_DATABASE.getMessageDefinition(connectorName,
                                                                                                      qualifiedName,
                                                                                                      databaseGUID));

            addCatalogTarget(friendshipConnectorGUID,
                             databaseGUID,
                             databaseManagerGUID,
                             databaseName,
                             templates,
                             configurationProperties);
        }
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for connection to UC
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = connectionDetails.getConfigurationProperties().get(PostgresConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Add a catalog target relationship between the PostgreSQL server's asset and the connector that is able to
     * catalog inside a PostgreSQL Database.  This will start the cataloging of the schemas tables and columns within the database.
     *
     * @param friendshipConnectorGUID guid of database connector
     * @param databaseGUID unique identifier of the catalog
     * @param dbmsQualifiedName qualified name of the database's software capability - becomes metadataSourceQualifiedName
     * @param databaseName name of the catalog - may be used as a placeholder property
     * @param templates list of templates
     * @param configurationProperties configuration properties for this server
     *
     * @throws InvalidParameterException error from call to the metadata store
     * @throws PropertyServerException error from call to the metadata store
     * @throws UserNotAuthorizedException error from call to the metadata store
     */
    private void addCatalogTarget(String              friendshipConnectorGUID,
                                  String              databaseGUID,
                                  String              dbmsQualifiedName,
                                  String              databaseName,
                                  Map<String, String> templates,
                                  Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "addCatalogTarget";

        if (databaseGUID != null)
        {
            if (friendshipConnectorGUID != null)
            {
                CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

                catalogTargetProperties.setCatalogTargetName(databaseName);
                catalogTargetProperties.setMetadataSourceQualifiedName(dbmsQualifiedName);
                catalogTargetProperties.setTemplateProperties(templates);

                Map<String, Object> targetConfigurationProperties = new HashMap<>();

                if (configurationProperties != null)
                {
                    targetConfigurationProperties.putAll(configurationProperties);
                }

                targetConfigurationProperties.put(PostgresConfigurationProperty.DATABASE_NAME.getName(), databaseName);
                targetConfigurationProperties.put(OpenMetadataProperty.GUID.name, databaseGUID);

                catalogTargetProperties.setConfigurationProperties(targetConfigurationProperties);

                String relationshipGUID = integrationContext.addCatalogTarget(friendshipConnectorGUID, databaseGUID, catalogTargetProperties);

                auditLog.logMessage(methodName,
                                    PostgresAuditCode.NEW_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                              relationshipGUID,
                                                                                              friendshipConnectorGUID,
                                                                                              databaseGUID,
                                                                                              databaseName));
            }
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnect();
    }
}
