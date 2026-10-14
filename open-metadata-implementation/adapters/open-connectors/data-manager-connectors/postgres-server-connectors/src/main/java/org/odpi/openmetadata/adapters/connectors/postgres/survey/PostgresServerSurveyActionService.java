/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PostgresServerSurveyActionService surveys each of the databases hosted by a PostgreSQL Database Server.
 */
public class PostgresServerSurveyActionService extends SurveyActionServiceConnector
{
    private final List<JDBCResourceConnector> jdbcResourceConnectors = new CopyOnWriteArrayList<>();

    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     * <br><br>
     * The survey is broken into short units of work so that it does not hold a database transaction open for
     * its whole duration: every query runs in auto-commit mode, the connection to the server's postgres
     * database is released before the individual databases are surveyed, and each database is surveyed
     * through its own connection, which is released before the next database starts.  A database that cannot
     * be surveyed is logged and skipped, and the annotations for each database are stored as soon as that
     * database is complete, so a problem late in a large survey does not discard the work done before it.
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

            final String sqlCommand1 = "SELECT datname, datistemplate, datallowconn, has_database_privilege(datname, 'CONNECT') AS canconnect FROM pg_catalog.pg_database;";

            List<String> validDatabases = new ArrayList<>();
            List<String> inaccessibleDatabases = new ArrayList<>();

            PostgresDatabaseStatsExtractor statsExtractor = null;
            List<String>                   surveyDatabases = new ArrayList<>();

            DataSource jdbcDataSource = assetConnector.getDataSource();
            try (Connection jdbcConnection = jdbcDataSource.getConnection())
            {
                /*
                 * The survey only reads, so each query can be its own transaction.  The pool restores
                 * auto-commit to off when the connection is returned.
                 */
                jdbcConnection.setAutoCommit(true);

                try (PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand1);
                     ResultSet         resultSet         = preparedStatement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        /*
                         * This first test removes databases that are templates or do not allow connections.
                         */
                        if ((! resultSet.getBoolean("datistemplate")) &&
                                (resultSet.getBoolean("datallowconn")))
                        {
                            String databaseName = resultSet.getString("datname");

                            if (resultSet.getBoolean("canconnect"))
                            {
                                validDatabases.add(databaseName);
                            }
                            else
                            {
                                inaccessibleDatabases.add(databaseName);
                            }
                        }
                    }
                }

                if ((validDatabases.isEmpty()) && (inaccessibleDatabases.isEmpty()))
                {
                    logRecord(methodName, PostgresAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                              assetStore.getQualifiedName(),
                                                                                              assetStore.getAssetGUID()));
                }
                else
                {
                    List<String> excludedDatabases = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_DATABASE_LIST.getName(),
                                                                                                  Collections.singletonList("postgres"));

                    List<String> includedDatabases = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.INCLUDE_DATABASE_LIST.getName(),
                                                                                                  null);

                    for (String databaseName : validDatabases)
                    {
                        if (surveyContext.elementShouldBeSurveyed(databaseName, excludedDatabases, includedDatabases))
                        {
                            surveyDatabases.add(databaseName);
                        }
                    }

                    for (String databaseName : inaccessibleDatabases)
                    {
                        if (surveyContext.elementShouldBeSurveyed(databaseName, excludedDatabases, includedDatabases))
                        {
                            logRecord(methodName, PostgresAuditCode.NO_CONNECT_PRIVILEGE.getMessageDefinition(surveyActionServiceName,
                                                                                                              databaseName,
                                                                                                              assetStore.getQualifiedName(),
                                                                                                              assetConnector.getConnection().getUserId()));
                        }
                    }

                    List<String> excludedSchemas = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                                                null);

                    List<String> includedSchemas = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                                                null);

                    statsExtractor = new PostgresDatabaseStatsExtractor(surveyDatabases,
                                                                        excludedSchemas,
                                                                        includedSchemas,
                                                                        this,
                                                                        auditLog,
                                                                        surveyActionServiceName);

                    statsExtractor.getDatabaseStatistics(jdbcConnection);
                }
            }

            if (statsExtractor != null)
            {
                /*
                 * Stopping here records the server-level statistics for each database, without surveying
                 * the databases' schemas.
                 */
                if (super.isFinalAnalysisStep())
                {
                    this.addAnnotations(statsExtractor.getDatabaseAnnotations(), annotationStore);
                    return;
                }

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                for (String databaseName : surveyDatabases)
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
     * Survey the schemas in a single database and store the resulting annotations.  Any failure is logged and
     * confined to this database.  The connector for the database is disconnected afterwards, so that a server
     * with many databases does not accumulate an open connection pool for each of them.
     *
     * @param serverConnector connector to the server's postgres database
     * @param databaseName name of the database to survey
     * @param statsExtractor extractor holding the server-level statistics for the database
     * @param annotationStore destination for the annotations
     */
    private void surveyDatabase(JDBCResourceConnector          serverConnector,
                                String                         databaseName,
                                PostgresDatabaseStatsExtractor statsExtractor,
                                AnnotationStore                annotationStore)
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

            this.addAnnotations(statsExtractor.getAnnotations(databaseName), annotationStore);
        }
        catch (Exception error)
        {
            logRecord(methodName, PostgresAuditCode.DATABASE_NOT_SURVEYED.getMessageDefinition(surveyActionServiceName,
                                                                                               databaseName,
                                                                                               error.getClass().getName(),
                                                                                               error.getMessage()));
        }
        finally
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
     * Return a started connector for a specific database.  This code is needed
     * because the databases are not catalogued at this time so we are creating connections to
     * each database based on the name of the database and the databaseURL used to connect to the
     * "postgres" database which describes the whole server.
     *
     * @param serverConnector connector to the server's postgres database
     * @param databaseName name of the database
     * @return started connector
     * @throws Exception the connector could not be created or started
     */
    private JDBCResourceConnector getDatabaseConnector(JDBCResourceConnector serverConnector,
                                                       String                databaseName) throws Exception
    {
        String serverNetworkAddress = serverConnector.getConnection().getEndpoint().getNetworkAddress();
        String databaseSpecificURL  = serverNetworkAddress.replace("/postgres", "/" + databaseName);

        org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection databaseConnectionDetails =
                new org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection(serverConnector.getConnection());

        /*
         * The Connection copy constructor only takes a shallow copy of the endpoint, so mutating
         * databaseConnectionDetails.getEndpoint() in place would also corrupt the endpoint shared with
         * serverConnector's own connection - and every survey after the first would then be built from an
         * already-substituted URL instead of the original server URL.  Taking an explicit copy of the
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
