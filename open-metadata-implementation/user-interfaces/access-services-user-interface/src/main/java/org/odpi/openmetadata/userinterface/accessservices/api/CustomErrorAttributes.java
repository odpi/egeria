/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {


    public Map<String, Object> getErrorAttributes(WebRequest webRequest, String errorMessage) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, false);
        errorAttributes.put("message", errorMessage);
        return errorAttributes;
    }
}
