/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * JDBCResourceConnector provides basic implementation of {@link DataSource} interface in order to get a {@link Connection} to
 * target database. This is done via a static inner class, since {@link DataSource#getConnection()} clashes with
 * {@link ConnectorBase#getConnection()}.
 * <br><br>
 * The DataSource can be used directly.  There are also selected methods to issue common SQL statements to the database.
 */
public class PostgresServerSurveyActionService extends SurveyActionServiceConnector
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private Connector connector = null;


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

            annotationStore.setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            /*
             * Before performing any real work, check the type of the asset.
             */
            AssetUniverse assetUniverse = assetStore.getAssetProperties();

            if (assetUniverse == null)
            {
                super.throwNoAsset(assetStore.getAssetGUID(), surveyActionServiceName, methodName);
                return;
            }
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.SOFTWARE_SERVER.typeName))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.SOFTWARE_SERVER.typeName,
                                            methodName);
                return;
            }

            /*
             * The asset should have a special connector for databases.  If the connector is wrong the cast will fail.
             */
            connector = assetStore.getConnectorToAsset();
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;
            assetConnector.start();

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            Map<String, DatabaseStats> databases = new HashMap<>();

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
                String pg_stat_user_tablesSQLCommand = "SELECT datname, tup_fetched, tup_inserted, tup_updated, tup_deleted, session_time, active_time, stats_reset FROM pg_catalog.pg_stat_database;";
                preparedStatement = jdbcConnection.prepareStatement(pg_stat_user_tablesSQLCommand);

                resultSet = preparedStatement.executeQuery();

                Map<String, Long>   databaseTuplesFetched  = new HashMap<>();
                Map<String, Long>   databaseTuplesInserted = new HashMap<>();
                Map<String, Long>   databaseTuplesUpdated  = new HashMap<>();
                Map<String, Long>   databaseTuplesDeleted  = new HashMap<>();
                Map<String, Double> databaseSessionTime    = new HashMap<>();
                Map<String, Double> databaseActiveTime     = new HashMap<>();
                Map<String, Date>   databaseStatsReset     = new HashMap<>();

                while (resultSet.next())
                {
                    String databaseName = resultSet.getString("datname");

                    if (validDatabases.contains(databaseName))
                    {
                        databaseTuplesFetched.put(databaseName, resultSet.getLong("tup_fetched"));
                        databaseTuplesInserted.put(databaseName, resultSet.getLong("tup_inserted"));
                        databaseTuplesUpdated.put(databaseName, resultSet.getLong("tup_updated"));
                        databaseTuplesDeleted.put(databaseName, resultSet.getLong("tup_deleted"));
                        databaseSessionTime.put(databaseName, resultSet.getDouble("session_time"));
                        databaseActiveTime.put(databaseName, resultSet.getDouble("active_time"));
                        databaseStatsReset.put(databaseName, resultSet.getDate("stats_reset"));
                    }
                }

                resultSet.close();
                preparedStatement.close();

                Map<String, Long> databaseSizes = new HashMap<>();

                for (String databaseName : databases.keySet())
                {
                    final String sqlCommand2 = "SELECT pg_database_size(" + databases.get(databaseName) + ");";

                    preparedStatement = jdbcConnection.prepareStatement(sqlCommand2);
                    try
                    {
                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next())
                        {
                            databaseSizes.put(databaseName, resultSet.getLong("pg_database_size"));
                        }
                    }
                    catch (Exception noSize)
                    {
                        // Nothing to do
                    }

                    resultSet.close();
                    preparedStatement.close();
                }

                ResourceProfileAnnotation annotation = new ResourceProfileAnnotation();

                annotation.setAnnotationType(PostgresAnnotationType.DATABASE_SIZES.getName());
                annotation.setSummary(PostgresAnnotationType.DATABASE_SIZES.getSummary());
                annotation.setExplanation(PostgresAnnotationType.DATABASE_SIZES.getExplanation());

                annotation.setProfileCounts(databaseSizes);

                annotationStore.addAnnotation(annotation, surveyContext.getAssetGUID());

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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        if (connector != null)
        {
            connector.disconnect();
        }

        super.disconnect();
    }


    /**
     * DatabaseStats captures information about a particular database.
     */
    static class DatabaseStats
    {
        long   oid            = 0L;
        long   size           = 0L;
        long   tuplesFetched  = 0L;
        long   tuplesInserted = 0L;
        long   tuplesUpdated  = 0L;
        long   tuplesDeleted  = 0L;
        double sessionTime    = 0;
        double activeTime     = 0;
        Date   statsReset     = null;
    }
}
