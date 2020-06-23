/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

public interface SubjectAreaRelationship {

   SubjectAreaRelationshipClient<Hasa> hasa();

   SubjectAreaRelationshipClient<RelatedTerm> relatedTerm();

   SubjectAreaRelationshipClient<TermAnchor> termAnchor();

   SubjectAreaRelationshipClient<ProjectScope> projectScope();

   SubjectAreaRelationshipClient<Synonym> synonym();

   SubjectAreaRelationshipClient<Antonym> antonym();

   SubjectAreaRelationshipClient<Translation> translation();

   SubjectAreaRelationshipClient<UsedInContext> usedInContext();

   SubjectAreaRelationshipClient<PreferredTerm> preferredTerm();

   SubjectAreaRelationshipClient<ValidValue> validValue();

   SubjectAreaRelationshipClient<ReplacementTerm> replacementTerm();

   SubjectAreaRelationshipClient<TypedBy> typedBy();

   SubjectAreaRelationshipClient<Isa> isa();

   SubjectAreaRelationshipClient<IsaTypeOf> isaTypeOf();

   SubjectAreaRelationshipClient<Categorization> termCategorization();

   SubjectAreaRelationshipClient<SemanticAssignment> semanticAssignment();

   SubjectAreaRelationshipClient<CategoryAnchor> categoryAnchor();
}
