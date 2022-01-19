/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.RexSearchException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    /**
     *
     * @param webRequest initial request
     * @param errorCode the error code
     * @return error attributes to return to client
     */
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, UserInterfaceErrorCodes errorCode) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest,
                ErrorAttributeOptions.defaults()
                );
        errorAttributes.put("message", errorCode.getErrorMessage());
        errorAttributes.put("status", errorCode.getHttpErrorCode());
        errorAttributes.put("userAction", errorCode.getUserAction());
        errorAttributes.put("systemAction", errorCode.getSystemAction());
        errorAttributes.put("errorId", errorCode.getErrorMessageId());
        return errorAttributes;
    }


    public Map<String, Object> getRexSearchErrorAttributes(WebRequest webRequest, UserInterfaceErrorCodes errorCode, RexSearchException exception) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest,
                ErrorAttributeOptions.defaults()
        );
        errorAttributes.put("message", exception.getMessage());
        errorAttributes.put("searchCategory", exception.getSearchCategory());
        errorAttributes.put("searchText", exception.getSearchText());
        errorAttributes.put("serverName", exception.getServerName());

        errorAttributes.put("status", errorCode.getHttpErrorCode());
        errorAttributes.put("userAction", errorCode.getUserAction());
        errorAttributes.put("systemAction", errorCode.getSystemAction());
        errorAttributes.put("errorId", errorCode.getErrorMessageId());
        return errorAttributes;
    }
}
