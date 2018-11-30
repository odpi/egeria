/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.ibm.gaiandb.Logger;
import com.ibm.gaiandb.Util;
import com.ibm.gaiandb.policyframework.SQLResultFilterX;
import org.apache.derby.iapi.types.DataValueDescriptor;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

// call rangerGaianPlugin
// passin all the params from user query

/**
 * Initial policy plugin for gaianDB, as part of the VirtualDataConnector project
 * <p>
 * Refer to the toplevel README.md in this project for further information on how to build, deploy & use
 *
 * @author jonesn@uk.ibm.com
 */

public class RangerPolicyResultFilter extends SQLResultFilterX {


    //	Use PROPRIETARY notice if class contains a main() method, otherwise use COPYRIGHT notice.
    public static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corp. 2018";

    // Initialize gaianDB logging
    private static final Logger logger = new Logger("RangerPolicyResultFilter", 25);

    // Query context is used to build up the context of the query through a series of
    // calls made to this class by Gaian, so that we can then pass to the ranger policy engine
    private QueryContext queryContext = new QueryContext();

    private GaianAuthorizer authorizer = new RangerGaianAuthorizer();
    private boolean authorizeResult = true;

    /**
     * Policy instantiation constructor - invoked for every new query.
     * This instance will be re-used if the calling GaianTable results from a PreparedStatement which is re-executed by the calling application.
     */
    public RangerPolicyResultFilter() {
        logger.logDetail("\nEntered RangerPolicyResultFilter() constructor");
    }

    public boolean setLogicalTable(String logicalTableName, ResultSetMetaData logicalTableResultSetMetaData) {
        logger.logDetail("Entered setLogicalTable(), logicalTable: " + logicalTableName + ", structure: " + logicalTableResultSetMetaData);

        try {
            queryContext.setTableName(logicalTableName);
            queryContext.setActionType("SELECT");
            queryContext.setSchema("Gaian"); // Hardcoded for now

            // This gets overwritten in setQueriedColumns, which provides a more precise list
            // accounting for what columns are used in select & predicate.
            List<String> columns = new ArrayList<String>();
            int count = logicalTableResultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String str = logicalTableResultSetMetaData.getColumnName(i);
                columns.add(str);
            }
            queryContext.setColumns(columns);
            queryContext.setResourceType("COLUMN");
            queryContext.setColumnTransformers(new ArrayList<String>());

            // User would have been set below (look for OP_ID_SET_AUTHENTICATED_DERBY_USER_RETURN_IS_QUERY_ALLOWED)
            // For now we HARDCODE to group users - until LDAP integration done.
            Set<String> users = new HashSet<String>();
            users.add("users");
            queryContext.setUserGroups(users);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorizeResult; // allow query to continue (i.e. accept this logical table)
    }

    public boolean setForwardingNode(String nodeName) {
        logger.logDetail("Entered setForwardingNode(), forwardingNode: " + nodeName);
        return true; // allow query to continue (i.e. accept this forwardingNode)
    }

    // Used to parse the GDB_CREDENTIALS string passed on every statement. Not used by the ranger plugin support
    public boolean setUserCredentials(String credentialsStringBlock) {
        logger.logDetail("Entered setUserCredentials(), credentialsStringBlock: " + credentialsStringBlock);
        return true; // allow query to continue (i.e. accept this credentialsStringBlock)
    }

    public int nextQueriedDataSource(String dataSourceID, String dataSourceDescription, int[] columnMappings) {
        logger.logDetail("Entered nextQueriedDataSource(), dataSourceID: " + dataSourceID
                + ", dataSourceDescription: " + dataSourceDescription + ", columnMappings: " + Util.intArrayAsString(columnMappings));
        return -1; // allow all records to be returned (i.e. don't impose a maximum number)
    }

    // This only works correctly when either using VTI or table function syntax
    // VTI : select * from new com.ibm.db2j.GaianTable('EMPLOYEE') EMPLOYEE
    // Table function: select * from TABLE(VEMPLOYEE('VEMPLOYEE')) VEMP
    // If derby views are used the column list is ALL of those in the queried table, even ones unusued
    // ie : select * from VEMPLOYEE
    // typically leading to more strict checks - ie failing if any restricted column even exists in the table
    public boolean setQueriedColumns(int[] queriedColumns) {
        logger.logDetail("Entered setQueriedColumns(), queriedColumns: " + Util.intArrayAsString(queriedColumns));

        try {
            // update to the queried columns
            List<String> columns = new ArrayList<String>();
            for (int i = 0; i < queriedColumns.length; i++) {
                columns.add(queryContext.getColumns().get(queriedColumns[i] - 1));
            }
            queryContext.setColumns(columns);
            queryContext.setResourceType("COLUMN");
            authorizer.init();
            authorizeResult = authorizer.isAuthorized(queryContext);
        } catch (GaianAuthorizationException e) {
            e.printStackTrace();
        }

        // As of 9 Feb 2018 there is a bug in GaianTable that means this return is ignored.
        // We therefore check this return when processing rows in result set
        // Once fixed the return below will be respected and the backend query will be skipped to improve efficiency
        return authorizeResult; // allow query to continue (i.e. accept that all these columns be queried)
    }

    /**
     * Apply policy on a batch of rows..
     * This is helpful if you need to send the rows to a 3rd party to evaluate policy - so you can minimize the number of round trips to it.
     */
    public DataValueDescriptor[][] filterRowsBatch(String dataSourceID, DataValueDescriptor[][] rows) {

        logger.logDetail("Entered filterRowsBatch(), dataSourceID: " + dataSourceID + ", number of rows: " + rows.length);

        if (!authorizeResult) {
            logger.logDetail("filterRowsBatch: UnAuthorized query!");

            //return new DataValueDescriptor[rows.length][rows[0].length];
            return new DataValueDescriptor[0][0];
        } else {

            // When called using a select from a view, or a VTI, the rows array will only contain the columns we expect
            // for example a select firstname,lastname from vemployee would only give us 2 columns in the rows array
            // However when using table function we get ALL columns - and they are stripped upstream.
            // ie NULL,NULL,Georgi,Facello,NULL,NULL ....
            // Thus our index for overwriting a column value with it's transformation is wrong, leading to wrong
            // results or exceptions.
            // **HACK** for now, skip over columns with NULL data. PURELY for demo support pending a proper
            // fix
            authorizer.applyRowFilterAndColumnMasking(queryContext);

            int resultSetColumnIndexOffset = 0;
            int querySetColumnIndex = 0;
            int firstValidRow = -1;

            // If there's no rows, masking isn't needed
            if (null != rows && rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] != null) {
                        firstValidRow = i;
                        break;
                    }
                }

                // if all the rows are null return directly
                if (firstValidRow == -1) return rows;

                while (querySetColumnIndex < queryContext.getColumns().size()
                        && querySetColumnIndex + resultSetColumnIndexOffset < rows[firstValidRow].length) {
                    // We ONLY look at the first row - this will fail if the data is null
                    // instead should be consulting metadata (work needed to resolve)
                    // BIG HACK warning....
                    if (rows[firstValidRow][querySetColumnIndex + resultSetColumnIndexOffset].isNull()) {
                        resultSetColumnIndexOffset++; // increment the fudge factor
                        continue; // resume with the next expected column
                    }

                    if (queryContext.getColumnTransformers().get(querySetColumnIndex) != queryContext.getColumns().get(querySetColumnIndex)) {
                        // Now do the transformation for each row
                        for (int j = 0; j < rows.length; j++) {
                            if (rows[j] == null) continue;
                            ApplyMasking.redact(rows[j][querySetColumnIndex + resultSetColumnIndexOffset]);
                        }

                    }
                    querySetColumnIndex++;

                }

            }
                // END of hack. Original code follows:

                //for (int i = 0; i < queryContext.getColumns().size(); i++) {
                //	if (queryContext.getColumnTransformers().get(i) != queryContext.getColumns().get(i)) {
                //		for (int j = 0; j < rows.length; j++) {
                //			rows[j][i].setValue(queryContext.getColumnTransformers().get(i));
                //		}
                //	}

        }
        return rows; // allow query to continue (i.e. accept this logical table)
    }


    /**
     * This method provides generic extensibility of the Policy framework.
     * For any new operations required in future, a new operation ID (opID) will be assigned, for which
     * a given set of arguments will be expected, we well as a given return object.
     */
    protected Object executeOperationImpl(String opID, Object... args) {
        logger.logDetail("Entered executeOperation(), opID: " + opID + ", args: " + (null == args ? null : Arrays.asList(args)));

        // If we get OP_ID_SET_AUTHENTICATED_DERBY_USER_RETURN_IS_QUERY_ALLOWED it's passing us the
        // userId to be used for the security check, so save it in context
        boolean haveUser = false;

        if (null != opID && opID.equals(OP_ID_SET_AUTHENTICATED_DERBY_USER_RETURN_IS_QUERY_ALLOWED)) {
            if (args.length >= 1) {
                if (null != args[0]) { // shouldn't be >1 for this opcode...
                    // Ranger appears to be case significant. Users normally lowercase but gaian forces to upper case....
                    String gaianUser = args[0].toString().toLowerCase();
                    queryContext.setUser(gaianUser);
                    logger.logInfo("Found user for query :" + gaianUser);
                    haveUser = true;
                }
            }

            // This will happen if VTI syntax is used on the query, ie
            // select firstname from new com.ibm.db2.db2j('VEMPLOYEE') VEMPLOYEE
            // If this happens log, and make up a userId unlikely to be used, so hopefully will default to a safe
            // option. In future may fail the query entirely
            if (haveUser == false) {
                queryContext.setUser("<UNKNOWN>");
                logger.logImportant("Unable to retrieve user. Ensure Table functions configured and used in Query. Defaulting to setting user to <UNKNOWN>");
            }
        }
        return null; // Generic return of 'null' just lets the query proceed. Otherwise, the returned object should depend on the opID.
    }


    /**
     * Invoked when Derby closes the GaianTable or GaianQuery instance.
     * This should be when the query's statement is closed by the application - but this is not guaranteed as Derby may cache it for re-use.
     */
    public void close() {
        logger.logDetail("Entered close()");
    }


    /****************************************************************************************************************************************************************
     * 									DEPRECATED / UNUSED METHODS - REQUIRED ONLY FOR COMPATIBILITY WITH 'SQLResultFilter'
     ****************************************************************************************************************************************************************/

    /**
     * This method is deprecated in favour of the same method below having 3 arguments - it is here for compatibility with SQLResultFilter
     */
    public int nextQueriedDataSource(String dataSource, int[] columnMappings) {
        logger.logDetail("Entered nextQueriedDataSource() (unexpectedly), dataSource: " + dataSource + ", columnMappings: " + Util.intArrayAsString(columnMappings));
        return -1; // allow all records to be returned (i.e. don't impose a maximum number)
    }

    /**
     * This method is not currently called by Gaian.
     */
    public int setDataSourceWrapper(String wrapperID) {
        logger.logDetail("Entered setDataSourceWrapper() (unexpectedly), wrapperID: " + wrapperID);
        return -1; // allow a maximum number of records to be returned
    }

    /**
     * This method is deprecated in favour of filterRowsBatch() - it is here for compatibility with SQLResultFilter
     */
    public boolean filterRow(DataValueDescriptor[] row) {
        logger.logDetail("Entered filterRow() (unexpectedly), row: " + Arrays.asList(row));
        return true; // allow this record to be returned
    }

}
