/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line;


public enum LineType {
    // glossary glossary
    ExternallySourcedGlossary,
    //term term
    UsedInContext,
    Translation,
    PreferredTerm,
    ValidValue,
    ReplacementTerm,
    TermTYPEDBYRelationship,
    Synonym,
    Antonym,
    ISARelationship,
    TermISATypeOFRelationship,
    RelatedTerm,
    TermHASARelationship,
    // category category
    LibraryCategoryReference,
    CategoryHierarchyLink,
    // category term
    TermCategorization,
    // glossary category
    CategoryAnchor,
    //glossary term
    TermAnchor,
     // common
    AttachedComment,
    License,
    ToDoSource,
    ExternalIdLink,
    CollectionMember,
    ExternalReferenceLink,
    MediaReference,
    ExternalIdScope,
    StaffAssignment,
    Certification,
    ToDoOnReferenceable,
    Contributor,
    AttachedRating,
    AttachedNoteLog,
    SemanticAssignment,
    MeetingOnReferenceable,
    AttachedTag,
    ReferenceableFacet,
    AttachedLike,

    /**
     * Other type of line.
     */
    Other
}


