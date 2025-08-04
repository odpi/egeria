/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Responsible for adding the certifying that a hospital has completed all the agreements to participate in a clinical trial.
 * This includes:
 * <ul>
 *     <li>Locating the correct certification type for the clinical trial project</li>
 *     <li>Linking the certification type to the hospital using the certification relationship.</li>
 * </ul>
 */
public class CocoClinicalTrialCertifyHospitalService extends CocoClinicalTrialBaseService
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

        String projectGUID             = null;
        String clinicalTrialName       = null;
        String hospitalGUID            = null;
        String hospitalName            = null;
        String certificationTypeGUID   = null;
        String custodianGUID           = null;

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
                            projectGUID = actionTargetElement.getTargetElement().getElementGUID();

                            clinicalTrialName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                 actionTargetElement.getTargetElement().getElementProperties(),
                                                                                 methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalGUID = actionTargetElement.getTargetElement().getElementGUID();
                            hospitalName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                                            actionTargetElement.getTargetElement().getElementProperties(),
                                                                            methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            certificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            custodianGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            List<String>              outputGuards = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;

            if ((projectGUID == null) || (hospitalGUID == null) || (certificationTypeGUID == null) || (custodianGUID == null))
            {
                if (projectGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.PROJECT.getName());
                }
                else if (hospitalGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL.getName());
                }
                else if (certificationTypeGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName());
                }
                else
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                super.checkCertificationValidForProject(projectGUID, certificationTypeGUID);

                this.updateCertificationToHospital(projectGUID,
                                                   clinicalTrialName,
                                                   hospitalGUID,
                                                   hospitalName,
                                                   custodianGUID,
                                                   certificationTypeGUID);

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
     * Create a certification relationship between the hospital and the certification type OR,
     * if the certification is already in place then update the start date to make it valid.
     *
     * @param projectGUID unique identifier of the project
     * @param clinicalTrialName name of the clinical trial
     * @param hospitalGUID unique identifier of the hospital
     * @param hospitalName name of the hospital
     * @param custodianGUID unique identifier of the custodian
     * @param certificationTypeGUID unique identifier of the certification type
     *
     * @throws InvalidParameterException invalid parameter passed somehow
     * @throws PropertyServerException problem connecting to the open metadata repository
     * @throws UserNotAuthorizedException security problem
     */
    private void updateCertificationToHospital(String projectGUID,
                                               String clinicalTrialName,
                                               String hospitalGUID,
                                               String hospitalName,
                                               String custodianGUID,
                                               String certificationTypeGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException, ConnectorCheckedException
    {
        final String methodName = "updateCertificationToHospital";

        int startFrom = 0;
        OpenMetadataRelationshipList existingCertifications = governanceContext.getOpenMetadataStore().getMetadataElementRelationships(hospitalGUID,
                                                                                                                                       certificationTypeGUID,
                                                                                                                                       OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                                                                                       startFrom,
                                                                                                                                       governanceContext.getMaxPageSize());

        if ((existingCertifications == null) || (existingCertifications.getElementList() == null))
        {
            PersonContactDetails custodianContactDetails = super.getContactDetailsForPersonGUID(custodianGUID);

            if (custodianContactDetails != null)
            {
                /*
                 * The hospital is not part of the clinical trial.
                 */
                String title        = "Set up of onboarding pipeline for hospital " + hospitalName + "(" + hospitalGUID + ") failed because this hospital is not nominated for project " + clinicalTrialName + "(" + projectGUID + ")";
                String instructions = "Check with the clinical records clerk if you think this hospital should be included in the clinical trial.  Otherwise ignore this message.";

                governanceContext.openToDo(governanceServiceName + "::" + connectorInstanceId,
                                           title,
                                           instructions,
                                           "Clinical Trial Action",
                                           1,
                                           null,
                                           custodianGUID,
                                           hospitalGUID,
                                           CocoClinicalTrialActionTarget.HOSPITAL.getName());
            }

            throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.HOSPITAL_NOT_NOMINATED.getMessageDefinition(governanceServiceName,
                                                                                                                             hospitalName,
                                                                                                                             hospitalGUID,
                                                                                                                             clinicalTrialName,
                                                                                                                             projectGUID),
                                                this.getClass().getName(),
                                                methodName);
        }
        else
        {
            ElementProperties updatedProperties = propertyHelper.addDateProperty(null,
                                                                                 OpenMetadataProperty.COVERAGE_START.name,
                                                                                 new Date());

            for (OpenMetadataRelationship certification : existingCertifications.getElementList())
            {
                if (certification != null)
                {
                    Date startDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                    OpenMetadataProperty.COVERAGE_START.name,
                                                                    certification.getRelationshipProperties(),
                                                                    methodName);
                    Date endDate = propertyHelper.getDateProperty(governanceServiceName,
                                                                  OpenMetadataProperty.COVERAGE_END.name,
                                                                  certification.getRelationshipProperties(),
                                                                  methodName);

                    if ((endDate == null) && (startDate == null))
                    {
                        governanceContext.getOpenMetadataStore().updateRelatedElementsInStore(certification.getRelationshipGUID(),
                                                                                              governanceContext.getOpenMetadataStore().getUpdateOptions(false),
                                                                                              updatedProperties);
                    }
                }
            }
        }
    }
}
