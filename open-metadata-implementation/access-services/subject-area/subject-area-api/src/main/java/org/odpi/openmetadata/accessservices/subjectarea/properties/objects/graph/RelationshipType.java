/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RelationshipType {
    // glossary glossary
    ExternallySourcedGlossary,
    //term term
    UsedInContext,
    Translation,
    PreferredTerm,
    ValidValue,
    ReplacementTerm,
    TypedBy,
    Synonym,
    Antonym,
    IsA,
    IsATypeOf,
    RelatedTerm,
    HasA,
    // category category
    LibraryCategoryReference,
    CategoryHierarchyLink,
    // category term
    TermCategorization,
    // glossary category
    CategoryAnchor,
    //glossary term
    TermAnchor,
    // TODO add support for the following
//    AttachedComment,
//    License,
//    ToDoSource,
//    ExternalIdLink,
//    CollectionMember,
//    ExternalReferenceLink,
//    MediaReference,
//    ExternalIdScope,
//    StaffAssignment,
//    Certification,
//    ToDoOnReferenceable,
//    Contributor,
//    AttachedRating,
//    AttachedNoteLog,
//    MeetingOnReferenceable,
//    AttachedTag,
//    ReferenceableFacet,
//    AttachedLike,
    SemanticAssignment,
    // project scope
    ProjectScope,
    Unknown;
}


