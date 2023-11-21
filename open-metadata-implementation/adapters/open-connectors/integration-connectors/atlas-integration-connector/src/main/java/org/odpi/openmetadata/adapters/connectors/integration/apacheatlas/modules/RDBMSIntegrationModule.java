/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules;


import org.odpi.openmetadata.accessservices.assetmanager.properties.DataAssetProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RDBMSIntegrationModule manages the cataloguing of RDBMS entities stored in Apache Atlas into the open metadata ecosystem.
 */
public class RDBMSIntegrationModule extends DatabaseIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String rdbmsModuleName = "RDBMS Integration Module";

    private static final String rdbmsDatabaseTypeName            = "rdbms_db";
    private static final String rdbmsDatabaseTablesPropertyName  = "tables";
    private static final String rdbmsDatabaseTableTypeName       = "rdbms_table";
    private static final String rdbmsDatabaseColumnsPropertyName = "columns";
    private static final String rdbmsDatabaseColumnTypeName      = "rdbms_column";

    private static final String rdbmsInstancePropertyName = "instance";
    private static final String egeriaPathTypeName        = "DataFile";



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
    public RDBMSIntegrationModule(String                   connectorName,
                                  ConnectionProperties     connectionProperties,
                                  AuditLog                 auditLog,
                                  CatalogIntegratorContext myContext,
                                  String                   targetRootURL,
                                  ApacheAtlasRESTConnector atlasClient,
                                  List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              rdbmsModuleName,
              rdbmsDatabaseTypeName,
              rdbmsDatabaseTablesPropertyName,
              rdbmsDatabaseTableTypeName,
              rdbmsDatabaseColumnsPropertyName,
              rdbmsDatabaseColumnTypeName,
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
        final String typeNamePropertyName = "data_type";

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