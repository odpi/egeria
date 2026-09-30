/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DatabaseTableTemplateType describes the catalog templates supplied with Egeria for tables in relational
 * databases, data warehouses, lakehouses and SQL query engines.  Each template creates just the asset, identified
 * by its resource name within a namespace (for example the name and namespace that OpenLineage gives a dataset or
 * job).
 */
public enum DatabaseTableTemplateType implements TemplateDefinition
{
    /**
     * Create an asset with the PostgreSQL Table technology type.
     */
    POSTGRESQL_TABLE_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_TABLE.getDeployedImplementationType(),
                              "Create a " + PostgresDeployedImplementationType.POSTGRESQL_TABLE.getAssociatedTypeName() + " asset with technology type " + PostgresDeployedImplementationType.POSTGRESQL_TABLE.getDeployedImplementationType() + ".",
                              PostgresDeployedImplementationType.POSTGRESQL_TABLE.getAssociatedTypeName(),
                              false,
                              "4d52a7b6-6b3d-4776-9707-04b228fda2c0",
                              PostgresDeployedImplementationType.POSTGRESQL_TABLE,
                              PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                              null),

    /**
     * Create an asset with the Microsoft SQL Server Table technology type.
     */
    MSSQL_TABLE_TEMPLATE(MSSQLDeployedImplementationType.MSSQL_TABLE.getDeployedImplementationType(),
                         "Create a " + MSSQLDeployedImplementationType.MSSQL_TABLE.getAssociatedTypeName() + " asset with technology type " + MSSQLDeployedImplementationType.MSSQL_TABLE.getDeployedImplementationType() + ".",
                         MSSQLDeployedImplementationType.MSSQL_TABLE.getAssociatedTypeName(),
                         false,
                         "bf9299aa-6b0e-44e3-9b74-763dc274825d",
                         MSSQLDeployedImplementationType.MSSQL_TABLE,
                         PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                         null),

    /**
     * Create an asset with the Oracle Table technology type.
     */
    ORACLE_TABLE_TEMPLATE(OracleDeployedImplementationType.ORACLE_TABLE.getDeployedImplementationType(),
                          "Create a " + OracleDeployedImplementationType.ORACLE_TABLE.getAssociatedTypeName() + " asset with technology type " + OracleDeployedImplementationType.ORACLE_TABLE.getDeployedImplementationType() + ".",
                          OracleDeployedImplementationType.ORACLE_TABLE.getAssociatedTypeName(),
                          false,
                          "a62d00c8-06b0-49bd-8821-6971af8b4403",
                          OracleDeployedImplementationType.ORACLE_TABLE,
                          PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                          null),

    /**
     * Create an asset with the Db2 Table technology type.
     */
    DB2LUW_TABLE_TEMPLATE(DB2LUWDeployedImplementationType.DB2LUW_TABLE.getDeployedImplementationType(),
                          "Create a " + DB2LUWDeployedImplementationType.DB2LUW_TABLE.getAssociatedTypeName() + " asset with technology type " + DB2LUWDeployedImplementationType.DB2LUW_TABLE.getDeployedImplementationType() + ".",
                          DB2LUWDeployedImplementationType.DB2LUW_TABLE.getAssociatedTypeName(),
                          false,
                          "57fafc82-adc6-4fd0-abc1-1d7a0c78109f",
                          DB2LUWDeployedImplementationType.DB2LUW_TABLE,
                          PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                          null),

    /**
     * Create an asset with the Amazon Athena Table technology type.
     */
    AMAZON_ATHENA_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.AMAZON_ATHENA_TABLE.getDeployedImplementationType(),
                                 "Create a " + DataWarehouseDeployedImplementationType.AMAZON_ATHENA_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.AMAZON_ATHENA_TABLE.getDeployedImplementationType() + ".",
                                 DataWarehouseDeployedImplementationType.AMAZON_ATHENA_TABLE.getAssociatedTypeName(),
                                 false,
                                 "deed8680-7d2f-490e-a41e-37838e544e93",
                                 DataWarehouseDeployedImplementationType.AMAZON_ATHENA_TABLE,
                                 PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                 null),

    /**
     * Create an asset with the AWS Glue Table technology type.
     */
    AWS_GLUE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.AWS_GLUE_TABLE.getDeployedImplementationType(),
                            "Create a " + DataWarehouseDeployedImplementationType.AWS_GLUE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.AWS_GLUE_TABLE.getDeployedImplementationType() + ".",
                            DataWarehouseDeployedImplementationType.AWS_GLUE_TABLE.getAssociatedTypeName(),
                            false,
                            "565af0d3-8fac-4a0c-b568-5509dc746388",
                            DataWarehouseDeployedImplementationType.AWS_GLUE_TABLE,
                            PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                            null),

    /**
     * Create an asset with the Azure Synapse Analytics Table technology type.
     */
    AZURE_SYNAPSE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.AZURE_SYNAPSE_TABLE.getDeployedImplementationType(),
                                 "Create a " + DataWarehouseDeployedImplementationType.AZURE_SYNAPSE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.AZURE_SYNAPSE_TABLE.getDeployedImplementationType() + ".",
                                 DataWarehouseDeployedImplementationType.AZURE_SYNAPSE_TABLE.getAssociatedTypeName(),
                                 false,
                                 "90a7b075-0fb5-4930-a06e-c9ef1648b257",
                                 DataWarehouseDeployedImplementationType.AZURE_SYNAPSE_TABLE,
                                 PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                 null),

    /**
     * Create an asset with the Azure Data Explorer Table technology type.
     */
    AZURE_DATA_EXPLORER_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.AZURE_DATA_EXPLORER_TABLE.getDeployedImplementationType(),
                                       "Create a " + DataWarehouseDeployedImplementationType.AZURE_DATA_EXPLORER_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.AZURE_DATA_EXPLORER_TABLE.getDeployedImplementationType() + ".",
                                       DataWarehouseDeployedImplementationType.AZURE_DATA_EXPLORER_TABLE.getAssociatedTypeName(),
                                       false,
                                       "cea56a87-4851-4bfc-a765-566b8a221181",
                                       DataWarehouseDeployedImplementationType.AZURE_DATA_EXPLORER_TABLE,
                                       PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                       null),

    /**
     * Create an asset with the Google BigQuery Table technology type.
     */
    GOOGLE_BIGQUERY_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.GOOGLE_BIGQUERY_TABLE.getDeployedImplementationType(),
                                   "Create a " + DataWarehouseDeployedImplementationType.GOOGLE_BIGQUERY_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.GOOGLE_BIGQUERY_TABLE.getDeployedImplementationType() + ".",
                                   DataWarehouseDeployedImplementationType.GOOGLE_BIGQUERY_TABLE.getAssociatedTypeName(),
                                   false,
                                   "bde5fa70-6dbf-4de1-a248-14e8fd4be148",
                                   DataWarehouseDeployedImplementationType.GOOGLE_BIGQUERY_TABLE,
                                   PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                   null),

    /**
     * Create an asset with the Microsoft Fabric Warehouse Table technology type.
     */
    MICROSOFT_FABRIC_WAREHOUSE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.MICROSOFT_FABRIC_WAREHOUSE_TABLE.getDeployedImplementationType(),
                                              "Create a " + DataWarehouseDeployedImplementationType.MICROSOFT_FABRIC_WAREHOUSE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.MICROSOFT_FABRIC_WAREHOUSE_TABLE.getDeployedImplementationType() + ".",
                                              DataWarehouseDeployedImplementationType.MICROSOFT_FABRIC_WAREHOUSE_TABLE.getAssociatedTypeName(),
                                              false,
                                              "aebb8ff6-6a10-46cf-abca-8af0ac2a6a18",
                                              DataWarehouseDeployedImplementationType.MICROSOFT_FABRIC_WAREHOUSE_TABLE,
                                              PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                              null),

    /**
     * Create an asset with the MySQL Table technology type.
     */
    MYSQL_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.MYSQL_TABLE.getDeployedImplementationType(),
                         "Create a " + DataWarehouseDeployedImplementationType.MYSQL_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.MYSQL_TABLE.getDeployedImplementationType() + ".",
                         DataWarehouseDeployedImplementationType.MYSQL_TABLE.getAssociatedTypeName(),
                         false,
                         "328e4059-ed6f-4869-9f55-1184b102997a",
                         DataWarehouseDeployedImplementationType.MYSQL_TABLE,
                         PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                         null),

    /**
     * Create an asset with the CrateDB Table technology type.
     */
    CRATEDB_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.CRATEDB_TABLE.getDeployedImplementationType(),
                           "Create a " + DataWarehouseDeployedImplementationType.CRATEDB_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.CRATEDB_TABLE.getDeployedImplementationType() + ".",
                           DataWarehouseDeployedImplementationType.CRATEDB_TABLE.getAssociatedTypeName(),
                           false,
                           "90281e28-ce5f-4931-b03f-0471f30a5a68",
                           DataWarehouseDeployedImplementationType.CRATEDB_TABLE,
                           PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                           null),

    /**
     * Create an asset with the Apache Hive Table technology type.
     */
    APACHE_HIVE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.APACHE_HIVE_TABLE.getDeployedImplementationType(),
                               "Create a " + DataWarehouseDeployedImplementationType.APACHE_HIVE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.APACHE_HIVE_TABLE.getDeployedImplementationType() + ".",
                               DataWarehouseDeployedImplementationType.APACHE_HIVE_TABLE.getAssociatedTypeName(),
                               false,
                               "113b9fcf-1bfe-4d35-bb9c-81fdd8e498b2",
                               DataWarehouseDeployedImplementationType.APACHE_HIVE_TABLE,
                               PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                               null),

    /**
     * Create an asset with the OceanBase Table technology type.
     */
    OCEANBASE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.OCEANBASE_TABLE.getDeployedImplementationType(),
                             "Create a " + DataWarehouseDeployedImplementationType.OCEANBASE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.OCEANBASE_TABLE.getDeployedImplementationType() + ".",
                             DataWarehouseDeployedImplementationType.OCEANBASE_TABLE.getAssociatedTypeName(),
                             false,
                             "77bdb6c4-b967-4fc2-aeda-c0d47aa44730",
                             DataWarehouseDeployedImplementationType.OCEANBASE_TABLE,
                             PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                             null),

    /**
     * Create an asset with the Teradata Table technology type.
     */
    TERADATA_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.TERADATA_TABLE.getDeployedImplementationType(),
                            "Create a " + DataWarehouseDeployedImplementationType.TERADATA_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.TERADATA_TABLE.getDeployedImplementationType() + ".",
                            DataWarehouseDeployedImplementationType.TERADATA_TABLE.getAssociatedTypeName(),
                            false,
                            "ec209a5a-3121-486a-ac62-369dbe8e4dec",
                            DataWarehouseDeployedImplementationType.TERADATA_TABLE,
                            PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                            null),

    /**
     * Create an asset with the Amazon Redshift Table technology type.
     */
    AMAZON_REDSHIFT_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.AMAZON_REDSHIFT_TABLE.getDeployedImplementationType(),
                                   "Create a " + DataWarehouseDeployedImplementationType.AMAZON_REDSHIFT_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.AMAZON_REDSHIFT_TABLE.getDeployedImplementationType() + ".",
                                   DataWarehouseDeployedImplementationType.AMAZON_REDSHIFT_TABLE.getAssociatedTypeName(),
                                   false,
                                   "bb45f0e3-6ca5-4686-9794-f467b7c81fc4",
                                   DataWarehouseDeployedImplementationType.AMAZON_REDSHIFT_TABLE,
                                   PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                   null),

    /**
     * Create an asset with the Snowflake Table technology type.
     */
    SNOWFLAKE_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE.getDeployedImplementationType(),
                             "Create a " + DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE.getDeployedImplementationType() + ".",
                             DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE.getAssociatedTypeName(),
                             false,
                             "36f609b7-64ea-4fa1-9cc1-bf5475146265",
                             DataWarehouseDeployedImplementationType.SNOWFLAKE_TABLE,
                             PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                             null),

    /**
     * Create an asset with the Google Cloud Spanner Table technology type.
     */
    GOOGLE_SPANNER_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.GOOGLE_SPANNER_TABLE.getDeployedImplementationType(),
                                  "Create a " + DataWarehouseDeployedImplementationType.GOOGLE_SPANNER_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.GOOGLE_SPANNER_TABLE.getDeployedImplementationType() + ".",
                                  DataWarehouseDeployedImplementationType.GOOGLE_SPANNER_TABLE.getAssociatedTypeName(),
                                  false,
                                  "7244fdc7-4147-4e37-ac74-46fc2d937d2b",
                                  DataWarehouseDeployedImplementationType.GOOGLE_SPANNER_TABLE,
                                  PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                  null),

    /**
     * Create an asset with the Trino Table technology type.
     */
    TRINO_TABLE_TEMPLATE(DataWarehouseDeployedImplementationType.TRINO_TABLE.getDeployedImplementationType(),
                         "Create a " + DataWarehouseDeployedImplementationType.TRINO_TABLE.getAssociatedTypeName() + " asset with technology type " + DataWarehouseDeployedImplementationType.TRINO_TABLE.getDeployedImplementationType() + ".",
                         DataWarehouseDeployedImplementationType.TRINO_TABLE.getAssociatedTypeName(),
                         false,
                         "74eaabb9-f619-40bf-8250-ff5b36eaa1ad",
                         DataWarehouseDeployedImplementationType.TRINO_TABLE,
                         PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                         null),

    /**
     * Create an asset with the Unity Catalog Table technology type.
     */
    UNITY_CATALOG_TABLE_TEMPLATE(UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(),
                                 "Create a " + UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getAssociatedTypeName() + " asset with technology type " + UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType() + ".",
                                 UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getAssociatedTypeName(),
                                 false,
                                 "632fd18d-4fa5-4899-820f-f9f4c5b1a9cb",
                                 UnityCatalogDeployedImplementationType.OSS_UC_TABLE,
                                 PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                 null)

    ;


    /**
     * Symbolic name of the template.
     */
    private final String templateName;

    /**
     * Description of the value to provide for this template.
     */
    private final String templateDescription;

    /**
     * Open metadata type name of the template.
     */
    private final String typeName;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private final boolean required;


    /**
     * Option guid for a template to use if no template is specified.
     */
    private final String defaultTemplateGUID;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;

    private final DeployedImplementationTypeDefinition deployedImplementationType;

    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     * @param otherPropertyValues other values
     */
    DatabaseTableTemplateType(String                               templateName,
                      String                               templateDescription,
                      String                               typeName,
                      boolean                              required,
                      String                               defaultTemplateGUID,
                      DeployedImplementationTypeDefinition deployedImplementationType,
                      List<PlaceholderPropertyType>        placeholderPropertyTypes,
                      Map<String, String>                  otherPropertyValues)
    {
        this.templateName               = templateName;
        this.templateDescription        = templateDescription;
        this.typeName                   = typeName;
        this.required                   = required;
        this.defaultTemplateGUID        = defaultTemplateGUID;
        this.deployedImplementationType = deployedImplementationType;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return defaultTemplateGUID;
    }

    /**
     * Return the name of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    @Override
    public String getTemplateDescription()
    {
        return templateDescription;
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "6.2-SNAPSHOT";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }



    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return null;
    }


    /**
     * Return the open metadata type name.
     *
     * @return open metadata type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return whether this catalog template is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Return a map of property name to property value that the catalog template should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (DatabaseTableTemplateType templateTypeEnum : DatabaseTableTemplateType.values())
        {
            templateTypes.add(templateTypeEnum.getTemplateType());
        }

        return templateTypes;
    }


    /**
     * Return the catalog template type for a specific catalog template enum.
     *
     * @return catalog template type
     */
    public TemplateType getTemplateType()
    {
        TemplateType templateType = new TemplateType();

        templateType.setName(templateName);
        templateType.setOpenMetadataTypeName(typeName);
        templateType.setDescription(templateDescription);
        templateType.setRequired(required);
        templateType.setDefaultTemplateGUID(defaultTemplateGUID);
        templateType.setOtherPropertyValues(otherPropertyValues);

        return templateType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DatabaseTableTemplateType{templateName='" + templateName + "'}";
    }
}
