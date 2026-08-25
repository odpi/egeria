/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
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
            "The system cannot create the requested connector instance without the connection information that describes which type of connector is required.",
            "Recode call to system to include a correctly formatted connection object and retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-400-003 - Null connectorType property passed in connection {0}
     */
    NULL_CONNECTOR_TYPE(400, "OCF-CONNECTION-400-003",
            "Null connectorType property passed in connection {0}",
            "The system cannot create the requested connector instance without information on the type of connection required.",
            "Update the connection configuration to include a valid connectorType definition.  Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-400-004 - Null Connector Provider passed in connection {0}
     */
    NULL_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-004",
            "Null Connector Provider passed in connection {0}",
            "The system cannot create the requested connector instance without information on the type of connection required.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-400-005 - Unknown Connector Provider class {0} passed in connection {1}
     */
    UNKNOWN_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-005",
            "Unknown Connector Provider class {0} passed in connection {1}",
            "The system cannot create the requested connector instance because the Connector Provider's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-400-006 - Class {0} passed in connection {1} is not a Connector Provider
     */
    NOT_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-006",
            "Class {0} passed in connection {1} is not a Connector Provider",
            "The system cannot create the requested connector instance because the Connector Provider's class does not implement org.odpi.openmetadata.ConnectorProvider.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-400-008 - Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3}
     */
    INVALID_CONNECTOR_PROVIDER(400, "OCF-CONNECTION-400-008",
            "Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3}",
            "The system cannot create the requested connector instance because the Connector Provider's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    MALFORMED_DATE_CONFIGURATION_PROPERTY(400, "OCF-CONNECTION-400-011",
                       "The {0} configuration property of {1} is set to an invalid date format.  Use dd/MM/yyyy/hh:mm:ss",
                       "The system cannot initialize the requested connector because the configuration property in the connection is not formatted correctly.",
                       "Correct the configuration property into the connection object and retry the request.",
                       "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-PROPERTIES-400-014 - No more elements in {0} iterator
     */
    NO_MORE_ELEMENTS(400, "OCF-PROPERTIES-400-014",
            "No more elements in {0} iterator",
            "A caller stepping through an iterator has requested more elements when there are none left.",
            "Recode the caller to use the hasNext() method to check for more elements before calling next() and then retry.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-PROPERTIES-400-019 - Virtual connection {0} has no embedded connections
     */
    INVALID_VIRTUAL_CONNECTION(400, "OCF-PROPERTIES-400-019",
            "Virtual connection {0} has no embedded connections",
            "The virtual connection properties object is invalid because it does not include any embedded connections.",
            "Add embedded connections to the virtual connection and retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /*
     * Invalid use of statistics methods.
     */

    /**
     * OCF-STATISTICS-400-001 - The {0} is already in use as a counter statistic and can not be used by the {1} method to {2}
     */
    ALREADY_COUNTER_NAME(400, "OCF-STATISTICS-400-001",
                         "The {0} is already in use as a counter statistic and can not be used by the {1} method to {2}",
                         "The integration context returns an exception on the invalid request.",
                         "Change the connector logic to use a different name for the statistic.",
                         "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-STATISTICS-400-002 - The {0} is already in use as a property statistic and can not be used by the {1} method to {2}
     */
    ALREADY_PROPERTY_NAME(400, "OCF-STATISTICS-400-002",
                          "The {0} is already in use as a property statistic and can not be used by the {1} method to {2}",
                          "The integration context returns an exception on the invalid request.",
                          "Change the connector logic to use a different name for the statistic.",
                          "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-STATISTICS-400-003 - The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2}
     */
    ALREADY_TIMESTAMP_NAME(400, "OCF-STATISTICS-400-003",
                           "The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2}",
                           "The integration context returns an exception on the invalid request.",
                           "Change the connector logic to use a different name for the statistic.",
                           "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-005 - Asset {0} is of type {1} but the {2} connector only supports the following asset type(s): {3}
     */
    INVALID_ASSET_TYPE(400, "OCF-CONNECTOR-400-005",
                       "Asset {0} is of type {1} but the {2} connector only supports the following asset type(s): {3}",
                       "The connector terminates.",
                       "The caller has requested a connector work with the wrong type of asset.  It should be reconfigured with the correct type of asset and rerun.",
                       "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-006 - Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}
     */
    INVALID_ROOT_SCHEMA_TYPE(400, "OCF-CONNECTOR-400-006",
                             "Asset {0} has a root schema of type {1} but connector {2} only supports the following root schema type(s): {3}",
                             "The connector terminates because it can not proceed.",
                             "The caller has requested a governance request type that cannot process a root schema for an asset because its type is unsupported." +
                                     "  This problem could be resolved by issuing the survey request with " +
                                     "a governance request type that is compatible with the asset's schema, or changing the connector " +
                                     "associated with the governance request type to one that supports this type of schema.",
                                     "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-007 - {0} asset {1} describes a resource called {2} which is of type {3} but connector {4} only supports the following type(s) of resources: {5}
     */
    INVALID_RESOURCE(400, "OCF-CONNECTOR-400-007",
                     "{0} asset {1} describes a resource called {2} which is of type {3} but connector {4} only supports the following type(s) of resources: {5}",
                     "The connector terminates because it does not know how to process this type of resource.",
                     "There is a mismatch between the asset in the open metadata catalog and the resource that it represents. Update the asset in the asset catalog so that it is matched with more appropriate services.",
                     "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-008 - {0} asset {1} describes a resource called {2} does not exist
     */
    NO_RESOURCE(400, "OCF-CONNECTOR-400-008",
                "{0} asset {1} describes a resource called {2} that does not exist",
                "The connector terminates because it does not have access to the resource.",
                "Ensure the resource is correctly identified in the asset. Rerun this request when the resource is created.",
                "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-009 - The {0} connector cannot proceed with is processing of {1} because the configuration property called {2} was not supplied
     */
    MISSING_CONFIGURATION_PROPERTY(400, "OCF-CONNECTOR-400-009",
                                   "The {0} connector cannot proceed with is processing of {1} because the configuration property called {2} was not supplied",
                                   "The connector stop processing the named element.",
                                   "Update the source of the configuration properties.  This is typically in the connector's connection.  However, the configuration properties may be overridden by, say, the CatalogTarget relationship linking the connector to the resource it is processing.",
                                   "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-010 - The {0} connector cannot proceed with is processing because the endpoint address is null
     */
    MISSING_ENDPOINT_ADDRESS(400, "OCF-CONNECTOR-400-010",
                                   "The {0} connector cannot proceed with is processing because the endpoint address is null",
                                   "The connector cannot access the digital resource it is supposed to connect to.",
                                   "Update the source of the endpoint.  This may be from a template or from a connector.",
                                   "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-400-011 - {0} element {1} does not exist
     */
    MISSING_ELEMENT(400, "OCF-CONNECTOR-400-011",
                "{0} element {1} does not exist",
                "The connector terminates because it can not find an element that it depends on.",
                "Ensure the element is correctly identified and exists in the metadata repository.  Then retry the request.",
                "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-500-001 - OCF method detected an unexpected exception
     */
    CAUGHT_EXCEPTION(500, "OCF-CONNECTION-500-001",
            "OCF method detected an unexpected exception",
            "The system detected an error during connector processing.",
            "The root cause of the error is captured in previous reported messages.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-500-002 - No information about the asset {0} has been returned from the asset store for connector {1}
     */
    NO_ASSET(500, "OCF-CONNECTOR-500-002",
             "No information about the asset {0} has been returned from the asset store for connector {1}",
             "The connector terminates without running the requested function.",
             "This is an unexpected condition because if the metadata server was unavailable, an exception would have been caught.",
             "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-500-006 - The class name for the connector is not set up
     */
    NULL_CONNECTOR_CLASS(500, "OCF-CONNECTOR-500-006",
            "The class name for the connector is not set up",
            "The system cannot create the requested connector instance without the name of the Java class for the connector.",
            "Update the implementation of the connector provider to ensure the connector's java class is initialized correctly",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-500-007 - Unknown Connector Java class {0} for Connector {1}
     */
    UNKNOWN_CONNECTOR(500,"OCF-CONNECTOR-500-007",
            "Unknown Connector Java class {0} for Connector {1}",
            "The system cannot create the requested connector instance because the Connector's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTOR-500-008 - Java class {0} for connector named {1} does not implement the Connector interface
     */
    NOT_CONNECTOR(500,"OCF-CONNECTOR-500-008",
            "Java class {0} for connector named {1} does not implement the Connector interface",
            "The system cannot create the requested connector instance because the Connector's class does not implement org.odpi.openmetadata.Connector.",
            "Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-500-010 - Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3}
     */
    INVALID_CONNECTOR(500, "OCF-CONNECTION-500-010",
            "Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3}",
            "The system cannot create the requested connector instance because the Connector's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.",
            "Verify that the Connector Provider and Connector jar files are properly configured in the process.  Then retry the request.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    /**
     * OCF-CONNECTION-500-011 - Connector Provider {0} returned a null connector instance for connection {1}
     */
    NULL_CONNECTOR(500, "OCF-CONNECTION-500-011",
            "Connector Provider {0} returned a null connector instance for connection {1}",
            "The system detected an error during connector processing and was unable to create a connector.",
            "The root cause of the error is captured in previous reported messages.",
            "https://egeria-project.org/frameworks/ocf/overview/"),

    ;


    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;
    private final String url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique 1d for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OCFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique 1d for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OCFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                              userAction,
                                              url);
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
                                                                                      userAction,
                                                                                      url);

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
                       ", url='" + url + '\'' +
                       '}';
    }
}