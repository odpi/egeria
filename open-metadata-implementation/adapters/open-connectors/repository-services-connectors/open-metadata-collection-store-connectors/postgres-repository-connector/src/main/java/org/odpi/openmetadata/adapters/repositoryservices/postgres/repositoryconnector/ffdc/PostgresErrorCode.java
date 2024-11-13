/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

/**
 * The PostgresErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * XTDB as an OMRS Metadata Repository.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <br><br>
 * The 5 fields in the enum are:
 * <ul>
 *   <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *   <li><ul>
 *     <li>500 - internal error</li>
 *     <li>400 - invalid parameters</li>
 *     <li>404 - not found</li>
 *   </ul></li>
 *   <li>Error Message Id - to uniquely identify the message</li>
 *   <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *   <li>SystemAction - describes the result of the error</li>
 *   <li>UserAction - describes how a AssetConsumerInterface should correct the error</li>
 * </ul>
 */
public enum PostgresErrorCode implements ExceptionMessageSet
{
    /**
     * POSTGRES-REPOSITORY-CONNECTOR-400-001 - The {0} postgreSQL repository connector is running in read-only mode; updates are not allowed
     */
    READ_ONLY_MODE(400, "POSTGRES-REPOSITORY-CONNECTOR-400-001",
                                    "The {0} postgreSQL repository connector is running in read-only mode; updates are not allowed",
                                    "The connector is not able to to perform any changes to the repository.",
                                    "Read-only mode is enabled through the repositoryMode configuration property for this repository.  If read-only mode is set in error then change the repository's configuration properties and restart the server."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-400-002 - The {0} postgreSQL repository connector has detected an incompatible search property with operator {1}: {2}
     */
    BAD_SEARCH_PROPERTY(400, "POSTGRES-REPOSITORY-CONNECTOR-400-002",
                   "The {0} postgreSQL repository connector has detected an incompatible search property with operator {1}: {2}",
                   "The search method is unable to match the supplied property with the supplied operator.",
                   "Correct the values supplied on the search so that single values are supplied with single value operators such as 'Equal' and multiple values are supplied on multi-value operators such as 'In'."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-400-003 - The search string {0} is taking over 500 ms to run against a simple string
     */
    BAD_REGEX(400,"POSTGRES-REPOSITORY-CONNECTOR-400-003",
              "The search string {0} is taking over 500 ms to run against a simple string",
              "An invalid parameter exception is returned to the caller since the server does not accept the request.",
              "Simplify the search expression and retry the request."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-001 - The {0} postgreSQL connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION(500, "POSTGRES-REPOSITORY-CONNECTOR-500-001",
                         "The {0} postgreSQL repository connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The connector is unable to process the current request.",
                         "Use the details from the error message to determine the cause of the error and retry the request once it is resolved."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-002 - The  postgreSQL connector is missing {0} value during method {1} in mapper {2}
     */
    MISSING_MAPPING_VALUE(500, "POSTGRES-REPOSITORY-CONNECTOR-500-002",
                         "The postgreSQL repository connector is missing {0} value during method {1} in mapper {2}",
                         "The connector is unable to process the current request because of an internal sequencing error.",
                         "Use a trace to determine why one of the mapper was called in the wrong sequence."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-003 - The {0} postgreSQL repository connector detected a missing value for column {1} during method {2} in mapper {3}; row values are: {4}
     */
    MISSING_REPOSITORY_VALUE(500, "POSTGRES-REPOSITORY-CONNECTOR-500-003",
                             "The {0} postgreSQL repository connector detected a missing value for column {1} during method {2} in mapper {3}; row values are: {4}",
                             "The connector is unable to process the current request because of a missing value in the database.",
                             "Investigate the contents of the database and the SQL requests used to populate it."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-004 - The {0} postgreSQL repository connector detected an invalid value for column {1} during method {2} in mapper {3}; row values are: {4}
     */
    INVALID_REPOSITORY_VALUE(500, "POSTGRES-REPOSITORY-CONNECTOR-500-004",
                             "The {0} postgreSQL repository connector detected an invalid value for column {1} during method {2} in mapper {3}; row values are: {4}",
                             "The connector is unable to process the current request because of an incorrect value in the database.",
                             "This is a logic error since only valid values should make it into the database.  Investigate the contents of the database and the SQL requests used to populate it."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-005 - The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for server {2}
     */
    CONTROL_SERVER_MISMATCH(500, "POSTGRES-REPOSITORY-CONNECTOR-500-005",
                             "The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for server {2}",
                             "The connector will not use a repository designated to another server.  The server is shutdown.",
                             "This is a configuration error since each repository should only be used by one server.  If you want to have multiple server instances using this repository then each should run on a different OMAG Server Platform and have the same server name."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-006 - The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for server {2}
     */
    CONTROL_MC_ID_MISMATCH(500, "POSTGRES-REPOSITORY-CONNECTOR-500-006",
                            "The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because this repository is for metadata collection id {2} rather than the configured value of {3}",
                            "The connector will not use a repository designated to another metadata collection.  The server is shutdown.",
                            "This is a configuration error since each repository is assigned a unique metadata collection id when it is first configured.  This value is broadcast across the cohort and so it should not change.  Use the administration services to change the repository's metadata collection id back to its original value."),

    /**
     * POSTGRES-REPOSITORY-CONNECTOR-500-007 - The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because it does not support schema version {2}
     */
    CONTROL_SCHEMA_VERSION_MISMATCH(500, "POSTGRES-REPOSITORY-CONNECTOR-500-007",
                            "The {0} postgreSQL repository connector is not able to use the contents of database schema {1} because it does not support schema version {2}",
                            "The connector is not able to understand the structure of the schema.  The server is shutdown.",
                            "This is caused by using an older version of Egeria than the one used to create the repository.  Upgrade your Egeria installation to the latest level."),
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
    PostgresErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
