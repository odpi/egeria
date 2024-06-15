/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;

/**
 * Provides the definition for all properties defined in the open metadata types.
 */
public enum OpenMetadataProperty
{
    /**
     * Unique identifier of an open metadata entity or relationship.
     */
    GUID("guid", "string", "Unique identifier of an open metadata entity or relationship.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55"),

    /**
     * The unique identifier for the metadata collection that is the home for a metadata element.
     */
    METADATA_COLLECTION_ID("metadataCollectionId", "string", "The unique identifier for the metadata collection that is the home for a metadata element.", "151b9d80-8417-41c4-8f04-3ab90a387196", "01d7e832-ef18-4451-8e4c-4ba972292a8e"),

    /**
     * Unique identifier of an open metadata entity, classification or relationship.
     */
    TYPE_NAME("typeName", "string", "Unique name of an open metadata entity,classification or relationship.", "Asset", "7c5a7e83-2709-4789-b014-d23082a659bd"),

    /**
     * Time that an instance was created.
     */
    CREATE_TIME("createTime", "date", "Time that an instance was created.", null, "433f573a-0044-4943-a8ee-78102ae2fb32"),

    /**
     * Last time that an instance was updated.
     */
    UPDATE_TIME("updateTime", "date", "Last time that an instance was updated.", null, "6b94347b-f692-4c44-9c61-d050d9758c54"),

    /**
     * Timestamp when the archive occurred or was detected.
     */
    ARCHIVE_DATE("archiveDate", "string", "Timestamp when the archive occurred or was detected.", null, "5235c1b1-fa03-4d17-80e2-7826242dfc75"),

    /**
     * Name of user that performed the archive - or detected the archive.
     */
    ARCHIVE_USER("archiveUser", "string", "Name of user that performed the archive - or detected the archive.", null, "fe81b808-3d78-4e84-a1de-b0b273a89bec"),

    /**
     * Name of process that performed the archive - or detected the archive.
     */
    ARCHIVE_PROCESS("archiveProcess", "string", "Name of process that performed the archive - or detected the archive.", null, "0aef1b3a-50dc-4a6c-981f-93bf8815e7f4"),

    /**
     * Name of service that created this classification.
     */
    ARCHIVE_SERVICE("archiveService", "string", "Name of service that created this classification.", null, "c2170958-998e-4d8c-8e33-e91ef4872c8a"),

    /**
     * Name of method that created this classification.
     */
    ARCHIVE_METHOD("archiveMethod", "string", "Name of method that created this classification.", null, "45d558ba-00b4-4a67-9226-590b100da7e9"),

    /**
     * Properties to locate the real-world counterpart in the archive.
     */
    ARCHIVE_PROPERTIES("archiveProperties", "string", "Properties to locate the real-world counterpart in the archive.", null, "d149d2d8-f181-4c20-8d68-5460f54bece5"),

    /**
     * The unique identifier of the referenceable that this element is directly or indirectly anchored to.
     */
    ANCHOR_GUID("anchorGUID", "string", "The unique identifier of the referenceable that this element is directly or indirectly anchored to.", "f1ad7bbe-1d9f-4149-b87c-205bbd174b55", "e7de4efc-6fa4-4942-97eb-e9b5be701875"),

    /**
     * Unique name of the type of the anchor.
     */
    ANCHOR_TYPE_NAME("anchorTypeName", "string", "Unique name of the type of the anchor.", "Asset", "605c9dd5-fa79-4457-b299-9169c5567f97"),

    /**
     * Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.
     */
    ANCHOR_DOMAIN_NAME("anchorDomainName", "string", "Unique name of the domain of the anchor.  This is an Open Metadata Type Name that either directly inherits from OpenMetadataRoot or Referenceable.", "Asset", "00ba532f-792f-4b78-8940-b5a9fd72f854"),

    /**
     * Unique identifier for the entity.
     */
    QUALIFIED_NAME("qualifiedName", "string", "Unique identifier for the entity.", "SoftwareServer:MyAsset:MyAssetName", "e31e5b9b-0f96-42a9-8e67-0e3fc66ad305"),

    /**
     * Additional properties for the element.
     */
    ADDITIONAL_PROPERTIES("additionalProperties", "map<string,string>", "Additional properties for the element.", null, "534b5665-73d4-4bdc-b83b-1a8fed19dba3"),

    /**
     * Unique identifier for the entity.
     */
    NAME("name", "string", "Name that the element is known as - not necessarily unique.", "MyAssetName", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),

    /**
     * Display name of the element used for summary tables and titles
     */
    DISPLAY_NAME("displayName", "string", "Display name of the element used for summary tables and titles.", "MyGlossaryTerm", "a42ceece-6cd8-4c71-9920-e3c96df3c8bb"),

    /**
     * Description of the element or associated resource in free-text.
     */
    DESCRIPTION("description", "string", "Description of the element or associated resource in free-text.", null, "ee09b6f9-e15b-40fb-9799-ef3b98c1de2c"),

    /**
     * Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.
     */
    VERSION_IDENTIFIER("versionIdentifier", "string", "Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.", "V1.0", "6e765a3f-04c2-4eca-bd2d-519cae777c03"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    DEPLOYED_IMPLEMENTATION_TYPE("deployedImplementationType", "string", "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "PostgreSQL Database Server", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Identifier of the person or process who is accountable for the proper management of the element or related resource.
     */
    OWNER("owner", "string", "Identifier of the person or process who is accountable for the proper management of the element or related resource.", null, "8b3e49d6-987c-4f2f-b624-ba91e5d28b33"),

    /**
     * Type of element that describes the owner.
     */
    OWNER_TYPE_NAME("ownerTypeName", "string", "Type of element that describes the owner.", "PersonRole", "c0acfbc6-4250-437b-9aa3-ce3d5a3bd490"),

    /**
     * Name of the property from the element used to identify the owner.
     */
    OWNER_PROPERTY_NAME("ownerPropertyName", "string", "Name of the property from the element used to identify the owner.", "qualifiedName", "9d1fdeb4-132a-4ec7-8312-dc9362e4f2f7"),

    /**
     * The list of governance zones that this asset belongs to.
     */
    ZONE_MEMBERSHIP("zoneMembership", "array<string>", "The list of governance zones that this asset belongs to.", null, "2af69520-6991-4097-aa94-543127b73066"),

    /**
     * Formula that describes the behaviour of the element.  May include placeholders for queryIds
     */
    FORMULA("formula", "string", "Formula that describes the behaviour of the element.  May include placeholders for queryIds.", null, "0b35fe46-1c16-4af7-b7b1-ea6951718dc7"),

    /**
     * Format of the expression provided in the formula attribute.
     */
    FORMULA_TYPE("formulaType", "string", "Format of the expression provided in the formula attribute.", "SQL", "dd9b24cb-0359-4915-8b6b-9eae251adc1c"),

    /**
     * Is this element visible to all, or only the author?
     */
    IS_PUBLIC("isPublic", "boolean",  "Is this element visible to all, or only the author?", "true", "e7a8eaa8-4358-4dd3-8d82-2ab3c118cfe4"),

    /**
     * Rating level provided.
     */
    STARS("stars", "StarRating", "Rating level provided.", "***", "896fd016-fad1-43dc-87a9-3ee577d8d898"),

    /**
     * Additional comments associated with the rating.
     */
    REVIEW("review", "string", "Additional comments associated with the rating.", null, "ae1ef693-6264-44ee-8014-23a283ad3b0e"),

    /**
     * Feedback comments or additional information.
     */
    TEXT("text", "string", "Feedback comments or additional information.", null, "baef44c7-e345-46ef-b8f7-a4d7fbcf77d7"),

    /**
     * Type of comment.
     */
    TYPE("type", "CommentType", "Type of comment.", "Answer", "465c7ed7-74d4-4eb3-9e4c-9d47f06a0ebd"),

    /**
     * Title
     */
    TITLE("title", "string", "Title", null, "c669dc73-3ae3-4350-95b7-4508a85bfc94"),

    /**
     * Descriptive name of the tag.
     */
    TAG_NAME("tagName", "string", "Descriptive name of the tag.", null, "c6054168-4236-4de1-9ece-ebcb5597dbf4"),

    /**
     * More detail on the meaning of the tag.
     */
    TAG_DESCRIPTION("tagDescription", "string", "More detail on the meaning of the tag.", null, "e8fc2fc9-afae-47cd-b5f0-f7c374fb6ea4"),

    /**
     * Description of the technique used to create the sample.
     */
    SAMPLING_METHOD("samplingMethod", "string", "Description of the technique used to create the sample.", null, "1cb00437-adde-4b71-a655-f736f2c8989d"),

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
    CLASSIFICATION_NAME("classificationName", "string", "The name of the associated classification.", "Ownership", "ef2169c4-c8f3-48b0-9051-cfb2bbb1e5f2"),

    /**
     * Name of the property in the classification where this value is used.
     */
    CLASSIFICATION_PROPERTY_NAME("classificationPropertyName", "string", "Name of the property in the classification where this value is used.", "owner", "5471ec69-a23d-4d89-b134-b30a8a01f435"),

    /**
     * If an attached entity or relationship to it changed, this is its unique identifier.
     */
    ATTACHMENT_GUID("attachmentGUID", "string", "If an attached entity or relationship to it changed, this is its unique identifier.", null, "74d62753-fd42-47c5-a804-29334f394728"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name.
     */
    ATTACHMENT_TYPE("attachmentType", "string", "If an attached entity or relationship to changed, this is its unique type name.", null, "b7f9f49a-e227-44d8-ae26-7e9c4f552379"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name of the relationship.
     */
    RELATIONSHIP_TYPE("relationshipType", "string", "If an attached entity or relationship to changed, this is its unique type name of the relationship.", null, "afa67874-fa00-422e-8902-69e5e3cc9e43"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER("user", "string", "The user identifier for the person/system executing the request.", null, "1afa1bb1-a9f0-4739-a45a-ff0d01b867f5"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER_ID("userId", "string", "The user identifier for the person/system executing the request.", null, "c65a21dc-d1ae-4a8f-ba33-58ec401c1b42"),

    /**
     * The unique identifier of the engine action that initiated the governance service that created this element.
     */
    ENGINE_ACTION_GUID("engineActionGUID", "string", "The unique identifier of the engine action that initiated the governance service that created this element.", null, "519cf063-9386-42e5-88dd-c5baab234df6"),

    /**
     * The unique identifier of the associated asset.
     */
    ASSET_GUID("assetGUID", "string", "The unique identifier of the associated asset.", null, "fd4b6779-9582-4cc2-885e-72e44445ff04"),

    /**
     * Description of the activity.
     */
    ACTION_DESCRIPTION("actionDescription", "string", "Description of the activity.", null, "2f0a4467-7b72-4dba-a345-d80959d8a3d5"),

    /**
     * The version number of the template element when the copy was created.
     */
    SOURCE_VERSION_NUMBER("sourceVersionNumber", "long", "The version number of the template element when the copy was created.", null, "4729488f-2e42-4801-bc17-9dfad508c1fa"),

    /**
     * The fully qualified physical location of the data store.
     */
    PATH_NAME("pathName", "string", "The fully qualified physical location of the data store.", null, "34d24b66-12f1-437c-afd3-1f1ab3377472"),

    /**
     * File type descriptor (or logical file type) typically extracted from the file name.
     */
    FILE_TYPE("fileType", "string", "File type descriptor (or logical file type) typically extracted from the file name.", null, "f180c49b-2de6-4657-bdf7-069747bc612d"),

    /**
     * The name of the file with extension.
     */
    FILE_NAME("fileName", "string", "The name of the file with extension.", null, "f22d0164-3790-42c3-aacb-154ccee7fbb6"),

    /**
     * The file extension used at the end of the file's name.  This identifies the format of the file.
     */
    FILE_EXTENSION("fileExtension", "string", "The file extension used at the end of the file's name.  This identifies the format of the file.", null, "62b00622-fa2c-4ee0-be60-3f15b1cb3dd6"),

    /**
     * The request type used to call the service.
     */
    REQUEST_TYPE("requestType", "string", "The request type used to call the service.", null, "be22cf20-f704-459e-823f-bdd8f7ef003b"),

    /**
     * The request type used to call the service.
     */
    REQUESTER_USER_ID("requesterUserId", "string", "User identity for the person, process or engine that requested this action.", null, "061de8f4-87a6-45b6-b91b-d375f202fe0f"),

    /**
     * Properties that configure the governance service for this type of request.
     */
    REQUEST_PARAMETERS("requestParameters", "map<string, string>", "Request parameters to pass to the governance service when called.", null, "882a5a30-3724-41ea-90ff-667cb7627bde"),

    /**
     * Lists the names of the request parameters to remove from the requestParameters supplied by the caller.
     */
    REQUEST_PARAMETER_FILTER("requestParameterFilter", "array<string>", "Lists the names of the request parameters to remove from the requestParameters supplied by the caller.", null, "9f7e13d1-ec51-4c95-9931-383b3ab9295b"),

    /**
     * Provides a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service.
     */
    REQUEST_PARAMETER_MAP("requestParameterMap", "map<string, string>", "Provides a translation map between the supplied names in the requestParameters and the names supported by the implementation of the governance service.", null, "64730138-399a-4f30-a0b8-1f6cc487cb53"),

    /**
     * Lists the names of the action targets to remove from the supplied action targets.
     */
    ACTION_TARGET_FILTER("actionTargetFilter", "array<string>", "Lists the names of the action targets to remove from the supplied action targets.", null, "91b5c627-2c3a-4598-af72-e1b52f1f03c5"),

    /**
     * Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.
     */
    ACTION_TARGET_MAP("actionTargetMap", "map<string, string>", "Provides a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.", null, "b848b720-4171-4d28-9e4c-1d34f51aaf5f"),

    /**
     * Request type supported by the governance service (overrides requestType on call to governance service if specified)
     */
    SERVICE_REQUEST_TYPE("serviceRequestType", "string", "Request type supported by the governance service (overrides requestType on call to governance service if specified).", null, "63238bb7-e935-4c38-8d0a-61917f01a31d"),

    /**
     * Unique name of the link type that connects the edge to the vertex.
     */
    LINK_TYPE_NAME("linkTypeName", "string", "Unique name of the link type that connects the edge to the vertex.", null, "4f275bc0-3a33-4f6a-96ee-cd7bd13ba579"),

    /**
     * If the end of a relationship is significant, set to 1 or 2 to indicate the desired end; otherwise use 0.
     */
    RELATIONSHIP_END("relationshipEnd", "int", "If the end of a relationship is significant, set to 1 or 2 to indicated the desired end; otherwise use 0.", null, "8b53224f-e330-4ded-9d18-da6517094994"),

    /**
     * Display name for the relationship end.
     */
    RELATIONSHIP_END_NAME("relationshipEndName", "string", "Display name for the relationship end.", null, "e98f95e3-316b-4200-b608-57d7836d8901"),

    /**
     * Open metadata type name for the associated schema type.
     */
    SCHEMA_TYPE_NAME("schemaTypeName", "string", "Open metadata type name for the associated schema type.", null, "4948ea82-387c-4a82-99ad-3e94bb5253b7"),

    /**
     * The name of a primitive data type.
     */
    DATA_TYPE("dataType", "string", "The name of a primitive data type.", null, "50e73f9f-10a0-4b41-9cb6-bf55630f3734"),

    /**
     * Position of the element in a collection of relationships.
     */
    POSITION("position", "int", "Position of the element in a collection of relationships.", null, "2fd62293-99e3-48f9-825f-e9b22d8470ae"),

    /**
     * Minimum number of allowed instances.
     */
    MIN_CARDINALITY("minCardinality", "int", "Minimum number of allowed instances.", null, "d3e13cd5-414f-4c82-94b8-e61dad64f7c3"),

    /**
     * Maximum number of allowed instances.
     */
    MAX_CARDINALITY("maxCardinality", "int", "Maximum number of allowed instances.", null, "5caa1b1a-590f-4a2e-85ad-260b64f4bbc1"),

    /**
     * Provides additional reasons, or expectations from the results.
     */
    PURPOSE("purpose", "string", "Provides additional reasons, or expectations from the results.  This is typically expressed in business terms", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Name of the type of annotation.
     */
    ANNOTATION_TYPE("annotationType", "string", "Name of the type of annotation.", null, "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Short description for summary tables.
     */
    SUMMARY("summary", "string", "Short description for summary tables.", null, "30719554-1a97-424f-ac05-f4e4e67bd278"),

    /**
     * Level of certainty in the accuracy of the results.
     */
    CONFIDENCE_LEVEL("confidenceLevel", "int", "Level of certainty in the accuracy of the results.", null, "29372374-38bb-472d-9d6e-529b68aed1e0"),

    /**
     * Expression used to create the annotation.
     */
    EXPRESSION("expression", "string", "Expression used to create the annotation.", null, "e130bc01-7c58-4799-8d8a-ae3ec659a064"),

    /**
     * Explanation of the analysis.
     */
    EXPLANATION("explanation", "string", "Explanation of the analysis.", null, "9445c89d-250f-464c-bb9c-744f25b7b7e1"),

    /**
     * Additional request parameters passed to the service.
     */
    ANALYSIS_PARAMETERS("analysisParameters", "map<string,string>", "Additional request parameters passed to the service.", null, "b3eb6d7f-9c52-44f0-99c3-359f52218c7e"),

    /**
     * The step in the analysis that produced the annotation.
     */
    ANALYSIS_STEP("analysisStep", "string", "The step in the analysis that produced the annotation.", null, "0b8d13b5-39eb-46d1-9ab0-1cc192697ff7"),

    /**
     * Additional properties used in the request.
     */
    JSON_PROPERTIES("jsonProperties", "map<string,string>", "Additional properties used in the specification.", null, "fe0c84c7-6f19-4c36-897f-9067447d0d9a"),

    /**
     * Date of the review.
     */
    REVIEW_DATE("reviewDate", "date", "Date of the review.", null, "7c19aa3a-5fbe-4455-928d-3330b21b22dd"),

    /**
     * User identifier for the steward performing the review.
     */
    STEWARD("steward", "string", "Unique identifier for the steward performing the action.", null, "6777fa1e-3289-4896-a032-1097b4ad78b2"),

    /**
     * Type name of the Actor entity identifying the steward.
     */
    STEWARD_TYPE_NAME("stewardTypeName", "string", "Type name of the Actor entity identifying the steward.", "Person", "b4c4637d-b196-4268-86a2-c6daf444dd7d"),

    /**
     * Property name for the steward's unique identifier (typically guid or qualifiedName).
     */
    STEWARD_PROPERTY_NAME("stewardPropertyName", "string", "Property name for the steward's unique identifier (typically guid or qualifiedName).", "guid", "b9e55ebe-43fb-4eb0-8485-6ef22e07f0a8"),

    /**
     * Notes on why decision were made relating to this element, and other useful information.
     */
    NOTES("notes", "string", "Notes on why decision were made relating to this element, and other useful information.", null, "577281c3-82f8-4c1d-ad95-04ef5919e57c"),

    /**
     * Notes provided by the steward.
     */
    COMMENT("comment", "string", "Notes provided by the steward.", null, "bb38c0ab-de6c-47d6-ae3a-d4848fd79c58"),

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
    START_DATE("startDate", "date", "Date/time that work started on this element.", null, "e3e374cc-0f9d-45f6-ae74-7d7a438b17bf"),

    /**
     * Date/time that work is expected to be complete for this element.
     */
    PLANNED_END_DATE("startDate", "date", "Date/time that work is expected to be complete for this element.", null, "330ae312-1e88-4c7b-810e-4b4a50e540e8"),

    /**
     * Date/time that work stopped on this element.
     */
    COMPLETION_DATE("completionDate", "date", "Date/time that work stopped on this element.", null, "28585eb7-ca9f-4149-b51f-ad29bbfe3f7c"),

    /**
     * The name to identify the action target to the actor that processes it.
     */
    ACTION_TARGET_NAME("actionTargetName", "string", "The name to identify the action target to the actor that processes it.", null, "3a5d325f-267c-4821-beb2-2c59d89891ed"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLE("example", "string", "An example of the described concept, element or value.", null, "b207bc5c-b5b5-43f1-94f8-41f4e2902ee5"),

    /**
     * An example of the described concept, element or value.
     */
    EXAMPLES("examples", "string", "Examples of this concept in use.", null, "29c50714-461f-4856-8fcd-30e5b9e7ebaf"),

    /**
     * How this concept name is abbreviated.
     */
    ABBREVIATION("abbreviation", "string", "How this glossary term is abbreviated.", null, "0a1eae14-718b-416c-a7d8-04d6f194d7dc"),

    /**
     * Visible version identifier.
     */
    PUBLISH_VERSION_ID("publishVersionIdentifier", "string", "Visible version identifier.", "V1.2", "cae42a1b-f752-4ca9-8d26-96829b2f591d"),

    /**
     * Is this value required?
     */
    REQUIRED("required", "boolean", "Is this value required?", null, "fb810cde-18a7-46a0-88a3-6a0fb69b2286"),

    /**
     * Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.
     */
    COMPLETION_MESSAGE("completionMessage", "string", "Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.", null, "f7633bda-9a90-4561-8d5a-356126a855ea"),

    /**
     * Name of the keyword.
     */
    KEYWORD("keyword", "string", "Name of the keyword.", null, "b15f3d92-c4fb-44f6-a773-986b0c02906b"),

    /**
     * Unique name of the process that initiated this request (if applicable).
     */
    PROCESS_NAME("processName", "string", "Unique name of the process that initiated this request (if applicable).", null, "9b3e9119-503e-42b4-b972-95169163539b"),

    /**
     * Unique identifier of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_GUID("processStepGUID", "string", "Unique identifier of the governance action process step that initiated this request (if applicable).", null, "69f3ec60-338b-4af4-9ebf-4139a33850a4"),

    /**
     * Unique name of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_NAME("processStepName", "string", "Unique name of the governance action process step that initiated this request (if applicable).", null, "5f851f84-c871-4ada-b8fa-629449f1ca7b"),

    /**
     * Unique identifier of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_GUID("governanceActionTypeGUID", "string", "Unique identifier of the governance action type that initiated this request (if applicable).", null, "73c3d8bb-05ec-40eb-9ff0-31de48407c14"),

    /**
     * Unique name of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_NAME("governanceActionTypeName", "string", "Unique name of the governance action type that initiated this request (if applicable).", null, "e1921f31-306c-4a11-b37c-67eaae2eefea"),

    /**
     * Display name for the discovered schema.
     */
    SCHEMA_NAME("schemaName", "string", "Display name for the discovered schema.", null, "c6011a00-b9c0-45d9-a525-88014aa3546f"),

    /**
     * Type name for the discovered schema.
     */
    SCHEMA_TYPE("schemaType", "string", "Type name for the discovered schema.", null, "6a68d174-fd29-49ef-aa4b-26205dbebb58"),

    /**
     * Length of the data field.
     */
    LENGTH("length", "int", "Length of the data field.", null, "35a17811-1635-4ab1-9a15-0f79bc771a27"),

    /**
     * Potential classification names and properties as JSON.
     */
    CANDIDATE_CLASSIFICATIONS("candidateClassifications", "map<string, string>", "Potential classification names and properties as JSON.", null, "3ea11266-368b-4c0d-ad4d-2943a14a38ad"),

    /**
     * Inferred data type based on the data values.
     */
    INFERRED_DATA_TYPE("inferredDataType", "string", "Inferred data type based on the data values.", null, "27c3d24d-8ea9-4daf-ab82-c813b3dfa01a"),

    /**
     * Inferred data format based on the data values.
     */
    INFERRED_FORMAT("inferredFormat", "string", "Inferred data format based on the data values.", null, "0198a639-1906-4377-b8ba-56cccbf88b3e"),

    /**
     * Inferred data field length based on the data values.
     */
    INFERRED_LENGTH("inferredLength", "int", "Inferred data field length based on the data values.", null, "e67b7353-20e0-46be-b912-e96192a200fc"),

    /**
     * Inferred precision of the data based on the data values.
     */
    INFERRED_PRECISION("inferredPrecision", "int", "Inferred precision of the data based on the data values.", null, "4ce73780-f940-4da6-bfcb-3b726ede2f7b"),

    /**
     * Inferred scale applied to the data based on the data values.
     */
    INFERRED_SCALE("inferredScale", "int", "Inferred scale applied to the data based on the data values.", null, "bd28ec07-01bd-4db4-9377-33b15097ded4"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_PROPERTY_NAMES("profilePropertyNames", "array<string>", "List of property names used in this annotation.", "[profileEndDate, profileCounts]", "210e2977-6803-4fce-93e8-15547168d459"),

    /**
     * Time at which the profiling started collecting data.
     */
    PROFILE_START_DATE("profileStartDate", "date", "Time at which the profiling started collecting data.", null, "6bee91ad-4eae-4292-ab28-ff1e5404e74d"),

    /**
     * Time at which the profiling stopped collecting data.
     */
    PROFILE_END_DATE("profileEndDate", "date", "Time at which the profiling stopped collecting data.", null, "9f31776f-f567-40aa-92cd-b990131dbdb8"),

    /**
     * Additional profile properties discovered during the analysis.
     */
    PROFILE_PROPERTIES("profileProperties", "map<string, string>", "Additional profile properties discovered during the analysis.", null, "53f5e89d-6730-4ae5-bfbf-62d4512eff58"),

    /**
     * Additional flags (booleans) discovered during the analysis.
     */
    PROFILE_FLAGS("profileFlags", "map<string, boolean>", "Additional flags (booleans) discovered during the analysis.", null, "b7b28f24-5464-4cab-8c6b-10bc11ed6118"),

    /**
     * Relevant dates discovered during the analysis.
     */
    PROFILE_DATES("profileDates", "map<string, date>", "Relevant dates discovered during the analysis.", null, "7d700cc5-b56c-4aeb-8416-7cf8d17d16c2"),

    /**
     * Additional counts discovered during the analysis.
     */
    PROFILE_COUNTS("profileCounts", "map<string, long>", "Additional counts discovered during the analysis.", null, "6550a7b0-4ab2-4188-b32f-c1576f4bcedc"),

    /**
     * Additional large counts discovered during the analysis.
     */
    PROFILE_DOUBLES("profileDoubles", "map<string, double>", "Additional large counts discovered during the analysis.", null, "e77933c9-64c6-476f-bd0f-e905838fb7fa"),

    /**
     * List of individual values in the data.
     */
    VALUE_LIST("valueList", "array<string>", "List of individual values in the data.", null, "6d67712d-d302-48b0-a05a-35d9d6da4380"),

    /**
     * Count of individual values in the data.
     */
    VALUE_COUNT("valueCount", "map<string, int>", "Count of individual values in the data.", null, "544a4595-7d96-465f-acc9-1b1cf472c671"),

    /**
     * Lowest value in the data.
     */
    VALUE_RANGE_FROM("valueRangeFrom", "string", "Lowest value in the data.", null, "8a5f6805-1014-4016-a94e-6d02236f6408"),

    /**
     * Highest value in the data.
     */
    VALUE_RANGE_TO("valueRangeTo", "string", "Highest value in the data.", null, "623c5733-80b4-41be-a9cc-4823c5ce5cf7"),

    /**
     * Typical value in the data.
     */
    AVERAGE_VALUE("averageValue", "string", "Typical value in the data.", null, "52ed8053-42b9-4d26-a1d5-c6aba915ccb1"),

    /**
     * List of possible matching data classes.
     */
    CANDIDATE_DATA_CLASS_GUIDS("candidateDataClassGUIDs", "array<string>", "List of possible matching data classes.", null, "e6d6f746-50a4-4e1c-b998-06de82f0a839"),

    /**
     * Number of values that match the data class specification.
     */
    MATCHING_VALUES("matchingValues", "long", "Number of values that match the data class specification.", null, "96c33d36-cff9-455e-944f-38224eb7448b"),

    /**
     * Number of values that do not match the data class specification.
     */
    NON_MATCHING_VALUES("nonMatchingValues", "long", "Number of values that do not match the data class specification.", null, "7bb6364d-486f-4005-97c9-941c7b7154d5"),

    /**
     * Suggested term based on the analysis.
     */
    INFORMAL_TERM("informalTerm", "string", "Suggested term based on the analysis.", null, "3a13654b-f671-4928-990a-96cfe8d25422"),

    /**
     * List of potentially matching glossary terms.
     */
    CANDIDATE_GLOSSARY_TERM_GUIDS("candidateGlossaryTermGUIDs", "array<string>", "List of potentially matching glossary terms.", null, "8be05385-75fe-42e2-a87f-2adb47289395"),

    /**
     * Suggested category based on the analysis.
     */
    INFORMAL_CATEGORY("informalCategory", "string", "Suggested category based on the analysis.", null, "15b33606-c660-43b8-b6fc-8b715aeac55f"),

    /**
     * List of potentially matching glossary categories.
     */
    CANDIDATE_GLOSSARY_CATEGORY_GUIDS("candidateGlossaryCategoryGUIDs", "array<string>", "List of potentially matching glossary categories.", null, "5b865d63-65b8-4d29-b382-e0490de041c9"),

    /**
     * Type of quality calculation.
     */
    QUALITY_DIMENSION("qualityDimension", "string", "Type of quality calculation.", null, "a865a7d9-b18c-4aae-853b-c429bd5f8c32"),

    /**
     * Calculated quality value.
     */
    QUALITY_SCORE("qualityScore", "int", "Calculated quality value.", null, "cdbe5189-771d-42a5-917a-42923dedaf89"),

    /**
     * Type name of the potential relationship.
     */
    RELATIONSHIP_TYPE_NAME("relationshipTypeName", "string", "Type name of the potential relationship.", null, "477c226d-ceb0-4f4a-87d2-f60632a9dbf2"),

    /**
     * Entity that should be linked to the asset being analyzed.
     */
    RELATED_ENTITY_GUID("relatedEntityGUID", "string", "Entity that should be linked to the asset being analyzed.", null, "d399931b-29f4-4039-bee5-4a68c1b68d28"),

    /**
     * Properties to add to the relationship.
     */
    RELATIONSHIP_PROPERTIES("relationshipProperties", "map<string, string>", "Properties to add to the relationship.", null, "d7cfc6d9-1b1a-4266-875a-b9f713d80643"),

    /**
     * Discovered properties of the data source.
     */
    RESOURCE_PROPERTIES("resourceProperties", "map<string, string>", "Discovered properties of the resource.", null, "c187fe2c-cee8-4c4b-99e7-dcb27032a88d"),

    /**
     * Name of the activity that revealed the need for action.
     */
    ACTION_SOURCE_NAME("actionSourceName", "string", "Name of the activity that revealed the need for action.", null, "dddf1c9e-d0d6-4812-a757-1d01c79f34ef"),

    /**
     * What needs to be done.
     */
    ACTION_REQUESTED("actionRequested", "string", "What needs to be done.", null, "3eca27fe-0538-43f1-83b9-cf9ee43a32ec"),

    /**
     * Additional information for use during action processing.
     */
    ACTION_PROPERTIES("actionProperties", "map<string, string>", "Additional information for use during action processing.", null, "b2302d78-f5e2-4fd7-8932-ad10ac868ab9"),

    /**
     * Creation time of the data store.
     */
    RESOURCE_CREATE_TIME("resourceCreateTime", "date", "Creation time of the data store.", null, "766d9f17-40f9-44da-a0c8-679002a0cf91"),

    /**
     * Last known modification time.
     */
    RESOURCE_UPDATE_TIME("resourceUpdateTime", "date", "Last known modification time.", null, "6012bdee-31d7-46d4-9f3b-f4d19c47e662"),

    /**
     * Last known modification time.
     */
    RESOURCE_LAST_ACCESSED_TIME("resourceLastAccessedTime", "date", "Last known access time.", null, "f884c0bf-f2d1-4cdb-81ff-0fc7cc7c945c"),

    /**
     * Creation time of the data store.
     */
    STORE_CREATE_TIME("storeCreateTime", "date", "Creation time of the data store.", null, "1a3abee2-f174-433d-8355-44c5810c471b"),

    /**
     * Last known modification time.
     */
    STORE_UPDATE_TIME("storeUpdateTime", "date", "Last known modification time.", null, "134bbbc3-4b68-4d35-a8da-85719cced8a9"),


    /**
     * Size of the data source.
     */
    SIZE("size", "int", "Size of the data source.", null, "c2d67f78-3387-4b83-9c1c-bd0aa88a4df0"),

    /**
     * Encoding scheme used on the data.
     */
    ENCODING("encoding", "string", "Type of encoding scheme used on the data.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Language used in the encoding.
     */
    ENCODING_LANGUAGE("encodingLanguage", "string", "Language used in the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Description of the encoding.
     */
    ENCODING_DESCRIPTION("encodingDescription", "string", "Description of the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Additional properties describing the encoding.
     */
    ENCODING_PROPERTIES("encodingProperties", "string", "Additional properties describing the encoding.", null, "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Identifier that describes the type of resource use.
     */
    RESOURCE_USE("resourceUse", "string", "Identifier that describes the type of resource use.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Description of how the resource is used, or why it is useful.
     */
    RESOURCE_USE_DESCRIPTION("resourceUseDescription", "string", "Description of how the resource is used, or why it is useful.", null, "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * Additional properties that explains how to use the resource.
     */
    RESOURCE_USE_PROPERTIES("resourceUseProperties", "map<string, string>", "Additional properties that explains how to use the resource.", null, "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Indicator whether the anchor should receive notifications of changes to the resource.
     */
    WATCH_RESOURCE("watchResource", "boolean", "Indicator whether the anchor should receive notifications of changes to the resource.", null, "d727b3ce-d58b-45d5-8abc-55b1394e030a"),

    /**
     * "Time that the IT Infrastructure was deployed."
     */
    DEPLOYMENT_TIME( "deploymentTime", "date", "Time that the IT Infrastructure was deployed.", null, "aa372f7f-8284-4e24-902e-9f01dcc70b3e"),

    /**
     * Type name of deployer.
     */
    DEPLOYER_TYPE_NAME( "deployerTypeName", "string", "Type name of deployer.", null, "cb027494-8de7-43cc-845c-57d4f0bbf6d5"),

    /**
     * Identifying property name of deployer.
     */
    DEPLOYER_PROPERTY_NAME( "deployerPropertyName", "string", "Identifying property name of deployer.", null, "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Person, organization or engine that deployed the IT Infrastructure.
     */
    DEPLOYER("deployer", "string", "Person, organization or engine that deployed the IT Infrastructure.", null, "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * The operational status of the software server capability on this software server.
     */
    OPERATIONAL_STATUS("operationalStatus", "OperationalStatus", "The operational status of the deployed infrastructure.", null, "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Type of the software capability.
     */
    CAPABILITY_TYPE("capabilityType", "string", "Type of the software capability.", null, "c8ef1b3a-63bf-4ca1-9ef8-475e2ad67a2b"),

    /**
     * Version number of the software capability.
     */
    CAPABILITY_VERSION("capabilityVersion", "string", "Version number of the software capability.", "V1.0", "7548df50-1553-46e1-a335-b782db998f88"),

    /**
     * Patch level of the software server capability.
     */
    PATCH_LEVEL("patchLevel", "string", "Patch level of the software server capability.", "456", "574bea24-8dd6-407a-bed4-aaa432d10276"),

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
    IDENTIFIER("identifier", "string", "Identifier used in an external system.", null, "2f831c53-0c71-4e03-92ed-20a899ed771f"),

    /**
     * The username of the person or process that created the instance in the external system.
     */
    EXT_INSTANCE_CREATED_BY("externalInstanceCreatedBy"   , "string", "The username of the person or process that created the instance in the external system.", null, "480a57d1-7300-4691-ad8b-540d97ef0870"),

    /**
     * The date/time when the instance in the external system was created.
     */
    EXT_INSTANCE_CREATION_TIME("externalInstanceCreationTime", "date", "The date/time when the instance in the external system was created.", null, "50e22b77-6091-4316-836e-2f76468add71"),

    /**
     * The username of the person or process that last updated the instance in the external system.
     */
    EXT_INSTANCE_LAST_UPDATED_BY("externalInstanceLastUpdatedBy", "string", "The username of the person or process that last updated the instance in the external system.", null, "f29f9183-e651-42d5-b0c8-bb1491f3151e"),

    /**
     * The date/time when the instance in the external system was last updated.
     */
    EXT_INSTANCE_LAST_UPDATE_TIME("externalInstanceLastUpdateTime", "date", "The date/time when the instance in the external system was last updated.", null, "0502401e-5292-4860-9fd7-dd2b0c46859d"),

    /**
     * The latest version of the element in the external system.
     */
    EXT_INSTANCE_VERSION("externalInstanceVersion" , "long", "The latest version of the element in the external system.", null, "349199e2-5781-4413-8550-c85241e20cc5"),

    /**
     * Additional properties to aid the mapping to the element in an external metadata source.
     */
    MAPPING_PROPERTIES("mappingProperties", "map<string, string>", "Additional properties to aid the mapping to the the element in an external metadata source.", null, "8161e120-993b-490c-bf66-cb9fd85192fc"),

    /**
     * Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.
     */
    LAST_SYNCHRONIZED("lastSynchronized", "date", "Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.", null, "45f5f2fc-17ec-4f02-8d7a-8cfe8f1557fe"),

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
    USAGE("usage"   , "string", "Guidance on how the element should be used.", null, "e92f8669-5a07-4130-9ad6-62aadca7a505"),

    /**
     * Details of where the element was sourced from.
     */
    SOURCE("source"   , "string", "Details of the organization, person or process that created the element, or provided the information used to create the element.", null, "9c40c4e3-1d6d-45fd-8df0-f1a2e09db636"),

    /**
     * Level of confidence in the correctness of the element.
     */
    CONFIDENCE("confidence"   , "int", "Level of confidence in the correctness of the element. 0=unknown; 1=low confidence; 100=total confidence.", "100", "26dd007a-cff3-45e7-963d-2a753c2b7000"),

    /**
     * The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.
     */
    USES_BLOCKING_CALLS("usesBlockingCalls", "boolean", "The integration connector needs to use blocking calls to a third party technology and so needs to run in its own thread.", "false", "cd23ea21-75b0-45d2-9292-e63510c3a1e2"),

    /**
     * Full name of the topic as used by programs to access its contents
     */
    TOPIC_NAME("topicName", "string", "Full name of the topic as used by programs to access its contents.", "egeria.omag.server.active-metadata-store.omas.assetconsumer.outTopic", "eda530d2-62d3-4325-9840-514f001ffc12"),

    /**
     * Type of topic.
     */
    TOPIC_TYPE("topicType", "string", "Type of topic.", "PLAINTEXT", "17eb67ae-6805-4f47-98a7-ed124804c9a6"),

    /**
     * The type of property that the valid value represents.
     */
    PROPERTY_TYPE("propertyType", "string", "The type of property that the valid value represents.", "producedGuard", "f9f2eba1-943a-4611-8bdd-647c1645b036"),

    /**
     * The phase in the lifecycle of the project.
     */
    PROJECT_PHASE("projectPhase", "string", "The phase in the lifecycle of the project.", "Plan", "9178fece-e112-4250-9c08-5c336bd93f78"),

    /**
     * Indicator on how well the project is tracking to plan.
     */
    PROJECT_HEALTH("projectHealth", "string", "Indicator on how well the project is tracking to plan.", "On Track", "eabe799d-72d7-46a2-aa56-5ceaf723a65f"),

    /**
     * Short description on current status of the project.
     */
    PROJECT_STATUS("projectStatus", "string", "Short description on current status of the project.", "Active", "39643f86-185b-465e-9e84-3f74905bad82"),

    /**
     * Descriptive name of the concept that this collection represents.
     */
    COLLECTION_TYPE("collectionType", "string", "Descriptive name of the concept that this collection represents.", "Product Contents", "05b7dcee-1c4c-4d2b-a484-bd84ec4d67e2"),

    /**
     * Display name of the product.
     */
    PRODUCT_STATUS("productStatus", "string",  "Lifecycle status of the product.", "DRAFT", "9aecda29-f1e1-4fe2-8066-2a9d094b990e"),

    /**
     * Display name of the product.
     */
    PRODUCT_NAME("productName", "string",  "Display name of the product.", "Teddy Bear Drop Foot Clinical Trial Patient Measurements", "aae2b086-05ea-433a-b1d6-502674c4ae6e"),

    /**
     * Type or category of the product.
     */
    PRODUCT_TYPE("productType", "string", "Type or category of the product.", "Files", "e64cd76f-f1b8-4c00-9ce9-87fcae8bd3d2"),

    /**
     * Date that the product was made available.
     */
    INTRODUCTION_DATE("introductionDate", "date", "Date that the product was made available.", null,  "9fffa5e8-f2d2-4184-be1d-482d7e093ec0"),

    /**
     * Date when is the next version is expected to be released.
     */
    NEXT_VERSION_DATE("nextVersionDate", "date",  "Date when is the next version is expected to be released.", null, "5866d607-157f-41f2-8875-00b60f534e99"),

    /**
     * Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.
     */
    WITHDRAW_DATE("withdrawDate", "date", "Date when the product is expected to be (or has been) withdrawn, preventing new consumers from subscribing.", null, "f2fc6a12-6bf9-4c2f-ad99-68c892b44e76"),

    /**
     * Level of maturity for the product.
     */
    MATURITY("maturity", "string", "Level of maturity for the product.", "InDev",  "666b4e71-cb40-4421-9291-0736cfa5136e"),

    /**
     * Length of time that the product is expected to be in service.
     */
    SERVICE_LIFE("serviceLife", "string", "Length of time that the product is expected to be in service.", "2 years", "f61776bc-e78e-4c4a-9b22-275c8eb4c132"),

    /**
     * Which is the current supported version that is recommended for consumers.  Specified as a versionIdentifier from the asset.
     */
    CURRENT_VERSION("currentVersion", "string", "Which is the current supported version that is recommended for consumers.  Specified as a versionIdentifier from the asset.", "V1.0", "d863a1f5-5770-4a57-a331-01ef59994fba"),

    /**
     * Defines the provenance and confidence that a member belongs in a collection.
     */
    MEMBERSHIP_STATUS("membershipStatus", CollectionMemberStatus.getOpenTypeName(), CollectionMemberStatus.getOpenTypeDescription(), CollectionMemberStatus.PROPOSED.getName(), "e304a92d-60d2-4605-8e3f-d338bd33e6d3"),

    /**
     * Description of how the member is used, or why it is useful in this collection.
     */
    MEMBERSHIP_RATIONALE("membershipRationale", "string", "Description of how the member is used, or why it is useful in this collection.", null, "f4c0da71-f8e8-4d05-a92c-5a1e6b4a263e"),

    /**
     * Defines the sequencing for a collection.
     */
    COLLECTION_ORDER("collectionOrder", OrderBy.getOpenTypeName(), OrderBy.getOpenTypeDescription(), OrderBy.DATE_ADDED.getName(), "068bf5f0-9caa-4e68-ad5e-e230b06b4a3a"),

    /**
     * Name of property to use for ordering.
     */
    ORDER_BY_PROPERTY_NAME("orderByPropertyName", "string", "Name of property to use for ordering.", OpenMetadataProperty.STEWARD.name, "31b6f74e-5759-47e1-8b92-a0ce43bc73db"),

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
    CONTACT_TYPE("contactType", "string", "Type of contact - such as home address, work mobile, emergency contact ...", ContactMethodType.EMAIL.getName(), "caa6fbd3-a470-473c-a656-0265c4496d54"),

    /**
     * Details of the contact method
     */
    CONTACT_METHOD_VALUE("contactMethodValue", "string", "Details of the contact method.", null, "ef793035-7111-4424-b776-6c6c84f38343"),

    /**
     * Details of the service supporting the contact method.
     */
    CONTACT_METHOD_SERVICE("contactMethodService", "string", "Details of the service supporting the contact method.", null, "55a9ed5a-c279-4dbc-895e-fda5c15000da"),


    /**
     * Defines the level of confidence to place in the accuracy of a data item.
     */
    CONFIDENCE_LEVEL_IDENTIFIER("levelIdentifier", "int", ConfidenceLevel.getOpenTypeDescription(), Integer.toString(ConfidenceLevel.AUTHORITATIVE.getOrdinal()), "449558d5-3811-401d-b2be-f131480f9bc8"),

    /**
     * Defines how important a data item is to the organization.
     */
    CRITICALITY_LEVEL_IDENTIFIER("levelIdentifier", "int", CriticalityLevel.getOpenTypeDescription(), Integer.toString(CriticalityLevel.IMPORTANT.getOrdinal()), "b2c99d60-4078-4d06-beec-f7fcc588c344"),

    /**
     * Defines how confidential a data item is.
     */
    CONFIDENTIALITY_LEVEL_IDENTIFIER("levelIdentifier", "int", ConfidentialityLevel.getOpenTypeDescription(), Integer.toString(ConfidentialityLevel.CONFIDENTIAL.getOrdinal()), "908488a2-13f2-469d-b28d-20a7af6be4a1"),

    /**
     * Defines the retention requirements associated with a data item.
     */
    RETENTION_BASIS_IDENTIFIER("basisIdentifier", "int", RetentionBasis.getOpenTypeDescription(), Integer.toString(RetentionBasis.PROJECT_LIFETIME.getOrdinal()), "68069d73-0e20-4cb7-a7ec-1555edbbc280"),

    /**
     * Defines the severity of the impact that a situation has.
     */
    SEVERITY_IDENTIFIER("severityIdentifier", "int", ImpactSeverity.getOpenTypeDescription(), Integer.toString(ImpactSeverity.MEDIUM.getOrdinal()), "f565af06-37a1-42e9-b680-2c98715f9ff3"),

    /**
     * Defines how important a data item is to the organization.
     */
    STATUS_IDENTIFIER("statusIdentifier", "int", GovernanceClassificationStatus.getOpenTypeDescription(), Integer.toString(GovernanceClassificationStatus.VALIDATED.getOrdinal()), "8279602a-c86d-4cf7-b66d-1ea98b75c491"),


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
    PRIORITY("priority", "int", "An indication of the relative position in which this work item should be tackled compared to others in the overall work list.", "10", "6168ae13-f6ee-49e7-9f21-693e7a401926"),

    /**
     * Metadata properties embedded in the media file.
     */
    EMBEDDED_METADATA("embeddedMetadata", "map<string,string>", "Metadata properties embedded in the media file.", null, "af5a6693-e14c-489e-a76c-1a45248e9dbd"),


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
