/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;

/**
 * Mapping methods to map between the categoryHierarchyLink and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class CategoryHierarchyLinkMapper extends RelationshipMapper<CategoryHierarchyLink> {
    public static final String CATEGORY_HIERARCHY_LINK = "CategoryHierarchyLink";

    public CategoryHierarchyLinkMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get the TypeDefName associated with this Relationship
     *
     * @return name of the type def
     */
    @Override
    public String getTypeName() {
        return CATEGORY_HIERARCHY_LINK;
    }

    @Override
    protected CategoryHierarchyLink getRelationshipInstance() {
        return new CategoryHierarchyLink();
    }
}
