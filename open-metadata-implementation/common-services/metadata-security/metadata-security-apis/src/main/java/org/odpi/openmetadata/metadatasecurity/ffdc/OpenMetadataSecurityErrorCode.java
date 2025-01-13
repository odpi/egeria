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
     * OMAG-PLATFORM-SECURITY-400-001 - The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}
     */
    BAD_PLATFORM_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-001",
                                     "The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}",
                                     "The system is unable to validate the users issuing platform requests.",
                                     "Review the error message to determine the cause of the problem."),

    /**
     * OMAG-PLATFORM-SECURITY-400-002 - The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}
     */
    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-002",
                                   "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
                                   "The system is unable to validate the users issuing requests to this server.",
                                   "Review the error message to determine the cause of the problem."),

    /**
     * OMAG-PLATFORM-SECURITY-403-001 - User {0} is not authorized to issue request to {1}
     */
    UNAUTHORIZED_PLATFORM_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-001",
                                 "User {0} is not authorized to issue {1} request to {2}",
                                 "The system is unable to process a request from the user because they do not have access to the requested platform" +
                                         " services.  The request fails with a UserNotAuthorizedException exception.",
                                 "Determine if this is a configuration error, a mistake or the platform is under attack.  Correct any " +
                                         "configuration error and re-run the request, if it is a valid request; otherwise contact your security " +
                                         "team."),

    /**
     * OMAG-SERVER-SECURITY-403-002 - User {0} is not authorized to issue a request to server {1}
     */
    UNAUTHORIZED_SERVER_ACCESS(403, "OMAG-SERVER-SECURITY-403-002",
                                "User {0} is not authorized to issue a request to server {1}",
                                "The system is unable to process a request from the user because they do not have access to the requested " +
                                       "OMAG server.  The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the server.  If they should have, take steps to add " +
                                       "them to the authorized list of users.  If this user should not have access, investigate where the request " +
                                       "came from to determine if the system is under attack, or it was a mistake, or the user's tool is not " +
                                       "configured to connect to the correct server."),

    /**
     * OMAG-SERVER-SECURITY-403-003 - User {0} is not authorized to issue {1} requests
     */
    UNAUTHORIZED_SERVICE_ACCESS(403, "OMAG-SERVER-SECURITY-403-003",
                                "User {0} is not authorized to issue {1} requests",
                                "The system is unable to process a request from the user because they do not have access to the " +
                                        "requested services. The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the requested service. If they should have, take steps to add " +
                                        "them to the authorized list of users.  If this user should not " +
                                        "have access, investigate where the request came from to determine if the system is under attack, or it was a" +
                                        " mistake."),

    /**
     * OMAG-SERVER-SECURITY-403-004 - User {0} is not authorized to attach feedback to asset {1}
     */
    UNAUTHORIZED_ASSET_FEEDBACK(403, "OMAG-SERVER-SECURITY-403-004",
                             "User {0} is not authorized to attach feedback to asset {1}",
                             "The system is unable to process a request from the user because they do not have access to augment the " +
                                        "requested asset.  The request fails with a UserNotAuthorizedException exception.",
                             "Using information about the asset and the user, determine if this result is expected, or if the configuration needs " +
                                        "to be adjusted to allow this user to perform the request."),

    /**
     * OMAG-SERVER-SECURITY-403-005 - User {0} is not authorized to change the zone membership for asset {1} from {2} to {3}
     */
    UNAUTHORIZED_ZONE_CHANGE(403, "OMAG-SERVER-SECURITY-403-005",
                             "User {0} is not authorized to change the zone membership for asset {1} from {2} to {3}",
                             "The system is unable to process a request from the user because they do not have access to update " +
                                     "the requested asset.  The request fails with a UserNotAuthorizedException exception.",
                             "Using information about the asset, the zones and the user, determine if this result is expected, " +
                                     "or if the configuration needs to be adjusted to allow this user to perform the request."),


    /**
     * OMAG-SERVER-SECURITY-403-006 - User {0} is not authorized to access connection {1}
     */
    UNAUTHORIZED_CONNECTION_ACCESS(403, "OMAG-SERVER-SECURITY-403-006",
                             "User {0} is not authorized to access connection {1}",
                             "The system is unable to process a request from the user because they do not have access to the" +
                                           "requested connection.  The request fails with a UserNotAuthorizedException exception.",
                             "Using knowledge about the connection and the user, determine if this result is expected, " +
                                           "or if the configuration needs to be adjusted to allow this user to perform the request."),

    /**
     * OMAG-SERVER-SECURITY-403-007 - User {0} is not authorized to issue operation {1} on {2} anchor element {3}
     */
    UNAUTHORIZED_ANCHOR_ACCESS(403, "OMAG-SERVER-SECURITY-403-007",
                               "User {0} is not authorized to issue operation {1} on {2} anchor element {3}",
                               "The system is unable to process a request from the user because they do not have access to the " +
                                      "requested asset.  The request fails with a UserNotAuthorizedException exception.",
                               "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow access."),

    /**
     * OMAG-SERVER-SECURITY-403-008 - User {0} is not authorized to create an asset of type {1}
     */
    UNAUTHORIZED_ASSET_CREATE(403, "OMAG-SERVER-SECURITY-403-008",
                              "User {0} is not authorized to create an asset of type {1}",
                              "The system is unable to process a request from the user because they do not have authority to create " +
                                      "an asset of the requested type.  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow the user to create the asset."),

    /**
     * OMAG-SERVER-SECURITY-403-009 - User {0} is not authorized to change asset {1}
     */
    UNAUTHORIZED_ASSET_CHANGE(403, "OMAG-SERVER-SECURITY-403-009",
                              "User {0} is not authorized to change asset {1}",
                              "The system is unable to process a request from the user because they do not have access to change the " +
                                      "properties of the requested asset.  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow the user to update the asset."),

    /**
     * OMAG-SERVER-SECURITY-403-010 - User {0} is not authorized to change asset {1} because it has missing properties: {2}
     */
    INCOMPLETE_ASSET(         403, "OMAG-SERVER-SECURITY-403-010",
                              "User {0} is not authorized to change asset {1} because it has missing properties: {2}",
                              "The system is unable to process a request from the user because the asset is not correctly or completely filled out." +
                                      "  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the asset determine why the properties are missing and whether it needs to be " +
                                      "updated, or that the user should not be accessing the asset."),

    /**
     * OMAG-SERVER-SECURITY-403-011 - User {0} is not authorized to access open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_ACCESS(403, "OMAG-SERVER-SECURITY-403-011",
                              "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                              "The system is unable to process a request from the user because they do not have access to the " +
                                     "necessary services and/or resources to retrieve type information.  The request fails with a " +
                                     "UserNotAuthorizedException exception.",
                              "Determine if the user should be allowed access to the type information or not.  If they should then " +
                                     "change the configuration to give them access."),

    /**
     * OMAG-SERVER-SECURITY-403-012 - User {0} is not authorized to change open metadata type {1} ({2}) on server {3}
     */
    UNAUTHORIZED_TYPE_CHANGE(403, "OMAG-SERVER-SECURITY-403-012",
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The system is unable to process a request from the user because they do not have access to " +
                                     "update an open metadata type.  The request fails with a UserNotAuthorizedException exception.",
                             "The ability to change types is typically limited to a restricted group of users.  Determine " +
                                     "if the user is privileged to make these changes.  If they are then update the configuration to grant them " +
                                     "access."),

    /**
     * OMAG-SERVER-SECURITY-403-013 - User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_ACCESS(403, "OMAG-SERVER-SECURITY-403-013",
                             "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have read access to the " +
                                         "requested metadata.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then" +
                                         " change the configuration to give them the required privileges."),

    /**
     * OMAG-SERVER-SECURITY-403-014 - User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}
     */
    UNAUTHORIZED_INSTANCE_CHANGE(403, "OMAG-SERVER-SECURITY-403-014",
                             "User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have access to " +
                                         "make changes to the requested metadata instance.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then " +
                                         "change the configuration to give them the required update privileges."),

    /**
     * OMAG-SERVER-SECURITY-403-015 - {0} connections are connected to the asset with unique identifier {1} but there is no security connector to select a connection for user {2}; the calling method is {3}
     */
    MULTIPLE_CONNECTIONS_FOUND(403, "OMAG-SERVER-SECURITY-403-015",
                               "{0} connections are connected to the asset with unique identifier {1} but there is no security connector to select a connection for user {2}; the calling method is {3}",
                               "The system is unable to process a request because multiple connections have been discovered and it is unsure which connection to return.",
                               "Either add a server security connection or use a method such as getConnectionsForAsset() to page through the list of connections to select the one that is appropriate for their use case."),

    /**
     * OMAG-SERVER-SECURITY-403-016 - {0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3}
     */
    NO_CONNECTIONS_ALLOWED(403, "OMAG-SERVER-SECURITY-403-016",
                               "{0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3}",
                               "The system is unable to process a request because the calling user does not have sufficient privileges.",
                               "No action is required if this user should not have access to the connection.  To gain access to the connection, either the security credentials of the user need changing, or a different userId is required."),

    /**
     * OPEN-METADATA-SECURITY-403-017 - User {0} is not recognized
     */
    UNKNOWN_USER(403, "OPEN-METADATA-SECURITY-403-017",
                 "User {0} is not recognized",
                 "The security service has received a request from an unknown user.",
                 "Track down the source of the request and either add the user to the user directory or prevent the user from accessing again."),


    /**
     * OPEN-METADATA-SECURITY-403-018 - Exception {0} occurred when retrieving user {1}; message was {2}
     */
    FAILED_TO_RETRIEVE_USER(403,"OPEN-METADATA-SECURITY-403-018",
                            "Exception {0} occurred when retrieving user {1}; message was {2}",
                            "An exception occurred when the security service tried to retrieve a user account.",
                            "Use the information in the exception to determine the cause of this error.  The user will not be granted access to the open metadata ecosystem."),


    /**
     * OMAG-SERVER-SECURITY-403-020 - User {0} is not authorized to issue an operation {1} on glossary {2}
     */
    UNAUTHORIZED_ELEMENT_ACCESS(403, "OMAG-SERVER-SECURITY-403-020",
                                "User {0} is not authorized to issue an operation {1} on {2} element {3}",
                                "The security service detected an unauthorized access to a glossary.",
                                "Review the security policies and settings to determine if this access to a glossary should be allowed or not." +
                                         "  Take action to either change the security sessions or determine the reason for the unauthorized request."),

    /**
     * OMAG-PLATFORM-SECURITY-500-001 - {0} connections are connected to the asset with unique identifier {1} but the connector selecting the connection for user {2} has returned an unrecognized connection; the calling method is {3}
     */
    UNKNOWN_CONNECTION_RETURNED(500, "OMAG-PLATFORM-SECURITY-500-001",
                           "{0} connections are connected to the asset with unique identifier {1} but the connector selecting the connection for user {2} has returned an unrecognized connection; the calling method is {3}",
                           "The system is unable to process a request because the security connector is behaving strangely.",
                           "Investigate and correct the behaviour of the server security connector."),

    /**
     * OMAG-SERVER-SECURITY-500-002 - User {0} is not authorized to issue operation {1} because the anchor is null
     */
    NULL_ANCHOR(500, "OMAG-SERVER-SECURITY-500-002",
                "User {0} is not authorized to issue operation {1} on an element because the anchor is null",
                "The system is unable to process a request from the user because the element is not correctly anchored.",
                "The request fails with a UserNotAuthorizedException exception. Add the anchor relationship of the glossary element to its glossary and corresponding Anchors classification.  When both are in place, re-run the request."),

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
    OpenMetadataSecurityErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
