/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OwnerSetterGovernanceActionConnector sets the Ownership classification on the elements supplied as action targets.
 * The owner, ownerTypeName, ownerPropertyName and userIds come from the configuration properties of the connector's
 * connection and/or the request parameters.  A value in the request parameters overrides the value of the same name
 * in the configuration properties.  Any existing Ownership classification on an action target is replaced.
 * The Ownership classification can only be attached to Referenceables, so any other action target is skipped.
 * If at least one action target is classified, the output guard is owner-assigned.
 * If none of the action targets is a Referenceable, the output guard is invalid-action-target-type.
 * If no owner is supplied, the output guard is missing-owner and no action target is changed.
 * If no action targets are passed the output guard is no-targets-detected.
 */
public class OwnerSetterGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        String       owner             = this.getNonBlankProperty(OwnerSetterRequestParameter.OWNER.getName());
        String       ownerTypeName     = this.getNonBlankProperty(OwnerSetterRequestParameter.OWNER_TYPE_NAME.getName());
        String       ownerPropertyName = this.getNonBlankProperty(OwnerSetterRequestParameter.OWNER_PROPERTY_NAME.getName());
        List<String> userIds           = this.getUserIds();

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            if ((governanceContext.getActionTargetElements() == null) || (governanceContext.getActionTargetElements().isEmpty()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);

                completionStatus = OwnerSetterGuard.NO_TARGETS.getCompletionStatus();
                outputGuards.add(OwnerSetterGuard.NO_TARGETS.getName());
            }
            else if (owner == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_OWNER.getMessageDefinition(governanceServiceName);

                completionStatus = OwnerSetterGuard.MISSING_OWNER.getCompletionStatus();
                outputGuards.add(OwnerSetterGuard.MISSING_OWNER.getName());
            }
            else
            {
                ElementProperties properties = this.getOwnershipProperties(owner, ownerTypeName, ownerPropertyName, userIds);
                int               elementCount = 0;

                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    /*
                     * The Ownership classification can only be attached to Referenceables.
                     */
                    if ((actionTarget != null) &&
                            (actionTarget.getTargetElement() != null) &&
                            (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.REFERENCEABLE.typeName)))
                    {
                        OpenMetadataElement element = actionTarget.getTargetElement();

                        logRecord(methodName,
                                  GovernanceActionConnectorsAuditCode.SETTING_OWNER.getMessageDefinition(governanceServiceName,
                                                                                                         element.getElementGUID(),
                                                                                                         owner,
                                                                                                         ownerTypeName,
                                                                                                         ownerPropertyName,
                                                                                                         String.valueOf(userIds)));

                        governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(element.getElementGUID(),
                                                                                                OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                                                                governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                                new NewElementProperties(properties));
                        elementCount++;
                    }
                }

                if (elementCount == 0)
                {
                    completionStatus = OwnerSetterGuard.INVALID_ACTION_TARGET_TYPE.getCompletionStatus();
                    outputGuards.add(OwnerSetterGuard.INVALID_ACTION_TARGET_TYPE.getName());
                }
                else
                {
                    completionStatus = OwnerSetterGuard.OWNER_ASSIGNED.getCompletionStatus();
                    outputGuards.add(OwnerSetterGuard.OWNER_ASSIGNED.getName());
                }
            }

            if (messageDefinition != null)
            {
                logRecord(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (OMFCheckedExceptionBase error)
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
     * Return the value of a property from the request parameters or, if it is not set there, the configuration properties.
     * A blank value is treated as not set.
     *
     * @param propertyName name of the property
     * @return trimmed property value or null
     */
    private String getNonBlankProperty(String propertyName)
    {
        String propertyValue = super.getProperty(propertyName, null);

        if ((propertyValue == null) || (propertyValue.isBlank()))
        {
            return null;
        }

        return propertyValue.trim();
    }


    /**
     * Return the userIds of the owner from the request parameters or, if they are not set there, the configuration
     * properties.  The value may be a comma separated list or, in the configuration properties, a list of values.
     *
     * @return list of userIds or null
     */
    private List<String> getUserIds()
    {
        final String propertyName = OwnerSetterRequestParameter.USER_IDS.getName();

        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        List<String> userIds = null;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            userIds = getArrayValue(requestParameters.get(propertyName));
        }
        else if (configurationProperties != null)
        {
            userIds = getArrayValue(configurationProperties.get(propertyName));
        }

        if ((userIds == null) || (userIds.isEmpty()))
        {
            return null;
        }

        return userIds;
    }


    /**
     * Return the properties for the Ownership classification.
     *
     * @param owner identifier of the owner
     * @param ownerTypeName type of element that describes the owner
     * @param ownerPropertyName name of the property used to identify the owner
     * @param userIds userIds of the owner
     * @return element properties for the Ownership classification
     */
    private ElementProperties getOwnershipProperties(String       owner,
                                                     String       ownerTypeName,
                                                     String       ownerPropertyName,
                                                     List<String> userIds)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.OWNER.name,
                                                                               owner);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                             ownerTypeName);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                             ownerPropertyName);
        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.USER_IDS.name,
                                                                  userIds);

        return elementProperties;
    }
}
