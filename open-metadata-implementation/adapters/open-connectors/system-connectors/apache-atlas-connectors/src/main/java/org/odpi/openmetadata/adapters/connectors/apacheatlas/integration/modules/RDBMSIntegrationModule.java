/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

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
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public RDBMSIntegrationModule(String                   connectorName,
                                  Connection               connectionDetails,
                                  AuditLog                 auditLog,
                                  IntegrationContext        myContext,
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
     * @param egeriaSchemaAttributeTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected RelationalColumnProperties getEgeriaDatabaseColumnProperties(AtlasEntity atlasEntity,
                                                                           String      egeriaSchemaAttributeTypeName)
    {

        /*
         * Begin by mapping the common DataSet properties.
         */
        SchemaAttributeProperties schemaAttributeProperties = super.getSchemaAttributeProperties(atlasEntity,
                                                                                                 egeriaSchemaAttributeTypeName);

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
    protected TypeEmbeddedAttributeProperties getEgeriaDatabaseColumnTypeProperties(AtlasEntity atlasEntity,
                                                                                    String egeriaSchemaTypeTypeName)
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