/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;

/**
 * MediaReference is a relationship between an entity of type Referenceable and an entity of type RelatedMedia.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has consumingItem as the proxy name for entity type Referenceable.
 * The second entity proxy has relatedMedia as the proxy name for entity type RelatedMedia.
 * <p>
 * Each entity proxy also stores the entities guid.
 * <p>
 * Link to related media such as images, videos and audio.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaReference extends Line {
    private static final Logger log = LoggerFactory.getLogger(MediaReference.class);
    private static final String className = MediaReference.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "mediaId",
            "description",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "mediaId",
            "description",

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));
    private String entity1Guid;
    private String entity2Guid;


    public MediaReference() {
        initialise();
    }

    private void initialise() {
        name = "MediaReference";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        entity1Name = "consumingItem";
        entity1Type = "Referenceable";
        entity2Name = "relatedMedia";
        entity2Type = "RelatedMedia";
        typeDefGuid = "1353400f-b0ab-4ab9-ab09-3045dd8a7140";
    }

    public MediaReference(Line template) {
        super(template);
        initialise();
    }

    public MediaReference(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "MediaReference";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
    }

    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue = null;
        MapPropertyValue mapPropertyValue = null;
        PrimitivePropertyValue primitivePropertyValue = null;
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("mediaId", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("description", primitivePropertyValue);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    private String mediaId;

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

    private String description;

    /**
     * {@literal Description of the relevance of this media to the linked item. }
     *
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }

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