/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for relationships.
 */
public interface SubjectAreaRelationshipClients {

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with HasA objects
     */
    SubjectAreaRelationshipClient<HasA> hasA();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with RelatedTerm objects
     */
    SubjectAreaRelationshipClient<RelatedTerm> relatedTerm();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with TermAnchor objects
     */
    SubjectAreaRelationshipClient<TermAnchor> termAnchor();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with ProjectScope objects
     */
    SubjectAreaRelationshipClient<ProjectScope> projectScope();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Synonym objects
     */
    SubjectAreaRelationshipClient<Synonym> synonym();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Antonym objects
     */
    SubjectAreaRelationshipClient<Antonym> antonym();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Translation objects
     */
    SubjectAreaRelationshipClient<Translation> translation();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with UsedInContext objects
     */
    SubjectAreaRelationshipClient<UsedInContext> usedInContext();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with PreferredTerm objects
     */
    SubjectAreaRelationshipClient<PreferredTerm> preferredTerm();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with ValidValue objects
     */
    SubjectAreaRelationshipClient<ValidValue> validValue();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with ReplacementTerm objects
     */
    SubjectAreaRelationshipClient<ReplacementTerm> replacementTerm();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with TypedBy objects
     */
    SubjectAreaRelationshipClient<TypedBy> typedBy();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with IsA objects
     */
    SubjectAreaRelationshipClient<IsA> isA();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with IsaTypeOf objects
     */
    SubjectAreaRelationshipClient<IsATypeOf> isaTypeOf();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Categorization objects
     */
    SubjectAreaRelationshipClient<Categorization> termCategorization();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with SemanticAssignment objects
     */
    SubjectAreaRelationshipClient<SemanticAssignment> semanticAssignment();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with CategoryAnchor objects
     */
    SubjectAreaRelationshipClient<CategoryAnchor> categoryAnchor();

    /**
     * @return {@link SubjectAreaRelationshipClient} for client calls(described in {@link SubjectAreaClient})
     * when working with CategoryHierarchyLink objects
     */
    SubjectAreaClient<CategoryHierarchyLink> categoryHierarchyLink();
}
