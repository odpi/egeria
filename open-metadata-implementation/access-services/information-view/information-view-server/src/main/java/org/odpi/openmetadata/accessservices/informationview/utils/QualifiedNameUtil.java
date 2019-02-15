/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.utils;

import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewSource;

public class QualifiedNameUtil {


    public static final String QUALIFIED_NAME_SEPARATOR = ":";


    public static String getQualifiedName(DataViewColumnSource dataViewColumn) {
        if (dataViewColumn == null) {
            return null;
        }
        DataViewSource dataViewSource = dataViewColumn.getDataViewSource();
        if (dataViewSource == null || dataViewSource.getNetworkAddress() == null || dataViewSource.getNetworkAddress().isEmpty()) {
            return replaceSpecialCharacters(dataViewSource.getId() + QUALIFIED_NAME_SEPARATOR + dataViewColumn.getId());
        } else {
            return replaceSpecialCharacters(dataViewSource.getNetworkAddress() + QUALIFIED_NAME_SEPARATOR + dataViewSource.getId() + QUALIFIED_NAME_SEPARATOR + dataViewColumn.getId());
        }
    }


    public static String getQualifiedName(DataViewSource dataViewSource) {
        if (dataViewSource == null) {
            return null;
        }

        if (dataViewSource.getNetworkAddress() == null || dataViewSource.getNetworkAddress().isEmpty()) {
            return replaceSpecialCharacters(dataViewSource.getId());
        } else {
            return replaceSpecialCharacters(dataViewSource.getNetworkAddress() + QUALIFIED_NAME_SEPARATOR + dataViewSource.getId());
        }
    }


    private static String replaceSpecialCharacters(String qualifiedName) {
        return qualifiedName.replace(".", QUALIFIED_NAME_SEPARATOR).replace("_", QUALIFIED_NAME_SEPARATOR);
    }

}
