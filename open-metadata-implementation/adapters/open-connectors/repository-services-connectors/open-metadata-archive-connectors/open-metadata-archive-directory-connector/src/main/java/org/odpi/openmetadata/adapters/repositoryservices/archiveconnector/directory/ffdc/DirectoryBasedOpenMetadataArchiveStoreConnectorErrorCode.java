/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur within the
 * DirectoryBasedOpenMetadataArchiveStoreConnector.
 * It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).
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
public enum DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode implements ExceptionMessageSet
{
    DUPLICATE_TYPE_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-001",
            "The same type {0} of category {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}.",
            "The build of the archive terminates.",
            "Verify the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request."),
    DUPLICATE_INSTANCE_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-002",
            "The {0} instance {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}.",
            "The build of the archive terminates immediately.",
            "Verify the definition of the instance being added to the archive. Once the definitions have been corrected, rerun the request."),
    DUPLICATE_TYPENAME_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-003",
            "The same type name {0} has been added twice to an open metadata archive. First version was {1} and the second was {2}.",
            "The build of the archive ends.",
            "Check the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request."),
    DUPLICATE_GUID_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-004",
            "The guid {0} has been used twice to an open metadata archive. First version was {1} and the second was {2}.",
            "The build of the archive will terminate.",
            "Verify the definition of the elements being added to the archive. Once the definitions have been corrected, rerun the request."),
    MISSING_TYPE_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-005",
            "The type {0} of category {1} is not found in an open metadata archive.",
            "The build of the archive now ends.",
            "Verify the definition of all the elements being added to the archive. Once the definitions have been corrected, rerun the request."),
    MISSING_NAME_FOR_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-006",
            "A request for a type from category {0} passed a null name.",
            "The build of the archive stops.",
            "Verify the definition of the elements being added to the archive. Once the definitions are corrected, rerun the request."),
    DUPLICATE_ENDDEF1_NAME_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-007",
            "RelationshipEndDef1 type {0} and EndDef1 name {1} in RelationshipDef {2} are incorrect, because another entity or " +
                    "relationship endDef is already using this attribute name",
            "The build of the archive exits.",
            "Verify the definition of the types being added to the archive. Once the definitions have been corrected, repeat the request."),
    DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-008",
            "RelationshipEndDef2 type {0} and EndDef2 name {1} in RelationshipDef {2} are incorrect, because another entity or " +
                    "relationship endDef is already using this attribute name",
            "The archive build terminates.",
            "Verify the definition of the types being added to the archive. Once the definitions have been fixed, repeat the request."),
    DUPLICATE_RELATIONSHIP_ATTR_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-009",
            "Duplicate attribute name {0} is defined in RelationshipDef {1}.",
            "The archive build stops.",
            "Verify the definition of the types being added to the archive. Once the definitions have been fixed, rerun the request."),
    DUPLICATE_ENTITY_ATTR_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-010",
            "Duplicate attribute name {0} is defined in EntityDef {1}.",
            "The archive build will terminate.",
            "Verify the definition of the types being added to the archive. Once the definitions are corrected, rerun the request."),
    DUPLICATE_CLASSIFICATION_ATTR_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-011",
            "Duplicate attribute name {0} is defined in ClassificationDef {1}.",
            "The archive build will exit.",
            "Check the definition of the types being added to the archive. Once the definitions have been fixed, retry the request."),
    BLANK_TYPENAME_IN_ARCHIVE(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-012",
            "Type name {0} is invalid because it contains a blank character.",
            "The archive build has ended.",
            "Verify the definition of the types being added to the archive. Once the definitions are fixed, rerun the request."),
    UNEXPECTED_EXCEPTION(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-013",
            "An unexpected {0} exception was caught by {1}; error message was {2}",
            "The system is not able to take action on the request.",
            "Review the error message and other diagnostics created at the same time."),
    UNKNOWN_GUID(400, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-400-014",
            "Method {0} is unable to locate an instance with guid {1} in the archive",
            "The system is unable to process the incoming request.",
            "Check the error message and other diagnostics created at the same time."),
    ARCHIVE_UNAVAILABLE(500, "OCF-DIRECTORY-OPEN-METADATA-ARCHIVE-STORE-CONNECTOR-500-001",
            "The archive builder failed to initialize.",
            "There is an internal error in the archive building process.",
            "Raise a Github issue this can be investigated."),
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
    DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
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
