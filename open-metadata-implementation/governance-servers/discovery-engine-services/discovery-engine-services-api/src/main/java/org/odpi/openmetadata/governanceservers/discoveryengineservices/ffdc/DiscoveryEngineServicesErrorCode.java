/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.ffdc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ODF error code is used to define first failure data capture (FFDC) for errors that occur when working with
 * ODF Discovery Services.  It is used in conjunction with all ODF Exceptions, both Checked and Runtime (unchecked).
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code for translating between REST and JAVA - Typically the numbers used are:</li>
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
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DiscoveryServerErrorCode
{
    NO_CONFIG_DOC(400,"OMAS-DISCOVERY-SERVER-400-001 ",
                  "Discovery server {0} does not have a configuration document",
                  "The server is not able to retrieve its configuration.  It fails to start.",
                  "Add the discovery configuration to the discovery server's configuration document."),

    NO_OMAS_SERVER_URL(400,"OMAS-DISCOVERY-SERVER-400-002 ",
                       "Discovery server {0} is not configured with the platform URL root for the Discovery Engine OMAS",
                       "The server is not able to retrieve its configuration.  It fails to start.",
                       "Add the configuration for the platform URL root to this discovery server's configuration document."),

    NO_OMAS_SERVER_NAME(400, "OMAS-DISCOVERY-SERVER-400-003 ",
                        "Discovery server {0} is not configured with the name for the server running the Discovery Engine OMAS",
                        "The server is not able to retrieve its configuration.  It fails to start.",
                        "Add the configuration for the server name to this discovery server's configuration document."),

    NO_DISCOVERY_ENGINES(400,"OMAS-DISCOVERY-SERVER-400-004 ",
                         "Discovery server {0} is configured with no discovery engines",
                         "The server is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this discovery server."),

    UNKNOWN_DISCOVERY_ENGINE(400, "DISCOVERY-SERVER-400-005 ",
            "Discovery engine {0} is not running in the discovery server {1}",
            "The discovery engine requested on a request is not known to the discovery server.",
            "This may be a configuration or a code error.  Look for other error messages and review the code of the discovery server.  Once the cause is resolved, retry the discovery request."),

    UNKNOWN_DISCOVERY_ENGINE_NAME(400, "DISCOVERY-SERVER-400-006 ",
                             "Properties for discovery engine called {0} are not returned by open metadata server {1}.  Exception {2} with message {3} returned to discovery server {4}",
                             "The discovery server is not able to initialize the discovery engine and so it will not de able to support discovery requests targeted to this discovery engine.",
                             "This may be a configuration error or the metadata server may be down.  Look for other error messages and review the " +
                                          "configuration of the discovery server.  Once the cause is resolved, restart the discovery server."),

    SERVICE_INSTANCE_FAILURE(400, "DISCOVERY-SERVER-400-007 ",
                             "The discovery engine services are unable to initialize a new instance of discovery server {0}; error message is {1}",
                             "The discovery engine services detected an error during the start up of a specific discovery server instance.  Its discovery services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    NO_DISCOVERY_ENGINES_STARTED(400,"OMAS-DISCOVERY-SERVER-400-008 ",
                         "Discovery server {0} is unable to start any discovery engines",
                         "The server is not able to run any discovery requests.  It fails to start.",
                         "Add the configuration for at least one discovery engine to this discovery server.");


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryServerErrorCode.class);


    /**
     * The constructor for ODFErrorCode expects to be passed one of the enumeration rows defined in
     * ODFErrorCode above.   For example:
     *
     *     ODFErrorCode   errorCode = ODFErrorCode.UNKNOWN_ENDPOINT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique Id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    DiscoveryServerErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params   strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("ODFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }
}
