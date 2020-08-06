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
 * CategoryHierarchyLink is a relationship between an entity of type GlossaryCategory and an entity of type GlossaryCategory.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has superCategory as the proxy name for entity type GlossaryCategory.
 * The second entity proxy has subcategories as the proxy name for entity type GlossaryCategory.
 * <p>
 * Each entity proxy also stores the entities guid.
 * <p>
 * Relationship between two glossary categories used to create nested categories.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryHierarchyLink extends Line {
    private static final Logger log = LoggerFactory.getLogger(CategoryHierarchyLink.class);
    private static final String className = CategoryHierarchyLink.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{

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
    private String superCategoryGuid;
    private String subCategoryGuid;


    public CategoryHierarchyLink() {
        initialise();
    }

    private void initialise() {
        name = "CategoryHierarchyLink";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        entity1Name = "superCategory";
        entity1Type = "GlossaryCategory";
        entity2Name = "subcategories";
        entity2Type = "GlossaryCategory";
        typeDefGuid = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
    }

    public CategoryHierarchyLink(Line template) {
        super(template);
        initialise();
    }

    public CategoryHierarchyLink(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "CategoryHierarchyLink";
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
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    public String getSuperCategoryGuid() {
        return superCategoryGuid;
    }

    public void setSuperCategoryGuid(String superCategoryGuid) {
        this.superCategoryGuid = superCategoryGuid;
    }

    public String getSubCategoryGuid() {
        return subCategoryGuid;
    }

    public void setSubCategoryGuid(String subCategoryGuid) {
        this.subCategoryGuid = subCategoryGuid;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append(" CategoryHierarchyLink=");
        sb.append(super.toString(sb));
        sb.append(" CategoryHierarchyLink Attributes{");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}