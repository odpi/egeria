/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

/**
 * A type of Category called a Subject Area Definition is one that describes a subject area or a domain.
 * For a category to be in a subject area - it would be within the children category hierarchies under
 * a Subject Area Definition.
 */
public class SubjectAreaDefinition extends Category {
    public SubjectAreaDefinition() {
        nodeType = NodeType.SubjectAreaDefinition;
    }
    public SubjectAreaDefinition(Category category) {
        this.setName(category.getName());
        this.setParentCategory(category.getParentCategory());
        this.setGlossary(category.getGlossary());
        this.setClassifications(category.getClassifications());
        this.setAdditionalProperties(category.getAdditionalProperties());
        this.setIcons(category.getIcons());
        this.setDescription(category.getDescription());
        this.setQualifiedName(category.getQualifiedName());
        this.setSystemAttributes(category.getSystemAttributes());
    }
}