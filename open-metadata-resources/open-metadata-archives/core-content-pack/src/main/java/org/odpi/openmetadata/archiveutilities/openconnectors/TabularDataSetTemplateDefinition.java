/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.controls.CSVFileTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of the templates for tabular data sets.
 */
public enum TabularDataSetTemplateDefinition implements TemplateDefinition
{
    POSTGRES_TABULAR_DATA_SET_TEMPLATE(PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_TEMPLATE.getTemplateGUID(),
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET,
                                       PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.TABLE_NAME.getPlaceholder(),
                                       PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType() + "::" + PlaceholderProperty.SERVER_NAME.getPlaceholder() + "::" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.TABLE_NAME.getPlaceholder(),
                                       null,
                                       "relational",
                                       "SQL",
                                       new PostgresTabularDataSetProvider().getConnectorType().getGUID(),
                                       getPostgresDataSetConfigurationProperties(),
                                       new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                                       "jdbc:postgresql://" +
                                               PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                               PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                               PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                               PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                       DataAssetTemplateDefinition.getPostgresSchemaConfigurationProperties(),
                                       PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholder(),
                                       SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                                       new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                                       PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                                       PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_TEMPLATE.getReplacementAttributes(),
                                       PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_TEMPLATE.getPlaceholders(),
                                       ContentPackDefinition.POSTGRES_CONTENT_PACK),

    POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE(PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getTemplateGUID(),
                                                  PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION,
                                                  PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType() + "::" + PlaceholderProperty.SERVER_NAME.getPlaceholder() + "::" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  null,
                                                  "relational",
                                                  "SQL",
                                                  new PostgresTabularDataSetCollectionProvider().getConnectorType().getGUID(),
                                                  getPostgresDataSetCollectionConfigurationProperties(),
                                                  new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                                                  "jdbc:postgresql://" +
                                                          PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                          PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                                          PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                                          PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  DataAssetTemplateDefinition.getPostgresSchemaConfigurationProperties(),
                                                  PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholder(),
                                                  SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                                                  new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                                                  PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                                                  PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getReplacementAttributes(),
                                                  PostgreSQLTemplateType.POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getPlaceholders(),
                                                  ContentPackDefinition.POSTGRES_CONTENT_PACK),


    CSV_TABULAR_DATA_SET_TEMPLATE(CSVFileTemplateType.CSV_TABULAR_DATA_SET_TEMPLATE.getTemplateGUID(),
                                  DeployedImplementationType.CSV_TABULAR_DATA_SET,
                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                  DeployedImplementationType.CSV_TABULAR_DATA_SET.getDeployedImplementationType() + "::" + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder() + "::" + PlaceholderProperty.TABLE_NAME.getPlaceholder() + "::" + PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                  null,
                                  "csv",
                                  "unicode",
                                  new CSVTabularDataSetProvider().getConnectorType().getGUID(),
                                  getCSVTabularDataSetConfigProperties(),
                                  new CSVFileStoreProvider().getConnectorType().getGUID(),
                                  null,
                                  null,
                                  null,
                                  null,
                                  null,
                                  null,
                                  CSVFileTemplateType.CSV_TABULAR_DATA_SET_TEMPLATE.getReplacementAttributes(),
                                  CSVFileTemplateType.CSV_TABULAR_DATA_SET_TEMPLATE.getPlaceholders(),
                                  ContentPackDefinition.CORE_CONTENT_PACK),


    CSV_TABULAR_DATA_SET_COLLECTION_TEMPLATE(CSVFileTemplateType.CSV_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getTemplateGUID(),
                                             DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION,
                                             PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                             DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType() + "::" + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder() + "::" + PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                             null,
                                             "csv",
                                             "unicode",
                                             new CSVTabularDataSetCollectionProvider().getConnectorType().getGUID(),
                                             getCSVTabularDataSetCollectionConfigProperties(),
                                             new CSVFileStoreProvider().getConnectorType().getGUID(),
                                             null,
                                             null,
                                             null,
                                             null,
                                             null,
                                             null,
                                             CSVFileTemplateType.CSV_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getReplacementAttributes(),
                                             CSVFileTemplateType.CSV_TABULAR_DATA_SET_COLLECTION_TEMPLATE.getPlaceholders(),
                                             ContentPackDefinition.CORE_CONTENT_PACK),

    ;



    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getPostgresDataSetCollectionConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_NAME.getName(), PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder());
        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getName(), PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder());

        return configurationProperties;
    }


    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getPostgresDataSetConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_NAME.getName(), PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder());
        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getName(), PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder());
        configurationProperties.put(PostgresConfigurationProperty.TABLE_NAME.getName(), PostgresPlaceholderProperty.TABLE_NAME.getPlaceholder());
        configurationProperties.put(PostgresConfigurationProperty.TABLE_DESCRIPTION.getName(), PostgresPlaceholderProperty.TABLE_DESCRIPTION.getPlaceholder());

        return configurationProperties;
    }


    /**
     * Build the extended properties for a CSV tabular data set.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getCSVTabularDataSetConfigProperties()
    {
        Map<String, Object> configProperties = new HashMap<>();

        configProperties.put(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());
        configProperties.put(CSVFileConfigurationProperty.TABLE_NAME.name, PlaceholderProperty.TABLE_NAME.getName());
        configProperties.put(CSVFileConfigurationProperty.TABLE_DESCRIPTION.name, PlaceholderProperty.TABLE_DESCRIPTION.getName());

        return configProperties;
    }


    /**
     * Build the extended properties for a CSV tabular data set collection.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getCSVTabularDataSetCollectionConfigProperties()
    {
        Map<String, Object> configProperties = new HashMap<>();

        configProperties.put(CSVFileConfigurationProperty.DIRECTORY_PATH_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());

        return configProperties;
    }


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final String                               assetName;
    private final String                               assetDescription;
    private final String                               qualifiedName;
    private final Map<String, Object>                  extendedProperties;
    private final String                               encoding;
    private final String                               encodingLanguage;
    private final String                               dataSetConnectorTypeGUID;
    private final Map<String, Object>                  dataSetConfigurationProperties;
    private final String              technologyConnectorTypeGUID;
    private final String              technologyNetworkAddress;
    private final Map<String, Object> technologyConfigurationProperties;
    private final String                               secretsCollectionName;
    private final String                               secretsStorePurpose;
    private final String                               secretsStoreConnectorTypeGUID;
    private final String                               secretsStoreFileName;
    private final List<ReplacementAttributeType>       replacementAttributeTypes;
    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;
    private final ContentPackDefinition                contentPackDefinition;


    /**
     * Construct the description of the template.
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param qualifiedName optional server name
     * @param extendedProperties subtype properties for the asset
     * @param encoding           what encoding is needed?
     * @param encodingLanguage           language used to encode the contents of the file
     * @param technologyConnectorTypeGUID connector type to link to the connection
     * @param technologyNetworkAddress network address for the endpoint
     * @param secretsCollectionName name of collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param technologyConfigurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     * @param contentPackDefinition            which content pack does this server belong?
     */
    TabularDataSetTemplateDefinition(String                               guid,
                                     DeployedImplementationTypeDefinition deployedImplementationType,
                                     String                               assetName,
                                     String                               assetDescription,
                                     String                               qualifiedName,
                                     Map<String, Object>                  extendedProperties,
                                     String                               encoding,
                                     String                               encodingLanguage,
                                     String                               dataSetConnectorTypeGUID,
                                     Map<String, Object>                  dataSetConfigurationProperties,
                                     String                               technologyConnectorTypeGUID,
                                     String                               technologyNetworkAddress,
                                     Map<String, Object>                  technologyConfigurationProperties,
                                     String                               secretsCollectionName,
                                     String                               secretsStorePurpose,
                                     String                               secretsStoreConnectorTypeGUID,
                                     String                               secretsStoreFileName,
                                     List<ReplacementAttributeType>       replacementAttributeTypes,
                                     List<PlaceholderPropertyType>        placeholderPropertyTypes,
                                     ContentPackDefinition                contentPackDefinition)
    {
        this.guid                              = guid;
        this.deployedImplementationType        = deployedImplementationType;
        this.assetName                         = assetName;
        this.assetDescription                  = assetDescription;
        this.qualifiedName                     = qualifiedName;
        this.extendedProperties                = extendedProperties;
        this.encoding                          = encoding;
        this.encodingLanguage                  = encodingLanguage;
        this.dataSetConnectorTypeGUID          = dataSetConnectorTypeGUID;
        this.dataSetConfigurationProperties    = dataSetConfigurationProperties;
        this.technologyConnectorTypeGUID       = technologyConnectorTypeGUID;
        this.technologyNetworkAddress          = technologyNetworkAddress;
        this.technologyConfigurationProperties = technologyConfigurationProperties;
        this.secretsCollectionName             = secretsCollectionName;
        this.secretsStorePurpose               = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID     = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName              = secretsStoreFileName;
        this.replacementAttributeTypes         = replacementAttributeTypes;
        this.placeholderPropertyTypes          = placeholderPropertyTypes;
        this.contentPackDefinition             = contentPackDefinition;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return guid;
    }


    /**
     * Return the name to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateName()
    {
        return deployedImplementationType.getDeployedImplementationType() + " template";
    }

    /**
     * Return the description to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateDescription()
    {
        return "Create a " + deployedImplementationType.getDeployedImplementationType() + " asset with an associated Connection.";
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "5.4-SNAPSHOT";
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
     * Return the name of the asset.
     *
     * @return enum
     */
    public String getAssetName()
    {
        return assetName;
    }


    /**
     * Return the name for the associated capability.
     *
     * @return string
     */
    public String getAssetDescription()
    {
        return assetDescription;
    }


    /**
     * Return the name of the server where this asset resides.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the properties defined for the asset subtype.
     *
     * @return map
     */
    public Map<String, Object> getExtendedProperties()
    {
        return extendedProperties;
    }


    /**
     * Return the type of encoding.  If null no encoding classification is added to the asset.
     *
     * @return string
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Return the optional encoding language used by the asset.
     *
     * @return string
     */
    public String getEncodingLanguage()
    {
        return encodingLanguage;
    }


    public String getDataSetConnectorTypeGUID()
    {
        return dataSetConnectorTypeGUID;
    }

    public Map<String, Object> getDataSetConfigurationProperties()
    {
        return dataSetConfigurationProperties;
    }

    /**
     * Return the connector type GUID for the connection.
     *
     * @return string
     */
    public String getTechnologyConnectorTypeGUID()
    {
        return technologyConnectorTypeGUID;
    }


    /**
     * Return the endpoint address.
     *
     * @return string
     */
    public String getTechnologyNetworkAddress()
    {
        return technologyNetworkAddress;
    }


    /**
     * Return the configuration properties for the connection.
     *
     * @return map
     */
    public Map<String, Object> getTechnologyConfigurationProperties()
    {
        return technologyConfigurationProperties;
    }


    /**
     * Return the name of the secrets collection to use to locate this asset's secrets.
     *
     * @return name
     */
    public String getSecretsCollectionName()
    {
        return secretsCollectionName;
    }


    /**
     * Return the purpose of the secrets store.
     *
     * @return name
     */
    public String getSecretsStorePurpose()
    {
        return secretsStorePurpose;
    }

    /**
     * Return the optional secrets store connector provider for the server.
     *
     * @return connector provider
     */
    public String getSecretsStoreConnectorTypeGUID()
    {
        return secretsStoreConnectorTypeGUID;
    }


    /**
     * Return the location of the secrets store.
     *
     * @return path name of file
     */
    public String getSecretsStoreFileName()
    {
        return secretsStoreFileName;
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
        return replacementAttributeTypes;
    }


    /**
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateDefinition{templateName='" + getTemplateName() + "'}";
    }
}
