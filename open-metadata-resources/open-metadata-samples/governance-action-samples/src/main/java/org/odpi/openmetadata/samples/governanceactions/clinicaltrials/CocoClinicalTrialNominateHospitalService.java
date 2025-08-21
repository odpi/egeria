/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
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
import java.util.UUID;

/**
 * Responsible for adding the incomplete certification relationship to a hospital so that the hospital contacts
 * that will complete the certification process can be identified.
 */
public class CocoClinicalTrialNominateHospitalService extends CocoClinicalTrialBaseService
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
        String hospitalGUID            = null;
        String certificationTypeGUID   = null;
        String processOwnerGUID        = null;
        String processOwnerTypeName    = null;
        String custodianGUID           = null;
        String custodianTypeName       = null;
        String hospitalContactGUID     = null;
        String hospitalContactTypeName = null;

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
                        else if (CocoClinicalTrialActionTarget.HOSPITAL.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            certificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            custodianGUID = actionTargetElement.getTargetElement().getElementGUID();
                            custodianTypeName = actionTargetElement.getTargetElement().getType().getTypeName();
                        }
                        else if (CocoClinicalTrialActionTarget.CLINICAL_TRIAL_OWNER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            processOwnerGUID = actionTargetElement.getTargetElement().getElementGUID();
                            processOwnerTypeName =  actionTargetElement.getTargetElement().getType().getTypeName();
                        }
                        else if (CocoClinicalTrialActionTarget.CONTACT_PERSON.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalContactGUID = actionTargetElement.getTargetElement().getElementGUID();
                            hospitalContactTypeName =  actionTargetElement.getTargetElement().getType().getTypeName();
                        }
                    }
                }
            }

            List<String>              outputGuards = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;

            if ((projectGUID == null) || (hospitalGUID == null) || (certificationTypeGUID == null) ||
                    (hospitalContactGUID == null) || custodianGUID == null || processOwnerGUID == null)
            {
                if ((projectGUID == null) || (projectGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.PROJECT.getName());
                }
                if ((hospitalGUID == null) || (hospitalGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL.getName());
                }
                if ((certificationTypeGUID == null) || (certificationTypeGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName());
                }
                if ((custodianGUID == null) || (custodianGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CLINICAL_TRIAL_MANAGER.getName());
                }
                if ((processOwnerGUID == null) || (processOwnerGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CLINICAL_TRIAL_OWNER.getName());
                }
                if ((hospitalContactGUID == null) || (hospitalContactGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.CONTACT_PERSON.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                this.checkCertificationValidForProject(projectGUID, certificationTypeGUID);

                this.addNewCertificationToHospital(hospitalGUID,
                                                   processOwnerGUID,
                                                   processOwnerTypeName,
                                                   custodianGUID,
                                                   custodianTypeName,
                                                   hospitalContactGUID,
                                                   hospitalContactTypeName,
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
     * @param hospitalGUID unique identifier of the hospital
     * @param processOwnerGUID unique identifier of the process owner
     * @param processOwnerTypeName type name of element used to identify the process owner
     * @param custodianGUID unique identifier of the custodian
     * @param custodianTypeName type name of the element used to identify the custodian
     * @param hospitalContactGUID unique identifier of the hospital employee that is hospital representative
     * @param hospitalContactTypeName type name of the element used to identify the hospital representative
     * @param certificationTypeGUID unique identifier of the certification type
     *
     * @throws InvalidParameterException invalid parameter passed somehow
     * @throws PropertyServerException problem connecting to the open metadata repository
     * @throws UserNotAuthorizedException security problem
     */
    private void addNewCertificationToHospital(String hospitalGUID,
                                               String processOwnerGUID,
                                               String processOwnerTypeName,
                                               String custodianGUID,
                                               String custodianTypeName,
                                               String hospitalContactGUID,
                                               String hospitalContactTypeName,
                                               String certificationTypeGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        int startFrom = 0;
        OpenMetadataRelationshipList existingCertifications = governanceContext.getOpenMetadataStore().getMetadataElementRelationships(hospitalGUID,
                                                                                                                                       certificationTypeGUID,
                                                                                                                                       OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                                                                                       startFrom,
                                                                                                                                       governanceContext.getMaxPageSize());

        /*
         * All existing certifications are ended - assuming that the hospital contact people have changed - or
         * a similar reason, which has caused Coco Pharmaceuticals to "start again" with this hospital.
         */
        while ((existingCertifications != null) && (existingCertifications.getElementList() != null))
        {
            ElementProperties updatedProperties = propertyHelper.addDateProperty(null,
                                                                                 OpenMetadataProperty.COVERAGE_END.name,
                                                                                 new Date());

            for (OpenMetadataRelationship certification : existingCertifications.getElementList())
            {
                if (certification != null)
                {
                    governanceContext.getOpenMetadataStore().updateRelatedElementsInStore(certification.getRelationshipGUID(),
                                                                                          governanceContext.getOpenMetadataStore().getUpdateOptions(false),
                                                                                          updatedProperties);
                }
            }

            startFrom = startFrom + governanceContext.getMaxPageSize();
            existingCertifications = governanceContext.getOpenMetadataStore().getMetadataElementRelationships(hospitalGUID,
                                                                                                              certificationTypeGUID,
                                                                                                              OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                                                              startFrom,
                                                                                                              governanceContext.getMaxPageSize());
        }

        /*
         * Create the new certification relationship
         */
        ElementProperties elementProperties = propertyHelper.addDateProperty(null,
                                                                             OpenMetadataProperty.COVERAGE_START.name,
                                                                             null);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                             UUID.randomUUID().toString());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CERTIFIED_BY.name,
                                                             processOwnerGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                             processOwnerTypeName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                             OpenMetadataProperty.GUID.name);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CUSTODIAN.name,
                                                             custodianGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                             custodianTypeName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                             OpenMetadataProperty.GUID.name);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RECIPIENT.name,
                                                             hospitalContactGUID);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RECIPIENT_TYPE_NAME.name,
                                                             hospitalContactTypeName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RECIPIENT_PROPERTY_NAME.name,
                                                             OpenMetadataProperty.GUID.name);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                              hospitalGUID,
                                                                              certificationTypeGUID,
                                                                              null,
                                                                              null,
                                                                              elementProperties);

    }
}
