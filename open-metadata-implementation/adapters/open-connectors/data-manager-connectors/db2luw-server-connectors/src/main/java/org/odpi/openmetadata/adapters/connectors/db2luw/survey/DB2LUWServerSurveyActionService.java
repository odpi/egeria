/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.db2luw.ffdc.DB2LUWAuditCode;
import org.odpi.openmetadata.adapters.connectors.db2luw.utilities.DB2LUWUtils;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
 * <br><br>
 * Db2 has no cheap way to tell, before connecting, whether the survey's userId holds the CONNECT privilege on a
 * database (that is recorded in each database's own SYSCAT.DBAUTH), so an inaccessible database is detected
 * when the connection to it fails, and is reported with the DATABASE_NOT_SURVEYED audit code.
 */
public class DB2LUWServerSurveyActionService extends SurveyActionServiceConnector
{
    private final List<JDBCResourceConnector> jdbcResourceConnectors = new CopyOnWriteArrayList<>();

    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     * <br><br>
     * The survey is broken into short units of work so that it does not hold a database transaction open for
     * its whole duration: every query runs in auto-commit mode, and each database is visited through its own
     * connection, which is released before the next database starts.  Every database is visited twice: first
     * to gather its database statistics (PROFILING_ASSOCIATED_RESOURCES) and then, unless the survey stops
     * after that step, to survey its schemas (PRODUCE_INVENTORY).  A database that cannot be surveyed is
     * logged and skipped, and the annotations for each database are stored as soon as that database is
     * complete, so a problem late in a large survey does not discard the work done before it.
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

            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            /*
             * There is no cross-database catalog view to query, so the candidate database list comes from
             * configuration: includeDatabaseList if supplied, otherwise just the single database this connection
             * already names.
             */
            List<String> includedDatabases = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                                          null);

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
                logRecord(methodName, DB2LUWAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                          assetStore.getQualifiedName(),
                                                                                          assetStore.getAssetGUID()));
            }
            else
            {
                List<String> excludedDatabases = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                                              Collections.emptyList());

                List<String> surveyDatabases = new ArrayList<>();

                for (String databaseName : validDatabases)
                {
                    if (surveyContext.elementShouldBeSurveyed(databaseName, excludedDatabases, null))
                    {
                        surveyDatabases.add(databaseName);
                    }
                }

                List<String> excludedSchemas = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                List<String> includedSchemas = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                DB2LUWDatabaseStatsExtractor statsExtractor = new DB2LUWDatabaseStatsExtractor(surveyDatabases,
                                                                                               excludedSchemas,
                                                                                               includedSchemas,
                                                                                               this,
                                                                                               auditLog,
                                                                                               surveyActionServiceName);

                /*
                 * Unlike Oracle's CDB root (which can gather every PDB's size in one query before looping for
                 * per-PDB schema detail), each Db2 database's own statistics are only visible from a connection
                 * to that specific database.  The database statistics for every database are gathered first,
                 * each through its own short-lived connection, so that this analysis step is complete before
                 * the survey tests whether it should stop.
                 */
                List<String> measuredDatabases = new ArrayList<>();

                for (String databaseName : surveyDatabases)
                {
                    if (! super.isActive())
                    {
                        break;
                    }

                    if (this.measureDatabase(assetConnector, databaseName, statsExtractor))
                    {
                        measuredDatabases.add(databaseName);
                    }
                }

                /*
                 * Stopping here records the database-level statistics for each database, without surveying
                 * the databases' schemas.
                 */
                if (super.isFinalAnalysisStep())
                {
                    this.addAnnotations(statsExtractor.getDatabaseAnnotations(), annotationStore);
                    return;
                }

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                /*
                 * Each database is reconnected to survey its schemas.  Holding every database's connection
                 * open between the two loops would accumulate a connection pool per database.
                 */
                for (String databaseName : measuredDatabases)
                {
                    if (! super.isActive())
                    {
                        break;
                    }

                    this.surveyDatabase(assetConnector, databaseName, statsExtractor, annotationStore);
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
     * Gather the database-level statistics for a single database.  Any failure is logged and confined to
     * this database.  The connector for the database is disconnected afterwards, so that a survey of many
     * databases does not accumulate an open connection pool for each of them.
     *
     * @param serverConnector connector for the entry database's connection
     * @param databaseName name of the database to measure
     * @param statsExtractor extractor that gathers the statistics for the database
     * @return true if the database could be connected to (its schemas can then be surveyed)
     */
    private boolean measureDatabase(JDBCResourceConnector        serverConnector,
                                    String                       databaseName,
                                    DB2LUWDatabaseStatsExtractor statsExtractor)
    {
        final String methodName = "measureDatabase";

        JDBCResourceConnector databaseConnector = null;

        try
        {
            databaseConnector = this.getDatabaseConnector(serverConnector, databaseName);

            try (Connection databaseSpecificConnection = databaseConnector.getDataSource().getConnection())
            {
                /*
                 * The survey only reads, so each query can be its own short transaction rather than one
                 * transaction lasting the whole survey.  The pool restores auto-commit to off when the
                 * connection is returned.
                 */
                databaseSpecificConnection.setAutoCommit(true);

                statsExtractor.getDatabaseStatistics(databaseName, databaseSpecificConnection);
            }

            return true;
        }
        catch (Exception error)
        {
            logRecord(methodName, DB2LUWAuditCode.DATABASE_NOT_SURVEYED.getMessageDefinition(surveyActionServiceName,
                                                                                             databaseName,
                                                                                             error.getClass().getName(),
                                                                                             error.getMessage()));
            return false;
        }
        finally
        {
            this.releaseDatabaseConnector(databaseConnector);
        }
    }


    /**
     * Survey the schemas in a single database and store the resulting annotations, which include the database
     * statistics gathered by measureDatabase().  Any failure is logged and confined to this database.  The
     * connector for the database is disconnected afterwards, so that a survey of many databases does not
     * accumulate an open connection pool for each of them.
     *
     * @param serverConnector connector for the entry database's connection
     * @param databaseName name of the database to survey
     * @param statsExtractor extractor holding the database statistics for the database
     * @param annotationStore destination for the annotations
     */
    private void surveyDatabase(JDBCResourceConnector        serverConnector,
                                String                       databaseName,
                                DB2LUWDatabaseStatsExtractor statsExtractor,
                                AnnotationStore              annotationStore)
    {
        final String methodName = "surveyDatabase";

        JDBCResourceConnector databaseConnector = null;

        try
        {
            databaseConnector = this.getDatabaseConnector(serverConnector, databaseName);

            try (Connection databaseSpecificConnection = databaseConnector.getDataSource().getConnection())
            {
                databaseSpecificConnection.setAutoCommit(true);

                statsExtractor.getSchemaStatistics(databaseName, databaseSpecificConnection);
            }

            /*
             * The connection is released before the annotations are stored.
             */
            this.addAnnotations(statsExtractor.getAnnotations(databaseName), annotationStore);
        }
        catch (Exception error)
        {
            logRecord(methodName, DB2LUWAuditCode.DATABASE_NOT_SURVEYED.getMessageDefinition(surveyActionServiceName,
                                                                                             databaseName,
                                                                                             error.getClass().getName(),
                                                                                             error.getMessage()));
        }
        finally
        {
            this.releaseDatabaseConnector(databaseConnector);
        }
    }


    /**
     * Disconnect a connector for a specific database and stop tracking it.
     *
     * @param databaseConnector connector to release (may be null)
     */
    private void releaseDatabaseConnector(JDBCResourceConnector databaseConnector)
    {
        if (databaseConnector != null)
        {
            jdbcResourceConnectors.remove(databaseConnector);

            try
            {
                databaseConnector.disconnect();
            }
            catch (Exception error)
            {
                // Already reported by the connector; nothing more to release
            }
        }
    }


    /**
     * Store annotations against the surveyed asset.
     *
     * @param annotations annotations to store (may be null)
     * @param annotationStore destination for the annotations
     * @throws Exception problem storing an annotation
     */
    private void addAnnotations(List<AnnotationProperties> annotations,
                                AnnotationStore            annotationStore) throws Exception
    {
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


    /**
     * Return a started connector for a specific database.  This code is needed because the sibling
     * databases are not catalogued at this time so we are creating connections to each database based on its
     * name and the databaseURL used to connect to the entry database.
     *
     * @param serverConnector connector for the entry database's connection
     * @param databaseName name of the target database
     * @return started connector
     * @throws Exception the connector could not be created or started
     */
    private JDBCResourceConnector getDatabaseConnector(JDBCResourceConnector serverConnector,
                                                       String                databaseName) throws Exception
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
         * by disconnect() - or by releaseDatabaseConnector() if the survey is still running.
         */
        jdbcResourceConnectors.add(newConnector);

        newConnector.start();

        return newConnector;
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
