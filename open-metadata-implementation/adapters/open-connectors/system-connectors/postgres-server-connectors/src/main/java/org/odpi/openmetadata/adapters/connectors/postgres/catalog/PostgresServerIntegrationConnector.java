/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.catalog;

import org.odpi.openmetadata.accessservices.itinfrastructure.properties.OperationalStatus;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.postgres.utilities.PostgresUtils;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorConnector;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
    {
        super.start();
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
                                         connectionProperties.getConfigurationProperties(),
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

        if (DeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName().equals(requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName()))
        {
            String databaseServerGUID = requestedCatalogTarget.getCatalogTargetElement().getGUID();
            String databaseManagerGUID = this.getDatabaseManagerGUID(databaseServerGUID, requestedCatalogTarget.getCatalogTargetElement().getUniqueName());
            try
            {
                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(databaseServerGUID);

                JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;

                assetConnector.start();

                catalogDatabases(databaseServerGUID,
                                 databaseManagerGUID,
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
                                        DeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
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
            List<RelatedMetadataElement> relatedCapabilities = openMetadataAccess.getRelatedMetadataElements(databaseServerGUID,
                                                                                                             1,
                                                                                                             OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                             startFrom,
                                                                                                             getContext().getMaxPageSize());
            while (relatedCapabilities != null)
            {
                for (RelatedMetadataElement relatedCapability : relatedCapabilities)
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
                                                                                      propertyHelper.addStringProperty(null,
                                                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                       databaseServerQualifiedName + ":DBMS"),
                                                                                      databaseServerGUID,
                                                                                      OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                      propertyHelper.addEnumProperty(null,
                                                                                                                     OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                                                     OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
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
     * @param configurationProperties configuration properties
     * @param assetConnector connector to the database server
     * @throws ConnectorCheckedException unrecoverable error
     */
    private void catalogDatabases(String                databaseServerGUID,
                                  String                databaseManagerGUID,
                                  Map<String, Object>   configurationProperties,
                                  JDBCResourceConnector assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogDatabases";

        List<String> excludedDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                             configurationProperties);

        List<String> includedDatabases = super.getArrayConfigurationProperty(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                             configurationProperties);

        String catalogTemplateGUID = super.getStringConfigurationProperty(PostgresConfigurationProperty.DATABASE_CATALOG_TEMPLATE_QUALIFIED_NAME.getName(),
                                                                          configurationProperties);

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

                    if (PostgresUtils.databaseShouldBeCatalogued(databaseName, excludedDatabases, includedDatabases))
                    {
                        catalogDatabase(databaseName,
                                        databaseServerGUID,
                                        databaseManagerGUID,
                                        catalogTemplateGUID,
                                        configurationProperties);
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
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
     * @param catalogTemplateGUID unique identifier of the catalog template to use
     * @param configurationProperties configuration properties
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private   void catalogDatabase(String              databaseName,
                                   String              databaseServerGUID,
                                   String              databaseManagerGUID,
                                   String              catalogTemplateGUID,
                                   Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException,
                                                                                       ConnectorCheckedException
    {
        OpenMetadataAccess openMetadataAccess = getContext().getIntegrationGovernanceContext().getOpenMetadataAccess();
        ElementProperties   serverAssetUseProperties = propertyHelper.addEnumProperty(null,
                                                                                      OpenMetadataProperty.USE_TYPE.name,
                                                                                      OpenMetadataType.SERVER_ASSET_USE_TYPE_TYPE_NAME,
                                                                                      OpenMetadataType.SERVER_ASSET_USE_TYPE_OWNS_SYMBOLIC_NAME);
        {
            if (catalogTemplateGUID != null)
            {
                Map<String, String> placeholderProperties = super.getSuppliedPlaceholderProperties(configurationProperties);

                if (placeholderProperties == null)
                {
                    placeholderProperties = new HashMap<>();
                }

                placeholderProperties.put(PostgresConfigurationProperty.DATABASE_NAME.getName(), databaseName);

                OpenMetadataElement templateElement = openMetadataAccess.getMetadataElementByGUID(catalogTemplateGUID);

                String qualifiedName = propertyHelper.getResolvedStringPropertyFromTemplate(connectorName,
                                                                                            templateElement,
                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                            placeholderProperties);

                if (openMetadataAccess.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name) == null)
                {
                    openMetadataAccess.createMetadataElementFromTemplate(OpenMetadataType.RELATIONAL_DATABASE_TYPE_NAME,
                                                                         databaseServerGUID,
                                                                         false,
                                                                         null,
                                                                         null,
                                                                         catalogTemplateGUID,
                                                                         null,
                                                                         placeholderProperties,
                                                                         databaseManagerGUID,
                                                                         OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                         serverAssetUseProperties,
                                                                         true);
                }
            }
            else
            {
                String hostIdentifier = super.getStringConfigurationProperty(PostgresConfigurationProperty.HOST_IDENTIFIER.getName(), configurationProperties);
                String portNumber = super.getStringConfigurationProperty(PostgresConfigurationProperty.PORT_NUMBER.getName(), configurationProperties);
                String serverName = super.getStringConfigurationProperty(PostgresConfigurationProperty.SERVER_NAME.getName(), configurationProperties);
                String databaseUserId = super.getStringConfigurationProperty(PostgresConfigurationProperty.DATABASE_USER_ID.getName(), configurationProperties);
                String databasePassword = super.getStringConfigurationProperty(PostgresConfigurationProperty.DATABASE_PASSWORD.getName(), configurationProperties);

                String qualifiedName = "PostgreSQLDatabase:" + serverName + ":" + databaseName;

                if (openMetadataAccess.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name) == null)
                {
                    ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                           qualifiedName);
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.NAME.name,
                                                                         databaseName);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                         DeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType());

                    String databaseGUID = openMetadataAccess.createMetadataElementInStore(DeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
                                                                                          ElementStatus.ACTIVE,
                                                                                          null,
                                                                                          databaseServerGUID,
                                                                                          false,
                                                                                          null,
                                                                                          null,
                                                                                          elementProperties,
                                                                                          databaseManagerGUID,
                                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                                          serverAssetUseProperties,
                                                                                          true);

                    elementProperties = propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         "PostgreSQLDatabase:" + serverName + ":" + databaseName + ":Connection");
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                         databaseName + " connection");

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.USER_ID.name,
                                                                         databaseUserId);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataType.CLEAR_PASSWORD_PROPERTY_NAME,
                                                                         databasePassword);

                    String connectionGUID = openMetadataAccess.createMetadataElementInStore(OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                                            ElementStatus.ACTIVE,
                                                                                            null,
                                                                                            databaseGUID,
                                                                                            false,
                                                                                            null,
                                                                                            null,
                                                                                            elementProperties,
                                                                                            databaseGUID,
                                                                                            OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                            null,
                                                                                            false);


                    JDBCResourceConnectorProvider jdbcResourceConnectorProvider = new JDBCResourceConnectorProvider();

                    ConnectorType connectorType = jdbcResourceConnectorProvider.getConnectorType();

                    getContext().setupConnectorType(connectionGUID, connectorType.getGUID());

                    elementProperties = propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         "PostgreSQLDatabase:" + serverName + ":" + databaseName + ":Endpoint");
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.NAME.name,
                                                                         databaseName + " endpoint");

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataType.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                         "jdbc:postgresql://" + hostIdentifier + ":" + portNumber + "/" + databaseName);

                    openMetadataAccess.createMetadataElementInStore(OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                                    ElementStatus.ACTIVE,
                                                                    null,
                                                                    databaseGUID,
                                                                    false,
                                                                    null,
                                                                    null,
                                                                    elementProperties,
                                                                    connectionGUID,
                                                                    OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                    null,
                                                                    false);
                }
            }
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnect();
    }
}
