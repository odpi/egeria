/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import java.util.Map;
import java.util.Properties;

/**
 * Small, framework-independent helper for building up the Properties object passed to
 * DriverManager.getConnection(url, properties).  Kept separate from JDBCResourceConnector so it can be unit
 * tested without needing the rest of the Egeria connector framework on the classpath.
 */
final class JDBCConnectionPropertiesUtil
{
    private JDBCConnectionPropertiesUtil()
    {
    }


    /**
     * Copy the entries of the additionalConnectionProperties configuration property (if it is a Map) into the
     * supplied Properties object, ignoring anything else and any null keys/values.  These are passed straight
     * through to the JDBC driver on every new connection - for example, Oracle's remarksReporting=true, which is
     * needed to get table/column REMARKS (comments/descriptions) via DatabaseMetaData since the Oracle driver
     * omits them by default.
     *
     * @param additionalConnectionPropertiesValue value of the additionalConnectionProperties configuration property
     * @param target properties object to add the entries to
     */
    static void addAdditionalConnectionProperties(Object     additionalConnectionPropertiesValue,
                                                   Properties target)
    {
        if (additionalConnectionPropertiesValue instanceof Map<?, ?> additionalPropertiesMap)
        {
            for (Map.Entry<?, ?> entry : additionalPropertiesMap.entrySet())
            {
                if ((entry.getKey() != null) && (entry.getValue() != null))
                {
                    target.setProperty(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        }
    }
}
