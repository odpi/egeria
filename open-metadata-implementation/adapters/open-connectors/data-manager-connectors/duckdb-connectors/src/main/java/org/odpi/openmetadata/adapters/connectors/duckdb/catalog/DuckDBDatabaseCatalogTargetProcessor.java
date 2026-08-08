/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.DuckDBDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBTemplateType;
import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode;
import org.odpi.openmetadata.adapters.connectors.duckdb.survey.DuckDBFederationExtractor;
import org.odpi.openmetadata.adapters.connectors.duckdb.survey.SurveyDuckDBAnnotationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.utilities.DuckDBUtils;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls.FilesTemplateType;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.EndpointClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.TabularDataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.RelationalDatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DerivedSchemaTypeQueryTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularFileColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DuckDBDatabaseCatalogTargetProcessor is the most novel of the DuckDB connectors.  A DuckDB database is embedded
 * (a single ".duckdb" file, or an in-memory ":memory:" session) so there is no equivalent of the "server" catalog
 * target used by the other database connector suites for the connector to be pointed at - the catalog target
 * configured for this processor may be any convenient, already-catalogued element (for example, a FileFolder for
 * the directory the database file lives in, or a previous run's own DuckDB database asset); the actual identity of
 * the DuckDB database is always (re)computed from the {@link DuckDBConfigurationProperty#DATABASE_PATH}
 * configuration property using the files-integration-connector's FileClassifier, exactly the way
 * DataFilesMonitorForTarget.catalogFile() computes the identity of a data file.
 * <br>
 * DESIGN NOTE (judgement call): CatalogTargetProcessorBase's contract assumes the catalog target element already
 * exists, but the whole point of this processor is that it can materialise the RelationalDatabase asset for a
 * DuckDB file the first time it is refreshed.  This processor therefore always looks the database asset up by its
 * FileClassifier-derived qualifiedName first (which will typically find the catalog target element itself, once it
 * has been catalogued once) and only creates a brand-new self-anchored asset if no match is found - see
 * {@link #getOrCreateDuckDBDatabaseAsset(String, Map)}.
 * <br>
 * Once the database asset's identity is settled, this processor does two more things on every refresh:
 * <ol>
 *     <li>hands off cataloguing of the database's native schemas/tables/columns to the "friendship" JDBC
 *     integration connector, using exactly the same catalog target hand-off mechanism as the other four vendor
 *     connector suites;</li>
 *     <li>catalogues DuckDB's federation relationships (databases ATTACH-ed to this one, and views that scan
 *     external files/object-store resources) - see {@link #catalogFederationRelationships(String, String, Connection)}.
 *     This reuses DuckDBFederationExtractor's discovery queries (its SurveyActionServiceConnector constructor
 *     parameter is unused by its query logic, so null is passed here) rather than duplicating the SQL/regex
 *     matching a second time.</li>
 * </ol>
 */
public class DuckDBDatabaseCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    final PropertyHelper propertyHelper = new PropertyHelper();

    String              defaultFriendshipGUID = null;
    Map<String, String> defaultTemplates      = new HashMap<>();

    String fileSystemName      = FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getExample();
    String localMountPoint     = FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample();
    String canonicalMountPoint = FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getExample();


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param integrationContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public DuckDBDatabaseCatalogTargetProcessor(CatalogTarget        catalogTarget,
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
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        defaultFriendshipGUID = this.getFriendshipGUID(this.getConfigurationProperties());

        if (defaultFriendshipGUID != null)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.FRIENDSHIP_GUID.getMessageDefinition(connectorName,
                                                                                     defaultFriendshipGUID));
        }

        for (DuckDBTemplateType templateType : DuckDBTemplateType.values())
        {
            defaultTemplates.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }

        if (this.getConfigurationProperties() != null)
        {
            if (this.getConfigurationProperties().get(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName()) != null)
            {
                fileSystemName = this.getConfigurationProperties().get(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName()).toString();
            }
            if (this.getConfigurationProperties().get(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName()) != null)
            {
                localMountPoint = this.getConfigurationProperties().get(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName()).toString();
            }
            if (this.getConfigurationProperties().get(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName()) != null)
            {
                canonicalMountPoint = this.getConfigurationProperties().get(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName()).toString();
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        Map<String, Object> configurationProperties = this.getConfigurationProperties();

        String databasePath = super.getStringConfigurationProperty(DuckDBConfigurationProperty.DATABASE_PATH.getName(), configurationProperties);

        try
        {
            /*
             * Step 1 - establish the identity of the DuckDB database asset via the FileClassifier (skipped for an
             * in-memory database, which has no file to classify).
             */
            String databaseGUID = this.getOrCreateDuckDBDatabaseAsset(databasePath, configurationProperties);

            if (databaseGUID != null)
            {
                /*
                 * Step 2 - hand off cataloguing of the database's native schemas/tables/columns to the friendship
                 * connector (the generic JDBC integration connector), exactly as the other four database connector
                 * suites do for their own database assets.
                 */
                this.addCatalogTargetToFriendshipConnector(databaseGUID,
                                                           (databasePath == null ? "duckdbDatabase" : databasePath),
                                                           configurationProperties);

                /*
                 * Step 3 - catalog DuckDB's federation relationships (attached databases and external file scans).
                 * This is wrapped defensively - a single database's lack of, or problem with, federation metadata
                 * must never prevent the rest of the catalog pass from completing.
                 */
                try (Connection duckdbConnection = this.openDuckDBConnection(databasePath))
                {
                    /*
                     * DuckDB does not persist ATTACH-ed data sources in the database file between sessions, so
                     * this connection must re-issue any configured ATTACH (and INSTALL/LOAD) statements itself
                     * before federation discovery queries duckdb_databases().
                     */
                    DuckDBUtils.runAttachStatements(duckdbConnection,
                                                    DuckDBUtils.getAttachStatements(configurationProperties),
                                                    auditLog,
                                                    connectorName,
                                                    databasePath);

                    this.catalogFederationRelationships(databaseGUID, databasePath, duckdbConnection);
                }
                catch (Exception error)
                {
                    auditLog.logMessage(methodName,
                                        DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                                      "federation discovery",
                                                                                                      databasePath,
                                                                                                      error.getMessage()));
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  DuckDBAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);
        }
    }


    /**
     * Open a direct JDBC connection to the DuckDB database file/session.  This processor opens its own connection
     * (rather than always relying on connectorToTarget) because, on the very first refresh of a brand-new catalog
     * target, connectorToTarget's connection may not yet describe the DuckDB file itself.
     *
     * @param databasePath path to the ".duckdb" file, or ":memory:"/null
     * @return JDBC connection
     * @throws java.sql.SQLException problem opening the database
     */
    private Connection openDuckDBConnection(String databasePath) throws java.sql.SQLException
    {
        if ((connectorToTarget instanceof JDBCResourceConnector jdbcResourceConnector) && (jdbcResourceConnector.getDataSource() != null))
        {
            return jdbcResourceConnector.getDataSource().getConnection();
        }

        return DriverManager.getConnection(DuckDBUtils.getDatabaseURL(databasePath));
    }


    /**
     * Determine the identity of the DuckDB database asset using the FileClassifier (for a file-based database),
     * matching or creating a self-anchored RelationalDatabase asset for it.  An in-memory database has no file to
     * classify, so its identity is instead based on the configured/default database name.
     *
     * @param databasePath path to the ".duckdb" file, or ":memory:"/null
     * @param configurationProperties configuration properties for this catalog target
     * @return unique identifier of the (matched or newly created) DuckDB database asset
     * @throws Exception problem accessing the open metadata repositories
     */
    private String getOrCreateDuckDBDatabaseAsset(String               databasePath,
                                                   Map<String, Object> configurationProperties) throws Exception
    {
        final String methodName = "getOrCreateDuckDBDatabaseAsset";

        /*
         * Prefer the catalog target element this processor was actually given.  Confirmed as a real bug via live
         * testing this session: when a governance action process creates the RelationalDatabase asset from
         * DUCKDB_DATABASE_TEMPLATE and then wires it as this connector's catalog target (the normal
         * CreateAsCatalogTargetGovernanceActionProcess flow), unconditionally recomputing the identity below via
         * the FileClassifier produces a qualifiedName that does not match the template's own convention, so a
         * second, duplicate RelationalDatabase asset for the same physical file gets created every refresh.  If the
         * given catalog target is already the RelationalDatabase asset, reuse it directly; only fall back to
         * FileClassifier-based matching/creation below when it is something else (for example, a FileFolder for the
         * directory the file lives in - see the class-level design note on the first-ever-refresh case).
         */
        if ((super.getCatalogTargetElement() != null) && (super.getCatalogTargetElement().getElementHeader() != null) &&
                (super.getCatalogTargetElement().getElementHeader().getType() != null) &&
                (OpenMetadataType.RELATIONAL_DATABASE.typeName.equals(super.getCatalogTargetElement().getElementHeader().getType().getTypeName())))
        {
            return super.getCatalogTargetElement().getElementHeader().getGUID();
        }

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.RELATIONAL_DATABASE.typeName);

        String qualifiedName;
        String fileAddress;
        String databaseName = super.getStringConfigurationProperty(DuckDBConfigurationProperty.DATABASE_NAME.getName(), configurationProperties);

        if (DuckDBUtils.isInMemoryDatabase(databasePath))
        {
            if (databaseName == null)
            {
                databaseName = "memory";
            }

            qualifiedName = OpenMetadataType.RELATIONAL_DATABASE.typeName + "::" + fileSystemName + ":" + DuckDBUtils.IN_MEMORY_DATABASE + ":" + databaseName;
            fileAddress   = DuckDBUtils.IN_MEMORY_DATABASE;
        }
        else
        {
            FileClassifier     fileClassifier     = integrationContext.getFileClassifier(fileSystemName, canonicalMountPoint, localMountPoint);
            FileClassification fileClassification = fileClassifier.classifyFile(new File(databasePath));

            /*
             * The generic file classification's own guess at asset type/deployedImplementationType is discarded -
             * a DuckDB database is always catalogued as a RelationalDatabase, never as a generic DataFile.
             */
            qualifiedName = OpenMetadataType.RELATIONAL_DATABASE.typeName + "::" + fileClassification.getFileSystemName() + ":" + fileClassification.getCanonicalPathName();
            fileAddress   = fileClassification.getFileAddress();

            if (databaseName == null)
            {
                databaseName = fileClassification.getFileName();
            }
        }

        OpenMetadataRootElement existingAsset = assetClient.getAssetByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, assetClient.getGetOptions());

        if (existingAsset != null)
        {
            auditLog.logMessage(methodName, DuckDBAuditCode.SKIPPING_DATABASE.getMessageDefinition(connectorName,
                                                                                                     existingAsset.getElementHeader().getGUID(),
                                                                                                     qualifiedName));

            return existingAsset.getElementHeader().getGUID();
        }

        String databaseGUID = this.createDuckDBDatabaseAsset(qualifiedName, databaseName, fileAddress);

        auditLog.logMessage(methodName, DuckDBAuditCode.CATALOGED_DATABASE.getMessageDefinition(connectorName,
                                                                                                  qualifiedName,
                                                                                                  databaseGUID));

        return databaseGUID;
    }


    /**
     * Create a new self-anchored (isOwnAnchor=true) RelationalDatabase asset for the DuckDB database, plus its
     * Connection and Endpoint, following the same pattern used by
     * DataFilesMonitorForTarget.addDataFileToCatalog() in files-integration-connectors for a DataFile asset.
     *
     * @param qualifiedName unique name for the new asset
     * @param databaseName  display name for the new asset
     * @param fileAddress   real (unmasked) network address to store in the Connection's Endpoint - used to build the JDBC URL
     * @return unique identifier of the new asset
     * @throws Exception problem accessing the open metadata repositories
     */
    private String createDuckDBDatabaseAsset(String qualifiedName,
                                             String databaseName,
                                             String fileAddress) throws Exception
    {
        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.RELATIONAL_DATABASE.typeName);

        RelationalDatabaseProperties properties = new RelationalDatabaseProperties();

        properties.setTypeName(OpenMetadataType.RELATIONAL_DATABASE.typeName);
        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(databaseName);
        properties.setResourceName(databaseName);
        properties.setDeployedImplementationType(DuckDBDeployedImplementationType.DUCKDB_DATABASE.getDeployedImplementationType());

        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        String databaseGUID = assetClient.createAsset(newElementOptions, null, properties, null);

        if (databaseGUID != null)
        {
            ConnectionClient connectionClient = integrationContext.getConnectionClient();

            ConnectionProperties connectionProperties = new ConnectionProperties();

            NewElementOptions connectionOptions = new NewElementOptions(connectionClient.getMetadataSourceOptions());

            connectionOptions.setIsOwnAnchor(false);
            connectionOptions.setAnchorGUID(databaseGUID);
            connectionOptions.setParentAtEnd1(true);
            connectionOptions.setParentGUID(databaseGUID);
            connectionOptions.setParentRelationshipTypeName(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

            connectionProperties.setQualifiedName(qualifiedName + "_connection");

            String connectionGUID = connectionClient.createConnection(connectionOptions, null, connectionProperties, null);

            connectionClient.linkConnectionConnectorType(connectionGUID,
                                                         EgeriaOpenConnectorDefinition.JDBC_RESOURCE_CONNECTOR.getConnectorTypeGUID(),
                                                         connectionClient.getMakeAnchorOptions(false),
                                                         null);

            EndpointClient endpointClient = integrationContext.getEndpointClient();

            EndpointProperties endpointProperties = new EndpointProperties();
            endpointProperties.setQualifiedName(qualifiedName + "_endpoint");
            endpointProperties.setNetworkAddress(DuckDBUtils.getDatabaseURL(fileAddress));

            NewElementOptions endpointOptions = new NewElementOptions(endpointClient.getMetadataSourceOptions());

            endpointOptions.setIsOwnAnchor(false);
            endpointOptions.setAnchorGUID(databaseGUID);
            endpointOptions.setParentAtEnd1(true);
            endpointOptions.setParentGUID(connectionGUID);
            endpointOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

            endpointClient.createEndpoint(endpointOptions, null, endpointProperties, null);
        }

        return databaseGUID;
    }


    /**
     * Add a catalog target relationship between the friendship connector (the generic JDBC integration connector)
     * and the DuckDB database asset.  This starts the cataloguing of the database's native schemas, tables and
     * columns.  This mirrors PostgresServerCatalogTargetProcessor.addCatalogTarget() exactly, substituting the
     * DuckDB database asset for the per-database asset that the Postgres processor creates from its server target.
     *
     * @param databaseGUID unique identifier of the DuckDB database asset
     * @param databaseName name/path of the DuckDB database - used as the catalog target name
     * @param configurationProperties configuration properties for this catalog target
     * @throws Exception problem accessing the open metadata repositories
     */
    private void addCatalogTargetToFriendshipConnector(String               databaseGUID,
                                                        String               databaseName,
                                                        Map<String, Object> configurationProperties) throws Exception
    {
        final String methodName = "addCatalogTargetToFriendshipConnector";

        String friendshipConnectorGUID = getFriendshipGUID(configurationProperties);

        if (friendshipConnectorGUID != null)
        {
            org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties catalogTargetProperties =
                    new org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties();

            catalogTargetProperties.setCatalogTargetName(databaseName);
            catalogTargetProperties.setTemplates(defaultTemplates);
            catalogTargetProperties.setConfigurationProperties(configurationProperties);

            String relationshipGUID = integrationContext.getAssetClient().addCatalogTarget(friendshipConnectorGUID,
                                                                                            databaseGUID,
                                                                                            new org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions(integrationContext.getAssetClient().getMetadataSourceOptions()),
                                                                                            catalogTargetProperties);

            auditLog.logMessage(methodName,
                                DuckDBAuditCode.NEW_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                        relationshipGUID,
                                                                                        friendshipConnectorGUID,
                                                                                        databaseGUID,
                                                                                        databaseName));
        }
    }


    /**
     * Discover and catalog DuckDB's federation relationships: databases ATTACH-ed to the DuckDB database, and
     * views that scan external files/object-store resources.  Every finding is processed independently and
     * defensively - a problem resolving or creating the asset for one federation relationship must not prevent
     * the others from being catalogued.
     *
     * @param databaseGUID     unique identifier of the DuckDB database asset
     * @param databaseName     name/path of the DuckDB database - used only for audit log messages
     * @param duckdbConnection JDBC connection to the DuckDB database
     */
    private void catalogFederationRelationships(String     databaseGUID,
                                                String     databaseName,
                                                Connection duckdbConnection)
    {
        final String methodName = "catalogFederationRelationships";

        /*
         * Reuse DuckDBFederationExtractor's discovery queries rather than duplicating the SQL/regex matching.
         * Its SurveyActionServiceConnector constructor parameter is only ever used to format JSON properties
         * for survey reports, which this catalog-target-processor context never needs, so null is passed here.
         */
        DuckDBFederationExtractor federationExtractor = new DuckDBFederationExtractor(null, auditLog, connectorName);

        federationExtractor.discoverAttachedSources(duckdbConnection, databaseName);
        federationExtractor.discoverExternalFileSources(duckdbConnection, databaseName);

        /*
         * The DuckDB database asset's own (real) qualifiedName is needed to locate the friendship connector's
         * RelationalTable/RelationalColumn elements for views and attached tables - looked up once here, rather
         * than in every finding, since it does not change during this refresh.
         */
        String databaseQualifiedName = this.getDatabaseQualifiedName(databaseGUID);

        for (AnnotationProperties annotation : federationExtractor.getAnnotations())
        {
            if (annotation instanceof ResourceMeasureAnnotationProperties resourceMeasureAnnotation)
            {
                try
                {
                    if (SurveyDuckDBAnnotationType.ATTACHED_SOURCE.getName().equals(resourceMeasureAnnotation.getAnnotationType()))
                    {
                        this.catalogAttachedSource(databaseGUID, resourceMeasureAnnotation.getResourceProperties(), duckdbConnection, federationExtractor);
                    }
                    else if (SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE.getName().equals(resourceMeasureAnnotation.getAnnotationType()))
                    {
                        this.catalogExternalFileSource(databaseGUID, databaseQualifiedName, resourceMeasureAnnotation.getResourceProperties(), duckdbConnection, federationExtractor);
                    }
                }
                catch (Exception error)
                {
                    auditLog.logMessage(methodName,
                                        DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                                      resourceMeasureAnnotation.getResourceProperties().toString(),
                                                                                                      databaseName,
                                                                                                      error.getMessage()));
                }
            }
        }
    }


    /**
     * Look up the DuckDB database asset's own (real) qualifiedName by its GUID.  This is needed to reconstruct the
     * deterministic qualifiedNames that the friendship connector (RelationalDatabaseCataloguer) builds for the
     * DuckDB database's own tables/views/columns, so that this processor can locate (never create) them.
     *
     * @param databaseGUID unique identifier of the DuckDB database asset
     * @return qualifiedName, or null if it could not be determined
     */
    private String getDatabaseQualifiedName(String databaseGUID)
    {
        final String methodName = "getDatabaseQualifiedName";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.RELATIONAL_DATABASE.typeName);

            OpenMetadataRootElement databaseElement = assetClient.getAssetByGUID(databaseGUID, assetClient.getGetOptions());

            return assetClient.getQualifiedName(databaseElement);
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.FEDERATION_QUERY_FAILED.getMessageDefinition(connectorName,
                                                                                              "database qualifiedName lookup",
                                                                                              databaseGUID,
                                                                                              error.getMessage()));
            return null;
        }
    }


    /**
     * Catalog one attached-database federation finding.  A file-backed source (DuckDB-to-DuckDB, or sqlite) reuses
     * the FileClassifier + FilesTemplateType matching machinery; anything else is treated as network-backed, in
     * which case the tables it exposes are additionally catalogued as TabularDataSet assets (see
     * {@link #catalogAttachedDatabaseTables}).
     *
     * @param databaseGUID       unique identifier of the DuckDB database asset
     * @param resourceProperties "Alias"/"Source Type"/"Location"/"Read Only" properties describing the finding
     * @param duckdbConnection   JDBC connection to the DuckDB database - used to discover the attached data source's
     *                           own tables/columns for a network-backed source
     * @param federationExtractor shared extractor instance, reused for its duckdb_tables()/duckdb_columns() queries
     * @throws Exception problem accessing the open metadata repositories
     */
    private void catalogAttachedSource(String                     databaseGUID,
                                       Map<String, String>        resourceProperties,
                                       Connection                  duckdbConnection,
                                       DuckDBFederationExtractor   federationExtractor) throws Exception
    {
        String alias      = resourceProperties.get("Alias");
        String sourceType = resourceProperties.get("Source Type");
        String location    = resourceProperties.get("Location");

        String relatedAssetGUID;

        if (("duckdb".equalsIgnoreCase(sourceType)) || ("sqlite".equalsIgnoreCase(sourceType)))
        {
            relatedAssetGUID = this.resolveOrCreateFileBackedAsset(location);
        }
        else
        {
            relatedAssetGUID = this.resolveOrCreateNetworkBackedAsset(location, sourceType, alias);

            if (relatedAssetGUID != null)
            {
                this.catalogAttachedDatabaseTables(relatedAssetGUID, alias, duckdbConnection, federationExtractor);
            }
        }

        if (relatedAssetGUID != null)
        {
            this.linkResourceList(databaseGUID, relatedAssetGUID, "Attached Data Source", "Database attached to the DuckDB database via ATTACH (alias '" + alias + "').");
        }
    }


    /**
     * Catalog one external-file-scan federation finding, reusing the files-integration-connector's FileClassifier
     * and FilesTemplateType machinery in exactly the same way DataFilesMonitorForTarget.catalogFile() does.  Once
     * the file asset is resolved/created, its real schema (as read directly from the file via the DuckDB view's
     * own columns) and lineage back from the DuckDB-side view/columns are also catalogued - see
     * {@link #catalogFileSchemaAndLineage}.
     *
     * @param databaseGUID          unique identifier of the DuckDB database asset
     * @param databaseQualifiedName qualifiedName of the DuckDB database asset - used to locate the DuckDB-side
     *                               RelationalTable for the view, may be null if it could not be determined
     * @param resourceProperties    "View Name"/"Scan Function"/"Location"/"Format" properties describing the finding
     * @param duckdbConnection      JDBC connection to the DuckDB database - used to read the view's own columns
     * @param federationExtractor   shared extractor instance, reused for its duckdb_columns() query
     * @throws Exception problem accessing the open metadata repositories
     */
    private void catalogExternalFileSource(String                     databaseGUID,
                                           String                      databaseQualifiedName,
                                           Map<String, String>        resourceProperties,
                                           Connection                  duckdbConnection,
                                           DuckDBFederationExtractor   federationExtractor) throws Exception
    {
        String viewName  = resourceProperties.get("View Name");
        String location  = resourceProperties.get("Location");

        String relatedAssetGUID = this.resolveOrCreateFileBackedAsset(location);

        if (relatedAssetGUID != null)
        {
            this.linkResourceList(databaseGUID, relatedAssetGUID, "External File Source", "External file scanned by DuckDB view '" + viewName + "'.");

            this.catalogFileSchemaAndLineage(databaseQualifiedName, viewName, relatedAssetGUID, duckdbConnection, federationExtractor);
        }
    }


    /* ==================================================================================================
     * Richer schema/lineage modeling for external-file-scan (EXTERNAL_FILE_SOURCE) findings
     * ================================================================================================== */

    /**
     * Build (or find, if already built on a previous refresh) a schema for the external file asset representing
     * its "real" columns - as read directly from DuckDB's own knowledge of the view's columns - then, provided the
     * DuckDB-side RelationalTable/RelationalColumns for the view have already been catalogued by the friendship
     * connector (which now applies the CalculatedValue classification to a view and its columns itself, since their
     * content is not physically stored in DuckDB), link each DuckDB column to its corresponding real file column
     * via DerivedSchemaTypeQueryTarget.
     * <br><br>
     * This whole method is wrapped defensively: it is entirely expected, especially on an early refresh, that the
     * friendship connector (which runs on its own separate refresh cycle) has not yet catalogued the DuckDB-side
     * table/columns - this is logged as an information message, not an error, and never prevents the file asset's
     * own schema from being catalogued.
     *
     * @param databaseQualifiedName qualifiedName of the DuckDB database asset, or null if it could not be determined
     * @param viewName              name of the DuckDB view that scans the external file
     * @param fileAssetGUID         unique identifier of the (already resolved/created) file asset
     * @param duckdbConnection      JDBC connection to the DuckDB database
     * @param federationExtractor   shared extractor instance, reused for its duckdb_columns() query
     */
    private void catalogFileSchemaAndLineage(String                     databaseQualifiedName,
                                             String                      viewName,
                                             String                      fileAssetGUID,
                                             Connection                  duckdbConnection,
                                             DuckDBFederationExtractor   federationExtractor)
    {
        final String methodName = "catalogFileSchemaAndLineage";

        try
        {
            AssetClient fileClient = integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

            OpenMetadataRootElement fileAsset = fileClient.getAssetByGUID(fileAssetGUID, fileClient.getGetOptions());
            String                  fileAssetQualifiedName = fileClient.getQualifiedName(fileAsset);

            if (fileAssetQualifiedName == null)
            {
                return;
            }

            List<DuckDBFederationExtractor.DuckDBColumnInfo> viewColumns = federationExtractor.getColumnsForTable(duckdbConnection, viewName);

            if (viewColumns.isEmpty())
            {
                return;
            }

            Map<String, String> fileColumnGUIDs = this.getOrCreateFileSchema(fileAssetGUID, fileAssetQualifiedName, viewColumns);

            if ((databaseQualifiedName == null) || (fileColumnGUIDs.isEmpty()))
            {
                return;
            }

            SchemaAttributeClient duckdbTableClient = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_TABLE.typeName);

            String primaryTableQualifiedName  = databaseQualifiedName + "::main::" + viewName;
            String fallbackTableQualifiedName = databaseQualifiedName + "::" + viewName;

            OpenMetadataRootElement duckdbTable = this.findSchemaAttributeByQualifiedName(duckdbTableClient, primaryTableQualifiedName);

            if (duckdbTable == null)
            {
                duckdbTable = this.findSchemaAttributeByQualifiedName(duckdbTableClient, fallbackTableQualifiedName);
            }

            if (duckdbTable == null)
            {
                /*
                 * Not an error - the friendship connector runs on its own separate refresh cycle and may simply
                 * not have catalogued this view yet.  The file asset and its schema have still been catalogued.
                 */
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.DUCKDB_TABLE_NOT_YET_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                          viewName,
                                                                                                          primaryTableQualifiedName,
                                                                                                          fallbackTableQualifiedName));
                return;
            }

            String duckdbTableGUID = duckdbTable.getElementHeader().getGUID();

            SchemaAttributeClient duckdbColumnClient = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_COLUMN.typeName);

            List<OpenMetadataRootElement> duckdbColumns = duckdbColumnClient.getNestedSchemaAttributes(duckdbTableGUID, duckdbColumnClient.getQueryOptions());

            if (duckdbColumns == null)
            {
                return;
            }

            for (OpenMetadataRootElement duckdbColumn : duckdbColumns)
            {
                String duckdbColumnGUID = duckdbColumn.getElementHeader().getGUID();
                String duckdbColumnName = this.getElementName(duckdbColumnClient, duckdbColumn);

                if (duckdbColumnName == null)
                {
                    continue;
                }

                String fileColumnGUID = fileColumnGUIDs.get(duckdbColumnName);

                if (fileColumnGUID == null)
                {
                    for (Map.Entry<String, String> entry : fileColumnGUIDs.entrySet())
                    {
                        if (entry.getKey().equalsIgnoreCase(duckdbColumnName))
                        {
                            fileColumnGUID = entry.getValue();
                            break;
                        }
                    }
                }

                if (fileColumnGUID != null)
                {
                    this.linkDerivedSchemaTypeQueryTarget(duckdbColumnClient, duckdbColumnGUID, fileColumnGUID, duckdbColumnName, viewName);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                             "schema/lineage for view " + viewName,
                                                                                             (databaseQualifiedName == null ? "<unknown>" : databaseQualifiedName),
                                                                                             error.getMessage()));
        }
    }


    /**
     * Build (or find, if already built on a previous refresh) a TabularSchemaType and one TabularFileColumn per
     * column, linked to the given file asset, representing its "real" structure.
     *
     * @param fileAssetGUID          unique identifier of the file asset
     * @param fileAssetQualifiedName qualifiedName of the file asset
     * @param columns                columns to represent, in order
     * @return map of column name to the guid of its (matched or newly created) TabularFileColumn
     * @throws Exception problem accessing the open metadata repositories
     */
    private Map<String, String> getOrCreateFileSchema(String                                            fileAssetGUID,
                                                       String                                            fileAssetQualifiedName,
                                                       List<DuckDBFederationExtractor.DuckDBColumnInfo>  columns) throws Exception
    {
        final String methodName = "getOrCreateFileSchema";

        SchemaTypeClient      schemaTypeClient = integrationContext.getSchemaTypeClient(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName);
        SchemaAttributeClient columnClient     = integrationContext.getSchemaAttributeClient(OpenMetadataType.TABULAR_FILE_COLUMN.typeName);

        String schemaTypeQualifiedName = "TabularSchemaType::" + fileAssetQualifiedName;

        String schemaTypeGUID = this.findSchemaTypeGUIDByQualifiedName(schemaTypeClient, schemaTypeQualifiedName);

        boolean schemaTypeIsNew = (schemaTypeGUID == null);

        if (schemaTypeIsNew)
        {
            TabularSchemaTypeProperties schemaTypeProperties = new TabularSchemaTypeProperties();

            schemaTypeProperties.setQualifiedName(schemaTypeQualifiedName);
            schemaTypeProperties.setDisplayName("Schema for " + fileAssetQualifiedName);

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(fileAssetGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(fileAssetGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            schemaTypeGUID = schemaTypeClient.createSchemaType(newElementOptions, null, schemaTypeProperties, null);
        }

        Map<String, String> columnGUIDs = new HashMap<>();

        for (DuckDBFederationExtractor.DuckDBColumnInfo column : columns)
        {
            try
            {
                String columnQualifiedName = "TabularFileColumn::" + fileAssetQualifiedName + "::" + column.columnName();

                OpenMetadataRootElement existingColumn = this.findSchemaAttributeByQualifiedName(columnClient, columnQualifiedName);

                String columnGUID;

                if (existingColumn != null)
                {
                    columnGUID = existingColumn.getElementHeader().getGUID();
                }
                else
                {
                    TabularFileColumnProperties columnProperties = new TabularFileColumnProperties();

                    columnProperties.setQualifiedName(columnQualifiedName);
                    columnProperties.setDisplayName(column.columnName());

                    if (column.dataType() != null)
                    {
                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("duckdb.dataType", column.dataType());
                        columnProperties.setAdditionalProperties(additionalProperties);
                    }

                    NewElementOptions newElementOptions = new NewElementOptions(columnClient.getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(fileAssetGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(schemaTypeGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

                    columnGUID = columnClient.createSchemaAttribute(newElementOptions, null, columnProperties, null);
                }

                columnGUIDs.put(column.columnName(), columnGUID);
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                                 "file column " + column.columnName(),
                                                                                                 fileAssetQualifiedName,
                                                                                                 error.getMessage()));
            }
        }

        if (schemaTypeIsNew)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.FILE_SCHEMA_CATALOGED.getMessageDefinition(connectorName,
                                                                                           fileAssetQualifiedName,
                                                                                           Integer.toString(columnGUIDs.size())));
        }

        return columnGUIDs;
    }



    /**
     * Link a DuckDB-side (calculated) column to the real file column that ultimately supplies its data, via
     * DerivedSchemaTypeQueryTarget.  Wrapped defensively so that one column's link failing does not stop the rest.
     *
     * @param duckdbColumnClient client for the DuckDB-side RelationalColumn
     * @param duckdbColumnGUID   unique identifier of the DuckDB-side (derived/calculated) column - end 1
     * @param fileColumnGUID     unique identifier of the real file column - end 2
     * @param columnName         name of the column - used only to build a human-readable query string
     * @param viewName           name of the DuckDB view - used only to build a human-readable query string
     */
    private void linkDerivedSchemaTypeQueryTarget(SchemaAttributeClient duckdbColumnClient,
                                                   String                duckdbColumnGUID,
                                                   String                fileColumnGUID,
                                                   String                columnName,
                                                   String                viewName)
    {
        final String methodName = "linkDerivedSchemaTypeQueryTarget";

        try
        {
            DerivedSchemaTypeQueryTargetProperties properties = new DerivedSchemaTypeQueryTargetProperties();

            properties.setQuery("SELECT " + columnName + " FROM " + viewName);

            duckdbColumnClient.linkQueryTarget(duckdbColumnGUID,
                                               fileColumnGUID,
                                               new MakeAnchorOptions(duckdbColumnClient.getMetadataSourceOptions()),
                                               properties);
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                             "DerivedSchemaTypeQueryTarget for column " + columnName,
                                                                                             viewName,
                                                                                             error.getMessage()));
        }
    }


    /* ==================================================================================================
     * Richer TabularDataSet modeling for network-backed ATTACHED_SOURCE findings
     * ================================================================================================== */

    /**
     * Discover the tables exposed through a network-backed ATTACH-ed data source (using this processor's own
     * connection, which has already re-issued the ATTACH statement) and catalog each one as a TabularDataSet asset
     * linked to the target RelationalDatabase asset via DataSetContent.  Wrapped defensively so that one table's
     * problem does not stop the others, and one attached source's problem does not stop the rest of federation
     * discovery (the caller already wraps this call in its own try/catch).
     *
     * @param targetDatabaseGUID  unique identifier of the (already resolved/created) target RelationalDatabase asset
     * @param alias                alias the data source was ATTACH-ed under
     * @param duckdbConnection     JDBC connection to the DuckDB database
     * @param federationExtractor  shared extractor instance, reused for its duckdb_tables()/duckdb_columns() queries
     * @throws Exception problem accessing the open metadata repositories
     */
    private void catalogAttachedDatabaseTables(String                     targetDatabaseGUID,
                                               String                      alias,
                                               Connection                  duckdbConnection,
                                               DuckDBFederationExtractor   federationExtractor) throws Exception
    {
        final String methodName = "catalogAttachedDatabaseTables";

        AssetClient databaseClient = integrationContext.getAssetClient(OpenMetadataType.RELATIONAL_DATABASE.typeName);

        OpenMetadataRootElement targetDatabase            = databaseClient.getAssetByGUID(targetDatabaseGUID, databaseClient.getGetOptions());
        String                  targetDatabaseQualifiedName = databaseClient.getQualifiedName(targetDatabase);

        if (targetDatabaseQualifiedName == null)
        {
            return;
        }

        List<String> tableNames = federationExtractor.getTablesForAttachedDatabase(duckdbConnection, alias);

        for (String tableName : tableNames)
        {
            try
            {
                this.catalogAttachedTable(targetDatabaseGUID, targetDatabaseQualifiedName, alias, tableName, duckdbConnection, federationExtractor);
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                                 "attached table " + tableName,
                                                                                                 alias,
                                                                                                 error.getMessage()));
            }
        }
    }


    /**
     * Catalog one table exposed through a network-backed ATTACH-ed data source: create (or find) a TabularDataSet
     * asset for it, link it to the target RelationalDatabase asset via DataSetContent, and build its
     * RelationalTableType/RelationalColumn structure.  There is no DuckDB-side friendship-connector-catalogued
     * representation to link from for this case (the friendship connector's own connection never runs this
     * processor's ATTACH statements), so no CalculatedValue classification or DerivedSchemaTypeQueryTarget lineage
     * is attempted here.
     *
     * @param targetDatabaseGUID          unique identifier of the target RelationalDatabase asset
     * @param targetDatabaseQualifiedName qualifiedName of the target RelationalDatabase asset
     * @param alias                       alias the data source was ATTACH-ed under
     * @param tableName                   name of the table
     * @param duckdbConnection            JDBC connection to the DuckDB database
     * @param federationExtractor         shared extractor instance, reused for its duckdb_columns() query
     * @throws Exception problem accessing the open metadata repositories
     */
    private void catalogAttachedTable(String                     targetDatabaseGUID,
                                      String                      targetDatabaseQualifiedName,
                                      String                      alias,
                                      String                      tableName,
                                      Connection                  duckdbConnection,
                                      DuckDBFederationExtractor   federationExtractor) throws Exception
    {
        final String methodName = "catalogAttachedTable";

        List<DuckDBFederationExtractor.DuckDBColumnInfo> columns = federationExtractor.getColumnsForAttachedTable(duckdbConnection, alias, tableName);

        if (columns.isEmpty())
        {
            return;
        }

        AssetClient tabularDataSetClient = integrationContext.getAssetClient(OpenMetadataType.TABULAR_DATA_SET.typeName);

        String dataSetQualifiedName = OpenMetadataType.TABULAR_DATA_SET.typeName + "::" + targetDatabaseQualifiedName + "::" + tableName;

        OpenMetadataRootElement existingDataSet = tabularDataSetClient.getAssetByUniqueName(dataSetQualifiedName,
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                             tabularDataSetClient.getGetOptions());

        String dataSetGUID;

        if (existingDataSet != null)
        {
            dataSetGUID = existingDataSet.getElementHeader().getGUID();
        }
        else
        {
            TabularDataSetProperties dataSetProperties = new TabularDataSetProperties();

            dataSetProperties.setQualifiedName(dataSetQualifiedName);
            dataSetProperties.setDisplayName(tableName);
            dataSetProperties.setResourceName(tableName);
            dataSetProperties.setDescription("Table discovered via DuckDB ATTACH of data source '" + alias + "'.");

            NewElementOptions newElementOptions = new NewElementOptions(tabularDataSetClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(true);

            dataSetGUID = tabularDataSetClient.createAsset(newElementOptions, null, dataSetProperties, null);

            DataSetContentProperties dataSetContentProperties = new DataSetContentProperties();

            dataSetContentProperties.setQuery("SELECT * FROM " + tableName);

            tabularDataSetClient.linkDataSetContent(dataSetGUID,
                                                    targetDatabaseGUID,
                                                    new MakeAnchorOptions(tabularDataSetClient.getMetadataSourceOptions()),
                                                    dataSetContentProperties);

            auditLog.logMessage(methodName,
                                DuckDBAuditCode.ATTACHED_TABLE_CATALOGED.getMessageDefinition(connectorName,
                                                                                              tableName,
                                                                                              alias,
                                                                                              Integer.toString(columns.size())));
        }

        this.getOrCreateRelationalTableSchema(dataSetGUID, dataSetQualifiedName, columns);
    }


    /**
     * Build (or find, if already built on a previous refresh) a RelationalTableType and one RelationalColumn per
     * column, linked to the given TabularDataSet asset.
     *
     * @param dataSetGUID          unique identifier of the TabularDataSet asset
     * @param dataSetQualifiedName qualifiedName of the TabularDataSet asset
     * @param columns              columns to represent, in order
     * @throws Exception problem accessing the open metadata repositories
     */
    private void getOrCreateRelationalTableSchema(String                                            dataSetGUID,
                                                   String                                            dataSetQualifiedName,
                                                   List<DuckDBFederationExtractor.DuckDBColumnInfo>  columns) throws Exception
    {
        final String methodName = "getOrCreateRelationalTableSchema";

        SchemaTypeClient schemaTypeClient = integrationContext.getSchemaTypeClient(OpenMetadataType.RELATIONAL_TABLE_TYPE.typeName);

        String schemaTypeQualifiedName = "RelationalTableType::" + dataSetQualifiedName;

        String schemaTypeGUID = this.findSchemaTypeGUIDByQualifiedName(schemaTypeClient, schemaTypeQualifiedName);

        if (schemaTypeGUID == null)
        {
            SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();

            schemaTypeProperties.setTypeName(OpenMetadataType.RELATIONAL_TABLE_TYPE.typeName);
            schemaTypeProperties.setQualifiedName(schemaTypeQualifiedName);
            schemaTypeProperties.setDisplayName("Table Type for " + dataSetQualifiedName);

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(dataSetGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(dataSetGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            schemaTypeGUID = schemaTypeClient.createSchemaType(newElementOptions, null, schemaTypeProperties, null);
        }

        SchemaAttributeClient columnClient = integrationContext.getSchemaAttributeClient(OpenMetadataType.RELATIONAL_COLUMN.typeName);

        for (DuckDBFederationExtractor.DuckDBColumnInfo column : columns)
        {
            try
            {
                String columnQualifiedName = "RelationalColumn::" + dataSetQualifiedName + "::" + column.columnName();

                OpenMetadataRootElement existingColumn = this.findSchemaAttributeByQualifiedName(columnClient, columnQualifiedName);

                if (existingColumn == null)
                {
                    RelationalColumnProperties columnProperties = new RelationalColumnProperties();

                    columnProperties.setQualifiedName(columnQualifiedName);
                    columnProperties.setDisplayName(column.columnName());

                    if (column.dataType() != null)
                    {
                        Map<String, String> additionalProperties = new HashMap<>();
                        additionalProperties.put("duckdb.dataType", column.dataType());
                        columnProperties.setAdditionalProperties(additionalProperties);
                    }

                    NewElementOptions newElementOptions = new NewElementOptions(columnClient.getMetadataSourceOptions());

                    newElementOptions.setAnchorGUID(dataSetGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(schemaTypeGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

                    columnClient.createSchemaAttribute(newElementOptions, null, columnProperties, null);
                }
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    DuckDBAuditCode.FEDERATION_LINK_FAILED.getMessageDefinition(connectorName,
                                                                                                 "attached table column " + column.columnName(),
                                                                                                 dataSetQualifiedName,
                                                                                                 error.getMessage()));
            }
        }
    }


    /* ==================================================================================================
     * Shared "does this element already exist" lookups - mirrors the pattern used by
     * RelationalDatabaseCataloguer.findExistingSchemaAttribute() in the jdbc-integration-connector module: an
     * ambiguous match (more than one element with the same qualifiedName) is treated as "skip this refresh",
     * never as "does not exist yet" (which would risk creating a duplicate).
     * ================================================================================================== */

    /**
     * Look up a schema attribute (table or column, of any specific type) by its exact qualified name.
     *
     * @param client        client for the schema attribute's specific type
     * @param qualifiedName qualified name to look up
     * @return the matching element, or null if none exists yet, or the lookup failed/was ambiguous
     * @throws Exception problem accessing the open metadata repositories
     */
    private OpenMetadataRootElement findSchemaAttributeByQualifiedName(SchemaAttributeClient client, String qualifiedName) throws Exception
    {
        final String methodName = "findSchemaAttributeByQualifiedName";

        List<OpenMetadataRootElement> candidates = client.getSchemaAttributesByName(qualifiedName, client.getQueryOptions());

        OpenMetadataRootElement match      = null;
        int                     matchCount = 0;

        if (candidates != null)
        {
            for (OpenMetadataRootElement candidate : candidates)
            {
                if (qualifiedName.equals(client.getQualifiedName(candidate)))
                {
                    match = candidate;
                    matchCount++;
                }
            }
        }

        if (matchCount > 1)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.AMBIGUOUS_ELEMENT_FOUND.getMessageDefinition(connectorName,
                                                                                              Integer.toString(matchCount),
                                                                                              qualifiedName));
            return null;
        }

        return match;
    }


    /**
     * Look up a schema type (of any specific type) by its exact qualified name.
     *
     * @param client        client for the schema type's specific type
     * @param qualifiedName qualified name to look up
     * @return guid of the matching element, or null if none exists yet, or the lookup failed/was ambiguous
     * @throws Exception problem accessing the open metadata repositories
     */
    private String findSchemaTypeGUIDByQualifiedName(SchemaTypeClient client, String qualifiedName) throws Exception
    {
        final String methodName = "findSchemaTypeGUIDByQualifiedName";

        List<OpenMetadataRootElement> candidates = client.getSchemaTypesByName(qualifiedName, client.getQueryOptions());

        String matchGUID  = null;
        int    matchCount = 0;

        if (candidates != null)
        {
            for (OpenMetadataRootElement candidate : candidates)
            {
                if (qualifiedName.equals(client.getQualifiedName(candidate)))
                {
                    matchGUID = candidate.getElementHeader().getGUID();
                    matchCount++;
                }
            }
        }

        if (matchCount > 1)
        {
            auditLog.logMessage(methodName,
                                DuckDBAuditCode.AMBIGUOUS_ELEMENT_FOUND.getMessageDefinition(connectorName,
                                                                                              Integer.toString(matchCount),
                                                                                              qualifiedName));
            return null;
        }

        return matchGUID;
    }


    /**
     * Extract a schema attribute's own name: its displayName if set (RelationalDatabaseCataloguer always sets
     * displayName to the JDBC column name), falling back to the last "::"-delimited segment of its qualifiedName.
     *
     * @param client  client for the schema attribute's specific type
     * @param element element to extract the name from
     * @return name, or null if it could not be determined
     */
    private String getElementName(SchemaAttributeClient client, OpenMetadataRootElement element)
    {
        String displayName = client.getDisplayName(element);

        if (displayName != null)
        {
            return displayName;
        }

        String qualifiedName = client.getQualifiedName(element);

        if (qualifiedName != null)
        {
            int lastSeparator = qualifiedName.lastIndexOf("::");

            if (lastSeparator >= 0)
            {
                return qualifiedName.substring(lastSeparator + 2);
            }

            return qualifiedName;
        }

        return null;
    }


    /**
     * Resolve (or create) the asset for a file-backed federation finding, following exactly the pattern used by
     * DataFilesMonitorForTarget.catalogFile()/addDataFileViaTemplate() in files-integration-connectors: classify
     * the file with the FileClassifier, look it up by its classified qualifiedName, and if not found, use
     * FilesTemplateType to find the best-matching template (falling back to the generic DATA_FILE_TEMPLATE) and
     * create the new asset from that template.
     *
     * @param location path (local file path, or a URI such as an S3 path) reported for the federation finding
     * @return unique identifier of the matched or newly created asset, or null if the location could not be classified
     */
    private String resolveOrCreateFileBackedAsset(String location)
    {
        if ((location == null) || (location.isBlank()))
        {
            return null;
        }

        try
        {
            AssetClient fileClient = integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

            FileClassifier     fileClassifier     = integrationContext.getFileClassifier(fileSystemName, canonicalMountPoint, localMountPoint);
            FileClassification fileClassification = fileClassifier.classifyFile(new File(location));

            OpenMetadataRootElement existingElement = fileClient.getAssetByUniqueName(fileClassification.getQualifiedName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                       fileClient.getGetOptions());

            if (existingElement != null)
            {
                return existingElement.getElementHeader().getGUID();
            }

            String fileTemplateGUID = FilesTemplateType.getDefaultTemplateGUID(fileClassification.getAssetTypeName());

            if (fileTemplateGUID == null)
            {
                fileTemplateGUID = FilesTemplateType.DATA_FILE_TEMPLATE.getTemplateGUID();
            }

            Map<String, String> placeholderProperties = new HashMap<>();

            placeholderProperties.put(PlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getName(), fileClassification.getDeployedImplementationType());
            placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), fileClassification.getFileSystemName());
            placeholderProperties.put(PlaceholderProperty.FILE_PATH_NAME.getName(), fileClassification.getCanonicalPathName());
            placeholderProperties.put(PlaceholderProperty.FILE_ADDRESS.getName(), fileClassification.getFileAddress());
            placeholderProperties.put(PlaceholderProperty.FILE_TYPE.getName(), fileClassification.getFileType());
            placeholderProperties.put(PlaceholderProperty.FILE_EXTENSION.getName(), fileClassification.getFileExtension());
            placeholderProperties.put(PlaceholderProperty.FILE_NAME.getName(), fileClassification.getFileName());
            placeholderProperties.put(PlaceholderProperty.FILE_ENCODING.getName(), fileClassification.getEncoding());
            placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
            placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), "Discovered via DuckDB's federation (external file scan) capability.");

            OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

            return openMetadataStore.getMetadataElementFromTemplate(fileClassification.getAssetTypeName(),
                                                                     null,
                                                                     true,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     fileTemplateGUID,
                                                                     null,
                                                                     null,
                                                                     placeholderProperties,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     false);
        }
        catch (Exception error)
        {
            /*
             * Not every location reported by DuckDB (eg an s3:// URI) can necessarily be classified/reached by
             * the local FileClassifier - this must not fail the rest of the catalog pass.
             */
            return null;
        }
    }


    /**
     * Resolve (or create a placeholder for) the asset for a network-backed federation finding (eg an attached
     * PostgreSQL or MySQL database).  DESIGN NOTE (judgement call): there is no shared, published qualifiedName
     * convention available to this connector for the sibling vendor connectors' own database templates (that
     * convention lives in core-content-pack, which is out of scope for this change), so this method uses its own
     * best-effort deterministic qualifiedName based on the source type, host, port and database name; if no asset
     * with that qualifiedName already exists, a minimal, clearly-labelled placeholder RelationalDatabase asset is
     * created instead of leaving the relationship uncatalogued.
     *
     * @param location   raw ATTACH connection string/path (already password-redacted by DuckDBFederationExtractor)
     * @param sourceType DuckDB-reported source type, eg "postgres"/"mysql"
     * @param alias      alias the database was ATTACH-ed under
     * @return unique identifier of the matched or newly created placeholder asset
     * @throws Exception problem accessing the open metadata repositories
     */
    private String resolveOrCreateNetworkBackedAsset(String location,
                                                      String sourceType,
                                                      String alias) throws Exception
    {
        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.RELATIONAL_DATABASE.typeName);

        Map<String, String> connectionInfo = parseLibpqStyleConnectionString(location);

        String host   = connectionInfo.getOrDefault("host", "unknown-host");
        String port   = connectionInfo.getOrDefault("port", "unknown-port");
        String dbname = connectionInfo.getOrDefault("dbname", alias);

        String qualifiedName = OpenMetadataType.RELATIONAL_DATABASE.typeName + "::" + sourceType + "::" + host + ":" + port + "/" + dbname;

        OpenMetadataRootElement existingElement = assetClient.getAssetByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, assetClient.getGetOptions());

        if (existingElement != null)
        {
            return existingElement.getElementHeader().getGUID();
        }

        RelationalDatabaseProperties properties = new RelationalDatabaseProperties();

        properties.setTypeName(OpenMetadataType.RELATIONAL_DATABASE.typeName);
        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(dbname);
        properties.setResourceName(dbname);
        properties.setDescription("Externally discovered via DuckDB ATTACH, unverified.  Source type: " + sourceType + "; alias: " + alias + "; location: " + location);

        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        return assetClient.createAsset(newElementOptions, null, properties, null);
    }


    /**
     * Parse a libpq-style ("key=value key=value ...") connection string into a map.  DuckDB's ATTACH statement
     * uses this style of connection string for its Postgres and MySQL scanners.
     *
     * @param connectionString raw connection string (already password-redacted)
     * @return map of key to value - never null, may be empty
     */
    private Map<String, String> parseLibpqStyleConnectionString(String connectionString)
    {
        Map<String, String> values = new HashMap<>();

        if (connectionString != null)
        {
            for (String pair : connectionString.trim().split("\\s+"))
            {
                String[] keyValue = pair.split("=", 2);

                if (keyValue.length == 2)
                {
                    values.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
                }
            }
        }

        return values;
    }


    /**
     * Create a RESOURCE_LIST_RELATIONSHIP between the DuckDB database asset and the asset that represents a
     * federation finding (either an attached database or an externally-scanned file).  There was no existing
     * example of this relationship being created from an integration connector to copy, so this uses
     * OpenMetadataStore's generic two-element relationship-creation method
     * (createRelatedElementsInStore(typeName, guid1, guid2, effectiveFrom, effectiveTo, ElementProperties)),
     * populating the same properties (resourceUse, description) that ResourceListProperties exposes.
     *
     * @param databaseGUID    unique identifier of the DuckDB database asset (end 1)
     * @param relatedAssetGUID unique identifier of the related asset (end 2)
     * @param resourceUse      short label describing why the resource is related
     * @param description      longer description of the relationship
     * @throws Exception problem accessing the open metadata repositories
     */
    private void linkResourceList(String databaseGUID,
                                  String relatedAssetGUID,
                                  String resourceUse,
                                  String description) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.RESOURCE_USE.name, resourceUse);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, description);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        databaseGUID,
                                                        relatedAssetGUID,
                                                        null,
                                                        null,
                                                        properties);
    }


    /**
     * Extract the friendship GUID from the configuration properties - or use the default.
     *
     * @param configurationProperties configuration properties for this catalog target
     * @return friendship GUID or null
     */
    private String getFriendshipGUID(Map<String, Object> configurationProperties)
    {
        String friendshipGUID = defaultFriendshipGUID;

        if ((configurationProperties != null) &&
                (configurationProperties.get(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName()) != null))
        {
            friendshipGUID = configurationProperties.get(DuckDBConfigurationProperty.FRIENDSHIP_GUID.getName()).toString();
        }

        return friendshipGUID;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        /*
         * This disconnects any embedded connections such as secrets connectors.
         */
        super.disconnect();
    }
}
