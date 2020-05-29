/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

/**
 * Types of response
 */
public enum ResponseCategory {
    Unknown,
    // valid rest
    Void,
    Term,
    Terms,
    Category,
    Categories,
    SubjectAreaDefinition,
    SubjectAreaDefinitions,
    Glossary,
    Glossaries,
    Project,
    Projects,
    Comment,

    // term to term relationship
    TermHASARelationship,
    RelatedTerm,
    SynonymRelationship,
    AntonymRelationship,
    TranslationRelationship,
    TermISARelationship,
    PreferredTermRelationship,
    TermISATYPEOFRelationship,
    TermReplacementRelationship,
    TermTYPEDBYRelationship,
    TermUsedInContextRelationship,
    ValidValueRelationship,
    // term to glossary relationships
    TermAnchorRelationship,
    // category to glossary relationships
    CategoryAnchorRelationship,
    // project scope relationship
    ProjectScopeRelationship,
    // Lines
    Lines,
    // external glossary relationships
    LibraryCategoryReferenceRelationshipRelationship,
    LibraryTermReferenceRelationshipRelationship,
    // term to asset
    SemanticAssignmentRelationship,
    // category to term
    TermCategorizationRelationship,
    // category relationships
    CategoryRelationships,

    // graph
    Graph,

    // errors
    ClassificationException,
    EntityNotDeletedException,
    FunctionNotSupportedException,
    EntityNotPurgedException,
    RelationshipNotDeletedException,
    RelationshipNotPurgedException,
    InvalidParameterException,
    StatusNotSupportedException,
    SubjectAreaRuntimeException,
    UnrecognizedGUIDException,
    UnrecognizedNameException,
    UserNotAuthorizedException,
    PropertyServerException,
    MetadataServerUncontactableException,
    UnexpectedException,
    // used internally
    OmrsEntityDetail,
    OmrsEntityDetails,
    OmrsRelationship,
    OmrsRelationships,
    OmrsInstanceGraph,
    GlossarySummary,
    TermSummary,
    CategorySummary,
    IconSummarySet,

}
