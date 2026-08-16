/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.db2luw.ffdc.DB2LUWAuditCode;
import org.odpi.openmetadata.adapters.connectors.db2luw.utilities.DB2LUWUtils;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DB2LUWServerSurveyActionService surveys the databases hosted by a Db2 for Linux, UNIX and Windows server.
 * <br><br>
 * Unlike PostgreSQL (whose pg_database catalog view lists every database on the server from a single connection)
 * and Oracle (whose CDB root can see every pluggable database through v$pdbs), Db2 for Linux, UNIX and Windows has
 * no catalog view that spans multiple databases - each database maintains its own independent SYSCAT schema, and
 * the list of databases known to an instance lives only in the server's local database directory, which is not
 * queryable over JDBC.  Consequently, the set of databases to survey cannot be auto-discovered: it is either just
 * the single database the asset's connection already names, or - if the caller has supplied one - the
 * includeDatabaseList configuration property naming every database that should be surveyed.  excludeDatabaseList
 * still applies as a filter on top of whichever list is used.
 */
public class DB2LUWServerSurveyActionService extends SurveyActionServiceConnector
{
    private final List<JDBCResourceConnector> jdbcResourceConnectors = new ArrayList<>();

    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException a problem within the discovery service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            AnnotationStore  annotationStore = surveyContext.getAnnotationStore();
            SurveyAssetStore assetStore      = surveyContext.getAssetStore();

            /*
             * The asset should have a special connector for databases.  If the connector is wrong the cast will fail.
             */
            connector = super.performCheckAssetAnalysisStep(JDBCResourceConnector.class, OpenMetadataType.SOFTWARE_SERVER.typeName);
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;
            assetConnector.start();

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            /*
             * There is no cross-database catalog view to query, so the candidate database list comes from
             * configuration: includeDatabaseList if supplied, otherwise just the single database this connection
             * already names.
             */
            List<String> includedDatabases = super.getArrayConfigurationProperty(DB2LUWConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                                  connectionBean.getConfigurationProperties());

            List<String> validDatabases = new ArrayList<>();

            if ((includedDatabases != null) && (! includedDatabases.isEmpty()) && (! includedDatabases.contains("*")))
            {
                validDatabases.addAll(includedDatabases);
            }
            else if (assetConnector.getDatabaseName() != null)
            {
                validDatabases.add(assetConnector.getDatabaseName());
            }

            if (validDatabases.isEmpty())
            {
                auditLog.logMessage(methodName, DB2LUWAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                                    assetStore.getQualifiedName(),
                                                                                                    assetStore.getAssetGUID()));
            }
            else
            {
                List<String> excludedDatabases = super.getArrayConfigurationProperty(DB2LUWConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                                      connectionBean.getConfigurationProperties(),
                                                                                      Collections.emptyList());

                List<String> surveyDatabases = new ArrayList<>();

                for (String databaseName : validDatabases)
                {
                    if (surveyContext.elementShouldBeSurveyed(databaseName, excludedDatabases, null))
                    {
                        surveyDatabases.add(databaseName);
                    }
                }

                DB2LUWDatabaseStatsExtractor statsExtractor = new DB2LUWDatabaseStatsExtractor(surveyDatabases,
                                                                                                this);

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                /*
                 * Unlike Oracle's CDB root (which can gather every PDB's size in one query before looping for
                 * per-PDB schema detail), each Db2 database's own statistics are only visible from a connection
                 * to that specific database - so both steps happen together, once per database connection.
                 */
                for (String databaseName : surveyDatabases)
                {
                    java.sql.Connection databaseSpecificConnection = this.getDatabaseConnection(assetConnector, databaseName);

                    if (databaseSpecificConnection != null)
                    {
                        statsExtractor.getDatabaseStatistics(databaseName, databaseSpecificConnection);
                        statsExtractor.getSchemaStatistics(databaseName, databaseSpecificConnection);
                    }
                }

                List<AnnotationProperties> annotations = statsExtractor.getAnnotations();
                if (annotations != null)
                {
                    for (AnnotationProperties annotation : annotations)
                    {
                        if (super.isActive())
                        {
                            annotationStore.addAnnotation(annotation, surveyContext.getAssetGUID());
                        }
                    }
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.handleUnexpectedException(methodName, error);
        }
    }


    /**
     * Return a JDBC connection that connects to a specific database.  This code is needed because the sibling
     * databases are not catalogued at this time so we are creating connections to each database based on its
     * name and the databaseURL used to connect to the entry database.
     *
     * @param serverConnector connector for the entry database's connection
     * @param databaseName name of the target database
     * @return jdbc connection
     */
    private java.sql.Connection getDatabaseConnection(JDBCResourceConnector serverConnector,
                                                      String                databaseName)
    {
        final String methodName = "getDatabaseConnection";

        try
        {
            String serverNetworkAddress = serverConnector.getConnection().getEndpoint().getNetworkAddress();
            String databaseSpecificURL  = DB2LUWUtils.getDatabaseURL(serverNetworkAddress, databaseName);

            org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection databaseConnectionDetails =
                    new org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection(serverConnector.getConnection());

            /*
             * The Connection copy constructor only takes a shallow copy of the endpoint, so mutating
             * databaseConnectionDetails.getEndpoint() in place would also corrupt the endpoint shared with
             * serverConnector's own connection - and every database after the first would then be built from an
             * already-substituted URL instead of the original entry-database URL.  Taking an explicit copy of the
             * endpoint avoids that.
             */
            org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint databaseEndpoint =
                    new org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint(databaseConnectionDetails.getEndpoint());

            databaseEndpoint.setNetworkAddress(databaseSpecificURL);
            databaseConnectionDetails.setEndpoint(databaseEndpoint);

            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            JDBCResourceConnector newConnector = (JDBCResourceConnector) connectorBroker.getConnector(databaseConnectionDetails);

            /*
             * Track the connector before it is started so that a connector whose start() fails is still released
             * by disconnect().
             */
            jdbcResourceConnectors.add(newConnector);

            newConnector.start();

            return newConnector.getDataSource().getConnection();
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               DB2LUWAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                        error.getClass().getName(),
                                                                                        methodName,
                                                                                        error.getMessage()),
                               error);
        }

        return null;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public synchronized void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        for (JDBCResourceConnector resourceConnector: jdbcResourceConnectors)
        {
            if ((resourceConnector != null) && (resourceConnector.isActive()))
            {
                resourceConnector.disconnect();
            }
        }
    }
}
