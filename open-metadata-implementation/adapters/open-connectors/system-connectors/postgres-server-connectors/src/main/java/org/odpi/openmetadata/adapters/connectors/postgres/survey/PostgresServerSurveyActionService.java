/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.postgres.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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

            Map<String, Long> databases = new HashMap<>();

            DataSource jdbcDataSource = assetConnector.getDataSource();
            Connection jdbcConnection = jdbcDataSource.getConnection();

            final String sqlCommand1 = "SELECT oid, datname, datistemplate, datallowconn from pg_database;";

            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(sqlCommand1);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                /*
                 * This first test removes databases that are templates or do not allow connections.
                 */
                if ((! resultSet.getBoolean("datistemplate")) &&
                        (resultSet.getBoolean("datallowconn")))
                {
                    long oid = resultSet.getLong("oid");
                    String databaseName = resultSet.getString("datname");

                    databases.put(databaseName, oid);
                }
            }

            resultSet.close();
            preparedStatement.close();

            if (databases.isEmpty())
            {
                auditLog.logMessage(methodName, PostgresAuditCode.NO_DATABASES.getMessageDefinition(surveyActionServiceName,
                                                                                                    assetStore.getAssetProperties().getQualifiedName(),
                                                                                                    assetStore.getAssetGUID()));
            }
            else
            {
                Map<String, Long> profileCounts = new HashMap<>();

                for (String databaseName : databases.keySet())
                {
                    final String sqlCommand2 = "SELECT pg_database_size(" + databases.get(databaseName) + ");";

                    preparedStatement = jdbcConnection.prepareStatement(sqlCommand2);
                    try
                    {
                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next())
                        {
                            profileCounts.put(databaseName, resultSet.getLong("pg_database_size"));
                        }
                        else
                        {
                            profileCounts.put(databaseName, 0L);
                        }
                    }
                    catch (Exception noSize)
                    {
                        profileCounts.put(databaseName, 0L);
                    }

                    resultSet.close();
                    preparedStatement.close();
                }

                ResourceProfileAnnotation annotation = new ResourceProfileAnnotation();

                annotation.setAnnotationType(PostgresAnnotationType.DATABASE_SIZES.getName());
                annotation.setSummary(PostgresAnnotationType.DATABASE_SIZES.getSummary());
                annotation.setExplanation(PostgresAnnotationType.DATABASE_SIZES.getExplanation());

                annotation.setProfileCounts(profileCounts);

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
}
