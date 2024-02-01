/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The DocStoreErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the encrypted file based doc store.  It is used in conjunction with all Exceptions, both Checked and Runtime (unchecked).
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
public enum DocStoreErrorCode implements ExceptionMessageSet
{
    /**
     * ENCRYPTED-DOC-STORE-400-001 - Unable to initialize encryption library configuration; exception was {0} with message {1}
     */
    INIT_ERROR(400, "ENCRYPTED-DOC-STORE-400-001",
            "Unable to initialize encryption library configuration; exception was {0} with message {1}",
            "The system is unable to create the requested configuration document store because the encryption libraries are not available.",
            "Ensure the libraries are on the class path. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-002 - Unable to write encrypted configuration file; exception was {0} with message {1}
     */
    WRITE_ERROR(400, "ENCRYPTED-DOC-STORE-400-002",
            "Unable to write encrypted configuration file; exception was {0} with message {1}",
            "The system is unable to write the requested configuration document due to an encryption library or I/O problem.",
            "Check the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-003 - Unable to read encrypted configuration file; exception was {0} with message {1}
     */
    READ_ERROR(400, "ENCRYPTED-DOC-STORE-400-003",
            "Unable to read encrypted configuration file; exception was {0} with message {1}",
            "The system is unable to read the requested configuration document due to an encryption library or I/O problem.",
            "Check the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-004 - Unable to parse the key store; exception was {0} with message {1}
     */
    INVALID_KEYSTORE(400, "ENCRYPTED-DOC-STORE-400-004",
            "Unable to parse the key store; exception was {0} with message {1}",
            "The system is unable to parse the provided key store.",
            "Check the full stack trace in the logs, and ensure the provided key store follows Google Tink's structure. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-005 - Found a file with the expected prefix 'keystore_', when this must be a directory: {1}
     */
    KEYSTORE_NOT_DIRECTORY(400, "ENCRYPTED-DOC-STORE-400-005",
            "Found a file with the expected prefix 'keystore_', when this must be a directory: {1}",
            "The system is unable to handle encrypted configuration files using a file-based key store when this file is present.",
            "Check the full stack trace in the logs, and ensure the provided key store follows Google Tink's structure. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-006 - Found multiple key files under secure directory, unable to determine which to use for decryption
     */
    MULTIPLE_FILES(400, "ENCRYPTED-DOC-STORE-400-006",
            "Found multiple key files under secure directory, unable to determine which to use for decryption",
            "The system requires exactly one file with a '.key' extension in the secure key store directory.",
            "Check your generated, secure key store directory (prefixed with 'keystore_') to ensure there is only one file present with a '.key' extension."),

    /**
     * ENCRYPTED-DOC-STORE-400-007 - Found multiple key store directories, unable to determine which to use for decryption
     */
    MULTIPLE_DIRECTORIES(400, "ENCRYPTED-DOC-STORE-400-007",
            "Found multiple key store directories, unable to determine which to use for decryption",
            "The system requires exactly one directory with a 'keystore_' prefix.",
            "Check the path where the OMAG platform is started to ensure there is only one directory that starts with 'keystore_' present."),

    /**
     * ENCRYPTED-DOC-STORE-400-008 - Unable to create secure location for storing encryption key
     */
    INVALID_DIRECTORY(400, "ENCRYPTED-DOC-STORE-400-008",
            "Unable to create secure location for storing encryption key",
            "The system was unable to generate a secure, random directory in which to store the encryption keys.",
            "Check the path where the OMAG Server Platform is started to ensure there are sufficient permissions to create files and directories. Review the logs for other potential I/O issues."),

    /**
     * ENCRYPTED-DOC-STORE-400-009 - Unable to create secure file for storing encryption key
     */
    INVALID_FILE(400, "ENCRYPTED-DOC-STORE-400-009",
            "Unable to create secure file for storing encryption key",
            "The system was unable to generate a secure, random file in which to store the encryption keys.",
            "Check the path where the OMAG platform is started to ensure there are sufficient permissions to create files and directories. Review the logs for other potential I/O issues."),

    /**
     * ENCRYPTED-DOC-STORE-400-010 - Unable to find any file containing keys in the secure directory, and unable to remove the directory; exception was {0} with message {1}
     */
    KEYSTORE_EMPTY(400, "ENCRYPTED-DOC-STORE-400-010",
            "Unable to find any file containing keys in the secure directory, and unable to remove the directory; exception was {0} with message {1}",
            "The system was unable to find any file with a '.key' extension inside the secure directory with the 'keystore_' prefix.",
            "The system was unable to automatically remove this empty keystore directory to generate a new one. Check permissions on the server or manually remove the directory and retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-011 - Unable to find any keys defined for decrypting the configuration
     */
    NO_KEYSTORE(400, "ENCRYPTED-DOC-STORE-400-011",
            "Unable to find any keys defined for decrypting the configuration",
            "The system was unable to find keys defined in either the EGERIA_CONFIG_KEYS environment variable or a generated secure directory with the 'keystore_' prefix.",
            "The system cannot parse the existing configuration file. Either provide the key to decrypt the configuration file, or remove the configuration file and new keys will be automatically generated when creating a new configuration."),

    /**
     * ENCRYPTED-DOC-STORE-400-012 - Unable to delete the secure key storage location; exception was {0} with message {1}
     */
    KEYSTORE_DELETE_ERROR(400, "ENCRYPTED-DOC-STORE-400-012",
            "Unable to delete the secure key storage location; exception was {0} with message {1}",
            "The system was unable to delete the generated secure key storage location prefixed with 'keystore_'.",
            "Review the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-013 - Unable to delete the encrypted configuration file {0}; exception was {1} with message {2}
     */
    CONFIG_DELETE_ERROR(400, "ENCRYPTED-DOC-STORE-400-013",
            "Unable to delete the encrypted configuration file {0}; exception was {1} with message {2}",
            "The system was unable to delete the encrypted configuration file.",
            "Review the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-014 - Unable to retrieve the encrypted configuration files; exception was {0} with message {1}, while attempting access file {2}
     */
    CONFIG_RETRIEVE_ALL_ERROR(400, "ENCRYPTED-DOC-STORE-400-014",
                        "Unable to retrieve the encrypted configuration files; exception was {0} with message {1}, while attempting access file {2}",
                        "The system was unable to retrieve the encrypted configuration files.",
                        "Review the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-400-015 - Unable to retrieve the encrypted configuration files because the store template name {0} is not valid
     */
    CONFIG_RETRIEVE_ALL_ERROR_INVALID_TEMPLATE(400, "ENCRYPTED-DOC-STORE-400-015",
                              "Unable to retrieve the encrypted configuration files because the store template name {0} is not valid",
                              "The system was unable to retrieve the encrypted configuration files as the template was invalid.",
                              "Either use the default store template or specify a valid template. One with 1 or 2 inserts and with the 2 inserts not both representing folders."),

    /**
     * ENCRYPTED-DOC-STORE-500-001 - Unable to handle the error {0} with message {1}
     */
    UNHANDLED_ERROR(500, "ENCRYPTED-DOC-STORE-500-001",
            "Unable to handle the error {0} with message {1}",
            "The system was unable to handle the error indicated.",
            "Check the full stack trace in the logs to troubleshoot further. Then retry the request."),

    /**
     * ENCRYPTED-DOC-STORE-500-002 - Unable to retrieve or generate the AEAD primitive
     */
    AEAD_UNAVAILABLE(500, "ENCRYPTED-DOC-STORE-500-002",
            "Unable to retrieve or generate the AEAD primitive",
            "The system was unable to retrieve or generate the Authenticated encryption with associated data (AEAD) primitive needed for decryption.",
            "Check the logs for any issues, enable debugging if necessary, and raise an issue on the Egeria GitHub."),
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
    DocStoreErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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