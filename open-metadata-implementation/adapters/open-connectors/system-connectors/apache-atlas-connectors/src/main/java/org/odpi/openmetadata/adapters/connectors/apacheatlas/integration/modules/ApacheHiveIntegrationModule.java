/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
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
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * ApacheHiveIntegrationModule manages the cataloguing of Apache Hive entities stored in Apache Atlas into the open metadata ecosystem.
 */
public class ApacheHiveIntegrationModule extends DatabaseIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String hiveModuleName = "Apache Hive Integration Module";

    private static final String hiveDatabaseTypeName            = "hive_db";
    private static final String hiveDatabaseTablesPropertyName  = "tables";
    private static final String hiveDatabaseTableTypeName       = "hive_table";
    private static final String hiveDatabaseColumnsPropertyName = "columns";
    private static final String hiveDatabaseColumnTypeName      = "hive_column";

    private static final String hiveLocationPathPropertyName    = "locationPath";
    private static final String egeriaPathTypeName              = "DataFile";



    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public ApacheHiveIntegrationModule(String                   connectorName,
                                       Connection               connectionDetails,
                                       AuditLog                 auditLog,
                                       IntegrationContext       myContext,
                                       String                   targetRootURL,
                                       ApacheAtlasRESTConnector atlasClient,
                                       List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              hiveModuleName,
              hiveDatabaseTypeName,
              hiveDatabaseTablesPropertyName,
              hiveDatabaseTableTypeName,
              hiveDatabaseColumnsPropertyName,
              hiveDatabaseColumnTypeName,
              connectionDetails,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected DataAssetProperties getEgeriaDatabaseProperties(AtlasEntity atlasEntity,
                                                              String      egeriaTypeName)
    {
        DataAssetProperties dataAssetProperties = super.getDataAssetProperties(atlasEntity, egeriaTypeName);

        dataAssetProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                 atlasAssetProperties));

        return dataAssetProperties;
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
    @Override
    protected void   augmentAtlasDatabaseInEgeria(AtlasEntityWithExtInfo atlasDatabaseEntity,
                                                  String                 egeriaDatabaseGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        AtlasEntityWithExtInfo hivePathEntity = atlasClient.getRelatedEntity(atlasDatabaseEntity, hiveLocationPathPropertyName);

        if ((hivePathEntity != null) && (hivePathEntity.getEntity() != null))
        {
            ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(hivePathEntity.getEntity().getGuid(),
                                                                                           hivePathEntity.getEntity().getTypeName(),
                                                                                           hivePathEntity.getEntity().getCreatedBy(),
                                                                                           hivePathEntity.getEntity().getCreateTime(),
                                                                                           hivePathEntity.getEntity().getUpdatedBy(),
                                                                                           hivePathEntity.getEntity().getUpdateTime(),
                                                                                           hivePathEntity.getEntity().getVersion());

            ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasDatabaseEntity.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                       null,
                                                                                       PermittedSynchronization.FROM_THIRD_PARTY);

            DataAssetProperties dataAssetProperties = getEgeriaDataFileProperties(hivePathEntity.getEntity(),
                                                                                  egeriaPathTypeName);

            /*
            Todo support for external identifiers
            String dataFileGUID = dataAssetExchangeService.createDataAsset(true,
                                                                           externalIdentifierProperties,
                                                                           dataAssetProperties);
                                                                           *

            setOwner(hivePathEntity, dataFileGUID);

             */
        }
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected RelationalTableProperties getEgeriaDatabaseTableProperties(AtlasEntity atlasEntity,
                                                                         String      egeriaSchemaAttributeTypeName)
    {
        SchemaAttributeProperties schemaAttributeProperties = super.getSchemaAttributeProperties(atlasEntity,
                                                                                                 egeriaSchemaAttributeTypeName);

        schemaAttributeProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                       atlasAssetProperties));

        return new RelationalTableProperties(schemaAttributeProperties);
    }



    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected RelationalColumnProperties getEgeriaDatabaseColumnProperties(AtlasEntity atlasEntity,
                                                                           String      egeriaSchemaTypeTypeName)
    {
        final String typeNamePropertyName = "type";

        /*
         * Begin by mapping the common DataSet properties.
         */
        SchemaAttributeProperties schemaAttributeProperties = super.getSchemaAttributeProperties(atlasEntity,
                                                                                                 egeriaSchemaTypeTypeName);

        List<String> atlasColumnPropertyNames = new ArrayList<>(atlasAssetProperties);

        schemaAttributeProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                       atlasColumnPropertyNames));

        return new RelationalColumnProperties(schemaAttributeProperties);
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity              retrieve entity from Apache Atlas
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    @Override
    protected TypeEmbeddedAttributeProperties getEgeriaDatabaseColumnTypeProperties(AtlasEntity atlasEntity, String egeriaSchemaTypeTypeName)
    {
        final String typeNamePropertyName = "data_type";

        TypeEmbeddedAttributeProperties properties = new TypeEmbeddedAttributeProperties();

        String dataTypeName = getAtlasStringProperty(atlasEntity.getAttributes(), typeNamePropertyName);

        if (dataTypeName == null)
        {
            dataTypeName = DataType.STRING.getName();
        }

        properties.setSchemaTypeName(egeriaSchemaTypeTypeName);
        properties.setDataType(dataTypeName);
        return properties;
    }
}