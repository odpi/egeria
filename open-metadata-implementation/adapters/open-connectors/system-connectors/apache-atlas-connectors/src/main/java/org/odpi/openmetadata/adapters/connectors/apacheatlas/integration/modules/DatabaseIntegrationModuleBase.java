/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataAssetProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SynchronizationDirection;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.List;

/**
 * DatabaseIntegrationModuleBase abstracts the process of synchronizing relational metadata from Apache Atlas, so it is
 * independent of the actual Apache Atlas types.   The subclasses supply the types.  This is because Apache Atlas has multiple type
 * definitions for this type of metadata.
 */
public abstract class DatabaseIntegrationModuleBase extends AtlasRegisteredIntegrationModuleBase
{
    protected final static String egeriaDatabaseTypeName               = "DeployedDatabaseSchema";
    protected final static String egeriaRootDatabaseSchemaTypeTypeName = "RelationalDBSchemaType";
    protected final static String egeriaDatabaseTableTypeName          = "RelationalTable";
    protected final static String egeriaDatabaseTableTypeTypeName      = "RelationalTableType";
    protected final static String egeriaDatabaseColumnTypeName         = "RelationalColumn";
    protected final static String egeriaDatabaseColumnTypeTypeName     = "PrimitiveSchemaType";


    private final String atlasDatabaseTypeName;
    private final String atlasDatabaseTablesPropertyName;
    private final String atlasDatabaseTableTypeName;
    private final String atlasDatabaseColumnsPropertyName;


    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param moduleName name of this module
     * @param atlasDatabaseTypeName name of type in atlas used for the database
     * @param atlasDatabaseTablesPropertyName name of the property used to navigate from the database to its tables.
     * @param atlasDatabaseTableTypeName name of the type used to represent a database table in atlas
     * @param atlasDatabaseColumnsPropertyName name of the property used to navigate from a database table to its columns
     * @param atlasDatabaseColumnTypeName name of the type used to represent a database column in atlas
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public DatabaseIntegrationModuleBase(String                   connectorName,
                                         String                   moduleName,
                                         String                   atlasDatabaseTypeName,
                                         String                   atlasDatabaseTablesPropertyName,
                                         String                   atlasDatabaseTableTypeName,
                                         String                   atlasDatabaseColumnsPropertyName,
                                         String                   atlasDatabaseColumnTypeName,
                                         ConnectionProperties     connectionProperties,
                                         AuditLog                 auditLog,
                                         CatalogIntegratorContext myContext,
                                         String                   targetRootURL,
                                         ApacheAtlasRESTConnector atlasClient,
                                         List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              moduleName,
              connectionProperties,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors,
              new String[]{atlasDatabaseTypeName, atlasDatabaseTableTypeName, atlasDatabaseColumnTypeName},
              null);

        this.atlasDatabaseTypeName = atlasDatabaseTypeName;
        this.atlasDatabaseTablesPropertyName = atlasDatabaseTablesPropertyName;
        this.atlasDatabaseTableTypeName = atlasDatabaseTableTypeName;
        this.atlasDatabaseColumnsPropertyName = atlasDatabaseColumnsPropertyName;
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
        final String methodName = "refresh(" + moduleName + ")";

        /*
         * The configuration can turn off the cataloguing of assets into the open metadata ecosystem.
         */
        if ((myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
            (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * Retrieve the databases catalogued in Apache Atlas.  They are turned into an Open Metadata DeployedDatabaseSchema entities.
                 */
                int startFrom = 0;
                int pageSize  = myContext.getMaxPageSize();

                List<AtlasEntityHeader> atlasSearchResult = atlasClient.getEntitiesForType(atlasDatabaseTypeName, startFrom, pageSize);

                while ((atlasSearchResult != null) && (! atlasSearchResult.isEmpty()))
                {
                    /*
                     * Found new databases - process each one in turn.
                     */
                    for (AtlasEntityHeader atlasEntityHeader : atlasSearchResult)
                    {
                        String atlasDatabaseGUID = atlasEntityHeader.getGuid();

                        AtlasEntityWithExtInfo atlasDatabaseEntity = atlasClient.getEntityByGUID(atlasDatabaseGUID);

                        String egeriaDatabaseGUID = this.syncAtlasDatabase(atlasDatabaseEntity);

                        if (egeriaDatabaseGUID != null)
                        {
                            /*
                             * Synchronize each table in turn
                             */
                            if ((atlasDatabaseEntity != null) &&
                                        (atlasDatabaseEntity.getEntity() != null) &&
                                        (atlasDatabaseEntity.getEntity().getRelationshipAttributes() != null) &&
                                        (atlasDatabaseEntity.getEntity().getRelationshipAttributes().get(atlasDatabaseTablesPropertyName) != null))
                            {
                                List<AtlasEntityWithExtInfo> atlasDatabaseTables = atlasClient.getRelatedEntities(atlasDatabaseEntity,
                                                                                                                  atlasDatabaseTablesPropertyName);

                                if (atlasDatabaseTables != null)
                                {
                                    for (AtlasEntityWithExtInfo atlasDatabaseTable : atlasDatabaseTables)
                                    {
                                        if ((atlasDatabaseTable != null) && (atlasDatabaseTable.getEntity() != null))
                                        {
                                            String egeriaDatabaseTableGUID = syncAtlasDatabaseTable(atlasDatabaseTable, egeriaDatabaseGUID);

                                            if (egeriaDatabaseTableGUID != null)
                                            {
                                                /*
                                                 * Synchronize each column in turn.
                                                 */
                                                if ((atlasDatabaseTable.getEntity().getRelationshipAttributes() != null) &&
                                                    (atlasDatabaseTable.getEntity().getRelationshipAttributes().get(atlasDatabaseColumnsPropertyName) != null))
                                                {
                                                    List<AtlasEntityWithExtInfo> atlasDatabaseColumns = atlasClient.getRelatedEntities(
                                                            atlasDatabaseTable,
                                                            atlasDatabaseColumnsPropertyName);

                                                    if (atlasDatabaseColumns != null)
                                                    {
                                                        for (AtlasEntityWithExtInfo atlasDatabaseColumn : atlasDatabaseColumns)
                                                        {
                                                            if ((atlasDatabaseColumn != null) && (atlasDatabaseTable.getEntity() != null))
                                                            {
                                                                syncAtlasDatabaseColumn(atlasDatabaseColumn, egeriaDatabaseTableGUID);
                                                            }
                                                        }

                                                        this.checkForAdditionalEgeriaColumns(egeriaDatabaseTableGUID, atlasDatabaseColumns);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    this.checkForAdditionalEgeriaTables(egeriaDatabaseGUID, atlasDatabaseTables);
                                }
                            }
                        }
                    }

                    /*
                     * Retrieve the next page
                     */
                    startFrom = startFrom + pageSize;
                    atlasSearchResult = atlasClient.getEntitiesForType(atlasDatabaseTableTypeName, startFrom, pageSize);
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                        error.getClass().getName(),
                                                                                                                        methodName,
                                                                                                                        error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        // Ignore events
    }



    /**
     * Return the unique identifier of the database schema type for the requested database.  This may or may not have been created already.  The
     * method issues a query to the open metadata ecosystem.  If the database schema type is found, its unique identifier is returned.  If it is
     * not found, a new database schema type is created and attached to the database entity and the unique identifier of the new database schema type
     * is returned.
     *
     * @param egeriaDatabaseGUID unique identifier of the database in the open metadata ecosystem.
     *
     * @return unique identifier of the database schema type attached to the database.  Tables are attached to this object.
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating with Egeria.
     */
    protected String getEgeriaDatabaseSchemaType(String egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        SchemaTypeElement databaseSchemaType = dataAssetExchangeService.getSchemaTypeForElement(egeriaDatabaseGUID,
                                                                                                egeriaDatabaseTypeName,
                                                                                                null);

        if (databaseSchemaType != null)
        {
            return databaseSchemaType.getElementHeader().getGUID();
        }

        SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();

        schemaTypeProperties.setTypeName(egeriaRootDatabaseSchemaTypeTypeName);
        schemaTypeProperties.setQualifiedName(egeriaRootDatabaseSchemaTypeTypeName + " for " + egeriaDatabaseGUID);

        String databaseSchemaTypeGUID = dataAssetExchangeService.createAnchoredSchemaType(true,
                                                                                          egeriaDatabaseGUID,
                                                                                          null,
                                                                                          schemaTypeProperties);

        if (databaseSchemaTypeGUID != null)
        {
            dataAssetExchangeService.setupSchemaTypeParent(true,
                                                           databaseSchemaTypeGUID,
                                                           egeriaDatabaseGUID,
                                                           egeriaDatabaseTypeName,
                                                           null,
                                                           null);
        }

        return databaseSchemaTypeGUID;
    }


    /**
     * Copy the contents of the Atlas database entity into open metadata.
     *
     * @param atlasDatabaseEntity entity retrieved from Apache Atlas
     *
     * @return unique identifier of the equivalent element in open metadata (egeriaDatabaseGUID)
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private String syncAtlasDatabase(AtlasEntityWithExtInfo  atlasDatabaseEntity) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "syncAtlasDatabase";

        if ((atlasDatabaseEntity != null) && (atlasDatabaseEntity.getEntity() != null))
        {
            String egeriaDatabaseGUID = super.getEgeriaGUID(atlasDatabaseEntity);

            if (egeriaDatabaseGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaDatabaseGUID = createAtlasDatabaseInEgeria(atlasDatabaseEntity);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    dataAssetExchangeService.getDataAssetByGUID(egeriaDatabaseGUID, null);

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasDatabaseInEgeria(atlasDatabaseEntity, egeriaDatabaseGUID);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaDatabaseTypeName,
                                                                                                               egeriaDatabaseGUID,
                                                                                                               atlasDatabaseEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasDatabaseEntity);
                    egeriaDatabaseGUID = createAtlasDatabaseInEgeria(atlasDatabaseEntity);
                }
            }

            this.augmentAtlasDatabaseInEgeria(atlasDatabaseEntity, egeriaDatabaseGUID);

            return egeriaDatabaseGUID;
        }

        return null;
    }


    /**
     * Create the database in the open metadata ecosystem.
     *
     * @param atlasDatabaseEntity entity retrieved from Apache Atlas
     *
     * @return unique identifier of the database entity in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasDatabaseInEgeria(AtlasEntityWithExtInfo  atlasDatabaseEntity) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "createAtlasDatabaseInEgeria";

        if (atlasDatabaseEntity != null)
        {
            AtlasEntity atlasEntity = atlasDatabaseEntity.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdentifierProperties externalIdentifierProperties = super.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                        atlasEntity.getTypeName(),
                                                                                                        atlasEntity.getCreatedBy(),
                                                                                                        atlasEntity.getCreateTime(),
                                                                                                        atlasEntity.getUpdatedBy(),
                                                                                                        atlasEntity.getUpdateTime(),
                                                                                                        atlasEntity.getVersion(),
                                                                                                        SynchronizationDirection.FROM_THIRD_PARTY);

                DataAssetProperties dataAssetProperties = this.getEgeriaDatabaseProperties(atlasEntity, egeriaDatabaseTypeName);

                String egeriaDatabaseGUID = dataAssetExchangeService.createDataAsset(true,
                                                                                     externalIdentifierProperties,
                                                                                     dataAssetProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaDatabaseTypeName,
                                                                                                          egeriaDatabaseGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaDatabaseGUID,
                                      dataAssetProperties.getQualifiedName(),
                                      egeriaDatabaseTypeName,
                                      false,
                                      false);

                setOwner(atlasDatabaseEntity, egeriaDatabaseGUID);


                return egeriaDatabaseGUID;
            }
        }

        return null;
    }


    /**
     * Update the properties of an open metadata database with the current properties from Apache Atlas.
     *
     * @param atlasDatabaseEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void   updateAtlasDatabaseInEgeria(AtlasEntityWithExtInfo  atlasDatabaseEntity,
                                                 String                  egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "updateAtlasDatabaseInEgeria";

        if (atlasDatabaseEntity != null)
        {
            AtlasEntity      atlasEntity      = atlasDatabaseEntity.getEntity();
            DataAssetElement dataAssetElement = dataAssetExchangeService.getDataAssetByGUID(egeriaDatabaseGUID, null);

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(egeriaDatabaseGUID,
                                              egeriaDatabaseTypeName,
                                              dataAssetElement,
                                              atlasEntity))
                {
                    DataAssetProperties dataAssetProperties = this.getEgeriaDatabaseProperties(atlasEntity, egeriaDatabaseTypeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              egeriaDatabaseTypeName,
                                                                                                              egeriaDatabaseGUID));

                    dataAssetExchangeService.updateDataAsset(egeriaDatabaseGUID, atlasEntity.getGuid(), false, dataAssetProperties, null);
                }
            }
        }
    }


    /**
     * Allow a subclass to attach additional information to the database.
     *
     * @param atlasDatabaseEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    @SuppressWarnings(value = "unused")
    protected void   augmentAtlasDatabaseInEgeria(AtlasEntityWithExtInfo  atlasDatabaseEntity,
                                                  String                  egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        // Optionally implemented by subclass
    }


    /**
     * Copy the contents of the Atlas database table entity into open metadata.
     *
     * @param atlasDatabaseTableEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the database in open metadata
     *
     * @return unique identifier of the equivalent element in open metadata (egeriaDatabaseTableGUID)
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private String syncAtlasDatabaseTable(AtlasEntityWithExtInfo  atlasDatabaseTableEntity,
                                          String                  egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "syncAtlasDatabaseTable";

        if ((atlasDatabaseTableEntity != null) && (atlasDatabaseTableEntity.getEntity() != null))
        {
            String egeriaDatabaseTableGUID = super.getEgeriaGUID(atlasDatabaseTableEntity);

            if (egeriaDatabaseTableGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaDatabaseTableGUID = createAtlasDatabaseTableInEgeria(atlasDatabaseTableEntity, egeriaDatabaseGUID);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    dataAssetExchangeService.getDataAssetByGUID(egeriaDatabaseTableGUID, null);

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasDatabaseTableInEgeria(atlasDatabaseTableEntity, egeriaDatabaseTableGUID);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaDatabaseTableTypeName,
                                                                                                               egeriaDatabaseTableGUID,
                                                                                                               atlasDatabaseTableEntity.getEntity().getGuid()));

                    removeEgeriaGUID(atlasDatabaseTableEntity);

                    egeriaDatabaseTableGUID = createAtlasDatabaseTableInEgeria(atlasDatabaseTableEntity, egeriaDatabaseGUID);
                }
            }

            augmentAtlasDatabaseTableInEgeria(atlasDatabaseTableEntity, egeriaDatabaseTableGUID);

            return egeriaDatabaseTableGUID;
        }

        return null;
    }


    /**
     * Create the database table in the open metadata ecosystem.
     *
     * @param atlasDatabaseTableEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the database in the open metadata ecosystem
     *
     * @return unique identifier of the database entity in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasDatabaseTableInEgeria(AtlasEntityWithExtInfo  atlasDatabaseTableEntity,
                                                      String                  egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "createAtlasDatabaseTableInEgeria";

        if (atlasDatabaseTableEntity != null)
        {
            AtlasEntity atlasEntity = atlasDatabaseTableEntity.getEntity();

            if (atlasEntity != null)
            {
                String egeriaDatabaseSchemaTypeGUID = this.getEgeriaDatabaseSchemaType(egeriaDatabaseGUID);

                if (egeriaDatabaseSchemaTypeGUID != null)
                {
                    ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                           atlasEntity.getTypeName(),
                                                                                                           atlasEntity.getCreatedBy(),
                                                                                                           atlasEntity.getCreateTime(),
                                                                                                           atlasEntity.getUpdatedBy(),
                                                                                                           atlasEntity.getUpdateTime(),
                                                                                                           atlasEntity.getVersion(),
                                                                                                           SynchronizationDirection.FROM_THIRD_PARTY);
                    SchemaAttributeProperties schemaAttributeProperties = getEgeriaDatabaseTableProperties(atlasEntity,
                                                                                                           egeriaDatabaseTableTypeName,
                                                                                                           egeriaDatabaseTableTypeTypeName);

                    String egeriaDatabaseTableGUID = dataAssetExchangeService.createSchemaAttribute(true,
                                                                                                    egeriaDatabaseSchemaTypeGUID,
                                                                                                    externalIdentifierProperties,
                                                                                                    schemaAttributeProperties,
                                                                                                    null);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              egeriaDatabaseTableTypeName,
                                                                                                              egeriaDatabaseTableGUID,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid()));

                    saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                          egeriaDatabaseGUID,
                                          schemaAttributeProperties.getQualifiedName(),
                                          egeriaDatabaseTableTypeName,
                                          false,
                                          false);

                    setOwner(atlasDatabaseTableEntity, egeriaDatabaseTableGUID);

                    return egeriaDatabaseTableGUID;
                }
            }
        }

        return null;
    }


    /**
     * Update the properties of an open metadata database table with the current properties from Apache Atlas.
     *
     * @param atlasDatabaseTableEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseTableGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void updateAtlasDatabaseTableInEgeria(AtlasEntityWithExtInfo  atlasDatabaseTableEntity,
                                                    String                  egeriaDatabaseTableGUID) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "updateAtlasDatabaseTableInEgeria";

        if (atlasDatabaseTableEntity != null)
        {
            AtlasEntity atlasEntity = atlasDatabaseTableEntity.getEntity();

            DataAssetElement dataAssetElement = dataAssetExchangeService.getDataAssetByGUID(egeriaDatabaseTableGUID,
                                                                                            null);

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(egeriaDatabaseTableGUID,
                                              egeriaDatabaseTableTypeName,
                                              dataAssetElement,
                                              atlasEntity))
                {
                    SchemaAttributeProperties databaseTableProperties = this.getEgeriaDatabaseTableProperties(atlasEntity,
                                                                                                              egeriaDatabaseTableTypeName,
                                                                                                              egeriaDatabaseTableTypeTypeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              egeriaDatabaseTableTypeName,
                                                                                                              egeriaDatabaseTableGUID));

                    dataAssetExchangeService.updateSchemaAttribute(egeriaDatabaseTableGUID,
                                                                   atlasEntity.getGuid(),
                                                                   false,
                                                                   databaseTableProperties,
                                                                   null);
                }
            }
        }
    }


    /**
     * Allow a subclass to attach additional information to the database table.
     *
     * @param atlasDatabaseTableEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseTableGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    @SuppressWarnings(value = "unused")
    protected void augmentAtlasDatabaseTableInEgeria(AtlasEntityWithExtInfo  atlasDatabaseTableEntity,
                                                     String                  egeriaDatabaseTableGUID) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        // Optionally implemented by subclass
    }


    /**
     * Retrieve the tables attached to the database in Egeria and check that each one is still defined in Apache Atlas.
     *
     * @param egeriaDatabaseGUID unique identifier of the database in the open metadata ecosystem
     * @param atlasDatabaseTables  details of the tables that are linked ot the database in Apache Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private void checkForAdditionalEgeriaTables(String                       egeriaDatabaseGUID,
                                                List<AtlasEntityWithExtInfo> atlasDatabaseTables) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "checkForAdditionalEgeriaTables";

        if ((atlasDatabaseTables != null) && (! atlasDatabaseTables.isEmpty()))
        {
            String egeriaDatabaseSchemaTypeGUID = this.getEgeriaDatabaseSchemaType(egeriaDatabaseGUID);

            if (egeriaDatabaseSchemaTypeGUID != null)
            {
                List<String> validAtlasGUIDs = getValidAtlasGUIDs(atlasDatabaseTables);

                /*
                 * Retrieve the tables catalogued in Egeria.  This is turned into an Open Metadata DeployedDatabaseSchema.
                 */
                int startFrom = 0;
                int pageSize  = myContext.getMaxPageSize();

                List<SchemaAttributeElement> egeriaDatabaseTables = dataAssetExchangeService.getNestedAttributes(egeriaDatabaseSchemaTypeGUID,
                                                                                                                 startFrom,
                                                                                                                 pageSize,
                                                                                                                 null);

                while (egeriaDatabaseTables != null)
                {
                    for (SchemaAttributeElement egeriaDatabaseTable : egeriaDatabaseTables)
                    {
                        String atlasDatabaseTableGUID = super.getAtlasGUID(egeriaDatabaseTable);

                        if (! validAtlasGUIDs.contains(atlasDatabaseTableGUID))
                        {
                            /*
                             * The table in Egeria is not matched with an Atlas equivalent.
                             */
                            auditLog.logMessage(methodName,
                                                AtlasIntegrationAuditCode.DELETING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                                      egeriaDatabaseTableTypeName,
                                                                                                                      egeriaDatabaseTable.getElementHeader().getGUID(),
                                                                                                                      atlasDatabaseTableGUID));

                            dataAssetExchangeService.removeDataAsset(egeriaDatabaseTable.getElementHeader().getGUID(),
                                                                     atlasDatabaseTableGUID,
                                                                     null);
                        }
                    }

                    startFrom = startFrom + pageSize;
                    egeriaDatabaseTables = dataAssetExchangeService.getNestedAttributes(egeriaDatabaseSchemaTypeGUID,
                                                                                        startFrom,
                                                                                        pageSize,
                                                                                        null);
                }
            }
        }
    }


    /**
     * Copy the contents of the Atlas database column entity into open metadata.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseTableGUID unique identifier of the database table to connect column to in open metadata.
     *
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private void syncAtlasDatabaseColumn(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                         String                  egeriaDatabaseTableGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "syncAtlasDatabaseColumn";

        if (atlasDatabaseColumnEntity != null)
        {
            String  egeriaDatabaseColumnGUID = super.getEgeriaGUID(atlasDatabaseColumnEntity);

            if (egeriaDatabaseColumnGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaDatabaseColumnGUID = createAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseTableGUID);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    SchemaAttributeElement egeriaDatabaseColumn = dataAssetExchangeService.getSchemaAttributeByGUID(egeriaDatabaseColumnGUID, null);

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseColumnGUID, egeriaDatabaseColumn);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaDatabaseColumnTypeName,
                                                                                                               egeriaDatabaseColumnGUID,
                                                                                                               atlasDatabaseColumnEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasDatabaseColumnEntity);
                    egeriaDatabaseColumnGUID = createAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseTableGUID);
                }
            }

            augmentAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseColumnGUID);
        }
    }


    /**
     * Create the database in the open metadata ecosystem.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     *
     * @return unique identifier of the database entity in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasDatabaseColumnInEgeria(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                                       String                  egeriaDatabaseTableGUID) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "createAtlasDatabaseColumnInEgeria";

        if ((atlasDatabaseColumnEntity != null) && (egeriaDatabaseTableGUID != null))
        {
            AtlasEntity atlasEntity = atlasDatabaseColumnEntity.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                       atlasEntity.getTypeName(),
                                                                                                       atlasEntity.getCreatedBy(),
                                                                                                       atlasEntity.getCreateTime(),
                                                                                                       atlasEntity.getUpdatedBy(),
                                                                                                       atlasEntity.getUpdateTime(),
                                                                                                       atlasEntity.getVersion(),
                                                                                                       SynchronizationDirection.FROM_THIRD_PARTY);

                SchemaAttributeProperties schemaAttributeProperties = getEgeriaDatabaseColumnProperties(atlasEntity,
                                                                                                        egeriaDatabaseColumnTypeName,
                                                                                                        egeriaDatabaseColumnTypeTypeName);

                String egeriaDatabaseColumnGUID = dataAssetExchangeService.createSchemaAttribute(true,
                                                                                                 egeriaDatabaseTableGUID,
                                                                                                 externalIdentifierProperties,
                                                                                                 schemaAttributeProperties,
                                                                                                 null);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaDatabaseColumnTypeName,
                                                                                                          egeriaDatabaseColumnGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaDatabaseColumnGUID,
                                      schemaAttributeProperties.getQualifiedName(),
                                      egeriaDatabaseColumnTypeName,
                                      false,
                                      false);
                setOwner(atlasDatabaseColumnEntity, egeriaDatabaseColumnGUID);

                return egeriaDatabaseColumnGUID;
            }
        }

        return null;
    }


    /**
     * Update the properties of an open metadata database with the current properties from Apache Atlas.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseColumnGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @param egeriaDatabaseColumn retrieved entity from the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void   updateAtlasDatabaseColumnInEgeria(AtlasEntityWithExtInfo atlasDatabaseColumnEntity,
                                                       String                 egeriaDatabaseColumnGUID,
                                                       SchemaAttributeElement egeriaDatabaseColumn) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "updateAtlasDatabaseColumnInEgeria";

        if (atlasDatabaseColumnEntity != null)
        {
            AtlasEntity atlasEntity = atlasDatabaseColumnEntity.getEntity();

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(egeriaDatabaseColumnGUID,
                                              egeriaDatabaseColumnTypeName,
                                              egeriaDatabaseColumn,
                                              atlasEntity))
                {
                    SchemaAttributeProperties databaseColumnProperties = this.getEgeriaDatabaseColumnProperties(atlasEntity,
                                                                                                                egeriaDatabaseColumnTypeName,
                                                                                                                egeriaDatabaseColumnTypeTypeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              egeriaDatabaseColumnTypeName,
                                                                                                              egeriaDatabaseColumnGUID));

                    dataAssetExchangeService.updateSchemaAttribute(egeriaDatabaseColumnGUID,
                                                                   atlasEntity.getGuid(),
                                                                   false,
                                                                   databaseColumnProperties,
                                                                   null);
                }
            }
        }
    }


    /**
     * Allow a subclass to attach additional information to the database column.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseColumnGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    @SuppressWarnings(value = "unused")
    protected void   augmentAtlasDatabaseColumnInEgeria(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                                        String                  egeriaDatabaseColumnGUID) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        // Optionally implemented by subclass
    }


    /**
     * Retrieve the columns attached to the database table in Egeria and check that each one is still defined in Apache Atlas.
     *
     * @param egeriaDatabaseTableGUID unique identifier of the database table in the open metadata ecosystem
     * @param atlasDatabaseColumns  details of the tables that are linked ot the database in Apache Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private void checkForAdditionalEgeriaColumns(String                       egeriaDatabaseTableGUID,
                                                 List<AtlasEntityWithExtInfo> atlasDatabaseColumns) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "checkForAdditionalEgeriaColumns";

        if ((atlasDatabaseColumns != null) && (! atlasDatabaseColumns.isEmpty()))
        {
            if (egeriaDatabaseTableGUID != null)
            {
                List<String> validAtlasGUIDs = getValidAtlasGUIDs(atlasDatabaseColumns);

                /*
                 * Retrieve the columns catalogued in Egeria.
                 */
                int startFrom = 0;
                int pageSize  = myContext.getMaxPageSize();

                List<SchemaAttributeElement> egeriaDatabaseColumns = dataAssetExchangeService.getNestedAttributes(egeriaDatabaseTableGUID,
                                                                                                                  startFrom,
                                                                                                                  pageSize,
                                                                                                                  null);

                while (egeriaDatabaseColumns != null)
                {
                    for (SchemaAttributeElement egeriaDatabaseColumn : egeriaDatabaseColumns)
                    {
                        String atlasDatabaseColumnGUID = super.getAtlasGUID(egeriaDatabaseColumn);

                        if (! validAtlasGUIDs.contains(atlasDatabaseColumnGUID))
                        {
                            /*
                             * The table in Egeria is not matched with an Atlas equivalent.
                             */
                            auditLog.logMessage(methodName,
                                                AtlasIntegrationAuditCode.DELETING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                                      egeriaDatabaseColumnTypeName,
                                                                                                                      egeriaDatabaseColumn.getElementHeader().getGUID(),
                                                                                                                      atlasDatabaseColumnGUID));

                            dataAssetExchangeService.removeDataAsset(egeriaDatabaseColumn.getElementHeader().getGUID(),
                                                                     atlasDatabaseColumnGUID,
                                                                     null);
                        }
                    }

                    startFrom = startFrom + pageSize;
                    egeriaDatabaseColumns = dataAssetExchangeService.getNestedAttributes(egeriaDatabaseTableGUID,
                                                                                         startFrom,
                                                                                         pageSize,
                                                                                         null);
                }
            }
        }
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected abstract DataAssetProperties getEgeriaDatabaseProperties(AtlasEntity atlasEntity,
                                                                       String      egeriaTypeName);


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected abstract SchemaAttributeProperties getEgeriaDatabaseTableProperties(AtlasEntity atlasEntity,
                                                                                  String      egeriaSchemaAttributeTypeName,
                                                                                  String      egeriaSchemaTypeTypeName);



    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected abstract SchemaAttributeProperties getEgeriaDatabaseColumnProperties(AtlasEntity atlasEntity,
                                                                                   String      egeriaSchemaAttributeTypeName,
                                                                                   String      egeriaSchemaTypeTypeName);
}