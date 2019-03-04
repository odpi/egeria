/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.utils;

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


    public static String buildQualifiedNameForInformationView(String hostAddress, String databaseName, String schemaName){
            String endpointQualifiedName = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER, hostAddress);
            String databaseQualifiedName = QualifiedNameUtils.buildQualifiedName(endpointQualifiedName, Constants.DATA_STORE, databaseName);
            return QualifiedNameUtils.buildQualifiedName(databaseQualifiedName, Constants.INFORMATION_VIEW, schemaName);
    }


    public static String buildQualifiedNameForRelationalDbSchemaType(String hostAddress, String databaseName, String schemaName){
            String endpointQualifiedName = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER, hostAddress);
            String databaseQualifiedName = QualifiedNameUtils.buildQualifiedName(endpointQualifiedName, Constants.DATA_STORE, databaseName);
            return QualifiedNameUtils.buildQualifiedName(databaseQualifiedName, Constants.RELATIONAL_DB_SCHEMA_TYPE, schemaName + Constants.TYPE_SUFFIX);
    }


    public static String buildQualifiedNameForDataViewColumn(String hostAddress, String informationViewId, String columnName){
        String endpointQualifiedName = "";
        if(!StringUtils.isEmpty(hostAddress)){
            endpointQualifiedName = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER, hostAddress);
        }
        String informationViewQualifiedName = QualifiedNameUtils.buildQualifiedName(endpointQualifiedName, Constants.INFORMATION_VIEW, informationViewId);
        return QualifiedNameUtils.buildQualifiedName(informationViewQualifiedName, Constants.DERIVED_SCHEMA_ATTRIBUTE, columnName);

    }



}
