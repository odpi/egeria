/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Taxonomy.class,
        visible = true
)
@JsonSubTypes.Type(value = CanonicalTaxonomy.class, name = "CanonicalTaxonomy")
public class Taxonomy extends Glossary {
    private String organizingPrinciple = null;

    public Taxonomy() {
        nodeType = NodeType.Taxonomy;
    }

    public String getOrganizingPrinciple() {
        return organizingPrinciple;
    }

    public void setOrganizingPrinciple(String organizingPrinciple) {
        this.organizingPrinciple = organizingPrinciple;
    }
}
