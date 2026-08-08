/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode;
import org.odpi.openmetadata.adapters.connectors.duckdb.utilities.DuckDBUtils;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
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
import java.util.Collections;
import java.util.List;

/**
 * DuckDBDatabaseSurveyActionService surveys the content of a DuckDB database.  As well as the vendor-neutral
 * schema/table/column measurements gathered by DuckDBDatabaseStatsExtractor (also produced by the other database
 * connector suites), this service unconditionally runs DuckDBFederationExtractor to discover and report DuckDB's
 * federation features - other databases ATTACH-ed to this one, and views that scan external files/object-store
 * resources.  Federation discovery is not a separate survey service; it is always run as part of every DuckDB
 * database survey.
 */
public class DuckDBDatabaseSurveyActionService extends SurveyActionServiceConnector
{
    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException a problem within the survey service.
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
            connector = super.performCheckAssetAnalysisStep(JDBCResourceConnector.class, OpenMetadataType.RELATIONAL_DATABASE.typeName);
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;
            assetConnector.start();

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            String databaseName = assetConnector.getDatabaseName();

            if (databaseName == null)
            {
                auditLog.logMessage(methodName, DuckDBAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                            "null",
                                                                                                            methodName,
                                                                                                            "The connected DuckDB database has no name"));
            }
            else
            {
                /*
                 * DuckDB does not persist ATTACH-ed data sources in the database file between sessions, so this
                 * connection must re-issue any configured ATTACH (and INSTALL/LOAD) statements itself before
                 * federation discovery queries duckdb_databases() - otherwise it would never see any attached
                 * databases, even though they were reachable when the file was originally set up.
                 */
                DuckDBUtils.runAttachStatements(jdbcConnection,
                                                DuckDBUtils.getAttachStatements(connectionBean.getConfigurationProperties()),
                                                auditLog,
                                                surveyActionServiceName,
                                                databaseName);

                annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

                List<String> validDatabases = Collections.singletonList(databaseName);

                DuckDBDatabaseStatsExtractor statsExtractor = new DuckDBDatabaseStatsExtractor(validDatabases, this);

                statsExtractor.getDatabaseStatistics(jdbcConnection);

                annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                statsExtractor.getSchemaStatistics(databaseName, jdbcConnection);

                /*
                 * Federation discovery is unconditional - every DuckDB database survey also reports the databases
                 * that have been ATTACH-ed to it, and the views that scan external files/object-store resources.
                 */
                DuckDBFederationExtractor federationExtractor = new DuckDBFederationExtractor(this, auditLog, surveyActionServiceName);

                federationExtractor.discoverAttachedSources(jdbcConnection, databaseName);
                federationExtractor.discoverExternalFileSources(jdbcConnection, databaseName);

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

                for (AnnotationProperties annotation : federationExtractor.getAnnotations())
                {
                    if (super.isActive())
                    {
                        annotationStore.addAnnotation(annotation, surveyContext.getAssetGUID());
                    }
                }
            }

            jdbcConnection.commit();
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
}
