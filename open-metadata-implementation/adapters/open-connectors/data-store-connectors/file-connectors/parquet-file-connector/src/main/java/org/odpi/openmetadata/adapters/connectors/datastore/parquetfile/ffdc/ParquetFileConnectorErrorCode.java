/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The ParquetFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Parquet File Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum ParquetFileConnectorErrorCode implements ExceptionMessageSet
{
    /**
     * PARQUET-FILE-CONNECTOR-400-001 - File {0} is not an Apache Parquet file: {1}
     */
    NOT_PARQUET_FILE(400, "PARQUET-FILE-CONNECTOR-400-001",
                     "File {0} is not an Apache Parquet file: {1}",
                     "The connector is not able to describe the contents of the file because it does not have the layout of a Parquet file.",
                     "Check that the file named in the connection's endpoint is the right one, and that it is complete.  If the file is in a different format, catalog it with the type of asset, and connector, that matches the format.",
                     "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-400-002 - The footer of Apache Parquet file {0} is encrypted
     */
    ENCRYPTED_FOOTER(400, "PARQUET-FILE-CONNECTOR-400-002",
                     "The footer of Apache Parquet file {0} is encrypted",
                     "The connector is not able to describe the contents of the file because the schema, row counts and statistics are all held in the footer and the connector does not support decryption.",
                     "Use the technology that wrote the file, along with the appropriate encryption keys, to discover the contents of the file.",
                     "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-400-003 - The footer of Apache Parquet file {0} is not valid: {1}
     */
    INVALID_FOOTER(400, "PARQUET-FILE-CONNECTOR-400-003",
                   "The footer of Apache Parquet file {0} is not valid: {1}",
                   "The connector is not able to describe the contents of the file because the footer, which holds the schema, row counts and statistics, can not be decoded.",
                   "The file is either corrupt or truncated.  Try reading the file with the technology that wrote it.  If that succeeds, raise an issue for the Egeria community, including details of the technology that wrote the file.",
                   "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-400-004 - File {0} does not have a row {1}
     */
    FILE_TOO_SHORT(400, "PARQUET-FILE-CONNECTOR-400-004",
                   "File {0} does not have a row {1}",
                   "The connector cannot retrieve the requested row because the file has fewer rows than that.",
                   "Ensure the row number requested is within the size of the file.  The first row is row 0 and method getRecordCount returns the number of rows in the file.",
                   "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-400-005 - The data in Apache Parquet file {0} cannot be read because the DuckDB JDBC driver is not available
     */
    DATA_ACCESS_NOT_AVAILABLE(400, "PARQUET-FILE-CONNECTOR-400-005",
                              "The data in Apache Parquet file {0} cannot be read because the DuckDB JDBC driver is not available",
                              "The connector uses DuckDB to read the rows of data in a Parquet file.  The DuckDB JDBC driver is not on the class path and so the request fails.  The description of the file, which comes from its footer, is still available.",
                              "Add the DuckDB JDBC driver (org.duckdb:duckdb_jdbc) to the class path of the process running the connector.  It is included in the OMAG Server Platform.  Method isDataAccessAvailable can be used to test whether data can be read before requesting it.",
                              "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-400-006 - Connection {0} has been configured without the embedded Parquet File Store connection
     */
    NO_EMBEDDED_FILE_STORE(400, "PARQUET-FILE-CONNECTOR-400-006",
                           "Connection {0} has been configured without the embedded Parquet File Store connection",
                           "The tabular data set connector cannot start because it does not have the connector that reads the Parquet file.",
                           "Update the connection so that it is a virtual connection with an embedded connection for the Parquet File Store connector.  The Parquet Tabular Data Set template in the core content pack creates a connection with this structure.",
                           "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-500-001 - The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}
     */
    UNEXPECTED_IO_EXCEPTION(500, "PARQUET-FILE-CONNECTOR-500-001",
                            "The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}",
                            "The connector is not able to describe the contents of the file.",
                            "Use details from the error message to determine the cause of the error and retry the request once it is resolved.",
                            "https://egeria-project.org/concepts/digital-resource-connector/"),

    /**
     * PARQUET-FILE-CONNECTOR-500-002 - The connector was not able to read the data in Apache Parquet file {0}; the error message from DuckDB was: {1}
     */
    DATA_READ_FAILED(500, "PARQUET-FILE-CONNECTOR-500-002",
                     "The connector was not able to read the data in Apache Parquet file {0}; the error message from DuckDB was: {1}",
                     "The connector uses DuckDB to read the rows of data in a Parquet file and DuckDB reported an error.  The request fails.",
                     "Check that the request names a column that is in the file, and that the file has not been changed or removed since its footer was read.  Otherwise, use the message from DuckDB to determine the cause of the error.",
                     "https://egeria-project.org/concepts/digital-resource-connector/"),

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
    ParquetFileConnectorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
    ParquetFileConnectorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction, String url)
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
        return "ParquetFileConnectorErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       '}';
    }
}
