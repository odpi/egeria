/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OCF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * OCF Connectors.  It is used in conjunction with all OCF Exceptions, both Checked and Runtime (unchecked).
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum OCFErrorCode implements ExceptionMessageSet
{
    /**
     * OCF-CONNECTION-400-001 - Null connection object passed on request for new connector instance
     */
    NULL_CONNECTION(400, "OCF-CONNECTION-400-001",
            "Null connection object passed on request for new connector instance",
            "The system is unable to create the requested connector instance without the connection information that describes which type of connector is required.",
            "Recode call to system to include a correctly formatted connection object and retry the request."),

    /**
     * OCF-CONNECTION-400-002 - Unnamed connection object passed to requested action {0}
     */
    UNNAMED_CONNECTION(400, "OCF-CONNECTION-400-002",
            "Unnamed connection object passed to requested action {0}",
            "The system is unable to perform the requested action without a connection name.",
            "Update connection configuration to include a value for at least one of the following name properties: qualifiedName, displayName, guid. Then retry the request."),

    /**
     * OCF-CONNECTION-400-003 - Null connectorType property passed in connection {0}
     */
    NULL_CONNECTOR_TYPE(400, "OCF-CONNECTION-400-003",
            "Null connectorType property passed in connection {0}",
            "The system is unable to create the requested connector instance without information on the type of connection required.",
            "Update the connection configuration to include a valid connectorType definition.  Then retry the request."),

    /**
     * OCF-CONNECTION-400-004 - Null Connector Provider passed in connection {0}
     */
    NULL_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-004",
            "Null Connector Provider passed in connection {0}",
            "The system is unable to create the requested connector instance without information on the type of connection required.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTION-400-005 - Unknown Connector Provider class {0} passed in connection {1}
     */
    UNKNOWN_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-005",
            "Unknown Connector Provider class {0} passed in connection {1}",
            "The system is unable to create the requested connector instance because the Connector Provider's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTION-400-006 - Class {0} passed in connection {1} is not a Connector Provider
     */
    NOT_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-006",
            "Class {0} passed in connection {1} is not a Connector Provider",
            "The system is unable to create the requested connector instance because the Connector Provider's class does not implement org.odpi.openmetadata.ConnectorProvider.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTION-400-008 - Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3}
     */
    INVALID_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-008",
            "Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3}",
            "The system is unable to create the requested connector instance because the Connector Provider's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTION-400-009 - Null endpoint detected in connection {0}
     */
    NULL_ENDPOINT_IN_CONNECTION(400, "OCF-CONNECTION-400-009",
            "Null endpoint detected in connection {0}",
            "The system is unable to initialize the requested connector because the endpoint information in the connection is missing.",
            "Add the endpoint information into the connection object and retry the request."),

    /**
     * OCF-CONNECTION-400-010 - The endpoint attribute {0} in connection {1} is set to "{2}" which is invalid
     */
    MALFORMED_ENDPOINT(400, "OCF-CONNECTION-400-010",
            "The endpoint attribute {0} in connection {1} is set to \"{2}\" which is invalid",
            "The system is unable to initialize the requested connector because the endpoint information in the connection is not formatted correctly for this type of connection.",
            "Correct the endpoint information into the connection object and retry the request."),

    /**
     * OCF-PROPERTIES-400-011 - Null property name passed to entity {0} of type {1}
     */
    NULL_PROPERTY_NAME(400, "OCF-PROPERTIES-400-011",
            "Null property name passed to entity {0} of type {1}",
            "A request to set an additional property failed because the property name passed was null",
            "Recode the call to the property object with a valid property name and retry."),

    /**
     * OCF-PROPERTIES-400-012 - Non-string property names stored in entity {0} of type {1}
     */
    INVALID_PROPERTY_NAMES(400, "OCF-PROPERTIES-400-012",
            "Non-string property names stored in entity {0} of type {1}",
            "A request to retrieve additional properties failed because the properties have become corrupted.",
            "Debug the calls to the properties object."),

    /**
     * OCF-PROPERTIES-400-013 - Null securedProperty name passed to connection {0}
     */
    NULL_SECURED_PROPERTY_NAME(400, "OCF-PROPERTIES-400-013",
            "Null securedProperty name passed to connection {0}",
            "A request to set a secured property failed because the property name passed was null",
            "Recode the call to the connection object with a valid property name and retry."),

    /**
     * OCF-PROPERTIES-400-014 - No more elements in {0} iterator
     */
    NO_MORE_ELEMENTS(400, "OCF-PROPERTIES-400-014",
            "No more elements in {0} iterator",
            "A caller stepping through an iterator has requested more elements when there are none left.",
            "Recode the caller to use the hasNext() method to check for more elements before calling next() and then retry."),

    /**
     * OCF-PROPERTIES-400-015 - No type-specific iterator for {0} paging iterator
     */
    NO_ITERATOR(400, "OCF-PROPERTIES-400-015",
            "No type-specific iterator for {0} paging iterator",
            "A caller requesting a paging iterator has not supplied a type-specific iterator in the constructor.",
            "Recode the caller to use the hasNext() method to check for more elements before calling next() and then retry."),

    /**
     * OCF-PROPERTIES-400-016 - No classification name for entity {0} of type {1}
     */
    NULL_CLASSIFICATION_NAME(400, "OCF-PROPERTIES-400-016",
            "No classification name for entity {0} of type {1}",
            "A classification with a null name is assigned to an entity.   This value should come from a metadata repository, and always be filled in.",
            "Look for other error messages to identify the source of the problem.  Identify the metadata repository where the asset came from.  Correct the cause of the error and then retry."),

    /**
     * OCF-PROPERTIES-400-017 - No tag name for entity {0} of type {1}
     */
    NULL_TAG_NAME(400, "OCF-PROPERTIES-400-017",
            "No tag name for entity {0} of type {1}",
            "A tag with a null name is assigned to an entity.   This value should come from a metadata repository, and always be filled in.",
            "Look for other error messages to identify the source of the problem.  Identify the metadata repository where the asset came from.  Correct the cause of the error and then retry."),

    /**
     * OCF-PROPERTIES-400-018 - Unable to remove element through iterator {0}
     */
    UNABLE_TO_REMOVE(400, "OCF-PROPERTIES-400-018",
            "Unable to remove element through iterator {0}",
            "The caller has called the remove() method on one of the iterators from Connected Asset Properties.  This is not supported.",
            "Remove the call to the remove() method and retry."),

    /**
     * OCF-PROPERTIES-400-019 - Virtual connection {0} has no embedded connections
     */
    INVALID_VIRTUAL_CONNECTION(400, "OCF-PROPERTIES-400-019",
            "Virtual connection {0} has no embedded connections",
            "The virtual connection properties object is invalid because it does not include any embedded connections.",
            "Add embedded connections to the virtual connection and retry the request."),


    /*
     * Invalid use of statistics methods.
     */

    /**
     * OCF-STATISTICS-400-001 - The {0} is already in use as a counter statistic and can not be used by the {1} method to {2}
     */
    ALREADY_COUNTER_NAME(400, "OCF-STATISTICS-400-001",
                         "The {0} is already in use as a counter statistic and can not be used by the {1} method to {2}",
                         "The integration context returns an exception on the invalid request.",
                         "Change the connector logic to use a different name for the statistic."),

    /**
     * OCF-STATISTICS-400-002 - The {0} is already in use as a property statistic and can not be used by the {1} method to {2}
     */
    ALREADY_PROPERTY_NAME(400, "OCF-STATISTICS-400-002",
                          "The {0} is already in use as a property statistic and can not be used by the {1} method to {2}",
                          "The integration context returns an exception on the invalid request.",
                          "Change the connector logic to use a different name for the statistic."),

    /**
     * OCF-STATISTICS-400-003 - The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2}
     */
    ALREADY_TIMESTAMP_NAME(400, "OCF-STATISTICS-400-003",
                           "The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2}",
                           "The integration context returns an exception on the invalid request.",
                           "Change the connector logic to use a different name for the statistic."),

    /**
     * OCF-CONNECTOR-404-001 - Endpoint {0} in connection {1} for connector instance {2} is either unknown or unavailable
     */
    UNKNOWN_ENDPOINT(404, "OCF-CONNECTOR-404-001",
            "Endpoint {0} in connection {1} for connector instance {2} is either unknown or unavailable",
            "The requested action is not able to complete because the remote endpoint where the assets are located is not responding.  It may be unavailable or unknown.",
            "Verify that the endpoint information is correct and the server that supports it is operational, then retry the request."),

    /**
     * OCF-PROPERTIES-404-002 - Exception with error message "{0}" was returned to object {1} resulted from a request for connected asset properties
     */
    PROPERTIES_NOT_AVAILABLE(404, "OCF-PROPERTIES-404-002",
            "Exception with error message \"{0}\" was returned to object {1} resulted from a request for connected asset properties",
            "The requested action is not able to complete which may mean that the server is not able to return all the properties associated with the asset.",
                     "Verify that the endpoint information is correct and the server that supports it is operational, then retry the request."),

    /**
     * OCF-PROPERTIES-404-003 - {0} retrieved a schema attribute of type {1} which does not have an associated schema type.  The schema attribute is from metadata collection {2} ({3})
     */
    UNKNOWN_SCHEMA_TYPE(404, "OCF-PROPERTIES-404-003",
            "{0} retrieved a schema attribute of type {1} which does not have an associated schema type.  The schema attribute is from metadata " +
                    "collection {2} ({3})",
            "The requested action is not able to complete because part of the schema attribute was not found in the repositories.",
            "Trace the origin of the schema attribute using the metadata collection information.  Determine why it was not created " +
                                "correctly and correct its definition. Then retry the request."),

    /**
     * OCF-CONNECTION-500-001 - OCF method detected an unexpected exception
     */
    CAUGHT_EXCEPTION(500, "OCF-CONNECTION-500-001",
            "OCF method detected an unexpected exception",
            "The system detected an error during connector processing.",
            "The root cause of the error is captured in previous reported messages."),

    /**
     * OCF-CONNECTION-500-002 - OCF method {0} detected an unexpected exception, message was {1}
     */
    CAUGHT_EXCEPTION_WITH_MSG(500, "OCF-CONNECTION-500-002",
                              "OCF method {0} detected an unexpected exception, message was {1}",
                              "The system detected an error during connector processing.",
                              "The root cause of the error is captured in previous reported messages."),

    /**
     * OCF-CONNECTION-500-003 - OCF method {0} not yet implemented
     */
    NOT_IMPLEMENTED(500, "OCF-CONNECTION-500-003",
            "OCF method {0} not yet implemented",
            "The system is not able to process a request because a feature is not yet implemented.",
            "Contact your support organization for help in discovering a workaround, fix or upgrade to the system."),

    /**
     * OCF-CONNECTION-500-004 - Connection error detected
     */
    UNKNOWN_ERROR(500, "OCF-CONNECTION-500-004",
            "Connection error detected",
            "The system detected an error during connection processing.",
            "The root cause of the error is captured in previous reported messages."),

    /**
     * OCF-CONNECTION-500-005 - Internal error in OCF method {0}
     */
    INTERNAL_ERROR(500, "OCF-CONNECTION-500-005",
            "Internal error in OCF method {0}",
            "The system detected an error during connection processing.",
            "The root cause of the error is captured in previous reported messages."),

    /**
     * OCF-CONNECTOR-500-006 - The class name for the connector is not set up
     */
    NULL_CONNECTOR_CLASS(500, "OCF-CONNECTOR-500-006",
            "The class name for the connector is not set up",
            "The system is unable to create the requested connector instance without the name of the Java class for the connector.",
            "Update the implementation of the connector provider to ensure the connector's java class is initialized correctly"),

    /**
     * OCF-CONNECTOR-500-007 - Unknown Connector Java class {0} for Connector {1}
     */
    UNKNOWN_CONNECTOR(500,"OCF-CONNECTOR-500-007",
            "Unknown Connector Java class {0} for Connector {1}",
            "The system is unable to create the requested connector instance because the Connector's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTOR-500-008 - Java class {0} for connector named {1} does not implement the Connector interface
     */
    NOT_CONNECTOR(500,"OCF-CONNECTOR-500-008",
            "Java class {0} for connector named {1} does not implement the Connector interface",
            "The system is unable to create the requested connector instance because the Connector's class does not implement org.odpi.openmetadata.Connector.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

    /**
     * OCF-CONNECTOR-500-009 - Unable to load Connector Java class {0}
     */
    INCOMPLETE_CONNECTOR(500,"OCF-CONNECTOR-500-009",
            "Unable to load Connector Java class {0}",
            "The system is unable to create the requested connector instance because the Connector's class is failing to load in the JVM.  This has resulted in an exception in the class loader.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process. Then retry the request."),

    /**
     * OCF-CONNECTION-500-010 - Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3}
     */
    INVALID_CONNECTOR(500, "OCF-CONNECTION-500-010",
            "Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3}",
            "The system is unable to create the requested connector instance because the Connector's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Then retry the request."),

    /**
     * OCF-CONNECTION-500-011 - Connector Provider {0} returned a null connector instance for connection {1}
     */
    NULL_CONNECTOR(500, "OCF-CONNECTION-500-011",
            "Connector Provider {0} returned a null connector instance for connection {1}",
            "The system detected an error during connector processing and was unable to create a connector.",
            "The root cause of the error is captured in previous reported messages."),

    /**
     * OCF-CONNECTOR-500-012 - Java class {0} is not a VirtualConnector and so can not support VirtualConnection {1}
     */
    NOT_VIRTUAL_CONNECTOR(500,"OCF-CONNECTOR-500-012",
            "Java class {0} is not a VirtualConnector and so can not support VirtualConnection {1}",
            "The system is unable to create the requested connector instance because the supplied connection is a virtual connection but the connector's class does not implement org.odpi.openmetadata.VirtualConnector.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request."),

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
     * @param errorMessageId   unique 1d for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OCFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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