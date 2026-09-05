/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The OpenMetadataSecurityErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with open metadata security connectors.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA   Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>403 - forbidden</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OpenMetadataSecurityErrorCode implements ExceptionMessageSet
{
    /**
     * OPEN-METADATA-SECURITY-400-001 - The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}
     */
    BAD_PLATFORM_SECURITY_CONNECTION(400, "OPEN-METADATA-SECURITY-400-001",
                                     "The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}",
                                     "The system cannot validate the users issuing platform requests.",
                                     "Review the error message to determine the cause of the problem and correct the connection supplied for the platform security connector.  Then restart the OMAG Server Platform.",
                                     "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-400-002 - The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}
     */
    BAD_SERVER_SECURITY_CONNECTION(400, "OPEN-METADATA-SECURITY-400-002",
                                   "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
                                   "The system cannot validate the users issuing requests to this server.",
                                   "Review the error message to determine the cause of the problem and correct the connection for the server's security connector in the server's configuration document.  Then restart the server.",
                                   "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-001 - User {0} is not authorized to issue request to {1}
     */
    UNAUTHORIZED_PLATFORM_ACCESS(403, "OPEN-METADATA-SECURITY-403-001",
                                 "User {0} is not authorized to issue {1} request to {2}",
                                 "The system cannot process a request from the user because they do not have access to the requested platform" +
                                         " services.  The request fails with a UserNotAuthorizedException exception.",
                                 "Determine if this is a configuration error, a mistake or the platform is under attack.  Correct any " +
                                         "configuration error and re-run the request, if it is a valid request; otherwise contact your security " +
                                         "team.",
                                         "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-002 - User {0} is not authorized to issue a request to server {1}
     */
    UNAUTHORIZED_SERVER_ACCESS(403, "OPEN-METADATA-SECURITY-403-002",
                                "User {0} is not authorized to issue a request to server {1}",
                                "The system cannot process a request from the user because they do not have access to the requested " +
                                       "OMAG server.  The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the server.  If they should have, take steps to add " +
                                       "them to the authorized list of users.  If this user should not have access, investigate where the request " +
                                       "came from to determine if the system is under attack, or it was a mistake, or the user's tool is not " +
                                       "configured to connect to the correct server.",
                                       "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-003 - User {0} is not authorized to issue {1} requests
     */
    UNAUTHORIZED_SERVICE_ACCESS(403, "OPEN-METADATA-SECURITY-403-003",
                                "User {0} is not authorized to issue {1} requests",
                                "The system cannot process a request from the user because they do not have access to the " +
                                        "requested services. The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the requested service. If they should have, take steps to add " +
                                        "them to the authorized list of users.  If this user should not " +
                                        "have access, investigate where the request came from to determine if the system is under attack, or it was a" +
                                        " mistake.",
                                        "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-004 - User {0} is not authorized to attach feedback to element {1}
     */
    UNAUTHORIZED_ADD_FEEDBACK(403, "OPEN-METADATA-SECURITY-403-004",
                              "User {0} is not authorized to attach feedback to element {1}",
                              "The system cannot process a request from the user because they do not have access to augment the " +
                                        "requested element.  The request fails with a UserNotAuthorizedException exception.",
                              "Using information about the element and the user, determine if this result is expected, or if the configuration needs " +
                                        "to be adjusted to allow this user to perform the request.",
                                        "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-005 - User {0} is not authorized to change the zone membership for element {1} from {2} to {3}
     */
    UNAUTHORIZED_ZONE_CHANGE(403, "OPEN-METADATA-SECURITY-403-005",
                             "User {0} is not authorized to change the zone membership for element {1} from {2} to {3}",
                             "The system cannot process a request from the user because they do not have access to update " +
                                     "the requested element.  The request fails with a UserNotAuthorizedException exception.",
                             "Using information about the element, the zones and the user, determine if this result is expected, " +
                                     "or if the configuration needs to be adjusted to allow this user to perform the request.",
                                     "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-007 - User {0} is not authorized to issue operation {1} on {2} anchor element {3}
     */
    UNAUTHORIZED_ANCHOR_ACCESS(403, "OPEN-METADATA-SECURITY-403-007",
                               "User {0} is not authorized to issue operation {1} on {2} anchor element {3}",
                               "The system cannot process a request from the user because they do not have access to the " +
                                      "requested element.  The request fails with a UserNotAuthorizedException exception.",
                               "Using knowledge about the user and the element, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow access.",
                                      "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-008 - User {0} is not authorized to create an element of type {1}
     */
    UNAUTHORIZED_INSTANCE_CREATE(403, "OPEN-METADATA-SECURITY-403-008",
                              "User {0} is not authorized to create an element of type {1}",
                              "The system cannot process a request from the user because they do not have authority to create " +
                                      "an element of the requested type.  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the user and the element, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow the user to create the element.",
                                      "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-011 - User {0} is not authorized to access open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_ACCESS(403, "OPEN-METADATA-SECURITY-403-011",
                              "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                              "The system cannot process a request from the user because they do not have access to the " +
                                     "necessary services and/or resources to retrieve type information.  The request fails with a " +
                                     "UserNotAuthorizedException exception.",
                              "Determine if the user should be allowed access to the type information or not.  If they should then " +
                                     "change the configuration to give them access.",
                                     "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-012 - User {0} is not authorized to change open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_CHANGE(403, "OPEN-METADATA-SECURITY-403-012",
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The system cannot process a request from the user because they do not have access to " +
                                     "update an open metadata type.  The request fails with a UserNotAuthorizedException exception.",
                             "The ability to change types is typically limited to a restricted group of users.  Determine " +
                                     "if the user is privileged to make these changes.  If they are then update the configuration to grant them " +
                                     "access.",
                                     "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-013 - User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_ACCESS(403, "OPEN-METADATA-SECURITY-403-013",
                             "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                             "The system cannot process a request from the user because they do not have read access to the " +
                                         "requested metadata.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then" +
                                         " change the configuration to give them the required privileges.",
                                         "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-014 - User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_CHANGE(403, "OPEN-METADATA-SECURITY-403-014",
                             "User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}",
                             "The system cannot process a request from the user because they do not have access to " +
                                         "make changes to the requested metadata instance.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then " +
                                         "change the configuration to give them the required update privileges.",
                                         "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-016 - {0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3}
     */
    NO_CONNECTIONS_ALLOWED(403, "OPEN-METADATA-SECURITY-403-016",
                               "{0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3}",
                               "The system cannot process a request because the calling user does not have sufficient privileges.",
                               "No action is required if this user should not have access to the connection.  To gain access to the connection, either the security credentials of the user need changing, or a different userId is required.",
                               "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-017 - User {0} is not recognized
     */
    UNKNOWN_USER(403, "OPEN-METADATA-SECURITY-403-017",
                 "User {0} is not recognized",
                 "The security service has received a request from an unknown user.",
                 "Track down the source of the request and either add the user to the user directory or prevent the user from accessing again.",
                 "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-018 - Exception {0} occurred when retrieving user {1}; the exception message was {2}
     */
    FAILED_TO_RETRIEVE_USER(403,"OPEN-METADATA-SECURITY-403-018",
                            "Exception {0} occurred when retrieving user {1}; the exception message was {2}",
                            "An exception occurred when the security service tried to retrieve a user account.",
                            "Use the information in the exception to determine the cause of this error.  The user will not be granted access to the open metadata ecosystem.",
                            "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-020 - User {0} is not authorized to issue an operation {1} on {2} element {3}
     */
    UNAUTHORIZED_ELEMENT_ACCESS(403, "OPEN-METADATA-SECURITY-403-020",
                                "User {0} is not authorized to issue an operation {1} on {2} element {3}",
                                "The security service detected an unauthorized access to a glossary.",
                                "Review the security policies and settings to determine if this access to the element should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request.",
                                         "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-025 - Security access control {0} is not recognized
     */
    UNKNOWN_CONTROL(403, "OPEN-METADATA-SECURITY-403-025",
                    "Security access control {0} is not recognized",
                    "The security service has received a request for an unknown control.",
                    "Track down the source of the request and correct the name of the control - or add the missing control to the secrets store.",
                    "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-403-026 - Exception {0} occurred when retrieving security access control {1}; the exception message was {2}
     */
    FAILED_TO_RETRIEVE_CONTROL(403,"OPEN-METADATA-SECURITY-403-026",
                               "Exception {0} occurred when retrieving security access control {1}; the exception message was {2}",
                               "An exception occurred when the security service tried to retrieve a security access control.",
                               "Use the information in the exception to determine the cause of this error.  The control will not be returned to the calling user.",
                               "https://egeria-project.org/features/metadata-security/overview/"),

    /**
     * OPEN-METADATA-SECURITY-500-002 - Element {1} is not visible to user {0}; it has been filtered from the search results
     */
    FILTERED_ELEMENT(500, "OPEN-METADATA-SECURITY-500-002",
                     "Element {0} is not visible to user {1}; it has been filtered from the search results",
                     "The system has filtered an element from the results because the user does not have the necessary permissions to access it.",
                     "The element is filtered from the results.",
                     "https://egeria-project.org/features/metadata-security/overview/"),

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
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OpenMetadataSecurityErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this(httpErrorCode, errorMessageId, errorMessage, systemAction, userAction, null);
    }


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    OpenMetadataSecurityErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
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
