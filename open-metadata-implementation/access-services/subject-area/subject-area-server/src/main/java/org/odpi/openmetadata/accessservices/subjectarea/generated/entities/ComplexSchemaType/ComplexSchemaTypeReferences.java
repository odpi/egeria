/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.ComplexSchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ComplexSchemaTypeToSchemaAttribute.AttributesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttributeForSchema.AttributeForSchema;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttributeForSchema.AttributeForSchemaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToMapSchemaType.ParentMapToReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MapToElementType.MapToElementType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MapToElementType.MapToElementTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.License;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.LicenseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToAPIOperation.UsedAsAPIHeaderReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIHeader.APIHeader;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIHeader.APIHeaderMapper;
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
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToAPIOperation.UsedAsAPIResponseReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIResponse.APIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIResponse.APIResponseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignmentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToAsset.DescribesAssetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMember;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMemberMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToSchemaAttribute.UsedInSchemasReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SchemaAttributeType.SchemaAttributeType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SchemaAttributeType.SchemaAttributeTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSourceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToActorProfile.ContributorsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.Contributor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.ContributorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToSchemaLinkElement.LinkedByReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LinkedType.LinkedType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LinkedType.LinkedTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToImplementationSnippet.ImplementationSnippetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SchemaTypeImplementation.SchemaTypeImplementation;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SchemaTypeImplementation.SchemaTypeImplementationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToAPIOperation.UsedAsAPIRequestReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIRequest.APIRequest;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.APIRequest.APIRequestMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPerson.StaffReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignmentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.TodosReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedCommentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SchemaTypeToMapSchemaType.ParentMapFromReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MapFromElementType.MapFromElementType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MapFromElementType.MapFromElementTypeMapper;
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
public class ComplexSchemaTypeReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ComplexSchemaTypeReferences.class);
    private static final String className = ComplexSchemaTypeReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "attributes",
             "meetings",
             "parentMapTo",
             "licenses",
             "usedAsAPIHeader",
             "starRatings",
             "tags",
             "relatedMedia",
             "certifications",
             "managedResources",
             "alsoKnownAs",
             "usedAsAPIResponse",
             "meaning",
             "describesAssets",
             "externalReference",
             "foundInCollections",
             "usedInSchemas",
             "facets",
             "actions",
             "contributors",
             "linkedBy",
             "implementationSnippets",
             "usedAsAPIRequest",
             "staff",
             "todos",
             "comments",
             "noteLogs",
             "parentMapFrom",
             "likes",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "AttributeForSchema",
             "MeetingOnReferenceable",
             "MapToElementType",
             "License",
             "APIHeader",
             "AttachedRating",
             "AttachedTag",
             "MediaReference",
             "Certification",
             "ExternalIdScope",
             "ExternalIdLink",
             "APIResponse",
             "SemanticAssignment",
             "AssetSchemaType",
             "ExternalReferenceLink",
             "CollectionMember",
             "SchemaAttributeType",
             "ReferenceableFacet",
             "ToDoSource",
             "Contributor",
             "LinkedType",
             "SchemaTypeImplementation",
             "APIRequest",
             "StaffAssignment",
             "ToDoOnReferenceable",
             "AttachedComment",
             "AttachedNoteLog",
             "MapFromElementType",
             "AttachedLike",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity ComplexSchemaType
       * Each of these relationships should map to a reference (a uniquely named attribute in ComplexSchemaType).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationship can be related to one or many
       * entities.
       *
       * @param lines
       * @param entityGuid
       * @throws InvalidParameterException
       */
     public ComplexSchemaTypeReferences(String entityGuid, List<Line> lines) throws InvalidParameterException {
         for (Line relationship: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationship.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("attributes".equals(referenceName)) {
                         AttributeForSchema attributeForSchema_relationship = (AttributeForSchema)relationship;
                         AttributesReference attributesReference = new AttributesReference(entityGuid,attributeForSchema_relationship);
                         if ( attributes== null ) {
                              attributes = new HashSet();
                         }
                          attributes.add(attributesReference);
                    }
                    if ("meetings".equals(referenceName)) {
                         MeetingOnReferenceable meetingOnReferenceable_relationship = (MeetingOnReferenceable)relationship;
                         MeetingsReference meetingsReference = new MeetingsReference(entityGuid,meetingOnReferenceable_relationship);
                         if ( meetings== null ) {
                              meetings = new HashSet();
                         }
                          meetings.add(meetingsReference);
                    }
                    if ("parentMapTo".equals(referenceName)) {
                         MapToElementType mapToElementType_relationship = (MapToElementType)relationship;
                         ParentMapToReference parentMapToReference = new ParentMapToReference(entityGuid,mapToElementType_relationship);
                         if ( parentMapTo== null ) {
                              parentMapTo = new HashSet();
                         }
                          parentMapTo.add(parentMapToReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationship;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("usedAsAPIHeader".equals(referenceName)) {
                         APIHeader aPIHeader_relationship = (APIHeader)relationship;
                         UsedAsAPIHeaderReference usedAsAPIHeaderReference = new UsedAsAPIHeaderReference(entityGuid,aPIHeader_relationship);
                         if ( usedAsAPIHeader== null ) {
                              usedAsAPIHeader = new HashSet();
                         }
                          usedAsAPIHeader.add(usedAsAPIHeaderReference);
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
                    if ("usedAsAPIResponse".equals(referenceName)) {
                         APIResponse aPIResponse_relationship = (APIResponse)relationship;
                         UsedAsAPIResponseReference usedAsAPIResponseReference = new UsedAsAPIResponseReference(entityGuid,aPIResponse_relationship);
                         if ( usedAsAPIResponse== null ) {
                              usedAsAPIResponse = new HashSet();
                         }
                          usedAsAPIResponse.add(usedAsAPIResponseReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationship;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("describesAssets".equals(referenceName)) {
                         AssetSchemaType assetSchemaType_relationship = (AssetSchemaType)relationship;
                         DescribesAssetsReference describesAssetsReference = new DescribesAssetsReference(entityGuid,assetSchemaType_relationship);
                         if ( describesAssets== null ) {
                              describesAssets = new HashSet();
                         }
                          describesAssets.add(describesAssetsReference);
                    }
                    if ("externalReference".equals(referenceName)) {
                         ExternalReferenceLink externalReferenceLink_relationship = (ExternalReferenceLink)relationship;
                         ExternalReferenceReference externalReferenceReference = new ExternalReferenceReference(entityGuid,externalReferenceLink_relationship);
                         if ( externalReference== null ) {
                              externalReference = new HashSet();
                         }
                          externalReference.add(externalReferenceReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMember collectionMember_relationship = (CollectionMember)relationship;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMember_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("usedInSchemas".equals(referenceName)) {
                         SchemaAttributeType schemaAttributeType_relationship = (SchemaAttributeType)relationship;
                         UsedInSchemasReference usedInSchemasReference = new UsedInSchemasReference(entityGuid,schemaAttributeType_relationship);
                         if ( usedInSchemas== null ) {
                              usedInSchemas = new HashSet();
                         }
                          usedInSchemas.add(usedInSchemasReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationship;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
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
                    if ("linkedBy".equals(referenceName)) {
                         LinkedType linkedType_relationship = (LinkedType)relationship;
                         LinkedByReference linkedByReference = new LinkedByReference(entityGuid,linkedType_relationship);
                         if ( linkedBy== null ) {
                              linkedBy = new HashSet();
                         }
                          linkedBy.add(linkedByReference);
                    }
                    if ("implementationSnippets".equals(referenceName)) {
                         SchemaTypeImplementation schemaTypeImplementation_relationship = (SchemaTypeImplementation)relationship;
                         ImplementationSnippetsReference implementationSnippetsReference = new ImplementationSnippetsReference(entityGuid,schemaTypeImplementation_relationship);
                         if ( implementationSnippets== null ) {
                              implementationSnippets = new HashSet();
                         }
                          implementationSnippets.add(implementationSnippetsReference);
                    }
                    if ("usedAsAPIRequest".equals(referenceName)) {
                         APIRequest aPIRequest_relationship = (APIRequest)relationship;
                         UsedAsAPIRequestReference usedAsAPIRequestReference = new UsedAsAPIRequestReference(entityGuid,aPIRequest_relationship);
                         if ( usedAsAPIRequest== null ) {
                              usedAsAPIRequest = new HashSet();
                         }
                          usedAsAPIRequest.add(usedAsAPIRequestReference);
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
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationship;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }
                    if ("parentMapFrom".equals(referenceName)) {
                         MapFromElementType mapFromElementType_relationship = (MapFromElementType)relationship;
                         ParentMapFromReference parentMapFromReference = new ParentMapFromReference(entityGuid,mapFromElementType_relationship);
                         if ( parentMapFrom== null ) {
                              parentMapFrom = new HashSet();
                         }
                          parentMapFrom.add(parentMapFromReference);
                    }
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationship;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }

                 }
             }
         }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
// Set properties

    private Set<AttributesReference> attributes;
    private Set<MeetingsReference> meetings;
    private Set<ParentMapToReference> parentMapTo;
    private Set<LicensesReference> licenses;
    private Set<UsedAsAPIHeaderReference> usedAsAPIHeader;
    private Set<StarRatingsReference> starRatings;
    private Set<TagsReference> tags;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<CertificationsReference> certifications;
    private Set<ManagedResourcesReference> managedResources;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<UsedAsAPIResponseReference> usedAsAPIResponse;
    private Set<MeaningReference> meaning;
    private Set<DescribesAssetsReference> describesAssets;
    private Set<ExternalReferenceReference> externalReference;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<UsedInSchemasReference> usedInSchemas;
    private Set<FacetsReference> facets;
    private Set<ActionsReference> actions;
    private Set<ContributorsReference> contributors;
    private Set<LinkedByReference> linkedBy;
    private Set<ImplementationSnippetsReference> implementationSnippets;
    private Set<UsedAsAPIRequestReference> usedAsAPIRequest;
    private Set<StaffReference> staff;
    private Set<TodosReference> todos;
    private Set<CommentsReference> comments;
    private Set<NoteLogsReference> noteLogs;
    private Set<ParentMapFromReference> parentMapFrom;
    private Set<LikesReference> likes;

// List properties

    // setters and setters

// Sets
    public Set<AttributesReference> getAttributesReferences() {
        return attributes;
    }

    public void setAttributesReferences(Set<AttributesReference> attributes) {
        this.attributes =attributes;
    }
    public Set<MeetingsReference> getMeetingsReferences() {
        return meetings;
    }

    public void setMeetingsReferences(Set<MeetingsReference> meetings) {
        this.meetings =meetings;
    }
    public Set<ParentMapToReference> getParentMapToReferences() {
        return parentMapTo;
    }

    public void setParentMapToReferences(Set<ParentMapToReference> parentMapTo) {
        this.parentMapTo =parentMapTo;
    }
    public Set<LicensesReference> getLicensesReferences() {
        return licenses;
    }

    public void setLicensesReferences(Set<LicensesReference> licenses) {
        this.licenses =licenses;
    }
    public Set<UsedAsAPIHeaderReference> getUsedAsAPIHeaderReferences() {
        return usedAsAPIHeader;
    }

    public void setUsedAsAPIHeaderReferences(Set<UsedAsAPIHeaderReference> usedAsAPIHeader) {
        this.usedAsAPIHeader =usedAsAPIHeader;
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
    public Set<UsedAsAPIResponseReference> getUsedAsAPIResponseReferences() {
        return usedAsAPIResponse;
    }

    public void setUsedAsAPIResponseReferences(Set<UsedAsAPIResponseReference> usedAsAPIResponse) {
        this.usedAsAPIResponse =usedAsAPIResponse;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<DescribesAssetsReference> getDescribesAssetsReferences() {
        return describesAssets;
    }

    public void setDescribesAssetsReferences(Set<DescribesAssetsReference> describesAssets) {
        this.describesAssets =describesAssets;
    }
    public Set<ExternalReferenceReference> getExternalReferenceReferences() {
        return externalReference;
    }

    public void setExternalReferenceReferences(Set<ExternalReferenceReference> externalReference) {
        this.externalReference =externalReference;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
    }
    public Set<UsedInSchemasReference> getUsedInSchemasReferences() {
        return usedInSchemas;
    }

    public void setUsedInSchemasReferences(Set<UsedInSchemasReference> usedInSchemas) {
        this.usedInSchemas =usedInSchemas;
    }
    public Set<FacetsReference> getFacetsReferences() {
        return facets;
    }

    public void setFacetsReferences(Set<FacetsReference> facets) {
        this.facets =facets;
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
    public Set<LinkedByReference> getLinkedByReferences() {
        return linkedBy;
    }

    public void setLinkedByReferences(Set<LinkedByReference> linkedBy) {
        this.linkedBy =linkedBy;
    }
    public Set<ImplementationSnippetsReference> getImplementationSnippetsReferences() {
        return implementationSnippets;
    }

    public void setImplementationSnippetsReferences(Set<ImplementationSnippetsReference> implementationSnippets) {
        this.implementationSnippets =implementationSnippets;
    }
    public Set<UsedAsAPIRequestReference> getUsedAsAPIRequestReferences() {
        return usedAsAPIRequest;
    }

    public void setUsedAsAPIRequestReferences(Set<UsedAsAPIRequestReference> usedAsAPIRequest) {
        this.usedAsAPIRequest =usedAsAPIRequest;
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
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }
    public Set<ParentMapFromReference> getParentMapFromReferences() {
        return parentMapFrom;
    }

    public void setParentMapFromReferences(Set<ParentMapFromReference> parentMapFrom) {
        this.parentMapFrom =parentMapFrom;
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

        sb.append("ComplexSchemaTypeReferences{");
        sb.append("attributesReference='").append(attributes.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("parentMapToReference='").append(parentMapTo.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("usedAsAPIHeaderReference='").append(usedAsAPIHeader.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("usedAsAPIResponseReference='").append(usedAsAPIResponse.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("describesAssetsReference='").append(describesAssets.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("usedInSchemasReference='").append(usedInSchemas.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("linkedByReference='").append(linkedBy.toString());
        sb.append("implementationSnippetsReference='").append(implementationSnippets.toString());
        sb.append("usedAsAPIRequestReference='").append(usedAsAPIRequest.toString());
        sb.append("staffReference='").append(staff.toString());
        sb.append("todosReference='").append(todos.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("parentMapFromReference='").append(parentMapFrom.toString());
        sb.append("likesReference='").append(likes.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         ComplexSchemaTypeReferences typedThat = (ComplexSchemaTypeReferences) o;
      // compare single cardinality attributes
         if (this.attributes != null && !Objects.equals(this.attributes,typedThat.attributes)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.parentMapTo != null && !Objects.equals(this.parentMapTo,typedThat.parentMapTo)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.usedAsAPIHeader != null && !Objects.equals(this.usedAsAPIHeader,typedThat.usedAsAPIHeader)) {
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
         if (this.usedAsAPIResponse != null && !Objects.equals(this.usedAsAPIResponse,typedThat.usedAsAPIResponse)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.describesAssets != null && !Objects.equals(this.describesAssets,typedThat.describesAssets)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.usedInSchemas != null && !Objects.equals(this.usedInSchemas,typedThat.usedInSchemas)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.linkedBy != null && !Objects.equals(this.linkedBy,typedThat.linkedBy)) {
                            return false;
                 }
         if (this.implementationSnippets != null && !Objects.equals(this.implementationSnippets,typedThat.implementationSnippets)) {
                            return false;
                 }
         if (this.usedAsAPIRequest != null && !Objects.equals(this.usedAsAPIRequest,typedThat.usedAsAPIRequest)) {
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
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
         if (this.parentMapFrom != null && !Objects.equals(this.parentMapFrom,typedThat.parentMapFrom)) {
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
    ,this.attributes
    ,this.meetings
    ,this.parentMapTo
    ,this.licenses
    ,this.usedAsAPIHeader
    ,this.starRatings
    ,this.tags
    ,this.relatedMedia
    ,this.certifications
    ,this.managedResources
    ,this.alsoKnownAs
    ,this.usedAsAPIResponse
    ,this.meaning
    ,this.describesAssets
    ,this.externalReference
    ,this.foundInCollections
    ,this.usedInSchemas
    ,this.facets
    ,this.actions
    ,this.contributors
    ,this.linkedBy
    ,this.implementationSnippets
    ,this.usedAsAPIRequest
    ,this.staff
    ,this.todos
    ,this.comments
    ,this.noteLogs
    ,this.parentMapFrom
    ,this.likes
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
