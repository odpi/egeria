/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.frameworks.openmetadata.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The GAF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * GAF Components.  It is used in conjunction with the GAFCheckedException and GAFRuntimeException.
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
     * GAF-PROPERTIES-400-004 - The name passed on the {0} parameter of the {1} operation is null
     */
    NULL_NAME(400, "GAF-PROPERTIES-400-004",
              "The name passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a name.",
              "Correct the code in the caller to provide the name on the parameter."),

    /**
     * GAF-PROPERTIES-400-005 - The unique identifier (guid) passed on the {0} parameter of the {1} operation is null
     */
    NULL_GUID(400, "GAF-PROPERTIES-400-005",
              "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
              "The system is unable to process the request without a guid.",
              "Correct the code in the caller to provide the guid."),

    /**
     * GAF-PROPERTIES-400-006 - The unique name {0} passed on the {1} parameter of the {2} operation is not known to the metadata store
     */
    UNKNOWN_ELEMENT(400, "GAF-PROPERTIES-400-006",
                    "The unique name {0} passed on the {1} parameter of the {2} operation is not known to the metadata store",
                    "The system is unable to process the request without being able to retrieve the element.",
                    "Correct the code in the caller to provide the guid of a real element or ensure the intended element is in the metadata store."),

    /**
     * GAF-PROPERTIES-400-007 - The unique name {0} passed on the {1} parameter of the {2} operation matches multiple elements: {3}
     */
    DUPLICATE_ELEMENT(400, "GAF-PROPERTIES-400-007",
                      "The unique name {0} passed on the {1} parameter of the {2} operation matches multiple elements: {3}",
                      "The system is unable to process the request because it is not sure which element to use.",
                      "The problem is in the open metadata repository cohort.  Multiple repositories have loaded metadata about the same element.  " +
                         "If it is not possible to delete the duplicate entries, it is necessary to enable duplicate processing to link the duplicates."),

    /**
     * GAF-PROPERTIES-400-008 - Null property name passed to properties object
     */
    NULL_PROPERTY_NAME(400, "GAF-PROPERTIES-400-008",
                       "Null property name passed to properties object",
                       "A request to set an additional property failed because the property name passed was null",
                       "Recode the call to the property object with a valid property name and retry."),

    /**
     * GAF-PROPERTIES-400-009 - {0} is unable to add a new element to location {1} of an array of size {2} value
     */
    ARRAY_OUT_OF_BOUNDS(400, "GAF-PROPERTIES-400-009",
                        "{0} is unable to add a new element to location {1} of an array of size {2} value",
                        "There is an error in the update of an ArrayTypePropertyValue.",
                        "Recode the call to the property object with a valid element location and retry."),

    /**
     * GAF-PROPERTIES-400-010 - The {0} method has been called without an open metadata element to work with
     */
    NO_METADATA_ELEMENT(400, "GAF-PROPERTIES-400-010",
                        "The {0} method has been called without an open metadata element to work with",
                        "The provisioning governance action service connector is designed to manage files on request.  " +
                                "It is unable to operate without the name of the source file and so it terminates with a FAILED completion status.",
                        "The source file is passed to the governance action service through the request parameters or via the TargetForAction " +
                                "relationship.  Correct the information passed to the governance service and rerun the request"),

    /**
     * GAF-GOVERNANCE-SERVICE-500-001 - Unexpected {0} exception in governance action service {1} of type {2} detected by method {3}.  The error message was {4}
     */
    UNEXPECTED_EXCEPTION(500, "GAF-GOVERNANCE-SERVICE-500-001",
                         "Unexpected {0} exception in governance action service {1} of type {2} detected by method {3}.  The error message was {4}",
                         "The governance action service failed during its operation.",
                         "This may be a configuration or a code error.  Look for other error messages and review the code of the governance action service. " +
                                 "Once the cause is resolved, retry the governance request."),


    /**
     * GAF-PROPERTIES-500-001 - The Java class {0} for PrimitiveTypeCategory {1} is not known
     */
    INVALID_PRIMITIVE_CLASS_NAME(500, "GAF-PROPERTIES-500-001",
                                 "The Java class {0} for PrimitiveTypeCategory {1} is not known",
                                 "There is an internal error in Java class PrimitiveTypeCategory as it has been set up with an invalid class.",
                                 "Raise a Github issue to get this fixed."),

    /**
     * GAF-PROPERTIES-500-002 - The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2}
     */
    INVALID_PRIMITIVE_VALUE(500, "GAF-PROPERTIES-500-002",
                            "The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveTypeCategory {2}",
                            "There is an internal error in the creation of a PrimitiveTypeValue.",
                            "Open an issue on GitHub to get this addressed."),

    /**
     * GAF-PROPERTIES-500-003 - There is a problem in the definition of primitive type {0}
     */
    INVALID_PRIMITIVE_CATEGORY(500, "GAF-PROPERTIES-500-003",
                               "There is a problem in the definition of primitive type {0}",
                               "There is an internal error during the creation of a PrimitiveTypeValue.",
                               "Open a Github issue to get this looked into."),

    /**
     * GAF-PROPERTIES-500-004 - The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2}
     */
    INVALID_PRIMITIVE_TYPE(500, "GAF-PROPERTIES-500-004",
                           "The value supplied for an attribute of PrimitiveTypeCategory {0} is expected as Java class {1} but was supplied as Java class {2}",
                           "There is an internal error - code that sets a primitive property value is using an incorrect Java class.",
                           "Report as a Github issue to get this addressed."),

    /**
     * GAF-PROPERTIES-HELPER-503-001 - A caller {0} has passed an invalid parameter to the propertiesHelper {1} operation as part of the {2} request
     */
    HELPER_LOGIC_ERROR(503, "GAF-PROPERTIES-HELPER-503-001",
                       "A caller {0} has passed an invalid parameter to the propertiesHelper {1} operation as part of the {2} request",
                       "The open metadata component has invoked the property helper operations in the wrong order or has a similar logic error.",
                       "Review the calling code to detect the source of the error."),

    /**
     * GAF-PROPERTIES-HELPER-503-002 - A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2}
     * request resulting in an unexpected exception {3} with message {4}
     */
    HELPER_LOGIC_EXCEPTION(503, "GAF-PROPERTIES-HELPER-503-002",
                           "A caller {0} has passed an invalid parameter to the property helper {1} operation as part of the {2} request resulting in an " +
                                   "unexpected exception {3} with message {4}",
                           "The open metadata component has invoked the property helper operations in the wrong sequence or has a similar logic error.",
                           "Review the code around the original exception to detect the source of the error."),

    /**
     * GAF-CONVERTER-500-001 - An unsupported bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; error message was: {4}
     */
    INVALID_BEAN_CLASS(500, "GAF-CONVERTER-500-001",
                       "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system is unable to process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up."),

    /**
     * GAF-CONVERTER-500-002 - The {0} service has not implemented the {1} method in a subclass of the {2} converter class for
     * bean class {3} and so is unable to create the bean for method {4}
     */
    MISSING_CONVERTER_METHOD(500, "GAF-CONVERTER-500-002",
                             "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                                     "unable to create the bean for method {4}",
                             "The system is unable to process the request because it is not able to populate the bean.",
                             "Correct the converter implementation as part of this module."),

    /**
     * GAF-CONVERTER-500-003 - An unexpected bean class named {0} was passed to the repository services by the {1} request for
     * open metadata access service {2} on server {3}; the expected class name is: {4}
     */
    UNEXPECTED_BEAN_CLASS(500, "GAF-CONVERTER-500-003",
                          "An unexpected bean class named {0} was passed to the repository services by the {1} request for " +
                                  "open metadata access service {2} on server {3}; " +
                                  "the expected class name is: {4}",
                          "The system is unable to process the request because it is not able to support the bean's methods.",
                          "Correct the code that sets up the converter as part of this service."),

    /**
     * GAF-CONVERTER-500-004 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} has not passed to method {3}
     */
    MISSING_METADATA_INSTANCE(500, "GAF-CONVERTER-500-004",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} has not passed to method {3}",
                              "The system is unable to process the request because it is missing one or more metadata elements" +
                                      "needed to instantiate the bean.",
                              "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation."),

    /**
     * GAF-CONVERTER-500-005 - One of the converters for the {0} service is not able to populate a bean of type {1}
     * because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}
     */
    BAD_INSTANCE_TYPE(500, "GAF-CONVERTER-500-005",
                      "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                              "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                      "The system is unable to process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                      "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up."),


    /**
     * GAF-CONVERTER-500-011 - An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_ENTITY(500, "GAF-CONVERTER-500-011",
               "An entity has been retrieved by method {0} from service {1} that has an invalid header: {2}",
               "The system is unable to format all or part of the response because the repositories have returned an invalid entity.",
               "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                       "There is probably an error in the implementation of the repository that originated the entity."),


    /**
     * GAF-CONVERTER-500-013 - A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}
     */
    BAD_RELATIONSHIP(500, "GAF-CONVERTER-500-013",
                     "A relationship has been retrieved by method {0} from service {1} that has an invalid header: {2}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid relationship.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship."),
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
