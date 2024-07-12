/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String clinicalTrialId   = null;
        String clinicalTrialName = null;
        String hospitalName      = null;
        String contactName       = null;
        String contactDept       = null;

        /*
         * Retrieve the request parameters which define the clinical trial and the hospital being onboarded.
         */
        if (governanceContext.getRequestParameters() != null)
        {
            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            for (String requestParameterName : requestParameters.keySet())
            {
                if (requestParameterName != null)
                {
                    if (CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_ID.getName().equals(requestParameterName))
                    {
                        clinicalTrialId = requestParameters.get(requestParameterName);
                    }
                    else if (CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_NAME.getName().equals(requestParameterName))
                    {
                        clinicalTrialName = requestParameters.get(requestParameterName);
                    }
                    else if (CocoClinicalTrialRequestParameter.HOSPITAL_NAME.getName().equals(requestParameterName))
                    {
                        hospitalName = requestParameters.get(requestParameterName);
                    }
                    else if (CocoClinicalTrialRequestParameter.CONTACT_NAME.getName().equals(requestParameterName))
                    {
                        contactName = requestParameters.get(requestParameterName);
                    }
                    else if (CocoClinicalTrialRequestParameter.CONTACT_DEPT.getName().equals(requestParameterName))
                    {
                        contactDept = requestParameters.get(requestParameterName);
                    }
                }
            }
        }

        List<String>     outputGuards = new ArrayList<>();
        CompletionStatus completionStatus;

        try
        {
            if ((clinicalTrialId == null) || (clinicalTrialName == null) || (hospitalName == null) || (contactName == null) || (contactDept == null) ||
                (clinicalTrialId.isBlank()) || (clinicalTrialName.isBlank()) || (hospitalName.isBlank()) || (contactName.isBlank()) || (contactDept.isBlank()))
            {

                if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionConnectorsAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                           CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_ID.getName()));
                }
                if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionConnectorsAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                           CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_NAME.getName()));
                }
                if ((hospitalName == null) || (hospitalName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionConnectorsAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                           CocoClinicalTrialRequestParameter.HOSPITAL_NAME.getName()));
                }
                if ((contactName == null) || (contactName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionConnectorsAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                           CocoClinicalTrialRequestParameter.CONTACT_NAME.getName()));
                }
                if ((contactDept == null) || (contactDept.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionConnectorsAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                           CocoClinicalTrialRequestParameter.CONTACT_DEPT.getName()));
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                // todo

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
        }
        catch (OCFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    private String createTemplate(String rawTemplateGUID,
                                  String hospitalName,
                                  String clinicalTrialId,
                                  String clinicalTrialName)
    {
        //todo
        return null;
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
        File landingFolder = new File(pathName);

        landingFolder.mkdir();
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
