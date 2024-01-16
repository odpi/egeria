/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.mapper;

/**
 * Provides the definition for all properties defined in the open metadata types.
 */
public enum OpenMetadataProperty
{
    /**
     * Unique identifier of an open metadata entity or relationship.
     */
    GUID("guid", "string", "Unique identifier of an open metadata entity or relationship.", null),

    /**
     * Timestamp when the archive occurred or was detected.
     */
    ARCHIVE_DATE("archiveDate", "string", "Timestamp when the archive occurred or was detected.", "5235c1b1-fa03-4d17-80e2-7826242dfc75"),

    /**
     * Name of user that performed the archive - or detected the archive.
     */
    ARCHIVE_USER("archiveUser", "string", "Name of user that performed the archive - or detected the archive.", "fe81b808-3d78-4e84-a1de-b0b273a89bec"),

    /**
     * Name of process that performed the archive - or detected the archive.
     */
    ARCHIVE_PROCESS("archiveProcess", "string", "Name of process that performed the archive - or detected the archive.", "0aef1b3a-50dc-4a6c-981f-93bf8815e7f4"),

    /**
     * Name of service that created this classification.
     */
    ARCHIVE_SERVICE("archiveService", "string", "Name of service that created this classification.", "c2170958-998e-4d8c-8e33-e91ef4872c8a"),

    /**
     * Name of method that created this classification.
     */
    ARCHIVE_METHOD("archiveMethod", "string", "Name of method that created this classification.", "45d558ba-00b4-4a67-9226-590b100da7e9"),

    /**
     * Properties to locate the real-world counterpart in the archive.
     */
    ARCHIVE_PROPERTIES("archiveProperties", "string", "Properties to locate the real-world counterpart in the archive.", "d149d2d8-f181-4c20-8d68-5460f54bece5"),

    /**
     * The unique identifier of the referenceable that this element is directly or indirectly anchored to.
     */
    ANCHOR_GUID("anchorGUID", "string", "The unique identifier of the referenceable that this element is directly or indirectly anchored to.", "e7de4efc-6fa4-4942-97eb-e9b5be701875"),

    /**
     * Unique name of the type of the anchor.
     */
    ANCHOR_TYPE_NAME("anchorTypeName", "string", "Unique name of the type of the anchor.", "605c9dd5-fa79-4457-b299-9169c5567f97"),

    /**
     * Unique identifier for the entity.
     */
    QUALIFIED_NAME("qualifiedName", "string", "Unique identifier for the entity.", "e31e5b9b-0f96-42a9-8e67-0e3fc66ad305"),

    /**
     * Additional properties for the element.
     */
    ADDITIONAL_PROPERTIES("additionalProperties", "map<string,string>", "Additional properties for the element.", "534b5665-73d4-4bdc-b83b-1a8fed19dba3"),

    /**
     * Unique identifier for the entity.
     */
    NAME("name", "string", "Name that the element is known as - not necessarily unique.", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),

    /**
     * Display name of the element used for summary tables and titles
     */
    DISPLAY_NAME("displayName", "string", "Display name of the element used for summary tables and titles.", "a42ceece-6cd8-4c71-9920-e3c96df3c8bb"),

    /**
     * Description of the element or associated resource in free-text.
     */
    DESCRIPTION("description", "string", "Description of the element or associated resource in free-text.", "ee09b6f9-e15b-40fb-9799-ef3b98c1de2c"),

    /**
     * Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.
     */
    VERSION_IDENTIFIER("versionIdentifier", "string", "Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.", "6e765a3f-04c2-4eca-bd2d-519cae777c03"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    DEPLOYED_IMPLEMENTATION_TYPE("deployedImplementationType", "string", "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    @Deprecated
    TYPE("type", "string", "Deprecated property, use deployedImplementationType.", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Identifier of the person or process who is accountable for the proper management of the element or related resource.
     */
    OWNER("owner", "string", "Identifier of the person or process who is accountable for the proper management of the element or related resource.", "8b3e49d6-987c-4f2f-b624-ba91e5d28b33"),

    /**
     * Type of identifier used for owner property.
     */
    OWNER_TYPE("ownerType", "AssetOwnerType", "Type of identifier used for owner property.", "142b2bbf-0f2f-4c1e-b10e-850cd208b31c"),

    /**
     * The list of governance zones that this asset belongs to.
     */
    ZONE_MEMBERSHIP("zoneMembership", "array<string>", "The list of governance zones that this asset belongs to.", "2af69520-6991-4097-aa94-543127b73066"),

    /**
     * Description of the last change to the asset's metadata.
     */
    LATEST_CHANGE("latestChange", "string", "Description of the last change to the asset's metadata.", "756cf128-12e5-456e-97d7-843e7efec11b"),

    /**
     * Formula that describes the behaviour of the element.  May include placeholders for variables
     */
    FORMULA("formula", "string", "Formula that describes the behaviour of the element.  May include placeholders for variables.", "0b35fe46-1c16-4af7-b7b1-ea6951718dc7"),

    /**
     * Format of the expression provided in the formula attribute.
     */
    FORMULA_TYPE("formulaType", "string", "Format of the expression provided in the formula attribute.", "dd9b24cb-0359-4915-8b6b-9eae251adc1c"),

    /**
     * Is this element visible to all, or only the author?
     */
    IS_PUBLIC("isPublic", "boolean", "Is this element visible to all, or only the author?", "e7a8eaa8-4358-4dd3-8d82-2ab3c118cfe4"),

    /**
     * Description of the technique used to create the sample.
     */
    SAMPLING_METHOD("samplingMethod", "string", "Description of the technique used to create the sample.", "1cb00437-adde-4b71-a655-f736f2c8989d"),

    /**
     * The relationship of element that has been changed to the anchor.
     */
    CHANGE_TARGET("changeTarget", "LatestChangeTarget", "The relationship of element that has been changed to the anchor.", "b3a4c3c0-f17f-4380-848a-6c6d985c2dd3"),

    /**
     * The type of change.
     */
    CHANGE_ACTION("changeAction", "LatestChangeAction", "The type of change.", "365c4667-b86a-445f-860b-dc35eac917f2"),

    /**
     * The name of the associated classification.
     */
    CLASSIFICATION_NAME("classificationName", "string", "The name of the associated classification.", "ef2169c4-c8f3-48b0-9051-cfb2bbb1e5f2"),

    /**
     * Name of the property in the classification where this value is used.
     */
    CLASSIFICATION_PROPERTY_NAME("classificationPropertyName", "string", "Name of the property in the classification where this value is used.", "5471ec69-a23d-4d89-b134-b30a8a01f435"),

    /**
     * If an attached entity or relationship to it changed, this is its unique identifier.
     */
    ATTACHMENT_GUID("attachmentGUID", "string", "If an attached entity or relationship to it changed, this is its unique identifier.", "74d62753-fd42-47c5-a804-29334f394728"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name.
     */
    ATTACHMENT_TYPE("attachmentType", "string", "If an attached entity or relationship to changed, this is its unique type name.", "b7f9f49a-e227-44d8-ae26-7e9c4f552379"),

    /**
     * If an attached entity or relationship to changed, this is its unique type name of the relationship.
     */
    RELATIONSHIP_TYPE("relationshipType", "string", "If an attached entity or relationship to changed, this is its unique type name of the relationship.", "afa67874-fa00-422e-8902-69e5e3cc9e43"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER("user", "string", "The user identifier for the person/system executing the request.", "1afa1bb1-a9f0-4739-a45a-ff0d01b867f5"),

    /**
     * The user identifier for the person/system executing the request.
     */
    USER_ID("userId", "string", "The user identifier for the person/system executing the request.", "c65a21dc-d1ae-4a8f-ba33-58ec401c1b42"),

    /**
     * The unique identifier of the engine action that initiated the governance service that created this element.
     */
    ENGINE_ACTION_GUID("engineActionGUID", "string", "The unique identifier of the engine action that initiated the governance service that created this element.", "519cf063-9386-42e5-88dd-c5baab234df6"),

    /**
     * The unique identifier of the associated asset.
     */
    ASSET_GUID("assetGUID", "string", "The unique identifier of the associated asset.", "fd4b6779-9582-4cc2-885e-72e44445ff04"),

    /**
     * Description of the activity.
     */
    ACTION_DESCRIPTION("actionDescription", "string", "Description of the activity.", "2f0a4467-7b72-4dba-a345-d80959d8a3d5"),

    /**
     * The display name for the template to help requester choose the template to use.
     */
    TEMPLATE_NAME("templateName", "string", "The display name for the template to help requester choose the template to use.", "32c4a43e-cc81-4d85-af2e-97bd492a8893"),

    /**
     * The description of the template to help requester choose the template to use.
     */
    TEMPLATE_DESCRIPTION("templateDescription", "string", "The description of the template to help requester choose the template to use.", "1c537e66-a43e-42bc-94f7-055af7696c86"),

    /**
     * Map of attribute names to values that should be replaced in the template.  These attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities.
     */
    REPLACEMENT_PROPERTIES("replacementProperties", "map<string,string>", "Map of attribute names to values that should be replaced in the template.  These attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities.", "e4bc87ae-b15d-4fd6-ba33-2ed85f699244"),

    /**
     * Map of placeholder names to values that should be replaced in the template.
     */
    PLACEHOLDER_PROPERTIES("placeholderProperties", "map<string,string>", "Map of placeholder names to values that should be replaced in the template.", "f16227bb-61d7-4ea9-947c-a0415b541542"),

    /**
     * The version number of the template element when the copy was created.
     */
    SOURCE_VERSION_NUMBER("sourceVersionNumber", "long", "The version number of the template element when the copy was created.", "4729488f-2e42-4801-bc17-9dfad508c1fa"),

    /**
     * The fully qualified physical location of the data store.
     */
    PATH_NAME("pathName", "string", "The fully qualified physical location of the data store.", "34d24b66-12f1-437c-afd3-1f1ab3377472"),

    /**
     * File type descriptor (or logical file type) typically extracted from the file name.
     */
    FILE_TYPE("fileType", "string", "File type descriptor (or logical file type) typically extracted from the file name.", "f180c49b-2de6-4657-bdf7-069747bc612d"),

    /**
     * The name of the file with extension.
     */
    FILE_NAME("fileName", "string", "The name of the file with extension.", "f22d0164-3790-42c3-aacb-154ccee7fbb6"),

    /**
     * The file extension used at the end of the file's name.  This identifies the format of the file.
     */
    FILE_EXTENSION("fileExtension", "string", "The file extension used at the end of the file's name.  This identifies the format of the file.", "62b00622-fa2c-4ee0-be60-3f15b1cb3dd6"),

    /**
     * The request type used to call the service.
     */
    REQUEST_TYPE("requestType", "string", "The request type used to call the service.", "be22cf20-f704-459e-823f-bdd8f7ef003b"),

    /**
     * Properties that configure the governance service for this type of request.
     */
    REQUEST_PARAMETERS("requestParameters", "map<string, string>", "Request parameters to pass to the governance service when called.", "882a5a30-3724-41ea-90ff-667cb7627bde"),

    /**
     * Properties that configure the governance service for this type of request.
     */
    REQUEST_PARAMETER_FILTER("requestParameterFilter", "array<string>", "List the names of the request parameters to remove from the supplied requestParameters.", "9f7e13d1-ec51-4c95-9931-383b3ab9295b"),

    /**
     * Provide a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service.
     */
    REQUEST_PARAMETER_MAP("requestParameterMap", "map<string, string>", "Provide a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service.", "64730138-399a-4f30-a0b8-1f6cc487cb53"),

    /**
     * List the names of the action targets to remove from the supplied action targets.
     */
    ACTION_TARGET_FILTER("actionTargetFilter", "array<string>", "List the names of the action targets to remove from the supplied action targets.", "91b5c627-2c3a-4598-af72-e1b52f1f03c5"),

    /**
     * Provide a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.
     */
    ACTION_TARGET_MAP("actionTargetMap", "map<string, string>", "Provide a translation map between the supplied name of an action target and the name supported by the implementation of the governance service.", "b848b720-4171-4d28-9e4c-1d34f51aaf5f"),

    /**
     * Request type supported by the governance service (overrides requestType on call to governance service if specified)
     */
    SERVICE_REQUEST_TYPE("serviceRequestType", "string", "Request type supported by the governance service (overrides requestType on call to governance service if specified).", "63238bb7-e935-4c38-8d0a-61917f01a31d"),

    /**
     * Unique name of the link type that connects the edge to the vertex.
     */
    LINK_TYPE_NAME("linkTypeName", "string", "Unique name of the link type that connects the edge to the vertex.", "4f275bc0-3a33-4f6a-96ee-cd7bd13ba579"),

    /**
     * If the end of a relationship is significant set to 1 or 2 to indicated the end; otherwise use 0.
     */
    RELATIONSHIP_END("relationshipEnd", "int", "If the end of a relationship is significant set to 1 or 2 to indicated the end; otherwise use 0.", "8b53224f-e330-4ded-9d18-da6517094994"),

    /**
     * Display name for the relationship end.
     */
    RELATIONSHIP_END_NAME("relationshipEndName", "string", "Display name for the relationship end.", "e98f95e3-316b-4200-b608-57d7836d8901"),

    /**
     * Open metadata type name for the associated schema type.
     */
    SCHEMA_TYPE_NAME("schemaTypeName", "string", "Open metadata type name for the associated schema type.", "4948ea82-387c-4a82-99ad-3e94bb5253b7"),

    /**
     * The name of a primitive data type.
     */
    DATA_TYPE("dataType", "string", "The name of a primitive data type.", "50e73f9f-10a0-4b41-9cb6-bf55630f3734"),

    /**
     * Position of the element in a collection of relationships.
     */
    POSITION("position", "int", "Position of the element in a collection of relationships.", "2fd62293-99e3-48f9-825f-e9b22d8470ae"),

    /**
     * Minimum number of allowed instances.
     */
    MIN_CARDINALITY("minCardinality", "int", "Minimum number of allowed instances.", "d3e13cd5-414f-4c82-94b8-e61dad64f7c3"),

    /**
     * Maximum number of allowed instances.
     */
    MAX_CARDINALITY("maxCardinality", "int", "Maximum number of allowed instances.", "5caa1b1a-590f-4a2e-85ad-260b64f4bbc1"),

    /**
     * Provides additional reasons, or expectations from the results.
     */
    PURPOSE("purpose", "string", "Provides additional reasons, or expectations from the results.", "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Name of the type of annotation.
     */
    ANNOTATION_TYPE("annotationType", "string", "Name of the type of annotation.", "be802acc-3324-4c32-9b4a-69746a9b3018"),

    /**
     * Short description for summary tables.
     */
    SUMMARY("summary", "string", "Short description for summary tables.", "30719554-1a97-424f-ac05-f4e4e67bd278"),

    /**
     * Level of certainty in the accuracy of the results.
     */
    CONFIDENCE_LEVEL("confidenceLevel", "int", "Level of certainty in the accuracy of the results.", "29372374-38bb-472d-9d6e-529b68aed1e0"),

    /**
     * Expression used to create the annotation.
     */
    EXPRESSION("expression", "string", "Expression used to create the annotation.", "e130bc01-7c58-4799-8d8a-ae3ec659a064"),

    /**
     * Explanation of the analysis.
     */
    EXPLANATION("explanation", "string", "Explanation of the analysis.", "9445c89d-250f-464c-bb9c-744f25b7b7e1"),

    /**
     * Additional request parameters passed to the service.
     */
    ANALYSIS_PARAMETERS("analysisParameters", "map<string,string>", "Additional request parameters passed to the service.", "b3eb6d7f-9c52-44f0-99c3-359f52218c7e"),

    /**
     * The step in the analysis that produced the annotation.
     */
    ANALYSIS_STEP("analysisStep", "string", "The step in the analysis that produced the annotation.", "0b8d13b5-39eb-46d1-9ab0-1cc192697ff7"),

    /**
     * Additional properties used in the request.
     */
    JSON_PROPERTIES("jsonProperties", "map<string,string>", "Additional properties used in the specification.", "fe0c84c7-6f19-4c36-897f-9067447d0d9a"),

    /**
     * Date of the review.
     */
    REVIEW_DATE("reviewDate", "date", "Date of the review.", "7c19aa3a-5fbe-4455-928d-3330b21b22dd"),

    /**
     * User identifier for the steward performing the review.
     */
    STEWARD("Steward", "string", "User identifier for the steward performing the review.", "6777fa1e-3289-4896-a032-1097b4ad78b2"),

    /**
     * Notes provided by the steward.
     */
    COMMENT("comment", "string", "Notes provided by the steward.", "bb38c0ab-de6c-47d6-ae3a-d4848fd79c58"),

    /**
     * Status of the processing as a result of the annotation.
     */
    ANNOTATION_STATUS("annotationStatus", "AnnotationStatus", "Status of the processing as a result of the annotation.", "d36618bf-99fc-474f-a958-e8c64cd715ee"),

    /**
     * The status of the work on this element.
     */
    STATUS("status", "ToDoStatus", "The status of the work on this element.", "bcd570a4-03af-4569-ba34-72b823ba01c5"),

    /**
     * Date/time that work started on this element.
     */
    START_DATE("startDate", "date", "Date/time that work started on this element.", "e3e374cc-0f9d-45f6-ae74-7d7a438b17bf"),

    /**
     * Date/time that work stopped on this element.
     */
    COMPLETION_DATE("completionDate", "date", "Date/time that work stopped on this element.", "28585eb7-ca9f-4149-b51f-ad29bbfe3f7c"),

    /**
     * The name to identify the action target to the actor that processes it.
     */
    ACTION_TARGET_NAME("actionTargetName", "string", "The name to identify the action target to the actor that processes it.", "3a5d325f-267c-4821-beb2-2c59d89891ed"),

    /**
     * Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.
     */
    COMPLETION_MESSAGE("completionMessage", "string", "Message to provide additional information on the results of acting on the target by the actor or the reasons for any failures.", "f7633bda-9a90-4561-8d5a-356126a855ea"),

    /**
     * Name of the keyword.
     */
    KEYWORD("keyword", "string", "Name of the keyword.", "b15f3d92-c4fb-44f6-a773-986b0c02906b"),

    /**
     * Unique name of the process that initiated this request (if applicable).
     */
    PROCESS_NAME("processName", "string", "Unique name of the process that initiated this request (if applicable).", "9b3e9119-503e-42b4-b972-95169163539b"),

    /**
     * Unique identifier of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_GUID("processStepGUID", "string", "Unique identifier of the governance action process step that initiated this request (if applicable).", "69f3ec60-338b-4af4-9ebf-4139a33850a4"),

    /**
     * Unique name of the governance action process step that initiated this request (if applicable).
     */
    PROCESS_STEP_NAME("processStepName", "string", "Unique name of the governance action process step that initiated this request (if applicable).", "5f851f84-c871-4ada-b8fa-629449f1ca7b"),

    /**
     * Unique identifier of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_GUID("governanceActionTypeGUID", "string", "Unique identifier of the governance action type that initiated this request (if applicable).", "73c3d8bb-05ec-40eb-9ff0-31de48407c14"),

    /**
     * Unique name of the governance action type that initiated this request (if applicable).
     */
    GOVERNANCE_ACTION_TYPE_NAME("governanceActionTypeName", "string", "Unique name of the governance action type that initiated this request (if applicable).", "e1921f31-306c-4a11-b37c-67eaae2eefea"),

    /**
     * Display name for the discovered schema.
     */
    SCHEMA_NAME("schemaName", "string", "Display name for the discovered schema.", "c6011a00-b9c0-45d9-a525-88014aa3546f"),

    /**
     * Type name for the discovered schema.
     */
    SCHEMA_TYPE("schemaType", "string", "Type name for the discovered schema.", "6a68d174-fd29-49ef-aa4b-26205dbebb58"),

    /**
     * Length of the data field.
     */
    LENGTH("length", "int", "Length of the data field.", "35a17811-1635-4ab1-9a15-0f79bc771a27"),

    /**
     * Potential classification names and properties as JSON.
     */
    CANDIDATE_CLASSIFICATIONS("candidateClassifications", "map<string, string>", "Potential classification names and properties as JSON.", "3ea11266-368b-4c0d-ad4d-2943a14a38ad"),

    /**
     * Inferred data type based on the data values.
     */
    INFERRED_DATA_TYPE("inferredDataType", "string", "Inferred data type based on the data values.", "27c3d24d-8ea9-4daf-ab82-c813b3dfa01a"),

    /**
     * Inferred data format based on the data values.
     */
    INFERRED_FORMAT("inferredFormat", "string", "Inferred data format based on the data values.", "0198a639-1906-4377-b8ba-56cccbf88b3e"),

    /**
     * Inferred data field length based on the data values.
     */
    INFERRED_LENGTH("inferredLength", "int", "Inferred data field length based on the data values.", "e67b7353-20e0-46be-b912-e96192a200fc"),

    /**
     * Inferred precision of the data based on the data values.
     */
    INFERRED_PRECISION("inferredPrecision", "int", "Inferred precision of the data based on the data values.", "4ce73780-f940-4da6-bfcb-3b726ede2f7b"),

    /**
     * Inferred scale applied to the data based on the data values.
     */
    INFERRED_SCALE("inferredScale", "int", "Inferred scale applied to the data based on the data values.", "bd28ec07-01bd-4db4-9377-33b15097ded4"),

    /**
     * Additional profile properties discovered during the analysis.
     */
    PROFILE_PROPERTIES("profileProperties", "map<string, string>", "Additional profile properties discovered during the analysis.", "53f5e89d-6730-4ae5-bfbf-62d4512eff58"),

    /**
     * Additional flags (booleans) discovered during the analysis.
     */
    PROFILE_FLAGS("profileFlags", "map<string, boolean>", "Additional flags (booleans) discovered during the analysis.", "b7b28f24-5464-4cab-8c6b-10bc11ed6118"),

    /**
     * Additional counts discovered during the analysis.
     */
    PROFILE_COUNTS("profileCounts", "map<string, long>", "Additional counts discovered during the analysis.", "6550a7b0-4ab2-4188-b32f-c1576f4bcedc"),

    /**
     * List of individual values in the data.
     */
    VALUE_LIST("valueList", "array<string>", "List of individual values in the data.", "6d67712d-d302-48b0-a05a-35d9d6da4380"),

    /**
     * Count of individual values in the data.
     */
    VALUE_COUNT("valueCount", "map<string, int>", "Count of individual values in the data.", "544a4595-7d96-465f-acc9-1b1cf472c671"),

    /**
     * Lowest value in the data.
     */
    VALUE_RANGE_FROM("valueRangeFrom", "string", "Lowest value in the data.", "8a5f6805-1014-4016-a94e-6d02236f6408"),

    /**
     * Highest value in the data.
     */
    VALUE_RANGE_TO("valueRangeTo", "string", "Highest value in the data.", "623c5733-80b4-41be-a9cc-4823c5ce5cf7"),

    /**
     * Typical value in the data.
     */
    AVERAGE_VALUE("averageValue", "string", "Typical value in the data.", "52ed8053-42b9-4d26-a1d5-c6aba915ccb1"),

    /**
     * List of possible matching data classes.
     */
    CANDIDATE_DATA_CLASS_GUIDS("candidateDataClassGUIDs", "array<string>", "List of possible matching data classes.", "e6d6f746-50a4-4e1c-b998-06de82f0a839"),

    /**
     * Number of values that match the data class specification.
     */
    MATCHING_VALUES("matchingValues", "long", "Number of values that match the data class specification.", "96c33d36-cff9-455e-944f-38224eb7448b"),

    /**
     * Number of values that do not match the data class specification.
     */
    NON_MATCHING_VALUES("nonMatchingValues", "long", "Number of values that do not match the data class specification.", "7bb6364d-486f-4005-97c9-941c7b7154d5"),

    /**
     * Suggested term based on the analysis.
     */
    INFORMAL_TERM("informalTerm", "string", "Suggested term based on the analysis.", "3a13654b-f671-4928-990a-96cfe8d25422"),

    /**
     * List of potentially matching glossary terms.
     */
    CANDIDATE_GLOSSARY_TERM_GUIDS("candidateGlossaryTermGUIDs", "array<string>", "List of potentially matching glossary terms.", "8be05385-75fe-42e2-a87f-2adb47289395"),

    /**
     * Suggested category based on the analysis.
     */
    INFORMAL_CATEGORY("informalCategory", "string", "Suggested category based on the analysis.", "15b33606-c660-43b8-b6fc-8b715aeac55f"),

    /**
     * List of potentially matching glossary categories.
     */
    CANDIDATE_GLOSSARY_CATEGORY_GUIDS("candidateGlossaryCategoryGUIDs", "array<string>", "List of potentially matching glossary categories.", "5b865d63-65b8-4d29-b382-e0490de041c9"),

    /**
     * Type of quality calculation.
     */
    QUALITY_DIMENSION("qualityDimension", "string", "Type of quality calculation.", "a865a7d9-b18c-4aae-853b-c429bd5f8c32"),

    /**
     * Calculated quality value.
     */
    QUALITY_SCORE("qualityScore", "int", "Calculated quality value.", "cdbe5189-771d-42a5-917a-42923dedaf89"),

    /**
     * Type name of the potential relationship.
     */
    RELATIONSHIP_TYPE_NAME("relationshipTypeName", "string", "Type name of the potential relationship.", "477c226d-ceb0-4f4a-87d2-f60632a9dbf2"),

    /**
     * Entity that should be linked to the asset being analyzed.
     */
    RELATED_ENTITY_GUID("relatedEntityGUID", "string", "Entity that should be linked to the asset being analyzed.", "d399931b-29f4-4039-bee5-4a68c1b68d28"),

    /**
     * Properties to add to the relationship.
     */
    RELATIONSHIP_PROPERTIES("relationshipProperties", "map<string, string>", "Properties to add to the relationship.", "d7cfc6d9-1b1a-4266-875a-b9f713d80643"),

    /**
     * Discovered properties of the data source.
     */
    DATA_SOURCE_PROPERTIES("dataSourceProperties", "map<string, string>", "Discovered properties of the data source.", "c187fe2c-cee8-4c4b-99e7-dcb27032a88d"),

    /**
     * Name of the activity that revealed the need for action.
     */
    ACTION_SOURCE_NAME("actionSourceName", "string", "Name of the activity that revealed the need for action.", "dddf1c9e-d0d6-4812-a757-1d01c79f34ef"),

    /**
     * What needs to be done.
     */
    ACTION_REQUESTED("actionRequested", "string", "What needs to be done.", "3eca27fe-0538-43f1-83b9-cf9ee43a32ec"),

    /**
     * Additional information for use during action processing.
     */
    ACTION_PROPERTIES("actionProperties", "map<string, string>", "Additional information for use during action processing.", "b2302d78-f5e2-4fd7-8932-ad10ac868ab9"),

    /**
     * Creation time of the data store.
     */
    SOURCE_CREATE_TIME("sourceCreateTime", "date", "Creation time of the data store.", "766d9f17-40f9-44da-a0c8-679002a0cf91"),

    /**
     * Last known modification time.
     */
    SOURCE_UPDATE_TIME("sourceUpdateTime", "date", "Last known modification time.", "6012bdee-31d7-46d4-9f3b-f4d19c47e662"),

    /**
     * Size of the data source.
     */
    SIZE("size", "int", "Size of the data source.", "c2d67f78-3387-4b83-9c1c-bd0aa88a4df0"),

    /**
     * Encoding scheme used on the data.
     */
    ENCODING("encoding", "string", "Encoding scheme used on the data.", "7ebb8847-5ed3-4881-bee4-0e28c16613b8"),

    /**
     * Identifier that describes the type of resource use.
     */
    RESOURCE_USE("resourceUse", "string", "Identifier that describes the type of resource use.", "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Description of how the resource is used, or why it is useful.
     */
    RESOURCE_USE_DESCRIPTION("resourceUseDescription", "string", "Description of how the resource is used, or why it is useful.", "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * Additional properties that explains how to use the resource.
     */
    RESOURCE_USE_PROPERTIES("resourceUseProperties", "map<string, string>", "Additional properties that explains how to use the resource.", "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * Indicator whether the anchor should receive notifications of changes to the resource.
     */
    WATCH_RESOURCE("watchResource", "boolean", "Indicator whether the anchor should receive notifications of changes to the resource.", "d727b3ce-d58b-45d5-8abc-55b1394e030a"),

    /**
     * "Time that the IT Infrastructure was deployed."
     */
    DEPLOYMENT_TIME( "deploymentTime", "date", "Time that the IT Infrastructure was deployed.", "aa372f7f-8284-4e24-902e-9f01dcc70b3e"),

    /**
     * Type name of deployer.
     */
    DEPLOYER_TYPE_NAME( "deployerTypeName", "string", "Type name of deployer.", "cb027494-8de7-43cc-845c-57d4f0bbf6d5"),

    /**
     * Identifying property name of deployer.
     */
    DEPLOYER_PROPERTY_NAME( "deployerPropertyName", "string", "Identifying property name of deployer.", "152aafd9-57c4-4341-82bb-945d213a686e"),

    /**
     * Person, organization or engine that deployed the IT Infrastructure.
     */
    DEPLOYER("deployer", "string", "Person, organization or engine that deployed the IT Infrastructure.", "c579fd34-4144-4968-b0d9-fa17bd81ca9c"),

    /**
     * The operational status of the software server capability on this software server.
     */
    SERVER_CAPABILITY_STATUS("serverCapabilityStatus", "OperationalStatus", "The operational status of the software server capability on this software server.", "30c86736-c31a-4f29-8451-0c849f730a0b"),

    /**
     * The operational status of the the IT Infrastructure on the specific destination.
     */
    DEPLOYMENT_STATUS("deploymentStatus", "OperationalStatus", "The operational status of the the IT Infrastructure on the specific destination.", "30c86736-c31a-4f29-8451-0c849f730a0b"),

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
     * Unique identifier of description valid value.
     */
    public final String descriptionGUID;


    /**
     * Constructor.
     *
     * @param name            property name
     * @param type            property type
     * @param description     property description
     * @param descriptionGUID unique identifier of the valid value describing this property
     */
    OpenMetadataProperty(String name,
                         String type,
                         String description,
                         String descriptionGUID)
    {
        this.name            = name;
        this.type            = type;
        this.description     = description;
        this.descriptionGUID = descriptionGUID;
    }
}
