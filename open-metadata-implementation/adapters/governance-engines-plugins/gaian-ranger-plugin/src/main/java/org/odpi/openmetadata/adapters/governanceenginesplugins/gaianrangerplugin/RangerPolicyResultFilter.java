/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.gaiandb.Logger;
import com.ibm.gaiandb.Util;
import com.ibm.gaiandb.policyframework.SQLResultFilterX;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.types.DataValueDescriptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.COLUMN_RESOURCE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.DEFAULT_GAIAN_DB_USER;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.DEFAULT_SCHEMA;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.SELECT_ACTION;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.UNKNOWN_USER;

/**
 * Initial policy plugin for GaianDB
 * Refer to the top level README.md in this project for further information on how to build, deploy & use
 */
public class RangerPolicyResultFilter extends SQLResultFilterX {

    private static final Logger logger = new Logger("RangerPolicyResultFilter", 25);

    private QueryContext queryContext = new QueryContext();
    private GaianAuthorizer authorizer = new RangerGaianAuthorizer();
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
    private static RangerUser getRangerUser(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return mapResultToRangerUser(result);
    }

    private static RangerUser mapResultToRangerUser(ResponseEntity<String> result) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(result.getBody(), RangerUser.class);
        } catch (IOException e) {
            logger.logException("403", e.getMessage(), e);
        }

        return null;
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> accept = new ArrayList<>(1);
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);

        String base64Credentials = getBase64CredsString();
        headers.set("Authorization", "Basic " + base64Credentials);
        return headers;
    }

    private static String getBase64CredsString() {
        byte[] plainCredentialBytes = "admin:admin".getBytes();
        byte[] base64Credential = Base64.getEncoder().encode(plainCredentialBytes);
        return new String(base64Credential);
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
        authorizer.applyRowFilterAndColumnMasking(queryContext);

        if (rows.length == 0) {
            return rows;
        }

        int firstValidRow = getFirstValidRow(rows);
        if (firstValidRow == -1) {
            return rows;
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
                        ApplyMasking.redact(row[querySetColumnIndex + resultSetColumnIndexOffset]);
                    } catch (StandardException e) {
                        logger.logException(String.valueOf(e.getErrorCode()), e.getMessage(), e);
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
            queryContext.setResourceType(COLUMN_RESOURCE);
            logger.logDetail("This is the setQueriedColumns " + queryContext.toString());

            authorizer.init();
            authorizeResult = authorizer.isAuthorized(queryContext);
        } catch (GaianAuthorizationException e) {
            logger.logException(String.valueOf("1"), e.getMessage(), e);
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

    private void setUserDetailsForQueryContext(Object arg) {
        String gaianUser = arg.toString().toLowerCase();
        queryContext.setUser(gaianUser);
        logger.logInfo("Found user for query :" + gaianUser);

        if (!gaianUser.equals(DEFAULT_GAIAN_DB_USER)) {
            Set<String> userGroups = getUserGroups(gaianUser);
            if (userGroups != null) {
                queryContext.setUserGroups(userGroups);
            }
        }

    }

    private List<String> getQueryColumns(int[] queriedColumns) {
        List<String> columns = new ArrayList<>(queriedColumns.length);

        for (int i = 0; i < queriedColumns.length; i++) {
            columns.add(queryContext.getColumns().get(queriedColumns[i] - 1));
        }

        return columns;
    }

    /**
     * Overwritten in setQueriedColumns, which provides a more precise list
     * accounting for what columns are used in select & predicate.
     *
     * @param logicalTableResultSetMetaData logical table structure
     * @return the list of the columns are used in select & predicate
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
     * @param userName
     * @return
     */
    private Set<String> getUserGroups(String userName) {
        logger.logDetail("getUserGroups for: " + userName);
        final String rangerURL = getRangerURL();

        String userDetailsUrl = getUserDetailsURL(rangerURL, userName);
        final RangerUser userDetails = getRangerUser(userDetailsUrl);
        if (userDetails != null) {
            logger.logInfo(userDetails.toString());
            return getUserGroupsByUserId(rangerURL, userDetails);
        }

        return null;
    }

    private Set<String> getUserGroupsByUserId(String rangerURL, RangerUser userDetails) {
        String userGroupsUrl = getUserGroupURL(rangerURL, userDetails.getId());
        final RangerUser userGroups = getRangerUser(userGroupsUrl);

        if (userGroups != null) {
            logger.logInfo(userGroups.toString());
            return userGroups.getGroupNameList();
        }

        return null;
    }

    private String getUserGroupURL(String rangerURL, String userId) {
        final String url = rangerURL + "/service/xusers/secure/users/{0}";
        return MessageFormat.format(url, userId);
    }

    private String getUserDetailsURL(String rangerURL, String param) {
        final String url = rangerURL + "/service/xusers/users/userName/{0}";
        return MessageFormat.format(url, param);
    }

    private String getRangerURL() {
        Properties properties = new Properties();

        try {
            InputStream resource = RangerPolicyResultFilter.class.getClassLoader().getResourceAsStream("application.properties");
            if (resource != null) {
                properties.load(resource);
                logger.logDetail(properties.toString());
                final String rangerURL = properties.getProperty("ranger.plugin.gaian.policy.rest.url");
                logger.logDetail(rangerURL);
                resource.close();
                return rangerURL;
            }
        } catch (IOException e) {
            logger.logException("403", e.getMessage(), e);
        }
        return null;
    }

}