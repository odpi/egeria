/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

/**
 * OpenMetadataAPIMapper provides property name mapping for the generic builder, handler and converter.
 * It includes identifiers for all the types that need specialist processing at this level in the stack
 */
public class OpenMetadataAPIMapper
{
    public static final String GUID_PROPERTY_NAME                        = "guid";

    /* ============================================================================================================================*/
    /* Area 0 - Basic definitions and Infrastructure                                                                               */
    /* ============================================================================================================================*/

    public static final String OPEN_METADATA_ROOT_TYPE_GUID              = "4e7761e8-3969-4627-8f40-bfe3cde85a1d";
    public static final String OPEN_METADATA_ROOT_TYPE_NAME              = "OpenMetadataRoot";        /* from Area 0 */

    public static final String REFERENCEABLE_TYPE_GUID                   = "a32316b8-dc8c-48c5-b12b-71c1b2a080bf";
    public static final String REFERENCEABLE_TYPE_NAME                   = "Referenceable";          /* from Area 0 */

    public static final String QUALIFIED_NAME_PROPERTY_NAME              = "qualifiedName";                        /* from Referenceable entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME       = "additionalProperties";                 /* from Referenceable entity */

    public static final String ASSET_TYPE_GUID                           = "896d14c2-7522-4f6c-8519-757711943fe6";
    public static final String ASSET_TYPE_NAME                           = "Asset";
    /* Referenceable */

    public static final String PROCESS_TYPE_GUID                         = "d8f33bd7-afa9-4a11-a8c7-07dcec83c050";
    public static final String PROCESS_TYPE_NAME                         = "Process";
    /* Referenceable */

    public static final String DATA_SET_TYPE_GUID                        = "1449911c-4f44-4c22-abc0-7540154feefb";  /* from Area 0 */
    public static final String DATA_SET_TYPE_NAME                        = "DataSet";
    /* Asset */

    public static final String NAME_PROPERTY_NAME                        = "name";                                 /* from Asset entity */
    public static final String VERSION_IDENTIFIER_PROPERTY_NAME          = "versionIdentifier";                    /* from Asset entity */
    public static final String FORMULA_PROPERTY_NAME                     = "formula";                              /* from Process entity */
    public static final String FORMULA_TYPE_PROPERTY_NAME                = "formulaType";                          /* from Process entity */

    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";              /* from feedback relationships - Area 1 */
    public static final String DISPLAY_NAME_PROPERTY_NAME                = "displayName";           /* from many entities */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";           /* from many entities */
    public static final String COLLECTION_TYPE_PROPERTY_NAME             = "collectionType";        /* from Collection entity */

    public static final String SAMPLE_DATA_TYPE_GUID                     = "0ee9c0f1-a89b-4806-8276-7c74f07fe190";
    public static final String SAMPLE_DATA_TYPE_NAME                     = "SampleData";

    public static final String SAMPLING_METHOD_PROPERTY_NAME             = "samplingMethod";       /* from SampleData relationship */

    public static final String ANCHORS_CLASSIFICATION_TYPE_GUID                  = "aa44f302-2e43-4669-a1e7-edaae414fc6e";
    public static final String ANCHORS_CLASSIFICATION_TYPE_NAME                  = "Anchors";
    public static final String ANCHOR_GUID_PROPERTY_NAME                         = "anchorGUID";

    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_GUID               = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_NAME               = "LatestChangeTarget";
    public static final int    ENTITY_STATUS_LATEST_CHANGE_TARGET_ORDINAL             = 0;
    public static final int    ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL           = 1;
    public static final int    ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL     = 2;
    public static final int    ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL       = 3;
    public static final int    ATTACHMENT_LATEST_CHANGE_TARGET_ORDINAL                = 4;
    public static final int    ATTACHMENT_STATUS_LATEST_CHANGE_TARGET_ORDINAL         = 5;
    public static final int    ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL       = 6;
    public static final int    ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL = 7;
    public static final int    ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL   = 8;
    public static final int    OTHER_LATEST_CHANGE_TARGET_ORDINAL                     = 99;

    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_GUID               = "032d844b-868f-4c4a-bc5d-81f0f9704c4d";
    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_NAME               = "LatestChangeAction";
    public static final int    CREATED_LATEST_CHANGE_ACTION_ORDINAL                   = 0;
    public static final int    UPDATED_LATEST_CHANGE_ACTION_ORDINAL                   = 1;
    public static final int    DELETED_LATEST_CHANGE_ACTION_ORDINAL                   = 2;
    public static final int    OTHER_LATEST_CHANGE_ACTION_ORDINAL                     = 99;

    public static final String LATEST_CHANGE_CLASSIFICATION_TYPE_GUID            = "adce83ac-10f1-4279-8a35-346976e94466";
    public static final String LATEST_CHANGE_CLASSIFICATION_TYPE_NAME            = "LatestChange";
    public static final String CHANGE_TARGET_PROPERTY_NAME                       = "changeTarget";
    public static final String CHANGE_ACTION_PROPERTY_NAME                       = "changeAction";
    public static final String CLASSIFICATION_NAME_PROPERTY_NAME                 = "classificationName";
    public static final String CLASSIFICATION_PROPERTY_NAME_PROPERTY_NAME        = "classificationPropertyName";
    public static final String ATTACHMENT_GUID_PROPERTY_NAME                     = "attachmentGUID";
    public static final String ATTACHMENT_TYPE_PROPERTY_NAME                     = "attachmentType";
    public static final String RELATIONSHIP_TYPE_PROPERTY_NAME                   = "relationshipType";
    public static final String USER_PROPERTY_NAME                                = "user";
    public static final String ACTION_DESCRIPTION_PROPERTY_NAME                  = "description";

    public static final String TEMPLATE_CLASSIFICATION_TYPE_GUID                 = "25fad4a2-c2d6-440d-a5b1-e537881f84ee";
    public static final String TEMPLATE_CLASSIFICATION_TYPE_NAME                 = "Template";

    public static final String TEMPLATE_SUBSTITUTE_CLASSIFICATION_TYPE_GUID      = "93b293c3-1185-4921-aa1c-237d3f0a5d5c";
    public static final String TEMPLATE_SUBSTITUTE_CLASSIFICATION_TYPE_NAME      = "TemplateSubstitute";

    public static final String TEMPLATE_NAME_PROPERTY_NAME                       = "name";
    public static final String TEMPLATE_DESCRIPTION_PROPERTY_NAME                = "description";
    public static final String TEMPLATE_ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties";

    public static final String SOURCED_FROM_RELATIONSHIP_TYPE_GUID               = "87b7371e-e311-460f-8849-08646d0d6ad3";   /* from Area 0 */
    public static final String SOURCED_FROM_RELATIONSHIP_TYPE_NAME               = "SourcedFrom";
    /* End1 = NewEntity; End 2 = Template */

    public static final String SOURCE_VERSION_NUMBER_PROPERTY_NAME               = "sourceVersionNumber";

    public static final String MEMENTO_CLASSIFICATION_TYPE_GUID                  = "ecdcd472-6701-4303-8dec-267bcb54feb9";
    public static final String MEMENTO_CLASSIFICATION_TYPE_NAME                  = "Memento";
    public static final String ARCHIVE_DATE_PROPERTY_NAME                        = "archiveDate";
    public static final String ARCHIVE_USER_PROPERTY_NAME                        = "archiveUser";
    public static final String ARCHIVE_PROCESS_PROPERTY_NAME                     = "archiveProcess";
    public static final String ARCHIVE_SERVICE_PROPERTY_NAME                     = "archiveService";
    public static final String ARCHIVE_METHOD_PROPERTY_NAME                      = "archiveMethod";
    public static final String ARCHIVE_PROPERTIES_PROPERTY_NAME                  = "archiveProperties";

    public static final String REFERENCEABLE_TO_MORE_INFO_TYPE_GUID              = "1cbf059e-2c11-4e0c-8aae-1da42c1ee73f";
    public static final String REFERENCEABLE_TO_MORE_INFO_TYPE_NAME              = "MoreInformation";
    /* End1 = Referenceable; End 2 = more info Referenceable */

    public static final String SEARCH_KEYWORD_TYPE_GUID                          = "0134c9ae-0fe6-4224-bb3b-e18b78a90b1e";
    public static final String SEARCH_KEYWORD_TYPE_NAME                          = "SearchKeyword";
    public static final String KEYWORD_PROPERTY_NAME                             = "keyword";
    public static final String KEYWORD_DESCRIPTION_PROPERTY_NAME                 = "description";

    public static final String REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID         = "d2f8df24-6905-49b8-b389-31b2da156ece";
    public static final String REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME         = "SearchKeywordLink";
    /* End1 = Referenceable; End 2 = SearchKeyword */

    public static final String SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID       = "f9ffa8a8-80f5-4e6d-9c05-a3a5e0277d62";
    public static final String SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME       = "RelatedKeyword";
    /* End1 = SearchKeyword; End 2 = SearchKeyword */

    public static final String EXTERNAL_IDENTIFIER_TYPE_GUID               = "7c8f8c2c-cc48-429e-8a21-a1f1851ccdb0";
    public static final String EXTERNAL_IDENTIFIER_TYPE_NAME               = "ExternalId";              /* from Area 0 */

    public static final String IDENTIFIER_PROPERTY_NAME                    = "identifier";                      /* from ExternalId entity */
    public static final String EXT_INSTANCE_CREATED_BY_PROPERTY_NAME       = "externalInstanceCreatedBy";       /* from ExternalId entity */
    public static final String EXT_INSTANCE_CREATION_TIME_PROPERTY_NAME    = "externalInstanceCreationTime";    /* from ExternalId entity */
    public static final String EXT_INSTANCE_LAST_UPDATED_BY_PROPERTY_NAME  = "externalInstanceLastUpdatedBy";   /* from ExternalId entity */
    public static final String EXT_INSTANCE_LAST_UPDATE_TIME_PROPERTY_NAME = "externalInstanceLastUpdateTime";  /* from ExternalId entity */
    public static final String EXT_INSTANCE_VERSION_PROPERTY_NAME          = "externalInstanceVersion";         /* from ExternalId entity */
    public static final String MAPPING_PROPERTIES_PROPERTY_NAME = "mappingProperties";      /* from ExternalId entity */
    public static final String LAST_SYNCHRONIZED_PROPERTY_NAME  = "lastSynchronized";       /* from ExternalId entity */
    public static final String KEY_PATTERN_PROPERTY_NAME        = "keyPattern";             /* from ExternalId entity */
    /* Enum type KeyPattern */

    public static final String KEY_PATTERN_ENUM_TYPE_GUID  = "8904df8f-1aca-4de8-9abd-1ef2aadba300";
    public static final String KEY_PATTERN_ENUM_TYPE_NAME  = "KeyPattern";

    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID    = "28ab0381-c662-4b6d-b787-5d77208de126";
    public static final String REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME    = "ExternalIdLink";
    /* End1 = Referenceable; End 2 = ExternalId */

    public static final String SOURCE_PROPERTY_NAME                      = "source"; /* from ExternalIdLink relationship */

    public static final String EXTERNAL_ID_SCOPE_TYPE_GUID               = "8c5b1415-2d1f-4190-ba6c-1fdd47f03269";
    public static final String EXTERNAL_ID_SCOPE_TYPE_NAME               = "ExternalIdScope";
    /* End1 = Referenceable; End 2 = ExternalId */

    public static final String PERMITTED_SYNC_PROPERTY_NAME  = "permittedSynchronization"; /* from ExternalId and RegisteredIntegrationConnector */
    /* Enum type PermittedSynchronization */

    public static final String PERMITTED_SYNC_ENUM_TYPE_GUID  = "973a9f4c-93fa-43a5-a0c5-d97dbd164e78";
    public static final String PERMITTED_SYNC_ENUM_TYPE_NAME  = "PermittedSynchronization";

    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_GUID                = "7d818a67-ab45-481c-bc28-f6b1caf12f06";
    public static final String REFERENCEABLE_TO_EXT_REF_TYPE_NAME                = "ExternalReferenceLink";
    /* End1 = Referenceable; End 2 = ExternalReference */

    public static final String EXTERNAL_REFERENCE_TYPE_GUID           = "af536f20-062b-48ef-9c31-1ddd05b04c56";
    public static final String EXTERNAL_REFERENCE_TYPE_NAME           = "ExternalReference";    /* from Area 0 */
    /* Referenceable */

    public static final String URL_PROPERTY_NAME                         = "url";                 /* from ExternalReference entity */
    public static final String REFERENCE_VERSION_PROPERTY_NAME           = "referenceVersion";    /* from ExternalReference entity */

    public static final String REFERENCE_ID_PROPERTY_NAME                = "referenceId";         /* from ExternalReferenceLink relationship */
    public static final String PAGES_PROPERTY_NAME                       = "pages";               /* from ExternalReferenceLink relationship */
    /* plus description property */

    public static final String RELATED_MEDIA_TYPE_GUID                   = "747f8b86-fe7c-4c9b-ba75-979e093cc307";
    public static final String RELATED_MEDIA_TYPE_NAME                   = "RelatedMedia";         /* from Area 0 */
    /* ExternalReference */

    public static final String MEDIA_USAGE_PROPERTY_NAME                 = "mediaUsage";           /* from RelatedMedia entity */
    public static final String MEDIA_TYPE_PROPERTY_NAME                  = "mediaType";            /* from RelatedMedia entity */
    /* MediaType enum */

    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_GUID  = "1353400f-b0ab-4ab9-ab09-3045dd8a7140";
    public static final String REFERENCEABLE_TO_RELATED_MEDIA_TYPE_NAME  = "MediaReference";
    /* End1 = Referenceable; End 2 = RelatedMedia */

    public static final String MEDIA_ID_PROPERTY_NAME                    = "mediaId";              /* from MediaReference relationship */
    public static final String MEDIA_DESCRIPTION_PROPERTY_NAME           = "description";          /* from MediaReference relationship */



    public static final String COLLECTION_TYPE_GUID                      = "347005ba-2b35-4670-b5a7-12c9ebed0cf7";
    public static final String COLLECTION_TYPE_NAME                      = "Collection";              /* from Area 1 */
    /* Referenceable */

    public static final String RESOURCE_LIST_RELATIONSHIP_TYPE_GUID      = "73cf5658-6a73-4ebc-8f4d-44fdfac0b437";
    public static final String RESOURCE_LIST_RELATIONSHIP_TYPE_NAME      = "ResourceList";
    /* End1 = Referenceable (anchor); End 2 = Referenceable */

    public static final String RESOURCE_USE_PROPERTY_NAME                = "resourceUse";   /* from ResourceList relationship */
    public static final String WATCH_RESOURCE_PROPERTY_NAME              = "watchResource"; /* from ResourceList relationship */

    public static final String COLLECTION_MEMBERSHIP_TYPE_GUID           = "5cabb76a-e25b-4bb5-8b93-768bbac005af";
    public static final String COLLECTION_MEMBERSHIP_TYPE_NAME           = "CollectionMembership";
    /* End1 = Collection; End 2 = Referenceable */

    public static final String REFERENCEABLE_TO_COLLECTION_TYPE_GUID     = COLLECTION_MEMBERSHIP_TYPE_GUID;
    public static final String REFERENCEABLE_TO_COLLECTION_TYPE_NAME     = COLLECTION_MEMBERSHIP_TYPE_NAME;
    /* End1 = Collection; End 2 = Referenceable */

    public static final String MEMBERSHIP_RATIONALE_PROPERTY_NAME        = "membershipRationale";

    public static final String SET_TYPE_GUID                             = "3947f08d-7412-4022-81fc-344a20dfbb26";
    public static final String SET_TYPE_NAME                             = "Set";                 /* from Area 1 */

    public static final String FOLDER_TYPE_GUID                          = "3c0fa687-8a63-4c8e-8bda-ede9c78be6c7";
    public static final String FOLDER_TYPE_NAME                          = "Folder";              /* from Area 1 */

    public static final String ORDER_BY_PROPERTY_NAME                    = "orderBy";           /* from Folder classification */
    public static final String ORDER_PROPERTY_NAME_PROPERTY_NAME         = "orderPropertyName"; /* from Folder classification */

    public static final String ORDER_BY_TYPE_ENUM_TYPE_GUID              = "1d412439-4272-4a7e-a940-1065f889fc56";
    public static final String ORDER_BY_TYPE_ENUM_TYPE_NAME              = "OrderBy";

    public static final String MEMBERSHIP_STATUS_ENUM_TYPE_GUID          = "a3bdb2ac-c28e-4e5a-8ab7-76aa01038832";
    public static final String MEMBERSHIP_STATUS_ENUM_TYPE_NAME          = "MembershipStatus";

    public static final String PROPERTY_FACET_TYPE_GUID                  = "6403a704-aad6-41c2-8e08-b9525c006f85";
    public static final String PROPERTY_FACET_TYPE_NAME                  = "PropertyFacet";
    /* Referenceable */

    public static final String SCHEMA_VERSION_PROPERTY_NAME              = "schemaVersion";      /* from PropertyFacet entity */
    public static final String PROPERTIES_PROPERTY_NAME                  = "properties";         /* from PropertyFacet entity */

    public static final String VENDOR_PROPERTIES_DESCRIPTION_VALUE       = "vendorProperties";


    public static final String REFERENCEABLE_TO_PROPERTY_FACET_TYPE_GUID = "58c87647-ada9-4c90-a3c3-a40ace46b1f7";
    public static final String REFERENCEABLE_TO_PROPERTY_FACET_TYPE_NAME = "ReferenceableFacet";
    /* End1 = Referenceable; End 2 = PropertyFacet */

    public static final String LOCATION_TYPE_GUID                        = "3e09cb2b-5f15-4fd2-b004-fe0146ad8628";
    public static final String LOCATION_TYPE_NAME                        = "Location";
    /* Referenceable */

    public static final String FIXED_LOCATION_CLASSIFICATION_TYPE_GUID   = "bc111963-80c7-444f-9715-946c03142dd2";
    public static final String FIXED_LOCATION_CLASSIFICATION_TYPE_NAME   = "FixedLocation";
    public static final String COORDINATES_PROPERTY_NAME                 = "coordinates";
    public static final String MAP_PROJECTION_PROPERTY_NAME              = "mapProjection";
    public static final String POSTAL_ADDRESS_PROPERTY_NAME              = "postalAddress";
    public static final String POSTAL_ADDRESS_PROPERTY_NAME_DEP          = "address";
    public static final String TIME_ZONE_PROPERTY_NAME                   = "timezone";

    public static final String SECURE_LOCATION_CLASSIFICATION_TYPE_GUID  = "e7b563c0-fcdd-4ba7-a046-eecf5c4638b8";
    public static final String SECURE_LOCATION_CLASSIFICATION_TYPE_NAME  = "SecureLocation";
    public static final String LEVEL_PROPERTY_NAME                       = "level";

    public static final String CYBER_LOCATION_CLASSIFICATION_TYPE_GUID   = "f9ec3633-8ac8-480b-aa6d-5e674b9e1b17";
    public static final String CYBER_LOCATION_CLASSIFICATION_TYPE_NAME   = "CyberLocation";
    public static final String NETWORK_ADDRESS_PROPERTY_NAME_DEP         = "address";

    public static final String MOBILE_ASSET_CLASSIFICATION_TYPE_GUID     = "b25fb90d-8fa2-4aa9-b884-ff0a6351a697";
    public static final String MOBILE_ASSET_CLASSIFICATION_TYPE_NAME     = "MobileAsset";

    public static final String ASSET_LOCATION_TYPE_GUID                  = "bc236b62-d0e6-4c5c-93a1-3a35c3dba7b1";  /* from Area 0 */
    public static final String ASSET_LOCATION_TYPE_NAME                  = "AssetLocation";
    /* End1 = Location; End 2 = Asset */

    public static final String PROFILE_LOCATION_TYPE_GUID                = "4d652ef7-99c7-4ec3-a2fd-b10c0a1ab4b4";  /* from Area 1 */
    public static final String PROFILE_LOCATION_TYPE_NAME                = "ProfileLocation";
    /* End1 = ActorProfile; End 2 = Location */

    public static final String ASSOCIATION_TYPE_PROPERTY_NAME            = "associationType";

    public static final String NESTED_LOCATION_TYPE_GUID                 = "f82a96c2-95a3-4223-88c0-9cbf2882b772";  /* from Area 0 */
    public static final String NESTED_LOCATION_TYPE_NAME                 = "NestedLocation";
    /* End1 = ParentLocation; End 2 = ChildLocation */

    public static final String ADJACENT_LOCATION_TYPE_GUID               = "017d0518-fc25-4e5e-985e-491d91e61e17";  /* from Area 0 */
    public static final String ADJACENT_LOCATION_TYPE_NAME               = "AdjacentLocation";
    /* End1 = Location; End 2 = Location */

    public static final String IT_INFRASTRUCTURE_TYPE_GUID               = "151e6dd1-54a0-4b7f-a072-85caa09d1dda";
    public static final String IT_INFRASTRUCTURE_TYPE_NAME               = "ITInfrastructure";
    /* Infrastructure */

    public static final String HOST_TYPE_GUID                            = "1abd16db-5b8a-4fd9-aee5-205db3febe99";
    public static final String HOST_TYPE_NAME                            = "Host";
    /* ITInfrastructure */

    public static final String BARE_METAL_COMPUTER_TYPE_GUID             = "8ef355d4-5cd7-4038-8337-62671b088920";
    public static final String BARE_METAL_COMPUTER_TYPE_NAME             = "BareMetalComputer";
    /* Host */

    public static final String HOST_CLUSTER_TYPE_GUID                    = "9794f42f-4c9f-4fe6-be84-261f0a7de890";
    public static final String HOST_CLUSTER_TYPE_NAME                    = "HostCluster";
    /* Host */

    public static final String HADOOP_CLUSTER_TYPE_GUID                  = "abc27cf7-e526-4d1b-9c25-7dd60a7993e4";
    public static final String HADOOP_CLUSTER_TYPE_NAME                  = "HadoopCluster";
    /* HostCluster */

    public static final String KUBERNETES_CLUSTER_TYPE_GUID              = "101f1c93-7f5d-44e2-9ea4-5cf21726ba5c";
    public static final String KUBERNETES_CLUSTER_TYPE_NAME              = "KubernetesCluster";
    /* HostCluster */

    public static final String VIRTUAL_CONTAINER_TYPE_GUID               = "e2393236-100f-4ac0-a5e6-ce4e96c521e7";
    public static final String VIRTUAL_CONTAINER_TYPE_NAME               = "VirtualContainer";
    /* Virtual container */

    public static final String DOCKER_CONTAINER_TYPE_GUID                = "9882b8aa-eba3-4a30-94c6-43117efd11cc";
    public static final String DOCKER_CONTAINER_TYPE_NAME                = "DockerContainer";
    /* VirtualContainer */

    public static final String VIRTUAL_MACHINE_TYPE_GUID                 = "28452091-6b27-4f40-8e31-47ce34f58387";
    public static final String VIRTUAL_MACHINE_TYPE_NAME                 = "VirtualMachine";
    /* Host */

    public static final String STORAGE_VOLUME_TYPE_GUID                  = "14145458-f0d0-4955-8899-b8a2874708c9";
    public static final String STORAGE_VOLUME_TYPE_NAME                  = "StorageVolume";
    /* Referenceable */

    public static final String ATTACHED_STORAGE_TYPE_GUID                = "2cf1e949-7189-4bf2-8ee4-e1318e59abd7";  /* from Area 0 */
    public static final String ATTACHED_STORAGE_TYPE_NAME                = "AttachedStorage";
    /* End1 = Host; End2 = StorageVolume */


    public static final String OPERATING_PLATFORM_TYPE_GUID              = "bd96a997-8d78-42f6-adf7-8239bc98501c";
    public static final String OPERATING_PLATFORM_TYPE_NAME              = "OperatingPlatform";
    /* Referenceable */

    public static final String OPERATING_SYSTEM_PROPERTY_NAME             = "operatingSystem";           /* from OperatingPlatform entity */
    public static final String OPERATING_SYSTEM_PATCH_LEVEL_PROPERTY_NAME = "operatingSystemPatchLevel"; /* from OperatingPlatform entity */
    public static final String BYTE_ORDERING_PROPERTY_NAME                = "byteOrdering";              /* from OperatingPlatform entity */
    public static final String BYTE_ORDERING_PROPERTY_NAME_DEP            = "endianness";                /* from OperatingPlatform entity */

    public static final String ENDIANNESS_ENUM_TYPE_GUID                 = "e5612c3a-49bd-4148-8f67-cfdf145d5fd8";
    public static final String ENDIANNESS_ENUM_TYPE_NAME                 = "Endianness";                            /* from Area 1 */

    public static final String HOST_OPERATING_PLATFORM_TYPE_GUID         = "b9179df5-6e23-4581-a8b0-2919e6322b12";  /* from Area 0 */
    public static final String HOST_OPERATING_PLATFORM_TYPE_NAME         = "HostOperatingPlatform";
    /* End1 = Host; End2 = OperatingPlatform */

    public static final String OPERATING_PLATFORM_MANIFEST_TYPE_GUID     = "e5bd6acf-932c-4d9c-85ff-941a8e4451db";  /* from Area 0 */
    public static final String OPERATING_PLATFORM_MANIFEST_TYPE_NAME     = "OperatingPlatformManifest";
    /* End1 = OperatingPlatform; End2 = Collection */

    public static final String SOFTWARE_PACKAGE_MANIFEST_TYPE_GUID       = "e328ae6e-0b16-4490-9883-c953b4258841";
    public static final String SOFTWARE_PACKAGE_MANIFEST_TYPE_NAME       = "SoftwarePackageManifest";
    /* Classification attached to Collection */

    public static final String HOST_CLUSTER_MEMBER_TYPE_GUID             = "1a1c3933-a583-4b0c-9e42-c3691296a8e0";  /* from Area 0 */
    public static final String HOST_CLUSTER_MEMBER_TYPE_NAME             = "HostClusterMember";
    /* End1 = HostCluster; End2 = Host (Member) */

    public static final String DEPLOYED_VIRTUAL_CONTAINER_TYPE_GUID      = "4b981d89-e356-4d9b-8f17-b3a8d5a86676";  /* from Area 0 */
    public static final String DEPLOYED_VIRTUAL_CONTAINER_TYPE_NAME      = "DeployedVirtualContainer";
    /* End1 = Host; End2 = VirtualContainer (running on host) */

    public static final String SOFTWARE_SERVER_PLATFORM_TYPE_GUID        = "ba7c7884-32ce-4991-9c41-9778f1fec6aa";
    public static final String SOFTWARE_SERVER_PLATFORM_TYPE_NAME        = "SoftwareServerPlatform";
    /* ITInfrastructure */

    public static final String DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME     = "deployedImplementationType"; /* from SoftwareServerPlatform */
    public static final String DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME_DEP = "type";                       /* from SoftwareServerPlatform */
    public static final String PLATFORM_VERSION_PROPERTY_NAME                 = "platformVersion";            /* from SoftwareServerPlatform */

    public static final String SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_GUID = "b909eb3b-5205-4180-9f63-122a65b30738";  /* from Area 0 */
    public static final String SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_TYPE_NAME = "SoftwareServerPlatformDeployment";
    /* End1 = Host; End2 = SoftwareServerPlatform (running on host) */

    public static final String DEPLOYMENT_TIME_PROPERTY_NAME             = "deploymentTime";          /* from multiple */
    public static final String DEPLOYER_PROPERTY_NAME                    = "deployer";                /* from multiple */
    public static final String PLATFORM_STATUS_PROPERTY_NAME             = "platformStatus";            /* from SoftwareServerPlatform */

    public static final String OPERATIONAL_STATUS_ENUM_TYPE_GUID         = "24e1e33e-9250-4a6c-8b07-05c7adec3a1d";
    public static final String OPERATIONAL_STATUS_ENUM_TYPE_NAME         = "OperationalStatus";              /* from Area 1 */

    public static final String SOFTWARE_SERVER_TYPE_GUID                 = "896d14c2-7522-4f6c-8519-757711943fe6";
    public static final String SOFTWARE_SERVER_TYPE_NAME                 = "SoftwareServer";
    /* ITInfrastructure */

    public static final String SERVER_VERSION_PROPERTY_NAME         = "serverVersion";   /* from SoftwareServer entity */


    public static final String SERVER_ENDPOINT_TYPE_GUID     = "2b8bfab4-8023-4611-9833-82a0dc95f187";
    public static final String SERVER_ENDPOINT_TYPE_NAME     = "ServerEndpoint";
    /* End 1 = ITInfrastructure; End 2 = Endpoint */

    public static final String SERVER_DEPLOYMENT_TYPE_GUID   = "d909eb3b-5205-4180-9f63-122a65b30738";
    public static final String SERVER_DEPLOYMENT_TYPE_NAME   = "SoftwareServerDeployment";
    /* End 1 = SoftwareServerPlatform; End 2 = SoftwareServer */

    public static final String SERVER_STATUS_PROPERTY_NAME            = "serverStatus";  /* from SoftwareServerDeployment */

    public static final String ENDPOINT_TYPE_GUID                        = "dbc20663-d705-4ff0-8424-80c262c6b8e7";
    public static final String ENDPOINT_TYPE_NAME                        = "Endpoint";
    /* Referenceable */

    public static final String ENDPOINT_DISPLAY_NAME_PROPERTY_NAME       = "name";                                 /* from Endpoint entity */
    public static final String NETWORK_ADDRESS_PROPERTY_NAME             = "networkAddress";                       /* from Endpoint entity */
    public static final String PROTOCOL_PROPERTY_NAME                    = "protocol";                             /* from Endpoint entity */
    public static final String ENCRYPTION_METHOD_PROPERTY_NAME           = "encryptionMethod";                     /* from Endpoint entity */


    public static final String SOFTWARE_CAPABILITY_TYPE_GUID   = "54055c38-b9ad-4a66-a75b-14dc643d4c69";
    public static final String SOFTWARE_CAPABILITY_TYPE_NAME   = "SoftwareCapability";
    /* Referenceable */

    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID   = "fe30a033-8f86-4d17-8986-e6166fa24177";
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_NAME   = "SoftwareServerCapability";
    /* SoftwareCapability */

    public static final String CAPABILITY_TYPE_PROPERTY_NAME        = "capabilityType";             /* from SoftwareCapability entity */
    public static final String CAPABILITY_TYPE_PROPERTY_NAME_DEP1   = "type";                       /* from SoftwareServerCapability entity */
    public static final String CAPABILITY_TYPE_PROPERTY_NAME_DEP2   = "deployedImplementationType"; /* from SoftwareServerCapability entity */
    public static final String CAPABILITY_VERSION_PROPERTY_NAME     = "capabilityVersion";          /* from SoftwareCapability entity */
    public static final String CAPABILITY_VERSION_PROPERTY_NAME_DEP = "version";       /* from SoftwareCapability entity */
    public static final String PATCH_LEVEL_PROPERTY_NAME            = "patchLevel";    /* from SoftwareCapability entity */

    public static final String SUPPORTED_CAPABILITY_TYPE_GUID         = "2480aa71-44c5-414d-8b32-9c4340786d77";
    public static final String SUPPORTED_CAPABILITY_TYPE_NAME         = "SupportedSoftwareCapability";
    /* End 1 = ITInfrastructure; End 2 = SoftwareCapability */

    public static final String SERVER_CAPABILITY_STATUS_PROPERTY_NAME = "serverCapabilityStatus";  /* from SoftwareServerSupportedCapability */

    public static final String SERVER_ASSET_USE_TYPE_GUID                = "56315447-88a6-4235-ba91-fead86524ebf";  /* from Area 0 */
    public static final String SERVER_ASSET_USE_TYPE_NAME                = "ServerAssetUse";
    /* End1 = SoftwareCapability; End 2 = Asset */

    public static final String USE_TYPE_PROPERTY_NAME                    = "useType";                       /* from ServerAssetUse relationship */
    public static final String MINIMUM_INSTANCES_PROPERTY_NAME           = "minimumInstances";              /* from ServerAssetUse relationship */
    public static final String MAXIMUM_INSTANCES_PROPERTY_NAME           = "maximumInstances";              /* from ServerAssetUse relationship */


    public static final String SERVER_ASSET_USE_TYPE_TYPE_GUID          = "09439481-9489-467c-9ae5-178a6e0b6b5a";  /* from Area 0 */
    public static final String SERVER_ASSET_USE_TYPE_TYPE_NAME          = "ServerAssetUseType";
    public static final int SERVER_ASSET_USE_TYPE_OWNS_ORDINAL          = 0;
    public static final int SERVER_ASSET_USE_TYPE_GOVERNS_ORDINAL       = 1;
    public static final int SERVER_ASSET_USE_TYPE_MAINTAINS_ORDINAL     = 2;
    public static final int SERVER_ASSET_USE_TYPE_USES_ORDINAL          = 3;
    public static final int SERVER_ASSET_USE_TYPE_OTHER_ORDINAL         = 99;

    public static final String APPLICATION_TYPE_GUID                  = "58280f3c-9d63-4eae-9509-3f223872fb25";
    public static final String APPLICATION_TYPE_NAME                  = "Application";
    /* SoftwareServerCapability */

    public static final String API_MANAGER_TYPE_GUID                  = "283a127d-3acd-4d64-b558-1fce9db9a35b";
    public static final String API_MANAGER_TYPE_NAME                  = "APIManager";
    /* SoftwareServerCapability */

    public static final String EVENT_BROKER_TYPE_GUID                 = "309dfc3c-663b-4732-957b-e4a084436314";
    public static final String EVENT_BROKER_TYPE_NAME                 = "EventBroker";
    /* SoftwareServerCapability */

    public static final String DATA_MANAGER_TYPE_GUID                 = "82efa1fa-501f-4ac7-942c-6536c4a1cd61";
    public static final String DATA_MANAGER_TYPE_NAME                 = "DataManager";
    /* SoftwareServerCapability */

    public static final String CATALOG_TYPE_GUID                      = "f4fffcc0-d9eb-4bb9-8aff-0718932f689e";
    public static final String CATALOG_TYPE_NAME                      = "Catalog";
    /* SoftwareServerCapability */

    public static final String ENGINE_TYPE_GUID                       = "3566527f-b1bd-4e7a-873e-a3e04d5f2a14";
    public static final String ENGINE_TYPE_NAME                       = "Engine";
    /* SoftwareServerCapability */

    public static final String WORKFLOW_ENGINE_CLASSIFICATION_GUID   = "37a6d212-7c4a-4a82-b4e2-601d4358381c";
    public static final String WORKFLOW_ENGINE_CLASSIFICATION_NAME   = "WorkflowEngine";
    /* Engine */

    public static final String REPORTING_ENGINE_CLASSIFICATION_GUID   = "e07eefaa-16e0-46cf-ad54-bed47fb15812";
    public static final String REPORTING_ENGINE_CLASSIFICATION_NAME   = "ReportingEngine";
    /* Engine */

    public static final String ANALYTICS_ENGINE_CLASSIFICATION_GUID   = "1a0dc6f6-7980-42f5-98bd-51e56543a07e";
    public static final String ANALYTICS_ENGINE_CLASSIFICATION_NAME   = "AnalyticsEngine";
    /* Engine */

    public static final String DATA_MOVEMENT_ENGINE_CLASSIFICATION_GUID = "d2ed6621-9d99-4fe8-843a-b28d816cf888";
    public static final String DATA_MOVEMENT_ENGINE_CLASSIFICATION_NAME = "DataMovementEngine";
    /* Engine */

    public static final String DATA_VIRTUALIZATION_ENGINE_CLASSIFICATION_GUID = "03e25cd0-03d7-4d96-b28b-eed671824ed6";
    public static final String DATA_VIRTUALIZATION_ENGINE_CLASSIFICATION_NAME = "DataVirtualizationEngine";
    /* Engine */

    public static final String ASSET_MANAGER_TYPE_GUID = "03170ce7-edf1-4e94-b6ab-2d5cbbf1f13c";
    public static final String ASSET_MANAGER_TYPE_NAME = "AssetManager";
    /* Attaches to Referenceable - preferably SoftwareServer */

    public static final String USER_PROFILE_MANAGER_TYPE_GUID = "53ef4062-9e0a-4892-9824-8d51d4ad59d3";
    public static final String USER_PROFILE_MANAGER_TYPE_NAME = "UserProfileManager";
    /* Attaches to Referenceable - preferably SoftwareServer */

    public static final String USER_ACCESS_DIRECTORY_TYPE_GUID = "29c98cf7-32b3-47d2-a411-48c1c9967e6d";
    public static final String USER_ACCESS_DIRECTORY_TYPE_NAME = "UserAccessDirectory";
    /* Attaches to Referenceable - preferably SoftwareServer */

    public static final String MASTER_DATA_MANAGER_TYPE_GUID = "5bdad12e-57e7-4ff9-b7be-5d869e77d30b";
    public static final String MASTER_DATA_MANAGER_TYPE_NAME = "MasterDataManager";
    /* Attaches to Referenceable - preferably SoftwareServer */

    public static final String SOFTWARE_SERVICE_TYPE_GUID = "f3f69251-adb1-4042-9d95-70082f95a028";
    public static final String SOFTWARE_SERVICE_TYPE_NAME = "SoftwareService";
    /* SoftwareServerCapability */

    public static final String APPLICATION_SERVICE_TYPE_GUID = "5b7f340e-7dc9-45c0-a636-c20605147c94";
    public static final String APPLICATION_SERVICE_TYPE_NAME = "ApplicationService";
    /* SoftwareService */

    public static final String METADATA_INTEGRATION_SERVICE_TYPE_GUID = "92f7fe27-cd2f-441c-a084-156821aa5bca8";
    public static final String METADATA_INTEGRATION_SERVICE_TYPE_NAME = "MetadataIntegrationService";
    /* SoftwareService */

    public static final String METADATA_ACCESS_SERVICE_TYPE_GUID = "0bc3a16a-e8ed-4ad0-a302-0773365fdef0";
    public static final String METADATA_ACCESS_SERVICE_TYPE_NAME = "MetadataAccessService";
    /* SoftwareService */

    public static final String ENGINE_HOSTING_SERVICE_TYPE_GUID = "90880f0b-c7a3-4d1d-93cc-0b877f27cd33";
    public static final String ENGINE_HOSTING_SERVICE_TYPE_NAME = "EngineHostingService";
    /* SoftwareService */

    public static final String USER_VIEW_SERVICE_TYPE_GUID = "1f83fc7c-75bb-491d-980d-ff9a6f80ae02";
    public static final String USER_VIEW_SERVICE_TYPE_NAME = "UserViewService";
    /* SoftwareService */

    public static final String NETWORK_TYPE_GUID                     = "e0430f59-f021-411a-9d81-883e1ff3f6f6";
    public static final String NETWORK_TYPE_NAME                     = "Network";
    /* ITInfrastructure */

    public static final String NETWORK_GATEWAY_TYPE_GUID             = "9bbae94d-e109-4c96-b072-4f97123f04fd";
    public static final String NETWORK_GATEWAY_TYPE_NAME             = "NetworkGateway";
    /* SoftwareServerCapability */

    public static final String HOST_NETWORK_TYPE_GUID                = "f2bd7401-c064-41ac-862c-e5bcdc98fa1e";  /* from Area 0 */
    public static final String HOST_NETWORK_TYPE_NAME                = "HostNetwork";
    /* End1 = Host; End2 = Network */

    public static final String NETWORK_GATEWAY_LINK_TYPE_GUID        = "5bece460-1fa6-41fb-a29f-fdaf65ec8ce3";  /* from Area 0 */
    public static final String NETWORK_GATEWAY_LINK_TYPE_NAME        = "NetworkGatewayLink";
    /* End1 = NetworkGateway; End2 = Network */

    public static final String CLOUD_PROVIDER_CLASSIFICATION_GUID    = "a2bfdd08-d0a8-49db-bc97-7f240628104";
    public static final String CLOUD_PROVIDER_CLASSIFICATION_NAME    = "CloudProvider";
    /* Host */

    public static final String PROVIDER_NAME_PROPERTY_NAME           = "providerName";  /* from CloudProvider */

    public static final String CLOUD_PLATFORM_CLASSIFICATION_GUID    = "1b8f8511-e606-4f65-86d3-84891706ad12";
    public static final String CLOUD_PLATFORM_CLASSIFICATION_NAME    = "CloudPlatform";
    /* SoftwareServerPlatform */

    public static final String CLOUD_TENANT_CLASSIFICATION_GUID      = "1b8f8522-e606-4f65-86d3-84891706ad12";
    public static final String CLOUD_TENANT_CLASSIFICATION_NAME      = "CloudTenant";
    /* SoftwareServer */

    public static final String TENANT_NAME_PROPERTY_NAME             = "TenantName";  /* from CloudTenant */
    public static final String TENANT_TYPE_PROPERTY_NAME             = "TenantType";  /* from CloudTenant */

    public static final String CLOUD_SERVICE_CLASSIFICATION_GUID     = "337e7b1a-ad4b-4818-aa3e-0ff3307b2fbe6";
    public static final String CLOUD_SERVICE_CLASSIFICATION_NAME     = "CloudService";
    /* SoftwareServerCapability */

    public static final String OFFERING_NAME_PROPERTY_NAME           = "offeringName";  /* from CloudService */
    public static final String SERVICE_TYPE_PROPERTY_NAME            = "serviceType";   /* from CloudService */

    /* ============================================================================================================================*/
    /* Area 1 - Collaboration                                                                                                      */
    /* ============================================================================================================================*/

    public static final String ACTOR_PROFILE_TYPE_GUID                   = "5a2f38dc-d69d-4a6f-ad26-ac86f118fa35";
    public static final String ACTOR_PROFILE_TYPE_NAME                   = "ActorProfile";                 /* from Area 1 */
    /* Referenceable */

    public static final String USER_IDENTITY_TYPE_GUID                   = "fbe95779-1f3c-4ac6-aa9d-24963ff16282";
    public static final String USER_IDENTITY_TYPE_NAME                   = "UserIdentity";
    /* Referenceable */

    public static final String PROFILE_IDENTITY_RELATIONSHIP_TYPE_GUID   = "01664609-e777-4079-b543-6baffe910ff1";   /* from Area 1 */
    public static final String PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME   = "ProfileIdentity";
    /* End1 = ActorProfile; End 2 = UserIdentity */

    public static final String ROLE_TYPE_NAME_PROPERTY_NAME              = "roleTypeName"; /* from ProfileIdentity relationship */
    public static final String ROLE_GUID_PROPERTY_NAME                   = "roleGUID";     /* from ProfileIdentity relationship */

    public static final String CONTACT_DETAILS_TYPE_GUID                 = "79296df8-645a-4ef7-a011-912d1cdcf75a";
    public static final String CONTACT_DETAILS_TYPE_NAME                 = "ContactDetails";

    public static final String CONTACT_TYPE_PROPERTY_NAME                = "contactType";          /* from ContactDetail entity */
    public static final String CONTACT_METHOD_TYPE_PROPERTY_NAME         = "contactMethodType";    /* from ContactDetail entity */
    public static final String CONTACT_METHOD_SERVICE_PROPERTY_NAME      = "contactMethodService"; /* from ContactDetail entity */
    public static final String CONTACT_METHOD_VALUE_PROPERTY_NAME        = "contactMethodValue";   /* from ContactDetail entity */

    public static final String CONTACT_METHOD_TYPE_ENUM_TYPE_GUID        = "30e7d8cd-df01-46e8-9247-a24c5650910d";
    public static final String CONTACT_METHOD_TYPE_ENUM_TYPE_NAME        = "ContactMethodType";

    public static final String CONTACT_THROUGH_RELATIONSHIP_TYPE_GUID    = "6cb9af43-184e-4dfa-854a-1572bcf0fe75";   /* from Area 1 */
    public static final String CONTACT_THROUGH_RELATIONSHIP_TYPE_NAME    = "ContactThrough";
    /* End1 = ActorProfile; End 2 = ContactDetails */

    public static final String PERSON_TYPE_GUID                          = "ac406bf8-e53e-49f1-9088-2af28bbbd285";
    public static final String PERSON_TYPE_NAME                          = "Person";                 /* from Area 1 */
    /* ActorProfile */

    public static final String TITLE_PROPERTY_NAME                       = "title";          /* from Person entity */
    public static final String INITIALS_PROPERTY_NAME                    = "initials";       /* from Person entity */
    public static final String GIVEN_NAMES_PROPERTY_NAME                 = "givenNames";     /* from Person entity */
    public static final String SURNAME_PROPERTY_NAME                     = "surname";        /* from Person entity */
    public static final String FULL_NAME_PROPERTY_NAME                   = "fullName";       /* from Person entity */
    public static final String PRONOUNS_PROPERTY_NAME                    = "pronouns";       /* from Person entity */
    public static final String PREFERRED_LANGUAGE_PROPERTY_NAME          = "preferredLanguage";  /* from Person entity */
    public static final String JOB_TITLE_PROPERTY_NAME                   = "jobTitle";       /* from Person entity */
    public static final String EMPLOYEE_NUMBER_PROPERTY_NAME             = "employeeNumber"; /* from Person entity */
    public static final String EMPLOYEE_TYPE_PROPERTY_NAME               = "employeeType";   /* from Person entity */

    public static final String PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID  = "4a316abe-eeee-4d11-ad5a-4bfb4079b80b";   /* from Area 1 */
    public static final String PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_NAME  = "PersonalContribution";
    /* End1 = Person; End 2 = ContributionRecord */

    public static final String CONTRIBUTION_RECORD_TYPE_GUID             = "ac406bf8-e53e-49f1-9088-2af28cccd285";
    public static final String CONTRIBUTION_RECORD_TYPE_NAME             = "ContributionRecord";                 /* from Area 1 */
    /* Referenceable */

    public static final String KARMA_POINTS_PROPERTY_NAME                = "karmaPoints";   /* from ContributionRecord entity */

    public static final String PERSON_ROLE_TYPE_GUID                     = "ac406bf8-e53e-49f1-9088-2af28bcbd285";
    public static final String PERSON_ROLE_TYPE_NAME                     = "PersonRole";
    /* Referenceable */

    public static final String HEAD_COUNT_PROPERTY_NAME                  = "headCount";   /* from PersonRole entity */

    public static final String PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_GUID  = "4a316abe-bcce-4d11-ad5a-4bfb4079b80b";   /* from Area 1 */
    public static final String PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME  = "PersonRoleAppointment";
    /* End1 = Person; End 2 = PersonRole */

    public static final String PEER_RELATIONSHIP_TYPE_GUID               = "4a316abe-bccd-4d11-ad5a-4bfb4079b80b";   /* from Area 1 */
    public static final String PEER_RELATIONSHIP_TYPE_NAME               = "Peer";
    /* End1 = Person; End 2 = Person */

    public static final String TEAM_TYPE_GUID                            = "36db26d5-aba2-439b-bc15-d62d373c5db6";
    public static final String TEAM_TYPE_NAME                            = "Team";                 /* from Area 1 */
    /* ActorProfile */

    public static final String TEAM_STRUCTURE_RELATIONSHIP_TYPE_GUID     = "5ebc4fb2-b62a-4269-8f18-e9237a2229ca";   /* from Area 1 */
    public static final String TEAM_STRUCTURE_RELATIONSHIP_TYPE_NAME     = "TeamStructure";
    /* End1 = Team; End 2 = Team */

    public static final String DELEGATION_ESCALATION_PROPERTY_NAME       = "delegationEscalationAuthority";   /* from TeamStructure relationship */

    public static final String TEAM_MEMBER_TYPE_GUID                     = "46db26d5-abb2-538b-bc15-d62d373c5db6";
    public static final String TEAM_MEMBER_TYPE_NAME                     = "TeamMember";                 /* from Area 1 */
    /* PersonRole */

    public static final String TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_GUID    = "1ebc4fb2-b62a-4269-8f18-e9237a2119ca";   /* from Area 1 */
    public static final String TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_NAME    = "TeamMembership";
    /* End1 = TeamMember; End 2 = Team */

    public static final String TEAM_LEADER_TYPE_GUID                     = "36db26d5-abb2-439b-bc15-d62d373c5db6";
    public static final String TEAM_LEADER_TYPE_NAME                     = "TeamLeader";                 /* from Area 1 */
    /* PersonRole */

    public static final String TEAM_LEADERSHIP_RELATIONSHIP_TYPE_GUID    = "5ebc4fb2-b62a-4269-8f18-e9237a2119ca";   /* from Area 1 */
    public static final String TEAM_LEADERSHIP_RELATIONSHIP_TYPE_NAME    = "TeamLeadership";
    /* End1 = TeamLeader; End 2 = Team */

    public static final String IT_PROFILE_TYPE_GUID                      = "81394f85-6008-465b-926e-b3fae4668937";
    public static final String IT_PROFILE_TYPE_NAME                      = "ITProfile";                 /* from Area 1 */
    /* ActorProfile */

    public static final String IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_GUID = "4c579e3d-a4ff-41c1-9931-33e6fc992f2b";   /* from Area 1 */
    public static final String IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_NAME = "ITInfrastructureProfile";
    /* End1 = ITInfrastructure; End 2 = ITProfile */

    public static final String ASSIGNMENT_SCOPE_RELATIONSHIP_TYPE_GUID   = "e3fdafe3-692a-46c6-a595-c538cc189dd9";   /* from Area 1 */
    public static final String ASSIGNMENT_SCOPE_RELATIONSHIP_TYPE_NAME   = "AssignmentScope";
    /* End1 = assignedActors - Referenceable; End 2 = assignedScope - Referenceable */

    public static final String ASSIGNMENT_TYPE_PROPERTY_NAME             = "assignmentType";                          /* from Area 1 */

    public static final String PROJECT_TYPE_GUID                         = "0799569f-0c16-4a1f-86d9-e2e89568f7fd";
    public static final String PROJECT_TYPE_NAME                         = "Project";   /* from Area 1 */
    /* Referenceable */

    public static final String CAMPAIGN_CLASSIFICATION_TYPE_NAME           = "Campaign";          /* from Area 1 */
    public static final String TASK_CLASSIFICATION_TYPE_NAME               = "Task";              /* from Area 1 */

    public static final String PROJECT_STATUS_PROPERTY_NAME              = "projectStatus";                     /* from Area 1 */
    public static final String TEAM_ROLE_PROPERTY_NAME                   = "teamRole";                          /* from Area 1 */
    public static final String DEPENDENCY_SUMMARY_PROPERTY_NAME          = "dependencySummary";                 /* from Area 1 */

    public static final String PROJECT_TEAM_RELATIONSHIP_TYPE_GUID       = "746875af-2e41-4d1f-864b-35265df1d5dc";
    public static final String PROJECT_TEAM_RELATIONSHIP_TYPE_NAME       = "ProjectTeam";   /* from Area 1 */
    /* End1 = Project; End 2 = ActorProfile */

    public static final String PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_GUID = "ac63ac45-a4d0-4fba-b583-92859de77dd8";
    public static final String PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME = "ProjectManagement";   /* from Area 1 */
    /* End1 = Project; End 2 = PersonRole */

    public static final String PROJECT_HIERARCHY_RELATIONSHIP_TYPE_GUID  = "8f1134f6-b9fe-4971-bc57-6e1b8b302b55";
    public static final String PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME  = "ProjectHierarchy";   /* from Area 1 */
    /* End1 = managingProject; End 2 = managedProject */

    public static final String PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_GUID = "5b6a56f1-68e2-4e10-85f0-fda47a4263fd";
    public static final String PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_NAME = "ProjectDependency";   /* from Area 1 */
    /* End1 = dependentProject; End 2 = dependsOnProject */

    public static final String STAKEHOLDER_RELATIONSHIP_TYPE_GUID        = "efd8a136-0aea-4668-b91a-30f947e38b82";
    public static final String STAKEHOLDER_RELATIONSHIP_TYPE_NAME        = "Stakeholder";   /* from Area 1 */
    /* End1 = commissioned - Referenceable; End 2 = commissionedBy - Referenceable */

    public static final String STAKEHOLDER_ROLE_PROPERTY_NAME            = "stakeholderRole";                          /* from Area 1 */

    public static final String MEETING_TYPE_GUID                         = "6bf90c79-32f4-47ad-959c-8fff723fe744";
    public static final String MEETING_TYPE_NAME                         = "Meeting";   /* from Area 1 */
    /* Referenceable */

    public static final String START_TIME_PROPERTY_NAME                  = "startTime";                          /* from Area 1 */
    public static final String END_TIME_PROPERTY_NAME                    = "endTime";                          /* from Area 1 */
    public static final String OBJECTIVE_PROPERTY_NAME                   = "objective";                          /* from Area 1 */
    public static final String MINUTES_PROPERTY_NAME                     = "minutes";                          /* from Area 1 */
    public static final String MEETING_TYPE_PROPERTY_NAME                = "meetingType";                          /* from Area 1 */

    public static final String MEETINGS_RELATIONSHIP_TYPE_GUID           = "a05f918e-e7e2-419d-8016-5b37406df63a";
    public static final String MEETINGS_RELATIONSHIP_TYPE_NAME           = "Meetings";   /* from Area 1 */
    /* End1 = Meeting; End 2 = Referenceable */

    public static final String TO_DO_TYPE_GUID                           = "93dbc58d-c826-4bc2-b36f-195148d46f86";
    public static final String TO_DO_TYPE_NAME                           = "ToDo";   /* from Area 1 */
    /* Referenceable */

    public static final String CREATION_TIME_PROPERTY_NAME               = "creationTime";                          /* from Area 1 */
    public static final String PRIORITY_PROPERTY_NAME                    = "priority";                          /* from Area 1 */
    public static final String DUE_TIME_PROPERTY_NAME                    = "dueTime";                          /* from Area 1 */
    public static final String COMPLETION_TIME_PROPERTY_NAME             = "completionTime";                          /* from Area 1 */
    public static final String STATUS_PROPERTY_NAME                      = "status";                          /* from Area 1 */

    public static final String TO_DO_STATUS_ENUM_TYPE_GUID               = "7197ea39-334d-403f-a70b-d40231092df7";  /* from Area 0 */
    public static final String TO_DO_STATUS_ENUM_TYPE_NAME               = "ToDoStatus";
    public static final int     TO_DO_STATUS_ENUM_OPEN_ORDINAL              = 0;
    public static final int     TO_DO_STATUS_ENUM_IN_PROGRESS_ORDINAL       = 1;
    public static final int     TO_DO_STATUS_ENUM_WAITING_ORDINAL           = 2;
    public static final int     TO_DO_STATUS_ENUM_COMPLETE_ORDINAL          = 3;
    public static final int     TO_DO_STATUS_ENUM_ABANDONED_ORDINAL         = 4;

    public static final String TO_DO_SOURCE_RELATIONSHIP_TYPE_GUID       = "a0b7ba50-4c97-4b76-9a7d-c6a00e1be646";
    public static final String TO_DO_SOURCE_RELATIONSHIP_TYPE_NAME       = "ToDoSource";   /* from Area 1 */
    /* End1 = source -Referenceable; End 2 = To Do */

    public static final String ACTIONS_RELATIONSHIP_TYPE_GUID            = "aca1277b-bf1c-42f5-9b3b-fbc2c9047325";
    public static final String ACTIONS_RELATIONSHIP_TYPE_NAME            = "Actions";   /* from Area 1 */
    /* End1 = originator - Referenceable; End 2 = To Do */

    public static final String ACTION_TARGET_RELATIONSHIP_TYPE_GUID      = "207e2594-e3e4-4be8-a12c-4c401656e241";
    public static final String ACTION_TARGET_RELATIONSHIP_TYPE_NAME      = "ActionTarget";   /* from Area 1 */
    /* End1 = To Do; End 2 = Referenceable */

    public static final String ACTION_ASSIGNMENT_RELATIONSHIP_TYPE_GUID  = "af2b5fab-8f83-4a2b-b749-1e6219f61f79";
    public static final String ACTION_ASSIGNMENT_RELATIONSHIP_TYPE_NAME  = "ActionAssignment";   /* from Area 1 */
    /* End1 = PersonRole; End 2 = To Do */

    public static final String COMMUNITY_TYPE_GUID                       = "ba846a7b-2955-40bf-952b-2793ceca090a";
    public static final String COMMUNITY_TYPE_NAME                       = "Community";          /* from Area 1 */
    /* Referenceable */

    public static final String COMMUNITY_MEMBER_TYPE_GUID                = "fbd42379-f6c3-4f09-b6f7-378565cda993";
    public static final String COMMUNITY_MEMBER_TYPE_NAME                = "CommunityMember";    /* from Area 1 */
    /* PersonRole */

    public static final String COMMUNITY_MEMBERSHIP_TYPE_ENUM_TYPE_GUID  = "b0ef45bf-d12b-4b6f-add6-59c14648d750";
    public static final String COMMUNITY_MEMBERSHIP_TYPE_ENUM_TYPE_NAME  = "CommunityMembershipType";

    public static final String COMMUNITY_MEMBERSHIP_TYPE_GUID            = "7c7da1a3-01b3-473e-972e-606eff0cb112";
    public static final String COMMUNITY_MEMBERSHIP_TYPE_NAME            = "CommunityMembership";
    /* End1 = Community; End 2 = CommunityMember */

    public static final String MISSION_PROPERTY_NAME                     = "mission";
    public static final String MEMBERSHIP_TYPE_PROPERTY_NAME             = "membershipType";

    public static final String INFORMAL_TAG_TYPE_GUID                    = "ba846a7b-2955-40bf-952b-2793ceca090a";
    public static final String INFORMAL_TAG_TYPE_NAME                    = "InformalTag";          /* from Area 1 */

    public static final String TAG_IS_PUBLIC_PROPERTY_NAME               = "isPublic"; /* from InformalTag entity and AttachedTag relationship */
    public static final String TAG_NAME_PROPERTY_NAME                    = "tagName";              /* from InformalTag entity */
    public static final String TAG_DESCRIPTION_PROPERTY_NAME             = "tagDescription";       /* from InformalTag entity */
    public static final String TAG_USER_PROPERTY_NAME                    = "createdBy";            /* From Audit Header */

    public static final String REFERENCEABLE_TO_TAG_TYPE_GUID            = "4b1641c4-3d1a-4213-86b2-d6968b6c65ab";
    public static final String REFERENCEABLE_TO_TAG_TYPE_NAME            = "AttachedTag";
    /* End1 = Referenceable; End 2 = InformalTag */
    /* Also isPublic */

    public static final String LIKE_TYPE_GUID                            = "deaa5ca0-47a0-483d-b943-d91c76744e01";
    public static final String LIKE_TYPE_NAME                            = "Like";          /* from Area 1 */

    public static final String REFERENCEABLE_TO_LIKE_TYPE_GUID                   = "e2509715-a606-415d-a995-61d00503dad4";
    public static final String REFERENCEABLE_TO_LIKE_TYPE_NAME                   = "AttachedLike";
    /* End1 = Referenceable; End 2 = Like */

    public static final String LIKE_IS_PUBLIC_PROPERTY_NAME              = "isPublic";      /* from AttachedLike relationship*/

    public static final String RATING_TYPE_GUID                          = "7299d721-d17f-4562-8286-bcd451814478";
    public static final String RATING_TYPE_NAME                          = "Rating";          /* from Area 1 */

    public static final String STAR_RATING_ENUM_TYPE_GUID                = "77fea3ef-6ec1-4223-8408-38567e9d3c93";
    public static final String STAR_RATING_ENUM_TYPE_NAME                = "StarRating";              /* from Area 1 */

    public static final String STARS_PROPERTY_NAME                       = "stars";           /* from Rating entity */
    /* StarRating enum */
    public static final String REVIEW_PROPERTY_NAME                      = "review";          /* from Rating entity */

    public static final String REFERENCEABLE_TO_RATING_TYPE_GUID         = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";
    public static final String REFERENCEABLE_TO_RATING_TYPE_NAME         = "AttachedRating";
    /* End1 = Referenceable; End 2 = Rating */

    public static final String RATING_IS_PUBLIC_PROPERTY_NAME            = "isPublic";        /* from AttachedRating relationship */

    public static final String COMMENT_TYPE_GUID                         = "1a226073-9c84-40e4-a422-fbddb9b84278";
    public static final String COMMENT_TYPE_NAME                         = "Comment";              /* from Area 1 */
    /* Referenceable */

    public static final String COMMENT_TEXT_PROPERTY_NAME                = "text";          /* from Comment entity */
    public static final String COMMENT_TYPE_PROPERTY_NAME                = "commentType";   /* from Comment entity */
    public static final String COMMENT_TYPE_PROPERTY_NAME_DEP            = "type";          /* from Comment entity */

    public static final String COMMENT_TYPE_ENUM_TYPE_GUID               = "06d5032e-192a-4f77-ade1-a4b97926e867";
    public static final String COMMENT_TYPE_ENUM_TYPE_NAME               = "CommentType";              /* from Area 1 */

    public static final String REFERENCEABLE_TO_COMMENT_TYPE_GUID        = "0d90501b-bf29-4621-a207-0c8c953bdac9";
    public static final String REFERENCEABLE_TO_COMMENT_TYPE_NAME        = "AttachedComment";
    /* End1 = Referenceable; End 2 = Comment */

    public static final String ANSWER_RELATIONSHIP_TYPE_GUID             = "ecf1a3ca-adc5-4747-82cf-10ec590c5c69";
    public static final String ANSWER_RELATIONSHIP_TYPE_NAME             = "AcceptedAnswer";
    /* End1 = Comment; End 2 = Comment */

    public static final String TEXT_PROPERTY_NAME                        = "text";   /* from NoteEntry entity */

    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID      = "4f798c0c-6769-4a2d-b489-d2714d89e0a4";
    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME      = "AttachedNoteLog";
    /* End1 = Referenceable; End 2 = NoteLog */
    /* And isPublicProperty */

    public static final String NOTE_LOG_TYPE_GUID                       = "646727c7-9ad4-46fa-b660-265489ad96c6";
    public static final String NOTE_LOG_TYPE_NAME                       = "NoteLog";              /* from Area 1 */
    /* Referenceable */

    public static final String NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_GUID  = "38edecc6-f385-4574-8144-524a44e3e712";
    public static final String NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_NAME  = "AttachedNoteLogEntry";
    /* End1 = NoteLog; End 2 = NoteEntry */


    /*
    Added warning suppression for SonarCloud since the below constants contain the pattern
         ..AUTH..="GUID"
         which looks exactly like
         myAuth = SECURITY_SECRET
         which is reported as a security risk.
    */
    @SuppressWarnings("java:S6418")
    public static final String NOTE_LOG_AUTHOR_RELATIONSHIP_TYPE_GUID  = "8f798c0c-6769-4a2d-b489-12714d89e0a4";

    public static final String NOTE_LOG_AUTHOR_RELATIONSHIP_TYPE_NAME  = "NoteLogAuthorship";
    /* End1 = NoteLogAuthor; End 2 = NoteLog */

    @SuppressWarnings("java:S6418")
    public static final String NOTE_LOG_AUTHOR_TYPE_GUID                = "3a84d94c-ac6f-4be1-a72a-07dbec7b1fe3";
    public static final String NOTE_LOG_AUTHOR_TYPE_NAME                = "NoteLogAuthor";              /* from Area 1 */
    /* PersonRole */

    public static final String NOTE_TYPE_GUID                           = "2a84d94c-ac6f-4be1-a72a-07dcec7b1fe3";
    public static final String NOTE_TYPE_NAME                            = "NoteEntry";              /* from Area 1 */
    /* Referenceable */

    /* ============================================================================================================================*/
    /* Area 2 - Assets                                                                                                             */
    /* ============================================================================================================================*/

    public static final String CONNECTION_TYPE_GUID                      = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
    public static final String CONNECTION_TYPE_NAME                      = "Connection";
    /* Referenceable */

    public static final String SECURED_PROPERTIES_PROPERTY_NAME          = "securedProperties";                    /* from Connection entity */
    public static final String CONFIGURATION_PROPERTIES_PROPERTY_NAME    = "configurationProperties";              /* from Connection entity */
    public static final String USER_ID_PROPERTY_NAME                     = "userId";                               /* from Connection entity */
    public static final String CLEAR_PASSWORD_PROPERTY_NAME              = "clearPassword";                        /* from Connection entity */
    public static final String ENCRYPTED_PASSWORD_PROPERTY_NAME          = "encryptedPassword";                    /* from Connection entity */

    public static final String CONNECTION_ENDPOINT_TYPE_GUID             = "887a7132-d6bc-4b92-a483-e80b60c86fb2";
    public static final String CONNECTION_ENDPOINT_TYPE_NAME             = "ConnectionEndpoint";
    /* End1 = Endpoint; End 2 = Connection */

    public static final String CONNECTION_CONNECTOR_TYPE_TYPE_GUID       = "e542cfc1-0b4b-42b9-9921-f0a5a88aaf96";
    public static final String CONNECTION_CONNECTOR_TYPE_TYPE_NAME       = "ConnectionConnectorType";
    /* End1 = Connection; End 2 = ConnectorType */

    public static final String CONNECTOR_TYPE_TYPE_GUID                  = "954421eb-33a6-462d-a8ca-b5709a1bd0d4";
    public static final String CONNECTOR_TYPE_TYPE_NAME                  = "ConnectorType";
    /* Referenceable */

    public static final String CONNECTOR_CATEGORY_TYPE_GUID              = "fb60761f-7afd-4d3d-9efa-24bc85a7b22e";
    public static final String CONNECTOR_CATEGORY_TYPE_NAME              = "ConnectorCategory";
    /* Referenceable */

    public static final String CONNECTOR_TYPE_DIRECTORY_TYPE_GUID        = "9678ef11-ed7e-404b-a041-736df7514339";
    public static final String CONNECTOR_TYPE_DIRECTORY_TYPE_NAME        = "ConnectorTypeDirectory";
    /* Classification on Collection */

    public static final String CONNECTOR_IMPLEMENTATION_CHOICE_TYPE_GUID = "633648f3-c951-4ad7-b975-9fc04e0f3d2e";
    public static final String CONNECTOR_IMPLEMENTATION_CHOICE_TYPE_NAME = "ConnectorImplementationChoice";
    /* End1 = ConnectorCategory; End 2 = ConnectorType */

    public static final String SUPPORTED_ASSET_TYPE_NAME                 = "supportedAssetTypeName";               /* from ConnectorType entity */
    public static final String EXPECTED_DATA_FORMAT                      = "expectedDataFormat";                   /* from ConnectorType entity */
    public static final String CONNECTOR_PROVIDER_PROPERTY_NAME          = "connectorProviderClassName";           /* from ConnectorType entity */
    public static final String CONNECTOR_FRAMEWORK_NAME                  = "connectorFrameworkName";               /* from ConnectorType entity */
    public static final String CONNECTOR_FRAMEWORK_NAME_DEFAULT          = "Open Connector Framework (OCF)";
    public static final String CONNECTOR_INTERFACE_LANGUAGE              = "connectorInterfaceLanguage";           /* from ConnectorType entity */
    public static final String CONNECTOR_INTERFACE_LANGUAGE_DEFAULT      = "Java";
    public static final String CONNECTOR_INTERFACES                      = "connectorInterfaces";                  /* from ConnectorType entity */
    public static final String TARGET_TECHNOLOGY_SOURCE                  = "targetTechnologySource";               /* from ConnectorType and ConnectorCategory entity */
    public static final String TARGET_TECHNOLOGY_NAME                    = "targetTechnologyName";                 /* from ConnectorType and ConnectorCategory entity */
    public static final String TARGET_TECHNOLOGY_INTERFACES              = "targetTechnologyInterfaces";           /* from ConnectorType entity */
    public static final String TARGET_TECHNOLOGY_VERSIONS                = "targetTechnologyVersions";             /* from ConnectorType entity */
    public static final String RECOGNIZED_ADD_PROPS_PROPERTY_NAME        = "recognizedAdditionalProperties";       /* from ConnectorType and ConnectorCategory entity */
    public static final String RECOGNIZED_SEC_PROPS_PROPERTY_NAME        = "recognizedSecuredProperties";          /* from ConnectorType and ConnectorCategory entity */
    public static final String RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME     = "recognizedConfigurationProperties";    /* from ConnectorType and ConnectorCategory entity */

    public static final String VIRTUAL_CONNECTION_TYPE_GUID              = "82f9c664-e59d-484c-a8f3-17088c23a2f3";
    public static final String VIRTUAL_CONNECTION_TYPE_NAME              = "VirtualConnection";

    public static final String EMBEDDED_CONNECTION_TYPE_GUID             = "eb6dfdd2-8c6f-4f0d-a17d-f6ce4799f64f";
    public static final String EMBEDDED_CONNECTION_TYPE_NAME             = "EmbeddedConnection";
    /* End1 = VirtualConnection; End 2 = Connection */

    public static final String POSITION_PROPERTY_NAME                    = "position";              /* from EmbeddedConnection relationship */
    public static final String ARGUMENTS_PROPERTY_NAME                   = "arguments";             /* from EmbeddedConnection relationship */

    public static final String ASSET_TO_CONNECTION_TYPE_GUID             = "e777d660-8dbe-453e-8b83-903771f054c0";
    public static final String ASSET_TO_CONNECTION_TYPE_NAME             = "ConnectionToAsset";
    /* End1 = Connection; End 2 = Asset */

    public static final String ASSET_SUMMARY_PROPERTY_NAME               = "assetSummary";          /* from ConnectionToAsset relationship */

    public static final String DATA_STORE_TYPE_GUID                      = "10752b4a-4b5d-4519-9eae-fdd6d162122f";  /* from Area 2 */
    public static final String DATA_STORE_TYPE_NAME                      = "DataStore";
    /* Asset */

    public static final String PATH_NAME_PROPERTY_NAME                   = "pathName";                              /* from DataStore entity */
    public static final String STORE_CREATE_TIME_PROPERTY_NAME           = "storeCreateTime";                       /* from DataStore entity */
    public static final String STORE_CREATE_TIME_PROPERTY_NAME_DEP       = "createTime";                            /* from DataStore entity */
    public static final String STORE_UPDATE_TIME_PROPERTY_NAME           = "storeUpdateTime";                       /* from DataStore entity */
    public static final String STORE_UPDATE_TIME_PROPERTY_NAME_DEP       = "updateTime";                            /* from DataStore entity */

    public static final String DATA_STORE_ENCODING_CLASSIFICATION_GUID   = "f08e48b5-6b66-40f5-8ff6-c2bfe527330b";
    public static final String DATA_STORE_ENCODING_CLASSIFICATION_NAME   = "DataStoreEncoding";

    public static final String ENCODING_TYPE_PROPERTY_NAME               = "encoding";      /* from DataStoreEncoding classification */
    public static final String ENCODING_LANGUAGE_PROPERTY_NAME           = "language";      /* from DataStoreEncoding classification */
    public static final String ENCODING_DESCRIPTION_PROPERTY_NAME        = "description";   /* from DataStoreEncoding classification */
    public static final String ENCODING_PROPERTIES_PROPERTY_NAME         = "properties";    /* from DataStoreEncoding classification */

    public static final String DATA_FIELD_VALUES_CLASSIFICATION_GUID     = "740e76e1-77b4-4426-ad52-d0a4ed15fff9";
    public static final String DATA_FIELD_VALUES_CLASSIFICATION_NAME     = "DataFieldValues";

    public static final String DEFAULT_VALUE_PROPERTY_NAME               = "defaultValue";      /* from DataFieldValues classification */
    public static final String SAMPLE_VALUES_PROPERTY_NAME               = "sampleValues";      /* from DataFieldValues classification */
    public static final String DATA_PATTERN_PROPERTY_NAME                = "dataPattern";       /* from DataFieldValues classification */
    public static final String NAME_PATTERN_PROPERTY_NAME                = "namePattern";       /* from DataFieldValues classification */

    public static final String DATABASE_TYPE_GUID                        = "0921c83f-b2db-4086-a52c-0d10e52ca078";  /* from Area 2 */
    public static final String DATABASE_TYPE_NAME                        = "Database";
    /* DataStore */

    public static final String DATABASE_TYPE_PROPERTY_NAME               = "deployedImplementationType";          /* from Database entity */
    public static final String DATABASE_TYPE_PROPERTY_NAME_DEP           = "type";          /* from Database entity */
    public static final String DATABASE_VERSION_PROPERTY_NAME            = "databaseVersion";       /* from Database entity */
    public static final String DATABASE_VERSION_PROPERTY_NAME_DEP        = "version";       /* from Database entity */
    public static final String DATABASE_INSTANCE_PROPERTY_NAME           = "instance";      /* from Database entity */
    public static final String DATABASE_IMPORTED_FROM_PROPERTY_NAME      = "importedFrom";  /* from Database entity */

    public static final String FILE_FOLDER_TYPE_GUID                     = "229ed5cc-de31-45fc-beb4-9919fd247398";  /* from Area 2 */
    public static final String FILE_FOLDER_TYPE_NAME                     = "FileFolder";
    /* DataStore */

    public static final String DATA_FOLDER_TYPE_GUID                     = "9f1fb984-db15-43ee-85fb-f8b0353bfb8b";  /* from Area 2 */
    public static final String DATA_FOLDER_TYPE_NAME                     = "DataFolder";
    /* FileFolder */

    public static final String FOLDER_HIERARCHY_TYPE_GUID                = "48ac9028-45dd-495d-b3e1-622685b54a01";  /* from Area 2 */
    public static final String FOLDER_HIERARCHY_TYPE_NAME                = "FolderHierarchy";

    public static final String NESTED_FILE_TYPE_GUID                     = "4cb88900-1446-4eb6-acea-29cd9da45e63";  /* from Area 2 */
    public static final String NESTED_FILE_TYPE_NAME                     = "NestedFile";

    public static final String LINKED_FILE_TYPE_GUID                     = "970a3405-fde1-4039-8249-9aa5f56d5151";  /* from Area 2 */
    public static final String LINKED_FILE_TYPE_NAME                     = "LinkedFile";

    public static final String DATA_FILE_TYPE_GUID                       = "10752b4a-4b5d-4519-9eae-fdd6d162122f";  /* from Area 2 */
    public static final String DATA_FILE_TYPE_NAME                       = "DataFile";
    /* DataStore */

    public static final String FILE_TYPE_PROPERTY_NAME                   = "fileType";                   /* from DataFile entity */
    public static final String FILE_NAME_PROPERTY_NAME                   = "fileName";                   /* from DataFile entity */

    public static final String MEDIA_FILE_TYPE_GUID                      = "c5ce5499-9582-42ea-936c-9771fbd475f8";  /* from Area 2 */
    public static final String MEDIA_FILE_TYPE_NAME                      = "MediaFile";
    /* DataFile */
    public static final String DOCUMENT_TYPE_GUID                        = "b463827c-c0a0-4cfb-a2b2-ddc63746ded4";  /* from Area 2 */
    public static final String DOCUMENT_TYPE_NAME                        = "Document";
    /* MediaFile */
    public static final String DOCUMENT_STORE_TYPE_GUID                  = "37156790-feac-4e1a-a42e-88858ae6f8e1";  /* from Area 2 */
    public static final String DOCUMENT_STORE_TYPE_NAME                  = "DocumentStore";
    /* DataStore */
    public static final String MEDIA_COLLECTION_TYPE_GUID                = "0075d603-1627-41c5-8cae-f5458d1247fe";  /* from Area 2 */
    public static final String MEDIA_COLLECTION_TYPE_NAME                = "MediaCollection";
    /* DataSet */

    public static final String LINKED_MEDIA_TYPE_GUID                    = "cee3a190-fc8d-4e53-908a-f1b9689581e0";  /* from Area 2 */
    public static final String LINKED_MEDIA_TYPE_NAME                    = "LinkedMedia";
    /* End1 = MediaFile; End 2 = MediaFile */

    public static final String GROUPED_MEDIA_TYPE_GUID                   = "7d881574-461d-475c-ab44-077451528cb8";  /* from Area 2 */
    public static final String GROUPED_MEDIA_TYPE_NAME                   = "GroupedMedia";
    /* End1 = MediaCollection; End 2 = MediaFile */

    public static final String EMBEDDED_METADATA_PROPERTY_NAME           = "embeddedMetadata";                      /* from MediaFile entity */

    public static final String AVRO_FILE_TYPE_GUID                       = "75293260-3373-4777-af7d-7274d5c0b9a5";  /* from Area 2 */
    public static final String AVRO_FILE_TYPE_NAME                       = "AvroFile";
    /* DataFile */

    public static final String CSV_FILE_TYPE_GUID                        = "2ccb2117-9cee-47ca-8150-9b3a543adcec";  /* from Area 2 */
    public static final String CSV_FILE_TYPE_NAME                        = "CSVFile";
    /* DataFile */

    public static final String DELIMITER_CHARACTER_PROPERTY_NAME         = "delimiterCharacter";                   /* from CSVFile entity */
    public static final String QUOTE_CHARACTER_PROPERTY_NAME             = "quoteCharacter";                       /* from CSVFile entity */


    public static final String JSON_FILE_TYPE_GUID                       = "baa608fa-510e-42d7-95cd-7c12fa37bb35";  /* from Area 2 */
    public static final String JSON_FILE_TYPE_NAME                       = "JSONFile";
    /* DataFile */

    public static final String DEPLOYED_DATABASE_SCHEMA_TYPE_GUID        = "eab811ec-556a-45f1-9091-bc7ac8face0f";  /* from Area 2 */
    public static final String DEPLOYED_DATABASE_SCHEMA_TYPE_NAME        = "DeployedDatabaseSchema";
    /* DataSet */

    public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_GUID       = "b827683c-2924-4df3-a92d-7be1888e23c0";  /* from Area 2 */
    public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_NAME       = "DataContentForDataSet";
    /* End1 = Asset; End 2 = DataSet */

    public static final String DATABASE_MANAGER_TYPE_GUID                = "68b35c1e-6c28-4ac3-94f9-2c3dbcbb79e9";
    public static final String DATABASE_MANAGER_TYPE_NAME                = "DatabaseManager";
    /* SoftwareServerCapability */

    public static final String FILE_SYSTEM_CLASSIFICATION_TYPE_GUID      = "cab5ba1d-cfd3-4fca-857d-c07711fc4157";
    public static final String FILE_SYSTEM_CLASSIFICATION_TYPE_NAME      = "FileSystem";
    /* SoftwareCapability */

    public static final String FORMAT_PROPERTY_NAME                      = "format";                /* from FileSystem */
    public static final String ENCRYPTION_PROPERTY_NAME                  = "encryption";            /* from FileSystem */

    public static final String FILE_MANAGER_CLASSIFICATION_TYPE_GUID     = "eadec807-02f0-4d6f-911c-261eddd0c2f5";
    public static final String FILE_MANAGER_CLASSIFICATION_TYPE_NAME     = "FileManager";
    /* SoftwareCapability */

    public static final String NOTIFICATION_MANAGER_CLASSIFICATION_GUID  = "3e7502a7-396a-4737-a106-378c9c94c1057";
    public static final String NOTIFICATION_MANAGER_CLASSIFICATION_NAME  = "NotificationManager";
    /* SoftwareCapability */

    public static final String ENTERPRISE_ACCESS_LAYER_TYPE_GUID         = "39444bf9-638e-4124-a5f9-1b8f3e1b008b";
    public static final String ENTERPRISE_ACCESS_LAYER_TYPE_NAME         = "EnterpriseAccessLayer";
    /* SoftwareServerCapability */

    public static final String TOPIC_ROOT_PROPERTY_NAME                  = "topicRoot";            /* from EnterpriseAccessLayer */
    public static final String METADATA_COLLECTION_ID_PROPERTY_NAME      = "accessedMetadataCollectionId"; /* from EnterpriseAccessLayer */

    public static final String COHORT_MEMBER_TYPE_GUID                   = "42063797-a78a-4720-9353-52026c75f667";
    public static final String COHORT_MEMBER_TYPE_NAME                   = "CohortMember";
    /* SoftwareServerCapability */

    public static final String PROTOCOL_VERSION_PROPERTY_NAME            = "protocolVersion";            /* from CohortMember */

    public static final String DEPLOYED_API_TYPE_GUID                    = "7dbb3e63-138f-49f1-97b4-66313871fc14";  /* from Area 2 */
    public static final String DEPLOYED_API_TYPE_NAME                    = "DeployedAPI";
    /* Asset */
    public static final String API_ENDPOINT_TYPE_GUID                    = "de5b9501-3ad4-4803-a8b2-e311c72a4336";  /* from Area 2 */
    public static final String API_ENDPOINT_TYPE_NAME                    = "APIEndpoint";
    /* End1 = DeployedAPI; End 2 = Endpoint */

    public static final String LOG_FILE_TYPE_GUID                        = "ff4c8484-9127-464a-97fc-99579d5bc429";  /* from Area 2 */
    public static final String LOG_FILE_TYPE_NAME                        = "LogFile";
    /* DataFile */
    public static final String LOG_FILE_TYPE_PROPERTY_NAME               = "deployedImplementationType";            /* from LogFile entity */

    public static final String TOPIC_TYPE_GUID                           = "29100f49-338e-4361-b05d-7e4e8e818325";  /* from Area 2 */
    public static final String TOPIC_TYPE_NAME                           = "Topic";
    /* DataSet */
    public static final String TOPIC_TYPE_PROPERTY_NAME                  = "topicType";                             /* from Topic entity */

    public static final String SUBSCRIBER_LIST_TYPE_GUID                 = "69751093-35f9-42b1-944b-ba6251ff513d";  /* from Area 2 */
    public static final String SUBSCRIBER_LIST_TYPE_NAME                 = "SubscriberList";
    /* DataSet */

    public static final String TOPIC_SUBSCRIBERS_TYPE_GUID               = "bc91a28c-afb9-41a7-8eb2-fc8b5271fe9e";  /* from Area 2 */
    public static final String TOPIC_SUBSCRIBERS_TYPE_NAME               = "TopicSubscribers";
    /* End1 = SubscriberList; End 2 = Topic */

    public static final String ASSOCIATED_LOG_TYPE_GUID                  = "0999e2b9-45d6-42c4-9767-4b74b0b48b89";  /* from Area 2 */
    public static final String ASSOCIATED_LOG_TYPE_NAME                  = "AssociatedLog";
    /* End1 = Referenceable; End 2 = Asset */

    public static final String INFORMATION_VIEW_TYPE_GUID                = "68d7b905-6438-43be-88cf-5de027b4aaaf";  /* from Area 2 */
    public static final String INFORMATION_VIEW_TYPE_NAME                = "InformationView";
    /* DataSet */

    public static final String FORM_TYPE_GUID                            = "8078e3d1-0c63-4ace-aafa-68498b39ccd6";  /* from Area 2 */
    public static final String FORM_TYPE_NAME                            = "Form";
    /* DataSet */

    public static final String DEPLOYED_REPORT_TYPE_GUID                 = "e9077f4f-955b-4d7b-b1f7-12ee769ff0c3";  /* from Area 2 */
    public static final String DEPLOYED_REPORT_TYPE_NAME                 = "DeployedReport";
    /* DataSet */

    public static final String ID_PROPERTY_NAME                          = "id";                                    /* from DeployedReport entity */
    public static final String CREATED_TIME_PROPERTY_NAME                = "createdTime";                            /* from DeployedReport entity */
    public static final String LAST_MODIFIED_TIME_PROPERTY_NAME          = "lastModifiedTime";                      /* from DeployedReport entity */
    public static final String LAST_MODIFIER_PROPERTY_NAME               = "lastModifier";                          /* from DeployedReport entity */


    public static final String DEPLOYED_SOFTWARE_COMPONENT_TYPE_GUID     = "486af62c-dcfd-4859-ab24-eab2e380ecfd";  /* from Area 2 */
    public static final String DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME     = "DeployedSoftwareComponent";
    /* Process */
    public static final String IMPLEMENTATION_LANGUAGE_PROPERTY_NAME     = "implementationLanguage";                /* from Topic entity */

    public static final String PROCESS_HIERARCHY_TYPE_GUID               = "70dbbda3-903f-49f7-9782-32b503c43e0e";  /* from Area 2 */
    public static final String PROCESS_HIERARCHY_TYPE_NAME               = "ProcessHierarchy";
    /* End1 = Process - parent; End 2 = Process - child */

    public static final String PROCESS_CONTAINMENT_TYPE_ENUM_TYPE_GUID   = "1bb4b908-7983-4802-a2b5-91b095552ee9";
    public static final String PROCESS_CONTAINMENT_TYPE_ENUM_TYPE_NAME   = "ProcessContainmentType";                /* from Area 2 */

    public static final String CONTAINMENT_TYPE_PROPERTY_NAME            = "containmentType";               /* from ProcessHierarchy relationship */

    public static final String PORT_TYPE_GUID                            = "e3d9FD9F-d5eD-2aed-CC98-0bc21aB6f71C";  /* from Area 2 */
    public static final String PORT_TYPE_NAME                            = "Port";
    /* Referenceable */

    public static final String PORT_TYPE_ENUM_TYPE_GUID                  = "b57Fbce7-42ac-71D1-D6a6-9f62Cb7C6dc3";
    public static final String PORT_TYPE_ENUM_TYPE_NAME                  = "PortType";                              /* from Area 2 */

    public static final String PORT_TYPE_PROPERTY_NAME                   = "portType";                              /* from Port entity */

    public static final String PORT_ALIAS_TYPE_GUID                      = "DFa5aEb1-bAb4-c25B-bDBD-B95Ce6fAB7F5";  /* from Area 2 */
    public static final String PORT_ALIAS_TYPE_NAME                      = "PortAlias";
    /* Port */

    public static final String PORT_IMPLEMENTATION_TYPE_GUID             = "ADbbdF06-a6A3-4D5F-7fA3-DB4Cb0eDeC0E";  /* from Area 2 */
    public static final String PORT_IMPLEMENTATION_TYPE_NAME             = "PortImplementation";
    /* Port */

    public static final String PROCESS_PORT_TYPE_GUID                    = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";  /* from Area 2 */
    public static final String PROCESS_PORT_TYPE_NAME                    = "ProcessPort";
    /* End1 = Process; End 2 = Port */

    public static final String PORT_DELEGATION_TYPE_GUID                 = "98bB8BA1-dc6A-eb9D-32Cf-F837bEbCbb8E";  /* from Area 2 */
    public static final String PORT_DELEGATION_TYPE_NAME                 = "PortDelegation";
    /* End1 = Port delegating from; End 2 = Port delegating to */


    /* ============================================================================================================================*/
    /* Area 3 - Glossary                                                                                                           */
    /* ============================================================================================================================*/

    public static final String GLOSSARY_TYPE_GUID                                = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String GLOSSARY_TYPE_NAME                                = "Glossary";               /* from Area 3 */
    /* Referenceable */

    public static final String LANGUAGE_PROPERTY_NAME                            = "language";      /* from Glossary entity*/
    public static final String USAGE_PROPERTY_NAME                               = "usage";         /* from Glossary entity*/

    public static final String TAXONOMY_CLASSIFICATION_TYPE_GUID                 = "37116c51-e6c9-4c37-942e-35d48c8c69a0";
    public static final String TAXONOMY_CLASSIFICATION_TYPE_NAME                 = "Taxonomy";               /* from Area 3 */

    public static final String ORGANIZING_PRINCIPLE_PROPERTY_NAME                = "organizingPrinciple";  /* from Taxonomy classification */

    public static final String CANONICAL_VOCAB_CLASSIFICATION_TYPE_GUID          = "33ad3da2-0910-47be-83f1-daee018a4c05";
    public static final String CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME          = "CanonicalVocabulary";               /* from Area 3 */

    public static final String SCOPE_PROPERTY_NAME                               = "scope";  /* from CanonicalVocabulary classification */

    public static final String EXTERNAL_GLOSSARY_LINK_TYPE_GUID                  = "183d2935-a950-4d74-b246-eac3664b5a9d";
    public static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME                  = "ExternalGlossaryLink";               /* from Area 3 */
    /* ExternalReference */

    public static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_GUID             = "7786a39c-436b-4538-acc7-d595b5856add";
    public static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME             = "ExternallySourcedGlossary";     /* from Area 3 */

    public static final String GLOSSARY_CATEGORY_TYPE_GUID                       = "e507485b-9b5a-44c9-8a28-6967f7ff3672";
    public static final String GLOSSARY_CATEGORY_TYPE_NAME                       = "GlossaryCategory";       /* from Area 3 */
    /* Referenceable */

    public static final String ROOT_CATEGORY_CLASSIFICATION_TYPE_GUID            = "1d0fec82-7444-4e4c-abd4-4765bb855ce3";
    public static final String ROOT_CATEGORY_CLASSIFICATION_TYPE_NAME            = "RootCategory";

    public static final String CATEGORY_ANCHOR_TYPE_GUID                         = "c628938e-815e-47db-8d1c-59bb2e84e028";
    public static final String CATEGORY_ANCHOR_TYPE_NAME                         = "CategoryAnchor";     /* from Area 3 */

    public static final String CATEGORY_HIERARCHY_TYPE_GUID                      = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
    public static final String CATEGORY_HIERARCHY_TYPE_NAME                      = "CategoryHierarchyLink";     /* from Area 3 */

    public static final String LIBRARY_CATEGORY_REFERENCE_TYPE_GUID              = "3da21cc9-3cdc-4d87-89b5-c501740f00b2e";
    public static final String LIBRARY_CATEGORY_REFERENCE_TYPE_NAME              = "LibraryCategoryReference";     /* from Area 3 */

    public static final String GLOSSARY_TERM_TYPE_GUID                           = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    public static final String GLOSSARY_TERM_TYPE_NAME                           = "GlossaryTerm";           /* from Area 3 */
    /* Referenceable */

    public static final String CONTROLLED_GLOSSARY_TERM_TYPE_GUID                = "c04e29b2-2d66-48fc-a20d-e59895de6040";
    public static final String CONTROLLED_GLOSSARY_TERM_TYPE_NAME                = "ControlledGlossaryTerm";           /* from Area 3 */
    /* GlossaryTerm */

    public static final String EDITING_GLOSSARY_CLASSIFICATION_TYPE_GUID         = "173614ba-c582-4ecc-8fcc-cde5fb664548";
    public static final String EDITING_GLOSSARY_CLASSIFICATION_TYPE_NAME         = "EditingGlossary";           /* from Area 3 */
    /* Glossary */

    public static final String STAGING_GLOSSARY_CLASSIFICATION_TYPE_GUID         = "361fa044-e703-404c-bb83-9402f9221f54";
    public static final String STAGING_GLOSSARY_CLASSIFICATION_TYPE_NAME         = "StagingGlossary";       /* from Area 3 */
    /* Glossary */

    public static final String SUMMARY_PROPERTY_NAME                             = "summary";       /* from GlossaryTerm and GovernanceDefinition entity */
    public static final String EXAMPLES_PROPERTY_NAME                            = "examples";      /* from GlossaryTerm entity */
    public static final String ABBREVIATION_PROPERTY_NAME                        = "abbreviation";  /* from GlossaryTerm entity */
    public static final String PUBLISH_VERSION_ID_PROPERTY_NAME                  = "publishVersionIdentifier";  /* from GlossaryTerm entity */

    public static final String TERM_RELATIONSHIP_STATUS_ENUM_TYPE_GUID           = "42282652-7d60-435e-ad3e-7cfe5291bcc7";
    public static final String TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME           = "TermRelationshipStatus";     /* from Area 3 */

    public static final String TERM_ANCHOR_TYPE_GUID                             = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    public static final String TERM_ANCHOR_TYPE_NAME                             = "TermAnchor";     /* from Area 3 */

    public static final String TERM_CATEGORIZATION_TYPE_GUID                     = "696a81f5-ac60-46c7-b9fd-6979a1e7ad27";
    public static final String TERM_CATEGORIZATION_TYPE_NAME                     = "TermCategorization";     /* from Area 3 */

    public static final String LIBRARY_TERM_REFERENCE_TYPE_GUID                  = "38c346e4-ddd2-42ef-b4aa-55d53c078d22";
    public static final String LIBRARY_TERM_REFERENCE_TYPE_NAME                  = "LibraryTermReference";   /* from Area 3 */

    public static final String ACTIVITY_TYPE_ENUM_TYPE_GUID                      = "af7e403d-9865-4ebb-8c1a-1fd57b4f4bca";
    public static final String ACTIVITY_TYPE_ENUM_TYPE_NAME                      = "ActivityType";   /* from Area 3 */

    public static final String ACTIVITY_TYPE_PROPERTY_NAME                       = "activityType";   /* from Area 3 */

    public static final String ACTIVITY_DESC_CLASSIFICATION_TYPE_GUID            = "317f0e52-1548-41e6-b90c-6ae5e6c53fed";
    public static final String ACTIVITY_DESC_CLASSIFICATION_TYPE_NAME            = "ActivityDescription";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_GUID         = "9d725a07-4abf-4939-a268-419d200b69c2";
    public static final String ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_NAME         = "AbstractConcept";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String DATA_VALUE_CLASSIFICATION_TYPE_GUID               = "ab253e31-3d8a-45a7-8592-24329a189b9e";
    public static final String DATA_VALUE_CLASSIFICATION_TYPE_NAME               = "DataValue";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String CONTEXT_DEFINITION_CLASSIFICATION_TYPE_GUID       = "54f9f41a-3871-4650-825d-59a41de01330e";
    public static final String CONTEXT_DEFINITION_CLASSIFICATION_TYPE_NAME       = "ContextDefinition";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String SPINE_OBJECT_CLASSIFICATION_TYPE_GUID             = "a41ee152-de1e-4533-8535-2f8b37897cac";
    public static final String SPINE_OBJECT_CLASSIFICATION_TYPE_NAME             = "SpineObject";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_GUID          = "ccb749ba-34ec-4f71-8755-4d8b383c34c3";
    public static final String SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_NAME          = "SpineAttribute";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_GUID        = "3d1e4389-27de-44fa-8df4-d57bfaf809ea";
    public static final String OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_NAME        = "ObjectIdentifier";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String GLOSSARY_PROJECT_CLASSIFICATION_TYPE_GUID         = "43be51a9-2d19-4044-b399-3ba36af10929";
    public static final String GLOSSARY_PROJECT_CLASSIFICATION_TYPE_NAME         = "GlossaryProject";   /* from Area 3 */
    /* GlossaryProject */

    public static final String REFERENCEABLE_TO_MEANING_TYPE_GUID                = "e6670973-645f-441a-bec7-6f5570345b92";
    public static final String REFERENCEABLE_TO_MEANING_TYPE_NAME                = "SemanticAssignment";
    /* End1 = Referenceable; End 2 = GlossaryTerm */

    public static final String TERM_ASSIGNMENT_STATUS_ENUM_TYPE_GUID             = "c8fe36ac-369f-4799-af75-46b9c1343ab3";
    public static final String TERM_ASSIGNMENT_STATUS_ENUM_TYPE_NAME             = "TermAssignmentStatus";

    public static final String EXPRESSION_PROPERTY_NAME                          = "expression";
    public static final String USER_DEFINED_STATUS_PROPERTY_NAME                 = "userDefinedStatus";
    public static final String CONFIDENCE_PROPERTY_NAME                          = "confidence";
    public static final String STEWARD_PROPERTY_NAME                             = "steward";
    public static final String STEWARD_TYPE_NAME_PROPERTY_NAME                   = "stewardTypeName";
    public static final String STEWARD_PROPERTY_NAME_PROPERTY_NAME               = "stewardPropertyName";
    public static final String CREATED_BY_PROPERTY_NAME                          = "createdBy";


    public static final String ELEMENT_SUPPLEMENT_CLASSIFICATION_TYPE_GUID       = "58520015-ce6e-47b7-a1fd-864030544819";
    public static final String ELEMENT_SUPPLEMENT_CLASSIFICATION_TYPE_NAME       = "ElementSupplement";   /* from Area 3 */
    /* Referencable */

    public static final String SUPPLEMENTARY_PROPERTIES_TYPE_GUID                = "2bb10ba5-7aa2-456a-8b3a-8fdbd75c95cd";
    public static final String SUPPLEMENTARY_PROPERTIES_TYPE_NAME                = "SupplementaryProperties";  /* from Area 3 */
    /* End1 = Referenceable; End 2 = GlossaryTerm */


    /* ============================================================================================================================*/
    /* Area 4 - Governance                                                                                                         */
    /* ============================================================================================================================*/

    public static final String GOVERNANCE_DOMAIN_TYPE_GUID               = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
    public static final String GOVERNANCE_DOMAIN_TYPE_NAME               = "GovernanceDomainDescription";
    /* Referenceable */

    public static final String GOVERNANCE_DOMAIN_SET_CLASSIFICATION_NAME = "GovernanceDomainSet";

    public static final String GOVERNANCE_ROLE_TYPE_GUID                 = "de2d7f2e-1759-44e3-b8a6-8af53e8fb0ee";
    public static final String GOVERNANCE_ROLE_TYPE_NAME                 = "GovernanceRole";
    /* PersonRole */

    public static final String GOVERNANCE_OFFICER_TYPE_GUID              = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
    public static final String GOVERNANCE_OFFICER_TYPE_NAME              = "GovernanceOfficer";
    /* GovernanceRole */

    public static final String GOVERNANCE_REPRESENTATIVE_TYPE_GUID       = "6046bdf8-a37e-4bc4-b51d-325d8c31a96c";
    public static final String GOVERNANCE_REPRESENTATIVE_TYPE_NAME       = "GovernanceRepresentative";
    /* GovernanceRole */

    public static final String LOCATION_OWNER_TYPE_GUID                  = "3437fd1d-5098-426c-9b55-c94d1fc5dc0e";
    public static final String LOCATION_OWNER_TYPE_NAME                  = "LocationOwner";
    /* GovernanceRole */

    public static final String ASSET_OWNER_TYPE_GUID                     = "ac406bf8-e53e-49f1-9088-2af28eeee285";
    public static final String ASSET_OWNER_TYPE_NAME                     = "AssetOwner";
    /* GovernanceRole */

    public static final String BUSINESS_OWNER_TYPE_GUID                  = "0e83bb5f-f2f5-4a85-92eb-f71e92a181f5";
    public static final String BUSINESS_OWNER_TYPE_NAME                  = "BusinessOwner";
    /* GovernanceRole */

    public static final String SOLUTION_OWNER_TYPE_GUID                  = "e44d5019-37e5-4965-8b89-2bef412833bf";
    public static final String SOLUTION_OWNER_TYPE_NAME                  = "SolutionOwner";
    /* GovernanceRole */

    public static final String COMPONENT_OWNER_TYPE_GUID                 = "21756af1-06c9-4b06-87d2-3ef911f0a58a";
    public static final String COMPONENT_OWNER_TYPE_NAME                 = "ComponentOwner";
    /* GovernanceRole */

    public static final String DATA_ITEM_OWNER_TYPE_GUID                 = "69836cfd-39b8-460b-8727-b04e19210069";
    public static final String DATA_ITEM_OWNER_TYPE_NAME                 = "DataItemOwner";
    /* GovernanceRole */

    public static final String GOVERNANCE_DEFINITION_TYPE_GUID           = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
    public static final String GOVERNANCE_DEFINITION_TYPE_NAME           = "GovernanceDefinition";
    /* Referenceable */

    public static final String GOVERNANCE_DRIVER_TYPE_GUID               = "c403c109-7b6b-48cd-8eee-df445b258b33";
    public static final String GOVERNANCE_DRIVER_TYPE_NAME               = "GovernanceDriver";
    /* GovernanceDefinition */

    public static final String GOVERNANCE_STRATEGY_TYPE_GUID             = "3c34f121-07a6-4e95-a07d-9b0ef17b7bbf";
    public static final String GOVERNANCE_STRATEGY_TYPE_NAME             = "GovernanceStrategy";
    /* GovernanceDriver */

    public static final String REGULATION_TYPE_GUID                      = "e3c4293d-8846-4500-b0c0-197d73aba8b0";
    public static final String REGULATION_TYPE_NAME                      = "Regulation";
    /* GovernanceDriver */

    public static final String GOVERNANCE_POLICY_TYPE_GUID               = "a7defa41-9cfa-4be5-9059-359022bb016d";
    public static final String GOVERNANCE_POLICY_TYPE_NAME               = "GovernancePolicy";
    /* GovernanceDefinition */

    public static final String GOVERNANCE_PRINCIPLE_TYPE_GUID            = "3b7d1325-ec2c-44cb-8db0-ce207beb78cf";
    public static final String GOVERNANCE_PRINCIPLE_TYPE_NAME            = "GovernancePrinciple";
    /* GovernancePolicy */

    public static final String GOVERNANCE_OBLIGATION_TYPE_GUID           = "0cec20d3-aa29-41b7-96ea-1c544ed32537";
    public static final String GOVERNANCE_OBLIGATION_TYPE_NAME           = "GovernanceObligation";
    /* GovernancePolicy */

    public static final String GOVERNANCE_APPROACH_TYPE_GUID             = "2d03ec9d-bd6b-4be9-8e17-95a7ecdbaa67";
    public static final String GOVERNANCE_APPROACH_TYPE_NAME             = "GovernanceApproach";
    /* GovernancePolicy */

    public static final String GOVERNANCE_CONTROL_TYPE_GUID              = "c794985e-a10b-4b6c-9dc2-6b2e0a2901d3";
    public static final String GOVERNANCE_CONTROL_TYPE_NAME              = "GovernanceControl";
    /* GovernanceDefinition */

    public static final String TECHNICAL_CONTROL_TYPE_GUID               = "d8f6eb5b-36f0-49bd-9b25-bf16f370d1ec";
    public static final String TECHNICAL_CONTROL_TYPE_NAME               = "TechnicalControl";
    /* GovernanceControl */

    public static final String GOVERNANCE_RULE_TYPE_GUID                 = "8f954380-12ce-4a2d-97c6-9ebe250fecf8";
    public static final String GOVERNANCE_RULE_TYPE_NAME                 = "GovernanceRule";
    /* TechnicalControl */

    public static final String NAMING_STANDARD_RULE_TYPE_GUID            = "52505b06-98a5-481f-8a32-db9b02afabfc";
    public static final String NAMING_STANDARD_RULE_TYPE_NAME            = "NamingStandardRule";
    /* GovernanceRule */

    public static final String GOVERNANCE_PROCESS_TYPE_GUID              = "b68b5d9d-6b79-4f3a-887f-ec0f81c54aea";
    public static final String GOVERNANCE_PROCESS_TYPE_NAME              = "GovernanceProcess";
    /* TechnicalControl */

    public static final String ORGANIZATIONAL_CONTROL_TYPE_GUID          = "befa1458-79b8-446a-b813-536700e60fa8";
    public static final String ORGANIZATIONAL_CONTROL_TYPE_NAME          = "OrganizationalControl";
    /* GovernanceControl */

    public static final String GOVERNANCE_RESPONSIBILITY_TYPE_GUID       = "89a76b24-deb8-45bf-9304-a578a610326f";
    public static final String GOVERNANCE_RESPONSIBILITY_TYPE_NAME       = "GovernanceResponsibility";
    /* OrganizationalControl */

    public static final String GOVERNANCE_PROCEDURE_TYPE_GUID            = "69055d10-51dc-4c2b-b21f-d76fad3f8ef3";
    public static final String GOVERNANCE_PROCEDURE_TYPE_NAME            = "GovernanceProcedure";
    /* OrganizationalControl */


    public static final String NAMING_STANDARD_RULE_SET_TYPE_GUID        = "ba70f506-1f81-4890-bb4f-1cb1d99c939e";
    public static final String NAMING_STANDARD_RULE_SET_TYPE_NAME        = "NamingStandardRuleSet";
    /* Collection */

    public static final String PRIME_WORD_CLASSIFICATION_TYPE_GUID       = "3ea1ea66-8923-4662-8628-0bacef3e9c5f";
    public static final String PRIME_WORD_CLASSIFICATION_TYPE_NAME       = "PrimeWord";

    public static final String CLASS_WORD_CLASSIFICATION_TYPE_GUID       = "feac4bd9-37d9-4437-82f6-618ce3e2793e";
    public static final String CLASS_WORD_CLASSIFICATION_TYPE_NAME       = "ClassWord";

    public static final String MODIFIER_CLASSIFICATION_TYPE_GUID         = "f662c95a-ae3f-4f71-b442-78ab70f2ee47";
    public static final String MODIFIER_CLASSIFICATION_TYPE_NAME         = "Modifier";

    public static final String GOVERNED_BY_TYPE_GUID                     = "89c3c695-9e8d-4660-9f44-ed971fd55f89";
    public static final String GOVERNED_BY_TYPE_NAME                     = "GovernedBy";  /* from Area 4 */
    /* End1 = GovernanceDefinition; End 2 = Referenceable */

    public static final String GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID     = "3845b5cc-8c85-462f-b7e6-47472a568793";
    public static final String GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME     = "GovernanceDefinitionScope";  /* from Area 4 */
    /* End1 = Referenceable; End 2 = GovernanceDefinition */

    public static final String GOVERNANCE_RESPONSE_TYPE_GUID             = "8845990e-7fd9-4b79-a19d-6c4730dadd6b";
    public static final String GOVERNANCE_RESPONSE_TYPE_NAME             = "GovernanceResponse";  /* from Area 4 */
    /* End1 = GovernanceDriver; End 2 = GovernancePolicy */

    public static final String GOVERNANCE_DRIVER_LINK_TYPE_NAME          = "GovernanceDriverLink";  /* from Area 4 */
    /* End1 = GovernanceDriver; End 2 = GovernanceDriver */

    public static final String GOVERNANCE_POLICY_LINK_TYPE_GUID          = "0c42c999-4cac-4da4-afab-0e381f3a818e";
    public static final String GOVERNANCE_POLICY_LINK_TYPE_NAME          = "GovernancePolicyLink";  /* from Area 4 */
    /* End1 = GovernancePolicy; End 2 = GovernancePolicy */

    public static final String GOVERNANCE_IMPLEMENTATION_TYPE_GUID       = "787eaf46-7cf2-4096-8d6e-671a0819d57e";
    public static final String GOVERNANCE_IMPLEMENTATION_TYPE_NAME       = "GovernanceImplementation";  /* from Area 4 */
    /* End1 = GovernancePolicy; End 2 = GovernanceControl */

    public static final String GOVERNANCE_CONTROL_LINK_TYPE_GUID         = "806933fb-7925-439b-9876-922a960d2ba1";
    public static final String GOVERNANCE_CONTROL_LINK_TYPE_NAME         = "GovernanceControlLink";  /* from Area 4 */
    /* End1 = GovernanceControl; End 2 = GovernanceControl */

    public static final String GOVERNANCE_ROLE_ASSIGNMENT_TYPE_GUID      = "cb10c107-b7af-475d-aab0-d78b8297b982";
    public static final String GOVERNANCE_ROLE_ASSIGNMENT_TYPE_NAME      = "GovernanceRoleAssignment";    /* from Area 4 */
    /* End1 = Referenceable; End 2 = PersonRole */

    public static final String GOVERNANCE_RESPONSIBILITY_ASSIGNMENT_TYPE_GUID = "cb15c107-b7af-475d-aab0-d78b8297b982";
    public static final String GOVERNANCE_RESPONSIBILITY_ASSIGNMENT_TYPE_NAME = "GovernanceResponsibilityAssignment";    /* from Area 4 */
    /* End1 = PersonRole; End 2 = GovernanceResponsibility */

    public static final String DOMAIN_IDENTIFIER_PROPERTY_NAME           = "domainIdentifier";          /* from many governance entities */
    public static final String CRITERIA_PROPERTY_NAME                    = "criteria";                  /* from many governance entities */

    public static final String IMPLICATIONS_PROPERTY_NAME                = "implications";              /* from GovernanceDefinition entity */
    public static final String OUTCOMES_PROPERTY_NAME                    = "outcomes";                  /* from GovernanceDefinition entity */
    public static final String RESULTS_PROPERTY_NAME                     = "results";                   /* from GovernanceDefinition entity */
    public static final String BUSINESS_IMPERATIVES_PROPERTY_NAME        = "businessImperatives";       /* from GovernanceStrategy entity */
    public static final String JURISDICTION_PROPERTY_NAME                = "jurisdiction";              /* from Regulation entity */
    public static final String IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME  = "implementationDescription"; /* from GovernanceControl entity */
    public static final String NOTES_PROPERTY_NAME                       = "notes";                     /* from multiple entities */
    public static final String ATTRIBUTE_NAME_PROPERTY_NAME              = "attributeName";             /* from ReferenceValueAssignment relationship */
    public static final String RATIONALE_PROPERTY_NAME                   = "rationale";                 /* from GovernanceResponse, GovernanceImplementation relationship */

    public static final String GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_GUID = "37142317-4125-4046-9514-71dc5031563f";
    public static final String GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_NAME = "GovernanceProject";
    /* Attached to Project */

    public static final String GOVERNANCE_CLASSIFICATION_LEVEL_TYPE_GUID   = "8af91d61-2ae8-4255-992e-14d7f745a556";
    public static final String GOVERNANCE_CLASSIFICATION_LEVEL_TYPE_NAME   = "GovernanceClassificationLevel";
    /* Referenceable */

    public static final String LEVEL_IDENTIFIER_PROPERTY_NAME              = "levelIdentifier";           /* from many governance entities and classifications */
    public static final String SEVERITY_IDENTIFIER_PROPERTY_NAME           = "severityIdentifier";        /* from Impact classification */
    public static final String BASIS_IDENTIFIER_PROPERTY_NAME              = "basisIdentifier";           /* from Retention classification */

    public static final String GOVERNANCE_CLASSIFICATION_SET_TYPE_GUID     = "d92b7f31-c92d-418d-b345-ea45bb3f73f5";
    public static final String GOVERNANCE_CLASSIFICATION_SET_TYPE_NAME     = "GovernanceClassificationSet";
    /* Attached to Collection */

    public static final String GOVERNANCE_STATUS_LEVEL_TYPE_GUID           = "a518de03-0f72-4944-9cd5-e05b43ae9c5e";
    public static final String GOVERNANCE_STATUS_LEVEL_TYPE_NAME           = "GovernanceStatusLevel";
    /* Referenceable */

    public static final String STATUS_IDENTIFIER_PROPERTY_NAME             = "statusIdentifier";

    public static final String GOVERNANCE_STATUS_SET_TYPE_GUID             = "c13261bb-0cfe-4540-a44a-cca2b14f412b";
    public static final String GOVERNANCE_STATUS_SET_TYPE_NAME             = "GovernanceStatusSet";
    /* Attached to Collection */

    public static final String GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_GUID   = "cc540586-ac7c-41ba-8cc1-4da694a6a8e4";
    public static final String GOVERNANCE_CLASSIFICATION_STATUS_ENUM_TYPE_NAME   = "GovernanceClassificationStatus";
    public static final int    DISCOVERED_GC_STATUS_ORDINAL                      = 0;
    public static final int    PROPOSED_GC_STATUS_ORDINAL                        = 1;
    public static final int    IMPORTED_GC_STATUS_ORDINAL                        = 2;
    public static final int    VALIDATED_GC_STATUS_ORDINAL                       = 3;
    public static final int    DEPRECATED_GC_STATUS_ORDINAL                      = 4;
    public static final int    OBSOLETE_GC_STATUS_ORDINAL                        = 5;
    public static final int    OTHER_GC_STATUS_ORDINAL                           = 99;

    public static final String CONFIDENCE_LEVEL_ENUM_TYPE_GUID                   = "ae846797-d88a-4421-ad9a-318bf7c1fe6f";
    public static final String CONFIDENCE_LEVEL_ENUM_TYPE_NAME                   = "ConfidenceLevel";

    public static final String RETENTION_BASIS_ENUM_TYPE_GUID                    = "de79bf78-ecb0-4fd0-978f-ecc2cb4ff6c7";
    public static final String RETENTION_BASIS_ENUM_TYPE_NAME                    = "RetentionBasis";

    public static final String CRITICALITY_LEVEL_ENUM_TYPE_GUID                  = "22bcbf49-83e1-4432-b008-e09a8f842a1e";
    public static final String CRITICALITY_LEVEL_ENUM_TYPE_NAME                  = "CriticalityLevel";

    public static final String IMPACT_SEVERITY_ENUM_TYPE_GUID                    = "3a6c4ba7-3cc5-48cd-8952-a50a92da016d";
    public static final String IMPACT_SEVERITY_ENUM_TYPE_NAME                    = "ImpactSeverity";

    public static final String CONFIDENTIALITY_CLASSIFICATION_TYPE_GUID          = "742ddb7d-9a4a-4eb5-8ac2-1d69953bd2b6";
    public static final String CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME          = "Confidentiality";

    public static final String CONFIDENCE_CLASSIFICATION_TYPE_GUID               = "25d8f8d5-2998-4983-b9ef-265f58732965";
    public static final String CONFIDENCE_CLASSIFICATION_TYPE_NAME               = "Confidence";

    public static final String CRITICALITY_CLASSIFICATION_TYPE_GUID              = "d46d211a-bd22-40d5-b642-87b4954a167e";
    public static final String CRITICALITY_CLASSIFICATION_TYPE_NAME              = "Criticality";

    public static final String IMPACT_CLASSIFICATION_TYPE_GUID                   = "5b905856-90ec-4944-80c4-0d42bcad484a";
    public static final String IMPACT_CLASSIFICATION_TYPE_NAME                   = "Impact";

    public static final String RETENTION_CLASSIFICATION_TYPE_GUID                = "83dbcdf2-9445-45d7-bb24-9fa661726553";
    public static final String RETENTION_CLASSIFICATION_TYPE_NAME                = "Retention";

    public static final String GOVERNANCE_CLASSIFICATION_STATUS_PROPERTY_NAME       = "status";
    public static final String GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME   = "confidence";
    public static final String GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME      = "steward";
    public static final String GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME       = "source";
    public static final String GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME        = "notes";

    public static final String CONFIDENTIALITY_LEVEL_PROPERTY_NAME                  = "level";
    public static final String CRITICALITY_LEVEL_PROPERTY_NAME                      = "level";
    public static final String RETENTION_BASIS_PROPERTY_NAME                        = "basis";
    public static final String RETENTION_ASSOCIATED_GUID_PROPERTY_NAME              = "associatedGUID";
    public static final String RETENTION_ARCHIVE_AFTER_PROPERTY_NAME                = "archiveAfter";
    public static final String RETENTION_DELETE_AFTER_PROPERTY_NAME                 = "deleteAfter";

    public static final String SECURITY_GROUP_TYPE_GUID                             = "042d9b5c-677e-477b-811f-1c39bf716759";
    public static final String SECURITY_GROUP_TYPE_NAME                             = "SecurityGroup";
    /* TechnicalControl */

    public static final String SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION_TYPE_GUID   = "21a16f1e-9231-4983-b371-a0686d555273";
    public static final String SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION_TYPE_NAME   = "SecurityGroupMembership";
    public static final String GROUPS_PROPERTY_NAME                                 = "groups";
    public static final String DISTINGUISHED_NAME_PROPERTY_NAME                     = "distinguishedName";

    public static final String SECURITY_TAG_CLASSIFICATION_TYPE_GUID             = "a0b07a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String SECURITY_TAG_CLASSIFICATION_TYPE_NAME             = "SecurityTags";
    public static final String SECURITY_LABELS_PROPERTY_NAME                     = "securityLabels";
    public static final String SECURITY_PROPERTIES_PROPERTY_NAME                 = "securityProperties";
    public static final String ACCESS_GROUPS_PROPERTY_NAME                       = "accessGroups";


    public static final String ZONE_TYPE_GUID                           = "290a192b-42a7-449a-935a-269ca62cfdac";
    public static final String ZONE_TYPE_NAME                           = "GovernanceZone";
    /* Referenceable */

    public static final String ZONE_NAME_PROPERTY_NAME                 = "zoneName";

    public static final String ZONE_HIERARCHY_TYPE_GUID                = "ee6cf469-cb4d-4c3b-a4c7-e2da1236d139";
    public static final String ZONE_HIERARCHY_TYPE_NAME                = "ZoneHierarchy";  /* from Area 4 */
    /* End1 = Parent Zone; End 2 = Child Zone */

    public static final String SUBJECT_AREA_TYPE_GUID                  = "d28c3839-bc6f-41ad-a882-5667e01fea72";
    public static final String SUBJECT_AREA_TYPE_NAME                  = "SubjectAreaDefinition";
    /* Referenceable */

    public static final String SUBJECT_AREA_NAME_PROPERTY_NAME          = "subjectAreaName";

    public static final String SUBJECT_AREA_HIERARCHY_TYPE_GUID         = "fd3b7eaf-969c-4c26-9e1e-f31c4c2d1e4b";
    public static final String SUBJECT_AREA_HIERARCHY_TYPE_NAME         = "SubjectAreaHierarchy";  /* from Area 4 */
    /* End1 = Parent Subject Area; End 2 = Child Subject Area */

    public static final String SUBJECT_AREA_CLASSIFICATION_TYPE_GUID    = "480e6993-35c5-433a-b50b-0f5c4063fb5d";
    public static final String SUBJECT_AREA_CLASSIFICATION_TYPE_NAME    = "SubjectArea";
    /* Referenceable */

    public static final String ORGANIZATION_TYPE_GUID                   = "50a61105-35be-4ee3-8b99-bdd958ed0685";
    public static final String ORGANIZATION_TYPE_NAME                   = "Organization";                /* from Area 4 */
    /* Team */

    public static final String BUSINESS_CAPABILITY_TYPE_GUID            = "7cc6bcb2-b573-4719-9412-cf6c3f4bbb15";
    public static final String BUSINESS_CAPABILITY_TYPE_NAME            = "BusinessCapability";          /* from Area 4 */
    /* Referenceable */

    public static final String ORGANIZATIONAL_CAPABILITY_TYPE_GUID      = "47f0ad39-db77-41b0-b406-36b1598e0ba7";
    public static final String ORGANIZATIONAL_CAPABILITY_TYPE_NAME      = "OrganizationalCapability";    /* from Area 4 */
    /* End1 = BusinessCapability; End 2 = Team */

    public static final String BUSINESS_CAPABILITY_CONTROLS_TYPE_GUID   = "b5de932a-738c-4c69-b852-09fec2b9c678";
    public static final String BUSINESS_CAPABILITY_CONTROLS_TYPE_NAME   = "BusinessCapabilityControls";  /* from Area 4 */
    /* End1 = GovernanceControl; End 2 = BusinessCapability */

    public static final String ASSET_ORIGIN_CLASSIFICATION_GUID          = "e530c566-03d2-470a-be69-6f52bfbd5fb7";
    public static final String ASSET_ORIGIN_CLASSIFICATION_NAME          = "AssetOrigin";

    public static final String ORGANIZATION_PROPERTY_NAME                = "organization";                          /* from AssetOrigin classification */
    public static final String ORGANIZATION_PROPERTY_NAME_PROPERTY_NAME  = "organizationPropertyName";              /* from AssetOrigin classification */
    public static final String BUSINESS_CAPABILITY_PROPERTY_NAME         = "businessCapability";                    /* from AssetOrigin classification */
    public static final String BUSINESS_CAPABILITY_PROPERTY_NAME_PROPERTY_NAME = "businessCapabilityPropertyName";  /* from AssetOrigin classification */
    public static final String OTHER_ORIGIN_VALUES_PROPERTY_NAME         = "otherOriginValues";                     /* from AssetOrigin classification */

    public static final String BUSINESS_CAPABILITY_TYPE_PROPERTY_NAME    = "businessCapabilityType";                /* from BusinessCapability entity */

    public static final String ASSET_ZONES_CLASSIFICATION_GUID           = "a1c17a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String ASSET_ZONES_CLASSIFICATION_NAME           = "AssetZoneMembership";

    public static final String ZONE_MEMBERSHIP_PROPERTY_NAME             = "zoneMembership";                       /* from Area 4 */

    public static final String PROJECT_CHARTER_TYPE_GUID                 = "f96b5a32-42c1-4a74-8f77-70a81cec783d";
    public static final String PROJECT_CHARTER_TYPE_NAME                 = "ProjectCharter";
    /* Referenceable */

    public static final String PROJECT_CHARTER_LINK_TYPE_GUID            = "f081808d-545a-41cb-a9aa-c4f074a16c78";
    public static final String PROJECT_CHARTER_LINK_TYPE_NAME            = "ProjectCharterLink";
    /* End1 = Project; End 2 = ProjectCharter */

    public static final String PROJECT_TYPE_PROPERTY_NAME                = "projectType";                             /* from Area 4 */
    public static final String PURPOSES_PROPERTY_NAME                    = "purposes";                                /* from Area 4 */

    public static final String ASSET_OWNERSHIP_CLASSIFICATION_GUID       = "d531c566-03d2-470a-be69-6f52cabd5fb9";
    public static final String ASSET_OWNERSHIP_CLASSIFICATION_NAME       = "AssetOwnership";

    public static final String OWNERSHIP_CLASSIFICATION_TYPE_GUID = "8139a911-a4bd-432b-a9f4-f6d11c511abe";
    public static final String OWNERSHIP_CLASSIFICATION_TYPE_NAME = "Ownership";

    public static final String OWNER_PROPERTY_NAME                       = "owner";                                /* from Area 4 */
    public static final String OWNER_TYPE_PROPERTY_NAME                  = "ownerType"; /* deprecated */
    public static final String OWNER_TYPE_NAME_PROPERTY_NAME             = "ownerTypeName";
    public static final String OWNER_PROPERTY_NAME_PROPERTY_NAME         = "ownerPropertyName";

    public static final String OWNER_TYPE_ENUM_TYPE_GUID                 = "5ce92a70-b86a-4e0d-a9d7-fc961121de97";
    public static final String OWNER_TYPE_ENUM_TYPE_NAME                 = "OwnerType"; /* deprecated */

    public static final String ASSET_OWNER_TYPE_ENUM_TYPE_GUID           = "9548390c-69f5-4dc6-950d-6feeee257b56";
    public static final String ASSET_OWNER_TYPE_ENUM_TYPE_NAME           = "AssetOwnerType";

    public static final int    USER_ID_OWNER_TYPE_ORDINAL                = 0;
    public static final int    PROFILE_ID_OWNER_TYPE_ORDINAL             = 1;
    public static final int    OTHER_OWNER_TYPE_ORDINAL                  = 99;

    public static final String GOVERNANCE_METRIC_TYPE_GUID               = "9ada8e7b-823c-40f7-adf8-f164aabda77e";
    public static final String GOVERNANCE_METRIC_TYPE_NAME               = "GovernanceMetric";
    /* Referenceable */

    public static final String MEASUREMENT_PROPERTY_NAME                 = "measurement";            /* from Area 4 */
    public static final String TARGET_PROPERTY_NAME                      = "target";                 /* from Area 4 */

    public static final String GOVERNANCE_RESULTS_TYPE_GUID              = "89c3c695-9e8d-4660-9f44-ed971fd55f88";
    public static final String GOVERNANCE_RESULTS_TYPE_NAME              = "GovernanceResults";
    /* End1 = GovernanceMetric; End 2 = DataSet */

    public static final String GOVERNANCE_DEFINITION_METRIC_TYPE_GUID    = "e076fbb3-54f5-46b8-8f1e-a7cb7e792673";
    public static final String GOVERNANCE_DEFINITION_METRIC_TYPE_NAME    = "GovernanceDefinitionMetric";
    /* End1 = GovernanceDefinition; End 2 = GovernanceMetric */

    public static final String GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_GUID    = "fcda7261-865d-464d-b279-7d9880aaab39";
    public static final String GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_NAME    = "GovernanceExpectations";
    /* Referenceable */

    public static final String COUNTS_PROPERTY_NAME                       = "counts";                 /* from Area 4 */
    public static final String VALUES_PROPERTY_NAME                       = "values";                 /* from Area 4 */
    public static final String FLAGS_PROPERTY_NAME                        = "flags";                  /* from Area 4 */


    public static final String GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_GUID = "9d99d962-0214-49ba-83f7-c9b1f9f5bed4";
    public static final String GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_NAME = "GovernanceMeasurements";
    /* Referenceable */

    public static final String MEASUREMENT_COUNTS_PROPERTY_NAME           = "measurementCounts";                 /* from Area 4 */
    public static final String MEASUREMENT_VALUES_PROPERTY_NAME           = "measurementValues";                 /* from Area 4 */
    public static final String MEASUREMENT_FLAGS_PROPERTY_NAME            = "measurementFlags";                  /* from Area 4 */

    public static final String GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_GUID = "789f2e89-accd-4489-8eca-dc43b432c022";
    public static final String GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_NAME = "GovernanceMeasurementsResultsDataSet";
    /* Referenceable */

    public static final String EXECUTION_POINT_DEFINITION_TYPE_GUID      = "d7f8d1d2-8cec-4fd2-b9fd-c8307cad750d";
    public static final String EXECUTION_POINT_DEFINITION_TYPE_NAME      = "ExecutionPointDefinition";
    /* Referenceable */

    public static final String CONTROL_POINT_DEFINITION_TYPE_GUID        = "a376a993-5f1c-4926-b74e-a15a38e1d55ad";
    public static final String CONTROL_POINT_DEFINITION_TYPE_NAME        = "ControlPointDefinition";
    /* ExecutionPointDefinition */

    public static final String VERIFICATION_POINT_DEFINITION_TYPE_GUID   = "27db26a1-ff66-4042-9932-ddc728b977b9";
    public static final String VERIFICATION_POINT_DEFINITION_TYPE_NAME   = "VerificationPointDefinition";
    /* ExecutionPointDefinition */

    public static final String ENFORCEMENT_POINT_DEFINITION_TYPE_GUID    = "e87ff806-bb9c-4c5d-8106-f38f2dd21037";
    public static final String ENFORCEMENT_POINT_DEFINITION_TYPE_NAME    = "EnforcementPointDefinition";
    /* ExecutionPointDefinition */

    public static final String CONTROL_POINT_CLASSIFICATION_TYPE_GUID    = "acf8b73e-3545-435d-ba16-fbfae060dd28";
    public static final String CONTROL_POINT_CLASSIFICATION_TYPE_NAME    = "ControlPoint";
    /* Referenceable */

    public static final String VERIFICATION_POINT_CLASSIFICATION_TYPE_GUID = "12d78c95-3879-466d-883f-b71f6477a741";
    public static final String VERIFICATION_POINT_CLASSIFICATION_TYPE_NAME = "VerificationPoint";
    /* Referenceable */

    public static final String ENFORCEMENT_POINT_CLASSIFICATION_TYPE_GUID  = "f4ce104e-7430-4c30-863d-60f6af6394d9";
    public static final String ENFORCEMENT_POINT_CLASSIFICATION_TYPE_NAME  = "EnforcementPoint";
    /* Referenceable */

    public static final String EXECUTION_POINT_USE_TYPE_GUID               = "3eb268f4-9419-4281-a487-d25ccd88eba3";
    public static final String EXECUTION_POINT_USE_TYPE_NAME               = "ExecutionPointUse";
    /* End1 = GovernanceDefinition; End 2 = ExecutionPointDefinition */

    public static final String POLICY_ADMINISTRATION_POINT_CLASSIFICATION_TYPE_GUID = "4f13baa3-31b3-4a85-985e-2abc784900b8";
    public static final String POLICY_ADMINISTRATION_POINT_CLASSIFICATION_TYPE_NAME = "PolicyAdministrationPoint";
    /* Referenceable */

    public static final String POLICY_DECISION_POINT_CLASSIFICATION_TYPE_GUID = "12d78c95-3879-466d-883f-b71f6477a741";
    public static final String POLICY_DECISION_POINT_CLASSIFICATION_TYPE_NAME = "PolicyDecisionPoint";
    /* Referenceable */

    public static final String POLICY_ENFORCEMENT_POINT_CLASSIFICATION_TYPE_GUID = "9a68b20b-3f84-4d7d-bc9e-790c4b27e685";
    public static final String POLICY_ENFORCEMENT_POINT_CLASSIFICATION_TYPE_NAME = "PolicyEnforcementPoint";
    /* Referenceable */

    public static final String POLICY_INFORMATION_POINT_CLASSIFICATION_TYPE_GUID = "2058ab6f-ddbf-45f9-9136-47354544e282";
    public static final String POLICY_INFORMATION_POINT_CLASSIFICATION_TYPE_NAME = "PolicyInformationPoint";
    /* Referenceable */

    public static final String POLICY_RETRIEVAL_POINT_CLASSIFICATION_TYPE_GUID = "d7367412-7ba6-409f-84db-42b51e859367";
    public static final String POLICY_RETRIEVAL_POINT_CLASSIFICATION_TYPE_NAME = "PolicyRetrievalPoint";
    /* Referenceable */


    public static final String POINT_TYPE_PROPERTY_NAME                   = "pointType";                                /* from Area 4 */


    public static final String GOVERNANCE_ENGINE_TYPE_GUID               = "3fa23d4a-aceb-422f-9301-04ed474c6f74";
    public static final String GOVERNANCE_ENGINE_TYPE_NAME               = "GovernanceEngine";
    /* SoftwareServerCapability */

    public static final String GOVERNANCE_SERVICE_TYPE_GUID              = "191d870c-26f4-4310-a021-b8ca8772719d";
    public static final String GOVERNANCE_SERVICE_TYPE_NAME              = "GovernanceService";
    /* DeployedConnector */

    public static final String GOVERNANCE_ACTION_ENGINE_TYPE_GUID        = "5d74250a-57ca-4197-9475-8911f620a94e";
    public static final String GOVERNANCE_ACTION_ENGINE_TYPE_NAME        = "GovernanceActionEngine";
    /* GovernanceEngine */

    public static final String GOVERNANCE_ACTION_SERVICE_TYPE_GUID       = "ececb378-31ac-4cc3-99b4-1c44e5fbc4d9";
    public static final String GOVERNANCE_ACTION_SERVICE_TYPE_NAME       = "GeneralGovernanceActionService";
    /* DeployedConnector */

    public static final String SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID = "2726df0e-4f3a-44e1-8433-4ca5301457fd";
    public static final String SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME = "SupportedGovernanceService";
    /* End1 = GovernanceEngine; End 2 = GovernanceService */

    public static final String REQUEST_TYPE_PROPERTY_NAME                = "requestType";        /* from SupportedGovernanceService relationship */
    public static final String SERVICE_REQUEST_TYPE_PROPERTY_NAME        = "serviceRequestType"; /* from SupportedGovernanceService relationship */
    public static final String REQUEST_PARAMETERS_PROPERTY_NAME          = "requestParameters";  /* from SupportedGovernanceService relationship */

    public static final String GOVERNANCE_ACTION_PROCESS_TYPE_GUID       = "4d3a2b8d-9e2e-4832-b338-21c74e45b238";
    public static final String GOVERNANCE_ACTION_PROCESS_TYPE_NAME       = "GovernanceActionProcess";
    /* Process */

    public static final String GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID = "92e20083-0393-40c0-a95b-090724a91ddc";
    public static final String GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME = "GovernanceActionProcessStep";
    /* Reference */

    public static final String PRODUCED_GUARDS_PROPERTY_NAME             = "producedGuards";     /* from GovernanceActionPRocessStep entity */

    public static final String GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_GUID = "5f6ddee5-31ea-4d4f-9c3f-00ad2fcb2aa0";
    public static final String GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME = "GovernanceActionProcessFlow";
    /* End1 = GovernanceActionProcess; End 2 = GovernanceActionProcessStep */

    public static final String GOVERNANCE_ACTION_PROCESS_STEP_EXECUTOR_TYPE_GUID = "f672245f-35b5-4ca7-b645-014cf61d5b75";
    public static final String GOVERNANCE_ACTION_PROCESS_STEP_EXECUTOR_TYPE_NAME = "GovernanceActionProcessStepExecutor";
    /* End1 = GovernanceActionProcessStep; End 2 = GovernanceEngine */

    public static final String NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID = "d9567840-9904-43a5-990b-4585c0446e00";
    public static final String NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME = "NextGovernanceActionProcessStep";
    /* End1 = GovernanceActionProcessStep; End 2 = GovernanceActionProcessStep */

    public static final String GUARD_PROPERTY_NAME                       = "guard"; /* from NextGovernanceActionProcessStep relationship */
    public static final String MANDATORY_GUARD_PROPERTY_NAME             = "mandatoryGuard"; /* from NextGovernanceActionProcessStep relationship */
    public static final String IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME    = "ignoreMultipleTriggers"; /* from NextGovernanceActionProcessStep relationship */
    public static final String WAIT_TIME_PROPERTY_NAME                   = "waitTime"; /* from NextGovernanceActionProcessStep relationship */

    public static final String ENGINE_ACTION_STATUS_ENUM_TYPE_GUID = "a6e698b0-a4f7-4a39-8c80-db0bb0f972ec";
    public static final String ENGINE_ACTION_STATUS_ENUM_TYPE_NAME = "EngineActionStatus";
    public static final int REQUESTED_EA_STATUS_ORDINAL   = 0;
    public static final int APPROVED_EA_STATUS_ORDINAL    = 1;
    public static final int WAITING_EA_STATUS_ORDINAL     = 2;
    public static final int IN_PROGRESS_EA_STATUS_ORDINAL = 4;
    public static final int ACTIONED_EA_STATUS_ORDINAL    = 10;
    public static final int INVALID_EA_STATUS_ORDINAL     = 11;
    public static final int IGNORED_EA_STATUS_ORDINAL     = 12;
    public static final int FAILED_EA_STATUS_ORDINAL      = 13;
    public static final int OTHER_EA_STATUS_ORDINAL       = 99;

    public static final String ENGINE_ACTION_TYPE_GUID = "c976d88a-2b11-4b40-b972-c38d41bfc6be";
    public static final String ENGINE_ACTION_TYPE_NAME = "EngineAction";
    /* Reference */

    public static final String EXECUTOR_ENGINE_GUID_PROPERTY_NAME        = "executorEngineGUID";        /* from EngineAction entity */
    public static final String EXECUTOR_ENGINE_NAME_PROPERTY_NAME        = "executorEngineName";        /* from EngineAction entity */
    public static final String PROCESS_NAME_PROPERTY_NAME                = "processName";               /* from EngineAction entity */
    public static final String PROCESS_STEP_GUID_PROPERTY_NAME           = "processStepGUID";           /* from EngineAction entity */
    public static final String PROCESS_STEP_NAME_PROPERTY_NAME           = "processStepName";           /* from EngineAction entity */

    public static final String OLD_PROCESS_STEP_GUID_PROPERTY_NAME       = "governanceActionTypeGUID";  /* from GovernanceAction entity */
    public static final String OLD_PROCESS_STEP_NAME_PROPERTY_NAME       = "governanceActionTypeName";  /* from GovernanceAction entity */
    public static final String MANDATORY_GUARDS_PROPERTY_NAME            = "mandatoryGuards";           /* from EngineAction entity */
    public static final String RECEIVED_GUARDS_PROPERTY_NAME             = "receivedGuards";            /* from EngineAction entity */
    public static final String START_DATE_PROPERTY_NAME                  = "startDate";                 /* from EngineAction and Project entity and RegisteredIntegrationConnector relationship*/
    public static final String PLANNED_END_DATE_PROPERTY_NAME            = "plannedEndDate";            /* from Project entity */
    public static final String ACTION_STATUS_PROPERTY_NAME               = "actionStatus";              /* from EngineAction entity */
    public static final String PROCESSING_ENGINE_USER_ID_PROPERTY_NAME   = "processingEngineUserId";    /* from EngineAction entity */
    public static final String COMPLETION_DATE_PROPERTY_NAME             = "completionDate";            /* from EngineAction entity */
    public static final String COMPLETION_GUARDS_PROPERTY_NAME           = "completionGuards";          /* from EngineAction entity */
    public static final String COMPLETION_MESSAGE_PROPERTY_NAME          = "completionMessage";         /* from EngineAction entity and TargetForAction relationship*/

    public static final String ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME   = "originGovernanceService"; /* from GovernanceActionTypeUse relationship */
    public static final String ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME    = "originGovernanceEngine";  /* from GovernanceActionTypeUse relationship */

    public static final String ENGINE_ACTION_REQUEST_SOURCE_TYPE_GUID = "5323a705-4c1f-456a-9741-41fdcb8e93ac";
    public static final String ENGINE_ACTION_REQUEST_SOURCE_TYPE_NAME = "EngineActionRequestSource";
    /* End1 = OpenMetadataRoot; End 2 = EngineAction */

    public static final String REQUEST_SOURCE_NAME_PROPERTY_NAME         = "requestSourceName"; /* from EngineActionRequestSource relationship */

    public static final String TARGET_FOR_ACTION_TYPE_GUID               = "46ec49bf-af66-4575-aab7-06ce895120cd";
    public static final String TARGET_FOR_ACTION_TYPE_NAME               = "TargetForAction";
    /* End1 = EngineAction; End 2 = Referenceable */

    public static final String ACTION_TARGET_NAME_PROPERTY_NAME          = "actionTargetName"; /* from TargetForAction relationship */

    public static final String NEXT_ENGINE_ACTION_TYPE_GUID              = "4efd16d4-f397-449c-a75d-ebea42fe581b";
    public static final String NEXT_ENGINE_ACTION_TYPE_NAME              = "NextEngineAction";
    /* End1 = EngineAction; End 2 = EngineAction */

    public static final String GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID      = "e690ab17-6779-46b4-a8f1-6872d88c1bbb";
    public static final String GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME      = "GovernanceActionExecutor";
    /* End1 = EngineAction; End 2 = GovernanceEngine */


    public static final String INTEGRATION_GROUP_TYPE_GUID                = "4d7c43ec-983b-40e4-af78-6fb66c4f5136";
    public static final String INTEGRATION_GROUP_TYPE_NAME                = "IntegrationGroup";

    public static final String INTEGRATION_CONNECTOR_TYPE_GUID            = "759da11b-ebb6-4382-bdc9-72adc7c922db";
    public static final String INTEGRATION_CONNECTOR_TYPE_NAME            = "IntegrationConnector";

    public static final String USES_BLOCKING_CALLS_PROPERTY_NAME          = "usesBlockingCalls";     /* from IntegrationConnector entity */

    public static final String REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID = "7528bcd4-ae4c-47d0-a33f-4aeebbaa92c2";
    public static final String REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME = "RegisteredIntegrationConnector";
    /* End1 = IntegrationGroup; End 2 = IntegrationConnector */

    public static final String CONNECTOR_NAME_PROPERTY_NAME               = "connectorName";  /* from RegisteredIntegrationConnector relationship */
    public static final String CONNECTOR_USER_ID_PROPERTY_NAME            = "connectorUserId";/* from RegisteredIntegrationConnector  relationship */
    public static final String METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME = "metadataSourceQualifiedName";  /* from RegisteredIntegrationConnector relationship */
    public static final String STOP_DATE_PROPERTY_NAME                    = "stopDate";       /* from RegisteredIntegrationConnector relationship */
    public static final String REFRESH_TIME_INTERVAL_PROPERTY_NAME        = "refreshTimeInterval"; /* from RegisteredIntegrationConnector relationship */
    public static final String GENERATE_INTEGRATION_REPORT_PROPERTY_NAME  = "generateIntegrationReport"; /* from RegisteredIntegrationConnector relationship */

    public static final String CATALOG_TARGET_RELATIONSHIP_TYPE_GUID      = "bc5a5eb1-881b-4055-aa2c-78f314282ac2";
    public static final String CATALOG_TARGET_RELATIONSHIP_TYPE_NAME      = "CatalogTarget";
    /* End1 = IntegrationConnector; End 2 = OpenMetadataRoot */

    public static final String CATALOG_TARGET_NAME_PROPERTY_NAME         = "catalogTargetName";     /* from CatalogTarget relationship */

    public static final String INTEGRATION_REPORT_TYPE_GUID              = "b8703d3f-8668-4e6a-bf26-27db1607220d";
    public static final String INTEGRATION_REPORT_TYPE_NAME              = "IntegrationReport";

    public static final String SERVER_NAME_PROPERTY_NAME                 = "serverName";             /* from IntegrationReport entity */
    public static final String CONNECTOR_ID_PROPERTY_NAME                = "connectorId";            /* from IntegrationReport entity */
    public static final String REFRESH_START_DATE_PROPERTY_NAME          = "refreshStartDate";       /* from IntegrationReport entity */
    public static final String REFRESH_COMPLETION_DATE_PROPERTY_NAME     = "refreshCompletionDate";  /* from IntegrationReport entity */
    public static final String CREATED_ELEMENTS_PROPERTY_NAME            = "createdElements";        /* from IntegrationReport entity */
    public static final String UPDATED_ELEMENTS_PROPERTY_NAME            = "updatedElements";        /* from IntegrationReport entity */
    public static final String DELETED_ELEMENTS_PROPERTY_NAME            = "deletedElements";        /* from IntegrationReport entity */

    public static final String RELATED_INTEGRATION_REPORT_TYPE_GUID      = "83d12156-f8f3-4b4b-b31b-18c140df9aa3";
    public static final String RELATED_INTEGRATION_REPORT_TYPE_NAME      = "RelatedIntegrationReport";
    /* End1 = OpenMetadataRoot; End 2 = IntegrationReport */

    public static final String KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID  = "e55062b2-907f-44bd-9831-255642285731";
    public static final String KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME  = "KnownDuplicate";
    /* Referenceable */

    public static final String PEER_DUPLICATE_LINK_TYPE_GUID             = "a94b2929-9e62-4b12-98ab-8ac45691e5bd";
    public static final String PEER_DUPLICATE_LINK_TYPE_NAME             = "PeerDuplicateLink";
    /* End1 = Referenceable (Oldest); End 2 = Referenceable (Newest) */

    public static final String CONSOLIDATED_DUPLICATE_TYPE_GUID          = "e40e80d7-5a29-482c-9a88-0dc7251f08de";
    public static final String CONSOLIDATED_DUPLICATE_TYPE_NAME          = "ConsolidatedDuplicate";

    public static final String CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID     = "a1fabffd-d6ec-4b2d-bfe4-646f27c07c82";
    public static final String CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME     = "ConsolidatedDuplicateLink";
    /* End1 = Referenceable (Detected Duplicate); End 2 = Referenceable (Result) */

    public static final String INCIDENT_REPORT_STATUS_ENUM_TYPE_GUID     = "a9d4f64b-fa24-4eb8-8bf6-308926ef2c14";
    public static final String INCIDENT_REPORT_STATUS_ENUM_TYPE_NAME     = "IncidentReportStatus";
    public static final int    RAISED_INCIDENT_ORDINAL                   = 0;
    public static final int    REVIEWED_INCIDENT_ORDINAL                 = 1;
    public static final int    VALIDATED_INCIDENT_ORDINAL                = 2;
    public static final int    RESOLVED_INCIDENT_ORDINAL                 = 3;
    public static final int    INVALID_INCIDENT_ORDINAL                  = 4;
    public static final int    IGNORED_INCIDENT_ORDINAL                  = 5;
    public static final int    OTHER_INCIDENT_ORDINAL                    = 99;

    public static final String INCIDENT_CLASSIFIER_TYPE_GUID             = "361158c0-ade1-4c92-a6a7-64f7ac39b87d";
    public static final String INCIDENT_CLASSIFIER_TYPE_NAME             = "IncidentClassifier";
    /* Referenceable */

    public static final String INCIDENT_CLASSIFIER_SET_TYPE_GUID         = "361158c0-ade1-4c92-a6a7-64f7ac39b87d";
    public static final String INCIDENT_CLASSIFIER_SET_TYPE_NAME         = "IncidentClassifierSet";
    /* Collection */

    public static final String CLASSIFIER_LABEL_PROPERTY_NAME            = "classifierLabel";       /* from IncidentClassifier entity */
    public static final String CLASSIFIER_IDENTIFIER_PROPERTY_NAME       = "classifierIdentifier";  /* from IncidentClassifier entity */
    public static final String CLASSIFIER_NAME_PROPERTY_NAME             = "classifierName";        /* from IncidentClassifier entity */
    public static final String CLASSIFIER_DESCRIPTION_PROPERTY_NAME      = "classifierDescription"; /* from IncidentClassifier entity */


    public static final String INCIDENT_REPORT_TYPE_GUID                 = "072f252b-dea7-4b88-bb2e-8f741c9ca7f6e";
    public static final String INCIDENT_REPORT_TYPE_NAME                 = "IncidentReport";
    /* Referenceable */

    public static final String BACKGROUND_PROPERTY_NAME                  = "background";     /* from IncidentReport entity */
    public static final String INCIDENT_STATUS_PROPERTY_NAME             = "incidentStatus"; /* from IncidentReport entity */

    public static final String INCIDENT_ORIGINATOR_TYPE_GUID             = "e490772e-c2c5-445a-aea6-1aab3499a76c";
    public static final String INCIDENT_ORIGINATOR_TYPE_NAME             = "IncidentOriginator";
    /* End1 = Referenceable; End 2 = IncidentReport */

    public static final String IMPACTED_RESOURCE_TYPE_GUID               = "0908e153-e0fd-499c-8a30-5ea8b81395cd";
    public static final String IMPACTED_RESOURCE_TYPE_NAME               = "ImpactedResource";
    /* End1 = Referenceable; End 2 = IncidentReport */

    public static final String SEVERITY_LEVEL_IDENTIFIER_PROPERTY_NAME   = "severityLevelIdentifier";       /* from Certification relationship */

    public static final String INCIDENT_DEPENDENCY_TYPE_GUID             = "017be6a8-0037-49d8-af5d-c45c41f25e0b";
    public static final String INCIDENT_DEPENDENCY_TYPE_NAME             = "IncidentDependency";
    /* End1 = IncidentReport; End 2 = IncidentReport */

    public static final String CERTIFICATION_TYPE_TYPE_GUID              = "97f9ffc9-e2f7-4557-ac12-925257345eea";
    public static final String CERTIFICATION_TYPE_TYPE_NAME              = "CertificationType";
    /* GovernanceDefinition */

    public static final String DETAILS_PROPERTY_NAME                     = "details";             /* from CertificationType/LicenseType entity */

    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID  = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME  = "Certification";
    /* End1 = Referenceable; End 2 = CertificationType */

    public static final String CERTIFICATE_GUID_PROPERTY_NAME            = "certificateGUID";           /* from Certification relationship */
    public static final String START_PROPERTY_NAME                       = "start";                     /* from Certification relationship */
    public static final String END_PROPERTY_NAME                         = "end";                       /* from Certification relationship */
    public static final String CONDITIONS_PROPERTY_NAME                  = "conditions";                /* from Certification relationship */
    public static final String CERTIFIED_BY_PROPERTY_NAME                = "certifiedBy";               /* from Certification relationship */
    public static final String CERTIFIED_BY_TYPE_NAME_PROPERTY_NAME      = "certifiedByTypeName";       /* from Certification relationship */
    public static final String CERTIFIED_BY_PROPERTY_NAME_PROPERTY_NAME  = "certifiedByPropertyName";   /* from Certification relationship */
    public static final String CUSTODIAN_PROPERTY_NAME                   = "custodian";                 /* from Certification and License relationship */
    public static final String CUSTODIAN_TYPE_NAME_PROPERTY_NAME         = "custodianTypeName";         /* from Certification and License relationship */
    public static final String CUSTODIAN_PROPERTY_NAME_PROPERTY_NAME     = "custodianPropertyName";     /* from Certification and License relationship */
    public static final String RECIPIENT_PROPERTY_NAME                   = "recipient";                 /* from Certification relationship */
    public static final String RECIPIENT_TYPE_NAME_PROPERTY_NAME         = "recipientTypeName";         /* from Certification relationship */
    public static final String RECIPIENT_PROPERTY_NAME_PROPERTY_NAME     = "recipientPropertyName";     /* from Certification relationship */

    public static final String REFERENCEABLE_TO_LICENSE_TYPE_GUID        = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String REFERENCEABLE_TO_LICENSE_TYPE_NAME        = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String LICENSE_TYPE_TYPE_GUID                    = "046a049d-5f80-4e5b-b0ae-f3cf6009b513";
    public static final String LICENSE_TYPE_TYPE_NAME                    = "LicenseType";
    /* GovernanceDefinition */

    public static final String LICENSE_OF_REFERENCEABLE_TYPE_GUID      = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String LICENSE_OF_REFERENCEABLE_TYPE_NAME      = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String LICENSE_GUID_PROPERTY_NAME              = "licenseGUID";            /* from License relationship */
    public static final String LICENSED_BY_PROPERTY_NAME               = "licensedBy";             /* from License relationship */
    public static final String LICENSED_BY_TYPE_NAME_PROPERTY_NAME     = "licensedByTypeName";     /* from License relationship */
    public static final String LICENSED_BY_PROPERTY_NAME_PROPERTY_NAME = "licensedByPropertyName"; /* from License relationship */
    public static final String LICENSEE_PROPERTY_NAME                  = "licensee";               /* from License relationship */
    public static final String LICENSEE_TYPE_NAME_PROPERTY_NAME        = "licenseeTypeName";       /* from License relationship */
    public static final String LICENSEE_PROPERTY_NAME_PROPERTY_NAME    = "licenseePropertyName";   /* from License relationship */


    /* ============================================================================================================================*/
    /* Area 5 - Schemas and Models                                                                                                 */
    /* ============================================================================================================================*/

    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_GUID            = "815b004d-73c6-4728-9dd9-536f4fe803cd";  /* from Area 5 */
    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_NAME            = "AssetSchemaType";
    /* End1 = Asset; End 2 = SchemaType */

    public static final String SCHEMA_ELEMENT_TYPE_GUID                 = "718d4244-8559-49ed-ad5a-10e5c305a656";   /* from Area 5 */
    public static final String SCHEMA_ELEMENT_TYPE_NAME                 = "SchemaElement";
    /* Referenceable */

    public static final String SCHEMA_DISPLAY_NAME_PROPERTY_NAME = "displayName";          /* from SchemaElement entity */
    public static final String SCHEMA_DESCRIPTION_PROPERTY_NAME  = "description";          /* from SchemaElement entity */
    public static final String IS_DEPRECATED_PROPERTY_NAME       = "isDeprecated";         /* from SchemaElement and ValidValueDefinition entity */

    /* For Schema Type */
    public static final String SCHEMA_TYPE_TYPE_GUID                    = "5bd4a3e7-d22d-4a3d-a115-066ee8e0754f";   /* from Area 5 */
    public static final String SCHEMA_TYPE_TYPE_NAME                    = "SchemaType";
    /* SchemaElement */

    public static final String VERSION_NUMBER_PROPERTY_NAME    = "versionNumber";        /* from SchemaType entity */
    public static final String AUTHOR_PROPERTY_NAME            = "author";               /* from SchemaType entity */
    public static final String SCHEMA_USAGE_PROPERTY_NAME      = "usage";                /* from SchemaType entity */
    public static final String ENCODING_STANDARD_PROPERTY_NAME = "encodingStandard";     /* from SchemaType entity */
    public static final String NAMESPACE_PROPERTY_NAME         = "namespace";            /* from SchemaType entity */

    /* For Complex Schema Type */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_GUID            = "786a6199-0ce8-47bf-b006-9ace1c5510e4";    /* from Area 5 */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_NAME            = "ComplexSchemaType";
    /* SchemaType */

    public static final String STRUCT_SCHEMA_TYPE_TYPE_GUID             = "a13b409f-fd67-4506-8d94-14dfafd250a4";    /* from Area 5 */
    public static final String STRUCT_SCHEMA_TYPE_TYPE_NAME             = "StructSchemaType";
    /* ComplexSchemaType */

    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";  /* from Area 5 */
    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "AttributeForSchema";
    /* End1 = ComplexSchemaType; End 2 = SchemaAttribute */

    /* For Literal Schema Type */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_GUID            = "520ebb91-c4eb-4d46-a3b1-974875cdcf0d";  /* from Area 5 */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_NAME            = "LiteralSchemaType";
    /* SchemaType */

    /* For External Schema Type */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_GUID           = "78de00ea-3d69-47ff-a6d6-767587526624";  /* from Area 5 */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_NAME           = "ExternalSchemaType";
    /* SchemaType */

    public static final String LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID = "9a5d78c2-1716-4783-bfc6-c300a9e2d092";  /* from Area 5 */
    public static final String LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME = "LinkedExternalSchemaType";
    /* End1 = SchemaElement; End 2 = SchemaType */

    /* For Schema Type Choice */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_GUID             = "5caf954a-3e33-4cbd-b17d-8b8613bd2db8";  /* from Area 5 */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_NAME             = "SchemaTypeChoice";
    /* SchemaType */

    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID = "eb4f1f98-c649-4560-8a46-da17c02764a9";   /* from Area 5 */
    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME = "SchemaTypeOption";
    /* End1 = SchemaTypeChoice; End 2 = SchemaType */

    /* For Schema Type Choice */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_GUID            = "b5ec6e07-6419-4225-9dc4-fb55aba255c6";  /* from Area 5 */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_NAME            = "SimpleSchemaType";
    /* SchemaType */

    /* For Primitive Schema Type */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_GUID          = "f0f75fba-9136-4082-8352-0ad74f3c36ed";  /* from Area 5 */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_NAME          = "PrimitiveSchemaType";
    /* SimpleSchemaType */

    /* For Enum Schema Type */
    public static final String ENUM_SCHEMA_TYPE_TYPE_GUID               = "24b092ac-42e9-43dc-aeca-eb034ce307d9";  /* from Area 5 */
    public static final String ENUM_SCHEMA_TYPE_TYPE_NAME               = "EnumSchemaType";
    /* SimpleSchemaType */

    public static final String DATA_TYPE_PROPERTY_NAME                  = "dataType";     /* from SimpleSchemaType and LiteralSchemaType entity */
    public static final String FIXED_VALUE_PROPERTY_NAME                = "fixedValue";   /* from LiteralSchemaType entity */

    /* For Map Schema Type */
    public static final String MAP_SCHEMA_TYPE_TYPE_GUID                = "bd4c85d0-d471-4cd2-a193-33b0387a19fd";   /* from Area 5 */
    public static final String MAP_SCHEMA_TYPE_TYPE_NAME                = "MapSchemaType";
    /* SchemaType */

    public static final String MAP_TO_RELATIONSHIP_TYPE_GUID            = "8b9856b3-451e-45fc-afc7-fddefd81a73a";   /* from Area 5 */
    public static final String MAP_TO_RELATIONSHIP_TYPE_NAME            = "MapToElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    public static final String MAP_FROM_RELATIONSHIP_TYPE_GUID          = "6189d444-2da4-4cd7-9332-e48a1c340b44";   /* from Area 5 */
    public static final String MAP_FROM_RELATIONSHIP_TYPE_NAME          = "MapFromElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    /* For Bounded Schema Type (Deprecated) */
    public static final String BOUNDED_SCHEMA_TYPE_TYPE_GUID            = "77133161-37a9-43f5-aaa3-fd6d7ff92fdb";   /* from Area 5 */
    public static final String BOUNDED_SCHEMA_TYPE_TYPE_NAME            = "BoundedSchemaType";

    public static final String MAX_ELEMENTS_PROPERTY_NAME               = "maximumElements";      /* from BoundedSchemaType entity */

    public static final String BOUNDED_ELEMENT_RELATIONSHIP_TYPE_GUID   = "3e844049-e59b-45dd-8e62-cde1add59f9e";   /* from Area 5 */
    public static final String BOUNDED_ELEMENT_RELATIONSHIP_TYPE_NAME   = "BoundedSchemaElementType";
    /* End1 = BoundedSchemaType; End 2 = SchemaType */

    /* For Schema Attribute */
    public static final String SCHEMA_ATTRIBUTE_TYPE_GUID               = "1a5e159b-913a-43b1-95fe-04433b25fca9";   /* from Area 5 */
    public static final String SCHEMA_ATTRIBUTE_TYPE_NAME               = "SchemaAttribute";
    /* SchemaElement */

    public static final String ELEMENT_POSITION_PROPERTY_NAME           = "position";              /* from SchemaAttribute entity */
    public static final String CARDINALITY_PROPERTY_NAME                = "cardinality";           /* from SchemaAttribute entity */
    public static final String MAX_CARDINALITY_PROPERTY_NAME            = "maxCardinality";        /* from SchemaAttribute entity */
    public static final String MIN_CARDINALITY_PROPERTY_NAME            = "minCardinality";        /* from SchemaAttribute entity */
    public static final String DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME     = "defaultValueOverride";  /* from SchemaAttribute entity */
    public static final String ALLOWS_DUPLICATES_PROPERTY_NAME          = "allowsDuplicateValues"; /* from SchemaAttribute entity */
    public static final String ORDERED_VALUES_PROPERTY_NAME             = "orderedValues";         /* from SchemaAttribute entity */
    public static final String NATIVE_CLASS_PROPERTY_NAME               = "nativeClass";           /* from SchemaAttribute entity */
    public static final String ALIASES_PROPERTY_NAME                    = "aliases";               /* from SchemaAttribute entity */
    public static final String SORT_ORDER_PROPERTY_NAME                 = "sortOrder";             /* from SchemaAttribute entity */
    public static final String MIN_LENGTH_PROPERTY_NAME                 = "minimumLength";         /* from SchemaAttribute entity */
    public static final String LENGTH_PROPERTY_NAME                     = "length";                /* from SchemaAttribute entity */
    public static final String SIGNIFICANT_DIGITS_PROPERTY_NAME         = "significantDigits";     /* from SchemaAttribute entity */
    public static final String PRECISION_PROPERTY_NAME                  = "precision";             /* from SchemaAttribute entity */
    public static final String VERSION_PROPERTY_NAME                    = "version";             /* from SchemaAttribute entity */
    public static final String IS_NULLABLE_PROPERTY_NAME                = "isNullable";            /* from SchemaAttribute entity */

    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID = "2d955049-e59b-45dd-8e62-cde1add59f9e";  /* from Area 5 */
    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME = "SchemaAttributeType";
    /* End1 = SchemaAttribute; End 2 = SchemaType */

    public static final String DATA_ITEM_SORT_ORDER_TYPE_GUID           = "aaa4df8f-1aca-4de8-9abd-1ef2aadba300";  /* from Area 5 */
    public static final String DATA_ITEM_SORT_ORDER_TYPE_NAME           = "DataItemSortOrder";

    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID  = "0ffb9d87-7074-45da-a9b0-ae0859611133";  /* from Area 5 */
    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME  = "NestedSchemaAttribute";
    /* End1 = SchemaAttribute; End 2 = SchemaAttribute */

    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID  = "e2bb76bb-774a-43ff-9045-3a05f663d5d9";  /* from Area 5 */
    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME  = "TypeEmbeddedAttribute";
    /* Linked to SchemaAttribute */
    public static final String TYPE_NAME_PROPERTY_NAME                  = "schemaTypeName";      /* from TypeEmbeddedAttribute classification */

    /* For Schema Link */
    public static final String SCHEMA_LINK_TYPE_GUID                    = "67e08705-2d2a-4df6-9239-1818161a41e0";      /* from Area 5 */
    public static final String SCHEMA_LINK_TYPE_NAME                    = "SchemaLinkElement";
    /* SchemaElement */

    public static final String LINK_NAME_PROPERTY_NAME                  = "linkName";             /* from SchemaAttribute entity */
    public static final String LINK_PROPERTIES_PROPERTY_NAME            = "linkProperties";       /* from SchemaAttribute entity */

    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_GUID      = "292125f7-5660-4533-a48a-478c5611922e";     /* from Area 5 */
    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_NAME      = "LinkedType";
    /* End1 = SchemaLinkElement; End 2 = SchemaType */

    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_GUID = "db9583c5-4690-41e5-a580-b4e30a0242d3";     /* from Area 5 */
    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_NAME = "SchemaLinkToType";
    /* End1 = SchemaAttribute; End 2 = SchemaLinkElement */

    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID = "1c2622b7-ac21-413c-89e1-6f61f348cd19"; /* from Area 5 */
    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME = "DerivedSchemaTypeQueryTarget";
    /* End1 = SchemaElement; End 2 = SchemaElement (target) */

    public static final String QUERY_ID_PROPERTY_NAME                   = "queryId"; /* from DerivedSchemaTypeQueryTarget relationship */
    public static final String QUERY_PROPERTY_NAME                      = "query";   /* from DerivedSchemaTypeQueryTarget relationship */

    /* - Known Subtypes ------------------------------------------------------- */

    public static final String ARRAY_SCHEMA_TYPE_TYPE_GUID              = "ba8d29d2-a8a4-41f3-b29f-91ad924dd944";   /* from Area 5 */
    public static final String ARRAY_SCHEMA_TYPE_TYPE_NAME              = "ArraySchemaType";
    /* BoundedSchemaType */

    public static final String SET_SCHEMA_TYPE_TYPE_GUID                = "b2605d2d-10cd-443c-b3e8-abf15fb051f0";   /* from Area 5 */
    public static final String SET_SCHEMA_TYPE_TYPE_NAME                = "SetSchemaType";
    /* BoundedSchemaType */

    public static final String PORT_SCHEMA_RELATIONSHIP_TYPE_GUID       = "B216fA00-8281-F9CC-9911-Ae6377f2b457"; /* from Area 5 */
    public static final String PORT_SCHEMA_RELATIONSHIP_TYPE_NAME       = "PortSchema";
    /* End1 = Port; End 2 = SchemaType */

    public static final String TABULAR_SCHEMA_TYPE_TYPE_GUID            = "248975ec-8019-4b8a-9caf-084c8b724233";   /* from Area 5 */
    public static final String TABULAR_SCHEMA_TYPE_TYPE_NAME            = "TabularSchemaType";
    /* ComplexSchemaType */

    public static final String TABULAR_COLUMN_TYPE_GUID                 = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";   /* from Area 5 */
    public static final String TABULAR_COLUMN_TYPE_NAME                 = "TabularColumn";
    /* PrimitiveSchemaType */

    public static final String TABULAR_FILE_COLUMN_TYPE_GUID            = "af6265e7-5f58-4a9c-9ae7-8d4284be62bd";   /* from Area 5 */
    public static final String TABULAR_FILE_COLUMN_TYPE_NAME            = "TabularFileColumn";
    /* TabularColumn */

    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_GUID           = "33da99cd-8d04-490c-9457-c58908da7794";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_NAME           = "DocumentSchemaType";
    /* ComplexSchemaType */

    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_GUID      = "b5cefb7e-b198-485f-a1d7-8e661012499b";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_NAME      = "DocumentSchemaAttribute";
    /* SchemaAttribute */

    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_GUID           = "42cfccbf-cc68-4980-8c31-0faf1ee002d3";   /* from Area 5 */
    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_NAME           = "SimpleDocumentType";
    /* PrimitiveSchemaType */

    public static final String STRUCT_DOCUMENT_TYPE_TYPE_GUID           = "f6245c25-8f73-45eb-8fb5-fa17a5f27649";   /* from Area 5 */
    public static final String STRUCT_DOCUMENT_TYPE_TYPE_NAME           = "StructDocumentType";
    /* StructSchemaType */

    public static final String MAP_DOCUMENT_TYPE_TYPE_GUID              = "b0f09598-ceb6-415b-befc-563ecadd5727";   /* from Area 5 */
    public static final String MAP_DOCUMENT_TYPE_TYPE_NAME              = "MapDocumentType";
    /* MapSchemaType */

    public static final String OBJECT_SCHEMA_TYPE_TYPE_GUID             = "6920fda1-7c07-47c7-84f1-9fb044ae153e";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_TYPE_TYPE_NAME             = "ObjectSchemaType";
    /* ComplexSchemaType */

    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_GUID        = "ccb408c0-582e-4a3a-a926-7082d53bb669";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_NAME        = "ObjectSchemaAttribute";
    /* SchemaAttribute */

    public static final String GRAPH_SCHEMA_TYPE_TYPE_GUID              = "983c5e72-801b-4e42-bc51-f109527f2317";   /* from Area 5 */
    public static final String GRAPH_SCHEMA_TYPE_TYPE_NAME              = "GraphSchemaType";
    /* ComplexSchemaType */

    public static final String GRAPH_VERTEX_TYPE_GUID                   = "1252ce12-540c-4724-ad70-f70940956de0";   /* from Area 5 */
    public static final String GRAPH_VERTEX_TYPE_NAME                   = "GraphVertex";
    /* SchemaAttribute */

    public static final String GRAPH_EDGE_TYPE_GUID                     = "d4104eb3-4f2d-4d83-aca7-e58dd8d5e0b1";   /* from Area 5 */
    public static final String GRAPH_EDGE_TYPE_NAME                     = "GraphEdge";
    /* SchemaAttribute */

    /* For Graph Edge/Vertex */
    public static final String GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_GUID   = "503b4221-71c8-4ba9-8f3d-6a035b27971c";   /* from Area 5 */
    public static final String GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_NAME   = "GraphEdgeLink";
    /* End1 = GraphEdge; End 2 = GraphVertex */

    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID      = "f20f5f45-1afb-41c1-9a09-34d8812626a4";   /* from Area 5 */
    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME      = "RelationalDBSchemaType";
    /* ComplexSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_TYPE_GUID          = "1321bcc0-dc6a-48ed-9ca6-0c6f934b0b98";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_TYPE_NAME          = "RelationalTableType";
    /* TabularSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_GUID               = "ce7e72b8-396a-4013-8688-f9d973067425";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_NAME               = "RelationalTable";
    /* SchemaAttribute */

    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID = "4814bec8-482d-463d-8376-160b0358e139";
    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME = "CalculatedValue";
    /* Linked to SchemaType */

    public static final String RELATIONAL_COLUMN_TYPE_GUID              = "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9";   /* from Area 5 */
    public static final String RELATIONAL_COLUMN_TYPE_NAME              = "RelationalColumn";
    /* TabularColumn */

    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_GUID     = "b239d832-50bd-471b-b17a-15a335fc7f40";
    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_NAME     = "PrimaryKey";
    /* Linked to RelationalColumn */
    public static final String PRIMARY_KEY_PATTERN_PROPERTY_NAME        = "keyPattern";    /* from PrimaryKey classification */
    public static final String PRIMARY_KEY_NAME_PROPERTY_NAME           = "name";          /* from PrimaryKey classification */

    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_GUID       = "3cd4e0e7-fdbf-47a6-ae88-d4b3205e0c07"; /* from Area 5 */
    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_NAME       = "ForeignKey";
    /* End1 = RelationalColumn; End 2 = RelationalColumn */
    public static final String FOREIGN_KEY_NAME_PROPERTY_NAME           = "name";          /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME    = "description";   /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME     = "confidence";    /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_STEWARD_PROPERTY_NAME        = "steward";       /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_SOURCE_PROPERTY_NAME         = "source";        /* from ForeignKey relationship */

    /* For Event Types */
    public static final String EVENT_TYPE_LIST_TYPE_GUID                = "77ccda3d-c4c6-464c-a424-4b2cb27ac06c";   /* from Area 5 */
    public static final String EVENT_TYPE_LIST_TYPE_NAME                = "EventTypeList";
    /* ComplexSchemaType */

    public static final String EVENT_TYPE_TYPE_GUID                     = "8bc88aba-d7e4-4334-957f-cfe8e8eadc32";   /* from Area 5 */
    public static final String EVENT_TYPE_TYPE_NAME                     = "EventType";
    /* ComplexSchemaType */

    public static final String EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID         = "5be4ee8f-4d0c-45cd-a411-22a468950342";   /* from Area 5 */
    public static final String EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME         = "EventSchemaAttribute";
    /* SchemaAttribute */

    public static final String EVENT_SET_TYPE_GUID                      = "bead9aa4-214a-4596-8036-aa78395bbfb1";   /* from Area 5 */
    public static final String EVENT_SET_TYPE_NAME                      = "EventSet";
    /* Collection */

    /* For API Schema Type */
    public static final String API_SCHEMA_TYPE_TYPE_GUID                = "b46cddb3-9864-4c5d-8a49-266b3fc95cb8";   /* from Area 5 */
    public static final String API_SCHEMA_TYPE_TYPE_NAME                = "APISchemaType";
    /* SchemaType */
    public static final String API_OPERATION_TYPE_GUID                  = "f1c0af19-2729-4fac-996e-a7badff3c21c";   /* from Area 5 */
    public static final String API_OPERATION_TYPE_NAME                  = "APIOperation";
    /* SchemaType */
    public static final String API_PARAMETER_LIST_TYPE_GUID             = "ba167b12-969f-49d3-8bea-d04228d9a44b";   /* from Area 5 */
    public static final String API_PARAMETER_LIST_TYPE_NAME             = "APIParameterList";
    /* ComplexSchemaType */

    public static final String ENCODING_PROPERTY_NAME                   = "encoding";        /* from APIParameter and APIParameterList entities */
    public static final String REQUIRED_PROPERTY_NAME                   = "required";        /* from APIParameterList entity */
    public static final String PARAMETER_TYPE_PROPERTY_NAME             = "parameterType";   /* from APIParameter entity */

    public static final String API_PARAMETER_TYPE_GUID                  = "10277b13-509c-480e-9829-bc16d0eafc53";   /* from Area 5 */
    public static final String API_PARAMETER_TYPE_NAME                  = "APIParameter";
    /* SchemaAttribute */

    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_GUID    = "03737169-ceb5-45f0-84f0-21c5929945af"; /* from Area 5 */
    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_NAME    = "APIOperations";
    /* End1 = APISchemaType; End 2 = APIOperation */
    public static final String API_HEADER_RELATIONSHIP_TYPE_GUID        = "e8fb46d1-5f75-481b-aa66-f43ad44e2cc6"; /* from Area 5 */
    public static final String API_HEADER_RELATIONSHIP_TYPE_NAME        = "APIHeader";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_GUID       = "4ab3b466-31bd-48ea-8aa2-75623476f2e2"; /* from Area 5 */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_NAME       = "APIRequest";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_GUID      = "e8001de2-1bb1-442b-a66f-9addc3641eae"; /* from Area 5 */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_NAME      = "APIResponse";
    /* End1 = APIOperation; End 2 = SchemaType */

    public static final String REFERENCEABLE_TO_REFERENCE_VALUE_TYPE_GUID = "111e6d2e-94e9-43ed-b4ed-f0d220668cbf";
    public static final String REFERENCEABLE_TO_REFERENCE_VALUE_TYPE_NAME = "ReferenceValueAssignment";
    /* End1 = Referenceable; End 2 = ValidValueDefinition */

    public static final String DISPLAY_DATA_SCHEMA_TYPE_TYPE_GUID       = "2f5796f5-3fac-4501-9d0d-207aa8620d16";   /* from Area 5 */
    public static final String DISPLAY_DATA_SCHEMA_TYPE_TYPE_NAME       = "DisplayDataSchemaType";
    /* ComplexSchemaType */

    public static final String DISPLAY_DATA_CONTAINER_TYPE_GUID         = "f2a4ff99-1954-48c0-8081-92d1a4dfd910";   /* from Area 5 */
    public static final String DISPLAY_DATA_CONTAINER_TYPE_NAME         = "DisplayDataContainer";
    /* SchemaAttribute */

    public static final String DISPLAY_DATA_FIELD_TYPE_GUID             = "46f9ea33-996e-4c62-a67d-803df75ef9d4";   /* from Area 5 */
    public static final String DISPLAY_DATA_FIELD_TYPE_NAME             = "DisplayDataField";
    /* SchemaAttribute */

    public static final String INPUT_FIELD_PROPERTY_NAME                = "inputField";            /* from DisplayDataField entity */

    public static final String QUERY_SCHEMA_TYPE_TYPE_GUID              = "4d11bdbb-5d4a-488b-9f16-bf1e34d34dd9";   /* from Area 5 */
    public static final String QUERY_SCHEMA_TYPE_TYPE_NAME              = "QuerySchemaType";
    /* ComplexSchemaType */

    public static final String QUERY_DATA_CONTAINER_TYPE_GUID           = "0eb92215-52b1-4fac-92e7-ff02ff385a68";   /* from Area 5 */
    public static final String QUERY_DATA_CONTAINER_TYPE_NAME           = "QueryDataContainer";
    /* SchemaAttribute */

    public static final String QUERY_DATA_FIELD_TYPE_GUID               = "0eb92215-52b1-4fac-92e7-ff02ff385a68";   /* from Area 5 */
    public static final String QUERY_DATA_FIELD_TYPE_NAME               = "QueryDataField";
    /* SchemaAttribute */

    public static final String VALID_VALUE_DEFINITION_TYPE_GUID         = "09b2133a-f045-42cc-bb00-ee602b74c618";   /* from Area 5 */
    public static final String VALID_VALUE_DEFINITION_TYPE_NAME         = "ValidValueDefinition";
    /* Referenceable */

    public static final String VALID_VALUE_DISPLAY_NAME_PROPERTY_NAME  = "name";                 /* from ValidValueDefinition entity */
    public static final String VALID_VALUE_DESCRIPTION_PROPERTY_NAME   = "description";          /* from ValidValueDefinition entity */
    public static final String VALID_VALUE_SCOPE_PROPERTY_NAME         = "scope";                /* from ValidValueDefinition entity */
    public static final String PREFERRED_VALUE_PROPERTY_NAME           = "preferredValue";       /* from ValidValueDefinition entity */

    public static final String VALID_VALUE_SET_TYPE_GUID                = "7de10805-7c44-40e3-a410-ffc51306801b";   /* from Area 5 */
    public static final String VALID_VALUE_SET_TYPE_NAME                = "ValidValuesSet";
    /* ValidValueDefinition */

    public static final String REFERENCE_DATA_CLASSIFICATION_TYPE_GUID  = "55e5ae33-39c6-4834-9d05-ef0ae4e0163b";  /* from Area 5 */
    public static final String REFERENCE_DATA_CLASSIFICATION_TYPE_NAME  = "ReferenceData";
    /* Linked to Asset */

    public static final String INSTANCE_METADATA_CLASSIFICATION_TYPE_GUID  = "e6d5c097-a5e9-4bc4-a614-2506276059af";  /* from Area 5 */
    public static final String INSTANCE_METADATA_CLASSIFICATION_TYPE_NAME  = "InstanceMetadata";
    /* Linked to SchemaElement */

    public static final String INSTANCE_METADATA_TYPE_NAME_PROPERTY_NAME   = "typeName";         /* from InstanceMetadata classification */

    public static final String VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID = "c5d48b73-eadd-47db-ab64-3be99b2fb32d";  /* from Area 5 */
    public static final String VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME = "ValidValuesAssignment";
    /* End1 = Referenceable; End 2 = ValidValuesDefinition */

    public static final String IS_STRICT_REQUIREMENT_PROPERTY_NAME      = "strictRequirement";     /* from ValidValuesAssignment relationship */
    public static final String IS_DEFAULT_VALUE_PROPERTY_NAME      = "isDefaultValue";             /* from ValidValuesMember relationship */

    public static final String VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID = "6337c9cd-8e5a-461b-97f9-5151bcb97a9e";  /* from Area 5 */
    public static final String VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME = "ValidValueMember";
    /* End1 = ValidValuesSet; End 2 = ValidValuesDefinition */

    public static final String VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID = "d9a39553-6a47-4477-a217-844300c07cf2";  /* from Area 5 */
    public static final String VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME = "ValidValuesImplementation";
    /* End1 = ValidValuesDefinition; End 2 = Asset */

    public static final String SYMBOLIC_NAME_PROPERTY_NAME             = "symbolicName";            /* from ValidValuesImplementation relationship */
    public static final String IMPLEMENTATION_VALUE_PROPERTY_NAME      = "implementationValue";     /* from ValidValuesImplementation relationship */
    public static final String ADDITIONAL_VALUES_PROPERTY_NAME         = "additionalValues";        /* from ValidValuesImplementation relationship */

    public static final String VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID = "203ce62c-3cbf-4542-bf82-81820cba718f";  /* from Area 5 */
    public static final String VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME = "ValidValuesMapping";
    /* End1 = ValidValuesDefinition; End 2 = ValidValuesDefinition */

    public static final String ASSOCIATION_DESCRIPTION_PROPERTY_NAME = "associationDescription";  /* from ValidValuesMapping relationship */

    public static final String REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID = "111e6d2e-94e9-43ed-b4ed-f0d220668cbf";  /* from Area 5 */
    public static final String REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME = "ReferenceValueAssignment";
    /* End1 = Referenceable; End 2 = ValidValuesDefinition */



    /* ============================================================================================================================*/
    /* Area 6 - Discovery                                                                                                          */
    /* ============================================================================================================================*/

    public static final String DISCOVERY_ENGINE_TYPE_GUID                = "be650674-790b-487a-a619-0a9002488055";
    public static final String DISCOVERY_ENGINE_TYPE_NAME                = "OpenDiscoveryEngine";
    /* GovernanceEngine */

    public static final String DISCOVERY_SERVICE_TYPE_GUID               = "2f278dfc-4640-4714-b34b-303e84e4fc40";
    public static final String DISCOVERY_SERVICE_TYPE_NAME               = "OpenDiscoveryService";
    /* GovernanceService */

    public static final String DISCOVERY_PIPELINE_TYPE_GUID              = "081abe00-740e-4143-b0d5-a1f55450fc22";
    public static final String DISCOVERY_PIPELINE_TYPE_NAME              = "OpenDiscoveryPipeline";
    /* GovernanceService */

    public static final String CONNECTION_TO_ASSET_TYPE_GUID             = "e777d660-8dbe-453e-8b83-903771f054c0";
    public static final String CONNECTION_TO_ASSET_TYPE_NAME             = "ConnectionToAsset";
    /* End1 = Connection; End 2 = Asset */

    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_GUID    = "acc7cbc8-09c3-472b-87dd-f78459323dcb";
    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_NAME    = "OpenDiscoveryAnalysisReport";
    /* Referenceable */

    public static final String EXECUTION_DATE_PROPERTY_NAME           = "executionDate";              /* from OpenDiscoveryAnalysisReport entity */
    public static final String ANALYSIS_PARAMS_PROPERTY_NAME          = "analysisParameters";         /* from OpenDiscoveryAnalysisReport entity */
    public static final String ANALYSIS_STEP_PROPERTY_NAME            = "discoveryRequestStep";       /* from OpenDiscoveryAnalysisReport entity */
    public static final String DISCOVERY_SERVICE_STATUS_PROPERTY_NAME = "discoveryServiceStatus";     /* from OpenDiscoveryAnalysisReport entity */

    public static final String DISCOVERY_REQUEST_STATUS_ENUM_TYPE_GUID  = "b2fdeddd-24eb-4e9c-a2a4-2693828d4a69";
    public static final String DISCOVERY_REQUEST_STATUS_ENUM_TYPE_NAME  = "DiscoveryServiceRequestStatus";

    public static final String REPORT_TO_ASSET_TYPE_GUID              = "7eded424-f176-4258-9ae6-138a46b2845f";     /* from Area 6 */
    public static final String REPORT_TO_ASSET_TYPE_NAME              = "AssetDiscoveryReport";
    /* End1 = Asset; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ENGINE_TYPE_GUID             = "2c318c3a-5dc2-42cd-a933-0087d852f67f";    /* from Area 6 */
    public static final String REPORT_TO_ENGINE_TYPE_NAME             = "DiscoveryEngineReport";
    /* End1 = OpenDiscoveryEngine; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_SERVICE_TYPE_GUID            = "1744d72b-903d-4273-9229-de20372a17e2";   /* from Area 6 */
    public static final String REPORT_TO_SERVICE_TYPE_NAME            = "DiscoveryInvocationReport";
    /* End1 = OpenDiscoveryService; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ANNOTATIONS_TYPE_GUID        = "51d386a3-3857-42e3-a3df-14a6cad08b93";   /* from Area 6 */
    public static final String REPORT_TO_ANNOTATIONS_TYPE_NAME        = "DiscoveredAnnotation";
    /* End1 = Annotation; End 2 = OpenDiscoveryAnalysisReport */


    public static final String ANNOTATION_TYPE_GUID    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String ANNOTATION_TYPE_NAME    = "Annotation";

    public static final String ANNOTATION_TYPE_PROPERTY_NAME       = "annotationType";        /* from Annotation entity */
    public static final String CONFIDENCE_LEVEL_PROPERTY_NAME      = "confidenceLevel";       /* from Annotation entity */
    public static final String EXPLANATION_PROPERTY_NAME           = "explanation";           /* from Annotation entity */
    public static final String JSON_PROPERTIES_PROPERTY_NAME       = "jsonProperties";        /* from Annotation entity */

    public static final String ANNOTATION_TO_EXTENSION_TYPE_GUID   = "605aaa6d-682e-405c-964b-ca6aaa94be1b";     /* from Area 6 */
    public static final String ANNOTATION_TO_EXTENSION_TYPE_NAME   = "Annotation";
    /* End1 = (extended)Annotation; End 2 = Annotation(Extension) */

    /* For AnnotationReview entity */
    public static final String ANNOTATION_REVIEW_TYPE_GUID         = "b893d6fc-642a-454b-beaf-809ee4dd876a";
    public static final String ANNOTATION_REVIEW_TYPE_NAME         = "AnnotationReview";

    public static final String REVIEW_DATE_PROPERTY_NAME           = "reviewDate";     /* from AnnotationReview entity */
    public static final String COMMENT_PROPERTY_NAME               = "comment";        /* from AnnotationReview entity */

    /* For AnnotationReviewLink relationship */
    public static final String ANNOTATION_REVIEW_LINK_TYPE_GUID    = "5d3c2fb7-fa04-4d77-83cb-fd9216a07769";
    public static final String ANNOTATION_REVIEW_LINK_TYPE_NAME    = "AnnotationReviewLink";
    /* End1 = Annotation; End 2 = AnnotationReview */

    public static final String ANNOTATION_STATUS_PROPERTY_NAME     = "annotationStatus";        /* from AnnotationReviewLink relationship */
    /* Enum Type AnnotationStatus */

    /* For SchemaAnalysisAnnotation entity */
    public static final String SCHEMA_ANALYSIS_ANNOTATION_TYPE_GUID         = "3c5aa68b-d562-4b04-b189-c7b7f0bf2ced";
    public static final String SCHEMA_ANALYSIS_ANNOTATION_TYPE_NAME         = "SchemaAnalysisAnnotation";

    public static final String SCHEMA_NAME_PROPERTY_NAME           = "schemaName";     /* from SchemaAnalysisAnnotation entity */
    public static final String SCHEMA_TYPE_PROPERTY_NAME           = "schemaType";     /* from SchemaAnalysisAnnotation entity */

    /* For DataField entity */
    public static final String DATA_FIELD_TYPE_GUID              = "3c5bbc8b-d562-4b04-b189-c7b7f0bf2cea";
    public static final String DATA_FIELD_TYPE_NAME              = "DataField";

    public static final String DATA_FIELD_NAME_PROPERTY_NAME               = "dataFieldName";        /* from DataField entity */
    public static final String DATA_FIELD_TYPE_PROPERTY_NAME               = "dataFieldType";        /* from DataField entity */
    public static final String DATA_FIELD_DESCRIPTION_PROPERTY_NAME        = "dataFieldDescription"; /* from DataField entity */
    public static final String DATA_FIELD_NAMESPACE_PROPERTY_NAME          = "dataFieldNamespace"; /* from DataField entity */
    public static final String DATA_FIELD_ALIASES_PROPERTY_NAME            = "dataFieldAliases";     /* from DataField entity */
    public static final String DATA_FIELD_SORT_ORDER_PROPERTY_NAME         = "dataFieldName";        /* from DataField entity */

    /* For DiscoveredDataField relationship */
    public static final String DISCOVERED_DATA_FIELD_TYPE_GUID    = "60f2d263-e24d-4f20-8c0d-b5e22222cd54";
    public static final String DISCOVERED_DATA_FIELD_TYPE_NAME    = "DiscoveredDataField";
    /* End1 = SchemaAnalysisAnnotation; End 2 = DataField */

    /* For DiscoveredDataField relationship */
    public static final String DISCOVERED_NESTED_DATA_FIELD_TYPE_GUID    = "60f2d263-e24d-4f20-8c0d-b5e12356cd54";
    public static final String DISCOVERED_NESTED_DATA_FIELD_TYPE_NAME    = "DiscoveredNestedDataField";
    /* End1 = (parent)DataField; End 2 = DataField */

    public static final String DISCOVERED_LINKED_DATA_FIELD_TYPE_GUID    = "cca4b116-4490-44c4-84e1-535231ae46a1";
    public static final String DISCOVERED_LINKED_DATA_FIELD_TYPE_NAME    = "DiscoveredLinkedDataField";
    /* End1 = (parent)DataField; End 2 = DataField */

    public static final String DATA_FIELD_POSITION_PROPERTY_NAME     = "dataFieldPosition";    /* from DiscoveredDataField and
                                                                                                    DiscoveredNestedDataField relationship */
    public static final String RELATIONSHIP_TYPE_NAME_PROPERTY_NAME  = "relationshipTypeName"; /* from DiscoveredLinkedDataField relationship and
                                                                                                    RelationshipAdviceAnnotation entity */
    public static final String RELATIONSHIP_END_PROPERTY_NAME        = "relationshipEnd";      /* from DiscoveredLinkedDataField relationship */

    /* For DataFieldAnnotation entity */
    public static final String DATA_FIELD_ANNOTATION_TYPE_GUID = "72ed6de6-79d9-4e7d-aefc-b969382fc4b0";
    public static final String DATA_FIELD_ANNOTATION_TYPE_NAME = "DataFieldAnnotation";
    /* Annotation */

    /* For DataFieldAnalysis relationship */
    public static final String DATA_FIELD_ANALYSIS_TYPE_GUID    = "833e849d-eda2-40bb-9e6b-c3ca0b56d581";
    public static final String DATA_FIELD_ANALYSIS_TYPE_NAME    = "DataFieldAnalysis";
    /* End1 = DataFieldAnnotation; End 2 = DataField */

    /* For ClassificationAnnotation entity */
    public static final String CLASSIFICATION_ANNOTATION_TYPE_GUID = "23e8287f-5c7e-4e03-8bd3-471fc7fc029c";
    public static final String CLASSIFICATION_ANNOTATION_TYPE_NAME = "ClassificationAnnotation";
    /* DataFieldAnnotation */

    public static final String CANDIDATE_CLASSIFICATIONS_PROPERTY_NAME  = "candidateClassifications";   /* from ClassificationAnnotation entity */

    /* For DataProfileAnnotation entity */
    public static final String DATA_PROFILE_ANNOTATION_TYPE_GUID = "bff1f694-afd0-4829-ab11-50a9fbaf2f5f";
    public static final String DATA_PROFILE_ANNOTATION_TYPE_NAME = "DataProfileAnnotation";
    /* DataFieldAnnotation */

    public static final String INFERRED_DATA_TYPE_PROPERTY_NAME  = "inferredDataType";    /* from DataProfileAnnotation entity */
    public static final String INFERRED_FORMAT_PROPERTY_NAME     = "inferredFormat";      /* from DataProfileAnnotation entity */
    public static final String INFERRED_LENGTH_PROPERTY_NAME     = "inferredLength";      /* from DataProfileAnnotation entity */
    public static final String INFERRED_PRECISION_PROPERTY_NAME  = "inferredPrecision";   /* from DataProfileAnnotation entity */
    public static final String INFERRED_SCALE_PROPERTY_NAME      = "inferredScale";       /* from DataProfileAnnotation entity */
    public static final String PROFILE_PROPERTIES_PROPERTY_NAME  = "profileProperties";   /* from DataProfileAnnotation entity */
    public static final String PROFILE_FLAGS_PROPERTY_NAME       = "profileFlags";        /* from DataProfileAnnotation entity */
    public static final String PROFILE_COUNTS_PROPERTY_NAME      = "profileCounts";       /* from DataProfileAnnotation entity */
    public static final String VALUE_LIST_PROPERTY_NAME          = "valueList";           /* from DataProfileAnnotation entity */
    public static final String VALUE_COUNT_PROPERTY_NAME         = "valueCount";          /* from DataProfileAnnotation entity */
    public static final String VALUE_RANGE_FROM_PROPERTY_NAME    = "valueRangeTo";        /* from DataProfileAnnotation entity */
    public static final String VALUE_RANGE_TO_PROPERTY_NAME      = "valueRangeTo";        /* from DataProfileAnnotation entity */
    public static final String AVERAGE_VALUE_PROPERTY_NAME       = "averageValue";        /* from DataProfileAnnotation entity */

    /* For DataProfileLogAnnotation entity */
    public static final String DATA_PROFILE_LOG_ANNOTATION_TYPE_GUID = "368e6fb3-7323-4f81-a723-5182491594bd";
    public static final String DATA_PROFILE_LOG_ANNOTATION_TYPE_NAME = "DataProfileLogAnnotation";
    /* DataFieldAnnotation */

    /* For DiscoveredDataField relationship */
    public static final String DATA_PROFILE_LOG_FILE_TYPE_GUID    = "75026fac-f9e5-4da8-9ad1-e9c68d47f577";
    public static final String DATA_PROFILE_LOG_FILE_TYPE_NAME    = "DataProfileLogFile";
    /* End1 = DataProfileLogAnnotation; End 2 = LogFile */

    /* For DataClassAnnotation entity */
    public static final String DATA_CLASS_ANNOTATION_TYPE_GUID = "0c8a3673-04ef-406f-899d-e88de67f6176";
    public static final String DATA_CLASS_ANNOTATION_TYPE_NAME = "DataClassAnnotation";
    /* DataFieldAnnotation */

    public static final String CANDIDATE_DATA_CLASS_GUIDS_PROPERTY_NAME  = "candidateDataClassGUIDs";   /* from DataClassAnnotation entity */
    public static final String MATCHING_VALUES_PROPERTY_NAME             = "matchingValues";            /* from DataClassAnnotation entity */
    public static final String NON_MATCHING_VALUES_PROPERTY_NAME         = "nonMatchingValues";         /* from DataClassAnnotation entity */

    /* For SemanticAnnotation entity */
    public static final String SEMANTIC_ANNOTATION_TYPE_GUID = "0b494819-28be-4604-b238-3af20963eea6";
    public static final String SEMANTIC_ANNOTATION_TYPE_NAME = "SemanticAnnotation";
    /* Annotation */

    public static final String INFORMAL_TERM_PROPERTY_NAME                     = "informalTerm";   /* from SemanticAnnotation entity */
    public static final String CANDIDATE_GLOSSARY_TERM_GUIDS_PROPERTY_NAME     = "candidateGlossaryTermGUIDs";  /* from SemanticAnnotation entity */
    public static final String INFORMAL_TOPIC_PROPERTY_NAME                    = "informalTopic";   /* from SemanticAnnotation entity */
    public static final String CANDIDATE_GLOSSARY_CATEGORY_GUIDS_PROPERTY_NAME = "candidateGlossaryCategoryGUIDs";  /* from SemanticAnnotation entity */

    /* For QualityAnnotation entity */
    public static final String QUALITY_ANNOTATION_TYPE_GUID = "72e6473d-4ce0-4609-80a4-e6e949a7f520";
    public static final String QUALITY_ANNOTATION_TYPE_NAME = "QualityAnnotation";
    /* DataFieldAnnotation */

    public static final String QUALITY_DIMENSION_PROPERTY_NAME        = "qualityDimension";   /* from QualityAnnotation entity */
    public static final String QUALITY_SCORE_PROPERTY_NAME            = "qualityScore";  /* from QualityAnnotation entity */

    /* For RelationshipAdviceAnnotation entity */
    public static final String RELATIONSHIP_ADVICE_ANNOTATION_TYPE_GUID = "740f07dc-4ee8-4c2a-baba-efb55c73eb68";
    public static final String RELATIONSHIP_ADVICE_ANNOTATION_TYPE_NAME = "RelationshipAdviceAnnotation";
    /* DataFieldAnnotation */

    public static final String RELATED_ENTITY_GUID_PROPERTY_NAME        = "relatedEntityGUID";      /* from RelationshipAdviceAnnotation entity */
    public static final String RELATIONSHIP_PROPERTIES_PROPERTY_NAME    = "relationshipProperties"; /* from RelationshipAdviceAnnotation entity */



    /* For SuspectDuplicateAnnotation entity */
    public static final String SUSPECT_DUPLICATE_ANNOTATION_TYPE_GUID = "f703a621-4078-4c07-ab22-e7c334b94235";
    public static final String SUSPECT_DUPLICATE_ANNOTATION_TYPE_NAME = "SuspectDuplicateAnnotation";
    /* plus Annotation */

    public static final String DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME        = "duplicateAnchorGUIDs";   /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_PROPERTY_NAMES_PROPERTY_NAME       = "matchingPropertyNames";  /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME = "matchingClassificationNames"; /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME     = "matchingAttachmentGUIDS";  /* from SuspectDuplicateAnnotation entity */
    public static final String MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME   = "matchingRelationshipGUIDs"; /* from SuspectDuplicateAnnotation entity */

    /* For DivergentDuplicateAnnotation entity */
    public static final String DIVERGENT_DUPLICATE_ANNOTATION_TYPE_GUID                = "251e443c-dee0-47fa-8a73-1a9d511915a0";
    public static final String DIVERGENT_DUPLICATE_ANNOTATION_TYPE_NAME                = "DivergentDuplicateAnnotation";
    /* plus Annotation */

    /* For DivergentValueAnnotation entity */
    public static final String DIVERGENT_VALUE_ANNOTATION_TYPE_GUID                    = "b86cdded-1078-4e42-b6ba-a718c2c67f62";
    public static final String DIVERGENT_VALUE_ANNOTATION_TYPE_NAME                    = "DivergentValueAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentClassificationAnnotation entity */
    public static final String DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_GUID           = "8efd6257-a53e-451d-abfc-8e4899c38b1f";
    public static final String DIVERGENT_CLASSIFICATION_ANNOTATION_TYPE_NAME           = "DivergentClassificationAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentRelationshipAnnotation entity */
    public static final String DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_GUID             = "b6c6938a-fdc9-438f-893c-0b5b1d4a5bb3";
    public static final String DIVERGENT_RELATIONSHIP_ANNOTATION_TYPE_NAME             = "DivergentRelationshipAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentAttachmentAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_GUID               = "f3ed48bc-b0ea-4e1f-a8ab-75f9f3cf87a6";
    public static final String DIVERGENT_ATTACHMENT_ANNOTATION_TYPE_NAME               = "DivergentAttachmentAnnotation";
    /* plus DivergentDuplicateAnnotation */

    /* For DivergentAttachmentValueAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_VALUE_ANNOTATION_TYPE_GUID         = "e22a1ffe-bd90-4faf-b6a1-13fafb7948a2";
    public static final String DIVERGENT_ATTACHMENT_VALUE_ANNOTATION_TYPE_NAME         = "DivergentAttachmentValueAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* For DivergentAttachmentClassificationAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_GUID         = "a2a5cb74-f8e0-470f-be71-26b7e32166a6";
    public static final String DIVERGENT_ATTACHMENT_CLASS_ANNOTATION_TYPE_NAME         = "DivergentAttachmentClassificationAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* For DivergentAttachmentRelationshipAnnotation entity */
    public static final String DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_GUID           = "5613677a-865f-474e-8044-4167fa5a31b9";
    public static final String DIVERGENT_ATTACHMENT_REL_ANNOTATION_TYPE_NAME           = "DivergentAttachmentRelationshipAnnotation";
    /* plus DivergentAttachmentAnnotation */

    /* for divergent annotations */
    public static final String DUPLICATE_ANCHOR_GUID_PROPERTY_NAME                      = "duplicateAnchorGUID";
    // public static final String ATTACHMENT_GUID_PROPERTY_NAME                            = "attachmentGUID";
    public static final String DUPLICATE_ATTACHMENT_GUID_PROPERTY_NAME                  = "duplicateAttachmentGUID";
    public static final String DIVERGENT_PROPERTY_NAMES_PROPERTY_NAME                   = "divergentPropertyNames";
    public static final String DIVERGENT_CLASSIFICATION_NAME_PROPERTY_NAME              = "divergentClassificationName";
    public static final String DIVERGENT_CLASSIFICATION_PROPERTY_NAMES_PROPERTY_NAME    = "divergentClassificationPropertyNames";
    public static final String DIVERGENT_RELATIONSHIP_GUID_PROPERTY_NAME                = "divergentRelationshipGUID";
    public static final String DIVERGENT_RELATIONSHIP_PROPERTY_NAMES_PROPERTY_NAME      = "divergentRelationshipPropertyNames";

    /* For DataSourceMeasurementAnnotation entity */
    public static final String DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_GUID         = "c85bea73-d7af-46d7-8a7e-cb745910b1d";
    public static final String DATA_SOURCE_MEASUREMENT_ANNOTATION_TYPE_NAME         = "DataSourceMeasurementAnnotation";
    /* plus Annotation */

    public static final String DATA_SOURCE_PROPERTIES_PROPERTY_NAME    = "dataSourceProperties";  /* from DataSourceMeasurementAnnotation entity */

    /* For DataSourcePhysicalStatusAnnotation entity */
    public static final String DS_PHYSICAL_STATUS_ANNOTATION_TYPE_GUID         = "e9ba276e-6d9f-4999-a5a9-9ddaaabfae23";
    public static final String DS_PHYSICAL_STATUS_ANNOTATION_TYPE_NAME         = "DataSourcePhysicalStatusAnnotation";
    /* plus DataSourceMeasurementAnnotation */

    public static final String SOURCE_CREATE_TIME_PROPERTY_NAME          = "sourceCreateTime";   /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String SOURCE_CREATE_TIME_PROPERTY_NAME_DEP      = "createTime";         /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String SOURCE_UPDATE_TIME_PROPERTY_NAME          = "sourceUpdateTime";   /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String SOURCE_UPDATE_TIME_PROPERTY_NAME_DEP      = "updateTime";        /* from DataSourcePhysicalStatusAnnotation entity */

    public static final String DS_PHYSICAL_SIZE_PROPERTY_NAME           = "size";            /* from DataSourcePhysicalStatusAnnotation entity */
    public static final String DS_PHYSICAL_ENCODING_PROPERTY_NAME       = "encoding";        /* from DataSourcePhysicalStatusAnnotation entity */

    /* For Request For Action Annotation entity */
    public static final String REQUEST_FOR_ACTION_ANNOTATION_TYPE_GUID         = "f45765a9-f3ae-4686-983f-602c348e020d";
    public static final String REQUEST_FOR_ACTION_ANNOTATION_TYPE_NAME         = "RequestForActionAnnotation";
    /* plus DataFieldAnnotation */

    public static final String DISCOVERY_ACTIVITY_PROPERTY_NAME    = "discoveryActivity";      /* from RequestForActionAnnotation entity */
    public static final String ACTION_REQUESTED_PROPERTY_NAME      = "actionRequested";        /* from RequestForActionAnnotation entity */
    public static final String ACTION_PROPERTIES_PROPERTY_NAME     = "actionProperties";       /* from RequestForActionAnnotation entity */

    /* For Discovery Activity relationship */
    public static final String DISCOVERY_ACTIVITY_TYPE_GUID    = "6cea5b53-558c-48f1-8191-11d48db29fb4";
    public static final String DISCOVERY_ACTIVITY_TYPE_NAME    = "DiscoveryActivity";
    /* End1 = RequestForActionAnnotation; End 2 = RequestForAction */

    /* ============================================================================================================================*/
    /* Area 7 - Lineage                                                                                                            */
    /* ============================================================================================================================*/

    public static final String DIGITAL_SERVICE_TYPE_GUID = "f671e1fc-b204-4ee6-a4e2-da1633ecf50e";
    public static final String DIGITAL_SERVICE_TYPE_NAME = "DigitalService";

    public static final String DIGITAL_PRODUCT_CLASSIFICATION_TYPE_GUID = "4aaaa7ca-6b4b-4c4b-997f-d5dfd42917b0";
    public static final String DIGITAL_PRODUCT_CLASSIFICATION_TYPE_NAME = "DigitalProduct";
    public static final String PRODUCT_NAME_PROPERTY_NAME = "productName";
    public static final String PRODUCT_TYPE_PROPERTY_NAME = "productType";
    public static final String INTRODUCTION_DATE_PROPERTY_NAME = "introductionDate";
    public static final String MATURITY_PROPERTY_NAME = "maturity";
    public static final String SERVICE_LIFE_PROPERTY_NAME = "serviceLife";
    public static final String CURRENT_VERSION_PROPERTY_NAME = "currentVersion";
    public static final String NEXT_VERSION_PROPERTY_NAME = "nextVersion";
    public static final String WITHDRAW_DATE_PROPERTY_NAME = "withdrawDate";

    public static final String DIGITAL_SERVICE_DEPENDENCY_RELATIONSHIP_TYPE_GUID = "e8303911-ba1c-4640-974e-c4d57ee1b310";
    public static final String DIGITAL_SERVICE_DEPENDENCY_RELATIONSHIP_TYPE_NAME = "DigitalServiceDependency";

    public static final String DIGITAL_SERVICE_PRODUCT_RELATIONSHIP_TYPE_GUID = "51465a59-c785-406d-929c-def34596e9af";
    public static final String DIGITAL_SERVICE_PRODUCT_RELATIONSHIP_TYPE_NAME = "DigitalServiceProduct";

    public static final String IMPLEMENTED_BY_RELATIONSHIP_TYPE_GUID = "28f63c94-aaef-4c84-98f7-d77aa605272e";
    public static final String IMPLEMENTED_BY_RELATIONSHIP_TYPE_NAME = "ImplementedBy";
    /* End1 = Referenceable; End 2 = Referenceable */

    public static final String DESIGN_STEP_PROPERTY_NAME = "designStep";
    public static final String ROLE_PROPERTY_NAME = "role";
    public static final String TRANSFORMATION_PROPERTY_NAME = "transformation";

    public static final String SOLUTION_PORT_SCHEMA_RELATIONSHIP_TYPE_GUID = "bf02c703-57a2-4ab7-b6db-f49b57b05985";
    public static final String SOLUTION_PORT_SCHEMA_RELATIONSHIP_TYPE_NAME = "SolutionPortSchema";
    /* End1 = SolutionPort; End 2 = SchemaType */

    public static final String DATA_FLOW_TYPE_GUID                  = "d2490c0c-06cc-458a-add2-33cf2f5dd724";
    public static final String DATA_FLOW_TYPE_NAME                  = "DataFlow";
    /* End1 = Referenceable - supplier; End 2 = Referenceable - consumer */

    public static final String CONTROL_FLOW_TYPE_GUID               = "35450726-1c32-4d41-b928-22db6d1ae2f4";
    public static final String CONTROL_FLOW_TYPE_NAME               = "ControlFlow";
    /* End1 = Referenceable - currentStep; End 2 = Referenceable - nextStep */

    public static final String PROCESS_CALL_TYPE_GUID               = "af904501-6347-4f52-8378-da50e8d74828";
    public static final String PROCESS_CALL_TYPE_NAME               = "ProcessCall";
    /* End1 = Referenceable - calls; End 2 = Referenceable - calledBy */

    public static final String BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID = "085febdd-f129-4f4b-99aa-01f3e6294e9f";
    public static final String BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME = "BusinessSignificance";
    /* Linked to Referenceable */

    public static final String BUSINESS_CAPABILITY_GUID_PROPERTY_NAME = "businessCapabilityGUID";  /* from BusinessSignificant entity */


    public static final String LINEAGE_MAPPING_TYPE_GUID            = "a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff";
    public static final String LINEAGE_MAPPING_TYPE_NAME            = "LineageMapping";
    /* End1 = Referenceable - sourceElement; End 2 = Referenceable - targetElement */

    public static final String INCOMPLETE_CLASSIFICATION_TYPE_GUID = "078432fb-a889-4a51-8ebe-9797becea9f1";
    public static final String INCOMPLETE_CLASSIFICATION_TYPE_NAME = "Incomplete";

    public static final String PROCESSING_STATE_CLASSIFICATION_TYPE_GUID = "261fb0aa-b884-4ee8-87ea-a60510e9751d";
    public static final String PROCESSING_STATE_CLASSIFICATION_TYPE_NAME = "ProcessingState";
}
