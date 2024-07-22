/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBCResourceConnector provides basic implementation of {@link DataSource} interface in order to get a {@link Connection} to
 * target database. This is done via a static inner class, since {@link DataSource#getConnection()} clashes with
 * {@link ConnectorBase#getConnection()}.
 * <br><br>
 * The DataSource can be used directly.  There are also selected methods to issue common SQL statements to the database.
 */
public class PostgresServerSurveyActionService extends SurveyActionServiceConnector
{
    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
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

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            final String sqlCommand1 = "SELECT oid, datname, datistemplate, datallowconn from pg_database;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand1);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> validDatabases = new ArrayList<>();

            while (resultSet.next())
            {
                /*
                 * This first test removes databases that are templates or do not allow connections.
                 */
                if ((! resultSet.getBoolean("datistemplate")) &&
                        (resultSet.getBoolean("datallowconn")))
                {
                    String databaseName = resultSet.getString("datname");

                    validDatabases.add(databaseName);
                }
            }

            resultSet.close();
            preparedStatement.close();

            if (validDatabases.isEmpty())
            {
                auditLog.logMessage(methodName, PostgresAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                                    assetStore.getAssetProperties().getQualifiedName(),
                                                                                                    assetStore.getAssetGUID()));
            }
            else
            {
                PostgresDatabaseStatsExtractor statsExtractor = new PostgresDatabaseStatsExtractor(validDatabases,
                                                                                                   jdbcConnection,
                                                                                                   this);

                List<Annotation> databaseStatistics = statsExtractor.getStatistics();

                if (databaseStatistics != null)
                {
                    for (Annotation annotation : databaseStatistics)
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
