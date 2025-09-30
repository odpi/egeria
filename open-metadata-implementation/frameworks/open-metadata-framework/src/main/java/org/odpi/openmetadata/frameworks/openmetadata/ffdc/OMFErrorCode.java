/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.frameworks.openmetadata.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The OMF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * OMF Components.  It is used in conjunction with the OMFCheckedException and OMFRuntimeException.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a user should correct the error</li>
 * </ul>
 */
public enum OMFErrorCode implements ExceptionMessageSet
{
    /**
     * OPEN-METADATA-400-001 - The {0} survey action service has been disconnected - either due to its own actions or a cancel request
     */
    DISCONNECT_DETECTED(400, "OPEN-METADATA-400-001",
                        "The {0} survey action service has been disconnected - either due to its own actions or a cancel request",
                        "The survey action framework will attempt to stop the work of the survey action framework",
                        "Monitor the shutdown of the survey action service."),


    /**
     * OPEN-METADATA-400-002 - The object passed on the {0} parameter of the {1} operation is null
     */
    NULL_OBJECT(400, "OPEN-METADATA-400-002",
                "The object passed on the {0} parameter of the {1} operation is null",
                "The system is unable to process the request without this object.",
                "Correct the code in the caller to provide the object."),


    /**
     * OPEN-METADATA-400-003 - The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}
     */
    UNEXPECTED_IO_EXCEPTION(500, "OPEN-METADATA-400-003",
                            "The listener manager received an unexpected IO exception when reading the file named {0}; the error message was: {1}",
                            "The listener manager attempted to retrieve the canonical file name and an IO exception occurred.  It is therefore unable to monitor the file.",
                            "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),
    
    /**
     * OPEN-METADATA-400-004 - The name passed on the {0} parameter of the {1} operation is null
     */
    NULL_NAME(400, "OPEN-METADATA-400-004",
              "The name passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a name.",
              "Correct the code in the caller to provide the name on the parameter."),

    /**
     * OPEN-METADATA-400-005 - The unique identifier (guid) passed on the {0} parameter of the {1} operation is null
     */
    NULL_GUID(400, "OPEN-METADATA-400-005",
              "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a guid.",
              "Correct the code in the caller to provide the guid."),

    /**
     * OPEN-METADATA-400-006 - The unique name {0} passed on the {1} parameter of the {2} operation is not known to the metadata store
     */
    UNKNOWN_ELEMENT(400, "OPEN-METADATA-400-006",
                    "The unique name {0} passed on the {1} parameter of the {2} operation is not known to the metadata store",
                    "The system is unable to process the request without being able to retrieve the element.",
                    "Correct the code in the caller to provide the guid of a real element or ensure the intended element is in the metadata store."),

    /**
     * OPEN-METADATA-400-007 - The unique name {0} passed on the {1} parameter of the {2} operation matches multiple elements: {3}
     */
    DUPLICATE_ELEMENT(400, "OPEN-METADATA-400-007",
                      "The unique name {0} passed on the {1} parameter of the {2} operation matches multiple elements: {3}",
                      "The system is unable to process the request because it is not sure which element to use.",
                      "The problem is in the open metadata repository cohort.  Multiple repositories have loaded metadata about the same element.  " +
                         "If it is not possible to delete the duplicate entries, it is necessary to enable duplicate processing to link the duplicates."),

    /**
     * OPEN-METADATA-400-008 - Null property name passed to properties object
     */
    NULL_PROPERTY_NAME(400, "OPEN-METADATA-400-008",
                       "Null property name passed to properties object",
                       "A request to set an additional property failed because the property name passed was null",
                       "Recode the call to the property object with a valid property name and retry."),

    /**
     * OPEN-METADATA-400-009 - {0} is unable to add a new element to location {1} of an array of size {2} value
     */
    ARRAY_OUT_OF_BOUNDS(400, "OPEN-METADATA-400-009",
                        "{0} is unable to add a new element to location {1} of an array of size {2} value",
                        "There is an error in the update of an ArrayTypePropertyValue.",
                        "Recode the call to the property object with a valid element location and retry."),

    /**
     * OPEN-METADATA-400-010 - The {0} method has been called without an open metadata element to work with
     */
    NO_METADATA_ELEMENT(400, "OPEN-METADATA-400-010",
                        "The {0} method has been called without an open metadata element to work with",
                        "The provisioning governance action service connector is designed to manage files on request.  " +
                                "It is unable to operate without the name of the source file and so it terminates with a FAILED completion status.",
                        "The source file is passed to the governance action service through the request parameters or via the TargetForAction " +
                                "relationship.  Correct the information passed to the governance service and rerun the request"),

    /**
     * OPEN-METADATA-400-020 - The user identifier (user id) passed on the {0} operation is null
     */
    NULL_USER_ID(400, "OPEN-METADATA-400-020",
                 "The user identifier (user id) passed on the {0} operation is null",
                 "The system is unable to process the request without a user id.",
                 "Correct the code in the caller to provide the user id."),

    /**
     * OPEN-METADATA-400-021 - The text field value passed on the {0} parameter of the {1} operation is null
     */
    NULL_TEXT(400, "OPEN-METADATA-400-021",
              "The text field value passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without this text field value.",
              "Correct the code in the caller to provide a value in the text field."),

    /**
     * OPEN-METADATA-400-022 - The search string passed on the {0} parameter of the {1} operation is null
     */
    NULL_SEARCH_STRING(400, "OPEN-METADATA-400-022",
                       "The search string passed on the {0} parameter of the {1} operation is null",
                       "The system is unable to process the request without a search string.",
                       "Correct the code in the caller to provide the search string."),


    /**
     * OPEN-METADATA-400-023 - The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative
     */
    NEGATIVE_START_FROM(400, "OPEN-METADATA-400-023",
                        "The starting point for the results {0}, passed on the {1} parameter of the {2} operation, is negative",
                        "The system is unable to process the request with this invalid value.  It should be zero for the start of the values, or a number greater than 0 to start partway down the list",
                        "Correct the code in the caller to provide a non-negative value for the starting point."),

    /**
     * OPEN-METADATA-400-024 - The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative
     */
    NEGATIVE_PAGE_SIZE(400, "OPEN-METADATA-400-024",
                       "The page size {0} for the results, passed on the {1} parameter of the {2} operation, is negative",
                       "The system is unable to process the request with this invalid value.  It should be zero to return all the result, or greater than zero to set a maximum",
                       "Correct the code in the caller to provide a non-negative value for the page size."),

    /**
     * OPEN-METADATA-400-025 - The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}
     */
    MAX_PAGE_SIZE(400, "OPEN-METADATA-400-025",
                  "The number of records to return, {0}, passed on the {1} parameter of the {2} operation, is greater than the allowable maximum of {3}",
                  "The system is unable to process the request with this page size value.",
                  "Correct the code in the caller to provide a smaller page size."),

    /**
     * OPEN-METADATA-400-026 - The {0} element is of type {1} rather than the expected type of {2}
     */
    WRONG_TYPE_FOR_ELEMENT(400, "OPEN-METADATA-400-026",
                  "The {0} element is of type {1} rather than the expected type of {2}",
                  "The system has retrieved an element that is not of the same type as expected.  The expected type is either supplied by the caller in the 'metadataElementTypeName' requests body field or the service uses its default value.",
                  "Correct the code in the caller to provide a suitable type name, or use a different service."),

    /**
     * OPEN-METADATA-400-028 - The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3}
     */
    INVALID_SEARCH_STRING(400, "OPEN-METADATA-400-028",
                          "The search string passed on the {0} parameter of the {1} operation is invalid and results in a {2} exception when executed.  The error message is {3}",
                          "The system is unable to process the request with this search string.",
                          "Correct the code in the caller to provide a valid regular expression search string."),

    /**
     * OPEN-METADATA-500-001 - Unexpected {0} exception in service {1} of type {2} detected by method {3}.  The error message was {4}
     */
    UNEXPECTED_EXCEPTION(500, "OPEN-METADATA-500-001",
                         "Unexpected {0} exception in service {1} of type {2} detected by method {3}.  The error message was {4}",
                         "The governance action service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the governance action service. " +
                                 "Once the cause is resolved, retry the governance request."),


    /**
     * OPEN-METADATA-500-002 - The Java class {0} for PrimitiveTypeCategory {1} is not known
     */
    INVALID_PRIMITIVE_CLASS_NAME(500, "OPEN-METADATA-500-002",
                                 "The Java class {0} for PrimitiveTypeCategory {1} is not known",
                                 "There is an internal error in Java class PrimitiveTypeCategory as it has been set up with an invalid class.",
                                 "Raise a Github issue to get this fixed."),

    /**
     * OPEN-METADATA-500-003 - The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2}
     */
    INVALID_PRIMITIVE_VALUE(500, "OPEN-METADATA-500-003",
                            "The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2}",
                            "There is an internal error in the creation of a PrimitiveTypeValue.",
                            "Open an issue on GitHub to get this addressed."),

    /**
     * OPEN-METADATA-500-004 - There is a problem in the definition of primitive type {0}
     */
    INVALID_PRIMITIVE_CATEGORY(500, "OPEN-METADATA-500-004",
                               "There is a problem in the definition of primitive type {0}",
                               "There is an internal error during the creation of a PrimitiveTypeValue.",
                               "Open a Github issue to get this looked into."),

    /**
     * OPEN-METADATA-500-005 - The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2}
     */
    INVALID_PRIMITIVE_TYPE(500, "OPEN-METADATA-500-005",
                           "The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2}",
                           "There is an internal error - code that sets a primitive property value is using an incorrect Java class.",
                           "Report as a Github issue to get this addressed."),

    /**
     * OPEN-METADATA-500-006 - An unsupported bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "OPEN-METADATA-500-006",
                       "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system is unable to process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up."),

    /**
     * OPEN-METADATA-500-007 - The {0} service has not implemented the {1} method in a subclass of the {2} converter class for
     * bean class {3} and so is unable to create the bean for method {4}
     */
    MISSING_CONVERTER_METHOD(500, "OPEN-METADATA-500-007",
                             "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                                     "unable to create the bean for method {4}",
                             "The system is unable to process the request because it is not able to populate the bean.",
                             "Correct the converter implementation as part of this module."),

    /**
     * OPEN-METADATA-500-008 - An unexpected bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; the expected class name is: {4}
     */
    UNEXPECTED_BEAN_CLASS(500, "OPEN-METADATA-500-008",
                          "An unexpected bean class named {0} was passed to the repository services by the {1} request for " +
                                  "open metadata access service {2} on server {3}; " +
                                  "the expected class name is: {4}",
                          "The system is unable to process the request because it is not able to support the bean's methods.",
                          "Correct the code that sets up the converter as part of this service."),

    /**
     * OPEN-METADATA-500-009 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} has not passed to method {3}
     */
    MISSING_METADATA_INSTANCE(500, "OPEN-METADATA-500-009",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} has not passed to method {3}",
                              "The system is unable to process the request because it is missing one or more metadata elements" +
                                      "needed to instantiate the bean.",
                              "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation."),

    /**
     * OPEN-METADATA-500-010 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}
     */
    BAD_INSTANCE_TYPE(500, "OPEN-METADATA-500-010",
                      "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                              "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                      "The system is unable to process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                      "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up."),

    /**
     * OPEN-METADATA-500-011 - An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_ENTITY(500, "OPEN-METADATA-500-011",
               "An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}",
               "The system is unable to format all or part of the response because the repositories have returned an invalid entity.",
               "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                       "There is probably an error in the implementation of the repository that originated the entity."),


    /**
     * OPEN-METADATA-500-013 - A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_RELATIONSHIP(500, "OPEN-METADATA-500-013",
                     "A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid relationship.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship."),

    /**
     * OPEN-METADATA-500-025 - The Design Model OMAS has received an unexpected {0} exception while formatting a response during method {1}.  The message was: {2}
     */
    UNEXPECTED_CONVERTER_EXCEPTION(500, "OPEN-METADATA-500-025",
                                   "The Open Metadata Store has received an unexpected {0} exception while formatting a response during method {1} for service {2}.  The message was: {3}",
                                   "The request returns with this exception to indicate there has been an internal server error. The server also created a detailed error message and stack trace in the audit log.",
                                   "Review the stack trace to identify where the error occurred and work to resolve the cause."),

    /**
     * OPEN-METADATA-503-001 - A caller {0} has passed an invalid parameter to the propertiesHelper {1} operation as part of the {2} request
     */
    HELPER_LOGIC_ERROR(503, "OPEN-METADATA-503-001",
                       "A caller {0} has passed an invalid parameter to the propertiesHelper {1} operation as part of the {2} request",
                       "The open metadata component has invoked the property helper operations in the wrong order or has a similar logic error.",
                       "Review the calling code to detect the source of the error."),

    /**
     * OPEN-METADATA-503-002 - A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2}
     * request resulting in an unexpected exception {3} with message {4}
     */
    HELPER_LOGIC_EXCEPTION(503, "OPEN-METADATA-503-002",
                           "A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2} request resulting in an " +
                                   "unexpected exception {3} with message {4}",
                           "The open metadata component has invoked the property helper operations in the wrong sequence or has a similar logic error.",
                           "Review the code around the original exception to detect the source of the error."),

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
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OMFErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
