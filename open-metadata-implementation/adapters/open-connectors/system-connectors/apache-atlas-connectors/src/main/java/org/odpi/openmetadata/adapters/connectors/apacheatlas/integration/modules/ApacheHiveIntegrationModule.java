/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.accessservices.assetmanager.properties.DataAssetProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SynchronizationDirection;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public ApacheHiveIntegrationModule(String                   connectorName,
                                       ConnectionProperties     connectionProperties,
                                       AuditLog                 auditLog,
                                       CatalogIntegratorContext myContext,
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
              connectionProperties,
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
            ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(hivePathEntity.getEntity().getGuid(),
                                                                                                   hivePathEntity.getEntity().getTypeName(),
                                                                                                   hivePathEntity.getEntity().getCreatedBy(),
                                                                                                   hivePathEntity.getEntity().getCreateTime(),
                                                                                                   hivePathEntity.getEntity().getUpdatedBy(),
                                                                                                   hivePathEntity.getEntity().getUpdateTime(),
                                                                                                   hivePathEntity.getEntity().getVersion(),
                                                                                                   SynchronizationDirection.FROM_THIRD_PARTY);

            DataAssetProperties dataAssetProperties = getEgeriaDataFileProperties(hivePathEntity.getEntity(),
                                                                                  egeriaPathTypeName);

            String dataFileGUID = dataAssetExchangeService.createDataAsset(true,
                                                                           externalIdentifierProperties,
                                                                           dataAssetProperties);
            setOwner(hivePathEntity, dataFileGUID);
        }
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected SchemaAttributeProperties getEgeriaDatabaseTableProperties(AtlasEntity atlasEntity,
                                                                         String      egeriaSchemaAttributeTypeName,
                                                                         String      egeriaSchemaTypeTypeName)
    {
        SchemaAttributeProperties schemaAttributeProperties = super.getSchemaAttributeProperties(atlasEntity,
                                                                                                 egeriaSchemaAttributeTypeName,
                                                                                                 egeriaSchemaTypeTypeName);

        schemaAttributeProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                       atlasAssetProperties));

        return schemaAttributeProperties;
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @param egeriaSchemaTypeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected SchemaAttributeProperties getEgeriaDatabaseColumnProperties(AtlasEntity atlasEntity,
                                                                          String      egeriaSchemaAttributeTypeName,
                                                                          String      egeriaSchemaTypeTypeName)
    {
        final String typeNamePropertyName = "type";

        /*
         * Begin by mapping the common DataSet properties.
         */
        SchemaAttributeProperties schemaAttributeProperties = super.getSchemaAttributeProperties(atlasEntity,
                                                                                                 egeriaSchemaAttributeTypeName,
                                                                                                 egeriaSchemaTypeTypeName);
        Map<String, Object> attributes = atlasEntity.getAttributes();

        String dataTypeName = getAtlasStringProperty(attributes, typeNamePropertyName);

        if (dataTypeName == null)
        {
            dataTypeName = "string";
        }

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put("dataType", dataTypeName);
        schemaAttributeProperties.getSchemaType().setExtendedProperties(extendedProperties);

        List<String> atlasColumnPropertyNames = new ArrayList<>(atlasAssetProperties);

        atlasColumnPropertyNames.add(typeNamePropertyName);
        schemaAttributeProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                       atlasColumnPropertyNames));

        return schemaAttributeProperties;
    }
}