/* SPDX-License-Identifier: Apache-2.0 */
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

    private static final String LOCATION_TYPE_NAME                       = "Location";
    private static final String FIXED_LOCATION_CLASSIFICATION_NAME       = "FixedLocation";
    private static final String SECURE_LOCATION_CLASSIFICATION_NAME      = "SecureLocation";
    private static final String CYBER_LOCATION_CLASSIFICATION_NAME       = "CyberLocation";
    private static final String MOBILE_ASSET_CLASSIFICATION_NAME         = "MobileAsset";
    private static final String NESTED_LOCATION_RELATIONSHIP_NAME        = "NestedLocation";
    private static final String ADJACENT_LOCATION_RELATIONSHIP_NAME      = "AdjacentLocation";
    private static final String ASSET_LOCATION_RELATIONSHIP_NAME         = "AssetLocation";

    private static final String COLLECTION_TYPE_NAME                     = "Collection";
    private static final String COLLECTION_MEMBER_RELATIONSHIP_NAME      = "CollectionMembership";
    public  static final String NAMING_STANDARD_RULE_SET_TYPE_NAME       = "NamingStandardRuleSet";

    public  static final String GOVERNANCE_DOMAIN_SET_CLASSIFICATION_NAME         = "GovernanceDomainSet";
    public  static final String GOVERNANCE_CLASSIFICATION_SET_CLASSIFICATION_NAME = "GovernanceClassificationSet";
    public  static final String GOVERNANCE_STATUS_SET_CLASSIFICATION_NAME         = "GovernanceStatusSet";

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

    private static final String GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME       = "GovernanceDomainDescription";

    private static final String GOVERNANCE_DEFINITION_TYPE_NAME               = "GovernanceDefinition";
      public static final String GOVERNANCE_DRIVER_TYPE_NAME                  = "GovernanceDriver";
      public static final String GOVERNANCE_STRATEGY_TYPE_NAME                = "GovernanceStrategy";
      public static final String BUSINESS_IMPERATIVE_TYPE_NAME                = "BusinessImperative";
      public static final String REGULATION_TYPE_NAME                         = "Regulation";
      public static final String REGULATION_ARTICLE_TYPE_NAME                 = "RegulationArticle";
      public static final String THREAT_TYPE_NAME                             = "Threat";
      public static final String GOVERNANCE_POLICY_TYPE_NAME                  = "GovernancePolicy";
      public static final String GOVERNANCE_PRINCIPLE_TYPE_NAME               = "GovernancePrinciple";
      public static final String GOVERNANCE_OBLIGATION_TYPE_NAME              = "GovernanceObligation";
      public static final String GOVERNANCE_APPROACH_TYPE_NAME                = "GovernanceApproach";
      public static final String GOVERNANCE_CONTROL_TYPE_NAME                 = "GovernanceControl";
      public static final String TECHNICAL_CONTROL_TYPE_NAME                  = "TechnicalControl";
      public static final String SECURITY_GROUP_TYPE_NAME                     = "SecurityGroup";
      public static final String GOVERNANCE_RULE_TYPE_NAME                    = "GovernanceRule";
      public static final String NAMING_STANDARD_RULE_TYPE_NAME               = "NamingStandardRule";
      public static final String GOVERNANCE_PROCESS_TYPE_NAME                 = "GovernanceProcess";
      public static final String ORGANIZATIONAL_CONTROL_TYPE_NAME             = "OrganizationalControl";
      public static final String GOVERNANCE_RESPONSIBILITY_TYPE_NAME          = "GovernanceResponsibility";
      public static final String GOVERNANCE_PROCEDURE_TYPE_NAME               = "GovernanceProcedure";
      public static final String LICENSE_TYPE_TYPE_NAME                       = "LicenseType";
      public static final String CERTIFICATION_TYPE_TYPE_NAME                 = "CertificationType";
      public static final String DATA_PROCESSING_PURPOSE_TYPE_NAME            = "DataProcessingPurpose";

    private static final String GOVERNANCE_DEFINITION_SCOPE_RELATIONSHIP_NAME = "GovernanceDefinitionScope";
    private static final String GOVERNED_BY_RELATIONSHIP_NAME                 = "GovernedBy";
    private static final String RESOURCE_LIST_RELATIONSHIP_NAME               = "ResourceList";

    public static final String GOVERNANCE_DRIVER_LINK_RELATIONSHIP_NAME      = "GovernanceDriverLink";
    public static final String GOVERNANCE_RESPONSE_RELATIONSHIP_NAME         = "GovernanceResponse";
    public static final String GOVERNANCE_POLICY_LINK_RELATIONSHIP_NAME      = "GovernanceDriverLink";
    public static final String GOVERNANCE_IMPLEMENTATION_RELATIONSHIP_NAME   = "GovernanceImplementation";
    public static final String GOVERNANCE_CONTROL_LINK_RELATIONSHIP_NAME     = "GovernanceControlLink";

    public  static final String GOVERNANCE_RULE_IMPLEMENTATION_RELATIONSHIP_NAME       = "GovernanceRuleImplementation";
    public  static final String GOVERNANCE_PROCESS_IMPLEMENTATION_RELATIONSHIP_NAME    = "GovernanceProcessImplementation";
    public  static final String GOVERNANCE_RESPONSIBILITY_ASSIGNMENT_RELATIONSHIP_NAME = "GovernanceResponsibilityAssignment";

    private static final String GOVERNANCE_ZONE_TYPE_NAME                     = "GovernanceZone";
    private static final String ZONE_HIERARCHY_RELATIONSHIP_NAME              = "ZoneHierarchy";
    private static final String ASSET_ZONE_MEMBERSHIP_CLASSIFICATION_NAME     = "AssetZoneMembership";

    private static final String BUSINESS_CAPABILITY_TYPE_NAME                 = "BusinessCapability";
    private static final String BUSINESS_CAPABILITY_TYPE_ENUM_NAME            = "BusinessCapabilityType";
    public  static final int       BUSINESS_CAPABILITY_TYPE_UNCLASSIFIED      = 0;
    public  static final int       BUSINESS_CAPABILITY_TYPE_BUSINESS_SERVICE  = 1;
    public  static final int       BUSINESS_CAPABILITY_TYPE_BUSINESS_AREA     = 2;
    public  static final int       BUSINESS_CAPABILITY_TYPE_OTHER             = 99;
    private static final String ORGANIZATIONAL_CAPABILITY_RELATIONSHIP_NAME   = "OrganizationalCapability";
    private static final String ASSET_ORIGIN_CLASSIFICATION_NAME              = "AssetOrigin";

    private static final String PROJECT_CHARTER_TYPE_NAME                = "ProjectCharter";
    private static final String PROJECT_CHARTER_LINK_RELATIONSHIP_NAME   = "ProjectCharterLink";

    private static final String OWNER_CLASSIFICATION_NAME                = "Owner";

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
    private static final String SYNONYM_RELATIONSHIP_TYPE_NAME           = "Synonym";
    private static final String SEMANTIC_ASSIGNMENT_TYPE_NAME            = "SemanticAssignment";
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

    private static final String PERSON_TYPE_NAME                         = "Person";
    private static final String TEAM_TYPE_NAME                           = "Team";
    public  static final String ORGANIZATION_TYPE_NAME                   = "Organization";
    private static final String IT_PROFILE_TYPE_NAME                     = "ITProfile";
    private static final String USER_IDENTITY_TYPE_NAME                  = "UserIdentity";
    private static final String CONTACT_DETAILS_TYPE_NAME                = "ContactDetails";

    private static final String SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION_NAME = "SecurityGroupMembership";
    private static final String SECURITY_TAGS_CLASSIFICATION_NAME             = "SecurityTags";

    private static final String CONTACT_METHOD_TYPE_ENUM_NAME             = "ContactMethodType";
    public  static final int       CONTACT_METHOD_TYPE_EMAIL              = 0;
    public  static final int       CONTACT_METHOD_TYPE_PHONE              = 1;
    public  static final int       CONTACT_METHOD_TYPE_CHAT               = 2;
    public  static final int       CONTACT_METHOD_TYPE_PROFILE            = 3;
    public  static final int       CONTACT_METHOD_TYPE_ACCOUNT            = 4;
    public  static final int       CONTACT_METHOD_TYPE_OTHER              = 99;

    private static final String PROFILE_LOCATION_RELATIONSHIP_NAME          = "ProfileLocation";
    private static final String PROFILE_IDENTITY_RELATIONSHIP_NAME          = "ProfileIdentity";
    private static final String CONTACT_THROUGH_RELATIONSHIP_NAME           = "ContactThrough";
    private static final String PEER_RELATIONSHIP_NAME                      = "Peer";
    private static final String PERSONAL_ROLE_APPOINTMENT_RELATIONSHIP_NAME = "PersonRoleAppointment";
    private static final String TEAM_STRUCTURE_RELATIONSHIP_NAME            = "TeamStructure";
    private static final String TEAM_MEMBERSHIP_RELATIONSHIP_NAME           = "TeamMembership";
    private static final String TEAM_LEADERSHIP_RELATIONSHIP_NAME           = "TeamLeadership";
    private static final String IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_NAME = "ITInfrastructureProfile";
    private static final String ASSIGNMENT_SCOPE_RELATIONSHIP_NAME          = "AssignmentScope";

    private static final String PERSON_ROLE_TYPE_NAME                    = "PersonRole";
    public  static final String TEAM_LEADER_TYPE_NAME                    = "TeamLeader";
    public  static final String TEAM_MEMBER_TYPE_NAME                    = "TeamMember";
    public  static final String PROJECT_MANAGER_TYPE_NAME                = "ProjectManager";
    public  static final String COMMUNITY_MEMBER_TYPE_NAME               = "CommunityMember";
    public  static final String NOTE_LOG_AUTHOR_TYPE_NAME                = "NoteLogAuthor";
    public  static final String GOVERNANCE_ROLE_TYPE_NAME                = "GovernanceRole";
    public  static final String GOVERNANCE_OFFICER_TYPE_NAME             = "GovernanceOfficer";
    public  static final String GOVERNANCE_REPRESENTATIVE_TYPE_NAME      = "GovernanceRepresentative";
    public  static final String LOCATION_OWNER_TYPE_NAME                 = "LocationOwner";
    public  static final String BUSINESS_OWNER_TYPE_NAME                 = "BusinessOwner";
    public  static final String SOLUTION_OWNER_TYPE_NAME                 = "SolutionOwner";
    public  static final String ASSET_OWNER_TYPE_NAME                    = "AssetOwner";
    public  static final String SUBJECT_AREA_OWNER_TYPE_NAME             = "SubjectAreaOwner";
    public  static final String COMPONENT_OWNER_TYPE_NAME                = "ComponentOwner";
    public  static final String DATA_ITEM_OWNER_TYPE_NAME                = "DataItemOwner";

    public  static final String PROJECT_TYPE_NAME                         = "Project";
    public  static final String CAMPAIGN_CLASSIFICATION_NAME              = "Campaign";
    public  static final String TASK_CLASSIFICATION_NAME                  = "Task";
    public  static final String GLOSSARY_PROJECT_CLASSIFICATION_NAME      = "GlossaryProject";
    public  static final String GOVERNANCE_PROJECT_CLASSIFICATION_NAME    = "GovernanceProject";
    private static final String PROJECT_HIERARCHY_RELATIONSHIP_NAME       = "ProjectHierarchy";
    private static final String PROJECT_DEPENDENCY_RELATIONSHIP_NAME      = "ProjectDependency";
    private static final String PROJECT_TEAM_RELATIONSHIP_NAME            = "ProjectTeam";
    private static final String PROJECT_MANAGEMENT_RELATIONSHIP_NAME      = "ProjectManagement";

    private static final String STAKEHOLDER_RELATIONSHIP_NAME             = "Stakeholder";

    public  static final String COMMUNITY_TYPE_NAME                      = "Community";
    private static final String COMMUNITY_MEMBERSHIP_RELATIONSHIP_NAME   = "CommunityMembership";
    private static final String COMMUNITY_MEMBERSHIP_TYPE_ENUM_NAME      = "CommunityMembershipType";
    public static final int      COMMUNITY_MEMBERSHIP_TYPE_CONTRIBUTOR   = 0;
    public static final int      COMMUNITY_MEMBERSHIP_TYPE_ADMINISTRATOR = 1;
    public static final int      COMMUNITY_MEMBERSHIP_TYPE_LEADER        = 2;
    public static final int      COMMUNITY_MEMBERSHIP_TYPE_OBSERVER      = 3;
    public static final int      COMMUNITY_MEMBERSHIP_TYPE_OTHER         = 99;

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
    private static final String PROCESS_TYPE_NAME                        = "Process";
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

    public static final String VALID_VALUE_DEFINITION_TYPE_NAME          = "ValidValueDefinition";
    public static final String VALID_VALUE_SET_TYPE_NAME                 = "ValidValuesSet";

    private static final String VALID_VALUE_MEMBER_TYPE_NAME             = "ValidValueMember";
    private static final String VALID_VALUES_ASSIGNMENT_TYPE_NAME        = "ValidValuesAssignment";
    private static final String VALID_VALUES_MAPPING_TYPE_NAME           = "ValidValuesMapping";
    private static final String VALID_VALUES_IMPLEMENTATION_TYPE_NAME    = "ValidValuesImplementation";
    private static final String REFERENCE_VALUE_ASSIGNMENT_TYPE_NAME     = "ReferenceValueAssignment";

    private static final String REFERENCE_DATA_CLASSIFICATION_NAME       = "ReferenceData";

    /*
     * Properties
     */
    protected static final String QUALIFIED_NAME_PROPERTY                    = "qualifiedName";
    protected static final String ADDITIONAL_PROPERTIES_PROPERTY             = "additionalProperties";

    protected static final String NAME_PROPERTY                              = "name";
    protected static final String VERSION_IDENTIFIER_PROPERTY                = "versionIdentifier";
    protected static final String DISPLAY_NAME_PROPERTY                      = "displayName";
    protected static final String DESCRIPTION_PROPERTY                       = "description";

    private static final String COORDINATES_PROPERTY                         = "coordinates";
    private static final String MAP_PROJECTION_PROPERTY                      = "mapProjection";
    private static final String POSTAL_ADDRESS_PROPERTY                      = "postalAddress";
    private static final String TIME_ZONE_PROPERTY                           = "timezone";
    private static final String LEVEL_PROPERTY                               = "level";

    private static final String ASSOCIATION_TYPE_PROPERTY                    = "associationType";
    private static final String CONTACT_TYPE_PROPERTY                        = "contactType";
    private static final String CONTACT_METHOD_TYPE_PROPERTY                 = "contactMethodType";
    private static final String CONTACT_METHOD_SERVICE_PROPERTY              = "contactMethodService";
    private static final String CONTACT_METHOD_VALUE_PROPERTY                = "contactMethodValue";
    private static final String DISTINGUISHED_NAME_PROPERTY                  = "distinguishedName";
    private static final String ROLE_TYPE_NAME_PROPERTY                      = "roleTypeName";
    private static final String ROLE_GUID_PROPERTY                           = "roleGUID";
    private static final String IS_PUBLIC_PROPERTY                           = "isPublic";
    private static final String HEAD_COUNT_PROPERTY                          = "headCount";
    private static final String PRONOUNS_PROPERTY                            = "pronouns";
    private static final String TITLE_PROPERTY                               = "title";
    private static final String INITIALS_PROPERTY                            = "initials";
    private static final String GIVEN_NAMES_PROPERTY                         = "givenNames";
    private static final String SURNAME_PROPERTY                             = "surname";
    private static final String FULL_NAME_PROPERTY                           = "fullName";
    private static final String JOB_TITLE_PROPERTY                           = "jobTitle";
    private static final String EMPLOYEE_NUMBER_PROPERTY                     = "employeeNumber";
    private static final String EMPLOYEE_TYPE_PROPERTY                       = "employeeType";
    private static final String PREFERRED_LANGUAGE_PROPERTY                  = "preferredLanguage";
    private static final String DELEGATION_ESCALATION_AUTHORITY_PROPERTY     = "delegationEscalationAuthority";
    private static final String TEAM_TYPE_PROPERTY                           = "teamType";
    private static final String ASSIGNMENT_TYPE_PROPERTY                     = "assignmentType";

    private static final String DEPENDENCY_SUMMARY_PROPERTY                  = "dependencySummary";
    private static final String TEAM_ROLE_PROPERTY                           = "teamRole";
    private static final String START_DATE_PROPERTY                          = "startDate";
    private static final String PLANNED_END_DATE_PROPERTY                    = "plannedEndDate";
    private static final String PROJECT_STATUS_PROPERTY                      = "projectStatus";

    private static final String MISSION_PROPERTY                             = "mission";
    private static final String MEMBERSHIP_TYPE_PROPERTY                     = "membershipType";
    private static final String MEMBERSHIP_RATIONALE_PROPERTY                = "membershipRationale";
    private static final String PROJECT_TYPE_PROPERTY                        = "projectType";
    private static final String PURPOSES_PROPERTY                            = "purposes";
    private static final String DETAILS_PROPERTY                             = "details";
    private static final String RESOURCE_USE_PROPERTY                        = "resourceUse";
    private static final String WATCH_RESOURCE_PROPERTY                      = "watchResource";

    private static final String GROUPS_PROPERTY                              = "groups";
    private static final String SECURITY_LABELS_PROPERTY                     = "securityLabels";
    private static final String SECURITY_PROPERTIES_PROPERTY                 = "securityProperties";
    private static final String ACCESS_GROUPS_PROPERTY                       = "accessGroups";

    private static final String IDENTIFIER_PROPERTY                          = "identifier";

    private static final String ORGANIZATION_PROPERTY                        = "organization";
    private static final String ORGANIZATION_PROPERTY_NAME_PROPERTY          = "organizationPropertyName";
    private static final String BUSINESS_CAPABILITY_PROPERTY                 = "businessCapability";
    private static final String BUSINESS_CAPABILITY_TYPE_PROPERTY            = "businessCapabilityType";
    private static final String BUSINESS_CAPABILITY_PROPERTY_NAME_PROPERTY   = "businessCapabilityPropertyName";
    private static final String OTHER_ORIGIN_VALUES_PROPERTY                 = "otherOriginValues";

    private static final String OWNER_PROPERTY                               = "owner";
    private static final String OWNER_TYPE_NAME_PROPERTY                     = "ownerTypeName";
    private static final String OWNER_PROPERTY_NAME_PROPERTY                 = "ownerPropertyName";

    private static final String ATTRIBUTE_NAME_PROPERTY                      = "attributeName";
    private static final String TECHNICAL_NAME_PROPERTY                      = "technicalName";
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
    private static final String ZONE_NAME_PROPERTY                           = "zoneName";
    private static final String SUBJECT_AREA_NAME_PROPERTY                   = "subjectAreaName";
    public  static final String DOMAIN_IDENTIFIER_PROPERTY                   = "domainIdentifier";
    private static final String PRIORITY_PROPERTY                            = "priority";
    private static final String IMPLICATIONS_PROPERTY                        = "implications";
    private static final String OUTCOMES_PROPERTY                            = "outcomes";
    private static final String RESULTS_PROPERTY                             = "results";
    private static final String JURISDICTION_PROPERTY                        = "jurisdiction";
    private static final String RATIONALE_PROPERTY                           = "rationale";

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
    private static final String PATH_PROPERTY                                = "path";
    private static final String COMMAND_PROPERTY                             = "command";
    private static final String SCOPE_PROPERTY                               = "scope";
    private static final String CRITERIA_PROPERTY                            = "criteria";
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

    private static final String SUMMARY_PROPERTY                             = "summary";
    private static final String EXAMPLES_PROPERTY                            = "examples";
    private static final String ABBREVIATION_PROPERTY                        = "abbreviation";
    private static final String LANGUAGE_PROPERTY                            = "language";
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

    private static final String STRICT_REQUIREMENT_PROPERTY                  = "strictRequirement";
    private static final String PREFERRED_VALUE_PROPERTY                     = "preferredValue";
    private static final String SYMBOLIC_NAME_PROPERTY                       = "symbolicName";
    private static final String IMPLEMENTATION_VALUE_PROPERTY                = "implementationValue";
    private static final String ADDITIONAL_VALUES_PROPERTY                   = "additionalValues";
    private static final String ASSOCIATION_DESCRIPTION_PROPERTY             = "associationDescription";
    private static final String IS_DEFAULT_VALUE_PROPERTY                    = "isDefaultValue";


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
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     archiveRootName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName)
    {
        this(archiveBuilder,
             archiveGUID,
             archiveName,
             originatorName,
             creationDate,
             versionNumber,
             versionName,
             archiveRootName + guidMapFileNamePostFix);
    }


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param instanceProvenanceType type of archive.
     * @param license license for the archive contents.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     archiveRootName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      InstanceProvenanceType     instanceProvenanceType,
                                      String                     license)
    {
        this(archiveBuilder,
             archiveGUID,
             archiveName,
             originatorName,
             creationDate,
             versionNumber,
             versionName,
             instanceProvenanceType,
             license,
             archiveRootName + guidMapFileNamePostFix);
    }


    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param guidMapFileName name of the guid map file.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      String                     guidMapFileName)
    {
        this(archiveBuilder, archiveGUID, archiveName, originatorName, creationDate, versionNumber, versionName, InstanceProvenanceType.CONTENT_PACK, null, guidMapFileName);
    }


    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param instanceProvenanceType type of archive.
     * @param license license for the archive contents.
     * @param guidMapFileName name of the guid map file.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      InstanceProvenanceType     instanceProvenanceType,
                                      String                     license,
                                      String                     guidMapFileName)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   archiveName,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName,
                                                   instanceProvenanceType,
                                                   license);

        this.idToGUIDMap = new OMRSArchiveGUIDMap(guidMapFileName);

        this.archiveRootName = archiveName;
        this.originatorName = originatorName;
        this.versionName = versionName;

        this.activeStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, 1);
    }


    /**
     * Return the guid of an element based on its qualified name.  This is a lookup in the GUID map not the archive.
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
     * Return the guid of an element based on its qualified name.  This is a query in the GUID map not the archive.
     * This means if the qualified name is not known, null is returned.
     *
     * @param qualifiedName qualified name ot look up
     * @return guid or null
     */
    public String queryGUID(String qualifiedName)
    {
        return idToGUIDMap.queryGUID(qualifiedName);
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
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, KEYWORD_PROPERTY, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(SEARCH_KEYWORD_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(SEARCH_KEYWORD_TYPE_NAME + ":" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(externalReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(SEARCH_KEYWORD_LINK_RELATIONSHIP_NAME,
                                                                                     idToGUIDMap.getGUID(externalReferenceEntity.getGUID() + "_to_" + keywordEntity.getGUID() + "_search_keyword_link_relationship"),
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
     * Create the relationship between a referenceable and an external reference.
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(EXTERNAL_REFERENCE_LINK_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + externalReferenceGUID + "_external_reference_link_relationship" + referenceId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a media reference entity.  This typically describes an image, audio clip or video clip.
     *
     * @param typeName name of element subtype to use - default is RelatedMedia
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param description description about the element
     * @param authors authors of the element
     * @param authorOrganization organization that the information is from
     * @param versionNumber version number for the element
     * @param referenceURL link to the external source
     * @param license name of the license associated with this external source
     * @param copyright copyright statement associated with this external source
     * @param attribution attribution statement to use when consuming this external source
     * @param mediaType type of media
     * @param mediaTypeOtherId if media type is "other" add type here
     * @param defaultMediaUsage usage of media if not supplied on media reference link relationship
     * @param defaultMediaUsageOtherId if default media usage is "other" add usage type here
     * @param searchKeywords list of keywords
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @return unique identifier for new media reference (mediaReferenceGUID)
     */
    public String addMediaReference(String               typeName,
                                    String               qualifiedName,
                                    String               displayName,
                                    String               description,
                                    List<String>         authors,
                                    String               authorOrganization,
                                    String               versionNumber,
                                    String               referenceURL,
                                    String               license,
                                    String               copyright,
                                    String               attribution,
                                    int                  mediaType,
                                    String               mediaTypeOtherId,
                                    int                  defaultMediaUsage,
                                    String               defaultMediaUsageOtherId,
                                    List<String>         searchKeywords,
                                    Map<String, String>  additionalProperties,
                                    Map<String, Object>  extendedProperties)
    {
        final String methodName = "addMediaReference";

        String elementTypeName = RELATED_MEDIA_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        EnumElementDef typeEnumElement = archiveHelper.getEnumElement(MEDIA_TYPE_ENUM_NAME, mediaType);
        EnumElementDef usageEnumElement = archiveHelper.getEnumElement(MEDIA_USAGE_ENUM_NAME, defaultMediaUsage);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, AUTHORS_PROPERTY, authors, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ORGANIZATION_PROPERTY, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, REFERENCE_VERSION_PROPERTY, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, URL_PROPERTY, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, LICENSE_PROPERTY, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, COPYRIGHT_PROPERTY, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ATTRIBUTION_PROPERTY, attribution, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, MEDIA_TYPE_PROPERTY, typeEnumElement.getOrdinal(), typeEnumElement.getValue(), typeEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, MEDIA_TYPE_OTHER_ID_PROPERTY, mediaTypeOtherId, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, DEFAULT_MEDIA_USAGE_PROPERTY, usageEnumElement.getOrdinal(), usageEnumElement.getValue(), usageEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DEFAULT_MEDIA_USAGE_OTHER_ID_PROPERTY, defaultMediaUsageOtherId, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail mediaReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                          idToGUIDMap.getGUID(qualifiedName),
                                                                          properties,
                                                                          InstanceStatus.ACTIVE,
                                                                          null);

        archiveBuilder.addEntity(mediaReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(SEARCH_KEYWORD_TYPE_NAME + ":" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, KEYWORD_PROPERTY, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(SEARCH_KEYWORD_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(SEARCH_KEYWORD_TYPE_NAME + ":" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(mediaReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(SEARCH_KEYWORD_LINK_RELATIONSHIP_NAME,
                                                                                     idToGUIDMap.getGUID(mediaReferenceEntity.getGUID() + "_to_" + keywordEntity.getGUID() + "_search_keyword_link_relationship"),
                                                                                     null,
                                                                                     InstanceStatus.ACTIVE,
                                                                                     end1,
                                                                                     end2));
                    }
                }
            }
        }

        return mediaReferenceEntity.getGUID();
    }


    /**
     * Create the relationship between a referenceable and an external reference.
     *
     * @param referenceableGUID unique identifier of the element making the reference
     * @param mediaReferenceGUID unique identifier of the media reference
     * @param mediaId unique reference id for this media element
     * @param description description of the relevance of the media reference
     * @param mediaUsage type of usage
     * @param mediaUsageOtherId other type of media usage (for example if using a valid value set).
     */
    public void addMediaReferenceLink(String referenceableGUID,
                                      String mediaReferenceGUID,
                                      String mediaId,
                                      String description,
                                      int    mediaUsage,
                                      String mediaUsageOtherId)
    {
        final String methodName = "addMediaReferenceLink";

        EnumElementDef enumElement = archiveHelper.getEnumElement(MEDIA_USAGE_ENUM_NAME, mediaUsage);

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(mediaReferenceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, MEDIA_ID_PROPERTY, mediaId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, MEDIA_USAGE_PROPERTY, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, MEDIA_TYPE_OTHER_ID_PROPERTY, mediaUsageOtherId, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(MEDIA_REFERENCE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + mediaReferenceGUID + "_media_reference_relationship" + mediaId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the DeployedOn relationship to the archive.
     *
     * @param deployedElementQName qualified name of element being deployed
     * @param deployedOnQName qualified name of target
     * @param deploymentTime time of the deployment
     * @param deployerTypeName type name of the element representing the deployer
     * @param deployerPropertyName property name used to identify the deployer
     * @param deployer identifier of the deployer
     * @param deploymentStatus status of the deployment
     */
    public void addDeployedOnRelationship(String deployedElementQName,
                                          String deployedOnQName,
                                          Date   deploymentTime,
                                          String deployerTypeName,
                                          String deployerPropertyName,
                                          String deployer,
                                          int    deploymentStatus)
    {
        final String methodName = "addDeployedOnRelationship";
        final String operationStatus = "OperationalStatus";

        String deployedElementId = this.idToGUIDMap.getGUID(deployedElementQName);
        String deployedOnId = this.idToGUIDMap.getGUID(deployedOnQName);

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedElementId));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedOnId));

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(operationStatus, deploymentStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, "deploymentTime", deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerTypeName", deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployerPropertyName", deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, "deployer", deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, operationStatus, statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship("DeployedOn", this.idToGUIDMap.getGUID(deployedElementId + "_to_" + deployedOnId + "_deployed_on_relationship"), properties, InstanceStatus.ACTIVE, end1, end2));
    }

    /**
     * Add a location entity to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    public String addLocation(String              qualifiedName,
                              String              identifier,
                              String              displayName,
                              String              description,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addLocation";

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, null, null, methodName);
    }


    /**
     * Add a location with a FixedLocation classification.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param coordinates location coordinates
     * @param mapProjection type of location coordinates
     * @param postalAddress full postal address
     * @param timeZone timezone of location
     * @param additionalProperties any additional properties
     * @return unique identifier of the new location
     */
    public String addFixedLocation(String              qualifiedName,
                                   String              identifier,
                                   String              displayName,
                                   String              description,
                                   String              coordinates,
                                   String              mapProjection,
                                   String              postalAddress,
                                   String              timeZone,
                                   Map<String, String> additionalProperties)
    {
        final String methodName = "addFixedLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, COORDINATES_PROPERTY, coordinates, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, MAP_PROJECTION_PROPERTY, mapProjection, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, POSTAL_ADDRESS_PROPERTY, postalAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TIME_ZONE_PROPERTY, timeZone, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, FIXED_LOCATION_CLASSIFICATION_NAME, properties, methodName);
    }


    /**
     * Add a location with a SecureLocation classification.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param level level of security
     * @param securityDescription description of security provision
     * @param additionalProperties additional properties for the location
     * @return unique identifier of the new location
     */
    public String addSecureLocation(String              qualifiedName,
                                    String              identifier,
                                    String              displayName,
                                    String              description,
                                    String              level,
                                    String              securityDescription,
                                    Map<String, String> additionalProperties)
    {
        final String methodName = "addSecureLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, LEVEL_PROPERTY, level, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, securityDescription, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, SECURE_LOCATION_CLASSIFICATION_NAME, properties, methodName);
    }


    /**
     * Add a location entity with the CyberLocation classification to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param networkAddress address of the cyber location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    public String addCyberLocation(String              qualifiedName,
                                   String              identifier,
                                   String              displayName,
                                   String              description,
                                   String              networkAddress,
                                   Map<String, String> additionalProperties)
    {
        final String methodName = "addCyberLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, NETWORK_ADDRESS_PROPERTY, networkAddress, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, CYBER_LOCATION_CLASSIFICATION_NAME, properties, methodName);
    }


    /**
     * Add a location entity to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    private String addClassifiedLocation(String              qualifiedName,
                                         String              identifier,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         String              classificationName,
                                         InstanceProperties  classificationProperties,
                                         String              methodName)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        Classification classification = archiveHelper.getClassification(classificationName, classificationProperties, InstanceStatus.ACTIVE);

        classifications.add(classification);

        EntityDetail location = archiveHelper.getEntityDetail(LOCATION_TYPE_NAME,
                                                              idToGUIDMap.getGUID(qualifiedName),
                                                              properties,
                                                              InstanceStatus.ACTIVE,
                                                              classifications);

        archiveBuilder.addEntity(location);

        return location.getGUID();
    }


    /**
     * Add the MobileAsset classification to the requested asset.
     *
     * @param assetGUID unique identifier of the element to classify
     */
    public void addMobileAssetClassification(String assetGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(MOBILE_ASSET_CLASSIFICATION_NAME,
                                                                         null,
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }


    /**
     * Create the relationship between a locations and one of the locations nested within it.
     *
     * @param broaderLocationGUID unique identifier of the broader location
     * @param nestedLocationGUID unique identifier of the nested location
     */
    public void addLocationHierarchy(String broaderLocationGUID,
                                     String nestedLocationGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(broaderLocationGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(nestedLocationGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(NESTED_LOCATION_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(broaderLocationGUID + "_to_" + nestedLocationGUID + "_nested_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two locations as peers using the AdjacentLocation relationship.
     *
     * @param location1GUID unique identifier of the broader location
     * @param location2GUID unique identifier of the nested location
     */
    public void addPeerLocations(String location1GUID,
                                 String location2GUID)
    {
        EntityDetail referenceableEntity = archiveBuilder.getEntity(location1GUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(location2GUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(ADJACENT_LOCATION_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(location1GUID + "_to_" + location2GUID + "_adjacent_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a location to an asset.
     *
     * @param locationQName qualified name of the location
     * @param assetQName qualified name of the asset
     */
    public void addAssetLocationRelationship(String locationQName,
                                             String assetQName)
    {
        String validValueId = idToGUIDMap.getGUID(locationQName);
        String assetId = idToGUIDMap.getGUID(assetQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(assetId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(ASSET_LOCATION_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(validValueId + "_to_" + assetId + "_asset_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Add a user identity entity to the archive.
     *
     * @param qualifiedName unique name of the user identity
     * @param userId  name of the user account
     * @param distinguishedName LDAP name for the user
     * @param additionalProperties any additional properties
     * @return unique identifier of the user identity
     */
    public String addUserIdentity(String              qualifiedName,
                                  String              userId,
                                  String              distinguishedName,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addUserIdentity";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USER_ID_PROPERTY, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISTINGUISHED_NAME_PROPERTY, distinguishedName, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail userIdentity = archiveHelper.getEntityDetail(USER_IDENTITY_TYPE_NAME,
                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                  properties,
                                                                  InstanceStatus.ACTIVE,
                                                                  null);

        archiveBuilder.addEntity(userIdentity);

        return userIdentity.getGUID();
    }


    /**
     * Link a location to a profile.
     *
     * @param profileQName qualified name of the profile
     * @param locationQName qualified name of the location
     * @param associationType identifier that describes the purpose of the association.
     */
    public void addProfileLocationRelationship(String profileQName,
                                               String locationQName,
                                               String associationType)
    {
        final String methodName = "addProfileLocationRelationship";

        String entity1GUID = idToGUIDMap.getGUID(locationQName);
        String entity2GUID = idToGUIDMap.getGUID(profileQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(entity1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(entity2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ASSOCIATION_TYPE_PROPERTY, associationType, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROFILE_LOCATION_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(entity1GUID + "_to_" + entity2GUID + "_profile_location_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a profile and one of its userIds.
     *
     * @param profileGUID unique identifier of the actor profile
     * @param userIdentityGUID unique identifier of the user identity
     * @param roleTypeName type of role that uses this userId
     * @param roleGUID unique identifier of role that uses this userId
     * @param description description of why role uses this userId
     */
    public void addProfileIdentity(String profileGUID,
                                   String userIdentityGUID,
                                   String roleTypeName,
                                   String roleGUID,
                                   String description)
    {
        final String methodName = "addProfileIdentity";

        EntityDetail profileEntity = archiveBuilder.getEntity(profileGUID);
        EntityDetail userIdentityEntity = archiveBuilder.getEntity(userIdentityGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(profileEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(userIdentityEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ROLE_TYPE_NAME_PROPERTY, roleTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ROLE_GUID_PROPERTY, roleGUID, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROFILE_IDENTITY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(profileGUID + "_to_" + userIdentityGUID + "_profile_identity_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a contact method to a profile.
     *
     * @param profileGUID unique identifier for a profile
     * @param name name of the contact method
     * @param contactType type of contact - eg home address, work mobile, emergency contact ...
     * @param contactMethodType type of contact address
     * @param contactMethodService service used in the contact method
     * @param contactMethodValue name/account/url used to contact the individual
     */
    public  void addContactDetails(String  profileGUID,
                                   String  name,
                                   String  contactType,
                                   int     contactMethodType,
                                   String  contactMethodService,
                                   String  contactMethodValue)
    {
        final String methodName = "addContactDetails";

        EntityDetail profileEntity = archiveBuilder.getEntity(profileGUID);

        EnumElementDef enumElement = archiveHelper.getEnumElement(CONTACT_METHOD_TYPE_ENUM_NAME, contactMethodType);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONTACT_TYPE_PROPERTY, contactType, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, CONTACT_METHOD_TYPE_PROPERTY, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONTACT_METHOD_SERVICE_PROPERTY, contactMethodService, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONTACT_METHOD_VALUE_PROPERTY, contactMethodValue, methodName);

        EntityDetail contactDetails = archiveHelper.getEntityDetail(CONTACT_DETAILS_TYPE_NAME,
                                                                    idToGUIDMap.getGUID(contactMethodValue),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    null);

        archiveBuilder.addEntity(contactDetails);

        EntityProxy end1 = archiveHelper.getEntityProxy(profileEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(contactDetails);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(CONTACT_THROUGH_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(profileGUID + "_to_" + contactDetails.getGUID() + "_contact_through_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new person role.
     *
     * @param suppliedTypeName type name to use for the person role
     * @param qualifiedName qualified name of role
     * @param identifier unique code
     * @param name display name
     * @param description description (eg job description)
     * @param scope scope of role's responsibilities
     * @param setHeadCount should the headcount field be set?
     * @param headCount number of people that may be appointed to the role (default = 1)
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addPersonRole(String              suppliedTypeName,
                                 String              qualifiedName,
                                 String              identifier,
                                 String              name,
                                 String              description,
                                 String              scope,
                                 boolean             setHeadCount,
                                 int                 headCount,
                                 Map<String, String> additionalProperties,
                                 Map<String, Object> extendedProperties)
    {
        final String methodName = "addPersonRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = PERSON_ROLE_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, HEAD_COUNT_PROPERTY, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail role = archiveHelper.getEntityDetail(typeName,
                                                          idToGUIDMap.getGUID(qualifiedName),
                                                          properties,
                                                          InstanceStatus.ACTIVE,
                                                          null);

        archiveBuilder.addEntity(role);

        return role.getGUID();
    }



    /**
     * Add a new person role.
     *
     * @param suppliedTypeName type name to use for the person role
     * @param qualifiedName qualified name of role
     * @param domainIdentifier identifier of governance domain
     * @param identifier unique code
     * @param name display name
     * @param description description (eg job description)
     * @param scope scope of role's responsibilities
     * @param setHeadCount should the headcount field be set?
     * @param headCount number of people that may be appointed to the role (default = 1)
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addGovernanceRole(String              suppliedTypeName,
                                     String              qualifiedName,
                                     int                 domainIdentifier,
                                     String              identifier,
                                     String              name,
                                     String              description,
                                     String              scope,
                                     boolean             setHeadCount,
                                     int                 headCount,
                                     Map<String, String> additionalProperties,
                                     Map<String, Object> extendedProperties)
    {
        final String methodName = "addPersonRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = PERSON_ROLE_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, DOMAIN_IDENTIFIER_PROPERTY, domainIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, HEAD_COUNT_PROPERTY, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail role = archiveHelper.getEntityDetail(typeName,
                                                          idToGUIDMap.getGUID(qualifiedName),
                                                          properties,
                                                          InstanceStatus.ACTIVE,
                                                          null);

        archiveBuilder.addEntity(role);

        return role.getGUID();
    }


    /**
     * Link a person profile to a person role.
     *
     * @param personQName qualified name of the person profile
     * @param personRoleQName qualified name of the person role
     * @param isPublic is this appointment public?
     */
    public void addPersonRoleAppointmentRelationship(String  personQName,
                                                     String  personRoleQName,
                                                     boolean isPublic)
    {
        final String methodName = "addPersonRoleAppointmentRelationship";

        String guid1 = idToGUIDMap.getGUID(personQName);
        String guid2 = idToGUIDMap.getGUID(personRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, IS_PUBLIC_PROPERTY, isPublic, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PERSONAL_ROLE_APPOINTMENT_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_person_role_appointment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person profile to another person as a peer.
     *
     * @param person1QName qualified name of the first person profile
     * @param person2QName qualified name of the second person profile
     */
    public void addPeerRelationship(String  person1QName,
                                    String  person2QName)
    {
        String guid1 = idToGUIDMap.getGUID(person1QName);
        String guid2 = idToGUIDMap.getGUID(person2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PEER_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_peer_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new person profile.
     *
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param pronouns preferred pronouns
     * @param description description (eg job description)
     * @param title courtesy title
     * @param initials given names initials
     * @param givenNames list of given names
     * @param surname family name
     * @param fullName full legal name
     * @param jobTitle job title
     * @param employeeNumber unique employee contract number
     * @param employeeType type of employee
     * @param preferredLanguage preferred language to communicate with
     * @param isPublic is this profile public
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addPerson(String              qualifiedName,
                             String              name,
                             String              pronouns,
                             String              description,
                             String              initials,
                             String              title,
                             String              givenNames,
                             String              surname,
                             String              fullName,
                             String              jobTitle,
                             String              employeeNumber,
                             String              employeeType,
                             String              preferredLanguage,
                             boolean             isPublic,
                             Map<String, String> additionalProperties)
    {
        final String methodName = "addPerson";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PRONOUNS_PROPERTY, pronouns, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TITLE_PROPERTY, title, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, INITIALS_PROPERTY, initials, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, GIVEN_NAMES_PROPERTY, givenNames, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SURNAME_PROPERTY, surname, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, FULL_NAME_PROPERTY, fullName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, JOB_TITLE_PROPERTY, jobTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EMPLOYEE_NUMBER_PROPERTY, employeeNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EMPLOYEE_TYPE_PROPERTY, employeeType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PREFERRED_LANGUAGE_PROPERTY, preferredLanguage, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, IS_PUBLIC_PROPERTY, isPublic, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail person = archiveHelper.getEntityDetail(PERSON_TYPE_NAME,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(person);

        return person.getGUID();
    }


    /**
     * Add a new team profile.
     *
     * @param suppliedTypeName type name for the team
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param description description (eg job description)
     * @param teamType type of team
     * @param identifier code value identifier for the team
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addTeam(String              suppliedTypeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           String              teamType,
                           String              identifier,
                           Map<String, String> additionalProperties)
    {
        final String methodName = "addTeam";

        String typeName = suppliedTypeName;
        if (typeName == null)
        {
            typeName = TEAM_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TEAM_TYPE_PROPERTY, teamType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, teamType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail profile = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(profile);

        return profile.getGUID();
    }


    /**
     * Link a person role as a team's leader.
     *
     * @param personRoleQName qualified name of the person role
     * @param teamQName qualified name of the team profile
     * @param position position of the role
     */
    public void addTeamLeadershipRelationship(String personRoleQName,
                                              String teamQName,
                                              String position)
    {
        final String methodName = "addTeamLeadershipRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(teamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, POSITION_PROPERTY, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TEAM_LEADERSHIP_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_leadership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person role as a team's member.
     *
     * @param personRoleQName qualified name of the person role
     * @param teamQName qualified name of the team profile
     * @param position position of the role
     */
    public void addTeamMembershipRelationship(String personRoleQName,
                                              String teamQName,
                                              String position)
    {
        final String methodName = "addTeamMembershipRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(teamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, POSITION_PROPERTY, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TEAM_MEMBERSHIP_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person role as a team's member.
     *
     * @param superTeamQName qualified name of the super team profile
     * @param subTeamQName qualified name of the subteam profile
     * @param delegationEscalationAuthority delegationEscalationAuthority of the role
     */
    public void addTeamStructureRelationship(String  superTeamQName,
                                             String  subTeamQName,
                                             boolean delegationEscalationAuthority)
    {
        final String methodName = "addTeamStructureRelationship";

        String guid1 = idToGUIDMap.getGUID(superTeamQName);
        String guid2 = idToGUIDMap.getGUID(subTeamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, DELEGATION_ESCALATION_AUTHORITY_PROPERTY, delegationEscalationAuthority, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(TEAM_STRUCTURE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_structure_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new IT profile.
     *
     * @param assetGUID unique identifier of asset to connect the profile to.
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param description description (eg job description)
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addITProfileToAsset(String              assetGUID,
                                       String              qualifiedName,
                                       String              name,
                                       String              description,
                                       Map<String, String> additionalProperties)
    {
        final String methodName = "addITProfile";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail profile = archiveHelper.getEntityDetail(IT_PROFILE_TYPE_NAME,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(profile);

        if (assetGUID != null)
        {
            EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(profile);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_NAME,
                                                                         idToGUIDMap.getGUID(assetGUID + "_to_" + profile.getGUID() + "_it_infrastructure_profile_relationship"),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return profile.getGUID();
    }



    /**
     * Link an actor (profile/person role) to a scope.
     *
     * @param actorQName qualified name of the actor
     * @param scopeQName qualified name of the scope
     * @param assignmentType type of the assignment
     * @param description description of the assignment
     */
    public void addAssignmentScopeRelationship(String actorQName,
                                               String scopeQName,
                                               String assignmentType,
                                               String description)
    {
        final String methodName = "addAssignmentScopeRelationship";

        String guid1 = idToGUIDMap.getGUID(actorQName);
        String guid2 = idToGUIDMap.getGUID(scopeQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ASSIGNMENT_TYPE_PROPERTY, assignmentType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(ASSIGNMENT_SCOPE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new project.
     *
     * @param suppliedTypeName subtype information
     * @param qualifiedName qualified name of project
     * @param identifier unique identifier of project - typically allocated externally
     * @param name display name
     * @param description description
     * @param startDate date the project started
     * @param plannedEndDate date the project is expected to end
     * @param projectStatus status of the project
     * @param setCampaignClassification should the Campaign classification be set?
     * @param setTaskClassification should the Task classification be set?
     * @param projectTypeClassification add special classification that defines the type of project - eg GlossaryProject or GovernanceProject
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addProject(String              suppliedTypeName,
                              String              qualifiedName,
                              String              identifier,
                              String              name,
                              String              description,
                              Date                startDate,
                              Date                plannedEndDate,
                              String              projectStatus,
                              boolean             setCampaignClassification,
                              boolean             setTaskClassification,
                              String              projectTypeClassification,
                              Map<String, String> additionalProperties,
                              Map<String, Object> extendedProperties)
    {
        final String methodName = "addProject";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = PROJECT_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, START_DATE_PROPERTY, startDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, PLANNED_END_DATE_PROPERTY, plannedEndDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PROJECT_STATUS_PROPERTY, projectStatus, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (setCampaignClassification)
        {
            Classification classification = archiveHelper.getClassification(CAMPAIGN_CLASSIFICATION_NAME, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }
        if (setTaskClassification)
        {
            Classification classification = archiveHelper.getClassification(TASK_CLASSIFICATION_NAME, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }
        if (projectTypeClassification != null)
        {
            Classification classification = archiveHelper.getClassification(projectTypeClassification, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        EntityDetail project = archiveHelper.getEntityDetail(typeName,
                                                             idToGUIDMap.getGUID(qualifiedName),
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             classifications);

        archiveBuilder.addEntity(project);

        return project.getGUID();
    }


    /**
     * Link a project to a subproject.
     *
     * @param projectQName qualified name of the parent project
     * @param subprojectQName qualified name of the subproject
     */
    public void addProjectHierarchyRelationship(String  projectQName,
                                                String  subprojectQName)
    {
        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(subprojectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROJECT_HIERARCHY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable to its stakeholders.
     *
     * @param referenceableQName qualified name of the parent project
     * @param actorQName qualified name of the subproject
     */
    public void addStakeHolderRelationship(String  referenceableQName,
                                           String  actorQName)
    {
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(actorQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(STAKEHOLDER_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_stakeholder_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a project to another project that it depends on.
     *
     * @param projectQName qualified name of the project
     * @param dependsOnProjectQName qualified name of the project that it depends on
     * @param dependencySummary description of what makes them dependent
     */
    public void addProjectDependencyRelationship(String  projectQName,
                                                 String  dependsOnProjectQName,
                                                 String  dependencySummary)
    {
        final String methodName = "addProjectDependencyRelationship";

        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(dependsOnProjectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, DEPENDENCY_SUMMARY_PROPERTY, dependencySummary, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROJECT_DEPENDENCY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_dependency_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link a project to a team.  A project may have multiple teams.
     *
     * @param personQName qualified name of the person profile
     * @param personRoleQName qualified name of the person role
     * @param teamRole role of this team in the project
     */
    public void addProjectTeamRelationship(String  personQName,
                                           String  personRoleQName,
                                           String  teamRole)
    {
        final String methodName = "addProjectTeamRelationship";

        String guid1 = idToGUIDMap.getGUID(personQName);
        String guid2 = idToGUIDMap.getGUID(personRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, TEAM_ROLE_PROPERTY, teamRole, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROJECT_TEAM_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_team_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a project to a person role that represents the project manager(s).
     *
     * @param projectQName qualified name of the project
     * @param projectManagerRoleQName qualified name of the person role
     */
    public void addProjectManagementRelationship(String  projectQName,
                                                 String  projectManagerRoleQName)
    {
        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(projectManagerRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(PROJECT_MANAGEMENT_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_management_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new community.
     *
     * @param suppliedTypeName subtype information
     * @param qualifiedName qualified name of community
     * @param name display name
     * @param description description
     * @param mission why is the community formed?
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addCommunity(String              suppliedTypeName,
                                String              qualifiedName,
                                String              name,
                                String              description,
                                String              mission,
                                Map<String, String> additionalProperties,
                                Map<String, Object> extendedProperties)
    {
        final String methodName = "addCommunity";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = COMMUNITY_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, MISSION_PROPERTY, mission, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail project = archiveHelper.getEntityDetail(typeName,
                                                             idToGUIDMap.getGUID(qualifiedName),
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             null);

        archiveBuilder.addEntity(project);

        return project.getGUID();
    }


    /**
     * Link a community to a person role.
     *
     * @param communityQName qualified name of the community
     * @param membershipRoleQName qualified name of the membership role
     * @param membershipType ordinal of enum
     */
    public void addCommunityMembershipRelationship(String  communityQName,
                                                   String  membershipRoleQName,
                                                   int     membershipType)
    {
        final String methodName = "addCommunityMembershipRelationship";

        EnumElementDef enumElement = archiveHelper.getEnumElement(COMMUNITY_MEMBERSHIP_TYPE_ENUM_NAME, membershipType);

        String guid1 = idToGUIDMap.getGUID(communityQName);
        String guid2 = idToGUIDMap.getGUID(membershipRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, MEMBERSHIP_TYPE_PROPERTY, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(COMMUNITY_MEMBERSHIP_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_community_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a governance definition entity.
     *
     * @param suppliedTypeName type of collection
     * @param classificationName name of classification to attach
     * @param qualifiedName unique name for the collection entity
     * @param displayName display name for the collection
     * @param description description about the collection
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for subject area (collectionGUID)
     */
    public String addCollection(String               suppliedTypeName,
                                String               classificationName,
                                String               qualifiedName,
                                String               displayName,
                                String               description,
                                Map<String, String>  additionalProperties,
                                Map<String, Object>  extendedProperties)
    {
        final String methodName = "addCollection";

        String typeName = COLLECTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        List<Classification> classifications = null;

        if (classificationName != null)
        {
            classifications = new ArrayList<>();

            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add a member to a collection.
     *
     * @param collectionGUID unique identifier of the collection
     * @param memberGUID unique identifier of the member
     * @param membershipRationale why is this member in this collection
     */
    public void addMemberToCollection(String collectionGUID,
                                      String memberGUID,
                                      String membershipRationale)
    {
        final String methodName = "addMemberToCollection";

        EntityDetail entity1 = archiveBuilder.getEntity(collectionGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(memberGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, MEMBERSHIP_RATIONALE_PROPERTY, membershipRationale, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(COLLECTION_MEMBER_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(collectionGUID + "_to_" + memberGUID + "_collection_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance domain description entity.
     *
     * @param qualifiedName unique name for the governance domain entity
     * @param domainIdentifier unique identifier of the governance domain
     * @param displayName display name for the governance domain
     * @param description description about the governance domain
     * @param additionalProperties any other properties
     * @param governanceDomainSetGUID unique identifier of the collection for the domain definitions
     *
     * @return unique identifier for the governance domain (governanceDomainGUID)
     */
    public String addGovernanceDomainDescription(String               governanceDomainSetGUID,
                                                 String               qualifiedName,
                                                 int                  domainIdentifier,
                                                 String               displayName,
                                                 String               description,
                                                 Map<String, String>  additionalProperties)
    {
        final String methodName = "addGovernanceDomainDescription";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, DOMAIN_IDENTIFIER_PROPERTY, domainIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        if (governanceDomainSetGUID != null)
        {
            addMemberToCollection(governanceDomainSetGUID, newEntity.getGUID(), null);
        }

        return newEntity.getGUID();
    }


    /**
     * Create a governance definition entity.
     *
     * @param suppliedTypeName type of governance definition to add
     * @param qualifiedName unique name for the governance definition entity
     * @param title title for the governance definition
     * @param summary short description for the governance definition
     * @param description description about the governance definition
     * @param scope scope where the governance definition is used
     * @param priority how important is the governance definition
     * @param domainIdentifier unique identifier of the governance domain
     * @param implications expected impact of adopting this definition
     * @param outcomes expected outcomes from adopting this definition
     * @param results results from adopting this definition
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for governance definition (governanceDefinitionGUID)
     */
    public String addGovernanceDefinition(String               suppliedTypeName,
                                          String               qualifiedName,
                                          String               title,
                                          String               summary,
                                          String               description,
                                          String               scope,
                                          int                  domainIdentifier,
                                          String               priority,
                                          List<String>         implications,
                                          List<String>         outcomes,
                                          List<String>         results,
                                          Map<String, String>  additionalProperties,
                                          Map<String, Object>  extendedProperties)
    {
        final String methodName = "addGovernanceDefinition";

        String typeName = GOVERNANCE_DEFINITION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, TITLE_PROPERTY, title, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SUMMARY_PROPERTY, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PRIORITY_PROPERTY, priority, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, DOMAIN_IDENTIFIER_PROPERTY, domainIdentifier, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, IMPLICATIONS_PROPERTY, implications, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OUTCOMES_PROPERTY, outcomes, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RESULTS_PROPERTY, results, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Link a referenceable to a governance definition to define the scope there it applies.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param governanceDefinitionQName qualified name of the governance definition
     */
    public void addGovernanceDefinitionScopeRelationship(String  referenceableQName,
                                                         String  governanceDefinitionQName)
    {
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinitionQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(GOVERNANCE_DEFINITION_SCOPE_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_scope_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable to a governance definition to indicate that it is governed by the governance definition.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param governanceDefinitionQName qualified name of the governance definition
     */
    public void addGovernedByRelationship(String  referenceableQName,
                                          String  governanceDefinitionQName)
    {
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinitionQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(GOVERNED_BY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governed_by_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    /**
     * Link a referenceable to another referenceable to indicate that the second referenceable is providing resources in support of the first.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param resourceQName qualified name of the second referenceable
     * @param resourceUse string description
     * @param watchResource should the resource be watched (boolean)
     */
    public void addResourceListRelationship(String  referenceableQName,
                                            String  resourceQName,
                                            String  resourceUse,
                                            boolean watchResource)
    {
        final String methodName = "addResourceListRelationship";

        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(resourceQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, RESOURCE_USE_PROPERTY, resourceUse, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, WATCH_RESOURCE_PROPERTY, watchResource, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(RESOURCE_LIST_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_resource_list_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two governance definitions at the same level.
     *
     * @param relationshipTypeName type of relationship
     * @param governanceDefinition1QName qualified name of the referenceable
     * @param governanceDefinition2QName qualified name of the governance definition
     * @param description description of the peer relationship
     */
    public void addGovernanceDefinitionPeerRelationship(String  relationshipTypeName,
                                                        String  governanceDefinition1QName,
                                                        String  governanceDefinition2QName,
                                                        String  description)
    {
        final String methodName = "addGovernanceDefinitionPeerRelationship";

        String guid1 = idToGUIDMap.getGUID(governanceDefinition1QName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinition2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(relationshipTypeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_" + relationshipTypeName + "_peer_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two governance definitions at the same level.
     *
     * @param relationshipTypeName type of relationship
     * @param governanceDefinition1QName qualified name of the referenceable
     * @param governanceDefinition2QName qualified name of the governance definition
     * @param rationale description of the delegation relationship
     */
    public void addGovernanceDefinitionDelegationRelationship(String  relationshipTypeName,
                                                              String  governanceDefinition1QName,
                                                              String  governanceDefinition2QName,
                                                              String  rationale)
    {
        final String methodName = "addGovernanceDefinitionDelegationRelationship";

        String guid1 = idToGUIDMap.getGUID(governanceDefinition1QName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinition2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, RATIONALE_PROPERTY, rationale, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(relationshipTypeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_" + relationshipTypeName + "_delegation_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance zone entity.
     *
     * @param qualifiedName unique name for the zone entity
     * @param zoneName unique name for the zone
     * @param displayName display name for the zone
     * @param description description about the zone
     * @param scope scope where the zone is used
     * @param criteria what is the common characteristic of assets in this zone?
     * @param domainIdentifier unique identifier of the governance domain (0 means all/any)
     * @param additionalProperties any other properties
     *
     * @return unique identifier for governance zone (governanceZoneGUID)
     */
    public String addGovernanceZone(String               qualifiedName,
                                    String               zoneName,
                                    String               displayName,
                                    String               description,
                                    String               criteria,
                                    String               scope,
                                    int                  domainIdentifier,
                                    Map<String, String>  additionalProperties)
    {
        final String methodName = "addGovernanceZone";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ZONE_NAME_PROPERTY, zoneName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CRITERIA_PROPERTY, criteria, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, DOMAIN_IDENTIFIER_PROPERTY, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(GOVERNANCE_ZONE_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a governance zone and one of its nested governance zones.
     *
     * @param broaderGovernanceZoneGUID unique identifier of the broader zone
     * @param nestedGovernanceZoneGUID unique identifier of the nested (narrower) zone
     */
    public void addZoneHierarchy(String broaderGovernanceZoneGUID,
                                 String nestedGovernanceZoneGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(broaderGovernanceZoneGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(nestedGovernanceZoneGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(ZONE_HIERARCHY_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(broaderGovernanceZoneGUID + "_to_" + nestedGovernanceZoneGUID + "_zone_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the asset zone membership classification to the requested asset.
     *
     * @param assetGUID unique identifier of the asset to classify
     * @param zones list of zones that the asset is a member of.
     */
    public void addAssetZoneMembershipClassification(String       assetGUID,
                                                     List<String> zones)
    {
        final String methodName = "addAssetZoneMembershipClassification";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityProxy  entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(ASSET_ZONE_MEMBERSHIP_CLASSIFICATION_NAME,
                                                                         archiveHelper.addStringArrayPropertyToInstance(archiveRootName,
                                                                                                                        null,
                                                                                                                        ZONE_MEMBERSHIP_PROPERTY,
                                                                                                                        zones,
                                                                                                                        methodName),
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }


    /**
     * Create a subject area definition entity.
     *
     * @param qualifiedName unique name for the subject area entity
     * @param subjectAreaName unique name for the subject area
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
                                           String               subjectAreaName,
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
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SUBJECT_AREA_NAME_PROPERTY, subjectAreaName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
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
     * Create the relationship between a subject area and one of its nested subject areas.
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
     * Create a business area entity.
     *
     * @param qualifiedName unique name for the business area entity
     * @param identifier unique name for the business area
     * @param displayName display name for the business area
     * @param description description about the business area
     * @param additionalProperties any other properties
     *
     * @return unique identifier for business area (businessAreaGUID)
     */
    public String addBusinessArea(String               qualifiedName,
                                  String               identifier,
                                  String               displayName,
                                  String               description,
                                  Map<String, String>  additionalProperties)
    {
        final String methodName = "addBusinessArea";

        EnumElementDef businessCapabilityTypeEnum = archiveHelper.getEnumElement(BUSINESS_CAPABILITY_TYPE_ENUM_NAME, BUSINESS_CAPABILITY_TYPE_BUSINESS_AREA);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IDENTIFIER_PROPERTY, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, BUSINESS_CAPABILITY_TYPE_PROPERTY, businessCapabilityTypeEnum.getOrdinal(), businessCapabilityTypeEnum.getValue(), businessCapabilityTypeEnum.getDescription(), methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(BUSINESS_CAPABILITY_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add an OrganizationalCapability relationship.
     *
     * @param businessCapabilityQName qualified name of the specialized term
     * @param teamQName qualified name of the generalized term
     * @param scope scope of the team's ability to support the business capability
     */
    public void addOrganizationalCapabilityRelationship(String businessCapabilityQName,
                                                        String teamQName,
                                                        String scope)
    {
        final String methodName = "addOrganizationalCapabilityRelationship";

        String end1GUID = idToGUIDMap.getGUID(businessCapabilityQName);
        String end2GUID = idToGUIDMap.getGUID(teamQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, SCOPE_PROPERTY, scope, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(IS_A_TYPE_OF_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(end1GUID + "_to_" + end2GUID + "_organizational_capability_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
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
     * @param name display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAsset(String               typeName,
                           String               qualifiedName,
                           String               name,
                           String               description,
                           Map<String, String>  additionalProperties,
                           Map<String, Object>  extendedProperties,
                           List<Classification> classifications)
    {
        return this.addAsset(typeName, qualifiedName, name, null, description, additionalProperties, extendedProperties, classifications);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAsset(String               typeName,
                           String               qualifiedName,
                           String               name,
                           String               versionIdentifier,
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
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, VERSION_IDENTIFIER_PROPERTY, versionIdentifier, methodName);
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
     * @param name display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        return this.addAsset(typeName, qualifiedName, name, description, additionalProperties, extendedProperties, null);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param governanceZones list of zones to add to the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           List<String>        governanceZones,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        return this.addAsset(typeName, qualifiedName, name, null, description, governanceZones, additionalProperties, extendedProperties);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version of the asset
     * @param description description about the asset
     * @param governanceZones list of zones to add to the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              versionIdentifier,
                           String              description,
                           List<String>        governanceZones,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        final String methodName = "addAsset (with governance zones)";

        if (governanceZones == null)
        {
            return this.addAsset(typeName, qualifiedName, name, versionIdentifier, description, additionalProperties, extendedProperties, null);
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

            return this.addAsset(typeName, qualifiedName, name, description, additionalProperties, extendedProperties, classifications);
        }
    }




    /**
     * Create a process entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param formula description of the logic that this process performs
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addProcess(String               typeName,
                             String               qualifiedName,
                             String               name,
                             String               description,
                             String               formula,
                             Map<String, String>  additionalProperties,
                             Map<String, Object>  extendedProperties,
                             List<Classification> classifications)
    {
        return this.addProcess(typeName, qualifiedName, name, null, description, formula, additionalProperties, extendedProperties, classifications);
    }


    /**
     * Create a process entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version of the asset
     * @param description description about the asset
     * @param formula description of the logic that this process performs
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addProcess(String               typeName,
                             String               qualifiedName,
                             String               name,
                             String               versionIdentifier,
                             String               description,
                             String               formula,
                             Map<String, String>  additionalProperties,
                             Map<String, Object>  extendedProperties,
                             List<Classification> classifications)
    {
        final String methodName = "addProcess";

        String processTypeName = PROCESS_TYPE_NAME;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, VERSION_IDENTIFIER_PROPERTY, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, FORMULA_PROPERTY, formula, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(processTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create a software capability entity.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param name display name for the capability
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
                                        String              name,
                                        String              description,
                                        String              capabilityType,
                                        String              capabilityVersion,
                                        String              patchLevel,
                                        String              source,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties)
    {
        final String methodName = "addSoftwareCapability";

        String entityTypeName = SOFTWARE_CAPABILITY_TYPE_NAME;

        if (typeName != null)
        {
            entityTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NAME_PROPERTY, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CAPABILITY_TYPE_PROPERTY, capabilityType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CAPABILITY_VERSION_PROPERTY, capabilityVersion, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PATCH_LEVEL_PROPERTY, patchLevel, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SOURCE_PROPERTY, source, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail entity = archiveHelper.getEntityDetail(entityTypeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(entity);

        return entity.getGUID();
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
        addDataContentForDataSet(dataContentGUID, dataSetGUID, null, null);
    }


    /**
     * Create the relationship between a data set and an asset that is providing all or part of its content.
     *
     * @param dataContentGUID unique identifier of the data store
     * @param dataSetGUID unique identifier of the consuming data set
     * @param queryId identifier of the query used to combine results in a broader formula of the data set
     * @param query query to issue against this data content
     */
    public void addDataContentForDataSet(String dataContentGUID,
                                         String dataSetGUID,
                                         String queryId,
                                         String query)
    {
        final String methodName = "addDataContentForDataSet";

        EntityDetail dataContentEntity = archiveBuilder.getEntity(dataContentGUID);
        EntityDetail dataSetEntity = archiveBuilder.getEntity(dataSetGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(dataContentEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(dataSetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUERY_ID_PROPERTY, queryId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, QUERY_PROPERTY, query, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(dataContentGUID + "_to_" + dataSetGUID + "_data_content_for_data_set_relationship"),
                                                                     properties,
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
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PATH_PROPERTY, path, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, COMMAND_PROPERTY, command, methodName);
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
     * @param parameterType type of parameter
     * @param additionalProperties any other properties.
     *
     * @return id for the schema attribute
     */
    public String addAPIParameter(String              typeName,
                                  String              schemaTypeName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              dataType,
                                  int                 length,
                                  int                 position,
                                  String              parameterType,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIParameter";

        String schemaAttributeTypeName = API_PARAMETER_TYPE_NAME;

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
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, POSITION_PROPERTY, position, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, PARAMETER_TYPE_PROPERTY, parameterType, methodName);
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
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, POSITION_PROPERTY, position, methodName);
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
     * @param connectorTypeDirectoryGUID unique identifier of connector type directory that this connector belongs to - or null for an independent connector category
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

            archiveBuilder.addRelationship(archiveHelper.getRelationship(COLLECTION_MEMBER_RELATIONSHIP_NAME,
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
        return addGlossary(qualifiedName, displayName, description, language, usage, externalLink, scope, null);
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
     * @param additionalProperties any other properties.
     *
     * @return id for the glossary
     */
    public String addGlossary(String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              language,
                              String              usage,
                              String              externalLink,
                              String              scope,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addGlossary";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, LANGUAGE_PROPERTY, language, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USAGE_PROPERTY, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

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
    public String addCategory(String              glossaryGUID,
                              String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              subjectArea)
    {
        return addCategory(glossaryGUID, qualifiedName, displayName, description, subjectArea, null);
    }


    /**
     * Add a glossary category to the archive and connect it to glossary.
     *
     * @param glossaryGUID identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     * @param additionalProperties any other properties.
     *
     * @return identifier of the category
     */
    public String addCategory(String              glossaryGUID,
                              String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              subjectArea,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addCategory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

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
        return addTerm(glossaryGUID, categoryGUIDs, false, qualifiedName, displayName, null, description, null, null,null, false, false, false, null, null, null);
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.  Add requested classifications
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param categoriesAsNames when true the categories are specified as qualified names, otherwise they are guids.
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param summary short description of the term
     * @param description description of the term
     * @param examples examples of the term
     * @param abbreviation abbreviation
     * @param usage how is the term used
     * @param isSpineObject term is a spine object
     * @param isSpineAttribute term is a spine attribute
     * @param isContext is this term a context definition?
     * @param contextDescription description to add to the ContextDefinition classification
     * @param contextScope scope to add to the context classification
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the term
     */
    public String addTerm(String              glossaryGUID,
                          List<String>        categoryIds,
                          boolean             categoriesAsNames,
                          String              qualifiedName,
                          String              displayName,
                          String              summary,
                          String              description,
                          String              examples,
                          String              abbreviation,
                          String              usage,
                          boolean             isSpineObject,
                          boolean             isSpineAttribute,
                          boolean             isContext,
                          String              contextDescription,
                          String              contextScope,
                          Map<String, String> additionalProperties)
    {
        final String methodName = "addTerm";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SUMMARY_PROPERTY, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EXAMPLES_PROPERTY, examples, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ABBREVIATION_PROPERTY, abbreviation, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USAGE_PROPERTY, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        if (examples !=null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EXAMPLES_PROPERTY, examples, methodName);
        }

        List<Classification> classifications = null;

        if (isSpineObject)
        {
            Classification  newClassification = archiveHelper.getClassification(SPINE_OBJECT_NAME,
                                                                                null,
                                                                                InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(newClassification);
        }

        if (isSpineAttribute)
        {
            Classification newClassification = archiveHelper.getClassification(SPINE_ATTRIBUTE_NAME,
                                                                               null,
                                                                               InstanceStatus.ACTIVE);

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(newClassification);
        }

        if (isContext)
        {
            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, DESCRIPTION_PROPERTY, contextDescription, methodName);
            classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, SCOPE_PROPERTY, contextScope, methodName);

            Classification  newClassification = archiveHelper.getClassification(CONTEXT_DEFINITION_CLASSIFICATION_NAME,
                                                                                classificationProperties,
                                                                                InstanceStatus.ACTIVE);

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(newClassification);
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
     * Link a term to a context referenceable to show that the term is valid in the context of the other.
     *
     * @param contextGUID unique identifier for the context
     * @param termGUID unique identifier for the term
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     */
    public void addTermToContext(String  contextGUID,
                                 String  termGUID,
                                 int     status,
                                 String  description)
    {
        final String methodName = "addTermToContext";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(contextGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, STATUS_PROPERTY, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(USED_IN_CONTEXT_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(contextGUID + "_to_" + termGUID + "_used_in_context_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link two terms together to show that one is valid in the context of the other.
     *
     * @param synonymGUID unique identifier for the context
     * @param termGUID unique identifier for the term
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     * @param expression the expression that indicates how close a synonym this is
     * @param steward the identifier of the steward that created this relationship
     * @param source source of the information that indicates this is a synonym
     */
    public void addTermToSynonym(String  synonymGUID,
                                 String  termGUID,
                                 int     status,
                                 String  description,
                                 String  expression,
                                 String  steward,
                                 String  source)
    {
        final String methodName = "addTermToSynonym";

        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(synonymGUID));
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(TERM_RELATIONSHIP_STATUS_ENUM_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, STATUS_PROPERTY, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, EXPRESSION_PROPERTY, expression, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SOURCE_PROPERTY, source, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SYNONYM_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(synonymGUID + "_to_" + termGUID + "_synonym_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the  term
     */
    public void addTermToCategory(String  categoryGUID,
                                  String  termGUID)
    {
        addTermToCategory(categoryGUID, termGUID, 1, null);
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the term
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
     * @param identifier identifier of the category in the external glossary
     * @param description description of the link
     * @param steward steward who created link
     * @param lastVerified last time this was verified
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
     * @param identifier identifier of the term in the external glossary
     * @param description description of the link
     * @param steward steward who created link
     * @param lastVerified last time this was verified
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


    /**
     * Add a valid value definition/set.
     *
     * @param typeName unique name of the valid value - ie a definition or a set
     * @param qualifiedName unique name of the valid value
     * @param displayName display name of the valid value
     * @param scope short description of the valid value
     * @param description description of the valid value
     * @param preferredValue preferredValue of the valid value
     * @param usage how is the valid value used
     * @param isDeprecated is value active
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the valid value
     */
    public String addValidValue(String              typeName,
                                String              qualifiedName,
                                String              displayName,
                                String              description,
                                String              usage,
                                String              scope,
                                String              preferredValue,
                                boolean             isDeprecated,
                                Map<String, String> additionalProperties)
    {
        final String methodName = "addValidValue";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USAGE_PROPERTY, usage, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, SCOPE_PROPERTY, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PREFERRED_VALUE_PROPERTY, preferredValue, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, IS_DEPRECATED_PROPERTY, isDeprecated, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);


        EntityDetail  termEntity = archiveHelper.getEntityDetail(typeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 null);

        archiveBuilder.addEntity(termEntity);

        return termEntity.getGUID();
    }


    /**
     * Link a valid value as a member of a valid value set.
     *
     * @param setQName qualified name of the set to add to
     * @param memberQName qualified name of the member to add
     * @param isDefaultValue is this the default value (only set to true for one member).
     */
    public void addValidValueMembershipRelationship(String  setQName,
                                                    String  memberQName,
                                                    boolean isDefaultValue)
    {
        final String methodName = "addValidValuesAssignmentRelationship";

        String setId = idToGUIDMap.getGUID(setQName);
        String memberId = idToGUIDMap.getGUID(memberQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(setId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(memberId));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, IS_DEFAULT_VALUE_PROPERTY, isDefaultValue, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(VALID_VALUE_MEMBER_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(setId + "_to_" + memberId + "_valid_value_member_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an element that represents a data field (either directly or indirectly) to a valid value (typically a valid value set).
     *
     * @param dataFieldQName qualified name of element that represents the data field
     * @param validValueQName qualified name of the valid value set/definition
     * @param strictRequirement do the valid values mandate the values stored in the data field?
     */
    public void addValidValuesAssignmentRelationship(String  dataFieldQName,
                                                     String  validValueQName,
                                                     boolean strictRequirement)
    {
        final String methodName = "addValidValuesAssignmentRelationship";

        String dataFieldId = idToGUIDMap.getGUID(dataFieldQName);
        String validValueId = idToGUIDMap.getGUID(validValueQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(dataFieldId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, STRICT_REQUIREMENT_PROPERTY, strictRequirement, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(VALID_VALUES_ASSIGNMENT_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(dataFieldId + "_to_" + validValueId + "_valid_values_assignment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a mapping between two valid values.  Typically they are in different valid value sets.
     *
     * @param matchingValue1QName qualified name of one of the valid values.
     * @param matchingValue2QName qualified name of the other valid value.
     * @param associationDescription a description of the meaning of the association
     * @param confidence how likely is the relationship correct - 0=unlikely; 100=certainty
     * @param steward who was the steward that made the link
     * @param stewardTypeName what is the type of the element used to represent the steward?
     * @param stewardPropertyName what is the name of the property used to represent the steward?
     * @param notes any notes on the relationship.
     */
    public void addValidValuesMappingRelationship(String matchingValue1QName,
                                                  String matchingValue2QName,
                                                  String associationDescription,
                                                  int    confidence,
                                                  String steward,
                                                  String stewardTypeName,
                                                  String stewardPropertyName,
                                                  String notes)
    {
        final String methodName = "addValidValuesMappingRelationship";

        String matchingValue1Id = idToGUIDMap.getGUID(matchingValue1QName);
        String matchingValueId = idToGUIDMap.getGUID(matchingValue2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(matchingValue1Id));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(matchingValueId));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, ASSOCIATION_DESCRIPTION_PROPERTY, associationDescription, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, CONFIDENCE_PROPERTY, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_TYPE_NAME_PROPERTY, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY_NAME_PROPERTY, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NOTES_PROPERTY, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(VALID_VALUES_MAPPING_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(matchingValue1Id + "_to_" + matchingValueId + "_valid_values_mapping_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable element to a valid value that is acting as a tag.
     *
     * @param referenceableQName qualified name of referenceable
     * @param validValueQName qualified name of valid value
     * @param confidence how likely is the relationship correct - 0=unlikely; 100=certainty
     * @param steward who was the steward that made the link
     * @param stewardTypeName what is the type of the element used to represent the steward?
     * @param stewardPropertyName what is the name of the property used to represent the steward?
     * @param notes any notes on the relationship.
     */
    public void addReferenceValueAssignmentRelationship(String referenceableQName,
                                                        String validValueQName,
                                                        int    confidence,
                                                        String steward,
                                                        String stewardTypeName,
                                                        String stewardPropertyName,
                                                        String notes)
    {
        final String methodName = "addReferenceValueAssignmentRelationship";

        String referenceableId = idToGUIDMap.getGUID(referenceableQName);
        String validValueId = idToGUIDMap.getGUID(validValueQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, CONFIDENCE_PROPERTY, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_TYPE_NAME_PROPERTY, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, STEWARD_PROPERTY_NAME_PROPERTY, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NOTES_PROPERTY, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(REFERENCE_VALUE_ASSIGNMENT_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + validValueId + "_reference_value_assignment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a valid value definition to the asset where it is implemented.
     *
     * @param validValueQName qualified name of the valid value
     * @param assetQName qualified name of the asset
     * @param symbolicName property name used in the asset to represent the valid value
     * @param implementationValue value used in the asset to represent the valid value
     * @param additionalValues additional mapping values
     */
    public void addValidValuesImplementationRelationship(String              validValueQName,
                                                         String              assetQName,
                                                         String              symbolicName,
                                                         String              implementationValue,
                                                         Map<String, String> additionalValues)
    {
        final String methodName = "addValidValuesImplementationRelationship";

        String validValueId = idToGUIDMap.getGUID(validValueQName);
        String assetId = idToGUIDMap.getGUID(assetQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(assetId));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, SYMBOLIC_NAME_PROPERTY, symbolicName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, IMPLEMENTATION_VALUE_PROPERTY, implementationValue, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_VALUES_PROPERTY, additionalValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(VALID_VALUES_IMPLEMENTATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(validValueId + "_to_" + assetId + "_valid_values_implementation_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the ReferenceData classification to the requested element.
     *
     * @param assetGUID unique identifier of the element to classify
     */
    public void addReferenceDataClassification(String assetGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy referenceableEntityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(REFERENCE_DATA_CLASSIFICATION_NAME,
                                                                         null,
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(referenceableEntityProxy, classification));
    }

    /**
     * Add a SecurityTags classification to the requested element.
     *
     * @param assetGUID unique identifier for the element to classify
     * @param securityLabels list of security labels
     * @param securityProperties map of security properties
     * @param accessGroups access group assignments
     */
    public void addSecurityTagsClassification(String                    assetGUID,
                                              List<String>              securityLabels,
                                              Map<String, Object>       securityProperties,
                                              Map<String, List<String>> accessGroups)
    {
        final String methodName = "addSecurityTagsClassification";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy entityProxy = archiveHelper.getEntityProxy(assetEntity);

        InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, null, SECURITY_LABELS_PROPERTY, securityLabels, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, SECURITY_PROPERTIES_PROPERTY, securityProperties, methodName);
        properties = archiveHelper.addStringArrayStringMapPropertyToInstance(archiveRootName, properties, ACCESS_GROUPS_PROPERTY, accessGroups, methodName);

        Classification classification = archiveHelper.getClassification(SECURITY_TAGS_CLASSIFICATION_NAME,
                                                                        properties,
                                                                        InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }
}
