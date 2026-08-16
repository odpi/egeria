/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.catalog;

import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.mssql.ffdc.MSSQLAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SoftwareCapabilityClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.DatabaseManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MSSQLServerIntegrationConnector retrieves details of the databases hosted on a Microsoft SQL Server Database Server
 * and creates associated data assets/server capabilities/connections for them.
 */
public class MSSQLServerCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    final PropertyHelper propertyHelper = new PropertyHelper();

    List<String>        defaultExcludeDatabases = null;
    List<String>        defaultIncludeDatabases = null;
    String              defaultFriendshipGUID   = null;
    Map<String, String> defaultTemplates        = new HashMap<>();


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public MSSQLServerCatalogTargetProcessor(CatalogTarget        catalogTarget,
                                             CatalogTargetContext integrationContext,
                                             Connector            connectorToTarget,
                                             String               connectorName,
                                             AuditLog             auditLog)
    {
        super(catalogTarget, integrationContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        defaultExcludeDatabases = super.getArrayConfigurationProperty(MSSQLConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                      this.getConfigurationProperties(),
                                                                      Collections.emptyList());

        defaultIncludeDatabases = super.getArrayConfigurationProperty(MSSQLConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                      this.getConfigurationProperties());

        defaultFriendshipGUID = this.getFriendshipGUID(this.getConfigurationProperties());

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                MSSQLAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                   defaultFriendshipGUID));
        }

        for (MSSQLTemplateType templateType : MSSQLTemplateType.values())
        {
            defaultTemplates.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if (propertyHelper.isTypeOf(this.getCatalogTargetElement().getElementHeader(), MSSQLDeployedImplementationType.MSSQL_SERVER.getAssociatedTypeName()))
        {
            JDBCResourceConnector assetConnector = null;

            try
            {
                String                  databaseServerGUID  = this.getCatalogTargetElement().getElementHeader().getGUID();
                OpenMetadataRootElement databaseManager     = this.getDatabaseManager(databaseServerGUID, this.getCatalogTargetElement());

                Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(databaseServerGUID, auditLog);

                assetConnector = (JDBCResourceConnector)connector;

                assetConnector.start();

                catalogDatabases(databaseServerGUID,
                                 databaseManager,
                                 this.getTemplates(),
                                 this.getConfigurationProperties(),
                                 assetConnector);
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      MSSQLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              exception.getClass().getName(),
                                                                                              methodName,
                                                                                              exception.getMessage()),
                                      exception);
            }
            finally
            {
                this.releaseAssetConnector(assetConnector, methodName);
            }
        }
        else
        {
            super.throwWrongTypeOfCatalogTarget(MSSQLDeployedImplementationType.MSSQL_SERVER.getAssociatedTypeName(),
                                                methodName);
        }
    }


    /**
     * Release the resource connector that refresh() created for this pass.  A new connector is created on every
     * refresh, so it must be released on both the success and the failure path; otherwise each failed refresh
     * leaks the connector along with the JDBC connections it has cached - and those are left idle-in-transaction
     * until the database times them out.
     * <br><br>
     * A failure to disconnect is logged rather than thrown so that it does not mask the error that caused the
     * cataloguing work to be abandoned.
     *
     * @param assetConnector connector to release - may be null if it was never successfully created
     * @param callingMethodName method reporting the original error
     */
    private void releaseAssetConnector(JDBCResourceConnector assetConnector,
                                       String                callingMethodName)
    {
        if ((assetConnector != null) && (assetConnector.isActive()))
        {
            try
            {
                assetConnector.disconnect();
            }
            catch (Exception exception)
            {
                auditLog.logException(callingMethodName,
                                      MSSQLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              exception.getClass().getName(),
                                                                                              callingMethodName,
                                                                                              exception.getMessage()),
                                      exception);
            }
        }
    }


    /**
     * Retrieve or recreate the database manager for this Microsoft SQL Server.
     *
     * @param databaseServerGUID    unique identifier of the database server
     * @param databaseServerElement unique name of the database server
     * @return the database manager
     * @throws InvalidParameterException  the type name, status, or one of the properties is invalid
     * @throws PropertyServerException    a problem with the metadata store
     * @throws UserNotAuthorizedException the connector is not authorized to create this type of element
     */
    private OpenMetadataRootElement getDatabaseManager(String                  databaseServerGUID,
                                                       OpenMetadataRootElement databaseServerElement) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient(OpenMetadataType.DATABASE_MANAGER.typeName);

        String databaseManagerGUID = null;

        /*
         * If the database server has a database manager, then use it.
         */
        if (databaseServerElement.getCapabilities() != null)
        {
            for (RelatedMetadataElementSummary relatedCapability : databaseServerElement.getCapabilities())
            {
                if ((relatedCapability != null) && (propertyHelper.isTypeOf(relatedCapability.getRelatedElement().getElementHeader(), OpenMetadataType.DATABASE_MANAGER.typeName)))
                {
                    databaseManagerGUID = relatedCapability.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        /*
         * No database manager, so need to create one
         */
        if (databaseManagerGUID == null)
        {
            if (databaseServerElement.getProperties() instanceof SoftwareServerProperties softwareServerProperties)
            {
                NewElementOptions newElementOptions = new NewElementOptions(softwareCapabilityClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(databaseServerGUID);
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorScopeGUIDs(null);
                newElementOptions.setParentGUID(databaseServerGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

                SoftwareCapabilityProperties softwareCapabilityProperties = new SoftwareCapabilityProperties();
                softwareCapabilityProperties.setQualifiedName(softwareServerProperties.getQualifiedName() + ":DBMS");
                softwareCapabilityProperties.setDisplayName("DBMS for " + softwareServerProperties.getDisplayName());
                softwareCapabilityProperties.setDescription("The Database Management System (DBMS) provides support for the creation, maintenance, and access to the data stored in the server's databases.");

                databaseManagerGUID = softwareCapabilityClient.createSoftwareCapability(newElementOptions,
                                                                                        null,
                                                                                        softwareCapabilityProperties,
                                                                                        null);
            }
        }

        return softwareCapabilityClient.getSoftwareCapabilityByGUID(databaseManagerGUID, softwareCapabilityClient.getGetOptions());
    }


    /**
     * Catalog a database retrieved from the requested database server.
     *
     * @param databaseServerGUID unique identifier of the entity representing the Microsoft SQL Server
     * @param databaseManager associated database manager
     * @param configuredTemplates list of templates
     * @param configurationProperties configuration properties
     * @param assetConnector connector to the database server
     * @throws ConnectorCheckedException unrecoverable error
     */
    private void catalogDatabases(String                  databaseServerGUID,
                                  OpenMetadataRootElement databaseManager,
                                  Map<String, String>     configuredTemplates,
                                  Map<String, Object>     configurationProperties,
                                  JDBCResourceConnector   assetConnector) throws ConnectorCheckedException
    {
        final String methodName = "catalogDatabases";

        List<String> excludedDatabases = super.getArrayConfigurationProperty(MSSQLConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                             configurationProperties,
                                                                             defaultExcludeDatabases);

        List<String> includedDatabases = super.getArrayConfigurationProperty(MSSQLConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
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

            /*
             * database_id > 4 excludes the four fixed Microsoft SQL Server system databases
             * (master=1, tempdb=2, model=3, msdb=4).
             */
            final String sqlCommand1 = "SELECT name, state_desc from sys.databases WHERE database_id > 4;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand1);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                /*
                 * This first test removes databases that are not currently able to be connected to.
                 */
                if ("ONLINE".equals(resultSet.getString("state_desc")))
                {
                    String databaseName = resultSet.getString("name");

                    if (integrationContext.elementShouldBeCatalogued(databaseName, excludedDatabases, includedDatabases))
                    {
                        catalogDatabase(databaseName,
                                        databaseServerGUID,
                                        databaseManager,
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
                                  MSSQLAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseServerGUID unique identifier of the entity representing the Microsoft SQL Server
     * @param databaseManager associated database manager
     * @param templates list of templates
     * @param configurationProperties configuration properties
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException a problem with the metadata store
     */
    private   void catalogDatabase(String                  databaseName,
                                   String                  databaseServerGUID,
                                   OpenMetadataRootElement databaseManager,
                                   Map<String, String>     templates,
                                   Map<String, Object>     configurationProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException,
                                                                                           ConnectorCheckedException
    {
        final String methodName = "catalogDatabase";

        String friendshipConnectorGUID = getFriendshipGUID(configurationProperties);

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        ElementProperties serverAssetUseProperties = propertyHelper.addEnumProperty(null,
                                                                                    OpenMetadataProperty.USE_TYPE.name,
                                                                                    CapabilityAssetUseType.getOpenTypeName(),
                                                                                    CapabilityAssetUseType.OWNS.name());

        String databaseTemplateGUID = templates.get(MSSQLTemplateType.MSSQL_DATABASE_TEMPLATE.getTemplateGUID());

        if (databaseTemplateGUID == null)
        {
            databaseTemplateGUID = MSSQLTemplateType.MSSQL_DATABASE_TEMPLATE.getTemplateGUID();
        }

        Map<String, String> placeholderProperties = super.getSuppliedPlaceholderProperties(configurationProperties);

        if (placeholderProperties == null)
        {
            placeholderProperties = new HashMap<>();
        }

        placeholderProperties.put(MSSQLPlaceholderProperty.DATABASE_NAME.getName(), databaseName);
        placeholderProperties.put(MSSQLPlaceholderProperty.DATABASE_DESCRIPTION.getName(), null);

        OpenMetadataElement templateElement = openMetadataStore.getMetadataElementByGUID(databaseTemplateGUID);

        String qualifiedName = propertyHelper.getResolvedStringPropertyFromTemplate(connectorName,
                                                                                    templateElement,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    placeholderProperties);

        OpenMetadataElement databaseElement = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        if (databaseElement != null)
        {
            auditLog.logMessage(methodName, MSSQLAuditCode.SKIPPING_DATABASE.getMessageDefinition(connectorName,
                                                                                                   databaseElement.getElementGUID(),
                                                                                                   qualifiedName));
        }
        else if (databaseManager.getProperties() instanceof DatabaseManagerProperties databaseManagerProperties)
        {
            String databaseGUID = openMetadataStore.getMetadataElementFromTemplate(OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                                                                    databaseServerGUID,
                                                                                    false,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    databaseTemplateGUID,
                                                                                    null,
                                                                                    null,
                                                                                    placeholderProperties,
                                                                                    databaseManager.getElementHeader().getGUID(),
                                                                                    OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                                                    serverAssetUseProperties,
                                                                                    true);

            auditLog.logMessage(methodName, MSSQLAuditCode.CATALOGED_DATABASE.getMessageDefinition(connectorName,
                                                                                                    qualifiedName,
                                                                                                    databaseGUID));

            addCatalogTarget(friendshipConnectorGUID,
                             databaseGUID,
                             databaseManagerProperties.getQualifiedName(),
                             databaseName,
                             templates,
                             configurationProperties);
        }
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for connection to the database server
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(MSSQLConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = this.getConfigurationProperties().get(MSSQLConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Add a catalog target relationship between the Microsoft SQL Server's asset and the connector that is able to
     * catalog inside a Microsoft SQL Server Database.  This will start the cataloging of the schemas tables and columns within the database.
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
                catalogTargetProperties.setMetadataCollectionQualifiedName(dbmsQualifiedName);
                catalogTargetProperties.setTemplates(templates);

                Map<String, Object> targetConfigurationProperties = new HashMap<>();

                if (configurationProperties != null)
                {
                    targetConfigurationProperties.putAll(configurationProperties);
                }

                targetConfigurationProperties.put(MSSQLConfigurationProperty.DATABASE_NAME.getName(), databaseName);
                targetConfigurationProperties.put(OpenMetadataProperty.GUID.name, databaseGUID);

                catalogTargetProperties.setConfigurationProperties(targetConfigurationProperties);

                String relationshipGUID = integrationContext.getAssetClient().addCatalogTarget(friendshipConnectorGUID,
                                                                                               databaseGUID,
                                                                                               new MakeAnchorOptions(integrationContext.getAssetClient().getMetadataSourceOptions()),
                                                                                               catalogTargetProperties);

                auditLog.logMessage(methodName,
                                    MSSQLAuditCode.NEW_CATALOG_TARGET.getMessageDefinition(connectorName,
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
     * @throws ConnectorCheckedException the connector detected a problem.
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
