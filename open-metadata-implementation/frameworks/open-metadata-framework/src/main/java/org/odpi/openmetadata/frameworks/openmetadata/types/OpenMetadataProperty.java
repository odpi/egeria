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
    GUID("guid", DataType.STRING.getName(), "Unique identifier of an open metadata entity or relationship.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55"),

    /**
     * The unique identifier for the metadata collection that is the home for a metadata element.
     */
    METADATA_COLLECTION_ID("metadataCollectionId", DataType.STRING.getName(), "The unique identifier for the metadata collection that is the home for a metadata element.", "151b9d80-8417-41c4-8f04-3ab90a387196", "01d7e832-ef18-4451-8e4c-4ba972292a8e"),

    /**
     * The unique identifier for the metadata collection that is the home for a metadata element.
     */
    METADATA_COLLECTION_NAME("metadataCollectionName", DataType.STRING.getName(), "The unique name for the metadata collection that is the home for a metadata element.", "MyDataManagerForDatabase1", "25b43665-87e9-4637-9e6d-d0658ba26261"),

    /**
     * Category of metadata collection.
     */
    INSTANCE_PROVENANCE_TYPE("instanceProvenanceType", DataType.STRING.getName(), "Category of metadata collection.", "Local Cohort", "f12562a3-52b2-42d2-9126-011207d4af55"),

    /**
     * UserId that created this instance.
     */
    CREATED_BY("createdBy", DataType.STRING.getName(), "UserId that created this instance.", "peterprofile", "5c9c9c94-b738-4203-b592-527801877f07"),

    /**
     * UserId that updated this instance.
     */
    UPDATED_BY("updatedBy", DataType.STRING.getName(), "UserId that updated this instance.", "erinoverview", "6fcbf02d-3d3b-4739-9209-51a02779393b"),

    /**
     * The date/time that this instance should start to be used (null means it can be used from creationTime).
     */
    EFFECTIVE_FROM_TIME("effectiveFromTime", DataType.DATE.getName(), "The date/time that this instance should start to be used (null means it can be used from creationTime).", null, "dfffede5-d593-4f53-a7a1-bf7ddf095ee3"),

    /**
     * The date/time that this instance should no longer be used.
     */
    EFFECTIVE_TO_TIME("effectiveToTime", DataType.DATE.getName(), "The date/time that this instance should no longer be used.", null, "c1de164e-b9bf-4d13-bac5-573a5078ef10"),

    /**
     * Version is a monotonically increasing indicator of the order that updates have been made to the instance. It is used by the open metadata repositories to ensure updates to reference copies of the instance are applied in the right sequence. The home open metadata repository (where the create an all subsequent updates happen) maintains the version number.
     */
    VERSION("version", DataType.LONG.getName(), "Version is a monotonically increasing indicator of the order that updates have been made to the instance. It is used by the open metadata repositories to ensure updates to reference copies of the instance are applied in the right sequence. The home open metadata repository (where the create an all subsequent updates happen) maintains the version number.", "1", "62dfdb89-5e99-449c-aafe-202d7e527414"),

    /**
     * Status of this instance. Values from the Instance Status enum. (use only for ordering results),
     */
    CURRENT_STATUS("currentStatus", DataType.STRING.getName(), "Status of this instance. Values from the Instance Status enum.", "Active", "de25c3fb-5061-4f3e-bd7d-29c1cd2c112f"),

    /**
     * Name of an open metadata type. (Use only for ordering results).
     */
    OPEN_METADATA_TYPE_NAME("openMetadataTypeName", DataType.STRING.getName(), "Name of an open metadata type.", "Asset", "a7eabe8c-d0c6-4785-86b3-f2bc310ec712"),


    /* ======================================================
     * These values are attributes defined in the type system.
     */

    /**
     * Unique identifier of an open metadata entity, classification or relationship.
     */
    TYPE_NAME("typeName", DataType.STRING.getName(), "Unique name of an open metadata entity,classification or relationship.", "Asset", "7c5a7e83-2709-4789-b014-d23082a659bd"),

    /**
     * Time that an instance was created.
     */
    CREATE_TIME("createTime", DataType.DATE.getName(), "Time that an instance was created.", null, "433f573a-0044-4943-a8ee-78102ae2fb32"),

    /**
     * Last time that an instance was updated.
     */
    UPDATE_TIME("updateTime", DataType.DATE.getName(), "Last time that an instance was updated.", null, "6b94347b-f692-4c44-9c61-d050d9758c54"),

    /**
     * Timestamp when the archive occurred or was detected.
     */
    ARCHIVE_DATE("archiveDate", DataType.STRING.getName(), "Timestamp when the archive occurred or was detected.", null, "5235c1b1-fa03-4d17-80e2-7826242dfc75"),

    /**
     * Name of user that performed the archive - or detected the archive.
     */
    ARCHIVE_USER("archiveUser", DataType.STRING.getName(), "Name of user that performed the archive - or detected the archive.", null, "fe81b808-3d78-4e84-a1de-b0b273a89bec"),

    /**
     * Name of process that performed the archive - or detected the archive.
     */
    ARCHIVE_PROCESS("archiveProcess", DataType.STRING.getName(), "Name of process that performed the archive - or detected the archive.", null, "0aef1b3a-50dc-4a6c-981f-93bf8815e7f4"),

    /**
     * Name of service that created this classification.
     */
    ARCHIVE_SERVICE("archiveService", DataType.STRING.getName(), "Name of service that created this classification.", null, "c2170958-998e-4d8c-8e33-e91ef4872c8a"),

    /**
     * Name of method that created this classification.
     */
    ARCHIVE_METHOD("archiveMethod", DataType.STRING.getName(), "Name of method that created this classification.", null, "45d558ba-00b4-4a67-9226-590b100da7e9"),

    /**
     * Properties to locate the real-world counterpart in the archive.
     */
    ARCHIVE_PROPERTIES("archiveProperties", DataType.STRING.getName(), "Properties to locate the real-world counterpart in the archive.", null, "d149d2d8-f181-4c20-8d68-5460f54bece5"),

    /**
     * The unique identifier of the referenceable that this element is directly or indirectly anchored to.
     */
    ANCHOR_GUID("anchorGUID", DataType.STRING.getName(), "The unique identifier of the referenceable that this element is directly or indirectly anchored to.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "e7de4efc-6fa4-4942-97eb-e9b5be701875"),

    /**
     * Unique name of the type of the anchor.
     */
    ANCHOR_TYPE_NAME("anchorTypeName", DataType.STRING.getName(), "Unique name of the type of the anchor.", "Asset", "605c9dd5-fa79-4457-b299-9169c5567f97"),

    /**
     * Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.
     */
    ANCHOR_DOMAIN_NAME("anchorDomainName", DataType.STRING.getName(), "Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.", "Asset", "00ba532f-792f-4b78-8940-b5a9fd72f854"),

    /**
     * Unique name for the element.
     */
    QUALIFIED_NAME("qualifiedName", DataType.STRING.getName(), "Unique name for the element.", "SoftwareServer:MyAsset:MyAssetName", "e31e5b9b-0f96-42a9-8e67-0e3fc66ad305"),

    /**
     * Additional properties for the element.
     */
    ADDITIONAL_PROPERTIES("additionalProperties", DataType.MAP_STRING_STRING.getName(), "Additional properties for the element.", null, "534b5665-73d4-4bdc-b83b-1a8fed19dba3"),

    /**
     * Name that the element is known as - not necessarily unique.
     */
    NAME("name", DataType.STRING.getName(), "Short name that the element is known as - not necessarily unique.", "MyAssetName", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),

    /**
     * Full name that the element is known as in the owning deployed technology.
     */
    RESOURCE_NAME("resourceName", DataType.STRING.getName(), "Full name that the element is known as in the owning deployed technology.  This name is typically unique within the scope of the owing technology", "MyAssetName", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),


    /**
     * Display name of the element used for summary tables and titles
     */
    DISPLAY_NAME("displayName", DataType.STRING.getName(), "Display name of the element used for summary tables and titles.", "MyGlossaryTerm", "a42ceece-6cd8-4c71-9920-e3c96df3c8bb"),

    /**
     * Description of the element or associated resource in free-text.
     */
    DESCRIPTION("description", DataType.STRING.getName(), "Description of the element or associated resource in free-text.", null, "ee09b6f9-e15b-40fb-9799-ef3b98c1de2c"),

    /**
     * Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.
     */
    VERSION_IDENTIFIER("versionIdentifier", DataType.STRING.getName(), "Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.", "V1.0", "6e765a3f-04c2-4eca-bd2d-519cae777c03"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    DEPLOYED_IMPLEMENTATION_TYPE("deployedImplementationType", DataType.STRING.getName(), "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "PostgreSQL Database Server", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE("supportedDeployedImplementationType", DataType.STRING.getName(), "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "PostgreSQL Database Server", "32567628-f7e3-4a6d-92b9-8ed734bd3dc1"),

    /**
     * Identifier of the person or process who is accountable for the proper management of the element or related resource.
     */
    OWNER("owner", DataType.STRING.getName(), "Identifier of the person or process who is accountable for the proper management of the element or related resource.", null, "8b3e49d6-987c-4f2f-b624-ba91e5d28b33"),

    /**
     * Type of element that describes the owner.
     */
    OWNER_TYPE_NAME("ownerTypeName", DataType.STRING.getName(), "Type of element that describes the owner.", "PersonRole", "c0acfbc6-4250-437b-9aa3-ce3d5a3bd490"),

    /**
     * Name of the property from the element used to identify the owner.
     */
    OWNER_PROPERTY_NAME("ownerPropertyName", DataType.STRING.getName(), "Name of the property from the element used to identify the owner.", "qualifiedName", "9d1fdeb4-132a-4ec7-8312-dc9362e4f2f7"),

    /**
     * The list of governance zones that this asset belongs to.
     */
    ZONE_MEMBERSHIP("zoneMembership", DataType.ARRAY_STRING.getName(), "The list of governance zones that this asset belongs to.", null, "2af69520-6991-4097-aa94-543127b73066"),

    /**
     * Formula that describes the behaviour of the element.  May include placeholders for queryIds
     */
    FORMULA("formula", DataType.STRING.getName(), "Formula that describes the behaviour of the element.  May include placeholders for queryIds.", null, "0b35fe46-1c16-4af7-b7b1-ea6951718dc7"),

    /**
     * Format of the expression provided in the formula attribute.
     */
    FORMULA_TYPE("formulaType", DataType.STRING.getName(), "Format of the expression provided in the formula attribute.", "SQL", "dd9b24cb-0359-4915-8b6b-9eae251adc1c"),

    /**
     * Time that a transient process started.
     */
    PROCESS_START_TIME("processStartTime", DataType.DATE.getName(), "Time that a transient process started.", null, "f2c4180f-a803-42d6-9eb1-2bd29bd20d88"),

    /**
     * Time that a transient process ended.
     */
    PROCESS_END_TIME("processEndTime", DataType.DATE.getName(), "Time that a transient process ended.", null, "2179e4e3-b640-403a-a2bb-10da5c39acf6"),

    /**
     * Is this element visible to all, or only the author?
     */
    IS_PUBLIC("isPublic", DataType.BOOLEAN.getName(),  "Is this element visible to all, or only the author?", "true", "e7a8eaa8-4358-4dd3-8d82-2ab3c118cfe4"),

    /**
     * Rating level provided.
     */
    STARS("stars", StarRating.getOpenTypeName(), "Rating level provided.", "***", "896fd016-fad1-43dc-87a9-3ee577d8d898"),

    /**
     * Additional comments associated with the rating.
     */
    REVIEW("review", DataType.STRING.getName(), "Additional comments associated with the rating.", null, "ae1ef693-6264-44ee-8014-23a283ad3b0e"),

    /**
     * Feedback comments or additional information.
     */
    TEXT("text", DataType.STRING.getName(), "Feedback comments or additional information.", null, "baef44c7-e345-46ef-b8f7-a4d7fbcf77d7"),

    /**
     * Type of comment.
     */
    TYPE("type", CommentType.getOpenTypeName(), "Type of comment.", "Answer", "465c7ed7-74d4-4eb3-9e4c-9d47f06a0ebd"),

    /**
     * Role of contributor.
     */
    ROLE_TYPE("roleType", CrowdSourcingRole.getOpenTypeName(), "Role of contributor.", "Contributor", "05fdcacb-b7ec-475c-96eb-600fbbc1fb88"),

    /**
     * The courtesy title for the person.
     */
    COURTESY_TITLE("title", DataType.STRING.getName(), "The courtesy title for the person.", "Dr", "c669dc73-3ae3-4350-95b7-4508a85bfc94"),

    /**
     * Descriptive name of the tag.
     */
    TAG_NAME("tagName", DataType.STRING.getName(), "Descriptive name of the tag.", null, "c6054168-4236-4de1-9ece-ebcb5597dbf4"),

    /**
     * More detail on the meaning of the tag.
     */
    TAG_DESCRIPTION("tagDescription", DataType.STRING.getName(), "More detail on the meaning of the tag.", null, "e8fc2fc9-afae-47cd-b5f0-f7c374fb6ea4"),

    /**
     * Description of the technique used to create the sample.
     */
    SAMPLING_METHOD("samplingMethod", DataType.STRING.getName(), "Description of the technique used to create the sample.", null, "1cb00437-adde-4b71-a655-f736f2c8989d"),

    /**
     * The relationship of element that has been changed to the anchor.
     */
    CHANGE_TARGET("changeTarget", "LatestChangeTarget", "The relationship to the anchor that the changed element has.", null, "b3a4c3c0-f17f-4380-848a-6c6d985c2dd3"),

    /**
     * The type of change.
     */
    CHANGE_ACTION("changeAction", "LatestChangeAction", "The type of change.", null, "365c4667-b86a-445f-860b-dc35eac917f2"),

    /**
     * The name of the associated classification.
     */
    CLASSIFICATION_NAME("classificationName", DataType.STRING.getName(), "The name of the associated classification.", "Ownership", "ef2169c4-c8f3-48b0-9051-cfb2bbb1e5f2"),

    /**
     * Name of the property in the classification where this value is used.
     */
    CLASSIFICATION_PROPERTY_NAME("classificationPropertyName", DataType.STRING.getName(), "Name of the property in the classification where this value is used.", "owner", "5471ec69-a23d-4d89-b134-b30a8a01f435"),

    /**
     * If an attached entity or relationship to it changed, this is its unique identifier.
     */
    ATTACHMENT_GUID("attachmentGUID", DataType.STRING.getName(), "If an attached entity or relationship to it changed, this is its unique identifier.", null, "74d62753-fd42-47c5-a804-29334f394728"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name.
     */
    ATTACHMENT_TYPE("attachmentType", DataType.STRING.getName(), "If an attached entity or relationship to changed, this is its unique type name.", null, "b7f9f49a-e227-44d8-ae26-7e9c4f552379"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name of the relationship.
     */
    RELATIONSHIP_TYPE("relationshipType", DataType.STRING.getName(), "If an attached entity or relationship to changed, this is its unique type name of the relationship.", null, "afa67874-fa00-422e-8902-69e5e3cc9e43"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER("user", DataType.STRING.getName(), "The user identifier for the person/system executing the request.", null, "1afa1bb1-a9f0-4739-a45a-ff0d01b867f5"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER_ID("userId", DataType.STRING.getName(), "The user identifier for the person/system executing the request.", null, "c65a21dc-d1ae-4a8f-ba33-58ec401c1b42"),

    /**
     * The unique identifier of the engine action that initiated the governance service that created this element.
     */
    ENGINE_ACTION_GUID("engineActionGUID", DataType.STRING.getName(), "The unique identifier of the engine action that initiated the governance service that created this element.", null, "519cf063-9386-42e5-88dd-c5baab234df6"),

    /**
     * The unique identifier of the associated asset.
     */
    ASSET_GUID("assetGUID", DataType.STRING.getName(), "The unique identifier of the associated asset.", null, "fd4b6779-9582-4cc2-885e-72e44445ff04"),

    /**
     * Description of the activity.
     */
    ACTION_DESCRIPTION("actionDescription", DataType.STRING.getName(), "Description of the activity.", null, "2f0a4467-7b72-4dba-a345-d80959d8a3d5"),

    /**
     * The version number of the template element when the copy was created.
     */
    SOURCE_VERSION_NUMBER("sourceVersionNumber", DataType.LONG.getName(), "The version number of the template element when the copy was created.", null, "4729488f-2e42-4801-bc17-9dfad508c1fa"),

    /**
     * The fully qualified physical location of the data store.
     */
    PATH_NAME("pathName", DataType.STRING.getName(), "The fully qualified physical location of the data store.", null, "34d24b66-12f1-437c-afd3-1f1ab3377472"),


    /**
     * The url path segments that identify this API operation.
     */
    PATH("path", DataType.STRING.getName(), "The url path segments that identify this API operation.", "/assets", "7f8c1ed7-90ac-404d-a1ab-08934e423ac5"),

    /**
     * The url path segments that identify this API operation.
     */
    COMMAND("command", DataType.STRING.getName(), "The REST command for this API operation.", "POST", "cc4a83dd-4a11-4873-8a16-e7c9232ad4cf"),

    /**
     * File type descriptor (or logical file type) typically extracted from the file name.
     */
    FILE_TYPE("fileType", DataType.STRING.getName(), "File type descriptor (or logical file type) typically extracted from the file name.", null, "f180c49b-2de6-4657-bdf7-069747bc612d"),

    /**
     * The name of the file with extension.
     */
    FILE_NAME("fileName", DataType.STRING.getName(), "The name of the file with extension.", null, "f22d0164-3790-42c3-aacb-154ccee7fbb6"),

    /**
     * The file extension used at the end of the file's name.  This identifies the format of the file.
     */
    FILE_EXTENSION("fileExtension", DataType.STRING.getName(), "The file extension used at the end of the file's name.  This identifies the format of the file.", null, "62b00622-fa2c-4ee0-be60-3f15b1cb3dd6"),

    /**
     * The request type used to call the service.
     */
    REQUEST_TYPE("requestType", DataType.STRING.getName(), "The request type used to call the service.", null, "be22cf20-f704-459e-823f-bdd8f7ef003b"),

    /**
     * The request type used to call the service.
     */
    REQUESTER_USER_ID("requesterUserId", DataType.STRING.getName(), "User identity for the person, process or engine that requested this action.", null, "061de8f4-87a6-45b6-b91b-d375f202fe0f"),

    /**
     * Time that the ending action should start.  This is the request time plus any requested wait time.
     */
    REQUESTED_START_DATE("requestedStartDate", DataType.DATE.getName(), "Time that the ending action should start.  This is the request time plus any requested wait time.", null, "7bf281bf-e91c-41f5-883c-af0025d0ad66"),

    /**
     * Properties that configure the governance service for this type of request.
     */
    REQUEST_PARAMETERS("requestParameters", DataType.MAP_STRING_STRING.getName(), "Request parameters to pass to the governance service when called.", null, "882a5a30-3724-41ea-90ff-667cb7627bde"),

    /**
     * Lists the names of the request parameters to remove from the requestParameters supplied by the caller.
     */
    REQUEST_PARAMETER_FILTER("requestParameterFilter", DataType.ARRAY_STRING.getName(), "Lists the names of the request parameters to remove from the requestParameters supplied by the caller.", null, "9f7e13d1-ec51-4c95-9931-383b3ab9295b"),

    /**
     * Provides a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service.
     */
    REQUEST_PARAMETER_MAP("requestParameterMap", DataType.MAP_STRING_STRING.getName(), "Provides a translation map between the supplied names in the requestParameters and the names supported by the implementation of the governance service.", null, "64730138-399a-4f30-a0b8-1f6cc487cb53"),

    /**
     * Lists the names of the action targets to remove from the supplied action targets.
     */
    ACTION_TARGET_FILTER("actionTargetFilter", DataType.ARRAY_STRING.getName(), "Lists the names of the action targets to remove from the supplied action targets.", null, "91b5c627-2c3a-4598-af72-e1b52f1f03c5"),

    /**
     * Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.
     */
    ACTION_TARGET_MAP("actionTargetMap", DataType.MAP_STRING_STRING.getName(), "Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.", null, "b848b720-4171-4d28-9e4c-1d34f51aaf5f"),

    /**
     * Request type supported by the governance service (overrides requestType on call to governance service if specified)
     */
    SERVICE_REQUEST_TYPE("serviceRequestType", DataType.STRING.getName(), "Request type supported by the governance service (overrides requestType on call to governance service if specified).", null, "63238bb7-e935-4c38-8d0a-61917f01a31d"),

    /**
     * Unique name of the link type that connects the edge to the vertex.
     */
    LINK_TYPE_NAME("linkTypeName", DataType.STRING.getName(), "Unique name of the link type that connects the edge to the vertex.", null, "4f275bc0-3a33-4f6a-96ee-cd7bd13ba579"),

    /**
     * If the end of a relationship is significant, set to 1 or 2 to indicate the desired end; otherwise use 0.
     */
    RELATIONSHIP_END("relationshipEnd", DataType.INT.getName(), "If the end of a relationship is significant, set to 1 or 2 to indicated the desired end; otherwise use 0.", null, "8b53224f-e330-4ded-9d18-da6517094994"),

    /**
     * Display name for the relationship end.
     */
    RELATIONSHIP_END_NAME("relationshipEndName", DataType.STRING.getName(), "Display name for the relationship end.", null, "e98f95e3-316b-4200-b608-57d7836d8901"),

    /**
     * Open metadata type name for the associated schema type.
     */
    SCHEMA_TYPE_NAME("schemaTypeName", DataType.STRING.getName(), "Open metadata type name for the associated schema type.", null, "4948ea82-387c-4a82-99ad-3e94bb5253b7"),

    /**
     * The name of a primitive data type.
     */
    DATA_TYPE("dataType", DataType.STRING.getName(), "The name of a primitive data type.", null, "50e73f9f-10a0-4b41-9cb6-bf55630f3734"),

    /**
     * The default value of a primitive data type.
     */
    DEFAULT_VALUE("defaultValue", DataType.STRING.getName(), "The default value of a primitive data type.", null, "e2ac5648-054c-492f-9818-bb1c55554bd6"),

    /**
     * The value of a literal data type.
     */
    FIXED_VALUE("fixedValue", DataType.STRING.getName(), "The value of a literal data type.", null, "bd0b89c3-d865-4b62-bd86-a06b3fad08fb"),

    /**
     * Position of the element in a collection of relationships.
     */
    POSITION("position", DataType.INT.getName(), "Position of the element in a collection of relationships.", null, "2fd62293-99e3-48f9-825f-e9b22d8470ae"),

    /**
     * Minimum number of allowed instances.
     */
    MIN_CARDINALITY("minCardinality", DataType.INT.getName(), "Minimum number of allowed instances.", null, "d3e13cd5-414f-4c82-94b8-e61dad64f7c3"),

    /**
     * Maximum number of allowed instances.
     */
    MAX_CARDINALITY("maxCardinality", DataType.INT.getName(), "Maximum number of allowed instances.", null, "5caa1b1a-590f-4a2e-85ad-260b64f4bbc1"),

    /**
     * Provides additional reasons, or expectations from the results.
     */
    PURPOSE("purpose", DataType.STRING.getName(), "Provides additional reasons, or expectations from the results.  This is typically expressed in business terms", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Name of the type of annotation.
     */
    ANNOTATION_TYPE("annotationType", DataType.STRING.getName(), "Name of the type of annotation.", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Short description for summary tables.
     */
    SUMMARY("summary", DataType.STRING.getName(), "Short description for summary tables.", null, "30719554-1a97-424f-ac05-f4e4e67bd278"),

    /**
     * Level of certainty in the accuracy of the results.
     */
    CONFIDENCE_LEVEL("confidenceLevel", DataType.INT.getName(), "Level of certainty in the accuracy of the results.", null, "29372374-38bb-472d-9d6e-529b68aed1e0"),

    /**
     * Expression used to create the annotation.
     */
    EXPRESSION("expression", DataType.STRING.getName(), "Expression used to create the annotation.", null, "e130bc01-7c58-4799-8d8a-ae3ec659a064"),

    /**
     * Explanation of the analysis.
     */
    EXPLANATION("explanation", DataType.STRING.getName(), "Explanation of the analysis.", null, "9445c89d-250f-464c-bb9c-744f25b7b7e1"),

    /**
     * Additional request parameters passed to the service.
     */
    ANALYSIS_PARAMETERS("analysisParameters", DataType.MAP_STRING_STRING.getName(), "Additional request parameters passed to the service.", null, "b3eb6d7f-9c52-44f0-99c3-359f52218c7e"),

    /**
     * The step in the analysis that produced the annotation.
     */
    ANALYSIS_STEP("analysisStep", DataType.STRING.getName(), "The step in the analysis that produced the annotation.", null, "0b8d13b5-39eb-46d1-9ab0-1cc192697ff7"),

    /**
     * Additional properties used in the request.
     */
    JSON_PROPERTIES("jsonProperties", DataType.MAP_STRING_STRING.getName(), "Additional properties used in the specification.", null, "fe0c84c7-6f19-4c36-897f-9067447d0d9a"),

    /**
     * Date of the review.
     */
    REVIEW_DATE("reviewDate", DataType.DATE.getName(), "Date of the review.", null, "7c19aa3a-5fbe-4455-928d-3330b21b22dd"),

    /**
     * User identifier for the steward performing the review.
     */
    STEWARD("steward", DataType.STRING.getName(), "Unique identifier for the steward performing the action.", null, "6777fa1e-3289-4896-a032-1097b4ad78b2"),

    /**
     * Type name of the Actor entity identifying the steward.
     */
    STEWARD_TYPE_NAME("stewardTypeName", DataType.STRING.getName(), "Type name of the Actor entity identifying the steward.", "Person", "b4c4637d-b196-4268-86a2-c6daf444dd7d"),

    /**
     * Property name for the steward's unique identifier (typically guid or qualifiedName).
     */
    STEWARD_PROPERTY_NAME("stewardPropertyName", DataType.STRING.getName(), "Property name for the steward's unique identifier (typically guid or qualifiedName).", "guid", "b9e55ebe-43fb-4eb0-8485-6ef22e07f0a8"),

    /**
     * Notes on why decision were made relating to this element, and other useful information.
     */
    NOTES("notes", DataType.STRING.getName(), "Notes on why decision were made relating to this element, and other useful information.", null, "577281c3-82f8-4c1d-ad95-04ef5919e57c"),

    /**
     * Notes provided by the steward.
     */
    COMMENT("comment", DataType.STRING.getName(), "Notes provided by the steward.", null, "bb38c0ab-de6c-47d6-ae3a-d4848fd79c58"),

    /**
     * Status of the processing as a result of the annotation.
     */
    ANNOTATION_STATUS("annotationStatus", "AnnotationStatus", "Status of the processing as a result of the annotation.", null, "d36618bf-99fc-474f-a958-e8c64cd715ee"),

    /**
     * The status of the work on this element.
     */
    TO_DO_STATUS("toDoStatus", ToDoStatus.getOpenTypeName(), ToDoStatus.getOpenTypeDescription(), ToDoStatus.OPEN.getName(), "bcd570a4-03af-4569-ba34-72b823ba01c5"),

    /**
     * Date/time that work started on this element.
     */
    START_DATE("startDate", DataType.DATE.getName(), "Date/time that work started on this element.", null, "e3e374cc-0f9d-45f6-ae74-7d7a438b17bf"),

    /**
     * Date/time that work is expected to be complete for this element.
     */
    PLANNED_END_DATE("startDate", DataType.DATE.getName(), "Date/time that work is expected to be complete for this element.", null, "330ae312-1e88-4c7b-810e-4b4a50e540e8"),

    /**
     * Date/time that work stopped on this element.
     */
    COMPLETION_DATE("completionDate", DataType.DATE.getName(), "Date/time that work stopped on this element.", null, "28585eb7-ca9f-4149-b51f-ad29bbfe3f7c"),

    /**
     * The name to identify the action target to the actor that processes it.
     */
    ACTION_TARGET_NAME("actionTargetName", DataType.STRING.getName(), "The name to identify the action target to the actor that processes it.", null, "3a5d325f-267c-4821-beb2-2c59d89891ed"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLE("example", DataType.STRING.getName(), "An example of the described concept, element or value.", null, "b207bc5c-b5b5-43f1-94f8-41f4e2902ee5"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLES("examples", DataType.STRING.getName(), "Examples of this concept in use.", null, "29c50714-461f-4856-8fcd-30e5b9e7ebaf"),

    /**
     * How this concept name is abbreviated.
     */
    ABBREVIATION("abbreviation", DataType.STRING.getName(), "How this glossary term is abbreviated.", null, "0a1eae14-718b-416c-a7d8-04d6f194d7dc"),

    /**
     * Visible version identifier.
     */
    PUBLISH_VERSION_ID("publishVersionIdentifier", DataType.STRING.getName(), "Visible version identifier.", "V1.2", "cae42a1b-f752-4ca9-8d26-96829b2f591d"),

    /**
     * Is this value required?
     */
    REQUIRED("required", DataType.BOOLEAN.getName(), "Is this value required?", null, "fb810cde-18a7-46a0-88a3-6a0fb69b2286"),

    /**
     * Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.
     */
    COMPLETION_MESSAGE("completionMessage", DataType.STRING.getName(), "Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.", null, "f7633bda-9a90-4561-8d5a-356126a855ea"),

    /**
     * Name of the keyword.
     */
    KEYWORD("keyword", DataType.STRING.getName(), "Name of the keyword.", null, "b15f3d92-c4fb-44f6-a773-986b0c02906b"),

    /**
     * Unique name of the process that initiated this request (if applicable).
     */
    PROCESS_NAME("processName", DataType.STRING.getName(), "Unique name of the process that initiated this request (if applicable).", null, "9b3e9119-503e-42b4-b972-95169163539b"),

    /**
     * Unique identifier of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_GUID("processStepGUID", DataType.STRING.getName(), "Unique identifier of the governance action process step that initiated this request (if applicable).", null, "69f3ec60-338b-4af4-9ebf-4139a33850a4"),

    /**
     * Unique name of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_NAME("processStepName", DataType.STRING.getName(), "Unique name of the governance action process step that initiated this request (if applicable).", null, "5f851f84-c871-4ada-b8fa-629449f1ca7b"),

    /**
     * Unique identifier of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_GUID("governanceActionTypeGUID", DataType.STRING.getName(), "Unique identifier of the governance action type that initiated this request (if applicable).", null, "73c3d8bb-05ec-40eb-9ff0-31de48407c14"),

    /**
     * Unique name of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_NAME("governanceActionTypeName", DataType.STRING.getName(), "Unique name of the governance action type that initiated this request (if applicable).", null, "e1921f31-306c-4a11-b37c-67eaae2eefea"),

    /**
     * Display name for the discovered schema.
     */
    SCHEMA_NAME("schemaName", DataType.STRING.getName(), "Display name for the discovered schema.", null, "c6011a00-b9c0-45d9-a525-88014aa3546f"),

    /**
     * Type name for the discovered schema.
     */
    SCHEMA_TYPE("schemaType", DataType.STRING.getName(), "Type name for the discovered schema.", null, "6a68d174-fd29-49ef-aa4b-26205dbebb58"),

    /**
     * Length of the data field.
     */
    LENGTH("length", DataType.INT.getName(), "Length of the data field.", null, "35a17811-1635-4ab1-9a15-0f79bc771a27"),

    /**
     * Potential classification names and properties as JSON.
     */
    CANDIDATE_CLASSIFICATIONS("candidateClassifications", DataType.MAP_STRING_STRING.getName(), "Potential classification names and properties as JSON.", null, "3ea11266-368b-4c0d-ad4d-2943a14a38ad"),

    /**
     * Inferred data type based on the data values.
     */
    INFERRED_DATA_TYPE("inferredDataType", DataType.STRING.getName(), "Inferred data type based on the data values.", null, "27c3d24d-8ea9-4daf-ab82-c813b3dfa01a"),

    /**
     * Inferred data format based on the data values.
     */
    INFERRED_FORMAT("inferredFormat", DataType.STRING.getName(), "Inferred data format based on the data values.", null, "0198a639-1906-4377-b8ba-56cccbf88b3e"),

    /**
     * Inferred data field length based on the data values.
     */
    INFERRED_LENGTH("inferredLength", DataType.INT.getName(), "Inferred data field length based on the data values.", null, "e67b7353-20e0-46be-b912-e96192a200fc"),

    /**
     * Inferred precision of the data based on the data values.
     */
    INFERRED_PRECISION("inferredPrecision", DataType.INT.getName(), "Inferred precision of the data based on the data values.", null, "4ce73780-f940-4da6-bfcb-3b726ede2f7b"),

    /**
     * Inferred scale applied to the data based on the data values.
     */
    INFERRED_SCALE("inferredScale", DataType.INT.getName(), "Inferred scale applied to the data based on the data values.", null, "bd28ec07-01bd-4db4-9377-33b15097ded4"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_PROPERTY_NAMES("profilePropertyNames", DataType.ARRAY_STRING.getName(), "List of property names used in this annotation.", "[profileEndDate, profileCounts]", "210e2977-6803-4fce-93e8-15547168d459"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_START_DATE("profileStartDate", DataType.DATE.getName(), "Time at which the profiling started collecting data.", null, "6bee91ad-4eae-4292-ab28-ff1e5404e74d"),

    /**
     * Time at which the profiling stopped collecting data.
     */
    PROFILE_END_DATE("profileEndDate", DataType.DATE.getName(), "Time at which the profiling stopped collecting data.", null, "9f31776f-f567-40aa-92cd-b990131dbdb8"),

    /**
     * Additional profile properties discovered during the analysis.
     */
    PROFILE_PROPERTIES("profileProperties", DataType.MAP_STRING_STRING.getName(), "Additional profile properties discovered during the analysis.", null, "53f5e89d-6730-4ae5-bfbf-62d4512eff58"),

    /**
     * Additional flags (booleans) discovered during the analysis.
     */
    PROFILE_FLAGS("profileFlags", DataType.MAP_STRING_BOOLEAN.getName(), "Additional flags (booleans) discovered during the analysis.", null, "b7b28f24-5464-4cab-8c6b-10bc11ed6118"),

    /**
     * Relevant dates discovered during the analysis.
     */
    PROFILE_DATES("profileDates", DataType.MAP_STRING_DATE.getName(), "Relevant dates discovered during the analysis.", null, "7d700cc5-b56c-4aeb-8416-7cf8d17d16c2"),

    /**
     * Additional counts discovered during the analysis.
     */
    PROFILE_COUNTS("profileCounts", DataType.MAP_STRING_LONG.getName(), "Additional counts discovered during the analysis.", null, "6550a7b0-4ab2-4188-b32f-c1576f4bcedc"),

    /**
     * Additional large counts discovered during the analysis.
     */
    PROFILE_DOUBLES("profileDoubles", DataType.MAP_STRING_DOUBLE.getName(), "Additional large counts discovered during the analysis.", null, "e77933c9-64c6-476f-bd0f-e905838fb7fa"),

    /**
     * List of individual values in the data.
     */
    VALUE_LIST("valueList", DataType.ARRAY_STRING.getName(), "List of individual values in the data.", null, "6d67712d-d302-48b0-a05a-35d9d6da4380"),

    /**
     * Count of individual values in the data.
     */
    VALUE_COUNT("valueCount", DataType.MAP_STRING_INT.getName(), "Count of individual values in the data.", null, "544a4595-7d96-465f-acc9-1b1cf472c671"),

    /**
     * Lowest value in the data.
     */
    VALUE_RANGE_FROM("valueRangeFrom", DataType.STRING.getName(), "Lowest value in the data.", null, "8a5f6805-1014-4016-a94e-6d02236f6408"),

    /**
     * Highest value in the data.
     */
    VALUE_RANGE_TO("valueRangeTo", DataType.STRING.getName(), "Highest value in the data.", null, "623c5733-80b4-41be-a9cc-4823c5ce5cf7"),

    /**
     * Typical value in the data.
     */
    AVERAGE_VALUE("averageValue", DataType.STRING.getName(), "Typical value in the data.", null, "52ed8053-42b9-4d26-a1d5-c6aba915ccb1"),

    /**
     * List of possible matching data classes.
     */
    CANDIDATE_DATA_CLASS_GUIDS("candidateDataClassGUIDs", DataType.ARRAY_STRING.getName(), "List of possible matching data classes.", null, "e6d6f746-50a4-4e1c-b998-06de82f0a839"),

    /**
     * Number of values that match the data class specification.
     */
    MATCHING_VALUES("matchingValues", DataType.LONG.getName(), "Number of values that match the data class specification.", null, "96c33d36-cff9-455e-944f-38224eb7448b"),

    /**
     * Number of values that do not match the data class specification.
     */
    NON_MATCHING_VALUES("nonMatchingValues", DataType.LONG.getName(), "Number of values that do not match the data class specification.", null, "7bb6364d-486f-4005-97c9-941c7b7154d5"),

    /**
     * Suggested term based on the analysis.
     */
    INFORMAL_TERM("informalTerm", DataType.STRING.getName(), "Suggested term based on the analysis.", null, "3a13654b-f671-4928-990a-96cfe8d25422"),

    /**
     * List of potentially matching glossary terms.
     */
    CANDIDATE_GLOSSARY_TERM_GUIDS("candidateGlossaryTermGUIDs", DataType.ARRAY_STRING.getName(), "List of potentially matching glossary terms.", null, "8be05385-75fe-42e2-a87f-2adb47289395"),

    /**
     * Suggested category based on the analysis.
     */
    INFORMAL_CATEGORY("informalCategory", DataType.STRING.getName(), "Suggested category based on the analysis.", null, "15b33606-c660-43b8-b6fc-8b715aeac55f"),

    /**
     * List of potentially matching glossary categories.
     */
    CANDIDATE_GLOSSARY_CATEGORY_GUIDS("candidateGlossaryCategoryGUIDs", DataType.ARRAY_STRING.getName(), "List of potentially matching glossary categories.", null, "5b865d63-65b8-4d29-b382-e0490de041c9"),

    /**
     * Type of quality calculation.
     */
    QUALITY_DIMENSION("qualityDimension", DataType.STRING.getName(), "Type of quality calculation.", null, "a865a7d9-b18c-4aae-853b-c429bd5f8c32"),

    /**
     * Calculated quality value.
     */
    QUALITY_SCORE("qualityScore", DataType.INT.getName(), "Calculated quality value.", null, "cdbe5189-771d-42a5-917a-42923dedaf89"),

    /**
     * Type name of the potential relationship.
     */
    RELATIONSHIP_TYPE_NAME("relationshipTypeName", DataType.STRING.getName(), "Type name of the potential relationship.", null, "477c226d-ceb0-4f4a-87d2-f60632a9dbf2"),

    /**
     * Entity that should be linked to the asset being analyzed.
     */
    RELATED_ENTITY_GUID("relatedEntityGUID", DataType.STRING.getName(), "Entity that should be linked to the asset being analyzed.", null, "d399931b-29f4-4039-bee5-4a68c1b68d28"),

    /**
     * Properties to add to the relationship.
     */
    RELATIONSHIP_PROPERTIES("relationshipProperties", DataType.MAP_STRING_STRING.getName(), "Properties to add to the relationship.", null, "d7cfc6d9-1b1a-4266-875a-b9f713d80643"),

    /**
     * Discovered properties of the data source.
     */
    RESOURCE_PROPERTIES("resourceProperties", DataType.MAP_STRING_STRING.getName(), "Discovered properties of the resource.", null, "c187fe2c-cee8-4c4b-99e7-dcb27032a88d"),

    /**
     * Name of the activity that revealed the need for action.
     */
    ACTION_SOURCE_NAME("actionSourceName", DataType.STRING.getName(), "Name of the activity that revealed the need for action.", null, "dddf1c9e-d0d6-4812-a757-1d01c79f34ef"),

    /**
     * What needs to be done.
     */
    ACTION_REQUESTED("actionRequested", DataType.STRING.getName(), "What needs to be done.", null, "3eca27fe-0538-43f1-83b9-cf9ee43a32ec"),

    /**
     * Additional information for use during action processing.
     */
    ACTION_PROPERTIES("actionProperties", DataType.MAP_STRING_STRING.getName(), "Additional information for use during action processing.", null, "b2302d78-f5e2-4fd7-8932-ad10ac868ab9"),

    /**
     * Creation time of the data store.
     */
    RESOURCE_CREATE_TIME("resourceCreateTime", DataType.DATE.getName(), "Creation time of the data store.", null, "766d9f17-40f9-44da-a0c8-679002a0cf91"),

    /**
     * Last known modification time.
     */
    RESOURCE_UPDATE_TIME("resourceUpdateTime", DataType.DATE.getName(), "Last known modification time.", null, "6012bdee-31d7-46d4-9f3b-f4d19c47e662"),

    /**
     * Last known modification time.
     */
    RESOURCE_LAST_ACCESSED_TIME("resourceLastAccessedTime", DataType.DATE.getName(), "Last known access time.", null, "f884c0bf-f2d1-4cdb-81ff-0fc7cc7c945c"),

    /**
     * Creation time of the data store.
     */
    STORE_CREATE_TIME("storeCreateTime", DataType.DATE.getName(), "Creation time of the data store.", null, "1a3abee2-f174-433d-8355-44c5810c471b"),

    /**
     * Last known modification time.
     */
    STORE_UPDATE_TIME("storeUpdateTime", DataType.DATE.getName(), "Last known modification time.", null, "134bbbc3-4b68-4d35-a8da-85719cced8a9"),


    /**
     * Size of the data source.
     */
    SIZE("size", DataType.INT.getName(), "Size of the data source.", null, "c2d67f78-3387-4b83-9c1c-bd0aa88a4df0"),

    /**
     * Encoding scheme used on the data.
     */
    ENCODING("encoding", DataType.STRING.getName(), "Type of encoding scheme used on the data.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Language used in the encoding.
     */
    ENCODING_LANGUAGE("encodingLanguage", DataType.STRING.getName(), "Language used in the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Description of the encoding.
     */
    ENCODING_DESCRIPTION("encodingDescription", DataType.STRING.getName(), "Description of the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Additional properties describing the encoding.
     */
    ENCODING_PROPERTIES("encodingProperties", DataType.MAP_STRING_STRING.getName(), "Additional properties describing the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Identifier that describes the type of resource use.
     */
    RESOURCE_USE("resourceUse", DataType.STRING.getName(), "Identifier that describes the type of resource use.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Description of how the resource is used, or why it is useful.
     */
    RESOURCE_USE_DESCRIPTION("resourceUseDescription", DataType.STRING.getName(), "Description of how the resource is used, or why it is useful.", null, "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * Additional properties that explains how to use the resource.
     */
    RESOURCE_USE_PROPERTIES("resourceUseProperties", DataType.MAP_STRING_STRING.getName(), "Additional properties that explains how to use the resource.", null, "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Indicator whether the anchor should receive notifications of changes to the resource.
     */
    WATCH_RESOURCE("watchResource", DataType.BOOLEAN.getName(), "Indicator whether the anchor should receive notifications of changes to the resource.", null, "d727b3ce-d58b-45d5-8abc-55b1394e030a"),

    /**
     * "Time that the IT Infrastructure was deployed."
     */
    DEPLOYMENT_TIME( "deploymentTime", DataType.DATE.getName(), "Time that the IT Infrastructure was deployed.", null, "aa372f7f-8284-4e24-902e-9f01dcc70b3e"),

    /**
     * Type name of deployer.
     */
    DEPLOYER_TYPE_NAME( "deployerTypeName", DataType.STRING.getName(), "Type name of deployer.", null, "cb027494-8de7-43cc-845c-57d4f0bbf6d5"),

    /**
     * Identifying property name of deployer.
     */
    DEPLOYER_PROPERTY_NAME( "deployerPropertyName", DataType.STRING.getName(), "Identifying property name of deployer.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Person, organization or engine that deployed the IT Infrastructure.
     */
    DEPLOYER("deployer", DataType.STRING.getName(), "Person, organization or engine that deployed the IT Infrastructure.", null, "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * The operational status of the software server capability on this software server.
     */
    OPERATIONAL_STATUS("operationalStatus", "OperationalStatus", "The operational status of the deployed infrastructure.", null, "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Type of the software capability.
     */
    CAPABILITY_TYPE("capabilityType", DataType.STRING.getName(), "Type of the software capability.", null, "c8ef1b3a-63bf-4ca1-9ef8-475e2ad67a2b"),

    /**
     * Version number of the software capability.
     */
    CAPABILITY_VERSION("capabilityVersion", DataType.STRING.getName(), "Version number of the software capability.", "V1.0", "7548df50-1553-46e1-a335-b782db998f88"),

    /**
     * Patch level of the software server capability.
     */
    PATCH_LEVEL("patchLevel", DataType.STRING.getName(), "Patch level of the software server capability.", "456", "574bea24-8dd6-407a-bed4-aaa432d10276"),

    /**
     * Describes how the software capability uses the asset.
     */
    USE_TYPE("useType", "ServerAssetUseType", "Describes how the software capability uses the asset.", "OWNS", "a8cfffa4-a761-4fe0-be8b-6be43ac55020"),

    /**
     * Maximum number of running asset instances controlled by the software capability.
     */
    MAXIMUM_INSTANCES("maximumInstances", "integer", "Maximum number of running asset instances controlled by the software capability.", "0", "76b06411-e3bf-4a39-9351-140e6ad0b82f"),

    /**
     * Minimum number of running asset instances controlled by the software capability.
     */
    MINIMUM_INSTANCES("minimumInstances", "integer", "Minimum number of running asset instances controlled by the software capability.", "12", "d5033e20-3cd7-4c27-a07f-4a95feee10d8"),

    /**
     * Identifier used in an external system.
     */
    IDENTIFIER("identifier", DataType.STRING.getName(), "Identifier used in an external system.", null, "2f831c53-0c71-4e03-92ed-20a899ed771f"),

    /**
     * The username of the person or process that created the instance in the external system.
     */
    EXT_INSTANCE_CREATED_BY("externalInstanceCreatedBy", DataType.STRING.getName(), "The username of the person or process that created the instance in the external system.", null, "480a57d1-7300-4691-ad8b-540d97ef0870"),

    /**
     * The date/time when the instance in the external system was created.
     */
    EXT_INSTANCE_CREATION_TIME("externalInstanceCreationTime", DataType.DATE.getName(), "The date/time when the instance in the external system was created.", null, "50e22b77-6091-4316-836e-2f76468add71"),

    /**
     * The username of the person or process that last updated the instance in the external system.
     */
    EXT_INSTANCE_LAST_UPDATED_BY("externalInstanceLastUpdatedBy", DataType.STRING.getName(), "The username of the person or process that last updated the instance in the external system.", null, "f29f9183-e651-42d5-b0c8-bb1491f3151e"),

    /**
     * The date/time when the instance in the external system was last updated.
     */
    EXT_INSTANCE_LAST_UPDATE_TIME("externalInstanceLastUpdateTime", DataType.DATE.getName(), "The date/time when the instance in the external system was last updated.", null, "0502401e-5292-4860-9fd7-dd2b0c46859d"),

    /**
     * The latest version of the element in the external system.
     */
    EXT_INSTANCE_VERSION("externalInstanceVersion" , DataType.LONG.getName(), "The latest version of the element in the external system.", null, "349199e2-5781-4413-8550-c85241e20cc5"),

    /**
     * Additional properties to aid the mapping to the element in an external metadata source.
     */
    MAPPING_PROPERTIES("mappingProperties", DataType.MAP_STRING_STRING.getName(), "Additional properties to aid the mapping to the the element in an external metadata source.", null, "8161e120-993b-490c-bf66-cb9fd85192fc"),

    /**
     * Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.
     */
    LAST_SYNCHRONIZED("lastSynchronized", DataType.DATE.getName(), "Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.", null, "45f5f2fc-17ec-4f02-8d7a-8cfe8f1557fe"),

    /**
     * Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.
     */
    PERMITTED_SYNCHRONIZATION("permittedSynchronization", "PermittedSynchronization", "Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.", null, "45f5f2fc-17ec-4f02-8d7a-8cfe8f1557fe"),

    /**
     * Type of identifier that identifies its lifecycle, for example, its scope and whether the value is reused.
     */
    KEY_PATTERN("keyPattern", "KeyPattern", "Type of identifier that identifies its lifecycle, for example, its scope and whether the value is reused.", null, "a8805753-865d-4860-ab95-1e83c3eaf01d"),

    /**
     * Guidance on how the element should be used.
     */
    USAGE("usage", DataType.STRING.getName(), "Guidance on how the element should be used.", null, "e92f8669-5a07-4130-9ad6-62aadca7a505"),

    /**
     * Details of where the element was sourced from.
     */
    SOURCE("source", DataType.STRING.getName(), "Details of the organization, person or process that created the element, or provided the information used to create the element.", null, "9c40c4e3-1d6d-45fd-8df0-f1a2e09db636"),

    /**
     * Deployed version number for this platform.
     */
    PLATFORM_VERSION("platformVersion", DataType.STRING.getName(), "Deployed version number for this platform.", null, "03b0bab5-2a86-4720-8ab3-5b0d75c743dd"),


    /**
     * Deployed version number for this server.
     */
    SERVER_VERSION("serverVersion", DataType.STRING.getName(), "Deployed version number for this server.", null, "c762845d-f333-43b3-bd98-8780bc979167"),

    /**
     * Level of confidence in the correctness of the element.
     */
    CONFIDENCE("confidence", DataType.INT.getName(), "Level of confidence in the correctness of the element. 0=unknown; 1=low confidence; 100=total confidence.", "100", "26dd007a-cff3-45e7-963d-2a753c2b7000"),

    /**
     * The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.
     */
    USES_BLOCKING_CALLS("usesBlockingCalls", DataType.BOOLEAN.getName(), "The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.", "false", "cd23ea21-75b0-45d2-9292-e63510c3a1e2"),

    /**
     * Full name of the topic as used by programs to access its contents
     */
    TOPIC_NAME("topicName", DataType.STRING.getName(), "Full name of the topic as used by programs to access its contents.", "egeria.omag.server.active-metadata-store.omas.assetconsumer.outTopic", "eda530d2-62d3-4325-9840-514f001ffc12"),

    /**
     * Type of topic.
     */
    TOPIC_TYPE("topicType", DataType.STRING.getName(), "Type of topic.", "PLAINTEXT", "17eb67ae-6805-4f47-98a7-ed124804c9a6"),

    /**
     * The type of property that the valid value represents.
     */
    PROPERTY_TYPE("propertyType", DataType.STRING.getName(), "The type of property that the valid value represents.", "producedGuard", "f9f2eba1-943a-4611-8bdd-647c1645b036"),

    /**
     * The phase in the lifecycle of the project.
     */
    PROJECT_PHASE("projectPhase", DataType.STRING.getName(), "The phase in the lifecycle of the project.", "Plan", "9178fece-e112-4250-9c08-5c336bd93f78"),

    /**
     * Indicator on how well the project is tracking to plan.
     */
    PROJECT_HEALTH("projectHealth", DataType.STRING.getName(), "Indicator on how well the project is tracking to plan.", "On Track", "eabe799d-72d7-46a2-aa56-5ceaf723a65f"),

    /**
     * Short description on current status of the project.
     */
    PROJECT_STATUS("projectStatus", DataType.STRING.getName(), "Short description on current status of the project.", "Active", "39643f86-185b-465e-9e84-3f74905bad82"),

    /**
     * Descriptive name of the concept that this collection represents.
     */
    COLLECTION_TYPE("collectionType", DataType.STRING.getName(), "Descriptive name of the concept that this collection represents.", "Product Contents", "05b7dcee-1c4c-4d2b-a484-bd84ec4d67e2"),

    /**
     * Display name of the product.
     */
    PRODUCT_STATUS("productStatus", DataType.STRING.getName(),  "Lifecycle status of the product.", "DRAFT", "9aecda29-f1e1-4fe2-8066-2a9d094b990e"),

    /**
     * Display name of the product.
     */
    PRODUCT_NAME("productName", DataType.STRING.getName(),  "Display name of the product.", "Teddy Bear Drop Foot Clinical Trial Patient Measurements", "aae2b086-05ea-433a-b1d6-502674c4ae6e"),

    /**
     * Type or category of the product.
     */
    PRODUCT_TYPE("productType", DataType.STRING.getName(), "Type or category of the product.", "Files", "e64cd76f-f1b8-4c00-9ce9-87fcae8bd3d2"),

    /**
     * Date that the product was made available.
     */
    INTRODUCTION_DATE("introductionDate", DataType.DATE.getName(), "Date that the product was made available.", null,  "9fffa5e8-f2d2-4184-be1d-482d7e093ec0"),

    /**
     * Date when is the next version is expected to be released.
     */
    NEXT_VERSION_DATE("nextVersionDate", DataType.DATE.getName(),  "Date when is the next version is expected to be released.", null, "5866d607-157f-41f2-8875-00b60f534e99"),

    /**
     * Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.
     */
    WITHDRAW_DATE("withdrawDate", DataType.DATE.getName(), "Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.", null, "f2fc6a12-6bf9-4c2f-ad99-68c892b44e76"),

    /**
     * Level of maturity for the product.
     */
    MATURITY("maturity", DataType.STRING.getName(), "Level of maturity for the product.", "InDev",  "666b4e71-cb40-4421-9291-0736cfa5136e"),

    /**
     * Length of time that the product is expected to be in service.
     */
    SERVICE_LIFE("serviceLife", DataType.STRING.getName(), "Length of time that the product is expected to be in service.", "2 years", "f61776bc-e78e-4c4a-9b22-275c8eb4c132"),

    /**
     * Which is the current supported version that is recommended for consumers.  Specified as a versionIdentifier from the asset.
     */
    CURRENT_VERSION("currentVersion", DataType.STRING.getName(), "Which is the current supported version that is recommended for consumers.  Specified as a versionIdentifier from the asset.", "V1.0", "d863a1f5-5770-4a57-a331-01ef59994fba"),

    /**
     * Defines the provenance and confidence that a member belongs in a collection.
     */
    MEMBERSHIP_STATUS("membershipStatus", CollectionMemberStatus.getOpenTypeName(), CollectionMemberStatus.getOpenTypeDescription(), CollectionMemberStatus.PROPOSED.getName(), "e304a92d-60d2-4605-8e3f-d338bd33e6d3"),

    /**
     * Description of how the member is used, or why it is useful in this collection.
     */
    MEMBERSHIP_RATIONALE("membershipRationale", DataType.STRING.getName(), "Description of how the member is used, or why it is useful in this collection.", null, "f4c0da71-f8e8-4d05-a92c-5a1e6b4a263e"),

    /**
     * Defines the sequencing for a collection.
     */
    COLLECTION_ORDER("collectionOrder", OrderBy.getOpenTypeName(), OrderBy.getOpenTypeDescription(), OrderBy.DATE_ADDED.getName(), "068bf5f0-9caa-4e68-ad5e-e230b06b4a3a"),

    /**
     * Name of property to use for ordering.
     */
    ORDER_BY_PROPERTY_NAME("orderByPropertyName", DataType.STRING.getName(), "Name of property to use for ordering.", OpenMetadataProperty.STEWARD.name, "31b6f74e-5759-47e1-8b92-a0ce43bc73db"),

    /**
     * Defines the sequential order in which bytes are arranged into larger numerical values when stored in memory or when transmitted over digital links.
     */
    BYTE_ORDERING("byteOrdering", ByteOrdering.getOpenTypeName(), ByteOrdering.getOpenTypeDescription(), ByteOrdering.LITTLE_ENDIAN.getName(), "efbf89db-dc9f-4f21-a752-c190ffeae4d5"),

    /**
     * Descriptor for a comment that indicated its intent.
     */
    COMMENT_TYPE("commentType", CommentType.getOpenTypeName(), CommentType.getOpenTypeDescription(), CommentType.SUGGESTION.getName(), "37ceb246-ebbe-4bbe-be63-b68e145181a6"),

    /**
     * Type of mechanism to contact an actor.
     */
    CONTACT_METHOD_TYPE("contactMethodType", ContactMethodType.getOpenTypeName(), ContactMethodType.getOpenTypeDescription(), ContactMethodType.EMAIL.getName(), "7c67bc0d-8256-4113-b2fb-0fafe0ec8100"),

    /**
     * Type of contact - such as home address, work mobile, emergency contact ...
     */
    CONTACT_TYPE("contactType", DataType.STRING.getName(), "Type of contact - such as home address, work mobile, emergency contact ...", ContactMethodType.EMAIL.getName(), "caa6fbd3-a470-473c-a656-0265c4496d54"),

    /**
     * Details of the contact method
     */
    CONTACT_METHOD_VALUE("contactMethodValue", DataType.STRING.getName(), "Details of the contact method.", null, "ef793035-7111-4424-b776-6c6c84f38343"),

    /**
     * Details of the service supporting the contact method.
     */
    CONTACT_METHOD_SERVICE("contactMethodService", DataType.STRING.getName(), "Details of the service supporting the contact method.", null, "55a9ed5a-c279-4dbc-895e-fda5c15000da"),


    /**
     * Defines the level of confidence to place in the accuracy of a data item.
     */
    CONFIDENCE_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT.getName(), ConfidenceLevel.getOpenTypeDescription(), Integer.toString(ConfidenceLevel.AUTHORITATIVE.getOrdinal()), "449558d5-3811-401d-b2be-f131480f9bc8"),

    /**
     * Defines how important a data item is to the organization.
     */
    CRITICALITY_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT.getName(), CriticalityLevel.getOpenTypeDescription(), Integer.toString(CriticalityLevel.IMPORTANT.getOrdinal()), "b2c99d60-4078-4d06-beec-f7fcc588c344"),

    /**
     * Defines how confidential a data item is.
     */
    CONFIDENTIALITY_LEVEL_IDENTIFIER("levelIdentifier", DataType.INT.getName(), ConfidentialityLevel.getOpenTypeDescription(), Integer.toString(ConfidentialityLevel.CONFIDENTIAL.getOrdinal()), "908488a2-13f2-469d-b28d-20a7af6be4a1"),

    /**
     * Defines the retention requirements associated with a data item.
     */
    RETENTION_BASIS_IDENTIFIER("basisIdentifier", DataType.INT.getName(), RetentionBasis.getOpenTypeDescription(), Integer.toString(RetentionBasis.PROJECT_LIFETIME.getOrdinal()), "68069d73-0e20-4cb7-a7ec-1555edbbc280"),

    /**
     * Defines the severity of the impact that a situation has.
     */
    SEVERITY_IDENTIFIER("severityIdentifier", DataType.INT.getName(), ImpactSeverity.getOpenTypeDescription(), Integer.toString(ImpactSeverity.MEDIUM.getOrdinal()), "f565af06-37a1-42e9-b680-2c98715f9ff3"),

    /**
     * Defines how important a data item is to the organization.
     */
    STATUS_IDENTIFIER("statusIdentifier", DataType.INT.getName(), GovernanceClassificationStatus.getOpenTypeDescription(), Integer.toString(GovernanceClassificationStatus.VALIDATED.getOrdinal()), "8279602a-c86d-4cf7-b66d-1ea98b75c491"),


    /**
     * Defines the suggested order that data values in this data item should be sorted by.
     */
    SORT_ORDER("sortOrder", DataItemSortOrder.getOpenTypeName(),DataItemSortOrder.getOpenTypeDescription(), DataItemSortOrder.ASCENDING.getName(), "645fd9ac-530d-4351-ab6b-73a999332a78"),

    /**
     * Defines the suggested order that data values in this data item should be sorted by.
     */
    DATA_FIELD_SORT_ORDER("dataFieldSortOrder", DataItemSortOrder.getOpenTypeName(),DataItemSortOrder.getOpenTypeDescription(), DataItemSortOrder.ASCENDING.getName(), "a5458a7b-4736-4df1-9e93-42867c65f8fe"),

    /**
     * Defines the current status of an incident report.
     */
    INCIDENT_STATUS("incidentStatus", IncidentReportStatus.getOpenTypeName(), IncidentReportStatus.getOpenTypeDescription(), IncidentReportStatus.VALIDATED.getName(), "36ce39eb-1389-493c-9a7a-5d52e7f16cda"),

    /**
     * Defines the current execution status of an engine action.
     */
    ACTION_STATUS("actionStatus", EngineActionStatus.getOpenTypeName(), EngineActionStatus.getOpenTypeDescription(), EngineActionStatus.ACTIVATING.getName(), "c2d8fd79-0c16-4e01-888f-2ae7a9cdf11d"),

    /**
     * Different types of activities.
     */
    ACTIVITY_TYPE("type", GlossaryTermActivityType.getOpenTypeName(), GlossaryTermActivityType.getOpenTypeDescription(), GlossaryTermActivityType.PROJECT.getName(), "6949c588-d7ab-441f-be03-a97b1dc2900b"),

    /**
     * Defines the confidence in the assigned relationship.
     */
    TERM_RELATIONSHIP_STATUS("status", GlossaryTermRelationshipStatus.getOpenTypeName(), GlossaryTermRelationshipStatus.getOpenTypeDescription(), GlossaryTermRelationshipStatus.DRAFT.getName(), "5cc02a53-2428-434a-9b97-883eae896552"),

    /**
     * Defines the provenance and confidence of a semantic assignment.
     */
    TERM_ASSIGNMENT_STATUS("termAssignmentStatus", GlossaryTermAssignmentStatus.getOpenTypeName(), GlossaryTermAssignmentStatus.getOpenTypeDescription(), GlossaryTermAssignmentStatus.IMPORTED.getName(), "d842dfdd-f080-4539-9a3c-eacdf0a03d07"),

    /**
     * Defines the provenance and confidence of a data class assignment.
     */
    DATA_CLASS_ASSIGNMENT_STATUS("dataClassAssignmentStatus", DataClassAssignmentStatus.getOpenTypeName(), DataClassAssignmentStatus.getOpenTypeDescription(), DataClassAssignmentStatus.IMPORTED.getName(), "71e53cf4-7158-4054-b7f8-da643a34d2da"),

    /**
     * An indication of the relative position in which this work item should be tackled compared to others in the overall work list.
     */
    PRIORITY("priority", DataType.INT.getName(), "An indication of the relative position in which this work item should be tackled compared to others in the overall work list.", "10", "6168ae13-f6ee-49e7-9f21-693e7a401926"),

    /**
     * Metadata properties embedded in the media file.
     */
    EMBEDDED_METADATA("embeddedMetadata", DataType.MAP_STRING_STRING.getName(), "Metadata properties embedded in the media file.", null, "af5a6693-e14c-489e-a76c-1a45248e9dbd"),

    /**
     * Full publication title of the external source.
     */
    REFERENCE_TITLE("referenceTitle", DataType.STRING.getName(), "Full publication title of the external source.", null, "e98ea20f-111b-421d-91a5-586d469f990d"),

    /**
     * Summary of the key messages in the external source.
     */
    REFERENCE_ABSTRACT("referenceAbstract", DataType.STRING.getName(), "Summary of the key messages in the external source.", null, "57b63b64-5139-45db-94e4-146a57389fe8"),

    /**
     * Name of the revision or version of the external source.
     */
    REFERENCE_VERSION("referenceVersion", DataType.STRING.getName(), "Name of the revision or version of the external source.", null, "f0365e94-3c4b-4386-a75a-a03f6e78cb7d"),

    /**
     * List of authors for the external source.
     */
    AUTHORS("authors", DataType.ARRAY_STRING.getName(), "List of authors for the external source.", null, "b0dd1911-7bb6-47be-bd88-5a9c86f00fe0"),

    /**
     * Number of pages that this external source has.
     */
    NUMBER_OF_PAGES("numberOfPages", DataType.INT.getName(), "Number of pages that this external source has.", null, "dfdb0daa-3b5d-4f76-a674-bf2e683f587c"),

    /**
     * Range of pages that this reference covers.
     */
    PAGE_RANGE("pageRange", DataType.STRING.getName(), "Range of pages that this reference covers. For example, if it is a journal article, this could be the range of pages for the article in the journal.", "35-98", "6a97b691-fbba-44ae-9557-f18b627a7fb1"),

    /**
     * Name of the journal or series of publications that this external source is from.
     */
    PUBLICATION_SERIES("publicationSeries", DataType.STRING.getName(), "Name of the journal or series of publications that this external source is from.", null, "93426a39-9a40-475b-aa3c-937a3abfd40e"),

    /**
     * Name of the volume in the publication series that this external source is from.
     */
    PUBLICATION_SERIES_VOLUME("publicationSeriesVolume", DataType.STRING.getName(), "Name of the volume in the publication series that this external source is from.", null, "9ba221d6-d9c6-4d4e-9e65-9bb0a523faff"),

    /**
     * Name of the edition for this external source.
     */
    EDITION("edition", DataType.STRING.getName(), "Name of the edition for this external source.", "First Edition", "d327eb3c-22d5-4dc1-8206-fe82089c1011"),

    /**
     * Network address where this external source can be accessed from.
     */
    URL("url", DataType.STRING.getName(), "Network address where this external source can be accessed from.", null, "604dbf10-335d-4132-8a32-c30fa9fec154"),

    /**
     * Name of the publisher responsible for producing this external source.
     */
    PUBLISHER("publisher", DataType.STRING.getName(), "Name of the publisher responsible for producing this external source.", null, "3a92b878-c0ae-464b-a70b-63fd61a36ec5"),

    /**
     * Date of the first published version/edition of this external source.
     */
    FIRST_PUB_DATE("firstPublicationDate", DataType.DATE.getName(), "Date of the first published version/edition of this external source.", null, "22eac39f-8097-4f17-84b2-d718f06b242c"),

    /**
     * Date when this version/edition of this external source was published.
     */
    PUBLICATION_DATE("publicationDate", DataType.INT.getName(), "Date when this version/edition of this external source was published.", null, "1b6d0398-31f3-4f8e-9998-3b975323423d"),

    /**
     * City where the publishers are based.
     */
    PUBLICATION_CITY("publicationCity", DataType.STRING.getName(), "City where the publishers are based.", "London", "16bb6599-a056-4d49-b757-833ba1bd2b38"),

    /**
     * Year when the publication of this version/edition of the external source was published.
     */
    PUBLICATION_YEAR("publicationYear", DataType.STRING.getName(), "Year when the publication of this version/edition of the external source was published.", "2020", "5ae46954-7762-4ab5-87e2-828287e82013"),

    /**
     * List of unique numbers allocated by the publisher for this external source.  For example ISBN, ASIN, UNSPSC code.
     */
    PUBLICATION_NUMBERS("publicationNumbers", DataType.ARRAY_STRING.getName(), "List of unique numbers allocated by the publisher for this external source.  For example ISBN, ASIN, UNSPSC code.", null, "2c734b4c-de01-490f-9675-da4addc5efe4"),

    /**
     * Name of license associated with this external source.
     */
    LICENSE("license", DataType.STRING.getName(), "Name of license associated with this external source.", null, "c64f169a-863b-4355-b5a6-27338e1152c1"),

    /**
     * Copyright statement associated with this external source.
     */
    COPYRIGHT("copyright", DataType.STRING.getName(), "Copyright statement associated with this external source.", null, "0ece8225-2dfe-4716-bfe2-bac791b6c5f7"),

    /**
     * Attribution statement to use when consuming this external resource.
     */
    ATTRIBUTION("attribution", DataType.STRING.getName(), "Attribution statement to use when consuming this external resource.", null, "f2e665a5-c7e0-48c0-b3d6-e47c3b1fde02"),

    /**
     * Local identifier for the reference.
     */
    REFERENCE_ID("referenceId", DataType.STRING.getName(), "Local identifier for the reference.", null, "90b32257-1795-4b2f-8383-bfdbdbc1ad89"),

    /**
     * Range of pages in the external reference that this link refers.
     */
    PAGES("pages", DataType.STRING.getName(), "Range of pages in the external reference that this link refers.", "1-10", "31144831-a9df-47b1-9975-902e0f76836a"),

    /**
     * Specific media usage by the consumer that overrides the default media usage documented in the related media.
     */
    MEDIA_USAGE("mediaUsage", "MediaUsage", "Specific media usage by the consumer that overrides the default media usage documented in the related media.", null, "483bbce1-e070-46ad-8636-f813c9309e56"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media use.
     */
    MEDIA_USAGE_OTHER_ID("mediaUsageOtherId", DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media use.", null, "66102545-c7b2-4501-a85e-de6e348a92a5"),

    /**
     * The most common, or expected use of this media resource.
     */
    DEFAULT_MEDIA_USAGE("defaultMediaUsage", MediaUsage.getOpenTypeName(), "The most common, or expected use of this media resource.", null, "279f1eb4-e60c-4ebc-9568-cc545c2fe491"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media use.
     */
    DEFAULT_MEDIA_USAGE_OTHER_ID("defaultMediaUsageOtherId", DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media use.", null, "7132f04f-92db-4571-9c98-f64f55390a55"),

    /**
     * Type of media.
     */
    MEDIA_TYPE("mediaType", MediaType.getOpenTypeName(), "Type of media.", null, "d6581642-3fbd-447c-a90b-95bfa770491e"),

    /**
     * Unique identifier of the code (typically a valid value definition) that defines the media type.
     */
    MEDIA_TYPE_OTHER_ID("mediaTypeOtherId", DataType.STRING.getName(), "Unique identifier of the code (typically a valid value definition) that defines the media type.", null, "4a839c98-31ec-4c0d-88c1-206e1b13b7f6"),

    /**
     * Local identifier for the media.
     */
    MEDIA_ID("mediaId", DataType.STRING.getName(), "Local identifier for the media, from the perspective of the referencee.  For example. it may be the citation number in the list of references", "COLT-2", "1f738dc4-2df1-4177-919a-44b91b3ed22e"),

    /**
     * Name of the organization that this external source is from.
     */
    ORGANIZATION("organization", DataType.STRING.getName(), "Name of the organization that this external source is from.", null, "a837b6b5-ca30-4662-a643-5477d793bd29"),

    /**
     * Network address used to connect to the endpoint.
     */
    NETWORK_ADDRESS("networkAddress", DataType.STRING.getName(), "Network address used to connect to the endpoint.", "https://localhost:9443", "0f38d466-e288-4971-bda8-1c5fde81bc82"),

    /**
     * Name of the protocol used to connect to the endpoint.
     */
    PROTOCOL("protocol", DataType.STRING.getName(), "Name of the protocol used to connect to the endpoint.", "HTTPS", "173d3d2f-c0c2-4341-9693-20e3bb6abb86"),

    /**
     * Type of encryption used at the endpoint (if any).
     */
    ENCRYPTION_METHOD("encryptionMethod", DataType.STRING.getName(), "Type of encryption used at the endpoint (if any).", null, "bc4a9ded-0414-4bbf-8cd2-c9ecc3e64ee9"),

    /**
     * Version of the database.
     */
    DATABASE_VERSION("databaseVersion", DataType.STRING.getName(), "Version of the database.", null, "f13db2cf-a0fc-4790-ba50-74ae5443bf36"),

    /**
     * Instance of the database.
     */
    INSTANCE ("instance", DataType.STRING.getName(), "Instance of the database.", null, "ecd5593a-647f-4767-b465-279e4bd35efc"),

    /**
     * importedFrom
     */
    IMPORTED_FROM("importedFrom", DataType.STRING.getName(), "System that the database was imported from.", null, "6e7b1e16-bcf4-4010-becd-244506f1d3ed"),

    /**
     * Name of the language used to implement this component.
     */
    IMPLEMENTATION_LANGUAGE("implementationLanguage", DataType.STRING.getName(), "Name of the language used to implement this component.", "Java", "e56dbaa5-6a2a-4aa0-8584-b3305234851f"),

    /**
     * The Date/time that this action was reviewed.
     */
    LAST_REVIEW_TIME("lastReviewTime", DataType.DATE.getName(), "The Date/time that this action was reviewed.", null, "adac8415-11c8-43e9-8a43-92a799893555"),

    /**
     * Version of the property facet schema.
     */
    SCHEMA_VERSION("schemaVersion", DataType.STRING.getName(), "Version of the property facet schema.", null, "7eb8affc-c5d5-4ab2-9395-2ac8216be397"),

    /**
     * properties
     */
    PROPERTIES("properties", DataType.MAP_STRING_STRING.getName(), "Property name-value pairs.", null, "6cf26b71-85db-41e0-9d7a-8e3fe956b649"),

    /**
     * Identifies which type of delete to use.
     */
    DELETE_METHOD("deleteMethod", DeleteMethod.getOpenTypeName(), "Identifies which type of delete to use.", "ARCHIVE", "0028d073-fe1f-482d-b546-28a77dd712e2"),

    /**
     * Geographical coordinates of this location.
     */
    COORDINATES("coordinates", DataType.STRING.getName(), "Geographical coordinates of this location.", null, "bfa8b230-f9ec-4105-a2fd-3159e9715043"),

    /**
     * The scheme used to define the meaning of the coordinates.
     */
    MAP_PROJECTION("mapProjection", DataType.STRING.getName(), "The scheme used to define the meaning of the coordinates.", null, "a4d71daa-6e87-4214-99e9-2db487ee6573"),

    /**
     * Postal address of the location.
     */
    POSTAL_ADDRESS("postalAddress", DataType.STRING.getName(), "Postal address of the location.", null, "a2743556-f1d6-473b-93bd-1615a0f937b8"),

    /**
     * Timezone for the location.
     */
    TIME_ZONE("timezone", DataType.STRING.getName(), "Timezone for the location.", null, "f163935d-78d1-41ab-b00f-b688e110fdc7"),

    /**
     * Level of security at this location.
     */
    LEVEL("level", DataType.STRING.getName(), "Level of security at this location.", null, "82e1a347-ce26-4b35-83ac-e310652572bf"),

    /**
     * Descriptive name of the association.
     */
    ASSOCIATION_NAME("associationName", DataType.STRING.getName(), "Descriptive name of the association.", null, "539b51a4-015d-4f3e-8389-6e73fcfb1555"),

    /**
     * Type of the association, such as 'containment', 'aggregation' or 'inheritance.'
     */
    ASSOCIATION_TYPE("associationType", DataType.STRING.getName(), "Type of the association, such as 'containment', 'aggregation' or 'inheritance.'", null, "6c2665d5-f202-49cb-8ce9-7333501e3072"),

    /**
     * Name of the operating system running on this operating platform.
     */
    OPERATING_SYSTEM("operatingSystem", DataType.STRING.getName(), "Name of the operating system running on this operating platform.", "macOS", "cbd3c5ec-0259-43cf-94b8-0339291e871b"),

    /**
     * Level of patches applied to the operating system.
     */
    OPERATING_SYSTEM_PATCH_LEVEL("operatingSystemPatchLevel", DataType.STRING.getName(), "Level of patches applied to the operating system.", null, "6fdda5ad-488a-477d-a1cc-33bb8267ef4b"),

    MEMBER_ROLE("memberRole", DataType.STRING.getName(), "The role of the member in the host cluster.  This value is typically defined by the technology of the host cluster.", null, "9c143e06-6a8c-47ba-acf6-28a87fef4f2b"),
    /**
     * Preferred pronouns to use when addressing this person.
     */
    PRONOUNS("pronouns", DataType.STRING.getName(), "Preferred pronouns to use when addressing this person.", null, "8eaa378a-f50c-41da-bb1c-867251a63637"),

    /**
     * Identifier of the tenant.
     */
    TENANT_NAME("tenantName", DataType.STRING.getName(), "Identifier of the tenant.", null, "16745cc3-d657-4e99-a030-6c0023878bb1"),

    /**
     * Description of the type of tenant.
     */
    TENANT_TYPE("tenantType", DataType.STRING.getName(), "Description of the type of tenant.", null, "7b5805fe-397d-4171-96a3-f41072c6453b"),

    /**
     * Commercial name of the service.
     */
    OFFERING_NAME("offeringName", DataType.STRING.getName(), "Commercial name of the service.", null, "8294e9c1-2d8d-429f-8ac2-9f361551291c"),

    /**
     * Description of the type of the service.
     */
    SERVICE_TYPE("serviceType", DataType.STRING.getName(), "Description of the type of the service.", null, "7c05c0a8-c2c3-4043-af10-75858616482d"),

    /**
     * Name of the cloud provider.
     */
    PROVIDER_NAME("providerName", DataType.STRING.getName(), "Name of the cloud provider.", null, "2c1df3cd-a043-457d-8a41-72bec6c12855"),

    /**
     * The LDAP distinguished name (DN) that gives a unique positional name in the LDAP DIT.
     */
    DISTINGUISHED_NAME("distinguishedName", DataType.STRING.getName(), "The LDAP distinguished name (DN) that gives a unique positional name in the LDAP DIT.", null, "63d87f10-10c5-41b3-af11-17167e7c7269"),

    /**
     * The type name of the PersonRole that the UserIdentity is used for.
     */
    ROLE_TYPE_NAME("roleTypeName", DataType.STRING.getName(), "", null, "3216d40f-58e0-435c-90cd-980af2645ee7"),

    /**
     * The unique identifier of the specific PersonRole that the UserIdentity is used for.
     */
    ROLE_GUID("roleGUID", DataType.STRING.getName(), "The unique identifier of the specific PersonRole that the UserIdentity is used for.", null, "c2b71682-2d08-40b4-a250-96a99c6bc575"),

    /**
     * First letter of each of the person's given names.
     */
    INITIALS("initials", DataType.STRING.getName(), "First letter of each of the person's given names.", "ABC", "dfac6629-2c0b-436b-831b-d6a2ef1fed94"),

    /**
     * The name strings that are the part of a person's name that is not their surname.
     */
    GIVEN_NAMES("givenNames", DataType.STRING.getName(), "The names that are the part of a person's name that is not their surname.", null, "1f50956a-e042-44b0-b032-bc9d49feeb82"),

    /**
     * The family name of the person.
     */
    SURNAME("surname", DataType.STRING.getName(), "The family name of the person.", null, "4fa69f84-0e64-43f3-a5bd-76bbf6466e0a"),

    /**
     * Full or official/legal name of the individual (if different from known name).
     */
    FULL_NAME("fullName", DataType.STRING.getName(), "Full or official/legal name of the individual (if different from known name).", null, "d8d1460f-7b04-4427-b928-d39ff555ab6e"),

    /**
     * Spoken or written language preferred by the person.
     */
    PREFERRED_LANGUAGE("preferredLanguage", DataType.STRING.getName(), "Spoken or written language preferred by the person.", null, "caa48b00-1953-46f8-abda-54211f831d98"),

    /**
     * Country that is the person's primary residence.
     */
    RESIDENT_COUNTRY("residentCountry", DataType.STRING.getName(), "Country that is the person's primary residence.", "United Kingdom", "f4d113b2-fc77-48c9-a9b0-73cbc953e2ad"),

    /**
     * Principle role or level in the organization.
     */
    JOB_TITLE("jobTitle", DataType.STRING.getName(), "Principle role or level in the organization.", null, "b20696fe-3ff4-49e0-bdaf-b587abd8f0be"),

    /**
     * The unique identifier of a person used by their employer.
     */
    EMPLOYEE_NUMBER("employeeNumber", DataType.STRING.getName(), "The unique identifier of a person used by their employer.", null, "fc94b1a2-08c2-4078-b349-32904097ac6a"),

    /**
     * Code used by employer typically to describe the type of employment contract
     */
    EMPLOYEE_TYPE("employeeType", DataType.STRING.getName(), "Code used by employer typically to describe the type of employment contract.", null, "62ef771b-87b6-4e0f-adf2-3b471f0e7173"),

    /**
     * Points capturing a person's engagement with open metadata.
     */
    KARMA_POINTS("karmaPoints", DataType.LONG.getName(), "Points capturing a person's engagement with open metadata.", null, "ec847574-376b-4b21-a8a9-4bcdfcb2000d"),

    /**
     * Number of people that can be appointed to the role.
     */
    HEAD_COUNT("headCount", DataType.INT.getName(), "Number of people that can be appointed to the role.", null, "58e9c9c4-d5e3-4576-a2a7-9f459bcdd439"),

    /**
     * What percentage of time is the appointee expected to devote to this role.
     */
    EXPECTED_TIME_ALLOCATION_PERCENT("expectedTimeAllocationPercent", DataType.INT.getName(), "What percentage of time is the appointee expected to devote to this role.", "50", "027c580d-c5c9-4dda-a7d3-79a9951a85ad"),

    /**
     * Type of team, such as department.
     */
    TEAM_TYPE("teamType", DataType.STRING.getName(), "Type of team, such as division, or department.", null, "109d24af-b694-4f1e-90d2-ad3945596f2f"),

    /**
     * Details of the type of role position within a group, team, project, eg deputy.
     */
    ROLE_POSITION("position", DataType.STRING.getName(), "Details of the type of role position within a group, team, project, eg deputy.", null, "dc83ac84-914f-441e-876b-5a02293fc568"),

    /**
     * Can delegations and escalations flow on this relationship.
     */
    DELEGATION_ESCALATION("delegationEscalationAuthority", DataType.BOOLEAN.getName(), "Can delegations and escalations flow on this relationship.", null, "c8238d05-07ac-4c1e-a89c-34299d9827f8"),

    /**
     * What is the scope or nature of the assignment.
     */
    ASSIGNMENT_TYPE("assignmentType", DataType.STRING.getName(), "What is the scope or nature of the assignment.", null, "6d6af0b8-1b56-4941-a0c9-524331c46038"),

    /**
     * Description of the role of the team in the project.
     */
    TEAM_ROLE("teamRole", DataType.STRING.getName(), "Description of the role of the team in the project.", null, "4ce8ffb3-2688-425e-abdd-b165bbf2adc6"),

    /**
     * Reasons for the project dependency.
     */
    DEPENDENCY_SUMMARY("dependencySummary", DataType.STRING.getName(), "Reasons for the project dependency.", null, "c1e02d1e-efb8-4299-be32-529b4a5976c1"),

    /**
     * Identifier that describes the role that the stakeholders will play in the operation of the Referenceable.
     */
    STAKEHOLDER_ROLE("stakeholderRole", DataType.STRING.getName(), "Identifier that describes the role that the stakeholders will play in the operation of the Referenceable.", "fundingExecutive", "09b45956-ad1b-4e51-ab7f-b1858f71de0d"),

    /**
     * Short summary or purpose of the element.
     */
    TITLE("title", DataType.STRING.getName(), "Short summary or purpose of the element.", null, "b87aeaa9-ef37-479b-9d59-dc03c98b3248"),

    /**
     * Breadth of responsibility or coverage.
     */
    SCOPE("scope", DataType.STRING.getName(), "Breadth of responsibility or coverage.", OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE, "033b4f92-fa46-4151-a095-c0bae938de8f"),

    /**
     * The value to use.
     */
    PREFERRED_VALUE("preferredValue", DataType.STRING.getName(), "The value to use.", "Survey Resource", "b6258cbe-72f5-430b-ab62-6acb6c667e87"),

    /**
     * Descriptive name of the concept that this valid value applies to.
     */
    CATEGORY("category", DataType.STRING.getName(), "Descriptive name of the concept that this valid value applies to.", null, "385d7ca6-bcdd-43e4-925f-026a1043e37d"),

    /**
     * Is this valid value case-sensitive, or should the values match irrespective of case?
     */
    IS_CASE_SENSITIVE("isCaseSensitive", DataType.BOOLEAN.getName(), "Is this valid value case-sensitive, or should the values match irrespective of case?", "true", "999d6f38-b244-44da-91f9-53694a25c174"),

    IS_DEPRECATED("isDeprecated", DataType.BOOLEAN.getName(),"This element may still be used but is flagged that it will be removed at some point in the future.", "false", "bf8e38f8-d841-4c7b-990a-26d701713bd0"),

    /**
     * "Reason for the meeting and intended outcome.
     */
    OBJECTIVE("objective", DataType.STRING.getName(), "Reason for the meeting and intended outcome.", null, "a0f9f9c5-ea35-458c-b969-1de5fb3116eb"),

    /**
     * Description of what happened at the meeting.
     */
    MINUTES("minutes", DataType.ARRAY_STRING.getName(), "Description of what happened at the meeting.", null, "9b65f72c-76c1-438c-a672-49039f2ee62d"),

    /**
     * Start time of the meeting.
     */
    START_TIME("startTime", DataType.DATE.getName(), "Start time of the meeting.", null, "90ea014e-09ed-41a5-972d-68a534c61a5c"),

    /**
     * End time of the meeting.
     */
    END_TIME("endTime", DataType.DATE.getName(), "End time of the meeting.", null, "de498d70-59b4-4639-9069-5f41b40265fb"),

    /**
     * When the requested action was identified.
     */
    CREATION_TIME("creationTime", DataType.DATE.getName(), "When the requested action was identified.", null, "3b9586f3-3563-42cd-920b-11359d94e6ce"),

    /**
     * Type of to do - typically managed in a valid value set and used in stewardship automation.
     */
    TO_DO_TYPE("toDoType", DataType.STRING.getName(), "Type of to do - typically managed in a valid value set and used in stewardship automation.", null, "3ea557fa-9f01-49f0-ae17-e2825185312e"),

    /**
     * Relative importance of this governance definition compared to its peers.
     */
    IMPORTANCE("importance", null, "Relative importance of this definition compared to its peers.", null, "dfae7171-34a5-4460-bb61-4219454155db"),

    /**
     * When the requested action needs to be completed.
     */
    DUE_TIME("dueTime", DataType.DATE.getName(), "When the requested action needs to be completed.", null, "200f604a-8936-4ad7-aa13-01abbecc82a6"),

    /**
     * When the requested action was completed.
     */
    COMPLETION_TIME("completionTime", DataType.DATE.getName(), "When the requested action was completed.", null, "9359f36f-ed11-4c33-80c3-e02941fee6b5"),

    /**
     * Purpose of the community.
     */
    MISSION("mission", DataType.STRING.getName(), "Purpose of the community.", "To share new data science techniques.", "cb870681-de85-406b-bea6-6d138777cfe9"),

    /**
     * Type of community membership.
     */
    MEMBERSHIP_TYPE("membershipType", CommunityMembershipType.getOpenTypeName(), "Type of community membership.", "Observer.", "d94a88e9-f5f6-4c3f-8ce2-e565817d6294"),

    /**
     * Private properties accessible only to the connector.
     */
    SECURED_PROPERTIES("securedProperties", DataType.MAP_STRING_OBJECT.getName(), "Private properties accessible only to the connector.", null, "b1b3e2dd-0ad1-4466-884b-697cf6333382"),

    /**
     * Specific configuration properties used to configure the behaviour of the connector.
     */
    CONFIGURATION_PROPERTIES("configurationProperties", DataType.MAP_STRING_OBJECT.getName(), "Specific configuration properties used to configure the behaviour of the connector.", null, "ce3009ac-54f3-4c6f-9658-27c2d2e75c3a"),

    /**
     * Password for the userId in clear text.
     */
    CLEAR_PASSWORD("clearPassword", DataType.STRING.getName(), "Password for the userId in clear text.", null, "4b2912cb-5a98-457a-853f-44a853d9a459"),

    /**
     * Encrypted password that the connector needs to decrypt before use.
     */
    ENCRYPTED_PASSWORD("encryptedPassword", DataType.STRING.getName(), "Encrypted password that the connector needs to decrypt before use.", null, "8a5c52ed-8b82-434e-8a94-2a772da8caa7"),

    /**
     * Name of the Java class that implements this connector type's open connector framework (OCF) connector provider.
     */
    CONNECTOR_PROVIDER_CLASS_NAME("connectorProviderClassName", DataType.STRING.getName(), "Name of the Java class that implements this connector type's open connector framework (OCF) connector provider.", null, "7c8b3671-f6f4-4156-845c-ff20ca627eac"),

    /**
     * List of additional connection property names supported by the connector implementation.
     */
    RECOGNIZED_ADDITIONAL_PROPERTIES("recognizedAdditionalProperties", DataType.ARRAY_STRING.getName(), "List of additional connection property names supported by the connector implementation.", null, "93e83ce7-0255-43c0-b56f-a58835134106"),

    /**
     * List of secured connection property names supported by the connector implementation.
     */
    RECOGNIZED_SECURED_PROPERTIES("recognizedSecuredProperties", DataType.ARRAY_STRING.getName(), "List of secured connection property names supported by the connector implementation.", null, "6cdf6c43-cf30-485f-b174-1864b4f785d4"),

    /**
     * List of configuration property names supported by the connector implementation.
     */
    RECOGNIZED_CONFIGURATION_PROPERTIES("recognizedConfigurationProperties", DataType.ARRAY_STRING.getName(), "List of configuration property names supported by the connector implementation.", null, "750b88ff-c9b7-49cd-9c93-4f4c85d71e16"),

    /**
     * Type of asset supported by the connector implementation.
     */
    SUPPORTED_ASSET_TYPE_NAME("supportedAssetTypeName", DataType.STRING.getName(), "Type of asset supported by the connector implementation.", null, "c745e64a-a03e-4780-8a7f-46364a0d815a"),

    /**
     * Description of the format of the data expected by the connector implementation.
     */
    EXPECTED_DATA_FORMAT("expectedDataFormat", DataType.STRING.getName(), "Description of the format of the data expected by the connector implementation.", null, "870c0740-8797-4707-86d4-8d470c3143c5"),

    /**
     * Name of the framework that the connector implements. The default is 'Open Connector Framework (OCF)'
     */
    CONNECTOR_FRAMEWORK_NAME("connectorFrameworkName", DataType.STRING.getName(), "Name of the framework that the connector implements. The default is 'Open Connector Framework (OCF)'", null, "72d7f244-d71d-4efe-80d4-bd564a27ea18"),

    /**
     * The programming language used to implement the connector's interface.
     */
    CONNECTOR_INTERFACE_LANGUAGE("connectorInterfaceLanguage", DataType.STRING.getName(), "The programming language used to implement the connector's interface.", null, "e37051ed-baa0-46f4-a39c-cb6cbaf7a671"),

    /**
     * List of interfaces supported by the connector.
     */
    CONNECTOR_INTERFACES("connectorInterfaces", DataType.ARRAY_STRING.getName(), "List of interfaces supported by the connector.", null, "3ee2f999-cbbc-43e9-82df-241641950fa6"),

    /**
     * Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation.
     */
    TARGET_TECHNOLOGY_SOURCE("targetTechnologySource", DataType.STRING.getName(), "Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation", null, "b8e9b52c-ea79-46b9-ad2e-514807115d99"),

    /**
     * Name of the technology that the connectors access. For example, Apache Kafka.
     */
    TARGET_TECHNOLOGY_NAME("targetTechnologyName", DataType.STRING.getName(), "Name of the technology that the connectors access. For example, Apache Kafka.", null, "a59e71d3-6a81-4f3d-98ee-d5d7bdadd351"),

    /**
     * Names of the technology's interfaces that the connectors use.
     */
    TARGET_TECHNOLOGY_INTERFACES("targetTechnologyInterfaces", DataType.ARRAY_STRING.getName(), "Names of the technology's interfaces that the connectors use.", null, "755d4353-7f33-44b2-805b-dd2c5ac2f916"),

    /**
     * List of versions of the technology that the connector implementation supports.
     */
    TARGET_TECHNOLOGY_VERSIONS("targetTechnologyVersions", DataType.ARRAY_STRING.getName(), "List of versions of the technology that the connector implementation supports.", null, "48324db4-d4a6-46d7-8430-0dba4473f13b"),

    /**
     * Additional arguments needed by the virtual connector when using each connection.
     */
    ARGUMENTS("arguments", DataType.MAP_STRING_OBJECT.getName(), "Additional arguments needed by the virtual connector when using each connection.", null, "0ba7abef-1a62-4bf6-a21c-0239c3521d50"),

    /**
     * Description of the asset that is retrieved through this connection.
     */
    ASSET_SUMMARY("assetSummary", DataType.STRING.getName(), "Description of the asset that is retrieved through this connection.", null, "2ab4e6eb-9064-4638-8c72-5c9d5eb61383"),

    /**
     * Character used between each column.
     */
    DELIMITER_CHARACTER("delimiterCharacter", DataType.STRING.getName(), "Character used between each column.", null, "9b6e9ae9-0251-41c8-a81c-0df3126453e8"),

    /**
     * The character used to group the content of the column that contains one or more delimiter characters.
     */
    QUOTE_CHARACTER("quoteCharacter", DataType.STRING.getName(), "The character used to group the content of the column that contains one or more delimiter characters.", null, "d23618c6-45a2-40a0-b34c-6385d252b5be"),

    /**
     * Format of the file system.
     */
    FORMAT("format", DataType.STRING.getName(), "Format of the file system.", null, "207a7ab2-e530-494f-a859-432d7fdb40fd"),

    /**
     * Level of encryption used on the filesystem (if any).
     */
    ENCRYPTION("encryption", DataType.STRING.getName(), "Level of encryption used on the filesystem (if any).", null, "011e4f70-b914-4dc8-9dc0-f259014e6f63"),

    /**
     * Display label to use when displaying this lineage relationship in a lineage graph.
     */
    LABEL("label", DataType.STRING.getName(), "Display label to use when displaying this lineage relationship in a lineage graph.", "provision data", "767aead2-5b37-4607-ba09-77d805e17d35"),

    /**
     * Location of the call in the implementation.
     */
    LINE_NUMBER("lineNumber", DataType.STRING.getName(), "Location of the call in the implementation.", "21", "942f07c9-54ba-44c4-8b9b-78753bc4fef2"),

    /**
     * Function that must be true to travel down this control flow.
     */
    GUARD( "guard", DataType.STRING.getName(), "Function, or value that must be true to travel down this control flow.", "x>4", "ca2d3fd9-3c6a-4771-9e9c-7bb623f62ba2"),

    /**
     * The number of hops along the lineage graph to the ultimate source organized by type of element.
     */
    HOPS("hops", DataType.MAP_STRING_INT.getName(), "The number of hops along the lineage graph to the ultimate source organized by type of element.", null, "4bcc1f8e-ebef-4a6e-adbe-65d3932104e7"),


    ;


    /**
     * Name
     */
    public final String name;

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
     * @param type            property type
     * @param description     property description
     * @param example         an example of a value for this property
     * @param descriptionGUID unique identifier of the valid value describing this property
     */
    OpenMetadataProperty(String name,
                         String type,
                         String description,
                         String example,
                         String descriptionGUID)
    {
        this.name            = name;
        this.type            = type;
        this.description     = description;
        this.example         = example;
        this.descriptionGUID = descriptionGUID;
    }
}
