/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Responsible for adding the certifying that a hospital has completed all the agreements to participate in a clinical trial.
 * This includes:
 * <ul>
 *     <li>Locating the correct certification type for the clinical trial project</li>
 *     <li>Linking the certification type to the hospital using the certification relationship.</li>
 * </ul>
 */
public class CocoClinicalTrialCertifyHospitalService extends GeneralGovernanceActionService
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

        String projectGUID           = null;
        String hospitalGUID          = null;
        String certificationTypeGUID = null;
        String dataOwnerGUID         = null;
        String custodianGUID         = null;
        String hospitalContactGUID   = null;

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
                        }
                        if (CocoClinicalTrialActionTarget.HOSPITAL.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        if (CocoClinicalTrialActionTarget.CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            certificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        if (CocoClinicalTrialActionTarget.CUSTODIAN.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            custodianGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        if (CocoClinicalTrialActionTarget.DATA_OWNER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataOwnerGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        if (CocoClinicalTrialActionTarget.CONTACT_PERSON.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalContactGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

             List<String>     outputGuards = new ArrayList<>();
             CompletionStatus completionStatus;

            if ((projectGUID == null) || (hospitalGUID == null) || (certificationTypeGUID == null))
            {
                if ((projectGUID == null) || (projectGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.PROJECT.getName()));
                }
                if ((hospitalGUID == null) || (hospitalGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.HOSPITAL.getName()));
                }
                if ((certificationTypeGUID == null) || (certificationTypeGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.CERTIFICATION_TYPE.getName()));
                }
                if ((custodianGUID == null) || (custodianGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.CUSTODIAN.getName()));
                }
                if ((dataOwnerGUID == null) || (dataOwnerGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.DATA_OWNER.getName()));
                }
                if ((hospitalContactGUID == null) || (hospitalContactGUID.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.CONTACT_PERSON.getName()));
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                this.checkCertificationValidForProject(projectGUID, certificationTypeGUID);

                this.addCertificationToHospital(hospitalGUID,
                                                dataOwnerGUID,
                                                custodianGUID,
                                                hospitalContactGUID,
                                                certificationTypeGUID);

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
     * Check that the certification type is associated with the requested project.  Otherwise, the certification does not make sense.
     *
     * @param projectGUID unique identifier of the project
     * @param certificationTypeGUID unique identifier of the certification type
     *
     * @throws ConnectorCheckedException the certification type is not linked to the project
     * @throws InvalidParameterException invalid parameter passed somehow
     * @throws PropertyServerException problem connecting to the open metadata repository
     * @throws UserNotAuthorizedException security problem
     */
    private void checkCertificationValidForProject(String projectGUID,
                                                   String certificationTypeGUID) throws ConnectorCheckedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "checkCertificationValidForProject";

        int projectStartFrom = 0;
        List<RelatedMetadataElement> projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certificationTypeGUID,
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
                        return;
                    }
                }
            }

            projectStartFrom = projectStartFrom + governanceContext.getMaxPageSize();

            projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certificationTypeGUID,
                                                                                           2,
                                                                                           OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                                                                           projectStartFrom,
                                                                                           governanceContext.getMaxPageSize());
        }

        /*
         * If we get to this point, the certification type is not linked to the clinical trial.
         */
        throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.WRONG_CERTIFICATION_TYPE_FOR_TRIAL.getMessageDefinition(certificationTypeGUID, projectGUID),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Create a certification relationship between the hospital and the certification type.
     *
     * @param hospitalGUID unique identifier of the hospital
     * @param certificationTypeGUID unique identifier of the certification type
     *
     * @throws InvalidParameterException invalid parameter passed somehow
     * @throws PropertyServerException problem connecting to the open metadata repository
     * @throws UserNotAuthorizedException security problem
     */
    private void addCertificationToHospital(String hospitalGUID,
                                            String dataOwnerGUID,
                                            String custodianGUID,
                                            String hospitalContactGUID,
                                            String certificationTypeGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        ElementProperties elementProperties = propertyHelper.addDateProperty(null,
                                                                             OpenMetadataType.START_PROPERTY_NAME,
                                                                             new Date());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CERTIFICATE_GUID_PROPERTY_NAME,
                                                             UUID.randomUUID().toString());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CERTIFIED_BY_PROPERTY_NAME,
                                                             dataOwnerGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CERTIFIED_BY_TYPE_NAME_PROPERTY_NAME,
                                                             OpenMetadataType.PERSON_TYPE_NAME);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CERTIFIED_BY_PROPERTY_NAME_PROPERTY_NAME,
                                                             OpenMetadataProperty.GUID.name);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CUSTODIAN_PROPERTY_NAME,
                                                             custodianGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CUSTODIAN_TYPE_NAME_PROPERTY_NAME,
                                                             OpenMetadataType.PERSON_TYPE_NAME);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.CUSTODIAN_PROPERTY_NAME_PROPERTY_NAME,
                                                             OpenMetadataProperty.GUID.name);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.RECIPIENT_PROPERTY_NAME,
                                                             hospitalContactGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.RECIPIENT_TYPE_NAME_PROPERTY_NAME,
                                                             OpenMetadataType.PERSON_TYPE_NAME);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataType.RECIPIENT_PROPERTY_NAME_PROPERTY_NAME,
                                                             OpenMetadataProperty.GUID.name);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                                                              hospitalGUID,
                                                                              certificationTypeGUID,
                                                                              null,
                                                                              null,
                                                                              elementProperties);
    }
}
