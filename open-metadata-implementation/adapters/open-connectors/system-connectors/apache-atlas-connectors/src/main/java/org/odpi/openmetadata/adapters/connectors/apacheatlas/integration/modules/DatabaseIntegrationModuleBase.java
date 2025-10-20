/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DatabaseIntegrationModuleBase abstracts the process of synchronizing relational metadata from Apache Atlas, so it is
 * independent of the actual Apache Atlas types.   The subclasses supply the types.  This is because Apache Atlas has multiple type
 * definitions for this type of metadata.
 */
public abstract class DatabaseIntegrationModuleBase extends AtlasRegisteredIntegrationModuleBase
{
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
     * @param connectionDetails connection properties used to start the connector
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
                                         Connection               connectionDetails,
                                         AuditLog                 auditLog,
                                         IntegrationContext       myContext,
                                         String                   targetRootURL,
                                         ApacheAtlasRESTConnector atlasClient,
                                         List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              moduleName,
              connectionDetails,
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
                            if ((atlasDatabaseEntity.getEntity() != null) &&
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
                                                                syncAtlasDatabaseColumn(atlasDatabaseColumn, egeriaDatabaseGUID, egeriaDatabaseTableGUID);
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
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        // Ignore events
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
                    dataAssetClient.getAssetByGUID(egeriaDatabaseGUID, null);

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
                                                                                                               OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
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
                ExternalIdProperties externalIdentifierProperties = super.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                atlasEntity.getTypeName(),
                                                                                                atlasEntity.getCreatedBy(),
                                                                                                atlasEntity.getCreateTime(),
                                                                                                atlasEntity.getUpdatedBy(),
                                                                                                atlasEntity.getUpdateTime(),
                                                                                                atlasEntity.getVersion());

                ExternalIdLinkProperties externalIdLinkProperties = super.getExternalIdLink(getAtlasStringProperty(atlasDatabaseEntity.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                            null,
                                                                                            PermittedSynchronization.FROM_THIRD_PARTY);

                DataAssetProperties dataAssetProperties = this.getEgeriaDatabaseProperties(atlasEntity, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);

                NewElementOptions newElementOptions = new NewElementOptions(dataAssetClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(true);

                String egeriaDatabaseGUID = dataAssetClient.createAsset(newElementOptions,
                                                                        null,
                                                                        dataAssetProperties,
                                                                        null);

                externalIdClient.createExternalId(egeriaDatabaseGUID, externalIdLinkProperties, externalIdentifierProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                                                          egeriaDatabaseGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaDatabaseGUID,
                                      dataAssetProperties.getQualifiedName(),
                                      OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
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
            AtlasEntity             atlasEntity      = atlasDatabaseEntity.getEntity();
            OpenMetadataRootElement dataAssetElement = dataAssetClient.getAssetByGUID(egeriaDatabaseGUID, dataAssetClient.getGetOptions());

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(dataAssetElement, atlasEntity))
                {
                    DataAssetProperties dataAssetProperties = this.getEgeriaDatabaseProperties(atlasEntity, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                                                              egeriaDatabaseGUID));

                    dataAssetClient.updateAsset(egeriaDatabaseGUID, dataAssetClient.getUpdateOptions(true), dataAssetProperties);
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
                    dataAssetClient.getAssetByGUID(egeriaDatabaseTableGUID, null);

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
                                                                                                               OpenMetadataType.RELATIONAL_TABLE.typeName,
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
                OpenMetadataRootElement egeriaDatabaseSchemaType = schemaTypeClient.getSchemaTypeForElement(egeriaDatabaseGUID,
                                                                                               OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName);

                if (egeriaDatabaseSchemaType != null)
                {
                    ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                   atlasEntity.getTypeName(),
                                                                                                   atlasEntity.getCreatedBy(),
                                                                                                   atlasEntity.getCreateTime(),
                                                                                                   atlasEntity.getUpdatedBy(),
                                                                                                   atlasEntity.getUpdateTime(),
                                                                                                   atlasEntity.getVersion());

                    ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasEntity.getAttributes(), atlasNamePropertyName),
                                                                                               null,
                                                                                               PermittedSynchronization.FROM_THIRD_PARTY);


                    RelationalTableProperties schemaAttributeProperties = getEgeriaDatabaseTableProperties(atlasEntity,
                                                                                                           OpenMetadataType.RELATIONAL_TABLE.typeName);

                    NewElementOptions newElementOptions = new NewElementOptions(schemaAttributeClient.getMetadataSourceOptions());

                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setAnchorGUID(egeriaDatabaseGUID);
                    newElementOptions.setParentGUID(egeriaDatabaseSchemaType.getElementHeader().getGUID());
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

                    Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

                    initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, getEgeriaDatabaseTableTypeProperties());

                    String egeriaDatabaseTableGUID = schemaAttributeClient.createSchemaAttribute(newElementOptions,
                                                                                                 initialClassifications,
                                                                                                 schemaAttributeProperties,
                                                                                                 null);

                    externalIdClient.createExternalId(egeriaDatabaseTableGUID, externalIdLinkProperties, externalIdentifierProperties);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              OpenMetadataType.RELATIONAL_TABLE.typeName,
                                                                                                              egeriaDatabaseTableGUID,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid()));

                    saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                          egeriaDatabaseGUID,
                                          schemaAttributeProperties.getQualifiedName(),
                                          OpenMetadataType.RELATIONAL_TABLE.typeName,
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

            OpenMetadataRootElement dataAssetElement = dataAssetClient.getAssetByGUID(egeriaDatabaseTableGUID,
                                                                                      dataAssetClient.getGetOptions());

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(dataAssetElement, atlasEntity))
                {
                    SchemaAttributeProperties databaseTableProperties = this.getEgeriaDatabaseTableProperties(atlasEntity,
                                                                                                              OpenMetadataType.RELATIONAL_TABLE.typeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              OpenMetadataType.RELATIONAL_TABLE.typeName,
                                                                                                              egeriaDatabaseTableGUID));

                    schemaAttributeClient.updateSchemaAttribute(egeriaDatabaseTableGUID,
                                                                schemaAttributeClient.getUpdateOptions(true),
                                                                databaseTableProperties);
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
            OpenMetadataRootElement egeriaDatabaseSchemaType = schemaTypeClient.getSchemaTypeForElement(egeriaDatabaseGUID,
                                                                                                        OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName);

            if (egeriaDatabaseSchemaType != null)
            {
                List<String> validAtlasGUIDs = getValidAtlasGUIDs(atlasDatabaseTables);

                /*
                 * Retrieve the tables catalogued in Egeria.  This is turned into an Open Metadata DeployedDatabaseSchema.
                 */
                List<RelatedMetadataElementSummary> egeriaDatabaseTables = egeriaDatabaseSchemaType.getSchemaAttributes();

                if (egeriaDatabaseTables != null)
                {
                    for (RelatedMetadataElementSummary egeriaDatabaseTable : egeriaDatabaseTables)
                    {
                        String atlasDatabaseTableGUID = super.getAtlasGUID(egeriaDatabaseTable);

                        if (! validAtlasGUIDs.contains(atlasDatabaseTableGUID))
                        {
                            /*
                             * The table in Egeria is not matched with an Atlas equivalent.
                             */
                            auditLog.logMessage(methodName,
                                                AtlasIntegrationAuditCode.DELETING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.RELATIONAL_TABLE.typeName,
                                                                                                                      egeriaDatabaseTable.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                      atlasDatabaseTableGUID));

                            schemaAttributeClient.deleteSchemaAttribute(egeriaDatabaseTable.getRelatedElement().getElementHeader().getGUID(), schemaAttributeClient.getDeleteOptions(true));
                        }
                    }
                }
            }
        }
    }


    /**
     * Copy the contents of the Atlas database column entity into open metadata.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the data base
     * @param egeriaDatabaseTableGUID unique identifier of the database table to connect column to in open metadata.
     *
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private void syncAtlasDatabaseColumn(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                         String                  egeriaDatabaseGUID,
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
                egeriaDatabaseColumnGUID = createAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseGUID, egeriaDatabaseTableGUID);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    OpenMetadataRootElement egeriaDatabaseColumn = schemaAttributeClient.getSchemaAttributeByGUID(egeriaDatabaseColumnGUID, schemaAttributeClient.getGetOptions());

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
                                                                                                               OpenMetadataType.RELATIONAL_COLUMN.typeName,
                                                                                                               egeriaDatabaseColumnGUID,
                                                                                                               atlasDatabaseColumnEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasDatabaseColumnEntity);
                    egeriaDatabaseColumnGUID = createAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseGUID, egeriaDatabaseTableGUID);
                }
            }

            augmentAtlasDatabaseColumnInEgeria(atlasDatabaseColumnEntity, egeriaDatabaseColumnGUID);
        }
    }


    /**
     * Create the database in the open metadata ecosystem.
     *
     * @param atlasDatabaseColumnEntity entity retrieved from Apache Atlas
     * @param egeriaDatabaseGUID unique identifier of the database (anchor)
     * @param egeriaDatabaseTableGUID unique identifier of the database table to connect column to
     *
     * @return unique identifier of the database entity in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasDatabaseColumnInEgeria(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                                       String                  egeriaDatabaseGUID,
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
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                               atlasEntity.getTypeName(),
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());

                ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasEntity.getAttributes(), atlasNamePropertyName),
                                                                                           null,
                                                                                           PermittedSynchronization.FROM_THIRD_PARTY);

                SchemaAttributeProperties schemaAttributeProperties = getEgeriaDatabaseColumnProperties(atlasEntity,
                                                                                                        OpenMetadataType.RELATIONAL_COLUMN.typeName);

                NewElementOptions newElementOptions = new NewElementOptions(schemaAttributeClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(egeriaDatabaseGUID);
                newElementOptions.setParentGUID(egeriaDatabaseTableGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

                Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

                initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                           this.getEgeriaDatabaseColumnTypeProperties(atlasEntity, OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName));

                String egeriaDatabaseColumnGUID = schemaAttributeClient.createSchemaAttribute(newElementOptions,
                                                                                              initialClassifications,
                                                                                              schemaAttributeProperties,
                                                                                              null);

                externalIdClient.createExternalId(egeriaDatabaseColumnGUID, externalIdLinkProperties, externalIdentifierProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          OpenMetadataType.RELATIONAL_COLUMN.typeName,
                                                                                                          egeriaDatabaseColumnGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaDatabaseColumnGUID,
                                      schemaAttributeProperties.getQualifiedName(),
                                      OpenMetadataType.RELATIONAL_COLUMN.typeName,
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
    protected void   updateAtlasDatabaseColumnInEgeria(AtlasEntityWithExtInfo  atlasDatabaseColumnEntity,
                                                       String                  egeriaDatabaseColumnGUID,
                                                       OpenMetadataRootElement egeriaDatabaseColumn) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "updateAtlasDatabaseColumnInEgeria";

        if (atlasDatabaseColumnEntity != null)
        {
            AtlasEntity atlasEntity = atlasDatabaseColumnEntity.getEntity();

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(egeriaDatabaseColumn, atlasEntity))
                {
                    SchemaAttributeProperties databaseColumnProperties = this.getEgeriaDatabaseColumnProperties(atlasEntity,
                                                                                                                OpenMetadataType.RELATIONAL_COLUMN.typeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              OpenMetadataType.RELATIONAL_COLUMN.typeName,
                                                                                                              egeriaDatabaseColumnGUID));

                    schemaAttributeClient.updateSchemaAttribute(egeriaDatabaseColumnGUID,
                                                                schemaAttributeClient.getUpdateOptions(true),
                                                                databaseColumnProperties);
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
                OpenMetadataRootElement databaseTable = schemaAttributeClient.getSchemaAttributeByGUID(egeriaDatabaseTableGUID, schemaAttributeClient.getGetOptions());

                List<RelatedMetadataElementSummary> egeriaDatabaseColumns = databaseTable.getSchemaAttributes();

                while (egeriaDatabaseColumns != null)
                {
                    for (RelatedMetadataElementSummary egeriaDatabaseColumn : egeriaDatabaseColumns)
                    {
                        String atlasDatabaseColumnGUID = super.getAtlasGUID(egeriaDatabaseColumn);

                        if (! validAtlasGUIDs.contains(atlasDatabaseColumnGUID))
                        {
                            /*
                             * The table in Egeria is not matched with an Atlas equivalent.
                             */
                            auditLog.logMessage(methodName,
                                                AtlasIntegrationAuditCode.DELETING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.RELATIONAL_COLUMN.typeName,
                                                                                                                      egeriaDatabaseColumn.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                      atlasDatabaseColumnGUID));

                            schemaAttributeClient.deleteSchemaAttribute(egeriaDatabaseColumn.getRelatedElement().getElementHeader().getGUID(),
                                                                        schemaAttributeClient.getDeleteOptions(true));
                        }
                    }
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
     * @return properties to pass to Egeria
     */
    protected abstract RelationalTableProperties getEgeriaDatabaseTableProperties(AtlasEntity atlasEntity,
                                                                                  String      egeriaSchemaAttributeTypeName);

    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @return properties to pass to Egeria
     */
    protected TypeEmbeddedAttributeProperties getEgeriaDatabaseTableTypeProperties()
    {
        TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties = new TypeEmbeddedAttributeProperties();

        typeEmbeddedAttributeProperties.setSchemaTypeName(OpenMetadataType.RELATIONAL_TABLE_TYPE.typeName);

        return typeEmbeddedAttributeProperties;
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected abstract RelationalColumnProperties getEgeriaDatabaseColumnProperties(AtlasEntity atlasEntity,
                                                                                    String      egeriaSchemaAttributeTypeName);

    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected abstract TypeEmbeddedAttributeProperties getEgeriaDatabaseColumnTypeProperties(AtlasEntity atlasEntity,
                                                                                             String      egeriaSchemaTypeTypeName);
}