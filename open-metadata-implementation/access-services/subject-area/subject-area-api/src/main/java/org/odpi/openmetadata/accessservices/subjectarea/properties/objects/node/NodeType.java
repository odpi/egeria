/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  Different types of nodes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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
     * SubjectArea
     */
    SubjectArea
    ,
    // TODO Consider other types like classification, asset, comment,

    /**
     * Unknown type of node.
     */
    Unknown
}
