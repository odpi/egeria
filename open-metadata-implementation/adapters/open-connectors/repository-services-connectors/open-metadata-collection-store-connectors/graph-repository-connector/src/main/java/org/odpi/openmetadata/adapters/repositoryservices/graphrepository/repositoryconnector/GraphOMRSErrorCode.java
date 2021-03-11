/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


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
public enum GraphOMRSErrorCode implements ExceptionMessageSet
{
    INVALID_MATCH_CRITERIA(400, "OMRS-GRAPH-REPOSITORY-400-001",
            "The match criteria parameter was not set to a recognised value - it should be ANY, ALL or NONE - reported by the {0} method of class {1} to open metadata repository {2}",
            "The system is unable to perform the request because the match criteria is incorrect.",
            "Correct the caller's code and retry the request."),
    ENTITY_ALREADY_EXISTS(400, "OMRS-GRAPH-REPOSITORY-400-002",
            "There is an already an entity with GUID {0} so cannot honor request to create entity in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already an entity with the same GUID.",
            "Correct the caller's code and retry the request."),
    ENTITY_NOT_CREATED(400, "OMRS-GRAPH-REPOSITORY-400-003",
            "The attempt to create an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity create request.",
            "Correct the caller's code and retry the create request."),
    ENTITY_NOT_FOUND(400, "OMRS-GRAPH-REPOSITORY-400-004",
            "The attempt to retrieve an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code and retry the entity retrieval request."),
    RELATIONSHIP_NOT_CREATED(400, "OMRS-GRAPH-REPOSITORY-400-005",
            "The attempt to create a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship create request.",
            "Correct the caller's code and retry the relationship create request."),
    RELATIONSHIP_NOT_FOUND(400, "OMRS-GRAPH-REPOSITORY-400-006",
            "The attempt to retrieve a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship retrieval.",
            "Correct the caller's code and retry the relationship retrieval request."),
    ENTITY_NOT_UPDATED(400, "OMRS-GRAPH-REPOSITORY-400-007",
            "The attempt to update an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity update request.",
            "Correct the caller's code and retry the entity update request."),
    RELATIONSHIP_NOT_UPDATED(400, "OMRS-GRAPH-REPOSITORY-400-008",
            "The attempt to update a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship update request.",
            "Correct the caller's code and retry the relationship update request."),
    RELATIONSHIP_TYPE_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-009",
            "The attempt to map an edge to a relationship failed because no type was found with type name {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship retrieval request.",
            "Correct the caller's code to use recognized type definition identifiers for the relationship and retry the request."),
    RELATIONSHIP_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-010",
            "The attempt to map an edge and a relationship failed because the properties could not be mapped for relationship with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship mapping request.",
            "Correct the caller's code to request valid relationship properties and retry the request."),
    ENTITY_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-011",
            "The attempt to map a vertex and an entity failed because the properties could not be mapped for entity with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity mapping request.",
            "Correct the caller's code to request valid entity properties and retry the request."),
    ENTITY_TYPE_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-012",
            "The type found in a vertex could not be identified, type name {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity mapping request.",
            "Correct the caller's code to use valid entity type definition identifiers and retry the request."),
    GRAPH_INITIALIZATION_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-013",
            "The graph database could not be initialized for open metadata repository {0}",
            "The server was unable to initialize.",
            "Please raise a github issue to investigate the initialization issue."),
    CLASSIFICATION_PROPERTIES_ERROR(400, "OMRS-GRAPH-REPOSITORY-400-014",
            "The attempt to map a vertex and a classification failed because the properties could not be mapped for classification with name {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the classification mapping request.",
            "Correct the caller's code and retry the classification request."),
    ENTITY_PROXY_ONLY(400, "OMRS-GRAPH-REPOSITORY-400-015",
            "The attempt to retrieve an entity with GUID {0} found an entity proxy in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code to request an entity and retry the request."),
    ENTITY_TYPE_GUID_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-018",
            "The attempt to find an entity type failed because no type was found with typeGUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to retrieve the entity type.",
            "Correct the caller's code to use the correct entity type definition GUID and retry the request."),
    RELATIONSHIP_TYPE_GUID_NOT_KNOWN(400, "OMRS-GRAPH-REPOSITORY-400-019",
            "The attempt to find a relationship type failed because no type was found with typeGUID {0} failed in {1} method of class {2} to open " +
                 "metadata repository {3}",
            "The system was unable to retrieve the relationship type.",
            "Correct the caller's code to use the correct relationship type definition GUID and retry the request."),
    CONNECTED_ENTITIES_FAILURE(400, "OMRS-GRAPH-REPOSITORY-400-020",
            "The attempt to retrieve the path between two entities failed, start entity has GUID {0} and end entity has GUID {1} in {2} method of class {3} to open metadata repository {4}",
            "The system was unable to perform the entity retrieval.",
            "Correct the caller's code and retry the graph retrieval request."),
    CANNOT_OPEN_GRAPH_DB(400, "OMRS-GRAPH-REPOSITORY-400-021",
            "It is not possible to open the graph database in the {0} method of {1} class for repository {2}",
            "The system was unable to open the graph repository graph database",
            "Please check that the graph database exists and is not in use by another process."),
    GRAPH_DB_HAS_DIFFERENT_METADATACOLLECTION_ID(400, "OMRS-GRAPH-REPOSITORY-400-022",
            "It is not possible to open the graph database with metadataCollectionId {0}, because the repository connector has metadataCollectionId {1}",
            "The system was unable to open the graph repository graph database",
            "Please check the configuration of the repository connection and update it if necessary."),
    RELATIONSHIP_ALREADY_EXISTS(400, "OMRS-GRAPH-REPOSITORY-400-023",
            "There is an already a relationship with GUID {0} so cannot honor request to create relationship in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already a relationship with the same GUID.",
            "Correct the caller's code and retry the create request."),
    INVALID_MATCH_PROPERTY(
            400, "OMRS-GRAPH-REPOSITORY-400-024",
            "The match property with name {0} is of type {1} instead of the correct type {2} - reported by the {3} method of class {4} to open metadata repository {5}",
            "The system is unable to perform the request because the match properties are not valid.",
            "Check the named match property is not deprecated and has the correct type, then retry the request."),
    INVALID_PROPERTY_CONDITION(
            400, "OMRS-GRAPH-REPOSITORY-400-025",
            "The search properties contains an invalid combination of property conditions - reported by the {0} method of class {1} to open metadata repository {2}",
            "The system is unable to perform the request because the search properties is incorrect.",
            "Correct the caller's code and retry the request."),
    UNSUPPORTED_SEARCH_PROPERTY_OPERATOR(
            400, "OMRS-GRAPH-REPOSITORY-400-026",
            "The search properties contains a property condition with unsupported operator {0} - reported by the {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because the search properties operator is not supported.",
            "Please use a different operator or raise an issue."),
    INVALID_SEARCH_PROPERTY_VALUE(
            400, "OMRS-GRAPH-REPOSITORY-400-027",
            "The search properties contains an invalid value for property {0} - reported by the {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because the search property does not have a valid value.",
            "Correct the caller's code and retry the request."),
    INVALID_SEARCH_PROPERTY_NAME(
            400, "OMRS-GRAPH-REPOSITORY-400-028",
            "The search properties contains an unknown property {0} - reported by the {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because the search property is not known.",
            "Correct the caller's code and retry the request."),
    INVALID_SEARCH_PROPERTY_TYPE(
            400, "OMRS-GRAPH-REPOSITORY-400-029",
            "The search properties contains a values that do not match the type of property {0} - reported by the {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because the provided values do not match the type of the property.",
            "Correct the caller's code and retry the request."),

    ;

    private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for GraphOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * GraphOMRSErrorCode above.   For example:
     *
     *     GraphOMRSErrorCode   errorCode = GraphOMRSErrorCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GraphOMRSErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
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
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
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
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
