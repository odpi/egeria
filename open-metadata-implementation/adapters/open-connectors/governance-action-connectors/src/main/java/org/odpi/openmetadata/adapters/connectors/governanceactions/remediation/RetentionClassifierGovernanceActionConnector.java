/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.governanceaction.RemediationGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.Guard;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceClassificationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.RetentionBasis;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ZonePublisherGovernanceActionConnector sets the supplied governance zone names into the assets supplied as action targets.
 * If there is at least one asset, their zones are updated, and the output guard is set to zone-assigned.
 * If no Assets are passed as action targets the output guard is no-targets-detected.
 */
public class RetentionClassifierGovernanceActionConnector extends RemediationGovernanceActionService
{
    /**
     * Value used to set up the zones.
     */
    private final int confidence = 100;

    private int statusIdentifier = GovernanceClassificationStatus.VALIDATED.getOrdinal();
    private int basisIdentifier  = RetentionBasis.REGULATED_LIFETIME.getOrdinal();
    private long timeToArchive   = 31556952000L * 5;
    private long timeToDelete    = 31556952000L * 15;

    private String stewardGUID = null;
    private String stewardTypeName = null;

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

        /*
         * Retrieve the zones to set in the assets.  This may override the value set in the configuration properties.
         */
        if (governanceContext.getRequestParameters() != null)
        {
            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            for (String requestParameterName : requestParameters.keySet())
            {
                if (requestParameterName != null)
                {
                    if (RetentionClassifierRequestParameter.STATUS_IDENTIFIER.getName().equals(requestParameterName))
                    {
                        statusIdentifier = getIntRequestParameter(RetentionClassifierRequestParameter.STATUS_IDENTIFIER.getName(),
                                                                  requestParameters);
                    }
                    else if (RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName().equals(requestParameterName))
                    {
                        basisIdentifier = getIntRequestParameter(RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName(),
                                                                 requestParameters);
                    }
                    else if (RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName().equals(requestParameterName))
                    {
                        timeToArchive = getLongRequestParameter(RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName(),
                                                                requestParameters);
                    }
                    else if (RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName().equals(requestParameterName))
                    {
                        timeToDelete = getLongRequestParameter(RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName(),
                                                                requestParameters);
                    }
                }
            }
        }

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            if (governanceContext.getActionTargetElements() == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);

                completionStatus = Guard.NO_TARGETS_DETECTED.getCompletionStatus();
                outputGuards.add(Guard.NO_TARGETS_DETECTED.getName());
            }
            else
            {
                /*
                 * First pass of the action targets locates the Steward information.
                 */
                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    if ((actionTarget != null) && (ActionTarget.STEWARD.getName().equals(actionTarget.getActionTargetName())))
                    {
                        stewardGUID     = actionTarget.getTargetElement().getElementGUID();
                        stewardTypeName = actionTarget.getTargetElement().getType().getTypeName();
                    }
                }

                if (stewardGUID == null)
                {
                    messageDefinition = GovernanceActionConnectorsAuditCode.NO_STEWARD.getMessageDefinition(governanceServiceName);
                    outputGuards.add(RetentionClassifierGuard.MISSING_STEWARD.getName());
                    completionStatus = RetentionClassifierGuard.MISSING_STEWARD.getCompletionStatus();
                }
                else
                {
                    /*
                     * Second pass works on the new assets that need the retention classification.
                     */
                    Date              now           = new Date();
                    Date              dateToArchive = new Date(now.getTime() + timeToArchive);
                    Date              dateToDelete  = new Date(now.getTime() + timeToDelete);
                    ElementProperties properties    = this.getRetentionProperties(dateToArchive, dateToDelete);

                    for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                    {
                        if ((actionTarget != null) &&
                                (actionTarget.getTargetElement() != null) &&
                                (ActionTarget.NEW_ASSET.getName().equals(actionTarget.getActionTargetName())) &&
                                (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.ASSET.typeName)))
                        {
                            OpenMetadataElement element = actionTarget.getTargetElement();

                            auditLog.logMessage(methodName,
                                                GovernanceActionConnectorsAuditCode.SETTING_RETENTION.getMessageDefinition(governanceServiceName,
                                                                                                                           element.getElementGUID(),
                                                                                                                           dateToArchive.toString(),
                                                                                                                           dateToDelete.toString()));

                            governanceContext.classifyMetadataElement(element.getElementGUID(),
                                                                      OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                                      false,
                                                                      false,
                                                                      properties,
                                                                      new Date());
                        }
                    }

                    completionStatus = RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getCompletionStatus();
                    outputGuards.add(RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
                }
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
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the retention specification.
     *
     * @return element properties for the Retention classification
     */
    private ElementProperties getRetentionProperties(Date dateToArchive,
                                                     Date dateToDelete)
    {
        ElementProperties elementProperties = propertyHelper.addIntProperty(null,
                                                                            OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                            statusIdentifier);
        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                          OpenMetadataProperty.CONFIDENCE.name,
                                                          confidence);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.STEWARD.name,
                                                             stewardGUID);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                             stewardTypeName);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                             OpenMetadataProperty.GUID.name);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.SOURCE.name,
                                                             governanceServiceName);
        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                          OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                          basisIdentifier);
        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataType.RETENTION_ARCHIVE_AFTER_PROPERTY_NAME,
                                                           dateToArchive);
        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataType.RETENTION_DELETE_AFTER_PROPERTY_NAME,
                                                           dateToDelete);

        return elementProperties;
    }
}
