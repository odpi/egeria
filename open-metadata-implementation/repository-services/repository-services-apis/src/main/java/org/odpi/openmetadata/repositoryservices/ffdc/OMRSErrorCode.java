/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OMRSErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMRS
 * It is used in conjunction with all OMRS Exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA. Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500: internal error</li>
 *         <li>501: not implemented </li>
 *         <li>503: Service not available</li>
 *         <li>400: invalid parameters</li>
 *         <li>401: unauthorized</li>
 *         <li>404: not found</li>
 *         <li>405: method not allowed</li>
 *         <li>409: data conflict errors, for example an item is already defined</li>
 *     </ul></li>
 *     <li>Error Message Id: to uniquely identify the message</li>
 *     <li>Error Message Text: includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction: describes the result of the error</li>
 *     <li>UserAction: describes how a user should correct the error</li>
 * </ul>
 */
public enum OMRSErrorCode implements ExceptionMessageSet
{
    /**
     * OMRS-REPOSITORY-400-001 - Unable to delete the TypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2}
     */
    TYPEDEF_IN_USE(400, "OMRS-REPOSITORY-400-001",
            "Unable to delete the TypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2}",
            "The system is unable to delete the TypeDef because there are still instances in the metadata repository that are using it.",
            "Remove the existing instances from the open metadata repositories and try the delete again."),

    /**
     * OMRS-REPOSITORY-400-002 - Unable to delete the AttributeTypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2}
     */
    ATTRIBUTE_TYPEDEF_IN_USE(400, "OMRS-REPOSITORY-400-002",
            "Unable to delete the AttributeTypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2}",
            "The system is unable to delete the AttributeTypeDef because there are still instances in the metadata repository that are using it.",
            "Remove the instances that have properties of this type from the open metadata repositories and try the delete again."),

    /**
     * OMRS-REPOSITORY-400-003 - Unable to add the TypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}
     */
    TYPEDEF_ALREADY_DEFINED(400, "OMRS-REPOSITORY-400-003",
            "Unable to add the TypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}",
            "The system is unable to add the TypeDef to its repository because it is already defined.",
            "Validate that the existing type definition is as required.  It is possible to patch the TypeDef, or delete it and re-define it."),

    /**
     * OMRS-REPOSITORY-400-004 - Unable to add the AttributeTypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}
     */
    ATTRIBUTE_TYPEDEF_ALREADY_DEFINED(400, "OMRS-REPOSITORY-400-004",
            "Unable to add the AttributeTypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}",
            "The system is unable to delete the AttributeTypeDef because because it is already defined.",
            "Validate that the existing attribute type definition is as required.  It is not possible to patch the AttributeTypeDef so " +
                                              "re-define it with a new name."),

    /**
     * OMRS-REPOSITORY-400-005 - Classification {0} is not a recognized classification type by open metadata repository {1}
     */
    UNKNOWN_CLASSIFICATION(400, "OMRS-REPOSITORY-400-005",
            "Classification {0} is not a recognized classification type by open metadata repository {1}",
            "The system is unable to create a new classification for an entity because the open metadata repository does not recognize the classification type.",
            "Create a ClassificationDef for the classification and retry the request."),

    /**
     * OMRS-REPOSITORY-400-006 - Open metadata repository {0} is unable to assign a classification of type {1} to an entity of type {2} as the classification type is not valid for this type of entity
     */
    INVALID_CLASSIFICATION_FOR_ENTITY(400, "OMRS-REPOSITORY-400-006",
            "Open metadata repository {0} is unable to assign a classification of type {1} to an entity of type {2} as the classification type is not valid for this type of entity",
            "The system is not able to classify an entity since the ClassificationDef for the classification does not list the entity type, or one of its super-types.",
            "Update the ClassificationDef to include the entity's type and rerun the request. Alternatively use a different classification."),

    /**
     * OMRS-REPOSITORY-400-007 - A null TypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_TYPEDEF_NAME(400, "OMRS-REPOSITORY-400-007",
            "A null TypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the TypeDef name is needed.",
            "Correct the caller's code to include the TypeDef name and retry the request."),

    /**
     * OMRS-REPOSITORY-400-008 - A null AttributeTypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_ATTRIBUTE_TYPEDEF_NAME(400, "OMRS-REPOSITORY-400-008",
            "A null AttributeTypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the AttributeTypeDef name is needed.",
            "Correct the caller's code to include the AttributeTypeDef name and retry the request."),

    /**
     * OMRS-REPOSITORY-400-009 - A null TypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_TYPEDEF_CATEGORY(400, "OMRS-REPOSITORY-400-009",
            "A null TypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the TypeDef category is needed.",
            "Correct the caller's code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-010 - A null AttributeTypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_ATTRIBUTE_TYPEDEF_CATEGORY(400, "OMRS-REPOSITORY-400-010",
            "A null AttributeTypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the AttributeTypeDef category is needed.",
            "Fix the caller's code and try the request again."),

    /**
     * OMRS-REPOSITORY-400-011 - A null list of match criteria properties has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_MATCH_CRITERIA(400, "OMRS-REPOSITORY-400-011",
            "A null list of match criteria properties has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the match criteria is needed.",
            "Correct the calling code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-012 - Null values for all the parameters describing an external id for a standard has been passed on a {0} request to open metadata repository {1}
     */
    NO_EXTERNAL_ID(400, "OMRS-REPOSITORY-400-012",
            "Null values for all the parameters describing an external id for a standard has been passed on a {0} request to open metadata repository {1}",
            "The system is unable to perform the request because at least one of the values are needed.",
            "Correct the caller's code and repeat the request."),

    /**
     * OMRS-REPOSITORY-400-013 - A null search criteria has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_SEARCH_CRITERIA(400, "OMRS-REPOSITORY-400-013",
            "A null search criteria has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the search criteria is needed.",
            "Correct the caller's code and repeat the request again."),

    /**
     * OMRS-REPOSITORY-400-014 - A null unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_GUID(400, "OMRS-REPOSITORY-400-014",
            "A null unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the unique identifier is needed.",
            "Fix the calling code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-015 - A null TypeDef has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_TYPEDEF(400, "OMRS-REPOSITORY-400-015",
            "A null TypeDef has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the TypeDef is needed.",
            "Correct the calling code and try the request again."),

    /**
     * OMRS-REPOSITORY-400-016 - A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_ATTRIBUTE_TYPEDEF(400, "OMRS-REPOSITORY-400-016",
            "A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the AttributeTypeDef is needed.",
            "Correct the caller's source code and try the request once done."),

    /**
     * OMRS-REPOSITORY-400-017 - An invalid TypeDef {0} (guid={1}) has been passed as the {2} parameter on a {3} request to open metadata repository {4}. Full TypeDef is {5}
     */
    INVALID_TYPEDEF(400, "OMRS-REPOSITORY-400-017",
            "An invalid TypeDef {0} (guid={1}) has been passed as the {2} parameter on a {3} request to open metadata repository {4}. Full TypeDef is {5}",
            "The system is not able to perform the request because the TypeDef is needed.",
            "Correct the caller's source code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-018 - An invalid AttributeTypeDef {0} (guid={1}) has been passed as the {2} parameter on a {3} request to open metadata repository {4}. Full AttributeTypeDef is {5}
     */
    INVALID_ATTRIBUTE_TYPEDEF(400, "OMRS-REPOSITORY-400-018",
            "An invalid AttributeTypeDef {0} (guid={1}) has been passed as the {2} parameter on a {3} request to open metadata repository {4}. Full AttributeTypeDef is {5}",
            "The system is unable to perform the request as the AttributeTypeDef is needed.",
            "Correct the caller's source code and repeat the request."),

    /**
     * OMRS-REPOSITORY-400-019 - A null TypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}
     */
    NULL_TYPEDEF(400, "OMRS-REPOSITORY-400-019",
            "A null TypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}",
            "The system is unable to perform the request because the TypeDef is needed to perform the operation.",
            "Fix the invoking code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-020 - A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}
     */
    NULL_ATTRIBUTE_TYPEDEF(400, "OMRS-REPOSITORY-400-020",
            "A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}",
            "The system is unable to perform the request because the AttributeTypeDef is needed to perform the operation.",
            "Correct the invoking source code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-021 - A null TypeDefGalleryResponse object has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_TYPEDEF_GALLERY(400, "OMRS-REPOSITORY-400-021",
            "A null TypeDefGalleryResponse object has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the TypeDefGalleryResponse should contain the information to process.",
            "Fix the invocation of this call and retry the request."),

    /**
     * OMRS-REPOSITORY-400-022 - A null unique identifier (guid) for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_TYPEDEF_IDENTIFIER(400, "OMRS-REPOSITORY-400-022",
            "A null unique identifier (guid) for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the identifier for the TypeDef is needed to proceed.",
            "Fix the invocation in the caller's code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-023 - A null unique name for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_TYPEDEF_NAME(400, "OMRS-REPOSITORY-400-023",
            "A null unique name for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is not able to perform the request because the identifier for the TypeDef is needed to proceed.",
            "Fix the invocation in the caller's code and repeat the request."),

    /**
     * OMRS-REPOSITORY-400-024 - A null unique identifier (guid) for a AttributeTypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_ATTRIBUTE_TYPEDEF_IDENTIFIER(400, "OMRS-REPOSITORY-400-024",
            "A null unique identifier (guid) for a AttributeTypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the identifier for the AttributeTypeDef is needed to proceed.",
            "Correct the caller's code and try the request again."),

    /**
     * OMRS-REPOSITORY-400-025 - Local metadata repository has not initialized correctly because it was unable to create its metadata collection
     */
    NULL_METADATA_COLLECTION(400, "OMRS-REPOSITORY-400-025",
            "Local metadata repository has not initialized correctly because it was unable to create its metadata collection",
            "The metadata collection object provides access to the storage, or remote metadata service that is supporting the repository.  The system is unable to process requests for this repository without a metadata collection.",
            "The repository connector for the local repository has not initialized correctly.  This may be an " +
                    "error in the repository connector's logic, or a missing or incorrect property in the connector's connection object stored in " +
                    "the server's configuration document, or a missing resource, or permission needed by the connector.  The repository connector should " +
                    "have output diagnostics either through an exception or message to the audit log that details the problem.  If no other diagnostics " +
                    "are present, contact the developers of the repository connector to request that the diagnostics are improved, particularly " +
                    "around initialization.  Use the diagnostics from the connector to diagnose the root cause of the problem and then correct " +
                    "either the repository connector's logic, or its configuration or runtime environment as appropriate."),

    /**
     * OMRS-REPOSITORY-400-026 - A null classification name has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_CLASSIFICATION_NAME(400, "OMRS-REPOSITORY-400-026",
            "A null classification name has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to access the local metadata repository.",
            "The classification name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-027 - A null username has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_USER_ID(400, "OMRS-REPOSITORY-400-027",
            "A null user name has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is not able to access the local metadata repository.",
            "The user name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-028 - A property called {0} has been proposed for a new metadata instance of category {1} and type {2}; it is not supported for this type in open metadata repository {3}
     */
    BAD_PROPERTY_FOR_TYPE(400, "OMRS-REPOSITORY-400-028",
            "A property called {0} has been proposed for a new metadata instance of category {1} and type {2}; it is not supported for this type in open metadata repository {3}",
            "The system is unable to store the metadata instance in the metadata repository because the properties listed do not match the supplied type definition.",
            "Verify that the property name is spelt correctly and the correct type has been used. Correct the call to the metadata repository and retry."),

    /**
     * OMRS-REPOSITORY-400-029 - Properties have been proposed for a new metadata instance of category {0} and type {1}; properties not supported for this type in open metadata repository {2}
     */
    NO_PROPERTIES_FOR_TYPE(400, "OMRS-REPOSITORY-400-029",
            "Properties have been proposed for a new metadata instance of category {0} and type {1}; properties not supported for this type in open metadata repository {2}",
            "The system is unable to store the metadata instance in the metadata repository as the properties listed do not match the supplied type definition.",
            "Verify that the property name is spelt correctly and the correct type has been used. Fix the call to the metadata repository and retry."),

    /**
     * OMRS-REPOSITORY-400-030 - A property called {0} of type {1} has been proposed for a new metadata instance of category {2} and type {3}; this property should be of type {4} in open metadata repository {5}
     */
    BAD_PROPERTY_TYPE(400, "OMRS-REPOSITORY-400-030",
            "A property called {0} of type {1} has been proposed for a new metadata instance of category {2} and type {3}; this property should be of type {4} in open metadata repository {5}",
            "The system is unable to store the metadata instance in the metadata repository since the properties listed do not match the supplied type definition.",
            "Check that the property name is spelt correctly and the correct type has been used. Correct the call to the metadata repository and retry."),

    /**
     * OMRS-REPOSITORY-400-031 - A null property name has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_PROPERTY_NAME_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-031",
            "A null property name has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to process the metadata instance.",
            "The property name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-032 - A null property value has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_PROPERTY_VALUE_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-032",
            "A null property value has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is not able to process the metadata instance.",
            "The property value is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-033 - A null property type has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_PROPERTY_TYPE_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-033",
            "A null property type has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to correctly process the metadata instance.",
            "The property type is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-034 - A invalid TypeDef unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    BAD_TYPEDEF_ID_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-034",
            "A invalid TypeDef unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to perform the request because the unique identifier is required.",
            "Fix the caller's code and try the request again when done."),

    /**
     * OMRS-REPOSITORY-400-035 - An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} but this status is not valid for an instance of type {4}
     */
    BAD_INSTANCE_STATUS(400, "OMRS-REPOSITORY-400-035",
            "An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} but this status is not valid for an instance of type {4}",
            "The system is unable to process this request.",
            "The instance status is supplied by the caller to the API. This call needs to be corrected before the server can complete this operation successfully."),

    /**
     * OMRS-REPOSITORY-400-036 - A null instance status has been passed as the {0} parameter on a {1} request to open metadata repository {2}
     */
    NULL_INSTANCE_STATUS(400, "OMRS-REPOSITORY-400-036",
            "A null instance status has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to process the metadata instance request.",
            "The instance status is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-037 - No properties have been passed on the {0} parameter on a {1} request to open metadata repository {2}
     */
    NO_NEW_PROPERTIES(400, "OMRS-REPOSITORY-400-037",
            "No properties have been passed on the {0} parameter on a {1} request to open metadata repository {2}",
            "The system is unable to process this metadata instance request.",
            "The instance status is supplied by the caller to the API. This call needs to be fixed before the server can operate correctly."),

    /**
     * OMRS-REPOSITORY-400-038 - A future time of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}
     */
    REPOSITORY_NOT_CRYSTAL_BALL(400, "OMRS-REPOSITORY-400-038",
            "A future time of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}",
            "The system declines to process the request lest it gives away its secret powers.",
            "The asOfTime is supplied by the caller to the API. This call needs to be corrected before the server will function correctly."),

    /**
     * OMRS-REPOSITORY-400-039 - Incompatible TypeDef unique identifiers (name={0}, guid{1}) have been passed on a {2} request for instance {3} to open metadata repository {4}
     */
    BAD_TYPEDEF_IDS_FOR_DELETE(400, "OMRS-REPOSITORY-400-039",
            "Incompatible TypeDef unique identifiers (name={0}, guid{1}) have been passed on a {2} request for instance {3} to open metadata " +
                    "repository {4}",
            "The system is unable to perform the request because the unique identifiers are needed.",
            "Correct the caller's code to provide compatible type identifiers and retry the request."),

    /**
     * OMRS-REPOSITORY-400-040 - Unexpected exception {0} occurred when comparing properties against a search string of {1} during the {2} operation from {3}. Error message was {4}
     */
    BAD_PROPERTY_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-040",
            "Unexpected exception {0} occurred when comparing properties against a search string of {1} during the {2} operation from {3}. Error message was {4}",
            "The system is unable to perform the request because the unique identifier must be provided.",
            "Correct the error in the requesting code and retry."),

    /**
     * OMRS-REPOSITORY-400-041 - A null reference instance has been passed to repository {0} during the {1} in the {2} parameter
     */
    NULL_REFERENCE_INSTANCE(400, "OMRS-REPOSITORY-400-041",
            "A null reference instance has been passed to repository {0} during the {1} in the {2} parameter",
            "The system is unable to perform the request because the instance is needed.",
            "The reference instance comes from another server.  Look for errors in the audit log and validate that the message passing " +
                                    "protocol levels are compatible. If nothing is obviously wrong with the set up, " +
                                    "raise a Github issue or ask for help on the dev mailing list."),

    /**
     * OMRS-REPOSITORY-400-043 - A null entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation
     */
    NULL_ENTITY_PROXY(400, "OMRS-REPOSITORY-400-043",
            "A null entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation",
            "The system is unable to perform the request because the entity proxy is needed.",
            "Correct the caller's code to supply the entity proxy and retry the request."),

    /**
     * OMRS-REPOSITORY-400-044 - An entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation which has the local repository as its home
     */
    LOCAL_ENTITY_PROXY(400, "OMRS-REPOSITORY-400-044",
            "An entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation which has the local repository as its home",
            "The system is unable to perform the request because the entity proxy should represent an entity that is homed in another repository.",
            "Correct the bug in the caller's code and retry the request."),

    /**
     * OMRS-REPOSITORY-400-045 - A {0} request has been made to repository {1} for an instance {2} that is already deleted
     */
    INSTANCE_ALREADY_DELETED(400, "OMRS-REPOSITORY-400-045",
            "A {0} request has been made to repository {1} for an instance {2} that is already deleted",
            "The system is unable to perform the request because the instance is in the wrong state.",
            "Try a different request or a different instance."),

    /**
     * OMRS-REPOSITORY-400-046 - A {0} request has been made to repository {1} for an instance {2} that is not deleted
     */
    INSTANCE_NOT_DELETED(400, "OMRS-REPOSITORY-400-046",
            "A {0} request has been made to repository {1} for an instance {2} that is not deleted",
            "The system is unable to perform the request since the instance is in the wrong state.",
            "Try again with a different request or specify a different instance."),

    /**
     * OMRS-REPOSITORY-400-047 - A {0} request has been made to repository {1} for a relationship that has one or more ends of the wrong or invalid type.
     * Relationship type is {2}; entity proxy {3} for end 1 is of type {4} rather than {5} and entity proxy {6} for end 2 is of type {7} rather than {8}
     */
    INVALID_RELATIONSHIP_ENDS(400, "OMRS-REPOSITORY-400-047",
            "A {0} request has been made to repository {1} for a relationship that has one or more ends of the wrong or invalid type.  " +
                    "Relationship type is {2}; entity proxy {3} for end 1 is of type {4} rather than {5} and entity proxy {6} for end 2 is of type {7} rather than {8}",
            "The system is unable to perform the request because the instance has invalid values.",
            "Correct the caller's code and attempt the request again."),

    /**
     * OMRS-REPOSITORY-400-048 - A {0} request has been made to repository {1} to access a non-existent classification {2} from entity {3}
     */
    ENTITY_NOT_CLASSIFIED(400, "OMRS-REPOSITORY-400-048",
            "A {0} request has been made to repository {1} to access a non-existent classification {2} from entity {3}",
            "The system is unable to perform the request as the instance has a missing classification.",
            "Correct the caller's code and reattempt the request."),

    /**
     * OMRS-REPOSITORY-400-049 - A null TypeDef patch has been passed on the {0} operation of repository {1}
     */
    NULL_TYPEDEF_PATCH(400, "OMRS-REPOSITORY-400-049",
            "A null TypeDef patch has been passed on the {0} operation of repository {1}",
            "The system is unable to perform the request because it needs the patch to process the update.",
            "Correct the calling code and reattempt the request."),

    /**
     * OMRS-REPOSITORY-400-050 - A negative pageSize of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}
     */
    NEGATIVE_PAGE_SIZE(400, "OMRS-REPOSITORY-400-050",
            "A negative pageSize of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}",
            "The system is unable to process the request.",
            "The pageSize parameter is supplied by the caller to the API. This call needs to be corrected before the server will operate correctly."),

    /**
     * OMRS-REPOSITORY-400-051 - A request for entity {0} has been passed to repository {1} as the {2} parameter of the {3} operation but only an entity proxy has been found
     */
    ENTITY_PROXY_ONLY(400, "OMRS-REPOSITORY-400-051",
            "A request for entity {0} has been passed to repository {1} as the {2} parameter of the {3} operation but only an entity proxy has been found",
            "The system is unable to return all the details of the entity.  It can only supply an entity summary.",
            "The fact that the system has a proxy means that the entity exists in one of the members of the connected cohorts.  " +
                              "The repository where it is located may be unavailable, or the entity has been deleted " +
                              "but the delete request has not propagated through to this repository."),

    /**
     * OMRS-REPOSITORY-400-052 - The entity {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}
     */
    INVALID_ENTITY_FROM_STORE(400, "OMRS-REPOSITORY-400-052",
            "The entity {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}",
            "The system is unable to continue processing the request.",
            "This error suggests there is a logic error in either this repository, or the home repository for the instance.  Raise a Github issue to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-053 - The relationship {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}
     */
    INVALID_RELATIONSHIP_FROM_STORE(400, "OMRS-REPOSITORY-400-053",
            "The relationship {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}",
            "The system is not able to continue processing the request.",
            "This error suggests there is a logic error in either this repository, or the home repository for this instance.  Raise a Github issue to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-054 - The element {0} retrieved from repository {1} during the {2} operation has a null metadata collection id in its header: {3}
     */
    NULL_INSTANCE_METADATA_COLLECTION_ID(400, "OMRS-REPOSITORY-400-054",
            "The element {0} retrieved from repository {1} during the {2} operation has a null metadata collection id in its header: {3}",
            "The system is unable to process the request further because the element has an invalid header.",
            "This error suggests there is a logic error in either this repository, or the home repository for the instance.  Open a Github issue to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-055 - An unexpected {0} exception was received from a repository connector during the {1} operation which had message: {2}
     */
    UNEXPECTED_EXCEPTION_FROM_COHORT(400, "OMRS-REPOSITORY-400-055",
            "An unexpected {0} exception was received from a repository connector during the {1} operation which had message: {2}",
            "The system can can not continue processing the request.",
            "This error suggests there is a logic error in either this repository, or the home repository for the instance.  Open up a Github issue to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-056 - The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for instance {1} located in metadata collection {2}
     */
    NO_HOME_FOR_INSTANCE(400, "OMRS-REPOSITORY-400-056",
            "The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for instance {1} located in metadata collection {2}",
            "The system is unable to proceed with processing this request.",
            "This error suggests there is a logic error in either this repository, or the home repository for the instance.  Raise a Github issue in order to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-057 - The OMRS repository connector operation {0} does not allow a null value for {1} from {2}
     */
    NULL_AS_OF_TIME(400, "OMRS-REPOSITORY-400-057",
            "The OMRS repository connector operation {0} does not allow a null value for {1} from {2}",
            "The system is unable continue processing the request because there is an error with the caller of the method.",
            "Correct the code in the caller's method and retry the request."),

    /**
     * OMRS-REPOSITORY-400-058 - An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} however this status is not valid for an instance of type {4}
     */
    BAD_DELETE_INSTANCE_STATUS(400, "OMRS-REPOSITORY-400-058",
            "An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} however this status is not valid for an instance of type {4}",
            "The system is unable to process the request because this status is only for use by the open metadata repository services (OMRS).",
            "The instance status is supplied by the caller to the API. This call needs to be changed to either a delete of purge request."),

    /**
     * OMRS-REPOSITORY-400-059 - Type definition with guid {0} and name {1} conflicts with an existing type definition in open metadata repository {2}
     */
    VERIFY_CONFLICT_DETECTED(400, "OMRS-REPOSITORY-400-059",
            "Type definition with guid {0} and name {1} conflicts with an existing type definition in open metadata repository {2}",
            "The system is unable to verify the supplied type definition because has different values from the type already defined in the repository.",
            "Review the supplied and stored types to locate the conflict and then ensure they are aligned by patching or deleting one of the type definitions."),

    /**
     * OMRS-REPOSITORY-400-060 - The repository helper method {0} has been called with a null parameter
     */
    NULL_PARAMETER(400, "OMRS-REPOSITORY-400-060",
            "The repository helper method {0} has been called with a null parameter",
            "The system is unable to process the request because it needs the parameter value.",
            "This is probably a logic error in Egeria. Raise a git issue to get this investigated and fixed."),

    /**
     * OMRS-REPOSITORY-400-061 - An invalid instance has been detected by repository helper method {0}.  The instance is {1}
     */
    INVALID_INSTANCE(400, "OMRS-REPOSITORY-400-061",
            "An invalid instance has been detected by repository helper method {0}.  The instance is {1}",
            "The system is unable to work with the supplied instance because key values are missing from its contents.",
            "This is probably a logic error in Egeria. Raise a GitHub issue to get this investigated and fixed."),

    /**
     * OMRS-REPOSITORY-400-062 - An unexpected {0} exception was caught by {1}; error message was {2}
     */
    UNEXPECTED_EXCEPTION(400, "OMRS-REPOSITORY-400-062",
            "An unexpected {0} exception was caught by {1}; error message was {2}",
            "The system is not able to take action on the request.",
            "Review the error message and other diagnostics created at the same time."),

    /**
     * OMRS-REPOSITORY-400-063 - Method {0} is unable to request a refresh of instance {1} as it is a local member of metadata collection {2} in repository {3}
     */
    HOME_REFRESH(400, "OMRS-REPOSITORY-400-063",
            "Method {0} is unable to request a refresh of instance {1} as it is a local member of metadata collection {2} in repository {3}",
            "The system can not continue to action the request.",
            "Review the error messages and other diagnostics created at the same time."),

    /**
     * OMRS-REPOSITORY-400-064 - Method {0} is unable to locate an instance with guid {1} in the archive
     */
    UNKNOWN_GUID(400, "OMRS-REPOSITORY-400-064",
            "Method {0} is unable to locate an instance with guid {1} in the archive",
            "The system is unable to process the incoming request.",
            "Check the error message and other diagnostics created at the same time."),

    /**
     * OMRS-REPOSITORY-400-065 - Method {0} is unable to accept the new type definition {1} from {2} because it has a header version of {3} which is greater than this repository can support ({4})
     */
    UNSUPPORTED_TYPE_HEADER_VERSION(400, "OMRS-REPOSITORY-400-065",
            "Method {0} is unable to accept the new type definition {1} from {2} because it has a header version of {3} which is greater than this repository can support ({4})",
            "The system will not to process this request.",
            "The repository is sharing metadata with a repository of greater capability and the local repository is unable to work with its types.  It may be time to upgrade the local repository."),

    /**
     * OMRS-REPOSITORY-400-066 - Method {0} is unable to accept the new {1} instance from {2} with guid {3} and type {4} because it has a header version of {5} which is greater than this repository can support ({6})
     */
    UNSUPPORTED_INSTANCE_HEADER_VERSION(400, "OMRS-REPOSITORY-400-066",
            "Method {0} is unable to accept the new {1} instance from {2} with guid {3} and type {4} because it has a header version of {5} which is greater than this repository can support ({6})",
            "The system is unable to process the call.",
            "The repository is sharing metadata with a repository of higher capability and the local repository is unable to work with its types.  It may be time to upgrade the local repository."),

    /**
     * OMRS-REPOSITORY-400-067 - Method {0} has detected invalid version values in TypeDef patch from {1}. The updateToVersion {2} is
     * less than the applyToVersion {3}.  This is the contents of the patch {4}
     */
    INVALID_PATCH_VERSION(400, "OMRS-REPOSITORY-400-067",
            "Method {0} has detected invalid version values in TypeDef patch from {1}. The updateToVersion {2} is less " +
                                  "than the applyToVersion {3}.  This is the contents of the patch {4}",
            "The system is unable to process the patch because it is invalid.",
            "Correct the source of the patch and then try reloading it."),

    /**
     * OMRS-REPOSITORY-400-068 - Method {0} has detected that a TypeDef patch from {1} is for a future level from the active TypeDef.
     * The applyToVersion is {2} and the active TypeDef's version is {3}. This is the contents of the patch {4}
     */
    INCOMPATIBLE_PATCH_VERSION(400, "OMRS-REPOSITORY-400-068",
            "Method {0} has detected that a TypeDef patch from {1} is for a future level from the active TypeDef.  The applyToVersion is {2} " +
                                  "and the active TypeDef's version is {3}. This is the contents of the patch {4}",
            "The system is unable to process the patch because it is for a future version of the type.  This means there is at least one missing " +
                                       "patch that needs to be applied first",
            "Locate and load the previous versions of the patch and then try reloading this one."),

    /**
     * OMRS-REPOSITORY-400-069 - Method {0} has detected that a TypeDef patch from {1} has the mandatory field {2} set to null which is invalid.
     * This is the contents of the patch {3}
     */
    NULL_MANDATORY_PATCH_FIELD(400, "OMRS-REPOSITORY-400-069",
            "Method {0} has detected that a TypeDef patch from {1} has the mandatory field {2} set to null which is invalid. This is " +
                                  "the contents of the patch {3}",
            "The system is unable to process the patch as it is invalid.",
            "Correct the source of the patch and then try loading it again."),

    /**
     * OMRS-REPOSITORY-400-070 - Method {0} has detected that a TypeDef patch from {1} attempts to change the type of property {2} from {3} to {4}.
     * This is the contents of the patch {5}
     */
    INCOMPATIBLE_PROPERTY_PATCH(400, "OMRS-REPOSITORY-400-070",
            "Method {0} has detected that a TypeDef patch from {1} attempts to change the type of property {2} " +
                                       "from {3} to {4}. This is the contents of the patch {5}",
            "The system is unable to process the patch since it is invalid.",
            "Correct the source of the patch and then try reloading it again."),

    /**
     * OMRS-REPOSITORY-400-071 - The Open Metadata Repository Services (OMRS) has been called to initialize with no audit log destinations defined for server {0}
     */
    NO_AUDIT_LOG_DESTINATIONS(400, "OMRS-REPOSITORY-400-071",
            "The Open Metadata Repository Services (OMRS) has been called to initialize with no audit log destinations defined for server {0}",
            "The local server is unable to continue without an audit log.",
            "Add at least one audit log destination to the server configuration."),

    /**
     * OMRS-REPOSITORY-400-072 - The Open Metadata Repository Services (OMRS) has been called to initialize its subsystems for server {0}
     * before the audit log is initialized
     */
    NULL_AUDIT_LOG(400, "OMRS-REPOSITORY-400-072",
            "The Open Metadata Repository Services (OMRS) has been called to initialize its subsystems for server {0} before the audit log is " +
                           "initialized",
            "The local server can not continue without an audit log.",
             "Review and correct the start up sequence of the server."),

    /**
     * OMRS-REPOSITORY-400-073 - An invalid instance was found in a batch of reference instances send by a remote member of the cohort.
     * The exception was {0} with message {1}
     */
    INVALID_INSTANCES(400, "OMRS-REPOSITORY-400-073",
            "An invalid instance was found in a batch of reference instances send by a remote member of the cohort. " +
                    "The exception was {0} with message {1}",
            "The instances that appear earlier in the batch have been processed.  However the server will not process any more " +
                              "of the batch in case there are other problems with it.",
            "Review the instances from the event (passed as additional information on this log message) to determine the source of " +
                              "the error and its resolution."),

    /**
     * OMRS-REPOSITORY-400-074 - An invalid list of property-based search conditions was provided:
     * nestedConditions is mutually exclusive with property, operator, value.
     */
    INVALID_PROPERTY_SEARCH(400, "OMRS-REPOSITORY-400-074",
            "An invalid list of property-based search conditions was provided: nestedConditions is mutually exclusive with property, operator, value",
            "The system is unable to process the requested search because the provided options are mutually-exclusive.",
            "Review the request payload and provide only a nestedConditions or a property, operator, value payload for each property-based condition object in the list of conditions."),

    /**
     * OMRS-REPOSITORY-400-075 - An invalid list of classification-based search conditions was provided: name of the classification is mandatory
     */
    INVALID_CLASSIFICATION_SEARCH(400, "OMRS-REPOSITORY-400-075",
            "An invalid list of classification-based search conditions was provided: name of the classification is mandatory",
            "The system is unable to process the requested search because a classification name was not provided.",
            "Review the request payload and provide at least a classification name for each classification-based condition object in the list of conditions."),

    /**
     * OMRS-REPOSITORY-400-076 - An invalid list was provided for the value of an IN operator
     */
    INVALID_LIST_CONDITION(400, "OMRS-REPOSITORY-400-076",
            "An invalid list was provided for the value of an IN operator",
            "The system is unable to process the requested search because an ArrayPropertyValue is required as the value for the IN operator.",
            "Review the request payload and ensure that an ArrayPropertyValue is provided when using the IN operator."),

    /**
     * OMRS-REPOSITORY-400-077 - An invalid string was provided for the value of a LIKE operator
     */
    INVALID_LIKE_CONDITION(400, "OMRS-REPOSITORY-400-077",
            "An invalid string was provided for the value of a LIKE operator",
            "The system is unable to process the requested search because only a string is permitted as the value for the LIKE operator.",
            "Review the request payload and ensure that a PrimitivePropertyValue of type OM_PRIMITIVE_TYPE_STRING is provided when using the LIKE operator."),

    /**
     * OMRS-REPOSITORY-400-078 - An invalid string was provided for the value of the {0} operator
     */
    INVALID_NUMERIC_CONDITION(400, "OMRS-REPOSITORY-400-078",
            "An invalid string was provided for the value of the {0} operator",
            "The system is unable to process the requested search because only a date or number is permitted as the value for the provided operator.",
            "Review the request payload and ensure that a PrimitivePropertyValue of a date or numeric type is provided when using the provided operator."),

    /**
     * OMRS-REPOSITORY-400-079 - The provided subtype {0} is not a subtype of typedef {1}
     */
    TYPEDEF_NOT_SUBTYPE(400, "OMRS-REPOSITORY-400-079",
            "The provided subtype {0} is not a subtype of typedef {1}",
            "The system is unable to process the requested search because the specified subtype is not a known subtype of the provided type.",
            "Review the request payload and ensure that the list of subtypes includes only valid subtypes for the provided entity type."),

    /**
     * OMRS-REPOSITORY-400-080 - Classification {0} is not a supported classification type in open metadata repository {1}
     */
    UNSUPPORTED_CLASSIFICATION(400, "OMRS-REPOSITORY-400-080",
            "Classification {0} is not a supported classification type in open metadata repository {1}",
            "The supplied classification is valid.  However, the system is unable to maintain the " +
                                       "classification for an entity in this repository because it does not support the classification type.  " +
                                       "The system will attempt to store the classification in another member of the cohort",
            "Ensure there is at least one repository in the cohort that supports this classification type."),

    /**
     * OMRS-REPOSITORY-400-081 - A {0} request has been made to repository {1} to add a classification {2} to entity {3} when this entity is already classified
     */
    ENTITY_ALREADY_CLASSIFIED(400, "OMRS-REPOSITORY-400-081",
                          "A {0} request has been made to repository {1} to add a classification {2} to entity {3} when this entity is already classified",
                          "The system is unable to perform the request as only one classification of a specific type is permitted.",
                          "Use the updateClassificationProperties to make changed to an existing classification."),

    /**
     * OMRS-REPOSITORY-400-082 - The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for classification {1} located in metadata collection {2}
     */
    NO_HOME_FOR_CLASSIFICATION(400, "OMRS-REPOSITORY-400-082",
                         "The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for classification {1} located in metadata collection {2}",
                         "The system is unable to proceed with processing this classification update request because it does not know which repository to call.",
                         "This error suggests there is a logic error in either this repository, or the home repository for the classification. " +
                                 "Note this may be different from the home repository for the entity.  Raise a Github issue in order to get this fixed."),

    /**
     * OMRS-REPOSITORY-400-083 - The OMRS repository connector operation {0} does not allow a time range from {1} to {2}
     */
    INVALID_TIME_RANGE(400, "OMRS-REPOSITORY-400-083",
            "The OMRS repository connector operation {0} does not allow a time range from {1} to {2}",
            "The system is unable continue processing the request because the time range provided does not overlap.",
            "Correct the code in the caller's method (potentially just reverse the times) and retry the request."),

    /**
     * OMRS-REST-API-400-001 - The OMRS REST API for server {0} has been called with a null username (userId)
     */
    NULL_USER_NAME(400, "OMRS-REST-API-400-001",
            "The OMRS REST API for server {0} has been called with a null user name (userId)",
            "The system is not able to get access to the local metadata repository.",
            "The user name is supplied by the caller to the API. This call needs to be fixed before the server is able to operate correctly."),

    /**
     * OMRS-PROPERTIES-400-001 - No more elements in {0} iterator
     */
    NO_MORE_ELEMENTS(400, "OMRS-PROPERTIES-400-001",
            "No more elements in {0} iterator",
            "A caller stepping through an iterator has requested more elements when there are none left.",
            "Recode the caller to use the hasNext() method to check for more elements before calling next() and then retry."),

    /**
     * OMRS-PROPERTIES-400-002 - No name provided for entity classification
     */
    NULL_CLASSIFICATION_PROPERTY_NAME(400, "OMRS-PROPERTIES-400-002",
            "No name provided for entity classification",
            "A classification with a null name is assigned to an entity.   This value should come from a metadata repository, and always be filled in.",
            "Look for other error messages to identify the source of the problem.  Identify the metadata repository where the asset came from.  Correct the cause of the error and then retry."),

    /**
     * OMRS-PROPERTIES-400-003 - Null property name passed to properties object
     */
    NULL_PROPERTY_NAME(400, "OMRS-PROPERTIES-400-003",
            "Null property name passed to properties object",
            "A request to set an additional property failed because the property name passed was null",
            "Recode the call to the property object with a valid property name and retry."),

    /**
     * OMRS-PROPERTIES-400-004 - {0} is unable to add a new element to location {1} of an array of size {2} value
     */
    ARRAY_OUT_OF_BOUNDS(400, "OMRS-PROPERTIES-400-004",
            "{0} is unable to add a new element to location {1} of an array of size {2} value",
            "There is an error in the update of an ArrayPropertyValue.",
            "Recode the call to the property object with a valid element location and retry."),

    /**
     * OMRS-PROPERTIES-400-005 - AttributeDefs may only be of primitive, collection or enum type. {0} of category {1} is not allowed
     */
    BAD_ATTRIBUTE_TYPE(400, "OMRS-PROPERTIES-400-005",
            "AttributeDefs may only be of primitive, collection or enum type. {0} of category {1} is not allowed",
            "There is an error in the creation of an AttributeDefType.",
            "Recode the call to the AttributeDefType object with a valid type."),

    /**
     * OMRS-PROPERTIES-400-006 - A {0} exception was returned when matching {2} against {1} in method {4}.  The exception message was: {3}
     */
    INVALID_SEARCH_CRITERIA(400, "OMRS-PROPERTIES-400-006",
            "A {0} exception was returned when matching {2} against {1} in method {4}.  The exception message was: {3}",
            "There was an error in the creation of an AttributeDefType.",
            "Fux the call to the AttributeDefType object to use a valid type."),

    /**
     * OMRS-PROPERTIES-400-007 - Data type {0} is not supported by method {1}
     */
    BAD_DATA_TYPE(400, "OMRS-PROPERTIES-400-007",
                       "Data type {0} is not supported by method {1}",
                       "This method needs to be extended to support other data types.",
                       "Update this method to include the requested type."),

    /**
     * OMRS-REST-CONNECTOR-400-001 - The connection passed in the cohort registration event does not contain the root URL for calling the server's REST API
     */
    REPOSITORY_URL_NULL(400, "OMRS-REST-CONNECTOR-400-001",
            "The connection passed in the cohort registration event does not contain the root URL for calling the server's REST API",
            "The system is unable to connect to the open metadata repository to retrieve metadata.",
            "Retry the cohort registration when the connection configuration for this repository is corrected.  " +
                                "If the server is running in an OMAG platform then the configuration of the LocalRepositoryRemoteConnection needs correcting."),

    /**
     * OMRS-CONNECTOR-400-001 - The Open Metadata Repository Server URL {0} is not in a recognized format
     */
    REPOSITORY_URL_MALFORMED(400, "OMRS-CONNECTOR-400-001",
            "The Open Metadata Repository Server URL {0} is not in a recognized format",
            "The system is unable to connect to the open metadata repository to retrieve metadata properties.",
            "Retry the request when the connection configuration for this repository is corrected."),

    /**
     * OMRS-CONNECTOR-400-002 - The connection passed to OMASConnectedAssetProperties for connector {0} is null
     */
    NULL_CONNECTION(400, "OMRS-CONNECTOR-400-002",
            "The connection passed to OMASConnectedAssetProperties for connector {0} is null",
            "The system is unable to populate the ConnectedAssetDetails object because it needs the connection to identify the asset.",
            "Look for other error messages to identify what caused this error.  When the issue is resolved, retry the request."),

    /**
     * OMRS-CONNECTOR-400-003 - The connection passed to the EnterpriseOMRSRepositoryConnector is null
     */
    NULL_OMRS_CONNECTION(400, "OMRS-CONNECTOR-400-003",
            "The connection passed to the EnterpriseOMRSRepositoryConnector is null",
            "The system is unable to populate the EnterpriseOMRSRepositoryConnector object because it needs the connection to identify the repository.",
            "Look for other error messages to identify what may have caused this error.  When the issue is resolved, retry the request."),

    /**
     * OMRS-CONNECTOR-400-004 - The connection {0} passed to the EnterpriseOMRSRepositoryConnector is invalid
     */
    INVALID_OMRS_CONNECTION(400, "OMRS-CONNECTOR-400-004",
            "The connection {0} passed to the EnterpriseOMRSRepositoryConnector is invalid",
            "The system is not able to populate the EnterpriseOMRSRepositoryConnector object because it needs the connection to identify the repository.",
            "Look for other error messages to identify what caused this error.  When the issue is fixed, retry the request."),

    /**
     * OMRS-CONNECTOR-400-005 - The connector to the local repository failed with a {0} exception and the following error message: {1}
     */
    BAD_REAL_LOCAL_REPOSITORY_CONNECTOR(400, "OMRS-CONNECTOR-400-005",
            "The connector to the local repository failed with a {0} exception and the following error message: {1}",
            "The server fails to start.",
            "Correct the configuration to ensure that the local repository local connection is valid."),

    /**
     * OMRS-TOPIC-CONNECTOR-400-001 - Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize
     */
    NULL_TOPIC_CONNECTOR(400, "OMRS-TOPIC-CONNECTOR-400-001",
            "Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize",
            "The local server will not connect to the cohort.",
            "The connection to the connector is configured in the server configuration.  " +
                                 "Review previous error messages to determine the precise error in the " +
                                 "start up configuration. " +
                                 "Correct the configuration and reconnect the server to the cohort. "),

    /**
     * OMRS-TOPIC-CONNECTOR-400-002 - The connector {0} has been configured without an embedded event bus connector
     */
    NO_EVENT_BUS_CONNECTORS(400, "OMRS-TOPIC-CONNECTOR-400-002",
            "The connector {0} has been configured without an embedded event bus connector",
            "There is an error in the connection for the connector. The connection is defined in the server's configuration document.",
            "Review the configuration document and correct the definition of the connection."),

    /**
     * OMRS-COHORT-REGISTRY-404-001 - The Open Metadata Repository Cohort Registry Store for cohort {0} is not available
     */
    NULL_REGISTRY_STORE(400, "OMRS-COHORT-REGISTRY-404-001",
            "The Open Metadata Repository Cohort Registry Store for cohort {0} is not available",
            "The system is unable to process registration requests from the open metadata repository cohort.",
            "Correct the configuration for the registry store connection in the server configuration. " +
            "Retry the request when the registry store configuration is correct."),

    /**
     * OMRS-COHORT-REGISTRY-400-002 - The Open Metadata Repository Cohort {0} is not available to server {1} because the local
     * metadata collection id has been changed from {2} to {3} since this server registered with the cohort
     */
    INVALID_LOCAL_METADATA_COLLECTION_ID(400, "OMRS-COHORT-REGISTRY-400-002",
            "The Open Metadata Repository Cohort {0} is not available to server {1} because the local " +
                    "metadata collection id has been changed from {2} to {3} since this server registered with the cohort",
            "The system is unable to connect with other members of the cohort while this incompatibility exists.",
            "If there is no reason for the change of local metadata collection id (this is the normal case) " +
                    "change the local metadata collection id back to its original valid in the server configuration. " +
                    "If the local metadata collection id must be changed (due to a conflict for example) " +
                    "then shutdown the server, restart it with no local repository configured and shut it down " +
                    "normally once the server has successfully unregistered with the cohort. " +
                    "Then re-establish the local repository configuration." +
            "Restart the server once the configuration is correct."),

    /**
     * OMRS-ARCHIVE-MANAGER-400-001 - An open metadata archive configured for server {0} is not accessible
     */
    NULL_ARCHIVE_STORE(400, "OMRS-ARCHIVE-MANAGER-400-001",
            "An open metadata archive configured for server {0} is not accessible",
             "The system is unable to process the contents of this open metadata archive.  " +
                               "Other services may fail if they were dependent on this open metadata archive.",
             "Correct the configuration for the open metadata archive connection in the server configuration. " +
                                 "Retry the request when the open metadata archive configuration is correct."),

    /**
     * OMRS-LOCAL-REPOSITORY-400-001 - The repository event mapper configured for the local repository for server {0} is not accessible
     */
    NULL_EVENT_MAPPER(400, "OMRS-LOCAL-REPOSITORY-400-001",
             "The repository event mapper configured for the local repository for server {0} is not accessible",
             "The system is unable to create the repository event mapper which means that events from the " +
                              "local repository will not be captured and processed.  " +
                              "Other services may fail if they were dependent on this event notification.",
             "Correct the configuration for the repository event mapper connection in the server configuration. " +
                               "Retry the request when the repository event mapper configuration is correct."),

    /**
     * OMRS-LOCAL-REPOSITORY-400-002 - The local repository is not able to re-home the instance {0} of type {1} ({2}) because it is
     * not managing the repository with the requested home metadata collection of {3}.  This local repository is managing the {4} metadata collection
     */
    NOT_FOR_LOCAL_COLLECTION(400, "OMRS-LOCAL-REPOSITORY-400-002",
                      "The local repository is not able to re-home the instance {0} of type {1} ({2}) because it is not managing the repository " +
                              "with the requested home metadata collection of {3}.  This local repository is managing the {4} metadata collection",
                      "The system can not continue processing the request.",
                      "Retry the request on the repository with the requested metadata collection identifier or retry the request on this " +
                                     "repository with the local metadata collection identifier."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-400-001 - Conflicting TypeDefs have been detected
     */
    CONFLICTING_ENTERPRISE_TYPEDEFS(400, "OMRS-ENTERPRISE-REPOSITORY-400-001",
            "Conflicting TypeDefs have been detected",
            "The system is unable to create a reliable list of TypeDefs for the enterprise.",
            "Details of the conflicts and the steps necessary to repair the situation can be found in the audit log. " +
                                  "Retry the request when the cohort configuration is correct."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-400-002 - No TypeDefs have been defined in any of the connected repositories
     */
    NO_TYPEDEFS_DEFINED(400, "OMRS-ENTERPRISE-REPOSITORY-400-002",
            "No TypeDefs have been defined in any of the connected repositories",
            "The system is unable to create a list of TypeDefs for the enterprise.",
            "Look for errors in the set up of the repositories in the audit log and verify that TypeDefs are configured. " +
                                            "Retry the request when the cohort configuration is correct."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-001 - The same type {0} of category {1} has been added twice to an open metadata archive.
     * First version was {2} and the second was {3}
     */
    DUPLICATE_TYPE_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-001",
            "The same type {0} of category {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}",
            "The build of the archive terminates.",
            "Verify the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-002 - The {0} instance {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}
     */
    DUPLICATE_INSTANCE_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-002",
            "The {0} instance {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}",
            "The build of the archive terminates immediately.",
            "Verify the definition of the instance being added to the archive. Once the definitions have been corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-003 - The same type name {0} has been added twice to an open metadata archive. First version was {1} and the second was {2}
     */
    DUPLICATE_TYPENAME_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-003",
            "The same type name {0} has been added twice to an open metadata archive. First version was {1} and the second was {2}",
            "The build of the archive ends.",
            "Check the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-004 - The guid {0} has been used twice to an open metadata archive. First version was {1} and the second was {2}
     */
    DUPLICATE_GUID_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-004",
            "The guid {0} has been used twice to an open metadata archive. First version was {1} and the second was {2}",
            "The build of the archive will terminate.",
            "Verify the definition of the elements being added to the archive. Once the definitions have been corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-005 - The type {0} of category {1} is not found in an open metadata archive
     */
    MISSING_TYPE_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-005",
            "The type {0} of category {1} is not found in an open metadata archive",
            "The build of the archive now ends.",
            "Verify the definition of all the elements being added to the archive. Once the definitions have been corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-006 - A request for a type from category {0} passed a null name
     */
    MISSING_NAME_FOR_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-006",
            "A request for a type from category {0} passed a null name",
            "The build of the archive stops.",
            "Verify the definition of the elements being added to the archive. Once the definitions are corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-007 - RelationshipEndDef1 type {0} and EndDef1 name {1} in RelationshipDef {2} are incorrect,
     * because another entity or relationship endDef is already using this attribute name
     */
    DUPLICATE_ENDDEF1_NAME_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-007",
            "RelationshipEndDef1 type {0} and EndDef1 name {1} in RelationshipDef {2} are incorrect, because another entity or " +
                    "relationship endDef is already using this attribute name",
            "The build of the archive exits.",
            "Verify the definition of the types being added to the archive. Once the definitions have been corrected, repeat the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-008 - RelationshipEndDef2 type {0} and EndDef2 name {1} in RelationshipDef {2} are incorrect,
     * because another entity or relationship endDef is already using this attribute name
     */
    DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-008",
            "RelationshipEndDef2 type {0} and EndDef2 name {1} in RelationshipDef {2} are incorrect, because another entity or " +
                    "relationship endDef is already using this attribute name",
            "The archive build terminates.",
            "Verify the definition of the types being added to the archive. Once the definitions have been fixed, repeat the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-009 - Duplicate attribute name {0} is defined in RelationshipDef {1}
     */
    DUPLICATE_RELATIONSHIP_ATTR_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-009",
            "Duplicate attribute name {0} is defined in RelationshipDef {1}",
            "The archive build stops.",
            "Verify the definition of the types being added to the archive. Once the definitions have been fixed, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-010 - Duplicate attribute name {0} is defined in EntityDef {1}
     */
    DUPLICATE_ENTITY_ATTR_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-010",
            "Duplicate attribute name {0} is defined in EntityDef {1}",
            "The archive build will terminate.",
            "Verify the definition of the types being added to the archive. Once the definitions are corrected, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-011 - Duplicate attribute name {0} is defined in ClassificationDef {1}
     */
    DUPLICATE_CLASSIFICATION_ATTR_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-011",
            "Duplicate attribute name {0} is defined in ClassificationDef {1}",
            "The archive build will exit.",
            "Check the definition of the types being added to the archive. Once the definitions have been fixed, retry the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-012 - Type name {0} is invalid because it contains a blank character
     */
    BLANK_TYPENAME_IN_ARCHIVE(400, "OMRS-ARCHIVE-BUILDER-400-012",
            "Type name {0} is invalid because it contains a blank character",
            "The archive build has ended.",
            "Verify the definition of the types being added to the archive. Once the definitions are fixed, rerun the request."),

    /**
     * OMRS-ARCHIVE-BUILDER-400-013 - The archive builder has been passed an unknown type name {0}
     */
    UNKNOWN_TYPENAME(400, "OMRS-ARCHIVE-BUILDER-400-013",
            "The archive builder has been passed an unknown type name {0}",
            "There is an problem in the code that created the instance.",
            "Identify the code that called the archive builder and correct the type name."),

    /**
     * OMRS-AUDIT-LOG-400-001 - There are no Audit Log destinations configured for server {0}
     */
    NO_AUDIT_LOG_STORE(400, "OMRS-AUDIT-LOG-400-001",
            "There are no Audit Log destinations configured for server {0}",
            "The system is unable to support diagnostic and audit logging because it has not been configured with any audit log destinations.",
            "Add the configuration for at least one audit log destination in the server configuration. " +
                          "Retry the request when the audit log configuration is correct."),

    /**
     * OMRS-AUDIT-LOG-400-002 - An Audit Log destination for server {0} is not correctly configured
     */
    NULL_AUDIT_LOG_STORE(400, "OMRS-AUDIT-LOG-400-002",
            "An Audit Log destination for server {0} is not correctly configured and a {1} exception occurred with message {2}",
            "The system is unable to send diagnostic and audit information to one of the configured audit log destinations because the supplied " +
                                 "connector failed to initialize.",
            "Correct the configuration for the audit log store connection in the server configuration. " +
                                 "Retry the request when the audit log store configuration is correct."),

    /**
     * OMRS-AUDIT-LOG-400-003 - A null log record has been passed by the audit log to the audit log destination {0}
     */
    NULL_LOG_RECORD(400, "OMRS-AUDIT-LOG-400-003",
            "A null log record has been passed by the audit log to the audit log destination {0}",
            "The audit log destination throws an exception and the log record is not written to the audit log destination.",
            "This is probably an internal error in the audit log.  Raise a Github issue to get this fixed."),

    /**
     * OMRS-AUDIT-LOG-400-004 - A log record with a null originator has been passed by the audit log to the audit log destination {0}
     */
    NULL_LOG_RECORD_ORIGINATOR(400, "OMRS-AUDIT-LOG-400-004",
            "A log record with a null originator has been passed by the audit log to the audit log destination {0}",
            "The audit log destination throws an exception and the log record is not written out to the audit log destination.",
            "This is probably an internal error in the audit log.  Raise a Github issue to get this addressed."),

    /**
     * OMRS-AUDIT-LOG-400-005 - A log record with a null reporting component has been passed by the audit log to the audit log destination {0}
     */
    NULL_LOG_RECORD_REPORTING_COMPONENT(400, "OMRS-AUDIT-LOG-400-005",
            "A log record with a null reporting component has been passed by the audit log to the audit log destination {0}",
            "The audit log destination throws an exception and the log record is not written to the configured audit log destination.",
            "This is probably an internal error in the audit log.  Raise a Github issue to get this investigated."),

    /**
     * OMRS-AUDIT-LOG-400-006 - The Audit Log destination {0} is not able to support queries
     */
    CAN_NOT_QUERY_AUDIT_LOG_STORE(400, "OMRS-AUDIT-LOG-400-006",
            "The Audit Log destination {0} is not able to support queries",
            "The system is unable to process the query request and throws the FunctionNotSupportedException.",
            "If queries on the audit log are required, then add a new audit log destination that supports queries and restart the server."),

    /**
     * OMRS-AUDIT-LOG-400-007 - The Audit Log destination {0} is not available.  The error returned was {1}
     */
    AUDIT_LOG_STORE_NOT_AVAILABLE(400, "OMRS-AUDIT-LOG-400-007",
            "The Audit Log destination {0} is not available.  The error returned was {1}",
            "The system is unable to store any diagnostics or audit information to this destination because it is currently unavailable.",
            "Restart or correct the configuration of this audit log destination."),

    /**
     * OMRS-AUDIT-LOG-400-008 - The Audit log destination {0} is not able to convert an audit log record to JSON format
     */
    AUDIT_LOG_RECORD_NOT_JSON_ENABLED(400, "OMRS-AUDIT-LOG-400-008",
            "The Audit log destination {0} is not able to convert an audit log record to JSON format",
            "The system is unable to store the log record to this destination because it is not able to" +
                                              " convert its contents into a suitable format.",
            "Investigate and correct the cause of the conversion failure."),

    /**
     * OMRS-AUDIT-LOG-400-009 - The archive manager is not active in server {0}
     */
    ARCHIVE_MANAGER_NOT_ACTIVE(400, "OMRS-AUDIT-LOG-400-009",
                                      "The archive manager is not active in server {0}.  Redirect the load request to a metadata access store",
                                      "The system is unable to load an open metadata archive because the archive manager is not active in this server.",
                                      "Redirect the load request to a metadata access store."),

    /**
     * OMRS-REPOSITORY-404-001 - The open metadata repository connector for server {0} is not active and is unable to service the {1} request
     */
    REPOSITORY_NOT_AVAILABLE(404, "OMRS-REPOSITORY-404-001",
            "The open metadata repository connector for server {0} is not active and is unable to service the {1} request",
            "The system is unable to retrieve any metadata properties from this repository.",
            "Retry the request when the repository connector is active."),

    /**
     * OMRS-REPOSITORY-404-002 - The entity identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}
     */
    ENTITY_NOT_KNOWN(404, "OMRS-REPOSITORY-404-002",
            "The entity identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}",
            "The system is unable to retrieve the properties for the requested entity because the supplied guid is not recognized.",
            "The guid is supplied by the caller to the server.  It may have a logic problem that has corrupted the guid, or the entity has been deleted since the guid was retrieved."),

    /**
     * OMRS-REPOSITORY-404-003 - The relationship identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}
     */
    RELATIONSHIP_NOT_KNOWN(404, "OMRS-REPOSITORY-404-003",
            "The relationship identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}",
            "The system is unable to process the request for the requested relationship because the supplied guid is not recognized.",
            "The guid is supplied by the caller to the OMRS.  It may have a logic problem that has corrupted the guid, or the " +
                                   "relationship has been deleted since the guid was retrieved.  " +
                                   "It is necessary to understand the logic of the caller to determine if this is a problem."),

    /**
     * OMRS-REPOSITORY-404-004 - The TypeDef {0} (guid = {1}) passed on the {2} parameter of the {3} operation is not known to the open metadata repository {4}
     */
    TYPEDEF_NOT_KNOWN(404, "OMRS-REPOSITORY-404-004",
            "The TypeDef {0} (guid = {1}) passed on the {2} parameter of the {3} operation is not known to the open metadata repository {4}",
            "The system is unable to retrieve the properties for the requested TypeDef because the supplied identifier is not recognized.",
            "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the typedef has been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-005 - The TypeDef {0} of category {1} passed by the {2} operation is not known to the open metadata repository {3}
     */
    TYPEDEF_NOT_KNOWN_FOR_INSTANCE(404, "OMRS-REPOSITORY-404-005",
            "The TypeDef {0} of category {1} passed by the {2} operation is not known to the open metadata repository {3}",
            "The system is unable to retrieve the properties for the requested TypeDef since the supplied identifier is not recognized.",
            "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the typedef may have been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-006 - The AttributeTypeDef {0} (guid = {1}) passed on the {2} call is not known to the open metadata repository {3}
     */
    ATTRIBUTE_TYPEDEF_NOT_KNOWN(404, "OMRS-REPOSITORY-404-006",
            "The AttributeTypeDef {0} (guid = {1}) passed on the {2} call is not known to the open metadata repository {3}",
            "The system is unable to retrieve the properties for the requested TypeDef as the supplied identifier is not recognized.",
            "The identifier is supplied by the caller.  It may have a logic issue that has corrupted the identifier, or the typedef has been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-007 - The TypeDef unique identifier {0} passed as parameter {1} on a {2} request to open metadata repository {3} is not known to this repository
     */
    TYPEDEF_ID_NOT_KNOWN(404, "OMRS-REPOSITORY-404-007",
            "The TypeDef unique identifier {0} passed as parameter {1} on a {2} request to open metadata repository {3} is not known to this repository",
            "The system is unable to retrieve the properties for the requested TypeDef because the supplied identifiers have not been recognized.",
            "The identifier is supplied by the caller.  It may have a logic defect that has corrupted the identifier, or the TypeDef has been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-008 - The AttributeTypeDef {0} (guid {1}) passed on a {2} request to open metadata repository {3} is not known to this repository
     */
    ATTRIBUTE_TYPEDEF_ID_NOT_KNOWN(404, "OMRS-REPOSITORY-404-008",
            "The AttributeTypeDef {0} (guid {1}) passed on a {2} request to open metadata repository {3} is not known to this repository",
            "The system is unable to retrieve the properties for the requested AttributeTypeDef because the supplied identifiers are not recognized.",
            "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the AttributeTypeDef has been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-009 - The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to this repository
     */
    TYPEDEF_NAME_NOT_KNOWN(404, "OMRS-REPOSITORY-404-009",
            "The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to this repository",
            "The system is unable to retrieve the properties for the requested TypeDef because the supplied identifiers are not recognized.",
            "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the TypeDef has been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-010 - The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to the repository
     */
    ATTRIBUTE_TYPEDEF_NAME_NOT_KNOWN(404, "OMRS-REPOSITORY-404-010",
            "The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to the repository",
            "The system is unable to retrieve the properties for the requested TypeDef as the supplied identifiers are not recognized.",
            "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the TypeDef may have been deleted since the identifier was retrieved."),

    /**
     * OMRS-REPOSITORY-404-011 - The relationship identified with guid {0} passed on the {1} call is not found to the open metadata repository {2}
     */
    RELATIONSHIP_NOT_FOUND(404, "OMRS-REPOSITORY-404-011",
                           "The relationship identified with guid {0} passed on the {1} call is not found to the open metadata repository {2}",
                           "The system is unable to retrieve the properties for the requested relationship because the supplied guid is not recognized.",
                           "The guid is supplied by the caller to the OMRS.  It may have a logic problem that has corrupted the guid, or the relationship has been deleted since the guid was retrieved."),

    /**
     * OMRS-REPOSITORY-404-012 - The {0} relationship identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}
     */
    RELATIONSHIP_SOFT_DELETED(404, "OMRS-REPOSITORY-404-012",
                           "The {0} relationship identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}",
                           "The system is unable to retrieve the properties for the requested relationship because the supplied guid is for a " +
                                      "relationship that has already been deleted.",
                           "The guid is supplied by the caller to the OMRS.  It is most likely to be a timing issue where the" +
                                      " relationship was deleted by another process since the guid was retrieved.  " +
                                      "However, there is a  " +
                                      "possibility of a logic problem that has corrupted the guid."),

    /**
     * OMRS-REPOSITORY-404-013 - The {0} entity identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}
     */
    ENTITY_SOFT_DELETED(404, "OMRS-REPOSITORY-404-013",
                              "The {0} entity identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}",
                              "The system is unable to retrieve the properties for the requested entity because the supplied guid is for a " +
                                      "entity that has already been deleted.",
                              "The guid is supplied by the caller to the OMRS.  It is most likely to be a timing issue where the" +
                                      " entity was deleted by another process since the guid was retrieved.  " +
                                      "However, there is a  " +
                                      "possibility of a logic problem that has corrupted the guid."),

    /**
     * OMRS-REPOSITORY-CONNECTOR-404-002 - The Open Metadata Repository Servers in the cohort are not available
     */
    COHORT_NOT_CONNECTED(404, "OMRS-REPOSITORY-CONNECTOR-404-002",
            "The Open Metadata Repository Servers in the cohort are not available",
            "The system is not able to retrieve any metadata properties from this repository.",
            "Repeat the request when the repository server is available."),

    /**
     * OMRS-REPOSITORY-CONNECTOR-404-003 - The open metadata repository servers in the cohort are not configured correctly
     */
    INVALID_COHORT_CONFIG(404, "OMRS-REPOSITORY-CONNECTOR-404-003",
            "The open metadata repository servers in the cohort are not configured correctly",
            "The underlying cause of this error is recorded in previous exceptions.",
            "Review the other error messages to determine the source of the error.  When these are resolved, retry the request."),

    /**
     * OMRS-METADATA-HIGHWAY-404-00 - The local server's metadata highway communication components are failing to initialize
     */
    METADATA_HIGHWAY_NOT_AVAILABLE(404, "OMRS-METADATA-HIGHWAY-404-001",
            "The local server's metadata highway communication components are failing to initialize",
            "The root cause of this error is reported in previous exceptions.",
            "Review the other error messages to determine the source of the error.  When resolved, retry the request."),

    /**
     * OMRS-METADATA-HIGHWAY-404-002 - The local server is unable to disconnect from an open metadata repository cohort {0}
     */
    COHORT_DISCONNECT_FAILED(404, "OMRS-METADATA-HIGHWAY-404-002",
            "The local server is unable to disconnect from an open metadata repository cohort {0}",
            "The underlying cause of this error is reported in previous exceptions.",
            "Check the other error messages to determine the source of the error.  When these are resolved, retry the request."),

    /**
     * OMRS-METADATA-HIGHWAY-404-003 - There are more than one cohort configurations with the same name of {0}
     */
    DUPLICATE_COHORT_NAME(400, "OMRS-METADATA-HIGHWAY-404-003",
            "There are more than one cohort configurations with the same name of {0}",
            "The system is unable to connect to more than one cohort with the same name.",
            "Correct the configuration for the cohorts in the server configuration. " +
                                  "Retry the request when the cohort configuration is correct."),

    /**
     * OMRS-TOPIC-CONNECTOR-404-001 - The OMRS Topic Connector is not available
     */
    TOPIC_CONNECTOR_NOT_AVAILABLE(404, "OMRS-TOPIC-CONNECTOR-404-001",
            "The OMRS Topic Connector is not available",
            "The system is not able to process events sent between repositories in the open metadata cohort.",
            "Correct the configuration for the OMRS Topic Connector (in the OMRS Configuration). Retry the request when the topic connector configuration is correct."),

    /**
     * OMRS-REST-REPOSITORY-CONNECTOR-404-001 - A call to the {0} of the open metadata repository server {1} results in an exception {2} with message {3}
     */
    REMOTE_REPOSITORY_ERROR(404, "OMRS-REST-REPOSITORY-CONNECTOR-404-001",
            "A call to the {0} of the open metadata repository server {1} results in an exception {2} with message {3}",
            "The system is not able to retrieve metadata properties from this repository.",
            "Retry the request when the repository server is available."),

    /**
     * OMRS-METADATA-HIGHWAY-404-004 - The local server is unable to initiate a connection to the cohort {0} when starting up
     */
    COHORT_STARTUP_ERROR(404, "OMRS-METADATA-HIGHWAY-404-004",
            "The local server is unable to initiate a connection to the cohort {0} when starting up",
            "The server will now cancel startup, and shutdown.",
            "Check the other error messages to determine the source of the error. When these are resolved, retry the request."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-CONNECTOR-405-001 - The requested method {0} is not supported by the EnterpriseOMRSRepositoryConnector
     */
    ENTERPRISE_NOT_SUPPORTED(405, "OMRS-ENTERPRISE-REPOSITORY-CONNECTOR-405-001",
            "The requested method {0} is not supported by the EnterpriseOMRSRepositoryConnector",
            "The system is not able to process the requested method because it is not supported by the " +
                                     "Open Metadata Repository Services (OMRS) Enterprise Repository Services.",
            "Correct the application that called this method."),

    /**
     * OMRS-METADATA-COLLECTION-409-001 - Multiple instances of type {0} have been returned to {2} of service {1} when there should be one at most.  These are examples of the entities returned: {3}
     */
    MULTIPLE_ENTITIES_FOUND(409, "OMRS-METADATA-COLLECTION-409-001",
            "Multiple instances of type {0} have been returned to {2} of service {1} when there should be one at most.  These are examples of the entities returned: {3}",
            "The type model defines certain properties as unique.  This means only one instance of this type with that property value should be returned.",
            "Investigate why multiple instances exist and either delete the duplicates, or change the values in it so the unique properties are unique."),

    /**
     * OMRS-METADATA-COLLECTION-409-002 - Multiple instances of type {0} have been returned to {2} of service {1} when there should be a maximum of one.  These are examples of the entities returned: {3}
     */
    MULTIPLE_RELATIONSHIPS_FOUND(409, "OMRS-METADATA-COLLECTION-409-002",
            "Multiple instances of type {0} have been returned to {2} of service {1} when there should be a maximum of one.  These are examples of the entities returned: {3}",
            "The type model defines how many relationships are allowed to connect with a specific entity instance.  This is an example of where the limit has been exceeded.",
            "Investigate why multiple instances exist and delete the duplicates."),

    /**
     * OMRS-METADATA-COLLECTION-500-001 - The Java class {0} for PrimitiveDefCategory {1} is not known
     */
    INVALID_PRIMITIVE_CLASS_NAME(500, "OMRS-METADATA-COLLECTION-500-001",
            "The Java class {0} for PrimitiveDefCategory {1} is not known",
            "There is an internal error in Java class PrimitiveDefCategory as it has been set up with an invalid class.",
            "Raise a Github issue to get this fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-002 - The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveDefCategory {2}
     */
    INVALID_PRIMITIVE_VALUE(500, "OMRS-METADATA-COLLECTION-500-002",
            "The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveDefCategory {2}",
            "There is an internal error in the creation of a PrimitiveTypeValue.",
            "Open an issue on GitHub to get this addressed."),

    /**
     * OMRS-METADATA-COLLECTION-500-003 - There is a problem in the definition of primitive type {0}
     */
    INVALID_PRIMITIVE_CATEGORY(500, "OMRS-METADATA-COLLECTION-500-003",
            "There is a problem in the definition of primitive type {0}",
            "There is an internal error during the creation of a PrimitiveTypeValue.",
            "Open a Github issue to get this looked into."),

    /**
     * OMRS-METADATA-COLLECTION-500-004 - Null home metadata collection identifier found by method {1} in property {0} from open metadata repository {3}
     */
    NULL_HOME_METADATA_COLLECTION_ID(500, "OMRS-METADATA-COLLECTION-500-004",
            "Null home metadata collection identifier found by method {1} in property {0} from open metadata repository {3}",
            "A request to retrieve a metadata instance (entity or relationship) has encountered a homeless metadata instance.",
            "Locate the open metadata repository that supplied the instance and correct the logic in its OMRSRepositoryConnector."),

    /**
     * OMRS-METADATA-COLLECTION-500-006 - The open metadata repository connector {0} has been initialized with a null metadata collection identifier
     */
    NULL_METADATA_COLLECTION_ID(500, "OMRS-METADATA-COLLECTION-500-006",
            "The open metadata repository connector {0} has been initialized with a null metadata collection identifier",
            "There is an internal error in the OMRS initialization.",
            "Raise a Github issue to get this investigated and fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-007 - Open metadata repository {0} has used an invalid paging parameter during the {1} operation
     */
    BAD_INTERNAL_PAGING(500, "OMRS-METADATA-COLLECTION-500-007",
            "Open metadata repository {0} has used an invalid paging parameter during the {1} operation",
            "There is an internal error in the OMRS repository connector.",
            "Open a Github issue to get this investigated and fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-008 - Open metadata repository {0} has used an invalid sequencing parameter during the {1} operation
     */
    BAD_INTERNAL_SEQUENCING(500, "OMRS-METADATA-COLLECTION-500-008",
            "Open metadata repository {0} has used an invalid sequencing parameter during the {1} operation",
            "There is an internal problem in the OMRS repository connector.",
            "Raise a Github issue against Egeria to get this fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-009 - Unable to complete operation {0} to open metadata repository {1} because the repository connector is null
     */
    NO_REPOSITORY_CONNECTOR_FOR_COLLECTION(500, "OMRS-METADATA-COLLECTION-500-009",
            "Unable to complete operation {0} to open metadata repository {1} because the repository connector is null",
            "There is an internal issue in the OMRS initialization.",
            "Raise a Github issue on Egeria to get this fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-010 - Unable to complete operation {0} to open metadata repository {1} because the repository validator is null
     */
    NO_REPOSITORY_VALIDATOR_FOR_COLLECTION(500, "OMRS-METADATA-COLLECTION-500-010",
            "Unable to complete operation {0} to open metadata repository {1} because the repository validator is null",
            "There is an internal logic  error in the OMRS initialization.",
            "Raise a Github issue on Egeria to get this addressed."),

    /**
     * OMRS-METADATA-COLLECTION-500-011 - Unable to complete operation {0} to open metadata repository {1} as the repository connector is null
     */
    NO_REPOSITORY_HELPER_FOR_COLLECTION(500, "OMRS-METADATA-COLLECTION-500-011",
            "Unable to complete operation {0} to open metadata repository {1} as the repository connector is null",
            "There is an internal logic problem in the OMRS initialization.",
            "Raise a Github issue on Egeria to get this investigated and fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-012 - Open metadata repository {0} has encountered an unexpected exception during the {1} operation.  The full message was {2}
     */
    REPOSITORY_LOGIC_ERROR(500, "OMRS-METADATA-COLLECTION-500-012",
            "Open metadata repository {0} has encountered an unexpected exception during the {1} operation.  The full message was {2}",
            "There is an internal error within the OMRS repository connector.",
            "Open a Github issue against Egeria to get this fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-013 - During the {0} operation, open metadata repository {1} retrieved an instance from its metadata store that has a null type
     */
    NULL_INSTANCE_TYPE(500, "OMRS-METADATA-COLLECTION-500-013",
           "During the {0} operation, open metadata repository {1} retrieved an instance from its metadata store that has a null type",
           "There is an internal issue in the OMRS repository connector.",
           "Open a Github issue on Egeria to get this looked into."),

    /**
     * OMRS-METADATA-COLLECTION-500-014 - During the {0} operation, open metadata repository {1} retrieved an instance (guid={2}) from its metadata store that has an inactive type called {3} (type guid = {4})
     */
    INACTIVE_INSTANCE_TYPE(500, "OMRS-METADATA-COLLECTION-500-014",
           "During the {0} operation, open metadata repository {1} retrieved an instance (guid={2}) from its metadata store that has an inactive type called {3} (type guid = {4})",
           "There is an internal problem in the OMRS repository connector code.",
           "Report as a Github issue to get this fixed."),

    /**
     * OMRS-METADATA-COLLECTION-500-015 - The value supplied for an attribute of PrimitiveDefCategory {0} is expected as Java class {1} but was supplied as Java class {2}
     */
    INVALID_PRIMITIVE_TYPE(500, "OMRS-METADATA-COLLECTION-500-015",
            "The value supplied for an attribute of PrimitiveDefCategory {0} is expected as Java class {1} but was supplied as Java class {2}",
            "There is an internal error - code that sets a primitive property value is using an incorrect Java class.",
            "Report as a Github issue to get this addressed."),

    /**
     * OMRS-METADATA-COLLECTION-500-016 - The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is not the metadata collection identifier {3} for the local metadata repository {4}
     */
    INSTANCE_HOME_NOT_LOCAL(500, "OMRS-METADATA-COLLECTION-500-016",
            "The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is not the metadata collection identifier {3} for the local metadata repository {4}",
            "A request to update a metadata instance (entity or relationship) has been encountered on a reference copy metadata instance.",
            "Locate the open metadata repository that has the home instance and perform the update at that repository."),

    /**
     * OMRS-METADATA-COLLECTION-500-017 - The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is the metadata collection identifier {3} for the local metadata repository {4}
     */
    INSTANCE_HOME_IS_LOCAL(500, "OMRS-METADATA-COLLECTION-500-017",
           "The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is the metadata collection identifier {3} for the local metadata repository {4}",
           "A rehome request to update a metadata instance (entity or relationship) metadata collection id has been encountered on a local metadata instance.  This request should be issues on the new home repository.",
           "Locate the open metadata repository that is to be the new home of the instance and perform the rehome at that repository."),

    /**
     * OMRS-METADATA-COLLECTION-500-018 - The open metadata repository connector {0} has returned a null metadata collection identifier
     */
    NULL_METADATA_COLLECTION_ID_FROM_REMOTE(500, "OMRS-METADATA-COLLECTION-500-018",
            "The open metadata repository connector {0} has returned a null metadata collection identifier",
            "There is an internal error in the remote repository.",
            "Determine the source of the implementation of the remote repository and request help from its developers."),

    /**
     * OMRS-COHORT-MANAGER-500-001 - OMRSCohortManager has been initialized with a null cohort name
     */
    NULL_COHORT_NAME(500, "OMRS-COHORT-MANAGER-500-001",
            "OMRSCohortManager has been initialized with a null cohort name",
            "There is an internal error within the OMRS initialization.",
            "Report as a Github issue in order to get this fixed."),

    /**
     * OMRS-OPERATIONAL-SERVICES-500-001 - No configuration has been passed to the Open Metadata Repository Services (OMRS) on initialization os server {0}
     */
    NULL_CONFIG(500, "OMRS-OPERATIONAL-SERVICES-500-001",
            "No configuration has been passed to the Open Metadata Repository Services (OMRS) on initialization os server {0}",
             "here is an internal issue in the OMRS initialization.",
             "Report as a Github issue in order to get this looked in to."),

    /**
     * The local repository services have been initialized with a null real metadata collection.
     */
    NULL_LOCAL_METADATA_COLLECTION(500, "OMRS-LOCAL-REPOSITORY-500-001",
            "The local repository services have been initialized with a null real metadata collection.",
            "There is an internal problem in the OMRS initialization.",
            "Report as a Github issue to get this corrected."),

    /**
     * OMRS-LOCAL-REPOSITORY-500-002 - The local repository for server {0} failed to initialize and returned a {1} exception with message {2}
     */
    LOCAL_REPOSITORY_FAILED_TO_START(500, "OMRS-LOCAL-REPOSITORY-500-002",
            "The local repository for server {0} failed to initialize and returned a {1} exception with message {2}",
            "There is an internal problem in the local repositories initialization.",
            "Validate and correct wither the configuration or the implementation of the local repository connector."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-500-001 - The enterprise repository services has detected a repository connector with a null metadata collection
     */
    NULL_ENTERPRISE_METADATA_COLLECTION(500, "OMRS-ENTERPRISE-REPOSITORY-500-001",
            "The enterprise repository services has detected a repository connector with a null metadata collection",
            "There is an internal error in the Open Metadata Repository Services (OMRS) operation.",
            "Open an issue on GitHub to get this fixed."),

    /**
     * OMRS-CONTENT-MANAGER-500-001 - The repository content manager method {0} has detected an unknown TypeDef {1} from {2} on behalf of method {3}
     */
    BAD_TYPEDEF(500, "OMRS-CONTENT-MANAGER-500-001",
            "The repository content manager method {0} has detected an unknown TypeDef {1} from {2} on behalf of method {3}",
            "There is an internal error within the Open Metadata Repository Services (OMRS) operation.",
            "Open up a Github issue to get this fixed."),

    /**
     * OMRS-CONTENT-MANAGER-500-002 - The repository content manager has detected an invalid attribute name in a TypeDef from {0}
     */
    BAD_TYPEDEF_ATTRIBUTE_NAME(500, "OMRS-CONTENT-MANAGER-500-002",
            "The repository content manager has detected an invalid attribute name in a TypeDef from {0}",
            "There is an internal problem within the Open Metadata Repository Services (OMRS) operation.",
            "Open up a Github issue to get this investigated."),

    /**
     * OMRS-CONTENT-MANAGER-500-003 - The repository content manager has detected a null attribute in a TypeDef from {0}
     */
    NULL_TYPEDEF_ATTRIBUTE(500, "OMRS-CONTENT-MANAGER-500-003",
            "The repository content manager has detected a null attribute in a TypeDef from {0}",
            "There is an internal error in the Open Metadata Repository Services (OMRS) code.",
            "Open up a Github issue to get this investigated and fixed."),

    /**
     * OMRS-CONTENT-MANAGER-500-004 - Source {0} has requested type {1} with an incompatible category of {2} from repository content manager
     */
    BAD_CATEGORY_FOR_TYPEDEF_ATTRIBUTE(500, "OMRS-CONTENT-MANAGER-500-004",
            "Source {0} has requested type {1} with an incompatible category of {2} from repository content manager",
            "There is an error in the Open Metadata Repository Services (OMRS) operation, probably in the source component.",
            "Raise a Github issue so that this can be fixed."),

    /**
     * OMRS-CONTENT-MANAGER-500-005 - The repository content manager has detected an unknown TypeDef {0} ({1}) from {2}. It was passed to method {3} via parameters {4} and {5}
     */
    UNKNOWN_TYPEDEF(500, "OMRS-CONTENT-MANAGER-500-005",
            "The repository content manager has detected an unknown TypeDef {0} ({1}) from {2}. It was passed to method {3} via parameters {4} and {5}",
            "There is an internal problem in the Open Metadata Ecosystem code or its callers because an invalid unique identifier, or name of a type has been passed to the Open Metadata Repository Services (OMRS).",
            "Trace the caller of the request to determine where the type information was specified.  If the error is in the Egeria code, or you need help from the community, raise a Github issue so this can be addressed."),

    /**
     * OMRS-CONTENT-MANAGER-500-006 - The repository content manager has received an instance {0} of class {1} with an open metadata type name of {2}, which is from category {3}
     */
    WRONG_TYPEDEF_CATEGORY(500, "OMRS-CONTENT-MANAGER-500-006",
                    "The repository content manager has received an instance {0} of class {1} with an open metadata type name of {2}, which is from category {3}",
                    "The local repository has received an instance either from an Open Metadata Archive, or another member of one of its Open Metadata Repository Cohorts, that is using a type from a different category of instance.",
                    "Trace the caller of the request to determine where the came from and correct the source."),

    /**
     * OMRS-OPEN-METADATA-ARCHIVE-500-001 - The archive builder failed to initialize
     */
    ARCHIVE_UNAVAILABLE(500, "OMRS-OPEN-METADATA-ARCHIVE-500-001",
            "The archive builder failed to initialize",
            "There is an internal error in the archive building process.",
            "Raise a Github issue this can be investigated."),

    /**
     * OMRS-EVENT-MANAGEMENT-500-001 - A null exchange rule has been passed to one of the event management components on method {0}
     */
    NULL_EXCHANGE_RULE(500, "OMRS-EVENT-MANAGEMENT-500-001",
            "A null exchange rule has been passed to one of the event management components on method {0}",
            "There is an internal error within the OMRS initialization code.",
            "Open a Github issue so this can be fixed."),

    /**
     * OMRS-EVENT-MANAGEMENT-500-002 - A null repository validator has been passed to one of the event management components
     */
    NULL_REPOSITORY_VALIDATOR(500, "OMRS-EVENT-MANAGEMENT-500-002",
            "A null repository validator has been passed to one of the event management components",
            "There is an internal error in the OMRS initialization code.",
            "Open a Github issue to get this looked in to."),

    /**
     * OMRS-EVENT-MANAGEMENT-500-003 - A null repository helper has been passed to one of the event management components
     */
    NULL_REPOSITORY_HELPER(500, "OMRS-EVENT-MANAGEMENT-500-003",
            "A null repository helper has been passed to one of the event management components",
            "There is an internal problem in the OMRS initialization code.",
            "Open a Github issue to get this checked and fixed."),

    /**
     * OMRS-EVENT-MANAGEMENT-500-004 - A null event has been passed to one of the event management components
     */
    NULL_OUTBOUND_EVENT(500, "OMRS-EVENT-MANAGEMENT-500-004",
            "A null event has been passed to one of the event management components",
            "There is an internal defect in the OMRS initialization.",
            "Raise an issue on Github so that this can be fixed."),

    /**
     * OMRS-REST-REPOSITORY-CONNECTOR-500-001 - A remote open metadata repository {0} returned a metadata collection identifier of {1} on its REST API after it registered with the cohort using a metadata collection identifier of {2}
     */
    METADATA_COLLECTION_ID_MISMATCH(500, "OMRS-REST-REPOSITORY-CONNECTOR-500-001",
            "A remote open metadata repository {0} returned a metadata collection identifier of {1} on its REST API after it registered with the cohort using a metadata collection identifier of {2}",
            "There is a configuration error in the remote open metadata repository.",
            "Review the set up of the remote repository.  Has it be reconfigured and changed its metadata collection id? It may be that the server-url-root parameter is incorrectly set and is clashing with the setting in another server registered with the same cohort."),

    /**
     * OMRS-REST-REPOSITORY-CONNECTOR-500-002 - A remote open metadata repository {0} returned a null metadata collection identifier on its REST API.  It registered with the cohort using a metadata collection identifier of {1}
     */
    NULL_REMOTE_METADATA_COLLECTION_ID(500, "OMRS-REST-REPOSITORY-CONNECTOR-500-002",
            "A remote open metadata repository {0} returned a null metadata collection identifier on its REST API.  It registered with the cohort using a metadata collection identifier of {1}",
            "There is an internal error in the remote open metadata repository.",
             "Report this to the Egeria team via a GitHub issue so that it can be fixed."),

    /**
     * OMRS-METADATA-TOPIC-CONNECTOR-500-001 - A null topic listener has been passed to the {0} open metadata topic connector {1}
     */
    NULL_OPEN_METADATA_TOPIC_LISTENER(500, "OMRS-METADATA-TOPIC-CONNECTOR-500-001",
            "A null topic listener has been passed to the {0} open metadata topic connector {1}",
            "There is an internal error in the open metadata repository.",
            "Report this to the Egeria team via a GitHub issue so that it can be investigated."),

    /**
     * OMRS-TOPIC-CONNECTOR-500-001 - A null topic listener has been specified to the open metadata topic connector {0}
     */
    NULL_OMRS_TOPIC_LISTENER(500, "OMRS-TOPIC-CONNECTOR-500-001",
            "A null topic listener has been specified to the open metadata topic connector {0}",
            "There is an internal error in the open metadata repository logic.",
            "Report this to the Egeria team via a GitHub issue so that it can be checked."),

    /**
     * OMRS-TOPIC-CONNECTOR-500-002 - Connector {0} received an unexpected exception from sending event {1}. The exception message was: {2}
     */
    OMRS_TOPIC_SEND_EVENT_FAILED(500, "OMRS-TOPIC-CONNECTOR-500-002",
            "Connector {0} received an unexpected exception from sending event {1}. The exception message was: {2}",
            "There is an internal issue in the open metadata repository logic.",
            "Report this to the Egeria team via a GitHub issue so that it can be investigated and fixed."),

    /**
     * OMRS-TOPIC-CONNECTOR-500-003 - Connector {0} is unable to send a null event
     */
    OMRS_TOPIC_SEND_NULL_EVENT(500, "OMRS-TOPIC-CONNECTOR-500-003",
             "Connector {0} is unable to send a null event",
             "There is an internal problem in the open metadata repository code logic.",
             "Report this to the Egeria team via a GitHub issue so that it can be checked and fixed."),

    /**
     * OMRS-TOPIC-CONNECTOR-500-006 - The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server at {2}
     */
    NULL_CONNECTOR_RETURNED(500, "OMRS-TOPIC-CONNECTOR-500-006",
             "The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service" +
                                    " running in OMAG Server at {2}",
             "The system is unable to create a connector which means some of its services will not work.",
             "This problem is likely to be caused by an incorrect connection object.  Check the settings on the remoteEnterpriseTopicConnection in the server configuration " +
                                    "and correct if necessary.  If the connection is correct, contact the Egeria community for help."),

    /**
     * OMRS-TOPIC-CONNECTOR-500-007 - The connector generated from the connection named {0} return by the {1} service running in OMAG Server at {2} is
     * not of the required type. It should be an instance of {3}
     */
    WRONG_TYPE_OF_CONNECTOR(500, "OMRS-TOPIC-CONNECTOR-500-007",
             "The connector generated from the connection named {0} return by the {1} service running in OMAG Server at {2} is " +
                                    "not of the required type. It should be an instance of {3}",
             "The system is unable to create the required connector which means some of its services will not work.",
             "Verify that the OMAG server is running and the OMAS service is correctly configured."),

    /**
     * OMRS-METADATA-COLLECTION-501-001 - OMRSMetadataInstanceStore method {0} for OMRS Connector {1} to repository type {2} is not implemented
     */
    METHOD_NOT_IMPLEMENTED(501, "OMRS-METADATA-COLLECTION-501-001",
            "OMRSMetadataInstanceStore method {0} for OMRS Connector {1} to repository type {2} is not implemented",
            "A method in MetadataCollectionBase was called which means that the connector's OMRSMetadataInstanceStore " +
                                   "(a subclass of MetadataCollectionBase) does not have a complete implementation.",
             "Report this to the Egeria team via a GitHub issue so that it can be addressed."),

    /**
     * OMRS-METADATA-COLLECTION-501-002 - Repository {0} is not able to support the {1} type
     */
    TYPE_NOT_IMPLEMENTED(501, "OMRS-METADATA-COLLECTION-501-002",
            "Repository {0} is not able to support the {1} type",
            "This repository has a fixed set of types that is can support.",
            "No action required, this is a limitation of the technology."),

    /**
     * OMRS-TOPIC-CONNECTOR-501-001 - Connector {0} is not able to support event protocol {1}
     */
    OMRS_UNSUPPORTED_EVENT_PROTOCOL(501, "OMRS-TOPIC-CONNECTOR-501-001",
            "Connector {0} is not able to support event protocol {1}",
            "This server does not support the requested event protocol level.",
            "The protocol level is set in the configuration.  The admin services should not allow a protocol level that is not supported by its local OMRS." +
                                            " Raise a Github issue to get this fixed."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-503-001 - There are no open metadata repositories available for access service {0}
     */
    NO_REPOSITORIES(503, "OMRS-ENTERPRISE-REPOSITORY-503-001",
            "There are no open metadata repositories available for access service {0}",
            "The configuration for the server is set up so there is no local repository and no remote repositories " +
                            "connected through the open metadata repository cohorts.  " +
                            "This may because of one or more configuration errors.",
            "Retry the request once the configuration is changed."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-503-002 - The enterprise repository services are disconnected from the open metadata repositories
     */
    ENTERPRISE_DISCONNECTED(503, "OMRS-ENTERPRISE-REPOSITORY-503-002",
            "The enterprise repository services are disconnected from the open metadata repositories",
            "The server has shutdown (or failed to set up) access to the open metadata repositories.",
            "Retry the request once the open metadata repositories are connected."),

    /**
     * OMRS-ENTERPRISE-REPOSITORY-503-003 - The enterprise repository services has detected a repository connector from cohort {0} for metadata collection identifier {1} that has a null metadata collection API object
     */
    NULL_COHORT_METADATA_COLLECTION(503, "OMRS-ENTERPRISE-REPOSITORY-503-003",
            "The enterprise repository services has detected a repository connector from cohort {0} for metadata collection identifier {1} that has a null metadata collection API object",
            "There is an internal error in the OMRS Repository Connector implementation.",
            "Raise a Github issue on the Egeria project to get this fixed."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-001 - A null repository content manager has been passed to one of the local repository's components on method {0}
     */
    NULL_CONTENT_MANAGER(503, "OMRS-LOCAL-REPOSITORY-503-001",
            "A null repository content manager has been passed to one of the local repository's components on method {0}",
            "There is an internal error in the OMRS Local Repository Connector implementation, or the way it has been initialized.",
            "Raise a Github issue on the Egeria project to get this addressed."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-002 - A null repository content manager has been specified to one of the local repository's components on method {0}
     */
    NULL_SOURCE_NAME(503, "OMRS-LOCAL-REPOSITORY-503-002",
            "A null repository content manager has been specified to one of the local repository's components on method {0}",
            "There is an internal error within the OMRS Local Repository Connector implementation, or the way it has been initialized.",
            "Raise a Github issue on the Egeria project to get this checked."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-003 - The connection to the local open metadata repository server is not configured correctly
     */
    LOCAL_REPOSITORY_CONFIGURATION_ERROR(503, "OMRS-LOCAL-REPOSITORY-503-003",
            "The connection to the local open metadata repository server is not configured correctly",
            "The root cause of this error is recorded in previous exceptions.",
            "Review the other error messages to determine the location of the error.  When these are resolved, retry the request."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-004 - The connection to the local open metadata repository server has not been configured correctly
     */
    BAD_LOCAL_REPOSITORY_CONNECTION(503, "OMRS-LOCAL-REPOSITORY-503-004",
            "The connection to the local open metadata repository server has not been configured correctly",
            "The root cause of this error is recorded in prior exceptions.",
            "Review the other error messages to determine the source of the error.  When these are fixed, retry the request."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-005 - An OMRS repository connector or access service {0} has passed an invalid parameter to the repository validator {1} operation as part of the {2} request
     */
    VALIDATION_LOGIC_ERROR(503, "OMRS-LOCAL-REPOSITORY-503-005",
             "An OMRS repository connector or access service {0} has passed an invalid parameter to the repository validator {1} operation as part of the {2} request",
             "The open metadata component has called the repository validator operations in the wrong order or has a similar logic error.",
             "Raise a Github issue on the Egeria project to get this investigated and fixed."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-006 - An OMRS repository connector {0} has passed an invalid parameter to the repository content manager {1} operation as part of the {2} request
     */
    CONTENT_MANAGER_LOGIC_ERROR(503, "OMRS-LOCAL-REPOSITORY-503-006",
             "An OMRS repository connector {0} has passed an invalid parameter to the repository content manager {1} operation as part of the {2} request",
             "The open metadata component has called the repository helper operations in the wrong order or has a similar logic error.",
             "Raise a Github issue so that this may be fixed."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-007 - The local OMRS repository connector {0} hosts the home metadata collection for entity {1} but only has an
     * entity proxy stored.  It is not able to complete the {2} request
     */
    ENTITY_PROXY_IN_HOME(503, "OMRS-LOCAL-REPOSITORY-503-007",
             "The local OMRS repository connector {0} hosts the home metadata collection for entity {1} but only has an entity proxy stored.  " +
                     "It is not able to complete the {2} request",
             "There is a logic error either in the EnterpriseOMRSRepositoryConnector causing an update request to be " +
                     "routed to the wrong repository, or there is an error in the local repository.",
             "Open a Github issue so that this can be fixed."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-008 - An OMRS repository connector or access server {0} has passed a null classification to the repository helper
     * {1} operation as part of the {2} request
     */
    NULL_CLASSIFICATION_CREATED(503, "OMRS-LOCAL-REPOSITORY-503-008",
             "An OMRS repository connector or access server {0} has passed a null classification to the repository helper {1} operation as part of the {2} request",
             "The repository connector has called the repository helper operations in the wrong order or has a similar logic error.",
             "Raise a Github issue in order for this to be addressed by the Egeria team."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-009 - The local OMRS repository connector {0} has been asked to update entity {1} but it is not the owner.
     * It is not able to complete the {2} request
     */
    ENTITY_CAN_NOT_BE_UPDATED(503, "OMRS-LOCAL-REPOSITORY-503-009",
             "The local OMRS repository connector {0} has been asked to update entity {1} but it is not the owner." +
                                 "It is not able to complete the {2} request",
             "There is a logic issue either in the EnterpriseOMRSRepositoryConnector causing an update request to be " +
                                 "routed to the wrong repository, or there is an error in the local repository.",
             "Open a Github issue in order for this to be addressed by the Egeria team."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-010 - The local OMRS repository connector {0} has been asked to update relationship {1} but it is not the owner.
     * It is not able to complete the {2} request
     */
    RELATIONSHIP_CAN_NOT_BE_UPDATED(503, "OMRS-LOCAL-REPOSITORY-503-010",
                              "The local OMRS repository connector {0} has been asked to update relationship {1} but it is not the owner." +
                                      "It is not able to complete the {2} request",
             "There is a logic problem either in the EnterpriseOMRSRepositoryConnector causing an update request to be " +
                                      "routed to the wrong repository, or there is an error in the local repository.",
             "Open up a Github issue in order for this to be addressed by the Egeria team."),

    /**
     * OMRS-LOCAL-REPOSITORY-503-011 - The local OMRS repository connector {0} requested an instance {1} from the real metadata collection but
     * a null was returned.  It is not able to complete the {2} request
     */
    NULL_INSTANCE(503, "OMRS-LOCAL-REPOSITORY-503-011",
             "The local OMRS repository connector {0} requested an instance {1} from the real metadata collection but a null was returned." +
                                            "It is not able to complete the {2} request",
             "There is probably a logic error in the repository connector for the real repository because it should have thrown an exception rather" +
                          " than return null.",
             "Raise an issue with the supplier of the real repository connector to get this fixed."),

    /**
     * OMRS-REPOSITORY-HELPER-503-001 - A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request
     */
    HELPER_LOGIC_ERROR(503, "OMRS-REPOSITORY-HELPER-503-001",
             "A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request",
             "The open metadata component has invoked the repository helper operations in the wrong order or has a similar logic error.",
             "Open up a Github issue on Egeria in order for this to be addressed by the Egeria team."),

    /**
     * OMRS-REPOSITORY-HELPER-503-002 - A caller {0} has passed an invalid parameter to the repository helper {1} operation as part
     * of the {2} request resulting in an unexpected exception {3} with message {4}
     */
    HELPER_LOGIC_EXCEPTION(503, "OMRS-REPOSITORY-HELPER-503-002",
             "A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request resulting in an " +
                     "unexpected exception {3} with message {4}",
             "The open metadata component has invoked the repository helper operations in the wrong sequence or has a similar logic error.",
             "Open up a Github issue on Egeria in order for this to be fixed by the Egeria team."),

    /**
     * OMRS-REST-API-503-001 - There is no local repository to support REST API call {0}
     */
    NO_LOCAL_REPOSITORY(503, "OMRS-REST-API-503-001",
            "There is no local repository to support REST API call {0}",
            "The server has received a call on its open metadata repository REST API services but is unable to process it because the local repository is not active.",
            "Ensure that the open metadata services have been activated in the server. If they are active and the server is supposed to have a local repository, correct the server's configuration document to include a local repository and restart the server."),

    /**
     * OMRS-REST-API-503-002 - There is no enterprise repository to support REST API call {0}
     */
    NO_ENTERPRISE_REPOSITORY(503, "OMRS-REST-API-503-002",
            "There is no enterprise repository to support REST API call {0}",
            "The server has received a call on its open metadata enterprise repository REST API services but is unable to process it because the enterprise repository services are not active.",
            "Ensure that the enterprise repository services have been activated in the server. If they are active and the server is supposed to have the enterprise repository services, correct the server's configuration document to include these services and restart the server."),

    /**
     * OMRS-REST-API-503-003 - There is no metadata highway to support REST API call {0}
     */
    NO_METADATA_HIGHWAY(503, "OMRS-REST-API-503-003",
            "There is no metadata highway to support REST API call {0}",
            "The server has received a call on its metadata highway REST API services but is unable to process it because the metadata highway services are not active.",
            "Ensure that the metadata highway services have been activated in the server. If they are supposed to be active, correct the server's configuration document to include these services and restart the server."),

    /**
     * OMRS-REST-API-503-004 - A null response was received from REST API call {0} to repository {1}
     */
    NULL_RESPONSE_FROM_API(503, "OMRS-REST-API-503-004",
            "A null response was received from REST API call {0} to repository {1}",
            "The server has issued a call to the open metadata repository REST API services in a remote repository and has received a null response.",
            "Look for errors in the remote repository's audit log and console to understand and correct the source of the error."),

    /**
     * OMRS-REST-API-503-005 - Unable to create REST Client for repository {0}.  The error message was {1}
     */
    NO_REST_CLIENT(503, "OMRS-REST-API-503-005",
            "Unable to create REST Client for repository {0}.  The error message was {1}",
            "The server has issued a call to the open metadata repository REST API services in a remote repository and has received an exception from the local client libraries.",
            "Look for errors in the local repository's audit log and console to understand and correct the source of the error."),

    /**
     * OMRS-REST-API-503-006 - A client-side exception was received from API call {0} to repository {1}.  The error message was {2}
     */
    CLIENT_SIDE_REST_API_ERROR(503, "OMRS-REST-API-503-006",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has invoked a call on the open metadata repository REST API services in a remote repository and has received an exception from the local client libraries.",
            "Look for errors in the local repository's audit log and console to identify and correct the source of the error.")

    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMRSErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
                                                                                      systemAction,
                                                                                      userAction);

        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
