/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SimpleCatalogArchiveHelper creates elements used when creating a simple catalog.  This includes assets, their schemas and connections, design models.
 */
public class SimpleCatalogArchiveHelper
{
    protected static final String guidMapFileNamePostFix    = "GUIDMap.json";

    private static final String ANCHORS_CLASSIFICATION_NAME              = "Anchors";
    private static final String ANCHOR_GUID_PROPERTY                     = "anchorGUID";

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

    private static final String SEARCH_KEYWORD_TYPE_NAME                 = "SearchKeyword";
    private static final String SEARCH_KEYWORD_LINK_RELATIONSHIP_NAME    = "SearchKeywordLink";

    private static final String EXTERNAL_REFERENCE_TYPE_NAME             = "ExternalReference";
    public  static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME         = "ExternalGlossaryLink"; // subtype
    public  static final String RELATED_MEDIA_TYPE_NAME                  = "RelatedMedia";         // subtype

    private static final String EXTERNAL_REFERENCE_LINK_RELATIONSHIP_NAME = "ExternalReferenceLink";
    private static final String MEDIA_REFERENCE_RELATIONSHIP_NAME         = "MediaReference";
    private static final String MEDIA_USAGE_ENUM_NAME                     = "MediaUsage";
    public  static final int       MEDIA_USAGE_ICON                       = 0;
    public  static final int       MEDIA_USAGE_THUMBNAIL                  = 1;
    public  static final int       MEDIA_USAGE_ILLUSTRATION               = 2;
    public  static final int       MEDIA_USAGE_USAGE_GUIDANCE             = 3;
    public  static final int       MEDIA_USAGE_OTHER                      = 99;
    private static final String MEDIA_TYPE_ENUM_NAME                      = "MediaType";
    public  static final int       MEDIA_TYPE_IMAGE                       = 0;
    public  static final int       MEDIA_TYPE_AUDIO                       = 1;
    public  static final int       MEDIA_TYPE_DOCUMENT                    = 2;
    public  static final int       MEDIA_TYPE_VIDEO                       = 3;
    public  static final int       MEDIA_TYPE_OTHER                       = 99;

    private static final String EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME = "ExternallySourcedGlossary";
    private static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME  = "LibraryCategoryReference";
    private static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME      = "LibraryTermReference";

    private static final String SUBJECT_AREA_DEFINITION_TYPE_NAME        = "SubjectAreaDefinition";
    private static final String SUBJECT_AREA_HIERARCHY_RELATIONSHIP_NAME = "SubjectAreaHierarchy";
    private static final String SUBJECT_AREA_CLASSIFICATION_NAME         = "SubjectArea";

    private static final String GLOSSARY_TYPE_NAME                       = "Glossary";
    private static final String CANONICAL_VOCABULARY_TYPE_NAME           = "CanonicalVocabulary";
    private static final String GLOSSARY_CATEGORY_TYPE_NAME              = "GlossaryCategory";
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

    private static final String TERM_RELATIONSHIP_STATUS_ENUM_NAME       = "TermRelationshipStatus";
    public static final int      TERM_RELATIONSHIP_STATUS_DRAFT          = 0;
    public static final int      TERM_RELATIONSHIP_STATUS_ACTIVE         = 1;
    public static final int      TERM_RELATIONSHIP_STATUS_DEPRECATED     = 2;
    public static final int      TERM_RELATIONSHIP_STATUS_OBSOLETE       = 3;
    public static final int      TERM_RELATIONSHIP_STATUS_OTHER          = 4;

    private static final String CONTEXT_DEFINITION_CLASSIFICATION_NAME   = "ContextDefinition";
    private static final String USED_IN_CONTEXT_RELATIONSHIP_NAME        = "UsedInContext";

    private static final String DESIGN_MODEL_TYPE_NAME                   = "DesignModel";
    private static final String CONCEPT_MODEL_CLASSIFICATION_NAME        = "ConceptModel";

    private static final String DESIGN_MODEL_SCOPE_TYPE_NAME             = "DesignModelScope";
    private static final String DESIGN_MODEL_ELEMENTS_IN_SCOPE_TYPE_NAME = "DesignModelElementsInScope";
    private static final String DESIGN_MODEL_ELEMENT_TYPE_NAME           = "DesignModelElement";
    private static final String DESIGN_MODEL_GROUP_TYPE_NAME             = "DesignModelGroup";
    private static final String DESIGN_MODEL_GROUP_MEMBERSHIP_NAME       = "DesignModelGroupMembership";
    private static final String DESIGN_MODEL_OWNERSHIP_RELATIONSHIP_NAME = "DesignModelOwnership";
    private static final String DESIGN_MODEL_IMPL_RELATIONSHIP_NAME      = "DesignModelImplementation";

    private static final String METAMODEL_INSTANCE_CLASSIFICATION_NAME   = "MetamodelInstance";
    private static final String METAMODEL_ELEMENT_GUID_PROPERTY_NAME     = "metamodelElementGUID";

    private static final String CONCEPT_BEAD_TYPE_NAME                   = "ConceptBead";
    private static final String CONCEPT_BEAD_LINK_TYPE_NAME              = "ConceptBeadLink";
    private static final String CONCEPT_BEAD_ATTRIBUTE_TYPE_NAME         = "ConceptBeadAttribute";
    private static final String CONCEPT_BEAD_RELATIONSHIP_END_NAME       = "ConceptBeadRelationshipEnd";
    private static final String CONCEPT_BEAD_ATTRIBUTE_LINK_TYPE_NAME    = "ConceptBeadAttributeLink";
    private static final String CONCEPT_MODEL_DECORATION_ENUM_NAME       = "ConceptModelDecoration";
    public static final int      CONCEPT_MODEL_DECORATION_NONE          = 0;
    public static final int      CONCEPT_MODEL_DECORATION_AGGREGATION   = 1;
    public static final int      CONCEPT_MODEL_DECORATION_COMPOSITION   = 2;
    public static final int      CONCEPT_MODEL_DECORATION_EXTENSION     = 3;

    private static final String CONCEPT_BEAD_COVERAGE_CATEGORY_ENUM_NAME  = "ConceptBeadAttributeCoverageCategory";
    public static final int      CONCEPT_BEAD_COVERAGE_UNKNOWN            = 0;
    public static final int      CONCEPT_BEAD_COVERAGE_UNIQUE_IDENTIFIER  = 1;
    public static final int      CONCEPT_BEAD_COVERAGE_IDENTIFIER         = 2;
    public static final int      CONCEPT_BEAD_COVERAGE_CORE_DETAIL        = 3;
    public static final int      CONCEPT_BEAD_COVERAGE_EXTENDED_DETAIL    = 4;
    private static final String CONCEPT_BEAD_COVERAGE_CLASSIFICATION_NAME = "ConceptBeadAttributeCoverage";
    private static final String CONCEPT_BEAD_COVERAGE_CATEGORY_PROPERTY   = "coverageCategory";

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
    public  static final String API_HEADER_TYPE_NAME                     = "APIHeader";
    public  static final String API_REQUEST_TYPE_NAME                    = "APIRequest";
    public  static final String API_RESPONSE_TYPE_NAME                   = "APIResponse";
    private static final String API_PARAMETER_TYPE_NAME                  = "APIParameter";
    private static final String API_PARAMETER_LIST_TYPE_NAME             = "APIParameterList";

    /*
     * Properties
     */
    private static final String QUALIFIED_NAME_PROPERTY                      = "qualifiedName";
    private static final String ADDITIONAL_PROPERTIES_PROPERTY               = "additionalProperties";

    private static final String NAME_PROPERTY                                = "name";
    private static final String ATTRIBUTE_NAME_PROPERTY                      = "attributeName";
    private static final String DISPLAY_NAME_PROPERTY                        = "displayName";
    private static final String TECHNICAL_NAME_PROPERTY                      = "technicalName";
    private static final String DESCRIPTION_PROPERTY                         = "description";
    private static final String DECORATION_PROPERTY                          = "decoration";
    private static final String VERSION_NUMBER_PROPERTY                      = "versionNumber";
    private static final String AUTHOR_PROPERTY                              = "author";
    private static final String POSITION_PROPERTY                            = "position";
    private static final String MIN_CARDINALITY_PROPERTY                     = "minCardinality";
    private static final String MAX_CARDINALITY_PROPERTY                     = "maxCardinality";
    private static final String UNIQUE_VALUES_PROPERTY                       = "uniqueValues";
    private static final String ORDERED_VALUES_PROPERTY                      = "orderedValues";
    private static final String NAVIGABLE_PROPERTY                           = "navigable";

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
    private static final String SCOPE_PROPERTY                               = "scope";
    private static final String STATUS_PROPERTY                              = "status";
    private static final String STEWARD_PROPERTY                             = "steward";
    private static final String STEWARD_TYPE_NAME_PROPERTY                   = "stewardTypeName";
    private static final String STEWARD_PROPERTY_NAME_PROPERTY               = "stewardPropertyName";
    private static final String SOURCE_PROPERTY                              = "source";
    private static final String EXPRESSION_PROPERTY                          = "expression";
    private static final String NOTES_PROPERTY                               = "notes";
    private static final String FORMULA_PROPERTY                             = "formula";
    private static final String QUERY_ID_PROPERTY                            = "queryId";
    private static final String QUERY_PROPERTY                               = "query";
    private static final String ALLOWS_DUPLICATE_VALUES_PROPERTY             = "allowsDuplicateValues";
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
    private static final String IS_DEPRECATED_PROPERTY                       = "isDeprecated";
    private static final String USAGE_PROPERTY                               = "usage";
    private static final String ENCODING_STANDARD_PROPERTY                   = "encodingStandard";
    private static final String NAMESPACE_PROPERTY                           = "namespace";
    private static final String DATA_TYPE_PROPERTY                           = "dataType";
    private static final String DEFAULT_VALUE_PROPERTY                       = "defaultValue";
    private static final String FIXED_VALUE_PROPERTY                         = "fixedValue";

    private static final String DOMAIN_IDENTIFIER_PROPERTY                   = "domainIdentifier";
    private static final String EXAMPLES_PROPERTY                            = "examples";
    private static final String LANGUAGE_PROPERTY                            = "language";
    private static final String IDENTIFIER_PROPERTY                          = "identifier";
    private static final String LAST_VERIFIED_PROPERTY                       = "lastVerified";

    private static final String REFERENCE_ID_PROPERTY                        = "referenceId";
    private static final String PAGES_PROPERTY                               = "pages";
    private static final String MEDIA_ID_PROPERTY                            = "mediaId";
    private static final String MEDIA_USAGE_PROPERTY                         = "mediaUsage";
    private static final String MEDIA_USAGE_OTHER_ID_PROPERTY                = "mediaUsageOtherId";

    private static final String KEYWORD_PROPERTY                             = "keyword";

    private static final String REFERENCE_TITLE_PROPERTY                     = "referenceTitle";
    private static final String REFERENCE_ABSTRACT_PROPERTY                  = "referenceAbstract";
    private static final String AUTHORS_PROPERTY                             = "authors";
    private static final String NUMBER_OF_PAGES_PROPERTY                     = "numberOfPages";
    private static final String PAGE_RANGE_PROPERTY                          = "pageRange";
    private static final String ORGANIZATION_PROPERTY                        = "organization";
    private static final String PUBLICATION_SERIES_PROPERTY                  = "publicationSeries";
    private static final String PUBLICATION_SERIES_VOLUME_PROPERTY           = "publicationSeriesVolume";
    private static final String EDITION_PROPERTY                             = "edition";
    private static final String REFERENCE_VERSION_PROPERTY                   = "referenceVersion";
    private static final String URL_PROPERTY                                 = "url";
    private static final String PUBLISHER_PROPERTY                           = "publisher";
    private static final String FIRST_PUBLICATION_DATE_PROPERTY              = "firstPublicationDate";
    private static final String PUBLICATION_DATE_PROPERTY                    = "publicationDate";
    private static final String PUBLICATION_CITY_PROPERTY                    = "publicationCity";
    private static final String PUBLICATION_YEAR_PROPERTY                    = "publicationYear";
    private static final String PUBLICATION_NUMBERS_PROPERTY                 = "publicationNumbers";
    private static final String LICENSE_PROPERTY                             = "license";
    private static final String COPYRIGHT_PROPERTY                           = "copyright";
    private static final String ATTRIBUTION_PROPERTY                         = "attribution";

    private static final String MEDIA_TYPE_PROPERTY                          = "mediaType";
    private static final String MEDIA_TYPE_OTHER_ID_PROPERTY                 = "mediaTypeOtherId";
    private static final String DEFAULT_MEDIA_USAGE_PROPERTY                 = "defaultMediaUsage";
    private static final String DEFAULT_MEDIA_USAGE_OTHER_ID_PROPERTY        = "defaultMediaUsageOtherId";

    private static final String STATUS_IDENTIFIER_PROPERTY                   = "statusIdentifier";
    private static final String CONFIDENCE_PROPERTY                          = "confidence";
    private static final String ATTRIBUTE_PROPERTY                           = "attribute";

    protected OpenMetadataArchiveBuilder archiveBuilder;
    protected OMRSArchiveHelper          archiveHelper;
    protected OMRSArchiveGUIDMap         idToGUIDMap;

    protected String             archiveRootName;
    protected String             originatorName;
    protected String             versionName;
    protected EnumElementDef     activeStatus;


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
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

        this.activeStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, 1);
    }


    /**
     * Return the guid of an element based on its qualified name.  This is a look up in the GUID map not the archive.
     * This means if the qualified name is not known, a new GUID is generated.
     *
     * @param qualifiedName qualified name ot look up
     * @return guid.
     */
    public String getGUID(String qualifiedName)
    {
        return idToGUIDMap.getGUID(qualifiedName);
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
     * Create an external reference entity.  This typically describes a publication, webpage book or reference source of information
     * that is from an external organization.
     *
     * @param typeName name of element subtype to use - default is ExternalReference
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param referenceTitle full title from the publication
     * @param referenceAbstract full abstract from the publication
     * @param description description about the element
     * @param authors authors of the element
     * @param numberOfPages number of pages in the external source
     * @param pageRange range of pages that is significant
     * @param authorOrganization organization that the information is from
     * @param publicationSeries publication series or journal that the external source is from.
     * @param publicationSeriesVolume volume of the publication series where the external source is found
     * @param edition edition where the external source is from
     * @param versionNumber version number for the element
     * @param referenceURL link to the external source
     * @param publisher publisher of the external source
     * @param firstPublicationDate date this material was first published (ie first version's publication date)
     * @param publicationDate date that this version was published
     * @param publicationCity city that the publisher operates from
     * @param publicationYear year that this version was published
     * @param publicationNumbers list of ISBNs for this external source
     * @param license name of the license associated with this external source
     * @param copyright copyright statement associated with this external source
     * @param attribution attribution statement to use when consuming this external source
     * @param searchKeywords list of keywords
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @return unique identifier for new external reference (externalReferenceGUID)
     */
    public String addExternalReference(String               typeName,
                                       String               qualifiedName,
                                       String               displayName,
                                       String               referenceTitle,
                                       String               referenceAbstract,
                                       String               description,
                                       List<String>         authors,
                                       int                  numberOfPages,
                                       String               pageRange,
                                       String               authorOrganization,
                                       String               publicationSeries,
                                       String               publicationSeriesVolume,
                                       String               edition,
                                       String               versionNumber,
                                       String               referenceURL,
                                       String               publisher,
                                       Date                 firstPublicationDate,
                                       Date                 publicationDate,
                                       String               publicationCity,
                                       String               publicationYear,
                                       List<String>         publicationNumbers,
                                       String               license,
                                       String               copyright,
                                       String               attribution,
                                       List<String>         searchKeywords,
                                       Map<String, String>  additionalProperties,
                                       Map<String, Object>  extendedProperties)
    {
        final String methodName = "addExternalReference";

        String elementTypeName = EXTERNAL_REFERENCE_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, REFERENCE_TITLE_PROPERTY, referenceTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, REFERENCE_ABSTRACT_PROPERTY, referenceAbstract, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, AUTHORS_PROPERTY, authors, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, NUMBER_OF_PAGES_PROPERTY, numberOfPages, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PAGE_RANGE_PROPERTY, pageRange, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ORGANIZATION_PROPERTY, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, REFERENCE_VERSION_PROPERTY, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PUBLICATION_SERIES_PROPERTY, publicationSeries, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PUBLICATION_SERIES_VOLUME_PROPERTY, publicationSeriesVolume, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EDITION_PROPERTY, edition, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, URL_PROPERTY, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PUBLISHER_PROPERTY, publisher, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, FIRST_PUBLICATION_DATE_PROPERTY, firstPublicationDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, PUBLICATION_DATE_PROPERTY, publicationDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PUBLICATION_CITY_PROPERTY, publicationCity, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PUBLICATION_YEAR_PROPERTY, publicationYear, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, PUBLICATION_NUMBERS_PROPERTY, publicationNumbers, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, LICENSE_PROPERTY, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, COPYRIGHT_PROPERTY, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ATTRIBUTION_PROPERTY, attribution, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail externalReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             null);

        archiveBuilder.addEntity(externalReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(SEARCH_KEYWORD_TYPE_NAME + ":" + keyword);
                    EntityDetail keywordEntity;

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.getEntity(keywordGUID);
                    }
                    else
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(SEARCH_KEYWORD_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(SEARCH_KEYWORD_TYPE_NAME + ":" + keyword),
                                                                       properties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(externalReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(SEARCH_KEYWORD_LINK_RELATIONSHIP_NAME,
                                                                                     idToGUIDMap.getGUID(externalReferenceEntity.getGUID() + "_to_" + keywordGUID + "_search_keyword_link_relationship"),
                                                                                     null,
                                                                                     InstanceStatus.ACTIVE,
                                                                                     end1,
                                                                                     end2));
                    }
                }
            }
        }

        return externalReferenceEntity.getGUID();
    }


    /**
     * Create the relationship between a design mode group and one of its members.
     *
     * @param referenceableGUID unique identifier of the element making the reference
     * @param externalReferenceGUID unique identifier of the external reference
     * @param referenceId unique reference id for this referenceable
     * @param description description of the relevance of the external reference
     * @param pages relevant pages in the external reference
     */
    public void addExternalReferenceLink(String referenceableGUID,
                                         String externalReferenceGUID,
                                         String referenceId,
                                         String description,
                                         String pages)
    {
        final String methodName = "addExternalReferenceLink";

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(externalReferenceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, REFERENCE_ID_PROPERTY, referenceId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PAGES_PROPERTY, pages, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(EXTERNAL_GLOSSARY_LINK_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + externalReferenceGUID + "_external_reference_link_relationship" + referenceId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a subject area entity.
     *
     * @param qualifiedName unique name for the subject area
     * @param displayName display name for the subject area
     * @param description description about the subject area
     * @param scope scope where the subject area is used
     * @param usage how is the subject area used
     * @param domainIdentifier unique identifier of the governance domain
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for subject area (subjectAreaGUID)
     */
    public String addSubjectAreaDefinition(String               qualifiedName,
                                           String               displayName,
                                           String               description,
                                           String               scope,
                                           String               usage,
                                           int                  domainIdentifier,
                                           Map<String, String>  additionalProperties,
                                           Map<String, Object>  extendedProperties)
    {
        final String methodName = "addSubjectAreaDefinition";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName); // it's an asset
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USAGE_PROPERTY, usage, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, DOMAIN_IDENTIFIER_PROPERTY, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(SUBJECT_AREA_DEFINITION_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a design mode group and one of its members.
     *
     * @param broaderSubjectAreaGUID unique identifier of the broader subject area
     * @param nestedSubjectAreaGUID unique identifier of the nested (narrower) subject area
     */
    public void addSubjectAreaHierarchy(String broaderSubjectAreaGUID,
                                        String nestedSubjectAreaGUID)
    {
        EntityDetail referenceableEntity = archiveBuilder.getEntity(broaderSubjectAreaGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(nestedSubjectAreaGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SUBJECT_AREA_HIERARCHY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(broaderSubjectAreaGUID + "_to_" + nestedSubjectAreaGUID + "_subject_area_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the subject area classification to the requested element.
     *
     * @param referenceableGUID unique identifier of the element to classify
     * @param subjectAreaQualifiedName name of the subject area.  The suggestion is that the name used is the qualified name.
     */
    public void addSubjectAreaClassification(String referenceableGUID,
                                             String subjectAreaQualifiedName)
    {
        final String methodName = "addSubjectAreaClassification";

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);

        EntityProxy referenceableEntityProxy = archiveHelper.getEntityProxy(referenceableEntity);

        Classification  subjectAreaClassification = archiveHelper.getClassification(SUBJECT_AREA_CLASSIFICATION_NAME,
                                                                                    archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                              null,
                                                                                                                              NAME_PROPERTY,
                                                                                                                              subjectAreaQualifiedName,
                                                                                                                              methodName),
                                                                                    InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(referenceableEntityProxy, subjectAreaClassification));
    }


    /**
     * Create a design model entity.
     *
     * @param typeName name of element subtype to use - default is DesignModel
     * @param classificationName name of classification the identifies the type of design model
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param technicalName technical name for the element
     * @param description description about the element
     * @param versionNumber version number for the element
     * @param author author of the element
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for new design model (designModelGUID)
     */
    public String addDesignModel(String               typeName,
                                 String               classificationName,
                                 String               qualifiedName,
                                 String               displayName,
                                 String               technicalName,
                                 String               description,
                                 String               versionNumber,
                                 String               author,
                                 Map<String, String>  additionalProperties,
                                 Map<String, Object>  extendedProperties)
    {
        final String methodName = "addDesignModel";

        String elementTypeName = DESIGN_MODEL_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        List<Classification> entityClassifications = null;

        if (classificationName != null)
        {
            entityClassifications = new ArrayList<>();

            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            entityClassifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, displayName, methodName); // it's an asset
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TECHNICAL_NAME_PROPERTY, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, VERSION_NUMBER_PROPERTY, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, AUTHOR_PROPERTY, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               entityClassifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create a design model element entity.  This may be a group or an element in the model.
     *
     * @param designModelGUID unique identifier of model that owns this element
     * @param typeName name of element subtype to use - default is DesignModelElement
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param technicalName technical name for the element
     * @param description description about the element
     * @param versionNumber version number for the element
     * @param author author of the element
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return unique identifier for the new model element
     */
    public String addDesignModelElement(String               designModelGUID,
                                        String               typeName,
                                        String               qualifiedName,
                                        String               displayName,
                                        String               technicalName,
                                        String               description,
                                        String               versionNumber,
                                        String               author,
                                        Map<String, String>  additionalProperties,
                                        Map<String, Object>  extendedProperties,
                                        List<Classification> classifications)
    {
        final String methodName = "addDesignModelElement";

        String elementTypeName = DESIGN_MODEL_ELEMENT_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        List<Classification> entityClassifications = classifications;

        if (designModelGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ANCHOR_GUID_PROPERTY, designModelGUID, methodName);
            Classification     classification           = archiveHelper.getClassification(ANCHORS_CLASSIFICATION_NAME, classificationProperties, InstanceStatus.ACTIVE);

            entityClassifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TECHNICAL_NAME_PROPERTY, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, VERSION_NUMBER_PROPERTY, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, AUTHOR_PROPERTY, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               entityClassifications);

        archiveBuilder.addEntity(newEntity);

        if (designModelGUID != null)
        {
            EntityDetail designModelEntity = archiveBuilder.getEntity(designModelGUID);
            EntityDetail designModelElementEntity = archiveBuilder.getEntity(newEntity.getGUID());

            EntityProxy end1 = archiveHelper.getEntityProxy(designModelEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(designModelElementEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
                                                                         idToGUIDMap.getGUID(designModelGUID + "_to_" + newEntity.getGUID() + "_design_model_group_membership_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a design model group and one of its members.
     *
     * @param groupGUID unique identifier of the design model group
     * @param memberGUID unique identifier of the member
     */
    public void addDesignModelGroupMembership(String groupGUID,
                                              String memberGUID)
    {
        EntityDetail designModelGroupEntity = archiveBuilder.getEntity(groupGUID);
        EntityDetail designModelElementEntity = archiveBuilder.getEntity(memberGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(designModelGroupEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(designModelElementEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
                                                                     idToGUIDMap.getGUID(groupGUID + "_to_" + memberGUID + "_design_model_group_membership_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a concept bead link and the concept bead at one of its ends.
     *
     * @param conceptBeadLinkGUID unique identifier of the concept bead link entity
     * @param conceptBeadGUID unique identifier of the concept bead
     * @param attributeName name of the attribute for this end
     * @param conceptModelDecoration what type of relationship end
     * @param position in the attributes for the bead
     * @param minCardinality minimum number of the relationships
     * @param maxCardinality maximum number of the relationships
     * @param uniqueValues are the relationship values unique
     * @param orderedValues are the relationship values in any order (using position)
     * @param navigable is it possible to navigate to the concept bead
     */
    public void addConceptBeadRelationshipEnd(String  conceptBeadLinkGUID,
                                              String  conceptBeadGUID,
                                              String  attributeName,
                                              int     conceptModelDecoration,
                                              int     position,
                                              int     minCardinality,
                                              int     maxCardinality,
                                              boolean uniqueValues,
                                              boolean orderedValues,
                                              boolean navigable)
    {
        final String methodName = "addConceptBeadRelationshipEnd";

        EntityDetail   entityOne                  = archiveBuilder.getEntity(conceptBeadLinkGUID);
        EntityDetail   entityTwo                  = archiveBuilder.getEntity(conceptBeadGUID);
        EnumElementDef conceptModelDecorationEnum = archiveHelper.getEnumElement(CONCEPT_MODEL_DECORATION_ENUM_NAME, conceptModelDecoration);

        EntityProxy end1 = archiveHelper.getEntityProxy(entityOne);
        EntityProxy end2 = archiveHelper.getEntityProxy(entityTwo);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ATTRIBUTE_NAME_PROPERTY, attributeName, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, POSITION_PROPERTY, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, MIN_CARDINALITY_PROPERTY, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, MAX_CARDINALITY_PROPERTY, maxCardinality, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, DECORATION_PROPERTY, conceptModelDecorationEnum.getOrdinal(), conceptModelDecorationEnum.getValue(), conceptModelDecorationEnum.getDescription(), methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, UNIQUE_VALUES_PROPERTY, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, ORDERED_VALUES_PROPERTY, orderedValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, NAVIGABLE_PROPERTY, navigable, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptBeadLinkGUID + "_to_" + conceptBeadGUID + "_concept_bead_relationship_end_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a concept bead link and the concept bead at one of its ends.
     *
     * @param conceptBeadGUID unique identifier of the concept bead entity
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute entity
     * @param position in the attributes for the bead
     * @param minCardinality minimum number of the relationships
     * @param maxCardinality maximum number of the relationships
     * @param uniqueValues are the relationship values unique
     * @param orderedValues are the relationship values in any order (using position)
     */
    public void addConceptBeadAttributeLink(String  conceptBeadGUID,
                                            String  conceptBeadAttributeGUID,
                                            int     position,
                                            int     minCardinality,
                                            int     maxCardinality,
                                            boolean uniqueValues,
                                            boolean orderedValues)
    {
        final String methodName = "addConceptBeadAttributeLink";

        EntityDetail   entityOne   = archiveBuilder.getEntity(conceptBeadGUID);
        EntityDetail   entityTwo   = archiveBuilder.getEntity(conceptBeadAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entityOne);
        EntityProxy end2 = archiveHelper.getEntityProxy(entityTwo);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, POSITION_PROPERTY, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, MIN_CARDINALITY_PROPERTY, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, MAX_CARDINALITY_PROPERTY, maxCardinality, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, UNIQUE_VALUES_PROPERTY, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, ORDERED_VALUES_PROPERTY, orderedValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptBeadGUID + "_to_" + conceptBeadAttributeGUID + "_concept_bead_attribute_link_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param displayName display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
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
     * @param capabilityType type
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
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
                                                                     idToGUIDMap.getGUID(assetGUID + "_to_" + connectionGUID + "_asset_connection_relationship"),
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
                                                                     idToGUIDMap.getGUID(dataContentGUID + "_to_" + dataSetGUID + "_data_content_for_data_set_relationship"),
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
                                                                         idToGUIDMap.getGUID(qualifiedName + "_asset_schema_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PATH_TYPE_PROPERTY, path, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, COMMAND_TYPE_PROPERTY, command, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(API_OPERATION_TYPE_NAME,
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
                                                                         idToGUIDMap.getGUID(qualifiedName + "_api_parameter_to_operation_relationship"),
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
        EntityDetail childSchemaAttributeEntity = archiveBuilder.getEntity(childSchemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(parentSchemaAttributeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(childSchemaAttributeEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(NESTED_SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentSchemaAttributeGUID + "_to_" + childSchemaAttributeGUID + "_nested_schema_attribute_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a schema attribute with a TypeEmbeddedAttribute classification.
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
    public    String addConnectorCategory(String               connectorTypeDirectoryGUID,
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
    public String addConnectorTypeDirectory(String              qualifiedName,
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
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, REFERENCE_VERSION_PROPERTY, versionName, methodName);

            EntityDetail  externalLinkEntity = archiveHelper.getEntityDetail(EXTERNAL_GLOSSARY_LINK_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(externalLinkQualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

            archiveBuilder.addEntity(externalLinkEntity);

            EntityProxy end1 = archiveHelper.getEntityProxy(glossaryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(externalLinkEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME,
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
     * @param glossaryGUID identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     *
     * @return identifier of the category
     */
    public String addCategory(String   glossaryGUID,
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
            Classification  subjectAreaClassification = archiveHelper.getClassification(SUBJECT_AREA_CLASSIFICATION_NAME,
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

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
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
     * @param glossaryGUID unique identifier of the glossary
     * @param categoryGUIDs unique identifiers of the categories
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     *
     * @return unique identifier of the term
     */
    public String addTerm(String       glossaryGUID,
                          List<String> categoryGUIDs,
                          String       qualifiedName,
                          String       displayName,
                          String       description)
    {
        return addTerm(glossaryGUID, categoryGUIDs, false, qualifiedName, displayName, description,null,false, false, false, null, null);
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.
     *
     * @param glossaryGUID unique identifier of the glossary
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
    public String addTerm(String       glossaryGUID,
                          List<String> categoryIds,
                          String       qualifiedName,
                          String       displayName,
                          String       description,
                          String       examples,
                          boolean      isSpineObject,
                          boolean      isSpineAttribute,
                          boolean      categoriesAsNames)
    {
        return addTerm(glossaryGUID,
                       categoryIds,
                       categoriesAsNames,
                       qualifiedName,
                       displayName,
                       description,
                       examples,
                       isSpineObject,
                       isSpineAttribute,
                       false,
                       null,
                       null);
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.  Add requested classifications
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param categoriesAsNames when true the categories are specified as qualified names, otherwise they are guids.
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     * @param examples examples of the term
     * @param isSpineObject term is a spine object
     * @param isSpineAttribute term is a spine attribute
     * @param isContext is this term a context definition?
     * @param contextDescription description to add to the ContextDefinition classification
     * @param contextScope scope to add to the context classification
     *
     * @return unique identifier of the term
     */
    public String addTerm(String       glossaryGUID,
                          List<String> categoryIds,
                          boolean      categoriesAsNames,
                          String       qualifiedName,
                          String       displayName,
                          String       description,
                          String       examples,
                          boolean      isSpineObject,
                          boolean      isSpineAttribute,
                          boolean      isContext,
                          String       contextDescription,
                          String       contextScope)
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

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(subjectAreaClassification);
        }

        if (isContext)
        {
            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, DESCRIPTION_PROPERTY, contextDescription, methodName);
            classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, SCOPE_PROPERTY, contextScope, methodName);

            Classification  subjectAreaClassification = archiveHelper.getClassification(CONTEXT_DEFINITION_CLASSIFICATION_NAME,
                                                                                        classificationProperties,
                                                                                        InstanceStatus.ACTIVE);

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(subjectAreaClassification);
        }

        EntityDetail  termEntity = archiveHelper.getEntityDetail(GLOSSARY_TERM_TYPE_NAME,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(termEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(termEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        if (categoryIds != null)
        {
            InstanceProperties categorizationProperties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, STATUS_PROPERTY, activeStatus.getOrdinal(), activeStatus.getValue(), activeStatus.getDescription(), methodName);

            for (String  categoryId : categoryIds)
            {
                if (categoryId != null)
                {
                    String categoryGUID = categoryId;

                    if (categoriesAsNames)
                    {
                        categoryGUID = idToGUIDMap.getGUID(categoryId);
                    }

                    end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));

                    /*
                     * Note properties set to ACTIVE - if you need different properties use addTermToCategory
                     */
                    archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_CATEGORIZATION_TYPE_NAME,
                                                                                 idToGUIDMap.getGUID(qualifiedName + "_category_" + categoryId + "_term_categorization_relationship"),
                                                                                 categorizationProperties,
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
     * @param parentCategoryGUID unique identifier for the parent category
     * @param childCategoryGUID unique identifier for the child category
     */
    public void addCategoryToCategory(String  parentCategoryGUID,
                                      String  childCategoryGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentCategoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(childCategoryGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CATEGORY_HIERARCHY_LINK_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentCategoryGUID + "_to_" + childCategoryGUID + "_category_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }




    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the child category
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     */
    public void addTermToContext(String  categoryGUID,
                                 String  termGUID,
                                 int     status,
                                 String  description)
    {
        final String methodName = "addTermToContext";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, STATUS_PROPERTY, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(USED_IN_CONTEXT_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + termGUID + "_used_in_context_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the child category
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     */
    public void addTermToCategory(String  categoryGUID,
                                  String  termGUID,
                                  int     status,
                                  String  description)
    {
        final String methodName = "addTermToCategory";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, STATUS_PROPERTY, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TERM_CATEGORIZATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + termGUID + "_term_categorization_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a category to an external glossary link with information on which category in the external glossary it corresponds to.
     *
     * @param categoryGUID unique identifier for the category
     * @param externalGlossaryLinkGUID unique identifier for the description of the external glossary (a type of external reference)
     */
    public void addLibraryCategoryReference(String categoryGUID,
                                            String externalGlossaryLinkGUID,
                                            String identifier,
                                            String description,
                                            String steward,
                                            Date   lastVerified)
    {
        final String methodName = "addLibraryCategoryReference";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(externalGlossaryLinkGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY, steward, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, LAST_VERIFIED_PROPERTY, lastVerified, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + externalGlossaryLinkGUID + "_library_category_reference_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }




    /**
     * Link a glossary term to an external glossary link with information on which term in the external glossary it corresponds to.
     *
     * @param termGUID unique identifier for the term
     * @param externalGlossaryLinkGUID unique identifier for the description of the external glossary (a type of external reference)
     */
    public void addLibraryTermReference(String termGUID,
                                        String externalGlossaryLinkGUID,
                                        String identifier,
                                        String description,
                                        String steward,
                                        Date   lastVerified)
    {
        final String methodName = "addLibraryTermReference";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(externalGlossaryLinkGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY, steward, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, LAST_VERIFIED_PROPERTY, lastVerified, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(termGUID + "_to_" + externalGlossaryLinkGUID + "_library_term_reference_relationship"),
                                                                     properties,
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
                                                                     idToGUIDMap.getGUID(describedElementId + "_to_" + describerElementId + "_more_information_relationship"),
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
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + termId + "_semantic_assignment_relationship"),
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
                                                                     idToGUIDMap.getGUID(specializedTermId + "_to_" + generalizedTermId + "_isatypeof_relationship"),
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
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_hasa_relationship"),
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
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_related_term_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
