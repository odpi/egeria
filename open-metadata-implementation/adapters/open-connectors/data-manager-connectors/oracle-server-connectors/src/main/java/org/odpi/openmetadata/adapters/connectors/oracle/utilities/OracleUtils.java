/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.utilities;


/**
 * OracleUtils provides simple functions to work with Oracle Database names and connection strings.
 */
public class OracleUtils
{
    /**
     * Convert the connection string for the CDB root into a connection string for one of its pluggable databases
     * (PDBs).  Oracle JDBC URLs use the EZCONNECT syntax, identifying the target service through the final path
     * segment (eg "jdbc:oracle:thin:@//host:port/serviceName"), so the existing service name on the CDB root URL
     * is replaced with the PDB's service name rather than appended as a connection property.
     *
     * @param cdbRootURL connection string used to connect to the Oracle Database Server's CDB root
     * @param pdbServiceName service name of the pluggable database
     * @return connection string
     */
    public static String getDatabaseURL(String cdbRootURL,
                                        String pdbServiceName)
    {
        int lastSlashIndex = cdbRootURL.lastIndexOf('/');

        String baseURL = cdbRootURL;

        if (lastSlashIndex != -1)
        {
            baseURL = cdbRootURL.substring(0, lastSlashIndex);
        }

        return baseURL + "/" + pdbServiceName;
    }
}
