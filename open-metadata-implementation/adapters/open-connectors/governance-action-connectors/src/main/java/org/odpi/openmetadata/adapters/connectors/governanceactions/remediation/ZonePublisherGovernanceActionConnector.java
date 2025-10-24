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
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * ZonePublisherGovernanceActionConnector sets the supplied governance zone names into the assets supplied as action targets.
 * If there is at least one asset, their zones are updated, and the output guard is set to zone-assigned.
 * If no Assets are passed as action targets the output guard is no-targets-detected.
 */
public class ZonePublisherGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Value used to set up the zones.
     */
    private List<String> publishZones = null;

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

        super.start();

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        /*
         * Retrieve the configuration properties from the Connection object.  These properties affect all requests to this connector.
         */
        if (configurationProperties != null)
        {
            Object publishZonesOption = configurationProperties.get(ZonePublisherRequestParameter.PUBLISH_ZONES.getName());

            if (publishZonesOption != null)
            {
                publishZones = getZoneArrayFromString(publishZonesOption.toString());
            }
        }

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
                    if (ZonePublisherRequestParameter.PUBLISH_ZONES.getName().equals(requestParameterName))
                    {
                        publishZones = getZoneArrayFromString(requestParameters.get(requestParameterName));
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
            else if ((publishZones == null) || (publishZones.isEmpty()))
            {
                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    if ((actionTarget != null) &&
                            (actionTarget.getTargetElement() != null) &&
                            (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.ASSET.typeName)))
                    {
                        OpenMetadataElement element = actionTarget.getTargetElement();

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                GovernanceActionConnectorsAuditCode.SETTING_ZONES.getMessageDefinition(governanceServiceName,
                                                                                                                       element.getElementGUID(),
                                                                                                                       "<null>"));
                        }

                        governanceContext.getOpenMetadataStore().declassifyMetadataElementInStore(element.getElementGUID(),
                                                                                                  OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                  governanceContext.getOpenMetadataStore().getMetadataSourceOptions());
                    }
                }

                messageDefinition = GovernanceActionConnectorsAuditCode.NO_ZONES.getMessageDefinition(governanceServiceName);
                completionStatus = ZonePublisherGuard.NO_ZONES_DETECTED.getCompletionStatus();
                outputGuards.add(ZonePublisherGuard.NO_ZONES_DETECTED.getName());
            }
            else
            {
                ElementProperties properties = this.getZoneProperties();

                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    if ((actionTarget != null) &&
                            (actionTarget.getTargetElement() != null) &&
                            ((ActionTarget.NEW_ASSET.getName().equals(actionTarget.getActionTargetName()) ||
                                    (ActionTarget.ANY_ELEMENT.getName().equals(actionTarget.getActionTargetName()))) &&
                            (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.OPEN_METADATA_ROOT.typeName))))
                    {
                        OpenMetadataElement element = actionTarget.getTargetElement();


                        auditLog.logMessage(methodName,
                                            GovernanceActionConnectorsAuditCode.SETTING_ZONES.getMessageDefinition(governanceServiceName,
                                                                                                                   element.getElementGUID(),
                                                                                                                   publishZones.toString()));

                        governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(element.getElementGUID(),
                                                                                                OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                                new NewElementProperties(properties));
                    }
                }

                completionStatus = ZonePublisherGuard.ZONE_ASSIGNED.getCompletionStatus();
                outputGuards.add(ZonePublisherGuard.ZONE_ASSIGNED.getName());
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
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the new zones in a property.
     *
     * @return element properties for the AssetZoneMembership classification
     */
    private ElementProperties getZoneProperties()
    {
        return propertyHelper.addStringArrayProperty(null, OpenMetadataProperty.ZONE_MEMBERSHIP.name, publishZones);
    }


    /**
     * Return the supplied zones as an array.
     *
     * @param zones comma separated list of zone names
     * @return string array with a zone name in each element
     */
    private List<String> getZoneArrayFromString(String  zones)
    {
        List<String>    zoneArray       = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(zones, ",");

        while (stringTokenizer.hasMoreTokens())
        {
            String zone = stringTokenizer.nextToken();
            String trimmedZone = zone.trim();

            if (! trimmedZone.isEmpty())
            {
                zoneArray.add(trimmedZone);
            }
        }

        return zoneArray;
    }
}
