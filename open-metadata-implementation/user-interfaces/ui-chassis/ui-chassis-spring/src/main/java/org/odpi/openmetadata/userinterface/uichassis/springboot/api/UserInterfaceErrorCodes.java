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
                                 "Check that the configuration for Asset Catalog is correct." ),
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
                                    "Check that the configuration for Open Lineage is correct." ),
    INVALID_REQUEST_FOR_GLOSSARY_VIEW(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-006",
            "The request for glossary view is invalid",
            "The system is unable to handle request.",
            "Check that the configuration for Glossary View is correct." );

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
