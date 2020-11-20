/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MediaReference is a relationship between a Referenceable and a RelatedMedia.
 * Link to related media such as images, videos and audio.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaReference extends Line {
    private String description = "Link to related media such as images, videos and audio.";

    /*
     * Set up end 1.
     */
     protected static final String END_1_NODE_TYPE = "Referenceable";
     protected static final String END_1_ATTRIBUTE_NAME = "consumingItem";
     protected static final String END_1_ATTRIBUTE_DESCRIPTION = "Item that is referencing this work.";
     protected static final RelationshipEndCardinality END_1_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     protected static final LineEnd LINE_END_1 = new LineEnd(END_1_NODE_TYPE,
            END_1_ATTRIBUTE_NAME, END_1_ATTRIBUTE_DESCRIPTION, END_1_CARDINALITY);

    /*
     * Set up end 2.
     */
     protected static final String END_2_NODE_TYPE = "RelatedMedia";
     protected static final String END_2_ATTRIBUTE_NAME = "relatedMedia";
     protected static final String END_2_ATTRIBUTE_DESCRIPTION = "Link to external media.";
     protected static final RelationshipEndCardinality END_2_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     protected static final LineEnd LINE_END_2 = new LineEnd(END_2_NODE_TYPE,
            END_2_ATTRIBUTE_NAME, END_2_ATTRIBUTE_DESCRIPTION, END_2_CARDINALITY);

    private String mediaId;

    public MediaReference() {
        super("MediaReference", "1353400f-b0ab-4ab9-ab09-3045dd8a7140", LINE_END_1, LINE_END_2);
    }

    /**
     * {@literal Local identifier for the media. }
     *
     * @return {@code String }
     */
    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * {@literal Description of the relevance of this media to the linked item. }
     *
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * {@literal Set the description of the relationship. }
     *
     * @param description {@code String }
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(" MediaReference=");
        sb.append(super.toString(sb));
        sb.append(" MediaReference Attributes{");
        sb.append("mediaId=").append(this.mediaId).append(",");
        sb.append("description=").append(this.description).append(",");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}