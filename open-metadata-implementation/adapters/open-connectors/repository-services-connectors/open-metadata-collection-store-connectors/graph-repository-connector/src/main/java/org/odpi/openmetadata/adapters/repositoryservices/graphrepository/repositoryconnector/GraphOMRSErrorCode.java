/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GraphOMRSErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMRS
 * It is used in conjunction with all OMRS Exceptions, both Checked and Runtime (unchecked).
 *
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
public enum GraphOMRSErrorCode
{
    INVALID_MATCH_CRITERIA(400, "OMRS-GRAPH-REPOSITORY-400-001 ",
            "The match criteria parameter was not set to a recognised value - it should be ANY, ALL or NONE - reported by the {0} method of class {1} to open metadata repository {2}",
            "The system is unable to perform the request because the match criteria is incorrect.",
            "Correct the caller's code and retry the request."),
    ENTITY_ALREADY_EXISTS(400, "OMRS-GRAPH-REPOSITORY-400-002 ",
            "There is an already an entity with GUID {0} so cannot honour request to create entity in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already an entity with the same GUID.",
            "Correct the caller's code and retry the request."),
    ENTITY_NOT_CREATED(400, "OMRS-GRAPH-REPOSITORY-400-003 ",
            "The attempt to create an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity create request.",
            "Correct the caller's code and retry the request."),
    ENTITY_NOT_FOUND(400, "OMRS-GRAPH-REPOSITORY-400-004 ",
            "The attempt to retrieve an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_NOT_CREATED(400, "OMRS-GRAPH-REPOSITORY-400-005 ",
            "The attempt to create a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship create request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_NOT_FOUND(400, "OMRS-GRAPH-REPOSITORY-400-006 ",
            "The attempt to retrieve a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship retrieval.",
            "Correct the caller's code and retry the request."),
    ENTITY_NOT_UPDATED(400, "OMRS-GRAPH-REPOSITORY-400-007 ",
            "The attempt to update an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity update request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_NOT_UPDATED(400, "OMRS-GRAPH-REPOSITORY-400-008 ",
            "The attempt to update a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship update request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_TYPE_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-009 ",
            "The attempt to map an edge to a relationship failed because no type was found with type name {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship retrieval request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-010 ",
            "The attempt to map an edge and a relationship failed because the properties could not be mapped for relationship with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship mapping request.",
            "Correct the caller's code and retry the request."),
    ENTITY_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-011 ",
            "The attempt to map a vertex and an entity failed because the properties could not be mapped for entity with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity mapping request.",
            "Correct the caller's code and retry the request."),
    ENTITY_TYPE_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-012 ",
            "The type found in a vertex could not be identified, type name {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity mapping request.",
            "Correct the caller's code and retry the request."),
    GRAPH_INITIALIZATION_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-013 ",
            "The graph database could not be initialized for open metadata repository {0}",
            "The system was unable to initialize.",
            "Please raise a github issue."),
    CLASSIFICATION_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-014 ",
            "The attempt to map a vertex and a classification failed because the properties could not be mapped for classification with name {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the classification mapping request.",
            "Correct the caller's code and retry the request."),
    ENTITY_PROXY_ONLY(400, "OMRS-GRAPH-REPOSITORY-400-015 ",
            "The attempt to retrieve an entity with GUID {0} found an entity proxy in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code and retry the request."),
    METADATA_COLLECTION_NOT_FOUND(400, "OMRS-GRAPH-REPOSITORY-400-016 ",
            "Could not access the metadata collection with id \"{0}\" for the repository",
            "The connector is unable to proceed",
            "Check the system logs and diagnose or report the problem."),
    EVENT_MAPPER_NOT_INITIALIZED(400, "OMRS-GRAPH-REPOSITORY-400-017 ",
            "There is no valid event mapper for repository \"{1}\"",
            "The refresh request could not be processed",
            "Check the system logs and diagnose or report the problem."),
    ENTITY_TYPE_GUID_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-018 ",
            "The attempt to find an entity type failed because no type was found with typeGUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to retrieve the entity type.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_TYPE_GUID_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-019 ",
        "The attempt to find a relationship type failed because no type was found with typeGUID {0} failed in {1} method of class {2} to open metadata repository {3}",
        "The system was unable to retrieve the relationship type.",
        "Correct the caller's code and retry the request."),
    CONNECTED_ENTITIES_FAILURE(400, "OMRS-GRAPH-REPOSITORY-400-020 ",
            "The attempt to retrieve the path between two entities failed, start entity has GUID {0} and end entity has GUID {1} in {2} method of class {3} to open metadata repository {4}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code and retry the request."),
    CANNOT_OPEN_GRAPH_DB(400, "OMRS-GRAPH-REPOSITORY-400-021 ",
            "It is not possible to open the graph database in the {0} method of {1} class for repository {2}",
            "The system was unable to open the graph repository graph database",
            "Please check that the graph database exists and is not in use by another process."),
    GRAPH_DB_HAS_DIFFERENT_METADATACOLLECTION_ID(400, "OMRS-GRAPH-REPOSITORY-400-022",
            "It is not possible to open the graph database with metadataCollectionId {0}, because the repository connector has metadataCollectionId {1}",
            "The system was unable to open the graph repository graph database",
            "Please check the configuration of the repository connection and update it if necessary."),
    RELATIONSHIP_ALREADY_EXISTS(400, "OMRS-GRAPH-REPOSITORY-400-023 ",
            "There is an already a relationship with GUID {0} so cannot honour request to create relationship in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already a relationship with the same GUID.",
            "Correct the caller's code and retry the request."),

    ;

    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSErrorCode.class);


    /**
     * The constructor for GraphOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * OMRSErrorCode above.   For example:
     *
     *     OMRSErrorCode   errorCode = GraphOMRSErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  error code to use over REST calls
     * @param newErrorMessageId  unique Id for the message
     * @param newErrorMessage  text for the message
     * @param newSystemAction  description of the action taken by the system when the error condition happened
     * @param newUserAction  instructions for resolving the error
     */
    GraphOMRSErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params  strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== GraphOMRSErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> GraphOMRSErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GraphOMRSErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
