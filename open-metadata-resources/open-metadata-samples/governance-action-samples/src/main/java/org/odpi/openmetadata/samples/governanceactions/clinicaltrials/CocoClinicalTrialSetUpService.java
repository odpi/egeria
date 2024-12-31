/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Responsible for setting up the processes that support the smooth operation of a clinical trial.
 * This includes:
 * <ul>
 *     <li>Establishing the GovernedBy relationship between the project and the certification type.</li>
 *     <li>Creating governance action processes for nominating hospitals, certifying them, setting up the data lake (UC) and creating the data onboarding pipelines.</li>
 * </ul>
 */
public class CocoClinicalTrialSetUpService extends CocoClinicalTrialBaseService
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

        String genericHospitalNominationGUID    = null;
        String genericHospitalCertificationGUID = null;
        String genericSetUpDataLakeGUID         = null;
        String genericHospitalOnboardingGUID    = null;
        String hospitalCertificationTypeGUID    = null;
        String dataQualityCertificationTypeGUID = null;
        String clinicalTrialProjectGUID         = null;
        String clinicalTrialId                  = null;
        String clinicalTrialName                = null;
        String processOwnerGUID                 = null;
        String custodianGUID                    = null;
        String stewardGUID                      = null;
        String genericOnboardingPipelineGUID    = null;
        String lastUpdateConnectorGUID          = null;
        String landingAreaConnectorGUID         = null;
        String landingAreaDirectoryTemplateGUID = null;
        String landingAreaFileTemplateGUID      = null;
        String dataLakeFileTemplateGUID         = null;

        super.start();

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
                            clinicalTrialProjectGUID = actionTargetElement.getActionTargetGUID();
                            clinicalTrialId = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                               OpenMetadataProperty.IDENTIFIER.name,
                                                                               actionTargetElement.getTargetElement().getElementProperties(),
                                                                               methodName);
                            clinicalTrialName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                 OpenMetadataProperty.NAME.name,
                                                                                 actionTargetElement.getTargetElement().getElementProperties(),
                                                                                 methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalCertificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataQualityCertificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_SET_UP_DATA_LAKE_GAT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericSetUpDataLakeGUID = actionTargetElement.getActionTargetGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_CERTIFICATION_GAT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericHospitalCertificationGUID = actionTargetElement.getActionTargetGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_NOMINATION_GAT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericHospitalNominationGUID = actionTargetElement.getActionTargetGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_ONBOARDING_GAT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericHospitalOnboardingGUID = actionTargetElement.getActionTargetGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            lastUpdateConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            landingAreaConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.PROCESS_OWNER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            processOwnerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.CUSTODIAN.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            custodianGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.STEWARD.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            stewardGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericOnboardingPipelineGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            /*
             * Retrieve the data lake information from the request parameters
             */
            if (governanceContext.getRequestParameters() != null)
            {
                landingAreaDirectoryTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                landingAreaFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                dataLakeFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());
            }

            List<String>              outputGuards = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;

            if (clinicalTrialProjectGUID == null ||
                    clinicalTrialId == null || clinicalTrialId.isBlank() ||
                    clinicalTrialName == null || clinicalTrialName.isBlank() ||
                    lastUpdateConnectorGUID == null ||
                    landingAreaConnectorGUID == null ||
                    hospitalCertificationTypeGUID == null ||
                    dataQualityCertificationTypeGUID == null ||
                    genericHospitalNominationGUID == null ||
                    genericHospitalCertificationGUID == null ||
                    genericSetUpDataLakeGUID == null ||
                    genericHospitalOnboardingGUID == null ||
                    processOwnerGUID == null ||
                    custodianGUID == null ||
                    stewardGUID == null ||
                    genericOnboardingPipelineGUID == null ||
                    landingAreaDirectoryTemplateGUID == null || landingAreaDirectoryTemplateGUID.isBlank() ||
                    landingAreaFileTemplateGUID == null || landingAreaFileTemplateGUID.isBlank() ||
                    dataLakeFileTemplateGUID == null || dataLakeFileTemplateGUID.isBlank())
            {
                if (clinicalTrialProjectGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.PROJECT.getName());
                }
                else if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName());
                }
                else if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName());
                }
                else if (lastUpdateConnectorGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName());
                }
                else if (landingAreaConnectorGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName());
                }
                else if (genericHospitalNominationGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_NOMINATION_GAT.getName());
                }
                else if (genericHospitalCertificationGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_CERTIFICATION_GAT.getName());
                }
                else if (genericSetUpDataLakeGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_SET_UP_DATA_LAKE_GAT.getName());
                }
                else if (genericHospitalOnboardingGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_ONBOARDING_GAT.getName());
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
                else if (genericOnboardingPipelineGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName());
                }
                else if (processOwnerGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.PROCESS_OWNER.getName());
                }
                else if (custodianGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CUSTODIAN.getName());
                }
                else if (stewardGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.STEWARD.getName());
                }
                else if ((landingAreaDirectoryTemplateGUID == null) || (landingAreaDirectoryTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                }
                else if ((landingAreaFileTemplateGUID == null) || (landingAreaFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                }
                else if ((dataLakeFileTemplateGUID == null) || (dataLakeFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                setUpCertificationType(clinicalTrialProjectGUID, hospitalCertificationTypeGUID);


                String nominateHospitalGUID = this.createProcessFromGovernanceActionType("ClinicalTrials:" + clinicalTrialId + ":nominate-hospital",
                                                                                         "Nominate Hospital",
                                                                                         "Set up the certification, data processing types and license for a hospital so that it may contribute data to the clinical trial.",
                                                                                         genericHospitalNominationGUID,
                                                                                         governanceContext.getRequestParameters());

                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), clinicalTrialProjectGUID);
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.CUSTODIAN.getName(), custodianGUID);
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.PROCESS_OWNER.getName(), processOwnerGUID);
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);

                String certifyHospitalGUID = this.createProcessFromGovernanceActionType("ClinicalTrials:" + clinicalTrialId + ":certify-hospital",
                                                                                        "Certify Hospital",
                                                                                        "Confirms the certification for a hospital so that it may contribute data to the clinical trial.",
                                                                                        genericHospitalCertificationGUID,
                                                                                        governanceContext.getRequestParameters());

                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), clinicalTrialProjectGUID);
                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);
                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.CUSTODIAN.getName(), custodianGUID);


                String onboardHospitalGUID = this.createProcessFromGovernanceActionType("ClinicalTrials:" + clinicalTrialId + ":onboard-hospital",
                                                                                        "Onboard Hospital",
                                                                                        "Set up the onboarding pipeline that take data from a particular hospital and adds it to the data lake.",
                                                                                        genericHospitalOnboardingGUID,
                                                                                        governanceContext.getRequestParameters());

                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), clinicalTrialProjectGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName(), dataQualityCertificationTypeGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName(), landingAreaConnectorGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName(), genericOnboardingPipelineGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.STEWARD.getName(), stewardGUID);


                String setUpDataLakeProcessGUID = this.createProcessFromGovernanceActionType("ClinicalTrials:" + clinicalTrialId + ":set-up-data-lake",
                                                                                             "Set Up Data Lake",
                                                                                             "Create the schema and volume in Unity Catalog (UC) for the clinical trial",
                                                                                             genericSetUpDataLakeGUID,
                                                                                             governanceContext.getRequestParameters());


                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), clinicalTrialProjectGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.STEWARD.getName(), stewardGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName(), lastUpdateConnectorGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.ONBOARD_HOSPITAL_PROCESS.getName(), onboardHospitalGUID);




                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
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
     * Link the certification type to the project.
     *
     * @param projectGUID unique identifier of the project
     * @param certificationTypeGUID unique identifier of the certification type
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void setUpCertificationType(String projectGUID,
                                        String certificationTypeGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                                                              certificationTypeGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              null);
    }


    /**
     * Create a specific governance action process for the clinical trial from the generic governance action type.
     *
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @param governanceActionTypeGUID the unique identifier of the governance action type
     * @param additionalRequestParameters the additional, predefined request parameters to add to the
     *                                   GovernanceActionProcessFlow relationship
     * @return unique identifier of new governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createProcessFromGovernanceActionType(String              processQualifiedName,
                                                         String              processName,
                                                         String              processDescription,
                                                         String              governanceActionTypeGUID,
                                                         Map<String, String> additionalRequestParameters) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        String processGUID = this.createGovernanceActionProcess(processQualifiedName, processName, processDescription);

        OpenMetadataElement governanceActionType = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(governanceActionTypeGUID);

        if (governanceActionType != null)
        {
            RelatedMetadataElement governanceActionExecutorRelationship = governanceContext.getOpenMetadataStore().getRelatedMetadataElement(governanceActionTypeGUID,
                                                                                                                                             1,
                                                                                                                                             OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                                                                                             new Date());

            if (governanceActionExecutorRelationship != null)
            {
                String governanceEngineGUID = governanceActionExecutorRelationship.getElement().getElementGUID();

                ElementProperties processStepProperties = propertyHelper.addStringProperty(governanceActionType.getElementProperties(),
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                           processQualifiedName + ":processStep1");

                ElementProperties processFlowProperties = propertyHelper.addStringMapProperty(null,
                                                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                              additionalRequestParameters);

                String processStep1GUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                                ElementStatus.ACTIVE,
                                                                                                                null,
                                                                                                                processGUID,
                                                                                                                false,
                                                                                                                null,
                                                                                                                null,
                                                                                                                processStepProperties,
                                                                                                                processGUID,
                                                                                                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME,
                                                                                                                processFlowProperties,
                                                                                                                true);

                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                                      processStep1GUID,
                                                                                      governanceEngineGUID,
                                                                                      null,
                                                                                      null,
                                                                                      governanceActionExecutorRelationship.getRelationshipProperties());


                RelatedMetadataElementList actionTargets = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(governanceActionTypeGUID,
                                                                                                                               1,
                                                                                                                               OpenMetadataType.TARGET_FOR_ACTION_TYPE.typeName,
                                                                                                                               0,
                                                                                                                               0);

                if ((actionTargets != null) && (actionTargets.getElementList() != null))
                {
                    for (RelatedMetadataElement actionTarget : actionTargets.getElementList())
                    {
                        if (actionTarget != null)
                        {
                            governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_ACTION_PROCESS.typeName,
                                                                                                  processGUID,
                                                                                                  actionTarget.getElement().getElementGUID(),
                                                                                                  null,
                                                                                                  null,
                                                                                                  actionTarget.getRelationshipProperties());
                        }
                    }
                }


                RelatedMetadataElementList specifications = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(governanceActionTypeGUID,
                                                                                                                                 1,
                                                                                                                                 OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                                                 0,
                                                                                                                                 0);

                if ((specifications != null) && (specifications.getElementList() != null))
                {
                    for (RelatedMetadataElement specification : specifications.getElementList())
                    {
                        if (specification != null)
                        {
                            governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                  processGUID,
                                                                                                  specification.getElement().getElementGUID(),
                                                                                                  null,
                                                                                                  null,
                                                                                                  specification.getRelationshipProperties());
                        }
                    }
                }
            }
        }

        return processGUID;
    }


    /**
     * Link an action target to a process.  This action target will be available for all steps in the process.
     *
     * @param processGUID unique identifier of the process
     * @param actionTargetName name of the target for action relationship
     * @param actionTargetGUID unique identifier of the target
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void addActionTargetToProcess(String processGUID,
                                          String actionTargetName,
                                          String actionTargetGUID) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_ACTION_PROCESS.typeName,
                                                                              processGUID,
                                                                              actionTargetGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null,
                                                                                                               OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                               actionTargetName));
    }
}
