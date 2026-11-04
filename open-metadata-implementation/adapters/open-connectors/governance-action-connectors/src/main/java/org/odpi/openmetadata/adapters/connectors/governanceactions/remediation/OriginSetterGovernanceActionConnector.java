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

/**
 * OriginSetterGovernanceActionConnector sets the DigitalResourceOrigin classification on the elements supplied as action
 * targets.  The organization, organizationPropertyName, businessCapability and businessCapabilityPropertyName come from the
 * configuration properties of the connector's connection and/or the request parameters.  A value in the request parameters
 * overrides the value of the same name in the configuration properties.  Any existing DigitalResourceOrigin classification
 * on an action target is replaced.
 * The DigitalResourceOrigin classification can only be attached to Referenceables, so any other action target is skipped.
 * If at least one action target is classified, the output guard is origin-assigned.
 * If none of the action targets is a Referenceable, the output guard is invalid-action-target-type.
 * If neither an organization nor a business capability is supplied, the output guard is missing-origin and no action target
 * is changed.
 * If no action targets are passed the output guard is no-targets-detected.
 */
public class OriginSetterGovernanceActionConnector extends GeneralGovernanceActionService
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

        String organization                   = this.getNonBlankProperty(OriginSetterRequestParameter.ORGANIZATION.getName());
        String organizationPropertyName       = this.getNonBlankProperty(OriginSetterRequestParameter.ORGANIZATION_PROPERTY_NAME.getName());
        String businessCapability             = this.getNonBlankProperty(OriginSetterRequestParameter.BUSINESS_CAPABILITY.getName());
        String businessCapabilityPropertyName = this.getNonBlankProperty(OriginSetterRequestParameter.BUSINESS_CAPABILITY_PROPERTY_NAME.getName());

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            if ((governanceContext.getActionTargetElements() == null) || (governanceContext.getActionTargetElements().isEmpty()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);

                completionStatus = OriginSetterGuard.NO_TARGETS.getCompletionStatus();
                outputGuards.add(OriginSetterGuard.NO_TARGETS.getName());
            }
            else if ((organization == null) && (businessCapability == null))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_ORIGIN.getMessageDefinition(governanceServiceName);

                completionStatus = OriginSetterGuard.MISSING_ORIGIN.getCompletionStatus();
                outputGuards.add(OriginSetterGuard.MISSING_ORIGIN.getName());
            }
            else
            {
                ElementProperties properties = this.getOriginProperties(organization,
                                                                        organizationPropertyName,
                                                                        businessCapability,
                                                                        businessCapabilityPropertyName);
                int               elementCount = 0;

                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    /*
                     * The DigitalResourceOrigin classification can only be attached to Referenceables.
                     */
                    if ((actionTarget != null) &&
                            (actionTarget.getTargetElement() != null) &&
                            (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.REFERENCEABLE.typeName)))
                    {
                        OpenMetadataElement element = actionTarget.getTargetElement();

                        logRecord(methodName,
                                  GovernanceActionConnectorsAuditCode.SETTING_ORIGIN.getMessageDefinition(governanceServiceName,
                                                                                                          element.getElementGUID(),
                                                                                                          organization,
                                                                                                          organizationPropertyName,
                                                                                                          businessCapability,
                                                                                                          businessCapabilityPropertyName));

                        governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(element.getElementGUID(),
                                                                                                OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                                                                                governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                                new NewElementProperties(properties));
                        elementCount++;
                    }
                }

                if (elementCount == 0)
                {
                    completionStatus = OriginSetterGuard.INVALID_ACTION_TARGET_TYPE.getCompletionStatus();
                    outputGuards.add(OriginSetterGuard.INVALID_ACTION_TARGET_TYPE.getName());
                }
                else
                {
                    completionStatus = OriginSetterGuard.ORIGIN_ASSIGNED.getCompletionStatus();
                    outputGuards.add(OriginSetterGuard.ORIGIN_ASSIGNED.getName());
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
     * Return the properties for the DigitalResourceOrigin classification.
     *
     * @param organization identifier of the organization
     * @param organizationPropertyName name of the property used to identify the organization
     * @param businessCapability identifier of the business capability
     * @param businessCapabilityPropertyName name of the property used to identify the business capability
     * @return element properties for the DigitalResourceOrigin classification
     */
    private ElementProperties getOriginProperties(String organization,
                                                  String organizationPropertyName,
                                                  String businessCapability,
                                                  String businessCapabilityPropertyName)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.ORGANIZATION.name,
                                                                               organization);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME.name,
                                                             organizationPropertyName);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                             businessCapability);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                                             businessCapabilityPropertyName);

        return elementProperties;
    }
}
