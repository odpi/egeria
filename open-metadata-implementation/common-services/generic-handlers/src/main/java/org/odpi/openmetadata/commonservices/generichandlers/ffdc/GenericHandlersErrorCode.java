/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The GenericHandlersErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Repository Handler Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>403 - forbidden</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum GenericHandlersErrorCode implements ExceptionMessageSet
{
    INTEGRATOR_NOT_RETURNED(400, "OMAG-GENERIC-HANDLERS-400-001",
                            "Service {0} is unable to process the properties supplied to method {1} because the requested owner {2} ({3}) is " +
                                    "not a recognized software server capability.  The associated error message is: {4}",
                            "The system is unable to create a new instance in the metadata repository with an invalid integrator specified as" +
                                    " the owner.",
                            "Ensure the request includes the unique identifiers for a valid software server capability entity to represent " +
                                    "the integrator and retry the request."),

    BAD_INTEGRATOR_NAME(400, "OMAG-GENERIC-HANDLERS-400-002",
                        "Service {0} is unable to process the properties supplied to method {1} because the unique name {2} given for the " +
                                "requested owner does not match the unique name of {3} returned in software server capability {4}",
                        "The system is unable to create a new instance with an invalid integrator specified as the owner.",
                        "Retry the request with a matching the unique identifier and name for a valid software server capability entity to " +
                                "represent the owner of the new instance."),

    UNKNOWN_EXTERNAL_IDENTITY (400, "OMAG-GENERIC-HANDLERS-400-003",
                        "Service {0} is unable to locate the external identifier {1} for the {2} ({3}) scope and {4} element {5}",
                        "The system is unable to confirm the synchronization of the element's property because the identifier " +
                                "from the third party technology (scope) is not known.",
                        "Investigate if the identifier is correct for the named element and scope.  Was it created successfully? " +
                                "Has something deleted it before this request ran? " +
                                "Typically these associations are created by an integration connector running in the Catalog Integrator OMIS." +
                                "Look for errors reported in the hosting integration daemon."),

    UNKNOWN_RESOURCE_LINK(400, "OMAG-GENERIC-HANDLERS-400-004",
                          "Service {0} is unable to locate the link between the external identifier {1} for the {2} ({3}) scope and {4} element {5}",
                          "The system is unable to confirm the synchronization of the element's property because the identifier " +
                                  "from the third party technology (scope) is not linked to the element.",
                          "Investigate if the identifier is correct for the named element and scope. " +
                                  "Typically these associations are created by an integration connector running in the Catalog Integrator OMIS." +
                                  "Look for errors reported in the hosting integration daemon."),

    ONLY_CREATOR_CAN_DELETE(403, "OMAG-GENERIC-HANDLERS-403-001",
            "The {0} method is unable to delete the requested relationship between {1} {2} and {3} {4} because it " +
                                    "was not created by the requesting user {5}",
                            "The request fails because the user does not have the rights to take this action.",
                            "Retry the request with a relationship created with this user, or request that the user who created " +
                                    "the relationship issues the delete request."),

    MULTIPLE_CONNECTIONS_FOUND(404, "OMAG-GENERIC-HANDLERS-404-001",
            "{0} connections are connected to the asset with unique identifier {1}; the calling method is {2} and the server is {3}",
            "The system is unable to process a request because multiple connections have been discovered and it is unsure which connection to " +
                                       "return.",
            "use the getConnectionsForAsset to page through the list of connections to select the one that is appropriate for your use case."),

    MULTIPLE_ENTITIES_FOUND(404, "OMAG-GENERIC-HANDLERS-404-002",
            "Multiple {0} entities where found with a name of {1}: the identifiers of the returned entities are {2}; the calling method is {3}, the name parameter is {4} and the server is {5}",
            "The system is unable to process a request because multiple entities have been discovered and it is unsure which entity to use.",
            "Investigate why multiple entities exist.  Then retry the request once the issue is resolved."),

    MULTIPLE_BEANS_FOUND(404, "OMAG-GENERIC-HANDLERS-404-003",
             "Multiple {0} entities where found by method {1}: the values of the returned entities are {2}; the starting point is {3}; the calling service is {4} and the server is {5}",
             "The system is unable to process a request because multiple entities have been discovered and this is not valid.",
             "Investigate why multiple entities exist.  It may be because they have been contributed by different repositories. " +
                     "Then retry the request once the issue is resolved."),


    INVALID_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-001",
                       "An unsupported bean class named {0} was passed to the repository services by the {1} request for open metadata access service {2} on " +
                               "server {3}; error message was: {4}",
                       "The system is unable to process the request because it is not able to instantiate the bean.",
                       "Correct the code that initializes the converter during server start up."),

    MISSING_CONVERTER_METHOD(500, "OMAG-GENERIC-HANDLERS-500-002",
                       "The {0} service has not implemented the {1} method in a subclass of the {2} converter class for bean class {3} and so is " +
                               "unable to create the bean for method {4}",
                       "The system is unable to process the request because it is not able to populate the bean.",
                       "Correct the converter implementation as part of this module."),

    UNEXPECTED_BEAN_CLASS(500, "OMAG-GENERIC-HANDLERS-500-003",
                       "An unexpected bean class named {0} was passed to the repository services by the {1} request for" +
                               "open metadata access service {2} on server {3}; " +
                               "the expected class name is: {4}",
                       "The system is unable to process the request because it is not able to support the bean's methods.",
                       "Correct the code that sets up the converter as part of this service."),

    MISSING_METADATA_INSTANCE(500, "OMAG-GENERIC-HANDLERS-500-004",
                          "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                  "because a metadata instance of type {2} has not passed to method {3}",
                          "The system is unable to process the request because it is missing one or more metadata elements" +
                                      "needed to instantiate the bean.",
                          "Correct the handler code that calls the converter as part of this request since it has not passed sufficient" +
                                      " metadata instances to the converter.  Alternatively, these instances may not be in the repositories " +
                                      "(legitimately) and the converter needs to be able to handle that variation."),

    BAD_INSTANCE_TYPE(500, "OMAG-GENERIC-HANDLERS-500-005",
                              "One of the converters for the {0} service is not able to populate a bean of type {1} " +
                                      "because a metadata instance of type {2} was passed to method {3} instead of the expected type of {4}",
                              "The system is unable to process the request because the wrong type of instances have been retrieved from " +
                              "the metadata repositories.",
                              "The error is likely to be either in the handler code that called the converter, or more likely, " +
                              "in the way that the handler and the converter were initialized at server start up."),

    NULL_EXTERNAL_ID_ENTITY(500, "OMAG-GENERIC-HANDLERS-500-006",
                      "The entity for identifier {0} and {1} {2} ({3}) is null",
                      "The system is unable to process the request because the handler has failed to retrieve the entity for the " +
                              "identifier correctly.",
                      "The error is likely to be either in the handler code or the integration connector that is managing the exchange" +
                              "of metadata for ."),
    ;

    private ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for GenericHandlersErrorCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineErrorCode above.   For example:
     *
     *     GenericHandlersErrorCode   errorCode = GenericHandlersErrorCode.ASSET_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GenericHandlersErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
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
        return "GenericHandlersErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
