/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.usageinsight.popularity;

import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MemberPopularityProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LovelacePopularityProfilerService maintains the MemberPopularityProfile classification of each root collection,
 * glossary, data sharing hub, collection folder and digital product catalog.  The profile totals the usage
 * activity of the collection's members (linked through CollectionMembership): each member contributes the counts in
 * its own UsageActivity classification, plus the totals of its own members if it is a collection too.
 * <p>
 * Nested collections of these kinds are profiled before the collection that contains them, within the same run, so
 * the activity of the leaf members ripples up through every level to the top-level collections.  A member that is
 * a different kind of collection contributes the totals of its stored MemberPopularityProfile, if it has one.
 * <p>
 * The service is run by the Babbage Analytical Engine on each of its refreshes.  Its reads of the members are made
 * as part of building each collection's profile (they set viewElementGUID), so they are not counted as the members
 * being found.  A profile whose counts have not changed is not rewritten.
 */
public class LovelacePopularityProfilerService extends GeneralGovernanceActionService
{
    /**
     * The kinds of collection that are given a MemberPopularityProfile.
     */
    private static final List<String> profiledCollectionTypes = List.of(OpenMetadataType.ROOT_COLLECTION.typeName,
                                                                        OpenMetadataType.GLOSSARY.typeName,
                                                                        OpenMetadataType.DATA_SHARING_HUB.typeName,
                                                                        OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                                        OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName);

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /*
     * The collections profiled in this run, and the totals they contribute to the collections that contain them.
     */
    private final Map<String, MemberActivity> profiledCollections = new HashMap<>();

    /*
     * The collections whose profile is being built - used to stop a membership cycle recursing for ever.
     */
    private final Set<String> collectionsInProgress = new HashSet<>();

    private int updatedProfiles = 0;
    private int refusedProfiles = 0;


    /**
     * Profile every collection of the supported kinds.  This is a standard method from the Open Connector
     * Framework (OCF), so be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

            for (String collectionTypeName : profiledCollectionTypes)
            {
                QueryOptions queryOptions = openMetadataStore.getQueryOptions(0, governanceContext.getMaxPageSize());

                queryOptions.setMetadataElementTypeName(collectionTypeName);

                List<OpenMetadataElement> collections = openMetadataStore.findMetadataElements(null, null, queryOptions);

                while ((collections != null) && (! collections.isEmpty()))
                {
                    for (OpenMetadataElement collection : collections)
                    {
                        if ((collection != null) && (propertyHelper.getElementHeader(collection).getTemplate() == null))
                        {
                            this.profileCollection(collection);
                        }
                    }

                    queryOptions.setStartFrom(queryOptions.getStartFrom() + governanceContext.getMaxPageSize());
                    collections = openMetadataStore.findMetadataElements(null, null, queryOptions);
                }
            }

            logRecord(methodName,
                      LovelaceInsightAuditCode.MEMBER_POPULARITY_PROFILED.getMessageDefinition(governanceServiceName,
                                                                                               Integer.toString(profiledCollections.size()),
                                                                                               Integer.toString(updatedProfiles),
                                                                                               Integer.toString(refusedProfiles)));

            AuditLogMessageDefinition messageDefinition = LovelaceInsightAuditCode.SERVICE_COMPLETED_SUCCESSFULLY.getMessageDefinition(governanceServiceName);
            List<String>              outputGuards      = new ArrayList<>();

            outputGuards.add(Guard.SERVICE_COMPLETED.getName());

            logRecord(methodName, messageDefinition);

            governanceContext.recordCompletionStatus(CompletionStatus.ACTIONED, outputGuards, null, null, messageDefinition);
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
     * Build, and store if it has changed, the MemberPopularityProfile of a collection.  Members that are themselves
     * collections of a profiled kind are profiled first.
     *
     * @param collection the collection
     * @return the activity that this collection contributes to the collections that contain it - its own usage
     *         activity is added by the caller
     * @throws InvalidParameterException  invalid parameter
     * @throws UserNotAuthorizedException the service's user is not permitted to perform this operation
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    private MemberActivity profileCollection(OpenMetadataElement collection) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "profileCollection";

        String collectionGUID = collection.getElementGUID();

        MemberActivity alreadyProfiled = profiledCollections.get(collectionGUID);

        if (alreadyProfiled != null)
        {
            return alreadyProfiled;
        }

        if (collectionsInProgress.contains(collectionGUID))
        {
            /*
             * The collection is (indirectly) a member of itself.  Its activity is already being counted further up
             * this path, so it contributes nothing here.
             */
            return new MemberActivity();
        }

        collectionsInProgress.add(collectionGUID);

        Map<String, MemberActivity> membersActivity = new LinkedHashMap<>();
        OpenMetadataStore           openMetadataStore = governanceContext.getOpenMetadataStore();
        QueryOptions                queryOptions      = openMetadataStore.getQueryOptions(0, governanceContext.getMaxPageSize());

        /*
         * Reading the members is building this collection's profile, not searching for them.
         */
        queryOptions.setViewElementGUID(collectionGUID);

        RelatedMetadataElementList members = openMetadataStore.getRelatedMetadataElements(collectionGUID,
                                                                                         1,
                                                                                         OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                         queryOptions);

        while ((members != null) && (members.getElementList() != null) && (! members.getElementList().isEmpty()))
        {
            for (RelatedMetadataElement relatedMember : members.getElementList())
            {
                if ((relatedMember != null) && (relatedMember.getElement() != null))
                {
                    OpenMetadataElement member       = relatedMember.getElement();
                    ElementHeader       memberHeader = propertyHelper.getElementHeader(member);
                    MemberActivity      contribution = MemberActivity.fromUsageActivity(memberHeader.getUsageActivity());

                    if (this.isProfiledCollection(member))
                    {
                        contribution.add(this.profileCollection(member));
                    }
                    else
                    {
                        contribution.add(MemberActivity.fromMemberPopularityProfile(memberHeader.getMemberPopularityProfile()));
                    }

                    /*
                     * A member linked more than once is counted once.
                     */
                    membersActivity.putIfAbsent(member.getElementGUID(), contribution);
                }
            }

            queryOptions.setStartFrom(queryOptions.getStartFrom() + governanceContext.getMaxPageSize());
            members = openMetadataStore.getRelatedMetadataElements(collectionGUID,
                                                                   1,
                                                                   OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                   queryOptions);
        }

        MemberPopularityProfileProperties profile = MemberActivity.getMemberPopularityProfile(membersActivity);

        try
        {
            this.storeProfile(collection, profile);
        }
        catch (InvalidParameterException | UserNotAuthorizedException error)
        {
            /*
             * One collection that can not be classified - typically because it is in a governance zone that does not
             * permit this service's user to classify it - must not stop the others being profiled.  Its totals are
             * still returned so that its members' activity ripples up to the collections that contain it.
             */
            refusedProfiles++;

            logRecord(methodName,
                      LovelaceInsightAuditCode.MEMBER_POPULARITY_PROFILE_NOT_STORED.getMessageDefinition(governanceServiceName,
                                                                                                         collection.getType().getTypeName(),
                                                                                                         collectionGUID,
                                                                                                         error.getClass().getName(),
                                                                                                         error.getMessage()));
        }

        MemberActivity totals = new MemberActivity();

        for (MemberActivity memberActivity : membersActivity.values())
        {
            totals.add(memberActivity);
        }

        collectionsInProgress.remove(collectionGUID);
        profiledCollections.put(collectionGUID, totals);

        return totals;
    }


    /**
     * Add or update the collection's MemberPopularityProfile classification, unless its counts are unchanged.
     *
     * @param collection collection element
     * @param profile newly built profile
     * @throws InvalidParameterException  invalid parameter
     * @throws UserNotAuthorizedException the service's user is not permitted to perform this operation
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    private void storeProfile(OpenMetadataElement               collection,
                              MemberPopularityProfileProperties profile) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        ElementHeader                collectionHeader = propertyHelper.getElementHeader(collection);
        ClassificationExplorerClient classificationExplorerClient = governanceContext.getClassificationExplorerClient();

        if ((collectionHeader.getMemberPopularityProfile() != null) &&
                (collectionHeader.getMemberPopularityProfile().getClassificationProperties() instanceof MemberPopularityProfileProperties existingProfile))
        {
            if (! MemberActivity.sameCounts(existingProfile, profile))
            {
                classificationExplorerClient.updateMemberPopularityProfileClassification(collection.getElementGUID(),
                                                                                         profile,
                                                                                         classificationExplorerClient.getUpdateOptions(false));
                updatedProfiles++;
            }
        }
        else
        {
            classificationExplorerClient.addMemberPopularityProfileClassification(collection.getElementGUID(),
                                                                                  profile,
                                                                                  classificationExplorerClient.getMetadataSourceOptions());
            updatedProfiles++;
        }
    }


    /**
     * Is this element one of the kinds of collection that this service profiles?
     *
     * @param element element to test
     * @return boolean
     */
    private boolean isProfiledCollection(OpenMetadataElement element)
    {
        for (String collectionTypeName : profiledCollectionTypes)
        {
            if (propertyHelper.isTypeOf(element, collectionTypeName))
            {
                return true;
            }
        }

        return false;
    }
}
