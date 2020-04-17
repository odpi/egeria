/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.utils;

import org.springframework.util.StringUtils;

public class QualifiedNameUtils {


    public static final String SEPARATOR = "::";
    public static final String EQUALS = "=";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSE_BRACKET = ")";


    public static String buildQualifiedName(String parentQualifiedName, String typeName, String value) {
        if (!StringUtils.isEmpty(parentQualifiedName)) {
            return parentQualifiedName + SEPARATOR + OPEN_BRACKET + typeName + CLOSE_BRACKET + EQUALS + value;
        } else {
            return OPEN_BRACKET + typeName + CLOSE_BRACKET + EQUALS + value;
        }
    }


    //TODO what constitutes a digital service qualified name ?
    public static String buildQualifiedNameForInformationView(String hostAddress, String databaseName, String digitalServiceName){
        return QualifiedNameUtils.buildQualifiedName("", Constants.DIGITAL_SERVICE, digitalServiceName);
    }


    public static String buildQualifiedNameForDigitalServiceType(String hostAddress, String databaseName, String digitalServiceName){
        return QualifiedNameUtils.buildQualifiedName("", Constants.DIGITAL_SERVICE_TYPE, digitalServiceName + Constants.TYPE_SUFFIX);
    }

}
