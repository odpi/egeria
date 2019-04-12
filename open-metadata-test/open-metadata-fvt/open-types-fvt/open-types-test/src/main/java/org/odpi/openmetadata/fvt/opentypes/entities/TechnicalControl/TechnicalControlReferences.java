/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.TechnicalControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceControlToGovernanceControl.LinkingControlsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceControlToGovernanceControl.LinkedControlsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceControlLink.GovernanceControlLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceControlToGovernancePolicy.PoliciesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceImplementation.GovernanceImplementation;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceImplementation.GovernanceImplementationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceControlToBusinessCapability.AffectedBusinessCapabilitiesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls.BusinessCapabilityControls;
import org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls.BusinessCapabilityControlsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.License;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.LicenseMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedToObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedFromObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLike.LikesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLikeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceDefinitionToGovernanceMetric.MetricsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric.GovernanceDefinitionMetric;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric.GovernanceDefinitionMetricMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPersonRole.GovernedByRolesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReferenceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembershipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRating.StarRatingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRatingMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.ResourceListAnchorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.ManagedResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCertificationType.CertificationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.Certification;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.CertificationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.Meetings;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.MeetingsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToInformalTag.TagsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTagMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedCommentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GovernanceDefinitionToGovernanceZone.GovernedZonesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ZoneGovernance.ZoneGovernance;
import org.odpi.openmetadata.fvt.opentypes.relationships.ZoneGovernance.ZoneGovernanceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCrowdSourcingContributor.ContributorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContribution;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContributionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSourceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.AlsoKnownAsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToDataClass.DataClassesAssignedToElementReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.RelatedActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.Actions;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.ActionsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.SupportingResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToProject.ImpactingProjectsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLogMapper;

import java.io.Serializable;
import java.util.*;
import org.odpi.openmetadata.fvt.opentypes.common.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * References for entity  TechnicalControl. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnicalControlReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(TechnicalControlReferences.class);
    private static final String className = TechnicalControlReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "linkingControls",
             "linkedControls",
             "policies",
             "affectedBusinessCapabilities",
             "licenses",
             "relatedToObjectAnnotations",
             "relatedFromObjectAnnotations",
             "likes",
             "meaning",
             "metrics",
             "governedByRoles",
             "relatedMedia",
             "foundInCollections",
             "starRatings",
             "resourceListAnchors",
             "externalReference",
             "managedResources",
             "certifications",
             "meetings",
             "tags",
             "comments",
             "governedZones",
             "contributors",
             "actions",
             "alsoKnownAs",
             "dataClassesAssignedToElement",
             "facets",
             "relatedActions",
             "supportingResources",
             "impactingProjects",
             "noteLogs",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "GovernanceControlLink",
             "GovernanceControlLink",
             "GovernanceImplementation",
             "BusinessCapabilityControls",
             "License",
             "RelationshipAnnotation",
             "RelationshipAnnotation",
             "AttachedLike",
             "SemanticAssignment",
             "GovernanceDefinitionMetric",
             "GovernanceRoleAssignment",
             "MediaReference",
             "CollectionMembership",
             "AttachedRating",
             "ResourceList",
             "ExternalReferenceLink",
             "ExternalIdScope",
             "Certification",
             "Meetings",
             "AttachedTag",
             "AttachedComment",
             "ZoneGovernance",
             "CrowdSourcingContribution",
             "ToDoSource",
             "ExternalIdLink",
             "DataClassAssignment",
             "ReferenceableFacet",
             "Actions",
             "ResourceList",
             "ProjectScope",
             "AttachedNoteLog",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity TechnicalControl
       * Each of these relationships should map to a reference (a uniquely named attribute in TechnicalControl).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public TechnicalControlReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("linkingControls".equals(referenceName)) {
                         GovernanceControlLink governanceControlLink_relationship = (GovernanceControlLink)relationships;
                         LinkingControlsReference linkingControlsReference = new LinkingControlsReference(entityGuid,governanceControlLink_relationship);
                         if ( linkingControls== null ) {
                              linkingControls = new HashSet();
                         }
                          linkingControls.add(linkingControlsReference);
                    }
                    if ("linkedControls".equals(referenceName)) {
                         GovernanceControlLink governanceControlLink_relationship = (GovernanceControlLink)relationships;
                         LinkedControlsReference linkedControlsReference = new LinkedControlsReference(entityGuid,governanceControlLink_relationship);
                         if ( linkedControls== null ) {
                              linkedControls = new HashSet();
                         }
                          linkedControls.add(linkedControlsReference);
                    }
                    if ("policies".equals(referenceName)) {
                         GovernanceImplementation governanceImplementation_relationship = (GovernanceImplementation)relationships;
                         PoliciesReference policiesReference = new PoliciesReference(entityGuid,governanceImplementation_relationship);
                         if ( policies== null ) {
                              policies = new HashSet();
                         }
                          policies.add(policiesReference);
                    }
                    if ("affectedBusinessCapabilities".equals(referenceName)) {
                         BusinessCapabilityControls businessCapabilityControls_relationship = (BusinessCapabilityControls)relationships;
                         AffectedBusinessCapabilitiesReference affectedBusinessCapabilitiesReference = new AffectedBusinessCapabilitiesReference(entityGuid,businessCapabilityControls_relationship);
                         if ( affectedBusinessCapabilities== null ) {
                              affectedBusinessCapabilities = new HashSet();
                         }
                          affectedBusinessCapabilities.add(affectedBusinessCapabilitiesReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationships;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("relatedFromObjectAnnotations".equals(referenceName)) {
                         RelationshipAnnotation relationshipAnnotation_relationship = (RelationshipAnnotation)relationships;
                         RelatedFromObjectAnnotationsReference relatedFromObjectAnnotationsReference = new RelatedFromObjectAnnotationsReference(entityGuid,relationshipAnnotation_relationship);
                         if ( relatedFromObjectAnnotations== null ) {
                              relatedFromObjectAnnotations = new HashSet();
                         }
                          relatedFromObjectAnnotations.add(relatedFromObjectAnnotationsReference);
                    }
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationships;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationships;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("metrics".equals(referenceName)) {
                         GovernanceDefinitionMetric governanceDefinitionMetric_relationship = (GovernanceDefinitionMetric)relationships;
                         MetricsReference metricsReference = new MetricsReference(entityGuid,governanceDefinitionMetric_relationship);
                         if ( metrics== null ) {
                              metrics = new HashSet();
                         }
                          metrics.add(metricsReference);
                    }
                    if ("governedByRoles".equals(referenceName)) {
                         GovernanceRoleAssignment governanceRoleAssignment_relationship = (GovernanceRoleAssignment)relationships;
                         GovernedByRolesReference governedByRolesReference = new GovernedByRolesReference(entityGuid,governanceRoleAssignment_relationship);
                         if ( governedByRoles== null ) {
                              governedByRoles = new HashSet();
                         }
                          governedByRoles.add(governedByRolesReference);
                    }
                    if ("relatedMedia".equals(referenceName)) {
                         MediaReference mediaReference_relationship = (MediaReference)relationships;
                         RelatedMediaReference relatedMediaReference = new RelatedMediaReference(entityGuid,mediaReference_relationship);
                         if ( relatedMedia== null ) {
                              relatedMedia = new HashSet();
                         }
                          relatedMedia.add(relatedMediaReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMembership collectionMembership_relationship = (CollectionMembership)relationships;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMembership_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("starRatings".equals(referenceName)) {
                         AttachedRating attachedRating_relationship = (AttachedRating)relationships;
                         StarRatingsReference starRatingsReference = new StarRatingsReference(entityGuid,attachedRating_relationship);
                         if ( starRatings== null ) {
                              starRatings = new HashSet();
                         }
                          starRatings.add(starRatingsReference);
                    }
                    if ("resourceListAnchors".equals(referenceName)) {
                         ResourceList resourceList_relationship = (ResourceList)relationships;
                         ResourceListAnchorsReference resourceListAnchorsReference = new ResourceListAnchorsReference(entityGuid,resourceList_relationship);
                         if ( resourceListAnchors== null ) {
                              resourceListAnchors = new HashSet();
                         }
                          resourceListAnchors.add(resourceListAnchorsReference);
                    }
                    if ("externalReference".equals(referenceName)) {
                         ExternalReferenceLink externalReferenceLink_relationship = (ExternalReferenceLink)relationships;
                         ExternalReferenceReference externalReferenceReference = new ExternalReferenceReference(entityGuid,externalReferenceLink_relationship);
                         if ( externalReference== null ) {
                              externalReference = new HashSet();
                         }
                          externalReference.add(externalReferenceReference);
                    }
                    if ("managedResources".equals(referenceName)) {
                         ExternalIdScope externalIdScope_relationship = (ExternalIdScope)relationships;
                         ManagedResourcesReference managedResourcesReference = new ManagedResourcesReference(entityGuid,externalIdScope_relationship);
                         if ( managedResources== null ) {
                              managedResources = new HashSet();
                         }
                          managedResources.add(managedResourcesReference);
                    }
                    if ("certifications".equals(referenceName)) {
                         Certification certification_relationship = (Certification)relationships;
                         CertificationsReference certificationsReference = new CertificationsReference(entityGuid,certification_relationship);
                         if ( certifications== null ) {
                              certifications = new HashSet();
                         }
                          certifications.add(certificationsReference);
                    }
                    if ("meetings".equals(referenceName)) {
                         Meetings meetings_relationship = (Meetings)relationships;
                         MeetingsReference meetingsReference = new MeetingsReference(entityGuid,meetings_relationship);
                         if ( meetings== null ) {
                              meetings = new HashSet();
                         }
                          meetings.add(meetingsReference);
                    }
                    if ("tags".equals(referenceName)) {
                         AttachedTag attachedTag_relationship = (AttachedTag)relationships;
                         TagsReference tagsReference = new TagsReference(entityGuid,attachedTag_relationship);
                         if ( tags== null ) {
                              tags = new HashSet();
                         }
                          tags.add(tagsReference);
                    }
                    if ("comments".equals(referenceName)) {
                         AttachedComment attachedComment_relationship = (AttachedComment)relationships;
                         CommentsReference commentsReference = new CommentsReference(entityGuid,attachedComment_relationship);
                         if ( comments== null ) {
                              comments = new HashSet();
                         }
                          comments.add(commentsReference);
                    }
                    if ("governedZones".equals(referenceName)) {
                         ZoneGovernance zoneGovernance_relationship = (ZoneGovernance)relationships;
                         GovernedZonesReference governedZonesReference = new GovernedZonesReference(entityGuid,zoneGovernance_relationship);
                         if ( governedZones== null ) {
                              governedZones = new HashSet();
                         }
                          governedZones.add(governedZonesReference);
                    }
                    if ("contributors".equals(referenceName)) {
                         CrowdSourcingContribution crowdSourcingContribution_relationship = (CrowdSourcingContribution)relationships;
                         ContributorsReference contributorsReference = new ContributorsReference(entityGuid,crowdSourcingContribution_relationship);
                         if ( contributors== null ) {
                              contributors = new HashSet();
                         }
                          contributors.add(contributorsReference);
                    }
                    if ("actions".equals(referenceName)) {
                         ToDoSource toDoSource_relationship = (ToDoSource)relationships;
                         ActionsReference actionsReference = new ActionsReference(entityGuid,toDoSource_relationship);
                         if ( actions== null ) {
                              actions = new HashSet();
                         }
                          actions.add(actionsReference);
                    }
                    if ("alsoKnownAs".equals(referenceName)) {
                         ExternalIdLink externalIdLink_relationship = (ExternalIdLink)relationships;
                         AlsoKnownAsReference alsoKnownAsReference = new AlsoKnownAsReference(entityGuid,externalIdLink_relationship);
                         if ( alsoKnownAs== null ) {
                              alsoKnownAs = new HashSet();
                         }
                          alsoKnownAs.add(alsoKnownAsReference);
                    }
                    if ("dataClassesAssignedToElement".equals(referenceName)) {
                         DataClassAssignment dataClassAssignment_relationship = (DataClassAssignment)relationships;
                         DataClassesAssignedToElementReference dataClassesAssignedToElementReference = new DataClassesAssignedToElementReference(entityGuid,dataClassAssignment_relationship);
                         if ( dataClassesAssignedToElement== null ) {
                              dataClassesAssignedToElement = new HashSet();
                         }
                          dataClassesAssignedToElement.add(dataClassesAssignedToElementReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationships;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
                    }
                    if ("relatedActions".equals(referenceName)) {
                         Actions actions_relationship = (Actions)relationships;
                         RelatedActionsReference relatedActionsReference = new RelatedActionsReference(entityGuid,actions_relationship);
                         if ( relatedActions== null ) {
                              relatedActions = new HashSet();
                         }
                          relatedActions.add(relatedActionsReference);
                    }
                    if ("supportingResources".equals(referenceName)) {
                         ResourceList resourceList_relationship = (ResourceList)relationships;
                         SupportingResourcesReference supportingResourcesReference = new SupportingResourcesReference(entityGuid,resourceList_relationship);
                         if ( supportingResources== null ) {
                              supportingResources = new HashSet();
                         }
                          supportingResources.add(supportingResourcesReference);
                    }
                    if ("impactingProjects".equals(referenceName)) {
                         ProjectScope projectScope_relationship = (ProjectScope)relationships;
                         ImpactingProjectsReference impactingProjectsReference = new ImpactingProjectsReference(entityGuid,projectScope_relationship);
                         if ( impactingProjects== null ) {
                              impactingProjects = new HashSet();
                         }
                          impactingProjects.add(impactingProjectsReference);
                    }
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationships;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }

                    if ("relatedToObjectAnnotations".equals(referenceName)) {
                         RelationshipAnnotation relationshipAnnotation_relationship = (RelationshipAnnotation)relationships;
                         relatedToObjectAnnotations = new RelatedToObjectAnnotationsReference(entityGuid, relationshipAnnotation_relationship);
                    }
                 }
             }
         }
        }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
    private RelatedToObjectAnnotationsReference relatedToObjectAnnotations;
// Set properties

    private Set<LinkingControlsReference> linkingControls;
    private Set<LinkedControlsReference> linkedControls;
    private Set<PoliciesReference> policies;
    private Set<AffectedBusinessCapabilitiesReference> affectedBusinessCapabilities;
    private Set<LicensesReference> licenses;
    private Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations;
    private Set<LikesReference> likes;
    private Set<MeaningReference> meaning;
    private Set<MetricsReference> metrics;
    private Set<GovernedByRolesReference> governedByRoles;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<StarRatingsReference> starRatings;
    private Set<ResourceListAnchorsReference> resourceListAnchors;
    private Set<ExternalReferenceReference> externalReference;
    private Set<ManagedResourcesReference> managedResources;
    private Set<CertificationsReference> certifications;
    private Set<MeetingsReference> meetings;
    private Set<TagsReference> tags;
    private Set<CommentsReference> comments;
    private Set<GovernedZonesReference> governedZones;
    private Set<ContributorsReference> contributors;
    private Set<ActionsReference> actions;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement;
    private Set<FacetsReference> facets;
    private Set<RelatedActionsReference> relatedActions;
    private Set<SupportingResourcesReference> supportingResources;
    private Set<ImpactingProjectsReference> impactingProjects;
    private Set<NoteLogsReference> noteLogs;

// List properties

    // setters and setters
    public RelatedToObjectAnnotationsReference getRelatedToObjectAnnotationsReference() {
        return relatedToObjectAnnotations;   }

    public void setRelatedToObjectAnnotationsReference(RelatedToObjectAnnotationsReference relatedToObjectAnnotations) {
        this.relatedToObjectAnnotations = relatedToObjectAnnotations; }

// Sets
    public Set<LinkingControlsReference> getLinkingControlsReferences() {
        return linkingControls;
    }

    public void setLinkingControlsReferences(Set<LinkingControlsReference> linkingControls) {
        this.linkingControls =linkingControls;
    }
    public Set<LinkedControlsReference> getLinkedControlsReferences() {
        return linkedControls;
    }

    public void setLinkedControlsReferences(Set<LinkedControlsReference> linkedControls) {
        this.linkedControls =linkedControls;
    }
    public Set<PoliciesReference> getPoliciesReferences() {
        return policies;
    }

    public void setPoliciesReferences(Set<PoliciesReference> policies) {
        this.policies =policies;
    }
    public Set<AffectedBusinessCapabilitiesReference> getAffectedBusinessCapabilitiesReferences() {
        return affectedBusinessCapabilities;
    }

    public void setAffectedBusinessCapabilitiesReferences(Set<AffectedBusinessCapabilitiesReference> affectedBusinessCapabilities) {
        this.affectedBusinessCapabilities =affectedBusinessCapabilities;
    }
    public Set<LicensesReference> getLicensesReferences() {
        return licenses;
    }

    public void setLicensesReferences(Set<LicensesReference> licenses) {
        this.licenses =licenses;
    }
    public Set<RelatedFromObjectAnnotationsReference> getRelatedFromObjectAnnotationsReferences() {
        return relatedFromObjectAnnotations;
    }

    public void setRelatedFromObjectAnnotationsReferences(Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations) {
        this.relatedFromObjectAnnotations =relatedFromObjectAnnotations;
    }
    public Set<LikesReference> getLikesReferences() {
        return likes;
    }

    public void setLikesReferences(Set<LikesReference> likes) {
        this.likes =likes;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<MetricsReference> getMetricsReferences() {
        return metrics;
    }

    public void setMetricsReferences(Set<MetricsReference> metrics) {
        this.metrics =metrics;
    }
    public Set<GovernedByRolesReference> getGovernedByRolesReferences() {
        return governedByRoles;
    }

    public void setGovernedByRolesReferences(Set<GovernedByRolesReference> governedByRoles) {
        this.governedByRoles =governedByRoles;
    }
    public Set<RelatedMediaReference> getRelatedMediaReferences() {
        return relatedMedia;
    }

    public void setRelatedMediaReferences(Set<RelatedMediaReference> relatedMedia) {
        this.relatedMedia =relatedMedia;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
    }
    public Set<StarRatingsReference> getStarRatingsReferences() {
        return starRatings;
    }

    public void setStarRatingsReferences(Set<StarRatingsReference> starRatings) {
        this.starRatings =starRatings;
    }
    public Set<ResourceListAnchorsReference> getResourceListAnchorsReferences() {
        return resourceListAnchors;
    }

    public void setResourceListAnchorsReferences(Set<ResourceListAnchorsReference> resourceListAnchors) {
        this.resourceListAnchors =resourceListAnchors;
    }
    public Set<ExternalReferenceReference> getExternalReferenceReferences() {
        return externalReference;
    }

    public void setExternalReferenceReferences(Set<ExternalReferenceReference> externalReference) {
        this.externalReference =externalReference;
    }
    public Set<ManagedResourcesReference> getManagedResourcesReferences() {
        return managedResources;
    }

    public void setManagedResourcesReferences(Set<ManagedResourcesReference> managedResources) {
        this.managedResources =managedResources;
    }
    public Set<CertificationsReference> getCertificationsReferences() {
        return certifications;
    }

    public void setCertificationsReferences(Set<CertificationsReference> certifications) {
        this.certifications =certifications;
    }
    public Set<MeetingsReference> getMeetingsReferences() {
        return meetings;
    }

    public void setMeetingsReferences(Set<MeetingsReference> meetings) {
        this.meetings =meetings;
    }
    public Set<TagsReference> getTagsReferences() {
        return tags;
    }

    public void setTagsReferences(Set<TagsReference> tags) {
        this.tags =tags;
    }
    public Set<CommentsReference> getCommentsReferences() {
        return comments;
    }

    public void setCommentsReferences(Set<CommentsReference> comments) {
        this.comments =comments;
    }
    public Set<GovernedZonesReference> getGovernedZonesReferences() {
        return governedZones;
    }

    public void setGovernedZonesReferences(Set<GovernedZonesReference> governedZones) {
        this.governedZones =governedZones;
    }
    public Set<ContributorsReference> getContributorsReferences() {
        return contributors;
    }

    public void setContributorsReferences(Set<ContributorsReference> contributors) {
        this.contributors =contributors;
    }
    public Set<ActionsReference> getActionsReferences() {
        return actions;
    }

    public void setActionsReferences(Set<ActionsReference> actions) {
        this.actions =actions;
    }
    public Set<AlsoKnownAsReference> getAlsoKnownAsReferences() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAsReferences(Set<AlsoKnownAsReference> alsoKnownAs) {
        this.alsoKnownAs =alsoKnownAs;
    }
    public Set<DataClassesAssignedToElementReference> getDataClassesAssignedToElementReferences() {
        return dataClassesAssignedToElement;
    }

    public void setDataClassesAssignedToElementReferences(Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement) {
        this.dataClassesAssignedToElement =dataClassesAssignedToElement;
    }
    public Set<FacetsReference> getFacetsReferences() {
        return facets;
    }

    public void setFacetsReferences(Set<FacetsReference> facets) {
        this.facets =facets;
    }
    public Set<RelatedActionsReference> getRelatedActionsReferences() {
        return relatedActions;
    }

    public void setRelatedActionsReferences(Set<RelatedActionsReference> relatedActions) {
        this.relatedActions =relatedActions;
    }
    public Set<SupportingResourcesReference> getSupportingResourcesReferences() {
        return supportingResources;
    }

    public void setSupportingResourcesReferences(Set<SupportingResourcesReference> supportingResources) {
        this.supportingResources =supportingResources;
    }
    public Set<ImpactingProjectsReference> getImpactingProjectsReferences() {
        return impactingProjects;
    }

    public void setImpactingProjectsReferences(Set<ImpactingProjectsReference> impactingProjects) {
        this.impactingProjects =impactingProjects;
    }
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("TechnicalControlReferences{");
        sb.append("linkingControlsReference='").append(linkingControls.toString());
        sb.append("linkedControlsReference='").append(linkedControls.toString());
        sb.append("policiesReference='").append(policies.toString());
        sb.append("affectedBusinessCapabilitiesReference='").append(affectedBusinessCapabilities.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("relatedFromObjectAnnotationsReference='").append(relatedFromObjectAnnotations.toString());
        sb.append("likesReference='").append(likes.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("metricsReference='").append(metrics.toString());
        sb.append("governedByRolesReference='").append(governedByRoles.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("resourceListAnchorsReference='").append(resourceListAnchors.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("governedZonesReference='").append(governedZones.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("dataClassesAssignedToElementReference='").append(dataClassesAssignedToElement.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("relatedActionsReference='").append(relatedActions.toString());
        sb.append("supportingResourcesReference='").append(supportingResources.toString());
        sb.append("impactingProjectsReference='").append(impactingProjects.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("relatedToObjectAnnotationsReference='").append(relatedToObjectAnnotations.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         TechnicalControlReferences typedThat = (TechnicalControlReferences) o;
      // compare single cardinality attributes
         if (this.linkingControls != null && !Objects.equals(this.linkingControls,typedThat.linkingControls)) {
                            return false;
                 }
         if (this.linkedControls != null && !Objects.equals(this.linkedControls,typedThat.linkedControls)) {
                            return false;
                 }
         if (this.policies != null && !Objects.equals(this.policies,typedThat.policies)) {
                            return false;
                 }
         if (this.affectedBusinessCapabilities != null && !Objects.equals(this.affectedBusinessCapabilities,typedThat.affectedBusinessCapabilities)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.relatedToObjectAnnotations != null && !Objects.equals(this.relatedToObjectAnnotations,typedThat.relatedToObjectAnnotations)) {
                            return false;
                 }
         if (this.relatedFromObjectAnnotations != null && !Objects.equals(this.relatedFromObjectAnnotations,typedThat.relatedFromObjectAnnotations)) {
                            return false;
                 }
         if (this.likes != null && !Objects.equals(this.likes,typedThat.likes)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.metrics != null && !Objects.equals(this.metrics,typedThat.metrics)) {
                            return false;
                 }
         if (this.governedByRoles != null && !Objects.equals(this.governedByRoles,typedThat.governedByRoles)) {
                            return false;
                 }
         if (this.relatedMedia != null && !Objects.equals(this.relatedMedia,typedThat.relatedMedia)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.starRatings != null && !Objects.equals(this.starRatings,typedThat.starRatings)) {
                            return false;
                 }
         if (this.resourceListAnchors != null && !Objects.equals(this.resourceListAnchors,typedThat.resourceListAnchors)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.managedResources != null && !Objects.equals(this.managedResources,typedThat.managedResources)) {
                            return false;
                 }
         if (this.certifications != null && !Objects.equals(this.certifications,typedThat.certifications)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.tags != null && !Objects.equals(this.tags,typedThat.tags)) {
                            return false;
                 }
         if (this.comments != null && !Objects.equals(this.comments,typedThat.comments)) {
                            return false;
                 }
         if (this.governedZones != null && !Objects.equals(this.governedZones,typedThat.governedZones)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.alsoKnownAs != null && !Objects.equals(this.alsoKnownAs,typedThat.alsoKnownAs)) {
                            return false;
                 }
         if (this.dataClassesAssignedToElement != null && !Objects.equals(this.dataClassesAssignedToElement,typedThat.dataClassesAssignedToElement)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.relatedActions != null && !Objects.equals(this.relatedActions,typedThat.relatedActions)) {
                            return false;
                 }
         if (this.supportingResources != null && !Objects.equals(this.supportingResources,typedThat.supportingResources)) {
                            return false;
                 }
         if (this.impactingProjects != null && !Objects.equals(this.impactingProjects,typedThat.impactingProjects)) {
                            return false;
                 }
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.linkingControls
    ,this.linkedControls
    ,this.policies
    ,this.affectedBusinessCapabilities
    ,this.licenses
    ,this.relatedToObjectAnnotations
    ,this.relatedFromObjectAnnotations
    ,this.likes
    ,this.meaning
    ,this.metrics
    ,this.governedByRoles
    ,this.relatedMedia
    ,this.foundInCollections
    ,this.starRatings
    ,this.resourceListAnchors
    ,this.externalReference
    ,this.managedResources
    ,this.certifications
    ,this.meetings
    ,this.tags
    ,this.comments
    ,this.governedZones
    ,this.contributors
    ,this.actions
    ,this.alsoKnownAs
    ,this.dataClassesAssignedToElement
    ,this.facets
    ,this.relatedActions
    ,this.supportingResources
    ,this.impactingProjects
    ,this.noteLogs
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
