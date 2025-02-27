/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.BasicFilesMonitoringConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.ClinicalTrialSolutionComponent;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Responsible for setting up the landing area and associated listeners for a new hospital joining a clinical trial.
 * This includes:
 * <ul>
 *     <li>Create a new directory for the incoming files in the landing area.</li>
 *     <li>Creating the file folder for this directory.</li>
 *     <li>Adding a catalog target to the landing area integration connector to catalog new files in the landing area.</li>
 *     <li>Adding a watchdog process to monitor for newly catalogued files in the landing area and initiate the onboarding process.</li>
 * </ul>
 */
public class CocoClinicalTrialHospitalOnboardingService extends CocoClinicalTrialBaseService
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        String clinicalTrialId                  = null;
        String clinicalTrialName                = null;
        String clinicalTrialGUID                = null;
        String projectGUID                      = null;
        String hospitalGUID                     = null;
        String hospitalName                     = null;
        String landingAreaDirectoryTemplateGUID = null;
        String landingAreaFileTemplateGUID      = null;
        String landingAreaPathName              = null;
        String dataLakePathName                 = null;
        String dataLakeFileTemplateGUID         = null;
        String newFileProcessName               = null;
        String integrationConnectorGUID         = null;
        String onboardingProcessGUID            = null;
        String stewardGUID                      = null;
        String hospitalCertificationTypeGUID    = null;
        String dataQualityCertificationTypeGUID = null;

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            /*
             * Retrieve the values needed from the action targets.
             */
            if (governanceContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
                {
                    if (actionTargetElement != null)
                    {
                        if (CocoClinicalTrialActionTarget.PROJECT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            clinicalTrialGUID = actionTargetElement.getTargetElement().getElementGUID();

                            clinicalTrialId = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                               OpenMetadataProperty.IDENTIFIER.name,
                                                                               actionTargetElement.getTargetElement().getElementProperties(),
                                                                               methodName);
                            clinicalTrialName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                 OpenMetadataProperty.NAME.name,
                                                                                 actionTargetElement.getTargetElement().getElementProperties(),
                                                                                 methodName);
                            projectGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                            OpenMetadataProperty.NAME.name,
                                                                            actionTargetElement.getTargetElement().getElementProperties(),
                                                                            methodName);
                            hospitalGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            integrationConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            onboardingProcessGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.STEWARD.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            stewardGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalCertificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataQualityCertificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            /*
             * Retrieve the template information from the request parameters
             */
            if (governanceContext.getRequestParameters() != null)
            {
                landingAreaDirectoryTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                landingAreaPathName = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_PATH_NAME.getName());
                landingAreaFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                dataLakeFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());

                /*
                 * These value are passed on - they are extracted here to provide validation.
                 */
                dataLakePathName = governanceContext.getRequestParameters().get(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName());
            }

            if (clinicalTrialId == null || clinicalTrialName == null || hospitalName == null ||
                clinicalTrialId.isBlank() || clinicalTrialName.isBlank() || hospitalName.isBlank() ||
                    landingAreaFileTemplateGUID==null || landingAreaFileTemplateGUID.isBlank() ||
                    landingAreaDirectoryTemplateGUID==null || landingAreaDirectoryTemplateGUID.isBlank() ||
                    landingAreaPathName==null || landingAreaPathName.isBlank() ||
                    dataLakePathName == null || dataLakePathName.isBlank() ||
                    newFileProcessName == null || newFileProcessName.isBlank() ||
                    dataLakeFileTemplateGUID == null || dataLakeFileTemplateGUID.isBlank() ||
                    onboardingProcessGUID == null || stewardGUID == null || integrationConnectorGUID == null || hospitalCertificationTypeGUID == null || dataQualityCertificationTypeGUID == null)
            {

                if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName());
                }
                else if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName());
                }
                else if ((hospitalName == null) || (hospitalName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getName());
                }
                else if ((landingAreaFileTemplateGUID == null) || (landingAreaFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                }
                else if ((landingAreaDirectoryTemplateGUID == null) || (landingAreaDirectoryTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                }
                else if ((landingAreaPathName == null) || (landingAreaPathName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_PATH_NAME.getName());
                }
                else if ((dataLakeFileTemplateGUID == null) || (dataLakeFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());
                }
                else if ((dataLakePathName == null) || (dataLakePathName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName());
                }
                else if (onboardingProcessGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName());
                }
                else if (stewardGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.STEWARD.getName());
                }
                else if (integrationConnectorGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName());
                }
                else if (hospitalCertificationTypeGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName());
                }
                else if (dataQualityCertificationTypeGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                /*
                 * Ensure that the hospital has approval to be a part of this clinical trial.
                 */
                PersonContactDetails hospitalContactDetails = checkHospitalCertification(projectGUID,
                                                                                         clinicalTrialName,
                                                                                         hospitalGUID,
                                                                                         hospitalName,
                                                                                         hospitalCertificationTypeGUID,
                                                                                         stewardGUID);


                String landingAreaFolderGUID = catalogLandingAreaFolder(hospitalName,
                                                                        landingAreaPathName,
                                                                        landingAreaDirectoryTemplateGUID);

                addSolutionComponentRelationship(ClinicalTrialSolutionComponent.HOSPITAL_LANDING_AREA_FOLDER.getGUID(), landingAreaFolderGUID);
                governanceContext.createLineageRelationship(OpenMetadataType.DATA_FLOW.typeName,
                                                            hospitalGUID,
                                                            informationSupplyChainQualifiedName,
                                                            "publish weekly measurements",
                                                            "A csv file containing the patient measurements for this week is sent from the hospital to the landing area folder.",
                                                            null,
                                                            landingAreaFolderGUID);

                /*
                 * These templates have the project and hospital information filled out, leaving specifics
                 * for the files to be filled out by the integration connector.
                 */
                String hospitalLandingAreaTemplateName = this.createTemplate(landingAreaFileTemplateGUID,
                                                                             hospitalName,
                                                                             clinicalTrialId,
                                                                             clinicalTrialName,
                                                                             hospitalContactDetails);

                String hospitalDataLakeTemplateName = this.createTemplate(dataLakeFileTemplateGUID,
                                                                          hospitalName,
                                                                          clinicalTrialId,
                                                                          clinicalTrialName,
                                                                          hospitalContactDetails);

                /*
                 * Create the physical folder
                 */
                this.provisionLandingFolder(landingAreaPathName);

                newFileProcessName = this.createNewFileProcess(onboardingProcessGUID,
                                                               clinicalTrialId,
                                                               clinicalTrialName,
                                                               hospitalName,
                                                               stewardGUID,
                                                               hospitalDataLakeTemplateName,
                                                               dataQualityCertificationTypeGUID,
                                                               integrationConnectorGUID);

                /*
                 * Create the context event for the data lake folder
                 */
                this.addContextEventToDataLakeFolder(dataLakePathName, clinicalTrialGUID, clinicalTrialId, clinicalTrialName, hospitalName);

                /*
                 * Add the catalog target for the new landing are folder and template.
                 */
                this.startMonitoringLandingArea(landingAreaFolderGUID,
                                                integrationConnectorGUID,
                                                hospitalLandingAreaTemplateName,
                                                hospitalName,
                                                clinicalTrialId,
                                                clinicalTrialName,
                                                newFileProcessName);

                addSolutionComponentRelationship(ClinicalTrialSolutionComponent.LANDING_FOLDER_CATALOGUER.getGUID(), integrationConnectorGUID);
                governanceContext.createLineageRelationship(OpenMetadataType.DATA_FLOW.typeName,
                                                            landingAreaFolderGUID,
                                                            informationSupplyChainQualifiedName,
                                                            "new file notification",
                                                            "The arrival of a new file creates a notification that is sent to the integration connector.",
                                                            null,
                                                            integrationConnectorGUID);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (OCFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Set up a context event for the destination folder to indicate that data is coming from a new source.
     *
     * @param dataLakePathName directory name where the data will be located.
     * @param clinicalTrialGUID unique identifier of the clinical trial (project) element
     * @param clinicalTrialId clinical trial identifier
     * @param clinicalTrialName clinical trial name
     * @param hospitalName name of the newly certified hospital
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void addContextEventToDataLakeFolder(String              dataLakePathName,
                                                 String              clinicalTrialGUID,
                                                 String              clinicalTrialId,
                                                 String              clinicalTrialName,
                                                 String              hospitalName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        String dataLakeFolderGUID = governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(dataLakePathName, OpenMetadataProperty.PATH_NAME.name);

        if (dataLakeFolderGUID != null)
        {
            Map<String, RelationshipProperties> effectedDataSources = new HashMap<>();
            ContextEventProperties              contextEventProperties = new ContextEventProperties();
            String                              contextEventName = "Hospital " + hospitalName + " ready to onboard data into clinical trial " + clinicalTrialId + ": " + clinicalTrialName;

            contextEventProperties.setQualifiedName("ContextEvent:" + contextEventName);
            contextEventProperties.setName(contextEventName);
            contextEventProperties.setEventEffect("Data from a new hospital will be incorporated in the measurements data sets.");
            contextEventProperties.setContextEventType("NewDataFeed");
            contextEventProperties.setActualStartDate(new Date());

            effectedDataSources.put(dataLakeFolderGUID, new RelationshipProperties());

            governanceContext.registerContextEvent(clinicalTrialGUID,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   effectedDataSources,
                                                   null,
                                                   contextEventProperties);
        }
    }


    /**
     * Ensure that the hospital is certified.  Begin at the node that represents the hospital and retrieve the
     * certification relationships it has.  For each certification navigate to
     *
     * @param projectGUID the unique identifier of the clinical trail project
     * @param clinicalTrialName name of the clinical trial
     * @param hospitalGUID the unique identifier of the hospital
     * @param hospitalName name of the hospital
     * @param certificationTypeGUID optional GUID for the certification type
     * @param stewardGUID unique identifier of the steward
     * @return details of the hospital contact extracted from the certification; neve null - but email can be null
     * @throws ConnectorCheckedException no certification
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private PersonContactDetails checkHospitalCertification(String projectGUID,
                                                            String clinicalTrialName,
                                                            String hospitalGUID,
                                                            String hospitalName,
                                                            String certificationTypeGUID,
                                                            String stewardGUID) throws ConnectorCheckedException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "checkHospitalCertification";

        PersonContactDetails custodianContactDetails = null;

        int startFrom = 0;
        RelatedMetadataElementList certifications = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(hospitalGUID,
                                                                                                                          1,
                                                                                                                          OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                                                                          startFrom,
                                                                                                                          governanceContext.getOpenMetadataStore().getMaxPagingSize());

        while ((certifications != null) && (certifications.getElementList() != null))
        {
            for (RelatedMetadataElement certification : certifications.getElementList())
            {
                if (certification != null)
                {
                    if ((certificationTypeGUID == null) || (certificationTypeGUID.equals(certification.getElement().getElementGUID())))
                    {
                        /*
                         * First check it is an active certification.
                         */
                        Date startDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                        OpenMetadataProperty.START.name,
                                                                        certification.getRelationshipProperties(),
                                                                        methodName);
                        Date endDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                      OpenMetadataProperty.END.name,
                                                                      certification.getRelationshipProperties(),
                                                                      methodName);

                        String hospitalContactGUID = propertyHelper.getStringProperty(governanceServiceName,
                                                                                      OpenMetadataProperty.RECIPIENT.name,
                                                                                      certification.getRelationshipProperties(),
                                                                                      methodName);

                        String custodianGUID = propertyHelper.getStringProperty(governanceServiceName,
                                                                                OpenMetadataProperty.CUSTODIAN.name,
                                                                                certification.getRelationshipProperties(),
                                                                                methodName);

                        PersonContactDetails hospitalContactDetails = super.getContactDetailsForPersonGUID(hospitalContactGUID);

                        if (custodianGUID != null)
                        {
                            custodianContactDetails = super.getContactDetailsForPersonGUID(custodianGUID);
                        }

                        if ((startDate != null) && (endDate == null))
                        {
                            /*
                             * Now check it is the certification for the right project.
                             */
                            int projectStartFrom = 0;
                            RelatedMetadataElementList projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certification.getElement().getElementGUID(),
                                                                                                                                        1,
                                                                                                                                        OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                                                                                        projectStartFrom,
                                                                                                                                        governanceContext.getMaxPageSize());
                            while ((projects != null) && (projects.getElementList() != null))
                            {
                                for (RelatedMetadataElement project : projects.getElementList())
                                {
                                    if (project != null)
                                    {
                                        if (projectGUID.equals(project.getElement().getElementGUID()))
                                        {
                                            /*
                                             * Hospital has correct certification.
                                             */
                                            auditLog.logMessage(methodName,
                                                                GovernanceActionSamplesAuditCode.CERTIFIED_HOSPITAL.getMessageDefinition(governanceServiceName,
                                                                                                                                         hospitalName,
                                                                                                                                         hospitalGUID,
                                                                                                                                         clinicalTrialName,
                                                                                                                                         projectGUID));
                                            return hospitalContactDetails;
                                        }
                                    }
                                }

                                projectStartFrom = projectStartFrom + governanceContext.getMaxPageSize();

                                projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certification.getElement().getElementGUID(),
                                                                                                               2,
                                                                                                               OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                                                               projectStartFrom,
                                                                                                               governanceContext.getMaxPageSize());
                            }
                        }
                    }
                }
            }

            startFrom = startFrom + governanceContext.getMaxPageSize();
            certifications = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(hospitalGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                                                                                 startFrom,
                                                                                                 governanceContext.getOpenMetadataStore().getMaxPagingSize());
        }

        PersonContactDetails stewardContactDetails = super.getContactDetailsForPersonGUID(stewardGUID);

        if (stewardContactDetails != null)
        {
            /*
             * The hospital is part of the project but has not completed its certification process.
             */
            if (custodianContactDetails != null)
            {
                String title        = "Set up of onboarding pipeline for hospital " + hospitalName + "(" + hospitalGUID + ") failed because this hospital is not certified for project " + clinicalTrialName + "(" + projectGUID + ")";
                String instructions = "Contact the clinical records clerk for this project to resolve the certification status of the hospital.  Once it is resolved, rerun this onboarding process.";
                instructions = instructions + "\n\n Contact Details:\n\n * Name: " + custodianContactDetails.contactName + "\n * Email: " + custodianContactDetails.contactEmail + "\n * GUID: " + custodianContactDetails.personGUID;

                governanceContext.openToDo("From " + governanceServiceName + ":" + connectorInstanceId + " to " + stewardGUID,
                                           title,
                                           instructions,
                                           "Clinical Trial Action",
                                           1,
                                           null,
                                           stewardGUID,
                                           hospitalGUID,
                                           CocoClinicalTrialActionTarget.HOSPITAL.getName());

                instructions = "Determine whether this hospital is to be included in the clinical trial.  If it is to be included, review the certification status and contact the hospital to complete the certification process.  Contact the steward to keep them informed of the hospital's status";
                instructions = instructions + "\n\n Contact Details for Steward:\n\n * Name: " + stewardContactDetails.contactName + "\n * Email: " + stewardContactDetails.contactEmail + "\n * GUID: " + stewardContactDetails.personGUID;

                governanceContext.openToDo("From " + governanceServiceName + ":" + connectorInstanceId + " to " + custodianContactDetails.personGUID,
                                           title,
                                           instructions,
                                           "Clinical Trial Action",
                                           1,
                                           null,
                                           custodianContactDetails.personGUID,
                                           hospitalGUID,
                                           CocoClinicalTrialActionTarget.HOSPITAL.getName());
            }
            else
            {
                /*
                 * The hospital is not part of the clinical trial.
                 */
                String title        = "Set up of onboarding pipeline for hospital " + hospitalName + "(" + hospitalGUID + ") failed because this hospital is not nominated for project " + clinicalTrialName + "(" + projectGUID + ")";
                String instructions = "Check with the clinical records clerk if you think this hospital should be included in the clinical trial.  Otherwise ignore this message.";

                governanceContext.openToDo("From " + governanceServiceName + ":" + connectorInstanceId + " to " + stewardGUID,
                                           title,
                                           instructions,
                                           "Clinical Trial Action",
                                           1,
                                           null,
                                           stewardGUID,
                                           hospitalGUID,
                                           CocoClinicalTrialActionTarget.HOSPITAL.getName());
            }
        }

        throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.UNCERTIFIED_HOSPITAL.getMessageDefinition(governanceServiceName,
                                                                                                                       hospitalName,
                                                                                                                       hospitalGUID,
                                                                                                                       clinicalTrialName,
                                                                                                                       projectGUID),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Create a FileFolder for the hospital landing are.
     *
     * @param hospitalName name of hospital
     * @param landingAreaPathName path name for the landing area
     * @param landingAreaDirectoryTemplateGUID template to use
     * @return unique identifier of the landing area
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String catalogLandingAreaFolder(String hospitalName,
                                            String landingAreaPathName,
                                            String landingAreaDirectoryTemplateGUID) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        Map<String, String> placeholderPropertyValues = new HashMap<>();

        placeholderPropertyValues.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), landingAreaPathName);
        placeholderPropertyValues.put(PlaceholderProperty.DIRECTORY_NAME.getName(), "drop-foot");
        placeholderPropertyValues.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholderPropertyValues.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), "");
        placeholderPropertyValues.put(PlaceholderProperty.DESCRIPTION.getName(), "Landing Area folder for" + hospitalName + "'s Teddy Bear Drop Foot clinical trial.");

        return governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(OpenMetadataType.FILE_FOLDER.typeName,
                                                                                       null,
                                                                                       true,
                                                                                       null,
                                                                                       null,
                                                                                       landingAreaDirectoryTemplateGUID,
                                                                                       null,
                                                                                       placeholderPropertyValues,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       true);

    }


    /**
     * Create a new template that partially fills out the placeholder properties with the clinical trial and
     * hospital information.  The placeholder properties left to fill out are FILE_PATH_NAME, FILE_NAME and
     * RECEIVED_DATE.
     *
     * @param rawTemplateGUID unique identifier of the raw template
     * @param hospitalName name of hospital
     * @param clinicalTrialId business identifier of the clinical trial project
     * @param clinicalTrialName display name of the project
     * @param hospitalContactDetails name and email of the person from the hospital that is the hospital's coordinator for the trial
     * @return  template
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createTemplate(String               rawTemplateGUID,
                                  String               hospitalName,
                                  String               clinicalTrialId,
                                  String               clinicalTrialName,
                                  PersonContactDetails hospitalContactDetails) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "createTemplate";

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName(), clinicalTrialId);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName(), clinicalTrialName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getName(), hospitalName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getName(), hospitalContactDetails.contactName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CONTACT_EMAIL.getName(), hospitalContactDetails.contactEmail);

        String templateGUID = governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(OpenMetadataType.CSV_FILE.typeName,
                                                                                                      null,
                                                                                                      true,
                                                                                                      null,
                                                                                                      null,
                                                                                                      rawTemplateGUID,
                                                                                                      null,
                                                                                                      placeholderProperties,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      true);

        OpenMetadataElement templateElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(templateGUID);

        return propertyHelper.getStringProperty(templateGUID,
                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                templateElement.getElementProperties(),
                                                methodName);
    }


    /**
     * Create a new process for onboarding files from a specific hospital.  This is effectively a facade that
     * adds the specific action targets and templates (request parameters) for the hospital.
     *
     * @param onboardingProcessGUID unique identifier of the generic process
     * @param clinicalTrialId identifier of the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param hospitalName name of hospital
     * @param stewardGUID unique identifier of the steward for the project
     * @param dataLakeTemplateName qualified name
     * @param dataQualityCertificationType certification type used in quality analysis
     * @param integrationConnectorGUID unique identifier of the integration connector
     */
    private String createNewFileProcess(String onboardingProcessGUID,
                                        String clinicalTrialId,
                                        String clinicalTrialName,
                                        String hospitalName,
                                        String stewardGUID,
                                        String dataLakeTemplateName,
                                        String dataQualityCertificationType,
                                        String integrationConnectorGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "createNewFileProcess";

        String processQualifiedName = "Coco:GovernanceActionProcess:" + clinicalTrialId + ":" + hospitalName + ":WeeklyMeasurements:Onboarding";

        OpenMetadataElement genericProcess = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(onboardingProcessGUID);

        if (genericProcess != null)
        {
            String processGUID = super.createGovernanceActionProcess(processQualifiedName,
                                                                     "Onboard Landing Area Files for " + clinicalTrialName,
                                                                     null);

            addSolutionComponentRelationship(ClinicalTrialSolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE.getGUID(), processGUID);
            governanceContext.createLineageRelationship(OpenMetadataType.DATA_FLOW.typeName,
                                                        integrationConnectorGUID,
                                                        informationSupplyChainQualifiedName,
                                                        "new file trigger",
                                                        "The arrival of a new file creates an instance of the onboarding pipeline process.",
                                                        null,
                                                        processGUID);

            RelatedMetadataElement firstProcessStep = governanceContext.getOpenMetadataStore().getRelatedMetadataElement(onboardingProcessGUID,
                                                                                                                         1,
                                                                                                                         OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                                                                         new Date());

            if (firstProcessStep != null)
            {
                ElementProperties relationshipProperties = new ElementProperties(firstProcessStep.getRelationshipProperties());

                Map<String, String> requestParameters = propertyHelper.getStringMapFromProperty(governanceServiceName,
                                                                                                OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                relationshipProperties,
                                                                                                methodName);
                if (requestParameters == null)
                {
                    requestParameters = new HashMap<>();
                }

                requestParameters.put(MoveCopyFileRequestParameter.TARGET_FILE_NAME_PATTERN.getName(), clinicalTrialId + "_{1, number,000000}.csv");
                requestParameters.put("publishZones", "data-lake,clinical-trials");
                requestParameters.put(MoveCopyFileRequestParameter.DESTINATION_TEMPLATE_NAME.getName(), dataLakeTemplateName);
                requestParameters.put(MoveCopyFileRequestParameter.INFORMATION_SUPPLY_CHAIN.getName(), informationSupplyChainQualifiedName);

                relationshipProperties = propertyHelper.addStringMapProperty(relationshipProperties,
                                                                             OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                             requestParameters);

                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                                      processGUID,
                                                                                      firstProcessStep.getElement().getElementGUID(),
                                                                                      null,
                                                                                      null,
                                                                                      relationshipProperties);

                ElementProperties actionTargetProperties = propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                            ActionTarget.STEWARD.getName());

                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_ACTION_PROCESS.typeName,
                                                                                      processGUID,
                                                                                      stewardGUID,
                                                                                      null,
                                                                                      null,
                                                                                      actionTargetProperties);
                actionTargetProperties = propertyHelper.addStringProperty(null,
                                                                          OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                          CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName());

                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_ACTION_PROCESS.typeName,
                                                                                      processGUID,
                                                                                      dataQualityCertificationType,
                                                                                      null,
                                                                                      null,
                                                                                      actionTargetProperties);
            }
        }

        return processQualifiedName;
    }


    /**
     * Add the new folder to the integration connector monitoring the landing area.
     *
     * @param folderGUID unique identifier of the folder
     * @param integrationConnectorGUID unique identifier of the integration connector that is to monitor the landing area folder.
     * @param hospitalName hospital name
     * @param clinicalTrialId identifier of the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param newFileProcessName qualified name of the process to onboard the files from the landing area.
     */
    private void startMonitoringLandingArea(String folderGUID,
                                            String integrationConnectorGUID,
                                            String templateName,
                                            String hospitalName,
                                            String clinicalTrialId,
                                            String clinicalTrialName,
                                            String newFileProcessName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName(clinicalTrialId + ":" + clinicalTrialName + ":" + hospitalName);

        Map<String, String> templateProperties = new HashMap<>();
        templateProperties.put(OpenMetadataType.DATA_FILE.typeName, templateName);
        templateProperties.put(OpenMetadataType.CSV_FILE.typeName, templateName);

        catalogTargetProperties.setTemplateProperties(templateProperties);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(BasicFilesMonitoringConfigurationProperty.NEW_FILE_PROCESS_NAME.getName(), newFileProcessName);

        if (governanceContext.getRequestParameters() != null)
        {
            for (String requestParameter : governanceContext.getRequestParameters().keySet())
            {
                configurationProperties.put(requestParameter, governanceContext.getRequestParameters().get(requestParameter));
            }
        }

        catalogTargetProperties.setConfigurationProperties(configurationProperties);

        governanceContext.addCatalogTarget(integrationConnectorGUID,
                                           folderGUID,
                                           catalogTargetProperties);
    }


    /**
     * Create the landing area folder.
     *
     * @param pathName landing area folder name
     */
    private void provisionLandingFolder(String pathName)
    {
        final String methodName = "provisionLandingFolder";

        File landingFolder = new File(pathName);

        if (! landingFolder.exists())
        {
            try
            {
                FileUtils.forceMkdir(landingFolder);
            }
            catch (IOException error)
            {
                auditLog.logMessage(methodName,
                                    GovernanceActionSamplesAuditCode.NO_LANDING_FOLDER.getMessageDefinition(governanceServiceName,
                                                                                                            pathName));
            }
        }
    }


    /**
     * Stat the watchdog process that waits for new files from the hospital
     * @param landingAreaFolderGUID unique identifier of the folder element
     * @param dataLakeTemplateName qualified name
     * @param newElementProcessName qualified name of the process to onboard the files into the landing area.
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private void initiateWatchdog(String landingAreaFolderGUID,
                                  String dataLakeTemplateName,
                                  String newElementProcessName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String actionTargetName = "watchedFolder";
        final String governanceActionTypeName = "AssetOnboarding:watch-for-new-files-in-folder";
        final String governanceEngineName = "ClinicalTrials@CocoPharmaceuticals";

        Map<String, String> requestParameters = governanceContext.getRequestParameters();

        if (requestParameters == null)
        {
            requestParameters = new HashMap<>();
        }
        requestParameters.put("interestingTypeName", OpenMetadataType.CSV_FILE.typeName);
        requestParameters.put("actionTargetName", "sourceFile");
        requestParameters.put("newElementProcessName", newElementProcessName);
        requestParameters.put("targetFileNamePattern", "DropFoot_{1, number,000000}.csv");
        requestParameters.put("publishZones", "data-lake,clinical-trials");
        requestParameters.put(MoveCopyFileRequestParameter.DESTINATION_TEMPLATE_NAME.getName(), dataLakeTemplateName);

        NewActionTarget newActionTarget = new NewActionTarget();
        newActionTarget.setActionTargetName(actionTargetName);
        newActionTarget.setActionTargetGUID(landingAreaFolderGUID);

        List<NewActionTarget> actionTargets = new ArrayList<>();

        actionTargets.add(newActionTarget);
        governanceContext.initiateGovernanceActionType(governanceActionTypeName,
                                                       null,
                                                       actionTargets,
                                                       null,
                                                       requestParameters,
                                                       governanceServiceName,
                                                       governanceEngineName);
    }

}
