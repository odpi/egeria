/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  Different types of nodes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum NodeType implements Serializable {
    /**
     * A Glossary that is not acting as a taxonomy or a Canonical Glossary
     */
    Glossary
    ,
    /**
     * This Glossary is a taxonomy. A Taxonomy is a glossary that has a formal structure. Typically the terms have been organized into a category hierarchy that reflects their
     * meaning or use. There may also be term relationships that also form part of the hierarchy. Taxonomies are often used to organize documents and other media in content repositories.
     */
    Taxonomy
    ,
    /**
     * /**
     * This Glossary is a taxonomy. A Taxonomy is a glossary that has a formal structure. Typically the terms have been organized into a category hierarchy that reflects their
     * meaning or use. There may also be term relationships that also form part of the hierarchy. Taxonomies are often used to organize documents and other media in content repositories.
     *
     * This Glossary is also acting as a canonical glossary - it has a Canonical Vocabulary providing the standard vocabulary definitions for an organization. Typically terms from other
     * glossaries are linked to terms from the canonical glossary.
     */
    TaxonomyAndCanonicalGlossary
    ,
    /**
     * This Glossary is a canonical glossary - it has a Canonical Vocabulary providing the standard vocabulary definitions for an organization. Typically terms from other glossaries are linked to terms
     * from the canonical glossary.
     */
    CanonicalGlossary
    ,
    /**
     * Term
     */
    Term
    ,
    /**
     * Activity
     */
    Activity
    ,
    /**
     * Category
     */
    Category
    ,

    /**
     * Asset
     */
    Asset
    ,
    /**
     * SubjectAreaDefinition
     */
    SubjectAreaDefinition
    ,

    /**
     * Project
     */
    Project,
    /**
     * Glossary Project
     */
    GlossaryProject,

    // TODO Consider other types like classification, comment,
    /**
     * Unknown type of node.
     */
    Unknown
}
