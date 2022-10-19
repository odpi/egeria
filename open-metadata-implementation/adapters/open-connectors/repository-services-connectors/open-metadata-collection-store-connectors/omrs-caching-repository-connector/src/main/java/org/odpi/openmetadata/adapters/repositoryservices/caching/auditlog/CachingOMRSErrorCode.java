/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

public enum CachingOMRSErrorCode implements ExceptionMessageSet {

    INVALID_PARAMETER_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-001 ",
            "Invalid parameter exception",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),

    REPOSITORY_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-002 ",
            "Repository error excpption",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),
    TYPE_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-003 ",
            "Type error exception",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),
    PROPERTY_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-004 ",
            "Property error exception",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),
    PAGING_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-005 ",
            "Paging error exception",
            "Connector is unable to be used",
            "Review the configuration around paging. Check the logs and debug."),
    FUNCTION_NOT_SUPPORTED_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-006 ",
            "Function not supported error exception",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),
    USER_NOT_AUTHORIZED_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-007 ",
            "user not authorized error exception",
            "Connector is unable to be used",
            "Review the configuration. Check the logs and debug."),
    HOME_ENTITY_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-008 ",
            "Reference copy requests have been issued on a home entity  error exception",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),
    ENTITY_CONFLICT_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-009 ",
            "Entity cannot be added as it conflicts with an entity that has already been stored",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),

    INVALID_ENTITY_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-010 ",
            "Entity cannot be added as it is invalid",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),

    HOME_RELATIONSHIP_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-011 ",
            "Reference copy requests have been issued on a home relationship error exception",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),
    RELATIONSHIP_CONFLICT_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-012 ",
            "Relationship cannot be added as it conflicts with an relationship that has already been stored",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),

    INVALID_RELATIONSHIP_ERROR_EXCEPTION(400, "OMRS-CACHING-REPOSITORY-400-013 ",
            "Relationship cannot be added as it is invalid",
            "Connector is unable to be used",
            "Logic error. Check the logs and debug."),
    EMBEDDED_CONNECTOR_NOT_SUPPLIED(400, "OMRS-CACHING-REPOSITORY-400-014 ",
            "The repository connector expected to have an embedded OMRS connector, but none was configured",
            "The system will shutdown the server",
            "Amend the configuration to supply an embedded OMRS connector."),

    EMBEDDED_CONNECTOR_WRONG_TYPE(400, "OMRS-CACHING-REPOSITORY-400-015 ",
            "The embedded connector supp;ied as not an OMRS connector,",
            "The system will shutdown the server",
            "Amend the configuration to supply an embedded OMRS connector rather than one that is not an OMRS Connector."),
    MULTIPLE_EMBEDDED_CONNECTORS_SUPPLIED(400, "OMRS-CACHING-REPOSITORY-400-016 ",
            "The repository connector expected to have 1 embedded OMRS connector, but multiple were configured",
            "The system will shutdown the server",
            "Amend the configuration to supply only 1 embedded OMRS connector."),

    ENTITY_NOT_KNOWN(404, "OMRS-CACHING-REPOSITORY-404-001 ",
            "On Server {0} for request {1}, the entity identified with guid {0} is not known to the open metadata repository {2}",
            "The system is unable to retrieve the properties for the requested entity because the supplied guid is not recognized.",
            "The guid is supplied by the caller to the server.  It may have a logic problem that has corrupted the guid, or the entity has been deleted since the guid was retrieved."),

    RELATIONSHIP_NOT_KNOWN(404, "OMRS-CACHING-REPOSITORY-404-002 ",
            "On Server {0} for request {1}, the relationship identified with guid {2} is not known to the open metadata repository {2}",
            "The system is unable to retrieve the properties for the requested relationship because the supplied guid is not recognized.",
            "The guid is supplied by the caller to the OMRS.  It may have a logic problem that has corrupted the guid, or the relationship has been deleted since the guid was retrieved."),
    ENTITY_PROXY_ONLY(404, "OMRS-CACHING-REPOSITORY-404-003",
            "On server {0} for request {1}, a specific entity instance for guid {2}, but this but only a proxy version of the entity is in the metadata collection",
            "The system is unable to return the entity as it is only a proxy.",
            "The guid identifier is supplied by the caller. Amend the caller to supply a guid assoicated with an Entity rather than a proxy."),


    ;

    final private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for CachingOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * CachingOMRSErrorCode above.   For example:
     * <p>
     * CachingOMRSErrorCode   errorCode = CachingOMRSErrorCode.INVALID_PARAMETER_EXCEPTION;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    CachingOMRSErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.messageDefinition = new ExceptionMessageDefinition(newHTTPErrorCode,
                newErrorMessageId,
                newErrorMessage,
                newSystemAction,
                newUserAction);
    }

    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString() {
        return "CachingOMRSErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }

}
