/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.RelationalDataManagerMeasurement;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceMeasureAnnotation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PostgresDatabaseStatsExtractor
{
    private final List<String> validDatabases;
    private final Connection   jdbcConnection;
    private final SurveyActionServiceConnector surveyActionServiceConnector;

    /**
     * Constructor sets up the list of databases to process and the connection to the database.
     *
     * @param validDatabases list of database names
     * @param jdbcConnection connection to the database
     */
    public PostgresDatabaseStatsExtractor(List<String>                 validDatabases,
                                          Connection                   jdbcConnection,
                                          SurveyActionServiceConnector surveyActionServiceConnector)
    {
        this.validDatabases = validDatabases;
        this.jdbcConnection = jdbcConnection;
        this.surveyActionServiceConnector = surveyActionServiceConnector;
    }


    /**
     * Retrieve statistics and convert them to annotations.
     *
     * @return list of annotations
     * @throws SQLException problem accessing the database
     * @throws PropertyServerException problem create JSON properties
     */
    List<Annotation> getStatistics() throws SQLException, PropertyServerException
    {
        Map<String, RelationalDataManagerMeasurement> databaseMeasurements = new HashMap<>();

        String pg_stat_user_tablesSQLCommand = "SELECT datname, tup_fetched, tup_inserted, tup_updated, tup_deleted, session_time, active_time, stats_reset FROM pg_catalog.pg_stat_database;";

        PreparedStatement preparedStatement = jdbcConnection.prepareStatement(pg_stat_user_tablesSQLCommand);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            String databaseName = resultSet.getString("datname");

            if (validDatabases.contains(databaseName))
            {
                RelationalDataManagerMeasurement databaseMeasurement = new RelationalDataManagerMeasurement();

                databaseMeasurement.setResourceName(databaseName);
                databaseMeasurement.setRowsFetched(resultSet.getLong("tup_fetched"));
                databaseMeasurement.setRowsInserted(resultSet.getLong("tup_inserted"));
                databaseMeasurement.setRowsUpdated(resultSet.getLong("tup_updated"));
                databaseMeasurement.setRowsDeleted(resultSet.getLong("tup_deleted"));
                databaseMeasurement.setSessionTime(resultSet.getDouble("session_time"));
                databaseMeasurement.setActiveTime(resultSet.getDouble("active_time"));
                databaseMeasurement.setStatsReset(resultSet.getDate("stats_reset"));

                databaseMeasurements.put(databaseName, databaseMeasurement);
            }
        }

        resultSet.close();
        preparedStatement.close();

        for (String databaseName : validDatabases)
        {
            RelationalDataManagerMeasurement currentDatabase = databaseMeasurements.get(databaseName);

            if (currentDatabase != null)
            {
                final String sqlCommand2 = "SELECT pg_database_size(" + databaseName + ");";

                preparedStatement = jdbcConnection.prepareStatement(sqlCommand2);
                try
                {
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next())
                    {
                        currentDatabase.setSize(resultSet.getLong("pg_database_size"));
                    }
                }
                catch (Exception noSize)
                {
                    // Nothing to do
                }

                databaseMeasurements.put(databaseName, currentDatabase);

                resultSet.close();
                preparedStatement.close();
            }
        }

        List<Annotation>  annotations = new ArrayList<>();

        for (String databaseName : databaseMeasurements.keySet())
        {
            RelationalDataManagerMeasurement currentDatabase = databaseMeasurements.get(databaseName);

            if (currentDatabase != null)
            {
                ResourceMeasureAnnotation annotation = new ResourceMeasureAnnotation();

                annotation.setAnnotationType(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName());
                annotation.setSummary(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getSummary());
                annotation.setExplanation(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getExplanation());
                annotation.setAnalysisStep(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getAnalysisStep());

                annotation.setJsonProperties(surveyActionServiceConnector.getJSONProperties(currentDatabase));

                Map<String, String> resourceProperties = new HashMap<>();

                resourceProperties.put("Database Name", currentDatabase.getResourceName());
                resourceProperties.put("Database Size", Long.toString(currentDatabase.getSize()));
                resourceProperties.put("Rows Fetched", Long.toString(currentDatabase.getRowsFetched()));
                resourceProperties.put("Rows Inserted", Long.toString(currentDatabase.getRowsInserted()));
                resourceProperties.put("Rows Updated", Long.toString(currentDatabase.getRowsUpdated()));
                resourceProperties.put("Rows Deleted", Long.toString(currentDatabase.getRowsDeleted()));
                resourceProperties.put("Session Time", Double.toString(currentDatabase.getSessionTime()));
                resourceProperties.put("Active Time", Double.toString(currentDatabase.getActiveTime()));

                annotation.setResourceProperties(resourceProperties);

                annotations.add(annotation);
            }
        }

        if (! annotations.isEmpty())
        {
            return annotations;
        }

        return null;
    }
}
