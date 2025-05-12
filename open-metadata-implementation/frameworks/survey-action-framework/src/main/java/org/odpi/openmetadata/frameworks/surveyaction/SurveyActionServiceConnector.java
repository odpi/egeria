/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.NestedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionGuard;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFAuditCode;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFErrorCode;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileLogAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * SurveyActionServiceConnector describes a specific type of connector that is responsible for analyzing the content
 * of a specific asset.  Information about the asset to analyze is passed in the survey context.
 * The returned discovery context also contains the results.
 * Some discovery services manage the invocation of other discovery services.  These discovery services are called
 * discovery pipelines.
 */
public abstract class SurveyActionServiceConnector extends ConnectorBase implements SurveyActionService,
                                                                                    AuditLoggingComponent
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();

    protected Connector connector = null;

    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected String          surveyActionServiceName = "<Unknown>";
    protected SurveyContext   surveyContext           = null;
    protected AuditLog        auditLog                = null;
    protected List<Connector> embeddedConnectors      = null;


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Convert the supplied properties object to a JSON String.
     *
     * @param properties properties object
     * @return properties as a JSON String
     * @throws PropertyServerException parsing error
     */
    public String getJSONProperties(Object properties) throws PropertyServerException
    {
        final String methodName = "getJSONProperties";

        try
        {
            return objectMapper.writeValueAsString(properties);
        }
        catch (JsonProcessingException parsingError)
        {
            throw new PropertyServerException(SAFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                     parsingError.getClass().getName(),
                                                                                                     methodName,
                                                                                                     parsingError.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              parsingError);
        }
    }


    /**
     * Set up details of the asset to analyze and the results of any previous analysis.
     *
     * @param surveyContext information about the asset to analyze and the results of analysis of
     *                      other survey action service request.  Partial results from other survey action
     *                      services run as part of the same survey action service request may also be
     *                      stored in the newAnnotations list.
     */
    public void setSurveyContext(SurveyContext surveyContext)
    {
        this.surveyContext = surveyContext;
    }


    /**
     * Set up the survey action service name.  This is used in error messages.
     *
     * @param surveyActionServiceName name of the survey action service
     */
    public void setSurveyActionServiceName(String surveyActionServiceName)
    {
        this.surveyActionServiceName = surveyActionServiceName;
    }


    /**
     * This is the typical first analysis step of a survey action service.
     *
     * @param expectedConnectorClass the class of the desired connector
     * @param expectedAssetType      the type of asset that this survey service works on
     * @param <T>                    type of connector
     * @return the started connector which is of the correct type
     * @throws ConnectorCheckedException  problem with the connector
     * @throws InvalidParameterException  invalid property
     * @throws PropertyServerException    problem with repositories
     * @throws UserNotAuthorizedException security problem
     * @throws ConnectionCheckedException problem with connection
     */
    protected <T> Connector performCheckAssetAnalysisStep(Class<T> expectedConnectorClass,
                                                          String expectedAssetType) throws ConnectorCheckedException,
                                                                                           InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException,
                                                                                           ConnectionCheckedException
    {
        final String methodName = "performCheckAssetAnalysisStep";

        performCheckAssetAnalysisStep(expectedAssetType);

        SurveyAssetStore assetStore = surveyContext.getAssetStore();

        /*
         * The asset should have a special connector for files.  If the connector is wrong,
         * the cast will fail.
         */
        Connector connector = assetStore.getConnectorToAsset();

        if (expectedConnectorClass.isInstance(connector))
        {
            connector.start();

            return connector;
        }

        surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                             Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                             null,
                                             null,
                                             SAFAuditCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(surveyActionServiceName,
                                                                                                       connector.getClass().getName(),
                                                                                                       expectedConnectorClass.getName(),
                                                                                                       assetStore.getAssetGUID()));

        throw new ConnectorCheckedException(SAFErrorCode.WRONG_TYPE_OF_CONNECTOR.getMessageDefinition(surveyActionServiceName,
                                                                                                      connector.getClass().getName(),
                                                                                                      expectedConnectorClass.getName(),
                                                                                                      assetStore.getAssetGUID()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * This is the typical first analysis step of a survey action service.
     * It is used by survey action services that do not use the asset connector.
     *
     * @param expectedAssetType the type of asset that this survey service works on
     * @throws ConnectorCheckedException  problem with the connector
     * @throws InvalidParameterException  invalid property
     * @throws PropertyServerException    problem with repositories
     * @throws UserNotAuthorizedException security problem
     */
    protected void performCheckAssetAnalysisStep(String expectedAssetType) throws ConnectorCheckedException,
                                                                                  InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "performCheckAssetAnalysisStep";

        AnnotationStore  annotationStore = surveyContext.getAnnotationStore();
        SurveyAssetStore assetStore      = surveyContext.getAssetStore();

        annotationStore.setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

        /*
         * Before performing any real work, check the type of the asset.
         */
        AssetUniverse assetUniverse = assetStore.getAssetProperties();

        if (assetUniverse == null)
        {
            surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                                 Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                                 null,
                                                 null,
                                                 SAFAuditCode.NO_ASSET.getMessageDefinition(assetStore.getAssetGUID(), surveyActionServiceName));

            super.throwNoAsset(assetStore.getAssetGUID(),
                               surveyActionServiceName,
                               methodName);
        }
        else if (!propertyHelper.isTypeOf(assetUniverse, expectedAssetType))
        {
            surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                                 Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                                 null,
                                                 null,
                                                 SAFAuditCode.WRONG_TYPE_OF_ASSET.getMessageDefinition(assetUniverse.getGUID(),
                                                                                                       assetUniverse.getType().getTypeName(),
                                                                                                       surveyActionServiceName,
                                                                                                       expectedAssetType));

            throw new ConnectorCheckedException(SAFErrorCode.INVALID_ASSET_TYPE.getMessageDefinition(assetUniverse.getGUID(),
                                                                                                     assetUniverse.getType().getTypeName(),
                                                                                                     surveyActionServiceName,
                                                                                                     expectedAssetType),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Return the nested schema type associated with the asset
     *
     * @param assetUniverse details of asset
     * @param schemaTypeName name of type for schema
     * @return nested schema type or null
     * @throws ConnectorCheckedException  problem with the connector
     * @throws InvalidParameterException  invalid property
     * @throws PropertyServerException    problem with repositories
     * @throws UserNotAuthorizedException security problem
     */
    protected NestedSchemaType getNestedSchemaType(AssetUniverse assetUniverse,
                                                   String        schemaTypeName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException,
                                                                                    ConnectorCheckedException
    {
        final String methodName = "getNestedSchemaType";

        SchemaType rootSchemaType = assetUniverse.getRootSchemaType();

        if (rootSchemaType instanceof NestedSchemaType nestedSchemaType)
        {
            if (propertyHelper.isTypeOf(nestedSchemaType, schemaTypeName))
            {
                if ((nestedSchemaType.getSchemaAttributes() != null) && (nestedSchemaType.getSchemaAttributes().hasNext()))
                {
                    return nestedSchemaType;
                }
                else
                {
                    surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                                         Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                                         null,
                                                         null,
                                                         SAFAuditCode.NO_SCHEMA_ATTRIBUTES.getMessageDefinition(surveyActionServiceName, assetUniverse.getGUID()));

                    throw new ConnectorCheckedException(SAFErrorCode.NO_SCHEMA_ATTRIBUTES.getMessageDefinition(surveyActionServiceName, assetUniverse.getGUID()),
                                                        this.getClass().getName(),
                                                        methodName);
                }
            }
        }

        if (rootSchemaType != null)
        {
            surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                                 Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                                 null,
                                                 null,
                                                 SAFAuditCode.INVALID_ROOT_SCHEMA_TYPE.getMessageDefinition(assetUniverse.getGUID(),
                                                                                                            assetUniverse.getType().getTypeName(),
                                                                                                            surveyActionServiceName,
                                                                                                            schemaTypeName));

            super.throwWrongTypeOfRootSchema(assetUniverse.getGUID(),
                                             rootSchemaType.getType().getTypeName(),
                                             schemaTypeName,
                                             surveyActionServiceName,
                                             methodName);
        }

        return null;
    }


    /**
     * Record that an asset does not have a schema.
     *
     * @param assetGUID unique identifier of the asset
     * @throws ConnectorCheckedException  problem with the connector
     * @throws InvalidParameterException  invalid property
     * @throws PropertyServerException    problem with repositories
     * @throws UserNotAuthorizedException security problem
     */
    protected void throwMissingSchemaType(String assetGUID) throws ConnectorCheckedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "throwMissingSchemaType";

        surveyContext.recordCompletionStatus(SurveyActionGuard.SURVEY_INVALID.getCompletionStatus(),
                                             Collections.singletonList(SurveyActionGuard.SURVEY_INVALID.getName()),
                                             null,
                                             null,
                                             SAFAuditCode.NO_SCHEMA.getMessageDefinition(surveyActionServiceName, assetGUID));

        throw new ConnectorCheckedException(SAFErrorCode.NO_SCHEMA.getMessageDefinition(surveyActionServiceName, assetGUID),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the survey context for this survey action service.  This is typically called after the disconnect()
     * method is called.  If called before disconnect(), it may only contain partial results.
     *
     * @return survey context containing the results discovered (so far) by the survey action service.
     * @throws ConnectorCheckedException the service is no longer active
     */
    protected SurveyContext getSurveyContext() throws ConnectorCheckedException
    {
        final String methodName = "getSurveyContext";

        validateIsActive(methodName);
        return surveyContext;
    }


    protected synchronized AnnotationStore getAnnotationStore() throws ConnectorCheckedException
    {
        final String methodName = "getAnnotationStore";

        validateIsActive(methodName);
        return surveyContext.getAnnotationStore();
    }


    /**
     * Transfer common properties into an annotation.
     *
     * @param annotation     output annotation
     * @param annotationType annotation type definition
     */
    protected void setUpAnnotation(Annotation     annotation,
                                   AnnotationType annotationType)
    {
        annotation.setAnnotationType(annotationType.getName());
        annotation.setAnalysisStep(annotationType.getAnalysisStep());
        annotation.setSummary(annotationType.getSummary());
        annotation.setExplanation(annotationType.getExplanation());
        annotation.setExpression(annotationType.getExpression());
    }


    /**
     * Create an inventory in a CSV File.
     * @param annotationType details about the annotation
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param propertyNames list of properties names in order they should appear in the file
     * @param propertyList list of property maps
     * @param surveyReportGUID unique identifier of the report to connect the log file to
     * @return inventory annotation
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    protected ResourceProfileLogAnnotation writePropertyListInventory(AnnotationType            annotationType,
                                                                      String                    inventoryName,
                                                                      List<String>              propertyNames,
                                                                      List<Map<String, String>> propertyList,
                                                                      String                    surveyReportGUID) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         IOException,
                                                                                                                         ConnectorCheckedException
    {
        ResourceProfileLogAnnotation inventoryLog = new ResourceProfileLogAnnotation();

        setUpAnnotation(inventoryLog, annotationType);

        List<String> dataProfileDataGUIDs = new ArrayList<>();
        dataProfileDataGUIDs.add(setUpExternalPropertyListLogFile(surveyReportGUID, inventoryName, propertyNames, propertyList));
        inventoryLog.setResourceProfileLogGUIDs(dataProfileDataGUIDs);

        return inventoryLog;
    }


    /**
     * Create and catalog a CSV file to store data about the list of names and the number of time they occur.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param propertyNames list of properties names in order they should appear in the file
     * @param propertyList list of property maps
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    private String setUpExternalPropertyListLogFile(String                    surveyReportGUID,
                                                    String                    inventoryName,
                                                    List<String>              propertyNames,
                                                    List<Map<String, String>> propertyList) throws IOException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   ConnectorCheckedException
    {
        final String methodName = "setUpExternalNamePropertiesLogFile";

        String  logFileName = "surveys/report-" + surveyReportGUID + "-" + inventoryName + ".csv";
        File    logFile     = new File(logFileName);
        boolean newLogFile  = false;

        if (propertyList != null)
        {
            try
            {
                FileUtils.sizeOf(logFile);
            }
            catch (IllegalArgumentException notFound)
            {
                newLogFile = true;
            }


            List<String>  columnList = new ArrayList<>(propertyNames);
            StringBuilder fileHeader = new StringBuilder();
            boolean       isFirstElement = true;

            for (String columnName : columnList)
            {
                if (isFirstElement)
                {
                    isFirstElement = false;
                    fileHeader.append(columnName);
                }
                else
                {
                    fileHeader.append(",").append(columnName);
                }
            }

            fileHeader.append("\n");

            FileUtils.writeStringToFile(logFile, fileHeader.toString(), (String) null, false);

            SurveyAssetStore surveyAssetStore = surveyContext.getAssetStore();

            String assetGUID = surveyAssetStore.addCSVFileToCatalog(inventoryName + " for survey report " + surveyReportGUID,
                                                                    "Provides an inventory of the resources discovered during the survey.",
                                                                    logFile.getCanonicalPath(),
                                                                    null,
                                                                    ',',
                                                                    '"');

            if (newLogFile)
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                        logFileName,
                                                                                        assetGUID));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                       logFileName));
            }

            for (Map<String, String> properties : propertyList)
            {
                StringBuilder row = new StringBuilder();

                isFirstElement = true;

                for (String columnName : columnList)
                {
                    if (isFirstElement)
                    {
                        isFirstElement = false;
                        row.append(properties.get(columnName));
                    }
                    else
                    {
                        row.append(",").append(properties.get(columnName));
                    }
                }
                row.append("\n");

                FileUtils.writeStringToFile(logFile, row.toString(), (String) null, true);
            }

            return assetGUID;
        }

        return null;
    }



    /**
     * Create an inventory in a CSV File.
     * @param annotationType details about the annotation
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param names list of  names
     * @param surveyReportGUID unique identifier of the report to connect the log file to
     * @return inventory annotation
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    protected ResourceProfileLogAnnotation writeNameListInventory(AnnotationType annotationType,
                                                                  String         inventoryName,
                                                                  List<String>   names,
                                                                   String        surveyReportGUID) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          IOException,
                                                                                                          ConnectorCheckedException
    {
        ResourceProfileLogAnnotation inventoryLog = new ResourceProfileLogAnnotation();

        setUpAnnotation(inventoryLog, annotationType);

        List<String> dataProfileDataGUIDs = new ArrayList<>();
        dataProfileDataGUIDs.add(setUpExternalNamesLogFile(surveyReportGUID, inventoryName, names));
        inventoryLog.setResourceProfileLogGUIDs(dataProfileDataGUIDs);

        return inventoryLog;
    }



    /**
     * Create and catalog a CSV file to store data about the list of names and the number of time they occur.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param names list of names
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    private String setUpExternalNamesLogFile(String       surveyReportGUID,
                                             String       inventoryName,
                                             List<String> names) throws IOException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectorCheckedException
    {
        final String methodName = "setUpExternalNamesLogFile";

        String  logFileName = "surveys/report-" + surveyReportGUID + "-" + inventoryName + ".csv";
        File    logFile     = new File(logFileName);
        boolean newLogFile  = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "Name\n", (String)null, false);
        }

        SurveyAssetStore surveyAssetStore = surveyContext.getAssetStore();

        String assetGUID = surveyAssetStore.addCSVFileToCatalog(inventoryName + " for survey report " + surveyReportGUID,
                                                                "Provides an inventory of the resources discovered during the survey.",
                                                                logFile.getCanonicalPath(),
                                                                null,
                                                                ',',
                                                                '"');

        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                    logFileName,
                                                                                    assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                   logFileName));
        }

        for (String name : names)
        {
            String row = name + "\n";

            FileUtils.writeStringToFile(logFile, row, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Create an inventory in a CSV File.
     * @param annotationType details about the annotation
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param nameCounts map of  names and how many times they appear
     * @param surveyReportGUID unique identifier of the report to connect the log file to
     * @return inventory annotation
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    protected ResourceProfileLogAnnotation writeNameCountInventory(AnnotationType       annotationType,
                                                                   String               inventoryName,
                                                                   Map<String, Integer> nameCounts,
                                                                   String               surveyReportGUID) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 IOException,
                                                                                                                 ConnectorCheckedException
    {
        ResourceProfileLogAnnotation inventoryLog = new ResourceProfileLogAnnotation();

        setUpAnnotation(inventoryLog, annotationType);

        List<String> dataProfileDataGUIDs = new ArrayList<>();
        dataProfileDataGUIDs.add(setUpExternalNameCountLogFile(surveyReportGUID, inventoryName, nameCounts));
        inventoryLog.setResourceProfileLogGUIDs(dataProfileDataGUIDs);

        return inventoryLog;
    }


    /**
     * Create and catalog a CSV file to store data about the list of names and the number of time they occur.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param nameCounts map of  names and how many times they appear
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    private String setUpExternalNameCountLogFile(String               surveyReportGUID,
                                                 String               inventoryName,
                                                 Map<String, Integer> nameCounts) throws IOException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         ConnectorCheckedException
    {
        final String methodName = "setUpExternalNameCountLogFile";

        String  logFileName = "surveys/report-" + surveyReportGUID + "-" + inventoryName + ".csv";
        File    logFile     = new File(logFileName);
        boolean newLogFile  = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "Name, Number of Occurrences\n", (String)null, false);
        }

        SurveyAssetStore surveyAssetStore = surveyContext.getAssetStore();

        String assetGUID = surveyAssetStore.addCSVFileToCatalog(inventoryName + " for survey report " + surveyReportGUID,
                                                                "Provides an inventory of the resources discovered during the survey.",
                                                                logFile.getCanonicalPath(),
                                                                null,
                                                                ',',
                                                                '"');

        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                    logFileName,
                                                                                    assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                   logFileName));
        }

        for (String fileName : nameCounts.keySet())
        {
            String row = fileName + "," + nameCounts.get(fileName) + "\n";

            FileUtils.writeStringToFile(logFile, row, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Create an inventory in a CSV File.
     * @param annotationType details about the annotation
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param nameDescriptions map of  names and how many times they appear
     * @param surveyReportGUID unique identifier of the report to connect the log file to
     * @return inventory annotation
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    protected ResourceProfileLogAnnotation writeNameDescriptionInventory(AnnotationType       annotationType,
                                                                         String               inventoryName,
                                                                         Map<String, String>  nameDescriptions,
                                                                         String               surveyReportGUID) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       IOException,
                                                                                                                       ConnectorCheckedException
    {
        ResourceProfileLogAnnotation inventoryLog = new ResourceProfileLogAnnotation();

        setUpAnnotation(inventoryLog, annotationType);

        List<String> dataProfileDataGUIDs = new ArrayList<>();
        dataProfileDataGUIDs.add(setUpExternalNameDescriptionLogFile(surveyReportGUID, inventoryName, nameDescriptions));
        inventoryLog.setResourceProfileLogGUIDs(dataProfileDataGUIDs);

        return inventoryLog;
    }


    /**
     * Create and catalog a CSV file to store data about the list of names and the number of time they occur.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param inventoryName name of the inventory (needs to be unique within the report
     * @param nameDescriptions map of  names and how many times they appear
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    private String setUpExternalNameDescriptionLogFile(String               surveyReportGUID,
                                                       String               inventoryName,
                                                       Map<String, String>  nameDescriptions) throws IOException,
                                                                                                     InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     ConnectorCheckedException
    {
        final String methodName = "setUpExternalNameDescriptionLogFile";

        String  logFileName = "surveys/report-" + surveyReportGUID + "-" + inventoryName + ".csv";
        File    logFile     = new File(logFileName);
        boolean newLogFile  = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "Name, Description\n", (String)null, false);
        }

        SurveyAssetStore surveyAssetStore = surveyContext.getAssetStore();

        String assetGUID = surveyAssetStore.addCSVFileToCatalog(inventoryName + " for survey report " + surveyReportGUID,
                                                                "Provides an inventory of the resources discovered during the survey.",
                                                                logFile.getCanonicalPath(),
                                                                null,
                                                                ',',
                                                                '"');

        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                    logFileName,
                                                                                    assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SAFAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                   logFileName));
        }

        for (String fileName : nameDescriptions.keySet())
        {
            String row = fileName + "," + nameDescriptions.get(fileName) + "\n";

            FileUtils.writeStringToFile(logFile, row, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Verify that the connector is still active.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private void validateIsActive(String methodName) throws ConnectorCheckedException
    {
        if (! isActive())
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName));
            }

            throw new ConnectorCheckedException(SAFErrorCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }



    /**
     * Retrieve and validate the list of embedded connectors and cast them to survey action service connector.
     * This is used by SurveyPipelines and SurveyScanningServices.
     *
     * @return list of survey action service connectors
     *
     * @throws ConnectorCheckedException one of the embedded connectors is not a survey action service
     */
    protected List<SurveyActionServiceConnector> getEmbeddedSurveyActionServices() throws ConnectorCheckedException
    {
        final String methodName = "getEmbeddedSurveyActionServices";
        
        List<SurveyActionServiceConnector> surveyActionServiceConnectors = null;

        if (embeddedConnectors != null)
        {
            surveyActionServiceConnectors = new ArrayList<>();

            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof SurveyActionServiceConnector)
                    {
                        surveyActionServiceConnectors.add((SurveyActionServiceConnector)embeddedConnector);
                    }
                    else
                    {
                        throw new ConnectorCheckedException(SAFErrorCode.INVALID_EMBEDDED_SURVEY_ACTION_SERVICE.getMessageDefinition(surveyActionServiceName),
                                this.getClass().getName(),
                                methodName);
                    }
                }
            }

            if (surveyActionServiceConnectors.isEmpty())
            {
                surveyActionServiceConnectors = null;
            }
        }

        return surveyActionServiceConnectors;
    }


    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     * This is where the function of the survey action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the survey action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (surveyContext == null)
        {
            throw new ConnectorCheckedException(SAFErrorCode.NULL_SURVEY_CONTEXT.getMessageDefinition(surveyActionServiceName),
                    this.getClass().getName(),
                    methodName);
        }
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Exception   error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SAFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                   error.getClass().getName(),
                                                                                                   methodName,
                                                                                                   error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
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

        if (surveyContext != null)
        {
            surveyContext.disconnect();
        }

        super.disconnect();
    }
}
