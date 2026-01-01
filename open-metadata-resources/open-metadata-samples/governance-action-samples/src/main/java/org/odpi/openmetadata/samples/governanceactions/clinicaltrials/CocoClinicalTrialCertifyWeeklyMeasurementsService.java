/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.QualityAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionGuard;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;

import java.util.*;
import java.util.regex.PatternSyntaxException;



/**
 * CocoClinicalTrialCertifyWeeklyMeasurementsService is a survey action service implementation for analysing CSV Files to
 * columns and profile the data in them.
 */
public class CocoClinicalTrialCertifyWeeklyMeasurementsService extends SurveyActionServiceConnector
{
    private final static String schemaType  = OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName;


    private Connector connector = null;



    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the survey service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            List<String>              outputGuards      = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;
            String                    certificationTypeGUID = null;
            String                    stewardGUID = null;
            String                    validateDataFilesDataSetGUID = null;
            String                    informationSupplyChainQualifiedName = null;

            AnnotationStore         annotationStore   = surveyContext.getAnnotationStore();

            annotationStore.setAnalysisStep(AnalysisStep.CHECK_ACTION_TARGETS.getName());

            if (surveyContext.getRequestParameters() != null)
            {
                informationSupplyChainQualifiedName = surveyContext.getRequestParameters().get(MoveCopyFileRequestParameter.INFORMATION_SUPPLY_CHAIN_QUALIFIED_NAME.getName());
            }
            /*
             * Retrieve the values needed from the action targets.
             */
            if (surveyContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTargetElement : surveyContext.getActionTargetElements())
                {
                    if (actionTargetElement != null)
                    {
                        if (CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            certificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (ActionTarget.STEWARD.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            stewardGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.VALIDATED_WEEKLY_FILES_DATA_SET.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            validateDataFilesDataSetGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            if (certificationTypeGUID == null)
            {
                messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(surveyActionServiceName,
                                                                                                        CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName());
                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else if (stewardGUID == null)
            {
                messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(surveyActionServiceName,
                                                                                                        CocoClinicalTrialActionTarget.DATA_ENGINEER.getName());
                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else if (validateDataFilesDataSetGUID == null)
            {
                messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(surveyActionServiceName,
                                                                                                        CocoClinicalTrialActionTarget.VALIDATED_WEEKLY_FILES_DATA_SET.getName());
                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else if (informationSupplyChainQualifiedName == null)
            {
                messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(surveyActionServiceName,
                                                                                                        MoveCopyFileRequestParameter.INFORMATION_SUPPLY_CHAIN_QUALIFIED_NAME.getName());
                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                SurveyAssetStore assetStore = surveyContext.getAssetStore();

                /*
                 * The asset should have a special connector for CSV files.  If the connector is wrong,
                 * the cast will fail.
                 */
                connector = super.performCheckAssetAnalysisStep(CSVFileStoreConnector.class,
                                                                OpenMetadataType.CSV_FILE.typeName);


                OpenMetadataRootElement assetUniverse  = assetStore.getAssetProperties();
                CSVFileStoreConnector   assetConnector = (CSVFileStoreConnector) connector;
                long                    recordCount    = assetConnector.getRecordCount();


                QualityAnnotationProperties columnCountAnnotation = new QualityAnnotationProperties();
                QualityAnnotationProperties columnNamesAnnotation = new QualityAnnotationProperties();
                QualityAnnotationProperties patientIdAnnotation   = new QualityAnnotationProperties();
                QualityAnnotationProperties dateAnnotation        = new QualityAnnotationProperties();
                QualityAnnotationProperties angleLeftAnnotation   = new QualityAnnotationProperties();
                QualityAnnotationProperties angleRightAnnotation  = new QualityAnnotationProperties();

                super.setUpAnnotation(columnCountAnnotation, CocoClinicalTrialsAnnotationType.SCHEMA_VALIDATION);
                super.setUpAnnotation(columnNamesAnnotation, CocoClinicalTrialsAnnotationType.SCHEMA_VALIDATION);
                super.setUpAnnotation(patientIdAnnotation, CocoClinicalTrialsAnnotationType.PATIENT_ID_VALIDATION);
                super.setUpAnnotation(dateAnnotation, CocoClinicalTrialsAnnotationType.PATIENT_ID_VALIDATION);
                super.setUpAnnotation(angleLeftAnnotation, CocoClinicalTrialsAnnotationType.ANGLE_VALIDATION);
                super.setUpAnnotation(angleRightAnnotation, CocoClinicalTrialsAnnotationType.ANGLE_VALIDATION);

                String columnCountAnnotationGUID = null;
                String columnNamesAnnotationGUID = null;
                String patientIdAnnotationGUID   = null;
                String dateAnnotationGUID        = null;
                String angleLeftAnnotationGUID   = null;
                String angleRightAnnotationGUID  = null;

                /*
                 * If a schema type is attached, it must be of the correct type so that information from the asset can
                 * be matched to it.
                 */
                annotationStore.setAnalysisStep(AnalysisStep.SCHEMA_VALIDATION.getName());

                String                              schemaTypeGUID;
                List<RelatedMetadataElementSummary> schemaAttributes;

                OpenMetadataRootElement nestedSchemaType = getRootSchemaType(assetUniverse, schemaType);

                if (nestedSchemaType == null)
                {
                    super.throwMissingSchemaType(assetUniverse.getElementHeader().getGUID());
                    return;
                }

                schemaTypeGUID   = nestedSchemaType.getElementHeader().getGUID();
                schemaAttributes = nestedSchemaType.getSchemaAttributes();

                Map<String,String>   expectedColumns      = new HashMap<>();
                Map<Integer, String> expectedColumnNames = new HashMap<>();

                if (schemaAttributes != null)
                {
                    for (RelatedMetadataElementSummary schemaAttribute: schemaAttributes)
                    {
                        if (schemaAttribute.getRelatedElement().getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                        {
                            String displayName = schemaAttributeProperties.getDisplayName();

                            if (displayName != null)
                            {
                                if ((schemaAttribute.getRelationshipProperties() != null) &&
                                        (schemaAttribute.getRelationshipProperties() instanceof AttributeForSchemaProperties attributeForSchemaProperties))
                                {
                                    int position = attributeForSchemaProperties.getPosition();

                                    expectedColumns.put(displayName, schemaAttribute.getRelatedElement().getElementHeader().getGUID());
                                    expectedColumnNames.put(position, displayName);
                                }
                            }
                        }
                    }
                }

                List<String> actualColumnNames = assetConnector.getColumnNames();

                columnCountAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.NO_OF_COLUMNS_MATCH.getDisplayName());
                columnNamesAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.NAMES_OF_COLUMNS_MATCH.getDisplayName());

                if (actualColumnNames == null)
                {
                    columnCountAnnotation.setQualityScore(-expectedColumns.size());
                }
                else
                {
                    columnCountAnnotation.setQualityScore(actualColumnNames.size() - expectedColumns.size());

                    if (columnCountAnnotation.getQualityScore() == 0)
                    {
                        int errorCount = 0;

                        for (int i = 0; i < expectedColumnNames.size(); i++)
                        {
                            if ((expectedColumnNames.get(i+1) == null) || (actualColumnNames.get(i) == null) ||
                                    (!actualColumnNames.get(i).contains(expectedColumnNames.get(i+1))))
                            {
                                errorCount--;
                            }
                        }

                        columnNamesAnnotation.setQualityScore(errorCount);
                    }
                    else if (columnCountAnnotation.getQualityScore() > 0)
                    {
                        columnNamesAnnotation.setQualityScore(-columnCountAnnotation.getQualityScore());
                    }
                    else
                    {
                        columnNamesAnnotation.setQualityScore(columnCountAnnotation.getQualityScore());
                    }
                }

                columnCountAnnotationGUID = annotationStore.addAnnotation(columnCountAnnotation, schemaTypeGUID);
                columnNamesAnnotationGUID = annotationStore.addAnnotation(columnNamesAnnotation, schemaTypeGUID);

                if (columnNamesAnnotation.getQualityScore() == 0)
                {
                    annotationStore.setAnalysisStep(AnalysisStep.DATA_VALIDATION.getName());

                    int patientIdQualityScore = 0;
                    int dateQualityScore = 0;
                    int angleLeftQualityScore = 0;
                    int angleRightQualityScore = 0;

                    for (long recordNumber = 0; recordNumber < recordCount; recordNumber++)
                    {
                        List<String> recordValues = assetConnector.readRecord(recordNumber);

                        if ((recordValues != null) && (!recordValues.isEmpty()))
                        {
                            try
                            {
                                int patientId = Integer.parseInt(recordValues.get(0));

                                if (patientId<1 || patientId > 32)
                                {
                                    patientIdQualityScore--;
                                }
                            }
                            catch (NumberFormatException notInt)
                            {
                                patientIdQualityScore--;
                            }

                            final List<String> validMonths = Arrays.asList(new String[]{"05", "06", "07"});

                            try
                            {
                                String[] dateParts = recordValues.get(1).split("-");

                                if (dateParts.length != 3 || ! "2024".equals(dateParts[0]) || ! validMonths.contains(dateParts[1]))
                                {
                                    dateQualityScore--;
                                }
                            }
                            catch (PatternSyntaxException notDate)
                            {
                                dateQualityScore--;
                            }

                            try
                            {
                                int angle = Integer.parseInt(recordValues.get(2));

                                if (angle<-90 || angle > 90)
                                {
                                    angleLeftQualityScore--;
                                }
                            }
                            catch (NumberFormatException notInt)
                            {
                                angleLeftQualityScore--;
                            }

                            try
                            {
                                int angle = Integer.parseInt(recordValues.get(3));

                                if (angle<-90 || angle > 90)
                                {
                                    angleRightQualityScore--;
                                }
                            }
                            catch (NumberFormatException notInt)
                            {
                                angleRightQualityScore--;
                            }
                        }
                    }

                    /*
                     * Create the annotations
                     */
                    patientIdAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.VALID_PATIENT_ID.getDisplayName());
                    dateAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.VALID_DATE.getDisplayName());
                    angleLeftAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.VALID_LEFT_ANGLE.getDisplayName());
                    angleRightAnnotation.setQualityDimension(CocoClinicalTrialQualityDimension.VALID_RIGHT_ANGLE.getDisplayName());

                    patientIdAnnotation.setQualityScore(patientIdQualityScore);
                    dateAnnotation.setQualityScore(dateQualityScore);
                    angleLeftAnnotation.setQualityScore(angleLeftQualityScore);
                    angleRightAnnotation.setQualityScore(angleRightQualityScore);

                    patientIdAnnotationGUID  = annotationStore.addAnnotation(patientIdAnnotation, expectedColumns.get("PatientId"));
                    dateAnnotationGUID       = annotationStore.addAnnotation(dateAnnotation, expectedColumns.get("Date"));
                    angleLeftAnnotationGUID  = annotationStore.addAnnotation(angleLeftAnnotation, expectedColumns.get("AngleLeft"));
                    angleRightAnnotationGUID = annotationStore.addAnnotation(angleRightAnnotation, expectedColumns.get("AngleRight"));
                }

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_ACTIONS.getName());

                if (columnCountAnnotation.getQualityScore() == 0 &&
                    columnNamesAnnotation.getQualityScore() == 0 &&
                    patientIdAnnotation.getQualityScore() == 0 &&
                    dateAnnotation.getQualityScore() == 0 &&
                    angleLeftAnnotation.getQualityScore() == 0 &&
                    angleRightAnnotation.getQualityScore() == 0)
                {
                    ElementProperties elementProperties = propertyHelper.addDateProperty(null,
                                                                                         OpenMetadataProperty.COVERAGE_START.name,
                                                                                         new Date());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                                         annotationStore.getSurveyReportGUID());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CERTIFIED_BY.name,
                                                                         this.getSurveyContext().getSurveyActionServiceName());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                                         OpenMetadataType.SURVEY_ACTION_SERVICE.typeName);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CUSTODIAN.name,
                                                                         stewardGUID);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                                         OpenMetadataType.PERSON.typeName);

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                                         OpenMetadataProperty.GUID.name);

                    surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                                      assetUniverse.getElementHeader().getGUID(),
                                                                                      certificationTypeGUID,
                                                                                      null,
                                                                                      null,
                                                                                      elementProperties);

                    surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                                      validateDataFilesDataSetGUID,
                                                                                      assetUniverse.getElementHeader().getGUID(),
                                                                                      null,
                                                                                      null,
                                                                                      propertyHelper.addStringProperty(null, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, informationSupplyChainQualifiedName));


                    outputGuards.add(SurveyActionGuard.DATA_CERTIFIED.getName());
                }
                else
                {
                    RequestForActionProperties requestForActionAnnotation = new RequestForActionProperties();

                    super.setUpAnnotation(requestForActionAnnotation, CocoClinicalTrialsAnnotationType.FAILED_TO_PASS_QUALITY_GATE);

                    String annotationGUID = annotationStore.addAnnotation(requestForActionAnnotation, null);

                    annotationStore.linkRequestForActionTarget(annotationGUID, stewardGUID, null);

                    /*
                     * Set up Associated Annotations as appropriate
                     */
                    if (columnCountAnnotation.getQualityScore() != 0 && columnCountAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          columnCountAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                    }
                    if (columnNamesAnnotation.getQualityScore() != 0 && columnNamesAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          columnNamesAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                    }
                    if (patientIdAnnotation.getQualityScore() != 0 && patientIdAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          patientIdAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                    }
                    if (dateAnnotation.getQualityScore() != 0 && dateAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          dateAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                    }
                    if (angleLeftAnnotation.getQualityScore() != 0 && angleLeftAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          angleLeftAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                    }
                    if (angleRightAnnotation.getQualityScore() != 0 && angleRightAnnotationGUID != null)
                    {
                        surveyContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                          angleRightAnnotationGUID,
                                                                                          annotationGUID,
                                                                                          null,
                                                                                          null,
                                                                                          null);
                        outputGuards.add(SurveyActionGuard.DATA_NOT_CERTIFIED.getName());
                    }
                }

                completionStatus = SurveyActionGuard.SURVEY_COMPLETED.getCompletionStatus();
                outputGuards.add(SurveyActionGuard.SURVEY_COMPLETED.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            surveyContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
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
