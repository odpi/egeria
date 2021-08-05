/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A type of Category called a Subject Area Definition is one that describes a subject area or a domain.
 * For a category to be in a subject area - it would be within the children category hierarchies under
 * a Subject Area Definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
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
        this.setExtendedProperties(category.getExtendedProperties());
        this.setAdditionalProperties(category.getAdditionalProperties());
    }
}