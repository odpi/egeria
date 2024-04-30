/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

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
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourcePhysicalStatusAnnotation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                super.throwNoAsset(assetStore.getAssetGUID(),
                                   surveyActionServiceName,
                                   methodName);
                return;
            }
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.DATABASE_TYPE_NAME))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.DATABASE_TYPE_NAME,
                                            methodName);
                return;
            }

            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = assetStore.getConnectorToAsset();
            JDBCResourceConnector assetConnector = (JDBCResourceConnector)connector;

            assetConnector.start();

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            final String pg_statsSQLCommand = "SELECT schemaname, tablename, attname, avg_width, most_common_vals, most_common_elements from pg_stats;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(pg_statsSQLCommand);

            ResultSet resultSet = preparedStatement.executeQuery();

            final String pg_tablesSQLCommand = "SELECT schemaname, tablename, tableowner, tablespace, hasindexes, hasrules, has_triggers from pg_tables;";

            preparedStatement = jdbcConnection.prepareStatement(pg_statsSQLCommand);

            resultSet = preparedStatement.executeQuery();

            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            ResourcePhysicalStatusAnnotation measurementAnnotation = new ResourcePhysicalStatusAnnotation();

            measurementAnnotation.setAnnotationType(PostgresAnnotationType.DATABASE_SIZES.getName());
            measurementAnnotation.setSummary(PostgresAnnotationType.DATABASE_SIZES.getSummary());
            measurementAnnotation.setExplanation(PostgresAnnotationType.DATABASE_SIZES.getExplanation());


            Map<String, String> dataSourceProperties = new HashMap<>();



            measurementAnnotation.setResourceProperties(dataSourceProperties);

            annotationStore.addAnnotation(measurementAnnotation, surveyContext.getAssetGUID());
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


    static class schemaDetails
    {
        private String schemaName = null;

        private List<TableDetails> tables = null;
    }

    static class TableDetails
    {
        private String tableName = null;


    }


    static class ColumnDetails
    {

    }
}
