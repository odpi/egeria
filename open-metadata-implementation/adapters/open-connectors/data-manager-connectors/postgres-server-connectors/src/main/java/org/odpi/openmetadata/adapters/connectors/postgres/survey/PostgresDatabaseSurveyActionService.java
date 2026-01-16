/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

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
            connector = super.performCheckAssetAnalysisStep(JDBCResourceConnector.class, OpenMetadataType.SOFTWARE_SERVER.typeName);
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;
            assetConnector.start();

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            String databaseName = assetConnector.getDatabaseName();

            if (databaseName == null)
            {
                auditLog.logMessage(methodName, PostgresAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                                    assetStore.getQualifiedName(),
                                                                                                    assetStore.getAssetGUID()));
            }
            else
            {
                annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

                List<String> validDatabases = Collections.singletonList(databaseName);

                PostgresDatabaseStatsExtractor statsExtractor = new PostgresDatabaseStatsExtractor(validDatabases,
                                                                                                   this);

                statsExtractor.getDatabaseStatistics(jdbcConnection);

                annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                statsExtractor.getSchemaStatistics(databaseName, jdbcConnection);

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
