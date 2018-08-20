/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

/**
 * Types of responses
 */
public enum ResponseCategory {
    Unknown,
    // valid responses
    Void,
    PossibleClassifications,
    PossibleRelationships,
    Term,
    Category,
    Glossary,
    Project,
    Comment,

    // errors
    ClassificationException,
    EntityNotDeletedException,
    FunctionNotSupportedException,
    GUIDNotPurgedException,
    InvalidParameterException,
    RelationshipNotDeletedException,
    StatusNotSupportedException,
    SubjectAreaRuntimeException,
    UnrecognizedGUIDException,
    UnrecognizedNameException,
    UserNotAuthorizedException,
    MetadataServerUncontactableException,
}
