/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QueryUtils {
    public static void encodeQueryParam(String queryParamName, String queryParamValue, StringBuffer queryStringSB) throws InvalidParameterException, UnsupportedEncodingException {
            String encodedString = URLEncoder.encode(queryParamValue, "UTF-8");
            addCharacterToQuery(queryStringSB);
            queryStringSB.append(queryParamName+"="+encodedString);
    }

    public static void addCharacterToQuery(StringBuffer queryStringSB) {
        String prependCharacter ="&";
        if (queryStringSB.length() ==0) {
            prependCharacter ="?";
        }
        queryStringSB.append(prependCharacter);
    }


}
