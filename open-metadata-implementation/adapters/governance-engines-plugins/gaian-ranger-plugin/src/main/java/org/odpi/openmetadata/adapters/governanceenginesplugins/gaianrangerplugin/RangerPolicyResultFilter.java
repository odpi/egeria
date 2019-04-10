/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.ibm.gaiandb.Logger;
import com.ibm.gaiandb.Util;
import com.ibm.gaiandb.policyframework.SQLResultFilterX;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.types.DataValueDescriptor;
import org.apache.ranger.authorization.hadoop.config.RangerConfiguration;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.*;

/**
 * Initial policy plugin for GaianDB
 * Refer to the top level README.md in this project for further information on how to build, deploy and use
 */
public class RangerPolicyResultFilter extends SQLResultFilterX {

    private static final Logger logger = new Logger("RangerPolicyResultFilter", 25);

    private QueryContext queryContext = new QueryContext();
    private RangerGaianAuthorizer rangerGaianAuthorizer = new RangerGaianAuthorizer();
    private boolean authorizeResult = true;

    /**
     * Policy instantiation constructor - invoked for every new query.
     * This instance will be re-used if the calling GaianTable results from a PreparedStatement
     * which is re-executed by the calling application.
     */
    public RangerPolicyResultFilter() {
        logger.logDetail("\nEntered RangerPolicyResultFilter() constructor");
    }

    /**
     * This method has no timeout and the REST call is synchronous.
     *
     * @param url the URL to Ranger Server
     * @return information about the user
     */
    private RangerUser getRangerUser(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<RangerUser> result = restTemplate.exchange(url, HttpMethod.GET, entity, RangerUser.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            return null;
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (rangerGaianAuthorizer.getRangerServerProperties() != null
                && rangerGaianAuthorizer.getRangerServerProperties().getServerAuthorization() != null) {
            String base64Credentials = rangerGaianAuthorizer.getRangerServerProperties().getServerAuthorization();
            headers.set("Authorization", base64Credentials);
        }

        return headers;
    }

    /**
     * Set up the forwarding node name
     *
     * @param nodeName forwarding node name
     * @return true allow query to continue
     */
    public boolean setForwardingNode(String nodeName) {
        logger.logDetail("Entered setForwardingNode(), forwardingNode: " + nodeName);
        return true;
    }

    /**
     * Invoked when Derby closes the GaianTable or GaianQuery instance.
     * This should be when the query's statement is closed by the application - but this is not guaranteed as Derby may cache it for re-use.
     */
    public void close() {
        logger.logDetail("Entered close()");
    }


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

    /**
     * Used to parse the GDB_CREDENTIALS string passed on every statement. Not used by the ranger plugin support
     *
     * @param credentialsStringBlock credentials as string
     * @return true allow query to continue (i.e. accept this credentialsStringBlock)
     */
    public boolean setUserCredentials(String credentialsStringBlock) {
        logger.logDetail("Entered setUserCredentials(), credentialsStringBlock: " + credentialsStringBlock);
        return true;
    }

    /**
     * @param dataSourceID          data source identifier
     * @param dataSourceDescription data source description
     * @param columnMappings        columns for be mapped
     * @return allow all records to be returned (i.e. don't impose a maximum number)
     */
    public int nextQueriedDataSource(String dataSourceID, String dataSourceDescription, int[] columnMappings) {
        logger.logDetail("Entered nextQueriedDataSource(), dataSourceID: " + dataSourceID
                + ", dataSourceDescription: " + dataSourceDescription + ", columnMappings: " + Util.intArrayAsString(columnMappings));
        return -1;
    }

    /**
     * Apply policy on a batch of rows..
     * This is helpful if you need to send the rows to a 3rd party to evaluate policy - so you can minimize the number of round trips to it.
     *
     * @param dataSourceID data source identifier
     * @param rows         rows that have to be evaluated
     * @return result after the policies are applied
     */
    public DataValueDescriptor[][] filterRowsBatch(String dataSourceID, DataValueDescriptor[][] rows) {

        logger.logDetail("Entered filterRowsBatch(), dataSourceID: " + dataSourceID + ", number of rows: " + rows.length);

        if (!authorizeResult) {
            logger.logDetail("filterRowsBatch: UnAuthorized query!");
            return new DataValueDescriptor[0][0];
        }

        // When called using a select from a view, or a VTI, the rows array will only contain the columns we expect
        // for example a select firstname,lastname from vemployee would only give us 2 columns in the rows array
        // However when using table function we get ALL columns - and they are stripped upstream.
        // ie NULL_MASK_TYPE,NULL_MASK_TYPE,Georgi,Facello,NULL_MASK_TYPE,NULL_MASK_TYPE ....
        // Thus our index for overwriting a column value with it's transformation is wrong, leading to wrong
        // results or exceptions.
        // **HACK** for now, skip over columns with NULL_MASK_TYPE data. PURELY for demo support pending a proper
        // fix
        rangerGaianAuthorizer.applyRowFilterAndColumnMasking(queryContext);

        if (rows.length == 0) {
            return rows;
        }

        int firstValidRow = getFirstValidRow(rows);
        if (firstValidRow == -1) {
            return rows;
        }

        Boolean isNullMasking = queryContext.getNullMasking();
        Properties properties = null;
        if (!isNullMasking) {
            properties = loadProperties();
        }

        int resultSetColumnIndexOffset = 0;
        int querySetColumnIndex = 0;
        while (querySetColumnIndex < queryContext.getColumns().size()
                && querySetColumnIndex + resultSetColumnIndexOffset < rows[firstValidRow].length) {
            // We ONLY look at the first row - this will fail if the data is null
            // instead should be consulting metadata (work needed to resolve)
            // BIG HACK warning....
            if (rows[firstValidRow][querySetColumnIndex + resultSetColumnIndexOffset].isNull()) {
                resultSetColumnIndexOffset++; // increment the fudge factor
                continue; // resume with the next expected column
            }

            if (!queryContext.getColumnTransformers().get(querySetColumnIndex).equals(queryContext.getColumns().get(querySetColumnIndex))) {
                // Now do the transformation for each row
                for (DataValueDescriptor[] row : rows) {
                    if (row == null) {
                        continue;
                    }
                    try {
                        ApplyMasking.redact(row[querySetColumnIndex + resultSetColumnIndexOffset], isNullMasking, properties);
                    } catch (StandardException | ParseException e) {
                        logger.logException("GAIAN_RANGER-Exeption-1", e.getMessage(), e);
                    }
                }

            }
            querySetColumnIndex++;
        }

        // END of hack. Original code follows:
        //for (int i = 0; i < queryContext.getColumns().size(); i++) {
        //	if (queryContext.getColumnTransformers().get(i) != queryContext.getColumns().get(i)) {
        //		for (int j = 0; j < rows.length; j++) {
        //			rows[j][i].setValue(queryContext.getColumnTransformers().get(i));
        //		}
        //	}

        return rows; // allow query to continue (i.e. accept this logical table)
    }

    // allow query to continue (i.e. accept this logical table)
    public boolean setLogicalTable(String logicalTableName, ResultSetMetaData logicalTableResultSetMetaData) {
        logger.logDetail("Entered setLogicalTable(), logicalTable: " + logicalTableName + ", structure: " + logicalTableResultSetMetaData);

        try {
            queryContext.setTableName(logicalTableName);
            queryContext.setActionType(SELECT_ACTION);
            queryContext.setSchema(DEFAULT_SCHEMA);

            List<String> columns = getColumnNamesForLogicalTableResultSet(logicalTableResultSetMetaData);
            queryContext.setColumns(columns);
            queryContext.setResourceType(COLUMN_RESOURCE);
            queryContext.setColumnTransformers(new ArrayList<>());

            Set<String> users = getDefaultUserGroups();
            queryContext.setNullMasking(isNullMasking());
            queryContext.setUserGroups(users);
        } catch (SQLException e) {
            logger.logException(String.valueOf(e.getErrorCode()), e.getMessage(), e);
        }
        return authorizeResult;
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
            List<String> columns = getQueryColumns(queriedColumns);
            queryContext.setColumns(columns);
            queryContext.setNullMasking(isNullMasking());
            queryContext.setResourceType(COLUMN_RESOURCE);
            logger.logDetail("This is the setQueriedColumns " + queryContext.toString());

            rangerGaianAuthorizer.init();
            authorizeResult = rangerGaianAuthorizer.isAuthorized(queryContext);
        } catch (GaianAuthorizationException e) {
            logger.logException("1", e.getMessage(), e);
        }

        // As of 9 Feb 2018 there is a bug in GaianTable that means this return is ignored.
        // We therefore check this return when processing rows in result set
        // Once fixed the return below will be respected and the backend query will be skipped to improve efficiency
        return authorizeResult; // allow query to continue (i.e. accept that all these columns be queried)
    }

    /**
     * This method provides generic extensibility of the Policy framework.
     * For any new operations required in future, a new operation ID (opID) will be assigned, for which
     * a given set of arguments will be expected, we well as a given return object.
     */
    protected Object executeOperationImpl(String opID, Object... args) {
        logger.logDetail("Entered executeOperation(), opID: " + opID + ", args: " + (null == args ? null : Arrays.asList(args)));
        boolean haveUser = false;

        if (null != opID && opID.equals(OP_ID_SET_AUTHENTICATED_DERBY_USER_RETURN_IS_QUERY_ALLOWED)) {

            if (args != null && args.length >= 1 && null != args[0]) {
                /* Ranger Gaian Plugin must be initialized at this step because
                properties should be loaded before fetching the user's groups */
                rangerGaianAuthorizer.init();
                setUserDetailsForQueryContext(args[0]);
                haveUser = true;

            }

            if (!haveUser) {
                queryContext.setUser(UNKNOWN_USER);
                logger.logImportant("Unable to retrieve user. Ensure Table functions configured and used in Query. Defaulting to setting user to <UNKNOWN>");
            }
        }
        return null;
    }

    private Boolean isNullMasking() {
        if (RangerConfiguration.getInstance() != null
                && RangerConfiguration.getInstance().getProperties() != null
                && RangerConfiguration.getInstance().getProperties().getProperty("ranger.plugin.gaian.masking.pattern") != null) {
            String maskingPattern = RangerConfiguration.getInstance().getProperties().getProperty("ranger.plugin.gaian.masking.pattern");
            if (maskingPattern != null) {
                return maskingPattern.equalsIgnoreCase("NULL");
            }
        }
        return Boolean.FALSE;
    }

    private Properties loadProperties() {
        return RangerConfiguration.getInstance().getProperties();
    }

    private void setUserDetailsForQueryContext(Object arg) {
        String gaianUser = arg.toString().toLowerCase();
        queryContext.setUser(gaianUser);
        logger.logInfo("Found user for query :" + gaianUser);

        Set<String> userGroups = getUserGroups(gaianUser);
        if (userGroups != null) {
            queryContext.setUserGroups(userGroups);
        }
    }

    private List<String> getQueryColumns(int[] queriedColumns) {
        List<String> columns = new ArrayList<>(queriedColumns.length);

        for (int queriedColumn : queriedColumns) {
            columns.add(queryContext.getColumns().get(queriedColumn - 1));
        }

        return columns;
    }

    /**
     * Overwritten in setQueriedColumns, which provides a more precise list
     * accounting for what columns are used in select and predicate.
     *
     * @param logicalTableResultSetMetaData logical table structure
     * @return the list of the columns are used in select and predicate
     * @throws SQLException provides information on a database access error or other errors
     */
    private List<String> getColumnNamesForLogicalTableResultSet(ResultSetMetaData logicalTableResultSetMetaData) throws SQLException {
        int count = logicalTableResultSetMetaData.getColumnCount();
        List<String> columns = new ArrayList<>(count);

        for (int i = 1; i <= count; i++) {
            String str = logicalTableResultSetMetaData.getColumnName(i);
            columns.add(str);
        }

        return columns;
    }

    private int getFirstValidRow(DataValueDescriptor[][] rows) {

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] != null) {
                return i;
            }
        }

        return -1;
    }

    private Set<String> getDefaultUserGroups() {
        Set<String> users = new HashSet<>(1);
        users.add("users");
        return users;
    }

    /**
     * The list of the users is retrieved from Ranger Server in a synchronous way, without timeout.
     * Take into consideration it may cause performance issues.
     *
     * @param userName name of the user
     * @return a collection of user's groups
     */
    private Set<String> getUserGroups(String userName) {
        String userDetailsURL = getRangerURL(userName, USER_DETAILS);
        if (userDetailsURL == null) {
            return Collections.emptySet();
        }

        final RangerUser userDetails = getRangerUser(userDetailsURL);
        if (userDetails != null) {
            logger.logInfo(userDetails.toString());
            return getUserGroupsByUserId(userDetails);
        }

        return Collections.emptySet();
    }

    private Set<String> getUserGroupsByUserId(RangerUser userDetails) {
        String userGroupsURL = getRangerURL(userDetails.getId(), USER_GROUPS);
        if (userGroupsURL == null) {
            return Collections.emptySet();
        }

        final RangerUser userGroups = getRangerUser(userGroupsURL);

        if (userGroups != null) {
            logger.logInfo(userGroups.toString());
            return userGroups.getGroupNameList();
        }

        return Collections.emptySet();
    }

    private String getRangerURL(String userId, String rangerSpecificURL) {
        String rangerURL = getRangerServerURL();
        if (rangerURL == null) {
            return null;
        }

        final String url = rangerURL + rangerSpecificURL;
        return MessageFormat.format(url, userId);
    }

    private String getRangerServerURL() {
        if (rangerGaianAuthorizer.getRangerServerProperties() == null ||
                rangerGaianAuthorizer.getRangerServerProperties().getServerURL() == null) {
            return null;
        }
        String rangerURL = rangerGaianAuthorizer.getRangerServerProperties().getServerURL();
        logger.logDetail("RangerURL: " + rangerURL);

        return rangerURL;
    }

}