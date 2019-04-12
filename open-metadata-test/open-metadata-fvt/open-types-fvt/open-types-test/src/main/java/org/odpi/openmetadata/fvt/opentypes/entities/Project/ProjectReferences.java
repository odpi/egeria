/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProjectCharter.CharterReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectCharterLink.ProjectCharterLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectCharterLink.ProjectCharterLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProject.ManagedProjectReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchyMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProject.ManagingProjectReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchy;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectHierarchy.ProjectHierarchyMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProject.DependsOnProjectsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependency;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependencyMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProject.DependentProjectReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependency;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectDependency.ProjectDependencyMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToProjectManager.ProjectManagersReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectManagement.ProjectManagement;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectManagement.ProjectManagementMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToActorProfile.SupportingActorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectTeam.ProjectTeam;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectTeam.ProjectTeamMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ProjectToReferenceable.ProjectScopeReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReferenceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.SupportingResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCertificationType.CertificationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.Certification;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.CertificationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPersonRole.GovernedByRolesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedCommentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCrowdSourcingContributor.ContributorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContribution;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContributionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.License;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.LicenseMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.RelatedActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.Actions;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.ActionsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRating.StarRatingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRatingMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedFromObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSourceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToDataClass.DataClassesAssignedToElementReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.ManagedResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.AlsoKnownAsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.ResourceListAnchorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedToObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToProject.ImpactingProjectsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembershipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.Meetings;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.MeetingsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToInformalTag.TagsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTagMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLike.LikesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLikeMapper;

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
 * References for entity  Project. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ProjectReferences.class);
    private static final String className = ProjectReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "charter",
             "managedProject",
             "managingProject",
             "dependsOnProjects",
             "dependentProject",
             "projectManagers",
             "supportingActors",
             "projectScope",
             "relatedMedia",
             "supportingResources",
             "certifications",
             "noteLogs",
             "governedByRoles",
             "comments",
             "contributors",
             "licenses",
             "relatedActions",
             "starRatings",
             "relatedFromObjectAnnotations",
             "actions",
             "dataClassesAssignedToElement",
             "managedResources",
             "alsoKnownAs",
             "resourceListAnchors",
             "relatedToObjectAnnotations",
             "externalReference",
             "impactingProjects",
             "meaning",
             "foundInCollections",
             "facets",
             "meetings",
             "tags",
             "likes",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "ProjectCharterLink",
             "ProjectHierarchy",
             "ProjectHierarchy",
             "ProjectDependency",
             "ProjectDependency",
             "ProjectManagement",
             "ProjectTeam",
             "ProjectScope",
             "MediaReference",
             "ResourceList",
             "Certification",
             "AttachedNoteLog",
             "GovernanceRoleAssignment",
             "AttachedComment",
             "CrowdSourcingContribution",
             "License",
             "Actions",
             "AttachedRating",
             "RelationshipAnnotation",
             "ToDoSource",
             "DataClassAssignment",
             "ExternalIdScope",
             "ExternalIdLink",
             "ResourceList",
             "RelationshipAnnotation",
             "ExternalReferenceLink",
             "ProjectScope",
             "SemanticAssignment",
             "CollectionMembership",
             "ReferenceableFacet",
             "Meetings",
             "AttachedTag",
             "AttachedLike",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity Project
       * Each of these relationships should map to a reference (a uniquely named attribute in Project).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public ProjectReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("managedProject".equals(referenceName)) {
                         ProjectHierarchy projectHierarchy_relationship = (ProjectHierarchy)relationships;
                         ManagedProjectReference managedProjectReference = new ManagedProjectReference(entityGuid,projectHierarchy_relationship);
                         if ( managedProject== null ) {
                              managedProject = new HashSet();
                         }
                          managedProject.add(managedProjectReference);
                    }
                    if ("dependsOnProjects".equals(referenceName)) {
                         ProjectDependency projectDependency_relationship = (ProjectDependency)relationships;
                         DependsOnProjectsReference dependsOnProjectsReference = new DependsOnProjectsReference(entityGuid,projectDependency_relationship);
                         if ( dependsOnProjects== null ) {
                              dependsOnProjects = new HashSet();
                         }
                          dependsOnProjects.add(dependsOnProjectsReference);
                    }
                    if ("dependentProject".equals(referenceName)) {
                         ProjectDependency projectDependency_relationship = (ProjectDependency)relationships;
                         DependentProjectReference dependentProjectReference = new DependentProjectReference(entityGuid,projectDependency_relationship);
                         if ( dependentProject== null ) {
                              dependentProject = new HashSet();
                         }
                          dependentProject.add(dependentProjectReference);
                    }
                    if ("projectManagers".equals(referenceName)) {
                         ProjectManagement projectManagement_relationship = (ProjectManagement)relationships;
                         ProjectManagersReference projectManagersReference = new ProjectManagersReference(entityGuid,projectManagement_relationship);
                         if ( projectManagers== null ) {
                              projectManagers = new HashSet();
                         }
                          projectManagers.add(projectManagersReference);
                    }
                    if ("supportingActors".equals(referenceName)) {
                         ProjectTeam projectTeam_relationship = (ProjectTeam)relationships;
                         SupportingActorsReference supportingActorsReference = new SupportingActorsReference(entityGuid,projectTeam_relationship);
                         if ( supportingActors== null ) {
                              supportingActors = new HashSet();
                         }
                          supportingActors.add(supportingActorsReference);
                    }
                    if ("projectScope".equals(referenceName)) {
                         ProjectScope projectScope_relationship = (ProjectScope)relationships;
                         ProjectScopeReference projectScopeReference = new ProjectScopeReference(entityGuid,projectScope_relationship);
                         if ( projectScope== null ) {
                              projectScope = new HashSet();
                         }
                          projectScope.add(projectScopeReference);
                    }
                    if ("relatedMedia".equals(referenceName)) {
                         MediaReference mediaReference_relationship = (MediaReference)relationships;
                         RelatedMediaReference relatedMediaReference = new RelatedMediaReference(entityGuid,mediaReference_relationship);
                         if ( relatedMedia== null ) {
                              relatedMedia = new HashSet();
                         }
                          relatedMedia.add(relatedMediaReference);
                    }
                    if ("supportingResources".equals(referenceName)) {
                         ResourceList resourceList_relationship = (ResourceList)relationships;
                         SupportingResourcesReference supportingResourcesReference = new SupportingResourcesReference(entityGuid,resourceList_relationship);
                         if ( supportingResources== null ) {
                              supportingResources = new HashSet();
                         }
                          supportingResources.add(supportingResourcesReference);
                    }
                    if ("certifications".equals(referenceName)) {
                         Certification certification_relationship = (Certification)relationships;
                         CertificationsReference certificationsReference = new CertificationsReference(entityGuid,certification_relationship);
                         if ( certifications== null ) {
                              certifications = new HashSet();
                         }
                          certifications.add(certificationsReference);
                    }
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationships;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }
                    if ("governedByRoles".equals(referenceName)) {
                         GovernanceRoleAssignment governanceRoleAssignment_relationship = (GovernanceRoleAssignment)relationships;
                         GovernedByRolesReference governedByRolesReference = new GovernedByRolesReference(entityGuid,governanceRoleAssignment_relationship);
                         if ( governedByRoles== null ) {
                              governedByRoles = new HashSet();
                         }
                          governedByRoles.add(governedByRolesReference);
                    }
                    if ("comments".equals(referenceName)) {
                         AttachedComment attachedComment_relationship = (AttachedComment)relationships;
                         CommentsReference commentsReference = new CommentsReference(entityGuid,attachedComment_relationship);
                         if ( comments== null ) {
                              comments = new HashSet();
                         }
                          comments.add(commentsReference);
                    }
                    if ("contributors".equals(referenceName)) {
                         CrowdSourcingContribution crowdSourcingContribution_relationship = (CrowdSourcingContribution)relationships;
                         ContributorsReference contributorsReference = new ContributorsReference(entityGuid,crowdSourcingContribution_relationship);
                         if ( contributors== null ) {
                              contributors = new HashSet();
                         }
                          contributors.add(contributorsReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationships;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("relatedActions".equals(referenceName)) {
                         Actions actions_relationship = (Actions)relationships;
                         RelatedActionsReference relatedActionsReference = new RelatedActionsReference(entityGuid,actions_relationship);
                         if ( relatedActions== null ) {
                              relatedActions = new HashSet();
                         }
                          relatedActions.add(relatedActionsReference);
                    }
                    if ("starRatings".equals(referenceName)) {
                         AttachedRating attachedRating_relationship = (AttachedRating)relationships;
                         StarRatingsReference starRatingsReference = new StarRatingsReference(entityGuid,attachedRating_relationship);
                         if ( starRatings== null ) {
                              starRatings = new HashSet();
                         }
                          starRatings.add(starRatingsReference);
                    }
                    if ("relatedFromObjectAnnotations".equals(referenceName)) {
                         RelationshipAnnotation relationshipAnnotation_relationship = (RelationshipAnnotation)relationships;
                         RelatedFromObjectAnnotationsReference relatedFromObjectAnnotationsReference = new RelatedFromObjectAnnotationsReference(entityGuid,relationshipAnnotation_relationship);
                         if ( relatedFromObjectAnnotations== null ) {
                              relatedFromObjectAnnotations = new HashSet();
                         }
                          relatedFromObjectAnnotations.add(relatedFromObjectAnnotationsReference);
                    }
                    if ("actions".equals(referenceName)) {
                         ToDoSource toDoSource_relationship = (ToDoSource)relationships;
                         ActionsReference actionsReference = new ActionsReference(entityGuid,toDoSource_relationship);
                         if ( actions== null ) {
                              actions = new HashSet();
                         }
                          actions.add(actionsReference);
                    }
                    if ("dataClassesAssignedToElement".equals(referenceName)) {
                         DataClassAssignment dataClassAssignment_relationship = (DataClassAssignment)relationships;
                         DataClassesAssignedToElementReference dataClassesAssignedToElementReference = new DataClassesAssignedToElementReference(entityGuid,dataClassAssignment_relationship);
                         if ( dataClassesAssignedToElement== null ) {
                              dataClassesAssignedToElement = new HashSet();
                         }
                          dataClassesAssignedToElement.add(dataClassesAssignedToElementReference);
                    }
                    if ("managedResources".equals(referenceName)) {
                         ExternalIdScope externalIdScope_relationship = (ExternalIdScope)relationships;
                         ManagedResourcesReference managedResourcesReference = new ManagedResourcesReference(entityGuid,externalIdScope_relationship);
                         if ( managedResources== null ) {
                              managedResources = new HashSet();
                         }
                          managedResources.add(managedResourcesReference);
                    }
                    if ("alsoKnownAs".equals(referenceName)) {
                         ExternalIdLink externalIdLink_relationship = (ExternalIdLink)relationships;
                         AlsoKnownAsReference alsoKnownAsReference = new AlsoKnownAsReference(entityGuid,externalIdLink_relationship);
                         if ( alsoKnownAs== null ) {
                              alsoKnownAs = new HashSet();
                         }
                          alsoKnownAs.add(alsoKnownAsReference);
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
                    if ("impactingProjects".equals(referenceName)) {
                         ProjectScope projectScope_relationship = (ProjectScope)relationships;
                         ImpactingProjectsReference impactingProjectsReference = new ImpactingProjectsReference(entityGuid,projectScope_relationship);
                         if ( impactingProjects== null ) {
                              impactingProjects = new HashSet();
                         }
                          impactingProjects.add(impactingProjectsReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationships;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMembership collectionMembership_relationship = (CollectionMembership)relationships;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMembership_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationships;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
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
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationships;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }

                    if ("charter".equals(referenceName)) {
                         ProjectCharterLink projectCharterLink_relationship = (ProjectCharterLink)relationships;
                         charter = new CharterReference(entityGuid, projectCharterLink_relationship);
                    }
                    if ("managingProject".equals(referenceName)) {
                         ProjectHierarchy projectHierarchy_relationship = (ProjectHierarchy)relationships;
                         managingProject = new ManagingProjectReference(entityGuid, projectHierarchy_relationship);
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
    private CharterReference charter;
    private ManagingProjectReference managingProject;
    private RelatedToObjectAnnotationsReference relatedToObjectAnnotations;
// Set properties

    private Set<ManagedProjectReference> managedProject;
    private Set<DependsOnProjectsReference> dependsOnProjects;
    private Set<DependentProjectReference> dependentProject;
    private Set<ProjectManagersReference> projectManagers;
    private Set<SupportingActorsReference> supportingActors;
    private Set<ProjectScopeReference> projectScope;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<SupportingResourcesReference> supportingResources;
    private Set<CertificationsReference> certifications;
    private Set<NoteLogsReference> noteLogs;
    private Set<GovernedByRolesReference> governedByRoles;
    private Set<CommentsReference> comments;
    private Set<ContributorsReference> contributors;
    private Set<LicensesReference> licenses;
    private Set<RelatedActionsReference> relatedActions;
    private Set<StarRatingsReference> starRatings;
    private Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations;
    private Set<ActionsReference> actions;
    private Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement;
    private Set<ManagedResourcesReference> managedResources;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<ResourceListAnchorsReference> resourceListAnchors;
    private Set<ExternalReferenceReference> externalReference;
    private Set<ImpactingProjectsReference> impactingProjects;
    private Set<MeaningReference> meaning;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<FacetsReference> facets;
    private Set<MeetingsReference> meetings;
    private Set<TagsReference> tags;
    private Set<LikesReference> likes;

// List properties

    // setters and setters
    public CharterReference getCharterReference() {
        return charter;   }

    public void setCharterReference(CharterReference charter) {
        this.charter = charter; }
    public ManagingProjectReference getManagingProjectReference() {
        return managingProject;   }

    public void setManagingProjectReference(ManagingProjectReference managingProject) {
        this.managingProject = managingProject; }
    public RelatedToObjectAnnotationsReference getRelatedToObjectAnnotationsReference() {
        return relatedToObjectAnnotations;   }

    public void setRelatedToObjectAnnotationsReference(RelatedToObjectAnnotationsReference relatedToObjectAnnotations) {
        this.relatedToObjectAnnotations = relatedToObjectAnnotations; }

// Sets
    public Set<ManagedProjectReference> getManagedProjectReferences() {
        return managedProject;
    }

    public void setManagedProjectReferences(Set<ManagedProjectReference> managedProject) {
        this.managedProject =managedProject;
    }
    public Set<DependsOnProjectsReference> getDependsOnProjectsReferences() {
        return dependsOnProjects;
    }

    public void setDependsOnProjectsReferences(Set<DependsOnProjectsReference> dependsOnProjects) {
        this.dependsOnProjects =dependsOnProjects;
    }
    public Set<DependentProjectReference> getDependentProjectReferences() {
        return dependentProject;
    }

    public void setDependentProjectReferences(Set<DependentProjectReference> dependentProject) {
        this.dependentProject =dependentProject;
    }
    public Set<ProjectManagersReference> getProjectManagersReferences() {
        return projectManagers;
    }

    public void setProjectManagersReferences(Set<ProjectManagersReference> projectManagers) {
        this.projectManagers =projectManagers;
    }
    public Set<SupportingActorsReference> getSupportingActorsReferences() {
        return supportingActors;
    }

    public void setSupportingActorsReferences(Set<SupportingActorsReference> supportingActors) {
        this.supportingActors =supportingActors;
    }
    public Set<ProjectScopeReference> getProjectScopeReferences() {
        return projectScope;
    }

    public void setProjectScopeReferences(Set<ProjectScopeReference> projectScope) {
        this.projectScope =projectScope;
    }
    public Set<RelatedMediaReference> getRelatedMediaReferences() {
        return relatedMedia;
    }

    public void setRelatedMediaReferences(Set<RelatedMediaReference> relatedMedia) {
        this.relatedMedia =relatedMedia;
    }
    public Set<SupportingResourcesReference> getSupportingResourcesReferences() {
        return supportingResources;
    }

    public void setSupportingResourcesReferences(Set<SupportingResourcesReference> supportingResources) {
        this.supportingResources =supportingResources;
    }
    public Set<CertificationsReference> getCertificationsReferences() {
        return certifications;
    }

    public void setCertificationsReferences(Set<CertificationsReference> certifications) {
        this.certifications =certifications;
    }
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }
    public Set<GovernedByRolesReference> getGovernedByRolesReferences() {
        return governedByRoles;
    }

    public void setGovernedByRolesReferences(Set<GovernedByRolesReference> governedByRoles) {
        this.governedByRoles =governedByRoles;
    }
    public Set<CommentsReference> getCommentsReferences() {
        return comments;
    }

    public void setCommentsReferences(Set<CommentsReference> comments) {
        this.comments =comments;
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
    public Set<RelatedActionsReference> getRelatedActionsReferences() {
        return relatedActions;
    }

    public void setRelatedActionsReferences(Set<RelatedActionsReference> relatedActions) {
        this.relatedActions =relatedActions;
    }
    public Set<StarRatingsReference> getStarRatingsReferences() {
        return starRatings;
    }

    public void setStarRatingsReferences(Set<StarRatingsReference> starRatings) {
        this.starRatings =starRatings;
    }
    public Set<RelatedFromObjectAnnotationsReference> getRelatedFromObjectAnnotationsReferences() {
        return relatedFromObjectAnnotations;
    }

    public void setRelatedFromObjectAnnotationsReferences(Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations) {
        this.relatedFromObjectAnnotations =relatedFromObjectAnnotations;
    }
    public Set<ActionsReference> getActionsReferences() {
        return actions;
    }

    public void setActionsReferences(Set<ActionsReference> actions) {
        this.actions =actions;
    }
    public Set<DataClassesAssignedToElementReference> getDataClassesAssignedToElementReferences() {
        return dataClassesAssignedToElement;
    }

    public void setDataClassesAssignedToElementReferences(Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement) {
        this.dataClassesAssignedToElement =dataClassesAssignedToElement;
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
    public Set<ImpactingProjectsReference> getImpactingProjectsReferences() {
        return impactingProjects;
    }

    public void setImpactingProjectsReferences(Set<ImpactingProjectsReference> impactingProjects) {
        this.impactingProjects =impactingProjects;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
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
    public Set<TagsReference> getTagsReferences() {
        return tags;
    }

    public void setTagsReferences(Set<TagsReference> tags) {
        this.tags =tags;
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

        sb.append("ProjectReferences{");
        sb.append("managedProjectReference='").append(managedProject.toString());
        sb.append("dependsOnProjectsReference='").append(dependsOnProjects.toString());
        sb.append("dependentProjectReference='").append(dependentProject.toString());
        sb.append("projectManagersReference='").append(projectManagers.toString());
        sb.append("supportingActorsReference='").append(supportingActors.toString());
        sb.append("projectScopeReference='").append(projectScope.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("supportingResourcesReference='").append(supportingResources.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("governedByRolesReference='").append(governedByRoles.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("relatedActionsReference='").append(relatedActions.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("relatedFromObjectAnnotationsReference='").append(relatedFromObjectAnnotations.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("dataClassesAssignedToElementReference='").append(dataClassesAssignedToElement.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("resourceListAnchorsReference='").append(resourceListAnchors.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("impactingProjectsReference='").append(impactingProjects.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("likesReference='").append(likes.toString());
        sb.append("charterReference='").append(charter.toString());
        sb.append("managingProjectReference='").append(managingProject.toString());
        sb.append("relatedToObjectAnnotationsReference='").append(relatedToObjectAnnotations.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         ProjectReferences typedThat = (ProjectReferences) o;
      // compare single cardinality attributes
         if (this.charter != null && !Objects.equals(this.charter,typedThat.charter)) {
                            return false;
                 }
         if (this.managedProject != null && !Objects.equals(this.managedProject,typedThat.managedProject)) {
                            return false;
                 }
         if (this.managingProject != null && !Objects.equals(this.managingProject,typedThat.managingProject)) {
                            return false;
                 }
         if (this.dependsOnProjects != null && !Objects.equals(this.dependsOnProjects,typedThat.dependsOnProjects)) {
                            return false;
                 }
         if (this.dependentProject != null && !Objects.equals(this.dependentProject,typedThat.dependentProject)) {
                            return false;
                 }
         if (this.projectManagers != null && !Objects.equals(this.projectManagers,typedThat.projectManagers)) {
                            return false;
                 }
         if (this.supportingActors != null && !Objects.equals(this.supportingActors,typedThat.supportingActors)) {
                            return false;
                 }
         if (this.projectScope != null && !Objects.equals(this.projectScope,typedThat.projectScope)) {
                            return false;
                 }
         if (this.relatedMedia != null && !Objects.equals(this.relatedMedia,typedThat.relatedMedia)) {
                            return false;
                 }
         if (this.supportingResources != null && !Objects.equals(this.supportingResources,typedThat.supportingResources)) {
                            return false;
                 }
         if (this.certifications != null && !Objects.equals(this.certifications,typedThat.certifications)) {
                            return false;
                 }
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
         if (this.governedByRoles != null && !Objects.equals(this.governedByRoles,typedThat.governedByRoles)) {
                            return false;
                 }
         if (this.comments != null && !Objects.equals(this.comments,typedThat.comments)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.relatedActions != null && !Objects.equals(this.relatedActions,typedThat.relatedActions)) {
                            return false;
                 }
         if (this.starRatings != null && !Objects.equals(this.starRatings,typedThat.starRatings)) {
                            return false;
                 }
         if (this.relatedFromObjectAnnotations != null && !Objects.equals(this.relatedFromObjectAnnotations,typedThat.relatedFromObjectAnnotations)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.dataClassesAssignedToElement != null && !Objects.equals(this.dataClassesAssignedToElement,typedThat.dataClassesAssignedToElement)) {
                            return false;
                 }
         if (this.managedResources != null && !Objects.equals(this.managedResources,typedThat.managedResources)) {
                            return false;
                 }
         if (this.alsoKnownAs != null && !Objects.equals(this.alsoKnownAs,typedThat.alsoKnownAs)) {
                            return false;
                 }
         if (this.resourceListAnchors != null && !Objects.equals(this.resourceListAnchors,typedThat.resourceListAnchors)) {
                            return false;
                 }
         if (this.relatedToObjectAnnotations != null && !Objects.equals(this.relatedToObjectAnnotations,typedThat.relatedToObjectAnnotations)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.impactingProjects != null && !Objects.equals(this.impactingProjects,typedThat.impactingProjects)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.tags != null && !Objects.equals(this.tags,typedThat.tags)) {
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
    ,this.charter
    ,this.managedProject
    ,this.managingProject
    ,this.dependsOnProjects
    ,this.dependentProject
    ,this.projectManagers
    ,this.supportingActors
    ,this.projectScope
    ,this.relatedMedia
    ,this.supportingResources
    ,this.certifications
    ,this.noteLogs
    ,this.governedByRoles
    ,this.comments
    ,this.contributors
    ,this.licenses
    ,this.relatedActions
    ,this.starRatings
    ,this.relatedFromObjectAnnotations
    ,this.actions
    ,this.dataClassesAssignedToElement
    ,this.managedResources
    ,this.alsoKnownAs
    ,this.resourceListAnchors
    ,this.relatedToObjectAnnotations
    ,this.externalReference
    ,this.impactingProjects
    ,this.meaning
    ,this.foundInCollections
    ,this.facets
    ,this.meetings
    ,this.tags
    ,this.likes
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
