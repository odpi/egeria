/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;



    /**
     * The RexErrorCode is used to define first failure data capture (FFDC) for errors that occur within REX.
     * It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).
     *
     * The 5 fields in the enum are:
     * <ul>
     *     <li>HTTP Error Code for translating between REST and JAVA. Typically the numbers used are:</li>
     *     <li><ul>
     *         <li>500: internal error</li>
     *         <li>501: not implemented </li>
     *         <li>503: Service not available</li>
     *         <li>400: invalid parameters</li>
     *         <li>401: unauthorized</li>
     *         <li>404: not found</li>
     *         <li>405: method not allowed</li>
     *         <li>409: data conflict errors, for example an item is already defined</li>
     *     </ul></li>
     *     <li>Error Message Id: to uniquely identify the message</li>
     *     <li>Error Message Text: includes placeholder to allow additional values to be captured</li>
     *     <li>SystemAction: describes the result of the error</li>
     *     <li>UserAction: describes how a user should correct the error</li>
     * </ul>
     */

    public enum RexErrorCode implements ExceptionMessageSet
    {
        NO_GUID(400, "REX-400-001",
                     "A null unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
                     "The system is unable to perform the request because the unique identifier is needed.",
                     "Correct the caller's code and retry the request."),

        INVALID_VALUE(400, "REX-400-002",
                     "An invalid value has been passed as the {0} parameter on a {1} request to open metadata repository {2}",
                     "The system is unable to perform the request using the value provided.",
                     "Correct the caller's code and retry the request."),

        ENTITY_NOT_KNOWN(400, "REX-400-003",
                      "The entity identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}",
                      "\"The system is unable to retrieve the properties for the requested entity because the supplied guid is not recognized.",
                      "The guid is supplied by the caller to the server.  It may have a logic problem that has corrupted the guid, or the entity has been deleted since the guid was retrieved."),

        RELATIONSHIP_NOT_KNOWN(400, "REX-400-004",
                         "The relationship identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}",
                         "\"The system is unable to retrieve the properties for the requested relationship because the supplied guid is not recognized.",
                         "The guid is supplied by the caller to the server.  It may have a logic problem that has corrupted the guid, or the relationship has been deleted since the guid was retrieved."),

        TROUBLE_AT_MILL(400, "REX-400-005",
                        "Apparently, one on't cross beams gone ow't askew on't treddle on repository {2}",
                        "The system is unable to perform the request because none expects the Spanish Inquisition!",
                        "Correct the caller's code and retry the request."),

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
        RexErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
