/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.ClinicalTrialSolutionComponent;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.util.*;

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
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        String genericHospitalNominationGUID            = null;
        String genericHospitalCertificationGUID         = null;
        String genericSetUpDataLakeGUID                 = null;
        String genericHospitalOnboardingGUID            = null;
        String informationSupplyChainTemplateGUID       = null;
        String hospitalCertificationTypeTemplateGUID    = null;
        String dataQualityCertificationTypeTemplateGUID = null;
        String clinicalTrialParentProjectGUID           = null;
        String clinicalTrialId                          = null;
        String clinicalTrialName                        = null;
        String clinicalTrialDescription                 = null;
        String clinicalTrialOwnerGUID                   = null;
        String clinicalTrialManagerGUID                 = null;
        String dataEngineerGUID                         = null;
        String dataScientistGUID                        = null;
        String itProjectManagerGUID                     = null;
        String integrationDeveloperGUID                 = null;
        String lastUpdateConnectorGUID                  = null;
        String landingAreaConnectorGUID                 = null;

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
                        if (CocoClinicalTrialActionTarget.PARENT_PROJECT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            clinicalTrialParentProjectGUID = actionTargetElement.getActionTargetGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN_TEMPLATE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            informationSupplyChainTemplateGUID = "1f71e403-1187-4f03-a1dd-ae7dc105f06f";
                            //informationSupplyChainTemplateGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE_TEMPLATE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalCertificationTypeTemplateGUID = "5acc69bf-dfbb-4e4a-b47b-19d610f2cb06";
                            //hospitalCertificationTypeTemplateGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE_TEMPLATE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataQualityCertificationTypeTemplateGUID = "8a921039-ad5f-454d-ae17-e5a5b69f9333";
                            //dataQualityCertificationTypeTemplateGUID = actionTargetElement.getTargetElement().getElementGUID();
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
                        else if (CocoClinicalTrialActionTarget.CLINICAL_TRIAL_OWNER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            clinicalTrialOwnerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            clinicalTrialManagerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_ENGINEER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataEngineerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.IT_PROJECT_MANAGER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            itProjectManagerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.INTEGRATION_DEVELOPER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            integrationDeveloperGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_SCIENTIST.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataScientistGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            if (governanceContext.getRequestParameters() != null)
            {
                clinicalTrialId          = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_ID.getName());
                clinicalTrialName        = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_NAME.getName());
                clinicalTrialDescription = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_DESCRIPTION.getName());
            }

            List<String>              outputGuards = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;

            if (clinicalTrialParentProjectGUID == null ||
                    clinicalTrialId == null || clinicalTrialId.isBlank() ||
                    clinicalTrialName == null || clinicalTrialName.isBlank() ||
                    clinicalTrialDescription == null || clinicalTrialDescription.isBlank() ||
                    lastUpdateConnectorGUID == null ||
                    landingAreaConnectorGUID == null ||
                    informationSupplyChainTemplateGUID == null ||
                    hospitalCertificationTypeTemplateGUID == null ||
                    dataQualityCertificationTypeTemplateGUID == null ||
                    genericHospitalNominationGUID == null ||
                    genericHospitalCertificationGUID == null ||
                    genericSetUpDataLakeGUID == null ||
                    genericHospitalOnboardingGUID == null ||
                    clinicalTrialOwnerGUID == null ||
                    clinicalTrialManagerGUID == null ||
                    dataEngineerGUID == null ||
                    dataScientistGUID == null ||
                    itProjectManagerGUID == null ||
                    integrationDeveloperGUID == null)
            {
                if (clinicalTrialParentProjectGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.PROJECT.getName());
                }
                else if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_ID.getName());
                }
                else if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_NAME.getName());
                }
                else if ((clinicalTrialDescription == null) || (clinicalTrialDescription.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.CLINICAL_TRIAL_DESCRIPTION.getName());
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
                else if (informationSupplyChainTemplateGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN_TEMPLATE.getName());
                }
                else if (hospitalCertificationTypeTemplateGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE_TEMPLATE.getName());
                }
                else if (dataQualityCertificationTypeTemplateGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE_TEMPLATE.getName());
                }
                else if (clinicalTrialOwnerGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CLINICAL_TRIAL_OWNER.getName());
                }
                else if (clinicalTrialManagerGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName());
                }
                else if (dataEngineerGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.DATA_ENGINEER.getName());
                }
                else if (dataScientistGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.DATA_SCIENTIST.getName());
                }
                else if (itProjectManagerGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.IT_PROJECT_MANAGER.getName());
                }
                else if (integrationDeveloperGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.INTEGRATION_DEVELOPER.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                Map<String, String> projectMap = this.createClinicalTrialProjects(clinicalTrialParentProjectGUID,
                                                                                  clinicalTrialId,
                                                                                  clinicalTrialName,
                                                                                  clinicalTrialDescription,
                                                                                  clinicalTrialOwnerGUID,
                                                                                  clinicalTrialManagerGUID,
                                                                                  dataEngineerGUID,
                                                                                  itProjectManagerGUID,
                                                                                  integrationDeveloperGUID,
                                                                                  dataScientistGUID);

                String              treatmentValidationInformationSupplyChainGUID = this.createInformationSupplyChain(clinicalTrialId, clinicalTrialName, projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()), informationSupplyChainTemplateGUID);
                String              hospitalCertificationTypeGUID                 = setUpCertificationType(clinicalTrialId, projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()), hospitalCertificationTypeTemplateGUID);
                String              dataQualityCertificationTypeGUID              = setUpCertificationType(clinicalTrialId, projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()), dataQualityCertificationTypeTemplateGUID);

                String nominateHospitalGUID = governanceContext.createProcessFromGovernanceActionType("ClinicalTrials::" + clinicalTrialId + "::nominate-hospital",
                                                                                                      "Nominate Hospital (" + clinicalTrialId + ")",
                                                                                                      "Set up the certification, data processing types and license for a hospital so that it may contribute data to the clinical trial.",
                                                                                                      genericHospitalNominationGUID,
                                                                                                      governanceContext.getRequestParameters(),
                                                                                                      null,
                                                                                                      projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));

                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.NOMINATE_HOSPITAL.getGUID(), nominateHospitalGUID, null, "Supports clinical trial " + clinicalTrialId);
                addResourceListRelationship(projectMap.get(CocoClinicalTrialActionTarget.HOSPITAL_MANAGEMENT_PROJECT.getName()), nominateHospitalGUID, ResourceUse.SUPPORTING_PROCESS);

                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName(), clinicalTrialManagerGUID);
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.CLINICAL_TRIAL_OWNER.getName(), clinicalTrialOwnerGUID);
                addActionTargetToProcess(nominateHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);


                String certifyHospitalGUID = governanceContext.createProcessFromGovernanceActionType("ClinicalTrials::" + clinicalTrialId + "::certify-hospital",
                                                                                                     "Certify Hospital (" + clinicalTrialId + ")",
                                                                                                     "Confirms the certification for a hospital so that it may contribute data to the clinical trial.",
                                                                                                     genericHospitalCertificationGUID,
                                                                                                     governanceContext.getRequestParameters(),
                                                                                                     null,
                                                                                                     projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));

                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.CERTIFY_HOSPITAL.getGUID(), certifyHospitalGUID, null, "Supports clinical trial " + clinicalTrialId);
                addResourceListRelationship(projectMap.get(CocoClinicalTrialActionTarget.HOSPITAL_MANAGEMENT_PROJECT.getName()), certifyHospitalGUID, ResourceUse.SUPPORTING_PROCESS);

                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));
                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);
                addActionTargetToProcess(certifyHospitalGUID, CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName(), clinicalTrialManagerGUID);


                String onboardHospitalGUID = governanceContext.createProcessFromGovernanceActionType("ClinicalTrials::" + clinicalTrialId + "::onboard-hospital",
                                                                                                     "Onboard Hospital (" + clinicalTrialId + ")",
                                                                                                     "Set up the onboarding pipeline that take data from a particular hospital and adds it to the data lake.",
                                                                                                     genericHospitalOnboardingGUID,
                                                                                                     governanceContext.getRequestParameters(),
                                                                                                     null,
                                                                                                     projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));

                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.ONBOARD_HOSPITAL.getGUID(), onboardHospitalGUID, null, "Supports clinical trial " + clinicalTrialId);
                addResourceListRelationship(projectMap.get(CocoClinicalTrialActionTarget.ONBOARD_PIPELINE_PROJECT.getName()), onboardHospitalGUID, ResourceUse.SUPPORTING_PROCESS);

                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName(), hospitalCertificationTypeGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName(), dataQualityCertificationTypeGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName(), landingAreaConnectorGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.DATA_ENGINEER.getName(), dataEngineerGUID);
                addActionTargetToProcess(onboardHospitalGUID, CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN.getName(), treatmentValidationInformationSupplyChainGUID);


                String setUpDataLakeProcessGUID = governanceContext.createProcessFromGovernanceActionType("ClinicalTrials::" + clinicalTrialId + "::set-up-data-lake",
                                                                                                          "Set Up Data Lake (" + clinicalTrialId + ")",
                                                                                                          "Set up the data stores for receiving data from the hospitals - this includes the file system directory and Unity Catalog Volume for incoming patient measurements, along with the data set collection for certified measurement files.",
                                                                                                          genericSetUpDataLakeGUID,
                                                                                                          governanceContext.getRequestParameters(),
                                                                                                          null,
                                                                                                          projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));

                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.SET_UP_DATA_LAKE.getGUID(), setUpDataLakeProcessGUID, null, "Supports clinical trial " + clinicalTrialId);
                addResourceListRelationship(projectMap.get(CocoClinicalTrialActionTarget.ONBOARD_PIPELINE_PROJECT.getName()), setUpDataLakeProcessGUID, ResourceUse.SUPPORTING_PROCESS);

                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.PROJECT.getName(), projectMap.get(CocoClinicalTrialActionTarget.PROJECT.getName()));
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.DATA_ENGINEER.getName(), dataEngineerGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName(), dataQualityCertificationTypeGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName(), lastUpdateConnectorGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.ONBOARD_HOSPITAL_PROCESS.getName(), onboardHospitalGUID);
                addActionTargetToProcess(setUpDataLakeProcessGUID, CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN.getName(), treatmentValidationInformationSupplyChainGUID);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (OMFCheckedExceptionBase error)
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
     * The information supply chain template creates three information supply chains.  There is a top level one for
     * the whole clinical trial and two segments for the subject onboarding and the treatment validation
     * @param toplevelInformationSupplyChainGUID new top level information supply chain
     * @return guid of clinical trial treatment validation information supply chain
     */
    private String getTreatmentValidationInformationSupplyChain(String toplevelInformationSupplyChainGUID) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getTreatmentValidationInformationSupplyChain";

        RelatedMetadataElementList relatedMetadataElementList = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(toplevelInformationSupplyChainGUID,
                                                                                                                                    1,
                                                                                                                                    OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                                                    0,
                                                                                                                                    governanceContext.getMaxPageSize());

        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    String qualifiedName = propertyHelper.getStringProperty(governanceServiceName,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            relatedMetadataElement.getElement().getElementProperties(),
                                                                            methodName);

                    if ("Clinical Trial Treatment Validation::".equals(qualifiedName))
                    {
                        return relatedMetadataElement.getElement().getElementGUID();
                    }
                }
            }
        }

        /*
         * Should not get here
         */
        return null;
    }

    /**
     * Set up the information supply chain for this clinical trial.
     *
     * @param clinicalTrialId identifier for the clinical trial
     * @param clinicalTrialName display name for hte clinical trial
     * @param clinicalTrialProjectGUID project for the clinical trial
     * @param informationSupplyChainTemplateGUID template for the information supply chain
     * @return information supply chain element
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createInformationSupplyChain(String clinicalTrialId,
                                                String clinicalTrialName,
                                                String clinicalTrialProjectGUID,
                                                String informationSupplyChainTemplateGUID) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName(), clinicalTrialId);
        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName(), clinicalTrialName);

        String informationSupplyChainGUID = governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                                                                                       null,
                                                                                                                       true,
                                                                                                                       clinicalTrialProjectGUID,
                                                                                                                       null,
                                                                                                                       null,
                                                                                                                       informationSupplyChainTemplateGUID,
                                                                                                                       null,
                                                                                                                       placeholderProperties,
                                                                                                                       clinicalTrialProjectGUID,
                                                                                                                       OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       false);
        if (informationSupplyChainGUID != null)
        {
            return this.getTreatmentValidationInformationSupplyChain(informationSupplyChainGUID);
        }

        /*
         * "Should" not happen!
         */
        return null;
    }


    /**
     * Link the certification type to the project.
     *
     * @param clinicalTrialId identifier of the clinical trial
     * @param clinicalTrialProjectGUID unique identifier of the project
     * @param certificationTypeTemplateGUID unique identifier of the certification type template
     * @return unique identifier of the certification type
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String setUpCertificationType(String clinicalTrialId,
                                          String clinicalTrialProjectGUID,
                                          String certificationTypeTemplateGUID) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName(), clinicalTrialId);

        return governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                                                                          null,
                                                                                          true,
                                                                                          clinicalTrialProjectGUID,
                                                                                          null,
                                                                                          null,
                                                                                          certificationTypeTemplateGUID,
                                                                                          null,
                                                                                          placeholderProperties,
                                                                                          clinicalTrialProjectGUID,
                                                                                          OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                                          null,
                                                                                          false);
    }


    /**
     * Set up the projects and nested tasks.
     *
     * @param parentProjectGUID parent project unique identifier
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param clinicalTrialDescription description of the clinical trial
     * @param processOwnerGUID owner of this clinical trial
     * @param clinicalTrialManagerGUID manager of this clinical trial
     * @param dataEngineerGUID data engineer managing the information flow
     * @param itProjectManagerGUID project manager for new it components
     * @param integrationDeveloperGUID integration developer for new IT components
     * @param dataScientistGUID data scientist for data analysis
     * @return unique identifiers of the projects
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private Map<String, String> createClinicalTrialProjects(String parentProjectGUID,
                                                            String clinicalTrialId,
                                                            String clinicalTrialName,
                                                            String clinicalTrialDescription,
                                                            String processOwnerGUID,
                                                            String clinicalTrialManagerGUID,
                                                            String dataEngineerGUID,
                                                            String itProjectManagerGUID,
                                                            String integrationDeveloperGUID,
                                                            String dataScientistGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, String> projects = new HashMap<>();

        String topLevelProjectGUID = this.createTopLevelProject(parentProjectGUID, clinicalTrialId, clinicalTrialName, clinicalTrialDescription, processOwnerGUID);

        projects.put(CocoClinicalTrialActionTarget.PROJECT.getName(), topLevelProjectGUID);

        String dataSpecProjectGUID = this.createDataSpecProject(topLevelProjectGUID,
                                                                clinicalTrialId,
                                                                clinicalTrialName,
                                                                processOwnerGUID,
                                                                clinicalTrialManagerGUID,
                                                                dataScientistGUID);

        projects.put(CocoClinicalTrialActionTarget.DATA_SPEC_PROJECT.getName(), dataSpecProjectGUID);

        String dataSharingAgreementProjectGUID = createDataSharingAgreementProject(topLevelProjectGUID,
                                                                                   dataSpecProjectGUID,
                                                                                   clinicalTrialId,
                                                                                   clinicalTrialName,
                                                                                   processOwnerGUID,
                                                                                   clinicalTrialManagerGUID);

        projects.put(CocoClinicalTrialActionTarget.DATA_SHARING_AGREEMENT_PROJECT.getName(), dataSharingAgreementProjectGUID);

        String hospitalManagementProjectGUID = this.createHospitalManagementProject(topLevelProjectGUID,
                                                                                    dataSharingAgreementProjectGUID,
                                                                                    clinicalTrialId,
                                                                                    clinicalTrialName,
                                                                                    clinicalTrialManagerGUID);

        projects.put(CocoClinicalTrialActionTarget.HOSPITAL_MANAGEMENT_PROJECT.getName(), hospitalManagementProjectGUID);

        String devProjectGUID = this.createDevProject(topLevelProjectGUID,
                                                      dataSpecProjectGUID,
                                                      clinicalTrialId,
                                                      clinicalTrialName,
                                                      itProjectManagerGUID);

        projects.put(CocoClinicalTrialActionTarget.DEV_PROJECT.getName(), devProjectGUID);

        projects.put(CocoClinicalTrialActionTarget.TEMPLATE_PROJECT.getName(), this.createTemplatesProject(topLevelProjectGUID,
                                                                                                           devProjectGUID,
                                                                                                           clinicalTrialId,
                                                                                                           clinicalTrialName,
                                                                                                           dataEngineerGUID));

        projects.put(CocoClinicalTrialActionTarget.DQ_PROJECT.getName(), this.createDQProject(topLevelProjectGUID,
                                                                                              devProjectGUID,
                                                                                              clinicalTrialId,
                                                                                              clinicalTrialName,
                                                                                              integrationDeveloperGUID));

        String onboardPipelineGUID = this.createOnboardingPipelinesProject(topLevelProjectGUID,
                                                                           dataSpecProjectGUID,
                                                                           clinicalTrialId,
                                                                           clinicalTrialName,
                                                                           dataEngineerGUID);

        projects.put(CocoClinicalTrialActionTarget.ONBOARD_PIPELINE_PROJECT.getName(), onboardPipelineGUID);

        projects.put(CocoClinicalTrialActionTarget.ANALYSIS_PROJECT.getName(), this.createAnalysisProject(topLevelProjectGUID,
                                                                                                          onboardPipelineGUID,
                                                                                                          clinicalTrialId,
                                                                                                          clinicalTrialName,
                                                                                                          dataScientistGUID));

        return projects;
    }


    /**
     * Create the top level project and link it to the clinical trial owner.
     *
     * @param parentProjectGUID overall clinical trial campaign
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param clinicalTrialOwnerGUID overall owner of the clinical trial
     * @return unique identifier for the top level process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createTopLevelProject(String parentProjectGUID,
                                         String clinicalTrialId,
                                         String clinicalTrialName,
                                         String clinicalTrialDescription,
                                         String clinicalTrialOwnerGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::" + clinicalTrialName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, clinicalTrialDescription);
        properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.START_DATE.name, new Date());

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                     ElementStatus.ACTIVE,
                                                                                     null,
                                                                                     parentProjectGUID,
                                                                                     false,
                                                                                     null,
                                                                                     new NewElementProperties(properties),
                                                                                     parentProjectGUID,
                                                                                     OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                     null,
                                                                                     true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialOwnerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.OWNER.getName()));

        return projectGUID;
    }


    /**
     * Create project and collection for data spec.  Link the team and link to parent project.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param clinicalTrialOwnerGUID overall owner of the clinical trial
     * @param clinicalTrialManagerGUID manager of the clinical trial process
     * @param dataScientistGUID analyst for the clinical trial data
     * @return unique identifier for the data spec project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createDataSpecProject(String topLevelProjectGUID,
                                         String clinicalTrialId,
                                         String clinicalTrialName,
                                         String clinicalTrialOwnerGUID,
                                         String clinicalTrialManagerGUID,
                                         String dataScientistGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::DataSpecification");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-SPEC");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Create Data Specification for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Define the specification of the data needed to conduct this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialOwnerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.OWNER.getName()));
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialManagerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.LEADER.getName()));
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              dataScientistGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));

        initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.DATA_SPEC_COLLECTION.typeName, null);

        properties = propertyHelper.addStringProperty(null,
                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                      "Collection::" + clinicalTrialId + "::DataSpecification");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Data Specification for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "The data needed to conduct this clinical trial.");

        String dataSpecGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.COLLECTION.typeName,
                                                                                                    ElementStatus.ACTIVE,
                                                                                                    initialClassifications,
                                                                                                    topLevelProjectGUID,
                                                                                                    false,
                                                                                                    null,
                                                                                                    new NewElementProperties(properties),
                                                                                                    topLevelProjectGUID,
                                                                                                    OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                                                    getResourceUseProperties(ResourceUse.DATA_SPECIFICATION),
                                                                                                    true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              dataSpecGUID,
                                                                              null,
                                                                              null,
                                                                              this.getResourceUseProperties(ResourceUse.DATA_SPECIFICATION));
        return projectGUID;
    }



    /**
     * Create project and collection for data spec.  Link the team and link to parent project.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param dataSpecProjectGUID identifier of the data specification project (dependency)
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param clinicalTrialOwnerGUID overall owner of the clinical trial
     * @param clinicalTrialManagerGUID manager of the clinical trial process
     * @return unique identifier for the data spec project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createDataSharingAgreementProject(String topLevelProjectGUID,
                                                     String dataSpecProjectGUID,
                                                     String clinicalTrialId,
                                                     String clinicalTrialName,
                                                     String clinicalTrialOwnerGUID,
                                                     String clinicalTrialManagerGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::DataSharingAgreement");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-AGREEMENT");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Create Base Data Sharing Agreement for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Define the data sharing agreement offered to hospitals participating in this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialOwnerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.OWNER.getName()));
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialManagerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.LEADER.getName()));

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              dataSpecProjectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "The data specification defines the shape of the data that the data sharing agreement must protect."));
        return projectGUID;
    }


    /**
     * Create the IT development project that is coordinating new components for this project.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param dataSpecProjectGUID data spec project dependency
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param itProjectManager project manager responsible for the IT delivery
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createDevProject(String topLevelProjectGUID,
                                    String dataSpecProjectGUID,
                                    String clinicalTrialId,
                                    String clinicalTrialName,
                                    String itProjectManager) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::ITDevelopment");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-DEV");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Create new components for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Define the data-oriented components to support this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              itProjectManager,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.LEADER.getName()));

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              dataSpecProjectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "The data specification defines the shape of the data that the components need to support."));

        return projectGUID;
    }


    /**
     * Create the project that delivers the new templates for the clinical trial project.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param devProjectGUID parent project
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param dataEngineerGUID data engineer developing the templates
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createTemplatesProject(String topLevelProjectGUID,
                                          String devProjectGUID,
                                          String clinicalTrialId,
                                          String clinicalTrialName,
                                          String dataEngineerGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::TemplatesAndStores");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-TEMPLATES");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Create new templates and data stores for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Define the metadata templates and data store definitions to support this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new NewElementProperties(properties),
                                                                                                   devProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              dataEngineerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));

        return projectGUID;
    }


    /**
     * Create the project that delivers the data quality survey components.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param devProjectGUID parent project
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param integrationDeveloperGUID developer of the new components
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createDQProject(String topLevelProjectGUID,
                                   String devProjectGUID,
                                   String clinicalTrialId,
                                   String clinicalTrialName,
                                   String integrationDeveloperGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::DataQualityComponents");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-DQ");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Create new data quality survey services for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Create the survey action services used to validate the values delivered to this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   topLevelProjectGUID,
                                                                                                   new NewElementProperties(properties),
                                                                                                   devProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              integrationDeveloperGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));

        return projectGUID;
    }


    /**
     * Define the project for hospital management.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param dataSharingAgreementProjectGUID data agreement project dependency
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param clinicalTrialManagerGUID manager of the hospital relationship
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createHospitalManagementProject(String topLevelProjectGUID,
                                                   String dataSharingAgreementProjectGUID,
                                                   String clinicalTrialId,
                                                   String clinicalTrialName,
                                                   String clinicalTrialManagerGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::HospitalManagement");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-HM");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Manage hospitals relationships for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Set up the data sharing agreements and ensure certification and data delivery by the hospitals chosen to support this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   topLevelProjectGUID,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              clinicalTrialManagerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              dataSharingAgreementProjectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "The data specification defines the data needed in the data sharing agreements."));

        return projectGUID;
    }


    /**
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param devProjectGUID component delivery project
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param dataEngineerGUID deployer of the pipelines
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createOnboardingPipelinesProject(String topLevelProjectGUID,
                                                    String devProjectGUID,
                                                    String clinicalTrialId,
                                                    String clinicalTrialName,
                                                    String dataEngineerGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::OnboardingPipelines");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-PIPELINES");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Weekly data onboarding pipelines for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Set up data onboarding pipelines from hospitals to support this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   topLevelProjectGUID,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              dataEngineerGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              devProjectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "The new components are needed to set up the pipelines."));

        return projectGUID;
    }


    /**
     * Set up the analysis task.
     *
     * @param topLevelProjectGUID top level project for this clinical trial
     * @param onboardingPipelinesProjectGUID pipeline deployment project
     * @param clinicalTrialId external identifier for the clinical trial
     * @param clinicalTrialName name of the clinical trial
     * @param dataScientistGUID analyst
     * @return unique identifier of the project
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private String createAnalysisProject(String topLevelProjectGUID,
                                         String onboardingPipelinesProjectGUID,
                                         String clinicalTrialId,
                                         String clinicalTrialName,
                                         String dataScientistGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        initialClassifications.put(OpenMetadataType.TASK_CLASSIFICATION.typeName, null);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Project::" + clinicalTrialId + "::DataAnalysis");

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.IDENTIFIER.name, clinicalTrialId + "-ANALYSIS");
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Data analysis for " + clinicalTrialName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, "Analyse the data from the hospitals to support this clinical trial.");

        String projectGUID = governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.PROJECT.typeName,
                                                                                                   ElementStatus.ACTIVE,
                                                                                                   initialClassifications,
                                                                                                   topLevelProjectGUID,
                                                                                                   false,
                                                                                                   topLevelProjectGUID,
                                                                                                   new NewElementProperties(properties),
                                                                                                   topLevelProjectGUID,
                                                                                                   OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                   null,
                                                                                                   true);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              dataScientistGUID,
                                                                              projectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.CONTRIBUTOR.getName()));
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                              projectGUID,
                                                                              onboardingPipelinesProjectGUID,
                                                                              null,
                                                                              null,
                                                                              propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "The pipelines deliver the data for the analysis."));

        return projectGUID;
    }
}
