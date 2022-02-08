/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public enum UserInterfaceErrorCodes {

    MALFORMED_INPUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-001",
            "The response received from service does not have the expected format.",
            "Check the configuration and the response from the service.",
            "Please check the request."),
    INVALID_REQUEST_FOR_ASSET_CATALOG(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-002",
            "The request for asset catalog is invalid",
            "The system is unable to handle request.",
            "Check that the configuration for Asset Catalog is correct."),
    RESOURCE_NOT_FOUND(HttpStatus.SERVICE_UNAVAILABLE, "USER-INTERFACE-503-003",
            "The resource cannot be accessed",
            "Check services are up.",
            "Please try again later."),
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "USER-INTERFACE-401-004",
            "User is not authorized",
            "The system is unable to authorize the user.",
            "Check your credentials."),
    INVALID_REQUEST_FOR_OPEN_LINEAGE(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-005",
            "The request for open lineage is invalid",
            "The system is unable to handle request.",
            "Check that the configuration for Open Lineage is correct."),
    INVALID_REQUEST_FOR_GLOSSARY_VIEW(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-006",
            "The request for glossary view is invalid",
            "The system is unable to handle request.",
            "Check that the configuration for Glossary View is correct."),

    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-001",
            "The entity is not found",
            "The system is unable to handle the request.",
            "Check that the unique identifier of the entity is correct."),

    LINEAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-002",
            "Could not retrieve lineage for the entity",
            "The system could not find lineage for the entity.",
            "Check that the entity identifier is correct"),

    GLOSSARY_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-003",
            "Could not retrieve glossary",
            "The system could not find the glossary with the provided identifier.",
            "Check that the glossary identifier is correct"),

    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-004",
            "Could not retrieve glossary term",
            "The system could not find the glossary term with the provided identifier.",
            "Check that the term identifier is correct"),

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-005",
            "Could not retrieve category",
            "The system could not find the category with the provided identifier.",
            "Check that the category identifier is correct"),

    REX_SUBGRAPH_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-INTERFACE-404-006",
            "Could not retrieve subgraph",
            "The system could not find the subgraph with the provided identifier.",
            "Check that the identifier is correct"),

    INVALID_SEARCH_REQUEST(HttpStatus.BAD_REQUEST, "USER-INTERFACE-400-001",
            "The search request is invalid.",
            "The system is unable to process the request with this search request.",
            "Check that the provided input is valid."),

    USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST(HttpStatus.BAD_REQUEST, "USER-INTERFACE-400-002",
            "Sorry - this username was not authorized to perform the request",
            "The user is not authorized to perform the request",
            "Check the user name in valid"),

    REX_INVALID_PARAMETER_REQUEST(HttpStatus.BAD_REQUEST, "USER-INTERFACE-400-003",
            "Unable to handle request",
            "The system is unable to handle the request",
            "Check that the input parameters are correct"),

    BAD_TYPE_INFORMATION(HttpStatus.BAD_REQUEST, "USER-INTERFACE-400-004",
            "There was a problem with Type information",
            "Check the type information",
            "Check the type information and retry"),

    BAD_PROPERTY_INFORMATION(HttpStatus.BAD_REQUEST, "USER-INTERFACE-400-005",
            "There was a problem with Property information",
            "Check the property information",
            "Check the Property information and retry"),



    OPERATION_NOT_SUPPORTED(HttpStatus.NOT_IMPLEMENTED, "USER-INTERFACE-501-001",
            "The UI tried to use an unsupported function",
            "The system is unable to handle the request",
            "The operation is not available at the moment");

    private HttpStatus httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(UserInterfaceErrorCodes.class);


    UserInterfaceErrorCodes(HttpStatus httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public HttpStatus getHttpErrorCode() {
        return httpErrorCode;
    }

    public String getErrorMessageId() {
        return errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }
}
