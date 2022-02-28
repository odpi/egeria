/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SimpleCatalogArchiveHelper creates elements used when creating a simple catalog.  This includes assets, their schemas and connections.
 */
public class SimpleCatalogArchiveHelper
{
    protected static final String guidMapFileNamePostFix    = "GUIDMap.json";

    private static final String CONNECTION_TYPE_NAME                     = "Connection";
    private static final String CONNECTOR_TYPE_TYPE_NAME                 = "ConnectorType";
    private static final String ENDPOINT_TYPE_NAME                       = "Endpoint";
    private static final String CONNECTION_CONNECTOR_TYPE_TYPE_NAME      = "ConnectionConnectorType";
    private static final String CONNECTION_ENDPOINT_TYPE_NAME            = "ConnectionEndpoint";
    private static final String CONNECTOR_CATEGORY_TYPE_NAME             = "ConnectorCategory";
    private static final String CONNECTOR_TYPE_DIRECTORY_TYPE_NAME       = "ConnectorTypeDirectory";
    private static final String CONNECTOR_IMPL_CHOICE_TYPE_NAME          = "ConnectorImplementationChoice";

    private static final String COLLECTION_TYPE_NAME                     = "Collection";
    private static final String COLLECTION_MEMBER_TYPE_NAME              = "CollectionMembership";

    private static final String GLOSSARY_TYPE_NAME                       = "Glossary";
    private static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME         = "ExternalGlossaryLink";
    private static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME    = "ExternallySourcedGlossary";
    private static final String CANONICAL_VOCABULARY_TYPE_NAME           = "CanonicalVocabulary";
    private static final String GLOSSARY_CATEGORY_TYPE_NAME              = "GlossaryCategory";
    private static final String SUBJECT_AREA_TYPE_NAME                   = "SubjectArea";
    private static final String CATEGORY_ANCHOR_TYPE_NAME                = "CategoryAnchor";
    private static final String CATEGORY_HIERARCHY_LINK_TYPE_NAME        = "CategoryHierarchyLink";
    private static final String GLOSSARY_TERM_TYPE_NAME                  = "GlossaryTerm";
    private static final String TERM_ANCHOR_TYPE_NAME                    = "TermAnchor";
    private static final String TERM_CATEGORIZATION_TYPE_NAME            = "TermCategorization";
    private static final String SEMANTIC_ASSIGNMENT_TYPE_NAME            = "TermAnchor";
    private static final String MORE_INFORMATION_TYPE_NAME               = "MoreInformation";
    private static final String SPINE_OBJECT_NAME                        = "SpineObject";
    private static final String SPINE_ATTRIBUTE_NAME                     = "SpineAttribute";
    private static final String IS_A_TYPE_OF_RELATIONSHIP_NAME           = "IsATypeOfRelationship";
    private static final String HAS_A_RELATIONSHIP_NAME                  = "TermHASARelationship";
    private static final String RELATED_TERM_RELATIONSHIP_NAME           = "RelatedTerm";

    private static final String SOFTWARE_CAPABILITY_TYPE_NAME            = "SoftwareCapability";

    private static final String ASSET_TYPE_NAME                          = "Asset";
    private static final String CONNECTION_TO_ASSET_TYPE_NAME            = "ConnectionToAsset";
    private static final String DATA_CONTENT_FOR_DATA_SET_TYPE_NAME      = "DataContentForDataSet";
    private static final String ASSET_SCHEMA_TYPE_TYPE_NAME              = "AssetSchemaType";
    private static final String ASSET_ZONE_MEMBERSHIP_TYPE_NAME          = "AssetZoneMembership";

    private static final String SCHEMA_TYPE_TYPE_NAME                    = "SchemaType";
    private static final String PRIMITIVE_SCHEMA_TYPE_TYPE_NAME          = "PrimitiveSchemaType";
    private static final String SCHEMA_TYPE_OPTION_TYPE_NAME             = "SchemaTypeOption";
    private static final String SCHEMA_ATTRIBUTE_TYPE_NAME               = "SchemaAttribute";

    private static final String ATTRIBUTE_FOR_SCHEMA_TYPE_NAME           = "AttributeForSchema";
    private static final String NESTED_SCHEMA_ATTRIBUTE_TYPE_NAME        = "NestedSchemaAttribute";
    private static final String TYPE_EMBEDDED_ATTRIBUTE_TYPE_NAME        = "TypeEmbeddedAttribute";
    private static final String CALCULATED_VALUE_TYPE_NAME               = "CalculatedValue";
    private static final String DERIVED_QUERY_TARGET_TYPE_NAME           = "DerivedSchemaTypeQueryTarget";
    private static final String PRIMARY_KEY_TYPE_NAME                    = "PrimaryKey";
    private static final String FOREIGN_KEY_TYPE_NAME                    = "ForeignKey";

    private static final String API_OPERATION_TYPE_NAME                  = "APIOperation";
    private static final String API_OPERATIONS_TYPE_NAME                 = "APIOperations";
    protected static final String API_HEADER_TYPE_NAME                     = "APIHeader";
    protected static final String API_REQUEST_TYPE_NAME                    = "APIRequest";
    protected static final String API_RESPONSE_TYPE_NAME                   = "APIResponse";
    private static final String API_PARAMETER_TYPE_NAME                  = "APIParameter";
    private static final String API_PARAMETER_LIST_TYPE_NAME             = "APIParameterList";

    private static final String QUALIFIED_NAME_PROPERTY                      = "qualifiedName";
    private static final String ADDITIONAL_PROPERTIES_PROPERTY               = "additionalProperties";

    private static final String NAME_PROPERTY                                = "name";
    private static final String DISPLAY_NAME_PROPERTY                        = "displayName";
    private static final String DESCRIPTION_PROPERTY                         = "description";

    private static final String ASSET_SUMMARY_PROPERTY                       = "assetSummary";
    private static final String ZONE_MEMBERSHIP_PROPERTY                     = "zoneMembership";

    private static final String CAPABILITY_TYPE_PROPERTY                     = "capabilityType";
    private static final String CAPABILITY_VERSION_PROPERTY                  = "capabilityVersion";
    private static final String PATCH_LEVEL_PROPERTY                         = "patchLevel";
    private static final String SUPPORTED_ASSET_TYPE_PROPERTY                = "supportedAssetTypeName";
    private static final String EXPECTED_DATA_FORMAT_PROPERTY                = "expectedDataFormat";
    private static final String CONNECTOR_PROVIDER_PROPERTY                  = "connectorProviderClassName";
    private static final String CONNECTOR_FRAMEWORK_PROPERTY                 = "connectorFrameworkNameName";
    private static final String CONNECTOR_FRAMEWORK_DEFAULT                  = "Open Connector Framework (OCF)";
    private static final String CONNECTOR_INTERFACE_LANGUAGE_PROPERTY        = "connectorInterfaceLanguage";
    private static final String CONNECTOR_INTERFACE_LANGUAGE_DEFAULT         = "Java";
    private static final String CONNECTOR_INTERFACES_PROPERTY                = "connectorInterfaces";
    private static final String TARGET_TECHNOLOGY_SOURCE_PROPERTY            = "targetTechnologySource";
    private static final String TARGET_TECHNOLOGY_NAME_PROPERTY              = "targetTechnologyName";
    private static final String TARGET_TECHNOLOGY_INTERFACES_PROPERTY        = "targetTechnologyInterfaces";
    private static final String TARGET_TECHNOLOGY_VERSIONS_PROPERTY          = "targetTechnologyVersions";
    private static final String SECURED_PROPERTIES_PROPERTY                  = "securedProperties";
    private static final String CONFIGURATION_PROPERTIES_PROPERTY            = "configurationProperties";
    private static final String USER_ID_PROPERTY                             = "userId";
    private static final String CLEAR_PASSWORD_PROPERTY                      = "clearPassword";
    private static final String ENCRYPTED_PASSWORD_PROPERTY                  = "encryptedPassword";
    private static final String RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY    = "recognizedAdditionalProperties";
    private static final String RECOGNIZED_SECURED_PROPERTIES_PROPERTY       = "recognizedSecuredProperties";
    private static final String RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY = "recognizedConfigurationProperties";
    private static final String NETWORK_ADDRESS_PROPERTY                     = "networkAddress";
    private static final String PROTOCOL_PROPERTY                            = "protocol";
    private static final String REQUIRED_PROPERTY                            = "required";
    private static final String PARAMETER_TYPE_PROPERTY                      = "parameterType";
    private static final String PATH_TYPE_PROPERTY                           = "path";
    private static final String COMMAND_TYPE_PROPERTY                        = "command";
    private static final String CONFIDENCE_TYPE_PROPERTY                     = "confidence";
    private static final String STEWARD_PROPERTY                             = "steward";
    private static final String STEWARD_TYPE_NAME_PROPERTY                   = "stewardTypeName";
    private static final String STEWARD_PROPERTY_NAME_PROPERTY               = "stewardPropertyName";
    private static final String SOURCE_PROPERTY                              = "source";
    private static final String NOTES_PROPERTY                               = "notes";
    private static final String FORMULA_PROPERTY                             = "formula";
    private static final String QUERY_ID_PROPERTY                            = "queryId";
    private static final String QUERY_PROPERTY                               = "query";
    private static final String POSITION_PROPERTY                            = "position";
    private static final String MIN_CARDINALITY_PROPERTY                     = "minCardinality";
    private static final String MAX_CARDINALITY_PROPERTY                     = "maxCardinality";
    private static final String ALLOWS_DUPLICATE_VALUES_PROPERTY             = "allowsDuplicateValues";
    private static final String ORDERED_VALUES_PROPERTY                      = "orderedValues";
    private static final String DEFAULT_VALUE_OVERRIDE_PROPERTY              = "defaultValueOverride";
    private static final String MINIMUM_LENGTH_PROPERTY                      = "minimumLength";
    private static final String LENGTH_PROPERTY                              = "length";
    private static final String PRECISION_PROPERTY                           = "precision";
    private static final String SIGNIFICANT_DIGITS_PROPERTY                  = "significantDigits";
    private static final String IS_NULLABLE_PROPERTY                         = "isNullable";
    private static final String NATIVE_CLASS_PROPERTY                        = "nativeClass";
    private static final String ALIASES_PROPERTY                             = "aliases";
    private static final String SORT_ORDER_PROPERTY                          = "sortOrder";
    private static final String SCHEMA_TYPE_NAME_PROPERTY                    = "schemaTypeName";
    private static final String VERSION_NUMBER_PROPERTY                      = "versionNumber";
    private static final String IS_DEPRECATED_PROPERTY                       = "isDeprecated";
    private static final String AUTHOR_PROPERTY                              = "author";
    private static final String USAGE_PROPERTY                               = "usage";
    private static final String ENCODING_STANDARD_PROPERTY                   = "encodingStandard";
    private static final String NAMESPACE_PROPERTY                           = "namespace";
    private static final String DATA_TYPE_PROPERTY                           = "dataType";
    private static final String DEFAULT_VALUE_PROPERTY                       = "defaultValue";
    private static final String FIXED_VALUE_PROPERTY                         = "fixedValue";

    private static final String EXAMPLES_PROPERTY                            = "examples";
    private static final String TECHNICAL_NAME_PROPERTY                      = "technicalName";
    private static final String LANGUAGE_PROPERTY                            = "language";
    private static final String SCOPE_PROPERTY                               = "scope";
    private static final String URL_PROPERTY                                 = "url";
    private static final String ORGANIZATION_PROPERTY                        = "organization";
    private static final String VERSION_PROPERTY                             = "version";
    private static final String STATUS_IDENTIFIER_PROPERTY                   = "statusIdentifier";
    private static final String CONFIDENCE_PROPERTY                          = "confidence";
    private static final String ATTRIBUTE_NAME_PROPERTY                      = "attribute";
    private static final String DECORATION_PROPERTY                          = "decoration";
    private static final String UNIQUE_VALUES_PROPERTY                       = "uniqueValues";
    private static final String NAVIGABLE_PROPERTY                           = "navigable";

    protected OpenMetadataArchiveBuilder archiveBuilder;
    protected OMRSArchiveHelper          archiveHelper;
    protected OMRSArchiveGUIDMap         idToGUIDMap;

    protected String             archiveRootName;
    protected String             originatorName;
    protected String             versionName;

    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveRootName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);

        this.idToGUIDMap = new OMRSArchiveGUIDMap(archiveRootName + guidMapFileNamePostFix);

        this.archiveRootName = archiveRootName;
        this.originatorName = originatorName;
        this.versionName = versionName;
    }


    /**
     * Save the GUIDs so that the GUIDs of the elements inside the archive are consistent each time the archive runs.
     */
    public void saveGUIDs()
    {
        System.out.println("GUIDs map size: " + idToGUIDMap.getSize());

        idToGUIDMap.saveGUIDs();
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param displayName display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the sub type
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAsset(String               typeName,
                           String               qualifiedName,
                           String               displayName,
                           String               description,
                           Map<String, String>  additionalProperties,
                           Map<String, Object>  extendedProperties,
                           List<Classification> classifications)
    {
        final String methodName = "addAsset";

        String assetTypeName = ASSET_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(assetTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param displayName display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the sub type
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              displayName,
                           String              description,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        return this.addAsset(typeName, qualifiedName, displayName, description, additionalProperties, extendedProperties, null);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param displayName display name for the asset
     * @param description description about the asset
     * @param governanceZones list of zones to add to the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the sub type
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              displayName,
                           String              description,
                           List<String>        governanceZones,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        final String methodName = "addAsset (with governance zones)";

        if (governanceZones == null)
        {
            return this.addAsset(typeName, qualifiedName, displayName, description, additionalProperties, extendedProperties, null);
        }
        else
        {
            List<Classification> classifications = new ArrayList<>();

            InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName,
                                                                                           null,
                                                                                           ZONE_MEMBERSHIP_PROPERTY,
                                                                                           governanceZones,
                                                                                           methodName);

            Classification classification = archiveHelper.getClassification(ASSET_ZONE_MEMBERSHIP_TYPE_NAME,
                                                                            properties,
                                                                            InstanceStatus.ACTIVE);

            classifications.add(classification);

            return this.addAsset(typeName, qualifiedName, displayName, description, additionalProperties, extendedProperties, classifications);
        }
    }


    /**
     * Create a software capability entity.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param additionalProperties any other properties.
     *
     * @return id for the capability
     */
    public String addSoftwareCapability(String              typeName,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        String              capabilityType,
                                        String              capabilityVersion,
                                        String              patchLevel,
                                        String              source,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties)
    {
        final String methodName = "addSoftwareCapability";

        String assetTypeName = SOFTWARE_CAPABILITY_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CAPABILITY_TYPE_PROPERTY, capabilityType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CAPABILITY_VERSION_PROPERTY, capabilityVersion, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PATCH_LEVEL_PROPERTY, patchLevel, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SOURCE_PROPERTY, source, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(assetTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 null);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create the relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetSummary summary of the asset from the connection perspective
     * @param connectionGUID unique identifier of the connection to its content
     */
    public void addConnectionForAsset(String assetGUID,
                                      String assetSummary,
                                      String connectionGUID)
    {
        final String methodName = "addConnectionForAsset";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityDetail connectionEntity = archiveBuilder.getEntity(connectionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(assetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ASSET_SUMMARY_PROPERTY, assetSummary, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTION_TO_ASSET_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(assetGUID + "_to_" + connectionGUID + "_data_consumer_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a data set and an asset that is providing all or part of its content.
     *
     * @param dataContentGUID unique identifier of the data store
     * @param dataSetGUID unique identifier of the consuming data set
     */
    public void addDataContentForDataSet(String dataContentGUID,
                                         String dataSetGUID)
    {
        EntityDetail dataContentEntity = archiveBuilder.getEntity(dataContentGUID);
        EntityDetail dataSetEntity = archiveBuilder.getEntity(dataSetGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(dataContentEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(dataSetEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(dataContentGUID + "_to_" + dataSetGUID + "_data_consumer_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the top level schema type for an asset.
     *
     * @param assetGUID unique identifier of asset
     * @param typeName name of asset subtype to use - default is SchemaType
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addTopLevelSchemaType(String              assetGUID,
                                        String              typeName,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Map<String, String> additionalProperties)
    {
        final String methodName = "addTopLevelSchemaType";

        String schemaTypeTypeName = SCHEMA_TYPE_TYPE_NAME;

        if (typeName != null)
        {
            schemaTypeTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(schemaTypeTypeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (assetGUID != null)
        {
            EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(ASSET_SCHEMA_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_asset_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return schemaTypeEntity.getGUID();
    }


    /**
     * Create the schema type for an API operation.
     *
     * @param apiSchemaTypeGUID unique identifier of top level schemaType
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param path the path name for the operation
     * @param command the command to issue eg GET, POST
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addAPIOperation(String              apiSchemaTypeGUID,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              path,
                                  String              command,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIOperation";

        String schemaTypeTypeName = API_OPERATION_TYPE_NAME;

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PATH_TYPE_PROPERTY, path, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, COMMAND_TYPE_PROPERTY, command, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(schemaTypeTypeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (apiSchemaTypeGUID != null)
        {
            EntityDetail parentEntity = archiveBuilder.getEntity(apiSchemaTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(parentEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(API_OPERATIONS_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_parent_api_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return schemaTypeEntity.getGUID();
    }


    /**
     * Create a parameter list schema type for an API operation.
     *
     * @param apiOperationGUID unique identifier of top level schemaType
     * @param relationshipTypeName name of relationship type - default is APIRequest
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param required is this parameter list required
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addAPIParameterList(String              apiOperationGUID,
                                      String              relationshipTypeName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      boolean             required,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIParameterList";

        String typeName = API_REQUEST_TYPE_NAME;

        if (relationshipTypeName != null)
        {
            typeName = relationshipTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, REQUIRED_PROPERTY, required, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail parameterListEntity = archiveHelper.getEntityDetail(API_PARAMETER_LIST_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(parameterListEntity);

        if (apiOperationGUID != null)
        {
            EntityDetail operationEntity = archiveBuilder.getEntity(apiOperationGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(operationEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(parameterListEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_operation_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return parameterListEntity.getGUID();
    }


    /**
     * Create the relationship between a SchemaTypeChoice element and a child element using the SchemaTypeOption relationship.
     *
     * @param schemaTypeChoiceGUID unique identifier of the parent element
     * @param schemaTypeOptionGUID unique identifier of the child element
     */
    public void addSchemaTypeOption(String schemaTypeChoiceGUID,
                                    String schemaTypeOptionGUID)
    {
        EntityDetail schemaTypeChoiceEntity = archiveBuilder.getEntity(schemaTypeChoiceGUID);
        EntityDetail schemaTypeOptionEntity = archiveBuilder.getEntity(schemaTypeOptionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(schemaTypeChoiceEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeOptionEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SCHEMA_TYPE_OPTION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(schemaTypeChoiceGUID + "_to_" + schemaTypeOptionGUID + "_schema_type_option_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a SchemaType element and a child SchemaAttribute element using the AttributeForSchema relationship.
     *
     * @param schemaTypeGUID unique identifier of the parent element
     * @param schemaAttributeGUID unique identifier of the child element
     */
    public void addAttributeForSchemaType(String schemaTypeGUID,
                                          String schemaAttributeGUID)
    {
        EntityDetail schemaTypeChoiceEntity = archiveBuilder.getEntity(schemaTypeGUID);
        EntityDetail schemaTypeOptionEntity = archiveBuilder.getEntity(schemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(schemaTypeChoiceEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeOptionEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(ATTRIBUTE_FOR_SCHEMA_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(schemaTypeGUID + "_to_" + schemaAttributeGUID + "_attribute_for_schema_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a SchemaAttribute element and a child SchemaAttribute element using the NestedSchemaAttribute relationship.
     *
     * @param parentSchemaAttributeGUID unique identifier of the parent element
     * @param childSchemaAttributeGUID unique identifier of the child element
     */
    public void addNestedSchemaAttribute(String parentSchemaAttributeGUID,
                                         String childSchemaAttributeGUID)
    {
        EntityDetail parentSchemaAttributeEntity = archiveBuilder.getEntity(parentSchemaAttributeGUID);
        EntityDetail childSchemaAtrributeEntity = archiveBuilder.getEntity(childSchemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(parentSchemaAttributeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(childSchemaAtrributeEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(NESTED_SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentSchemaAttributeGUID + "_to_" + childSchemaAttributeGUID + "_nested_schema_attribute_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the a schema attribute with a TypeEmbeddedAttribute classification.
     *
     * @param typeName name of schema attribute subtype to use - default is SchemaAttribute
     * @param schemaTypeName name of schema type subtype to use - default is PrimitiveSchemaType
     * @param qualifiedName unique name for the schema attribute
     * @param displayName display name for the schema attribute
     * @param description description about the schema attribute
     * @param dataType data type for the schema attribute
     * @param length length of the storage used by the schema attribute
     * @param position position in the schema at this level
     * @param additionalProperties any other properties.
     *
     * @return id for the schema attribute
     */
    public String addSchemaAttribute(String              typeName,
                                     String              schemaTypeName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              dataType,
                                     int                 length,
                                     int                 position,
                                     Map<String, String> additionalProperties)
    {
        final String methodName = "addSchemaAttribute";

        String schemaAttributeTypeName = SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
        }

        String schemaTypeTypeName = PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;

        if (schemaTypeName != null)
        {
            schemaTypeTypeName = schemaTypeName;
        }

        InstanceProperties entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, DESCRIPTION_PROPERTY, description, methodName);
        entityProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, entityProperties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, SCHEMA_TYPE_NAME_PROPERTY, schemaTypeTypeName, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, DATA_TYPE_PROPERTY, dataType, methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, classificationProperties, LENGTH_PROPERTY, length, methodName);

        Classification classification = archiveHelper.getClassification(TYPE_EMBEDDED_ATTRIBUTE_TYPE_NAME, classificationProperties, InstanceStatus.ACTIVE);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(classification);

        EntityDetail schemaAttributeEntity = archiveHelper.getEntityDetail(schemaAttributeTypeName,
                                                                           idToGUIDMap.getGUID(qualifiedName),
                                                                           entityProperties,
                                                                           InstanceStatus.ACTIVE,
                                                                           classifications);

        archiveBuilder.addEntity(schemaAttributeEntity);

        return schemaAttributeEntity.getGUID();
    }


    /**
     * Create a connection entity.
     *
     * @param qualifiedName unique name for the connection
     * @param displayName display name for the connection
     * @param description description about the connection
     * @param userId userId that the connector should use to connect to the platform that hosts the asset.
     * @param clearPassword possible password for the connector
     * @param encryptedPassword possible password for the connector
     * @param securedProperties properties hidden from the client
     * @param configurationProperties properties used to configure the connector
     * @param additionalProperties any other properties.
     * @param connectorTypeGUID unique identifier for the connector type
     * @param endpointGUID unique identifier for the endpoint of the asset
     *
     * @return id for the connection
     */
    public String addConnection(String              qualifiedName,
                                String              displayName,
                                String              description,
                                String              userId,
                                String              clearPassword,
                                String              encryptedPassword,
                                Map<String, String> securedProperties,
                                Map<String, Object> configurationProperties,
                                Map<String, String> additionalProperties,
                                String              connectorTypeGUID,
                                String              endpointGUID)
    {
        final String methodName = "addConnection";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USER_ID_PROPERTY, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CLEAR_PASSWORD_PROPERTY, clearPassword, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ENCRYPTED_PASSWORD_PROPERTY, encryptedPassword, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, SECURED_PROPERTIES_PROPERTY, securedProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, CONFIGURATION_PROPERTIES_PROPERTY, configurationProperties, methodName);

        EntityDetail connectionEntity = archiveHelper.getEntityDetail(CONNECTION_TYPE_NAME,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(connectionEntity);

        if (connectorTypeGUID != null)
        {
            EntityDetail connectorTypeEntity = archiveBuilder.getEntity(connectorTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connectorType_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (endpointGUID != null)
        {
            EntityDetail endpointEntity = archiveBuilder.getEntity(endpointGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(endpointEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectionEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTION_ENDPOINT_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_endpoint_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectionEntity.getGUID();
    }


    /**
     * Create a connector type entity.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param connectorTypeGUID fixed unique identifier for connector type - comes from the Connector Provider
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param supportedAssetTypeName type of asset supported by this connector
     * @param expectedDataFormat format of the data stored in the resource
     * @param connectorProviderClassName code for this type of connector
     * @param connectorFrameworkName name of the framework that the connector implements - default "Open Connector Framework (OCF)"
     * @param connectorInterfaceLanguage programming language of the connector's interface
     * @param connectorInterfaces the interfaces that the connector implements
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param targetTechnologyInterfaces called interfaces the target technology
     * @param targetTechnologyVersions supported versions of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    public String addConnectorType(String              connectorCategoryGUID,
                                   String              connectorTypeGUID,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              supportedAssetTypeName,
                                   String              expectedDataFormat,
                                   String              connectorProviderClassName,
                                   String              connectorFrameworkName,
                                   String              connectorInterfaceLanguage,
                                   List<String>        connectorInterfaces,
                                   String              targetTechnologySource,
                                   String              targetTechnologyName,
                                   List<String>        targetTechnologyInterfaces,
                                   List<String>        targetTechnologyVersions,
                                   List<String>        recognizedSecuredProperties,
                                   List<String>        recognizedConfigurationProperties,
                                   List<String>        recognizedAdditionalProperties,
                                   Map<String, String> additionalProperties)
    {
        idToGUIDMap.setGUID(qualifiedName, connectorTypeGUID);

        try
        {
            return this.addConnectorType(connectorCategoryGUID,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         supportedAssetTypeName,
                                         expectedDataFormat,
                                         connectorProviderClassName,
                                         connectorFrameworkName,
                                         connectorInterfaceLanguage,
                                         connectorInterfaces,
                                         targetTechnologySource,
                                         targetTechnologyName,
                                         targetTechnologyInterfaces,
                                         targetTechnologyVersions,
                                         recognizedSecuredProperties,
                                         recognizedConfigurationProperties,
                                         recognizedAdditionalProperties,
                                         additionalProperties);
        }
        catch (Exception alreadyDefined)
        {
            return connectorTypeGUID;
        }
    }


    /**
     * Create a connector type entity.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param supportedAssetTypeName type of asset supported by this connector
     * @param expectedDataFormat format of the data stored in the resource
     * @param connectorProviderClassName code for this type of connector
     * @param connectorFrameworkName name of the framework that the connector implements - default "Open Connector Framework (OCF)"
     * @param connectorInterfaceLanguage programming language of the connector's interface
     * @param connectorInterfaces the interfaces that the connector implements
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param targetTechnologyInterfaces called interfaces the target technology
     * @param targetTechnologyVersions supported versions of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorType(String              connectorCategoryGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              supportedAssetTypeName,
                                      String              expectedDataFormat,
                                      String              connectorProviderClassName,
                                      String              connectorFrameworkName,
                                      String              connectorInterfaceLanguage,
                                      List<String>        connectorInterfaces,
                                      String              targetTechnologySource,
                                      String              targetTechnologyName,
                                      List<String>        targetTechnologyInterfaces,
                                      List<String>        targetTechnologyVersions,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      List<String>        recognizedAdditionalProperties,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addConnectorType";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SUPPORTED_ASSET_TYPE_PROPERTY, supportedAssetTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EXPECTED_DATA_FORMAT_PROPERTY, expectedDataFormat, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_PROVIDER_PROPERTY, connectorProviderClassName, methodName);
        if (connectorFrameworkName != null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_FRAMEWORK_PROPERTY, connectorFrameworkName, methodName);
        }
        else
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_FRAMEWORK_PROPERTY, CONNECTOR_FRAMEWORK_DEFAULT, methodName);
        }
        if (connectorInterfaceLanguage != null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_INTERFACE_LANGUAGE_PROPERTY, connectorInterfaceLanguage, methodName);
        }
        else
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_INTERFACE_LANGUAGE_PROPERTY, CONNECTOR_INTERFACE_LANGUAGE_DEFAULT, methodName);
        }
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, CONNECTOR_INTERFACES_PROPERTY, connectorInterfaces, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_SOURCE_PROPERTY, targetTechnologySource, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_NAME_PROPERTY, targetTechnologyName, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_INTERFACES_PROPERTY, targetTechnologyInterfaces, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_VERSIONS_PROPERTY, targetTechnologyVersions, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_SECURED_PROPERTIES_PROPERTY, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail connectorTypeEntity = archiveHelper.getEntityDetail(CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(connectorTypeEntity);

        if (connectorCategoryGUID != null)
        {
            EntityDetail connectorCategoryEntity = archiveBuilder.getEntity(connectorCategoryGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectorCategoryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTOR_IMPL_CHOICE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connector_category_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectorTypeEntity.getGUID();
    }


    /**
     * Create a connector category entity.
     *
     * @param connectorTypeDirectoryGUID unique identifier of connector type directory that this connector connector belongs to - or null for an independent connector category
     * @param qualifiedName unique name for the connector category
     * @param displayName display name for the connector category
     * @param description description about the connector category
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorCategory(String               connectorTypeDirectoryGUID,
                                          String               qualifiedName,
                                          String               displayName,
                                          String               description,
                                          String               targetTechnologySource,
                                          String               targetTechnologyName,
                                          Map<String, Boolean> recognizedSecuredProperties,
                                          Map<String, Boolean> recognizedConfigurationProperties,
                                          Map<String, Boolean> recognizedAdditionalProperties,
                                          Map<String, String>  additionalProperties)
    {
        final String methodName = "addConnectorCategory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_SOURCE_PROPERTY, targetTechnologySource, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TARGET_TECHNOLOGY_NAME_PROPERTY, targetTechnologyName, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, RECOGNIZED_SECURED_PROPERTIES_PROPERTY, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail connectorCategoryEntity = archiveHelper.getEntityDetail(CONNECTOR_CATEGORY_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             null);

        archiveBuilder.addEntity(connectorCategoryEntity);

        if (connectorTypeDirectoryGUID != null)
        {
            EntityDetail connectorTypeDirectoryEntity = archiveBuilder.getEntity(connectorTypeDirectoryGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectorTypeDirectoryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorCategoryEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(COLLECTION_MEMBER_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connector_type_directory_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectorCategoryEntity.getGUID();
    }


    /**
     * Create a connector category entity.
     *
     * @param qualifiedName unique name for the connector type directory
     * @param displayName display name for the connector type directory
     * @param description description about the connector type directory
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorTypeDirectory(String              qualifiedName,
                                               String              displayName,
                                               String              description,
                                               Map<String, String> additionalProperties)
    {
        final String methodName = "addConnectorTypeDirectory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        Classification classification = archiveHelper.getClassification(CONNECTOR_TYPE_DIRECTORY_TYPE_NAME, null, InstanceStatus.ACTIVE);
        List<Classification> classifications = new ArrayList<>();

        classifications.add(classification);

        EntityDetail connectorTypeDirectoryEntity = archiveHelper.getEntityDetail(COLLECTION_TYPE_NAME,
                                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                                  properties,
                                                                                  InstanceStatus.ACTIVE,
                                                                                  classifications);

        archiveBuilder.addEntity(connectorTypeDirectoryEntity);

        return connectorTypeDirectoryEntity.getGUID();
    }


    /**
     * Create a endpoint entity.
     *
     * @param qualifiedName unique name for the endpoint
     * @param displayName display name for the endpoint
     * @param description description about the endpoint
     * @param networkAddress location of the asset
     * @param protocol protocol to use to connect to the asset
     * @param additionalProperties any other properties.
     *
     * @return id for the endpoint
     */
    public String addEndpoint(String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              networkAddress,
                              String              protocol,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addEndpoint";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NETWORK_ADDRESS_PROPERTY, networkAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PROTOCOL_PROPERTY, protocol, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail endpointEntity = archiveHelper.getEntityDetail(ENDPOINT_TYPE_NAME,
                                                                    idToGUIDMap.getGUID(qualifiedName),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    null);

        archiveBuilder.addEntity(endpointEntity);

        return endpointEntity.getGUID();
    }




    /**
     * Create a glossary entity.  If the external link is specified, the glossary entity is linked to an
     * ExternalGlossaryLink entity.  If the scope is specified, the glossary entity is classified as
     * a CanonicalGlossary.
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName display name for the glossary
     * @param description description about the glossary
     * @param language language that the glossary is written in
     * @param usage how the glossary should be used
     * @param externalLink link to material
     * @param scope scope of the content.
     *
     * @return id for the glossary
     */
    public String addGlossary(String   qualifiedName,
                              String   displayName,
                              String   description,
                              String   language,
                              String   usage,
                              String   externalLink,
                              String   scope)
    {
        final String methodName = "addGlossary";
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, LANGUAGE_PROPERTY, language, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USAGE_PROPERTY, usage, methodName);

        List<Classification> classifications = null;

        if (scope != null)
        {
            Classification  canonicalVocabClassification = archiveHelper.getClassification(CANONICAL_VOCABULARY_TYPE_NAME,
                                                                                           archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                     null,
                                                                                                                                     SCOPE_PROPERTY,
                                                                                                                                     scope,
                                                                                                                                     methodName),
                                                                                           InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(canonicalVocabClassification);
        }

        EntityDetail  glossaryEntity = archiveHelper.getEntityDetail(GLOSSARY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(glossaryEntity);

        if (externalLink != null)
        {
            String externalLinkQualifiedName = qualifiedName + "_external_link";
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, externalLinkQualifiedName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, URL_PROPERTY, externalLink, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ORGANIZATION_PROPERTY, originatorName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, VERSION_PROPERTY, versionName, methodName);

            EntityDetail  externalLinkEntity = archiveHelper.getEntityDetail(EXTERNAL_GLOSSARY_LINK_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(externalLinkQualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

            archiveBuilder.addEntity(externalLinkEntity);

            EntityProxy end1 = archiveHelper.getEntityProxy(glossaryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(externalLinkEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_link_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return glossaryEntity.getGUID();
    }


    /**
     * Add a glossary category to the archive and connect it to glossary.
     *
     * @param glossaryId identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     *
     * @return identifier of the category
     */
    public String addCategory(String   glossaryId,
                              String   qualifiedName,
                              String   displayName,
                              String   description,
                              String   subjectArea)
    {
        final String methodName = "addCategory";
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        List<Classification> classifications = null;

        if (subjectArea != null)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(SUBJECT_AREA_TYPE_NAME,
                                                                                        archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                  null,
                                                                                                                                  NAME_PROPERTY,
                                                                                                                                  subjectArea,
                                                                                                                                  methodName),
                                                                                        InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(subjectAreaClassification);
        }

        EntityDetail  categoryEntity = archiveHelper.getEntityDetail(GLOSSARY_CATEGORY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(categoryEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(categoryEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CATEGORY_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        return categoryEntity.getGUID();
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.
     *
     * @param glossaryId unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     *
     * @return unique identifier of the term
     */
    public String addTerm(String       glossaryId,
                          List<String> categoryIds,
                          String       qualifiedName,
                          String       displayName,
                          String       description)
    {
        return addTerm(glossaryId, categoryIds, qualifiedName, displayName, description,null,false,false,false);
    }

    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.
     *
     * @param glossaryId unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     * @param examples examples of the term
     * @param isSpineObject term is a spine object
     * @param isSpineAttribute term is a spine attribute
     * @param categoriesAsNames when true the categories are specified as qualified names, otherwise they are guids.
     * @return unique identifier of the term
     */
    public String addTerm(String       glossaryId,
                          List<String> categoryIds,
                          String       qualifiedName,
                          String       displayName,
                          String       description,
                          String       examples,
                          boolean      isSpineObject,
                          boolean      isSpineAttribute,
                          boolean      categoriesAsNames)
    {
        final String methodName = "addTerm";
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        if (examples !=null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EXAMPLES_PROPERTY, examples, methodName);
        }

        List<Classification> classifications = null;

        if (isSpineObject)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(SPINE_OBJECT_NAME,
                                                                                        null,
                                                                                        InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(subjectAreaClassification);
        }

        if (isSpineAttribute)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(SPINE_ATTRIBUTE_NAME,
                                                                                        null,
                                                                                        InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(subjectAreaClassification);
        }

        EntityDetail  termEntity = archiveHelper.getEntityDetail(GLOSSARY_TERM_TYPE_NAME,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(termEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(termEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        if (categoryIds != null)
        {
            for (String  categoryId : categoryIds)
            {
                if (categoryId != null)
                {
                    if (categoriesAsNames)
                    {
                        categoryId = idToGUIDMap.getGUID(categoryId);
                    }
                    end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryId));

                    archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_CATEGORIZATION_TYPE_NAME,
                                                                                 idToGUIDMap.getGUID(qualifiedName + "_category_" + categoryId + "_relationship"),
                                                                                 null,
                                                                                 InstanceStatus.ACTIVE,
                                                                                 end1,
                                                                                 end2));
                }
            }
        }

        return termEntity.getGUID();
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param parentCategoryId unique identifier for the parent category
     * @param childCategoryId unique identifier for the child category
     */
    public void addCategoryToCategory(String  parentCategoryId,
                                      String  childCategoryId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentCategoryId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(childCategoryId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CATEGORY_HIERARCHY_LINK_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentCategoryId + "_to_" + childCategoryId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a navigation link from one referenceable to another to show they provide more information.
     *
     * @param describedElementId unique identifier for the element that is referencing the other.
     * @param describerElementId unique identifier for the element being pointed to.
     */
    public void addMoreInformationLink(String  describedElementId,
                                       String  describerElementId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describedElementId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describerElementId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(MORE_INFORMATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(describedElementId + "_to_" + describerElementId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a semantic assignment between a term and a Referenceable - for example a model element.
     *
     * @param termId identifier of term
     * @param referenceableId identifier of referenceable
     */
    public void linkTermToReferenceable(String  termId,
                                        String  referenceableId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SEMANTIC_ASSIGNMENT_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + termId),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add an is-a-type-of relationship
     *
     * @param specialTermQName qualified name of the specialized term
     * @param generalizedTermQName qualified name of the generalized term
     */
    public void addIsATypeOfRelationship(String specialTermQName , String generalizedTermQName)
    {

        String specializedTermId = idToGUIDMap.getGUID(specialTermQName);
        String generalizedTermId = idToGUIDMap.getGUID(generalizedTermQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(specializedTermId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(generalizedTermId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(IS_A_TYPE_OF_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(specializedTermId + "_to_" + generalizedTermId ),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    public void addHasARelationship(String conceptQName, String propertyQName)
    {
        String conceptId = idToGUIDMap.getGUID(conceptQName);
        String propertyId = idToGUIDMap.getGUID(propertyQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(propertyId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(conceptId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(HAS_A_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_hasa"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    public void addRelatedTermRelationship(String conceptQName, String propertyQName)
    {
        String conceptId = idToGUIDMap.getGUID(conceptQName);
        String propertyId = idToGUIDMap.getGUID(propertyQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(conceptId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(propertyId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(RELATED_TERM_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_related"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add Category hierarchy relationship
     *
     * @param childCategoryName name of the child category
     * @param parentNames set of the names of the parent categories qualified names
     */
    public void addCategoryHierarchy(String childCategoryName, Set<String> parentNames)
    {
        String childId= idToGUIDMap.getGUID(childCategoryName);

        for (String parentName:parentNames)
        {
            String parentId  = idToGUIDMap.getGUID(parentName);
            addCategoryToCategory(parentId,childId);
        }
    }
}
