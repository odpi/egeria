/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OpenMetadataStoreErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Governance Action Framework (GAF) Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 * <li>Error Message Identifier - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a GovernanceEngineInterface should correct the error</li>
 * </ul>
 */
public enum OpenMetadataStoreErrorCode implements ExceptionMessageSet
{
    /**
     * OPEN-METADATA-STORE-404-002 - The open metadata repository services are not initialized for the {0} operation
     */
    OMRS_NOT_INITIALIZED(404, "OPEN-METADATA-STORE-404-002",
                         "The open metadata repository services are not initialized for the {0} operation",
                         "The system is unable to connect to the open metadata property server.",
                         "Check that the server where the Open Metadata Store Services are running initialized correctly.  " +
                                 "Correct any errors discovered and retry the request when the open metadata services are available."),

    /**
     * OPEN-METADATA-STORE-500-001 - An unsupported bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "OPEN-METADATA-STORE-500-001",
                               "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                               "The system is unable to process the request because it is not able to instantiate the bean.",
                               "Correct the code that initializes the converter during server start up."),

    /**
     * OPEN-METADATA-STORE-500-002 - The {0} service has not implemented the {1} method in a subclass of the {2} converter class for
     * bean class {3} and so is unable to create the bean for method {4}
     */
    MISSING_CONVERTER_METHOD(500, "OPEN-METADATA-STORE-500-002",
                                     "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                                     "unable to create the bean for method {4}",
                                     "The system is unable to process the request because it is not able to populate the bean.",
                                     "Correct the converter implementation as part of this module."),

    /**
     * OPEN-METADATA-STORE-500-003 - An unexpected bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; the expected class name is: {4}
     */
    UNEXPECTED_BEAN_CLASS(500, "OPEN-METADATA-STORE-500-003",
                                  "An unexpected bean class named {0} was passed to the repository services by the {1} request for " +
                                  "open metadata access service {2} on server {3}; " +
                                  "the expected class name is: {4}",
                                  "The system is unable to process the request because it is not able to support the bean's methods.",
                                  "Correct the code that sets up the converter as part of this service."),

    /**
     * OPEN-METADATA-STORE-500-004 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} has not passed to method {3}
     */
    MISSING_METADATA_INSTANCE(500, "OPEN-METADATA-STORE-500-004",
                                      "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} has not passed to method {3}",
                                      "The system is unable to process the request because it is missing one or more metadata elements" +
                                      "needed to instantiate the bean.",
                                      "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation."),

    /**
     * OPEN-METADATA-STORE-500-005 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}
     */
    BAD_INSTANCE_TYPE(500, "OPEN-METADATA-STORE-500-005",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                              "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                              "The system is unable to process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                              "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up."),


    /**
     * OPEN-METADATA-STORE-500-011 - An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_ENTITY(500, "OPEN-METADATA-STORE-500-011",
               "An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}",
               "The system is unable to format all or part of the response because the repositories have returned an invalid entity.",
               "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                       "There is probably an error in the implementation of the repository that originated the entity."),

    /**
     * OPEN-METADATA-STORE-500-012 - A relationship {0} has been retrieved by method {1} from service {2} that has an invalid entity proxy at end {3}: {4}
     */
    BAD_ENTITY_PROXY(500, "OPEN-METADATA-STORE-500-012",
                     "A relationship {0} has been retrieved by method {1} from service {2} that has an invalid entity proxy at end {3}: {4}",
                     "The system is unable to format all or part of the response because the repositories have returned a relationship with an " +
                             "invalid entity proxy that links it to an entity.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the relationship with the " +
                             "invalid entity proxy.  There is probably an error in the implementation of the repository that originated the relationship."),

    /**
     * OPEN-METADATA-STORE-500-013 - A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_RELATIONSHIP(500, "OPEN-METADATA-STORE-500-013",
                     "A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid relationship.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship."),
    
    UNEXPECTED_INITIALIZATION_EXCEPTION(503, "OPEN-METADATA-STORE-503-005",
                                        "A {0} exception was caught during start up of service {1} for server {2}. The error message was: {3}",
                                        "The system detected an unexpected error during start up and is now in an unknown state.",
                                        "The error message should indicate the cause of the error.  Otherwise look for errors in the " +
                                                "remote server's audit log and console to understand and correct the source of the error."),

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
    OpenMetadataStoreErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
