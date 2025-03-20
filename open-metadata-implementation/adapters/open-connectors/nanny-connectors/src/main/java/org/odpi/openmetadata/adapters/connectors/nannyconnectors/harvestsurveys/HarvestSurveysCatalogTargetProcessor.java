/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysErrorCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.schema.HarvestSurveysColumn;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.schema.HarvestSurveysTable;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyFileAnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyFolderAnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.*;
import org.odpi.openmetadata.integrationservices.catalog.connector.ConnectorFactoryService;

import java.util.*;


/**
 * HarvestSurveysConnector extracts survey results from the open metadata ecosystem.
 */
public class HarvestSurveysCatalogTargetProcessor extends CatalogTargetProcessorBase implements AssetManagerEventListener
{
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();
    

    /*
     * These maps contain the details of the columns in the tables that produce
     * a new row each time an update is made.  This is accomplished by having the sync_time
     * column as part of the primary key.
     */
    private final OpenMetadataAccess      openMetadataAccess;
    private final ConnectorFactoryService connectorFactoryService;
    private       JDBCResourceConnector   databaseClient     = null;


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination 
     * @param openMetadataAccess access to open metadata
     * @param connectorFactoryService client for creating open connectors
     * @throws ConnectorCheckedException error
     */
    public HarvestSurveysCatalogTargetProcessor(CatalogTarget           catalogTarget,
                                                Connector               connectorToTarget,
                                                String                  connectorName,
                                                AuditLog                auditLog,
                                                OpenMetadataAccess      openMetadataAccess,
                                                ConnectorFactoryService connectorFactoryService) throws ConnectorCheckedException
    {
        super(catalogTarget, connectorToTarget, connectorName, auditLog);

        this.openMetadataAccess = openMetadataAccess;
        this.openMetadataAccess.setForLineage(true);
        this.connectorFactoryService = connectorFactoryService;

        if (super.getCatalogTargetConnector() instanceof JDBCResourceConnector jdbcResourceConnector)
        {
            this.databaseClient = jdbcResourceConnector;
            this.databaseClient.start();

            String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), catalogTarget.getConfigurationProperties());
            if (schemaName == null)
            {
                schemaName = super.getStringConfigurationProperty(PostgresPlaceholderProperty.SCHEMA_NAME.getName(), catalogTarget.getConfigurationProperties());
            }

            loadDDL(databaseClient, schemaName);
        }
    }


    /**
     * Check that the tables for the repository are defined.
     *
     * @param schemaName name of the schema
     * @throws ConnectorCheckedException problem with the DDL
     */
    private void loadDDL(JDBCResourceConnector jdbcResourceConnector,
                         String                schemaName) throws ConnectorCheckedException
    {
        final String methodName = "loadDDL";

        java.sql.Connection databaseConnection = null;

        try
        {
            databaseConnection = jdbcResourceConnector.getDataSource().getConnection();

            PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                              "Survey records for a cohort of OMAG Servers.",
                                                                              HarvestSurveysTable.getTables());
            jdbcResourceConnector.addDatabaseDefinitions(databaseConnection, postgreSQLSchemaDDL.getDDLStatements());
            databaseConnection.commit();
        }
        catch (Exception error)
        {
            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  rollbackError)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            rollbackError.getClass().getName(),
                                                                                                            methodName,
                                                                                                            rollbackError.getMessage()),
                                          error);
                }
            }

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(schemaName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /* ==============================================================================
     * Standard methods that trigger activity.
     */


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        java.sql.Connection databaseConnection = null;

        final String methodName = "refresh";

        try
        {
            databaseConnection = databaseClient.getDataSource().getConnection();

            int startFrom = 0;
            List<OpenMetadataElement> surveyReportElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     openMetadataAccess.getMaxPagingSize());

            while (surveyReportElements != null)
            {
                for (OpenMetadataElement surveyReportElement : surveyReportElements)
                {
                    processSurveyReport(databaseConnection, surveyReportElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                surveyReportElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               startFrom,
                                                                               openMetadataAccess.getMaxPagingSize());
            }

            databaseConnection.commit();
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);

            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  rollbackError)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              rollbackError.getClass().getName(),
                                                                                                              methodName,
                                                                                                              rollbackError.getMessage()),
                                          error);
                }
            }

            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);

        }
    }


    /**
     * Extract information about a catalogued survey report.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportElement retrieved element
     */
    private void processSurveyReport(java.sql.Connection databaseConnection,
                                     OpenMetadataElement surveyReportElement) throws ConnectorCheckedException
    {
        final String methodName = "processSurveyReport";

        try
        {
            RelatedMetadataElement       relatedAsset                             = null;
            RelatedMetadataElement       relatedEngineAction                      = null;
            List<RelatedMetadataElement> relatedResourceProfileAnnotations        = new ArrayList<>();
            List<RelatedMetadataElement> relatedDataProfileLogAnnotations         = new ArrayList<>();
            List<RelatedMetadataElement> relatedDataSourceMeasurementsAnnotations = new ArrayList<>();
            List<RelatedMetadataElement> relatedRequestForActionAnnotations       = new ArrayList<>();
            List<RelatedMetadataElement> relatedOtherAnnotations                  = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                                               0,
                                                                                                               null,
                                                                                                               startFrom,
                                                                                                               openMetadataAccess.getMaxPagingSize());

            while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName))
                        {
                            relatedEngineAction = relatedMetadataElement;
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName))
                        {
                            relatedAsset = relatedMetadataElement;
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName))
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
                            {
                                relatedRequestForActionAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
                            {
                                relatedResourceProfileAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName))
                            {
                                relatedDataProfileLogAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
                            {
                                relatedDataSourceMeasurementsAnnotations.add(relatedMetadataElement);
                            }
                            else
                            {
                                relatedOtherAnnotations.add(relatedMetadataElement);
                            }
                        }
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                        0,
                                                                                        null,
                                                                                        startFrom,
                                                                                        openMetadataAccess.getMaxPagingSize());
            }

            syncSurveyReport(databaseConnection, surveyReportElement, relatedAsset, relatedEngineAction);

            processRequestForActionAnnotations(databaseConnection,
                                               surveyReportElement.getElementGUID(),
                                               relatedAsset,
                                               relatedRequestForActionAnnotations);

            processResourceProfileAnnotations(databaseConnection,
                                              surveyReportElement.getElementGUID(),
                                              relatedAsset,
                                              relatedResourceProfileAnnotations);

            processDataProfileLogAnnotations(databaseConnection,
                                             surveyReportElement.getElementGUID(),
                                             relatedAsset,
                                             relatedDataProfileLogAnnotations);

            processDataSourceMeasurementsAnnotations(databaseConnection,
                                                     surveyReportElement.getElementGUID(),
                                                     relatedAsset,
                                                     relatedDataSourceMeasurementsAnnotations);

            processOtherAnnotations(databaseConnection,
                                    surveyReportElement.getElementGUID(),
                                    relatedAsset,
                                    relatedOtherAnnotations);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Add details of request for action annotations to the database.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedRequestForActionAnnotations list of request for action annotations
     */
    private void processRequestForActionAnnotations(java.sql.Connection          databaseConnection,
                                                    String                       surveyReportGUID,
                                                    RelatedMetadataElement       relatedAsset,
                                                    List<RelatedMetadataElement> relatedRequestForActionAnnotations)
    {
        final String methodName = "processRequestForActionAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedRequestForActionAnnotations)
        {
            try
            {
                List<RelatedMetadataElement> relatedSubjects = this.syncAnnotation(databaseConnection,
                                                                                   surveyReportGUID,
                                                                                   relatedAnnotationElement.getElement(),
                                                                                   relatedAsset);
                List<RelatedMetadataElement> relatedActionTargets = null;
                int startFrom = 0;
                RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     1,
                                                                                                                     OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (relatedActionTargets == null)
                            {
                                relatedActionTargets = new ArrayList<>();
                            }

                            relatedActionTargets.add(relatedMetadataElement);
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            1,
                                                                                            OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                for (RelatedMetadataElement relatedAnnotationSubject : relatedSubjects)
                {
                    syncRequestForAction(databaseConnection,
                                         surveyReportGUID,
                                         relatedAnnotationElement.getElement(),
                                         relatedActionTargets,
                                         relatedAnnotationSubject);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of data profile annotations to the database.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedResourceProfileAnnotations list of request for action annotations
     */
    private void processResourceProfileAnnotations(java.sql.Connection          databaseConnection,
                                                   String                       surveyReportGUID,
                                                   RelatedMetadataElement       relatedAsset,
                                                   List<RelatedMetadataElement> relatedResourceProfileAnnotations)
    {
        final String methodName = "processResourceProfileAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedResourceProfileAnnotations)
        {
            try
            {
                List<RelatedMetadataElement> relatedAnnotationSubjects = this.syncAnnotation(databaseConnection,
                                                                                             surveyReportGUID,
                                                                                             relatedAnnotationElement.getElement(),
                                                                                             relatedAsset);

                for (RelatedMetadataElement relatedAnnotationSubject : relatedAnnotationSubjects)
                {
                    syncResourceProfile(databaseConnection,
                                        surveyReportGUID,
                                        relatedAnnotationElement.getElement(),
                                        relatedAnnotationSubject);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of data profile log annotations to the database.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataProfileLogAnnotations list of request for action annotations
     */
    private void processDataProfileLogAnnotations(java.sql.Connection          databaseConnection,
                                                  String                       surveyReportGUID,
                                                  RelatedMetadataElement       relatedAsset,
                                                  List<RelatedMetadataElement> relatedDataProfileLogAnnotations)
    {
        final String methodName = "processDataProfileLogAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedDataProfileLogAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedLogFile = null;
                List<RelatedMetadataElement> relatedAnnotationSubjects = this.syncAnnotation(databaseConnection,
                                                                                             surveyReportGUID,
                                                                                             relatedAnnotationElement.getElement(),
                                                                                             relatedAsset);
                int startFrom = 0;
                RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     1,
                                                                                                                     OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                    {
                        if (relatedMetadataElement != null)
                        {
                            relatedLogFile = relatedMetadataElement;
                            break;
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            1,
                                                                                            OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                if (relatedLogFile != null)
                {
                    Connector connector = connectorFactoryService.getConnectorForAsset(relatedLogFile.getElement().getElementGUID(), auditLog);

                    if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
                    {
                        Map<String, Integer> measurementValues = new HashMap<>();

                        for (long recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
                        {
                            List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                            if ((recordValues != null) && (recordValues.size() > 1))
                            {
                                try
                                {
                                    Integer measurementValue = Integer.parseInt(recordValues.get(1));

                                    measurementValues.put(recordValues.get(0), measurementValue);
                                }
                                catch (NumberFormatException notIntError)
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logException(methodName,
                                                              HarvestSurveysAuditCode.NOT_A_NUMBER.getMessageDefinition(connectorName,
                                                                                                                        recordValues.get(1),
                                                                                                                        Long.toString(recordNumber),
                                                                                                                        csvFileStoreConnector.getFileName(),
                                                                                                                        notIntError.getMessage()),
                                                              notIntError);
                                    }
                                }
                            }
                        }

                        for (RelatedMetadataElement relatedAnnotationSubject : relatedAnnotationSubjects)
                        {
                            syncValueProfile(databaseConnection,
                                             surveyReportGUID,
                                             relatedAnnotationElement.getElement(),
                                             relatedAnnotationSubject,
                                             measurementValues);
                        }
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Add details of data source measurements annotations to the database.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataSourceMeasurementsAnnotations list of request for action annotations
     */
    private void processDataSourceMeasurementsAnnotations(java.sql.Connection          databaseConnection,
                                                          String                       surveyReportGUID,
                                                          RelatedMetadataElement       relatedAsset,
                                                          List<RelatedMetadataElement> relatedDataSourceMeasurementsAnnotations)
    {
        final String methodName = "processDataSourceMeasurementsAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedDataSourceMeasurementsAnnotations)
        {
            try
            {
                List<RelatedMetadataElement> relatedAnnotationSubjects = this.syncAnnotation(databaseConnection,
                                                                                             surveyReportGUID,
                                                                                             relatedAnnotationElement.getElement(),
                                                                                             relatedAsset);



                for (RelatedMetadataElement relatedAnnotationSubject : relatedAnnotationSubjects)
                {
                    syncDataSourceMeasurements(databaseConnection,
                                               surveyReportGUID,
                                               relatedAnnotationElement.getElement(),
                                               relatedAnnotationSubject);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of request for action annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedOtherAnnotations list of request for action annotations
     */
    private void processOtherAnnotations(java.sql.Connection          databaseConnection,
                                         String                       surveyReportGUID,
                                         RelatedMetadataElement       relatedAsset,
                                         List<RelatedMetadataElement> relatedOtherAnnotations)
    {
        final String methodName = "processOtherAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedOtherAnnotations)
        {
            try
            {
                syncAnnotation(databaseConnection,
                               surveyReportGUID,
                               relatedAnnotationElement.getElement(),
                               relatedAsset);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                        error.getClass().getName(),
                                                                                                        methodName,
                                                                                                        error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add the value and type to a record used to insert a row into a table.
     *
     * @param openMetadataRecord map containing the column details
     * @param column column definition
     * @param value value of the column
     */
    private void addValueToRow(Map<String, JDBCDataValue> openMetadataRecord,
                               HarvestSurveysColumn       column,
                               Object                     value)
    {
        openMetadataRecord.put(column.getColumnName(), new JDBCDataValue(value, column.getColumnType().getJdbcType()));
    }


    /**
     * Add the value and type to a record used to insert a row into a table.
     *
     * @param openMetadataRecord map containing the column details
     * @param column column definition
     * @param value value of the column
     */
    private void addDateValueToRow(Map<String, JDBCDataValue> openMetadataRecord,
                                   HarvestSurveysColumn       column,
                                   Date                       value)
    {
        if (value != null)
        {
            addValueToRow(openMetadataRecord, column, new java.sql.Timestamp(value.getTime()));
        }
    }


    /**
     * Process information about a survey report.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportElement element describing the survey report
     * @param relatedAsset asset described in the report
     * @param relatedEngineAction details of the engine action that initiated the report
     */
    private void syncSurveyReport(java.sql.Connection    databaseConnection,
                                  OpenMetadataElement    surveyReportElement,
                                  RelatedMetadataElement relatedAsset,
                                  RelatedMetadataElement relatedEngineAction) throws ConnectorCheckedException
    {
        final String methodName = "syncSurveyReport";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getSurveyReportDataValues(surveyReportElement,
                                                                                           relatedAsset,
                                                                                           relatedEngineAction);

            databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.SURVEY_REPORT.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Convert the description of a specific survey report into columns for the sr_report table.
     *
     * @param surveyReportElement element describing the survey report
     * @param relatedAsset asset described in the report
     * @param relatedEngineAction details of the engine action that initiated the report
     * @return columns
     */
    private Map<String, JDBCDataValue> getSurveyReportDataValues(OpenMetadataElement    surveyReportElement,
                                                                 RelatedMetadataElement relatedAsset,
                                                                 RelatedMetadataElement relatedEngineAction)
    {
        final String methodName = "getSurveyReportDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.QUALIFIED_NAME.name, surveyReportElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.DISPLAY_NAME.name, surveyReportElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.DESCRIPTION.name, surveyReportElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.PURPOSE, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.PURPOSE.name, surveyReportElement.getElementProperties(), methodName));
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.START_TIMESTAMP, propertyHelper.getDateProperty(connectorName, OpenMetadataProperty.START_DATE.name, surveyReportElement.getElementProperties(), methodName));
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.END_TIMESTAMP, propertyHelper.getDateProperty(connectorName, OpenMetadataProperty.COMPLETION_DATE.name, surveyReportElement.getElementProperties(), methodName));

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ASSET_GUID, relatedAsset.getElement().getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ASSET_TYPE_NAME, relatedAsset.getElement().getType().getTypeName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, relatedAsset.getElement().getOrigin().getHomeMetadataCollectionId());

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ENGINE_ACTION_GUID, relatedEngineAction.getElement().getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.INITIATOR, relatedEngineAction.getElement().getVersions().getCreatedBy());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.REQUEST_TYPE, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.REQUEST_TYPE.name, relatedEngineAction.getElement().getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.GOVERNANCE_ENGINE_NAME, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name, relatedEngineAction.getElement().getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ENGINE_HOST_USER_ID, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.PROCESSING_ENGINE_USER_ID.name, relatedEngineAction.getElement().getElementProperties(), methodName));

        return openMetadataRecord;
    }


    /**
     * Process information about an annotation.  A row is added to the annotation for each subject
     * (element linked with AssociatedAnnotation).
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param annotationElement element describing the annotation
     * @param relatedAsset asset described in the report
     * @return list of the associated subject elements for the annotation
     */
    private List<RelatedMetadataElement> syncAnnotation(java.sql.Connection    databaseConnection,
                                                        String                 surveyReportGUID,
                                                        OpenMetadataElement    annotationElement,
                                                        RelatedMetadataElement relatedAsset) throws ConnectorCheckedException
    {
        final String methodName = "syncAnnotation";

        try
        {
            List<RelatedMetadataElement> relatedAnnotationSubjects = new ArrayList<>();
            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(annotationElement.getElementGUID(),
                                                                                                                 2,
                                                                                                                 OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                                 startFrom,
                                                                                                                 openMetadataAccess.getMaxPagingSize());
            while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        relatedAnnotationSubjects.add(relatedMetadataElement);
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(annotationElement.getElementGUID(),
                                                                                        2,
                                                                                        OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                        startFrom,
                                                                                        openMetadataAccess.getMaxPagingSize());
            }

            if (relatedAnnotationSubjects.isEmpty())
            {
                relatedAnnotationSubjects.add(relatedAsset);
            }

            for (RelatedMetadataElement relatedAnnotationSubject : relatedAnnotationSubjects)
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getAnnotationDataValues(surveyReportGUID, annotationElement, relatedAnnotationSubject);

                databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.ANNOTATION.getTableName(), openMetadataRecord);
            }

            return relatedAnnotationSubjects;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Convert the description of a specific annotation into columns for the sr_annotation table.
     *
     * @param annotationElement element describing the annotation
     * @param relatedAnnotationSubject element that the annotation refers to
     * @return columns
     */
    private Map<String, JDBCDataValue> getAnnotationDataValues(String                 surveyReportGUID,
                                                               OpenMetadataElement    annotationElement,
                                                               RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "getAnnotationDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.OPEN_METADATA_TYPE, annotationElement.getType().getTypeName());

        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, annotationElement.getVersions().getCreateTime());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_TYPE, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.ANNOTATION_TYPE.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUMMARY, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.SUMMARY.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.EXPLANATION, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.EXPLANATION.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.EXPRESSION, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.EXPRESSION.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.CONFIDENCE_LEVEL, propertyHelper.getIntProperty(connectorName, OpenMetadataProperty.CONFIDENCE_LEVEL.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.JSON_PROPERTIES, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.JSON_PROPERTIES.name, annotationElement.getElementProperties(), methodName));

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, relatedAnnotationSubject.getElement().getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, relatedAnnotationSubject.getElement().getType().getTypeName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, relatedAnnotationSubject.getElement().getOrigin().getHomeMetadataCollectionId());

        return openMetadataRecord;
    }


    /**
     * Process information about a request for action annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier for the survey report
     * @param requestForActionAnnotation description of the annotation
     * @param relatedActionTargets details of the elements that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncRequestForAction(java.sql.Connection          databaseConnection,
                                      String                       surveyReportGUID,
                                      OpenMetadataElement          requestForActionAnnotation,
                                      List<RelatedMetadataElement> relatedActionTargets,
                                      RelatedMetadataElement       relatedAnnotationSubject) throws ConnectorCheckedException
    {
        final String methodName = "syncRequestForAction";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getRequestForActionDataValues(surveyReportGUID,
                                                                                               requestForActionAnnotation,
                                                                                               relatedAnnotationSubject);

            databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.REQUEST_FOR_ACTION.getTableName(), openMetadataRecord);

            String annotationType = propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.ANNOTATION_TYPE.name, requestForActionAnnotation.getElementProperties(), methodName);

            if (relatedActionTargets != null)
            {
                for (RelatedMetadataElement relatedActionTarget : relatedActionTargets)
                {
                    openMetadataRecord = this.getRequestForActionTargetDataValues(surveyReportGUID,
                                                                                  requestForActionAnnotation,
                                                                                  relatedAnnotationSubject,
                                                                                  relatedActionTarget);

                    databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.REQUEST_FOR_ACTION_TARGET.getTableName(), openMetadataRecord);
                }

                if (SurveyFolderAnnotationType.MISSING_REF_DATA.getName().equals(annotationType))
                {
                    for (RelatedMetadataElement relatedActionTarget : relatedActionTargets)
                    {
                        processFileClassifiers(databaseConnection, surveyReportGUID, relatedActionTarget);
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
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }

    }


    /**
     * Convert the description of a specific request for action annotation into columns for the sr_request_for_action table.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param annotationElement description of the annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @return columns
     */
    private Map<String, JDBCDataValue> getRequestForActionDataValues(String                 surveyReportGUID,
                                                                     OpenMetadataElement    annotationElement,
                                                                     RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "getRequestForActionDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, relatedAnnotationSubject.getElement().getElementGUID());

        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, annotationElement.getVersions().getCreateTime());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTION_REQUEST_NAME, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.ACTION_REQUESTED.name, annotationElement.getElementProperties(), methodName));

        return openMetadataRecord;
    }



    /**
     * Convert the description of a specific request ofr action annotation into columns for the sr_request_for_action table.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param annotationElement description of the annotation
     * @param relatedActionTarget details of the element that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     * @return columns
     */
    private Map<String, JDBCDataValue> getRequestForActionTargetDataValues(String                 surveyReportGUID,
                                                                           OpenMetadataElement    annotationElement,
                                                                           RelatedMetadataElement relatedActionTarget,
                                                                           RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "getRequestForActionDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTION_REQUEST_GUID, relatedActionTarget.getRelationshipGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, relatedAnnotationSubject.getElement().getElementGUID());

        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, annotationElement.getVersions().getCreateTime());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTION_REQUEST_NAME, propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.ACTION_REQUESTED.name, annotationElement.getElementProperties(), methodName));
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTION_TARGET_GUID, relatedActionTarget.getElement().getElementGUID());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTION_TARGET_TYPE, relatedActionTarget.getElement().getType().getTypeName());

        return openMetadataRecord;
    }


    /**
     * Add details of the missing reference data to the database.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param actionTargetElement  details about the file that contains the missing reference data
     */
    private void processFileClassifiers(java.sql.Connection    databaseConnection,
                                        String                 surveyReportGUID,
                                        RelatedMetadataElement actionTargetElement) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           ConnectionCheckedException,
                                                                                           ConnectorCheckedException
    {
        Connector connector = connectorFactoryService.getConnectorForAsset(actionTargetElement.getElement().getElementGUID(), auditLog);

        if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
        {
            for (long recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
            {
                List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                if ((recordValues != null) && (recordValues.size() > 1))
                {
                    syncFileClassifiers(databaseConnection,
                                        surveyReportGUID,
                                        recordValues.get(0),
                                        recordValues.get(1),
                                        recordValues.get(2),
                                        recordValues.get(3),
                                        recordValues.get(4),
                                        recordValues.get(5),
                                        recordValues.get(6));
                }
            }
        }
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param fileName  details about the file that contains the missing reference data
     * @param fileExtension  details about the file that contains the missing reference data
     * @param pathName  details about the file that contains the missing reference data
     * @param fileType  details about the file that contains the missing reference data
     * @param assetTypeName  details about the file that contains the missing reference data
     * @param deployedImplementationType  details about the file that contains the missing reference data
     * @param encoding  details about the file that contains the missing reference data
     */
    private void syncFileClassifiers(java.sql.Connection databaseConnection,
                                     String              surveyReportGUID,
                                     String              fileName,
                                     String              fileExtension,
                                     String              pathName,
                                     String              fileType,
                                     String              assetTypeName,
                                     String              deployedImplementationType,
                                     String              encoding) throws ConnectorCheckedException
    {
        final String methodName = "syncFileClassifiers";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        HarvestSurveysTable.MISSING_FILE_CLASSIFIERS.getTableName(),
                                                                                        HarvestSurveysColumn.PATHNAME.getColumnName(),
                                                                                        null,
                                                                                        HarvestSurveysColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestSurveysTable.MISSING_FILE_CLASSIFIERS.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getFileClassifierDataValues(surveyReportGUID,
                                                                                             fileName,
                                                                                             fileExtension,
                                                                                             pathName,
                                                                                             fileType,
                                                                                             assetTypeName,
                                                                                             deployedImplementationType,
                                                                                             encoding);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestSurveysColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.MISSING_FILE_CLASSIFIERS.getTableName(), openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param fileName  details about the file that contains the missing reference data
     * @param fileExtension  details about the file that contains the missing reference data
     * @param pathName  details about the file that contains the missing reference data
     * @param fileType  details about the file that contains the missing reference data
     * @param assetType  details about the file that contains the missing reference data
     * @param deployedImplementationType  details about the file that contains the missing reference data
     * @param encoding  details about the file that contains the missing reference data
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileClassifierDataValues(String surveyReportGUID,
                                                                   String fileName,
                                                                   String fileExtension,
                                                                   String pathName,
                                                                   String fileType,
                                                                   String assetType,
                                                                   String deployedImplementationType,
                                                                   String encoding)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_NAME, fileName);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_EXTENSION, fileExtension);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.PATHNAME, pathName);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_TYPE, fileType);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ASSET_TYPE_NAME, assetType);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DEPLOYED_IMPLEMENTATION_TYPE, deployedImplementationType);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ENCODING, encoding);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process information about a data profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncResourceProfile(java.sql.Connection    databaseConnection,
                                     String                 surveyReportGUID,
                                     OpenMetadataElement    dataProfileAnnotation,
                                     RelatedMetadataElement relatedAnnotationSubject) throws ConnectorCheckedException
    {
        final String methodName = "syncResourceProfile";

        try
        {
            Map<String, Integer> valueCount = propertyHelper.getIntegerMapFromProperty(connectorName,
                                                                                       OpenMetadataProperty.VALUE_COUNT.name,
                                                                                       dataProfileAnnotation.getElementProperties(),
                                                                                       methodName);

            if (valueCount != null)
            {
                syncValueProfile(databaseConnection,
                                 surveyReportGUID,
                                 dataProfileAnnotation,
                                 relatedAnnotationSubject,
                                 valueCount);
            }
            else
            {
                Map<String, Long> profileCount = propertyHelper.getLongMapFromProperty(connectorName,
                                                                                       OpenMetadataProperty.PROFILE_COUNTS.name,
                                                                                       dataProfileAnnotation.getElementProperties(),
                                                                                       methodName);

                syncResourceProfile(databaseConnection,
                                    surveyReportGUID,
                                    dataProfileAnnotation,
                                    relatedAnnotationSubject,
                                    profileCount);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }



    /**
     * Process information about a resource profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @param measurementValues map of measurement categories to measurement counts
     */
    private void syncValueProfile(java.sql.Connection    databaseConnection,
                                  String                 surveyReportGUID,
                                  OpenMetadataElement    dataProfileAnnotation,
                                  RelatedMetadataElement relatedAnnotationSubject,
                                  Map<String, Integer>   measurementValues) throws ConnectorCheckedException
    {
        final String methodName = "syncValueProfile";

        try
        {
            String subjectGUID = null;
            String subjectType = null;

            if (relatedAnnotationSubject != null)
            {
                subjectGUID = relatedAnnotationSubject.getElement().getElementGUID();
                subjectType = relatedAnnotationSubject.getElement().getType().getTypeName();
            }

            String measurementName = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                      dataProfileAnnotation.getElementProperties(),
                                                                      methodName);

            if (measurementValues != null)
            {
                for (String valueCountName : measurementValues.keySet())
                {
                    int measurementValue = 0;

                    if (measurementValues.get(valueCountName) != null)
                    {
                        measurementValue = measurementValues.get(valueCountName);
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getValueProfileDataValues(surveyReportGUID,
                                                                                                   subjectGUID,
                                                                                                   subjectType,
                                                                                                   dataProfileAnnotation.getElementGUID(),
                                                                                                   dataProfileAnnotation.getVersions().getCreateTime(),
                                                                                                   dataProfileAnnotation.getOrigin().getHomeMetadataCollectionId(),
                                                                                                   measurementName,
                                                                                                   valueCountName,
                                                                                                   measurementValue);

                    databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.PROFILE_MEASURES.getTableName(), openMetadataRecord);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process information about a resource profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @param measurementValues map of measurement categories to measurement counts
     */
    private void syncResourceProfile(java.sql.Connection    databaseConnection,
                                     String                 surveyReportGUID,
                                     OpenMetadataElement    dataProfileAnnotation,
                                     RelatedMetadataElement relatedAnnotationSubject,
                                     Map<String, Long>      measurementValues) throws ConnectorCheckedException
    {
        final String methodName = "syncResourceProfile";

        try
        {
            String subjectGUID = null;
            String subjectType = null;
            String subjectMetadataCollectionId = null;

            if (relatedAnnotationSubject != null)
            {
                subjectGUID = relatedAnnotationSubject.getElement().getElementGUID();
                subjectType = relatedAnnotationSubject.getElement().getType().getTypeName();
                subjectMetadataCollectionId = relatedAnnotationSubject.getElement().getOrigin().getHomeMetadataCollectionId();
            }

            String measurementName = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                      dataProfileAnnotation.getElementProperties(),
                                                                      methodName);

            if (measurementValues != null)
            {
                for (String valueCountName : measurementValues.keySet())
                {
                    long measurementValue = 0;

                    if (measurementValues.get(valueCountName) != null)
                    {
                        measurementValue = measurementValues.get(valueCountName);
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getResourceProfileDataValues(surveyReportGUID,
                                                                                                      subjectGUID,
                                                                                                      subjectType,
                                                                                                      subjectMetadataCollectionId,
                                                                                                      dataProfileAnnotation.getElementGUID(),
                                                                                                      dataProfileAnnotation.getVersions().getCreateTime(),
                                                                                                      measurementName,
                                                                                                      valueCountName,
                                                                                                      measurementValue);

                    databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.PROFILE_MEASURES.getTableName(), openMetadataRecord);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Convert the description of an annotation into columns for the data profile table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param metadataCollectionId home
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @return columns
     */
    private Map<String, JDBCDataValue> getValueProfileDataValues(String                 surveyReportGUID,
                                                                 String                 subjectGUID,
                                                                 String                 subjectType,
                                                                 String                 annotationGUID,
                                                                 Date                   creationTime,
                                                                 String                 metadataCollectionId,
                                                                 String                 measurementName,
                                                                 String                 measurementCategory,
                                                                 int                    measurementValue)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, metadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_NAME, measurementName);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_CATEGORY, measurementCategory);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_NUMERIC_VALUE, measurementValue);

        return openMetadataRecord;
    }


    /**
     * Convert the description of an annotation into columns for the data profile table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId home of described element
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @return columns
     */
    private Map<String, JDBCDataValue> getResourceProfileDataValues(String                 surveyReportGUID,
                                                                    String                 subjectGUID,
                                                                    String                 subjectType,
                                                                    String                 subjectMetadataCollectionId,
                                                                    String                 annotationGUID,
                                                                    Date                   creationTime,
                                                                    String                 measurementName,
                                                                    String                 measurementCategory,
                                                                    long                   measurementValue)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_NAME, measurementName);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_CATEGORY, measurementCategory);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_NUMERIC_VALUE, measurementValue);

        return openMetadataRecord;
    }


    /**
     * Process information about a data source measurements annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param databaseConnection  connection to the database
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataSourceMeasurementsAnnotation element representing the data source measurements annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncDataSourceMeasurements(java.sql.Connection    databaseConnection,
                                            String                 surveyReportGUID,
                                            OpenMetadataElement    dataSourceMeasurementsAnnotation,
                                            RelatedMetadataElement relatedAnnotationSubject) throws ConnectorCheckedException
    {
        final String methodName = "syncDataSourceMeasurements";

        try
        {
            String subjectGUID = null;
            String subjectType = null;
            String subjectMetadataCollectionId = null;

            if (relatedAnnotationSubject != null)
            {
                subjectGUID = relatedAnnotationSubject.getElement().getElementGUID();
                subjectType = relatedAnnotationSubject.getElement().getType().getTypeName();
                subjectMetadataCollectionId = relatedAnnotationSubject.getElement().getOrigin().getHomeMetadataCollectionId();
            }

            String measurementName = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                      dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                      methodName);

            String jsonProperties = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.JSON_PROPERTIES.name,
                                                                      dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                      methodName);

            if (jsonProperties != null)
            {
                if (SurveyFileAnnotationType.MEASUREMENTS.getName().equals(measurementName))
                {
                    try
                    {
                        FileMeasurement fileMeasurement = OBJECT_READER.readValue(jsonProperties, FileMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getFileMeasurementsDataValues(surveyReportGUID,
                                                                                                           subjectGUID,
                                                                                                           subjectType,
                                                                                                           subjectMetadataCollectionId,
                                                                                                           dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                           dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                           fileMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.FILE_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
                else if (SurveyFolderAnnotationType.MEASUREMENTS.getName().equals(measurementName))
                {
                    try
                    {
                        FileDirectoryMeasurement fileDirectoryMeasurement = OBJECT_READER.readValue(jsonProperties, FileDirectoryMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getFileDirectoryMeasurementsDataValues(surveyReportGUID,
                                                                                                                    subjectGUID,
                                                                                                                    subjectType,
                                                                                                                    subjectMetadataCollectionId,
                                                                                                                    dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                    dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                    fileDirectoryMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.DIRECTORY_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
                else if ((UnityCatalogAnnotationType.CATALOG_METRICS.getName().equals(measurementName)) ||
                         (SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName().equals(measurementName)))
                {
                    try
                    {
                        RelationalDataManagerMeasurement relationalSchemaMeasurement = OBJECT_READER.readValue(jsonProperties, RelationalDataManagerMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getRelationalDataManagerMeasurementsDataValues(surveyReportGUID,
                                                                                                                            subjectGUID,
                                                                                                                            subjectType,
                                                                                                                            subjectMetadataCollectionId,
                                                                                                                            dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                            dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                            relationalSchemaMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.RELATIONAL_DATA_MANAGER_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
                else if (SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getName().equals(measurementName))
                {
                    try
                    {
                        RelationalSchemaMeasurement relationalSchemaMeasurement = OBJECT_READER.readValue(jsonProperties, RelationalSchemaMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getRelationalSchemaMeasurementsDataValues(surveyReportGUID,
                                                                                                                       subjectGUID,
                                                                                                                       subjectType,
                                                                                                                       subjectMetadataCollectionId,
                                                                                                                       dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                       dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                       relationalSchemaMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.RELATIONAL_SCHEMA_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
                else if (SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getName().equals(measurementName))
                {
                    try
                    {
                        RelationalTableMeasurement relationalTableMeasurement = OBJECT_READER.readValue(jsonProperties, RelationalTableMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getRelationalTableMeasurementsDataValues(surveyReportGUID,
                                                                                                                            subjectGUID,
                                                                                                                            subjectType,
                                                                                                                            subjectMetadataCollectionId,
                                                                                                                            dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                            dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                            relationalTableMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.RELATIONAL_TABLE_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
                else if (SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getName().equals(measurementName))
                {
                    try
                    {
                        RelationalColumnMeasurement relationalColumnMeasurement = OBJECT_READER.readValue(jsonProperties, RelationalColumnMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getRelationalColumnMeasurementsDataValues(surveyReportGUID,
                                                                                                                       subjectGUID,
                                                                                                                       subjectType,
                                                                                                                       subjectMetadataCollectionId,
                                                                                                                       dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                       dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                       relationalColumnMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.RELATIONAL_COLUMN_MEASUREMENTS.getTableName(), openMetadataRecord);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                              error);

                        throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              methodName,
                                                                                                                              error.getMessage()),
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error);
                    }
                }
            }

            Map<String, String> resourceProperties = propertyHelper.getStringMapFromProperty(connectorName,
                                                                                             OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                                                             dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                                             methodName);

            Date resourceCreateTime = propertyHelper.getDateProperty(connectorName,
                                                                     OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                                     dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                     methodName);

            Date resourceUpdateTime = propertyHelper.getDateProperty(connectorName,
                                                                     OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                                     dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                     methodName);
            Date resourceLastAccessTime = propertyHelper.getDateProperty(connectorName,
                                                                         OpenMetadataProperty.RESOURCE_LAST_ACCESSED_TIME.name,
                                                                         dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                         methodName);

            long resourceSize = propertyHelper.getLongProperty(connectorName,
                                                               OpenMetadataProperty.SIZE.name,
                                                               dataSourceMeasurementsAnnotation.getElementProperties(),
                                                               methodName);

            String encoding = propertyHelper.getStringProperty(connectorName,
                                                               OpenMetadataProperty.ENCODING.name,
                                                               dataSourceMeasurementsAnnotation.getElementProperties(),
                                                               methodName);

            if (resourceProperties != null)
            {
                for (String resourcePropertyName : resourceProperties.keySet())
                {
                    Integer measurementValue = null;

                    if (resourceProperties.get(resourcePropertyName) != null)
                    {
                        try
                        {
                            measurementValue = Integer.parseInt(resourceProperties.get(resourcePropertyName));
                        }
                        catch (NumberFormatException notNumber)
                        {
                            // ignore
                        }
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getDataSourceMeasurementsDataValues(surveyReportGUID,
                                                                                                             subjectGUID,
                                                                                                             subjectType,
                                                                                                             dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                             dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                             measurementName,
                                                                                                             resourcePropertyName,
                                                                                                             measurementValue,
                                                                                                             resourceProperties.get(resourcePropertyName),
                                                                                                             resourceCreateTime,
                                                                                                             resourceUpdateTime,
                                                                                                             resourceLastAccessTime,
                                                                                                             resourceSize,
                                                                                                             encoding);

                    databaseClient.insertRowIntoTable(databaseConnection, HarvestSurveysTable.RESOURCE_MEASURES.getTableName(), openMetadataRecord);
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Convert the description of an annotation into columns for the data source measurements table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @param measurementDisplayName string version fo measurement value (may not be a number)
     * @param resourceCreationTime time resource create (only for physical measurement annotation
     * @param resourceModificationTime time resource last modified (only for physical measurement annotation
     * @param resourceSize size of resource in bytes (only for physical measurement annotation
     * @param resourceLastAccessTime last access time
     * @param encoding encoding
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataSourceMeasurementsDataValues(String                 surveyReportGUID,
                                                                           String                 subjectGUID,
                                                                           String                 subjectType,
                                                                           String                 annotationGUID,
                                                                           Date                   creationTime,
                                                                           String                 measurementName,
                                                                           String                 measurementCategory,
                                                                           Integer                measurementValue,
                                                                           String                 measurementDisplayName,
                                                                           Date                   resourceCreationTime,
                                                                           Date                   resourceModificationTime,
                                                                           Date                   resourceLastAccessTime,
                                                                           long                   resourceSize,
                                                                           String                 encoding)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_NAME, measurementName);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_CATEGORY, measurementCategory);
        if (measurementValue != null)
        {
            addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_VALUE, measurementValue);
        }
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MEASUREMENT_DISPLAY_NAME, measurementDisplayName);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_CREATION_TIME, resourceCreationTime);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_MODIFIED_TIME, resourceModificationTime);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_ACCESSED_TIME, resourceLastAccessTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_SIZE, resourceSize);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ENCODING, encoding);

        return openMetadataRecord;
    }



    /**
     * Convert the description of an annotation into columns for the file measurements table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param fileMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileMeasurementsDataValues(String                 surveyReportGUID,
                                                                     String                 subjectGUID,
                                                                     String                 subjectType,
                                                                     String                 subjectMetadataCollectionId,
                                                                     String                 annotationGUID,
                                                                     Date                   creationTime,
                                                                     FileMeasurement        fileMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_NAME, fileMeasurement.getFileName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.PATHNAME, fileMeasurement.getPathName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_EXTENSION, fileMeasurement.getFileExtension());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_TYPE, fileMeasurement.getFileType());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DEPLOYED_IMPLEMENTATION_TYPE_COUNT, fileMeasurement.getDeployedImplementationType());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ENCODING, fileMeasurement.getEncoding());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ASSET_TYPE_NAME, fileMeasurement.getAssetTypeName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.CAN_READ, fileMeasurement.getCanRead());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.CAN_WRITE, fileMeasurement.getCanWrite());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.CAN_EXECUTE, fileMeasurement.getCanExecute());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.IS_SYM_LINK, fileMeasurement.getSymLink());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.IS_HIDDEN, fileMeasurement.getHidden());

        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_CREATION_TIME, fileMeasurement.getCreationTime());
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_MODIFIED_TIME, fileMeasurement.getLastModifiedTime());
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_ACCESSED_TIME, fileMeasurement.getLastAccessedTime());

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_SIZE, fileMeasurement.getFileSize());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RECORD_COUNT, fileMeasurement.getRecordCount());

        return openMetadataRecord;
    }


    /**
     * Convert the description of an annotation into columns for the file measurements table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param fileDirectoryMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileDirectoryMeasurementsDataValues(String                    surveyReportGUID,
                                                                              String                    subjectGUID,
                                                                              String                    subjectType,
                                                                              String                    subjectMetadataCollectionId,
                                                                              String                    annotationGUID,
                                                                              Date                      creationTime,
                                                                              FileDirectoryMeasurement  fileDirectoryMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.PATHNAME, fileDirectoryMeasurement.getDirectoryName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_COUNT, fileDirectoryMeasurement.getFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.TOTAL_FILE_SIZE, fileDirectoryMeasurement.getTotalFileSize());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUB_DIRECTORY_COUNT, fileDirectoryMeasurement.getSubDirectoryCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.READABLE_FILE_COUNT, fileDirectoryMeasurement.getReadableFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.WRITEABLE_FILE_COUNT, fileDirectoryMeasurement.getWriteableFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.EXECUTABLE_FILE_COUNT, fileDirectoryMeasurement.getExecutableFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SYM_LINK_FILE_COUNT, fileDirectoryMeasurement.getSymLinkFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.HIDDEN_FILE_COUNT, fileDirectoryMeasurement.getHiddenFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_NAME_COUNT, fileDirectoryMeasurement.getFileNameCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_EXTENSION_COUNT, fileDirectoryMeasurement.getFileExtensionCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.FILE_TYPE_COUNT, fileDirectoryMeasurement.getFileTypeCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ASSET_TYPE_COUNT, fileDirectoryMeasurement.getAssetTypeCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DEPLOYED_IMPLEMENTATION_TYPE_COUNT, fileDirectoryMeasurement.getDeployedImplementationTypeCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.UNCLASSIFIED_FILE_COUNT, fileDirectoryMeasurement.getUnclassifiedFileCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.INACCESSIBLE_FILE_COUNT, fileDirectoryMeasurement.getInaccessibleFileCount());

        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_FILE_CREATION_TIME, fileDirectoryMeasurement.getLastFileCreationTime());
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_FILE_MODIFICATION_TIME, fileDirectoryMeasurement.getLastFileModificationTime());
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_FILE_ACCESSED_TIME, fileDirectoryMeasurement.getLastFileAccessedTime());

        return openMetadataRecord;
    }



    /**
     * Convert the description of an annotation into columns for the data manager summary table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param relationalDataManagerMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getRelationalDataManagerMeasurementsDataValues(String                           surveyReportGUID,
                                                                                      String                           subjectGUID,
                                                                                      String                           subjectType,
                                                                                      String                           subjectMetadataCollectionId,
                                                                                      String                           annotationGUID,
                                                                                      Date                             creationTime,
                                                                                      RelationalDataManagerMeasurement relationalDataManagerMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_NAME, relationalDataManagerMeasurement.getResourceName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SCHEMA_COUNT, relationalDataManagerMeasurement.getSchemaCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.TABLE_COUNT, relationalDataManagerMeasurement.getTableCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.VIEW_COUNT, relationalDataManagerMeasurement.getViewCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MAT_VIEW_COUNT, relationalDataManagerMeasurement.getMaterializedViewCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.COLUMN_COUNT, relationalDataManagerMeasurement.getColumnCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DATA_SIZE, relationalDataManagerMeasurement.getSize());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_FETCHED, relationalDataManagerMeasurement.getRowsFetched());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_INSERTED, relationalDataManagerMeasurement.getRowsInserted());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_UPDATED, relationalDataManagerMeasurement.getRowsUpdated());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_DELETED, relationalDataManagerMeasurement.getRowsDeleted());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SESSION_TIME, relationalDataManagerMeasurement.getSessionTime());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ACTIVE_TIME, relationalDataManagerMeasurement.getActiveTime());
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.LAST_STATS_RESET, relationalDataManagerMeasurement.getStatsReset());

        return openMetadataRecord;
    }


    /**
     * Convert the description of an annotation into columns for the relational schema summary table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param relationalSchemaMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getRelationalSchemaMeasurementsDataValues(String                      surveyReportGUID,
                                                                                 String                      subjectGUID,
                                                                                 String                      subjectType,
                                                                                 String                      subjectMetadataCollectionId,
                                                                                 String                      annotationGUID,
                                                                                 Date                        creationTime,
                                                                                 RelationalSchemaMeasurement relationalSchemaMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_NAME, relationalSchemaMeasurement.getQualifiedSchemaName());

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DISPLAY_NAME, relationalSchemaMeasurement.getSchemaName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.TABLE_COUNT, relationalSchemaMeasurement.getTableCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.VIEW_COUNT, relationalSchemaMeasurement.getViewCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MAT_VIEW_COUNT, relationalSchemaMeasurement.getMaterializedViewCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.COLUMN_COUNT, relationalSchemaMeasurement.getColumnCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DATA_SIZE, relationalSchemaMeasurement.getTotalTableSize());

        return openMetadataRecord;
    }


    /**
     * Convert the description of an annotation into columns for the relational table summary table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param relationalTableMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getRelationalTableMeasurementsDataValues(String                     surveyReportGUID,
                                                                                String                     subjectGUID,
                                                                                String                     subjectType,
                                                                                String                     subjectMetadataCollectionId,
                                                                                String                     annotationGUID,
                                                                                Date                       creationTime,
                                                                                RelationalTableMeasurement relationalTableMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_NAME, relationalTableMeasurement.getQualifiedTableName());

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DISPLAY_NAME, relationalTableMeasurement.getTableName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DATA_SIZE, relationalTableMeasurement.getTableSize());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.TABLE_TYPE, relationalTableMeasurement.getTableType());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.COLUMN_COUNT, relationalTableMeasurement.getColumnCount());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_INSERTED, relationalTableMeasurement.getNumberOfRowsInserted());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_UPDATED, relationalTableMeasurement.getNumberOfRowsUpdated());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ROWS_DELETED, relationalTableMeasurement.getNumberOfRowsDeleted());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_OWNER, relationalTableMeasurement.getTableOwner());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.IS_POPULATED, relationalTableMeasurement.getIsPopulated());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.HAS_INDEXES, relationalTableMeasurement.getHasIndexes());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.HAS_RULES, relationalTableMeasurement.getHasRules());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.HAS_TRIGGERS, relationalTableMeasurement.getHasTriggers());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.HAS_ROW_SECURITY, relationalTableMeasurement.getHasRowSecurity());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.QUERY_DEFINITION, relationalTableMeasurement.getQueryDefinition());

        return openMetadataRecord;
    }


    /**
     * Convert the description of an annotation into columns for the relational column summary table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param subjectMetadataCollectionId  home
     * @param relationalColumnMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getRelationalColumnMeasurementsDataValues(String                      surveyReportGUID,
                                                                                 String                      subjectGUID,
                                                                                 String                      subjectType,
                                                                                 String                      subjectMetadataCollectionId,
                                                                                 String                      annotationGUID,
                                                                                 Date                        creationTime,
                                                                                 RelationalColumnMeasurement relationalColumnMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SURVEY_REPORT_GUID, surveyReportGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_GUID, subjectGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.ANNOTATION_GUID, annotationGUID);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.RESOURCE_NAME, relationalColumnMeasurement.getQualifiedColumnName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.METADATA_COLLECTION_ID, subjectMetadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.SUBJECT_TYPE, subjectType);
        addDateValueToRow(openMetadataRecord, HarvestSurveysColumn.CREATION_TIME, creationTime);
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DISPLAY_NAME, relationalColumnMeasurement.getColumnName());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DATA_TYPE, relationalColumnMeasurement.getColumnDataType());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.DATA_SIZE, relationalColumnMeasurement.getColumnSize());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.NOT_NULL, relationalColumnMeasurement.getColumnNotNull());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.AVERAGE_WIDTH, relationalColumnMeasurement.getAverageColumnWidth());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.NUMBER_OF_DISTINCT_VALUES, relationalColumnMeasurement.getNumberOfDistinctValues());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MOST_COMMON_VALUES, relationalColumnMeasurement.getMostCommonValues());
        addValueToRow(openMetadataRecord, HarvestSurveysColumn.MOST_COMMON_VALUES_FREQUENCY, relationalColumnMeasurement.getMostCommonValuesFrequency());

        return openMetadataRecord;
    }


    /**
     * Compare the two records and return true if the information from the open metadata ecosystem is
     * different from the information stored in the database.  Any discrepancy
     * found results in a return of true.
     *
     * @param latestStoredRecord record from the database
     * @param openMetadataRecord record from the open metadata ecosystem
     * @param ignoreProperty property to be skipped - typically called sync_time
     * @return boolean flag
     */
    private boolean newInformation(Map<String, JDBCDataValue> latestStoredRecord,
                                   Map<String, JDBCDataValue> openMetadataRecord,
                                   String                     ignoreProperty)
    {
        if (latestStoredRecord == null)
        {
            return true;
        }

        for (String columnName : latestStoredRecord.keySet())
        {
            /*
             * Skip the property if it is to be ignored
             */
            if ((ignoreProperty == null) || (! ignoreProperty.equals(columnName)))
            {
                if (latestStoredRecord.get(columnName) != null)
                {
                    /*
                     * Something is stored in the database - does it match?
                     */
                    JDBCDataValue latestStoredDataValue = latestStoredRecord.get(columnName);
                    JDBCDataValue openMetadataDataValue = openMetadataRecord.get(columnName);

                    if ((openMetadataDataValue == null) || (! openMetadataDataValue.equals(latestStoredDataValue)))
                    {
                        return true;
                    }
                }
                else if (openMetadataRecord.get(columnName) != null)
                {
                    /*
                     * There is no value stored in the database. Is something stored in open metadata?
                     */
                    JDBCDataValue dataValue = openMetadataRecord.get(columnName);

                    if (dataValue.getDataValue() != null)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        final String methodName = "processEvent";

        java.sql.Connection databaseConnection = null;

        try
        {
            OpenMetadataElement surveyReportElement = openMetadataAccess.getMetadataElementByGUID(event.getElementHeader().getGUID());

            if (surveyReportElement != null)
            {
                databaseConnection = databaseClient.getDataSource().getConnection();

                processSurveyReport(databaseConnection, surveyReportElement);

                databaseConnection.commit();
            }
        }
        catch (Exception error)
        {
            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  rollbackError)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            rollbackError.getClass().getName(),
                                                                                                            methodName,
                                                                                                            rollbackError.getMessage()),
                                          error);
                }
            }

            auditLog.logException(methodName,
                                  HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);
        }
    }
}
