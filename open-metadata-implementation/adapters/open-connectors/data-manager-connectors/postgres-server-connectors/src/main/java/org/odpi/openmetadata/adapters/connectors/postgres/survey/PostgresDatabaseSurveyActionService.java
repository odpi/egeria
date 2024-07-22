/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBCResourceConnector provides basic implementation of {@link DataSource} interface in order to get a {@link Connection} to
 * target database. This is done via a static inner class, since {@link DataSource#getConnection()} clashes with
 * {@link ConnectorBase#getConnection()}.
 * <br><br>
 * The DataSource can be used directly.  There are also selected methods to issue common SQL statements to the database.
 */
public class PostgresDatabaseSurveyActionService extends SurveyActionServiceConnector
{
    private Map<String, SchemaDetails> schemaDetailsMap = new HashMap<>();


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

            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = super.performCheckAssetAnalysisStep(JDBCResourceConnector.class, OpenMetadataType.RELATIONAL_DATABASE.typeName);
            JDBCResourceConnector assetConnector = (JDBCResourceConnector) connector;

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            String databaseName = assetConnector.getDatabaseName();
            List<String> validDatabases = new ArrayList<>();
            validDatabases.add(databaseName);

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            PostgresDatabaseStatsExtractor databaseStatsExtractor = new PostgresDatabaseStatsExtractor(validDatabases, jdbcConnection, this);

            List<Annotation> annotations = databaseStatsExtractor.getStatistics();



            final String pg_statsSQLCommand = "SELECT schemaname, tablename, attname, avg_width, most_common_vals, most_common_elems from pg_stats;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(pg_statsSQLCommand);

            ResultSet resultSet = preparedStatement.executeQuery();


            final String pg_stat_user_tablesSQLCommand = "SELECT schemaname, relname, seq_tup_read, n_tup_ins, n_tup_upd, n_tup_del from pg_catalog.pg_stat_user_tables;";

            preparedStatement = jdbcConnection.prepareStatement(pg_stat_user_tablesSQLCommand);

            resultSet = preparedStatement.executeQuery();

            final String pg_tablesSQLCommand = "SELECT schemaname, tablename, tableowner, tablespace, hasindexes, hasrules, hastriggers from pg_tables;";

            preparedStatement = jdbcConnection.prepareStatement(pg_tablesSQLCommand);

            resultSet = preparedStatement.executeQuery();

            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());
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





    static class SchemaDetails
    {
        Map<String, TableDetails> tables = new HashMap<>();
    }

    static class TableDetails
    {
        String tableOwner = null;
        boolean hasIndexes = false;
        boolean hasRules   = false;
        boolean hasTriggers = false;
        Map<String, ColumnDetails> columns = new HashMap<>();
    }

    static class ColumnDetails
    {

    }
}
