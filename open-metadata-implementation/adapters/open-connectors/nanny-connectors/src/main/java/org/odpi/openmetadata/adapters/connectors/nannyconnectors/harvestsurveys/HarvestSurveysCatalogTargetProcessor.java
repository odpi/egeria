/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.FileDirectoryMeasurement;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.FileMeasurement;
import org.odpi.openmetadata.integrationservices.catalog.connector.ConnectorFactoryService;

import java.sql.Types;
import java.util.*;


/**
 * HarvestSurveysConnector extracts relevant metadata from the open metadata ecosystem into the Observations database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by Asset Manager OMAS/Catalog Integrator OMIS.
 */
public class HarvestSurveysCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    /*
     * Names of the database tables
     */
    private static final String missingFileClassifiersDatabaseTable = "sr_missing_file_classifiers";
    private static final String surveyReportDatabaseTable           = "sr_report";
    private static final String resourceMeasurementDatabaseTable    = "sr_resource_measurement";
    private static final String profileMeasuresDatabaseTable        = "sr_profile_measures";
    private static final String requestForActionDatabaseTable       = "sr_request_for_action";
    private static final String fileMeasurementsDatabaseTable       = "sr_file_measurements";
    private static final String folderMeasurementsDatabaseTable     = "sr_folder_measurements";
    private static final String databaseMeasurementsDatabaseTable   = "sr_database_measurements";


    /*
     * Column names
     */
    private static final String columnNameSyncTime                   = "sync_time";
    private static final String columnNameDisplayName                = "display_name";
    private static final String columnNameAssetGUID                  = "asset_guid";
    private static final String columnNameAssetType                  = "asset_type";
    private static final String columnNameQualifiedName              = "qualified_name";
    private static final String columnNameDeployedImplementationType = "deployed_implementation_type";
    private static final String columnNameCreationTime               = "creation_time";
    private static final String columnNameMetadataCollectionId       = "metadata_collection_id";
    private static final String columnNameDescription                = "description";
    private static final String columnNameSurveyGUID                 = "sr_guid";
    private static final String columnNameEngineActionGUID           = "engine_action_guid";
    private static final String columnNameRequestType                = "request_type";
    private static final String columnNameGovernanceEngineName       = "governance_engine_name";
    private static final String columnNameEngineHostUserId           = "engine_host_user_id";
    private static final String columnNameInitiator                  = "initiator"; // engine action createdBy user_id
    private static final String columnNameEndTimestamp               = "end_timestamp";
    private static final String columnNameStartTimestamp             = "start_timestamp";
    private static final String columnNameSRGUID                     = "sr_guid";
    private static final String columnNameSubjectGUID                = "subject_guid";
    private static final String columnNameSubjectType                = "subject_type";
    private static final String columnNameAnnotationGUID             = "annotation_guid";
    private static final String columnNameJSONProperties             = "json_properties";
    private static final String columnNameMeasurementName            = "measurement_name";
    private static final String columnNameMeasurementDisplayValue    = "measurement_display_value";
    private static final String columnNameMeasurementValue           = "measurement_value";
    private static final String columnNameMeasurementCategory        = "measurement_category";
    private static final String columnNameResourceCreationTime       = "resource_creation_time";
    private static final String columnNameResourceSize               = "resource_size";
    private static final String columnNameActionRequestName          = "action_request_name";
    private static final String columnNameActionTargetGUID           = "action_target_guid";
    private static final String columnNameActionTargetType           = "action_target_type";
    private static final String columnNameSurveyReportGUID           = "sr_guid";
    private static final String columnNamePurpose                    = "purpose";
    private static final String columnNameFilename                   = "filename";
    private static final String columnNameFileExtension              = "file_extension";
    private static final String columnNamePathname                   = "pathname";
    private static final String columnNameFileType                   = "file_type";
    private static final String columnNameEncoding                   = "file_encoding";
    private static final String columnNameAssetTypeName              = "asset_type_name";
    private static final String columnNameCanRead                    = "can_read";
    private static final String columnNameCanWrite                   = "can_write";
    private static final String columnNameCanExecute                 = "can_execute";
    private static final String columnNameIsSymLink                  = "is_sym_link";
    private static final String columnNameIsHidden                   = "is_hidden";
    private static final String columnNameFileCreationTime           = "file_creation_time";
    private static final String columnNameLastModifiedTime           = "last_modified_time";
    private static final String columnNameLastAccessedTime           = "last_accessed_time";
    private static final String columnNameFileSize                   = "file_size";
    private static final String columnNameRecordCount                = "record_count";
    private static final String columnNameDirectoryName              = "directory_name";
    private static final String columnNameFileCount                  = "file_count";
    private static final String columnNameTotalFileSize              = "total_file_size";
    private static final String columnNameSubDirectoryCount          = "sub_directory_count";
    private static final String columnNameReadableFileCount          = "readable_file_count";
    private static final String columnNameWriteableFileCount         = "writeable_file_count";
    private static final String columnNameExecutableFileCount        = "executable_file_count";
    private static final String columnNameSymLinkFileCount           = "sym_link_file_count";
    private static final String columnNameHiddenFileCount            = "hidden_file_count";
    private static final String columnNameFileNameCount              = "file_name_count";
    private static final String columnNameFileExtensionCount         = "file_extension_count";
    private static final String columnNameFileTypeCount              = "file_type_count";
    private static final String columnNameAssetTypeCount             = "asset_type_count";
    private static final String columnNameDeployedImplementationTypeCount = "deployed_implementation_type_count";
    private static final String columnNameUnclassifiedFileCount      = "unclassified_file_count";
    private static final String columnNameInaccessibleFileCount      = "inaccessible_file_count";
    private static final String columnNameLastFileCreationTime       = "last_file_creation_time";
    private static final String columnNameLastFileModificationTime   = "last_file_modification_time";
    private static final String columnNameLastFileAccessedTime       = "last_file_accessed_time";


    /*
     * These maps contain the details of the columns in the tables that produce
     * a new row each time an update is made.  This is accomplished by having the sync_time
     * column as part of the primary key.
     */
    private final Map<String, Integer>    fileClassifiersTableColumns = new HashMap<>();
    private final OpenMetadataAccess      openMetadataAccess;
    private final ConnectorFactoryService connectorFactoryService;
    private       JDBCResourceConnector   databaseClient     = null;
    private       java.sql.Connection     databaseConnection = null;


    /**
     * Constructor
     *
     * @param template catalog target information
     * @param connectorName name of this integration connector
     * @param auditLog logging destination 
     * @param openMetadataAccess access to open metadata
     * @param connectorFactoryService client for creating open connectors
     * @throws ConnectorCheckedException error
     */
    public HarvestSurveysCatalogTargetProcessor(CatalogTarget           template,
                                                String                  connectorName,
                                                AuditLog                auditLog,
                                                OpenMetadataAccess      openMetadataAccess,
                                                ConnectorFactoryService connectorFactoryService) throws ConnectorCheckedException
    {
        super(template, connectorName, auditLog);

        this.openMetadataAccess = openMetadataAccess;
        this.connectorFactoryService = connectorFactoryService;

        if (super.getCatalogTargetConnector() instanceof JDBCResourceConnector jdbcResourceConnector)
        {
            this.databaseClient = jdbcResourceConnector;
        }
        
        fillFileClassifiersTableColumns();
    }



    /* ==============================================================================
     * Standard methods that trigger activity.
     */
    


    /**
     * Set up the definitions of the columns in the rd-file-classifiers table.
     */
    private void fillFileClassifiersTableColumns()
    {
        fileClassifiersTableColumns.put(columnNameSurveyReportGUID, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFilename, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFileExtension, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNamePathname, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFileType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameAssetType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameDeployedImplementationType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameEncoding, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


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
        final String methodName = "refresh";

        try
        {
            this.databaseConnection = databaseClient.getDataSource().getConnection();

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
                for (OpenMetadataElement userIdentityElement : surveyReportElements)
                {
                    processSurveyReport(userIdentityElement);
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

            databaseConnection.close();
            databaseConnection = null;
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
                    databaseConnection.close();
                }
                catch (Exception  closeError)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              closeError.getClass().getName(),
                                                                                                              methodName,
                                                                                                              closeError.getMessage()),
                                          error);
                }
                databaseConnection = null;
            }

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
     * @param surveyReportElement retrieved element
     */
    private void processSurveyReport(OpenMetadataElement surveyReportElement) throws ConnectorCheckedException
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

            int startFrom = 0;
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                                                 0,
                                                                                                                 null,
                                                                                                                 startFrom,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

            while (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
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

            syncSurveyReport(surveyReportElement,
                             relatedAsset,
                             relatedEngineAction);

            processRequestForActionAnnotations(surveyReportElement.getElementGUID(),
                                               relatedAsset,
                                               relatedRequestForActionAnnotations);

            processResourceProfileAnnotations(surveyReportElement.getElementGUID(),
                                              relatedAsset,
                                              relatedResourceProfileAnnotations);

            processDataProfileLogAnnotations(surveyReportElement.getElementGUID(),
                                             relatedAsset,
                                             relatedDataProfileLogAnnotations);

            processDataSourceMeasurementsAnnotations(surveyReportElement.getElementGUID(),
                                                     relatedAsset,
                                                     relatedDataSourceMeasurementsAnnotations);
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedRequestForActionAnnotations list of request for action annotations
     */
    private void processRequestForActionAnnotations(String                       surveyReportGUID,
                                                    RelatedMetadataElement       relatedAsset,
                                                    List<RelatedMetadataElement> relatedRequestForActionAnnotations)
    {
        final String methodName = "processRequestForActionAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedRequestForActionAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedActionTarget = null;
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName))
                            {
                                relatedActionTarget = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncRequestForAction(surveyReportGUID,
                                     relatedAnnotationElement.getElement(),
                                     relatedActionTarget,
                                     relatedAnnotationSubject);
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedResourceProfileAnnotations list of request for action annotations
     */
    private void processResourceProfileAnnotations(String                       surveyReportGUID,
                                                   RelatedMetadataElement       relatedAsset,
                                                   List<RelatedMetadataElement> relatedResourceProfileAnnotations)
    {
        final String methodName = "processResourceProfileAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedResourceProfileAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncResourceProfile(surveyReportGUID,
                                    relatedAnnotationElement.getElement(),
                                    relatedAnnotationSubject);
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataProfileLogAnnotations list of request for action annotations
     */
    private void processDataProfileLogAnnotations(String                       surveyReportGUID,
                                                  RelatedMetadataElement       relatedAsset,
                                                  List<RelatedMetadataElement> relatedDataProfileLogAnnotations)
    {
        final String methodName = "processDataProfileLogAnnotations";


        for (RelatedMetadataElement relatedAnnotationElement : relatedDataProfileLogAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedLogFile = null;
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName))
                            {
                                relatedLogFile = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                if (relatedLogFile != null)
                {
                    Connector connector = connectorFactoryService.getConnectorForAsset(relatedLogFile.getElement().getElementGUID());

                    if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
                    {
                        Map<String, Integer> measurementValues = new HashMap<>();

                        for (int recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
                        {
                            List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                            if ((recordValues != null) && (recordValues.size() > 1))
                            {
                                Integer measurementValue = Integer.parseInt(recordValues.get(1));

                                measurementValues.put(recordValues.get(0), measurementValue);
                            }
                        }

                        syncValueProfile(surveyReportGUID,
                                         relatedAnnotationElement.getElement(),
                                         relatedAnnotationSubject,
                                         measurementValues);
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
            }
        }
    }


    /**
     * Add details of data source measurements annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataSourceMeasurementsAnnotations list of request for action annotations
     */
    private void processDataSourceMeasurementsAnnotations(String                       surveyReportGUID,
                                                          RelatedMetadataElement       relatedAsset,
                                                          List<RelatedMetadataElement> relatedDataSourceMeasurementsAnnotations)
    {
        final String methodName = "processDataSourceMeasurementsAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedDataSourceMeasurementsAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     openMetadataAccess.getMaxPagingSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncDataSourceMeasurements(surveyReportGUID,
                                           relatedAnnotationElement.getElement(),
                                           relatedAnnotationSubject);
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
     * Process information about a survey report.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportElement element describing the survey report
     * @param relatedAsset asset described in the report
     * @param relatedEngineAction details of the engine action that initiated the report
     */
    private void syncSurveyReport(OpenMetadataElement    surveyReportElement,
                                  RelatedMetadataElement relatedAsset,
                                  RelatedMetadataElement relatedEngineAction) throws ConnectorCheckedException
    {
        final String methodName = "syncSurveyReport";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getSurveyReportDataValues(surveyReportElement,
                                                                                           relatedAsset,
                                                                                           relatedEngineAction);

            databaseClient.insertRowIntoTable(databaseConnection, surveyReportDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameSurveyGUID, new JDBCDataValue(surveyReportElement.getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(surveyReportElement.getOrigin().getHomeMetadataCollectionId(), Types.VARCHAR));
        openMetadataRecord.put(columnNameQualifiedName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                           surveyReportElement.getElementProperties(),
                                                                                                           methodName),
                                                                          Types.VARCHAR));
        openMetadataRecord.put(columnNameDisplayName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                         surveyReportElement.getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNameDescription, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                                                         surveyReportElement.getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNamePurpose, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                     OpenMetadataProperty.PURPOSE.name,
                                                                                                     surveyReportElement.getElementProperties(),
                                                                                                     methodName),
                                                                    Types.VARCHAR));

        openMetadataRecord.put(columnNameEndTimestamp, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                     OpenMetadataProperty.COMPLETION_DATE.name,
                                                                                                     surveyReportElement.getElementProperties(),
                                                                                                     methodName),
                                                                         Types.TIMESTAMP));
        openMetadataRecord.put(columnNameStartTimestamp, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                          OpenMetadataProperty.START_DATE.name,
                                                                                                          surveyReportElement.getElementProperties(),
                                                                                                          methodName),
                                                                           Types.TIMESTAMP));

        openMetadataRecord.put(columnNameAssetGUID, new JDBCDataValue(relatedAsset.getElement().getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(relatedAsset.getElement().getType().getTypeName(), Types.VARCHAR));


        openMetadataRecord.put(columnNameEngineActionGUID, new JDBCDataValue(relatedEngineAction.getElement().getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameInitiator, new JDBCDataValue(relatedEngineAction.getVersions().getCreatedBy(), Types.VARCHAR));
        openMetadataRecord.put(columnNameRequestType, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                                         relatedEngineAction.getElement().getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNameGovernanceEngineName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataType.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
                                                                                                         relatedEngineAction.getElement().getElementProperties(),
                                                                                                         methodName),
                                                                                 Types.VARCHAR));
        openMetadataRecord.put(columnNameEngineHostUserId, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                              OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                                              relatedEngineAction.getElement().getElementProperties(),
                                                                                                              methodName),
                                                                             Types.VARCHAR));


        return openMetadataRecord;
    }


    /**
     * Process information about a request for action annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param requestForActionAnnotation description of the annotation
     * @param relatedActionTarget details of the element that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncRequestForAction(String                 surveyReportGUID,
                                      OpenMetadataElement    requestForActionAnnotation,
                                      RelatedMetadataElement relatedActionTarget,
                                      RelatedMetadataElement relatedAnnotationSubject) throws ConnectorCheckedException
    {
        final String methodName = "syncRequestForAction";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getRequestForActionDataValues(surveyReportGUID,
                                                                                               requestForActionAnnotation,
                                                                                               relatedActionTarget,
                                                                                               relatedAnnotationSubject);

            databaseClient.insertRowIntoTable(databaseConnection, requestForActionDatabaseTable, openMetadataRecord);

            if (relatedActionTarget != null)
            {
                processFileClassifiers(surveyReportGUID, relatedActionTarget);
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
     * Convert the description of a specific request ofr action annotation into columns for the sr_request_for_action table.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param requestForActionAnnotation description of the annotation
     * @param relatedActionTarget details of the element that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     * @return columns
     */
    private Map<String, JDBCDataValue> getRequestForActionDataValues(String                 surveyReportGUID,
                                                                     OpenMetadataElement    requestForActionAnnotation,
                                                                     RelatedMetadataElement relatedActionTarget,
                                                                     RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "getRequestForActionDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(requestForActionAnnotation.getOrigin().getHomeMetadataCollectionId(), Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(requestForActionAnnotation.getVersions().getCreateTime(), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(requestForActionAnnotation.getElementGUID(), Types.VARCHAR));

        if (relatedAnnotationSubject != null)
        {
            openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(relatedAnnotationSubject.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(relatedAnnotationSubject.getElement().getType().getTypeName(), Types.VARCHAR));
        }
        else
        {
            openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(null, Types.VARCHAR));
            openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(null, Types.VARCHAR));
        }

        if (relatedActionTarget != null)
        {
            openMetadataRecord.put(columnNameActionTargetGUID, new JDBCDataValue(relatedActionTarget.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameActionTargetType, new JDBCDataValue(relatedActionTarget.getElement().getType().getTypeName(), Types.VARCHAR));
        }
        else
        {
            openMetadataRecord.put(columnNameActionTargetGUID, new JDBCDataValue(null, Types.VARCHAR));
            openMetadataRecord.put(columnNameActionTargetType, new JDBCDataValue(null, Types.VARCHAR));
        }

        openMetadataRecord.put(columnNameActionRequestName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                                                           requestForActionAnnotation.getElementProperties(),
                                                                                                           methodName),
                                                                              Types.VARCHAR));
        return openMetadataRecord;
    }


    /**
     * Add details of the missing reference data to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param actionTargetElement  details about the file that contains the missing reference data
     */
    private void processFileClassifiers(String                       surveyReportGUID,
                                        RelatedMetadataElement       actionTargetElement) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 ConnectionCheckedException,
                                                                                                 ConnectorCheckedException
    {
        Connector connector = connectorFactoryService.getConnectorForAsset(actionTargetElement.getElement().getElementGUID());

        if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
        {
            for (int recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
            {
                List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                if ((recordValues != null) && (recordValues.size() > 1))
                {
                    syncFileClassifiers(surveyReportGUID,
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param fileName  details about the file that contains the missing reference data
     * @param fileExtension  details about the file that contains the missing reference data
     * @param pathName  details about the file that contains the missing reference data
     * @param fileType  details about the file that contains the missing reference data
     * @param assetTypeName  details about the file that contains the missing reference data
     * @param deployedImplementationType  details about the file that contains the missing reference data
     * @param encoding  details about the file that contains the missing reference data
     */
    private void syncFileClassifiers(String surveyReportGUID,
                                     String fileName,
                                     String fileExtension,
                                     String pathName,
                                     String fileType,
                                     String assetTypeName,
                                     String deployedImplementationType,
                                     String encoding) throws ConnectorCheckedException
    {
        final String methodName = "syncFileClassifiers";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        missingFileClassifiersDatabaseTable,
                                                                                        columnNamePathname,
                                                                                        null,
                                                                                        columnNameSyncTime,
                                                                                        fileClassifiersTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getFileClassifierDataValues(surveyReportGUID,
                                                                                             fileName,
                                                                                             fileExtension,
                                                                                             pathName,
                                                                                             fileType,
                                                                                             assetTypeName,
                                                                                             deployedImplementationType,
                                                                                             encoding);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, missingFileClassifiersDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameSurveyReportGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameFilename, new JDBCDataValue(fileName, Types.VARCHAR));
        openMetadataRecord.put(columnNameFileExtension, new JDBCDataValue(fileExtension, Types.VARCHAR));
        openMetadataRecord.put(columnNamePathname, new JDBCDataValue(pathName, Types.VARCHAR));
        openMetadataRecord.put(columnNameFileType, new JDBCDataValue(fileType, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(assetType, Types.VARCHAR));
        openMetadataRecord.put(columnNameDeployedImplementationType, new JDBCDataValue(deployedImplementationType, Types.VARCHAR));
        openMetadataRecord.put(columnNameEncoding, new JDBCDataValue(encoding, Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process information about a data profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncResourceProfile(String                 surveyReportGUID,
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
                syncValueProfile(surveyReportGUID,
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

                syncResourceProfile(surveyReportGUID,
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @param measurementValues map of measurement categories to measurement counts
     */
    private void syncValueProfile(String                 surveyReportGUID,
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

                    databaseClient.insertRowIntoTable(databaseConnection, profileMeasuresDatabaseTable, openMetadataRecord);
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
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @param measurementValues map of measurement categories to measurement counts
     */
    private void syncResourceProfile(String                 surveyReportGUID,
                                     OpenMetadataElement    dataProfileAnnotation,
                                     RelatedMetadataElement relatedAnnotationSubject,
                                     Map<String, Long>      measurementValues) throws ConnectorCheckedException
    {
        final String methodName = "syncResourceProfile";

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
                    long measurementValue = 0;

                    if (measurementValues.get(valueCountName) != null)
                    {
                        measurementValue = measurementValues.get(valueCountName);
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getResourceProfileDataValues(surveyReportGUID,
                                                                                                      subjectGUID,
                                                                                                      subjectType,
                                                                                                      dataProfileAnnotation.getElementGUID(),
                                                                                                      dataProfileAnnotation.getVersions().getCreateTime(),
                                                                                                      dataProfileAnnotation.getOrigin().getHomeMetadataCollectionId(),
                                                                                                      measurementName,
                                                                                                      valueCountName,
                                                                                                      measurementValue);

                    databaseClient.insertRowIntoTable(databaseConnection, profileMeasuresDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementName, new JDBCDataValue(measurementName, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementCategory, new JDBCDataValue(measurementCategory, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementValue, new JDBCDataValue(measurementValue, Types.NUMERIC));

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
     * @param metadataCollectionId home
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @return columns
     */
    private Map<String, JDBCDataValue> getResourceProfileDataValues(String                 surveyReportGUID,
                                                                    String                 subjectGUID,
                                                                    String                 subjectType,
                                                                    String                 annotationGUID,
                                                                    Date                   creationTime,
                                                                    String                 metadataCollectionId,
                                                                    String                 measurementName,
                                                                    String                 measurementCategory,
                                                                    long                   measurementValue)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementName, new JDBCDataValue(measurementName, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementCategory, new JDBCDataValue(measurementCategory, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementValue, new JDBCDataValue(measurementValue, Types.NUMERIC));

        return openMetadataRecord;
    }


    /**
     * Process information about a data source measurements annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataSourceMeasurementsAnnotation element representing the data source measurements annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncDataSourceMeasurements(String                 surveyReportGUID,
                                            OpenMetadataElement    dataSourceMeasurementsAnnotation,
                                            RelatedMetadataElement relatedAnnotationSubject) throws ConnectorCheckedException
    {
        final String methodName = "syncDataSourceMeasurements";

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
                                                                      dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                      methodName);

            String jsonProperties = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.JSON_PROPERTIES.name,
                                                                      dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                      methodName);

            if (jsonProperties != null)
            {
                if ("Extract File Properties".equals(measurementName))
                {
                    try
                    {
                        FileMeasurement fileMeasurement = OBJECT_READER.readValue(jsonProperties, FileMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getFileMeasurementsDataValues(surveyReportGUID,
                                                                                                           subjectGUID,
                                                                                                           subjectType,
                                                                                                           dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                           dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                           dataSourceMeasurementsAnnotation.getOrigin().getHomeMetadataCollectionId(),
                                                                                                           fileMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, fileMeasurementsDatabaseTable, openMetadataRecord);
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
                else if ("Capture File Counts".equals(measurementName))
                {
                    try
                    {
                        FileDirectoryMeasurement fileDirectoryMeasurement = OBJECT_READER.readValue(jsonProperties, FileDirectoryMeasurement.class);

                        Map<String, JDBCDataValue> openMetadataRecord = this.getFileDirectoryMeasurementsDataValues(surveyReportGUID,
                                                                                                                    subjectGUID,
                                                                                                                    subjectType,
                                                                                                                    dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                                    dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                                    dataSourceMeasurementsAnnotation.getOrigin().getHomeMetadataCollectionId(),
                                                                                                                    fileDirectoryMeasurement);

                        databaseClient.insertRowIntoTable(databaseConnection, folderMeasurementsDatabaseTable, openMetadataRecord);
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

            long resourceSize = propertyHelper.getLongProperty(connectorName,
                                                               OpenMetadataProperty.SIZE.name,
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
                                                                                                             dataSourceMeasurementsAnnotation.getOrigin().getHomeMetadataCollectionId(),
                                                                                                             measurementName,
                                                                                                             resourcePropertyName,
                                                                                                             measurementValue,
                                                                                                             resourceProperties.get(resourcePropertyName),
                                                                                                             resourceCreateTime,
                                                                                                             resourceUpdateTime,
                                                                                                             resourceSize);

                    databaseClient.insertRowIntoTable(databaseConnection, resourceMeasurementDatabaseTable, openMetadataRecord);
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
     * @param metadataCollectionId home
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @param measurementDisplayName string version fo measurement value (may not be a number)
     * @param resourceCreationTime time resource create (only for physical measurement annotation
     * @param resourceModificationTime time resource last modified (only for physical measurement annotation
     * @param resourceSize size of resource in bytes (only for physical measurement annotation
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataSourceMeasurementsDataValues(String                 surveyReportGUID,
                                                                           String                 subjectGUID,
                                                                           String                 subjectType,
                                                                           String                 annotationGUID,
                                                                           Date                   creationTime,
                                                                           String                 metadataCollectionId,
                                                                           String                 measurementName,
                                                                           String                 measurementCategory,
                                                                           Integer                measurementValue,
                                                                           String                 measurementDisplayName,
                                                                           Date                   resourceCreationTime,
                                                                           Date                   resourceModificationTime,
                                                                           long                   resourceSize)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementName, new JDBCDataValue(measurementName, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementCategory, new JDBCDataValue(measurementCategory, Types.VARCHAR));
        if (measurementValue != null)
        {
            openMetadataRecord.put(columnNameMeasurementValue, new JDBCDataValue(measurementValue, Types.NUMERIC));
        }
        openMetadataRecord.put(columnNameMeasurementDisplayValue, new JDBCDataValue(measurementDisplayName, Types.VARCHAR));
        if (resourceCreationTime != null)
        {
            openMetadataRecord.put(columnNameResourceCreationTime, new JDBCDataValue(resourceCreationTime, Types.TIMESTAMP));
        }
        if (resourceModificationTime != null)
        {
            openMetadataRecord.put(columnNameLastModifiedTime, new JDBCDataValue(resourceModificationTime, Types.TIMESTAMP));
        }
        openMetadataRecord.put(columnNameResourceSize, new JDBCDataValue(resourceSize, Types.NUMERIC));

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
     * @param metadataCollectionId  home
     * @param fileMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileMeasurementsDataValues(String                 surveyReportGUID,
                                                                     String                 subjectGUID,
                                                                     String                 subjectType,
                                                                     String                 annotationGUID,
                                                                     Date                   creationTime,
                                                                     String                 metadataCollectionId,
                                                                     FileMeasurement        fileMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameFilename, new JDBCDataValue(fileMeasurement.getFileName(), Types.VARCHAR));
        openMetadataRecord.put(columnNamePathname, new JDBCDataValue(fileMeasurement.getPathName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameFileExtension, new JDBCDataValue(fileMeasurement.getFileExtension(), Types.VARCHAR));
        openMetadataRecord.put(columnNameFileType, new JDBCDataValue(fileMeasurement.getFileType(), Types.VARCHAR));
        openMetadataRecord.put(columnNameDeployedImplementationType, new JDBCDataValue(fileMeasurement.getDeployedImplementationType(), Types.VARCHAR));
        openMetadataRecord.put(columnNameEncoding, new JDBCDataValue(fileMeasurement.getEncoding(), Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetTypeName, new JDBCDataValue(fileMeasurement.getAssetTypeName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameCanRead, new JDBCDataValue(fileMeasurement.getCanRead(), Types.BOOLEAN));
        openMetadataRecord.put(columnNameCanWrite, new JDBCDataValue(fileMeasurement.getCanWrite(), Types.BOOLEAN));
        openMetadataRecord.put(columnNameCanExecute, new JDBCDataValue(fileMeasurement.getCanExecute(), Types.BOOLEAN));
        openMetadataRecord.put(columnNameIsSymLink, new JDBCDataValue(fileMeasurement.getSymLink(), Types.BOOLEAN));
        openMetadataRecord.put(columnNameIsHidden, new JDBCDataValue(fileMeasurement.getHidden(), Types.BOOLEAN));

        if (fileMeasurement.getCreationTime() != null)
        {
            openMetadataRecord.put(columnNameFileCreationTime, new JDBCDataValue(fileMeasurement.getCreationTime(), Types.TIMESTAMP));
        }
        if (fileMeasurement.getLastModifiedTime() != null)
        {
            openMetadataRecord.put(columnNameLastModifiedTime, new JDBCDataValue(fileMeasurement.getLastModifiedTime(), Types.TIMESTAMP));
        }
        if (fileMeasurement.getLastAccessedTime() != null)
        {
            openMetadataRecord.put(columnNameLastAccessedTime, new JDBCDataValue(fileMeasurement.getLastAccessedTime(), Types.TIMESTAMP));
        }

        openMetadataRecord.put(columnNameFileSize, new JDBCDataValue(fileMeasurement.getFileSize(), Types.NUMERIC));
        openMetadataRecord.put(columnNameRecordCount, new JDBCDataValue(fileMeasurement.getRecordCount(), Types.NUMERIC));

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
     * @param metadataCollectionId  home
     * @param fileDirectoryMeasurement properties to save
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileDirectoryMeasurementsDataValues(String                    surveyReportGUID,
                                                                              String                    subjectGUID,
                                                                              String                    subjectType,
                                                                              String                    annotationGUID,
                                                                              Date                      creationTime,
                                                                              String                    metadataCollectionId,
                                                                              FileDirectoryMeasurement  fileDirectoryMeasurement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameDirectoryName, new JDBCDataValue(fileDirectoryMeasurement.getDirectoryName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameFileCount, new JDBCDataValue(fileDirectoryMeasurement.getFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameTotalFileSize, new JDBCDataValue(fileDirectoryMeasurement.getTotalFileSize(), Types.NUMERIC));
        openMetadataRecord.put(columnNameSubDirectoryCount, new JDBCDataValue(fileDirectoryMeasurement.getSubDirectoryCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameReadableFileCount, new JDBCDataValue(fileDirectoryMeasurement.getReadableFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameWriteableFileCount, new JDBCDataValue(fileDirectoryMeasurement.getWriteableFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameExecutableFileCount, new JDBCDataValue(fileDirectoryMeasurement.getExecutableFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameSymLinkFileCount, new JDBCDataValue(fileDirectoryMeasurement.getSymLinkFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameHiddenFileCount, new JDBCDataValue(fileDirectoryMeasurement.getHiddenFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameFileNameCount, new JDBCDataValue(fileDirectoryMeasurement.getFileNameCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameFileExtensionCount, new JDBCDataValue(fileDirectoryMeasurement.getFileExtensionCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameFileTypeCount, new JDBCDataValue(fileDirectoryMeasurement.getFileTypeCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameAssetTypeCount, new JDBCDataValue(fileDirectoryMeasurement.getAssetTypeCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameDeployedImplementationTypeCount, new JDBCDataValue(fileDirectoryMeasurement.getDeployedImplementationTypeCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameUnclassifiedFileCount, new JDBCDataValue(fileDirectoryMeasurement.getUnclassifiedFileCount(), Types.NUMERIC));
        openMetadataRecord.put(columnNameInaccessibleFileCount, new JDBCDataValue(fileDirectoryMeasurement.getInaccessibleFileCount(), Types.NUMERIC));

        if (fileDirectoryMeasurement.getLastFileCreationTime() != null)
        {
            openMetadataRecord.put(columnNameLastFileCreationTime, new JDBCDataValue(fileDirectoryMeasurement.getLastFileCreationTime(), Types.TIMESTAMP));
        }
        if (fileDirectoryMeasurement.getLastFileModificationTime() != null)
        {
            openMetadataRecord.put(columnNameLastFileModificationTime, new JDBCDataValue(fileDirectoryMeasurement.getLastFileModificationTime(), Types.TIMESTAMP));
        }
        if (fileDirectoryMeasurement.getLastFileAccessedTime() != null)
        {
            openMetadataRecord.put(columnNameLastFileAccessedTime, new JDBCDataValue(fileDirectoryMeasurement.getLastFileAccessedTime(), Types.TIMESTAMP));
        }

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
}
