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
     * This is a standard method from the Open Connector Framework (OCF), so
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
                        ZoneMembershipProfileProperties zoneMembershipProfile = getGovernanceZoneTypeMembership(governanceZoneProperties.getIdentifier());

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
     * Add up the elements found for each type in the named zone.
     *
     * @param zoneName name of zone to lookup
     * @return analysis of the zone
     * @throws InvalidParameterException  one of the search parameters is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    private ZoneMembershipProfileProperties getGovernanceZoneTypeMembership(String zoneName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        Map<String, Long> membershipCounts = new HashMap<>();
        Map<String, Long> anchoredMembershipCounts = new HashMap<>();
        Map<String, Long> allMembershipCounts = new HashMap<>();

        /*
         * Consult the anchors classification to find the anchor elements for this zone.
         */
        long totalMembership = countZoneMembers(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                zoneName,
                                                membershipCounts,
                                                allMembershipCounts);
        log.debug("Membership counts for zone {} are {}", zoneName, membershipCounts);

        /*
         * Consult the anchors classification to find the anchored elements for this zone.
         */
        long anchoredTotalMembership = countZoneMembers(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                zoneName,
                                                anchoredMembershipCounts,
                                                allMembershipCounts);
        log.debug("Anchored Membership counts for zone {} are {}", zoneName, anchoredMembershipCounts);

        long allTotalMembership = totalMembership + anchoredTotalMembership;

        ZoneMembershipProfileProperties membershipProfile = new ZoneMembershipProfileProperties();
        membershipProfile.setTotalMembership(totalMembership);
        membershipProfile.setTypeMembership(membershipCounts);
        membershipProfile.setAnchoredTotalMembership(anchoredTotalMembership);
        membershipProfile.setAnchoredTypeMembership(anchoredMembershipCounts);
        membershipProfile.setAllTotalMembership(allTotalMembership);
        membershipProfile.setAllTypeMembership(allMembershipCounts);
        membershipProfile.setAnalysisTime(new Date());

        return membershipProfile;
    }


    /**
     * Counts the number of elements that belong to a specified zone and are classified with a specific classification.
     * It also updates maps containing membership counts per type for the given zone and across all zones.
     *
     * @param classificationName the name of the classification to filter metadata elements by.
     * @param zoneName the name of the zone to search for classified metadata elements.
     * @param membershipCounts a map to track the count of classified metadata elements per type within the specified zone.
     * @param allMembershipCounts a map to track the count of classified metadata elements per type across all zones.
     * @return the total number of classified metadata elements found in the specified zone.
     * @throws InvalidParameterException if any of the search parameters are invalid.
     * @throws PropertyServerException if there is a problem accessing the metadata store.
     * @throws UserNotAuthorizedException if the user is not authorized to perform this operation.
     */
    private long countZoneMembers(String classificationName,
                                  String zoneName,
                                  Map<String, Long> membershipCounts,
                                  Map<String, Long> allMembershipCounts) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();
        QueryOptions      queryOptions      = openMetadataStore.getQueryOptions(0, governanceContext.getMaxPageSize());

        List<OpenMetadataElement> elements = openMetadataStore.getMetadataElementsByClassificationPropertyValue(classificationName,
                                                                                                                List.of(OpenMetadataProperty.ZONE_MEMBERSHIP.name),
                                                                                                                zoneName,
                                                                                                                queryOptions);
        long membershipCount = 0L;

        while (elements != null)
        {
            for (OpenMetadataElement element : elements)
            {
                if (element != null)
                {
                    membershipCount++;
                    String elementType = element.getType().getTypeName();

                    membershipCounts.merge(elementType, 1L, Long::sum);
                    allMembershipCounts.merge(elementType, 1L, Long::sum);
                }
            }

            queryOptions.setStartFrom(queryOptions.getStartFrom() + governanceContext.getMaxPageSize());
            elements = openMetadataStore.getMetadataElementsByClassificationPropertyValue(classificationName,
                                                                                          List.of(OpenMetadataProperty.ZONE_MEMBERSHIP.name),
                                                                                          zoneName,
                                                                                          queryOptions);
        }

        return membershipCount;
    }
}
