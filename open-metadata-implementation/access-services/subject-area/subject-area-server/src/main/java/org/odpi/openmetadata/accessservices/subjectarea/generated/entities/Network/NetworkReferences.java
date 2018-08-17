/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.NetworkToHost.ConnectedHostsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.HostNetwork.HostNetwork;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.HostNetwork.HostNetworkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.NetworkToNetworkGateway.GatewaysReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.NetworkGatewayLink.NetworkGatewayLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.NetworkGatewayLink.NetworkGatewayLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToSchemaType.SchemaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToSoftwareServerCapability.ConsumedByReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetServerUse.AssetServerUse;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetServerUse.AssetServerUseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToConnection.ConnectionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ConnectionToAsset.ConnectionToAsset;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ConnectionToAsset.ConnectionToAssetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToDataSet.SupportedDataSetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.DataContentForDataSet.DataContentForDataSet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.DataContentForDataSet.DataContentForDataSetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToGovernanceZone.ZonesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ZoneMembership.ZoneMembership;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ZoneMembership.ZoneMembershipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSourceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToActorProfile.ContributorsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.Contributor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.ContributorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.License;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.LicenseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRating.StarRatingsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedRating.AttachedRatingMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToInformalTag.TagsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTagMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MediaReference.MediaReferenceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToCertificationType.CertificationsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification.Certification;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification.CertificationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalId.ManagedResourcesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdScope.ExternalIdScopeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalId.AlsoKnownAsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdLink.ExternalIdLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPerson.StaffReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignmentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.TodosReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedCommentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignmentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMember;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMemberMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToLike.LikesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedLike.AttachedLikeMapper;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NetworkReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(NetworkReferences.class);
    private static final String className = NetworkReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "connectedHosts",
             "gateways",
             "schema",
             "consumedBy",
             "connections",
             "supportedDataSets",
             "zones",
             "facets",
             "meetings",
             "actions",
             "contributors",
             "licenses",
             "starRatings",
             "tags",
             "relatedMedia",
             "certifications",
             "managedResources",
             "alsoKnownAs",
             "staff",
             "todos",
             "comments",
             "meaning",
             "externalReference",
             "noteLogs",
             "foundInCollections",
             "likes",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "HostNetwork",
             "NetworkGatewayLink",
             "AssetSchemaType",
             "AssetServerUse",
             "ConnectionToAsset",
             "DataContentForDataSet",
             "ZoneMembership",
             "ReferenceableFacet",
             "MeetingOnReferenceable",
             "ToDoSource",
             "Contributor",
             "License",
             "AttachedRating",
             "AttachedTag",
             "MediaReference",
             "Certification",
             "ExternalIdScope",
             "ExternalIdLink",
             "StaffAssignment",
             "ToDoOnReferenceable",
             "AttachedComment",
             "SemanticAssignment",
             "ExternalReferenceLink",
             "AttachedNoteLog",
             "CollectionMember",
             "AttachedLike",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity Network
       * Each of these relationships should map to a reference (a uniquely named attribute in Network).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationship can be related to one or many
       * entities.
       *
       * @param lines
       * @param entityGuid
       * @throws InvalidParameterException
       */
     public NetworkReferences(String entityGuid, List<Line> lines) throws InvalidParameterException {
         for (Line relationship: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationship.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("connectedHosts".equals(referenceName)) {
                         HostNetwork hostNetwork_relationship = (HostNetwork)relationship;
                         ConnectedHostsReference connectedHostsReference = new ConnectedHostsReference(entityGuid,hostNetwork_relationship);
                         if ( connectedHosts== null ) {
                              connectedHosts = new HashSet();
                         }
                          connectedHosts.add(connectedHostsReference);
                    }
                    if ("gateways".equals(referenceName)) {
                         NetworkGatewayLink networkGatewayLink_relationship = (NetworkGatewayLink)relationship;
                         GatewaysReference gatewaysReference = new GatewaysReference(entityGuid,networkGatewayLink_relationship);
                         if ( gateways== null ) {
                              gateways = new HashSet();
                         }
                          gateways.add(gatewaysReference);
                    }
                    if ("consumedBy".equals(referenceName)) {
                         AssetServerUse assetServerUse_relationship = (AssetServerUse)relationship;
                         ConsumedByReference consumedByReference = new ConsumedByReference(entityGuid,assetServerUse_relationship);
                         if ( consumedBy== null ) {
                              consumedBy = new HashSet();
                         }
                          consumedBy.add(consumedByReference);
                    }
                    if ("connections".equals(referenceName)) {
                         ConnectionToAsset connectionToAsset_relationship = (ConnectionToAsset)relationship;
                         ConnectionsReference connectionsReference = new ConnectionsReference(entityGuid,connectionToAsset_relationship);
                         if ( connections== null ) {
                              connections = new HashSet();
                         }
                          connections.add(connectionsReference);
                    }
                    if ("supportedDataSets".equals(referenceName)) {
                         DataContentForDataSet dataContentForDataSet_relationship = (DataContentForDataSet)relationship;
                         SupportedDataSetsReference supportedDataSetsReference = new SupportedDataSetsReference(entityGuid,dataContentForDataSet_relationship);
                         if ( supportedDataSets== null ) {
                              supportedDataSets = new HashSet();
                         }
                          supportedDataSets.add(supportedDataSetsReference);
                    }
                    if ("zones".equals(referenceName)) {
                         ZoneMembership zoneMembership_relationship = (ZoneMembership)relationship;
                         ZonesReference zonesReference = new ZonesReference(entityGuid,zoneMembership_relationship);
                         if ( zones== null ) {
                              zones = new HashSet();
                         }
                          zones.add(zonesReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationship;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
                    }
                    if ("meetings".equals(referenceName)) {
                         MeetingOnReferenceable meetingOnReferenceable_relationship = (MeetingOnReferenceable)relationship;
                         MeetingsReference meetingsReference = new MeetingsReference(entityGuid,meetingOnReferenceable_relationship);
                         if ( meetings== null ) {
                              meetings = new HashSet();
                         }
                          meetings.add(meetingsReference);
                    }
                    if ("actions".equals(referenceName)) {
                         ToDoSource toDoSource_relationship = (ToDoSource)relationship;
                         ActionsReference actionsReference = new ActionsReference(entityGuid,toDoSource_relationship);
                         if ( actions== null ) {
                              actions = new HashSet();
                         }
                          actions.add(actionsReference);
                    }
                    if ("contributors".equals(referenceName)) {
                         Contributor contributor_relationship = (Contributor)relationship;
                         ContributorsReference contributorsReference = new ContributorsReference(entityGuid,contributor_relationship);
                         if ( contributors== null ) {
                              contributors = new HashSet();
                         }
                          contributors.add(contributorsReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationship;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("starRatings".equals(referenceName)) {
                         AttachedRating attachedRating_relationship = (AttachedRating)relationship;
                         StarRatingsReference starRatingsReference = new StarRatingsReference(entityGuid,attachedRating_relationship);
                         if ( starRatings== null ) {
                              starRatings = new HashSet();
                         }
                          starRatings.add(starRatingsReference);
                    }
                    if ("tags".equals(referenceName)) {
                         AttachedTag attachedTag_relationship = (AttachedTag)relationship;
                         TagsReference tagsReference = new TagsReference(entityGuid,attachedTag_relationship);
                         if ( tags== null ) {
                              tags = new HashSet();
                         }
                          tags.add(tagsReference);
                    }
                    if ("relatedMedia".equals(referenceName)) {
                         MediaReference mediaReference_relationship = (MediaReference)relationship;
                         RelatedMediaReference relatedMediaReference = new RelatedMediaReference(entityGuid,mediaReference_relationship);
                         if ( relatedMedia== null ) {
                              relatedMedia = new HashSet();
                         }
                          relatedMedia.add(relatedMediaReference);
                    }
                    if ("certifications".equals(referenceName)) {
                         Certification certification_relationship = (Certification)relationship;
                         CertificationsReference certificationsReference = new CertificationsReference(entityGuid,certification_relationship);
                         if ( certifications== null ) {
                              certifications = new HashSet();
                         }
                          certifications.add(certificationsReference);
                    }
                    if ("managedResources".equals(referenceName)) {
                         ExternalIdScope externalIdScope_relationship = (ExternalIdScope)relationship;
                         ManagedResourcesReference managedResourcesReference = new ManagedResourcesReference(entityGuid,externalIdScope_relationship);
                         if ( managedResources== null ) {
                              managedResources = new HashSet();
                         }
                          managedResources.add(managedResourcesReference);
                    }
                    if ("alsoKnownAs".equals(referenceName)) {
                         ExternalIdLink externalIdLink_relationship = (ExternalIdLink)relationship;
                         AlsoKnownAsReference alsoKnownAsReference = new AlsoKnownAsReference(entityGuid,externalIdLink_relationship);
                         if ( alsoKnownAs== null ) {
                              alsoKnownAs = new HashSet();
                         }
                          alsoKnownAs.add(alsoKnownAsReference);
                    }
                    if ("staff".equals(referenceName)) {
                         StaffAssignment staffAssignment_relationship = (StaffAssignment)relationship;
                         StaffReference staffReference = new StaffReference(entityGuid,staffAssignment_relationship);
                         if ( staff== null ) {
                              staff = new HashSet();
                         }
                          staff.add(staffReference);
                    }
                    if ("todos".equals(referenceName)) {
                         ToDoOnReferenceable toDoOnReferenceable_relationship = (ToDoOnReferenceable)relationship;
                         TodosReference todosReference = new TodosReference(entityGuid,toDoOnReferenceable_relationship);
                         if ( todos== null ) {
                              todos = new HashSet();
                         }
                          todos.add(todosReference);
                    }
                    if ("comments".equals(referenceName)) {
                         AttachedComment attachedComment_relationship = (AttachedComment)relationship;
                         CommentsReference commentsReference = new CommentsReference(entityGuid,attachedComment_relationship);
                         if ( comments== null ) {
                              comments = new HashSet();
                         }
                          comments.add(commentsReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationship;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("externalReference".equals(referenceName)) {
                         ExternalReferenceLink externalReferenceLink_relationship = (ExternalReferenceLink)relationship;
                         ExternalReferenceReference externalReferenceReference = new ExternalReferenceReference(entityGuid,externalReferenceLink_relationship);
                         if ( externalReference== null ) {
                              externalReference = new HashSet();
                         }
                          externalReference.add(externalReferenceReference);
                    }
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationship;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMember collectionMember_relationship = (CollectionMember)relationship;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMember_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationship;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }

                    if ("schema".equals(referenceName)) {
                         AssetSchemaType assetSchemaType_relationship = (AssetSchemaType)relationship;
                         schema = new SchemaReference(entityGuid, assetSchemaType_relationship);
                    }
                 }
             }
         }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
    private SchemaReference schema;
// Set properties

    private Set<ConnectedHostsReference> connectedHosts;
    private Set<GatewaysReference> gateways;
    private Set<ConsumedByReference> consumedBy;
    private Set<ConnectionsReference> connections;
    private Set<SupportedDataSetsReference> supportedDataSets;
    private Set<ZonesReference> zones;
    private Set<FacetsReference> facets;
    private Set<MeetingsReference> meetings;
    private Set<ActionsReference> actions;
    private Set<ContributorsReference> contributors;
    private Set<LicensesReference> licenses;
    private Set<StarRatingsReference> starRatings;
    private Set<TagsReference> tags;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<CertificationsReference> certifications;
    private Set<ManagedResourcesReference> managedResources;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<StaffReference> staff;
    private Set<TodosReference> todos;
    private Set<CommentsReference> comments;
    private Set<MeaningReference> meaning;
    private Set<ExternalReferenceReference> externalReference;
    private Set<NoteLogsReference> noteLogs;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<LikesReference> likes;

// List properties

    // setters and setters
    public SchemaReference getSchemaReference() {
        return schema;   }

    public void setSchemaReference(SchemaReference schema) {
        this.schema = schema; }

// Sets
    public Set<ConnectedHostsReference> getConnectedHostsReferences() {
        return connectedHosts;
    }

    public void setConnectedHostsReferences(Set<ConnectedHostsReference> connectedHosts) {
        this.connectedHosts =connectedHosts;
    }
    public Set<GatewaysReference> getGatewaysReferences() {
        return gateways;
    }

    public void setGatewaysReferences(Set<GatewaysReference> gateways) {
        this.gateways =gateways;
    }
    public Set<ConsumedByReference> getConsumedByReferences() {
        return consumedBy;
    }

    public void setConsumedByReferences(Set<ConsumedByReference> consumedBy) {
        this.consumedBy =consumedBy;
    }
    public Set<ConnectionsReference> getConnectionsReferences() {
        return connections;
    }

    public void setConnectionsReferences(Set<ConnectionsReference> connections) {
        this.connections =connections;
    }
    public Set<SupportedDataSetsReference> getSupportedDataSetsReferences() {
        return supportedDataSets;
    }

    public void setSupportedDataSetsReferences(Set<SupportedDataSetsReference> supportedDataSets) {
        this.supportedDataSets =supportedDataSets;
    }
    public Set<ZonesReference> getZonesReferences() {
        return zones;
    }

    public void setZonesReferences(Set<ZonesReference> zones) {
        this.zones =zones;
    }
    public Set<FacetsReference> getFacetsReferences() {
        return facets;
    }

    public void setFacetsReferences(Set<FacetsReference> facets) {
        this.facets =facets;
    }
    public Set<MeetingsReference> getMeetingsReferences() {
        return meetings;
    }

    public void setMeetingsReferences(Set<MeetingsReference> meetings) {
        this.meetings =meetings;
    }
    public Set<ActionsReference> getActionsReferences() {
        return actions;
    }

    public void setActionsReferences(Set<ActionsReference> actions) {
        this.actions =actions;
    }
    public Set<ContributorsReference> getContributorsReferences() {
        return contributors;
    }

    public void setContributorsReferences(Set<ContributorsReference> contributors) {
        this.contributors =contributors;
    }
    public Set<LicensesReference> getLicensesReferences() {
        return licenses;
    }

    public void setLicensesReferences(Set<LicensesReference> licenses) {
        this.licenses =licenses;
    }
    public Set<StarRatingsReference> getStarRatingsReferences() {
        return starRatings;
    }

    public void setStarRatingsReferences(Set<StarRatingsReference> starRatings) {
        this.starRatings =starRatings;
    }
    public Set<TagsReference> getTagsReferences() {
        return tags;
    }

    public void setTagsReferences(Set<TagsReference> tags) {
        this.tags =tags;
    }
    public Set<RelatedMediaReference> getRelatedMediaReferences() {
        return relatedMedia;
    }

    public void setRelatedMediaReferences(Set<RelatedMediaReference> relatedMedia) {
        this.relatedMedia =relatedMedia;
    }
    public Set<CertificationsReference> getCertificationsReferences() {
        return certifications;
    }

    public void setCertificationsReferences(Set<CertificationsReference> certifications) {
        this.certifications =certifications;
    }
    public Set<ManagedResourcesReference> getManagedResourcesReferences() {
        return managedResources;
    }

    public void setManagedResourcesReferences(Set<ManagedResourcesReference> managedResources) {
        this.managedResources =managedResources;
    }
    public Set<AlsoKnownAsReference> getAlsoKnownAsReferences() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAsReferences(Set<AlsoKnownAsReference> alsoKnownAs) {
        this.alsoKnownAs =alsoKnownAs;
    }
    public Set<StaffReference> getStaffReferences() {
        return staff;
    }

    public void setStaffReferences(Set<StaffReference> staff) {
        this.staff =staff;
    }
    public Set<TodosReference> getTodosReferences() {
        return todos;
    }

    public void setTodosReferences(Set<TodosReference> todos) {
        this.todos =todos;
    }
    public Set<CommentsReference> getCommentsReferences() {
        return comments;
    }

    public void setCommentsReferences(Set<CommentsReference> comments) {
        this.comments =comments;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<ExternalReferenceReference> getExternalReferenceReferences() {
        return externalReference;
    }

    public void setExternalReferenceReferences(Set<ExternalReferenceReference> externalReference) {
        this.externalReference =externalReference;
    }
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
    }
    public Set<LikesReference> getLikesReferences() {
        return likes;
    }

    public void setLikesReferences(Set<LikesReference> likes) {
        this.likes =likes;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("NetworkReferences{");
        sb.append("connectedHostsReference='").append(connectedHosts.toString());
        sb.append("gatewaysReference='").append(gateways.toString());
        sb.append("consumedByReference='").append(consumedBy.toString());
        sb.append("connectionsReference='").append(connections.toString());
        sb.append("supportedDataSetsReference='").append(supportedDataSets.toString());
        sb.append("zonesReference='").append(zones.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("staffReference='").append(staff.toString());
        sb.append("todosReference='").append(todos.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("likesReference='").append(likes.toString());
        sb.append("schemaReference='").append(schema.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         NetworkReferences typedThat = (NetworkReferences) o;
      // compare single cardinality attributes
         if (this.connectedHosts != null && !Objects.equals(this.connectedHosts,typedThat.connectedHosts)) {
                            return false;
                 }
         if (this.gateways != null && !Objects.equals(this.gateways,typedThat.gateways)) {
                            return false;
                 }
         if (this.schema != null && !Objects.equals(this.schema,typedThat.schema)) {
                            return false;
                 }
         if (this.consumedBy != null && !Objects.equals(this.consumedBy,typedThat.consumedBy)) {
                            return false;
                 }
         if (this.connections != null && !Objects.equals(this.connections,typedThat.connections)) {
                            return false;
                 }
         if (this.supportedDataSets != null && !Objects.equals(this.supportedDataSets,typedThat.supportedDataSets)) {
                            return false;
                 }
         if (this.zones != null && !Objects.equals(this.zones,typedThat.zones)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.starRatings != null && !Objects.equals(this.starRatings,typedThat.starRatings)) {
                            return false;
                 }
         if (this.tags != null && !Objects.equals(this.tags,typedThat.tags)) {
                            return false;
                 }
         if (this.relatedMedia != null && !Objects.equals(this.relatedMedia,typedThat.relatedMedia)) {
                            return false;
                 }
         if (this.certifications != null && !Objects.equals(this.certifications,typedThat.certifications)) {
                            return false;
                 }
         if (this.managedResources != null && !Objects.equals(this.managedResources,typedThat.managedResources)) {
                            return false;
                 }
         if (this.alsoKnownAs != null && !Objects.equals(this.alsoKnownAs,typedThat.alsoKnownAs)) {
                            return false;
                 }
         if (this.staff != null && !Objects.equals(this.staff,typedThat.staff)) {
                            return false;
                 }
         if (this.todos != null && !Objects.equals(this.todos,typedThat.todos)) {
                            return false;
                 }
         if (this.comments != null && !Objects.equals(this.comments,typedThat.comments)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.likes != null && !Objects.equals(this.likes,typedThat.likes)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.connectedHosts
    ,this.gateways
    ,this.schema
    ,this.consumedBy
    ,this.connections
    ,this.supportedDataSets
    ,this.zones
    ,this.facets
    ,this.meetings
    ,this.actions
    ,this.contributors
    ,this.licenses
    ,this.starRatings
    ,this.tags
    ,this.relatedMedia
    ,this.certifications
    ,this.managedResources
    ,this.alsoKnownAs
    ,this.staff
    ,this.todos
    ,this.comments
    ,this.meaning
    ,this.externalReference
    ,this.noteLogs
    ,this.foundInCollections
    ,this.likes
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
