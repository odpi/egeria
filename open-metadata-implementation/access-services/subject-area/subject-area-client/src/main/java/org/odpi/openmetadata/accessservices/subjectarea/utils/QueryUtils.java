/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QueryUtils {
    /**
     * Convert param value to URL UTF-8 encode
     * @return encoded param value
     * */
    public static String encodeParams(String methodName, String paramName, String param) throws InvalidParameterException {
        try {
            param = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.ERROR_ENCODING_QUERY_PARAMETER.getMessageDefinition();
            messageDefinition.setMessageParameters(paramName, param);
            throw new InvalidParameterException(messageDefinition, "", methodName, paramName);
        }

        return param;
    }
}
