/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
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
 * PostgresDatabaseSurveyActionService surveys the content of a particular database.
 */
public class PostgresDatabaseSurveyActionService extends SurveyActionServiceConnector
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

            if (super.isFinalAnalysisStep())
            {
                return;
            }

            String databaseName = assetConnector.getDatabaseName();

            if (databaseName == null)
            {
                logRecord(methodName, PostgresAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                          assetStore.getQualifiedName(),
                                                                                          assetStore.getAssetGUID()));
            }
            else
            {
                annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

                List<String> validDatabases = Collections.singletonList(databaseName);

                List<String> excludedSchemas = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                List<String> includedSchemas = super.getArrayRequestOrConfigurationProperty(PostgresConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                                            null);

                PostgresDatabaseStatsExtractor statsExtractor = new PostgresDatabaseStatsExtractor(validDatabases,
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

                    statsExtractor.getDatabaseStatistics(jdbcConnection);

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
