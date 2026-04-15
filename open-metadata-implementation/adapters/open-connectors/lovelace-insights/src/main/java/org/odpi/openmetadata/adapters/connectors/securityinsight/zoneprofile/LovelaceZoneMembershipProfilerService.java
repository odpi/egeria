/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.securityinsight.zoneprofile;

import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * LovelaceZoneMembershipProfilerService counts the elements of each type in each zone and maintains
 * the counts in the ZoneMembershipProfile classification attached to the governance zone entity
 */
public class LovelaceZoneMembershipProfilerService extends GeneralGovernanceActionService
{
    private static final Logger log = LoggerFactory.getLogger(LovelaceZoneMembershipProfilerService.class);
    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();

    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is where the function of the watchdog action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            List<String>              outputGuards      = new ArrayList<>();
            CompletionStatus          completionStatus  = CompletionStatus.ACTIONED;
            AuditLogMessageDefinition messageDefinition = LovelaceInsightAuditCode.SERVICE_COMPLETED_SUCCESSFULLY.getMessageDefinition(governanceServiceName);

            outputGuards.add(Guard.SERVICE_COMPLETED.getName());

            OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();
            GovernanceDefinitionClient governanceDefinitionClient = governanceContext.getGovernanceDefinitionClient(OpenMetadataType.GOVERNANCE_ZONE.typeName);

            SearchOptions searchOptions = governanceDefinitionClient.getSearchOptions(0, governanceContext.getMaxPageSize());

            List<OpenMetadataRootElement> governanceZoneElements = governanceDefinitionClient.findGovernanceDefinitions(null, searchOptions);

            while (governanceZoneElements != null)
            {
                for (OpenMetadataRootElement governanceZoneElement : governanceZoneElements)
                {
                    if ((governanceZoneElement != null) && (governanceZoneElement.getProperties() instanceof GovernanceZoneProperties governanceZoneProperties))
                    {
                        ZoneMembershipProfileProperties zoneMembershipProfile = getGovernanceZoneTypeMembership(governanceZoneProperties.getIdentifier());;

                        if (governanceZoneElement.getElementHeader().getZoneMembershipProfile() == null)
                        {
                            openMetadataStore.classifyMetadataElementInStore(governanceZoneElement.getElementHeader().getGUID(),
                                                                             OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName,
                                                                             openMetadataStore.getMetadataSourceOptions(),
                                                                             classificationBuilder.getNewElementProperties(zoneMembershipProfile));
                        }
                        else
                        {
                            openMetadataStore.reclassifyMetadataElementInStore(governanceZoneElement.getElementHeader().getGUID(),
                                                                               OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName,
                                                                               openMetadataStore.getUpdateOptions(true),
                                                                               classificationBuilder.getNewElementProperties(zoneMembershipProfile));
                        }

                        auditLog.logMessage(methodName, LovelaceInsightAuditCode.GOVERNANCE_ZONE_PROCESSED.getMessageDefinition(governanceServiceName,
                                                                                                                                governanceZoneProperties.getIdentifier(),
                                                                                                                                governanceZoneElement.getElementHeader().getGUID()));
                    }
                }

                searchOptions.setStartFrom( searchOptions.getStartFrom() + governanceContext.getMaxPageSize());

                governanceZoneElements = governanceDefinitionClient.findGovernanceDefinitions(null, searchOptions);
            }

            auditLog.logMessage(methodName, messageDefinition);

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(LovelaceInsightErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Add up the number of elements fround for each type in the named zone.
     *
     * @param zoneName name of zone to lookup
     * @return analysis of the zone
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    private ZoneMembershipProfileProperties getGovernanceZoneTypeMembership(String zoneName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {

        Map<String, Long> membershipCounts = new HashMap<>();
        long totalMembership = 0L;

        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();
        QueryOptions      queryOptions      = openMetadataStore.getQueryOptions(0, governanceContext.getMaxPageSize());

        List<OpenMetadataElement> elements = openMetadataStore.getMetadataElementsByClassificationPropertyValue(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                List.of(OpenMetadataProperty.ZONE_MEMBERSHIP.name),
                                                                                                                zoneName,
                                                                                                                queryOptions);
        while (elements != null)
        {
            for (OpenMetadataElement element : elements)
            {
                if (element != null)
                {
                    totalMembership++;
                    String elementType = element.getType().getTypeName();

                    membershipCounts.merge(elementType, 1L, Long::sum);
                }
            }

            queryOptions.setStartFrom(queryOptions.getStartFrom() + governanceContext.getMaxPageSize());
            elements = openMetadataStore.getMetadataElementsByClassificationPropertyValue(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                          List.of(OpenMetadataProperty.ZONE_MEMBERSHIP.name),
                                                                                          zoneName,
                                                                                          queryOptions);
        }

        log.debug("Membership counts for zone {} are {}", zoneName, membershipCounts);

        ZoneMembershipProfileProperties membershipProfile = new ZoneMembershipProfileProperties();
        membershipProfile.setTotalMembership(totalMembership);
        membershipProfile.setTypeMembership(membershipCounts);
        membershipProfile.setAnalysisTime(new Date());

        return membershipProfile;
    }
}
