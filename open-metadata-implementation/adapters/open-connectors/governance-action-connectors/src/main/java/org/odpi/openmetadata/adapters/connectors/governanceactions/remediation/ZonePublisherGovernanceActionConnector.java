/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ZonePublisherGovernanceActionConnector sets the supplied governance zone names into the elements supplied as
 * newAsset (or "*") action targets.  The zones come from the publishZones configuration property of the service's
 * connection, and the publishZones request parameter overrides it.  Either may be a comma separated list, and the
 * configuration property may also be a list of zone names.
 * If at least one element is updated, the output guard is zones-assigned.
 * If no zones are supplied, the AssetZoneMembership classification is removed from the elements and the output
 * guard is no-zones-detected.
 * If there are no newAsset (or "*") action targets, the output guard is no-targets-detected.
 * If an element can not be updated, the output guard is zone-publishing-failed.
 */
public class ZonePublisherGovernanceActionConnector extends GeneralGovernanceActionService
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

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            List<String>              publishZones = this.getPublishZones();
            List<OpenMetadataElement> elements     = this.getTargetElements(governanceContext.getActionTargetElements());

            if (elements.isEmpty())
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);

                completionStatus = ZonePublisherGuard.NO_TARGETS.getCompletionStatus();
                outputGuards.add(ZonePublisherGuard.NO_TARGETS.getName());
            }
            else if (publishZones == null)
            {
                for (OpenMetadataElement element : elements)
                {
                    logRecord(methodName,
                              GovernanceActionConnectorsAuditCode.SETTING_ZONES.getMessageDefinition(governanceServiceName,
                                                                                                     element.getElementGUID(),
                                                                                                     "<null>"));

                    governanceContext.getOpenMetadataStore().declassifyMetadataElementInStore(element.getElementGUID(),
                                                                                              OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                              governanceContext.getOpenMetadataStore().getMetadataSourceOptions());
                }

                messageDefinition = GovernanceActionConnectorsAuditCode.NO_ZONES.getMessageDefinition(governanceServiceName);

                completionStatus = ZonePublisherGuard.NO_ZONES_DETECTED.getCompletionStatus();
                outputGuards.add(ZonePublisherGuard.NO_ZONES_DETECTED.getName());
            }
            else
            {
                ElementProperties properties = this.getZoneProperties(publishZones);

                for (OpenMetadataElement element : elements)
                {
                    logRecord(methodName,
                              GovernanceActionConnectorsAuditCode.SETTING_ZONES.getMessageDefinition(governanceServiceName,
                                                                                                     element.getElementGUID(),
                                                                                                     publishZones.toString()));

                    governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(element.getElementGUID(),
                                                                                            OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                            governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                            new NewElementProperties(properties));
                }

                completionStatus = ZonePublisherGuard.ZONE_ASSIGNED.getCompletionStatus();
                outputGuards.add(ZonePublisherGuard.ZONE_ASSIGNED.getName());
            }

            if (messageDefinition != null)
            {
                logRecord(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (OMFCheckedExceptionBase error)
        {
            this.recordFailure(error);

            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            this.recordFailure(error);

            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the zones to publish from the request parameters or, if they are not set there, the configuration
     * properties.  The value may be a comma separated list or, in the configuration properties, a list of values.
     * A request parameter that is present overrides the configuration property, even if it is empty.
     *
     * @return list of zone names or null if no zones are supplied
     */
    private List<String> getPublishZones()
    {
        final String propertyName = ZonePublisherRequestParameter.PUBLISH_ZONES.getName();

        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        List<String> publishZones = null;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            publishZones = getArrayValue(requestParameters.get(propertyName));
        }
        else if (configurationProperties != null)
        {
            publishZones = getArrayValue(configurationProperties.get(propertyName));
        }

        if ((publishZones == null) || (publishZones.isEmpty()))
        {
            return null;
        }

        return publishZones;
    }


    /**
     * Return the elements supplied as newAsset or "*" action targets.  These are the elements to update.
     *
     * @param actionTargets action targets passed to the service
     * @return list of elements (may be empty)
     */
    private List<OpenMetadataElement> getTargetElements(List<ActionTargetElement> actionTargets)
    {
        List<OpenMetadataElement> elements = new ArrayList<>();

        if (actionTargets != null)
        {
            for (ActionTargetElement actionTarget : actionTargets)
            {
                if ((actionTarget != null) &&
                        (actionTarget.getTargetElement() != null) &&
                        ((ActionTarget.NEW_ASSET.getName().equals(actionTarget.getActionTargetName())) ||
                                (ActionTarget.ANY_ELEMENT.getName().equals(actionTarget.getActionTargetName()))) &&
                        (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.OPEN_METADATA_ROOT.typeName)))
                {
                    elements.add(actionTarget.getTargetElement());
                }
            }
        }

        return elements;
    }


    /**
     * Return the new zones in a property.
     *
     * @param publishZones zones to set
     * @return element properties for the AssetZoneMembership classification
     */
    private ElementProperties getZoneProperties(List<String> publishZones)
    {
        return propertyHelper.addStringArrayProperty(null, OpenMetadataProperty.ZONE_MEMBERSHIP.name, publishZones);
    }


    /**
     * Record that the service failed, so that a governance action process can follow the failure guard.
     *
     * @param error the exception that stopped the service
     */
    private void recordFailure(Exception error)
    {
        try
        {
            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(ZonePublisherGuard.ZONE_PUBLISHING_FAILED.getName());

            governanceContext.recordCompletionStatus(ZonePublisherGuard.ZONE_PUBLISHING_FAILED.getCompletionStatus(),
                                                     outputGuards,
                                                     null,
                                                     null,
                                                     error.getMessage());
        }
        catch (Exception recordError)
        {
            /*
             * The exception from the service is more important, and is thrown by the caller.
             */
        }
    }
}
