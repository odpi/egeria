/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OpenMetadataSecurityErrorCode is used to define first failure data capture (FFDC) for errors that occur when
 * working with open metadata security connectors.
 *
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
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum OpenMetadataSecurityErrorCode implements ExceptionMessageSet
{
    BAD_PLATFORM_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-001 ",
                                     "The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}",
                                     "The system is unable to validate the users issuing platform requests.",
                                     "Review the error message to determine the cause of the problem."),

    BAD_SERVER_SECURITY_CONNECTION(400, "OMAG-PLATFORM-SECURITY-400-002 ",
                                   "The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}",
                                   "The system is unable to validate the users issuing requests to this server.",
                                   "Review the error message to determine the cause of the problem."),

    UNAUTHORIZED_PLATFORM_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-001 ",
                                 "User {0} is not authorized to issue request to {1}",
                                 "The system is unable to process a request from the user because they do not have access to the requested platform" +
                                         " services.  The request fails with a UserNotAuthorizedException exception.",
                                 "Determine if this is a configuration error, a mistake or the platform is under attack.  Correct any " +
                                         "configuration error and re-run the request, if it is a valid request; otherwise contact your security " +
                                         "team."),

    UNAUTHORIZED_SERVER_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-002 ",
                                "User {0} is not authorized to issue a request to server {1}",
                                "The system is unable to process a request from the user because they do not have access to the requested " +
                                       "OMAG server.  The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the server.  If they should have, take steps to add " +
                                       "them to the authorized list of users.  If this user should not have access, investigate where the request " +
                                       "came from to determine if the system is under attack, or it was a mistake, or the user's tool is not " +
                                       "configured to connect to the correct server."),

    UNAUTHORIZED_SERVICE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-003 ",
                                "User {0} is not authorized to issue {1} requests",
                                "The system is unable to process a request from the user because they do not have access to the " +
                                        "requested services. The request fails with a UserNotAuthorizedException exception.",
                                "Determine whether the user should have access to the requested service. If they should have, take steps to add " +
                                        "them to the authorized list of users.  If this user should not " +
                                        "have access, investigate where the request came from to determine if the system is under attack, or it was a" +
                                        " mistake."),

    UNAUTHORIZED_ASSET_FEEDBACK(403, "OMAG-PLATFORM-SECURITY-403-004 ",
                             "User {0} is not authorized to attach feedback to asset {1}",
                             "The system is unable to process a request from the user because they do not have access to augment the " +
                                        "requested asset.  The request fails with a UserNotAuthorizedException exception.",
                             "Using information about the asset and the user, determine if this result is expected, or if the configuration needs " +
                                        "to be adjusted to allow this user to perform the request."),

    UNAUTHORIZED_ZONE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-005 ",
                             "User {0} is not authorized to change the zone membership for asset {1} from {2} to {3}",
                             "The system is unable to process a request from the user because they do not have access to update " +
                                     "the requested asset.  The request fails with a UserNotAuthorizedException exception.",
                             "Using information about the asset, the zones and the user, determine if this result is expected, " +
                                     "or if the configuration needs to be adjusted to allow this user to perform the request."),

    UNAUTHORIZED_CONNECTION_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-006 ",
                             "User {0} is not authorized to access connection {1}",
                             "The system is unable to process a request from the user because they do not have access to the" +
                                           "requested connection.  The request fails with a UserNotAuthorizedException exception.",
                             "Using knowledge about the connection and the user, determine if this result is expected, " +
                                           "or if the configuration needs to be adjusted to allow this user to perform the request."),

    UNAUTHORIZED_ASSET_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-007 ",
                             "User {0} is not authorized to access asset {1}",
                             "The system is unable to process a request from the user because they do not have access to the " +
                                      "requested asset.  The request fails with a UserNotAuthorizedException exception.",
                             "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow access."),

    UNAUTHORIZED_ASSET_CREATE(403, "OMAG-PLATFORM-SECURITY-403-008 ",
                              "User {0} is not authorized to create an asset of type {1}",
                              "The system is unable to process a request from the user because they do not have authority to create " +
                                      "an asset of the requested type.  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow the user to create the asset."),

    UNAUTHORIZED_ASSET_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-009 ",
                              "User {0} is not authorized to change asset {1}",
                              "The system is unable to process a request from the user because they do not have access to change the " +
                                      "properties of the requested asset.  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the user and the asset, determine if this is the correct result or " +
                                      "the configuration needs to be changed to allow the user to update the asset."),

    INCOMPLETE_ASSET(         403, "OMAG-PLATFORM-SECURITY-403-010 ",
                              "User {0} is not authorized to change asset {1} because it has missing properties: {2}",
                              "The system is unable to process a request from the user because the asset is not correctly or completely filled out." +
                                      "  The request fails with a UserNotAuthorizedException exception.",
                              "Using knowledge about the asset determine why the properties are missing and whether it needs to be " +
                                      "updated, or that the user should not be accessing the asset."),

    UNAUTHORIZED_TYPE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-011 ",
                              "User {0} is not authorized to access open metadata type {1} ({2}) on server {3}",
                              "The system is unable to process a request from the user because they do not have access to the " +
                                     "necessary services and/or resources to retrieve type information.  The request fails with a " +
                                     "UserNotAuthorizedException exception.",
                              "Determine if the user should be allowed access to the type information or not.  If they should then " +
                                     "change the configuration to give them access."),

    UNAUTHORIZED_TYPE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-012 ",
                             "User {0} is not authorized to change open metadata type {1} ({2}) on server {3}",
                             "The system is unable to process a request from the user because they do not have access to " +
                                     "update an open metadata type.  The request fails with a UserNotAuthorizedException exception.",
                             "The ability to change types is typically limited to a restricted group of users.  Determine " +
                                     "if the user is privileged to make these changes.  If they are then update the configuration to grant them " +
                                     "access."),

    UNAUTHORIZED_INSTANCE_ACCESS(403, "OMAG-PLATFORM-SECURITY-403-013 ",
                             "User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have read access to the " +
                                         "requested metadata.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then" +
                                         " change the configuration to give them the required privileges."),

    UNAUTHORIZED_INSTANCE_CHANGE(403, "OMAG-PLATFORM-SECURITY-403-014 ",
                             "User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}",
                             "The system is unable to process a request from the user because they do not have access to " +
                                         "make changes to the requested metadata instance.  The request fails with a UserNotAuthorizedException exception.",
                             "Determine if the user should have access to this metadata instance and if they should then " +
                                         "change the configuration to give them the required update privileges."),

    ;

    private ExceptionMessageDefinition messageDefinition;



    /**
     * The constructor for OpenMetadataSecurityErrorCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataSecurityErrorCode above.   For example:
     *
     *     OpenMetadataSecurityErrorCode   errorCode = OpenMetadataSecurityErrorCode.BAD_PLATFORM_SECURITY_CONNECTION;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    OpenMetadataSecurityErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "OpenMetadataSecurityErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }
}
