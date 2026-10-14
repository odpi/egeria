/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.db2luw.ffdc.DB2LUWAuditCode;
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
 * DB2LUWDatabaseSurveyActionService surveys the content of a particular Db2 for Linux, UNIX and Windows database.
 */
public class DB2LUWDatabaseSurveyActionService extends SurveyActionServiceConnector
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
             * The expected type here is RELATIONAL_DATABASE (the asset being surveyed), not SOFTWARE_SERVER (that is
             * only correct for the server-level survey service).
             */
            connector = super.performCheckAssetAnalysisStep(JDBCResourceConnector.class, OpenMetadataType.RELATIONAL_DATABASE.typeName);
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;
            assetConnector.start();

            if (super.isFinalAnalysisStep())
            {
                return;
            }

            String databaseName = assetConnector.getDatabaseName();

            if (databaseName == null)
            {
                logRecord(methodName, DB2LUWAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                          assetStore.getQualifiedName(),
                                                                                          assetStore.getAssetGUID()));
            }
            else
            {
                annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

                List<String> validDatabases = Collections.singletonList(databaseName);

                List<String> excludedSchemas = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                List<String> includedSchemas = super.getArrayRequestOrConfigurationProperty(DB2LUWConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                DB2LUWDatabaseStatsExtractor statsExtractor = new DB2LUWDatabaseStatsExtractor(validDatabases,
                                                                                               excludedSchemas,
                                                                                               includedSchemas,
                                                                                               this,
                                                                                               auditLog,
                                                                                               surveyActionServiceName);

                boolean    surveyStopped;
                DataSource jdbcDataSource = assetConnector.getDataSource();
                try (Connection jdbcConnection = jdbcDataSource.getConnection())
                {
                    /*
                     * The survey only reads, so each query can be its own short transaction rather than one
                     * transaction lasting the whole survey.  The pool restores auto-commit to off when the
                     * connection is returned.
                     */
                    jdbcConnection.setAutoCommit(true);

                    statsExtractor.getDatabaseStatistics(databaseName, jdbcConnection);

                    /*
                     * Stopping after measuring the database records its statistics without surveying its schemas.
                     */
                    surveyStopped = super.isFinalAnalysisStep();

                    if (! surveyStopped)
                    {
                        annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                        statsExtractor.getSchemaStatistics(databaseName, jdbcConnection);
                    }
                }

                /*
                 * The connection is released before the annotations are stored.
                 */
                List<AnnotationProperties> annotations;

                if (surveyStopped)
                {
                    annotations = statsExtractor.getDatabaseAnnotations();
                }
                else
                {
                    annotations = statsExtractor.getAnnotations(databaseName);
                }

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
}
