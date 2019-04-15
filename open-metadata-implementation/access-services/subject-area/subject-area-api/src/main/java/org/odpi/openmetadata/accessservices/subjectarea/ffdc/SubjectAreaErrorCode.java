/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


/**
 * The SubjectAreaErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Asset Consumer OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Id - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a SubjectAreaInterface should correct the error</li>
 * </ul>
 */
public enum SubjectAreaErrorCode {
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-SUBJECTAREA-400-001 ",
            "The OMAS Server URL is null",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the Subject Area OMAS when it is created."),
    SERVER_URL_MALFORMED(400, "OMAS-SUBJECTAREA-400-002 ",
            "The OMAS Server URL {0} is not in a recognized format",
            "The system is unable to connect to the OMAS Server to retrieve metadata properties.",
            "Ensure a valid OMAS Server URL is passed to the Subject Area OMAS when it is created."),
    NULL_USER_ID(400, "OMAS-SUBJECTAREA-400-003 ",
            "The user identifier (user id) passed on the {0} operation is null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-SUBJECTAREA-400-004 ",
            "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    NULL_NAME(400, "OMAS-SUBJECTAREA-400-005 ",
            "The name passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a name.",
            "Correct the code in the caller to provide the name."),
    GUID_DOES_NOT_EXIST(400, "OMAS-SUBJECTAREA-400-006 ",
            "The unique identifier (guid) {0} passed on the {1} operation does not exist in the metadata repository",
            "The system is unable to process the request without a guid that exists.",
            "Correct the code in the caller to provide a guid that exists on the metadata server."),
    UPDATE_REQUESTED_ON_A_NON_EXISTENT_CLASSIFICATION(400, "OMAS-SUBJECTAREA-400-007 ",
            "The Classification name {0} passed on the {1} parameter of the {2} update operation does not match an existing classification associated with the entity with guid {3}",
            "The system is unable to process an update request when the entity does not have a Classification to update.",
            "Correct the code in the caller to either supply an existing classification for update or supply a new classification for a create."),
    SET_ATTRIBUTE_WRONG_TYPE(400, "OMAS-SUBJECTAREA-400-008 ",
            "Error attempting to set Attribute {0} with value {2}, which had an incorrect type. Expected type is {1}.",
            "The system is unable to process a request due to the attribute value having the incorrect type.",
            "Correct the code in the caller to supply the attribute value with an appropriate type."),
    USER_NOT_AUTHORIZED(400, "OMAS-SUBJECTAREA-400-009 ",
            "User {0} is not authorized to issue the {1} request for open metadata access service {2} on org.odpi.openmetadata.accessservices.subjectarea.server {3}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    METADATA_SERVER_UNCONTACTABLE_ERROR(400, "OMAS-SUBJECTAREA-400-010 ",
            "An unexpected error with message {0} was returned by the property org.odpi.openmetadata.accessservices.subjectarea.server during {1} request for open metadata access service {2} on org.odpi.openmetadata.accessservices.subjectarea.server {3}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    NULL_ENUM(400, "OMAS-SUBJECTAREA-400-011 ",
            "The enumeration value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this enumeration value.",
            "Correct the code in the caller to provide the name."),
    NULL_TEXT(400, "OMAS-SUBJECTAREA-400-012 ",
            "The text field value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this text field value.",
            "Correct the code in the caller to provide the name."),
    WRONG_TYPE_FOR_ENTITY_GUID(400, "OMAS-SUBJECTAREA-400-013 ",
            "A guid {0} was expected to be associated with type {1), but was actually associated with type {2}",
            "The system is unable to process the request with a guid associated with an incorrect type.",
            "Correct the code in the caller to provide a relevant guid."),

    ATTEMPTED_NAME_UPDATE(400, "OMAS-SUBJECTAREA-400-014 ",
            "{0} for {1) is not allowed to be updated",
            "The system is unable to process the request.",
            "Correct the code in the caller to not attempt this update or create a new {1) with {0}."),
    CREATE_WITHOUT_GLOSSARY(400, "OMAS-SUBJECTAREA-400-015 ",
            "Cannot continue with create without a glossary",
            "The system is unable to process the request.",
            "Correct the code in the caller to create with a glossary."),
    GLOSSARY_TERM_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECTAREA-400-016 ",
            "Cannot create a GlossaryTerm without a name",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a GlossaryTerm with a name."),
    GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECTAREA-400-018 ",
            "Cannot create a GlossaryCategory without a name",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a GlossaryCategory with a name."),
    GLOSSARY_TERM_CREATE_WITH_CATEGORIES(400, "OMAS-SUBJECTAREA-400-019 ",
            "Cannot create a Glossary Term {0} with categories",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the categories. Knit the term into categories after it has been created."),
    GLOSSARY_TERM_CREATE_WITH_ASSETS(400, "OMAS-SUBJECTAREA-400-020 ",
            "Cannot create a Glossary Term {0} with assets",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the assets."),
    GLOSSARY_TERM_UPDATE_WITH_CATEGORIES(400, "OMAS-SUBJECTAREA-400-021 ",
            "Cannot update a Glossary Term {0} with categories",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the categories."),
    GLOSSARY_TERM_UPDATE_WITH_ASSETS(400, "OMAS-SUBJECTAREA-400-022 ",
            "Cannot create a Glossary Term {0} with assets",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the assets."),
    GLOSSARY_PROJECT_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECTAREA-400-023 ",
            "Cannot create a Project without a name",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a Project with a name."),
    INVALID_PROJECT_DELETION(400, "OMAS-SUBJECTAREA-400-024 ",
            "Cannot delete Project as it was not intended for glossary use.",
            "The system is unable to process the request.",
            "Correct the code in the caller to delete Glossary Projects."),
    CREATE_WITH_GLOSSARY_RELATIONSHIP(400, "OMAS-SUBJECTAREA-400-026 ",
            "Glossary relationship with guid {0} supplied on a create. The relationship cannot exist prior to the relationship end being created.",
            "The system is unable to process the request.",
            "Correct the code in the caller to create with a valid glossary."),
    CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID(400, "OMAS-SUBJECTAREA-400-027 ",
            "Glossary with guid {0} does not exist. Cannot create without a glossary",
            "The system is unable to process the request.",
            "Correct the code in the caller to create with a valid glossary."),
    GLOSSARY_TERM_CREATE_WITH_PROJECTS(400, "OMAS-SUBJECTAREA-400-028 ",
            "Cannot create a Glossary Term {0} with projects",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the projects. Knit the term into a project after the term has been created."),
    GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY(400, "OMAS-SUBJECTAREA-400-029 ",
            "Glossary with name {0} does not exist. Cannot create a GlossaryCategory without a glossary",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a GlossaryCategory with a valid glossaryName."),
    GLOSSARY_TERM_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECTAREA-400-030 ",
            "GlossaryTerm creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary term and is unable to process the request.",
            "Retry the Glossary Term creation."),
    GLOSSARY_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECTAREA-400-031 ",
            "Glossary creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary and is unable to process the request.",
            "Retry the Glossary creation."),
    GLOSSARY_CONTENT_PREVENTED_DELETE(400, "OMAS-SUBJECTAREA-400-033 ",
            "Glossary (guid {0}) deletion failed, because there is glossary content",
            "The system is unable to process the request.",
            "Retry the Glossary deletion when it is is empty."),
    GLOSSARY_CATEGORY_CREATE_FAILED_ADDING_CLASSIFICATIONS(400, "OMAS-SUBJECTAREA-400-034 ",
            "Glossary Category creation with name {0} failed, because it was unable to create its classifications",
            "The system has deleted the Glossary category and is unable to process the request.",
            "Retry the Glossary Category creation."),
    GLOSSARY_CATEGORY_CREATE_FAILED_KNITTING_TO_GLOSSARY(400, "OMAS-SUBJECTAREA-400-035 ",
            "Glossary Category creation with name {0} failed, because it was unable to create the relationship with its Glossary",
            "The system has deleted the Glossary category and is unable to process the request.",
            "Retry the Glossary Category creation."),
    GLOSSARY_CATEGORY_CREATE_FAILED_CATEGORY_NAME_ALREADY_USED(400, "OMAS-SUBJECTAREA-400-036 ",
            "Glossary Category creation with name {0} failed, because the requested name was already present in a sibling category under the categories parent",
            "The system is unable to process the request.",
            "Retry the Glossary Category creation with a name that does not match a sibling categories name."),
    CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED(400, "OMAS-SUBJECTAREA-400-038 ",
            "Glossary Category update with name {0} failed, because the requested name was already present in a sibling category under the categories parent",
            "The system is unable to process the request.",
            "Retry the Glossary Category update with a name that does not match a sibling categories name."),
    CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY(400, "OMAS-SUBJECTAREA-400-039 ",
            "Glossary Category update with failed, because the category has been deleted.",
            "The system is unable to process the request.",
            "Retry the Glossary Category update against a category that has not been deleted."),
    GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY(400, "OMAS-SUBJECTAREA-400-040 ",
            "Glossary update with failed, because the glossary has been deleted.",
            "The system is unable to process the request.",
            "Retry the Glossary update against a glossary that has not been deleted."),
    TERM_UPDATE_FAILED_ON_DELETED_TERM(400, "OMAS-SUBJECTAREA-400-041 ",
            "Term update with failed, because the term has been deleted.",
            "The system is unable to process the request.",
            "Retry the term update against a term that has not been deleted."),
    TYPEDEF_NOT_KNOWN(400, "OMAS-SUBJECTAREA-400-042 ",
            "A request has been made for type {0} which is not known.",
            "The system is unable to process the request.",
            "Look into whether the typename was correctly specified."),
    TYPEDEF_ERROR(400, "OMAS-SUBJECTAREA-400-043 ",
            "An error occurred when processing a request involving type {0} .",
            "The system is unable to process the request.",
            "Contact your administrator to review the audit log to find the cause of the error."),
    ENTITY_NOT_KNOWN_ERROR(400, "OMAS-SUBJECTAREA-400-044 ",
            "An error occurred when processing a request involving entity {0}.",
            "The system is unable to process the request.",
            "Retry the request with a known entity."),
    RELATIONSHIP_NOT_KNOWN_ERROR(400, "OMAS-SUBJECTAREA-400-045 ",
            "An error occurred when processing a request involving relationship {0} .",
            "The system is unable to process the request.",
            "Retry the request with a known relationship."),


    // The following Exceptions are not descriptive enough - they need more information from the OMRS Exception
    INVALID_PARAMETER(400, "OMAS-SUBJECTAREA-400-046 ",
            "Invalid parameter.",
            "The system is unable to process the request.",
            "Please correct the parameter and retry."),
    CLASSIFICATION_ERROR(400, "OMAS-SUBJECTAREA-400-047 ",
            "Invalid parameter.",
            "The system is unable to process the request.",
            "Please correct the classification and retry."),
    STATUS_NOT_SUPPORTED_ERROR(400, "OMAS-SUBJECTAREA-400-048 ",
            "Status not supported.",
            "The system is unable to process the request.",
            "Please correct the status and retry."),
    FUNCTION_NOT_SUPPORTED(400, "OMAS-SUBJECTAREA-400-049 ",
            "Function not supported.",
            "The system is unable to process the request.",
            "Please so not use this function as it is unsupported."),
    PAGING_ERROR(400, "OMAS-SUBJECTAREA-400-050 ",
            "Paging error.",
            "The system is unable to process the request.",
            "Please retry the request with different paging options."),
    // End of The following Exception

    WRONG_TYPENAME(400, "OMAS-SUBJECTAREA-400-051 ",
            "The type name {0} is not of the expected type {1}",
            "The system is unable to populate the requested connection object.",
            "Amend the request to refer to an appropriate typename."),
    GLOSSARY_NAME_DOES_NOT_EXIST(400, "OMAS-SUBJECTAREA-400-052 ",
            "The glossary name {0}  does not exist in the metadata repository",
            "The system is unable to process the request without a guid that exists.",
            "Correct the code in the caller to provide a glossary name that exists on the metadata server."),
    GOVERNANCE_CLASSIFICATION_SUPPLIED_IN_CLASSIFICATIONS(400, "OMAS-SUBJECTAREA-400-053 ",
            "The goverance action classification {0} has been supplied with classifications",
            "The system is unable to process the request with the classification supplied this way.",
            "Correct the code in the caller to provide the {0} in the {0} field."),
    CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR(400, "OMAS-SUBJECTAREA-400-054 ",
            "Received unexpected response category {0} from the server.",
            "The system is unable to process the request.",
            "Contact your administrator to review the audit log to find the cause of the error."),
    CLIENT_INPUT_PARAMETER_COULD_NOT_BE_PROCESSED(400, "OMAS-SUBJECTAREA-400-0055 ",
            "The input parameter {0} could not be processed",
            "The system is unable to process the request with invalid parameters.",
            "Investigate why the input parameter is not valid (cannot be serialised to json)."),
    INVALID_STATUS_VALUE_SUPPLIED(400, "OMAS-SUBJECTAREA-400-0056 ",
            "A status value {0} was supplied - but this is not a valid status",
            "The system is unable to process the request with invalid parameters.",
            "Correct the code in the caller to provide a valid status."),
    STATUS_UPDATE_TO_DELETED_NOT_ALLOWED(400, "OMAS-SUBJECTAREA-400-0057 ",
            "A status was attempted to be updated to deleted, this is not permitted.",
            "The system is unable to process the request with invalid parameters.",
            "Correct the code in the caller to provide a valid status. Use delete call to change status to deleted."),
    GLOSSARY_TERM_CREATE_FAILED_KNITTING_TO_GLOSSARY(400, "OMAS-SUBJECTAREA-400-058 ",
            "GlossaryTerm creation with name {0} failed, because it was unable to create the relationship with its Glossary",
            "The system has deleted the Glossary term and is unable to process the request.",
            "Retry the Glossary Term creation."),
    GUID_NOT_PURGED_ERROR(400, "OMAS-SUBJECTAREA-400-059 ",
            "Guid {0} was not deleted.",
            "The system is unable to process the request.",
            "Contact your administrator to review the audit log to find the cause of the error."),
    GLOSSARY_CREATE_WITHOUT_NAME(400, "OMAS-SUBJECTAREA-400-060 ",
            "Cannot create a Glossary without a name",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a Glossary with a name."),
    GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY_NAME(400, "OMAS-SUBJECTAREA-400-061 ",
            "Glossary with name {0} does not exist. Cannot create a Glossary Category without a glossary",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a Glossary Category with a valid glossaryName."),
    GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID(400, "OMAS-SUBJECTAREA-400-062 ",
            "Glossary with guid {0} does not exist. Cannot create a Glossary Category without a glossary",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a Glossary Category with a valid glossary."),
    CREATE_WITH_CATEGORY_RELATIONSHIP(400, "OMAS-SUBJECTAREA-400-063 ",
            "Category relationship with guid {0} supplied on a create. The relationship cannot exist prior to the relationship end being created.",
            "The system is unable to process the request.",
            "Correct the code in the caller to create with a valid category."),
    CREATE_WITH_NON_EXISTANT_CATEGORY_GUID(400, "OMAS-SUBJECTAREA-400-064 ",
            "Category with guid {0} does not exist. Cannot create without a valid category guid",
            "The system is unable to process the request.",
            "Correct the code in the caller to create with a valid category guid."),
    UNABLE_TO_PARSE_SUPPLIED_JSON(400, "OMAS-SUBJECTAREA-400-0065 ",
            "Unable to parse the supplied json.",
            "The system is unable to process the request with invalid parameters.",
            "Correct the code in the caller to provide a valid json."),
    GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_PARENT(400, "OMAS-SUBJECTAREA-400-066 ",
            "Glossary category parent with guid {0} cannot be found. Cannot create a GlossaryCategory without a parent",
            "The system is unable to process the request.",
            "Correct the code in the caller to create a GlossaryCategory with a valid category parent."),
    GLOSSARY_CATEGORY_CREATE_WITH_PROJECTS(400, "OMAS-SUBJECTAREA-400-067 ",
            "Cannot create a Cateogrt {0} with projects",
            "The system is unable to process the request.",
            "Correct the code in the caller to remove the projects. Knit the category into a project after the category has been created."),
    INVALID_NODETYPE(400, "OMAS-SUBJECTAREA-400-068 ",
            "The nodeType passed is not valid for this operation",
            "The system is unable to process the request without a node type.",
            "Correct the code in the caller to provide a valid NodeType"),
    UNEXPECTED_NODETYPE(400, "OMAS-SUBJECTAREA-400-069 ",
            "The returned nodeType {0} did not equal the requested nodeType {1} ",
            "The system returned an invalid nodetype  in the response.",
            "Raising a github issue on the system."),
    INVALID_RELATIOMSHIP_GUID_WRONG_TYPE(400, "OMAS-SUBJECTAREA-400-070 ",
            "A get was issued for a relationship with guid {0}. The returned relationship was not of the required type {1} ",
            "The system returned an invalid nodetype  in the response.",
            "Raising a github issue on the system."),
    GUID_NOT_DELETED_ERROR(400, "OMAS-SUBJECTAREA-400-071 ",
            "A restore was issued for guid {0}, but the status was not deleted. Restores can only succeed after a soft delete.",
            "The system returns the not deleted response.",
            "Soft delete prior to attempting a restore."),
    PAST_EFFECTIVE_TO_DATE(400, "OMAS-SUBJECTAREA-400-072 ",
            "The effective to date passed on the {0} operation is in the past",
            "The system is unable to process the request that requests the effective date begins in the past.",
            "Correct the code in the caller to either not use the effective To date or use an effective date in the future."),
    PAST_EFFECTIVE_FROM_DATE(400, "OMAS-SUBJECTAREA-400-073 ",
            "The effective from date passed on the {0} operation is in the past",
            "The system is unable to process the request that requests the effective date begins in the past.",
            "Correct the code in the caller to either not use the effective To date or use an effective date in the future."),
    INCORRECT_EFFECTIVE_DATE_RANGE(400, "OMAS-SUBJECTAREA-400-074 ",
            "The supplied effectivity Date range  is incorrect, as it starts after it ends on the {0} operation",
            "The system is unable to process a request with an incorrect effectivity range.",
            "Correct the code in the caller to either not use the effectivity dates or to ensure the from is before the to Date."),
    ERROR_ENCODING_QUERY_PARAMETER(400, "OMAS-SUBJECTAREA-400-075 ",
            "An error occurred when attempting to encode the value of Query parameter {0}",
            "The client is unable to send the rest call as the supplied query parameter {0} cannot be encoded.",
            "Correct the code in the caller to supply a query parameter that can be encoded."),
    MAPPER_ENTITY_GUID_TYPE_ERROR(400, "OMAS-SUBJECTAREA-400-076 ",
            "An error occurred because guid {0} is not a {1} so the EntityDetail cannot be mapped to a {1} , ",
            "The server is unable to continue the call.",
            "Correct the code in the caller to supply a guid that corresponds to type {1}."),
    MAPPER_RELATIONSHIP_GUID_TYPE_ERROR(400, "OMAS-SUBJECTAREA-400-077 ",
            "An error occurred because guid {0} is not a {1} so the Relationship cannot be mapped to a {1} , ",
            "The server is unable to continue the call.",
            "Correct the code in the caller to supply a guid that corresponds to type {1}."),

    OMRS_NOT_INITIALIZED(404, "OMAS-SUBJECTAREA-404-001 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property org.odpi.openmetadata.accessservices.subjectarea.server.",
            "Check that the org.odpi.openmetadata.accessservices.subjectarea.server where the Asset Consumer OMAS is running initialized correctly." +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-SUBJECTAREA-404-002 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property org.odpi.openmetadata.accessservices.subjectarea.server.",
            "Check that the org.odpi.openmetadata.accessservices.subjectarea.server where the Asset Consumer OMAS is running initialized correctly." +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-SUBJECTAREA-404-003 ",
            "The requested connection {0} is not found in OMAS Server {1}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the open metadata org.odpi.openmetadata.accessservices.subjectarea.server URL is correct.  Retry the request when the connection is available in the OMAS Service"),




//    CONNECTION_NOT_FOUND(404, "OMAS-SUBJECTAREA-404-005 ",
//            "The requested connection {0} is not found in OMAS Server {1}, optional error message {2}",
//            "The system is unable to populate the requested connection object.",
//            "Check that the connection name and the OMAS Server URL is correct.  Retry the request when the connection is available in the OMAS Service"),
//    PROXY_CONNECTION_FOUND(404, "OMAS-SUBJECTAREA-404-006 ",
//            "Only an entity proxy for requested connection {0} is found in the open metadata org.odpi.openmetadata.accessservices.subjectarea.server {1}, error message was: {2}",
//            "The system is unable to populate the requested connection object.",
//            "Check that the connection name and the OMAS Server URL is correct.  Retry the request when the connection is available in the OMAS Service"),
//    ASSET_NOT_FOUND(404, "OMAS-SUBJECTAREA-404-006 ",
//            "The requested asset {0} is not found for connection {1}",
//            "The system is unable to populate the asset properties object because none of the open metadata repositories are returning the asset's properties.",
//            "Verify that the OMAS Service running and the connection definition in use is linked to the Asset definition in the metadata repository. Then retry the request."),
//    UNKNOWN_ASSET(404, "OMAS-SUBJECTAREA-404-006 ",
//            "The asset with unique identifier {0} is not found for method {1} of access service {2} in open metadata org.odpi.openmetadata.accessservices.subjectarea.server {3}, error message was: {4}",
//            "The system is unable to update information associated with the asset because none of the connected open metadata repositories recognize the asset's unique identifier.",
//            "The unique identifier of the asset is supplied by the caller.  Verify that the caller's logic is correct, and that there are no errors being reported by the open metadata repository. Once all errors have been resolved, retry the request."),
//

    NULL_CONNECTION_RETURNED(500, "OMAS-SUBJECTAREA-500-001 ",
            "The requested connection named {0} is not returned by the open metadata Server {1}",
            "The system is unable to create a connector because the OMAS Server is not returning the Connection properties.",
            "Verify that the OMAS org.odpi.openmetadata.accessservices.subjectarea.server running and the connection definition is correctly configured."),
    NULL_CONNECTOR_RETURNED(500, "OMAS-SUBJECTAREA-500-002 ",
            "The requested connector for connection named {0} is not returned by the OMAS Server {1}",
            "The system is unable to create a connector.",
            "Verify that the OMAS org.odpi.openmetadata.accessservices.subjectarea.server is running and the connection definition is correctly configured."),
    NULL_RESPONSE_FROM_API(503, "OMAS-SUBJECTAREA-503-001 ",
            "A null response was received from REST API call {0} to org.odpi.openmetadata.accessservices.subjectarea.server {1}",
            "The system has issued a call to an open metadata access service REST API in a remote org.odpi.openmetadata.accessservices.subjectarea.server and has received a null response.",
            "Look for errors in the remote org.odpi.openmetadata.accessservices.subjectarea.server's audit log and console to understand and correct the source of the error."),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-SUBJECTAREA-503-002 ",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The org.odpi.openmetadata.accessservices.subjectarea.server has issued a call to the open metadata access service REST API in a remote org.odpi.openmetadata.accessservices.subjectarea.server and has received an exception from the local client libraries.",
            "Look for errors in the local org.odpi.openmetadata.accessservices.subjectarea.server's console to understand and correct the source of the error. This could be due to the url being incorrect or the server not being up."),
    CLIENT_SIDE_API_REST_RESPONSE_ERROR(503, "OMAS-SUBJECTAREA-503-003 ",
            "The rest call successfully completed, but the response content could not be interpretted for API call {0} to repository {1}.  The error message was {2}",
            "REST API in a remote org.odpi.openmetadata.accessservices.subjectarea.server completed, but the response returned was not as expected.",
            "Look for errors in the local org.odpi.openmetadata.accessservices.subjectarea.server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(504, "OMAS-SUBJECTAREA-503-004 ",
            "The access service has not been initialized and can not support REST API call {0}",
            "The org.odpi.openmetadata.accessservices.subjectarea.server has received a call to one of its open metadata access services but is unable to process it because the access service is not active.",
            "If the org.odpi.openmetadata.accessservices.subjectarea.server is supposed to have this access service activated, correct the org.odpi.openmetadata.accessservices.subjectarea.server configuration and restart the org.odpi.openmetadata.accessservices.subjectarea.server.")
    ;

    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaErrorCode.class);


    /**
     * The constructor for SubjectAreaErrorCode expects to be passed one of the enumeration rows defined in
     * SubjectAreaErrorCode above.   For example:
     * <p>
     * SubjectAreaErrorCode   errorCode = SubjectAreaErrorCode.ASSET_NOT_FOUND;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    SubjectAreaErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode() {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage() {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("<== SubjectAreaErrorCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled()) {
            log.debug(String.format("==> SubjectAreaErrorCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction() {
        return userAction;
    }
}

