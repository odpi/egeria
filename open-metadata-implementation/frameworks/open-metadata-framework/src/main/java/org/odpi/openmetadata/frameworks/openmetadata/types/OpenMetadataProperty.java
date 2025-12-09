/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;


/**
 * Provides the definition for all properties defined in the open metadata types.
 */
public enum OpenMetadataProperty
{
    /* ======================================================
     * These values are from the repository element header.
     * They are used for searching properties and ordering results.
     * Using them when adding attributes to elements will result in an error.
     */

    /**
     * Unique identifier of an open metadata entity or relationship.
     */
    GUID("guid", DataType.STRING, DataType.STRING.getName(), "Unique identifier of an open metadata entity or relationship.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55"),

    /**
     * The unique identifier for the metadata collection that is the home for a metadata element.
     */
    METADATA_COLLECTION_ID("metadataCollectionId", DataType.STRING, DataType.STRING.getName(), "The unique identifier for the metadata collection that is the home for a metadata element.", "151b9d80-8417-41c4-8f04-3ab90a387196", "01d7e832-ef18-4451-8e4c-4ba972292a8e"),

    /**
     * The unique identifier for the metadata collection that is the home for a metadata element.
     */
    METADATA_COLLECTION_NAME("metadataCollectionName", DataType.STRING, DataType.STRING.getName(), "The unique name for the metadata collection that is the home for a metadata element.", "MyDataManagerForDatabase1", "25b43665-87e9-4637-9e6d-d0658ba26261"),

    /**
     * Category of metadata collection.
     */
    INSTANCE_PROVENANCE_TYPE("instanceProvenanceType", DataType.STRING, DataType.STRING.getName(), "Category of metadata collection.", "Local Cohort", "f12562a3-52b2-42d2-9126-011207d4af55"),

    /**
     * UserId that created this instance.
     */
    CREATED_BY("createdBy", DataType.STRING, DataType.STRING.getName(), "UserId that created this instance.", "peterprofile", "5c9c9c94-b738-4203-b592-527801877f07"),

    /**
     * Time when this instance was created.
     */
    CREATE_TIME("createTime", DataType.DATE, DataType.DATE.getName(), "Time when this instance was created.", null, "5376d9a4-ad82-43cc-8d37-9247643418d8"),

    /**
     * UserId that updated this instance.
     */
    UPDATED_BY("updatedBy", DataType.STRING, DataType.STRING.getName(), "UserId that updated this instance.", "erinoverview", "6fcbf02d-3d3b-4739-9209-51a02779393b"),

    /**
     * Time when this instance was last updated.
     */
    UPDATE_TIME("updateTime", DataType.DATE, DataType.DATE.getName(), "Time when this instance was last updated.", null, "3cefe85f-4904-411f-9afd-546c0a9ba016"),

    /**
     * The date/time that this instance should start to be used (null means it can be used from creationTime).
     */
    EFFECTIVE_FROM_TIME("effectiveFromTime", DataType.DATE, DataType.DATE.getName(), "The date/time that this instance should start to be used (null means it can be used from creationTime).", null, "dfffede5-d593-4f53-a7a1-bf7ddf095ee3"),

    /**
     * The date/time that this instance should no longer be used.
     */
    EFFECTIVE_TO_TIME("effectiveToTime", DataType.DATE, DataType.DATE.getName(), "The date/time that this instance should no longer be used.", null, "c1de164e-b9bf-4d13-bac5-573a5078ef10"),

    /**
     * Version is a monotonically increasing indicator of the order that updates have been made to the instance. It is used by the open metadata repositories to ensure updates to reference copies of the instance are applied in the right sequence. The home open metadata repository (where the create an all subsequent updates happen) maintains the version number.
     */
    VERSION("version", DataType.LONG, DataType.LONG.getName(), "Version is a monotonically increasing indicator of the order that updates have been made to the instance. It is used by the open metadata repositories to ensure updates to reference copies of the instance are applied in the right sequence. The home open metadata repository (where the create an all subsequent updates happen) maintains the version number.", "1", "62dfdb89-5e99-449c-aafe-202d7e527414"),

    /**
     * Status of this instance. Values from the Instance Status enum. (use only for ordering results),
     */
    CURRENT_STATUS("currentStatus", DataType.STRING, DataType.STRING.getName(), "Status of this instance. Values from the Instance Status enum.", "Active", "de25c3fb-5061-4f3e-bd7d-29c1cd2c112f"),

    /**
     * Name of an open metadata type. (Use only for ordering results).
     */
    OPEN_METADATA_TYPE_NAME("typeName", DataType.STRING, DataType.STRING.getName(), "Name of an open metadata type.", "Asset", "a7eabe8c-d0c6-4785-86b3-f2bc310ec712"),


    /* ======================================================
     * These values are attributes defined in the type system.
     */

    /**
     * Unique identifier of an open metadata entity, classification or relationship.
     */
    INSTANCE_METADATA_TYPE_NAME("instanceMetadataTypeName", DataType.STRING, DataType.STRING.getName(), "Name of the metadata data type extracted from an instance.", "JPEG", "7c5a7e83-2709-4789-b014-d23082a659bd"),

    /**
     * Timestamp when the archive occurred or was detected.
     */
    ARCHIVE_DATE("archiveDate", DataType.DATE, DataType.DATE.getName(), "Timestamp when the archive occurred or was detected.", null, "5235c1b1-fa03-4d17-80e2-7826242dfc75"),

    /**
     * Name of user that performed the archive - or detected the archive.
     */
    ARCHIVE_USER("archiveUser", DataType.STRING, DataType.STRING.getName(), "Name of user that performed the archive - or detected the archive.", null, "fe81b808-3d78-4e84-a1de-b0b273a89bec"),

    /**
     * Name of process that performed the archive - or detected the archive.
     */
    ARCHIVE_PROCESS("archiveProcess", DataType.STRING, DataType.STRING.getName(), "Name of process that performed the archive - or detected the archive.", null, "0aef1b3a-50dc-4a6c-981f-93bf8815e7f4"),

    /**
     * Name of service that created this classification.
     */
    ARCHIVE_SERVICE("archiveService", DataType.STRING, DataType.STRING.getName(), "Name of service that created this classification.", null, "c2170958-998e-4d8c-8e33-e91ef4872c8a"),

    /**
     * Name of method that created this classification.
     */
    ARCHIVE_METHOD("archiveMethod", DataType.STRING, DataType.STRING.getName(), "Name of method that created this classification.", null, "45d558ba-00b4-4a67-9226-590b100da7e9"),

    /**
     * Properties to locate the real-world counterpart in the archive.
     */
    ARCHIVE_PROPERTIES("archiveProperties", DataType.STRING, DataType.STRING.getName(), "Properties to locate the real-world counterpart in the archive.", null, "d149d2d8-f181-4c20-8d68-5460f54bece5"),

    /**
     * The unique identifier of the referenceable that this element is directly or indirectly anchored to.
     */
    ANCHOR_GUID("anchorGUID", DataType.STRING, DataType.STRING.getName(), "The unique identifier of the referenceable that this element is directly or indirectly anchored to.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "e7de4efc-6fa4-4942-97eb-e9b5be701875"),

    /**
     * Unique name of the type of the anchor.
     */
    ANCHOR_TYPE_NAME("anchorTypeName", DataType.STRING, DataType.STRING.getName(), "Unique name of the type of the anchor.", "Asset", "605c9dd5-fa79-4457-b299-9169c5567f97"),

    /**
     * Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.
     */
    ANCHOR_DOMAIN_NAME("anchorDomainName", DataType.STRING, DataType.STRING.getName(), "Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.", "Asset", "00ba532f-792f-4b78-8940-b5a9fd72f854"),

    /**
     * Unique identifier of the scope of the anchor.  This is an Open Metadata GUID of an element that represents a scope/ownership of an anchor element.  It is used to restrict searches.
     */
    ANCHOR_SCOPE_GUID("anchorScopeGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the scope of the anchor.  This is an Open Metadata GUID of an element that represents a scope/ownership of an anchor element.  It is used to restrict searches.", "0ba61188-a5db-4e98-af1a-31505660363e", "65afae9b-54d5-440f-b955-6ebe9c0eba21"),

    /**
     * Unique name for the element.
     */
    QUALIFIED_NAME("qualifiedName", DataType.STRING, DataType.STRING.getName(), "Unique name for the element.", "SoftwareServer:MyAsset:MyAssetName", "e31e5b9b-0f96-42a9-8e67-0e3fc66ad305"),

    /**
     * Additional properties for the element.
     */
    ADDITIONAL_PROPERTIES("additionalProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional properties for the element.", null, "534b5665-73d4-4bdc-b83b-1a8fed19dba3"),

    /**
     * Full name that the element is known as in the owning deployed technology.
     */
    RESOURCE_NAME("resourceName", DataType.STRING, DataType.STRING.getName(), "Full name that the element is known as in the owning deployed technology.  This name is typically unique within the scope of the owing technology", "MyAssetName", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),

    /**
     * Display name of the element used for summary tables and titles
     */
    DISPLAY_NAME("displayName", DataType.STRING, DataType.STRING.getName(), "Display name of the element used for summary tables and titles.", "MyGlossaryTerm", "a42ceece-6cd8-4c71-9920-e3c96df3c8bb"),

    /**
     * Description of the element or associated resource in free-text.
     */
    DESCRIPTION("description", DataType.STRING, DataType.STRING.getName(), "Description of the element or associated resource in free-text.", null, "ee09b6f9-e15b-40fb-9799-ef3b98c1de2c"),

    /**
     * Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.
     */
    VERSION_IDENTIFIER("versionIdentifier", DataType.STRING, DataType.STRING.getName(), "Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.", "V1.0", "6e765a3f-04c2-4eca-bd2d-519cae777c03"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    DEPLOYED_IMPLEMENTATION_TYPE("deployedImplementationType", DataType.STRING, DataType.STRING.getName(), "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "PostgreSQL Database Server", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE("supportedDeployedImplementationType", DataType.STRING, DataType.STRING.getName(), "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "PostgreSQL Database Server", "32567628-f7e3-4a6d-92b9-8ed734bd3dc1"),

    /**
     * Identifier of the person or process who is accountable for the proper management of the element or related resource.
     */
    OWNER("owner", DataType.STRING, DataType.STRING.getName(), "Identifier of the person or process who is accountable for the proper management of the element or related resource.", null, "8b3e49d6-987c-4f2f-b624-ba91e5d28b33"),

    /**
     * Type of element that describes the owner.
     */
    OWNER_TYPE_NAME("ownerTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element that describes the owner.", "PersonRole", "c0acfbc6-4250-437b-9aa3-ce3d5a3bd490"),

    /**
     * Name of the property from the element used to identify the owner.
     */
    OWNER_PROPERTY_NAME("ownerPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the owner.", "qualifiedName", "9d1fdeb4-132a-4ec7-8312-dc9362e4f2f7"),

    /**
     * The list of governance zones that this asset belongs to.
     */
    ZONE_MEMBERSHIP("zoneMembership", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "The list of governance zones that this asset belongs to.", null, "2af69520-6991-4097-aa94-543127b73066"),

    /**
     * Definition of the types of assets that belong in this zone.
     */
    CRITERIA("criteria", DataType.STRING, DataType.STRING.getName(), "Definition of the types of assets that belong in this zone.", "Incoming data that has not been checked.", "3e49feee-624f-41ab-beda-2f727edda93e"),

    /**
     * Formula that describes the behaviour of the element.  May include placeholders for queryIds
     */
    FORMULA("formula", DataType.STRING, DataType.STRING.getName(), "Formula that describes the behaviour of the element.  May include placeholders for queryIds.", null, "0b35fe46-1c16-4af7-b7b1-ea6951718dc7"),

    /**
     * Format of the expression provided in the formula attribute.
     */
    FORMULA_TYPE("formulaType", DataType.STRING, DataType.STRING.getName(), "Format of the expression provided in the formula attribute.", "SQL", "dd9b24cb-0359-4915-8b6b-9eae251adc1c"),

    /**
     * Time that the ending action should start.  This is the request time plus any requested wait time.
     */
    REQUESTED_START_TIME("requestedStartTime", DataType.DATE, DataType.DATE.getName(), "Time that the ending action should start.  This is the request time plus any requested wait time.", null, "7bf281bf-e91c-41f5-883c-af0025d0ad66"),

    /**
     * Time that an action was started.
     */
    START_TIME("startTime", DataType.DATE, DataType.DATE.getName(), "Time that a transient process started.", null, "f2c4180f-a803-42d6-9eb1-2bd29bd20d88"),

    /**
     * When the requested activity needs to be completed.
     */
    DUE_TIME("dueTime", DataType.DATE, DataType.DATE.getName(), "When the requested activity needs to be completed.", null, "200f604a-8936-4ad7-aa13-01abbecc82a6"),

    /**
     * The Date/time that this activity was reviewed.
     */
    LAST_REVIEW_TIME("lastReviewTime", DataType.DATE, DataType.DATE.getName(), "The Date/time that this activity was reviewed.", null, "adac8415-11c8-43e9-8a43-92a799893555"),

    /**
     * The Date/time that this activity was last paused.
     */
    LAST_PAUSE_TIME("lastPauseTime", DataType.DATE, DataType.DATE.getName(), "The Date/time that this activity was last paused.", null, "4133d47a-7644-4549-8960-6388e200c766"),

    /**
     * The Date/time that this activity was last resumed.
     */
    LAST_RESUME_TIME("lastResumeTime", DataType.DATE, DataType.DATE.getName(), "The Date/time that this activity was last resumed.", null, "ab3638ff-04ef-4e34-96ec-5a1f45e0bf72"),

    /**
     * When the requested activity was completed.
     */
    COMPLETION_TIME("completionTime", DataType.DATE, DataType.DATE.getName(), "When the requested activity was completed.", null, "9359f36f-ed11-4c33-80c3-e02941fee6b5"),


    /**
     * A symbol, such as a pictogram, logogram, ideogram, or smiley embedded in text and used in electronic messages and web pages.
     */
    EMOJI("emoji", DataType.STRING, DataType.STRING.getName(), "A symbol, such as a pictogram, logogram, ideogram, or smiley embedded in text and used in electronic messages and web pages. The primary function of modern emoji is to fill in emotional cues otherwise missing from typed conversation as well as to replace words as part of a logographic system. Emoji exist in various genres, including facial expressions, expressions, activity, food and drinks, celebrations, flags, objects, symbols, places, types of weather, animals, and nature.", ":)", "9e40ae6d-99f4-4336-a043-e1fac95e7ed6"),

    /**
     * Rating level provided.
     */
    STARS("stars", DataType.STRING, StarRating.getOpenTypeName(), "Rating level provided.", "***", "896fd016-fad1-43dc-87a9-3ee577d8d898"),

    /**
     * Additional comments associated with the rating.
     */
    REVIEW("review", DataType.STRING, DataType.STRING.getName(), "Additional comments associated with the rating.", null, "ae1ef693-6264-44ee-8014-23a283ad3b0e"),

    /**
     * Role of contributor.
     */
    ROLE_TYPE("roleType", DataType.STRING, CrowdSourcingRole.getOpenTypeName(), "Role of contributor.", "Contributor", "05fdcacb-b7ec-475c-96eb-600fbbc1fb88"),

    /**
     * The courtesy title for the person.
     */
    COURTESY_TITLE("courtesyTitle", DataType.STRING, DataType.STRING.getName(), "The honorific title for the person.", "Dr", "c669dc73-3ae3-4350-95b7-4508a85bfc94"),
    
    /**
     * Description of the technique used to create the sample.
     */
    SAMPLING_METHOD("samplingMethod", DataType.STRING, DataType.STRING.getName(), "Description of the technique used to create the sample.", null, "1cb00437-adde-4b71-a655-f736f2c8989d"),

    /**
     * The name of the associated classification.
     */
    CLASSIFICATION_NAME("classificationName", DataType.STRING, DataType.STRING.getName(), "The name of the associated classification.", "Ownership", "ef2169c4-c8f3-48b0-9051-cfb2bbb1e5f2"),

    /**
     * If an attached entity or relationship to it changed, this is its unique identifier.
     */
    ATTACHMENT_GUID("attachmentGUID", DataType.STRING, DataType.STRING.getName(), "If an attached entity or relationship to it changed, this is its unique identifier.", null, "74d62753-fd42-47c5-a804-29334f394728"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name.
     */
    ATTACHMENT_TYPE("attachmentType", DataType.STRING, DataType.STRING.getName(), "If an attached entity or relationship to changed, this is its unique type name.", null, "b7f9f49a-e227-44d8-ae26-7e9c4f552379"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name of the relationship.
     */
    RELATIONSHIP_TYPE("relationshipType", DataType.STRING, DataType.STRING.getName(), "If an attached entity or relationship to changed, this is its unique type name of the relationship.", null, "afa67874-fa00-422e-8902-69e5e3cc9e43"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER_ID("userId", DataType.STRING, DataType.STRING.getName(), "The user identifier for the person/system executing the request.", null, "c65a21dc-d1ae-4a8f-ba33-58ec401c1b42"),

    /**
     * The version number of the template element when the copy was created.
     */
    SOURCE_VERSION_NUMBER("sourceVersionNumber", DataType.LONG, DataType.LONG.getName(), "The version number of the template element when the copy was created.", null, "4729488f-2e42-4801-bc17-9dfad508c1fa"),

    /**
     * The fully qualified physical location of the data store.
     */
    PATH_NAME("pathName", DataType.STRING, DataType.STRING.getName(), "The fully qualified physical location of the data store.", null, "34d24b66-12f1-437c-afd3-1f1ab3377472"),

    /**
     * The url path segments that identify this API operation.
     */
    PATH("path", DataType.STRING, DataType.STRING.getName(), "The url path segments that identify this API operation.", "/assets", "7f8c1ed7-90ac-404d-a1ab-08934e423ac5"),

    /**
     * The url path segments that identify this API operation.
     */
    COMMAND("command", DataType.STRING, DataType.STRING.getName(), "The REST command for this API operation.", "POST", "cc4a83dd-4a11-4873-8a16-e7c9232ad4cf"),

    /**
     * What type of parameter is it.
     */
    PARAMETER_TYPE("parameterType", DataType.STRING, DataType.STRING.getName(), "What type of parameter is it.", null, "e1a0a48d-35a6-425d-bb2a-bb6880014fc5"),


    /**
     * File type descriptor (or logical file type) typically extracted from the file name.
     */
    FILE_TYPE("fileType", DataType.STRING, DataType.STRING.getName(), "File type descriptor (or logical file type) typically extracted from the file name.", null, "f180c49b-2de6-4657-bdf7-069747bc612d"),

    /**
     * The name of the file with extension.
     */
    FILE_NAME("fileName", DataType.STRING, DataType.STRING.getName(), "The name of the file with extension.", null, "f22d0164-3790-42c3-aacb-154ccee7fbb6"),

    /**
     * The file extension used at the end of the file's name.  This identifies the format of the file.
     */
    FILE_EXTENSION("fileExtension", DataType.STRING, DataType.STRING.getName(), "The file extension used at the end of the file's name.  This identifies the format of the file.", null, "62b00622-fa2c-4ee0-be60-3f15b1cb3dd6"),

    /**
     * The request type used to call the service.
     */
    REQUEST_TYPE("requestType", DataType.STRING, DataType.STRING.getName(), "The request type used to call the governance service.", null, "be22cf20-f704-459e-823f-bdd8f7ef003b"),

    /**
     * The request type used to call the service.
     */
    REQUESTER_USER_ID("requesterUserId", DataType.STRING, DataType.STRING.getName(), "User identity for the person, process or engine that requested this action.", null, "061de8f4-87a6-45b6-b91b-d375f202fe0f"),


    /**
     * Properties that configure the governance service for this type of request.
     */
    REQUEST_PARAMETERS("requestParameters", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Request parameters to pass to the governance service when called.", null, "882a5a30-3724-41ea-90ff-667cb7627bde"),

    /**
     * Lists the names of the request parameters to remove from the requestParameters supplied by the caller.
     */
    REQUEST_PARAMETER_FILTER("requestParameterFilter", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Lists the names of the request parameters to remove from the requestParameters supplied by the caller.", null, "9f7e13d1-ec51-4c95-9931-383b3ab9295b"),

    /**
     * Provides a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service.
     */
    REQUEST_PARAMETER_MAP("requestParameterMap", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Provides a translation map between the supplied names in the requestParameters and the names supported by the implementation of the governance service.", null, "64730138-399a-4f30-a0b8-1f6cc487cb53"),

    /**
     * Lists the names of the action targets to remove from the supplied action targets.
     */
    ACTION_TARGET_FILTER("actionTargetFilter", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Lists the names of the action targets to remove from the supplied action targets.", null, "91b5c627-2c3a-4598-af72-e1b52f1f03c5"),

    /**
     * Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.
     */
    ACTION_TARGET_MAP("actionTargetMap", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.", null, "b848b720-4171-4d28-9e4c-1d34f51aaf5f"),

    /**
     * Request type supported by the governance service (overrides requestType on call to governance service if specified)
     */
    SERVICE_REQUEST_TYPE("serviceRequestType", DataType.STRING, DataType.STRING.getName(), "Request type supported by the governance service (overrides requestType on call to governance service if specified).", null, "63238bb7-e935-4c38-8d0a-61917f01a31d"),

    /**
     * Unique identifier of the governance engine nominated to run the request.
     */
    EXECUTOR_ENGINE_GUID("executorEngineGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the governance engine nominated to run the request.", null, "2b658eb5-0f96-40f5-91ca-9ddd9984e36b"),

    /**
     * Unique name of the governance engine nominated to run the request.
     */
    EXECUTOR_ENGINE_NAME("executorEngineName", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the governance engine nominated to run the request.", null, "f87dacc3-2f6a-4aeb-a0fd-85bea6382c2d"),

    /**
     * The list of guards that must be received before this engine action can progress.
     */
    MANDATORY_GUARDS("mandatoryGuards", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "The list of guards that must be received before this engine action can progress.", null, "757e6f07-ecf1-496b-ac14-69647fa7bd12"),

    /**
     * Unique name of the link type that connects the edge to the vertex.
     */
    LINK_TYPE_NAME("linkTypeName", DataType.STRING, DataType.STRING.getName(), "Unique name of the link type that connects the edge to the vertex.", null, "4f275bc0-3a33-4f6a-96ee-cd7bd13ba579"),

    /**
     * If the end of a relationship is significant, set to 1 or 2 to indicate the desired end; otherwise use 0.
     */
    RELATIONSHIP_END("relationshipEnd", DataType.INT, DataType.INT.getName(), "If the end of a relationship is significant, set to 1 or 2 to indicated the desired end; otherwise use 0.", null, "8b53224f-e330-4ded-9d18-da6517094994"),

    /**
     * Display name for the relationship end.
     */
    RELATIONSHIP_END_NAME("relationshipEndName", DataType.STRING, DataType.STRING.getName(), "Display name for the relationship end.", null, "e98f95e3-316b-4200-b608-57d7836d8901"),

    /**
     * Open metadata type name for the associated schema type.
     */
    SCHEMA_TYPE_NAME("schemaTypeName", DataType.STRING, DataType.STRING.getName(), "Open metadata type name for the associated schema type.", null, "4948ea82-387c-4a82-99ad-3e94bb5253b7"),

    /**
     * The name of a primitive data type.
     */
    DATA_TYPE("dataType", DataType.STRING, DataType.STRING.getName(), "The name of a primitive data type.", "string", "50e73f9f-10a0-4b41-9cb6-bf55630f3734"),

    /**
     * The units of measure used in the data field.
     */
    UNITS("units", DataType.STRING, DataType.STRING.getName(), "The units of measure used in the data field.", "centimetres", "a62374e7-c4b9-4b5e-871d-d7bcf72faf4c"),

    /**
     * Value that is used when an instance of the data field is created.
     */
    DEFAULT_VALUE("defaultValue", DataType.STRING, DataType.STRING.getName(), "Value that is used when an instance of the data field is created.", null, "e2ac5648-054c-492f-9818-bb1c55554bd6"),

    /**
     * List of sample values for the data field.
     */
    SAMPLE_VALUES("sampleValues", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of sample values for the data field.", null, "70eaf827-ed11-41c0-a5a2-1b6c247475a5"),

    /**
     * A regular expression that characterizes the values in the data field.
     */
    DATA_PATTERNS("dataPatterns", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Regular expression that characterizes the values in the data field.", null, "bea19c2c-7a41-464d-852a-8fbfb821c208"),

    /**
     * A list of name patterns to use when generating schemas.  Use space separated capitalized works so the schema generators can easily convert to valid language keywords.
     */
    NAME_PATTERNS("namePatterns", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "A list of name patterns to use when generating schemas.  Use space separated capitalized works so the schema generators can easily convert to valid language keywords.", "[Customer Id, Customer Identifier]", "e5cf2938-d1c9-4a09-bf40-76d0431b6c1a"),

    /**
     * The value of a literal data type.
     */
    FIXED_VALUE("fixedValue", DataType.STRING, DataType.STRING.getName(), "The value of a literal data type.", null, "bd0b89c3-d865-4b62-bd86-a06b3fad08fb"),

    /**
     * Position of the element in a collection of relationships.
     */
    POSITION("position", DataType.INT, DataType.INT.getName(), "Position of the element in a collection of relationships.  Zero means no position set. A positive value identified the position starting from 1 for the first position.", null, "2fd62293-99e3-48f9-825f-e9b22d8470ae"),

    /**
     * Minimum length of the data value (zero means unlimited).
     */
    MINIMUM_LENGTH("minimumLength", DataType.INT, DataType.INT.getName(), "Minimum length of the data value (zero means unlimited).", null, "c7b61891-759f-425c-b0f8-268374e7d72a"),

    /**
     * Accepts null values or not.
     */
    IS_NULLABLE("isNullable", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Accepts null values or not.", null, "6e66ed4e-bc2e-446b-a884-d358044730e7"),

    /**
     * Number of digits after the decimal point.
     */
    PRECISION("precision", DataType.INT, DataType.INT.getName(), "Number of digits after the decimal point.", null, "45992a6f-096a-4ec2-a14f-886cb5467070"),

    /**
     * Minimum number of allowed instances.
     */
    MIN_CARDINALITY("minCardinality", DataType.INT, DataType.INT.getName(), "Minimum number of allowed instances.", null, "d3e13cd5-414f-4c82-94b8-e61dad64f7c3"),

    /**
     * Maximum number of allowed instances.
     */
    MAX_CARDINALITY("maxCardinality", DataType.INT, DataType.INT.getName(), "Maximum number of allowed instances.", null, "5caa1b1a-590f-4a2e-85ad-260b64f4bbc1"),

    /**
     * When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.
     */
    ALLOWS_DUPLICATE_VALUES("allowsDuplicateValues", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.", null, "67a94396-93ca-4a20-94a8-78538bacaa65"),

    /**
     * When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.
     */
    UNIQUE_VALUES("uniqueValues", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "When multiple occurrences are allowed, indicates whether duplicates of the same value are allowed or not.", null, "a93093de-6817-43ee-ae5a-8625e8366cac"),

    /**
     * When multiple occurrences are allowed, indicates whether the values are ordered or not.
     */
    ORDERED_VALUES("orderedValues", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "When multiple occurrences are allowed, indicates whether the values are ordered or not.", null, "3e136e52-5afe-49ae-9e18-b88368e83ab8"),

    /**
     * Is it possible to follow the link in this direction.
     */
    NAVIGABLE("navigable", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is it possible to follow the link in this direction.", null, "214c098b-ccea-4068-abe2-ab3d95368990"),

    /**
     * Usage and lifecycle for this connection between the concept bead and the link.
     */
    DECORATION("decoration", DataType.STRING, RelationshipDecoration.getOpenTypeName(), "Usage and lifecycle for this connection between the concept bead and the link.", RelationshipDecoration.AGGREGATION.getName(), "3f986ca0-c8f6-41b7-9693-53d4d228de3e"),

    /**
     * List of properties in the data class that should be used in the match processing.
     */
    MATCH_PROPERTY_NAMES("matchPropertyNames", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of properties in the data class that should be used in the match processing.", null, "d30793ea-ea4a-408c-bfbc-0327574a69c0"),

    /**
     * Name or explicit definition of rule used to identify values of this data class.
     */
    SPECIFICATION("specification", DataType.STRING, DataType.STRING.getName(), "Name or explicit definition of rule used to identify values of this data class.", null, "52ee0c75-41b8-4f7b-8369-6768058c30f5"),

    /**
     * used to configure the rule outlined in the specification
     */
    SPECIFICATION_DETAILS("specificationDetails", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional properties used to configure the rule outlined in the specification.", null, "fa18daee-00e0-43b7-9eca-509b9ff6e769"),

    /**
     * Percentage of matching data values that a data resource is expected to achieve to be assigned this data class.
     */
    MATCH_THRESHOLD("matchThreshold", DataType.INT, DataType.INT.getName(), "Percentage of matching data values that a data resource is expected to achieve to be assigned this data class.", null, "d1257da4-d04f-4ef6-8c73-40083f359044"),

    /**
     * Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     */
    CANONICAL_NAME("canonicalName", DataType.STRING, DataType.STRING.getName(), "Each word in the name should be capitalized, with spaces between the words to allow translation between different naming conventions.", null, "fce3a665-be7e-45ec-ab08-6fcbd90f8ea0"),

    /**
     * Initial value for the attribute (overriding the default value of its type.
     */
    DEFAULT_VALUE_OVERRIDE("defaultValueOverride", DataType.STRING, DataType.STRING.getName(), "Initial value for the attribute (overriding the default value of its type.", null, "762d14f8-e07c-42f1-b19b-23b2395ebd92"),

    /**
     * Native class used by the client to represent this element.
     */
    NATIVE_CLASS("nativeClass", DataType.STRING, DataType.STRING.getName(), "Native class used by the client to represent this element.", null, "df77c563-4797-40a2-8b8b-4f68a7b589df"),

    /**
     * List of alternative names.
     */
    ALIASES("aliases", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of alternative names.", null, "aa5304e0-2157-40eb-b01d-fc4d4799a8dd"),


    /**
     * Provides additional reasons, or expectations from the results.
     */
    PURPOSE("purpose", DataType.STRING, DataType.STRING.getName(), "Provides additional reasons, or expectations from the results.  This is typically expressed in business terms", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Name of the type of annotation.
     */
    ANNOTATION_TYPE("annotationType", DataType.STRING, DataType.STRING.getName(), "Name of the type of annotation.", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Short description for summary tables.
     */
    SUMMARY("summary", DataType.STRING, DataType.STRING.getName(), "Short description for summary tables.", null, "30719554-1a97-424f-ac05-f4e4e67bd278"),

    /**
     * Level of certainty in the accuracy of the results.
     */
    CONFIDENCE_LEVEL("confidenceLevel", DataType.INT, DataType.INT.getName(), "Level of certainty in the accuracy of the results.", null, "29372374-38bb-472d-9d6e-529b68aed1e0"),

    /**
     * Expression used to create the annotation.
     */
    EXPRESSION("expression", DataType.STRING, DataType.STRING.getName(), "Expression used to create the annotation.", null, "e130bc01-7c58-4799-8d8a-ae3ec659a064"),

    /**
     * Explanation of the analysis.
     */
    EXPLANATION("explanation", DataType.STRING, DataType.STRING.getName(), "Explanation of the analysis.", null, "9445c89d-250f-464c-bb9c-744f25b7b7e1"),

    /**
     * Additional request parameters passed to the service.
     */
    ANALYSIS_PARAMETERS("analysisParameters", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional request parameters passed to the service.", null, "b3eb6d7f-9c52-44f0-99c3-359f52218c7e"),

    /**
     * The step in the analysis that produced the annotation.
     */
    ANALYSIS_STEP("analysisStep", DataType.STRING, DataType.STRING.getName(), "The step in the analysis that produced the annotation.", null, "0b8d13b5-39eb-46d1-9ab0-1cc192697ff7"),

    /**
     * Additional properties used in the request.
     */
    JSON_PROPERTIES("jsonProperties", DataType.STRING, DataType.STRING.getName(), "Additional properties discovered in the analysis.", null, "fe0c84c7-6f19-4c36-897f-9067447d0d9a"),

    /**
     * Date of the review.
     */
    REVIEW_DATE("reviewDate", DataType.DATE, DataType.DATE.getName(), "Date of the review.", null, "7c19aa3a-5fbe-4455-928d-3330b21b22dd"),

    /**
     * Extend or replace the valid instance statuses with additional statuses controlled through valid metadata values.
     */
    USER_DEFINED_STATUS("userDefinedStatus", DataType.STRING, DataType.STRING.getName(), "Extend or replace the valid element statuses with additional statuses controlled through valid metadata values.", null, "ff8797aa-8e7c-414a-8987-ca56ff77ad21"),

    /**
     * Extend or replace the valid instance statuses with additional statuses controlled through valid metadata values.
     */
    USER_DEFINED_ACTIVITY_STATUS("userDefinedActivityStatus", DataType.STRING, DataType.STRING.getName(), "Extend or replace the valid activity statuses with additional statuses controlled through valid metadata values.", null, "37c31c39-39f0-42f4-b256-698669390d5d"),

    /**
     * Source of the regulation.
     */
    REGULATION_SOURCE("regulationSource", DataType.STRING, DataType.STRING.getName(), "Source of the regulation.", "European Union", "d90cffe0-d440-401e-befc-39aaddec46a4"),

    /**
     * Enforcers of the regulation.
     */
    REGULATORS("regulators", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Enforcers of the regulation.", "European Union", "cd2c94fb-41c7-46a5-a64a-9862ac3db071"),

    /**
     * Describes the reasoning for defining the relationship.
     */
    RATIONALE("rationale", DataType.STRING, DataType.STRING.getName(), "Describes the reasoning for defining the relationship.", null, "a724c4e5-b664-4841-87f2-b3df0fa84668"),

    /**
     * User identifier for the steward performing the review.
     */
    STEWARD("steward", DataType.STRING, DataType.STRING.getName(), "Unique identifier for the steward performing the action.", null, "6777fa1e-3289-4896-a032-1097b4ad78b2"),

    /**
     * Type name of the Actor entity identifying the steward.
     */
    STEWARD_TYPE_NAME("stewardTypeName", DataType.STRING, DataType.STRING.getName(), "Type name of the Actor entity identifying the steward.", "Person", "b4c4637d-b196-4268-86a2-c6daf444dd7d"),

    /**
     * Property name for the steward's unique identifier (typically guid or qualifiedName).
     */
    STEWARD_PROPERTY_NAME("stewardPropertyName", DataType.STRING, DataType.STRING.getName(), "Property name for the steward's unique identifier (typically guid or qualifiedName).", "guid", "b9e55ebe-43fb-4eb0-8485-6ef22e07f0a8"),

    /**
     * Notes on why decision were made relating to this element, and other useful information.
     */
    NOTES("notes", DataType.STRING, DataType.STRING.getName(), "Notes on why decision were made relating to this element, and other useful information.", null, "577281c3-82f8-4c1d-ad95-04ef5919e57c"),

    /**
     * Related entity used to determine the retention period.
     */
    ASSOCIATED_GUID("associatedGUID", DataType.STRING, DataType.STRING.getName(), "Related entity used to determine the retention period.", "57261de3-37ed-4fb3-80c9-284501b27416", "ef9fb957-1409-4aae-bbcc-ca9dbef92dc5"),

    /**
     * Date when archiving can take place.
     */
    ARCHIVE_AFTER("archiveAfter", DataType.DATE, DataType.DATE.getName(), "Date when archiving can take place.", null, "dd421eb2-eb6c-4ee4-b8ed-920228581ccd"),

    /**
     * Date when delete can take place.
     */
    DELETE_AFTER("deleteAfter", DataType.DATE, DataType.DATE.getName(), "Date when delete can take place.", null, "cf157cd4-491c-429a-80a6-faf76b91128b"),

    /**
     * Notes provided by the steward.
     */
    COMMENT("comment", DataType.STRING, DataType.STRING.getName(), "Notes provided by the steward.", null, "bb38c0ab-de6c-47d6-ae3a-d4848fd79c58"),

    /**
     * Status of the processing as a result of the annotation.
     */
    ANNOTATION_STATUS("annotationStatus", DataType.STRING, AnnotationStatus.getOpenTypeName(), "Status of the processing as a result of the annotation.", null, "d36618bf-99fc-474f-a958-e8c64cd715ee"),

    /**
     * Date/time that work started on this element.
     */
    START_DATE("startDate", DataType.DATE, DataType.DATE.getName(), "Date/time that work started on this element.", null, "e3e374cc-0f9d-45f6-ae74-7d7a438b17bf"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLE("example", DataType.STRING, DataType.STRING.getName(), "An example of the described concept, element or value.", null, "b207bc5c-b5b5-43f1-94f8-41f4e2902ee5"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLES("examples", DataType.STRING, DataType.STRING.getName(), "Examples of this concept in use.", null, "29c50714-461f-4856-8fcd-30e5b9e7ebaf"),

    /**
     * How this concept name is abbreviated.
     */
    ABBREVIATION("abbreviation", DataType.STRING, DataType.STRING.getName(), "How this glossary term is abbreviated.", null, "0a1eae14-718b-416c-a7d8-04d6f194d7dc"),

    /**
     * Visible version identifier.
     */
    PUBLISH_VERSION_ID("publishVersionIdentifier", DataType.STRING, DataType.STRING.getName(), "Visible version identifier controlled by the author.", "V1.2", "cae42a1b-f752-4ca9-8d26-96829b2f591d"),

    /**
     * Is this value required?
     */
    REQUIRED("required", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is this value required?", null, "fb810cde-18a7-46a0-88a3-6a0fb69b2286"),

    /**
     * Is this data field accepting new  data from the end user or not.
     */
    INPUT_FIELD("inputField", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is this data field accepting new  data from the end user or not.", null, "d2091eb1-0ab6-4038-87e3-695d5d626f57"),

    /**
     * Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.
     */
    COMPLETION_MESSAGE("completionMessage", DataType.STRING, DataType.STRING.getName(), "Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.", null, "f7633bda-9a90-4561-8d5a-356126a855ea"),

    /**
     * Unique name of the process that initiated this request (if applicable).
     */
    PROCESS_NAME("processName", DataType.STRING, DataType.STRING.getName(), "Unique name of the process that initiated this request (if applicable).", null, "9b3e9119-503e-42b4-b972-95169163539b"),

    /**
     * Unique identifier of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_GUID("processStepGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the governance action process step that initiated this request (if applicable).", null, "69f3ec60-338b-4af4-9ebf-4139a33850a4"),

    /**
     * Unique name of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_NAME("processStepName", DataType.STRING, DataType.STRING.getName(), "Unique name of the governance action process step that initiated this request (if applicable).", null, "5f851f84-c871-4ada-b8fa-629449f1ca7b"),

    /**
     * Unique identifier of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_GUID("governanceActionTypeGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the governance action type that initiated this request (if applicable).", null, "73c3d8bb-05ec-40eb-9ff0-31de48407c14"),

    /**
     * Unique name of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_NAME("governanceActionTypeName", DataType.STRING, DataType.STRING.getName(), "Unique name of the governance action type that initiated this request (if applicable).", null, "e1921f31-306c-4a11-b37c-67eaae2eefea"),

    /**
     * Display name for the discovered schema.
     */
    SCHEMA_NAME("schemaName", DataType.STRING, DataType.STRING.getName(), "Display name for the discovered schema.", null, "c6011a00-b9c0-45d9-a525-88014aa3546f"),

    /**
     * Type name for the discovered schema.
     */
    SCHEMA_TYPE("schemaType", DataType.STRING, DataType.STRING.getName(), "Type name for the discovered schema.", null, "6a68d174-fd29-49ef-aa4b-26205dbebb58"),

    /**
     * Length of the data field (zero means unlimited).
     */
    LENGTH("length", DataType.INT, DataType.INT.getName(), "Length of the data field (zero means unlimited).", null, "35a17811-1635-4ab1-9a15-0f79bc771a27"),

    /**
     * Potential classification names and properties as JSON.
     */
    CANDIDATE_CLASSIFICATIONS("candidateClassifications", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Potential classification names and properties as JSON.", null, "3ea11266-368b-4c0d-ad4d-2943a14a38ad"),

    /**
     * Inferred data type based on the data values.
     */
    INFERRED_DATA_TYPE("inferredDataType", DataType.STRING, DataType.STRING.getName(), "Inferred data type based on the data values.", null, "27c3d24d-8ea9-4daf-ab82-c813b3dfa01a"),

    /**
     * Inferred data format based on the data values.
     */
    INFERRED_FORMAT("inferredFormat", DataType.STRING, DataType.STRING.getName(), "Inferred data format based on the data values.", null, "0198a639-1906-4377-b8ba-56cccbf88b3e"),

    /**
     * Inferred data field length based on the data values.
     */
    INFERRED_LENGTH("inferredLength", DataType.INT, DataType.INT.getName(), "Inferred data field length based on the data values.", null, "e67b7353-20e0-46be-b912-e96192a200fc"),

    /**
     * Inferred precision of the data based on the data values.
     */
    INFERRED_PRECISION("inferredPrecision", DataType.INT, DataType.INT.getName(), "Inferred precision of the data based on the data values.", null, "4ce73780-f940-4da6-bfcb-3b726ede2f7b"),

    /**
     * Inferred scale applied to the data based on the data values.
     */
    INFERRED_SCALE("inferredScale", DataType.INT, DataType.INT.getName(), "Inferred scale applied to the data based on the data values.", null, "bd28ec07-01bd-4db4-9377-33b15097ded4"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_PROPERTY_NAMES("profilePropertyNames", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of property names used in this annotation.", "[profileEndDate, profileCounts]", "210e2977-6803-4fce-93e8-15547168d459"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_START_DATE("profileStartDate", DataType.DATE, DataType.DATE.getName(), "Time at which the profiling started collecting data.", null, "6bee91ad-4eae-4292-ab28-ff1e5404e74d"),

    /**
     * Time at which the profiling stopped collecting data.
     */
    PROFILE_END_DATE("profileEndDate", DataType.DATE, DataType.DATE.getName(), "Time at which the profiling stopped collecting data.", null, "9f31776f-f567-40aa-92cd-b990131dbdb8"),

    /**
     * Additional profile properties discovered during the analysis.
     */
    PROFILE_PROPERTIES("profileProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional profile properties discovered during the analysis.", null, "53f5e89d-6730-4ae5-bfbf-62d4512eff58"),

    /**
     * Additional flags (booleans) discovered during the analysis.
     */
    PROFILE_FLAGS("profileFlags", DataType.MAP_STRING_BOOLEAN, DataType.MAP_STRING_BOOLEAN.getName(), "Additional flags (booleans) discovered during the analysis.", null, "b7b28f24-5464-4cab-8c6b-10bc11ed6118"),

    /**
     * Relevant dates discovered during the analysis.
     */
    PROFILE_DATES("profileDates", DataType.MAP_STRING_DATE, DataType.MAP_STRING_DATE.getName(), "Relevant dates discovered during the analysis.", null, "7d700cc5-b56c-4aeb-8416-7cf8d17d16c2"),

    /**
     * Additional counts discovered during the analysis.
     */
    PROFILE_COUNTS("profileCounts", DataType.MAP_STRING_LONG, DataType.MAP_STRING_LONG.getName(), "Additional counts discovered during the analysis.", null, "6550a7b0-4ab2-4188-b32f-c1576f4bcedc"),

    /**
     * Additional large counts discovered during the analysis.
     */
    PROFILE_DOUBLES("profileDoubles", DataType.MAP_STRING_DOUBLE,DataType.MAP_STRING_DOUBLE.getName(), "Additional large counts discovered during the analysis.", null, "e77933c9-64c6-476f-bd0f-e905838fb7fa"),

    /**
     * List of individual values in the data.
     */
    VALUE_LIST("valueList", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of individual values in the data.", null, "6d67712d-d302-48b0-a05a-35d9d6da4380"),

    /**
     * Count of individual values in the data.
     */
    VALUE_COUNT("valueCount", DataType.MAP_STRING_INT, DataType.MAP_STRING_INT.getName(), "Count of individual values in the data.", null, "544a4595-7d96-465f-acc9-1b1cf472c671"),

    /**
     * Lowest value in the data.
     */
    VALUE_RANGE_FROM("valueRangeFrom", DataType.STRING, DataType.STRING.getName(), "Lowest value in the data.", null, "8a5f6805-1014-4016-a94e-6d02236f6408"),

    /**
     * Highest value in the data.
     */
    VALUE_RANGE_TO("valueRangeTo", DataType.STRING, DataType.STRING.getName(), "Highest value in the data.", null, "623c5733-80b4-41be-a9cc-4823c5ce5cf7"),

    /**
     * Typical value in the data.
     */
    AVERAGE_VALUE("averageValue", DataType.STRING, DataType.STRING.getName(), "Typical value in the data.", null, "52ed8053-42b9-4d26-a1d5-c6aba915ccb1"),

    /**
     * List of possible matching data classes.
     */
    CANDIDATE_DATA_CLASS_GUIDS("candidateDataClassGUIDs", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of possible matching data classes.", null, "e6d6f746-50a4-4e1c-b998-06de82f0a839"),

    /**
     * Number of values that match the data class specification.
     */
    MATCHING_VALUES("matchingValues", DataType.LONG, DataType.LONG.getName(), "Number of values that match the data class specification.", null, "96c33d36-cff9-455e-944f-38224eb7448b"),

    /**
     * Number of values that do not match the data class specification.
     */
    NON_MATCHING_VALUES("nonMatchingValues", DataType.LONG, DataType.LONG.getName(), "Number of values that do not match the data class specification.", null, "7bb6364d-486f-4005-97c9-941c7b7154d5"),

    /**
     * Suggested term based on the analysis.
     */
    INFORMAL_TERM("informalTerm", DataType.STRING, DataType.STRING.getName(), "Suggested term based on the analysis.", null, "3a13654b-f671-4928-990a-96cfe8d25422"),

    /**
     * List of potentially matching glossary terms.
     */
    CANDIDATE_GLOSSARY_TERM_GUIDS("candidateGlossaryTermGUIDs", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of potentially matching glossary terms.", null, "8be05385-75fe-42e2-a87f-2adb47289395"),

    /**
     * List of glossary folders that contain potentially matching glossary terms.
     */
    CANDIDATE_GLOSSARY_FOLDER_GUIDS("candidateGlossaryFolderGUIDs", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of glossary folders that contain potentially matching glossary terms.", null, "5b865d63-65b8-4d29-b382-e0490de041c9"),

    /**
     * Type of quality calculation.
     */
    QUALITY_DIMENSION("qualityDimension", DataType.STRING, DataType.STRING.getName(), "Type of quality calculation.", null, "a865a7d9-b18c-4aae-853b-c429bd5f8c32"),

    /**
     * Calculated quality value.
     */
    QUALITY_SCORE("qualityScore", DataType.INT, DataType.INT.getName(), "Calculated quality value.", null, "cdbe5189-771d-42a5-917a-42923dedaf89"),

    /**
     * Type name of the potential relationship.
     */
    RELATIONSHIP_TYPE_NAME("relationshipTypeName", DataType.STRING, DataType.STRING.getName(), "Type name of the potential relationship.", null, "477c226d-ceb0-4f4a-87d2-f60632a9dbf2"),

    /**
     * Entity that should be linked to the asset being analyzed.
     */
    RELATED_ENTITY_GUID("relatedEntityGUID", DataType.STRING, DataType.STRING.getName(), "Entity that should be linked to the asset being analyzed.", null, "d399931b-29f4-4039-bee5-4a68c1b68d28"),

    /**
     * Properties to add to the relationship.
     */
    RELATIONSHIP_PROPERTIES("relationshipProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Properties to add to the relationship.", null, "d7cfc6d9-1b1a-4266-875a-b9f713d80643"),

    /**
     * Discovered properties of the data source.
     */
    RESOURCE_PROPERTIES("resourceProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Discovered properties of the resource.", null, "c187fe2c-cee8-4c4b-99e7-dcb27032a88d"),

    /**
     * Name of the activity that revealed the need for action.
     */
    ACTION_SOURCE_NAME("actionSourceName", DataType.STRING, DataType.STRING.getName(), "Name of the activity that revealed the need for action.", null, "dddf1c9e-d0d6-4812-a757-1d01c79f34ef"),

    /**
     * What needs to be done.
     */
    ACTION_REQUESTED("actionRequested", DataType.STRING, DataType.STRING.getName(), "What needs to be done.", null, "3eca27fe-0538-43f1-83b9-cf9ee43a32ec"),

    /**
     * Additional information for use during action processing.
     */
    ACTION_PROPERTIES("actionProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional information for use during action processing.", null, "b2302d78-f5e2-4fd7-8932-ad10ac868ab9"),

    /**
     * Creation time of the data store.
     */
    RESOURCE_CREATE_TIME("resourceCreateTime", DataType.DATE, DataType.DATE.getName(), "Creation time of the data store.", null, "766d9f17-40f9-44da-a0c8-679002a0cf91"),

    /**
     * Last known modification time.
     */
    RESOURCE_UPDATE_TIME("resourceUpdateTime", DataType.DATE, DataType.DATE.getName(), "Last known modification time.", null, "6012bdee-31d7-46d4-9f3b-f4d19c47e662"),

    /**
     * Last known modification time.
     */
    RESOURCE_LAST_ACCESSED_TIME("resourceLastAccessedTime", DataType.DATE, DataType.DATE.getName(), "Last known access time.", null, "f884c0bf-f2d1-4cdb-81ff-0fc7cc7c945c"),

    /**
     * Creation time of the data store.
     */
    STORE_CREATE_TIME("storeCreateTime", DataType.DATE, DataType.DATE.getName(), "Creation time of the data store.", null, "1a3abee2-f174-433d-8355-44c5810c471b"),

    /**
     * Last known modification time.
     */
    STORE_UPDATE_TIME("storeUpdateTime", DataType.DATE, DataType.DATE.getName(), "Last known modification time.", null, "134bbbc3-4b68-4d35-a8da-85719cced8a9"),


    /**
     * Size of the data source.
     */
    SIZE("size", DataType.INT, DataType.INT.getName(), "Size of the data source.", null, "c2d67f78-3387-4b83-9c1c-bd0aa88a4df0"),

    /**
     * Encoding scheme used on the data.
     */
    ENCODING_TYPE("encodingType", DataType.STRING, DataType.STRING.getName(), "Type of encoding scheme used on the data.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Language used in the encoding.
     */
    ENCODING_LANGUAGE("encodingLanguage", DataType.STRING, DataType.STRING.getName(), "Language used in the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Description of the encoding.
     */
    ENCODING_DESCRIPTION("encodingDescription", DataType.STRING, DataType.STRING.getName(), "Description of the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Additional properties describing the encoding.
     */
    ENCODING_PROPERTIES("encodingProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional properties describing the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Identifier that describes the type of resource use.
     */
    RESOURCE_USE("resourceUse", DataType.STRING, DataType.STRING.getName(), "Identifier that describes the type of resource use.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * "Time that the IT Infrastructure was deployed."
     */
    DEPLOYMENT_TIME( "deploymentTime", DataType.DATE, DataType.DATE.getName(), "Time that the IT Infrastructure was deployed.", null, "aa372f7f-8284-4e24-902e-9f01dcc70b3e"),

    /**
     * Type name of deployer.
     */
    DEPLOYER_TYPE_NAME( "deployerTypeName", DataType.STRING, DataType.STRING.getName(), "Type name of deployer.", null, "cb027494-8de7-43cc-845c-57d4f0bbf6d5"),

    /**
     * Identifying property name of deployer.
     */
    DEPLOYER_PROPERTY_NAME( "deployerPropertyName", DataType.STRING, DataType.STRING.getName(), "Identifying property name of deployer.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Person, organization or engine that deployed the IT Infrastructure.
     */
    DEPLOYER("deployer", DataType.STRING, DataType.STRING.getName(), "Person, organization or engine that deployed the IT Infrastructure.", null, "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    INSTALL_TIME("installTime",
                 DataType.DATE, DataType.DATE.getName(),
                 "Time that the software was installed on the IT Infrastructure.",
                 null,
                 "6cc366b8-b871-4b8f-a4dc-e41c06a025c8"),

    /**
     * The operational status of the software server capability on this software server.
     */
    OPERATIONAL_STATUS("operationalStatus", DataType.STRING, OperationalStatus.getOpenTypeName(), "The operational status of the deployed infrastructure.", null, "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Patch level of the software server capability.
     */
    PATCH_LEVEL("patchLevel", DataType.STRING, DataType.STRING.getName(), "Patch level of the software server capability.", "456", "574bea24-8dd6-407a-bed4-aaa432d10276"),

    /**
     * Describes how the software capability uses the asset.
     */
    USE_TYPE("useType", DataType.STRING, CapabilityAssetUseType.getOpenTypeName(), "Describes how the software capability uses the asset.", "OWNS", "a8cfffa4-a761-4fe0-be8b-6be43ac55020"),

    /**
     * Maximum number of running asset instances controlled by the software capability.
     */
    MAXIMUM_INSTANCES("maximumInstances", DataType.INT, DataType.INT.getName(), "Maximum number of running asset instances controlled by the software capability.", "0", "76b06411-e3bf-4a39-9351-140e6ad0b82f"),

    /**
     * Minimum number of running asset instances controlled by the software capability.
     */
    MINIMUM_INSTANCES("minimumInstances", DataType.INT, DataType.INT.getName(),"Minimum number of running asset instances controlled by the software capability.", "12", "d5033e20-3cd7-4c27-a07f-4a95feee10d8"),

    /**
     * Identifier used in an external system.
     */
    IDENTIFIER("identifier", DataType.STRING, DataType.STRING.getName(), "Identifier used in an external system.", null, "2f831c53-0c71-4e03-92ed-20a899ed771f"),

    /**
     * The type name of the instance in the external system.
     */
    EXT_INSTANCE_TYPE_NAME("externalInstanceTypeName", DataType.STRING, DataType.STRING.getName(), "The type name of the instance in the external system.", null, "bcc3de39-b8d7-4a33-8f70-601fa8c2e9cc"),

    /**
     * The username of the person or process that created the instance in the external system.
     */
    EXT_INSTANCE_CREATED_BY("externalInstanceCreatedBy", DataType.STRING, DataType.STRING.getName(), "The username of the person or process that created the instance in the external system.", null, "480a57d1-7300-4691-ad8b-540d97ef0870"),

    /**
     * The date/time when the instance in the external system was created.
     */
    EXT_INSTANCE_CREATION_TIME("externalInstanceCreationTime", DataType.DATE, DataType.DATE.getName(), "The date/time when the instance in the external system was created.", null, "50e22b77-6091-4316-836e-2f76468add71"),

    /**
     * The username of the person or process that last updated the instance in the external system.
     */
    EXT_INSTANCE_LAST_UPDATED_BY("externalInstanceLastUpdatedBy", DataType.STRING, DataType.STRING.getName(), "The username of the person or process that last updated the instance in the external system.", null, "f29f9183-e651-42d5-b0c8-bb1491f3151e"),

    /**
     * The date/time when the instance in the external system was last updated.
     */
    EXT_INSTANCE_LAST_UPDATE_TIME("externalInstanceLastUpdateTime", DataType.DATE, DataType.DATE.getName(), "The date/time when the instance in the external system was last updated.", null, "0502401e-5292-4860-9fd7-dd2b0c46859d"),

    /**
     * The latest version of the element in the external system.
     */
    EXT_INSTANCE_VERSION("externalInstanceVersion" , DataType.LONG, DataType.LONG.getName(), "The latest version of the element in the external system.", null, "349199e2-5781-4413-8550-c85241e20cc5"),

    /**
     * Additional properties to aid the mapping to the element in an external metadata source.
     */
    MAPPING_PROPERTIES("mappingProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional properties to aid the mapping to the the element in an external metadata source.", null, "8161e120-993b-490c-bf66-cb9fd85192fc"),

    /**
     * Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.
     */
    LAST_SYNCHRONIZED("lastSynchronized", DataType.DATE, DataType.DATE.getName(), "Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.", null, "45f5f2fc-17ec-4f02-8d7a-8cfe8f1557fe"),

    /**
     * Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.
     */
    PERMITTED_SYNCHRONIZATION("permittedSynchronization", DataType.STRING, PermittedSynchronization.getOpenTypeName(), "Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.", null, "45f5f2fc-17ec-4f02-8d7a-8cfe8f1557fe"),

    /**
     * Type of role that the attribute plays as part of the concept bead.
     */
    KEY("key", DataType.STRING, DataType.STRING.getName(), "The third party system identifier.", "0bea195b-2bbc-460d-8e86-8202669abf12", "fc65a493-a5d6-48e7-a805-8950471aedb3"),

    /**
     * Type of identifier that identifies its lifecycle, for example, its scope and whether the value is reused.
     */
    KEY_PATTERN("keyPattern", DataType.STRING, KeyPattern.getOpenTypeName(), "Type of identifier that identifies its lifecycle, for example, its scope and whether the value is reused.", null, "a8805753-865d-4860-ab95-1e83c3eaf01d"),

    /**
     * Type of role that the attribute plays as part of the concept bead.
     */
    COVERAGE_CATEGORY("coverageCategory", DataType.STRING, CoverageCategory.getOpenTypeName(), "Used to describe how a collection of data values for an attribute cover the domain of the possible values to the linked attribute.", CoverageCategory.UNIQUE_IDENTIFIER.toString(), "4cac11a2-1187-4a54-b94a-8fa493c0b860"),


    /**
     * The relationship of element that has been changed to the anchor.
     */
    CHANGE_TARGET("changeTarget", DataType.STRING, LatestChangeTarget.getOpenTypeName(), "The relationship to the anchor that the changed element has.", null, "b3a4c3c0-f17f-4380-848a-6c6d985c2dd3"),

    /**
     * The type of change.
     */
    CHANGE_ACTION("changeAction", DataType.STRING, LatestChangeAction.getOpenTypeName(), "The type of change.", null, "365c4667-b86a-445f-860b-dc35eac917f2"),

    /**
     * Guidance on how the element should be used.
     */
    USAGE("usage", DataType.STRING, DataType.STRING.getName(), "Guidance on how the element should be used.", null, "e92f8669-5a07-4130-9ad6-62aadca7a505"),

    /**
     * Details of where the element was sourced from.
     */
    SOURCE("source", DataType.STRING, DataType.STRING.getName(), "Details of the organization, person or process that created the element, or provided the information used to create the element.", null, "9c40c4e3-1d6d-45fd-8df0-f1a2e09db636"),

    /**
     * Deployed version number for this software.
     */
    SOFTWARE_VERSION("softwareVersion", DataType.STRING, DataType.STRING.getName(), "Deployed version number for this software.", null, "1386c460-d28e-4c19-ba32-b61ba11c8038"),

    /**
     * Level of confidence in the correctness of the element.
     */
    CONFIDENCE("confidence", DataType.INT, DataType.INT.getName(), "Level of confidence in the correctness of the element. 0=unknown; 1=low confidence; 100=total confidence.", "100", "26dd007a-cff3-45e7-963d-2a753c2b7000"),

    /**
     * The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.
     */
    USES_BLOCKING_CALLS("usesBlockingCalls", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.", "false", "cd23ea21-75b0-45d2-9292-e63510c3a1e2"),

    /**
     * Full name of the topic as used by programs to access its contents
     */
    TOPIC_NAME("topicName", DataType.STRING, DataType.STRING.getName(), "Full name of the topic as used by programs to access its contents.", "egeria.omag.server.active-metadata-store.omas.assetconsumer.outTopic", "eda530d2-62d3-4325-9840-514f001ffc12"),

    /**
     * Type of topic.
     */
    TOPIC_TYPE("topicType", DataType.STRING, DataType.STRING.getName(), "Type of topic.", "PLAINTEXT", "17eb67ae-6805-4f47-98a7-ed124804c9a6"),

    /**
     * The name of the property that the valid value represents.
     */
    PROPERTY_NAME("propertyName", DataType.STRING, DataType.STRING.getName(), "The name of the property that the valid value represents.", "producedGuard", "f9f2eba1-943a-4611-8bdd-647c1645b036"),

    /**
     * The phase in the lifecycle of the project.
     */
    PROJECT_PHASE("projectPhase", DataType.STRING, DataType.STRING.getName(), "The phase in the lifecycle of the project.", "Plan", "9178fece-e112-4250-9c08-5c336bd93f78"),

    /**
     * Indicator on how well the project is tracking to plan.
     */
    PROJECT_HEALTH("projectHealth", DataType.STRING, DataType.STRING.getName(), "Indicator on how well the project is tracking to plan.", "On Track", "eabe799d-72d7-46a2-aa56-5ceaf723a65f"),

    /**
     * Short description on current status of the project.
     */
    PROJECT_STATUS("projectStatus", DataType.STRING, DataType.STRING.getName(), "Short description on current status of the project.", "Active", "39643f86-185b-465e-9e84-3f74905bad82"),

    /**
     * Display name of the product.
     */
    PRODUCT_NAME("productName", DataType.STRING, DataType.STRING.getName(),  "Display name of the product.", "Teddy Bear Drop Foot Clinical Trial Patient Measurements", "aae2b086-05ea-433a-b1d6-502674c4ae6e"),

    /**
     * Date that the product was made available.
     */
    INTRODUCTION_DATE("introductionDate", DataType.DATE, DataType.DATE.getName(), "Date that the product was made available.", null,  "9fffa5e8-f2d2-4184-be1d-482d7e093ec0"),

    /**
     * Date when is the next version is expected to be released.
     */
    NEXT_VERSION_DATE("nextVersionDate", DataType.DATE, DataType.DATE.getName(),  "Date when is the next version is expected to be released.", null, "5866d607-157f-41f2-8875-00b60f534e99"),

    /**
     * Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.
     */
    WITHDRAW_DATE("withdrawDate", DataType.DATE, DataType.DATE.getName(), "Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.", null, "f2fc6a12-6bf9-4c2f-ad99-68c892b44e76"),

    /**
     * Level of maturity for the product.
     */
    MATURITY("maturity", DataType.STRING, DataType.STRING.getName(), "Level of maturity for the product.", "InDev",  "666b4e71-cb40-4421-9291-0736cfa5136e"),

    /**
     * Length of time that the product is expected to be in service.
     */
    SERVICE_LIFE("serviceLife", DataType.STRING, DataType.STRING.getName(), "Length of time that the product is expected to be in service.", "2 years", "f61776bc-e78e-4c4a-9b22-275c8eb4c132"),

    /**
     * Defines the provenance and confidence that a member belongs in a collection.
     */
    MEMBERSHIP_STATUS("membershipStatus", DataType.STRING, CollectionMemberStatus.getOpenTypeName(), CollectionMemberStatus.getOpenTypeDescription(), CollectionMemberStatus.PROPOSED.getName(), "e304a92d-60d2-4605-8e3f-d338bd33e6d3"),

    /**
     * Description of how the member is used, or why it is useful in this collection.
     */
    MEMBERSHIP_RATIONALE("membershipRationale", DataType.STRING, DataType.STRING.getName(), "Description of how the member is used, or why it is useful in this collection.", null, "f4c0da71-f8e8-4d05-a92c-5a1e6b4a263e"),

    /**
     * Name of the type of membership.
     */
    MEMBERSHIP_TYPE("membershipType", DataType.STRING, DataType.STRING.getName(), "Name of the type of membership.", null, "ebf4af8d-b9f7-45d1-8f87-8cc383df4605"),

    /**
     * Defines the sequential order in which bytes are arranged into larger numerical values when stored in memory or when transmitted over digital links.
     */
    BYTE_ORDERING("byteOrdering", DataType.STRING, ByteOrdering.getOpenTypeName(), ByteOrdering.getOpenTypeDescription(), ByteOrdering.LITTLE_ENDIAN.getName(), "efbf89db-dc9f-4f21-a752-c190ffeae4d5"),

    /**
     * Descriptor for a comment that indicated its intent.
     */
    COMMENT_TYPE("commentType", DataType.STRING, CommentType.getOpenTypeName(), CommentType.getOpenTypeDescription(), CommentType.SUGGESTION.getName(), "37ceb246-ebbe-4bbe-be63-b68e145181a6"),

    /**
     * Type of mechanism to contact an actor.
     */
    CONTACT_METHOD_TYPE("contactMethodType", DataType.STRING, ContactMethodType.getOpenTypeName(), ContactMethodType.getOpenTypeDescription(), ContactMethodType.EMAIL.getName(), "7c67bc0d-8256-4113-b2fb-0fafe0ec8100"),

    /**
     * Type of contact - such as home address, work mobile, emergency contact ...
     */
    CONTACT_TYPE("contactType", DataType.STRING, DataType.STRING.getName(), "Type of contact - such as home address, work mobile, emergency contact ...", ContactMethodType.EMAIL.getName(), "caa6fbd3-a470-473c-a656-0265c4496d54"),

    /**
     * Details of the contact method
     */
    CONTACT_METHOD_VALUE("contactMethodValue", DataType.STRING, DataType.STRING.getName(), "Details of the contact method.", null, "ef793035-7111-4424-b776-6c6c84f38343"),

    /**
     * Details of the service supporting the contact method.
     */
    CONTACT_METHOD_SERVICE("contactMethodService", DataType.STRING, DataType.STRING.getName(), "Details of the service supporting the contact method.", null, "55a9ed5a-c279-4dbc-895e-fda5c15000da"),


    /**
     * Defines the level of confidence to place in the accuracy of a data item.
     */
    CONFIDENCE_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT, DataType.INT.getName(), ConfidenceLevel.getOpenTypeDescription(), Integer.toString(ConfidenceLevel.AUTHORITATIVE.getOrdinal()), "449558d5-3811-401d-b2be-f131480f9bc8"),

    /**
     * Defines how important a data item is to the organization.
     */
    CRITICALITY_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT, DataType.INT.getName(), CriticalityLevel.getOpenTypeDescription(), Integer.toString(CriticalityLevel.IMPORTANT.getOrdinal()), "b2c99d60-4078-4d06-beec-f7fcc588c344"),

    /**
     * Defines how confidential a data item is.
     */
    CONFIDENTIALITY_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT, DataType.INT.getName(), ConfidentialityLevel.getOpenTypeDescription(), Integer.toString(ConfidentialityLevel.CONFIDENTIAL.getOrdinal()), "908488a2-13f2-469d-b28d-20a7af6be4a1"),

    /**
     * Defines the retention requirements associated with a data item.
     */
    RETENTION_BASIS_IDENTIFIER("basisIdentifier", DataType.INT, DataType.INT.getName(), RetentionBasis.getOpenTypeDescription(), Integer.toString(RetentionBasis.PROJECT_LIFETIME.getOrdinal()), "68069d73-0e20-4cb7-a7ec-1555edbbc280"),

    /**
     * Defines the severity of the impact that a situation has.
     */
    SEVERITY_IDENTIFIER("severityIdentifier", DataType.INT, DataType.INT.getName(), ImpactSeverity.getOpenTypeDescription(), Integer.toString(ImpactSeverity.MEDIUM.getOrdinal()), "f565af06-37a1-42e9-b680-2c98715f9ff3"),

    /**
     * Defines how important a data item is to the organization.
     */
    STATUS_IDENTIFIER("statusIdentifier", DataType.INT, DataType.INT.getName(), GovernanceClassificationStatus.getOpenTypeDescription(), Integer.toString(GovernanceClassificationStatus.VALIDATED.getOrdinal()), "8279602a-c86d-4cf7-b66d-1ea98b75c491"),

    /**
     * Defines the suggested order that data values in this data item should be sorted by.
     */
    SORT_ORDER("sortOrder", DataType.STRING, DataItemSortOrder.getOpenTypeName(),DataItemSortOrder.getOpenTypeDescription(), DataItemSortOrder.ASCENDING.getName(), "645fd9ac-530d-4351-ab6b-73a999332a78"),

    /**
     * Defines the current status of an incident report.
     */
    INCIDENT_STATUS("incidentStatus", DataType.STRING, IncidentReportStatus.getOpenTypeName(), IncidentReportStatus.getOpenTypeDescription(), IncidentReportStatus.VALIDATED.getName(), "36ce39eb-1389-493c-9a7a-5d52e7f16cda"),

    /**
     * Defines the current execution status of an engine action.
     */
    ACTIVITY_STATUS("activityStatus", DataType.STRING, ActivityStatus.getOpenTypeName(), ActivityStatus.getOpenTypeDescription(), ActivityStatus.ACTIVATING.getName(), "c2d8fd79-0c16-4e01-888f-2ae7a9cdf11d"),

    /**
     * The name to identify the action target to the actor that processes it.
     */
    ACTION_TARGET_NAME("actionTargetName", DataType.STRING, DataType.STRING.getName(), "The name to identify the action target to the actor that processes it.", null, "3a5d325f-267c-4821-beb2-2c59d89891ed"),

    /**
     * Different types of activities.
     */
    ACTIVITY_TYPE("type", DataType.STRING, ActivityType.getOpenTypeName(), ActivityType.getOpenTypeDescription(), ActivityType.PROJECT.getName(), "6949c588-d7ab-441f-be03-a97b1dc2900b"),

    /**
     * Defines the confidence in the assigned relationship.
     */
    TERM_RELATIONSHIP_STATUS("status", DataType.STRING, TermRelationshipStatus.getOpenTypeName(), TermRelationshipStatus.getOpenTypeDescription(), TermRelationshipStatus.DRAFT.getName(), "5cc02a53-2428-434a-9b97-883eae896552"),

    /**
     * Defines the provenance and confidence of a semantic assignment.
     */
    TERM_ASSIGNMENT_STATUS("termAssignmentStatus", DataType.STRING, TermAssignmentStatus.getOpenTypeName(), TermAssignmentStatus.getOpenTypeDescription(), TermAssignmentStatus.IMPORTED.getName(), "d842dfdd-f080-4539-9a3c-eacdf0a03d07"),

    /**
     * Defines the provenance and confidence of a data class assignment.
     */
    DATA_CLASS_ASSIGNMENT_STATUS("dataClassAssignmentStatus", DataType.STRING, DataClassAssignmentStatus.getOpenTypeName(), DataClassAssignmentStatus.getOpenTypeDescription(), DataClassAssignmentStatus.IMPORTED.getName(), "71e53cf4-7158-4054-b7f8-da643a34d2da"),

    /**
     * An indication of the relative position in which this work item should be tackled compared to others in the overall work list.
     */
    PRIORITY("priority", DataType.INT, DataType.INT.getName(), "An indication of the relative position in which this work item should be tackled compared to others in the overall work list.", "10", "6168ae13-f6ee-49e7-9f21-693e7a401926"),

    /**
     * Metadata properties embedded in the media file.
     */
    EMBEDDED_METADATA("embeddedMetadata", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Metadata properties embedded in the media file.", null, "af5a6693-e14c-489e-a76c-1a45248e9dbd"),

    /**
     * Full publication title of the external source.
     */
    REFERENCE_TITLE("referenceTitle", DataType.STRING, DataType.STRING.getName(), "Full publication title of the external source.", null, "e98ea20f-111b-421d-91a5-586d469f990d"),

    /**
     * Summary of the key messages in the external source.
     */
    REFERENCE_ABSTRACT("referenceAbstract", DataType.STRING, DataType.STRING.getName(), "Summary of the key messages in the external source.", null, "57b63b64-5139-45db-94e4-146a57389fe8"),

    /**
     * List of authors for the external source.
     */
    AUTHORS("authors", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of authors for the external source.", null, "b0dd1911-7bb6-47be-bd88-5a9c86f00fe0"),

    /**
     * Number of pages that this external source has.
     */
    NUMBER_OF_PAGES("numberOfPages", DataType.INT, DataType.INT.getName(), "Number of pages that this external source has.", null, "dfdb0daa-3b5d-4f76-a674-bf2e683f587c"),

    /**
     * Range of pages that this reference covers.
     */
    PAGE_RANGE("pageRange", DataType.STRING, DataType.STRING.getName(), "Range of pages that this reference covers. For example, if it is a journal article, this could be the range of pages for the article in the journal.", "35-98", "6a97b691-fbba-44ae-9557-f18b627a7fb1"),

    /**
     * Name of the journal or series of publications that this external source is from.
     */
    PUBLICATION_SERIES("publicationSeries", DataType.STRING, DataType.STRING.getName(), "Name of the journal or series of publications that this external source is from.", null, "93426a39-9a40-475b-aa3c-937a3abfd40e"),

    /**
     * Name of the volume in the publication series that this external source is from.
     */
    PUBLICATION_SERIES_VOLUME("publicationSeriesVolume", DataType.STRING, DataType.STRING.getName(), "Name of the volume in the publication series that this external source is from.", null, "9ba221d6-d9c6-4d4e-9e65-9bb0a523faff"),

    /**
     * Name of the edition for this external source.
     */
    EDITION("edition", DataType.STRING, DataType.STRING.getName(), "Name of the edition for this external source.", "First Edition", "d327eb3c-22d5-4dc1-8206-fe82089c1011"),

    /**
     * Network address where this external source can be accessed from.
     */
    URL("url", DataType.STRING, DataType.STRING.getName(), "Primary address where this external source can be accessed from.", null, "604dbf10-335d-4132-8a32-c30fa9fec154"),


    /**
     * Names of external sources and specific location where this external information can be accessed from
     */
    SOURCES("sources", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Names of external sources and specific location where this external information can be accessed from.  This may be URLs, publisher or retailer information, for example.", null, "c4189336-10ea-4a92-8974-6802170eae88"),

    /**
     * Name of the publisher responsible for producing this external source.
     */
    PUBLISHER("publisher", DataType.STRING, DataType.STRING.getName(), "Name of the publisher responsible for producing this external source.", null, "3a92b878-c0ae-464b-a70b-63fd61a36ec5"),

    /**
     * Date of the first published version/edition of this external source.
     */
    FIRST_PUB_DATE("firstPublicationDate", DataType.DATE, DataType.DATE.getName(), "Date of the first published version/edition of this external source.", null, "22eac39f-8097-4f17-84b2-d718f06b242c"),

    /**
     * Date when this version/edition of this external source was published.
     */
    PUBLICATION_DATE("publicationDate", DataType.INT, DataType.INT.getName(), "Date when this version/edition of this external source was published.", null, "1b6d0398-31f3-4f8e-9998-3b975323423d"),

    /**
     * City where the publishers are based.
     */
    PUBLICATION_CITY("publicationCity", DataType.STRING, DataType.STRING.getName(), "City where the publishers are based.", "London", "16bb6599-a056-4d49-b757-833ba1bd2b38"),

    /**
     * Year when the publication of this version/edition of the external source was published.
     */
    PUBLICATION_YEAR("publicationYear", DataType.STRING, DataType.STRING.getName(), "Year when the publication of this version/edition of the external source was published.", "2020", "5ae46954-7762-4ab5-87e2-828287e82013"),

    /**
     * List of unique numbers allocated by the publisher for this external source.  For example ISBN, ASIN, UNSPSC code.
     */
    PUBLICATION_NUMBERS("publicationNumbers", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of unique numbers allocated by the publisher for this external source.  For example ISBN, ASIN, UNSPSC code.", null, "2c734b4c-de01-490f-9675-da4addc5efe4"),

    /**
     * Name of license associated with this external source.
     */
    LICENSE("license", DataType.STRING, DataType.STRING.getName(), "Name of license associated with this external source.", null, "c64f169a-863b-4355-b5a6-27338e1152c1"),

    /**
     * Copyright statement associated with this external source.
     */
    COPYRIGHT("copyright", DataType.STRING, DataType.STRING.getName(), "Copyright statement associated with this external source.", null, "0ece8225-2dfe-4716-bfe2-bac791b6c5f7"),

    /**
     * Attribution statement to use when consuming this external resource.
     */
    ATTRIBUTION("attribution", DataType.STRING, DataType.STRING.getName(), "Attribution statement to use when consuming this external resource.", null, "f2e665a5-c7e0-48c0-b3d6-e47c3b1fde02"),

    /**
     * Local identifier for the reference.
     */
    REFERENCE_ID("referenceId", DataType.STRING, DataType.STRING.getName(), "Local identifier for the reference.", null, "90b32257-1795-4b2f-8383-bfdbdbc1ad89"),

    /**
     * Range of pages in the external reference that this link refers.
     */
    PAGES("pages", DataType.STRING, DataType.STRING.getName(), "Range of pages in the external reference that this link refers.", "1-10", "31144831-a9df-47b1-9975-902e0f76836a"),

    /**
     * Specific media usage by the consumer that overrides the default media usage documented in the related media.
     */
    MEDIA_USAGE("mediaUsage", DataType.STRING, MediaUsage.getOpenTypeName(), "Specific media usage by the consumer that overrides the default media usage documented in the related media.", null, "483bbce1-e070-46ad-8636-f813c9309e56"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media use.
     */
    MEDIA_USAGE_OTHER_ID("mediaUsageOtherId", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media use.", null, "66102545-c7b2-4501-a85e-de6e348a92a5"),

    /**
     * The most common, or expected use of this media resource.
     */
    DEFAULT_MEDIA_USAGE("defaultMediaUsage", DataType.STRING, MediaUsage.getOpenTypeName(), "The most common, or expected use of this media resource.", null, "279f1eb4-e60c-4ebc-9568-cc545c2fe491"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media use.
     */
    DEFAULT_MEDIA_USAGE_OTHER_ID("defaultMediaUsageOtherId", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media use.", null, "7132f04f-92db-4571-9c98-f64f55390a55"),

    /**
     * Type of media.
     */
    MEDIA_TYPE("mediaType", DataType.STRING, MediaType.getOpenTypeName(), "Type of media.", null, "d6581642-3fbd-447c-a90b-95bfa770491e"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media type.
     */
    MEDIA_TYPE_OTHER_ID("mediaTypeOtherId", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media type.", null, "4a839c98-31ec-4c0d-88c1-206e1b13b7f6"),

    /**
     * Local identifier for the media.
     */
    MEDIA_ID("mediaId", DataType.STRING, DataType.STRING.getName(), "Local identifier for the media, from the perspective of the referencee.  For example. it may be the citation number in the list of references", "COLT-2", "1f738dc4-2df1-4177-919a-44b91b3ed22e"),

    /**
     * Identifier of the organization that this resource is from.
     */
    ORGANIZATION("organization", DataType.STRING, DataType.STRING.getName(), "Identifier of the organization that this resource is from.", null, "a837b6b5-ca30-4662-a643-5477d793bd29"),

    /**
     * Name of the property from the element used to identify the organization property.
     */
    ORGANIZATION_PROPERTY_NAME("organizationPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the organization property.", null, "6c59889d-c8b2-4154-812a-cdc6f27f5594"),

    /**
     * Identifier of the business capability where this asset originated from.
     */
    BUSINESS_CAPABILITY("businessCapability", DataType.STRING, DataType.STRING.getName(), "Identifier of the business capability where this resource originated from.", null, "586b18dd-e205-43bd-9fb2-3265384bd125"),

    /**
     * Type of business capability.
     */
    BUSINESS_CAPABILITY_TYPE("businessCapabilityType", DataType.STRING, BusinessCapabilityType.getOpenTypeName(), BusinessCapabilityType.getOpenTypeDescription(), BusinessCapabilityType.BUSINESS_AREA.getName(), BusinessCapabilityType.getOpenTypeDescriptionGUID()),

    /**
     * Implementation type for the business capability.
     */
    BUSINESS_IMPLEMENTATION_TYPE("businessImplementationType", DataType.STRING, DataType.STRING.getName(), "Implementation type for the business capability.", null, "43cd44fd-dbb4-464d-bd9f-4be0a369fc60"),

    /**
     * Name of the property from the element used to identify the businessCapability property.
     */
    BUSINESS_CAPABILITY_PROPERTY_NAME("businessCapabilityPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the businessCapability property.", null, "0dbfe481-106f-4092-8fca-11bf19895af9"),

    /**
     * Descriptive labels describing origin of the resource.
     */
    OTHER_ORIGIN_VALUES("otherOriginValues", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Descriptive labels describing origin of the resource.", null, "5982223c-72fb-4ceb-b060-298399d398d9"),

    /**
     * Network address used to connect to the endpoint.
     */
    NETWORK_ADDRESS("networkAddress", DataType.STRING, DataType.STRING.getName(), "Network address used to connect to the endpoint.", "https://localhost:9443", "0f38d466-e288-4971-bda8-1c5fde81bc82"),

    /**
     * Name of the protocol used to connect to the endpoint.
     */
    PROTOCOL("protocol", DataType.STRING, DataType.STRING.getName(), "Name of the protocol used to connect to the endpoint.", "HTTPS", "173d3d2f-c0c2-4341-9693-20e3bb6abb86"),

    /**
     * Type of encryption used at the endpoint (if any).
     */
    ENCRYPTION_METHOD("encryptionMethod", DataType.STRING, DataType.STRING.getName(), "Type of encryption used at the endpoint (if any).", null, "bc4a9ded-0414-4bbf-8cd2-c9ecc3e64ee9"),

    /**
     * Version of the database.
     */
    DATABASE_VERSION("databaseVersion", DataType.STRING, DataType.STRING.getName(), "Version of the database.", null, "f13db2cf-a0fc-4790-ba50-74ae5443bf36"),

    /**
     * Instance of the database.
     */
    INSTANCE ("databaseInstance", DataType.STRING, DataType.STRING.getName(), "Instance of the database.", null, "ecd5593a-647f-4767-b465-279e4bd35efc"),

    /**
     * importedFrom
     */
    IMPORTED_FROM("importedFrom", DataType.STRING, DataType.STRING.getName(), "System that the data was imported from.", null, "6e7b1e16-bcf4-4010-becd-244506f1d3ed"),

    /**
     * Name of the language used to implement this component.
     */
    IMPLEMENTATION_LANGUAGE("implementationLanguage", DataType.STRING, DataType.STRING.getName(), "Name of the language used to implement this component.", "Java", "e56dbaa5-6a2a-4aa0-8584-b3305234851f"),

    /**
     * Version of the property facet schema.
     */
    SCHEMA_VERSION("schemaVersion", DataType.STRING, DataType.STRING.getName(), "Version of the property facet schema.", null, "7eb8affc-c5d5-4ab2-9395-2ac8216be397"),

    /**
     * properties
     */
    PROPERTIES("properties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Property name-value pairs.", null, "6cf26b71-85db-41e0-9d7a-8e3fe956b649"),

    /**
     * Identifies which type of delete to use.
     */
    DELETE_METHOD("deleteMethod", DataType.STRING, DeleteMethod.getOpenTypeName(), "Identifies which type of delete to use.", "ARCHIVE", "0028d073-fe1f-482d-b546-28a77dd712e2"),

    /**
     * Geographical coordinates of this location.
     */
    COORDINATES("coordinates", DataType.STRING, DataType.STRING.getName(), "Geographical coordinates of this location.", null, "bfa8b230-f9ec-4105-a2fd-3159e9715043"),

    /**
     * The scheme used to define the meaning of the coordinates.
     */
    MAP_PROJECTION("mapProjection", DataType.STRING, DataType.STRING.getName(), "The scheme used to define the meaning of the coordinates.", null, "a4d71daa-6e87-4214-99e9-2db487ee6573"),

    /**
     * Postal address of the location.
     */
    POSTAL_ADDRESS("postalAddress", DataType.STRING, DataType.STRING.getName(), "Postal address of the location.", null, "a2743556-f1d6-473b-93bd-1615a0f937b8"),

    /**
     * Timezone for the location.
     */
    TIME_ZONE("timezone", DataType.STRING, DataType.STRING.getName(), "Timezone for the location.", null, "f163935d-78d1-41ab-b00f-b688e110fdc7"),

    /**
     * Level of security at this location.
     */
    LEVEL("level", DataType.STRING, DataType.STRING.getName(), "Level of security at this location.", null, "82e1a347-ce26-4b35-83ac-e310652572bf"),

    /**
     * Descriptive name of the association.
     */
    ASSOCIATION_NAME("associationName", DataType.STRING, DataType.STRING.getName(), "Descriptive name of the association.", null, "539b51a4-015d-4f3e-8389-6e73fcfb1555"),

    /**
     * Type of the association, such as 'containment', 'aggregation' or 'inheritance.'
     */
    ASSOCIATION_TYPE("associationType", DataType.STRING, DataType.STRING.getName(), "Type of the association, such as 'containment', 'aggregation' or 'inheritance.'", null, "6c2665d5-f202-49cb-8ce9-7333501e3072"),

    /**
     * Name of the operating system running on this operating platform.
     */
    OPERATING_SYSTEM("operatingSystem", DataType.STRING, DataType.STRING.getName(), "Name of the operating system running on this operating platform.", "macOS", "cbd3c5ec-0259-43cf-94b8-0339291e871b"),

    /**
     * Level of patches applied to the operating system.
     */
    OPERATING_SYSTEM_PATCH_LEVEL("operatingSystemPatchLevel", DataType.STRING, DataType.STRING.getName(), "Level of patches applied to the operating system.", null, "6fdda5ad-488a-477d-a1cc-33bb8267ef4b"),

    /**
     * The role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.
     */
    MEMBER_ROLE("memberRole", DataType.STRING, DataType.STRING.getName(), "The role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.", null, "9c143e06-6a8c-47ba-acf6-28a87fef4f2b"),

    /**
     * Preferred pronouns to use when addressing this person.
     */
    PRONOUNS("pronouns", DataType.STRING, DataType.STRING.getName(), "Preferred pronouns to use when addressing this person.", null, "8eaa378a-f50c-41da-bb1c-867251a63637"),

    /**
     * Identifier of the tenant.
     */
    TENANT_NAME("tenantName", DataType.STRING, DataType.STRING.getName(), "Identifier of the tenant.", null, "16745cc3-d657-4e99-a030-6c0023878bb1"),

    /**
     * Description of the type of tenant.
     */
    TENANT_TYPE("tenantType", DataType.STRING, DataType.STRING.getName(), "Description of the type of tenant.", null, "7b5805fe-397d-4171-96a3-f41072c6453b"),

    /**
     * Commercial name of the service.
     */
    OFFERING_NAME("offeringName", DataType.STRING, DataType.STRING.getName(), "Commercial name of the service.", null, "8294e9c1-2d8d-429f-8ac2-9f361551291c"),

    /**
     * Description of the type of the service.
     */
    SERVICE_TYPE("serviceType", DataType.STRING, DataType.STRING.getName(), "Description of the type of the service.", null, "7c05c0a8-c2c3-4043-af10-75858616482d"),

    /**
     * Name of the cloud provider.
     */
    PROVIDER_NAME("providerName", DataType.STRING, DataType.STRING.getName(), "Name of the cloud provider.", null, "2c1df3cd-a043-457d-8a41-72bec6c12855"),

    /**
     * The LDAP distinguished name (DN) that gives a unique positional name in the LDAP DIT.
     */
    DISTINGUISHED_NAME("distinguishedName", DataType.STRING, DataType.STRING.getName(), "The LDAP distinguished name (DN) that gives a unique positional name in the LDAP DIT.", null, "63d87f10-10c5-41b3-af11-17167e7c7269"),

    /**
     * The type name of the PersonRole that the UserIdentity is used for.
     */
    ROLE_TYPE_NAME("roleTypeName", DataType.STRING, DataType.STRING.getName(), "", null, "3216d40f-58e0-435c-90cd-980af2645ee7"),

    /**
     * The unique identifier of the specific PersonRole that the UserIdentity is used for.
     */
    ROLE_GUID("roleGUID", DataType.STRING, DataType.STRING.getName(), "The unique identifier of the specific PersonRole that the UserIdentity is used for.", null, "c2b71682-2d08-40b4-a250-96a99c6bc575"),

    /**
     * First letter of each of the person's given names.
     */
    INITIALS("initials", DataType.STRING, DataType.STRING.getName(), "First letter of each of the person's given names.", "ABC", "dfac6629-2c0b-436b-831b-d6a2ef1fed94"),

    /**
     * The name strings that are the part of a person's name that is not their surname.
     */
    GIVEN_NAMES("givenNames", DataType.STRING, DataType.STRING.getName(), "The names that are the part of a person's name that is not their surname.", null, "1f50956a-e042-44b0-b032-bc9d49feeb82"),

    /**
     * The family name of the person.
     */
    SURNAME("surname", DataType.STRING, DataType.STRING.getName(), "The family name of the person.", null, "4fa69f84-0e64-43f3-a5bd-76bbf6466e0a"),

    /**
     * Full or official/legal name of the individual (if different from known name).
     */
    FULL_NAME("fullName", DataType.STRING, DataType.STRING.getName(), "Full or official/legal name of the individual (if different from known name).", null, "d8d1460f-7b04-4427-b928-d39ff555ab6e"),

    /**
     * Spoken or written language preferred by the person.
     */
    PREFERRED_LANGUAGE("preferredLanguage", DataType.STRING, DataType.STRING.getName(), "Spoken or written language preferred by the person.", null, "caa48b00-1953-46f8-abda-54211f831d98"),

    /**
     * Country that is the person's primary residence.
     */
    RESIDENT_COUNTRY("residentCountry", DataType.STRING, DataType.STRING.getName(), "Country that is the person's primary residence.", "United Kingdom", "f4d113b2-fc77-48c9-a9b0-73cbc953e2ad"),

    /**
     * Principle role or level in the organization.
     */
    JOB_TITLE("jobTitle", DataType.STRING, DataType.STRING.getName(), "Principle role or level in the organization.", null, "b20696fe-3ff4-49e0-bdaf-b587abd8f0be"),

    /**
     * The unique identifier of a person used by their employer.
     */
    EMPLOYEE_NUMBER("employeeNumber", DataType.STRING, DataType.STRING.getName(), "The unique identifier of a person used by their employer.", null, "fc94b1a2-08c2-4078-b349-32904097ac6a"),

    /**
     * Code used by employer typically to describe the type of employment contract
     */
    EMPLOYEE_TYPE("employeeType", DataType.STRING, DataType.STRING.getName(), "Code used by employer typically to describe the type of employment contract.", null, "62ef771b-87b6-4e0f-adf2-3b471f0e7173"),

    /**
     * Points capturing a person's engagement with open metadata.
     */
    KARMA_POINTS("karmaPoints", DataType.LONG, DataType.LONG.getName(), "Points capturing a person's engagement with open metadata.", null, "ec847574-376b-4b21-a8a9-4bcdfcb2000d"),

    /**
     * Number of people that can be appointed to the role.
     */
    HEAD_COUNT("headCount", DataType.INT, DataType.INT.getName(), "Number of people that can be appointed to the role.", null, "58e9c9c4-d5e3-4576-a2a7-9f459bcdd439"),

    /**
     * What percentage of time is the appointee expected to devote to this role.
     */
    EXPECTED_TIME_ALLOCATION_PERCENT("expectedTimeAllocationPercent", DataType.INT, DataType.INT.getName(), "What percentage of time is the appointee expected to devote to this role.", "50", "027c580d-c5c9-4dda-a7d3-79a9951a85ad"),

    /**
     * Type of team, such as department.
     */
    TEAM_TYPE("teamType", DataType.STRING, DataType.STRING.getName(), "Type of team, such as division, or department.", null, "109d24af-b694-4f1e-90d2-ad3945596f2f"),

    /**
     * Can delegations and escalations flow on this relationship.
     */
    DELEGATION_ESCALATION("delegationEscalationAuthority", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Can delegations and escalations flow on this relationship.", null, "c8238d05-07ac-4c1e-a89c-34299d9827f8"),

    /**
     * What is the scope or nature of the assignment.
     */
    ASSIGNMENT_TYPE("assignmentType", DataType.STRING, DataType.STRING.getName(), "What is the scope or nature of the assignment.", null, "6d6af0b8-1b56-4941-a0c9-524331c46038"),

    /**
     * Breadth of responsibility or coverage.
     */
    SCOPE("scope", DataType.STRING, DataType.STRING.getName(), "Breadth of responsibility or coverage.", OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE, "033b4f92-fa46-4151-a095-c0bae938de8f"),

    /**
     * The value to use.
     */
    PREFERRED_VALUE("preferredValue", DataType.STRING, DataType.STRING.getName(), "The value to use for this field in data sets.", "Survey Resource", "b6258cbe-72f5-430b-ab62-6acb6c667e87"),

    /**
     * Descriptive name of the concept that this valid value applies to.
     */
    CATEGORY("category", DataType.STRING, DataType.STRING.getName(), "Descriptive name of the concept that this element applies to.", null, "385d7ca6-bcdd-43e4-925f-026a1043e37d"),

    /**
     * Is this valid value case-sensitive, or should the values match irrespective of case?
     */
    IS_CASE_SENSITIVE("isCaseSensitive", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is this valid value case-sensitive, or should the values match irrespective of case?", "true", "999d6f38-b244-44da-91f9-53694a25c174"),

    /**
     * "Reason for the meeting and intended outcome.
     */
    OBJECTIVE("objective", DataType.STRING, DataType.STRING.getName(), "Reason for the meeting and intended outcome.", null, "a0f9f9c5-ea35-458c-b969-1de5fb3116eb"),

    /**
     * Description of what happened at the meeting.
     */
    MINUTES("minutes", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Description of what happened at the meeting.", null, "9b65f72c-76c1-438c-a672-49039f2ee62d"),

    /**
     * Decisions made during the meeting.
     */
    DECISIONS("decisions", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Decisions made during the meeting.", null, "de498d70-59b4-4639-9069-5f41b40265fb"),

    /**
     * When the requested activity was documented.
     */
    REQUESTED_TIME("requestedTime", DataType.DATE, DataType.DATE.getName(), "When the requested action was documented.", null, "3b9586f3-3563-42cd-920b-11359d94e6ce"),

    /**
     * Relative importance of this governance definition compared to its peers.
     */
    IMPORTANCE("importance", DataType.STRING, DataType.STRING.getName(), "Relative importance of this definition compared to its peers.", null, "dfae7171-34a5-4460-bb61-4219454155db"),

    /**
     * Impact on the organization, people and services when adopting the recommendation in this governance definition.
     */
    IMPLICATIONS("implications", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Impact on the organization, people and services when adopting the recommendation in this governance definition.", null, "42d1d144-b81c-412e-bfc2-14ffbff089de"),

    /**
     * Expected outcomes.
     */
    OUTCOMES("outcomes", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Expected outcomes.", null, "15a6e909-00c9-464c-acf7-05c774b786b3"),

    /**
     * Actual results.
     */
    RESULTS("results", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Actual results.", null, "cb9ed4e0-bb5f-4ab9-95be-38c3d59517d8"),

    /**
     * The high-level goal of the activity.
     */
    MISSION("mission", DataType.STRING, DataType.STRING.getName(), "The high-level goal of the activity.", "To share new data science techniques.", "cb870681-de85-406b-bea6-6d138777cfe9"),

    /**
     * Private properties accessible only to the connector.
     */
    SECURED_PROPERTIES("securedProperties", DataType.MAP_STRING_OBJECT, DataType.MAP_STRING_OBJECT.getName(), "Private properties accessible only to the connector.", null, "b1b3e2dd-0ad1-4466-884b-697cf6333382"),

    /**
     * Specific configuration properties used to configure the behaviour of the connector.
     */
    CONFIGURATION_PROPERTIES("configurationProperties", DataType.MAP_STRING_OBJECT, DataType.MAP_STRING_OBJECT.getName(), "Specific configuration properties used to configure the behaviour of the connector.", null, "ce3009ac-54f3-4c6f-9658-27c2d2e75c3a"),

    /**
     * Password for the userId in clear text.
     */
    CLEAR_PASSWORD("clearPassword", DataType.STRING, DataType.STRING.getName(), "Password for the userId in clear text.", null, "4b2912cb-5a98-457a-853f-44a853d9a459"),

    /**
     * Encrypted password that the connector needs to decrypt before use.
     */
    ENCRYPTED_PASSWORD("encryptedPassword", DataType.STRING, DataType.STRING.getName(), "Encrypted password that the connector needs to decrypt before use.", null, "8a5c52ed-8b82-434e-8a94-2a772da8caa7"),

    /**
     * Name of the Java class that implements this connector type's open connector framework (OCF) connector provider.
     */
    CONNECTOR_PROVIDER_CLASS_NAME("connectorProviderClassName", DataType.STRING, DataType.STRING.getName(), "Name of the Java class that implements this connector type's open connector framework (OCF) connector provider.", null, "7c8b3671-f6f4-4156-845c-ff20ca627eac"),

    /**
     * List of additional connection property names supported by the connector implementation.
     */
    RECOGNIZED_ADDITIONAL_PROPERTIES("recognizedAdditionalProperties", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of additional connection property names supported by the connector implementation.", null, "93e83ce7-0255-43c0-b56f-a58835134106"),

    /**
     * List of secured connection property names supported by the connector implementation.
     */
    RECOGNIZED_SECURED_PROPERTIES("recognizedSecuredProperties", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of secured connection property names supported by the connector implementation.", null, "6cdf6c43-cf30-485f-b174-1864b4f785d4"),

    /**
     * List of configuration property names supported by the connector implementation.
     */
    RECOGNIZED_CONFIGURATION_PROPERTIES("recognizedConfigurationProperties", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of configuration property names supported by the connector implementation.", null, "750b88ff-c9b7-49cd-9c93-4f4c85d71e16"),

    /**
     * Type of asset supported by the connector implementation.
     */
    SUPPORTED_ASSET_TYPE_NAME("supportedAssetTypeName", DataType.STRING, DataType.STRING.getName(), "Type of asset supported by the connector implementation.", null, "c745e64a-a03e-4780-8a7f-46364a0d815a"),

    /**
     * Description of the format of the data expected by the connector implementation.
     */
    EXPECTED_DATA_FORMAT("expectedDataFormat", DataType.STRING, DataType.STRING.getName(), "Description of the format of the data expected by the connector implementation.", null, "870c0740-8797-4707-86d4-8d470c3143c5"),

    /**
     * Name of the framework that the connector implements. The default is 'Open Connector Framework (OCF)'
     */
    CONNECTOR_FRAMEWORK_NAME("connectorFrameworkName", DataType.STRING, DataType.STRING.getName(), "Name of the framework that the connector implements. The default is 'Open Connector Framework (OCF)'", null, "72d7f244-d71d-4efe-80d4-bd564a27ea18"),

    /**
     * The programming language used to implement the connector's interface.
     */
    CONNECTOR_INTERFACE_LANGUAGE("connectorInterfaceLanguage", DataType.STRING, DataType.STRING.getName(), "The programming language used to implement the connector's interface.", null, "e37051ed-baa0-46f4-a39c-cb6cbaf7a671"),

    /**
     * List of interfaces supported by the connector.
     */
    CONNECTOR_INTERFACES("connectorInterfaces", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of interfaces supported by the connector.", null, "3ee2f999-cbbc-43e9-82df-241641950fa6"),

    /**
     * Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation.
     */
    TARGET_TECHNOLOGY_SOURCE("targetTechnologySource", DataType.STRING, DataType.STRING.getName(), "Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation", null, "b8e9b52c-ea79-46b9-ad2e-514807115d99"),

    /**
     * Name of the technology that the connectors access. For example, Apache Kafka.
     */
    TARGET_TECHNOLOGY_NAME("targetTechnologyName", DataType.STRING, DataType.STRING.getName(), "Name of the technology that the connectors access. For example, Apache Kafka.", null, "a59e71d3-6a81-4f3d-98ee-d5d7bdadd351"),

    /**
     * Names of the technology's interfaces that the connectors use.
     */
    TARGET_TECHNOLOGY_INTERFACES("targetTechnologyInterfaces", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Names of the technology's interfaces that the connectors use.", null, "755d4353-7f33-44b2-805b-dd2c5ac2f916"),

    /**
     * List of versions of the technology that the connector implementation supports.
     */
    TARGET_TECHNOLOGY_VERSIONS("targetTechnologyVersions", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of versions of the technology that the connector implementation supports.", null, "48324db4-d4a6-46d7-8430-0dba4473f13b"),

    /**
     * Additional arguments needed by the virtual connector when using each connection.
     */
    ARGUMENTS("arguments", DataType.MAP_STRING_OBJECT, DataType.MAP_STRING_OBJECT.getName(), "Additional arguments needed by the virtual connector when using each connection.", null, "0ba7abef-1a62-4bf6-a21c-0239c3521d50"),

    /**
     * Character used between each column.
     */
    DELIMITER_CHARACTER("delimiterCharacter", DataType.STRING, DataType.STRING.getName(), "Character used between each column.", null, "9b6e9ae9-0251-41c8-a81c-0df3126453e8"),

    /**
     * The character used to group the content of the column that contains one or more delimiter characters.
     */
    QUOTE_CHARACTER("quoteCharacter", DataType.STRING, DataType.STRING.getName(), "The character used to group the content of the column that contains one or more delimiter characters.", null, "d23618c6-45a2-40a0-b34c-6385d252b5be"),

    /**
     * Format of the file system.
     */
    FORMAT("format", DataType.STRING, DataType.STRING.getName(), "Format of the file system.", null, "207a7ab2-e530-494f-a859-432d7fdb40fd"),

    /**
     * Level of encryption used on the filesystem (if any).
     */
    ENCRYPTION("encryption", DataType.STRING, DataType.STRING.getName(), "Level of encryption used on the filesystem (if any).", null, "011e4f70-b914-4dc8-9dc0-f259014e6f63"),

    /**
     * Display label to use when displaying this lineage relationship in a lineage graph.
     */
    LABEL("label", DataType.STRING, DataType.STRING.getName(), "Display label to use when displaying this lineage relationship in a lineage graph.", "provision data", "767aead2-5b37-4607-ba09-77d805e17d35"),

    /**
     * Location of the call in the implementation.
     */
    LINE_NUMBER("lineNumber", DataType.STRING, DataType.STRING.getName(), "Location of the call in the implementation.", "21", "942f07c9-54ba-44c4-8b9b-78753bc4fef2"),

    /**
     * Function that must be true to travel down this control flow.
     */
    GUARD( "guard", DataType.STRING, DataType.STRING.getName(), "Function, or value that must be true to travel down this control flow.", "x>4", "ca2d3fd9-3c6a-4771-9e9c-7bb623f62ba2"),

    /**
     * The number of hops along the lineage graph to the ultimate source organized by type of element.
     */
    HOPS("hops", DataType.MAP_STRING_INT, DataType.MAP_STRING_INT.getName(), "The number of hops along the lineage graph to the ultimate source organized by type of element.", null, "4bcc1f8e-ebef-4a6e-adbe-65d3932104e7"),

    /**
     * Root of topic names used by the Open Metadata Store.
     */
    TOPIC_ROOT("topicRoot", DataType.STRING, DataType.STRING.getName(), "Root of topic names used by the Open Metadata Access Services (OMASs).", null, "78db859f-2335-4fe4-8af5-442a641bfd66"),

    /**
     * Name of the topic(s) used to exchange registration, type definitions and metadata instances between the members of the open metadata repository cohort.
     */
    COHORT_TOPICS("cohortTopics", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Name of the topic(s) used to exchange registration, type definitions and metadata instances between the members of the open metadata repository cohort.", null, "2c1e2c5f-7f67-42b3-86d2-ad28de83a3fa"),

    /**
     * Unique identifier for the metadata collection accessed through this enterprise access layer.
     */
    ENTERPRISE_METADATA_COLLECTION_ID("enterpriseMetadataCollectionId", DataType.STRING, DataType.STRING.getName(), "Unique identifier for the metadata collection accessed through this enterprise access layer.", "40af7dbc-e9f4-46de-92a5-65e97fd8fd58", "40af7dbc-e9f4-46de-92a5-65e97fd8fd58"),

    /**
     * Unique identifier for a collection of metadata managed by a repository.
     */
    MANAGED_METADATA_COLLECTION_ID("managedMetadataCollectionId", DataType.STRING, DataType.STRING.getName(), "Unique identifier for a collection of metadata managed by a repository.", "844dbf4b-7446-4610-9777-698d05473feb", "844dbf4b-7446-4610-9777-698d05473feb"),

    /**
     * Date first registered with the cohort.
     */
    REGISTRATION_DATE("registrationDate", DataType.DATE, DataType.DATE.getName(), "Date first registered with the cohort.", null, "04bba48e-27f0-4c2e-af99-3208906a1c68"),

    /**
     * Version number of the protocol supported by the cohort registry.
     */
    PROTOCOL_VERSION("protocolVersion", DataType.STRING, DataType.STRING.getName(), "Version number of the protocol supported by the cohort registry.", null, "67fbbf35-ed67-4c50-b8d5-9eb03f6afbd5"),

    /**
     * Author of the resource.
     */
    AUTHOR("author", DataType.STRING, DataType.STRING.getName(), "Author of the resource.", null, "e121d939-cd79-49cb-aa3e-d05b99c64b8a"),

    /**
     * Report create time.
     */
    CREATED_TIME("createdTime", DataType.DATE, DataType.DATE.getName(), "Report create time.", null, "aa1b747d-77fd-46d6-bc4b-1e383fc41991"),

    /**
     * Report last modified time.
     */
    LAST_MODIFIED_TIME("lastModifiedTime", DataType.DATE, DataType.DATE.getName(), "Report last modified time.", null, "02d1f052-55ea-4b4c-845f-40d0ce5204e4"),

    /**
     * UserId of the report last modifier.
     */
    LAST_MODIFIER("lastModifier", DataType.STRING, DataType.STRING.getName(), "UserId of the report last modifier.", null, "22bab62c-e4bf-41fe-bf0c-8d11cb611119"),

    /**
     * The containment relationship between two processes: the parent and one of its children.
     */
    CONTAINMENT_TYPE("containmentType", DataType.STRING, ProcessContainmentType.getOpenTypeName(), ProcessContainmentType.getOpenTypeDescription(), ProcessContainmentType.OWNED.getName(), "563319e1-d17a-4b7b-ac78-393e5986bd38"),

    /**
     * Descriptor for a port that indicates the direction that data is flowing.
     */
    PORT_TYPE("portType", DataType.STRING, PortType.getOpenTypeName(), PortType.getOpenTypeDescription(), PortType.INOUT_PORT.getName(), "aa4efd6d-eba5-4d94-b728-6ec16ead4230"),

    /**
     * Natural language used in the glossary.
     */
    LANGUAGE("language", DataType.STRING, DataType.STRING.getName(), "Natural language used in the glossary.", "English", "0f6bde2f-0456-4c32-a1ef-5b7fbe9aafe3"),

    /**
     * Code for identifying the language - for example from ISO-639.
     */
    LANGUAGE_CODE("languageCode", DataType.STRING, DataType.STRING.getName(), "Code for identifying the language - for example from ISO-639.", null, "8a7bebe1-451a-4c28-bfde-5b37701e20b5"),

    /**
     * Locale for the translation.
     */
    LOCALE("locale", DataType.STRING, DataType.STRING.getName(), "Locale for the translation." , null, "576520c5-0ac8-48dd-882c-4cd692bc84df"),

    /**
     * Translations of other string properties found in the linked entity.
     */
    ADDITIONAL_TRANSLATIONS("additionalTranslations", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Translations of other string properties found in the linked entity.", null, "25cdcc9b-c133-42f2-9224-91347177f2c9"),

    /**
     * Characteristics that influence the organization of the taxonomy.
     */
    ORGANIZING_PRINCIPLE("organizingPrinciple", DataType.STRING, DataType.STRING.getName(), "Characteristics that influence the organization of the taxonomy.", "Alphabetical", "980250ab-c0e1-4cdb-851e-b6622d68fb41"),

    /**
     * Date when this reference was last checked.
     */
    LAST_VERIFIED("lastVerified", DataType.DATE, DataType.DATE.getName(), "Date when this reference was last checked.", null, "bc5280ed-bd1c-4874-870c-7bc4fefebad3"),

    /**
     * When does the entry refer to.
     */
    TIMELINE_ENTRY_DATE("timelineEntryDate", DataType.DATE, DataType.DATE.getName(), "When does the entry refer to.", null, "b04d9a3f-cdfe-44cf-91b3-504873af09f2"),

    /**
     * Describes the entry in the timeline.
     */
    TIMELINE_ENTRY_DESCRIPTION("timelineEntryDescription", DataType.DATE, DataType.DATE.getName(), "Describes the entry in the timeline.", null, "afc023fc-60ea-4ffb-bb2f-17612960e408"),

    /**
     * How severe is the impact on the resource?
     */
    SEVERITY_LEVEL_IDENTIFIER("severityLevelIdentifier", DataType.INT, DataType.INT.getName(), "How severe is the impact on the resource?", null, "2f549dab-9938-46fc-b35d-9773231bffd9"),

    /**
     * Describes the expected effects of the event on the organization.
     */
    EVENT_EFFECT("eventEffect", DataType.STRING, DataType.STRING.getName(), "Describes the expected effects of the event on the organization.", null, "6ce8112d-55da-44c0-a9de-668cfb67b368"),

    /**
     * Provides a planned date/time when the event should start.
     */
    PLANNED_START_DATE("plannedStartDate", DataType.DATE, DataType.DATE.getName(), "Provides a planned date/time when the event should start.", null, "43bdc270-4f2b-4fa9-a897-561940cd1d6a"),

    /**
     * Provides a definitive date/time when the event did start.
     */
    ACTUAL_START_DATE("actualStartDate", DataType.DATE, DataType.DATE.getName(), "Provides a definitive date/time when the event did start.", null, "0190f608-84f0-43d5-b0a6-18dac1eeaefd"),

    /**
     * Defines, in milliseconds, the length of time that the event is expected to last.
     */
    PLANNED_DURATION("plannedDuration", DataType.LONG, DataType.LONG.getName(), "Defines, in milliseconds, the length of time that the event is expected to last.", null, ""),

    /**
     * Defines, in milliseconds, the length of time that the event did last.
     */
    ACTUAL_DURATION("actualDuration", DataType.LONG, DataType.LONG.getName(), "Defines, in milliseconds, the length of time that the event did last.", null, "a68e8d73-e36c-47cb-946a-13f40233121d"),

    /**
     * Defines, in milliseconds, how frequently the event is expected to repeat.
     */
    REPEAT_INTERVAL("repeatInterval", DataType.LONG, DataType.LONG.getName(), "Defines, in milliseconds, how frequently the event is expected to repeat.", null, "815c6947-137f-4c6e-9459-b2179339acb3"),

    /**
     * Provides an expected date/time when the event is complete.
     */
    PLANNED_COMPLETION_DATE("plannedCompletionDate", DataType.DATE, DataType.DATE.getName(), "Provides an expected date/time when the event is complete.", null, "1643e955-e2d6-4ea0-871a-d1f351ee58eb"),

    /**
     * Provides a date/time when the event did complete.
     */
    ACTUAL_COMPLETION_DATE("actualCompletionDate", DataType.DATE, DataType.DATE.getName(), "Provides a date/time when the event did complete.", null, "f48ddf01-cafd-4c37-af5c-bc075c3649db"),

    /**
     * Provides a value to use in the starting effective dates for entities, relationships and classifications whose effectivity is triggered by this context event.
     */
    REFERENCE_EFFECTIVE_FROM("referenceEffectiveFrom", DataType.DATE, DataType.DATE.getName(), "Provides a value to use in the starting effective dates for entities, relationships and classifications whose effectivity is triggered by this context event.", null, "866f0c0b-ed24-4567-af7e-d1db6cd07691"),

    /**
     * Provides a value to use in the ending effective dates for entities, relationships and classifications whose effectivity is ended by this context event.
     */
    REFERENCE_EFFECTIVE_TO("referenceEffectiveTo", DataType.DATE, DataType.DATE.getName(), "Provides a value to use in the ending effective dates for entities, relationships and classifications whose effectivity is ended by this context event.", null, "0c05a464-34a3-4c4c-82fe-17259d3f4e9e"),

    /**
     * Map of template name to qualified name of parent element in associated catalog template.
     */
    TEMPLATES("templates", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Map of template name to qualified name of parent element in associated catalog template.", null, "b87eccc4-764b-40c3-aa4b-69d8c9aaed71"),

    /**
     * Name of connection to use to access the associated resource.  If this is null, the connection for the asset associated with the catalog target is used.  The asset may be the catalog target itself or the catalog target's anchor.
     */
    CONNECTION_NAME("connectionName", DataType.STRING, DataType.STRING.getName(), "Name of connection to use to access the associated resource.  If this is null, the connection for the asset associated with the catalog target is used.  The asset may be the catalog target itself or the catalog target's anchor.", null, "88bef8a2-a23a-433e-ad37-c4e640397ea8"),

    /**
     * Qualified name of a software server capability that is the owner/home of the metadata catalogued by the integration connector.
     */
    METADATA_SOURCE_QUALIFIED_NAME("metadataSourceQualifiedName", DataType.STRING, DataType.STRING.getName(), "Qualified name of a software server capability that is the owner/home of the metadata catalogued by the integration connector.", null, "5538fcb0-b3d9-4870-8d1f-1d2752c75255"),

    /**
     * If the data is bound by an area, this is the longitude for bottom-left corner of the bounding box (BBOX) for the area covered by the data.
     */
    MIN_LONGITUDE("minLongitude", DataType.FLOAT, DataType.FLOAT.getName(), "If the data is bound by an area, this is the longitude for bottom-left corner of the bounding box (BBOX) for the area covered by the data.", null, "9d1694e3-2197-49d4-b651-8d6ab0b8bc42"),

    /**
     * If the data is bound by an area, this is the latitude for the bottom-left corner of the bounding box (BBOX) for the area covered by the data.
     */
    MIN_LATITUDE("minLatitude", DataType.FLOAT, DataType.FLOAT.getName(), "If the data is bound by an area, this is the latitude for the bottom-left corner of the bounding box (BBOX) for the area covered by the data.", null, "ea7134ed-bb6d-4b1f-b938-48ae8f5686ac"),

    /**
     * If the data is bound by an area, this is the longitude for top-right corner of the bounding box (BBOX) for the area covered by the data.
     */
    MAX_LONGITUDE("maxLongitude", DataType.FLOAT, DataType.FLOAT.getName(), "If the data is bound by an area, this is the longitude for top-right corner of the bounding box (BBOX) for the area covered by the data.", null, "5e379349-632c-40c8-8125-694e339c475d"),

    /**
     * If the data is bound by an area, this is the latitude for top-right corner of the bounding box (BBOX) for the area covered by the data.
     */
    MAX_LATITUDE("maxLatitude", DataType.FLOAT, DataType.FLOAT.getName(), "If the data is bound by an area, this is the latitude for top-right corner of the bounding box (BBOX) for the area covered by the data.", null, "bce87e2b-603f-449a-9c90-15ca3d2bda81"),

    /**
     * If the height above ground is relevant, this is the lowest height that the data covers.
     */
    MIN_HEIGHT("minHeight", DataType.FLOAT, DataType.FLOAT.getName(), "If the height above ground is relevant, this is the lowest height that the data covers.", null, "33309620-ca7a-4aa3-a8d0-f98175e82672"),

    /**
     * If the height above ground is relevant, this is the highest height that the data covers.
     */
    MAX_HEIGHT("maxHeight", DataType.FLOAT, DataType.FLOAT.getName(), "", null, "cd188a84-17b7-410a-8aba-6195141268ec"),

    /**
     * If the data is bound by time, this is the start time.
     */
    DATA_COLLECTION_START_TIME("dataCollectionStartTime", DataType.DATE, DataType.DATE.getName(), "If the data is bound by time, this is the start time.", null, "f0e16621-0a71-40df-8679-0117c6ee2ecd"),

    /**
     * If the data is bound by time, this is the end time.
     */
    DATA_COLLECTION_END_TIME("dataCollectionEndTime", DataType.DATE, DataType.DATE.getName(), "If the data is bound by time, this is the end time.", null, "f216c3c4-247e-425b-9186-5c0d6a242d77"),


    /**
     * Labels that apply to the referenceable.
     */
    SECURITY_LABELS("securityLabels", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Labels that apply to the referenceable.", null, "28498a17-7d50-4d7c-b26d-3dbbebac614d"),

    /**
     * Properties that apply to the referenceable.
     */
    SECURITY_PROPERTIES("securityProperties", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Properties that apply to the referenceable.", null, "0d36a7b3-95f8-42bf-836b-4abd4a376ffc"),

    /**
     * Map of access groups.
     */
    ACCESS_GROUPS("accessGroups", DataType.MAP_STRING_ARRAY_STRING, DataType.MAP_STRING_ARRAY_STRING.getName(), "Map of access groups.", null, "1b1f3661-8630-488d-89cd-b9692fad160d"),

    /**
     * Description of how this governance control should be implemented.
     */
    IMPLEMENTATION_DESCRIPTION("implementationDescription", DataType.STRING, DataType.STRING.getName(), "Description of how this governance control should be implemented.", null, "cebf5cd8-60a2-402c-9216-b13ba16279d5"),

    /**
     * Name of the integration connector for logging purposes.
     */
    CONNECTOR_NAME("connectorName", DataType.STRING, DataType.STRING.getName(), "Name of the integration connector for logging purposes.", null, "8b854658-5de0-4218-ba7e-9b23ee40adda"),

    /**
     * Unique identifier of the integration connector deployment.
     */
    CONNECTOR_ID("connectorId", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the integration connector deployment.", null, "0f5ebc8d-9909-43ca-8148-d1b29f8de5f1"),

    /**
     * Name of the integration daemon where the integration connector is/was running.
     */
    SERVER_NAME("serverName", DataType.STRING, DataType.STRING.getName(), "Name of the integration daemon where the integration connector is/was running.", null, "a27047a9-5587-4bfd-b604-ddec605c03c6"),

    /**
     * Date/time when the connector's start() was called.
     */
    CONNECTOR_START_DATE("connectorStartDate", DataType.DATE, DataType.DATE.getName(), "Date/time when the connector's start() was called.", null, "0930c386-b447-484b-8470-7bcfad9c4ed3"),

    /**
     * Date/time when the refresh() call was made.
     */
    REFRESH_START_DATE("refreshStartDate", DataType.DATE, DataType.DATE.getName(), "Date/time when the connector's refresh() was called.", null, "4ef3b7e4-bfaa-4353-a451-7e999ec1d38c"),

    /**
     * Date/time when the connector returned from the refresh() call.
     */
    REFRESH_COMPLETION_DATE("refreshCompletionDate", DataType.DATE, DataType.DATE.getName(), "Date/time when the connector returned from the refresh() call.", null, "27a3a734-75e1-412b-8de4-9b8293234f1a"),


    /**
     * Date/time when the connector's disconnect() was called.
     */
    CONNECTOR_DISCONNECT_DATE("connectorDisconnectDate", DataType.DATE, DataType.DATE.getName(), "Date/time when the connector's disconnect() was called.", null, "ccebbf45-1750-4287-a040-6545e7d8c6de"),

    /**
     * List of elements that were created.
     */
    CREATED_ELEMENTS("createdElements", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of elements that were created.", null, "3aa69721-eced-45be-a0d9-939b28649e8e"),

    /**
     * List of elements that were updated.
     */
    UPDATED_ELEMENTS("updatedElements", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of elements that were updated.", null, "47fcc6f3-fe38-4517-bd71-ffbd96d95157"),

    /**
     * List of elements that were deleted.
     */
    DELETED_ELEMENTS("deletedElements", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of elements that were deleted.", null, "4a3aeffa-a747-407e-b03b-f4c5826026d9"),

    /**
     * Symbolic name of the catalog target to describe the remote system/service being connected to.
     */
    CATALOG_TARGET_NAME("catalogTargetName", DataType.STRING, DataType.STRING.getName(), "Symbolic name of the catalog target to describe the remote system/service being connected to.", null, "bd3579c1-46c8-4eac-92e1-351bc77000f5"),

    /**
     * UserId for the integration connector to use when working with open metadata.  The default userId comes from the hosting server if this value is blank.
     */
    CONNECTOR_USER_ID("connectorUserId", DataType.STRING, DataType.STRING.getName(), "UserId for the integration connector to use when working with open metadata.  The default userId comes from the hosting server if this value is blank.", null, "b0919bbe-e91e-4fc6-a9fe-80a674f7dbf7"),

    /**
     * Describes how frequently the integration connector should run - in minutes.
     */
    REFRESH_TIME_INTERVAL("refreshTimeInterval", DataType.LONG, DataType.LONG.getName(), "Describes how frequently the integration connector should run - in minutes.", null, "d261dc24-a26f-460f-808d-11b586c8ca7a"),

    /**
     * Latest time that the connector can run.
     */
    CONNECTOR_SHUTDOWN_DATE("connectorShutdownDate", DataType.DATE, DataType.DATE.getName(), "Latest time that the connector can run.", null, "ec798c83-660d-400e-8fa8-00dc8634fe24"),

    /**
     * Should the service/connector create integration reports based on its activity? (Default is true.)
     */
    GENERATE_CONNECTOR_ACTIVITY_REPORT("generateConnectorActivityReports", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Should the service/connector create integration reports based on its activity? (Default is true.)", null, "61f01f63-2e2d-47d0-9ea7-a95c0d561194"),

    /**
     * The name of the attribute that the reference data assignment represents.
     */
    ATTRIBUTE_NAME("attributeName", DataType.STRING, DataType.STRING.getName(), "The name of the attribute that the reference data assignment represents.", null, "be67948d-b0c2-457d-9897-41757cc2c6d1"),

    /**
     * The type of solution component - for example, is it a process, of file or database.
     */
    SOLUTION_COMPONENT_TYPE("solutionComponentType", DataType.STRING, DataType.STRING.getName(), "The type of solution component - for example, is it a process, of file or database.", "Automated Process", "2455eae0-911c-48a5-aa60-eb5e8c90fd22"),

    /**
     * The type of software component that is likely to serve as an implementation for this solution component.
     */
    PLANNED_DEPLOYED_IMPLEMENTATION_TYPE("plannedDeployedImplementationType", DataType.STRING, DataType.STRING.getName(), "The type of software component that is likely to serve as an implementation for this solution component.", "Governance Action Process", "ea0a0226-8a16-4f47-89e5-3aafdf544e8e"),

    /**
     * The reasons why some processing is occurring or some data is retained
     */
    PURPOSES("purposes", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "The reasons why some processing is occurring or some data is retained", null, "33abef8e-5a15-43ca-9d44-2c191792f8dd"),

    /**
     * Mechanism to flow data and control along the segment.
     */
    INTEGRATION_STYLE("integrationStyle",  DataType.STRING, DataType.STRING.getName(), "Mechanism to flow data and control along the segment.", null, "be8d6edd-d405-40b4-b270-c80d7dcf5a60"),

    /**
     * Properties that describe the expected volumes of data flowing through this segment.
     */
    ESTIMATED_VOLUMETRICS("estimatedVolumetrics", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Properties that describe the expected volumes of data flowing through this segment.", null, "4ef715ca-fa9b-49fe-9c8e-7dbc7769f9c0"),

    /**
     * Name used to identify a specific actor in the agreement.
     */
    ACTOR_NAME("actorName", DataType.STRING, DataType.STRING.getName(), "Name used to identify a specific actor in the agreement.", null, "4ec49806-b114-4580-9fac-dfdf26b62211"),

    /**
     * Process that created the refinement.
     */
    DESIGN_STEP("designStep", DataType.STRING, DataType.STRING.getName(), "Process that created the refinement.", null, "80574eeb-0213-45e3-b6ca-07504d126b00"),

    /**
     * Role that this artifact plays in implementing the abstract representation.
     */
    ROLE("role", DataType.STRING, DataType.STRING.getName(), "Role that this artifact plays in implementing the abstract representation.", null, "3c148d09-8383-4f64-8adc-57a03a45ac14"),

    /**
     * Transformation process used to create the refinement.
     */
    TRANSFORMATION("transformation", DataType.STRING, DataType.STRING.getName(), "Transformation process used to create the refinement.", null, "b2fa3b21-a298-4007-992f-07b3215ba698"),

   /**
     * Which way is data flowing?
     */
    DIRECTION("direction", DataType.STRING, SolutionPortDirection.getOpenTypeName(), "Which way is data flowing?", SolutionPortDirection.INPUT.getName(), "0c42037e-0e69-40dc-b8f8-d9ccd4a0d315"),

    /**
     * Root of the file path name that is due to the resource manager's view of the file system.
     */
    LOCAL_MOUNT_POINT("localMountPoint", DataType.STRING, DataType.STRING.getName(), "Root of the file path name that is due to the resource manager's view of the file system.", null, "e5fe3d87-0ff1-4435-aa06-7bf2570102dd"),

    /**
     * Value to replace the actual mount point with when storing/retrieving metadata about a file within the file system.
     */
    CANONICAL_MOUNT_POINT("canonicalMountPoint", DataType.STRING, DataType.STRING.getName(),"Value to replace the actual mount point with when storing/retrieving metadata about a file within the file system.", null, "fc93bccb-42e3-4611-86dd-191b051c75cc"),

    /**
     * Unique name for the element.
     */
    ISC_QUALIFIED_NAME("iscQualifiedName", DataType.STRING, DataType.STRING.getName(), "Unique name for the associated Information Supply Chain.", "InformationSupplyChain:Monthly Reporting", "705bec02-411a-4a92-9566-6a67bf5a612b"),

    /**
     * List of unique names of the information supply chains that this wire belongs to.
     */
    ISC_QUALIFIED_NAMES("iscQualifiedNames", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of unique names of the information supply chains that this wire belongs to.", null, "71916c51-d9f7-431b-b697-528f3ba2dc80"),

    /**
     * Placeholder for a query.
     */
    QUERY_ID("queryId", DataType.STRING, DataType.STRING.getName(), "Placeholder for a query.", null, "4a480cb0-b87d-47a5-8a68-660c2a144c9b"),

    /**
     * Query used to extract data, can include placeholders.
     */
    QUERY("query", DataType.STRING, DataType.STRING.getName(), "Query used to extract data, can include placeholders.", null, "62b8678e-a414-4bab-91c7-ca2f53397833"),

    /**
     * Type of query used to extract data.
     */
    QUERY_TYPE("queryType", DataType.STRING, DataType.STRING.getName(), "Type of query used to extract data.", "SQL", "58d5990e-8afa-49fd-8364-db53ccc1dfd8"),

    /**
     * Identifier of the governance domain that recognizes this process. Zero typically means 'any' domain.
     */
    DOMAIN_IDENTIFIER("domainIdentifier", DataType.INT, DataType.INT.getName(), "Identifier of the governance domain that recognizes this process. Zero typically means 'any' domain.", "0", "f76e01aa-6f89-4214-b79f-f2c2a87d261b"),

    /**
     * Format or description of the measurements captured for this metric.
     */
    MEASUREMENT("measurement", DataType.STRING, DataType.STRING.getName(), "Format or description of the measurements captured for this metric.", null, "1e1215b6-3d76-457a-b46f-d62655f8001c"),

    /**
     * Definition of the measurement values that the governance definitions are trying to achieve.
     */
    TARGET("target", DataType.STRING, DataType.STRING.getName(), "Definition of the measurement values that the governance definitions are trying to achieve.", null, "0798f3ef-ceda-46ff-9a8a-d3926dff7f36"),

    /**
     * Unique identifier of the automated process that processes this exception backlog.
     */
    PROCESS("process", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the automated process that processes this log.", null, "79e21ab2-959f-4e7d-9474-bec3261a376a"),

    /**
     * The minimum number of minutes that the governance engine should wait before calling the governance service.
     */
    WAIT_TIME("waitTime", DataType.INT, DataType.INT.getName(), "The minimum number of minutes that the governance engine should wait before calling the governance service.", "0", "fc0ed593-e4d5-465a-8e32-beeef0d7d736"),

    /**
     * List of guards that this action type produces.
     */
    PRODUCED_GUARDS("producedGuards", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of guards that this action type produces.", null, "2e9bb0fb-82af-48b2-a639-6665074f250e"),

    /**
     * Trigger one or many engine action instances within a process instance?
     */
    IGNORE_MULTIPLE_TRIGGERS("ignoreMultipleTriggers", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Trigger one or many engine action instances within a process instance?", "true", "2c31c693-e347-4369-9cdf-cf52a1af872e"),

    /**
     * Is this guard mandatory for the next step to run.
     */
    MANDATORY_GUARD("mandatoryGuard", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is this guard mandatory for the next step to run.", "true", "bd5acc27-7b8f-4460-b75c-94453a5d93dd"),

    /**
     * User identity of the governance engine running in the engine host server that has claimed responsibility for executing this engine action.
     */
    PROCESSING_ENGINE_USER_ID("processingEngineUserId", DataType.STRING, DataType.STRING.getName(), "User identity of the governance engine running in the engine host server that has claimed responsibility for executing this engine action.", null, "642a2571-bc8f-45f7-949a-72b0851e8ea2"),

    /**
     * List of guards returned by the governance service.
     */
    COMPLETION_GUARDS("completionGuards", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of guards returned by the governance service.", null, "8b106216-1b4d-4eca-8bb2-25c2dfe4c365"),

    /**
     * List of guards received from the previous governance service(s).
     */
    RECEIVED_GUARDS("receivedGuards", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of guards received from the previous governance service(s).", null, "6259d3fd-6d04-46b4-b844-0407675128c6"),

    /**
     * Goals or required outcomes from the business strategy that is supported by the data strategy.
     */
    BUSINESS_IMPERATIVES("businessImperatives", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Goals or required outcomes from the business strategy that is supported by the data strategy.", null, "962526a1-e16c-4604-8bf6-585b8943849c"),

    /**
     * Numeric value for the classification level.
     */
    LEVEL_IDENTIFIER("levelIdentifier", DataType.INT, DataType.INT.getName(), "Numeric value for the classification level.", null, "07962ad3-a239-4207-bdd1-0ddfa085e0b9"),

    /**
     * List of security group distinguished names.
     */
    GROUPS("groups", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "List of security group distinguished names.", null, "71adcf56-bf50-4cbe-abcc-fc9157a545f0"),

    /**
     * Descriptive type information about the policy management capability.
     */
    POINT_TYPE("pointType", DataType.STRING, DataType.STRING.getName(), "Descriptive type information about the the policy management capability.", null, "ab24e2bf-1ef8-4053-bb4d-9b4817bc095d"),

    /**
     * Identifier of the subject area.
     */
    SUBJECT_AREA_NAME("subjectAreaName", DataType.STRING, DataType.STRING.getName(), "Identifier of the subject area.", null, "ef1b567b-e079-4ef7-9a34-7c01e26a539e"),

    /**
     * A set of metric name to count value pairs.
     */
    COUNTS("counts", DataType.MAP_STRING_INT, DataType.MAP_STRING_INT.getName(), "A set of metric name to count value pairs.", null, "1ecedf35-5b23-47e0-b3c9-7ad0acbd6f3c"),

    /**
     * A set of metric name to string value pairs.
     */
    VALUES("values", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "A set of metric name to string value pairs.", null, "5e2444a0-2449-46e3-8949-95ce0eca2a79"),

    /**
     * A set of metric name to boolean value pairs.
     */
    FLAGS("flags", DataType.MAP_STRING_BOOLEAN, DataType.MAP_STRING_BOOLEAN.getName(), "A set of metric name to boolean value pairs.", null, "bbd22825-b6d7-4dc9-828c-5299d484ff55"),

    /**
     * A set of metric name to boolean value pairs.
     */
    DATES("dates", DataType.MAP_STRING_DATE, DataType.MAP_STRING_DATE.getName(), "A set of metric name to date value pairs.", null, "acb11838-9d81-4435-8831-86617e508bd6"),


    /**
     * Unique identifier of the actual certificate.
     */
    CERTIFICATE_GUID("certificateGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the actual certificate.", "2e0b760d-80fc-4e4e-a6bd-aa1169585533", "3ff7c882-7bfb-4e42-8cdc-a5c41e4db80b"),

    /**
     * Unique identifier of the actual license.
     */
    LICENSE_GUID("licenseGUID", DataType.STRING, DataType.STRING.getName(), "Unique identifier of the actual license.", "2e0b760d-80fc-4e4e-a6bd-aa1169585533", "2bd31f57-2984-4ca9-9dd9-2d86da804a75"),

    /**
     * Start date for the certification/license.
     */
    COVERAGE_START("coverageStart", DataType.DATE, DataType.DATE.getName(), "Start date for the certification/license.", null, "c9a4fdab-c44f-41d0-bce3-7c305bb13681"),

    /**
     * End date for the certification.
     */
    COVERAGE_END("coverageEnd", DataType.DATE, DataType.DATE.getName(), "End date for the certification/license.", null, "3933b3eb-a303-4818-8218-c7b0122393d8"),

    /**
     * Any special conditions or endorsements over the basic certification type.
     */
    CONDITIONS("conditions", DataType.STRING, DataType.STRING.getName(), "Any special conditions or endorsements over the basic certification/license type.", null, "df0f342a-c933-4bb3-8ad7-1721500db53b"),

    /**
     * Person or organization awarding the certification.
     */
    CERTIFIED_BY("certifiedBy", DataType.STRING, DataType.STRING.getName(), "Person or organization awarding the certification.", null, "64bf8d6e-a156-4e5b-a000-b414915ac3b8"),

    /**
     * Type of element referenced in the certifiedBy property.
     */
    CERTIFIED_BY_TYPE_NAME("certifiedByTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element referenced in the certifiedBy property.", null, "4218f738-e0bb-4afd-9f37-ab2d86a423e1"),

    /**
     * Name of the property from the element used to identify the certifiedBy property.
     */
    CERTIFIED_BY_PROPERTY_NAME("certifiedByPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the certifiedBy property.", null, "8851a966-9eac-4af6-bc01-daac185b981d"),

    /**
     * Person or organization awarding the license.
     */
    LICENSED_BY("licensedBy", DataType.STRING, DataType.STRING.getName(), "Person or organization awarding the license.", null, "351f42b6-7177-43f2-b48b-b640ff4d38bf"),

    /**
     * Type of element referenced in the licensedBy property.
     */
    LICENSED_BY_TYPE_NAME("licensedByTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element referenced in the licensedBy property.", null, "38a23a1a-be8f-4d2e-b9af-1636501d50af"),

    /**
     * Name of the property from the element used to identify the licensedBy property.
     */
    LICENSED_BY_PROPERTY_NAME("licensedByPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the licensedBy property.", null, "8e13071d-d8ca-4b12-8c56-3794117f0c4a"),

    /**
     * The person, engine or organization that will ensure the certification/license is honored.
     */
    CUSTODIAN("custodian", DataType.STRING, DataType.STRING.getName(), "The person, engine or organization that will ensure the certification/license is honored.", null, "f87be57e-f774-46f5-8f9c-e047e9a26023"),

    /**
     * Type of element referenced in the custodian property.
     */
    CUSTODIAN_TYPE_NAME("custodianTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element referenced in the custodian property.", null, "913794e9-d989-4274-81db-44fee1ac77a6"),

    /**
     * Name of the property from the element used to identify the custodian property.
     */
    CUSTODIAN_PROPERTY_NAME("custodianPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the custodian property.", null, "b2001ca5-04bf-465f-9974-921e51f5adb3"),

    /**
     * The person or organization that received the certification.
     */
    RECIPIENT("recipient", DataType.STRING, DataType.STRING.getName(), "The person or organization that received the certification.", null, "8c52edc2-f385-4cc8-bd1c-5c2c71023cf3"),

    /**
     * Type of element referenced in the recipient property.
     */
    RECIPIENT_TYPE_NAME("recipientTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element referenced in the recipient property.", null, "b932b5bc-add7-429f-baee-8e19245363dc"),

    /**
     * Name of the property from the element used to identify the recipient property.
     */
    RECIPIENT_PROPERTY_NAME("recipientPropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the recipient property.", null, "b6603182-95ce-4b9d-bdca-a4c483c3b755"),

    /**
     * The person or organization that received the license.
     */
    LICENSEE("licensee", DataType.STRING, DataType.STRING.getName(), "The person or organization that received the license.", null, "00a42e8b-208e-4dac-8e14-94e0194f9f04"),

    /**
     * Type of element referenced in the licensee property.
     */
    LICENSEE_TYPE_NAME("licenseeTypeName", DataType.STRING, DataType.STRING.getName(), "Type of element referenced in the licensee property.", null, "2be3b93f-471e-49a1-9879-e234a4015b0f"),

    /**
     * Name of the property from the element used to identify the licensee property.
     */
    LICENSEE_PROPERTY_NAME("licenseePropertyName", DataType.STRING, DataType.STRING.getName(), "Name of the property from the element used to identify the licensee property.", null, "ab10a18d-f449-4450-913b-e44055d5e3b8"),

    /**
     * The list of rights and permissions granted.
     */
    ENTITLEMENTS("entitlements", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "The list of rights and permissions granted.", null, "e7e05bae-2e26-4f5e-97a8-ae6f524b77b5"),

    /**
     * The list of limiting conditions or measures imposed.
     */
    RESTRICTIONS("restrictions", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "The list of limiting conditions or measures imposed.", null, "2119de4f-22ba-4979-b7ab-e926386b96da"),

    /**
     * The list of actions, duties or commitments required.
     */
    OBLIGATIONS("obligations", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "The list of actions, duties or commitments required.", null, "d5aa9324-70f9-40c1-9f3c-da28c8f7e6bf"),

    /**
     * Prefix for element names to ensure uniqueness.
     */
    NAMESPACE("namespace", DataType.STRING, DataType.STRING.getName(), "Prefix for element names to ensure uniqueness.", null, "00282ffd-950d-43d1-b9ee-d1b38c6ec49f"),

    /**
     * Format of the schema.
     */
    ENCODING_STANDARD("encodingStandard", DataType.STRING, DataType.STRING.getName(), "Format of the schema.", null, "ed168cda-302d-40fe-8170-32440db3c7ca"),

    /**
     * Concrete implementation of the definition of a schema type or data field.  It is used to guide developers or data definition/code generators.
     */
    SNIPPET("snippet", DataType.STRING, DataType.STRING.getName(), "Concrete implementation of the definition of a schema type or data field.  It is used to guide developers or data definition/code generators.", null, "4a375217-b571-41a8-8227-0f215d13c43b"),

    /**
     * Only values from the ValidValues set/definition are allowed.
     */
    STRICT_REQUIREMENT("strictRequirement", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Only values from the ValidValues set/definition are allowed.", null, "5d0ff357-906c-4483-8962-2df1bf978de4"),

    /**
     *  Is the member the default value in the set?
     */
    IS_DEFAULT_VALUE("isDefaultValue", DataType.BOOLEAN, DataType.BOOLEAN.getName(), "Is the member the default value in the set?", null, "b6c3a286-dc8a-4a2f-a7ab-d9117f53ec9e"),

    /**
     * Name of the valid value used in code.
     */
    SYMBOLIC_NAME("symbolicName", DataType.STRING, DataType.STRING.getName(), "Name of the valid value used in code.", null, "9909b6f9-00fc-4dba-a48c-225e5537ec55"),

    /**
     * Value in the asset that maps to this valid value if different from the preferred value.
     */
    IMPLEMENTATION_VALUE("implementationValue", DataType.STRING, DataType.STRING.getName(), "Value in the asset that maps to this valid value if different from the preferred value.", null, "faf3c645-db0a-4235-857e-7b02aa5394ea"),

    /**
     * Brief description describing how they are related.
     */
    ASSOCIATION_DESCRIPTION("associationDescription", DataType.STRING, DataType.STRING.getName(), "Brief description describing how they are related." , null, "61c19bbd-3603-498c-b03d-b9add5cdf2c4"),

    /**
     * Additional values for additional columns or fields in the reference data store.
     */
    ADDITIONAL_VALUES("additionalValues", DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(), "Additional values for additional columns or fields in the reference data store.", null, "59e020b4-a4f3-4a80-91e9-e08948a8578a"),

    /**
     * Method used to identify data class.
     */
    METHOD("method", DataType.STRING, DataType.STRING.getName(), "Method used to identify data class.", null, "116e6410-ced9-4825-8ec1-e06741304a98"),

    /**
     * What was the percentage of matching values used to determine that the data class matched.
     */
    THRESHOLD("threshold", DataType.INT, DataType.INT.getName(), "What was the percentage of matching values used to determine that the data class matched.", null, "af41954a-2c59-41f6-b70f-2dd55297a8f8"),

    /**
     * Description of the situation where this pattern may be useful.
     */
    CONTEXT("context", DataType.STRING, DataType.STRING.getName(), "Description of the situation where this pattern may be useful.", null, "08fc2def-98f5-4f32-bc4d-8b75fb044872"),

    /**
     * Description of the aspects of the situation that make the problem hard to solve.
     */
    FORCES("forces", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "Description of the aspects of the situation that make the problem hard to solve.", null, "26959b3a-50d7-4085-af0b-9bb40f35e69d"),

    /**
     * Description of the types of problem that this design pattern provides a solution to.
     */
    PROBLEM_STATEMENT("problemStatement", DataType.STRING, DataType.STRING.getName(), "Description of the types of problem that this design pattern provides a solution to.", null, "dc09958e-de1e-4340-88e4-e2b6fee777c0"),

    /**
     * One or more examples of the problem and its consequences.
     */
    PROBLEM_EXAMPLE("problemExample", DataType.STRING, DataType.STRING.getName(), "One or more examples of the problem and its consequences.", null, "42ca6fc4-6991-4b23-a88f-b2bd80265317"),

    /**
     * Description of how the solution works.
     */
    SOLUTION_DESCRIPTION("solutionDescription", DataType.STRING, DataType.STRING.getName(), "Description of how the solution works.", null, "d4927930-dd6f-4736-8b3c-7b53cbb2bbd4"),

    /**
     * Illustrations of how the solution resolves the problem examples.
     */
    SOLUTION_EXAMPLE("solutionExample", DataType.STRING, DataType.STRING.getName(), "Illustrations of how the solution resolves the problem examples.", null, "2afac311-4018-41bf-8881-bca9236f7a49"),

    /**
     * The positive outcomes from using this pattern.
     */
    BENEFITS("benefits", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "The positive outcomes from using this pattern.", null, "03a8bad1-e763-4473-9aa9-516806e8e901"),

    /**
     * The additional issues that need to be considered when using this pattern.
     */
    LIABILITIES("liabilities", DataType.ARRAY_STRING, DataType.ARRAY_STRING.getName(), "The additional issues that need to be considered when using this pattern.", null, "c3d38415-80a9-44b4-9d7c-c6316c23ab17"),

    /**
     * A string value that represents the content of the digital resource.
     */
    FINGERPRINT("fingerprint", DataType.STRING, DataType.STRING.getName(), "A string value that represents the content of the digital resource.", null, "9d9d4a02-e209-46f5-858f-8c08f21f829b"),

    /**
     * The algorithm use to generate the fingerprint.
     */
    FINGERPRINT_ALGORITHM("fingerprintAlgorithm", DataType.STRING, DataType.STRING.getName(), "The algorithm use to generate the fingerprint.", null, "99c237ac-e55c-424e-bc45-3f375e4c261d"),

    /**
     * An integer value that represents the content of the digital resource.
     */
    HASH("hash", DataType.LONG, DataType.LONG.getName(), "An long value that represents the content of the digital resource.", null, "7f62687d-482a-41a4-8f76-3ac2ec28f619"),

    /**
     * The algorithm use to generate the hash.
     */
    HASH_ALGORITHM("hashAlgorithm", DataType.STRING, DataType.STRING.getName(), "The algorithm use to generate the hash." , null, "d838386e-e1ad-4a9c-b33c-dde3240a7c08"),

    /**
     * Description of the background cause or activity.
     */
    BACKGROUND("background", DataType.STRING, DataType.STRING.getName(), "Description of the background cause or activity.", null, "5184178d-1d00-4868-81b0-3ef182b39952"),

    /**
     * Map of label to level indicator to provide customizable grouping of incidents.
     */
    INCIDENT_CLASSIFIERS("incidentClassifiers", DataType.MAP_STRING_INT, DataType.MAP_STRING_INT.getName(), "Map of label to level indicator to provide customizable grouping of incidents.", null, "ff752b21-761d-406c-9e3b-60d2439b6048"),

    /**
     * Collection of synchronization dates identified by a key.
     */
    SYNC_DATES_BY_KEY("syncDatesByKey", DataType.MAP_STRING_LONG, DataType.MAP_STRING_LONG.getName(), "Collection of synchronization dates identified by a key.", null, "9b17445c-9566-4be7-b2d9-b9a456d5224a"),

    /**
     * Unique identifier for the item within the agreement.
     */
    AGREEMENT_ITEM_ID("agreementItemId",
                      DataType.STRING, DataType.STRING.getName(),
                      "Unique identifier for the item within the agreement.",
                      null,
                      "82719ab9-02d6-41ba-8b2c-c48037ba8aa3"),

    /**
     * Date/time when this item becomes active in the agreement.
     */
    AGREEMENT_START("agreementStart",
                    DataType.DATE, DataType.DATE.getName(),
                    "Date/time when this item becomes active in the agreement.",
                    null,
                    "399af4a9-e2eb-41b3-ad1d-616ee682be0e"),

    /**
     * Date/time when this item becomes inactive in the agreement.
     */
    AGREEMENT_END("agreementEnd",
                  DataType.DATE, DataType.DATE.getName(),
                  "Date/time when this item becomes inactive in the agreement.",
                  null,
                  "2a0b81aa-3040-4b6d-8862-247250ca5663"),

    /**
     * Measurements of the actual use of this item under the agreement.
     */
    USAGE_MEASUREMENTS("usageMeasurements",
                       DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(),
                       "Measurements of the actual use of this item under the agreement.",
                       null,
                       "00e9915d-f462-418e-9791-7e9644490ae8"),

    /**
     * Identifier for the contract used in the agreement.
     */
    CONTRACT_ID("contractId",
                DataType.STRING, DataType.STRING.getName(),
                "Identifier for the contract used in the agreement.",
                null,
                "ae60af8d-50e7-4691-b1e5-babfbd59f6ec"),

    /**
     * Identifier of actor to contact with queries relating to the contract.
     */
    CONTRACT_LIAISON("contractLiaison",
                     DataType.STRING, DataType.STRING.getName(),
                     "Identifier of actor to contact with queries relating to the contract.",
                     null,
                     "27e37260-a3fe-48f8-87db-dcde916efb32"),

    /**
     * Type name of liaison actor element.
     */
    CONTRACT_LIAISON_TYPE_NAME("contractLiaisonTypeName",
                               DataType.STRING, DataType.STRING.getName(),
                               "Type name of liaison actor element.",
                               null,
                               "506713ed-c2cb-436a-a47f-c466e3bcda7e"),

    /**
     * The property from the actor element used as the identifier.
     */
    CONTRACT_LIAISON_PROPERTY_NAME("contractLiaisonPropertyName",
                                   DataType.STRING, DataType.STRING.getName(),
                                   "The property from the actor element used as the identifier.",
                                   null,
                                   "44159873-6a37-4764-b827-6cd581c63274"),

    /**
     * The type of library - may be a product name or open source project name.
     */
    LIBRARY_TYPE("libraryType",
                 DataType.STRING, DataType.STRING.getName(),
                 "The type of library - may be a product name or open source project name.",
                 null,
                 "f8084a40-dd0d-4167-b4eb-6d548edfc4d7"),

    /**
     * Level of support agreed for the subscriber.
     */
    SUPPORT_LEVEL("supportLevel",
                  DataType.STRING, DataType.STRING.getName(),
                  "Level of support agreed for the subscriber.",
                  null,
                  "93a526db-792d-4332-aae4-03f296a06380"),

    /**
     * Levels of service agreed with the subscriber.
     */
    SERVICE_LEVELS("serviceLevels",
                   DataType.MAP_STRING_STRING, DataType.MAP_STRING_STRING.getName(),
                   "Levels of service agreed with the subscriber.",
                   null,
                   "82fd9acd-7b75-49e5-838b-7a0776c4ff23"),

    /**
     * Unique identifier for the subscriber.
     */
    SUBSCRIBER_ID("subscriberId",
                  DataType.STRING, DataType.STRING.getName(),
                  "Unique identifier for the subscriber.  This is typically supplied by an external subscription manager.",
                  null,
                  "a457adf0-7034-4988-968a-dd537d0ded15"),

    /**
     * The activity/discovery of the producer that resulted in this action.
     */
    SITUATION("situation",
              DataType.STRING, DataType.STRING.getName(),
              "The activity/discovery of the producer that resulted in this action.",
              null,
              "6b354ca7-1a85-4cab-8b1e-0da5ad7f3724"),

    /**
     * The action that the person or automated process should perform.
     */
    EXPECTED_BEHAVIOUR("expectedBehaviour",
                       DataType.STRING, DataType.STRING.getName(),
                       "The action that the person or automated process should perform.",
                       null,
                       "0b015138-0870-4604-9df1-5bda47101d74"),

    /**
     * Expression used to filter data values passing through element.
     */
    FILTER_EXPRESSION("filterExpression",
                      DataType.STRING, DataType.STRING.getName(),
                      "Expression used to filter data values passing through element.",
                      null,
                      "67403964-08fc-4d7e-bf60-3e95a4fc15bb"),

    /**
     * The qualified name of the primary category of a GlossaryTerm.
     */
    CATEGORY_QUALIFIED_NAME("categoryQualifiedName",
                            DataType.STRING, DataType.STRING.getName(),
                            "The qualified name of the primary category of a GlossaryTerm.",
                            null,
                            "9a7df593-7057-4eed-aab1-868a3d2cfc28"),

    /**
     * Element in the metadata model that the attached element embodies.
     */
    METAMODEL_ELEMENT_GUID("metamodelElementGUID",
                           DataType.STRING, DataType.STRING.getName(),
                           "Element in the metadata model that the attached element embodies.",
                           null,
                           "67c764e9-07cd-4a85-8ce6-5fd4a2fcbf78"),

    /**
     * Name of the operation to that is controlled by the linked security group.
     */
    OPERATION_NAME("operationName",
                   DataType.STRING, DataType.STRING.getName(),
                   "Name of the operation to that is controlled by the linked security group.",
                   null,
                   "6bc26e25-c5c9-4552-9ab0-482cfa0317d9"),

    /**
     * Number of Kafka partitions.
     */
    PARTITIONS("partitions",
               DataType.INT, DataType.INT.getName(),
               "Number of Kafka partitions.",
               null,
               "af7a04a0-d5a5-4245-8c90-7280d1f6d71e"),

    /**
     * Number of Kafka replicas.
     */
    REPLICAS("replicas",
             DataType.INT, DataType.INT.getName(),
             "Number of Kafka replicas.",
             null,
             "f8739877-1c2c-461c-a8e4-cfef6a808317"),


    /**
     * Network address used by callers to the network gateway.
     */
    EXTERNAL_ENDPOINT_ADDRESS("externalEndpointAddress",
                              DataType.STRING, DataType.STRING.getName(),
                              "Network address used by callers to the network gateway.",
                              null,
                              "132a033f-0713-4e5b-9f8c-7d79fdab8e93"),

    /**
     * Network address that the network gateway maps the request to.
     */
    INTERNAL_ENDPOINT_ADDRESS("internalEndpointAddress",
                              DataType.STRING, DataType.STRING.getName(),
                              "Network address that the network gateway maps the request to.",
                              null,
                              "c7544b4b-b181-465e-a0ca-c090f3bfe212"),

    ;


    /**
     * Name
     */
    public final String name;

    /**
     * Data Type enum
     */
    public final DataType dataType;

    /**
     * Type
     */
    public final String type;

    /**
     * Description
     */
    public final String description;

    /**
     * Example of a value for this property.
     */
    public final String example;

    /**
     * Unique identifier of description valid value.
     */
    public final String descriptionGUID;


    /**
     * Constructor.
     *
     * @param name            property name
     * @param dataType        data type enum
     * @param type            property type
     * @param description     property description
     * @param example         an example of a value for this property
     * @param descriptionGUID unique identifier of the valid value describing this property
     */
    OpenMetadataProperty(String   name,
                         DataType dataType,
                         String   type,
                         String   description,
                         String   example,
                         String   descriptionGUID)
    {
        this.name            = name;
        this.dataType        = dataType;
        this.type            = type;
        this.description     = description;
        this.example         = example;
        this.descriptionGUID = descriptionGUID;
    }
}
