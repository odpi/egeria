/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
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
public class CocoClinicalTrialHospitalOnboardingService extends GeneralGovernanceActionService
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

        String clinicalTrialId          = null;
        String clinicalTrialName        = null;
        String projectGUID              = null;
        String hospitalGUID             = null;
        String hospitalName             = null;
        String contactName              = null;
        String contactEmail             = null;
        String rawTemplateGUID          = null;
        String landingAreaFolderGUID    = null;
        String landingAreaPathName      = null;
        String newElementProcessName    = null;
        String integrationConnectorGUID = null;

        List<String>     outputGuards = new ArrayList<>();
        CompletionStatus completionStatus;

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
                        else if (CocoClinicalTrialActionTarget.LANDING_AREA_FOLDER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            landingAreaFolderGUID = actionTargetElement.getTargetElement().getElementGUID();
                            landingAreaPathName   = propertyHelper.getStringProperty(governanceServiceName,
                                                                                     OpenMetadataProperty.PATH_NAME.name,
                                                                                     actionTargetElement.getTargetElement().getElementProperties(),
                                                                                     methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.CONTACT_PERSON.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            contactName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                           OpenMetadataProperty.NAME.name,
                                                                           actionTargetElement.getTargetElement().getElementProperties(),
                                                                           methodName);
                            String personGUID = actionTargetElement.getTargetElement().getElementGUID();

                            List<RelatedMetadataElement> contactDetails = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(personGUID,
                                                                                                                                              1,
                                                                                                                                              OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                                                                                                              0,
                                                                                                                                              0);
                            if (contactDetails != null)
                            {
                                for (RelatedMetadataElement contactDetail : contactDetails)
                                {
                                    if (contactDetail != null)
                                    {
                                        contactEmail = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                        OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                                                        contactDetail.getElement().getElementProperties(),
                                                                                        methodName);
                                    }
                                }
                            }
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_TEMPLATE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            rawTemplateGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            integrationConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.NEW_ELEMENT_PROCESS.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            newElementProcessName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                     actionTargetElement.getTargetElement().getElementProperties(),
                                                                                     methodName);
                        }
                    }
                }
            }

            /*
             * Retrieve the template information from the request parameters
             */
            if (governanceContext.getRequestParameters() != null)
            {
                rawTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.HOSPITAL_TEMPLATE.getName());
            }

            if ((clinicalTrialId == null) || (clinicalTrialName == null) || (hospitalName == null) || (contactName == null) || (contactEmail == null) ||
                (clinicalTrialId.isBlank()) || (clinicalTrialName.isBlank()) || (hospitalName.isBlank()) || (contactName.isBlank()) || (contactEmail.isBlank()) ||
                    (rawTemplateGUID==null) || (rawTemplateGUID.isBlank()) || (landingAreaFolderGUID==null) || (landingAreaFolderGUID.isBlank()) ||
                    (landingAreaPathName==null) || (landingAreaPathName.isBlank()))
            {

                if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName()));
                }
                if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName()));
                }
                if ((hospitalName == null) || (hospitalName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getName()));
                }
                if ((contactName == null) || (contactName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getName()));
                }
                if ((contactEmail == null) || (contactEmail.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CONTACT_EMAIL.getName()));
                }
                if ((rawTemplateGUID == null) || (rawTemplateGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialRequestParameter.HOSPITAL_TEMPLATE.getName()));
                }
                if ((landingAreaFolderGUID == null) || (landingAreaFolderGUID.isBlank()) ||
                    (landingAreaPathName==null) || (landingAreaPathName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.LANDING_AREA_FOLDER.getName()));
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                /*
                 * Ensure that the hospital has approval to be a part of this clinical trial.
                 */
                checkHospitalCertification(projectGUID,
                                           clinicalTrialName,
                                           hospitalGUID,
                                           hospitalName);

                /*
                 * This template has the project and hospital information filled out, leaving specifics
                 * for the files to be filled out by the integration connector.
                 */
                String hospitalTemplate = this.createTemplate(rawTemplateGUID,
                                                              hospitalName,
                                                              clinicalTrialId,
                                                              clinicalTrialName,
                                                              contactName,
                                                              contactEmail);

                /*
                 * Create the physical folder
                 */
                this.provisionLandingFolder(landingAreaPathName);

                /*
                 * Listen for new assets and initiate the provisioning workflow
                 */
                this.initiateWatchdog(landingAreaFolderGUID, newElementProcessName);

                /*
                 * Add the catalog target for the new landing are folder and template.
                 */
                this.startMonitoringLandingArea(landingAreaFolderGUID,
                                                integrationConnectorGUID,
                                                hospitalTemplate,
                                                hospitalName,
                                                clinicalTrialId,
                                                clinicalTrialName);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
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
     * Ensure that the hospital is certified.  Begin at the node that represents the hospital and retrieve the
     * certification relationships it has.  For each certification navigate to
     *
     * @param projectGUID the unique identifier of the clinical trail project
     * @param clinicalTrialName name of the clinical trial
     * @param hospitalGUID the unique identifier of the hospital
     * @param hospitalName name of the hospital
     * @throws ConnectorCheckedException no certification
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void checkHospitalCertification(String projectGUID,
                                            String clinicalTrialName,
                                            String hospitalGUID,
                                            String hospitalName) throws ConnectorCheckedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "checkHospitalCertification";

        int startFrom = 0;
        List<RelatedMetadataElement> certifications = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(hospitalGUID,
                                                                                                                          1,
                                                                                                                          OpenMetadataType.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                                                                                                          startFrom,
                                                                                                                          governanceContext.getOpenMetadataStore().getMaxPagingSize());

        while (certifications != null)
        {
            for (RelatedMetadataElement certification : certifications)
            {
                if (certification != null)
                {
                    /*
                     * First check it is an active certification.
                     */
                    Date startDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                    OpenMetadataType.START_PROPERTY_NAME,
                                                                    certification.getRelationshipProperties(),
                                                                    methodName);
                    Date endDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                  OpenMetadataType.END_PROPERTY_NAME,
                                                                  certification.getRelationshipProperties(),
                                                                  methodName);

                    if ((startDate != null) && (endDate == null))
                    {
                        /*
                         * Now check it is the certification for the right project.
                         */
                        int projectStartFrom = 0;
                        List<RelatedMetadataElement> projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certification.getElement().getElementGUID(),
                                                                                                                                    2,
                                                                                                                                    OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                                                                                                                    projectStartFrom,
                                                                                                                                    governanceContext.getMaxPageSize());
                        while (projects != null)
                        {
                            for (RelatedMetadataElement project : projects)
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
                                        return;
                                    }
                                }
                            }

                            projectStartFrom = projectStartFrom + governanceContext.getMaxPageSize();

                            projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certification.getElement().getElementGUID(),
                                                                                                           2,
                                                                                                           OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                                                                                           projectStartFrom,
                                                                                                           governanceContext.getMaxPageSize());
                        }
                    }
                }
            }

            startFrom = startFrom + governanceContext.getMaxPageSize();
            certifications = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(hospitalGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.CERTIFICATION_TYPE_TYPE_NAME,
                                                                                                 startFrom,
                                                                                                 governanceContext.getOpenMetadataStore().getMaxPagingSize());
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
     * Create a new template that partially fills out the placeholder properties with the clinical trial and
     * hospital information.  The placeholder properties left to fill out are FILE_PATH_NAME, FILE_NAME and
     * RECEIVED_DATE.
     *
     * @param rawTemplateGUID unique identifier of the raw template
     * @param hospitalName name of hospital
     * @param clinicalTrialId business identifier of the clinical trial project
     * @param clinicalTrialName display name of the project
     * @param contactName name of the person from the hospital that is the hospital's coordinator for the trial
     * @param contactEmail email of the contact
     * @return qualifiedName of template
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createTemplate(String rawTemplateGUID,
                                  String hospitalName,
                                  String clinicalTrialId,
                                  String clinicalTrialName,
                                  String contactName,
                                  String contactEmail) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "createTemplate";

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName(), clinicalTrialId);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName(), clinicalTrialName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.HOSPITAL_NAME.getName(), hospitalName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CONTACT_NAME.getName(), contactName);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CONTACT_EMAIL.getName(), contactEmail);

        String templateGUID = governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(OpenMetadataType.CSV_FILE.typeName,
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
     * Add the new folder to the integration connector monitoring the landing area.
     *
     * @param folderGUID unique identifier of the folder
     * @param integrationConnectorGUID unique identifier of the integration connector that is to monitor the landing area folder.
     * @param hospitalName hospital name
     * @param clinicalTrialId identifier of the clinical trial
     * @param clinicalTrialName name of the clinical trial
     */
    private void startMonitoringLandingArea(String folderGUID,
                                            String integrationConnectorGUID,
                                            String templateName,
                                            String hospitalName,
                                            String clinicalTrialId,
                                            String clinicalTrialName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName(clinicalTrialId + ":" + clinicalTrialName + ":" + hospitalName);

        Map<String, String> templateProperties = new HashMap<>();
        templateProperties.put(OpenMetadataType.DATA_FILE.typeName, templateName);

        catalogTargetProperties.setTemplateProperties(templateProperties);

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

        if (! landingFolder.mkdir())
        {
            auditLog.logMessage(methodName,
                                GovernanceActionSamplesAuditCode.NO_LANDING_FOLDER.getMessageDefinition(governanceServiceName,
                                                                                                        pathName));
        }
    }


    /**
     * Stat the watchdog process that waits for new files from the hospital
     * @param landingAreaFolderGUID unique identifier of the folder element
     * @param newElementProcessName qualified name of the process to onboard the files into the landing area.
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private void initiateWatchdog(String landingAreaFolderGUID,
                                  String newElementProcessName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String actionTargetName = "watchedFolder";
        final String governanceActionTypeName = "AssetOnboarding:watch-for-new-files-in-folder";
        final String governanceEngineName = "ClinicalTrials@CocoPharmaceuticals";

        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("interestingTypeName", OpenMetadataType.CSV_FILE.typeName);
        requestParameters.put("actionTargetName", "sourceFile");
        requestParameters.put("newElementProcessName", newElementProcessName);

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
